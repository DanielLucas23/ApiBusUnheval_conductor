package com.systemdk.apibusunheval_conductor.models

data class NotificationData(
    val topic: String? = null,
    val data: HashMap<String,String>
)
