package com.ride.model.request;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
public class LoginRequest {

    @NotBlank(message = "emailId is mandatory")
    @Email(message = "Invalid email")
    @Range(min= 4, max= 15)
    private String emailId;

    @NotBlank(message = "password is mandatory")
    @Range(min= 4, max= 20)
    private String password;
}
