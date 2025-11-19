package sami.pmm.ud45_ej1

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ActivityEj2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ej2)

        val etFavColor = findViewById<EditText>(R.id.et_ej2_fav_color)
        val cbLikeBlue = findViewById<CheckBox>(R.id.cb_ej2_like_blue)
        val rgGreen = findViewById<RadioGroup>(R.id.rg_ej2_green)
        val btnSend = findViewById<Button>(R.id.btn_ej2_send)

        btnSend.setOnClickListener {
            val favColor = etFavColor.text.toString().lowercase()
            val likesBlue = cbLikeBlue.isChecked
            val selectedRadioId = rgGreen.checkedRadioButtonId

            if (favColor.isEmpty()) {
                Toast.makeText(this, getString(R.string.ej2_error_color_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedRadioId == -1) {
                Toast.makeText(this, getString(R.string.ej2_error_green_unanswered), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadio = findViewById<RadioButton>(selectedRadioId)
            val likesGreen = selectedRadio.id == R.id.rb_ej2_yes

            val parts = mutableListOf<String>()
            parts += getString(R.string.ej2_result_prefix)
            parts += favColor
            if (likesBlue) parts += getString(R.string.ej2_result_blue)
            if (likesGreen) parts += getString(R.string.ej2_result_green)
            val message = parts.joinToString(", ")

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}
