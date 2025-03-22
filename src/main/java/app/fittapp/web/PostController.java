package app.fittapp.web;

import app.fittapp.post.model.Post;
import app.fittapp.post.service.PostService;
import app.fittapp.security.UserAuthDetails;
import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.workout.model.CompletedWorkout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/community")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getCommunityPage(@AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        List<Post> allSystemPosts = postService.getAllPosts();

        User user = userService.getUserById(userAuthDetails.getId());
        List<Post> userLikedPosts = userService.getAllLikedPostsByUser(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("community");
        modelAndView.addObject("allSystemPosts", allSystemPosts);
        modelAndView.addObject("userLikedPosts", userLikedPosts);

        return modelAndView;
    }

    @PostMapping("/add-post")
    public String postComment(@AuthenticationPrincipal UserAuthDetails userAuthDetails, @RequestParam String postContent) {

        User user = userService.getUserById(userAuthDetails.getId());

        postService.addPost(user, postContent);

        return "redirect:/community";
    }

    @DeleteMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable UUID postId) {

        Post post = postService.getPostById(postId);

        postService.deletePost(post);

        return "redirect:/community";
    }

    @PutMapping("/post/{postId}/like")
    public String likePost(@PathVariable UUID postId, @AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        User user = userService.getUserById(userAuthDetails.getId());

        Post post = postService.getPostById(postId);
        postService.likePost(post, user);

        return "redirect:/community";
    }
}
