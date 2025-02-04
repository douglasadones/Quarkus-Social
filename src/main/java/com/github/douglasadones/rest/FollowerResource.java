package com.github.douglasadones.rest;

import java.util.List;
import java.util.stream.Collectors;

import com.github.douglasadones.domain.model.Follower;
import com.github.douglasadones.domain.model.User;
import com.github.douglasadones.domain.repository.FollowerRepository;
import com.github.douglasadones.domain.repository.UserRepository;
import com.github.douglasadones.rest.dto.FollowerRequest;
import com.github.douglasadones.rest.dto.FollowerResponse;
import com.github.douglasadones.rest.dto.FollowersPerUserResponse;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("users/{userId}/followers")
public class FollowerResource {

    FollowerRepository followerRepository;
    UserRepository userRepository;

    @Inject
    FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional 
    public Response FollowUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {

        if (userId.equals(followerRequest.getFollowerId())) {
            return Response.status(Status.CONFLICT)
            .entity("You can't follow yourself").build();
        }

        User user = userRepository.findById(userId);

        System.out.println(followerRequest.getFollowerId());

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerRequest.getFollowerId());

        boolean alreadyFollower = followerRepository.follows(follower, user);

        // Verificando se já não é um seguidor
        if (!alreadyFollower) {
            Follower entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            followerRepository.persist(entity);
        }

        return Response.noContent().build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {

        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        
        FollowersPerUserResponse objectResponse = new FollowersPerUserResponse();

        List<Follower> allUserFollowers = followerRepository.findByUser(userId);

        
        List<FollowerResponse> content = allUserFollowers.stream()
        .map( FollowerResponse::new ).collect(Collectors.toList());

        objectResponse.setContent(content);
        objectResponse.setFollowersCount(allUserFollowers.size());

        return Response.ok(objectResponse).build();
    }
}
