package com.jamie.businessideasevaluator.View.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.databinding.ActivityTutorialStartUpBinding

class TutorialStartUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialStartUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTutorialStartUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonLetsGo = binding.letGoBtn
        buttonLetsGo.setOnClickListener {
            val toNextTut = Intent(this, TutorialExampleActivity::class.java)
            startActivity(toNextTut)

        }
    }
}