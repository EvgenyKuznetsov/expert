package com.evgKuznetsov.expert.user;

import com.evgKuznetsov.expert.AbstractControllerTest;
import com.evgKuznetsov.expert.controller.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminControllerTest extends AbstractControllerTest {

    @Autowired
    private AdminController adminController;

    private static final String URL = AdminController.ADMIN_PATH + "/";

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "users"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "users/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
