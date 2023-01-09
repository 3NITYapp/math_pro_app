package ru.ivos.mathproapp.data

import kotlin.random.Random

class QuestionGenerator() {

    fun generate(): List<Int> {
        var x = Random.nextInt(1, 200)
        var y = Random.nextInt(1, 200)
        var correctAnswer = 0
        var incorrectAnswer = 0
        var action = 0

        when(Random.nextInt(0, 3)) {
            0 -> {
                correctAnswer = x + y
                action = 0
            }
            1 -> {
                correctAnswer = x - y
                action = 1
            }
            2 -> {
                x = Random.nextInt(3, 20)
                y = Random.nextInt(3, 20)
                correctAnswer = x * y
                action = 2
            }
            else -> {
                x = Random.nextInt(1, 20)
                correctAnswer = Random.nextInt(0, 10)
                y = x / correctAnswer
                action = 3
            }
        }

        incorrectAnswer = when(Random.nextInt(0, 1)){
            0 ->  correctAnswer + Random.nextInt(5, 15)
            else ->  correctAnswer - Random.nextInt(5, 15)
        }

        return listOf(x, y, action, correctAnswer, incorrectAnswer)
    }
}