package com.aplikasistunting.model

data class DetectionRequest(
    val nama: String,
    val jenis_kelamin: String,
    val umur_bulan: Int,
    val berat: Double,
    val tinggi: Double,
    val lingkar_lengan: Double,
    val lingkar_kepala: Double,
    val kecamatan: String,
    val jumlah_vit_a: Int,
    val pendidikan_ibu: String,
    val pendidikan_ayah: String,
    val status_gizi: String
)