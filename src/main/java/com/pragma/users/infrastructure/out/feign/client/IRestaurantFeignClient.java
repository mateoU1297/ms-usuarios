package com.pragma.users.infrastructure.out.feign.client;

import com.pragma.users.application.dto.response.RestaurantEmployeeFeignRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-restaurants")
public interface IRestaurantFeignClient {

    @PostMapping("/api/v1/restaurants/owner/employee")
    void saveRestaurantEmployee(@RequestBody RestaurantEmployeeFeignRequest request,
                                @RequestHeader("X-User-Id") String userId,
                                @RequestHeader("X-User-Roles") String roles);
}
