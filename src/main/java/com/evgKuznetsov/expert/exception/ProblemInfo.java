package com.evgKuznetsov.expert.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProblemInfo {

    private HttpStatus httpStatus;
    private Set<String> details = new HashSet<>();

    public ProblemInfo(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void addDetail(@NonNull String detail) {
        details.add(detail);
    }
}
