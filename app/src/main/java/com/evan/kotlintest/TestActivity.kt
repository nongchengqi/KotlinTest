package com.evan.kotlintest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*

class TestActivity : BaseActivity() {
    var strokew = 0f
    var userStroke = false

    override fun setStatusbar(color: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        percentRectangleView.setTipArray("已用 xx%","剩余 xx%")

        testSize.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                percentRectangleView.setTestSizePercent(p1/100f).update()

            }

        })

        percent.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                percentRectangleView.setPercent(p1/100f).update()
            }

        })
        strokeW.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                strokew = p1.toFloat()
                if (userStroke)
                percentRectangleView.setStroke(p1.toFloat(),10f).update()
            }

        })

        stroke.setOnCheckedChangeListener { p0, p1 ->
            userStroke = p1
            if (p1){
                percentRectangleView.setStroke(strokew,10f).update()
            }else{
                percentRectangleView.setStroke(0f,10f).update()
            }
        }

        oneline.setOnCheckedChangeListener { p0, p1 ->
            if (p1){
                percentRectangleView.setTips("已用 xx% / 剩余 xx%").update()
            }else{
                percentRectangleView.setTips(null).setTipArray("已用 xx%","剩余 xx%").update()
            }
        }


    }


}
