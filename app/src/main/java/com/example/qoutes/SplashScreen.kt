package com.example.qoutes

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qoutes.databinding.ActivitySplashScreenBinding
import com.example.qoutes.ui.QuotesActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- هنا السر: التحكم في السرعة ---
        // 1.0f = السرعة العادية
        // 1.5f = أسرع مرة ونص
        // 2.0f = أسرع مرتين (الضعف)
        binding.lottieView.speed = 2.0f // جرب الرقم ده هيعجبك

        // التحكم في الأنيميشن
        binding.lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // أول ما يخلص (بالسرعة الجديدة) ينقل
                goToHome()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun goToHome() {
        val intent = Intent(this, QuotesActivity::class.java)
        startActivity(intent)
        finish()
    }
}