package com.aplikasistunting.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aplikasistunting.R
import com.aplikasistunting.data.AppDatabase
import com.aplikasistunting.data.ParentEntity
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val namaInput = view.findViewById<EditText>(R.id.editNama)
        val emailInput = view.findViewById<EditText>(R.id.editEmail)
        val passwordInput = view.findViewById<EditText>(R.id.editPassword)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)

        val dao = AppDatabase.getDatabase(requireContext()).parentDao()

        btnRegister.setOnClickListener {
            val nama = namaInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (nama.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newParent = ParentEntity(nama = nama, email = email, password = password)

            lifecycleScope.launch {
                val existing = dao.getParentByEmail(email)
                if (existing != null) {
                    Toast.makeText(requireContext(), "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                } else {
                    dao.insert(newParent)
                    Toast.makeText(requireContext(), "Registrasi berhasil, silakan login", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }

        return view
    }
}