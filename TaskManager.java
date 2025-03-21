// TaskManager.java
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manages the task list and provides operations like add, remove, and list tasks.
 */
public class TaskManager {
    private List<Task> tasks;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final String dataFile = "tasks.dat";
    private AutoSaveThread autoSaveThread;
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
        loadTasks();
        autoSaveThread = new AutoSaveThread(this);
        autoSaveThread.start();
    }
    
    /**
     * Adds a task to the list.
     * @param task The task to add
     */
    public void addTask(Task task) {
        lock.writeLock().lock();
        try {
            tasks.add(task);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Removes a task at the specified index.
     * @param index The index of the task to remove
     * @return true if the task was removed, false if the index is invalid
     */
    public boolean removeTask(int index) {
        lock.writeLock().lock();
        try {
            if (index >= 0 && index < tasks.size()) {
                tasks.remove(index);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Marks a task as completed or not completed.
     * @param index The index of the task
     * @param completed The completion status to set
     * @return true if the task was updated, false if the index is invalid
     */
    public boolean setTaskCompleted(int index, boolean completed) {
        lock.writeLock().lock();
        try {
            if (index >= 0 && index < tasks.size()) {
                tasks.get(index).setCompleted(completed);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Gets all tasks.
     * @return A list of all tasks
     */
    public List<Task> getAllTasks() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(tasks);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets only incomplete tasks.
     * @return A list of incomplete tasks
     */
    public List<Task> getIncompleteTasks() {
        lock.readLock().lock();
        try {
            List<Task> incompleteTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (!task.isCompleted()) {
                    incompleteTasks.add(task);
                }
            }
            return incompleteTasks;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets only completed tasks.
     * @return A list of completed tasks
     */
    public List<Task> getCompletedTasks() {
        lock.readLock().lock();
        try {
            List<Task> completedTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.isCompleted()) {
                    completedTasks.add(task);
                }
            }
            return completedTasks;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Saves all tasks to a file.
     */
    public void saveTasks() {
        lock.readLock().lock();
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
                oos.writeObject(tasks);
                System.out.println("Tasks saved to " + dataFile);
            } catch (IOException e) {
                System.err.println("Error saving tasks: " + e.getMessage());
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Loads tasks from a file.
     */
    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File(dataFile);
        if (!file.exists()) {
            System.out.println("No saved tasks found. Starting with an empty list.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            tasks = (List<Task>) ois.readObject();
            System.out.println("Loaded " + tasks.size() + " tasks from " + dataFile);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
        }
    }
    
    /**
     * Shuts down the auto-save thread.
     */
    public void shutdown() {
        if (autoSaveThread != null) {
            autoSaveThread.stopRunning();
            try {
                autoSaveThread.join(2000);  // Wait for the thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        saveTasks();  // Final save before shutting down
    }
}
