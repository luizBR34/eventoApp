package com.eventoApp.web.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnreachableResourceServerErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

}
