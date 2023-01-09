package ru.ivos.mathproapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ivos.mathproapp.MainViewModel
import ru.ivos.mathproapp.R
import ru.ivos.mathproapp.databinding.FragmentGameBinding
import kotlin.random.Random

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding ?: throw java.lang.RuntimeException("Binding is empty")
    private var listNumbers = emptyList<Int>()
    private var countOfQuestions = 0
    private var countOfCorrectAnswers = 0
    private var score = 0

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateNewQuestion()
        observeViewModel()

        with(binding) {
            tvAnswerLeft.setOnClickListener {
                val isCorrect = tvAnswerLeft.text == listNumbers[3].toString()
                viewModel.onButtonClicked(
                    x = listNumbers[0],
                    y = listNumbers[1],
                    action = listNumbers[2],
                    isCorrect = isCorrect
                )
                if(isCorrect) {
                    countOfCorrectAnswers++
                }
                generateNewQuestion()
            }

            tvAnswerRight.setOnClickListener {
                val isCorrect = tvAnswerRight.text == listNumbers[3].toString()
                viewModel.onButtonClicked(
                    x = listNumbers[0],
                    y = listNumbers[1],
                    action = listNumbers[2],
                    isCorrect = isCorrect
                )
                if(isCorrect) {
                    countOfCorrectAnswers++
                }
                generateNewQuestion()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.timer.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                with(binding) {
                    tvTimerCount.text = it
                    if(it == "time out") {
                        tvTimerCount.text = requireContext().getString(R.string.time_out)
                        delay(1000)
                        generateNewQuestion()
                    }
                }
            }
        }

        viewModel.score.observe(viewLifecycleOwner) {
            binding.tvScoreCount.text = it.toString()
            score = it
        }
    }

    private fun generateNewQuestion() {

        if(countOfQuestions > 20) {
            val fragment = EndFragment.newInstance(countOfCorrectAnswers, score)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragContainer, fragment)
                .commit()
        }

        viewModel.timer()

        countOfQuestions++

        listNumbers = viewModel.generateQuestion()
        with(binding) {
            tvQuestionX.text = listNumbers[0].toString()
            tvQuestionY.text = listNumbers[1].toString()

            tvQuestionAction.text = when (listNumbers[2]) {
                0 -> "+"
                1 -> "-"
                2 -> "*"
                else -> "/"
            }


            /**
             * I don't understand why that, but if I request random int
             * from 0 to 1, I always get 0 and right answer always
             * in the left text field. That's why I add 6 options
             */
            when (Random.nextInt(0, 5)) {
                0, 3, 5 -> {
                    tvAnswerLeft.text = listNumbers[3].toString()
                    tvAnswerRight.text = listNumbers[4].toString()
                }
                1, 2, 4 -> {
                    tvAnswerLeft.text = listNumbers[4].toString()
                    tvAnswerRight.text = listNumbers[3].toString()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = GameFragment()
    }

}