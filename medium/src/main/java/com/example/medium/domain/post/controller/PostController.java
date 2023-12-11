package com.example.medium.domain.post.controller;

import com.example.medium.domain.comment.dto.CommentRequestDto;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // Get : / *최신글 30개 노출
    @GetMapping("/")
    public String showRecentList(Model model) {
        model.addAttribute("paging", postService.getList(0, 30));
        return "domain/home/home";
    }

    // Get : /post/list *전체 글 리스트, 공개된 글만 노출
    @GetMapping("/post/list")
    public String showTotalList(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Post> paging = postService.getList(page, 10);
        model.addAttribute("paging", postService.getList(page, 10));

        // randomValue 추가 : 랜덤 페이지 이동
        int randomValue = new Random().nextInt(paging.getTotalPages() - 1);
        model.addAttribute("randomValue", randomValue);
        return "domain/post/list_total";
    }

    // Get : /post/mylist *내 글 리스트
    @GetMapping("/post/mylist")
    public String showMyList() {
        return "domain/post/list_member";
    }

    // Get: /post/{id} *글 상세보기
    @GetMapping("/post/{id}")
    public String showDetail(Model model, @PathVariable Long id) {
        model.addAttribute("post", postService.get(id));
        model.addAttribute("commentRequestDto", new CommentRequestDto());
        return "domain/post/detail";
    }

    // Get: /post/write *글 작성 폼
    @GetMapping("/post/write")
    public String showWriteForm() {
        return "domain/post/write_form";
    }

    // Post: /post/write *글 작성 처리
    @PostMapping("/post/write")
    public String write() {
        return "redirect:/post/list_member";
    }

    // Get: /post/{id}/modify *글 수정 폼
    @GetMapping("post/{id}/modify")
    public String showModifyForm() {
        return "domain/post/write_form.html";
    }

    // Post: /post/{id}/modify *글 수정 처리
    @PostMapping("post/{id}/modify")
    public String modify() {
        return "redirect:/post/list_member";
    }

    // Delete: /post/{id}/delete *글 삭제 처리
    @DeleteMapping("post/{id}/delete")
    public String delete() {
        return "redirect:/post/list_member";
    }

    // Get: /b/{userid} *유저의 전체 글 리스트
    @GetMapping("/b/{username}")
    public String showMemberPostList() {
        return "redirect:/post/list_member";
    }

    @GetMapping("/b/{username}/{id}")
    public String showMemberPostDetail() {
        return "redirect:/post/detail";
    }
}
