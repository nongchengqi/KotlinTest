package com.evan.online.util

import com.evan.online.bean.MusicBean
import com.evan.online.bean.SearchBean
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object MusicUtils {
    private var searchSize = 20;
    private var searchKey = ""

    private val baseUrl = "https://v1.itooi.cn"

    fun search(path: String, listener: OnSearchFinishListener) {
        Thread {
            val url = URL(baseUrl + path)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.readTimeout = 8000
            conn.connectTimeout = 8000
            conn.doInput = true
            conn.doOutput=false
            conn.connect()
            val input = conn.inputStream
            val ouput = ByteArrayOutputStream()
            var buffer = ByteArray(1024)
            var length: Int
            while (true) {
                length = input.read(buffer)
                if (length == -1) break
                ouput.write(buffer, 0, length)
            }
            val searchBean = Gson().fromJson(ouput.toString("utf-8"), SearchBean::class.javaObjectType)
            if (searchBean == null || searchBean.data.isEmpty()) {
                listener.error("result is empty")
            } else {
                listener.success(searchBean.data)
            }

        }.start()
    }


    fun muliteSearch(key: String, resultListener: OnSearchFinishListener) {
        searchKey = URLEncoder.encode(key, "utf-8")
        val result = ArrayList<MusicBean>()
        var index = 0
        val listener = object : OnSearchFinishListener {
            override fun error(msg: String) {
                index++
                if (index == 5) {
                    resultListener.success(result)
                }

            }

            override fun success(data: List<MusicBean>) {
                result.addAll(data)
                index++
                if (index == 5) {
                    resultListener.success(result)
                }
            }
        }
        Thread {
            search("/netease/search?keyword=$searchKey&type=song&pageSize=$searchSize&page=0&format=1", listener)
            search("/tencent/search?keyword=$searchKey&type=song&pageSize=$searchSize&page=0&format=1", listener)
            search("/kugou/search?keyword=$searchKey&type=song&pageSize=$searchSize&page=0&format=1", listener)
            search("/kuwo/search?keyword=$searchKey&type=song&pageSize=$searchSize&page=0&format=1", listener)
            search("/migu/search?keyword=$searchKey&type=song&pageSize=$searchSize&page=0&format=1", listener)
        }.start()
    }


    interface OnSearchFinishListener {
        fun error(msg: String)
        fun success(data: List<MusicBean>)
    }

}