package sami.pmm.ud45_ej1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ActivityEj4 : AppCompatActivity() {

    private lateinit var btnNavHome: Button
    private lateinit var btnNavProfile: Button
    private lateinit var btnNavSettings: Button
    private lateinit var tvSectionTitle: TextView
    private lateinit var tvSectionMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ej4)

        // Initialize views
        btnNavHome = findViewById(R.id.btn_nav_home)
        btnNavProfile = findViewById(R.id.btn_nav_profile)
        btnNavSettings = findViewById(R.id.btn_nav_settings)
        tvSectionTitle = findViewById(R.id.tv_section_title)
        tvSectionMessage = findViewById(R.id.tv_section_message)

        // Set up navigation button click listeners
        btnNavHome.setOnClickListener {
            navigateToSection("home")
        }

        btnNavProfile.setOnClickListener {
            navigateToSection("profile")
        }

        btnNavSettings.setOnClickListener {
            navigateToSection("settings")
        }
    }

    private fun navigateToSection(section: String) {
        when (section) {
            "home" -> {
                tvSectionTitle.text = getString(R.string.ej4_btn_home)
                tvSectionMessage.text = getString(R.string.ej4_section_home)
                Toast.makeText(this, R.string.ej4_section_home, Toast.LENGTH_SHORT).show()
            }
            "profile" -> {
                tvSectionTitle.text = getString(R.string.ej4_btn_profile)
                tvSectionMessage.text = getString(R.string.ej4_section_profile)
                Toast.makeText(this, R.string.ej4_section_profile, Toast.LENGTH_SHORT).show()
            }
            "settings" -> {
                tvSectionTitle.text = getString(R.string.ej4_btn_settings)
                tvSectionMessage.text = getString(R.string.ej4_section_settings)
                Toast.makeText(this, R.string.ej4_section_settings, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

