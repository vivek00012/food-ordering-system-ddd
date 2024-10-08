package com.food.ordering.system.order.service.domain;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;

    // usage of output port in domain application service
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher, OrderDataMapper orderDataMapper) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
        this.orderDataMapper = orderDataMapper;
    }


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        OrderCreatedEvent  orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id : {}",orderCreatedEvent.getOrder().getId().getValue());
//        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);//via output port
        orderCreatedEvent.fire();
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(),"Order created successfully");
    }


}
