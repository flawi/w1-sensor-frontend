# w1-sensor-frontend

A collection of scripts and configurations to store 1-Wire (w1) sensor values in [influxdb](https://www.influxdata.com/products/influxdb-overview/) and visualize using [chronograf](https://www.influxdata.com/time-series-platform/chronograf/).

## License

The code is licensed under the MIT license. See details [here](LICENSE.txt).

## Motivation

I started this small project after my dad wanted to have a way to monitor the w1-sensors attached to his Raspberry Pi. I started to look for existing solutions and found them either too complex, like [openHAB](https://www.openhab.org/), or too simple, like [RRDTool](https://oss.oetiker.ch/rrdtool/).

Well and because I just saw this as perfect opportunity to start a small personal project and look into some technologies I'm interested in.

I share this project here primarily to have it documented (and don't forget everything 2 weeks after my dad is happy). But be my guest if you want to use it as well.

## Installation

```bash
curl -sL https://repos.influxdata.com/influxdb.key | sudo apt-key add -
echo "deb https://repos.influxdata.com/debian stretch stable" | sudo tee /etc/apt/sources.list.d/influxdb.list
sudo apt-get install influxdb chronograf [telegraf kapacitor]
```

### telegraf

If you want to use telegraf to collect telemetri of the Pi it might be useful to add the temperature of the CPU and GPU. [Add this to your `/etc/telegraf/telegraf.conf`](https://github.com/TheMickeyMike/raspberrypi-temperature-telegraf/blob/master/README.md):

```bash
[[inputs.file]]
  files = ["/sys/class/thermal/thermal_zone0/temp"]
  name_override = "cpu_temperature"
  data_format = "value"
  data_type = "integer"

[[inputs.exec]]
  commands = [ "/opt/vc/bin/vcgencmd measure_temp" ]
  name_override = "gpu_temperature"
  data_format = "grok"
  grok_patterns = ["%{NUMBER:value:float}"]
```

### Python

In case you haven't installed `pip` (Rasbian Lite):

```bash
sudo apt-get install python3-pip
```

Install used libraries [w1thermsensor](https://github.com/timofurrer/w1thermsensor)and [influxdb-python](https://github.com/influxdata/influxdb-python):

```bash
pip3 install w1thermsensor
pip3 install influxdb
```
