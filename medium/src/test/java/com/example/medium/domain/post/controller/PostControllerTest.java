package com.example.medium.domain.post.controller;

import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.service.PostService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PostControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostService postService;

    @DisplayName("showRecentList")
    @Test
    @SneakyThrows
    void showRecentListTest() {
        // When
        ResultActions resultActions = mvc
                .perform((get("/")))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showRecentList"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        Recent
                        """.stripIndent().trim())))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        테스트 글 99
                        """.stripIndent().trim())))
                .andExpect(MockMvcResultMatchers.content().string(not(containsString("""
                        테스트 글 98
                        """.stripIndent().trim()))));
        ;
    }

    @DisplayName("showTotalList")
    @Test
    @SneakyThrows
    void showTotalListTest() {
        // When
        ResultActions resultActions = mvc
                .perform((get("/post/list")))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showTotalList"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        Total
                        """.stripIndent().trim())))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        테스트 글 99
                        """.stripIndent().trim())))
                .andExpect(MockMvcResultMatchers.content().string(not(containsString("""
                        테스트 글 98
                        """.stripIndent().trim()))));
        ;
    }

    @DisplayName("showDetail")
    @Test
    @SneakyThrows
    void showDetailTest() {
        // When
        ResultActions resultActions = mvc
                .perform((get("/post/detail/1")))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showDetail"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        테스트 글 1
                        """.stripIndent().trim())))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        테스트 내용 1
                        """.stripIndent().trim())))
        ;
    }

    @DisplayName("showWriteForm")
    @Test
    @SneakyThrows
    void showWriteFormTest() {
        // When
        ResultActions resultActions = mvc
                .perform((get("/post/write")))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showWriteForm"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        Title
                        """.stripIndent().trim())))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        Tell your story
                        """.stripIndent().trim())))
                .andExpect(MockMvcResultMatchers.content().string(containsString("""
                        Publish
                        """.stripIndent().trim())))
        ;
    }

    @DisplayName("write Post")
    @Test
    @SneakyThrows
    void writePostTest() {
        // When
        ResultActions resultActions = mvc
                .perform((post("/post/write"))
                        .with(csrf())
                        .param("title", "test title")
                        .param("content", "test content")
                        .param("published", "true")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is3xxRedirection()) // 저장 후 redirection 필요
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/post/*"));

                Post post = postService.getLatest();
                assertThat(post.getTitle()).isEqualTo("test title");
                assertThat(post.getContent()).isEqualTo("test content");
                assertThat(post.isPublished()).isEqualTo(true);
        ;
    }

    @DisplayName("modify Post")
    @Test
    @SneakyThrows
    void modifyPostTest() {
        // When
        ResultActions resultActions = mvc
                .perform((put("/post/1/modify"))
                        .with(csrf())
                        .param("title", "test title")
                        .param("content", "test content")
                        .param("published", "true")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is3xxRedirection()) // 저장 후 redirection 필요
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/post/*"));

        Post post = postService.get(1L);
        assertThat(post.getTitle()).isEqualTo("test title");
        assertThat(post.getContent()).isEqualTo("test content");
        assertThat(post.isPublished()).isEqualTo(true);
        ;
    }
}
