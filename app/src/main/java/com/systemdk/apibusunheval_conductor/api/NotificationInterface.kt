package com.systemdk.apibusunheval_conductor.api


import com.systemdk.apibusunheval_conductor.adapters.AccessToken
import com.systemdk.apibusunheval_conductor.models.Notification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {

    @POST("/v1/projects/apibusunheval/messages:send")
    @Headers("Content-Type: application/json",
        "Acept: application/json"
    )

    fun notification(
        @Body message: Notification,
        @Header ("Authorization") accessToken: String = "Bearer ${AccessToken.getAccesToken()}"
    ):Call<Notification>
}