package com.pragma.users.infrastructure.out.feign.adapter;

import com.pragma.users.application.dto.response.RestaurantEmployeeFeignRequest;
import com.pragma.users.domain.spi.IRestaurantPersistencePort;
import com.pragma.users.infrastructure.out.feign.client.IRestaurantFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantFeignAdapter implements IRestaurantPersistencePort {

    private final IRestaurantFeignClient restaurantFeignClient;
    private final HttpServletRequest httpServletRequest;

    @Override
    public void saveEmployeeInRestaurant(Long employeeId, Long restaurantId) {
        String userId = httpServletRequest.getHeader("X-User-Id");
        String roles = httpServletRequest.getHeader("X-User-Roles");

        RestaurantEmployeeFeignRequest request = new RestaurantEmployeeFeignRequest();
        request.setEmployeeId(employeeId);
        request.setRestaurantId(restaurantId);

        restaurantFeignClient.saveRestaurantEmployee(request, userId, roles);
    }
}
