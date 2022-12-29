package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ProductDiscount;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity(name = "product_discount")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ProductDiscountEntity implements ProductDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "disabled", nullable = false, columnDefinition = "boolean default false")
    private boolean disabled = false;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean getIsActive() {
        return !disabled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("product", product)
                .append("name", name)
                .append("disabled", disabled)
                .toString();
    }

}
