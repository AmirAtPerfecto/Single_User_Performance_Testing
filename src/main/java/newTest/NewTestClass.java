package main.java.newTest;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import main.java.perfecto.PerfectoUtils;
import main.java.perfecto.Utils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;


public class NewTestClass {
    RemoteWebDriver driver;
    PerfectoExecutionContext perfectoExecutionContext;
    ReportiumClient reportiumClient;


    @Parameters({ "platformName", "platformVersion", "manufacturer", "model", "deviceName", "appID" })
    @BeforeTest
    public void beforeTest(String platformName, String platformVersion, String manufacturer, String model, String deviceName, String appID) throws IOException {
        driver = Utils.getRemoteWebDriver(platformName, platformVersion, manufacturer, model, deviceName, appID );
        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("My Project", "1.0"))
                .withJob(new Job("My Job", 45))
                .withContextTags("tag1")
                .withWebDriver(driver)
                .build();
        reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
    }

    @Test
    public void test() {
        try {
            long timer;

            reportiumClient.testStart("Perfecto Single User PerfTest", new TestContext("Performance test", "iPhone X"));
            System.out.println("Yay");
            PerfectoUtils.switchToContext(driver, "NATIVE_APP");

            try {
                PerfectoUtils.closeApp(driver, "Netflix");
            } catch (Exception e) {
                e.printStackTrace();
            }
            PerfectoUtils.vitalsCollect(driver, "All");
            PerfectoUtils.startNetworkVirtualization(driver, PerfectoUtils.NetworkVirtualizationProfiles.NV_3_5G_HSPA_PLUS_Average, "true");

            PerfectoUtils.launchApp(driver, "Netflix");
            PerfectoUtils.ocrTextCheck(driver, "offline", 99, 30);

            timer = PerfectoUtils.getUXTimer(driver);
            System.out.println(System.lineSeparator() + "App launch time: "+ timer);
            PerfectoUtils.comment(driver, System.lineSeparator() + "App launch time: "+ timer);

            driver.findElementByXPath("//*[@label=\"Sign In\"]").click();
            PerfectoUtils.ocrTextCheck(driver, "Recover", 99, 30);

            driver.findElementByXPath("//*[@label=\"Email\"]").sendKeys(System.getenv("Netflix_Username"));
            driver.findElementByXPath("//*[@label=\"Password\"]").sendKeys(System.getenv("Netflix_Password"));
            driver.findElementByXPath("//*[@label=\"Sign In\"]").click();

            PerfectoUtils.ocrTextCheck(driver, "watching?", 99, 30);
            timer = PerfectoUtils.getUXTimer(driver);
            System.out.println(System.lineSeparator() + "App login time: "+ timer);
            PerfectoUtils.comment(driver, System.lineSeparator() + "App login time: "+ timer);

            driver.findElementByXPath("//*[@label=\"Amir\"]").click();

            PerfectoUtils.ocrTextCheck(driver, "search", 99, 30);
            driver.findElementByXPath("//*[@label=\"Search\"]").click();

            PerfectoUtils.ocrTextCheck(driver, "Download", 99, 30);
            driver.findElementByXPath("//*[@name=\"searchField\"]").sendKeys("Breaking Bad");

            PerfectoUtils.ocrTextCheck(driver, "movies", 99, 30);
            driver.getScreenshotAs(OutputType.BASE64);
            driver.findElementByXPath("//*[@label=\"Cancel\"]").click();

            // Start the sign out process
            driver.findElementByXPath("//*[@label=\"More\"]").click();
            PerfectoUtils.ocrTextCheck(driver, "Amir", 99, 30);

            PerfectoUtils.ocrTextClick(driver, "sign out", 99, 20);
            driver.findElementByXPath("//*[@label=\"Yes\"]").click();

            PerfectoUtils.ocrTextCheck(driver, "Offline", 99, 30);

            timer = PerfectoUtils.getUXTimer(driver);
            System.out.println(System.lineSeparator() + "App logout time: "+ timer);
            PerfectoUtils.comment(driver, System.lineSeparator() + "App logout time: "+ timer);



            PerfectoUtils.stopNetworkVirtualization(driver, "true");
            PerfectoUtils.vitalsStop(driver);

            reportiumClient.testStop(TestResultFactory.createSuccess());
        } catch (Exception e) {
            //reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
            e.printStackTrace();
        }
    }


    @AfterTest
    public void afterTest() {
        System.out.println("===>>> Entering: NewTestClass.afterTest()" );
        try {
            // Retrieve the URL of the Single Test Report, can be saved to your execution summary and used to download the report at a later point
            String reportURL = reportiumClient.getReportUrl();
            System.out.println("Report URL:" + reportURL);

            driver.close();
            System.out.println("Report: " + reportURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
        System.out.println("===>>> Exiting: NewTestClass.afterTest()" );
    }



}