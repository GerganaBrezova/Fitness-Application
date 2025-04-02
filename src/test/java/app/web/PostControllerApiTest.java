package app.web;

import app.meal.model.MealType;
import app.meal.service.MealService;
import app.post.model.Post;
import app.post.service.PostService;
import app.security.UserAuthDetails;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static app.TestBuilder.testUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthenticatedRequestToCommunityPage_thenReturnCommunityView() throws Exception {

        Post firstPost = Post.builder()
                .id(UUID.randomUUID())
                .author(testUser())
                .build();

        Post secondPost = Post.builder()
                .id(UUID.randomUUID())
                .author(testUser())
                .build();

        List<Post> allSystemPosts = List.of(firstPost, secondPost);
        when(postService.getAllPosts()).thenReturn(allSystemPosts);
        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/community").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("community"))
                .andExpect(model().attributeExists("user", "allSystemPosts", "userLikedPosts"));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUnauthenticatedRequestToCommunityPage_thenReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/community");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        verify(userService, never()).getUserById(any());
    }

    @Test
    void postAuthenticatedRequestToCommunityPage_thenRedirectToCommunityView() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/community/add-post")
                .with(user(principal))
                .param("postContent", "Hello!")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/community"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void postUnauthenticatedRequestToCommunityPage_thenReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = post("/community/add-post").with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        verify(userService, never()).getUserById(any());
    }

    @Test
    void deleteAuthenticatedRequestToCommunityPageByAdmin_thenRedirectToCommunityPage() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());
        when(postService.getPostById(any())).thenReturn(new Post());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.ADMIN, true);

        UUID postId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = delete("/community/delete-post/{postId}", postId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/community"));

        verify(postService, times(1)).getPostById(eq(postId));
        verify(postService, times(1)).deletePost(any());
    }

    @Test
    void putAuthenticatedRequestToCommunityPage_thenRedirectToCommunityPage() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());
        when(postService.getPostById(any())).thenReturn(new Post());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        UUID postId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = put("/community/post/{postId}/like", postId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/community"));

        verify(postService, times(1)).getPostById(eq(postId));
        verify(postService, times(1)).getPostById(eq(postId));
        verify(postService, times(1)).likePost(any(), any());
    }

    @Test
    void putUnauthenticatedRequestToCommunityPage_thenRedirectToLoginPage() throws Exception {

        UUID postId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = put("/community/post/{postId}/like", postId).with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        verify(postService, never()).getPostById(eq(postId));
        verify(postService, never()).getPostById(eq(postId));
        verify(postService, never()).likePost(any(), any());
    }
}
