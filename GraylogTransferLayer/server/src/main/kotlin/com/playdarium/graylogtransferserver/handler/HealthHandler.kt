package com.playdarium.graylogtransferserver.handler

import io.vertx.ext.web.RoutingContext
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class HealthHandler {

    fun check(h: RoutingContext) {
        try {
            h.response().putHeader("content-type", "application/json").end()
        } catch (e: Exception) {
            log.error("handle 400 exception", e)
            h.response().setStatusCode(400).end(e.stackTraceToString())
        }
    }
}