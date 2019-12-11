package com.evan.online

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.evan.kotlintest.R
import com.evan.online.bean.MusicBean
import com.evan.online.util.MusicUtils

class MusicOnlineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_online)
        MusicUtils.muliteSearch("芒种",object : MusicUtils.OnSearchFinishListener{
            override fun error(msg: String) {
            }

            override fun success(data: List<MusicBean>) {
                Log.d("",data.toString())
            }

        })
    }
}
