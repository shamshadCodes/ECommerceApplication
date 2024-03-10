package com.scaler.ECommerceProductService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
//    @GeneratedValue(generator = "uuidGenerator")
//    @GenericGenerator(name="uuidGenerator", strategy = "uuid2")
    @Column(name="id", columnDefinition = "binary(16)", nullable = false, updatable = false)
    private UUID id;
}
