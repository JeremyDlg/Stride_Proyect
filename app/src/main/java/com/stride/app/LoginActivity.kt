package com.stride.app

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stride.app.util.Util

class LoginActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var txtEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtName = findViewById<EditText>(R.id.nameInput)
        txtEmail = findViewById<EditText>(R.id.emailInput)
        val btnContinue = findViewById<TextView>(R.id.continueButton)

        btnContinue.setOnClickListener(View.OnClickListener { view ->
            continueToHome()
        })
    }

    private fun continueToHome() {
        val name = txtName.text.toString()
        val email = txtEmail.text.toString()

        if (!Util.isValidName(name)) {
            Toast.makeText(this, getString(R.string.TextInvalidNameError),
                Toast.LENGTH_LONG).show()
        } else if (!Util.isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.TextInvalidEmailError),
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.TextWelcome) + " " + name.trim(),
                Toast.LENGTH_LONG).show()
            Util.openActivity(this, HomeActivity::class.java)

            finish()
        }
    }
}
