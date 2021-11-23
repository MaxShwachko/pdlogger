package com.playdarium.graylogtransferserver.handler

import com.playdarium.graylogtransferserver.codec.PayloadDecoder
import com.playdarium.graylogtransferserver.codec.RequestDecoder
import com.playdarium.graylogtransferserver.codec.ResponseEncoder
import com.playdarium.graylogtransferserver.exchange.ExchangeManager
import com.playdarium.graylogtransferserver.exchange.RequestReaderManager
import com.playdarium.graylogtransferserver.model.User
import com.playdarium.graylogtransferserver.util.Version
import io.vertx.core.net.NetSocket
import mu.KotlinLogging
import org.graylog2.gelfclient.transport.GelfTransport
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

class ConnectHandler(private val apiVersion: Version, private val gelfTransport: GelfTransport) {
    private val users = ConcurrentHashMap<String, User>()

    fun handle(socket: NetSocket) {
        val remoteHost = socket.remoteAddress().hostAddress()

        log.info { "Handle connection from $remoteHost " }
        val payloadDecoder = PayloadDecoder()
        val responseEncoder = ResponseEncoder(socket)
        val exchangeManager = ExchangeManager(apiVersion, gelfTransport, users)
        val reader = RequestReaderManager()
        val requestDecoder = RequestDecoder(reader)

        users[remoteHost] = User()

        socket.closeHandler {
            log.info { "$remoteHost disconnected" }
            users.remove(remoteHost)
        }
        socket.handler { buffer ->
            log.info { "I received some length: ${buffer.length()}, bytes: ${buffer.bytes.size}, from: $remoteHost" }

            val payload = payloadDecoder.decode(buffer)
            val result = requestDecoder.decode(remoteHost, payload!!)

            if (result.request != null) {
                exchangeManager.handle(responseEncoder, result.request)
            } else {
                log.info { "Cannot handle request, header: ${result.header} " }
            }
        }
    }
}