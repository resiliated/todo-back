package todo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Todo extends PanacheEntity {

    @NotBlank
    //@Column(unique = true)
    public String title;

    public String content;

    @CreationTimestamp
    @Column(name = "CREATED_TIME", updatable = false)
    private Timestamp createdTime;

    @UpdateTimestamp
    @Column(name = "UPDATED_TIME")
    private Timestamp updatedTime;

    public int status = Status.TODO.getNumVal();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    public Category category;

    public void setUser(User user){
        this.user = user;
    }

    public boolean validateUser(User user){
        return this.user.id == user.id;
    }

    public static List<Todo> listTodoByUser(User user){
        return list("userid", user.id);
    }

    public static List<Todo> listTodoByCategory(Category category){
        return list("categoryid", category.id);
    }

    public static List<Todo> findNotCompleted() {
        return list("status", Status.COMPLETED.getNumVal());
    }

    public static List<Todo> findCompleted() {
        return list("status", Status.COMPLETED.getNumVal());
    }

    public static long deleteCompleted() {
        return delete("status", Status.COMPLETED.getNumVal());
    }

}
