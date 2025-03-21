// TaskListener.java
import java.lang.annotation.*;

/**
 * Custom annotation for methods that should be called when tasks change.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TaskListener {
    TaskEvent[] value() default {TaskEvent.ANY};
    
    enum TaskEvent {
        ADD, REMOVE, COMPLETE, ANY
    }
}
