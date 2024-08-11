package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import java.util.List;
import java.util.UUID;

// create a aggregate root of order
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryaddress;

    private final Money price;

    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryaddress = builder.deliveryaddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        intializeOrderItems();
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem ->{
            validateItemPrice(orderItem);
            return orderItem.getSubtotal();
        }).reduce(Money.Zero,Money::add);

        if(!price.equals(orderItemsTotal)){
            throw new OrderDomainException("Total Price "+ price.getAmount() + " is not equal to Order items total: "+ orderItemsTotal.getAmount() +"!");
        }


    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid())
            throw new OrderDomainException("Order item price : "+ orderItem.getPrice().getAmount() +
                " is not valid for product "+ orderItem.getProduct().getId().getValue());
    }

    private void validateTotalPrice() {
        if(price ==null || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price must be greater than zero");
        }
        
    }

    private void validateInitialOrder() {
        if(orderStatus !=null  || getId() !=null){
            throw new OrderDomainException("Order is not in correct state for initialization");
        }
    }

    private void intializeOrderItems(){
        long itemId =1;

        for(OrderItem item:items){
            item.intialiseOrderItem(super.getId(),new OrderItemId(itemId++));
        }

    }

    // business logic for setting the order status with respect to actions

    // After confirming the payment from payment domain, order status is changed to paid.
    public void pay(){
        if(orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("Order is not in correct state for pay operation");
        }
        orderStatus = OrderStatus.PAID;
    }

    // this status will be set by restaurant domain application service via event actions
    public void approve(){
        if(orderStatus!=OrderStatus.PAID){
            throw new OrderDomainException("Order is not in correct state for approve operation");
        }
        orderStatus = OrderStatus.APPROVED;

    }

    // cancelling will be initated after the order is paid and when restaurant cancelled the order
    public void initCancel(List<String> failureMessages){
        if(orderStatus!=OrderStatus.PAID)
             throw new OrderDomainException("Order is not in correct state for initCancel operation");

        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);

    }

    // will be cancelled after the restraument cancelled the order and order status is in cancelling state
    //or
    // cancelled when the order is in pending state, and payment didnot happended
    public void cancel(List<String> failureMessages){
        if(!(orderStatus == orderStatus.PENDING || orderStatus == orderStatus.CANCELLING)){
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = OrderStatus.CANCELLED;

        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages!=null &&  failureMessages!=null){
            this.failureMessages.addAll(failureMessages.stream().filter(message->!message.isEmpty()).toList());
        }

        if(this.failureMessages==null){
            this.failureMessages = failureMessages;

        }
    }


    public static Builder builder() {
        return new Builder();
    }
    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryaddress() {
        return deliveryaddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryaddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }


        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryaddress(StreetAddress val) {
            deliveryaddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
