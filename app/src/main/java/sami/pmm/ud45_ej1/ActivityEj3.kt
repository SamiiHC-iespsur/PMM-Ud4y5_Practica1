package sami.pmm.ud45_ej1

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ActivityEj3 : AppCompatActivity() {

    private lateinit var etNewTask: EditText
    private lateinit var btnAddTask: Button
    private lateinit var llTaskList: LinearLayout
    private lateinit var tvEmptyList: TextView

    private val tasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ej3)

        // Initialize views
        etNewTask = findViewById(R.id.et_new_task)
        btnAddTask = findViewById(R.id.btn_add_task)
        llTaskList = findViewById(R.id.ll_task_list)
        tvEmptyList = findViewById(R.id.tv_empty_list)


        // Set up add button click listener
        btnAddTask.setOnClickListener {
            addTask()
        }
    }

    private fun addTask() {
        val taskText = etNewTask.text.toString().trim()

        if (taskText.isEmpty()) {
            Toast.makeText(this, R.string.ej3_error_empty, Toast.LENGTH_SHORT).show()
            return
        }

        // Add task to list
        tasks.add(taskText)

        // Create task item view programmatically
        val taskItemLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundResource(android.R.drawable.list_selector_background)
        }

        // Create TextView for task name
        val tvTaskName = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginEnd = 24
            }
            text = taskText
            textSize = 16f
            setTextColor(ContextCompat.getColor(this@ActivityEj3, R.color.black))
            setPadding(16, 0, 24, 0)
        }

        // Create Delete button
        val btnDeleteTask = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                minimumWidth = 200
            }
            text = getString(R.string.ej3_btn_delete)
            isAllCaps = false

            // Create rounded background programmatically with maximum roundness
            val roundedBackground = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(ContextCompat.getColor(this@ActivityEj3, R.color.color_yellow))
                cornerRadius = 100f // Very high radius for pill-shaped buttons
            }
            background = roundedBackground

            setTextColor(ContextCompat.getColor(this@ActivityEj3, R.color.black))
            setPadding(24, 16, 24, 16)
            setOnClickListener {
                deleteTask(taskText, taskItemLayout)
            }
        }

        // Add views to task item layout
        taskItemLayout.addView(tvTaskName)
        taskItemLayout.addView(btnDeleteTask)

        // Add to main layout
        llTaskList.addView(taskItemLayout)

        // Clear input
        etNewTask.text.clear()

        // Update empty state
        updateEmptyState()

        Toast.makeText(this, R.string.ej3_task_added, Toast.LENGTH_SHORT).show()
    }

    private fun deleteTask(taskText: String, taskView: View) {
        // Remove from list
        tasks.remove(taskText)

        // Remove from layout
        llTaskList.removeView(taskView)

        // Update empty state
        updateEmptyState()

        Toast.makeText(this, R.string.ej3_task_deleted, Toast.LENGTH_SHORT).show()
    }

    private fun updateEmptyState() {
        if (tasks.isEmpty()) {
            tvEmptyList.visibility = View.VISIBLE
        } else {
            tvEmptyList.visibility = View.GONE
        }
    }
}

