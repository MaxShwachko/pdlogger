package com.playdarium.graylogtransferserver.exchange.response

import com.playdarium.byteformatter.ByteWriter
import com.playdarium.graylogtransferserver.exchange.Header
import com.playdarium.graylogtransferserver.exchange.Response
import java.nio.charset.StandardCharsets

class HandshakeResponse private constructor (
    val success: Boolean,
    val sessionToken: String?,
    private val error: String,
    private val errorCode: Byte?) : Response {

    enum class ErrorCode(val value: Byte) {
        WrongVersion(1),
        InvalidSessionToken(2),
        OtherError(3);
    }

    override val header: Byte
        get() = Header.Handshake.value

    override fun compose(writer: ByteWriter) {
        writer.write(success)
        if (success) {
            val tokenBytes = sessionToken!!.toByteArray(StandardCharsets.UTF_8)
            writer.write(tokenBytes)
        } else {
            val errorBytes = error.toByteArray(StandardCharsets.UTF_8)
            writer.write(errorBytes)
            writer.write(errorCode!!)
        }
    }

    companion object {
        fun createSuccess(sessionToken: String?): HandshakeResponse {
            return HandshakeResponse(true, sessionToken, "", null)
        }

        fun createFail(error: String, errorCode: ErrorCode): HandshakeResponse {
            return HandshakeResponse(false, null, error, errorCode.value)
        }
    }
}