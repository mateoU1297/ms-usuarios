package com.pragma.users.application.dto.response;

import lombok.Data;

@Data
public class RestaurantEmployeeFeignRequest {
    private Long employeeId;
    private Long restaurantId;
}