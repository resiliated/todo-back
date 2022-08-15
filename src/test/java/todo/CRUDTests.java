package todo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.common.mapper.TypeRef;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import todo.model.Category;
import todo.model.Todo;
import todo.model.User;
import static io.restassured.RestAssured.*;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CRUDTests {

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    void addUser(){

        User user = new User();

        user.username = "user";
        user.password = "user";
        user.role = "user";

        given()
                .body(user)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("username", is(user.username));
    }



    @Test
    @Order(2)
    @TestSecurity(user = "user", roles = {"user"})
    void login(){
        get("/users/login")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("username", is("user"));
    }

    @Test
    @Order(3)
    @TestSecurity(user ="user", roles = {"user"})
    void createTodo(){

        Todo todo = new Todo();
        todo.title = "test";

        given()
                .body(todo)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .post("/todo")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("title", is("test"));
    }

    @Test
    @Order(4)
    @TestSecurity(user = "user", roles = {"user"})
    void getTodos(){
        List<Todo> todos = get("/todo").then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(MediaType.APPLICATION_JSON)
                .extract().body().as(getTodoTypeRef());
        assertEquals(1, todos.size());

    }

    @Test
    @Order(5)
    @TestSecurity(user = "user" , roles = {"user"})
    void updateTodo(){
        Todo todo = new Todo();
        todo.title = "Test update";
        todo.id = (long)3;

        given()
                .body(todo)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .pathParam("id", todo.id)
                .when()
                .patch("/todo/{id}")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("title", is("Test update"));

    }

    @Test
    @Order(6)
    @TestSecurity(user = "user", roles={"user"})
    void deleteTodo(){
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .pathParam("id", 3)
                .when()
                .delete("/todo/{id}")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @Order(7)
    @TestSecurity(user = "user", roles={"user"})
    void createCategory(){
        Category category = new Category();
        category.title = "Test category";

        given()
                .body(category)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .post("/category")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("title", is("Test category"));

    }

    @Test
    @Order(8)
    @TestSecurity(user = "user", roles={"user"})
    void getCategories(){

        List<Category> categories = get("/category").then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(MediaType.APPLICATION_JSON)
                .extract().body().as(getCategoryTypeDef());
        assertEquals(1, categories.size());

    }

    @Test
    @Order(9)
    @TestSecurity(user = "user", roles={"user"})
    void updateCategory(){
        Category category = new Category();
        category.title = "Test update";
        category.id = (long)4;

        given()
                .body(category)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .pathParam("id", category.id)
                .when()
                .patch("/category/{id}")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("title", is("Test update"));
    }

    @Test
    @Order(10)
    @TestSecurity(user = "user", roles={"user"})
    void deleteCategory(){
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .pathParam("id", 4)
                .when()
                .delete("/category/{id}")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private TypeRef<List<Todo>> getTodoTypeRef() {
        return new TypeRef<List<Todo>>() {
            // Kept empty on purpose
        };
    }

    private TypeRef<List<Category>> getCategoryTypeDef(){
        return new TypeRef<List<Category>>(){

        };
    }

}
