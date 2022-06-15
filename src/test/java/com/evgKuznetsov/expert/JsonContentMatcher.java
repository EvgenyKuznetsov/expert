package com.evgKuznetsov.expert;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static com.evgKuznetsov.expert.JsonHelper.readIterable;
import static com.evgKuznetsov.expert.JsonHelper.readValue;
import static org.assertj.core.api.Assertions.assertThat;

public record JsonContentMatcher(String... ignoredProperties) {

    public <T> ResultMatcher matchWith(T expected, Class<T> type) {
        return result -> {
            T actual = readValue(result.getResponse().getContentAsString(), type);
            assertThat(actual).usingRecursiveComparison().ignoringFields(ignoredProperties).isEqualTo(expected);
        };
    }

    public <T> ResultMatcher matchSizeIterable(int expected, Class<T> type) {
        return result -> {
            List<T> actual = readIterable(result.getResponse().getContentAsString(), type);
            assertThat(actual.size()).isEqualTo(expected);
        };
    }
}

