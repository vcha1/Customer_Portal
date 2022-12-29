package com.my1stle.customer.portal.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;

public class SeleniumIntegrationTestWithAuthentication {

    private final String username = "test@test.com";
    private final String password = "test";

    @Value("${local.server.port}")
    private int port;

    private WebDriver driver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void init() {
        User user = userRepository.findByEmail(this.getUsername());
        if (user == null) {
            user = new User();
            user.setFirstName("Test");
            user.setLastName("Test");
            user.setPassword(passwordEncoder.encode(this.getPassword()));
            user.setEmail(this.getUsername());
            user.setEnabled(true);
            user.setTimeZone(ZoneId.systemDefault().getId());
            userRepository.save(user);
        } else {
            user.setPassword(passwordEncoder.encode(this.getPassword()));
            userRepository.save(user);
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");
        this.driver = new ChromeDriver(options);
    }

    /**
     * Navigates to relative url and and logs in user.
     */
    public WebDriverWait navigateWithAuthentication(String relativeUrl) {
        final String url = String.format("%s/%s", this.getBaseUrl(), relativeUrl);

        WebDriver driver = this.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.get(url);

        WebElement usernameElement = driver.findElement(By.name("username"));
        WebElement passwordElement = driver.findElement(By.name("password"));
        WebElement login = driver.findElement(By.name("submit"));

        usernameElement.sendKeys(this.getUsername());
        passwordElement.sendKeys(this.getPassword());
        login.submit();

        return wait ;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBaseUrl() {
        return "http://localhost:" + this.port;
    }
}
