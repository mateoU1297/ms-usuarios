package com.pragma.users.domain.spi;

public interface IRestaurantPersistencePort {

    void saveEmployeeInRestaurant(Long employeeId, Long restaurantId);
}
