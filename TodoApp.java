// TodoApp.java
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the To-Do List application
 */
public class TodoApp {
    private TaskManager taskManager;
    private Scanner scanner;
    private AutoSaveThread saveThread;
    
    // Constructor
    public TodoApp() {
        taskManager = new TaskManager();
        scanner = new Scanner(System.in);
        saveThread = new AutoSaveThread(taskManager);
        saveThread.start();
    }
    
    // Display how many tasks we have - this method is annotated
    @TaskComplete
    public void showTaskCount() {
        System.out.println("Total tasks: " + taskManager.getAllTasks().size());
    }
    
    // Main method to run the application
    public void run() {
        boolean running = true;
        
        System.out.println("Welcome to my To-Do List App!");
        
        while (running) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Add a new task");
            System.out.println("2. Show all tasks");
            System.out.println("3. Mark a task as complete");
            System.out.println("4. Delete a task");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                addNewTask();
            } else if (choice.equals("2")) {
                showTasks();
            } else if (choice.equals("3")) {
                completeTask();
            } else if (choice.equals("4")) {
                deleteTask();
            } else if (choice.equals("5")) {
                running = false;
            } else {
                System.out.println("Invalid choice! Please try again.");
            }
        }
        
        // Save tasks and exit
        taskManager.saveTasks();
        System.out.println("Goodbye!");
    }
    
    // Method to add a new task
    private void addNewTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter priority (LOW, MEDIUM, HIGH): ");
        String priority = scanner.nextLine().toUpperCase();
        
        // Check if priority is valid
        if (!priority.equals("LOW") && !priority.equals("MEDIUM") && !priority.equals("HIGH")) {
            System.out.println("Invalid priority! Setting to MEDIUM.");
            priority = "MEDIUM";
        }
        
        Task task = new Task(title, description, priority);
        taskManager.addTask(task);
        
        System.out.println("Task added successfully!");
        showTaskCount();
    }
    
    // Method to show tasks
    private void showTasks() {
        System.out.println("\nWhat tasks do you want to see?");
        System.out.println("1. All tasks");
        System.out.println("2. Incomplete tasks");
        System.out.println("3. Completed tasks");
        System.out.print("Enter your choice: ");
        
        String choice = scanner.nextLine();
        ArrayList<Task> tasks;
        
        if (choice.equals("2")) {
            tasks = taskManager.getIncompleteTasks();
            System.out.println("\n----- INCOMPLETE TASKS -----");
        } else if (choice.equals("3")) {
            tasks = taskManager.getCompletedTasks();
            System.out.println("\n----- COMPLETED TASKS -----");
        } else {
            tasks = taskManager.getAllTasks();
            System.out.println("\n----- ALL TASKS -----");
        }
        
        if (tasks.isEmpty()) {
            System.out.println("No tasks to show.");
            return;
        }
        
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }
    
    // Method to mark a task as complete
    @TaskComplete("Task has been marked complete!")
    private void completeTask() {
        ArrayList<Task> tasks = taskManager.getAllTasks();
        
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        
        System.out.println("\n----- MARK TASK AS COMPLETE -----");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String status = task.isCompleted() ? " (already completed)" : "";
            System.out.println((i + 1) + ". " + task.getTitle() + status);
        }
        
        System.out.print("Enter task number to toggle completion (or 0 to cancel): ");
        try {
            int taskNum = Integer.parseInt(scanner.nextLine());
            
            if (taskNum == 0) {
                return;
            }
            
            if (taskNum > 0 && taskNum <= tasks.size()) {
                Task task = tasks.get(taskNum - 1);
                boolean newStatus = !task.isCompleted();
                taskManager.markTaskCompleted(taskNum - 1, newStatus);
                
                if (newStatus) {
                    System.out.println("Task marked as completed!");
                } else {
                    System.out.println("Task marked as not completed!");
                }
            } else {
                System.out.println("Invalid task number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number!");
        }
    }
    
    // Method to delete a task
    private void deleteTask() {
        ArrayList<Task> tasks = taskManager.getAllTasks();
        
        if (tasks.isEmpty()) {
            System.out.println("No tasks to delete.");
            return;
        }
        
        System.out.println("\n----- DELETE TASK -----");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).getTitle());
        }
        
        System.out.print("Enter task number to delete (or 0 to cancel): ");
        try {
            int taskNum = Integer.parseInt(scanner.nextLine());
            
            if (taskNum == 0) {
                return;
            }
            
            if (taskNum > 0 && taskNum <= tasks.size()) {
                if (taskManager.removeTask(taskNum - 1)) {
                    System.out.println("Task deleted!");
                    showTaskCount();
                } else {
                    System.out.println("Could not delete task.");
                }
            } else {
                System.out.println("Invalid task number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number!");
        }
    }
    
    // Main method
    public static void main(String[] args) {
        TodoApp app = new TodoApp();
        app.run();
    }
}
