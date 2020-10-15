package com.alegator1209.model

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token_type")
    val type: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int
) {
    val tokenString: String
        get() = "$type $accessToken"
}
