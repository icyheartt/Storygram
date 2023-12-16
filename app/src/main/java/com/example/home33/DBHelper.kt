//StoryActivity의 DBHelper
package com.example.home33

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    var mContext: Context? = context

    init {
        Log.d("con", "db init 실행!!")
        DB_PATH = "/data/data/" + context?.packageName + "/databases/"
        this.mContext = context;
        dataBaseCheck()
    }

    private fun dataBaseCheck() {
        Log.d("co4", "db BaseCheck 실행!!")
        val dbFile = File(DB_PATH + DB_NAME)
        if (dbFile.exists()) {
            dbCopy()
            Log.d(TAG, "Database is copied.")
        }
        else {
            Log.d(TAG, "Database is not copied!!!!!!! 왜 ")
        }
    }

    private fun dbCopy() {
        try {
            Log.d("on", "db copy 실행!!")
            val folder = File(DB_PATH)
            if (!folder.exists()) {
                folder.mkdir()
            }
            val inputStream = mContext!!.assets.open(DB_NAME)
            val out_filename = DB_PATH + DB_NAME
            val outputStream: OutputStream = FileOutputStream(out_filename)
            val mBuffer = ByteArray(1024)
            var mLength: Int
            while (inputStream.read(mBuffer).also { mLength = it } > 0) {
                outputStream.write(mBuffer, 0, mLength)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("dbCopy", "IOException 발생함")
        }
    }


    override fun onCreate(db: SQLiteDatabase) { //DBHelper가 데이터베이스가 처음 생성되었을때에 대한 상태값을 가지고 올 수 있다
        // 데이터베이스가 생성되었을때 호출
        // 데이터베이스 -> 테이블 -> 컬럼 -> 값
        Log.d("con", "db oncreate 실행!!")

        db.execSQL("CREATE TABLE IF NOT EXISTS HashtagDB (id INTEGER, name TEXT);")
        db.execSQL("CREATE TABLE IF NOT EXISTS PostDB (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, date TEXT NOT NULL, imageurl TEXT);") //sql쿼리문
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        //Toast.makeText(mContext,"onOpen()",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onOpen() : DB Opening!")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS HashtagDB;")
        db.execSQL("DROP TABLE IF EXISTS PostDB;")

        onCreate(db)
    }

    @SuppressLint("Range")
    fun getStoryItem(): ArrayList<StoryItem> {
        // val storyList: ArrayList<StoryItem> get() {
        // SELECT 문 (할일 목록을 조회)
        val storyItems: ArrayList<StoryItem> = ArrayList<StoryItem>()
        val tagList = Array(10, {""})
        val db = this.writableDatabase
        //ORDER BY date DESC
        var cursor = db.rawQuery("SELECT * FROM PostDB ORDER BY id ASC;", null)
        var cursor_cnt = db.rawQuery("SELECT COUNT (*) FROM HashtagDB;", null)
        var cnt = cursor_cnt.count
        var i = 0
        //var cursor1: Cursor? = null
        if (cursor.count != 0) {
            // 조건문 데이터가 있을때 내부 수행
            while (cursor.moveToNext()) {
                var id = cursor.getInt(cursor.getColumnIndex("id"))
                var content = cursor.getString(cursor.getColumnIndex("content"))
                var writeDate = cursor.getString(cursor.getColumnIndex("date"))
                var imageurl = cursor.getString(cursor.getColumnIndex("imageurl"))
                Log.d("cusor1", "왜1!!!! cursor " + cursor.getColumnIndex("id"))
                Log.d("cusor1", "왜값! cursor " + cursor.getInt(cursor.getColumnIndex("id")))

                var cursor1 =
                    db.rawQuery("SELECT * FROM HashtagDB WHERE id = '$id';", null)
                var idx1 = cursor1.getColumnIndex("name")
                Log.d("str1", "cursor1 name컬럼 인덱스 : " + idx1)
                var mtf = cursor1.moveToFirst()
                Log.d("mtf", "cursor1 movetofirst" + mtf)
                var value =  cursor1.getString(idx1)


                Log.d("str2", "cursor1 name컬럼 인덱스에 있는 값 : " + value)
                var str : String = cursor1.getColumnName(idx1)
                Log.d("str3", "cursor1 컬럼 이름 : " + str)
                Log.d("cor1", "갸아악 cursor1 " + cursor1.getString(cursor1.getColumnIndex(str)))
                Log.d("cu", "으아아 id 컬럼 인덱스 cursor1 " + cursor1.getColumnIndex("id"))
                Log.d("cursor1", "cursor1 커서 개수 " + cursor1.count)


                var i = 0
                var close = 0
                if (cursor1 != null && cursor1.moveToFirst()) {
                    while(cursor1.moveToNext()) {
                        var str : String = cursor1.getColumnName(cursor1.getColumnIndex("id"))
                        Log.d("str", "cursor1 컬럼 이름 : " + str)
                        var idx = cursor1.getColumnIndex("name")
                        Log.d("cusor1", "인덱스값! cursor1 " + idx)
                        Log.d("tag", "인덱스로부터 name string 가져오기 " + cursor1.getString(idx))
                        var tag = cursor1.getString(idx)
                        Log.d("i", "cursor1 i 값 출력 : " + i)
                        Log.d("i[", "cursor1 i인덱스 array값 출력 " +  cursor1.getString(idx))

                        i++
                        try {
                            tagList[i] = cursor1.getString(idx)
                        } catch (e: ArrayIndexOutOfBoundsException) {
                            Log.d("er","Error: the array is empty!")
                        }
                    }
                }
                var tags : ArrayList<String> = arrayListOf<String>()
                for(tag in tagList) {
                    tags.add(tag)
                }

                Log.d("storyItem", "storyitem 내용물" + content + " " + writeDate + " " + imageurl)
                var storyItem = StoryItem(content, writeDate, imageurl, tags)
                storyItem?.setPostContent(content)
                storyItem?.setPostDate(writeDate)
                storyItem?.setPostTag(tags)
                storyItems.add(storyItem)

                tags.clear()
                close++
                if(close == cnt) cursor1.close()
            }
        }

        cursor.close()

        return storyItems
    }

    fun insertStory(storyItem : ArrayList<StoryItem>) {
        val db : SQLiteDatabase = this.writableDatabase
        var cursor = db.rawQuery("select id from PostDB ORDER BY id DESC LIMIT 1;", null)

        db.beginTransaction()
        try {
            for(story in storyItem) {
                var content: String? = story.content
                var date: String? = story.date
                var imageurl: String? = story.imageurl
                var tags: ArrayList<String>? = story.tag

                var values: ContentValues = ContentValues()
                values.put("imageurl", imageurl)
                values.put("date", date)
                values.put("content", content)
                var id = db.insert(TABLE_POST, null, values)

                if (tags != null) {
                    for (tag in tags) {
                        var value: ContentValues = ContentValues()
                        value.put("name", tag)
                        value.put("id", id)
                        db.insert(TABLE_HASH, null, value)
                    }
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        db.close()
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "DB2.db"
        private const val TABLE_POST = "PostDB"
        private const val TABLE_HASH = "HashtagDB"
        private var DB_PATH = ""

    }
}
