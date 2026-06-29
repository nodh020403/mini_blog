package io.donghun.blog.post.service;

import io.donghun.blog.board.domain.Board;
import io.donghun.blog.board.service.BoardService;
import io.donghun.blog.common.constants.ErrorCode;
import io.donghun.blog.common.constants.Role;
import io.donghun.blog.common.exception.BusinessException;
import io.donghun.blog.post.domain.Post;
import io.donghun.blog.post.dto.PostCreateRequest;
import io.donghun.blog.post.dto.PostUpdateRequest;
import io.donghun.blog.post.repository.PostRepository;
import io.donghun.blog.user.domain.User;
import io.donghun.blog.post.dto.PostListResponse;
import io.donghun.blog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;
    private final UserService userService;

    @Transactional
    public void createPost(PostCreateRequest request, String email) {
        User loginUser = userService.findByEmail(email);
        Board board = boardService.findById(request.boardId());

        validateWritePermission(board, loginUser);

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .board(board)
                .user(loginUser)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public Post getPostDetail(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.increaseViewCount();

        return post;
    }

    public PostListResponse getPostsByBoard(Long boardId, Pageable pageable) {
        Board board = boardService.findById(boardId);

        Page<Post> postPage = postRepository.findByBoardId(boardId, pageable);

        return new PostListResponse(board, postPage);
    }

    public Post getEditablePost(Long id, String email) {
        User loginUser = userService.findByEmail(email);
        Post post = findPost(id);
        validateAuthor(post, loginUser);
        return post;
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request, String email) {
        User loginUser = userService.findByEmail(email);
        Post post = findPost(postId);

        validateAuthor(post, loginUser);
        post.update(request.title(), request.content());
    }

    @Transactional
    public Long deletePost(Long postId, String email) {
        User loginUser = userService.findByEmail(email);
        Post post = findPost(postId);

        validateAuthor(post, loginUser);
        Long boardId = post.getBoard().getId();

        postRepository.delete(post);
        return boardId;
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateWritePermission(Board board, User user) {
        if (board.getRole() == Role.ADMIN && user.getRole() != Role.ADMIN) {
            throw new BusinessException(io.donghun.blog.common.constants.ErrorCode.ACCESS_DENIED);
        }
    }

    private void validateAuthor(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    public Board findBoard(Long boardId) {
        return boardService.findById(boardId);
    }
}
