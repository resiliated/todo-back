package todo.api;

import org.jboss.logging.Logger;
import todo.model.Category;
import todo.model.Todo;
import todo.model.User;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/category")
@Produces("application/json")
@Consumes("application/json")
@RolesAllowed("user")
public class CategoryResource {

    private static final Logger LOG = Logger.getLogger(CategoryResource.class);

    @POST
    @Transactional
    public Response create(@Context SecurityContext securityContext, @Valid Category category) {
        category.setUser(User.findByUserName(securityContext.getUserPrincipal().getName()));
        category.persist();
        return Response.status(Response.Status.CREATED).entity(category).build();
    }

    @GET
    @Path("/{id}/todo")
    public Response getTodosByCategory(@Context SecurityContext securityContext, @PathParam("id") Long id){
        Category category = Category.findById(id);

        if(category == null){
            throw new WebApplicationException("Category doesn't exist", Response.Status.NOT_FOUND);
        }

        return Response.status(Response.Status.OK).entity(Todo.listTodoByCategory(category)).build();
    }


    @GET
    @Transactional
    public Response getAll(@Context SecurityContext securityContext){
        return Response.status(Response.Status.OK).entity(Category.listCategoryByUser(User.findByUserName(securityContext.getUserPrincipal().getName()))).build();
    }

    @PATCH
    @Transactional
    public Response update(@Context SecurityContext securityContext, @Valid Category category){
        Category entity = Category.findById(category.id);
        entity.title = category.title;
        return Response.status(Response.Status.ACCEPTED).entity(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@Context SecurityContext securityContext, @PathParam("id") Long id){
        Category category = Category.findById(id);
        List<Todo> todoList = Todo.listTodoByCategory(category);

        if(!todoList.isEmpty()){
            throw new WebApplicationException("Category has todos, can't be deleted", Response.Status.FORBIDDEN);
        }
        Category.deleteById(id);

        return Response.noContent().build();
    }
}
