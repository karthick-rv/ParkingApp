package com.example.parkingapp.feature_fee_collection.domain.util

import android.app.AlertDialog
import android.content.Context

object DialogUtil {

    fun create(context: Context, title:String, message: String, positiveMsg: String, positiveLambda: ()->Unit): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveMsg) { dialog, _ ->
            dialog.dismiss()
            positiveLambda()
        }
        return builder.create()
    }

}