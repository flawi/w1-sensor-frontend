#!/usr/bin/python3

from w1thermsensor import W1ThermSensor
from influxdb import InfluxDBClient
import logging
from logging.handlers import RotatingFileHandler
from logging import StreamHandler
from time import sleep

# basic configuration
INTERVAL_IN_SECONDS = 60
formatter = logging.Formatter('%(asctime)s [%(levelname)-5.5s] %(message)s')
logger = logging.getLogger()
logger.setLevel(logging.INFO)
fileHandler = RotatingFileHandler("/tmp/read-data-influx.log", mode="a", maxBytes=5*1024*1024)
fileHandler.setFormatter(formatter)
logger.addHandler(fileHandler)

consoleHandler = StreamHandler()
consoleHandler.setFormatter(formatter)
logger.addHandler(consoleHandler)

try:
    # establish database connection
    logger.info("Establishing database connection...")
    client = InfluxDBClient('localhost', 8086, "w1-writer", "WrXrEoqsVIvlrNXF9Znz", "w1-sensors")

    # sorting sensors by id to display everytime in same order
    logger.info("Getting available sensors...")
    sensors = W1ThermSensor.get_available_sensors()
    logger.info("Found {} sensors. Beginning to write data...".format(len(sensors)))

    while True:
        try:
            for index, sensor in enumerate(sensors):
                sensor_id = sensor.id
                sensor_temperature = sensor.get_temperature()
                logger.debug("Writing value for sensor {}: {}".format(sensor_id, sensor_temperature))

                json_body = [
                    {
                        "measurement": "w1-temperatures",
                        "tags": {
                            "sensorId": sensor_id,
                        },
                        "fields": {
                            "value": sensor_temperature
                        }
                    }
                ]
                client.write_points(json_body)
            logger.info("Sleeping for " + str(INTERVAL_IN_SECONDS) + " seconds...")
            sleep(INTERVAL_IN_SECONDS)
        except Exception as e:
            logger.error(e)
finally:
    logger.info("Closing database connection...")
    client.close()

