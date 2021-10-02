package com.example.laundrybill

fun routeBuilder(route: String, itemId: Long): String {
    return (route + "?itemId=" + itemId.toString())
}