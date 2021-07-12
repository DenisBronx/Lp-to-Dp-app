package com.denisbrandi.testutil

fun <T> stubOrThrow(
    isValidInvocation: Boolean,
    result: T,
    errorMessage: String = "Invalid Mock"
): T {
    return if (isValidInvocation) {
        result
    } else {
        throw IllegalStateException(errorMessage)
    }
}