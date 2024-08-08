package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreatedCommandHandler;

    private final OrderTrackCommandHandler orderTrackCommandHandler;

    public OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreatedCommandHandler, OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCreatedCommandHandler = orderCreatedCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }


    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreatedCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
