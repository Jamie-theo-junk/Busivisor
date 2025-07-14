package com.jamie.businessideasevaluator.View.Activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.databinding.ActivityBusinessNameBinding

private lateinit var binding: ActivityBusinessNameBinding

class BusinessNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusinessNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val name = binding.businessNameInput
        val description = binding.businessDescriptionInput
        val saveButton = binding.saveCard
        saveButton.setOnClickListener {
            val businessName = name.text.toString().trim()
            val businessDescription = description.text.toString().trim()


            if (businessName.isEmpty() && businessDescription.isEmpty()) {
                Toast.makeText(this, "Please enter a business name or description", Toast.LENGTH_SHORT).show()
            } else {

                val intent = Intent(this, RankingsActivity::class.java)
                intent.putExtra("business_name", businessName)
                intent.putExtra("business_description",businessDescription)
                startActivity(intent)
            }
        }
    }
}