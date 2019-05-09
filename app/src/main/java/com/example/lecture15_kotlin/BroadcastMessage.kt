package com.example.lecture15_kotlin

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_broadcast_message.*

class BroadcastMessage : AppCompatActivity() {
    private var mHandler = Handler()
    private var phone_number_sender: String? =null
    private var message_sender: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast_message)

        Thread(object : Runnable {
            override fun run() {
                while (true) {
                    try {
                        Thread.sleep(5000)
                        mHandler.post(object : Runnable {
                            override fun run() {
                                display_messages!!.setText(messages)
                            }
                        })
                    } catch (e: InterruptedException) {
                        Log.i(TAG_APP, e.toString())
                        e.printStackTrace()
                    }

                }
            }
        }).start()
    }

    fun send_message(view : View){
        phone_number_sender = sender_phone_et.text.toString().trim()
        message_sender = send_message_et.text.toString().trim()

        try {
            if (ActivityCompat.checkSelfPermission(
                    this@BroadcastMessage,
                    Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@BroadcastMessage,
                    arrayOf<String>(Manifest.permission.SEND_SMS),
                    REQUEST_CODE
                )
            } else {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phone_number_sender, null, message_sender, null, null)
                Toast.makeText(this@BroadcastMessage, "Send", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: IllegalArgumentException) {
            Log.i(TAG_APP, ex.toString())
        }

    }

    class SmsReceiver : BroadcastReceiver() {
        private var smsManager = SmsManager.getDefault()
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras

            try {
                val pduObject = bundle!!.get("pdus") as Array<Any>

                for (i in pduObject.indices) {
                    val smsMessage = SmsMessage.createFromPdu(pduObject[i] as ByteArray)
                    val receiver_num = smsMessage.getDisplayOriginatingAddress()
                    val receiver_msg = smsMessage.getDisplayMessageBody()

                    messages = "${messages} : $receiver_num : $receiver_msg\n"
                }
            } catch (e: Exception) {
                Log.i(TAG_APP, e.toString())
            }

        }
    }

    protected override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    companion object {
        private val REQUEST_CODE = 7
        private val TAG_APP = "Error"
        private var messages = ""
    }
}
