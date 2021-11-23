package com.playdarium.graylogtransferserver.util

data class Version(val major: Int = 0, val minor: Int = 0, val patch: Int = 0) : Comparable<Version> {

    fun isGreaterThan(other: Version): Boolean {
        if (major > other.major) return true
        if (major == other.major && minor > other.minor) return true
        return major == other.major && minor == other.minor && patch > other.patch
    }

    fun isLessThan(other: Version): Boolean {
        if (major < other.major) return true
        if (major == other.major && minor < other.minor) return true
        return major == other.major && minor == other.minor && patch < other.patch
    }

    fun isEqual(other: Version): Boolean {
        return major == other.major && minor == other.minor && patch == other.patch
    }

    override fun compareTo(other: Version): Int {
        if (isEqual(other)) return 0
        return if (isGreaterThan(other)) 1 else -1
    }

    companion object {
        fun fromString(version: String): Version {
            val split = version.split(".").toTypedArray()
            if(split.size != 3)
                throw IllegalArgumentException("version $version has wrong format")

            val major = split[0].toInt()
            val minor = split[1].toInt()
            val patch = split[2].toInt()

            return Version(major, minor, patch)
        }
    }

}