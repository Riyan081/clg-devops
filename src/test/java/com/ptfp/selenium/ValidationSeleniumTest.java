package com.ptfp.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationSeleniumTest {

    private static WebDriver driver;
    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080");

    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void testFormValidationPreventsEmptySubmission() {
        driver.get(BASE_URL + "/feedback-form.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit-btn")));
        
        // Remove HTML5 'required' attributes to test JS / server-side fallback validation
        ((JavascriptExecutor) driver).executeScript("document.getElementById('route').removeAttribute('required');");
        ((JavascriptExecutor) driver).executeScript("document.getElementById('description').removeAttribute('required');");

        submitBtn.click();

        WebElement errorAlert = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("error-msg")));
        assertNotNull(errorAlert);
        assertTrue(errorAlert.getText().contains("required") || errorAlert.getText().contains("Please complete"));
    }
}
