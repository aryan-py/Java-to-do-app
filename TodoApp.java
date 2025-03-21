// TodoApp.java
import java.util.List;
import java.util.Scanner;

/**
 * Main application class for the To-Do List.
 */
public class TodoApp {
    private TaskManager taskManager;
    private Scanner scanner;
    
    public TodoApp() {
        taskManager = new TaskManager();
        scanner = new Scanner(System.in);
    }
    
    @TaskListener(value = {TaskListener.TaskEvent.ADD, TaskListener.TaskEvent.REMOVE})
    public void displayTaskCount() {
        System.out.println("Total tasks: " + taskManager.getAllTasks().size());
    }
    
    public void run() {
        boolean running = true;
        
        System.out.println("Welcome to the To-Do List Application!");
        
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    listTasks();
                    break;
                case "3":
                    markTaskCompleted();
                    break;
                case "4":
                    removeTask();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        taskManager.shutdown();
        System.out.println("Thank you for using the To-Do List Application!");
    }
    
    private void displayMenu() {
        System.out.println("\n== To-Do List Menu ==");
        System.out.println("1. Add Task");
        System.out.println("2. List Tasks");
        System.out.println("3. Mark Task as Completed");
        System.out.println("4. Remove Task");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void addTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Enter task description: ");
        String description = scanner.nextLine().trim();
        
        System.out.println("Enter priority (1=LOW, 2=MEDIUM, 3=HIGH): ");
        int priorityChoice = 0;
        try {
            priorityChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            // Default to medium if invalid
            priorityChoice = 2;
        }
        
        Task.Priority priority;
        switch (priorityChoice) {
            case 1:
                priority = Task.Priority.LOW;
                break;
            case 3:
                priority = Task.Priority.HIGH;
                break;
            default:
                priority = Task.Priority.MEDIUM;
        }
        
        Task task = new Task(title, description, priority);
        taskManager.addTask(task);
        
        System.out.println("Task added successfully!");
        displayTaskCount();
    }
    
    private void listTasks() {
        System.out.println("\n== Task List ==");
        System.out.println("1. All Tasks");
        System.out.println("2. Incomplete Tasks");
        System.out.println("3. Completed Tasks");
        System.out.print("Enter your choice: ");
        
        String choice = scanner.nextLine().trim();
        List<Task> tasksToShow;
        
        switch (choice) {
            case "2":
                tasksToShow = taskManager.getIncompleteTasks();
                System.out.println("\n== Incomplete Tasks ==");
                break;
            case "3":
                tasksToShow = taskManager.getCompletedTasks();
                System.out.println("\n== Completed Tasks ==");
                break;
            default:
                tasksToShow = taskManager.getAllTasks();
                System.out.println("\n== All Tasks ==");
        }
        
        if (tasksToShow.isEmpty()) {
            System.out.println("No tasks to display.");
            return;
        }
        
        for (int i = 0; i < tasksToShow.size(); i++) {
            System.out.println((i + 1) + ". " + tasksToShow.get(i));
        }
    }
    
    private void markTaskCompleted() {
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        
        System.out.println("\n== Mark Task as Completed ==");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". " + task.getTitle() + 
                    (task.isCompleted() ? " (Already completed)" : ""));
        }
        
        System.out.print("Enter task number to toggle completion status (or 0 to cancel): ");
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine().trim());
            if (taskNumber == 0) {
                return;
            }
            
            if (taskNumber > 0 && taskNumber <= tasks.size()) {
                Task task = tasks.get(taskNumber - 1);
                boolean newStatus = !task.isCompleted();
                taskManager.setTaskCompleted(taskNumber - 1, newStatus);
                System.out.println("Task marked as " + (newStatus ? "completed" : "incomplete") + ".");
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    private void removeTask() {
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks available to remove.");
            return;
        }
        
        System.out.println("\n== Remove Task ==");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).getTitle());
        }
        
        System.out.print("Enter task number to remove (or 0 to cancel): ");
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine().trim());
            if (taskNumber == 0) {
                return;
            }
            
            if (taskNumber > 0 && taskNumber <= tasks.size()) {
                if (taskManager.removeTask(taskNumber - 1)) {
                    System.out.println("Task removed successfully.");
                    displayTaskCount();
                } else {
                    System.out.println("Failed to remove task.");
                }
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    public static void main(String[] args) {
        TodoApp app = new TodoApp();
        app.run();
    }
}
