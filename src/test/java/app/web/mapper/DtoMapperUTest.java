package app.web.mapper;

import app.user.model.User;
import app.user.model.UserActivityLevel;
import app.user.model.UserGender;
import app.user.model.UserGoal;
import app.web.dto.CalculateRequest;
import app.web.dto.EditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    void happyPathWhenMappingUserToEditRequest() {

        User user = User.builder()
                .username("gery_11")
                .name("Gery")
                .email("gery_11@gmail.com")
                .profilePictureUrl("image.com")
                .build();

        EditRequest editRequest = DtoMapper.mapToEditRequest(user);

        assertNotNull(editRequest);
        assertEquals(user.getUsername(), editRequest.getUsername());
        assertEquals(user.getEmail(), editRequest.getEmail());
        assertEquals(user.getProfilePictureUrl(), editRequest.getProfilePictureUrl());
        assertEquals(user.getName(), editRequest.getName());
    }

    @Test
    void happyPathWhenMappingUserToCalculateRequest() {

        User user = User.builder()
                .gender(UserGender.FEMALE)
                .age(21)
                .activityLevel(UserActivityLevel.MODERATELY_ACTIVE)
                .height(170)
                .weight(60)
                .goal(UserGoal.LOSS_WEIGHT)
                .build();

        CalculateRequest calculateRequest = DtoMapper.mapToCalculateRequest(user);

        assertNotNull(calculateRequest);
        assertEquals(user.getGender(), calculateRequest.getGender());
        assertEquals(user.getAge(), calculateRequest.getAge());
        assertEquals(user.getHeight(), calculateRequest.getHeight());
        assertEquals(user.getWeight(), calculateRequest.getWeight());
        assertEquals(user.getGoal(), calculateRequest.getGoal());
        assertEquals(user.getActivityLevel(), calculateRequest.getActivityLevel());
    }
}
