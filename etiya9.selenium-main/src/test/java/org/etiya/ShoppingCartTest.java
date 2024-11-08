package org.etiya;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.BooleanAssert;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShoppingCartTest {
    FirefoxDriver firefoxDriver;
    WebDriverWait wait;
    SoftAssertions softAssertions;
    String testName;

    @BeforeEach
    public void setup() {
        firefoxDriver = new FirefoxDriver();
        wait = new WebDriverWait(firefoxDriver, Duration.ofSeconds(10));
        softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void cleanup(){
        firefoxDriver.quit();
    }

    public void login(String username, String password){
        firefoxDriver.get("https://www.saucedemo.com/");
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    @Test
    public void testErrorCheckoutWithEmptyCart() throws InterruptedException {
        testName = "testErrorCheckoutWithEmptyCart";
        login("standard_user","secret_sauce");
        WebElement cart = firefoxDriver.findElement(By.cssSelector("[data-test='shopping-cart-link']"));
        softAssertions
                .assertThat(cart.isDisplayed())
                .as("Cart butonu bulunamadı.");
        cart.click();
//        Thread.sleep(15000);
        WebElement checkoutBtn = firefoxDriver.findElement(By.cssSelector("[data-test='checkout']"));
        softAssertions
                .assertThat(checkoutBtn.isDisplayed())
                .as("Checkout butonu bulunamadı.");
        checkoutBtn.click();

        WebElement firstName = firefoxDriver.findElement(By.cssSelector("[data-test='firstName']"));
        firstName.sendKeys("asdsdf");
        WebElement lastName = firefoxDriver.findElement(By.cssSelector("[data-test='lastName']"));
        lastName.sendKeys("asdsdf");
        WebElement postalCode = firefoxDriver.findElement(By.cssSelector("[data-test='postalCode']"));
        postalCode.sendKeys("12345");
        WebElement continueBtn = firefoxDriver.findElement(By.cssSelector("[data-test='continue']"));
        continueBtn.click();
        WebElement finishBtn = firefoxDriver.findElement(By.cssSelector("[data-test='finish']"));
        finishBtn.click();
        WebElement completeText = firefoxDriver.findElement(By.cssSelector("[data-test='complete-text']"));
        String actualResult = completeText.getText();
        String expectedResult = "Your order has been dispatched, and will arrive just as fast as the pony can get there!";
        assertEquals(expectedResult, actualResult, "Do not match.");
    }
    @Test
    public void testAddToCartFunction() {
        testName = "testAddToCartFunction";
        login("standard_user","secret_sauce");
        //data-test="add-to-cart-sauce-labs-backpack"
        WebElement addToCartBtn = firefoxDriver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']"));
        softAssertions
                .assertThat(addToCartBtn.isDisplayed())
                .as("Add to cart butonu bulunamadı.");
        addToCartBtn.click();
        WebElement removeBtn = firefoxDriver.findElement(By.cssSelector("[data-test='remove-sauce-labs-backpack']"));
        String expectedResult = "remove";
        String actualResult = removeBtn.getText().toLowerCase();
        softAssertions
                .assertThat(removeBtn.isDisplayed())
                .as("Remove butonu bulunamadı.");
        assertEquals(expectedResult, actualResult, "Do not match");
        WebElement cartLink = firefoxDriver.findElement(By.cssSelector("[data-test='shopping-cart-link']"));
        WebElement cartBadge = firefoxDriver.findElement(By.cssSelector("[data-test='shopping-cart-badge']"));
        softAssertions
                .assertThat(cartBadge.isDisplayed())
                .as("Badge bulunamadı.");
        boolean actualResult1 = cartBadge.isDisplayed();
        boolean expectedResult2 = true;
        assertEquals(expectedResult2, actualResult1, "Not displayed.");
        assertTrue(cartBadge.getText().equals("1"), "Cart badge should show 1 item.");

    }

    @Test
    public void testRemoveFromCartFunction() {
        testName = "testRemoveFromCartFunction";
        login("standard_user","secret_sauce");
        WebElement addToCartBtn = firefoxDriver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']"));
        softAssertions
                .assertThat(addToCartBtn.isDisplayed())
                .as("Add to cart butonu bulunamadı.");
        addToCartBtn.click();
        WebElement cartBadge = firefoxDriver.findElement(By.cssSelector("[data-test='shopping-cart-badge']"));
        WebElement removeBtn = firefoxDriver.findElement(By.cssSelector("[data-test='remove-sauce-labs-backpack']"));
        softAssertions
                .assertThat(cartBadge.isDisplayed())
                .as("Badge bulunamadı.");
        boolean actualResult1 = cartBadge.isDisplayed();
        boolean expectedResult1 = true;
        assertEquals(expectedResult1, actualResult1, "Not displayed.");

        removeBtn.click();
        WebElement addToCartBtn1 = firefoxDriver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']"));
        softAssertions
                .assertThat(addToCartBtn1.isDisplayed())
                .as("Add to cart butonu bulunamadı.");
        boolean actualResult = addToCartBtn1.isDisplayed();
        boolean expectedResult = true;
        assertEquals(expectedResult, actualResult, "Not displayed.");
        boolean expectedResult2 = false;
        boolean isCartBadgePresent = firefoxDriver.findElements(By.className("shopping_cart_badge")).size() > 0;
        assertEquals(expectedResult2, isCartBadgePresent, "Badge is displayed.");

    }

}