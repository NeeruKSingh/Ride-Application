package com.ride.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessBaseResponse {

    private String code;
    private String message;
}
