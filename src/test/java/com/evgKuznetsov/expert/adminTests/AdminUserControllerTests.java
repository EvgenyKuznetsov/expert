package com.evgKuznetsov.expert.adminTests;

import com.evgKuznetsov.expert.AbstractControllerTest;
import com.evgKuznetsov.expert.JsonContentMatcher;
import com.evgKuznetsov.expert.JsonHelper;
import com.evgKuznetsov.expert.model.dto.UserTransferObject;
import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.repository.UserRepository;
import com.evgKuznetsov.expert.web.AdminUserController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.evgKuznetsov.expert.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AdminUserController tests")
public class AdminUserControllerTests extends AbstractControllerTest {

    private static final String BASE_PATH = AdminUserController.URL + "/";
    private static final String[] ignoredProperties = {"orders", "roles", "password"};
    private static JsonContentMatcher json;
    @Autowired
    UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        json = new JsonContentMatcher(ignoredProperties);
    }


    @Test
    @DisplayName("query of all exists users")
    void getAll() throws Exception {
        perform(get(BASE_PATH + "get_all"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE),
                        json.matchSizeIterable(userRepository.findAll().size(), User.class));
    }

    @Nested
    @DisplayName("query of the single user")
    class SingleUserQuery {

        @Nested
        @DisplayName("by ID")
        class ByID {

            @Test
            @DisplayName("then id is correct")
            void getById() throws Exception {

                perform(get(BASE_PATH + USER.getId()))
                        .andExpectAll(
                                status().isOk(),
                                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                                json.matchWith(USER_TO, UserTransferObject.class));
            }
        }

        @Nested
        @DisplayName("by a phone number")
        class ByPhone {

            @Test
            @DisplayName("when a phone number is correct")
            void getByPhone() throws Exception {
                String query = BASE_PATH + "get_by_phone_number";

                perform(get(query).queryParam("phone_number", USER.getPhoneNumber()))
                        .andExpectAll(
                                status().isOk(),
                                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                                json.matchWith(USER, User.class));

            }
        }

        @Nested
        @DisplayName("by an email")
        class ByEmail {

            @Test
            @DisplayName("when an email is correct")
            void getByEmail() throws Exception {
                String query = BASE_PATH + "get_by_email";

                perform(get(query).queryParam("email", USER.getEmail()))
                        .andExpectAll(
                                status().isOk(),
                                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                                json.matchWith(USER, User.class));
            }
        }
    }

    @Nested
    @DisplayName("query of updating user")
    class UpdateUser {

        @Test
        @DisplayName("when the passed user is fully correct and exists")
        void updateUser() throws Exception {
            perform(put(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonHelper.getJson(CHANGED_USER)))
                    .andExpect(status().is2xxSuccessful());

            User changedUser = userRepository.getById(USER_ID);
            assertThat(changedUser.getFullName())
                    .as("check the name is compatible with: \"%s\"", NEW_FULLNAME)
                    .isEqualTo(NEW_FULLNAME);
        }
    }

    @Nested
    @DisplayName("query of creating user")
    class NewUser {

        @Test
        @DisplayName("when the passed user is fully correct and doesn't exist")
        void newUser() throws Exception {
            User newUser = getNewUser();

            MvcResult result = perform(post(BASE_PATH + "new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonHelper.getJson(newUser)))
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                            header().exists("Location"))
                    .andReturn();

            User actual = userRepository.getByPhoneNumber(newUser.getPhoneNumber()).orElseThrow();
            Long idActual = actual.getId();
            String uriExpected = BASE_PATH + idActual;
            newUser.setId(idActual);

            assertThat(actual).as("check the actual object is compatible with: %s", newUser.toString())
                    .usingRecursiveComparison()
                    .ignoringFields(ignoredProperties)
                    .isEqualTo(newUser);

            assertThat(result.getResponse().getRedirectedUrl())
                    .as("check the actual redirected url ends with: %s", uriExpected)
                    .endsWith(uriExpected);
        }
    }

    @Nested
    @DisplayName("query of deleting user")
    class DeleteUser {

        @Test
        @DisplayName("when ID is correct")
        void deleteUser() throws Exception {
            perform(delete(BASE_PATH + USER_ID));

            assertThat(userRepository.existsById(USER_ID))
                    .as("check the user with id: %s doesn't exist anymore", USER_ID)
                    .isFalse();
        }
    }


}
