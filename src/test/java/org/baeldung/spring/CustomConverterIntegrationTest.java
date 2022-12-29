package org.baeldung.spring;


import com.dev1stle.scheduling.system.v1.model.salesforce.Installation;
import org.baeldung.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CustomConverterIntegrationTest {

    @Autowired
    ConversionService conversionService;

    @Test
    public void whenConvertStringToIntegerUsingDefaultConverter_thenSuccess() {
        assertThat(conversionService.convert("25", Integer.class)).isEqualTo(25);
    }

    @Test
    public void whenConvertStringToSchedulingInstallation_thenSuccess() {
        assertThat(conversionService.convert("a060g00001krcvs", Installation.class)).isNotNull();
    }
}
