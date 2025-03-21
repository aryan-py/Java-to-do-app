/ AutoSaveThread.java
/**
 * A thread that automatically saves tasks at regular intervals.
 */
public class AutoSaveThread extends Thread {
    private final TaskManager taskManager;
    private volatile boolean running = true;
    private static final int SAVE_INTERVAL_MS = 30000;  // 30 seconds
    
    public AutoSaveThread(TaskManager taskManager) {
        this.taskManager = taskManager;
        setDaemon(true);  // Set as daemon so it doesn't prevent JVM shutdown
        setName("AutoSaveThread");
    }
    
    @Override
    public void run() {
        System.out.println("Auto-save thread started");
        while (running) {
            try {
                Thread.sleep(SAVE_INTERVAL_MS);
                if (running) {
                    System.out.println("Auto-saving tasks...");
                    taskManager.saveTasks();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Auto-save thread stopping");
    }
    
    public void stopRunning() {
        this.running = false;
        interrupt();  // Interrupt sleep
    }
}
