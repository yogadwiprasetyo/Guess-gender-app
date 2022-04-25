package com.yogaprasetyo.juaraandroid.guessgender.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yogaprasetyo.juaraandroid.guessgender.R
import com.yogaprasetyo.juaraandroid.guessgender.data.Result
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.response.ResponseGuessGender
import com.yogaprasetyo.juaraandroid.guessgender.databinding.FragmentGuessGenderBinding
import com.yogaprasetyo.juaraandroid.guessgender.model.AppViewModel
import com.yogaprasetyo.juaraandroid.guessgender.model.ViewModelFactory

/**
 * App is running perfectly
 *
 * TODO Refactoring to using data binding and add unit testing
 * */

class GuessGenderFragment : Fragment() {

    private var _binding: FragmentGuessGenderBinding? = null
    private val binding get() = _binding

    // Declaration viewmodel with delegate KTX library
    private val appViewModel: AppViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Inflate options menu is true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGuessGenderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.result.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Result.Loading -> onLoading(true)
                    is Result.Success -> showResult(response.data)
                    is Result.Error -> {
                        onLoading(false)
                        Toast.makeText(requireContext(), response.error, Toast.LENGTH_LONG).show()
                        appViewModel.restart()
                    }
                }
            }
        }

        binding?.ibSendName?.setOnClickListener {
            hideKeyboard()

            val editText = binding?.textField?.editText
            val name = editText?.text.toString()

            // Checking empty name
            if (name.isEmpty()) { return@setOnClickListener }

            appViewModel.setName(name)
            editText?.setText("")
            editText?.clearFocus()
        }
    }

    /**
     * Attach all value data response to UI/widget
     * TODO Refactor with using data binding
     * */
    private fun showResult(data: ResponseGuessGender) {
        onLoading(false)

        // Extract value data class
        val (gender, probability, name, count) = data

        val resultGender = getString(R.string.result_gender, chooseGender(gender))
        val resultDescription = if (gender != null)
            getString(R.string.result_description, name, count, gender, toPercent(probability))
        else
            getString(R.string.result_unknown_description, name)

        binding?.apply {
            ivResult.setImageResource(pickRandomIconByGender(gender))
            tvResultGender.text = resultGender
            tvResultDescription.text = resultDescription
        }
    }

    /**
     * When gender is null, replace to "unknown"
     * and if not null return gender value.
     * */
    private fun chooseGender(gender: String?): String {
        val unknownGender = getString(R.string.gender_unknown)
        return gender ?: unknownGender
    }

    /**
     * Convert probability value to int
     * and calculate to the percent.
     * */
    private fun toPercent(value: Double): Int = (value * 100).toInt()

    /**
     * Show and hide UI component when loading is true/false
     * show progress when true and hide when false
     * */
    private fun onLoading(isWaiting: Boolean) {
        val gone = View.GONE
        val visible = View.VISIBLE

        if (isWaiting) {
            binding?.apply {
                ivResult.visibility = gone
                tvResultGender.visibility = gone
                tvResultDescription.visibility = gone
                pbLoading.visibility = visible
            }
        } else {
            binding?.apply {
                ivResult.visibility = visible
                tvResultGender.visibility = visible
                tvResultDescription.visibility = visible
                pbLoading.visibility = gone
            }
        }
    }

    /**
     * Pick random icon character male/female based on the response gender
     * this function will return @Drawable
     * */
    private fun pickRandomIconByGender(gender: String?): Int {
        return when (gender) {
            "male" -> allIconMale.random()
            "female" -> allIconFemale.random()
            else -> R.drawable.unknown_gender
        }
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Inflate the menu options by R.menu.menu_item
     * */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_item, menu)
    }

    /**
     * Move to fragment history when menu icon history clicked
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_history) findNavController().navigate(R.id.action_guessGenderFragment_to_historyFragment)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val allIconMale = listOf(
            R.drawable.male_one,
            R.drawable.male_two,
            R.drawable.male_three
        )

        private val allIconFemale = listOf(
            R.drawable.female_one,
            R.drawable.female_two,
            R.drawable.female_three,
        )
    }
}