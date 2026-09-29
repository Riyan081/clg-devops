package com.ptfp.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class StatusWorkflowSeleniumTest {

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
    void testOfficerCanUpdateStatusWorkflow() {
        driver.get(BASE_URL + "/feedback-list.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("select")));

        WebElement firstSelect = driver.findElement(By.cssSelector("#feedback-table-body select"));
        assertNotNull(firstSelect);

        Select select = new Select(firstSelect);
        select.selectByValue("RESOLVED");

        // Wait for badge text to update asynchronously
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#feedback-table-body .badge"), "RESOLVED"));

        assertTrue(driver.getPageSource().contains("RESOLVED"));
    }
}
