package todo.api;

import org.jboss.logging.Logger;
import todo.model.User;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/users")
@Produces("application/json")
@Consumes("application/json")
public class UserResource {

    private static final Logger LOG = Logger.getLogger(UserResource.class);


    @GET
    @RolesAllowed("user")
    @Path("/login")
    public Response login(@Context SecurityContext securityContext) {

        User user = User.findByUserName(securityContext.getUserPrincipal().getName());

        if(user == null){
            LOG.error(securityContext.getUserPrincipal().getName() + " doesn't exist");
            throw new WebApplicationException("User " + securityContext.getUserPrincipal().getName() + " does not exist.", Response.Status.NOT_FOUND);
        }

        return  Response.status(Response.Status.ACCEPTED).entity(user).build();
    }

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response createUser(@Valid User user){
        User.add(user.username, user.password, "user");
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

}