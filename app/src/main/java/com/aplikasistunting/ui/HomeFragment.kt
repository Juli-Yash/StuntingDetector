package com.aplikasistunting.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.aplikasistunting.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnDetect = view.findViewById<Button>(R.id.btn_detect)
        val btnHistory = view.findViewById<Button>(R.id.btn_history)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)

        btnDetect.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, FormFragment())
                addToBackStack(null)
            }

            bottomNav.selectedItemId = R.id.nav_form
        }

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