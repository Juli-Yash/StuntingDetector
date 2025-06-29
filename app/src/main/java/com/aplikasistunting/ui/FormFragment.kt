package com.aplikasistunting.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aplikasistunting.auth.SessionManager
import com.aplikasistunting.R
import com.aplikasistunting.data.AppDatabase
import com.aplikasistunting.data.HistoryEntity
import com.aplikasistunting.model.DetectionRequest
import com.aplikasistunting.model.DetectionResponse
import com.aplikasistunting.network.ApiClient
import com.aplikasistunting.repository.HistoryRepository
import com.aplikasistunting.viewmodel.HistoryViewModel
import com.aplikasistunting.viewmodel.HistoryViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FormFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)

        // Inisialisasi ViewModel
        val dao = AppDatabase.getDatabase(requireContext()).historyDao()
        val repository = HistoryRepository(dao)
        val factory = HistoryViewModelFactory(repository)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        // Referensi UI
        val etNama = view.findViewById<EditText>(R.id.etNama)
        val etJenisKelamin = view.findViewById<EditText>(R.id.etJenisKelamin)
        val etUmur = view.findViewById<EditText>(R.id.etUmur)
        val etBerat = view.findViewById<EditText>(R.id.etBerat)
        val etTinggi = view.findViewById<EditText>(R.id.etTinggi)
        val etLila = view.findViewById<EditText>(R.id.etLila)
        val etLingkarKepala = view.findViewById<EditText>(R.id.etLingkarKepala)
        val etKecamatan = view.findViewById<EditText>(R.id.etKecamatan)
        val etJumlahVitA = view.findViewById<EditText>(R.id.etJumlahVitA)
        val etPendidikanIbu = view.findViewById<EditText>(R.id.etPendidikanIbu)
        val etPendidikanAyah = view.findViewById<EditText>(R.id.etPendidikanAyah)
        val etStatusGizi = view.findViewById<EditText>(R.id.etStatusGizi)
        val btnKirim = view.findViewById<Button>(R.id.btnKirim)

        val session = SessionManager(requireContext())
        val childId = session.getActiveChildId()

        btnKirim.setOnClickListener {
            try {
                val request = DetectionRequest(
                    nama = etNama.text.toString(),
                    jenis_kelamin = etJenisKelamin.text.toString(),
                    umur_bulan = etUmur.text.toString().toInt(),
                    berat = etBerat.text.toString().toDouble(),
                    tinggi = etTinggi.text.toString().toDouble(),
                    lingkar_lengan = etLila.text.toString().toDouble(),
                    lingkar_kepala = etLingkarKepala.text.toString().toDouble(),
                    kecamatan = etKecamatan.text.toString(),
                    jumlah_vit_a = etJumlahVitA.text.toString().toInt(),
                    pendidikan_ibu = etPendidikanIbu.text.toString(),
                    pendidikan_ayah = etPendidikanAyah.text.toString(),
                    status_gizi = etStatusGizi.text.toString()
                )

                ApiClient.instance.detectStunting(request)
                    .enqueue(object : Callback<DetectionResponse> {
                        override fun onResponse(
                            call: Call<DetectionResponse>,
                            response: Response<DetectionResponse>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val res = response.body()!!

                                val history = HistoryEntity(
                                    tanggal = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                                    nama = request.nama,
                                    statusStunting = res.status_stunting,
                                    statusUnderweight = res.status_underweight,
                                    statusWasting = res.status_wasting,
                                    childId = childId // BENAR
                                )

                                // Simpan ke database via ViewModel
                                historyViewModel.insert(history)

                                val hasil = """
Stunting: ${res.status_stunting}
Underweight: ${res.status_underweight}
Wasting: ${res.status_wasting}

Saran Gizi:
${res.response}
                                """.trimIndent()

                                AlertDialog.Builder(requireContext())
                                    .setTitle("Hasil Deteksi")
                                    .setMessage(hasil)
                                    .setPositiveButton("OK", null)
                                    .show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal mendapatkan respons: ${response.code()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
                            Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Input tidak valid: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}