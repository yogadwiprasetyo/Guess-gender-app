package com.yogaprasetyo.juaraandroid.guessgender.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yogaprasetyo.juaraandroid.guessgender.R
import com.yogaprasetyo.juaraandroid.guessgender.databinding.FragmentHistoryBinding
import com.yogaprasetyo.juaraandroid.guessgender.model.AppViewModel
import com.yogaprasetyo.juaraandroid.guessgender.model.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding

    private val appViewModel: AppViewModel by activityViewModels {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false) // Not using menu options
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Remove/restart livedata value on view model
        appViewModel.restart()

        val historyAdapter = HistoryAdapter { history ->
            appViewModel.removeHistory(history)
        }

        appViewModel.loadAllHistory().observe(viewLifecycleOwner) { listHistory ->
            if (listHistory != null) {
                historyAdapter.submitList(listHistory)
            }
        }

        binding?.fbDeleteAll?.setOnClickListener {
            showAlertDialog()
        }

        binding?.rvHistory?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    /**
     * Show alert dialog for delete all history
     * */
    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.alert_title)
            .setMessage(R.string.alert_message)
            .setNegativeButton(R.string.alert_yes) { _, _ -> appViewModel.removeAllHistory() }
            .setPositiveButton(R.string.alert_no) { dialog, _ -> dialog.cancel() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}