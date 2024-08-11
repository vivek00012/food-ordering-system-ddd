package com.food.ordering.system;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC ="UTC";
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order,restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated",order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        HashMap<UUID,Product> restaurantProductMap =  new HashMap<>();

        restaurant.getProducts().forEach(product->{
            restaurantProductMap.putIfAbsent(product.getId().getValue(),product);
        });

        List<Product> itemProducts = order.getItems().stream().map(item->item.getProduct()).toList();
        // we need to validate the item ordered from the customer with the restaurant product

        itemProducts.forEach(product->{
            Product restaurantProduct = restaurantProductMap.getOrDefault(product.getId().getValue(),null);
            if(restaurantProduct!=null){
                log.info("product: {} , price :{}",restaurantProduct.getName(),restaurantProduct.getPrice().getAmount());
                product.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),restaurantProduct.getPrice());
            }
        });
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()) throw new OrderDomainException("Restauranat with id "+ restaurant.getId().getValue() + " is currently not active!");
        
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id :{} is paid",order.getId().getValue());
        return new OrderPaidEvent(order,ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id :{} is approved",order.getId().getValue());

    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order is: {}",order.getId());
        return new OrderCancelledEvent(order,ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled",order.getId().getValue());

    }
}
