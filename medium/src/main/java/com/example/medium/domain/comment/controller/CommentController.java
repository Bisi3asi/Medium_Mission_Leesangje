package com.example.medium.domain.comment.controller;

import com.example.medium.domain.comment.dto.CommentRequestDto;
import com.example.medium.domain.comment.entity.Comment;
import com.example.medium.domain.comment.service.CommentService;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.service.PostService;
import com.example.medium.global.response.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String write(Model model,
                        @PathVariable("id") Long id,
                        @ModelAttribute("commentRequestDto") @Valid CommentRequestDto commentRequestDto,
                        BindingResult brs,
                        RedirectAttributes attr,
                        Principal principal) {

        Post post = postService.get(id);
        Member member = memberService.findByUsername(principal.getName());

        if (brs.hasErrors()) {
            model.addAttribute("post", post);
            return "domain/post/detail";
        }

        ResponseData<Comment> resp = commentService.create(post, commentRequestDto, member);
        attr.addFlashAttribute("msg", resp.getMsg());
        return String.format("redirect:/post/%s", post.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/modify/{id}")
    public String modify(Model model,
                         @PathVariable("id") Long id,
                         @ModelAttribute("commentForm") @Valid CommentRequestDto commentRequestDto,
                         BindingResult brs,
                         RedirectAttributes attr,
                         Principal principal) {

        Comment comment = commentService.get(id);
        Member member = memberService.findByUsername(principal.getName());

        if (brs.hasErrors()) {
            model.addAttribute("post", comment.getPost());
            return "domain/post/detail";
        }

        ResponseData<Comment> resp = commentService.update(comment, commentRequestDto, member);
        attr.addFlashAttribute("msg", resp.getMsg());
        return String.format("redirect:/post/%s", comment.getPost().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         RedirectAttributes attr,
                         Principal principal) {

        Comment comment = commentService.get(id);
        Member member = memberService.findByUsername(principal.getName());

        ResponseData resp = commentService.delete(comment, member);
        attr.addFlashAttribute("msg", resp.getMsg());
        return String.format("redirect:/post/%s", comment.getPost().getId());
    }
}
