package com.example.home33

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class HomeActivity : AppCompatActivity() {
    private lateinit var dateTextView: TextView
    private val databaseManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        dateTextView = findViewById(R.id.dateTextView)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        val customLayout = findViewById<LinearLayout>(R.id.customLayout)

        menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val homeButton = findViewById<ImageButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            //val intent = Intent(this, homeActivity::class.java)
            //startActivity(intent)
        }

        val calendarButton = findViewById<ImageButton>(R.id.calendarButton)
        calendarButton.setOnClickListener {
            //val intent = Intent(this, calendarActivity::class.java)
            //startActivity(intent)
        }

        val storyButton = findViewById<ImageButton>(R.id.storyButton)
        storyButton.setOnClickListener {
            //val intent = Intent(this, storyActivity::class.java)
            //startActivity(intent)
        }

        val todolistButton = findViewById<ImageButton>(R.id.todolistButton)
        todolistButton.setOnClickListener {
            //val intent = Intent(this, todolistActivity::class.java)
            //startActivity(intent)
        }

        val reminderButton = findViewById<ImageButton>(R.id.reminderButton)
        reminderButton.setOnClickListener {
            //val intent = Intent(this, reminderActivity::class.java)
            //startActivity(intent)
        }

        val exitButton = findViewById<ImageButton>(R.id.exitButton)
        exitButton.setOnClickListener {
            //finish()
        }

        val addButton = findViewById<ImageButton>(R.id.addButton)
        addButton.setOnClickListener {
            //val intent = Intent(this, AddActivity::class.java)
            //startActivity(intent)
        }

        val postToAdd = Post(
            id = 0,
            title = "오늘의 일기",
            content = "잠을 못잤다",
            date = "2023/05/07",
            tag = "#불면증",
            imageUrl = "이미지 URL"
        )

        databaseManager.addPost(postToAdd)

        val latestPost = databaseManager.getLatestPost()

        if (latestPost != null) {
            findViewById<TextView>(R.id.dateTextView1).text = latestPost.date
            findViewById<TextView>(R.id.tagsTextView1).text = latestPost.tag
            findViewById<TextView>(R.id.contentTextView1).text = latestPost.content
        } else {
            val errorMessage = "최신 포스트를 불러올 수 없습니다."
            findViewById<TextView>(R.id.dateTextView1).text = errorMessage
        }
    }

    override fun onResume() {
        super.onResume()

        val timeZone = TimeZone.getDefault()
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        sdf.timeZone = timeZone

        val currentDate = sdf.format(Date())
        dateTextView.text = currentDate
    }
}
