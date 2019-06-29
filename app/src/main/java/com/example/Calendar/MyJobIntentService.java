package com.example.Calendar;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import com.example.Calendar.dataBase.DataBaseManager;
import com.example.Calendar.entity.Event;
import com.example.Calendar.utility.DateStringConvertor;

import java.util.*;

/**
 * Likely I should have created an infinite process here.  but it seems to work.
 */
public class MyJobIntentService extends JobIntentService {
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;
    DataBaseManager dbm = new DataBaseManager(this);
    DateStringConvertor dsc= new DateStringConvertor(Calendar.getInstance());

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, work);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onHandleWork(Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        int NotiID = 1;
        while (true) {





            ArrayList<Event> eventList1Day = getEvents(1440, 6);
            ArrayList<Event> eventList1Hour = getEvents(60, 5);
            ArrayList<Event> eventList30Min = getEvents(30, 4);
            ArrayList<Event> eventList15Min = getEvents(15, 3);
            ArrayList<Event> eventList10Min = getEvents(10, 2);
            ArrayList<Event> eventList5Min = getEvents(5, 1);
            ArrayList<Event> eventNow=getEvents(0,0);

            eventList1Day.addAll(eventList1Hour);
            eventList1Day.addAll(eventList30Min);
            eventList1Day.addAll(eventList15Min);
            eventList1Day.addAll(eventList10Min);
            eventList1Day.addAll(eventList5Min);
            eventList1Day.addAll(eventNow);


            //ArrayList<Event> events = Stream.of(eventList1Day, eventList1Hour,eventList5Min,eventList10Min,eventList15Min,eventList30Min).flatMap(x -> x.stream()).collect(Collectors.toList());
            if (eventList1Day.isEmpty() == false) {
                // for(int i=0;i<events.size()-1;i++){
                sendNoti(getApplicationContext(), eventList1Day, NotiID);
                // }

            }
            Log.wtf("JobSheduler", "send notification");
            try {
                Thread.sleep(10000);// 1000 is one second, once a minute would be 60000
            } catch (InterruptedException e) {
            }
        }
    }

    private ArrayList<Event> getEvents(int minute, int reminderType) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        ArrayList<Event> events = dbm.getEventForNext30min(cal.getTimeInMillis(), reminderType);
        return events;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //toast("All work complete");
    }

    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void sendNoti(Context context, ArrayList<Event> events, int notiID) {
        // ArrayList<Notification> notificationList = new ArrayList<Notification>();
        String info = "error"; //changed below.
        Random myRandom = new Random();
        NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //---PendingIntent to launch activity if the user selects
        // the notification---
        for (int k = 0; k < events.size(); k++) {
            Intent notificationIntent = new Intent(context, ViewEvent.class);
            notificationIntent.putExtra("ID", events.get(k).getEventId());
            //random notification
            info = events.get(k).getTitle();
            notificationIntent.putExtra("mText", info);

            PendingIntent contentIntent = PendingIntent.getActivity(context, k, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            String sd = dsc.getDateToStringConversion(dsc.getDateFromMillis(events.get(k).getStartDate()));
           String ed = dsc.getDateToStringConversion(dsc.getDateFromMillis(events.get(k).getEndDate()));
           String duration;
            if (sd.substring(0, 10).equals(ed.substring(0, 10))) {
                duration=sd.substring(0, 10) + " " + sd.substring(11, 19) + " to " + ed.substring(11, 19);
            } else {
                duration=dsc.getDateToStringConversion(dsc.getDateFromMillis(events.get(k).getStartDate())) + "to" + dsc.getDateToStringConversion(dsc.getDateFromMillis(events.get(k).getEndDate()));
        }
            //create the notification
            Notification notif = new NotificationCompat.Builder(context, "test_channel_01")
                    .setSmallIcon(R.drawable.icon_calendar)
                    .setWhen(System.currentTimeMillis()) //When the event occurred, now, since noti are stored by time.
                    .setContentTitle(info) //Title message top row.
                    .setContentText(duration) //message when looking at the notification, second row
                    .setContentIntent(contentIntent) //what activity to open.
                    .setChannelId("test_channel_01")
                    .setAutoCancel(true) //allow auto cancel when pressed.
                    .build(); //finally build and return a Notification.
            //Show the notification
            mManager.notify(k, notif);  //and if we want different notifications, use notiID here instead of 1.
        }
    }


}