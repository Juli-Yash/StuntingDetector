package com.aplikasistunting.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.aplikasistunting.R
import com.aplikasistunting.auth.SessionManager
import com.aplikasistunting.data.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Session & DB
        session = SessionManager(requireContext())
        val parentId = session.getParentLoginId()
        var childId = session.getActiveChildId()

        val db = AppDatabase.getDatabase(requireContext())
        val parentDao = db.parentDao()
        val childDao = db.childDao()
        val historyDao = db.historyDao()

        // UI elements
        val headerText = view.findViewById<TextView>(R.id.header_username)
        val profileText = view.findViewById<TextView>(R.id.profile_child_name)
        val riwayatContainer = view.findViewById<LinearLayout>(R.id.riwayat_container)
        val btnDetect = view.findViewById<Button>(R.id.btn_detect)
        val btnHistory = view.findViewById<Button>(R.id.btn_history)
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)

        lifecycleScope.launch {
            var childId = session.getActiveChildId()

            // Ambil nama parent
            val parentName = withContext(Dispatchers.IO) {
                parentDao.getParentById(parentId)?.nama
            }

            // Jika belum ada child aktif, cari dari DB
            if (childId == 0 || childId == -1) {
                val activeChild = withContext(Dispatchers.IO) {
                    childDao.getChildrenByParentIdOnce(parentId).firstOrNull { it.isActive }
                }
                if (activeChild != null) {
                    childId = activeChild.id
                    session.saveActiveChildId(childId)
                }
            }

            // Ambil nama anak aktif
            val childName = withContext(Dispatchers.IO) {
                childDao.getChildById(childId)?.namaAnak
            }

            headerText.text = "Hallo,\n${parentName ?: "Orangtua"}"
            profileText.text = if (childName != null) {
                "Akun anak aktif: $childName"
            } else {
                "Belum ada anak aktif.\nSilakan pilih di menu Profil."
            }

            // Tampilkan riwayat jika ada anak aktif, atau tampilkan pesan kosong
            val riwayatList = withContext(Dispatchers.IO) {
                if (childId != 0 && childId != -1) historyDao.getHistoryByChildId(childId)
                else emptyList()
            }

            // Kontrol tombol
            btnDetect.isEnabled = riwayatList.isNotEmpty()
            btnHistory.isEnabled = riwayatList.isNotEmpty()

            riwayatContainer.removeAllViews()
            if (riwayatList.isNotEmpty()) {
                riwayatList.forEach { riwayat ->
                    val tv = TextView(requireContext()).apply {
                        text = "${riwayat.tanggal} - ${riwayat.statusStunting}"
                        textSize = 14f
                        setPadding(24, 12, 24, 12)
                        setTextColor(
                            if (riwayat.statusStunting.contains("Berisiko", true))
                                resources.getColor(R.color.error)
                            else
                                resources.getColor(R.color.text_secondary)
                        )
                    }
                    riwayatContainer.addView(tv)
                }
            } else {
                val tvKosong = TextView(requireContext()).apply {
                    text = "Belum ada riwayat deteksi."
                    textSize = 14f
                    setPadding(24, 12, 24, 12)
                    setTextColor(resources.getColor(R.color.text_secondary))
                }
                riwayatContainer.addView(tvKosong)
            }
        }

        // Navigasi ke Form Deteksi
        btnDetect.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, FormFragment())
                addToBackStack(null)
            }
            bottomNav.selectedItemId = R.id.nav_form
        }

        // Navigasi ke Riwayat
        btnHistory.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, RiwayatFragment())
                addToBackStack(null)
            }
            bottomNav.selectedItemId = R.id.nav_riwayat
        }

        return view
    }
}