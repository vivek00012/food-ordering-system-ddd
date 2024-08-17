package com.food.ordering.system.customer.service.dataaccess.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer", schema = "customer")
@IdClass(CustomerEntityId.class)
@Entity
public class CustomerEntity {

    @Id
    private UUID id;
    @Id
    private String username;
    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
