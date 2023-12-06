package com.elfennani.readit.utilities

import java.util.Date
import kotlin.math.floor

fun formatEpoch(timestamp: Long, doublePrecise:Boolean = false):String {
    val now = Date().time / 1000
    val elapsedSeconds = (now - timestamp);

    if (elapsedSeconds < 60) {
        return "${elapsedSeconds}s";
    } else if (elapsedSeconds < 3600) {
        val minutes = floor((elapsedSeconds / 60L).toDouble()).toInt();
        val seconds = floor((elapsedSeconds % 60).toDouble()).toInt();
        return "${minutes}min" + (if(doublePrecise)" ${seconds}s" else "");
    } else if (elapsedSeconds < 86400) {
        val hours = floor((elapsedSeconds / 3600).toDouble()).toInt();
        val minutes = floor(((elapsedSeconds % 3600) / 60).toDouble()).toInt();
        return "${hours}h" + (if(doublePrecise) " ${minutes}min" else "");
    } else if (elapsedSeconds < 2592000) {
        val days = floor((elapsedSeconds / 86400).toDouble()).toInt();
        val hours = floor(((elapsedSeconds % 86400) / 3600).toDouble()).toInt();
        return "${days}d" + ( if(doublePrecise) " ${hours}h" else "");
    } else if (elapsedSeconds < 31536000) {
        val months = floor((elapsedSeconds / 2592000).toDouble()).toInt();
        val days = floor(((elapsedSeconds % 2592000) / 86400).toDouble()).toInt();
        return "${months}mo" + ( if(doublePrecise) " ${days}d" else "");
    } else {
        val years = floor((elapsedSeconds / 31536000).toDouble()).toInt();
        val months = floor(((elapsedSeconds % 31536000) / 2592000).toDouble()).toInt();
        return "${years}y" + ( if(doublePrecise) " ${months}mo" else "");
    }
}
