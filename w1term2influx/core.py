from influxdb import InfluxDBClient
from w1thermsensor import W1ThermSensor
from w1thermsensor.errors import W1ThermSensorError
import logging

# in case this is used as library, prevent logging if the application
# does not define it
logging.getLogger('foo').addHandler(logging.NullHandler())
logger = logging.getLogger(__name__)


class ValueCollector:

    def __init__(self,
                 database: str,
                 user: str,
                 password: str,
                 host: str = 'localhost',
                 port: int = 8086,
                 measurement: str = "w1-values"):
        logger.info("Initializing ValueCollector...")
        self.sensors: list = W1ThermSensor.get_available_sensors()
        logger.info("Found {} sensors: {}".format(len(self.sensors), self.sensors))
        self.client = InfluxDBClient(host, port, user, password, database)
        logger.info("Connect to InfluxDb (host={}, port={}, user={}, password=xxx, db={})"
                    .format(host, port, user, database))
        self.measurement = measurement
        logger.debug("Using measurement '{}' for values".format(measurement))
        logger.info("Initializing ValueCollector SUCCESSFUL!")

    def __enter__(self):
        return self

    def __exit__(self):
        if self.client is not None:
            self.client.close()

    def collect_and_write(self):
        fields: object = {}
        for index, sensor in enumerate(self.sensors):
            sensor_id = sensor.id
            try:
                sensor_temperature = sensor.get_temperature()
                logger.debug("Writing value for sensor {}: {}".format(sensor_id, sensor_temperature))
                fields[sensor_id] = sensor_temperature
            except W1ThermSensorError as e:
                logger.error("Error reading {}: {}".format(sensor_id, e.args))

        json_body = [
            {
                "measurement": self.measurement,
                "fields": fields
            }
        ]
        logger.debug("Influx JSON body: " + str(json_body))
        self.client.write_points(json_body)
