package com.example.home33

import com.greenfrvr.hashtagview.HashtagView

open class StoryItem(var content: String, var date: String, var imageurl: String, var tag: ArrayList<String>) {

    fun setPostContent(content: String) {
        this.content = content
    }
    fun setPostDate(date: String) {
        this.date = date
    }
    fun setPostTag(tag: ArrayList<String>) {
        this.tag = tag
    }
}





