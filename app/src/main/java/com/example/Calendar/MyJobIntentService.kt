package com.example.Calendar

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.entity.Event
import com.example.Calendar.utility.DateStringConvertor
import java.util.*

class MyJobIntentService : JobIntentService() {
    internal var dbm = DataBaseManager(this)
    internal var dsc = DateStringConvertor(Calendar.getInstance())

    internal val mHandler = Handler()

    @SuppressLint("NewApi")
    override fun onHandleWork(intent: Intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        val NotiID = 1
        while (true) {
            val eventList1Day = getEvents(1440, 6)
            val eventList1Hour = getEvents(60, 5)
            val eventList30Min = getEvents(30, 4)
            val eventList15Min = getEvents(15, 3)
            val eventList10Min = getEvents(10, 2)
            val eventList5Min = getEvents(5, 1)
            val eventNow = getEvents(0, 0)

            eventList1Day.addAll(eventList1Hour)
            eventList1Day.addAll(eventList30Min)
            eventList1Day.addAll(eventList15Min)
            eventList1Day.addAll(eventList10Min)
            eventList1Day.addAll(eventList5Min)
            eventList1Day.addAll(eventNow)
            if (eventList1Day.isEmpty() == false) {
                sendNoti(applicationContext, eventList1Day, NotiID)

            }
            try {
                Thread.sleep(25000)// 1000 is one second, once a minute would be 60000
            } catch (e: InterruptedException) {
            }

        }
    }

    private fun getEvents(minute: Int, reminderType: Int): ArrayList<Event> {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        cal.time = Date()
        cal.add(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return dbm.getEventForNext30min(cal.timeInMillis, reminderType)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // Helper for showing tests
    internal fun toast(text: CharSequence) {
        mHandler.post { Toast.makeText(this@MyJobIntentService, text, Toast.LENGTH_SHORT).show() }
    }


    fun sendNoti(context: Context, events: ArrayList<Event>, notiID: Int) {
        var info = "error" //changed below.
        val myRandom = Random()
        val mManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //---PendingIntent to launch activity if the user selects
        // the notification---
        for (k in events.indices) {
            val notificationIntent = Intent(context, ViewEvent::class.java)
            notificationIntent.putExtra("ID", events[k].eventId)
            //random notification
            info = events[k].title
            notificationIntent.putExtra("mText", info)
            val contentIntent =
                PendingIntent.getActivity(context, k, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val sd = dsc.getDateToStringConversion(dsc.getDateFromMillis(events[k].startDate))
            val ed = dsc.getDateToStringConversion(dsc.getDateFromMillis(events[k].endDate))
            val duration: String
            if (sd.substring(0, 10) == ed.substring(0, 10)) {
                duration = sd.substring(0, 10) + " " + sd.substring(11, 19) + " to " + ed.substring(11, 19)
            } else {
                duration =
                    dsc.getDateToStringConversion(dsc.getDateFromMillis(events[k].startDate)) + "to" + dsc.getDateToStringConversion(
                        dsc.getDateFromMillis(
                            events[k].endDate
                        )
                    )
            }
            //create the notification
            val notif = NotificationCompat.Builder(context, "test_channel_01")
                .setSmallIcon(R.drawable.icon_calendar)
                .setWhen(System.currentTimeMillis()) //When the event occurred, now, since noti are stored by time.
                .setContentTitle(info) //Title message top row.
                .setContentText(duration) //message when looking at the notification, second row
                .setContentIntent(contentIntent) //what activity to open.
                .setChannelId("test_channel_01")
                .setAutoCancel(true) //allow auto cancel when pressed.
                .build() //finally build and return a Notification.
            //Show the notification
            mManager.notify(k, notif)  //and if we want different notifications, use notiID here instead of 1.
        }
    }

    companion object {
        internal val JOB_ID = 1000
        internal fun enqueueWork(context: Context, work: Intent) {
            JobIntentService.enqueueWork(context, MyJobIntentService::class.java, JOB_ID, work)
        }
    }


}
