package app.web.mapper;

import app.user.model.User;
import app.web.dto.CalculateRequest;
import app.web.dto.EditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public EditRequest mapToEditRequest(User user) {

        return EditRequest.builder()
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    public CalculateRequest mapToCalculateRequest(User user) {

        return CalculateRequest.builder()
                .age(user.getAge())
                .height(user.getHeight())
                .weight(user.getWeight())
                .goal(user.getGoal())
                .activityLevel(user.getActivityLevel())
                .gender(user.getGender())
                .build();
    }
}
