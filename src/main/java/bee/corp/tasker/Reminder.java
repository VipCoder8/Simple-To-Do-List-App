package bee.corp.tasker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Reminder {
    private Task task;
    private NotificationSender nSender;
    private Thread waitingThread;
    private Date currentTime;
    private boolean checking = true;
    public Reminder(Task t){
        this.task = t;
        nSender = new NotificationSender(t.getContext());
        currentTime = new Date();
        waitingThread = new Thread(this::waitDate);
    }
    public void startWaiting(){
        waitingThread.start();
    }
    public void updateCurrentTime(){
        currentTime = null;
        currentTime = new Date();
    }
    public void setState(int s){
        if(s==States.ACTIVE) {
            checking = true;
        } else if(s==States.CANCELED){
            checking = false;
        }
    }
    private void waitDate(){
        while(checking){
            updateCurrentTime();
            if(currentTime.after(task.getTargetRemindTime())){
                nSender.sendNotification("Task Reminder", task.getTitle());
                checking = false;
            }
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {throw new RuntimeException(e);}
        }
    }
}
