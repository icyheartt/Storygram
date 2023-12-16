package com.example.home33
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Random

class ReminderMainActivity : AppCompatActivity() {

    private lateinit var reminderCardView: CardView
    private lateinit var rotatingImage: ImageView

    private val imageList = listOf(
        //데이터베이스에서 가져올 이미지
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_foreground,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reminder_activity_main)

        reminderCardView = findViewById(R.id.reminder_CardView)
        rotatingImage = findViewById(R.id.rotatingImage)

        rotatingImage.setImageResource(R.drawable.guide_image)

        rotatingImage.setOnClickListener {
            setRandomImage()
        }
    }

    private fun setRandomImage() {
        if (imageList.isEmpty()) {
            return
        }

        val randomIndex = Random().nextInt(imageList.size)
        val randomImage = imageList[randomIndex]

        // 이미지뷰에 랜덤 이미지 설정
        rotatingImage.setImageResource(randomImage)
        rotateImageView()
    }

    private fun rotateImageView() {
        // 2바퀴 회전 애니메이션 설정
        val rotateAnimation = RotateAnimation(
            0f, 720f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 4000
        rotateAnimation.fillAfter = true

        rotatingImage.startAnimation(rotateAnimation)
    }
}