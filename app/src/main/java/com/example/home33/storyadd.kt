package com.example.home33

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Suppress("DEPRECATION")
class storyadd : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    var imagePath: String? = null
    var photouri : Uri? = null
    var photoResult = registerForActivityResult(StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            photouri = it.data?.data

        }
    }

    private fun saveBitmapToCache(bitmap: Bitmap) {
        val file = File(cacheDir, "cached_image.jpg")

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // 이미지의 경로(file.absolutePath)를 사용하면 됩니다.
            val imagePath = file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun tag(editText: EditText) :ArrayList<String>{
        val inputText = editText.text.toString()
        var main_result = inputText

        var copy = "$inputText "		// 끝에 공백 추가 안하면 인덱스 벗어남;;
        val span: Spannable = main_result as Spannable
        val result = ArrayList<String>()		// 태그값을 저장할 배열
        var count = 0		// 태그 갯수
        var startIndex = 0		// 각 태그의 시작 인덱스를 저장할 변수

        for (element in copy) {		// 1
            if (element == '#') count++
        }

        for (i in 0 until count) {		// 2
            for (j in copy) startIndex = copy.indexOf('#')		// 3

            try {
                result.add(		// 4
                    copy.substring(
                        startIndex,
                        (copy.substring(startIndex).indexOf(" ") + startIndex)
                    )
                )

                // 5
                copy = copy.substring((copy.substring(startIndex).indexOf(" ") + startIndex) + 1)
            } catch (e: Exception) {
                println(e.message)
                Toast.makeText(this, "#를 붙이지 마세요", Toast.LENGTH_LONG).show()
            }
        }

        for (i in result) {		// 태그만 가지고 스팬 적용
            span.setSpan(
                RelativeSizeSpan(1.5f),
                main_result.toString().indexOf(i),
                main_result.toString().indexOf(i) + i.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return result
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.storyadd)
        var btnBack2 = findViewById<ImageButton>(R.id.Storyadd_ImageButton_back)
        var Image1 = findViewById<ImageView>(R.id.Storyadd_ImageView_image)
        var comment = findViewById<EditText>(R.id.Storyadd_EditText_comment)
        var viewFlipper = findViewById<ViewFlipper>(R.id.Storyadd_ViewFlipper_viewflipper)
        var next = findViewById<Button>(R.id.Storyadd_Button_next)
        var calendar1 = findViewById<CalendarView>(R.id.Storyadd_CalendarView_calendar)
        var time1 = findViewById<TimePicker>(R.id.Storyadd_TimePicker_time)
        var addtag = findViewById<EditText>(R.id.add_tag)
        var previous = findViewById<Button>(R.id.Storyadd_Button_previous)

        btnBack2.setOnClickListener {
                finish()
        }

        next.setOnClickListener {
            if (viewFlipper.displayedChild != viewFlipper.childCount-1) {
                viewFlipper.showNext()
                if (viewFlipper.displayedChild != 0) {
                    previous.visibility = VISIBLE
                }
                if (viewFlipper.displayedChild == viewFlipper.childCount-1) {
                    next.text = "confirm"
                }
            }
            else {
                //이부분에 DB에다 데이터 전송
                try {
                    //이미지 캐싱
                    Glide.with(this)
                        .asBitmap()
                        .load(photoResult)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?) {
                                // 이미지가 로드되면 캐시 파일로 저장
                                saveBitmapToCache(resource)
                            }
                        })
                    val dpHelper = DBHelper(this)
                    //여기에 dbHelper 메소드 삽입
                    val item = StoryItem(comment.toString(), calendar1.date.toString(), imagePath, tag(addtag))
                    dpHelper.insertStory(item)
                    finish()
                } catch (e: Exception) {
                    var errMsg = Toast.makeText(applicationContext, "모든 데이터를 기재해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        previous.setOnClickListener {
            viewFlipper.showPrevious()
            if (viewFlipper.displayedChild == 0) {
                previous.visibility = INVISIBLE
            }
            if (viewFlipper.displayedChild != viewFlipper.childCount-1) {
                next.text = "next"
            }
        }

        Image1.setOnClickListener {
            val photoPickerintent = Intent(Intent.ACTION_PICK)
            photoPickerintent.type = "image/*"
            photoResult.launch(photoPickerintent)

        }
    }
}