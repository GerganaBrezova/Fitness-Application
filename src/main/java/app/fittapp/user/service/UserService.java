package app.fittapp.user.service;

import app.fittapp.calculator.service.CalculatorService;
import app.fittapp.exceptions.*;
import app.fittapp.meal.model.Meal;
import app.fittapp.security.UserAuthDetails;
import app.fittapp.user.model.User;
import app.fittapp.user.model.UserRole;
import app.fittapp.user.repository.UserRepository;
import app.fittapp.web.dto.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CalculatorService calculatorService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CalculatorService calculatorService, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.calculatorService = calculatorService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public User register(RegisterRequest registerRequest) {

        Optional<User> optionalUserByUsername = userRepository.findByUsername(registerRequest.getUsername());
        Optional<User> optionalUserByEmail = userRepository.findByEmail(registerRequest.getEmail());

        if (optionalUserByUsername.isPresent()) {
            throw new UsernameAlreadyExists("Username %s already exists.".formatted(registerRequest.getUsername()));
        }

        if (optionalUserByEmail.isPresent()) {
            throw new EmailAlreadyExists("Email %s already exists.".formatted(registerRequest.getEmail()));
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordsDoNotMatch("Passwords do not match.");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .points(100)
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .build();

        userRepository.save(user);

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .build();


        applicationEventPublisher.publishEvent(event);

        log.info("Successfully created account for username %s with id %s.".formatted(user.getUsername(), user.getId()));

        return user;
    }

    public void calculateUserDailyIntake(UUID userId, CalculateRequest calculateRequest) {

        User user = getUserById(userId);

        double dailyIntake = calculatorService.calculateDailyIntake(calculateRequest);

        user.setDailyIntake(dailyIntake);

        userRepository.save(user);
    }


    public void editUserDetails(EditRequest editRequest, UUID id) {
        User user = getUserById(id);

        user.setUsername(editRequest.getUsername());
        user.setEmail(editRequest.getEmail());
        user.setName(editRequest.getName());
        user.setProfilePictureUrl(editRequest.getProfilePictureUrl());

        userRepository.save(user);
    }

    public void editUserAdditionalDetails(UUID userId, CalculateRequest calculateRequest) {
        User user = getUserById(userId);

        user.setHeight(calculateRequest.getHeight());
        user.setWeight(calculateRequest.getWeight());
        user.setAge(calculateRequest.getAge());
        user.setGender(calculateRequest.getGender());
        user.setActivityLevel(calculateRequest.getActivityLevel());
        user.setGoal(calculateRequest.getGoal());

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFound {

        User user = getUserByUsername(username);

        return new UserAuthDetails(user.getId(), username, user.getPassword(), user.getEmail(), user.getRole(), user.isActive());
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFound("User %s was not found.".formatted(username)));
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFound("User with id %s not found.".formatted(id)));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public int getCaloriesFromMeals(User user) {

        return user.getMeals().stream()
                .mapToInt(Meal::getCalories)
                .sum();
    }

    public void changeRoles(UUID userId) {

        User user = getUserById(userId);

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else if (user.getRole() == UserRole.ADMIN) {
            user.setRole(UserRole.USER);
        }

        userRepository.save(user);
    }

    public void changeStatus(UUID userId) {

        User user = getUserById(userId);

        user.setActive(!user.isActive());

        userRepository.save(user);
    }

}
