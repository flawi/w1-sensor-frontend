#!/usr/bin/python3   

from w1thermsensor import W1ThermSensor
import psycopg2

# establish database connection
try:
    conn=psycopg2.connect("dbname='w1-sensor-frontend-db' user='postgres' password='xxx' host='localhost'")
    cur = conn.cursor()
    # sorting sensors by id to display everytime in same order
    sensors = sorted(W1ThermSensor.get_available_sensors(), key=lambda sensor: sensor.id)
    for index, sensor in enumerate(sensors):
        print("Sensor {} ({}): {}".format(index+1, sensor.id, sensor.get_temperature()))
        cur.execute("""
            INSERT INTO "sensor-values" ("sensor-id", "sensor-value")
            VALUES (%s, %s)
        """, (sensor.id, sensor.get_temperature()))
    conn.commit()

except Exception as e:
    print(e)
finally:
    cur.close()
    conn.close()
