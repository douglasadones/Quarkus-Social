package com.github.douglasadones.rest;

import java.util.List;
import java.util.stream.Collectors;

import com.github.douglasadones.domain.model.Post;
import com.github.douglasadones.domain.model.User;
import com.github.douglasadones.domain.repository.PostRepository;
import com.github.douglasadones.domain.repository.UserRepository;
import com.github.douglasadones.rest.dto.CreatePostRequest;
import com.github.douglasadones.rest.dto.PostResponse;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public void PostResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request) {
        
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        PanacheQuery<Post> query = postRepository
        .find("user", Sort.by("dateTime", Sort.Direction.Descending), user);

        List<PostResponse> postList = query.stream()
        .map(PostResponse::fromEntity)
        .collect(Collectors.toList());

        return Response.ok(postList).build();
    }
    
}
