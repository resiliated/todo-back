package todo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Category extends PanacheEntity {
    @NotBlank
    public String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public void setUser(User user){
        this.user = user;
    }

    public static List<Category> listCategoryByUser(User user){
        return list("userid", user.id);
    }
    public static Category findByTitle(String title){
        return find("title", title).firstResult();
    }

}
