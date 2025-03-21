// TaskManager.java
import java.io.*;
import java.util.*;

/**
 * This class manages the task list and handles saving/loading tasks
 */
public class TaskManager {
    private ArrayList<Task> tasks;
    private String fileName = "tasks.dat";
    
    // Constructor
    public TaskManager() {
        tasks = new ArrayList<Task>();
        loadTasks();
        
        // Start the auto save thread
        AutoSaveThread saveThread = new AutoSaveThread(this);
        saveThread.start();
    }
    
    // Add a task to the list
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    // Remove a task from the list
    public boolean removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            return true;
        }
        return false;
    }
    
    // Mark a task as completed or not completed
    public boolean markTaskCompleted(int index, boolean completed) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).setCompleted(completed);
            return true;
        }
        return false;
    }
    
    // Get all tasks
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }
    
    // Get only incomplete tasks
    public ArrayList<Task> getIncompleteTasks() {
        ArrayList<Task> incompleteTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                incompleteTasks.add(task);
            }
        }
        return incompleteTasks;
    }
    
    // Get only completed tasks
    public ArrayList<Task> getCompletedTasks() {
        ArrayList<Task> completedTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }
    
    // Save tasks to file
    public void saveTasks() {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tasks);
            out.close();
            fileOut.close();
            System.out.println("Tasks saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    // Load tasks from file
    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("No saved tasks found. Starting with an empty list.");
            return;
        }
        
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tasks = (ArrayList<Task>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Loaded " + tasks.size() + " tasks from " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<Task>();
        }
    }
}
