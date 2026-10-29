package com.stride.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.R
import com.stride.app.util.Validators

/** Local login: the user types a name and an email that stay on the phone. */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<View>(R.id.continueButton).setOnClickListener { onContinueClicked() }
    }

    private fun onContinueClicked() {
        val name = findViewById<EditText>(R.id.nameInput).text.toString()
        val email = findViewById<EditText>(R.id.emailInput).text.toString()
        when {
            !Validators.isValidName(name) ->
                Toast.makeText(this, R.string.error_invalid_name, Toast.LENGTH_SHORT).show()
            !Validators.isValidEmail(email) ->
                Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show()
            else -> {
                Toast.makeText(this, getString(R.string.toast_welcome, name.trim()), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}