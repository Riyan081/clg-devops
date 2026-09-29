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

public class SearchFeedbackSeleniumTest {

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
    void testSearchAndFilterFeedback() {
        driver.get(BASE_URL + "/feedback-list.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement tableBody = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("feedback-table-body")));
        assertNotNull(tableBody);

        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.sendKeys("ROUTE-101");
        driver.findElement(By.id("search-btn")).click();

        wait.until(d -> driver.findElements(By.tagName("tr")).size() >= 1);
        assertTrue(driver.getPageSource().contains("ROUTE-101") || driver.getPageSource().contains("PTFP-"));
    }
}
