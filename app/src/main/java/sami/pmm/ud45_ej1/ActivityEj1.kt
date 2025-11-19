package sami.pmm.ud45_ej1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ActivityEj1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ej1)

        val etNombre = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val btnEnviar = findViewById<Button>(R.id.btn_submit)

        btnEnviar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            if (nombre.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, getString(R.string.ej1_error_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.ej1_success_message) + " (" + nombre + ", " + email + ")", Toast.LENGTH_LONG).show()
            }
        }
    }
}
