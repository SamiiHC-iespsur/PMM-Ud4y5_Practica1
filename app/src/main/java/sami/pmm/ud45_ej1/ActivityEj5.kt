package sami.pmm.ud45_ej1

import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

data class Task(
    val id: Int,
    val name: String,
    var isCompleted: Boolean = false
)

class ActivityEj5 : AppCompatActivity() {

    private lateinit var btnNavAll: Button
    private lateinit var btnNavCompleted: Button
    private lateinit var btnNavPending: Button
    private lateinit var tvSectionTitle: TextView
    private lateinit var etNewTask: EditText
    private lateinit var btnAddTask: Button
    private lateinit var llTaskList: LinearLayout
    private lateinit var tvEmptyMessage: TextView
    private lateinit var btnSave: Button

    private val tasks = mutableListOf<Task>()
    private var nextTaskId = 1
    private var currentFilter = Filter.ALL

    // Persistence helpers
    private val prefs by lazy { getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }

    companion object {
        private const val PREF_TASKS = "tasks_json"
        private const val PREF_NEXT_ID = "next_task_id"
        private const val PREFS_NAME = "ej5_prefs"
    }

    enum class Filter {
        ALL, COMPLETED, PENDING
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ej5)

        // Initialize views
        btnNavAll = findViewById(R.id.btn_nav_all)
        btnNavCompleted = findViewById(R.id.btn_nav_completed)
        btnNavPending = findViewById(R.id.btn_nav_pending)
        tvSectionTitle = findViewById(R.id.tv_section_title)
        etNewTask = findViewById(R.id.et_new_task)
        btnAddTask = findViewById(R.id.btn_add_task)
        llTaskList = findViewById(R.id.ll_task_list)
        tvEmptyMessage = findViewById(R.id.tv_empty_message)
        btnSave = findViewById(R.id.btn_save)

        // Load previously saved tasks (if any)
        loadSavedTasks()

        // Set up navigation listeners
        btnNavAll.setOnClickListener {
            currentFilter = Filter.ALL
            updateTaskList()
        }

        btnNavCompleted.setOnClickListener {
            currentFilter = Filter.COMPLETED
            updateTaskList()
        }

        btnNavPending.setOnClickListener {
            currentFilter = Filter.PENDING
            updateTaskList()
        }

        // Set up add task button
        btnAddTask.setOnClickListener {
            addTask()
        }

        // Set up save button
        btnSave.setOnClickListener {
            saveChanges()
        }

        // If nothing loaded, ensure empty state is reflected
        updateEmptyState()
    }

    private fun addTask() {
        val taskName = etNewTask.text.toString().trim()

        if (taskName.isEmpty()) {
            Toast.makeText(this, R.string.ej5_error_empty, Toast.LENGTH_SHORT).show()
            return
        }

        // Create new task
        val newTask = Task(nextTaskId++, taskName)
        tasks.add(newTask)

        // Clear input
        etNewTask.text.clear()

        // Update list
        updateTaskList()

        Toast.makeText(this, R.string.ej5_task_added, Toast.LENGTH_SHORT).show()
    }

    private fun updateTaskList() {
        // Clear current list
        llTaskList.removeAllViews()

        // Filter tasks
        val filteredTasks = when (currentFilter) {
            Filter.ALL -> tasks
            Filter.COMPLETED -> tasks.filter { it.isCompleted }
            Filter.PENDING -> tasks.filter { !it.isCompleted }
        }

        // Update title
        tvSectionTitle.text = when (currentFilter) {
            Filter.ALL -> getString(R.string.ej5_nav_tasks)
            Filter.COMPLETED -> getString(R.string.ej5_nav_completed)
            Filter.PENDING -> getString(R.string.ej5_nav_pending)
        }

        // Add tasks to list
        filteredTasks.forEach { task ->
            addTaskView(task)
        }

        updateEmptyState()
    }

    private fun addTaskView(task: Task) {
        // Create task item layout
        val taskItemLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16, 16, 16, 16)
            setBackgroundResource(android.R.drawable.list_selector_background)
        }

        // Create CheckBox for task completion
        val cbComplete = CheckBox(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 16
            }
            isChecked = task.isCompleted
            buttonTintList = ContextCompat.getColorStateList(this@ActivityEj5, R.color.color_blue)
            setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                if (isChecked) {
                    Toast.makeText(this@ActivityEj5, R.string.ej5_task_completed, Toast.LENGTH_SHORT).show()
                }
                updateTaskName(task)
            }
        }

        // Create TextView for task name
        val tvTaskName = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginEnd = 16
            }
            text = task.name
            textSize = 16f
            setTextColor(ContextCompat.getColor(this@ActivityEj5, R.color.black))
            setPadding(0, 0, 16, 0)

            if (task.isCompleted) {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                alpha = 0.6f
            }
        }

        // Store reference to update later
        cbComplete.tag = tvTaskName

        // Create Delete button
        val btnDelete = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                minimumWidth = 120
            }
            text = getString(R.string.ej5_btn_delete)
            isAllCaps = false

            val roundedBg = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(ContextCompat.getColor(this@ActivityEj5, R.color.color_blue))
                cornerRadius = 100f
            }
            background = roundedBg

            setTextColor(ContextCompat.getColor(this@ActivityEj5, R.color.white))
            setPadding(20, 12, 20, 12)

            setOnClickListener {
                deleteTask(task, taskItemLayout)
            }
        }

        // Add views to task item
        taskItemLayout.addView(cbComplete)
        taskItemLayout.addView(tvTaskName)
        taskItemLayout.addView(btnDelete)

        // Add to main list
        llTaskList.addView(taskItemLayout)
    }

    private fun updateTaskName(@Suppress("UNUSED_PARAMETER") task: Task) {
        // Recreate the entire list to update UI
        updateTaskList()
    }

    private fun deleteTask(task: Task, taskView: View) {
        tasks.remove(task)
        llTaskList.removeView(taskView)
        updateEmptyState()
        Toast.makeText(this, R.string.ej5_task_deleted, Toast.LENGTH_SHORT).show()
    }

    private fun updateEmptyState() {
        val filteredTasks = when (currentFilter) {
            Filter.ALL -> tasks
            Filter.COMPLETED -> tasks.filter { it.isCompleted }
            Filter.PENDING -> tasks.filter { !it.isCompleted }
        }

        if (filteredTasks.isEmpty()) {
            tvEmptyMessage.visibility = View.VISIBLE
            tvEmptyMessage.text = when (currentFilter) {
                Filter.ALL -> getString(R.string.ej5_empty_all)
                Filter.COMPLETED -> getString(R.string.ej5_empty_completed)
                Filter.PENDING -> getString(R.string.ej5_empty_pending)
            }
        } else {
            tvEmptyMessage.visibility = View.GONE
        }
    }

    private fun saveChanges() {
        // Serialize tasks into JSON and persist to SharedPreferences
        try {
            val arr = JSONArray()
            tasks.forEach { t ->
                val obj = JSONObject().apply {
                    put("id", t.id)
                    put("name", t.name)
                    put("completed", t.isCompleted)
                }
                arr.put(obj)
            }
            // Use KTX extension to edit SharedPreferences
            prefs.edit {
                putString(PREF_TASKS, arr.toString())
                putInt(PREF_NEXT_ID, nextTaskId)
            }
            Toast.makeText(this, R.string.ej5_changes_saved, Toast.LENGTH_LONG).show()
        } catch (_: Exception) {
            Toast.makeText(this, "Error al guardar las tareas", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadSavedTasks() {
        val json = prefs.getString(PREF_TASKS, null)
        tasks.clear()
        if (!json.isNullOrEmpty()) {
            try {
                val arr = JSONArray(json)
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val id = obj.optInt("id")
                    val name = obj.optString("name")
                    val completed = obj.optBoolean("completed")
                    tasks.add(Task(id, name, completed))
                }
                // Restore nextTaskId; if missing, compute from max id
                nextTaskId = prefs.getInt(PREF_NEXT_ID, (tasks.maxOfOrNull { it.id } ?: 0) + 1)
            } catch (_: Exception) {
                // Corrupt/invalid JSON; start fresh
                tasks.clear()
                nextTaskId = 1
            }
        } else {
            nextTaskId = 1
        }
        // Reflect loaded tasks
        updateTaskList()
    }
}
