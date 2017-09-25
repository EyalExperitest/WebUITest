import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class WikipediaTestMultiDriver {
    public static final int ITERATIONS = 10;
    public static final int DRIVERS_NUMBER = 2;
    private WebDriver[] drivers;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private ArrayList<String> tabs;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Experitest2012";
    public static final String TEST_NAME = "Selenium Test  Wikipedia Test " + ITERATIONS;
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

            this.driver.get("https://en.wikipedia.org/wiki/Main_Page");

            for (int i=0;i<ITERATIONS;i++){
                WebElement randomPage = this.driver.findElement(By.id("n-randompage"));
                randomPage.click();
                WebElement firstHeading = this.driver.findElement(By.id("firstHeading"));
                System.out.println(firstHeading.getText());
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
        dc.setCapability("takeScreenshots", true);
        dc.setCapability("Site", "Wikipedia");
        dc.setCapability("testName", TEST_NAME);
        dc.setCapability("testType", "Selenium");

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
