package sami.pmm.ud45_ej1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        findViewById<Button>(R.id.btn_ej1).setOnClickListener { startActivity(Intent(this, ActivityEj1::class.java)) }
        findViewById<Button>(R.id.btn_ej2).setOnClickListener { startActivity(Intent(this, ActivityEj2::class.java)) }
        findViewById<Button>(R.id.btn_ej3).setOnClickListener { startActivity(Intent(this, ActivityEj3::class.java)) }
        findViewById<Button>(R.id.btn_ej4).setOnClickListener { startActivity(Intent(this, ActivityEj4::class.java)) }
        findViewById<Button>(R.id.btn_ej5).setOnClickListener { startActivity(Intent(this, ActivityEj5::class.java)) }
    }
}