# Prepare Raspberry Pi

## Install InfluxDB

```bash
curl -sL https://repos.influxdata.com/influxdb.key | sudo apt-key add -
echo "deb https://repos.influxdata.com/debian stretch stable" | sudo tee /etc/apt/sources.list.d/influxdb.list
sudo apt-get install influxdb chronograf [telegraf kapacitor]
```

## Install Telegraf

If you want to use telegraf to collect telemetry of the Pi it might be useful to add the temperature of the CPU and GPU. [Add this to your `/etc/telegraf/telegraf.conf`](https://github.com/TheMickeyMike/raspberrypi-temperature-telegraf/blob/master/README.md):

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

##Python

In case you haven't installed `pip` (Rasbian Lite):

```bash
sudo apt-get install python3-pip
```