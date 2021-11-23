package com.playdarium.graylogtransferserver.model

enum class ScreenOrientation (val value: Byte) {
    Unknown(0),
    Portrait(1),
    PortraitUpsideDown(2),
    LandscapeLeft(3),
    LandscapeRight(4),
    AutoRotation(5);

    companion object {
        fun parse(value: Byte): ScreenOrientation? {
            for (orientation in ScreenOrientation.values()) {
                if (orientation.value == value) return orientation
            }
            return null
        }
    }
}