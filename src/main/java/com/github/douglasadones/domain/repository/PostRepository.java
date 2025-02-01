package com.github.douglasadones.domain.repository;

import com.github.douglasadones.domain.model.Post;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {}
