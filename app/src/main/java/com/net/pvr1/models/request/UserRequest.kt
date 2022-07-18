package com.net.pvr1.models.request

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)