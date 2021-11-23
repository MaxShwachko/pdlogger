package com.playdarium.graylogtransferserver.exchange.event

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.playdarium.graylogtransferserver.codec.ResponseEncoder
import com.playdarium.graylogtransferserver.exchange.Event
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.request.HandshakeRequest
import com.playdarium.graylogtransferserver.exchange.response.HandshakeResponse
import com.playdarium.graylogtransferserver.model.User
import com.playdarium.graylogtransferserver.util.Version
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger { }

class HandshakeEvent(private val serverApiVersion: Version,
                     private val users: ConcurrentHashMap<String, User>) : Event {

    override fun handle(encoder: ResponseEncoder, request: Request) {
        if(request !is HandshakeRequest)
            return

        val user = users[encoder.remoteHost] ?: return

        user.authToken = request.authToken
        try {

            val handshakeResponse = createHandshakeResponse(request)
            if (handshakeResponse.success) {
                user.sessionToken = handshakeResponse.sessionToken
                encoder.write(handshakeResponse)
            } else {
                users.remove(encoder.remoteHost)
                encoder.write(handshakeResponse)
                encoder.close()
            }
        } catch (e: Exception) {
            val handshakeResponse = HandshakeResponse.createFail(e.message!!, HandshakeResponse.ErrorCode.OtherError)
            encoder.write(handshakeResponse)
        }
    }

    private fun createHandshakeResponse(request: HandshakeRequest): HandshakeResponse {
        val majorOrMinorAreNotEqual = (request.apiVersion.major != serverApiVersion.major || request.apiVersion.minor != serverApiVersion.minor)
        if (majorOrMinorAreNotEqual) {
            log.info("Wrong app version. Client version: {}, required version: {}", request.apiVersion, serverApiVersion)
            return HandshakeResponse.createFail(serverApiVersion.toString(), HandshakeResponse.ErrorCode.WrongVersion)
        }
        var sessionToken: String? = request.sessionToken
        return if (sessionToken != null) {
            val verified = verifySessionToken(request.authToken, request.sessionToken)
            if (verified) {
                HandshakeResponse.createSuccess(sessionToken)
            } else {
                HandshakeResponse.createFail("e", HandshakeResponse.ErrorCode.InvalidSessionToken)
            }
        } else {
            sessionToken = createSessionToken(request.authToken)
            HandshakeResponse.createSuccess(sessionToken)
        }
    }

    private fun createSessionToken(authToken: String?): String {
        val algorithm = Algorithm.HMAC256(authToken)
        return JWT.create()
            .withIssuer("auth0")
            .sign(algorithm)
    }

    private fun verifySessionToken(authToken: String?, sessionToken: String?): Boolean {
        return try {
            val algorithm = Algorithm.HMAC256(authToken)
            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build() //Reusable verifier instance
            val jwt: DecodedJWT = verifier.verify(sessionToken)
            log.info("session token IS verified, sessionToken: {}, jwt token: {}", sessionToken, jwt.token)
            true
        } catch (e: JWTVerificationException) {
            log.info("session token IS NOT verified, {}", sessionToken)
            false
        }
    }
}
