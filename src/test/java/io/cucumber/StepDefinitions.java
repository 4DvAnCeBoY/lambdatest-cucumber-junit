package io.cucumber;

import cucumber.api.PickleStepTestStep;
import cucumber.api.Scenario;
import cucumber.api.TestCase;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import cucumber.api.java.BeforeStep;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StepDefinitions {
    private RemoteWebDriver driver;
    private String sessionId;

    private String username = System.getenv("LT_USERNAME");
    private String accesskey = System.getenv("LT_ACCESS_KEY");

    @Before
    public void setUp(Scenario scenario) throws MalformedURLException {
        // Add Capabilities : https://www.lambdatest.com/capabilities-generator/

        ChromeOptions browserOptions = new ChromeOptions();
        // browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("latest");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("username", username);
        ltOptions.put("accessKey", accesskey);
        ltOptions.put("project", "Untitled");
        ltOptions.put("name", scenario.getName());
        ltOptions.put("build", "testing");

        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-junit-cucumber");
        browserOptions.setCapability("LT:Options", ltOptions);

        // Create a new RemoteWebDriver instance, which initialize the test execution on
        // LambdaTest Hub
        String LT_HUB_URL = "https://hub.lambdatest.com/wd/hub";
        driver = new RemoteWebDriver(new URL(LT_HUB_URL), browserOptions);
        sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        System.out.println(sessionId);
    }

    private int currentStepDefIndex = 0;

    @BeforeStep
    public void stepContextBeforeStep(Scenario scenario) throws Exception {
    
        Field f = scenario.getClass().getDeclaredField("testCase");
        f.setAccessible(true);
        TestCase r = (TestCase) f.get(scenario);
    
        //You need to filter out before/after hooks
        List<PickleStepTestStep> stepDefs = r.getTestSteps()
                .stream()
                .filter(x -> x instanceof PickleStepTestStep)
                .map(x -> (PickleStepTestStep) x)
                .collect(Collectors.toList());
    
    
        //This object now holds the information about the current step definition
        //If you are using pico container 
        //just store it somewhere in your world state object 
        //and to make it available in your step definitions.
        PickleStepTestStep currentStepDef = stepDefs
                .get(currentStepDefIndex);
                driver.executeScript("lambdatest_executor: {\"action\": \"stepcontext\", \"arguments\": {\"data\": "+ currentStepDef.getStepText()+", \"level\": \"info\"}}");
    }
    
    @AfterStep
    public void stepContextAfterStep(Scenario scenario) {
        currentStepDefIndex += 1;
    }

    @After
    public void tearDown(Scenario scenario) {
        String status = "falied";
        if (scenario.isFailed()) {
            status = "falied";

        } else {
            status = "passed";
        }
        driver.executeScript("lambda-status=" + status, "");
        driver.quit();
    }

    @Given("^user is on home Page$")
    public void user_already_on_home_page() throws InterruptedException {
        System.out.println(driver.getCapabilities());
        driver.get("https://lambdatest.github.io/sample-todo-app/");
        Thread.sleep(5000);
    }

    @When("^select First Item$")
    public void select_first_item() {
        WebElement li = driver.findElement(By.name("li1"));
        li.click();
    }

    @Then("^select second item$")
    public void select_second_item() {
        WebElement li = driver.findElement(By.name("li2"));
        li.click();
    }

    @Then("^add new item$")
    public void add_new_item() throws InterruptedException {
        WebElement text = driver.findElement(By.id("sampletodotext"));
        text.clear();
        driver.findElement(By.id("sampletodotext")).sendKeys("Yey, Let's add it to list");
        Thread.sleep(1000);
        driver.findElement(By.id("addbutton")).click();
        Thread.sleep(2000);
    }

    @Then("^verify added item$")
    public void verify_added_item() {
        String item = driver.findElement(By.xpath("/html/body/div/div/div/ul/li[6]/span")).getText();
        Assert.assertTrue(item.contains("Yey, Let's add it to list"));
    }

}
