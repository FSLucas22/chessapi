package com.lucas.chessapi.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.chessapi.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerContextHelper extends TestContextHelper {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected ContextAuthenticationHelper authContext;

    protected ResultActions result;

    protected void givenAuthenticationHappensFor(UserEntity user) {
        authContext.givenAuthenticationHappensFor(user);
    }

    protected <T> void whenRequestIsPerformed(MockHttpServletRequestBuilder request, T content) {
        try {
            result = mockMvc.perform(request.content(mapper.writeValueAsString(content)));
        } catch (Exception e) {
            error = e;
        }
    }

    protected void whenRequestIsPerformed(RequestBuilder request) {
        try {
            result = mockMvc.perform(request);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenHeaderAuthorizationMustExist() {
        assertThat(authContext.getToken()).isNotNull();
        assertThat(authContext.getHeader()).isNotNull();
    }

    protected void thenIsExpectedFromResponse(ResultMatcher... resultMatchers) throws Exception {
        result.andExpectAll(resultMatchers);
    }

}
