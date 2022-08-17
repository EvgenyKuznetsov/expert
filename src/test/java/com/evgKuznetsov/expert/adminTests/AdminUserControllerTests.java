package com.evgKuznetsov.expert.adminTests;

import com.evgKuznetsov.expert.AbstractControllerTest;
import com.evgKuznetsov.expert.JsonContentMatcher;
import com.evgKuznetsov.expert.JsonHelper;
import com.evgKuznetsov.expert.model.dto.UserTo;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.repository.UserRepository;
import com.evgKuznetsov.expert.web.AdminUserController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Locale;

import static com.evgKuznetsov.expert.JsonHelper.getJson;
import static com.evgKuznetsov.expert.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWithIgnoringCase;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AdminUserController tests:")
public class AdminUserControllerTests extends AbstractControllerTest {

    private static final String BASE_PATH = AdminUserController.URL + "/";
    private static final String[] ignoredProperties = {"orders", "roles", "password"};
    private static JsonContentMatcher json;
    private final String detailPatten = "%s : %s";

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageSource messageSource;

    @BeforeAll
    static void setUp() {
        json = new JsonContentMatcher(ignoredProperties);
    }

    @Test
    @DisplayName("all exists users")
    void getAll() throws Exception {
        perform(get(BASE_PATH + "get_all"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE),
                        json.matchSizeIterable(userRepository.findAll().size(), User.class));

    }

    @Nested
    @DisplayName("single user")
    class SingleUserQuery {

        @Nested
        @DisplayName("by ID")
        class ByID {

            @Test
            @DisplayName("id is correct")
            void getById() throws Exception {
                UserTo userTo = getUserTo();

                perform(get(BASE_PATH + USER.getId()))
                        .andExpectAll(
                                status().isOk(),
                                content().contentTypeCompatibleWith(APPLICATION_JSON),
                                json.matchWith(userTo, UserTo.class));
            }

            @ParameterizedTest
            @DisplayName("there isn't row with this id")
            @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
            void getUserNotExist(Locale locale) throws Exception {
                String path = BASE_PATH + 100;
                String detail = messageSource.getMessage("validation.data.notfound", null, locale);
                String message = String.format(detailPatten, path, detail);

                perform(get(path)
                        .locale(locale))
                        .andExpectAll(
                                status().isNotFound(),
                                content().contentTypeCompatibleWith(APPLICATION_JSON),
                                jsonPath("$.details[0]").value(endsWithIgnoringCase(message)));
            }

            @ParameterizedTest
            @DisplayName("id is incorrect")
            @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
            void passedIncorrectId(Locale locale) throws Exception {
                String path = BASE_PATH + "0";
                String message = messageSource.getMessage("validation.data.positive", null, locale);

                perform(get(path)
                        .locale(locale))
                        .andExpectAll(
                                status().isBadRequest(),
                                content().contentTypeCompatibleWith(APPLICATION_JSON),
                                jsonPath("$.details[0]").value(containsString(message)));
            }

        }
    }

    @Nested
    @DisplayName("updating user")
    class UpdateUser {
        private UserTo changedUser;
        private Long id;
        private MockHttpServletRequestBuilder builder;

        @BeforeEach
        void beforeEach() {
            changedUser = getChangedUser();
            id = changedUser.getId();

            builder = put(BASE_PATH + id)
                    .contentType(APPLICATION_JSON)
                    .locale(new Locale("ru", "RU"))
                    .characterEncoding("UTF-8")
                    .content(getJson(changedUser));
        }

        @Test
        @DisplayName("is2xxSuccessful")
        void updateUser() throws Exception {
            perform(builder).andExpect(status().is2xxSuccessful());

            User changedUser = userRepository.getById(USER_ID);
            assertThat(changedUser.getFullName())
                    .as("check the name is compatible with: \"%s\"", NEW_FULLNAME)
                    .isEqualTo(NEW_FULLNAME);
        }

        @ParameterizedTest
        @DisplayName("ids are inconsistent")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void inconsistentIds(Locale locale) throws Exception {
            long neverRegistered = 100L;
            String message = messageSource.getMessage("validation.data.conflict", null, locale);
            message = String.format(message, "user.id");

            perform(put(BASE_PATH + neverRegistered)
                    .locale(locale)
                    .contentType(APPLICATION_JSON)
                    .content(getJson(changedUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("full name is empty")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void fullNameIsEmpty(Locale locale) throws Exception {
            changedUser.setFullName("      "); // 6 символов
            String detail = messageSource.getMessage("validation.data.not-blank", null, locale);
            String message = String.format(detailPatten, "fullName", detail);

            perform(builder.locale(locale).content(getJson(changedUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("full name is too long")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void fullNameIsTooLong(Locale locale) throws Exception {
            changedUser.setFullName("f".repeat(256));
            String detail = messageSource.getMessage("validation.data.length", null, locale);
            String message = String.format(detailPatten, "fullName", detail);

            perform(builder.locale(locale).content(getJson(changedUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("full name is too long")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void fullNameIsTooShort(Locale locale) throws Exception {
            changedUser.setFullName("four");
            String detail = messageSource.getMessage("validation.data.length", null, locale);
            String message = String.format(detailPatten, "fullName", detail);

            perform(builder.locale(locale).content(getJson(changedUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("email has incorrect format")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void emailHasIncorrectFormat(Locale locale) throws Exception {
            changedUser.setEmail(changedUser.getEmail().replace("@", ""));
            String detail = messageSource.getMessage("validation.data.email-format", null, locale);
            String message = String.format(detailPatten, "email", detail);

            perform(builder.locale(locale).content(getJson(changedUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details").value(message));
        }

        @ParameterizedTest
        @DisplayName("phone number has incorrect format")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void phoneNumberHasIncorrectFormat(Locale locale) throws Exception {
            changedUser.setPhoneNumber(changedUser.getPhoneNumber().replace(")", ""));
            String detail = messageSource.getMessage("validation.data.phone-format", null, locale);
            String message = String.format(detailPatten, "phoneNumber", detail);

            perform(builder.locale(locale).content(getJson(changedUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details").value(message));
        }

    }

    @Nested
    @DisplayName("creating user")
    class NewUser {
        private User newUser;
        private MockHttpServletRequestBuilder builder;

        @BeforeEach
        void beforeEach() {
            this.newUser = getNewUser();
            newUser.setId(null);

            builder = post(BASE_PATH + "new")
                    .contentType(APPLICATION_JSON)
                    .locale(new Locale("ru", "RU"))
                    .characterEncoding("UTF-8")
                    .content(getJson(newUser));
        }

        @Test
        @DisplayName("new user is correct")
        void newUser() throws Exception {
            MvcResult result = perform(builder)
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            header().exists("Location"))
                    .andReturn();

            UserTo userTo = JsonHelper.readValue(result.getResponse().getContentAsString(), UserTo.class);
            User actual = userRepository.getById(userTo.getId());
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

        @ParameterizedTest
        @DisplayName("id isn't null")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void idNotNull(Locale locale) throws Exception {
            newUser.setId(55L);
            String message = String.format(
                    messageSource.getMessage("validation.data.conflict", null, locale),
                    "user.id");

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("full name is too short")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void fullNameTooShort(Locale locale) throws Exception {
            newUser.setFullName("Anna");
            String message = String.format(
                    detailPatten,
                    "fullName",
                    messageSource.getMessage("validation.data.length", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("full name is too long")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void fullNameIsTooLong(Locale locale) throws Exception {
            newUser.setFullName("A".repeat(256));
            String message = String.format(
                    detailPatten,
                    "fullName",
                    messageSource.getMessage("validation.data.length", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("full name is empty")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void fullNameIsEmpty(Locale locale) throws Exception {
            newUser.setFullName(" ".repeat(5));
            String message = String.format(
                    detailPatten,
                    "fullName",
                    messageSource.getMessage("validation.data.not-blank", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("email is incorrect")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void emailHasIncorrectFormat(Locale locale) throws Exception {
            newUser.setEmail(newUser.getEmail().replace("@", ""));
            String message = String.format(
                    detailPatten,
                    "email",
                    messageSource.getMessage("validation.data.email-format", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("email is empty")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void emailIsEmpty(Locale locale) throws Exception {
            newUser.setEmail("");
            String message = String.format(
                    detailPatten,
                    "email",
                    messageSource.getMessage("validation.data.not-blank", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("password is too short")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void passwordIsTooShort(Locale locale) throws Exception {
            newUser.setPassword("1234");
            String message = String.format(
                    detailPatten,
                    "password",
                    messageSource.getMessage("validation.data.length", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("password is blank")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void passwordIsBlank(Locale locale) throws Exception {
            newUser.setPassword(" ".repeat(6));
            String message = String.format(
                    detailPatten,
                    "password",
                    messageSource.getMessage("validation.data.not-blank", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("password is too long")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void passwordIsTooLong(Locale locale) throws Exception {
            newUser.setPassword("f".repeat(101));
            String message = String.format(
                    detailPatten,
                    "password",
                    messageSource.getMessage("validation.data.length", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("phone number is incorrect")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void phoneIsIncorrect(Locale locale) throws Exception {
            newUser.setPhoneNumber(newUser.getPhoneNumber().replace(")", ""));
            String message = String.format(
                    detailPatten,
                    "phoneNumber",
                    messageSource.getMessage("validation.data.phone-format", null, locale));

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details[0]").value(message));
        }

        @ParameterizedTest
        @DisplayName("role is not exist")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void roleIsNotExist(Locale locale) throws Exception {
            Role nonExistent = new Role();
            nonExistent.setId(1L);
            nonExistent.setRole("non-existent");
            newUser.clearRoles();
            newUser.addRole(nonExistent);
            String message = String.format(
                    messageSource.getMessage("validation.data.conflict", null, locale),
                    "user.role");

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details").value(message));
        }

        @ParameterizedTest
        @DisplayName("role has an incorrect id")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void roleHasIncorrectId(Locale locale) throws Exception {
            Role idIncorrect = new Role();
            idIncorrect.setId(0L);
            idIncorrect.setRole("expert");
            newUser.clearRoles();
            newUser.addRole(idIncorrect);
            String message = String.format(
                    messageSource.getMessage("validation.data.conflict", null, locale),
                    "user.role");

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details").value(message));
        }

        @ParameterizedTest
        @DisplayName("role has an incorrect id")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void conflictRolesData(Locale locale) throws Exception {
            Role roleWithConflict = new Role(2L, "expert"); // [id = 1, role = "expert"] has saved
            newUser = getNewUser();
            newUser.clearRoles();
            newUser.addRole(roleWithConflict);
            String message = String.format(
                    messageSource.getMessage("validation.data.conflict", null, locale),
                    "user.role");

            perform(builder.locale(locale).content(getJson(newUser)))
                    .andExpectAll(
                            status().isUnprocessableEntity(),
                            content().contentTypeCompatibleWith(APPLICATION_JSON),
                            jsonPath("$.details").value(message));
        }
    }

    @Nested
    @DisplayName("deleting user")
    class DeleteUser {

        @Test
        @DisplayName("id is correct")
        void deleteUser() throws Exception {
            perform(delete(BASE_PATH + USER_ID));

            assertThat(userRepository.existsById(USER_ID))
                    .as("check the user with id: %s doesn't exist anymore", USER_ID)
                    .isFalse();
        }

        @ParameterizedTest
        @DisplayName("id is incorrect")
        @MethodSource("com.evgKuznetsov.expert.AbstractControllerTest#getLocale")
        void idIsNotCorrect(Locale locale) throws Exception {
            String message = "id : " + messageSource.getMessage("validation.data.positive", null, locale);
            perform(delete(BASE_PATH + 0L).locale(locale))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.details").value(message));
        }
    }


}
