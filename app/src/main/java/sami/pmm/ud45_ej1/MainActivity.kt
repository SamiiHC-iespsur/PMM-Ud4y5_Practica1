package sami.pmm.ud45_ej1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Wire buttons programmatically to avoid missing-method lint warnings
        findViewById<Button>(R.id.btn_ej1).setOnClickListener { openActivityByName("activity_ej1") }
        findViewById<Button>(R.id.btn_ej2).setOnClickListener { openActivityByName("activity_ej2") }
        findViewById<Button>(R.id.btn_ej3).setOnClickListener { openActivityByName("ActivityEj3") }
        findViewById<Button>(R.id.btn_ej4).setOnClickListener { openActivityByName("ActivityEj4") }
        findViewById<Button>(R.id.btn_ej5).setOnClickListener { openActivityByName("activity_ej5") }
    }

    private fun openActivityByName(simpleName: String) {
        val fullName = "$packageName.$simpleName"
        val intent = Intent()
        intent.setClassName(this, fullName)
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, "Activity '$simpleName' not found", Toast.LENGTH_SHORT).show()
        }
    }
}