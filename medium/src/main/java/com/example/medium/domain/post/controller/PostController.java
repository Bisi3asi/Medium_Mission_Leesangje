package com.example.medium.domain.post.controller;

import com.example.medium.domain.comment.dto.CommentRequestDto;
import com.example.medium.domain.file.entity.ImageFile;
import com.example.medium.domain.file.service.ImageFileService;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.domain.post.dto.PostRequestDto;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.service.PostService;
import com.example.medium.global.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/post/mylist")
    public String showMyList() {
        return "domain/post/list_member.html";
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
    public String showWriteForm(@ModelAttribute("postRequestDto") PostRequestDto postRequestDto) {
        return "domain/post/write_form";
    }

    // Post: /post/write *글 작성 처리
    @PostMapping("/post/write")
    public String write(@ModelAttribute("PostRequestDto")
                        @Valid PostRequestDto postRequestDto,
                        BindingResult brs,
                        RedirectAttributes attr,
                        @RequestPart("multipartFile") MultipartFile multipartFile) {
        if (brs.hasErrors()) {
            return "domain/post/write_form";
        }
        // 임시 : member 기능 구현 후 삭제
        if (postRequestDto.getAuthor() == null) {
            postRequestDto.setAuthor(memberService.findByUsername("testuser1"));
        }
        ResponseDto<Post> resp = postService.create(postRequestDto);
        attr.addFlashAttribute("msg", resp.getMsg());

        // MultiPartFile은 자동적으로 데이터 바인딩이 안되므로 @RequestPart로 받아온 후 직접 처리
        if (!multipartFile.isEmpty()) {
            ResponseDto<ImageFile> imageFileResponseDto = imageFileService.create(multipartFile, resp.getData());
        }

        return String.format("redirect:/post/%d", resp.getData().getId());

    }

    // Get: /post/{id}/modify *글 수정 폼
    @GetMapping("post/{id}/modify")
    public String showModifyForm(@PathVariable Long id,
                                 @ModelAttribute("postRequestDto") PostRequestDto postRequestDto) {
        Post post = postService.get(id);

        // 빌더 패턴이 아닌, 모델의 postRequestDto는 Setter로 인한 설정 만이 바인딩 값 유지 가능
        // 빌더 패턴을 사용할 시 매개변수의 postRequestDto 객체가 아닌 다른 객체가 들어가기 때문 = 바인딩 실패
        postRequestDto.setTitle(post.getTitle());
        postRequestDto.setContent(post.getContent());
        postRequestDto.setPublished(post.isPublished());

        return "domain/post/modify_form";
    }

    // Post: /post/{id}/modify *글 수정 처리
    @PutMapping("post/{id}/modify")
    public String modify(@PathVariable Long id,
                         @ModelAttribute("postRequestDto") @Valid PostRequestDto postRequestDto,
                         BindingResult brs,
                         RedirectAttributes attr) {
        if (brs.hasErrors()) {
            return "domain/post/modify_form";
        }
        // 임시 : member 기능 구현 후 삭제
        if (postRequestDto.getAuthor() == null) {
            postRequestDto.setAuthor(memberService.findByUsername("testuser1"));
        }

        ResponseDto<Post> resp = postService.modify(postRequestDto, id);
        attr.addFlashAttribute("msg", resp.getMsg());

        return String.format("redirect:/post/%d", id);
    }

    // Delete: /post/{id}/delete *글 삭제 처리
    @DeleteMapping("post/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attr) {
        ResponseDto<Post> resp = postService.delete(id);
        attr.addFlashAttribute("msg", resp.getMsg());

        return "redirect:/post/list";
    }

    // Get: /b/{userid} *유저의 전체 글 리스트
    @GetMapping("/b/{username}")
    public String showMemberPostList(@PathVariable String username,
                                     @RequestParam(defaultValue = "0") int page,
                                     Model model) {
        model.addAttribute("paging", postService.getMemberList(page, 10, username));

        return "domain/post/list_member";
    }

//    @GetMapping("/b/{username}/{id}")
//    public String showMemberPostDetail(@PathVariable Long id) {
//        return "redirect:/post/detail";
//    }
}
