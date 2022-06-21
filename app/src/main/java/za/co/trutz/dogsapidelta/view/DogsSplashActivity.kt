package za.co.trutz.dogsapidelta.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import za.co.trutz.dogsapidelta.databinding.ActivityDogsSplashBinding

class DogsSplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogsSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogsSplashBinding.inflate(layoutInflater)

        binding.splashScreenContainer.alpha = 0f
        binding.splashScreenContainer.animate().setDuration(1500).alpha(1f).withEndAction{
            val openMainScreenIntent = Intent(this, MainActivity::class.java)
            startActivity(openMainScreenIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
        setContentView(binding.root)
    }
}