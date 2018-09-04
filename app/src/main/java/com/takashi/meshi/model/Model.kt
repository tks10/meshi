package com.takashi.meshi.model

data class Meshi (
        val image_url: String,
        val thumb_url: String,
        val memo: String,
        val category_id: Int,
        val created_at: String
)
