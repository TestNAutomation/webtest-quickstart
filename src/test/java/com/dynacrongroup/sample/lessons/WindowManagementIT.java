package com.dynacrongroup.sample.lessons;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

/**
 * Created by IntelliJ IDEA.
 * User: yurodivuie
 * Date: 7/9/12
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class WindowManagementIT {

    private static final Logger LOG = LoggerFactory.getLogger( LocatorSampleIT.class );
    private static final int DELAY = 3000;

    static WebDriver driver;

    @BeforeClass
    public static void provisionWebDriver() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait( 5, TimeUnit.SECONDS );
        driver.get( "http://localhost:8080/webtest-quickstart/" );
        pause( DELAY );
    }

    @AfterClass
    public static void killDriver() {
        driver.quit();
    }

    @Test
    public void normalLink() {
        driver.findElement( By.linkText( "Regular Link" ) ).click();
        pause( DELAY );
        assertThat( driver.getTitle(), equalToIgnoringCase( "This window is new!" ) );
    }

    @Test
    public void popupLink() {
        driver.findElement( By.linkText( "New Window Link" ) ).click();

        try {
            switchWindows();
            pause( DELAY );
            assertThat( driver.getTitle(), equalToIgnoringCase( "This window is new!" ) );
        }
        finally {
            driver.close();  //Close the popup...
            switchWindows(); //switch back.
            pause( DELAY );
        }
    }



    private void switchWindows() {
        String currentHandle = null;
        try {
            currentHandle = driver.getWindowHandle();
        }
        catch (NoSuchWindowException e) {
            LOG.info("No current window.  I probably just closed it.");
        }
        Set<String> allHandles = driver.getWindowHandles();
        for ( String handle : allHandles ) {
            if ( !handle.equals( currentHandle ) ) {
                driver.switchTo().window( handle );
                break;
            }
        }
    }

    private static final void pause( int millis ) {
        try {
            Thread.sleep( millis );
        }
        catch ( InterruptedException e ) {
            LOG.info( e.getMessage() );
        }
    }
}
