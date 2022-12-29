package org.baeldung.spring;

import com.my1stle.customer.portal.web.dialect.ChronoDialect;
import com.my1stle.customer.portal.web.interceptor.CustomerOnboardingInterceptor;
import com.my1stle.customer.portal.web.interceptor.HttpsInterceptor;
import org.baeldung.validation.EmailValidator;
import org.baeldung.validation.PasswordMatchesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
/**
 *     @EnableWebMvc
 *     This disables spring boot's autoconfiguration.
 *     @see <a href="https://techblog.bozho.net/spring-boot-enablewebmvc-common-use-cases/">
 *         SPRING BOOT, @ENABLEWEBMVC AND COMMON USE-CASES
 *         </a>
 */
public class MvcConfig implements WebMvcConfigurer {

    public MvcConfig() {
        super();
    }

    private MessageSource messageSource;
    private CustomerOnboardingInterceptor customerOnboardingInterceptor;
    private HttpsInterceptor httpsInterceptor;

    @Autowired
    public MvcConfig(
            MessageSource messageSource,
            CustomerOnboardingInterceptor customerOnboardingInterceptor,
            HttpsInterceptor httpsInterceptor
            ){
        this.messageSource = messageSource;
        this.customerOnboardingInterceptor = customerOnboardingInterceptor;
        this.httpsInterceptor = httpsInterceptor;
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login");
        registry.addViewController("/login");
        registry.addViewController("/loginRememberMe");
        registry.addViewController("/customLogin");
        //Copied captacha template to this file.
        registry.addViewController("/registration.html");
        //registry.addViewController("/registrationCaptcha.html");
        registry.addViewController("/logout.html");
        registry.addViewController("/homepage.html");
        registry.addViewController("/expiredAccount.html");
        registry.addViewController("/badUser.html");
        registry.addViewController("/emailError.html");
        registry.addViewController("/home.html");
        registry.addViewController("/invalidSession.html");
        registry.addViewController("/console.html");
        registry.addViewController("/admin.html");
        registry.addViewController("/successRegister.html");
        registry.addViewController("/forgetPassword.html");
        registry.addViewController("/updatePassword.html");
        registry.addViewController("/changePassword.html");
        registry.addViewController("/users.html");
        registry.addViewController("/qrcode.html");
        registry.addViewController("welcomeVideo.html");
    }

    @Bean
    public ChronoDialect chronoDialect() {
        return new ChronoDialect();
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/", "/resources/");
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(this.customerOnboardingInterceptor);
        registry.addInterceptor(this.httpsInterceptor);
    }

    // beans

    @Bean
    public LocaleResolver localeResolver() {
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return cookieLocaleResolver;
    }

    // @Bean
    // public MessageSource messageSource() {
    // final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    // messageSource.setBasename("classpath:messages");
    // messageSource.setUseCodeAsDefaultMessage(true);
    // messageSource.setDefaultEncoding("UTF-8");
    // messageSource.setCacheSeconds(0);
    // return messageSource;
    // }

    @Bean
    public EmailValidator usernameValidator() {
        return new EmailValidator();
    }

    @Bean
    public PasswordMatchesValidator passwordMatchesValidator() {
        return new PasswordMatchesValidator();
    }

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

}