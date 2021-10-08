package com.example.eflorisity.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eflorisity.R

class LoginLoadingDialog(val description:String,val activity: Activity){
    private lateinit var isdialog:AlertDialog
    fun startLoading(){
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.progress_dialog,null)
        val builder = AlertDialog.Builder(activity)
        val tvLoadingDescription:TextView = dialogView.findViewById(R.id.tv_pd_loading_id)
        tvLoadingDescription.setText(description)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }

    fun dismissLoading(){
        isdialog.dismiss()
    }
}