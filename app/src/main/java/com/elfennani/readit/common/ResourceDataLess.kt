package com.elfennani.readit.common

sealed class ResourceDataLess(val message: String? = null) {
    data object Success : ResourceDataLess()
    class Error(message: String) : ResourceDataLess(message)
    data object Loading: ResourceDataLess()
}