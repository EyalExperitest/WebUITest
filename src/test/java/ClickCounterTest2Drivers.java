import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

public class ClickCounterTest2Drivers {
    public static final int CLICKS = 200;
    public static final int DRIVERS_NUMBER = 4;
    private WebDriver[] drivers;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private ArrayList<String> tabs;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Experitest2012";
    public static final String TEST_NAME = "Selenium Test  Click Counter " + CLICKS;
    private static URL url;

    public class SDriverTestRunner implements Runnable {
        private WebDriver driver;

        public SDriverTestRunner(WebDriver driver){
            this.driver=driver;
        }

        @Override
        public void run() {
            //String baseUrl = "http://192.168.4.85:8060/";
            String name = Thread.currentThread().getName();
            System.out.println("Starting Run On Thread "+name);
            ////*[@id="main"]/section/div/div[1]/div/ul
            //https://www.whatismybrowser.com/detect/what-is-my-local-ip-address
            ArrayList<String> tabs = new ArrayList<String>(this.driver.getWindowHandles());
            System.out.println( "Tabs Number :" +tabs.size());
            if(tabs.size()==2){
                for (int i=0;i<tabs.size();i++){
                    System.out.println("Tab "+i+" : "+tabs.get(i));
                }
                this.driver.switchTo().window(tabs.get(1));
                this.driver.close();
                this.driver.switchTo().window(tabs.get(0));
            }

            this.driver.get("https://www.whatismybrowser.com/detect/what-is-my-local-ip-address");
            String localIP = this.driver.findElement(By.xpath("//*[@id=\"main\"]/section/div/div[1]/div/ul/li")).getText();
            System.out.println("Local IP : "+localIP);

            this.driver.get("http://192.168.4.85:8060/" + "/html-tests/htmlpages/clickcounter.html");


            this.driver.manage().window().maximize();




            WebElement counter = this.driver.findElement(By.id("counter"));
            WebElement addButton = this.driver.findElement(By.id("add-button"));

            // ERROR: Caught exception [unknown command []]
            for(int i = 0; i< CLICKS; i++){
                String counterTest = counter.getText();
                System.out.println(counterTest);
                assertEquals(counterTest,i+"");
                //System.out.println(counterTest);
                //takeScreenShot("C:\\Users\\eyal.neumann\\workspace\\WebUITest\\screenshots\\ScreenshotB" + i + ".png");
                addButton.click();
                //takeScreenShot("C:\\Users\\eyal.neumann\\workspace\\WebUITest\\screenshots\\ScreenshotA" + i + ".png");
            }

            this.driver.quit();
        }
    }



    public class SDriverFactoryRunner implements Runnable {
        private WebDriver driver;
        private DesiredCapabilities dc = new DesiredCapabilities().chrome();
        private URL url;

        public SDriverFactoryRunner(URL url, DesiredCapabilities dc) {
            this.dc = dc;
            this.url = url;
        }

        @Override
        public void run() {
            this.driver = new RemoteWebDriver(url, dc);
            this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            System.out.println("Driver Initiated");

        }

        public WebDriver getDriver() {
            return this.driver;
        }
    }


    @Before
    public void setUp() throws Exception {
        url = new URL("http://eyalneumann.experitest.local/wd/hub");

        DesiredCapabilities dc = new DesiredCapabilities().chrome();
        dc.setCapability("user", ADMIN_USERNAME);
        dc.setCapability("password", ADMIN_PASSWORD);
        dc.setCapability("generateReport", true);
        dc.setCapability("takeScreenshots", false);
        dc.setCapability("Colour", "Blue");
        dc.setCapability("testName", TEST_NAME);

        drivers = new WebDriver[DRIVERS_NUMBER];
        SDriverFactoryRunner[] sDriverFactoryRunners = new SDriverFactoryRunner[DRIVERS_NUMBER];
        Thread[] threads = new Thread[DRIVERS_NUMBER];

        for (int i = 0; i < DRIVERS_NUMBER; i++) {
            sDriverFactoryRunners[i] = new SDriverFactoryRunner(url, dc);
            threads[i] = new Thread(sDriverFactoryRunners[i]);
        }
        for (int i = 0; i < DRIVERS_NUMBER; i++) {
            String name = threads[i].getName();
            threads[i].setName(name+" "+i);
            threads[i].start();
        }
        for (int i = 0; i < DRIVERS_NUMBER; i++) {
            threads[i].join();
        }

        for (int i = 0; i < DRIVERS_NUMBER; i++) {
            drivers[i]=sDriverFactoryRunners[i].getDriver();
        }


        }

    @Test
    public void testClickCounter() throws Exception {
        Thread[] threads = new Thread[DRIVERS_NUMBER];

        for (int i = 0; i < DRIVERS_NUMBER; i++) {
            threads[i] = new Thread(new SDriverTestRunner(drivers[i]));
        }
        for (int i = 0; i < DRIVERS_NUMBER; i++) {
            threads[i].start();
        }
        for (int i = 0; i < DRIVERS_NUMBER; i++) {
            threads[i].join();
        }



    }


    @After
    public void tearDown() throws Exception {

    }


}
