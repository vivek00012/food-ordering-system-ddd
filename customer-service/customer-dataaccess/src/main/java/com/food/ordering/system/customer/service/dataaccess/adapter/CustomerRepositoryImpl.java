package com.food.ordering.system.customer.service.dataaccess.adapter;
import com.food.ordering.system.customer.service.dataaccess.entity.CustomerEntity;
import com.food.ordering.system.customer.service.dataaccess.mapper.CustomerDataAccessMapper;
import com.food.ordering.system.customer.service.dataaccess.repository.CustomerJpaRepository;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;
import com.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;

import java.util.Optional;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository,
                                  CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }


    @Override
    public Customer saveCustomer(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(this.customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }

    @Override
    public void deleteCustomer(Customer customer) {
        this.customerJpaRepository.delete(customerDataAccessMapper.customerToCustomerEntity(customer));

    }

    @Override
    public Optional<Customer>  findCustomerInformation(CustomerId customerId) {
        Optional<CustomerEntity> customer = this.customerJpaRepository.findById(
                customerId.getValue()
                );
        return customer.map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    public Optional<Customer> findCustomerByUsername(String username) {
        Optional<CustomerEntity> customer = this.customerJpaRepository.findByUsername(username);
        return customer.map(customerDataAccessMapper::customerEntityToCustomer);
    }


}
