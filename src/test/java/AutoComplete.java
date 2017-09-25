
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class AutoComplete {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private static final String ADMIN_USERNAME = "admin";
  private static final String ADMIN_PASSWORD = "Experitest2012";
  public static final String TEST_NAME = "Selenium Test  Auto Complete Source";

  @Before
  public void setUp() throws Exception {
    DesiredCapabilities dc = new DesiredCapabilities().chrome();
    dc.setCapability("user", ADMIN_USERNAME);
    dc.setCapability("password", ADMIN_PASSWORD);
    dc.setCapability("generateReport", true);
    dc.setCapability("takeScreenshots", false);
    dc.setCapability("Colour", "Blue");
    dc.setCapability("testName", TEST_NAME);

    URL url=new URL("http://eyalneumann.experitest.local:8091/wd/hub");
    //URL url=new URL("http://eyalneumann.experitest.local:4444/wd/hub");
    driver = new RemoteWebDriver(url, dc);    baseUrl = "http://192.168.4.85:8060/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testAutoCompleteDemo() throws Exception {
    driver.get(baseUrl + "/html-tests/htmlpages/autocomplete.html");
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

    driver.findElement(By.id("searchInput")).clear();
    driver.findElement(By.id("searchInput")).sendKeys("South by Southwest");
    driver.findElement(By.name("select")).clear();
    driver.findElement(By.name("select")).sendKeys("Safari");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    String pageSource = driver.getPageSource();
    System.out.println(pageSource);
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
