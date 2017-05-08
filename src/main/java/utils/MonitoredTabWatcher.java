package utils;

import org.openqa.selenium.WebDriver;

/**
 * Created by eyal.neumann on 4/26/2017.
 */
public class MonitoredTabWatcher {
    private Thread thread;


    public MonitoredTabWatcher(WebDriver driver){
        this.thread = new Thread(new TabWatcher(driver));
    }

    public boolean watch(long timeOut) throws InterruptedException {

        boolean newTabCreated =false;

        this.thread.start();
        this.thread.join(timeOut);



        return newTabCreated;
    }

}
