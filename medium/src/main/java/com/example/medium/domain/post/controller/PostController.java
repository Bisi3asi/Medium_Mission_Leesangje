package com.example.medium.domain.post.controller;

import com.example.medium.domain.comment.dto.CommentRequestDto;
import com.example.medium.domain.file.entity.ImageFile;
import com.example.medium.domain.file.service.ImageFileService;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.domain.post.dto.PostRequestDto;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.service.PostService;
import com.example.medium.global.response.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Objects;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final ImageFileService imageFileService;

    // Get : / *최신글 30개 노출
    @GetMapping("/")
    public String showRecentList(Model model) {
        model.addAttribute("paging", postService.getTotalList(0, 30));
        return "domain/home/home";
    }

    // Get : /post/list *전체 글 리스트, 공개된 글만 노출
    // todo : category 별로 다른 페이징,
    @GetMapping("/post/list")
    public String showTotalList(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Post> paging = postService.getTotalList(page, 10);

        model.addAttribute("paging", postService.getTotalList(page, 10));
        model.addAttribute("fileStorePath", imageFileService.getFILESTORE_PATH());

        // randomValue 추가 : 랜덤 페이지 이동
        int randomValue = new Random().nextInt(paging.getTotalPages() - 1);
        model.addAttribute("randomValue", randomValue);
        return "domain/post/list_total";
    }

    // Get : /post/mylist *내 글 리스트
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/mylist")
    public String showMyList(@RequestParam(defaultValue = "0") int page,
                             Principal principal,
                             Model model) {

        model.addAttribute("paging", postService.getMyList(page, 10, principal.getName()));
        model.addAttribute("member", memberService.findByUsername(principal.getName()));
        return "domain/post/list_member";
    }

    // Get: /b/{userid} *유저의 전체 글 리스트
    @GetMapping("/b/{username}")
    public String showMemberPostList(@PathVariable String username,
                                     @RequestParam(defaultValue = "0") int page,
                                     Model model) {

        model.addAttribute("paging", postService.getMemberList(page, 10, username));
        model.addAttribute("member", memberService.findByUsername(username));

        return "domain/post/list_member";
    }

    // Get: /post/{id} *글 상세보기
    @GetMapping("/post/{id}")
    public String showDetail(Model model, @PathVariable Long id, Principal principal) {
        // 비공개 글에 접근하는 경우 에러 발생
        Post post = postService.get(id);
        if (!post.isPublished() && !Objects.equals(post.getAuthor().getUsername(), principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다.");
        }

        // 조회수 증가
        postService.incrViewCount(post);

        model.addAttribute("post", post);
        model.addAttribute("commentRequestDto", new CommentRequestDto());
        return "domain/post/detail";
    }

    // Get: /post/write *글 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/write")
    public String showWriteForm(@ModelAttribute("postRequestDto") PostRequestDto postRequestDto) {
        return "domain/post/write_form";
    }

    // Post: /post/write *글 작성 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/write")
    public String write(@ModelAttribute("postRequestDto")
                        @Valid PostRequestDto postRequestDto,
                        BindingResult brs,
                        @RequestPart("multipartFile") MultipartFile multipartFile,
                        RedirectAttributes attr,
                        Principal principal) {

        if (brs.hasErrors()) {
            return "domain/post/write_form";
        }

        ResponseData<Post> resp = postService.create(
                postRequestDto, memberService.findByUsername(principal.getName())
        );

        // MultiPartFile 은 자동적으로 데이터 바인딩이 안되므로 @RequestPart로 받아온 후 직접 처리
        if (!multipartFile.isEmpty()) {
            ResponseData<ImageFile> imageFileResponseData = imageFileService.create(multipartFile, resp.getData());
        }

        attr.addFlashAttribute("msg", resp.getMsg());
        return String.format("redirect:/post/%d", resp.getData().getId());
    }

    // Get: /post/{id}/modify *글 수정 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("post/{id}/modify")
    public String showModifyForm(@PathVariable Long id,
                                 @ModelAttribute("postRequestDto") PostRequestDto postRequestDto,
                                 Principal principal) {

        Post post = postService.get(id);
        postService.validateAuthor(post, memberService.findByUsername(principal.getName()));

        postRequestDto.setTitle(post.getTitle());
        postRequestDto.setContent(post.getContent());
        postRequestDto.setPublished(post.isPublished());
        postRequestDto.setPrime(post.isPrime());

        return "domain/post/modify_form";
    }

    // Post: /post/{id}/modify *글 수정 처리
    @PreAuthorize("isAuthenticated()")
    @PutMapping("post/{id}/modify")
    public String modify(@PathVariable Long id,
                         @ModelAttribute("postRequestDto") @Valid PostRequestDto postRequestDto,
                         BindingResult brs,
                         @RequestPart("multipartFile") MultipartFile multipartFile,
                         RedirectAttributes attr,
                         Principal principal) {

        if (brs.hasErrors()) {
            return "domain/post/modify_form";
        }

        ResponseData<Post> resp = postService.modify(
                postRequestDto,
                id,
                memberService.findByUsername(principal.getName())
        );

        if (!multipartFile.isEmpty()) {
            ResponseData<ImageFile> imageFileResponseData = imageFileService.modify(multipartFile, resp.getData());
        }

        attr.addFlashAttribute("msg", resp.getMsg());
        return String.format("redirect:/post/%d", id);
    }

    // Delete: /post/{id}/delete *글 삭제 처리
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("post/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes attr,
                         Principal principal) {

        ResponseData<Post> resp = postService.delete(id, memberService.findByUsername(principal.getName())
        );

        attr.addFlashAttribute("msg", resp.getMsg());
        return "redirect:/post/list";
    }
}
