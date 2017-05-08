package utils;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

/**
 * Created by eyal.neumann on 4/26/2017.
 */
public class TabWatcher implements Runnable{
    private WebDriver driver;

    public TabWatcher(WebDriver driver){
        this.driver=driver;
    }



    @Override
    public void run() {
        int millCounter=0;
        int maxTime=120000;
        int tabNumber=1;
        ArrayList<String> tabs;
        tabs = new ArrayList<String>(driver.getWindowHandles());
        tabNumber=tabs.size();

        int waitingForTabInterval = 5000;
        int maxWaitingForTabInterval = 10000;
        int waitingForTabIntervalStep = 200;

        while ((tabNumber==1)&&millCounter<maxTime){
            tabs = new ArrayList<String> (driver.getWindowHandles());
            tabNumber=tabs.size();
            try {
                Thread.sleep(waitingForTabInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            millCounter+=waitingForTabInterval;
            if (waitingForTabInterval< maxWaitingForTabInterval){
                waitingForTabInterval+= waitingForTabIntervalStep;
            }

        }
    }
}
