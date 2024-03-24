package bee.corp.tasker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

@SuppressLint("ViewConstructor")
public class Task extends FrameLayout {
    private int state;
    private int id;
    private final Date targetRemindTime;
    private Reminder reminder;
    private TextView title;
    private EditText titleChanger;
    private ImageButton deleteButton;
    private ImageButton notificationButton;
    private ImageButton editButton;
    private ImageButton applyButton;
    public Task(Context c, int x,int y,int w,int h){
        super(c);
        setX(x);
        setY(y);
        setBackgroundResource(R.drawable.rounding);
        setLayoutParams(new FrameLayout.LayoutParams(w,h));
        targetRemindTime = new Date();
        title = new TextView(c);
        title.setTextColor(Color.BLACK);
        deleteButton = new ImageButton(c);
        deleteButton.setLayoutParams(new FrameLayout.LayoutParams(50,50));
        deleteButton.setBackground(null);
        notificationButton = new ImageButton(c);
        notificationButton.setLayoutParams(new FrameLayout.LayoutParams(50,50));
        notificationButton.setBackground(null);
        editButton = new ImageButton(c);
        editButton.setLayoutParams(new FrameLayout.LayoutParams(50,50));
        editButton.setBackground(null);
        titleChanger = new EditText(c);
        titleChanger.setLayoutParams(new FrameLayout.LayoutParams(w-15,h));
        titleChanger.setTextSize(12);
        titleChanger.setInputType(InputType.TYPE_CLASS_TEXT);
        titleChanger.setX(10);
        titleChanger.setY(1);
        titleChanger.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(247, 206, 215)));
        titleChanger.setVisibility(View.INVISIBLE);
        applyButton = new ImageButton(c);
        applyButton.setLayoutParams(new FrameLayout.LayoutParams(50,50));
        this.addView(titleChanger);
        reminder = new Reminder(this);
    }
    public Date getTargetRemindTime() {return this.targetRemindTime;}
    public void setTargetRemindTime(int hours, int minutes, int seconds) {
        this.targetRemindTime.setHours(hours);
        this.targetRemindTime.setMinutes(minutes);
        this.targetRemindTime.setSeconds(seconds);
    }
    public Reminder getReminder(){
        return reminder;
    }
    public EditText getTitleChanger() {return titleChanger;}
    public String getTitle() {return title.getText().toString();}
    public void setTitleRaw(String t){
        title.setText(t);
    }
    public void setTitle(String t) {
        title.setText(t);
        title.setX(10);
        title.setY(10);
        if(t.length()>19){
            title.setText(title.getText().toString().substring(0,19)+"...");
        }
        this.addView(title);
    }
    public ImageButton getDeleteButton(){return deleteButton;}
    public ImageButton getEditButton(){return editButton;}
    public ImageButton getNotificationButton() {return notificationButton;}
    public int getState() {return state;}
    public void setState(int state) {this.state = state;}
    public void setDeleteButton(int image){
        deleteButton.setImageResource(image);
        deleteButton.setX(getLayoutParams().width/2f+50);
        deleteButton.setY(10);
        this.addView(deleteButton);
    }
    public void setEditButton(int image){
        editButton.setImageResource(image);
        editButton.setX(deleteButton.getX()+50);
        editButton.setY(deleteButton.getY());
        this.addView(editButton);
    }
    public void setNotificationButton(int imageOn, int imageOff){
        notificationButton.setImageResource(imageOn);
        notificationButton.setTag(imageOn);
        notificationButton.setX(editButton.getX()+50);
        notificationButton.setY(editButton.getY());
        notificationButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if((int)notificationButton.getTag()==imageOn){
                    notificationButton.setImageResource(imageOff);
                    notificationButton.setTag(imageOff);
                    setState(States.CANCELED);
                    reminder.setState(state);
                } else if((int)notificationButton.getTag()==imageOff){
                    notificationButton.setImageResource(imageOn);
                    notificationButton.setTag(imageOn);
                    setState(States.ACTIVE);
                    reminder.setState(state);
                }
            }
        });
        this.addView(notificationButton);
    }
    public void addOnNotificationButtonClick(NotificationListener listener) {
        notificationButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                listener.onNotificationButtonClick();
            }
        });
    }
    public int getID(){
        return id;
    }
    public void increasedID(){
        id++;
    }
    public void setApplyButton(int image){
        applyButton.setImageResource(image);
        applyButton.setX(notificationButton.getX());
        applyButton.setY(notificationButton.getY());
        applyButton.setVisibility(View.INVISIBLE);
        this.addView(applyButton);
    }
    public void editTitlePreparation(){
        title.setVisibility(View.INVISIBLE);
        notificationButton.setVisibility(View.INVISIBLE);
        editButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        titleChanger.setVisibility(View.VISIBLE);
    }
    public void onEditApply(){
        title.setVisibility(View.VISIBLE);
        notificationButton.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        titleChanger.setVisibility(View.INVISIBLE);
    }
    public interface NotificationListener {
        void onNotificationButtonClick();
    }
}