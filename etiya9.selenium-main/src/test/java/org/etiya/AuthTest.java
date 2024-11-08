package org.etiya;
import org.apache.commons.io.FileUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {
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
        takeScreenshotAfterTest();
        firefoxDriver.quit();
    }

    private void takeScreenshotAfterTest() {
        File screenshot = firefoxDriver.getScreenshotAs(OutputType.FILE);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String folderPath = "C:\\Users\\dilara.gebes\\Desktop\\etiya9.selenium-main\\src\\test" + currentDate;
        String filePath = folderPath + "\\" + testName + ".png";

        try {
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileUtils.copyFile(screenshot, new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SS alınamadı.");
        }
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
    public void testLoginWithCorrectCredentials() {
        testName = "testLoginWithCorrectCredentials";
        firefoxDriver.get("https://www.saucedemo.com/");

        try{
            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            softAssertions
                    .assertThat(usernameInput.isDisplayed())
                    .as("Kullanıcı adı girişi bulunamadı.");
            usernameInput.sendKeys("standard_user");

            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            softAssertions.assertThat(passwordInput.isDisplayed())
                    .as("Şifre girişi bulunamadı.");
            passwordInput.sendKeys("secret_sauce");


            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
            softAssertions
                    .assertThat(loginButton.isDisplayed())
                    .as("Giriş yap butonu bulunamadı.");
            loginButton.click();

            softAssertions.assertThat(firefoxDriver.getCurrentUrl())
                    .as("Giriş yapıldıktan sonra yönlendirilen url yanlış.")
                    .isEqualTo("https://www.saucedemo.com/inventory.html");
        }finally{
            softAssertions.assertAll();
        }

    }

    @Test
    public void testLockedOutUserError() {
        testName = "testLockedOutUserError";
        firefoxDriver.get("https://www.saucedemo.com/");

        try{
            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            softAssertions
                    .assertThat(usernameInput.isDisplayed())
                    .as("Kullanıcı adı girişi bulunamadı.");
            usernameInput.sendKeys("locked_out_user");

            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            softAssertions.assertThat(passwordInput.isDisplayed())
                    .as("Şifre girişi bulunamadı.");
            passwordInput.sendKeys("secret_sauce");


            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
            softAssertions
                    .assertThat(loginButton.isDisplayed())
                    .as("Giriş yap butonu bulunamadı.");
            loginButton.click();

            WebElement errorElement = firefoxDriver.findElement(By.cssSelector("[data-test='error']"));
            softAssertions
                    .assertThat(errorElement.isDisplayed())
                    .as("Error msg bulunamadı.");
            String actualResult = errorElement.getText();
            String expectedResult = "Epic sadface: Sorry, this user has been locked out.";
            assertEquals(expectedResult, actualResult, "Do not match.");

            softAssertions.assertThat(firefoxDriver.getCurrentUrl())
                    .as("Yanlış url.")
                    .isEqualTo("https://www.saucedemo.com/");
        }finally{
            softAssertions.assertAll();
        }
    }



//    @Test
//    public void testImgControl() throws IOException {
//        testName = "testImgControl";
//        login("visual_user","secret_sauce");
//        WebElement firstImg = firefoxDriver.findElement(By.id("item_4_img_link"));
//        firstImg.click();
//        WebElement orgImg = firefoxDriver.findElement(By.cssSelector("[data-test='item-sauce-labs-backpack-img']"));
//        File screenshot = ((TakesScreenshot) firefoxDriver).getScreenshotAs(OutputType.FILE);
//        BufferedImage actualImage = ImageIO.read(screenshot);
//    }
//
//    private boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
//        // Check if dimensions are the same
//        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
//            return false;
//        }
//
//        // Compare pixel by pixel
//        for (int y = 0; y < imgA.getHeight(); y++) {
//            for (int x = 0; x < imgA.getWidth(); x++) {
//                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }

    @Test
    public void testBackpackPrice() throws InterruptedException {
        testName = "testBackpackPrice";
        login("visual_user","secret_sauce");
//        List<WebElement> products = firefoxDriver.findElements(By.className("inventory_item"));
//        products.get(0);
        WebElement priceElement = firefoxDriver.findElement(By.cssSelector("[data-test='inventory-item-price']"));
        priceElement.getText();
        String actualResult = priceElement.getText();
        String expectedResult = "$" + "29.99";
        System.out.println(actualResult + " " + expectedResult);
        assertEquals(expectedResult, actualResult, "Do not match.");
    }
}
