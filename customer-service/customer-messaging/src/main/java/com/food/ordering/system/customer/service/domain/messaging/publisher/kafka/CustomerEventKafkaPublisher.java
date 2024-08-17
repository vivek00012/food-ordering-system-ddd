package com.food.ordering.system.customer.service.domain.messaging.publisher.kafka;

import com.food.ordering.system.customer.service.domain.config.CustomerServiceConfigData;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.messaging.mapper.CustomerMessagingDataMapper;
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessageRequestPublisher;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerEventKafkaPublisher implements CustomerMessageRequestPublisher {

    private final CustomerMessagingDataMapper customerMessagingDataMapper;
    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;
    private final CustomerServiceConfigData customerServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public CustomerEventKafkaPublisher(CustomerMessagingDataMapper customerMessagingDataMapper,
                                       KafkaProducer<String, CustomerAvroModel> kafkaProducer,
                                       CustomerServiceConfigData customerServiceConfigData,
                                       KafkaMessageHelper kafkaMessageHelper) {
        this.customerMessagingDataMapper = customerMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.customerServiceConfigData = customerServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(CustomerCreatedEvent customerCreatedEvent) {


        String customerId = customerCreatedEvent.getCustomer().getId().getValue().toString();

        log.info("Received Customer  for order id: {}",
                customerId);

        try {
            CustomerAvroModel customerAvroModel =
                    customerMessagingDataMapper
                            .customerCreatedEventToCustomerAvroModel(
                                    customerCreatedEvent);

            kafkaProducer.send(customerServiceConfigData.getCustomerTopicName(),
                    customerAvroModel.getId(),
                    customerAvroModel,
                    (result,ex)-> {
                        if (ex == null) {
                            RecordMetadata metadata = result.getRecordMetadata();
                            log.info("Received successful response from Kafka for customer id: {}" +
                                            " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                                    customerId,
                                    metadata.topic(),
                                    metadata.partition(),
                                    metadata.offset(),
                                    metadata.timestamp()
                            );
                        } else {
                            log.error("Error while sending {} " + "message {} to topic ",
                                    customerAvroModel,
                                    customerAvroModel.toString(), customerServiceConfigData.getCustomerTopicName(), ex);

                        }
                    }
        );


            log.info("CustomerCreatedEvent sent to kafka for customer id: {}",
                    customerAvroModel.getId());
        } catch (Exception e) {
            log.error("Error while sending CustomerEvent to kafka for customer id: {} " +
                    " error: {}", customerCreatedEvent.getCustomer().getId(), e.getMessage());
        }


    }
}
