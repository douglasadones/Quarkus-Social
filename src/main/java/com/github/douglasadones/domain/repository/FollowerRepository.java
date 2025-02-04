package com.github.douglasadones.domain.repository;

import java.util.List;
import java.util.Map;

import com.github.douglasadones.domain.model.Follower;
import com.github.douglasadones.domain.model.User;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    // verificar se usuário já é um seguidor.
    public boolean follows(User follower, User user) {
        // Formas para criar querys e setas parâmetros:
        // Primeiro método:
        // find("follower = :follower and user = :user", follower, user);

        // // Segunda forma:
        // Map<String, Object> params = new HashMap<>();
        // params.put("follower", follower);
        // params.put("user", user);
        // find("follower = :follower and user = :user", params);

        // Terceira forma (Panache)
        Map<String, Object>  params = Parameters.
        with("follower", follower).
        and("user", user).map(); 

        PanacheQuery<Follower> query = find("follower = :follower and user = :user", params);

        return query.firstResultOptional().isPresent();
    }

    public List<Follower> findByUser(Long userId) {
        PanacheQuery<Follower> query = find("user.id", userId);

        return query.list();
    }
}