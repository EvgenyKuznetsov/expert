package com.evgKuznetsov.expert.web.exceptionHandling;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseBody {
    private HttpStatus httpStatus;
    private String title;

    @Setter(AccessLevel.NONE)
    private List<String> details = new ArrayList<>();

    public void addDetail(String detail) {
        details.add(detail);
    }

}
