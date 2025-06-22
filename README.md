Fitness Application

A full-featured Spring Boot fitness web app that helps users stay on track with their fitness goals by managing workouts, meals, points, and in-app notifications through a microservice and event-driven architecture.

---

Key Features

Authentication & Authorization
- Secure login and registration with Spring Security

Daily Fitness Management
- Daily Intake Calculator** – Calculates personalized calorie needs based on user's goal (weight loss, maintenance, gain)
- Meal Tracking – Users can log up to 3 meals daily; app warns if calorie intake exceeds limits
- Workout Completion – Users can complete workouts and earn points
- Points System – Completing a workout grants user points
- Unlocking New Workout Plans - Users can unlock additional workout plans by the earned points through their activities in the app
  
Community & Interaction
- Community Forum – Users can post, like and read comments
- Inbox Notifications – Real-time in-app notifications for events like:
  - Successful registration (Welcome message)
  - Workout completion
  - Points earned

Event-Driven Architecture
- Apache Kafka used to communicate between services
- Notifications and user points triggered via Kafka events

Microservices
- Uses OpenFeign to integrate with `inbox-notifications-microservice`

---

Tech Stack

| Layer           | Technologies                             |
|-----------------|------------------------------------------|
| Backend         | Java 17, Spring Boot, Spring MVC         |
| Auth & Security | Spring Security                          |
| View Layer      | Thymeleaf, HTML, CSS                     |
| Messaging       | Apache Kafka                             |
| Microservices   | Spring Cloud OpenFeign                   |
| Database        | MySQL, Spring Data JPA                   |
| Build Tool      | Maven                                    |

---

Run the Project Locally

Prerequisites

- Java 17+
- MySQL running
- Kafka and Zookeeper running
- Maven
