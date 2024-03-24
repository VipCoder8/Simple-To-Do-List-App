package bee.corp.tasker;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.util.ArrayList;
public class TaskManager {
    private ArrayList<Task> tasks;
    private Context context;
    private ConstraintLayout mainLayout;
    public TaskSaver taskSaver;
    public TaskReader taskReader;
    private int x;
    private int newY;
    private int oldY;
    private int width;
    private int height;
    private int selectedIndex;
    public TaskManager(ConstraintLayout mainLayout,Context c, int x, int y, int w, int h){
        this.mainLayout = mainLayout;
        this.context=c;
        this.x=x;
        this.newY=y;
        this.oldY=newY;
        this.width=w;
        this.height=h;
        tasks = new ArrayList<>();
        taskSaver = new TaskSaver(c);
        taskReader = new TaskReader(c.getExternalFilesDir(null));
        readTasksFromFiles(mainLayout);
    }
    private void readTasksFromFiles(ConstraintLayout mainLayout) {
        try {
            taskReader.getTasks(this, mainLayout);
            updateListeners();
        } catch (IOException e) {throw new RuntimeException(e);}
    }
    public void addTaskSimple(String title, ImageButton addTask, int deleteImage, int editImage, int notificationImageOn, int notificationImageOff, boolean isReading){
        tasks.add(new Task(context,x,newY,width,height));
        tasks.get(tasks.size()-1).increasedID();
        tasks.get(tasks.size()-1).getTitleChanger().setVisibility(View.INVISIBLE);
        newY+=(this.height+5);
        tasks.get(tasks.size()-1).setTitle(title);
        tasks.get(tasks.size()-1).setDeleteButton(deleteImage);
        tasks.get(tasks.size()-1).setEditButton(editImage);
        tasks.get(tasks.size()-1).setNotificationButton(notificationImageOn, notificationImageOff);
        if(addTask != null) {
            addTask.setEnabled(true);
        }
        if(!isReading) {
            taskSaver.saveTask(tasks.get(tasks.size()-1));
        }
    }
    private void deleteTask(ConstraintLayout mainLayout, int index){
        if(index < tasks.size() && index > -1) {
            taskSaver.deleteTask(tasks.get(index));
            mainLayout.removeView(tasks.get(index));
            tasks.remove(index);
        }
        for (int i = index; i < tasks.size(); i++) {
            newY -= (height + 5);
            tasks.get(i).setY(newY - (height + 5));
        }
        if(tasks.size()==0){
            newY = oldY;
        }
    }
    public void updateListeners(){
        for(Task task : tasks) {
            Task selectedTask = task;
            task.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTask(mainLayout, tasks.indexOf(selectedTask));
                }
            });
        }
        for(int i = 0; i < tasks.size();i++){
            selectedIndex = i;
            tasks.get(i).getEditButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tasks.get(selectedIndex).editTitlePreparation();
                    tasks.get(selectedIndex).getTitleChanger().setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if(keyCode == KeyEvent.KEYCODE_ENTER){
                                tasks.get(selectedIndex).setTitleRaw(tasks.get(selectedIndex).getTitleChanger().getText().toString());
                                tasks.get(selectedIndex).onEditApply();
                            }
                            return true;
                        }
                    });
                }
            });
            tasks.get(i).addOnNotificationButtonClick(new Task.NotificationListener() {
                @Override
                public void onNotificationButtonClick() {
                    if((int)tasks.get(selectedIndex).getNotificationButton().getTag()==R.drawable.notifications_on){
                        tasks.get(selectedIndex).getNotificationButton().setImageResource(R.drawable.notifications_off);
                        tasks.get(selectedIndex).getNotificationButton().setTag(R.drawable.notifications_off);
                        taskSaver.editTask(tasks.get(selectedIndex));
                    } else if((int)tasks.get(selectedIndex).getNotificationButton().getTag()==R.drawable.notifications_off) {
                        tasks.get(selectedIndex).getNotificationButton().setImageResource(R.drawable.notifications_on);
                        tasks.get(selectedIndex).getNotificationButton().setTag(R.drawable.notifications_on);
                    }
                }
            });
        }
    }
    public ArrayList<Task> getTasks(){
        return tasks;
    }
}
