package io.donghun.blog.post.dto;

import io.donghun.blog.board.domain.Board;
import io.donghun.blog.post.domain.Post;
import org.springframework.data.domain.Page;

public record PostListResponse(
        Board board,
        Page<Post> postPage
) {

}
