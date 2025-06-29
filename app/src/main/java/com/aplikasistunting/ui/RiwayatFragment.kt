package com.aplikasistunting.ui

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aplikasistunting.R
import com.aplikasistunting.data.AppDatabase
import com.aplikasistunting.repository.HistoryRepository
import com.aplikasistunting.viewmodel.HistoryViewModel
import com.aplikasistunting.viewmodel.HistoryViewModelFactory
import kotlinx.coroutines.launch

class RiwayatFragment : Fragment() {
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_riwayat, container, false)
        val layoutRiwayat = view.findViewById<LinearLayout>(R.id.layoutRiwayat)

        val dao = AppDatabase.getDatabase(requireContext()).historyDao()
        val repository = HistoryRepository(dao)
        val factory = HistoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        viewModel.allHistory.observe(viewLifecycleOwner) { list ->
            layoutRiwayat.removeAllViews()

            list.forEach { history ->
                val card = CardView(requireContext()).apply {
                    radius = dpToPx(12f)
                    setContentPadding(32, 32, 32, 32)
                    cardElevation = dpToPx(4f)
                    useCompatPadding = true
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.card_bg)
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

                val textNamaTanggal = TextView(requireContext()).apply {
                    text = "Nama: ${history.nama}\nTanggal: ${history.tanggal}"
                    setTextColor(Color.BLACK)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                }

                val textStatus = TextView(requireContext()).apply {
                    text = "Stunting: ${history.statusStunting}\nUnderweight: ${history.statusUnderweight}\nWasting: ${history.statusWasting}"
                    setTextColor(Color.BLACK)
                    visibility = View.GONE
                    setPadding(0, dpToPx(8f).toInt(), 0, 0)
                }

                val expandButton = createStyledButton("Sembunyikan", R.drawable.btn_green).apply {
                    visibility = View.GONE
                }

                val deleteButton = createStyledButton("Hapus", R.drawable.btn_red).apply {
                    visibility = View.GONE
                    setOnClickListener {
                        lifecycleScope.launch {
                            viewModel.delete(history)
                        }
                    }
                }

                val showDetailButton = createStyledButton("Lihat Detail", R.drawable.btn_green).apply {
                    setOnClickListener {
                        textStatus.visibility = View.VISIBLE
                        expandButton.visibility = View.VISIBLE
                        deleteButton.visibility = View.VISIBLE
                        this.visibility = View.GONE
                    }
                }

                expandButton.setOnClickListener {
                    textStatus.visibility = View.GONE
                    expandButton.visibility = View.GONE
                    deleteButton.visibility = View.GONE
                    showDetailButton.visibility = View.VISIBLE
                }

                verticalLayout.apply {
                    addView(textNamaTanggal)
                    addView(showDetailButton)
                    addView(textStatus)
                    addView(expandButton)
                    addView(deleteButton)
                }

                card.addView(verticalLayout)
                layoutRiwayat.addView(card)
            }
        }

        return view
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