package com.my1stle.customer.portal.persistence.model;

import com.dev1stle.scheduling.system.v1.model.Skill;
import com.my1stle.customer.portal.persistence.attribute.converter.SkillAttributeConverter;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ProductAgreementDocument;
import com.my1stle.customer.portal.service.model.ProductDiscount;
import com.my1stle.customer.portal.service.pricing.PaymentSchedule;
import com.my1stle.customer.portal.service.pricing.PricingType;
import com.my1stle.customer.portal.service.util.ProductUtil;
import com.my1stle.customer.portal.serviceImpl.product.DefaultAgreementDocumentService;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
public class ProductEntity implements Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, name = "active", columnDefinition = "boolean default true")
    private Boolean active = true;

    @Column(unique = true, nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "duration")
    private Integer duration;

    @Column(nullable = false, name = "is_schedulable", columnDefinition = "boolean default true")
    private Boolean isSchedulable = true;

    @Column(nullable = false, name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    // TODO: Remove dependency on Skill class; this should not be used directly.
    @Column(name = "skills")
    @Convert(converter = SkillAttributeConverter.class)
    private Set<Skill> skills;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_type", nullable = false)
    private PricingType pricingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_schedule", nullable = false)
    private PaymentSchedule paymentSchedule;

    @OneToOne(mappedBy = "product")
    private ProductAgreementDocumentEntity productAgreement;

    @Range(min = 0, max = 1)
    @Column(name = "required_deposit_percentage", precision = 5, scale = 4, nullable = false)
    @ColumnDefault("0")
    private BigDecimal requiredDepositPercentage = BigDecimal.ZERO;

    @OneToMany(mappedBy = "product")
    private Set<ProductDiscountEntity> discounts;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getDuration() {
        return duration;
    }

    @Override
    public boolean getIsSchedulable() {
        return isSchedulable;
    }

    @Override
    public boolean getIsSubscriptionBased() {
        return ProductUtil.isSubscriptionBasedProduct(this);
    }

    @Override
    public BigDecimal getPricePerUnit() {
        return price;
    }

    @Override
    public BigDecimal getRequiredDepositPercentage() {
        return requiredDepositPercentage;
    }

    @Override
    public Set<ProductDiscount> getDiscounts() {
        return discounts
                .stream()
                .filter(ProductDiscountEntity::getIsActive)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Skill> getSkills() {
        return skills;
    }

    @Override
    public String getProductImageUrl() {
        return imageUrl;
    }

    @Override
    public PricingType getPricingType() {
        return pricingType;
    }

    @Override
    public PaymentSchedule getPaymentSchedule() {
        return paymentSchedule;
    }

    @Override
    public Optional<ProductAgreementDocument> getProductAgreementDocument() {

        if (null == productAgreement) {
            return Optional.empty();
        }

        return DefaultAgreementDocumentService.getInstance().getByProductId(this.id);

    }

    @Override
    public boolean equals(Object o) {

        if (o == this)
            return true;
        if (!(o instanceof ProductEntity)) {
            return false;
        }
        ProductEntity product = (ProductEntity) o;
        return Objects.equals(id, product.getId());

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
