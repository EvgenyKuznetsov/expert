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
    private List<String> details = new ArrayList<>();

    public ProblemInfo(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void addDetail(@NonNull String detail) {
        details.add(detail);
    }
}
