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

public class DashboardSeleniumTest {

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
    void testDashboardLoadsMetricsAndCategories() {
        driver.get(BASE_URL + "/dashboard.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement metricTotal = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("metric-total")));
        assertNotNull(metricTotal);

        // Wait for async fetch to populate total count (> 0 due to seed data)
        wait.until(d -> !metricTotal.getText().isEmpty() && !metricTotal.getText().equals("0"));

        WebElement breakdown = driver.findElement(By.id("category-breakdown"));
        assertNotNull(breakdown);
        assertTrue(breakdown.getText().contains("DELAY") || breakdown.getText().contains("CLEANLINESS"));
    }
}
