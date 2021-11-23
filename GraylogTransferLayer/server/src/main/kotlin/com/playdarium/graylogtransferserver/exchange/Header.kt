package com.playdarium.graylogtransferserver.exchange

enum class Header(val value: Byte) {
    Handshake(1),
    PingPong(2),
    ConstantData(3),
    Log(4);

    companion object {
        operator fun contains(value: Byte): Boolean {
            for (header in values()) {
                if (header.value == value) return true
            }
            return false
        }
    }
}