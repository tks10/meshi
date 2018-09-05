package com.takashi.meshi.model

data class Meshi (
        val image_url: String,
        val thumb_url: String,
        val memo: String,
        val category_id: Int,
        val created_at: Long
)

data class Id(val id: Int)

data class TestContainer(val Items: List<Test>)

data class Test(val id: String, val uid: String, val memo: String)