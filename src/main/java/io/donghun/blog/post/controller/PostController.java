package io.donghun.blog.post.controller;

import io.donghun.blog.post.domain.Post;
import io.donghun.blog.post.dto.PostCreateRequest;
import io.donghun.blog.post.dto.PostUpdateRequest;
import io.donghun.blog.post.service.PostService;
import io.donghun.blog.post.dto.PostListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/write")
    public String writeForm(@RequestParam("boardId") Long boardId, Model model) {
        model.addAttribute("board", postService.findBoard(boardId));
        model.addAttribute("postRequest", new PostCreateRequest(boardId, "", ""));
        return "post-write";
    }


    @PostMapping("/write")
    public String createPost(
            @Valid @ModelAttribute("postRequest") PostCreateRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {

            if (request.boardId() != null) {
                model.addAttribute("board", postService.findBoard(request.boardId()));
            }
            return "post-write";
        }

        postService.createPost(request, userDetails.getUsername());

        return "redirect:/posts?boardId=" + request.boardId();
    }

    @GetMapping
    public String getPostList(
            @RequestParam("boardId") Long boardId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        PostListResponse response = postService.getPostsByBoard(boardId, pageable);
        model.addAttribute("board", response.board());
        model.addAttribute("postPage", response.postPage());
        return "post-list";
    }

    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostDetail(id);
        model.addAttribute("post", post);
        return "post-detail";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        Post post = postService.getEditablePost(id, userDetails.getUsername());
        model.addAttribute("post", post);
        model.addAttribute("updateRequest", new PostUpdateRequest(post.getTitle(), post.getContent()));
        return "post-edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("updateRequest") PostUpdateRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {

            Post post = postService.getEditablePost(id, userDetails.getUsername());
            model.addAttribute("post", post);

            return "post-edit";
        }
        postService.updatePost(id, request, userDetails.getUsername());
        return "redirect:/posts/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePost(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long boardId = postService.deletePost(id, userDetails.getUsername());
        return "redirect:/posts?boardId=" + boardId;
    }
}
