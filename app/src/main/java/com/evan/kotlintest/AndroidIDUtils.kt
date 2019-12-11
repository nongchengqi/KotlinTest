package com.evan.kotlintest

import android.content.Context
import android.os.Environment
import android.provider.Settings
import java.io.*

object AndroidIDUtils {
    private val filePath = Environment.getExternalStorageDirectory().toString() + "/jmad/android_id.data"
    private val idSpliterator = "#_#"

    fun getAndroidId(context: Context, prdId: String): String {
        val file = File(filePath)
        if (file.exists()) {
            val br = BufferedReader(FileReader(file))
            while (true) {
                val line = br.readLine() ?: break
                if (line.startsWith(prdId)) {
                    return line.split(idSpliterator)[1]
                }
            }
            br.close()
        } else {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        val id = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        val bw = BufferedWriter(FileWriter(file, true))
        bw.newLine()
        bw.write("$prdId$idSpliterator$id")
        bw.flush()
        bw.close()
        return id
    }

}