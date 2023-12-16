package com.example.home33

import android.util.Log

    open class StoryItem(var content: String? = null, var date: String? = null, var imageurl: String? = null, var tag: ArrayList<String>? = null) {
        fun setPostTag(t: ArrayList<String>) {
            Log.d("t", "매개변수 tag = " + t.toString())
            Log.d("o", "원래 객체변수 tag = " + tag.toString())
            if(tag != null) tag?.clear()
            tag?.addAll(t)
            Log.i("초", "StoryItem에서 tag 초기화 했어요" + tag.toString())
        }

        fun printStoryItem() {
            Log.i("print", "printStoryItem 실행!")
            Log.d("story", "content : " + content)
            Log.d("story1", "date : " + date)
            Log.d("imageurl", "imageurl : " + imageurl)
            if(this.tag == null) Log.d("tagnull", "tag가 null이네요")
            else {
                Log.d("tag", "tag : " + this.tag.toString())
            }
        }
    }





