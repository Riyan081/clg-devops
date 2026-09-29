package com.ptfp.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubmitFeedbackSeleniumTest {

    private static WebDriver driver;
    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080");

    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void testCommuterCanSubmitFeedback() {
        try {
            driver.get(BASE_URL + "/feedback-form.html");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            WebElement routeSelectElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("route")));
            
            // Wait for routes options to load
            wait.until(d -> new Select(routeSelectElement).getOptions().size() > 1);

            Select routeSelect = new Select(routeSelectElement);
            routeSelect.selectByIndex(1);

            Select categorySelect = new Select(driver.findElement(By.id("category")));
            categorySelect.selectByValue("DELAY");

            driver.findElement(By.id("passengerName")).sendKeys("Selenium Commuter");
            driver.findElement(By.id("passengerEmail")).sendKeys("selenium@example.com");
            driver.findElement(By.id("description")).sendKeys("Morning bus was delayed by 30 minutes at Central Station.");

            driver.findElement(By.id("submit-btn")).click();

            WebElement successAlert = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("success-msg")));
            assertNotNull(successAlert);
            assertTrue(successAlert.getText().contains("Success"));
            assertTrue(successAlert.getText().contains("PTFP-"));

        } catch (Exception e) {
            captureScreenshot("SubmitFeedbackTest_Failure");
            throw e;
        }
    }

    private void captureScreenshot(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            Path destDir = Paths.get("target", "selenium-screenshots");
            Files.createDirectories(destDir);
            Files.copy(src.toPath(), destDir.resolve(testName + "_" + System.currentTimeMillis() + ".png"));
        } catch (IOException ex) {
            System.err.println("Could not capture screenshot: " + ex.getMessage());
        }
    }
}
