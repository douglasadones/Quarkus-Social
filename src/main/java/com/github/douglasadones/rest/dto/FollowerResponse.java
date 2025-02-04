package com.github.douglasadones.rest.dto;

import com.github.douglasadones.domain.model.Follower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse(Follower follower) {
        this(follower.getFollower().getId(), follower.getFollower().getName());
    }
}
