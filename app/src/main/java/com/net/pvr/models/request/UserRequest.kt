package com.net.pvr.models.request

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)