package com.aplikasistunting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aplikasistunting.R
import com.aplikasistunting.auth.SessionManager
import com.aplikasistunting.data.AppDatabase
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailInput = view.findViewById<EditText>(R.id.editEmail)
        val passwordInput = view.findViewById<EditText>(R.id.editPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnToRegister = view.findViewById<TextView>(R.id.btnToRegister)

        val session = SessionManager(requireContext())
        val dao = AppDatabase.getDatabase(requireContext()).parentDao()

        btnLogin.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            lifecycleScope.launch {
                val parent = dao.login(email, password)
                if (parent != null) {
                    session.saveParentLoginId(parent.id)
                    Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                } else {
                    Toast.makeText(requireContext(), "Email atau Password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }
}