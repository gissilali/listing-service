package com.trivago.utils

fun calculateReputationBadgeColor(reputation: Int): String {
    return when {
        reputation <= 500 -> "red"
        reputation <= 799 -> "yellow"
        else -> "green"
    }
}