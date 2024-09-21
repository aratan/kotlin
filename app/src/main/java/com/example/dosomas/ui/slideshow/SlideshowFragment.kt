package com.example.dosomas.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dosomas.R
import com.example.dosomas.databinding.FragmentSlideshowBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class SlideshowFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        val usernameEditText = root.findViewById<EditText>(R.id.et_username)
        val passwordEditText = root.findViewById<EditText>(R.id.et_password)
        val loginButton = root.findViewById<Button>(R.id.btn_login)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(username, password)) {
                Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                // Aquí puedes añadir la lógica para pasar a la siguiente actividad
            } else {
                Toast.makeText(activity, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun validateLogin(username: String, password: String): Boolean {
        // Aquí puedes añadir la lógica de validación, por ejemplo:
        return username == "admin" && password == "1234"
    }
}
