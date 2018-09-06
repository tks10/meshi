package com.takashi.meshi.model

data class Meshi (
        val raw_path: String,
        val thumb_url: String,
        val memo: String,
        val category_id: Int,
        val time_stamp: Double
)

data class MeshiContainer(val Items: List<Meshi>)

data class MeshiUploader (
        val id: String,
        val memo: String,
        val image_base64: String
)

data class Id(val id: String)

data class TestContainer(val Items: List<Test>)

data class Test(val id: String, val uid: String, val memo: String)