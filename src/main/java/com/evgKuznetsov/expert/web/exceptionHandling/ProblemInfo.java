package com.evgKuznetsov.expert.web.exceptionHandling;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProblemInfo {

    private HttpStatus httpStatus;
    private String title;
    private List<String> details = new ArrayList<>();

    public void addDetail(@NonNull String detail) {
        details.add(detail);
    }

    public ProblemInfo(HttpStatus httpStatus, String title) {
        this.httpStatus = httpStatus;
        this.title = title;
    }
}
