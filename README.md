# BTC FeedHandler

Description:

This component is able to read a data streaming of "bid and asks" or "trade orders" (bid and asks orders matching).

Disclaimer:

- Version: 0.1v alpha (Performance could not be perfect at production environments as you could expect).

Description:

- This component has the ability of handling Bitcoin exchanges feed and serve it through a AMQP based queue system:

Details:

- You can find application configuration file in "conf/app.properties" path. 


AMQP based system used:

- RabbitMQ

Language:

- Java SE.

Communications:

- WebSockets

WebServer for WS communications:

- Glashfish.

Default configuration:

- This software is prepared by default for working with Bitfinex exhange WS API (1, 2 versions)
