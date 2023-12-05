package com.example.medium.domain.post.controller;

import com.example.medium.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // Get : /post/list *전체 글 리스트, 공개된 글만 노출
    @GetMapping("/post/list")
    public String showTotalList(){
        return "domain/post/list_total";
    }

    // Get : /post/mylist *내 글 리스트
    @GetMapping("/post/mylist")
    public String showMyList(){
        return "domain/post/list_member";
    }

    // Get: /post/{id} *글 상세보기
    @GetMapping("/post/{id}")
    public String showDetail(){
        return "domain/post/detail";
    }

    // Get: /post/write *글 작성 폼
    @GetMapping("/post/write")
    public String showWriteForm(){
        return "domain/post/write_form";
    }

    // Post: /post/write *글 작성 처리
    @PostMapping("/post/write")
    public String write(){
        return "redirect:/post/list_member";
    }

    // Get: /post/{id}/modify *글 수정 폼
    @GetMapping("post/{id}/modify")
    public String showModifyForm(){
        return "domain/post/write_form";
    }

    // Post: /post/{id}/modify *글 수정 처리
    @PostMapping("post/{id}/modify")
    public String modify(){
        return "redirect:/post/list_member";
    }

    // Delete: /post/{id}/delete *글 삭제 처리
    @DeleteMapping("post/{id}/delete")
    public String delete(){
        return "redirect:/post/list_member";
    }

    // Get: /b/{userid} *유저의 전체 글 리스트
    @GetMapping("/b/{username}")
    public String showMemberPostList(){
        return "redirect:/post/list_member";
    }

    @GetMapping("/b/{username}/{id}")
    public String showMemberPostDetail(){
        return "redirect:/post/detail";
    }
}
