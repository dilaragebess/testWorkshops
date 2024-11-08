package org.etiya;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class AppTest {
    FirefoxDriver firefoxDriver;
    WebDriverWait wait;
    SoftAssertions softAssertions;
    String testName;

    @Test
    public void testTitleShouldBeCorrect() {
        //Arrange
        testName = "testTitleShouldBeCorrect";
        String expectedResult = "Swag Labs";

        //Act
        firefoxDriver.get("https://www.saucedemo.com");
        String actualResult = firefoxDriver.getTitle();

        //assert
        //assert expectedResult.equals(actualResult)
        assertEquals(expectedResult,actualResult, "titlelar uyuşmuyor");

    }

    @Test
    public void testLoginWithCorrectCredentials() throws InterruptedException {
        testName = "testLoginWithCorrectCredentials";
        firefoxDriver.get("https://www.saucedemo.com/");
        try{
            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            softAssertions.assertThat(usernameInput.isDisplayed())
                    .as("Kullanıcı adı girişi bulunamadı.");
            usernameInput.sendKeys("standard_user");

            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            softAssertions.assertThat(passwordInput.isDisplayed())
                    .as("Şifre girişi bulunamadı.");
            passwordInput.sendKeys("secret_sauce");

            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
            softAssertions
                    .assertThat(loginBtn.isDisplayed())
                    .as("Giriş yap butonu bulunamadı.");
            loginBtn.click();

            softAssertions.assertThat(firefoxDriver.getCurrentUrl())
                    .as("Giriş yapıldıktan sonra yönlendirilen url yanlış.")
                    .isEqualTo("https://www.saucedemo.com/inventory.html");
        }finally {
            softAssertions.assertAll();
        }
    }



    @BeforeEach // Her test öncesi çalıştırılacak ortak func.
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
        // Ekran görüntüsü al
        File screenshot = firefoxDriver.getScreenshotAs(OutputType.FILE);

        // Mevcut tarih formatı Gün-Ay-Yıl
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        // Klasör yolunu oluştur
        String folderPath = "C:\\Users\\dilara.gebes\\Desktop\\etiya9.selenium-main\\src\\test" + currentDate;

        // Test adıyla birlikte dosya yolu
        String filePath = folderPath + "\\" + testName + ".png";

        try {
            // Klasör yoksa oluştur
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Klasör ve alt klasörleri oluştur
            }

            // Ekran görüntüsünü kaydet
            FileUtils.copyFile(screenshot, new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SS alınamadı.");
        }
    }



}
