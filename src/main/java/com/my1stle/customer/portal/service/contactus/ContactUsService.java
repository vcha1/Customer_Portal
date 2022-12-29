package com.my1stle.customer.portal.service.contactus;

import com.my1stle.customer.portal.web.dto.contactus.ContactUsDto;

public interface ContactUsService {

    ContactUsResult submit(ContactUsDto request);

}