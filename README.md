# BTC FeedHandler

- Disclaimer:

Version: 0.1v alpha (Performance could not be perfect at production environments as you could expect).


- Details:

You can find exchange configuration in "conf/app.properties" path. This component has the ability of handling Bitcoin exchanges feed and serve it through a AMQP based queue system:



AMQP based system used:

- RabbitMQ

Language:

- Java SE.

Communications:

- WebSockets

WebServer for WS communications:

- Glashfish.
