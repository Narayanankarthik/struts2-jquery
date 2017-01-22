package com.jgeppert.jquery.menu;

import com.jgeppert.jquery.selenium.JQueryIdleCondition;
import com.jgeppert.jquery.selenium.JQueryNoAnimations;
import com.jgeppert.jquery.selenium.WebDriverFactory;
import com.jgeppert.jquery.junit.category.HtmlUnitCategory;
import com.jgeppert.jquery.junit.category.PhantomJSCategory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(Parameterized.class)
@Category({HtmlUnitCategory.class, PhantomJSCategory.class})
public class MenuTagIT {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                 { "http://localhost:8080/regular" }, 
                 { "http://localhost:8080/uncompressed" },
                 { "http://localhost:8080/loadatonce" }, 
                 { "http://localhost:8080/loadfromgoogle" }  
           });
    }
    
    private static final JQueryIdleCondition JQUERY_IDLE = new JQueryIdleCondition();
    private static final JQueryNoAnimations JQUERY_NO_ANIMATIONS = new JQueryNoAnimations();

    private String baseUrl;        
    private WebDriver driver;        

    public MenuTagIT(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Before
    public void before() {
        driver = WebDriverFactory.getWebDriver();
    }

    @After
    public void after() {
        driver.quit();
    }

    @Test
    public void testLocalContent() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        driver.get(baseUrl + "/menu/localcontent.action");

        WebElement menuItemWithSubMenu = driver.findElement(By.id("menuItem2"));
        WebElement submenu = driver.findElement(By.id("mySubmenu"));
        WebElement submenuItemWithAjaxLink = driver.findElement(By.id("submenuItem2"));
        WebElement resultDiv = driver.findElement(By.id("resultDiv"));

        Assert.assertFalse(submenu.isDisplayed());
        Assert.assertEquals("This is the result div.", resultDiv.getText());

        (new Actions(driver)).moveToElement(menuItemWithSubMenu).build().perform();
        wait.until(ExpectedConditions.visibilityOf(submenu));

        Assert.assertTrue(submenu.isDisplayed());
        Assert.assertEquals("This is the result div.", resultDiv.getText());

        (new Actions(driver)).moveToElement(submenuItemWithAjaxLink).click().build().perform();

        wait.until(JQUERY_IDLE);

        Assert.assertEquals("This is simple text from an ajax call.", resultDiv.getText());
    }

    @Test
    public void testLocalContentList() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        driver.get(baseUrl + "/menu/localcontent-list.action");

        WebElement menu = driver.findElement(By.id("myMenu"));
        List<WebElement> menuItems = menu.findElements(By.tagName("li"));
        WebElement item2Link = menuItems.get(1).findElement(By.tagName("a"));
        WebElement resultDiv = driver.findElement(By.id("resultDiv"));

        Assert.assertEquals(3, menuItems.size());
        Assert.assertEquals("This is the result div.", resultDiv.getText());

        item2Link.click();

        wait.until(JQUERY_IDLE);

        Assert.assertEquals("Echo : Item 2", resultDiv.getText());
    }

    @Test
    public void testLocalContentMap() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        driver.get(baseUrl + "/menu/localcontent-map.action");

        WebElement menu = driver.findElement(By.id("myMenu"));
        List<WebElement> menuItems = menu.findElements(By.tagName("li"));
        WebElement item2Link = menuItems.get(1).findElement(By.tagName("a"));
        WebElement resultDiv = driver.findElement(By.id("resultDiv"));

        Assert.assertEquals(3, menuItems.size());
        Assert.assertEquals("This is the result div.", resultDiv.getText());

        item2Link.click();

        wait.until(JQUERY_IDLE);

        Assert.assertEquals("Echo : 2", resultDiv.getText());
    }
}

