package com.example.getlocationapp

import android.content.Context

class Logger private constructor(
    val context: Context,
    val file_name: String
){
    companion object {
        private var instance : Logger? = null
        fun getInstance(context: Context) : Logger {
            if (instance == null)
                instance = Logger(context,
                    context.getString(R.string.logger_file_name))
            return instance as Logger
        }
        fun clearInstance(){
            instance = null
        }
    }
}