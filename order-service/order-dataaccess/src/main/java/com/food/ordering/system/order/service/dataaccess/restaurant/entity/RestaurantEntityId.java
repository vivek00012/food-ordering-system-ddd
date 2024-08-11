package com.food.ordering.system.order.service.dataaccess.restaurant.entity;

import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RestaurantEntityId.class)
public class RestaurantEntityId implements Serializable {
    @Id
    private UUID restaurantId;
    @Id
    private UUID productId;
}
