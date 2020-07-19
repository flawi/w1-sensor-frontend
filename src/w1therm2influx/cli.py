import argparse
import logging
from time import sleep
from .core import ValueCollector


class CliArguments:
    database: str
    user: str
    password: str
    host: str
    port: int
    measurement: str
    polling_interval: int
    log_level: str


def cli():
    parser = argparse.ArgumentParser(description="Collects values from W1 Sensors and writes them to Influx")
    parser.add_argument("--database", dest="database", type=str, required=True)
    parser.add_argument("--user", dest="user", type=str, required=True)
    parser.add_argument("--password", dest="password", type=str, required=True)
    parser.add_argument("--host", dest="host", type=str, required=False, default="localhost")
    parser.add_argument("--port", dest="port", type=int, required=False, default=8086)
    parser.add_argument("--measurement", dest="measurement", type=str, required=False, default="w1-values")
    parser.add_argument("--polling-interval", dest="polling_interval", type=int, required=False, default=60)
    parser.add_argument("--log-level", dest="log_level", type=str, required=False, default="WARNING")
    args = parser.parse_args(namespace=CliArguments())

    # configuring root logger
    logging.basicConfig(
        format='%(asctime)s [%(levelname)-5.5s] %(message)s',
        level=args.log_level
    )

    run(args)


def run(args: CliArguments):
    logger = logging.getLogger(__name__)

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
                logger.info("Sleeping for " + str(args.polling_interval) + " seconds...")
                sleep(args.polling_interval)
    except Exception as e:
        logger.exception("Error during execution ({}). Attempting reconnection in {} seconds."
                         .format(e.args, args.polling_interval))
        sleep(args.polling_interval)
        run(args)
