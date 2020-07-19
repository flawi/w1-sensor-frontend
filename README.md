# w1therm2influx

Little script that periodically fetches data from thermal sensors connected to the Raspberry Pie's 1-Wire (w1) bus and 
stores values in [influxdb](https://www.influxdata.com/products/influxdb-overview/). The values are retrieved using 
the [w1thermsensor](https://github.com/timofurrer/w1thermsensor) library.

## Installation

Have a look on [Pi Preparation](./pi-preparation.md) to install InfluxDB and other basic requirements. After that you can
install the script.
 
I haven't published the script to [PyPI](https://pypi.org/) yet. But you can install it as local package and here is how:

```bash
$ git clone https://github.com/rkschamer/w1therm2influx.git
$ cd w1therm2influx
$ pip3 install --user .
```

This installs the script locally and adds a binary to `~/.local/bin/`. If you omit the `--user` parameter you can also 
install the script globally, but then you need root privileges.

## Motivation

I mainly wrote the script to have a lightweight way to persist w1 sensor values in influxdb. I'm not sure if the script
can also be used for other platform than the Raspberry Pi running on Raspbian. 

The script using a fixed schema for the influx measurement and also other parts are not super flexible. It currently
satisfies my needs. If I'm lucky and have some spare time I might make it more flexible in the future.

## License

The code is licensed under the MIT license. See details [here](LICENSE.txt).
