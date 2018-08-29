# w1-sensor-frontend

A simple web front-end to display 1-Wire (w1) sensor values.

It uses a [Postgres](https://www.postgresql.org/) database to store sensor values, which are collected by Python script. A [Spring Boot](https://spring.io/projects/spring-boot) server is collecting the values and provide them to [Bootstrap](https://getbootstrap.com/)-based front-end.

### License
The code is licensed under the MIT license. See details [here](LICENSE.md). 

### Motivation

I started this small project after my dad wanted to have a way to monitor the w1-sensors attached to his Raspberry Pi. I started to look for existing solutions and found them either too complex, like [openHAB](https://www.openhab.org/), or too simple, like [RRDTool](https://oss.oetiker.ch/rrdtool/).

Well and because I just saw this as perfect opportunity to start a small personal project and look into some technologies I'm interested in.

I share this project here primarily to have it documented (and don't forget everything 2 weeks after my dad is happy). But be my guest if you want to use it as well.