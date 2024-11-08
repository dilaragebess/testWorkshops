package org.etiya;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
//type, send_keys()
        FirefoxDriver driver = new FirefoxDriver();

       getAllProducts(driver);


        Thread.sleep(5000);
        driver.quit();


        //csv örneğini ekle -> ödev


    }
    public static void getAllProducts(FirefoxDriver driver) {
        driver.get("https://www.saucedemo.com");

        WebElement usernameInput = driver.findElement(By.id("user-name"));
        usernameInput.sendKeys("standard_user");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("secret_sauce");

        WebElement loginBtn = driver.findElement(By.id("login-button"));
        loginBtn.click();

        List<WebElement> products = driver.findElements(By.className("inventory_item"));

        List <String> productsNames = new ArrayList<>();
        List <String> productInfo = new ArrayList<>();
        productInfo.add(0, "Product Name");
        productInfo.add(1, " Price");
        List <String> prices = new ArrayList<>();
//        String[] productNames1 = new String[2];
//        String[] prices = new String[2];
//        List <String> prices = new ArrayList<>();
//

        for(WebElement element: products) {

            //get price, description, image url, title....
// WebElement headerElement = element.findElement(By.cssSelector("[data-test = 'inventory-item-name']"));
            WebElement headerElement = element.findElement(By.className("inventory_item_name"));
            productsNames.add(headerElement.getText());
            productInfo.add(headerElement.getText());


            WebElement priceElement = element.findElement(By.className("inventory_item_price"));
            productInfo.add(" " +priceElement.getText());

            System.out.println(headerElement.getText()+ " " + priceElement.getText());

        }
        String excelFilePath = "C:\\Users\\dilara.gebes\\Desktop\\etiya9.selenium-main\\src\\product_names.xlsx";


//        combinedList.addAll(Arrays.asList(array2));
        System.out.println(productInfo);


        List<String> excelList = new ArrayList<>();
         try(FileInputStream fs = new FileInputStream(new File(excelFilePath))) {
             Workbook workbook = new XSSFWorkbook(fs);

             Sheet sheet = workbook.getSheet("product_names");

             for (Row row : sheet) {

                 Cell cell = row.getCell(0);
                 excelList.add(cell.getStringCellValue());

             }
         }

         catch (Exception e) {
             System.out.println("Excel file is not found");
         }

         for(String p: excelList) {
             System.out.println("Excel file is read: " +p);
         }

         if(excelList.equals(productsNames))  {
             System.out.println("Matches.");
         }

         else {
             System.out.println("Do not match.");
             System.out.println(productsNames);
         }
        String csvFilePath = "C:\\Users\\dilara.gebes\\Desktop\\etiya9.selenium-main\\src\\product_names.csv";

        CSVReader reader = null;
        List<String> csvList = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(csvFilePath));
            List<String[]> allRows = reader.readAll();
            // CSV dosyasındaki her bir satırı okuma
            for (String[] row : allRows) {
                // CSV satırlarının içeriği
               System.out.println("CSV Row Data is read: " );
                for (String data : row) {
                    System.out.print(data + " ");
                    csvList.add(data);
                }
                System.out.println();
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        if(csvList.equals(productInfo))  {
            System.out.println("Matches.");
        }

        else {
            System.out.println("Do not match.");
            System.out.println(productInfo);
            System.out.println(csvList);
        }
        // Tarayıcıyı kapat
        driver.quit();
    }


//        List<String> csvList = new ArrayList<>();
//        try(FileInputStream fs = new FileInputStream(new File(excelFilePath))) {
//
//            for () {
//
//
//            }
//        }
//
//        catch (Exception e) {
//            System.out.println("CSV file is not found");
//        }

    }

