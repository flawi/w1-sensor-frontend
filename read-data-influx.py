#!/usr/bin/python3
import argparse

from w1thermsensor import W1ThermSensor
from influxdb import InfluxDBClient
import logging
from logging.handlers import RotatingFileHandler
from logging import StreamHandler
from time import sleep

# basic configuration
from w1thermsensor.errors import W1ThermSensorError

INTERVAL_IN_SECONDS = 60
formatter = logging.Formatter('%(asctime)s [%(levelname)-5.5s] %(message)s')
logger = logging.getLogger()
logger.setLevel(logging.INFO)
fileHandler = RotatingFileHandler("/tmp/read-data-influx.log", mode="a", maxBytes=5 * 1024 * 1024)
fileHandler.setFormatter(formatter)
logger.addHandler(fileHandler)

consoleHandler = StreamHandler()
consoleHandler.setFormatter(formatter)
logger.addHandler(consoleHandler)


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


def main(args):
    try:
        with ValueCollector(database=args.database,
                            user=args.user,
                            password=args.password,
                            host=args.host,
                            port=args.port,
                            measurement=args.measurement) as collector:
            while True:
                logger.info("Writing sensor values...")
                collector.collect_and_write()
                logger.info("Sleeping for " + str(INTERVAL_IN_SECONDS) + " seconds...")
                sleep(INTERVAL_IN_SECONDS)
    except Exception as e:
        logger.debug(e)
        logger.error("Error during execution ({}). Attempting reconnection in {} seconds."
                     .format(e.args, INTERVAL_IN_SECONDS))
        sleep(INTERVAL_IN_SECONDS)
        main(args)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Collects values from W1 Sensors and writes them to Influx")
    parser.add_argument("--database", type=str, required=True)
    parser.add_argument("--user", type=str, required=True)
    parser.add_argument("--password", type=str, required=True)
    parser.add_argument("--host", type=str, required=False, default="localhost")
    parser.add_argument("--port", type=int, required=False, default=8086)
    parser.add_argument("--measurement", type=str, required=False, default="w1-values")
    main(parser.parse_args())
