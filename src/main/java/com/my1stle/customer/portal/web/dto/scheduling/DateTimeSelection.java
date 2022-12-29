package com.my1stle.customer.portal.web.dto.scheduling;

import com.my1stle.customer.portal.persistence.model.ProductEntity;
import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.model.Skill;
import com.dev1stle.scheduling.system.v1.model.salesforce.SalesforceCustomerInformation;
import com.dev1stle.scheduling.system.v1.service.request.TruckRollSubmissionRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @deprecated Should not be using same class for business logic and user input.
 * use {@link DateTimeSelectionDto} instead
 */
@Deprecated
public class DateTimeSelection implements TruckRollSubmissionRequest {

    private Calendar resource;

    @DateTimeFormat(pattern = "MM/dd/yyyy h:mm a ZZZ")
    private ZonedDateTime startDateTime;
    private String notes;

    private SalesforceCustomerInformation customerInformation;
    private ProductEntity product;

    private DateTimeSelection() {

    }

    public DateTimeSelection(SalesforceCustomerInformation customerInformation, ProductEntity product) {
        this.customerInformation = customerInformation;
        this.product = product;
    }

    @Override
    public String getName() {

        // TODO better name
        return String.format("%s @ %s", product.getName(), customerInformation.getAddress());
    }

    @Override
    public Integer getDuration() {
        return this.product.getDuration().intValue();
    }

    @Override
    public List<Skill> getSkillsRequired() {
        return new ArrayList<>(this.product.getSkills());
    }

    @Override
    public Boolean getNeedsConfirmation() {
        return false;
    }

    @Override
    public String getReason() {
        return this.product.getName();
    }

    @Override
    public SalesforceCustomerInformation getSalesforceCustomerInformation() {
        return getCustomerInformation();
    }

    public SalesforceCustomerInformation getCustomerInformation() {
        return customerInformation;
    }

    public void setCustomerInformation(SalesforceCustomerInformation customerInformation) {
        this.customerInformation = customerInformation;
    }

    @Override
    public Set<Calendar> getResources() {
        return Collections.singleton(getResource());
    }

    public Calendar getResource() {
        return resource;
    }

    public void setResource(Calendar resource) {
        this.resource = resource;
    }

    @Override
    public ZonedDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    @Override
    public Boolean getChooseStartDateTime() {
        // Customer will always choose a time
        return true;
    }

    @Override
    public Boolean getFixedAppointment() {
        return true;
    }

    @Override
    public String getAppointmentNotes() {
        return notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String getStreetNumber() {
        return null;
    }

    @Override
    public String getStreetAddress() {
        return null;
    }

    @Override
    public String getCity() {
        return null;
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public String getZipCode() {
        return null;
    }

    @Override
    public String getRequestedBy() {
        // TODO return customer's account salesforce id?
        return null;
    }

    public ProductEntity getProduct() {
        return product;
    }

}
