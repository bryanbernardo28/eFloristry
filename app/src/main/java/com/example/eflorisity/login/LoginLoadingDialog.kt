package com.example.eflorisity.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eflorisity.R

class LoginLoadingDialog(val description:String,val activity: Activity){
    private var isdialog:AlertDialog
    private var builder:AlertDialog.Builder

    init {
        builder = AlertDialog.Builder(activity)
        isdialog = builder.create()
    }

    fun startLoading(){
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.progress_dialog,null)
        val tvLoadingDescription:TextView = dialogView.findViewById(R.id.tv_pd_loading_id)
        tvLoadingDescription.setText(description)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }

    fun isShowing():Boolean{
        return isdialog.isShowing
    }

    fun dismissLoading(){
        isdialog.dismiss()
    }
}