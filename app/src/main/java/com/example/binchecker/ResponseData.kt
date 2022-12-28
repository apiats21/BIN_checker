package com.example.binchecker

data class ResponseData(
    val scheme: String,
    val type: String,
    val brand: String,
    val prepaid: Boolean,
    val number: CardNumber,
    val country: Country,
    val bank: Bank
)

data class CardNumber(
    val length: Int,
    val luhn: Boolean
)

data class Country(
    val numeric: Int,
    val alpha2: String,
    val name: String,
    val emoji: String,
    val currency: String,
    val latitude: Int,
    val longitude: Int
)

data class Bank(
    val name: String,
    val url: String,
    val phone: String,
    val city: String
)