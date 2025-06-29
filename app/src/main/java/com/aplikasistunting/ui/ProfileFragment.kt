package com.aplikasistunting.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aplikasistunting.R
import com.aplikasistunting.auth.SessionManager
import com.aplikasistunting.data.AppDatabase
import com.aplikasistunting.data.ChildEntity
import com.aplikasistunting.repository.ChildRepository
import com.aplikasistunting.viewmodel.ChildViewModel
import com.aplikasistunting.viewmodel.ChildViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ChildViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val layoutContainer = view.findViewById<LinearLayout>(R.id.layoutChildList)

        val session = SessionManager(requireContext())
        val parentId = session.getParentLoginId()

        val db = AppDatabase.getDatabase(requireContext())
        val childDao = db.childDao()
        val parentDao = db.parentDao()

        val repository = ChildRepository(childDao)
        val factory = ChildViewModelFactory(repository, parentId)
        viewModel = ViewModelProvider(this, factory)[ChildViewModel::class.java]

        lifecycleScope.launch {
            val parent = parentDao.getParentById(parentId)
            parent?.let {
                val card = CardView(requireContext()).apply {
                    radius = dpToPx(12f)
                    setContentPadding(32, 32, 32, 32)
                    cardElevation = dpToPx(4f)
                    setCardBackgroundColor(Color.parseColor("#E3F2FD"))
                    layoutParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = dpToPx(16f).toInt()
                    }
                }

                val containerLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                }

                val parentText = TextView(requireContext()).apply {
                    text = "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67 Orang Tua:\nNama: ${it.nama}\nEmail: ${it.email}\nID: ${it.id}"
                    setTextColor(Color.BLACK)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    setPadding(0, 0, 0, 24)
                }

                val logoutBtn = createStyledButton("Logout", R.drawable.btn_red).apply {
                    setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Konfirmasi Logout")
                            .setMessage("Apakah Anda yakin ingin logout?")
                            .setPositiveButton("Ya") { _, _ ->
                                session.clearSession()
                                requireActivity().finish()
                                startActivity(Intent(requireContext(), AuthActivity::class.java))
                            }
                            .setNegativeButton("Batal", null)
                            .show()
                    }
                }

                containerLayout.addView(parentText)
                containerLayout.addView(logoutBtn)
                card.addView(containerLayout)
                layoutContainer.addView(card)
            }
        }

        val btnTambahAnak = createStyledButton("Tambah Anak", R.drawable.btn_primary).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(16f).toInt()
            }
            setOnClickListener {
                showTambahAnakDialog(parentId)
            }
        }
        layoutContainer.addView(btnTambahAnak)

        viewModel.allChildren.observe(viewLifecycleOwner) { children ->
            if (layoutContainer.childCount > 2) {
                layoutContainer.removeViews(2, layoutContainer.childCount - 2)
            }

            children.forEach { child ->
                val card = CardView(requireContext()).apply {
                    radius = dpToPx(12f)
                    setContentPadding(32, 32, 32, 32)
                    cardElevation = dpToPx(4f)
                    setCardBackgroundColor(Color.parseColor("#F1F1F1"))
                    layoutParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = dpToPx(16f).toInt()
                    }
                }

                val verticalLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                }

                val textInfo = TextView(requireContext()).apply {
                    text = "Nama Anak: ${child.namaAnak}\nUmur: ${child.umur} tahun\nJenis Kelamin: ${child.jenisKelamin}"
                    setTextColor(Color.BLACK)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                }

                val statusText = TextView(requireContext()).apply {
                    text = if (child.isActive) "Sedang digunakan" else "Tidak aktif"
                    setTextColor(if (child.isActive) Color.GREEN else Color.GRAY)
                    setPadding(0, dpToPx(4f).toInt(), 0, 0)
                }

                val btnGunakan = createStyledButton("Gunakan", R.drawable.btn_green).apply {
                    setOnClickListener {
                        lifecycleScope.launch {
                            viewModel.setActive(child.id)
                            session.saveActiveChildId(child.id)
                        }
                    }
                }

                val btnHapus = if (!child.isActive) createStyledButton("Hapus", R.drawable.btn_red).apply {
                    setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Konfirmasi Penghapusan")
                            .setMessage("Apakah Anda yakin ingin menghapus data anak ini?")
                            .setPositiveButton("Ya") { _, _ ->
                                lifecycleScope.launch {
                                    viewModel.delete(child)
                                    Toast.makeText(requireContext(), "Data anak dihapus", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .setNegativeButton("Batal", null)
                            .show()
                    }
                } else null

                verticalLayout.addView(textInfo)
                verticalLayout.addView(statusText)
                if (!child.isActive) verticalLayout.addView(btnGunakan)
                btnHapus?.let { verticalLayout.addView(it) }

                card.addView(verticalLayout)
                layoutContainer.addView(card)
            }
        }

        return view
    }

    private fun showTambahAnakDialog(parentId: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_tambah_anak, null)
        val namaInput = dialogView.findViewById<EditText>(R.id.editNamaAnak)
        val umurInput = dialogView.findViewById<EditText>(R.id.editUmurAnak)
        val jkInput = dialogView.findViewById<EditText>(R.id.editJenisKelamin)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Anak")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val nama = namaInput.text.toString()
                val umur = umurInput.text.toString().toIntOrNull() ?: 0
                val jk = jkInput.text.toString()

                if (nama.isNotBlank() && jk.isNotBlank()) {
                    val anakBaru = ChildEntity(namaAnak = nama, umur = umur, jenisKelamin = jk, parentId = parentId)
                    lifecycleScope.launch {
                        viewModel.insert(anakBaru)
                    }
                } else {
                    Toast.makeText(requireContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun createStyledButton(textValue: String, backgroundDrawable: Int): Button {
        return Button(requireContext()).apply {
            text = textValue
            background = ContextCompat.getDrawable(requireContext(), backgroundDrawable)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dpToPx(8f).toInt()
            }
        }
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        )
    }
}