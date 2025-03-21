// Task.java
import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a task in our to-do list
 */
public class Task implements Serializable {
    private String title;
    private String description;
    private boolean completed;
    private Date createdDate;
    private Date completedDate;
    private String priority; // "LOW", "MEDIUM", or "HIGH"
    
    // Constructor
    public Task(String title, String description, String priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.completed = false;
        this.createdDate = new Date(); // current date and time
    }
    
    // Getters and setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            this.completedDate = new Date();
        } else {
            this.completedDate = null;
        }
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public Date getCompletedDate() {
        return completedDate;
    }
    
    // Override toString to display task information
    public String toString() {
        String status = completed ? "Completed" : "Not Completed";
        String completedString = "";
        if (completed && completedDate != null) {
            completedString = " (Completed on: " + completedDate + ")";
        }
        
        return title + " - " + description + " - Priority: " + priority + 
               " - Status: " + status + " - Created: " + createdDate + completedString;
    }
}
