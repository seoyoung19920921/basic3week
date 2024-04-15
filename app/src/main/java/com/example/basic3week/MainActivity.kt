package com.example.basic3week

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.basic3week.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var job: Job? = null
    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"
    private var counter = 1
    private var buttonCheck = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupButton()
        setRandomValueBetweenOneToHundred()

    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        if (buttonCheck) {
            setJobAndLaunch()
        } else {
            binding.tv2.text = counter.toString()
        }

    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        job?.cancel()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
        job?.cancel()

    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
        counter = savedInstanceState.getInt("counter")
        buttonCheck = savedInstanceState.getBoolean("buttonCheck")
        binding.tv1.text = savedInstanceState.getString("randomValue")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt("counter", counter)
        outState.putBoolean("buttonCheck", buttonCheck)
        outState.putString("randomValue", binding.tv1.text.toString())
    }

    private fun setupButton() {
        binding.btn1.setOnClickListener {
            checkAnswerAndShowToast()
            job?.cancel()
            buttonCheck = false
        }
    }

    private fun setRandomValueBetweenOneToHundred() {
        val randomValue = (1..100).random()
        binding.tv1.text = randomValue.toString()
    }


    private fun setJobAndLaunch() {
        job?.cancel()
        job = lifecycleScope.launch {
            while (counter <= 100) {
                if (isActive) {

                    binding.tv2.text = (++counter).toString()
                    delay(500)
                }
            }
        }


    }

    private fun checkAnswerAndShowToast() {
        val spartaText = binding.tv2.text.toString()
        val randomText = binding.tv1.text.toString()
        if (spartaText == randomText) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }
    }
}