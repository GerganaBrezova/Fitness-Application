package app.user;

import app.calculator.service.CalculatorService;
import app.event.UserRegisteredEventProducer;
import app.event.payload.UserRegisteredEvent;
import app.exceptions.EmailAlreadyExists;
import app.exceptions.PasswordsDoNotMatch;
import app.exceptions.UserNotFound;
import app.exceptions.UsernameAlreadyExists;
import app.meal.model.Meal;
import app.security.UserAuthDetails;
import app.user.model.*;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.CalculateRequest;
import app.web.dto.EditRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CalculatorService calculatorService;

    @Mock
    private UserRegisteredEventProducer userRegisteredEventProducer;

    @InjectMocks
    private UserService userService;

    //Register
    @Test
    void throwsException_whenRegister_withExistingUsername() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Gery")
                .password("111213")
                .confirmPassword("111213")
                .email("gery@gmail.com")
                .build();

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExists.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void throwsException_whenRegister_withExistingEmail() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Gery")
                .password("111213")
                .confirmPassword("111213")
                .email("gery@gmail.com")
                .build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExists.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void throwsException_whenRegister_withNonMatchingPasswords() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Gery")
                .password("111213")
                .confirmPassword("212223")
                .email("gery@gmail.com")
                .build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(PasswordsDoNotMatch.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void happyPath_whenRegister() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Gery")
                .password("111213")
                .confirmPassword("111213")
                .email("gery@gmail.com")
                .build();

        UUID expectedUserId = UUID.randomUUID();

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("111213");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> {
            User savedUser = userCaptor.getValue();
            savedUser.setId(expectedUserId);
            return savedUser;
        });

        User registeredUser = userService.register(registerRequest);

        assertNotNull(registeredUser);
        assertEquals(expectedUserId, registeredUser.getId());
        verify(userRepository, times(1)).save(any(User.class));

        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(userRegisteredEventProducer, times(1)).sendEvent(eventCaptor.capture());

        UserRegisteredEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals(expectedUserId, capturedEvent.getUserId());
    }


    //Get User By ID
    @Test
    void throwsException_whenGetUser_withNonExistingId() {

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.getUserById(any()));
    }

    @Test
    void returnsCorrectUser_whenGetUser_withExistingId() {

        UUID userId = UUID.randomUUID();

        User expectedUser = User.builder()
                .id(userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(userId);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    //Get User By Username
    @Test
    void throwsException_whenGetUser_withNonExistingUsername() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.getUserByUsername(any()));
    }

    @Test
    void returnsCorrectUser_whenGetUser_withExistingUsername() {

        String username = "Gery";

        User expectedUser = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserByUsername(username);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    }

    //User Daily intake
    @Test //Arrange -> Mock repository and service behaviors -> Act -> Assert
    void updatesCorrectly_dailyIntake_forUser() {

        //Arrange
        CalculateRequest calculateRequest = CalculateRequest.builder()
                .gender(UserGender.FEMALE)
                .age(21)
                .goal(UserGoal.LOSS_WEIGHT)
                .activityLevel(UserActivityLevel.MODERATELY_ACTIVE)
                .height(170.0)
                .weight(60.0)
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        double expectedDailyIntake = 1732;

        //Mock repository and service behaviors
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(calculatorService.calculateDailyIntake(any(CalculateRequest.class))).thenReturn(expectedDailyIntake);
        when(userRepository.save(any(User.class))).thenReturn(user);

        //Act
        userService.calculateUserDailyIntake(user.getId(), calculateRequest);

        //Assert
        assertEquals(expectedDailyIntake, user.getDailyIntake());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(user);
    }

    //Edit User Details
    @Test
    void testIf_UserDetails_areProperlyEdited() {

        EditRequest editRequest = EditRequest.builder()
                .username("Gery_11")
                .name("Gery")
                .email("gery@gmail.com")
                .profilePictureUrl("image.com")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userService.editUserDetails(editRequest, user.getId());

        assertEquals(editRequest.getUsername(), user.getUsername());
        assertEquals(editRequest.getEmail(), user.getEmail());
        assertEquals(editRequest.getProfilePictureUrl(), user.getProfilePictureUrl());
        assertEquals(editRequest.getName(), user.getName());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(user);
    }

    //Edit User Additional Details
    @Test
    void testIf_UserAdditionalDetails_areProperlyEdited() {

        CalculateRequest calculateRequest = CalculateRequest.builder()
                .gender(UserGender.FEMALE)
                .age(21)
                .goal(UserGoal.LOSS_WEIGHT)
                .activityLevel(UserActivityLevel.MODERATELY_ACTIVE)
                .height(170.0)
                .weight(60.0)
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userService.editUserAdditionalDetails(user.getId(), calculateRequest);

        assertEquals(calculateRequest.getGender(), user.getGender());
        assertEquals(calculateRequest.getAge(), user.getAge());
        assertEquals(calculateRequest.getGoal(), user.getGoal());
        assertEquals(calculateRequest.getActivityLevel(), user.getActivityLevel());
        assertEquals(calculateRequest.getHeight(), user.getHeight());
        assertEquals(calculateRequest.getWeight(), user.getWeight());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(user);
    }

    //Load user by username
    @Test
    void throwsException_whenLoadingUserByUsername_withNonExistingUsername() {

        when(userRepository.findByUsername("Gery")).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.loadUserByUsername("Gery"));
    }

    @Test
    void testIf_returningCorrectUserDetails_whenLoadingUserByUsername() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("Gery")
                .password("111012")
                .email("gery@gmail.com")
                .role(UserRole.USER)
                .isActive(true)
                .build();

        when(userRepository.findByUsername("Gery")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("Gery");

        assertNotNull(userDetails);
        assertInstanceOf(UserAuthDetails.class, userDetails);
        UserAuthDetails userAuthDetails = (UserAuthDetails) userDetails;
        assertEquals(user.getId(), userAuthDetails.getId());
        assertEquals(user.getUsername(), userAuthDetails.getUsername());
        assertEquals(user.getPassword(), userAuthDetails.getPassword());
        assertEquals(user.getEmail(), userAuthDetails.getEmail());
        assertEquals(user.getRole(), userAuthDetails.getRole());
        assertEquals(user.isActive(), userAuthDetails.isActive());
    }

    //Save User
    @Test
    void testIf_savesUser() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        when(userRepository.save(user)).thenReturn(user);

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    //Get All Users
    @Test
    void testIf_returns_allUsers() {

        List<User> users = List.of(new User(), new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> actualUsers = userService.getAllUsers();

        assertNotNull(actualUsers);
        assertEquals(users.size(), actualUsers.size());
        assertTrue(actualUsers.containsAll(users));
        assertTrue(users.containsAll(actualUsers));
    }

    //Get User's Calories From Meals
    @Test
    void testIfReturns_rightSumOfCaloriesOfMeals() {

        Meal firstMeal = Meal.builder()
                .calories(200)
                .build();

        Meal secondMeal = Meal.builder()
                .calories(300)
                .build();

        User user = User.builder()
                .meals(List.of(firstMeal, secondMeal))
                .build();

        int actualCalories = userService.getCaloriesFromMeals(user);

        assertEquals(500, actualCalories);
    }

    //Change Roles
    @Test
    void changesUserRole_fromUserToAdmin() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.USER)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.changeRoles(user.getId());

        assertEquals(UserRole.ADMIN, user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changesUserRole_fromAdminToUser() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ADMIN)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.changeRoles(user.getId());

        assertEquals(UserRole.USER, user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    //Change Status
    @Test
    void changeUserStatus_fromActiveToInactive() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.changeStatus(user.getId());

        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changeUserStatus_fromInactiveToActive() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(false)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.changeStatus(user.getId());

        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }
}










