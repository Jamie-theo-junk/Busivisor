package com.jamie.businessideasevaluator.View.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jamie.businessideasevaluator.R

class BookAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_ad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val container = findViewById<FrameLayout>(R.id.bookAdContainer)

        val layouts = listOf(R.layout.book_layout_one,)

        val randomLayoutId = layouts.random()

        container.removeAllViews()

        val view = layoutInflater.inflate(randomLayoutId, container, false)        

        view.translationY = 200f
        view.alpha = 0f

        container.addView(view)


        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1000)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {

                val button = view.findViewById<CardView>(R.id.saveCard)
                button?.apply {
                    alpha = 0f
                    scaleX = 0.8f
                    scaleY = 0.8f
                    postDelayed({
                        animate()
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(800)
                            .setInterpolator(AccelerateDecelerateInterpolator())
                            .start()
                    }, 300)
                }
            }
            .start()

        val button = findViewById<CardView>(R.id.saveCard)
        button.setOnClickListener {
            val url = "https://www.amazon.com/dp/B0FJ9Q8S6P?ref_=ast_author_dp"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        val backButton = findViewById<ImageButton>(R.id.backBtnBook)
        backButton.setOnClickListener {
            finish()
        }
    }
}
