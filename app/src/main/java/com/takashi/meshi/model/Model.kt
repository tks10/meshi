package com.takashi.meshi.model

data class Meshi (
        val image_url: String,
        val thumb_url: String,
        val memo: String,
        val content: String,
        val category_id: String,
        val created_at: String
)
