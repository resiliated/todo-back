package todo.api;



import todo.model.Todo;
import todo.model.User;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import org.jboss.logging.Logger;


@Path("/api")
@Produces("application/json")
@Consumes("application/json")
@RolesAllowed("user")
public class TodoResource {

    private static final Logger LOG = Logger.getLogger(TodoResource.class);

    @OPTIONS
    public Response opt() {
        return Response.ok().build();
    }

    @GET
    public List<Todo> getAll(@Context SecurityContext securityContext) {
        User user = User.findByUserName(securityContext.getUserPrincipal().getName());
        LOG.info("get all todos");
        return Todo.listTodoByUser(user);
    }

    @GET
    @Path("/{id}")
    public Todo getOne(@Context SecurityContext securityContext, @PathParam("id") Long id) {
        User user = User.findByUserName(securityContext.getUserPrincipal().getName());
        Todo entity = Todo.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
        }

        if(entity.validateUser(user)){
            throw new WebApplicationException("User " + user.username + " is trying to get a todo that is not his", Status.FORBIDDEN);
        }

        return entity;
    }

    @POST
    @Transactional
    public Response create(@Context SecurityContext securityContext, @Valid Todo item) {
        item.setUser(User.findByUserName(securityContext.getUserPrincipal().getName()));
        item.persist();
        return Response.status(Status.CREATED).entity(item).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response update(@Context SecurityContext securityContext, @Valid Todo todo, @PathParam("id") Long id) {
        User user = User.findByUserName(securityContext.getUserPrincipal().getName());
        Todo entity = Todo.findById(id);

        if(entity.validateUser(user)){
            throw new WebApplicationException("User " + user.username + " is trying to update a todo that is not his", Status.FORBIDDEN);
        }

        entity.id = id;
        entity.status = todo.status;
        entity.title = todo.title;
        entity.content = todo.content;

        return Response.ok(entity).build();
    }

    @DELETE
    @Transactional
    public Response deleteCompleted() {
        Todo.deleteCompleted();
        return Response.noContent().build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteOne(@Context SecurityContext securityContext, @PathParam("id") Long id) {
        User user = User.findByUserName(securityContext.getUserPrincipal().getName());
        Todo entity = Todo.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
        }

        if(entity.validateUser(user)){
            throw new WebApplicationException("User " + user.username + " is trying to delete a todo that is not his", Status.FORBIDDEN);
        }

        entity.delete();
        return Response.noContent().build();
    }

}