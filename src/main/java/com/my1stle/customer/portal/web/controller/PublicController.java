package com.my1stle.customer.portal.web.controller;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Usage : extends this class if the desired endpoint is to be publicly
 * accessible. The endpoint will begin with the path of "/public"
 *
 * @see {@link org.baeldung.spring.SecSecurityConfig#configure(HttpSecurity)} for more details
 * on public endpoint configuration
 */
@Controller
@RequestMapping(value = "/public")
public abstract class PublicController {

}