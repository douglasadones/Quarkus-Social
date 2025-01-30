package com.github.douglasadones.rest;

import java.util.Optional;
import java.util.Set;

import com.github.douglasadones.domain.model.User;
import com.github.douglasadones.domain.repository.UserRepository;
import com.github.douglasadones.rest.dto.CreateUserRequest;
import com.github.douglasadones.rest.dto.ResponseError;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository repository;
    private Validator validator;

    @Inject
    public void UserResource(UserRepository repository, Validator validator) {
        this.validator = validator;
        this.repository = repository;
    }
    
    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {

        Set<ConstraintViolation<CreateUserRequest>> violatios = validator.validate(userRequest);

        if(!violatios.isEmpty()) {
            return ResponseError
            .createFromValidator(violatios)
            .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());

        repository.persist(user);

        return Response
        .status(Status.CREATED.getStatusCode())
        .entity(user)
        .build(); 
    }

    @GET
    public Response listAllResponseUsers() {
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build(); 
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User userToDelete = repository.findById(id);
        
        
        if (userToDelete != null) {
            repository.delete(userToDelete);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT 
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest createUserRequest) {;
        User userToUpdate  = repository.findById(id);

        if(userToUpdate != null) {
            userToUpdate.setName(createUserRequest.getName());
            userToUpdate.setAge(createUserRequest.getAge());
            return Response.ok(userToUpdate).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
