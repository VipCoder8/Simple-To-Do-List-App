package bee.corp.tasker;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TaskReader {
    File mainFolder;
    public TaskReader(File where) {
        mainFolder = where;
    }
    public Task[] getTasks(TaskManager manager, ConstraintLayout mainLayout) throws IOException {
        File[] allFiles = mainFolder.listFiles();
        Task[] tasks = new Task[allFiles.length];
        for(int i = 0; i < allFiles.length;i++) {
            BufferedReader br = new BufferedReader(new FileReader(allFiles[i]));
            String[] values = br.readLine().split(":");
            manager.addTaskSimple(values[0], null, R.drawable.delete, R.drawable.edit, R.drawable.notifications_on, R.drawable.notifications_off, true);
            manager.getTasks().get(manager.getTasks().size()-1).setTargetRemindTime(Integer.valueOf(values[1]), Integer.valueOf(values[2]), 0);
            manager.getTasks().get(manager.getTasks().size()-1).getReminder().startWaiting();
            mainLayout.addView(manager.getTasks().get(manager.getTasks().size()-1));
        }
        return tasks;
    }
    public File[] readFiles() {
        return mainFolder.listFiles();
    }
}
