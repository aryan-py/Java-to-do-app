// AutoSaveThread.java
/**
 * This class creates a thread that automatically saves tasks
 */

// TaskComplete annotation
import java.lang.annotation.*;


public class AutoSaveThread extends Thread {
    private TaskManager taskManager;
    private boolean running;
    
    // Constructor
    public AutoSaveThread(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.running = true;
    }
    
    // Run method that executes when the thread starts
    public void run() {
        System.out.println("Auto-save thread started");
        while (running) {
            try {
                // Sleep for 30 seconds
                Thread.sleep(30000);
                
                // Save tasks
                System.out.println("Auto-saving tasks...");
                taskManager.saveTasks();
            } catch (InterruptedException e) {
                System.out.println("Auto-save thread interrupted");
                break;
            }
        }
    }
    
    // Stop the thread
    public void stopThread() {
        this.running = false;
        this.interrupt();
    }
}


/**
 * Custom annotation for marking methods related to task completion
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface TaskComplete {
    String value() default "Task completed";
}
