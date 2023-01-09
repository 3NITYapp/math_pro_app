package ru.ivos.mathproapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.ivos.mathproapp.R
import ru.ivos.mathproapp.data.local.PreferencesRepo
import ru.ivos.mathproapp.databinding.FragmentEndBinding


class EndFragment : Fragment() {

    private var _binding: FragmentEndBinding? = null
    private val binding get() = _binding ?: throw java.lang.RuntimeException("Binding is empty")

    private var countOfCorrectAnswers = 0
    private var score = 0

    private lateinit var preferencesRepo: PreferencesRepo

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferencesRepo = PreferencesRepo(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countOfCorrectAnswers = it.getInt(COUNT_CORRECT_ANSWERS)
            score = it.getInt(SCORE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRetryGame.setOnClickListener {
                val fragment = GameFragment.newInstance()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragContainer, fragment)
                    .commit()
            }

            btnHome.setOnClickListener {
                val fragment = HomeFragment.newInstance()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragContainer, fragment)
                    .commit()
            }

            tvRecordScore.text = preferencesRepo.getHighScore().toString()

            tvCountOfCorrectAnswers.text = countOfCorrectAnswers.toString()
            tvGameScore.text = score.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val COUNT_CORRECT_ANSWERS = "CountOfCorrectAnswers"
        private const val SCORE = "score"

        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EndFragment().apply {
                arguments = Bundle().apply {
                    putInt(COUNT_CORRECT_ANSWERS, param1)
                    putInt(SCORE, param2)
                }
            }

    }
}