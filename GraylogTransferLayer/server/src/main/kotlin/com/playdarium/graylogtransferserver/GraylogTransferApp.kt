package com.playdarium.graylogtransferserver

import com.playdarium.graylogtransferserver.handler.ConnectHandler
import com.playdarium.graylogtransferserver.handler.HealthHandler
import com.playdarium.graylogtransferserver.util.Version
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.net.NetServerOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import mu.KotlinLogging
import org.graylog2.gelfclient.GelfConfiguration
import org.graylog2.gelfclient.GelfTransports
import org.graylog2.gelfclient.transport.GelfTransport
import java.net.InetSocketAddress


private val log = KotlinLogging.logger {}

class GraylogTransferApp(private val port: Int,
                         private val graylogHost: String,
                         private val graylogPort: Int) : AbstractVerticle() {

    override fun start() {
        try {
            val apiVersion = Version.fromString("1.0.0")

            log.info("Application starting...")

            val router = Router.router(vertx)
            router.route().handler(
                CorsHandler.create("*")
                    .allowedMethod(HttpMethod.GET)
                    .allowedHeader("Access-Control-Request-Method").allowedHeader("Access-Control-Allow-Credentials")
                    .allowedHeader("Access-Control-Allow-Headers").allowedHeader("Authorization")
                    .allowedHeader("Access-Control-Allow-Origin").allowedHeader("Access-Control-Allow-Headers")
                    .allowedHeader("Content-Type")
            )

            router.route().handler(BodyHandler.create())
            val healthHandler = HealthHandler()
            router.get("/api/v1/health/").handler(healthHandler::check)

            val options = NetServerOptions()
            options.sendBufferSize = 32768
            options.receiveBufferSize = 32768

            val server = vertx.createNetServer(options)
            val gelfTransport = createGelfTransport(graylogHost, graylogPort)
//            val builder = GelfMessageBuilder("Hello, graylog!", "example.com")
//                .level(GelfMessageLevel.INFO)
//                .additionalField("_foo", "bar")
//            val message = builder.message("This is message #" + 10)
//                .additionalField("_count", 10)
//                .build();
//            gelfTransport.send(message)

            val connectHandler = ConnectHandler(apiVersion, gelfTransport)

            server.connectHandler(connectHandler::handle)
            server.listen(port)
                .onSuccess { log.info("Application started, port $port") }
                .onFailure { log.info("Application FAILED, port $port") }
        } catch (e: Exception) {
            log.info { e }
        }
    }

    private fun createGelfTransport(host: String, port: Int): GelfTransport {
        val config = GelfConfiguration(InetSocketAddress(host, port))
            .transport(GelfTransports.TCP)
            .queueSize(512)
            .connectTimeout(5000)
            .reconnectDelay(1000)
            .tcpNoDelay(true)
            .sendBufferSize(32768)

        return GelfTransports.create(config)
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val vertx = Vertx.vertx()
            val port = args[0].toInt()
            val graylogHost = args[1]
            val graylogPort = args[2].toInt()

            vertx.deployVerticle(GraylogTransferApp(port, graylogHost, graylogPort))
        }
    }
}