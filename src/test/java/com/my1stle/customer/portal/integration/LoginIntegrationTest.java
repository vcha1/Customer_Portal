package com.my1stle.customer.portal.integration;

import org.baeldung.Application;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTest extends SeleniumIntegrationTestWithAuthentication {


    @Test
    public void validLogin() {

        WebDriverWait wait = this.navigateWithAuthentication("console.html");

        wait.until(textToBePresentInElementLocated(By.tagName("body"),
                "This is the landing page for the admin"));

    }
}

