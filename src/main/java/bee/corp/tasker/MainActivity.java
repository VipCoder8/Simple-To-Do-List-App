package bee.corp.tasker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;
public class MainActivity extends AppCompatActivity {
    ConstraintLayout mainLayout;
    TaskManager taskManager;
    ImageButton addTask;
    Calendar calendar;
    EditText titleText;
    EditText addTime;
    Button addTaskFromForm;
    FrameLayout addTaskForm;
    TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        registerListeners();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        taskManager = new TaskManager(mainLayout, getApplicationContext(), 10,25, 400, 70);
        calendar = new GregorianCalendar();
    }
    public void setupViews(){
        mainLayout = findViewById(R.id.MainLayout);
        addTask = findViewById(R.id.addTask);
        titleText = findViewById(R.id.titleText);
        addTaskForm = findViewById(R.id.addTaskForm);
        addTime = findViewById(R.id.addTime);
        addTaskFromForm = findViewById(R.id.add);
    }
    @SuppressLint("ClickableViewAccessibility")
    public void registerListeners(){
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask.setEnabled(false);
                addTaskForm.setVisibility(View.VISIBLE);
            }
        });
        addTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        addTime.setText(hourOfDay + ":" + minute);
                        timePickerDialog.hide();
                    }
                };
                timePickerDialog = new TimePickerDialog(MainActivity.this,timeListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),true);
                timePickerDialog.show();
                return true;
            }
        });
        addTaskFromForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskForm.setVisibility(View.INVISIBLE);
                taskManager.addTaskSimple(titleText.getText().toString(), addTask, R.drawable.delete, R.drawable.edit, R.drawable.notifications_on, R.drawable.notifications_off, false);
                taskManager.getTasks().get(taskManager.getTasks().size()-1).setTargetRemindTime(Integer.valueOf(addTime.getText().toString().split(":")[0]), Integer.valueOf(addTime.getText().toString().split(":")[1]), 0);
                Log.v("Another time", taskManager.getTasks().get(taskManager.getTasks().size()-1).getTargetRemindTime().toString());
                taskManager.updateListeners();
                mainLayout.addView(taskManager.getTasks().get(taskManager.getTasks().size()-1));
                taskManager.getTasks().get(taskManager.getTasks().size()-1).getReminder().startWaiting();
                addTime.setText("Add Time");
                titleText.setText("");
            }
        });
    }
}