package edu.virginia.sde.reviews;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new UserRepository();
    }

    @Test
    void testCreateAndGetUser() {
        User user = new User("Username", "Password");
        userRepository.createUser(user);

        User databaseUser = userRepository.getUserByUsername("Username");

        assertNotNull(databaseUser);
        assertEquals("Username", databaseUser.getUsername());
    }

    @AfterAll
    static void teardown() {
        HibernateUtil.shutdown();
    }
}
