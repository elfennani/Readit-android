package com.elfennani.readit.utilities

import kotlin.math.floor

fun formatDifferenceSeconds(timeDifference: Long, doublePrecise: Boolean = false): String {
    val units =
      mapOf(
          "s" to 0.0,
          "m" to 60.0,
          "h" to 3600.0,
          "d" to 86400.0,
          "mo" to 2592000.0,
          "y" to 31536000.0,
      )

    var result = ""
    units.forEach { (type, length) ->
        if(timeDifference>length){
            val value = floor(timeDifference / length).toInt()
            result = "$value$type"

            if(doublePrecise){
                result += " ${formatDifferenceSeconds((timeDifference % length).toLong())}"
            }
        }
    }

    return result
}
