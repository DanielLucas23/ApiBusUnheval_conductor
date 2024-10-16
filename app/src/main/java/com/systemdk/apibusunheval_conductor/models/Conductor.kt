package com.systemdk.apibusunheval_conductor.models

import com.beust.klaxon.*

private val klaxon = Klaxon()

data class Conductor (
    val id: String? = null,
    val names: String? = null,
    val email: String? = null,
    val phone: String? = null,
    var image: String? = null,
    val placa: String? = null,
    val rut: String? = null
) {

    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Conductor>(json)
    }
}