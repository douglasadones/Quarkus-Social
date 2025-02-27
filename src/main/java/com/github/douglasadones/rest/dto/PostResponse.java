package com.github.douglasadones.rest.dto;

import java.time.LocalDateTime;

import com.github.douglasadones.domain.model.Post;

import lombok.Data;

@Data
public class PostResponse {

    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setText(post.getText());
        postResponse.setDateTime(post.getDateTime());
        return postResponse;
    }
}
