
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class ClickCounterTest {
  public static final int CLICKS = 30;
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private ArrayList<String> tabs;
  private static final String ADMIN_USERNAME = "admin";
  private static final String ADMIN_PASSWORD = "Experitest2012";
  public static final String TEST_NAME = "Selenium Test  Click Counter "+CLICKS;

  @Before
  public void setUp() throws Exception {
    //driver = new ChromeDriver();
    DesiredCapabilities dc = new DesiredCapabilities().chrome();
    dc.setCapability("user", ADMIN_USERNAME);
    dc.setCapability("password", ADMIN_PASSWORD);
    dc.setCapability("generateReport", true);
    //dc.setCapability("takeScreenshots", false);
    dc.setCapability("Colour", "Blue");
    dc.setCapability("testName", TEST_NAME);

    URL url=new URL("http://eyalneumann.experitest.local/wd/hub");
    //URL url=new URL("http://eyalneumann.experitest.local:4444/wd/hub");
    driver = new RemoteWebDriver(url, dc);

    baseUrl = "http://192.168.4.85:8060/";
    //baseUrl = "www.apple.com";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testClickCounter() throws Exception {

    driver.get(baseUrl + "/html-tests/htmlpages/clickcounter.html");

    driver.manage().window().maximize();
    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
    if(tabs.size()==2){
      for (int i=0;i<tabs.size();i++){
        System.out.println("Tab "+i+" : "+tabs.get(i));
      }
      driver.switchTo().window(tabs.get(1));
      driver.close();
      driver.switchTo().window(tabs.get(0));
    }


    WebElement counter = driver.findElement(By.id("counter"));
    WebElement addButton = driver.findElement(By.id("add-button"));

    // ERROR: Caught exception [unknown command []]
    for(int i = 0; i< CLICKS; i++){
      String counterTest = counter.getText();
      assertEquals(counterTest,i+"");
      //System.out.println(counterTest);
      takeScreenShot("C:\\Users\\eyal.neumann\\workspace\\WebUITest\\screenshots\\ScreenshotB" + i + ".png");
      addButton.click();
      //takeScreenShot("C:\\Users\\eyal.neumann\\workspace\\WebUITest\\screenshots\\ScreenshotA" + i + ".png");
    }

  }
  @Test@Ignore
  public void testAppleSite() throws Exception {
    driver.get("https://appleid.apple.com/#!&page=signin");
    driver.manage().window().maximize();
    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
    if(tabs.size()==2){
      for (int i=0;i<tabs.size();i++){
        System.out.println("Tab "+i+" : "+tabs.get(i));
      }
      driver.switchTo().window(tabs.get(1));
      driver.close();
      driver.switchTo().window(tabs.get(0));
    }
    Thread.sleep(10000);
    WebElement counter = driver.findElement(By.id("NopNop"));


  }
  private void takeScreenShot(String pathname) throws IOException {
    File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    FileUtils.copyFile(scrFile, new File(pathname));
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
