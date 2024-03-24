package bee.corp.tasker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TaskSaver {
    private File androidFolder;
    private File savedTaskFile;
    BufferedWriter writer;
    public TaskSaver(Context c) {
        androidFolder = new File(c.getExternalFilesDir(null).getPath());
        Log.v("stringpath", androidFolder.getPath());
        if(!androidFolder.exists()) {
            if(androidFolder.mkdir()) {
                Log.v("Folder", "nice");
            } else {
                Log.v("Folder", "not nice");
            }
        }
    }
    public void saveTask(Task t) {
        savedTaskFile = new File(androidFolder.getPath() +"/" + t.getTitle() + "." + t.getTargetRemindTime().getHours() + "." + t.getTargetRemindTime().getMinutes() + "." + t.getState() + "." + t.getID() + ".tsk");
        if(!savedTaskFile.exists()) {
            try {
                savedTaskFile.createNewFile();
                writer = new BufferedWriter(new FileWriter(savedTaskFile));
                writer.write(t.getTitle() + ":" + t.getTargetRemindTime().getHours() + ":" + t.getTargetRemindTime().getMinutes() + ":" + t.getState());
                writer.flush();
            } catch (IOException e) {throw new RuntimeException(e);}
        }
    }
    public void editTask(Task t) {
        if(!savedTaskFile.exists()) {
            try {
                writer = new BufferedWriter(new FileWriter(savedTaskFile));
                writer.write(t.getTitle() + ":" + t.getTargetRemindTime().getHours() + ":" + t.getTargetRemindTime().getMinutes() + ":" + t.getState());
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void deleteTask(Task t) {
        File toDelete = new File(androidFolder.getPath() +"/" + t.getTitle() + "." + t.getTargetRemindTime().getHours() + "." + t.getTargetRemindTime().getMinutes() + "." + t.getState() + "." + t.getID() + ".tsk");
        if(toDelete.exists()) {
            toDelete.delete();
        }
    }
}
