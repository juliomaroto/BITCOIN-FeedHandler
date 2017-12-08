# BTC FeedHandler

Description:

This component is able to read a data streaming of "bid and asks" or "trade orders" (bid and asks orders matching). Default configuration is set to working with websocket API of Bitfinex, but you can feel free to modify it and adapt the software for your personal needs. Forking and star this repo is grateful, if you consider it.

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
