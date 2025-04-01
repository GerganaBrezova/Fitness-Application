package app.post;

import app.exceptions.PostNotFound;
import app.post.model.Post;
import app.post.repository.PostRepository;
import app.post.service.PostService;
import app.user.model.User;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceUTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    //Add Post
    @Test
    void whenAddPost_thenSuccessfullyAddedPost() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        String content = "Hello!";

        Post post = Post.builder()
                .id(any())
                .content(content)
                .build();

        postService.addPost(user, content);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());

        Post savedPost = postCaptor.getValue();

        assertNotNull(savedPost);
        assertEquals(content, savedPost.getContent());
        assertEquals(post.getId(), savedPost.getId());
        verify(postRepository, times(1)).save(any());
    }

    //Get All Posts
    @Test
    void whenGetAllPosts_thenReturnsAllPostsOrderedByPostedOnDescending() {

        Post firstPost = Post.builder()
                .postedOn(LocalDateTime.now())
                .build();
        Post secondPost = Post.builder()
                .postedOn(LocalDateTime.now())
                .build();
        Post thirdPost = Post.builder()
                .postedOn(LocalDateTime.now())
                .build();

        List<Post> posts = List.of(thirdPost, secondPost, firstPost);

        when(postRepository.findAllByOrderByPostedOnDesc()).thenReturn(posts);

        List<Post> actualPosts = postService.getAllPosts();

        assertNotNull(actualPosts);
        assertEquals(3, actualPosts.size());
        assertTrue(actualPosts.containsAll(posts));
        assertTrue(posts.containsAll(actualPosts));
    }

    //Get Post By ID
    @Test
    void throwsExceptionWhen_tryingToGetPostWithNonExistentID() {

        when(postRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(PostNotFound.class, () -> postService.getPostById(any()));
    }

    @Test
    void whenGetPostById_thenReturnsCorrectPost() {

        UUID postId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post actualPost = postService.getPostById(postId);

        assertNotNull(actualPost);
        assertEquals(postId, actualPost.getId());
        assertEquals(post, actualPost);
    }

    //Delete Post
    @Test
    void whenDeletePost_thenPostIsDeletedFromAllUsersLikedPosts_andFromOwnerPosts() {

        UUID postId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId)
                .author(User.builder().id(ownerId).build())
                .build();

        User owner = User.builder()
                .id(ownerId)
                .posts(new ArrayList<>(List.of(post)))
                .likedPosts(new HashSet<>(Set.of(post)))
                .build();

        User firstUser = User.builder()
                .id(UUID.randomUUID())
                .likedPosts(new HashSet<>(Set.of(post)))
                .build();

        User secondUser = User.builder()
                .id(UUID.randomUUID())
                .likedPosts(new HashSet<>(Set.of(post)))
                .build();

        List<User> allUsers = List.of(owner, firstUser, secondUser);

        when(userService.getAllUsers()).thenReturn(allUsers);

        postService.deletePost(post);

        assertFalse(owner.getPosts().contains(post));
        assertFalse(firstUser.getLikedPosts().contains(post));
        assertFalse(secondUser.getLikedPosts().contains(post));
        verify(userService, times(3)).saveUser(any(User.class));
        verify(postRepository, times(1)).delete(post);
    }

    //Like Post
    @Test
    void whenUserLikePost_thenAddsToUserLikedPosts_whenUserUnlike_thenRemovesFromUserLikedPosts() {

        UUID postId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId)
                .likes(2)
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .likedPosts(new HashSet<>())
                .build();

        postService.likePost(post, user);

        assertTrue(user.getLikedPosts().contains(post));
        assertEquals(3, post.getLikes());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(userService, times(1)).saveUser(any(User.class));

        postService.likePost(post, user);

        assertFalse(user.getLikedPosts().contains(post));
        assertEquals(2, post.getLikes());
        verify(postRepository, times(2)).save(any(Post.class));
        verify(userService, times(2)).saveUser(any(User.class));
    }
}














