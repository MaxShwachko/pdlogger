package com.playdarium.graylogtransferserver.exchange

import com.playdarium.graylogtransferserver.codec.ResponseEncoder
import com.playdarium.graylogtransferserver.exchange.event.ConstantDataEvent
import com.playdarium.graylogtransferserver.exchange.event.GelfEvent
import com.playdarium.graylogtransferserver.exchange.event.HandshakeEvent
import com.playdarium.graylogtransferserver.exchange.event.PingPongEvent
import com.playdarium.graylogtransferserver.model.User
import com.playdarium.graylogtransferserver.util.Version
import mu.KotlinLogging
import org.graylog2.gelfclient.transport.GelfTransport
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

class ExchangeManager(
    apiVersion: Version,
    gelfTransport: GelfTransport,
    private val users: ConcurrentHashMap<String, User>) {

    private val events = ConcurrentHashMap<Byte, Event>()

    private fun registerEvent(header: Byte, handler: Event) {
        events[header] = handler
    }

    fun handle(encoder: ResponseEncoder, request: Request) {
        val header = request.header
        if (!events.containsKey(header)) {
            if (header != Header.Handshake.value) {
                log.error("handle -> no event for header $header")
            }
            return
        }

        if(request.header != Header.Handshake.value &&  !users[encoder.remoteHost]!!.isLogged) {
            log.error { "$encoder.remoteHost is not logged, header: ${request.header}" }
            return
        }

        val event = events[header]
        //        log.info("header: {}", header);
        try {
            event?.handle(encoder, request)
        } catch (e: Exception) {
            log.error("Exchange error", e) //ctx.close();
        }
    }

    init {
        registerEvent(Header.Handshake.value, HandshakeEvent(apiVersion, users))
        registerEvent(Header.PingPong.value, PingPongEvent())
        registerEvent(Header.ConstantData.value, ConstantDataEvent(users))
        registerEvent(Header.Log.value, GelfEvent(gelfTransport, users))
    }
}