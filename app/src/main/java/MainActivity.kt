package com.mycompany.myfirstapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mycompany.myfirstapp.R


class MainActivity : AppCompatActivity() {

    private lateinit var dino: View
    private lateinit var cactus: View
    private lateinit var scoreText: TextView
    private var isJumping = false
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dino = findViewById(R.id.dino)
        cactus = findViewById(R.id.cactus)
        scoreText = findViewById(R.id.scoreText)

        // Прыжок при нажатии на экран
        findViewById<FrameLayout>(R.id.gameRoot).setOnClickListener {
            if (!isJumping) jump()
        }

        startGame()
    }

    private fun jump() {
        isJumping = true
        dino.animate()
            .translationY(-300f) // Высота прыжка
            .setDuration(400)
            .withEndAction {
                dino.animate()
                    .translationY(0f) // Возврат вниз
                    .setDuration(400)
                    .withEndAction { isJumping = false }
                    .start()
            }.start()
    }

    private fun startGame() {
        val runnable = object : Runnable {
            override fun run() {
                // Двигаем кактус влево
                cactus.translationX -= 15f

                // Если кактус ушел за экран — возвращаем его на старт
                if (cactus.x + cactus.width < 0) {
                    cactus.translationX = 0f
                    score++
                    scoreText.text = "Score: $score"
                }

                // Простая проверка столкновения
                if (isCollision(dino, cactus)) {
                    score = 0
                    scoreText.text = "К счастью! Очки: 0"
                    cactus.translationX = 0f
                }

                handler.postDelayed(this, 20) // Скорость обновления (50 кадров в сек)
            }
        }
        handler.post(runnable)
    }

    private fun isCollision(v1: View, v2: View): Boolean {
        val v1Loc = IntArray(2)
        val v2Loc = IntArray(2)
        v1.getLocationOnScreen(v1Loc)
        v2.getLocationOnScreen(v2Loc)

        return (v1Loc[0] < v2Loc[0] + v2.width &&
                v1Loc[0] + v1.width > v2Loc[0] &&
                v1Loc[1] < v2Loc[1] + v2.height &&
                v1Loc[1] + v1.height > v2Loc[1])
    }
}
