
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
@RunWith(value = Parameterized.class)

public class SeeTestAutomationWebTest {
  public static final int TIMEOUT_IN_SECONDS = 1200;
  private static final int ITERATIONS_NUM =100 ;
  private WebDriver driver;
  private WebDriverWait wait;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private ArrayList<String> tabs;
  private int number;
  public SeeTestAutomationWebTest(int number) {
    this.number = number;

  }


  @Rule
  public Timeout globalTimeout = Timeout.seconds(TIMEOUT_IN_SECONDS);
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    ArrayList<Object[]> data = new ArrayList<Object[]>();
    for (int i = 0; i < ITERATIONS_NUM; i++) {
      //Thread.currentThread().setName("main : Iteration : "+i);
/*      if(stop){
        break;
      }*/
      Object[] e = { i };
      data.add(e);
    }
    return data;
  }

  @Before
  public void setUp() throws Exception {

/*    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");*/
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, 10000);

    baseUrl = "http://192.168.2.135/";
    baseUrl ="https://qacloud.experitest.com";
    //baseUrl ="http://mastercloud";

    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testOpenEachAvialableDevice() throws Exception {
    //Thread.sleep(3600);

    String userName = "eyal";
    String password = "Experitest2012";

    String statusFilterXpath ="//*[@id=\"content-after-toolbar\"]/div/div/div[1]/div/div/md-content/div[1]/div[3]/md-menu/md-input-container/textarea";
    String clearXpath="//*[@aria-hidden=\"false\"]//*[@aria-label=\"Clear\"]";
    String availableXpath ="//*[@aria-hidden=\"false\"]//*[@type=\"checkbox\"]//*[text()='Available']";
    String backdropXpath = "//md-backdrop";
    String deviceLines ="//*[@id=\"content-after-toolbar\"]/div/div/div[1]/div/div/md-content/div[2]/table//*[@st-select-row='device']";
    deviceLines ="//*[@id=\"content-after-toolbar\"]/div/div/div[1]/div/div/md-content/div[2]/table//*[@st-select-row='device']//td[3]";
    String deviceNameXpath ="/html/body/div[2]/div/div[1]/div/div/device-loupe/div/div/div[1]/h3[1]";
    String automationButtonXpath="//*[@id=\"full-page-container\"]/div[1]/div/div/div/button[7]";
    String endSessionXpath  = "/html/body/div[2]/div/div[1]/div/div/device-loupe/div/div/div[2]/div[3]/button/md-icon";
    String endSessionXpath2  = "/html/body/div[2]/div/div[1]/div/div/device-loupe/div/div/div[2]/div[3]/button";
    String releaseXpath = "/html/body/div[1]/div/div/before-exit-dialog/div/div[3]/button[1]/span";

    String toastXPath="/html/body/md-toast/div/span";


    driver.get(baseUrl + "/index.html#/login");
    driver.manage().window().maximize();
    tabs = new ArrayList<String> (driver.getWindowHandles());
    driver.switchTo().window(tabs.get(1));
    driver.close();
    driver.switchTo().window(tabs.get(0));

    driver.findElement(By.name("username")).clear();

    driver.findElement(By.name("username")).sendKeys(userName);
    //driver.findElement(By.cssSelector("div.ng-scope")).click(); //do nothing ?
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys(password);
    driver.findElement(By.name("login")).click();
    Thread.sleep(5000);
    //System.out.println(driver.getCurrentUrl());
   // driver.findElement(By.xpath("//ul[@id='side-menu']/li[6]/a/span")).click(); //do nothing?
    driver.findElement(By.linkText("Devices")).click();
    driver.findElement(By.name("list-view")).click();
    Thread.sleep(5000);



    //*[@id=\"input_27\"]
    //*[@id="menu_container_28"]/md-menu-content/section/button[2]/span"
    //*[@id=\"menu_container_28\"]/md-menu-content/md-menu-item[1]/md-checkbox/div[2]/span

    WebElement statusFilterElement = driver.findElement(By.xpath(statusFilterXpath));
    statusFilterElement.click();
    Thread.sleep(1000);
    driver.findElement(By.xpath(clearXpath)).click();
    driver.findElement(By.xpath(availableXpath)).click();
    driver.findElement(By.xpath(backdropXpath)).click();

/*    driver.findElement(By.linkText("Available, Offline, In Use, Cleanup, Cleanup Failed, Error, Unauthorized")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("Clear")).click();
    driver.findElement(By.linkText("Available")).click();*/


    //driver.findElement(By.cssSelector("div.ng-binding.ng-scope")).click(); //original line selector

    //driver.findElement(By.xpath("//div[@id='full-page-container']/div/div/div/div/button[7]")).click(); //do nothing ?
    //driver.findElement(By.cssSelector("button.btn.btn-white")).click();
    List<WebElement> deviceLinesElements = driver.findElements(By.xpath(deviceLines));
    System.out.println("Number of Devices found : "+deviceLinesElements.size());
    boolean failureFlag=false;
    StringBuffer failBuffer =new StringBuffer("Failures: ");
    for (WebElement deviceLineElement :deviceLinesElements){
      boolean tryClickFlag = tryClick(deviceLineElement);
      //System.out.println("deviceLineElement.getText = "+ deviceName);
      if (tryClickFlag){
        String deviceName = deviceLineElement.getText();
        WebElement automationButton = driver.findElement(By.xpath(automationButtonXpath));
        String disabled = automationButton.getAttribute("aria-disabled");
        //System.out.println("disabled : is "+disabled);
        if(disabled.equals("false")){
          automationButton.click();
          Thread.sleep(2000);

          try {
            String toastText = driver.findElement(By.xpath(toastXPath)).getText();
            System.out.println("Failure to open "+deviceName+" : "+toastText);
          } catch (Exception e) {


            long t0 = System.currentTimeMillis();
            Thread.sleep(2000);

            boolean newTabCreated=false;
            newTabCreated = waitforNewTabToBeCreated();
            long t1 = System.currentTimeMillis();
            long dt =t1-t0;
            long sec =dt/1000;
            Thread.sleep(2000);
            //System.out.println(driver.getCurrentUrl());
            if(newTabCreated){
              manageAutomationSession(deviceNameXpath, endSessionXpath2, releaseXpath, sec);
              driver.close();
              driver.switchTo().window(tabs.get(0));
            }
            else{
              System.out.println("Failed to open "+deviceName+" TimeOut");

            }
          }
        }
        else{
          System.out.println("Automation for device "+deviceName+" is disabled, check devices os version");

        }


          Thread.sleep(2000);

        }
      else{
        String failureMessage = "Failed to click on Device line ,Device might have become unavailable  during the test";
        System.out.println(failureMessage);
        failBuffer.append("\n"+failureMessage);
        failureFlag=true;
        //fail(failureMessage);
      }


    }
  if(failureFlag){
    fail(failBuffer.toString());
  }

  }

  private boolean tryClick(WebElement device) {
    boolean tryClickFlag=false;
    try {
      wait.until(ExpectedConditions.elementToBeClickable(device));
      device.click();
      tryClickFlag=true;
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return tryClickFlag;
  }

  private boolean waitforNewTabToBeCreated() throws InterruptedException {
    boolean newTabCreated;
    int millCounter=0;
    int maxTime=120000;
    int tabNumber=1;
    tabs = new ArrayList<String>(driver.getWindowHandles());
    tabNumber=tabs.size();

    int waitingForTabInterval = 100;
    int maxWaitingForTabInterval = 10000;
    int waitingForTabIntervalStep = 000;

    while ((tabNumber==1)&&millCounter<maxTime){
      tabs = new ArrayList<String> (driver.getWindowHandles());
      tabNumber=tabs.size();
      Thread.sleep(waitingForTabInterval);
      millCounter+=waitingForTabInterval;
      if (waitingForTabInterval< maxWaitingForTabInterval){
        waitingForTabInterval+= waitingForTabIntervalStep;
      }

    }
    if (tabNumber>1){
      driver.switchTo().window(tabs.get(1));
      newTabCreated=true;
    }
    else{
      newTabCreated=false;

    }
    return newTabCreated;
  }

  private void manageAutomationSession(String deviceNameXpath, String endSessionXpath, String releaseXpath, long sec) throws InterruptedException {
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(deviceNameXpath)));
    WebElement deviceNameElement  = driver.findElement(By.xpath(deviceNameXpath));
    //wait.until(ExpectedConditions.attributeToBeNotEmpty(deviceNameElement,"Text"));

    String deviceName = deviceNameElement.getText();
    if (deviceName.isEmpty()){
        Thread.sleep(5000);
        deviceName = deviceNameElement.getText();
    }
    System.out.println(driver.getCurrentUrl()+" | Device Name : \""+ deviceName +"\" : "+sec+" seconds ");
    Thread.sleep(5000);

    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(endSessionXpath)));

    driver.findElement(By.xpath(endSessionXpath)).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(releaseXpath)));

    driver.findElement(By.xpath(releaseXpath)).click();
  }

  @After
  public void tearDown() throws Exception {
    //driver.quit();
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
