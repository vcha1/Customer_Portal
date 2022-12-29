package com.my1stle.customer.portal.persistence.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "payment_method")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "convenience_cost_fixed", precision = 10, scale = 2, nullable = false)
    private BigDecimal convenienceCostFixed;

    @Range(min = 0, max = 1)
    @Column(name = "convenience_cost_percentage", precision = 5, scale = 4, nullable = false)
    private BigDecimal convenienceCostPercentage;

    private PaymentMethodEntity() {

    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getConvenienceCostFixed() {
        return convenienceCostFixed;
    }

    public BigDecimal getConvenienceCostPercentage() {
        return convenienceCostPercentage;
    }

}
