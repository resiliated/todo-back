package todo.api;

import todo.model.User;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/api/users")
public class UserResource {

    @GET
    @RolesAllowed("user")
    @Path("/login")
    public Response login(@Context SecurityContext securityContext) {
        return  Response.status(Response.Status.ACCEPTED).entity(User.findByUserName(securityContext.getUserPrincipal().getName())).build();
    }

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response createUser(@Valid User user){
        User.add(user.username, user.password, "user");
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

}