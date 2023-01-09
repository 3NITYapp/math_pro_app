package ru.ivos.mathproapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ivos.mathproapp.data.QuestionGenerator
import ru.ivos.mathproapp.data.local.PreferencesRepo
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val preferencesRepo = PreferencesRepo(context)

    private val generator = QuestionGenerator()
    private var job: Job? = null

    private val _timer = MutableLiveData<String>()
    val timer: LiveData<String> get() = _timer

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    fun generateQuestion() = generator.generate()

    private fun generateScore(x: Int, y: Int, action: Int): Int {
        return if (x < 40 && y < 40 && action < 2) {
            Random.nextInt(100, 130)
        } else if (action == 2) {
            Random.nextInt(130, 165)
        } else {
            Random.nextInt(165, 200)
        }
    }

    fun timer() {
        job?.cancel()
        job = viewModelScope.launch {
            val totalSeconds = 10L
            val tickSeconds = 1
            for (second in totalSeconds downTo tickSeconds) {
                val time = String.format(
                    "%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(second),
                    second - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.SECONDS.toMinutes(second)
                    )
                )
                _timer.value = time
                delay(1000)
            }
            _timer.value = "time out"
        }
    }

    fun onButtonClicked(x: Int, y: Int, action: Int, isCorrect: Boolean) {
        if (isCorrect) {
            _score.value = _score.value!! + generateScore(x, y, action)
            setSharedPref(_score.value!!)
        }
    }

    private fun setSharedPref(scorePref: Int) {
        val oldScore = preferencesRepo.getHighScore()
        if (oldScore < scorePref) {
            preferencesRepo.setHighScore(scorePref.toLong())
        }
    }
}