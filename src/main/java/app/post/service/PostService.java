package app.post.service;

import app.exceptions.PostNotFound;
import app.post.model.Post;
import app.post.repository.PostRepository;
import app.user.model.User;
import app.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public void addPost(User user, String postContent) {
        Post post = Post.builder()
                .likes(0)
                .author(user)
                .content(postContent)
                .postedOn(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByPostedOnDesc();
    }

    public Post getPostById(UUID postId) {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFound("Post with id %s was not found.".formatted(postId)));
    }

    public void deletePost(Post post) {

        List<User> users = userService.getAllUsers();

        for (User user : users) {
            if (user.getId().equals(post.getAuthor().getId())) {
                user.getPosts().remove(post);
            }
            user.getLikedPosts().remove(post);
            userService.saveUser(user);
        }

        postRepository.delete(post);
    }

    public void likePost(Post post, User user) {

        if (user.getLikedPosts().contains(post)) {
            user.getLikedPosts().remove(post);
            post.setLikes(post.getLikes() - 1);
        } else {
            user.getLikedPosts().add(post);
            post.setLikes(post.getLikes() + 1);
        }

        userService.saveUser(user);
        postRepository.save(post);
    }
}
