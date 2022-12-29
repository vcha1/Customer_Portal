package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PerAdditionalQuantityFlatAmountDiscount;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.baeldung.persistence.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@Table(name = "product_discount_per_additional_quantity_flat_amount")
public class PerAdditionalQuantityFlatAmountEntity extends ProductDiscountEntity implements PerAdditionalQuantityFlatAmountDiscount {

    @Column(name = "min_applicable_quantity", nullable = false)
    private int minApplicableQuantity;

    @Column(name = "max_applicable_quantity", nullable = false)
    private int maxApplicableQuantity;

    @Column(name = "per_unit_discount_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal perUnitDiscountAmount;

    @Override
    public int getMinApplicableQuantity() {
        return minApplicableQuantity;
    }

    @Override
    public int getMaxApplicableQuantity() {
        return maxApplicableQuantity;
    }

    @Override
    public BigDecimal getFlatDiscountAmount() {
        return perUnitDiscountAmount;
    }

    @Override
    public String getDescription() {
        return String.format("Get $%s off per additional %s", new DecimalFormat("#0.##").format(perUnitDiscountAmount), this.getProduct().getName());
    }

    @Override
    //public BigDecimal getTotalDiscountAmount(User user, Installation installation, int quantity) {
    public BigDecimal getTotalDiscountAmount(User user, OdooInstallationData odooInstallationData, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Expected quantity to be greater than zero!");
        }

        if (!getIsActive()) {
            return BigDecimal.ZERO;
        }

        if (quantity < minApplicableQuantity) {
            return BigDecimal.ZERO;
        }

        int calculatedQuantity = quantity - minApplicableQuantity + 1;

        int appliedQuantity = calculatedQuantity > maxApplicableQuantity ? maxApplicableQuantity : calculatedQuantity;

        return perUnitDiscountAmount.multiply(appliedQuantity < 0 ? BigDecimal.ZERO : BigDecimal.valueOf(appliedQuantity));

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof PerAdditionalQuantityFlatAmountEntity)) return false;

        PerAdditionalQuantityFlatAmountEntity that = (PerAdditionalQuantityFlatAmountEntity) o;

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(minApplicableQuantity)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("minQuantity", minApplicableQuantity)
                .append("maxQuantity", maxApplicableQuantity)
                .append("discountAmount", perUnitDiscountAmount)
                .toString();
    }
}
