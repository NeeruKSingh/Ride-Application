package com.ride.model.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include. NON_NULL)
public class DriverProfileDto {

    private int id;

    @NotBlank(message = "name is mandatory")
    private String name;

    @Email(message = "Invalid email")
    @Range(min= 4, max= 15)
    @NotBlank(message = "email id is mandatory")
    private String emailId;

    @NotBlank(message = "mobile number is mandatory")
    private String mobileNo;

    private boolean isOnboarded;

    private String status;

    private int userId;

    @NotNull
    private VehicleDetailsDto vehicleDetails;
}
