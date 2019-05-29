package com.skenvy.SeleniumNG;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import com.skenvy.SeleniumNG.DomainConstants.SeleniumNode;
import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriver;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriverFactory;

/***
 * An abstract base class that must be extended and have the method
 * {@code getPathToDomainConstantsConfig()} overridden to suffice the
 * utilisation of having multiple "selenium node" configurations read in
 * from an external XML config file and have a single invoking subclass
 * executed across all the defined selenium nodes. It is recommended to
 * Make a local abstract extension of this class which overrides the
 * {@code getPathToDomainConstantsConfig()}, and then sub class that class!
 */
public abstract class baseTest {
	
///////////////////////////////////////////////////////////////////////////////
/*
 * vars, the parameterless constructor, and the abstract path encapsulator
 */
///////////////////////////////////////////////////////////////////////////////

	
	/***
	 * An instance of the NiceWebDriver which will be used to carry out the
	 * standard WebDriver functionality required of the tests
	 */
	private NiceWebDriver nwd;
	
	/***
	 * The local selenium node that defines whether the test is running locally
	 * or remotely, what the remote URL is, and what DriverType is required
	 */
	private SeleniumNode seleniumNode = null;
	
	/***
	 * Set this to true by invoking 
	 * {@code declareThisTestAsCurrentlyBeingUnderDevelopment()},
	 * which will prevent the browser window from being closed at the
	 * end of the running test, to allow debugging the state of the browser
	 * when the test ends or is interrupted with an exception.
	 * Invoke {@code declareThisTestAsCurrentlyBeingUnderDevelopment()} at the
	 * start of any {@code @Test} case you want this to apply to.
	 */
	private boolean testInDevelopment = false;
	
	/***
	 * Set this to true by invoking 
	 * {@code declareThisTestAsCurrentlyBeingDemonstrated()},
	 * which will slow the execution of the clicking of WebElements and typing
	 * Invoke {@code declareThisTestAsCurrentlyBeingDemonstrated()} at the
	 * start of any {@code @Test} case you want this to apply to.
	 */
	private boolean testIsBeingDemonstrated = false;
	
	/***
	 * Set this to true by invoking 
	 * {@code declaseThisTestAsHavingVerboseOutput()}, to have verbose output
	 * enabled, or false with {@code declaseThisTestAsNotHavingVerboseOutput()}
	 */
	private boolean testOutputIsVerbose = false;
	
	/***
	 * The base test's reference of the NiceWebDriverFactory singleton
	 */
	private final NiceWebDriverFactory nwdf;
	
	/***
	 * The parameterless constructor. Instantiates the NiceWebDriverFactory,
	 * which in its own turn initialises the static variables of the
	 * Domain Constants.
	 */
	public baseTest() {
		nwdf = NiceWebDriverFactory.getFactory(getPathToDomainConstantsConfig());
	}
	
	/***
	 * Make an abstract "base" class for your tests, which overrides this
	 * and has it return the path to your xml configuration file. You may
	 * maintain more than one base class that point to different configuration
	 * files, although if they invoke the constructor of any other subclass to
	 * this baseTest with a different path returned by this method, then the
	 * Domain Constants will only match those for the first invocation and the
	 * singleton NiceWebDriverFactory will block the overwriting of Domain
	 * Constants that have already been read in from the first config file!
	 * @return
	 */
	public abstract String getPathToDomainConstantsConfig();
	
///////////////////////////////////////////////////////////////////////////////
/*
 * The @Factory solution to instantiating multiple Selenium tests!
 * Uses a factory to capture the runtime class of the subclass of the baseTest
 * then invokes the parameterless constructor and assigns the appropriate
 * SeleniumNode instantiation.
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * A non-static Factory method that will create multiple instances of any
	 * test class that extends the baseTest. The purpose is that any test class
	 * which extends the baseTest class, when it begins test execution, will
	 * spin up several isolated instances of the test with either the webdriver
	 * or the grid-node configuration.
	 * @throws InterruptedException
	 */
	@Factory
	public Object[] createInstances() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		// Create an array the size of the Selenium node configuration constant
		Object[] result = new Object[DomainConstants.seleniumNodes.length];
		// We need to translate the iteration of a forall into an array
		// So we use an incrementer ++ and for(any:all)
		int incr = 0;
		for(SeleniumNode seleniumNode : DomainConstants.seleniumNodes) {
			if(seleniumNode.local) {
				// If the node maps to a null DesiredCapabilites then it must be a local instance
				result[incr] = this.newLocal(seleniumNode.dt);
			} else {
				// Otherwise we can procure a new remote instance using the de
				result[incr] = this.newRemote(seleniumNode.nodeUrl,seleniumNode.dt);
			}
			incr++;
		}
        return result;
    }
	
	/***
	 * Finds the class object for the instantiating subclass, gets the subclass
	 * constructor, invokes the parameterless subclass constructor, and returns
	 * a new instance of the subclass passed as a reference to the baseTest.
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public baseTest newSubClassInstance() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		// Get the Class object, cast to an extension of this class, from the class name of the object calling this method
		Class<? extends baseTest> thisClass = (Class<? extends baseTest>) Class.forName(this.getClass().getName());
		// Create a new instance of the sub class from its parameterless constructor, typed to this baseTest class
		baseTest newObj = thisClass.getConstructor().newInstance();
		return newObj;
	}
	
	/***
	 * Create a new local instance of the invoking subclass.
	 * @param driverType
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public baseTest newLocal(DriverType driverType) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		baseTest newObj = this.newSubClassInstance();
		newObj.seleniumNode = new SeleniumNode(true,null,driverType);
		return newObj;
	}
	
	/***
	 * Create a new remote instance of the invoking subclass.
	 * @param nodeUrl
	 * @param driverType
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public baseTest newRemote(URL nodeUrl, DriverType driverType) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		baseTest newObj = this.newSubClassInstance();
		newObj.seleniumNode = new SeleniumNode(false,nodeUrl,driverType);
		return newObj;
	}
	
///////////////////////////////////////////////////////////////////////////////
/* 
 * The primary TestNG annotations @BeforeClass, @AfterClass and @BeforeMethod
 * and a Data providers Helper functions
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Before a class starts executing its test methods, use the 
	 * NiceWebDriverFactory to instantiate a new NiceWebDriver of the type
	 * requested in the configuration file.
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 */
	@BeforeClass
	public void beforeClass() throws MalformedURLException, FileNotFoundException {
		if(seleniumNode.local) {
			//TODO incorporate "arguments" passed in through the config, and remove these Chrome specific options
			nwd = nwdf.getNiceWebDriver(seleniumNode.dt,"--incognito --start-maximized",DomainConstants.test.waitSeconds);
		} else {
			nwd = nwdf.getNiceWebDriverRemote(seleniumNode.dt, seleniumNode.nodeUrl ,DomainConstants.test.waitSeconds);
		}
	}

	/***
	 * After a class has finished executing its test methods, close the driver
	 * and break the link to the NiceWebdriver object
	 */
	@AfterClass
	public void afterClass() {
		if(this.testInDevelopment) {
			//This does nothing to block the closing of the window
		} else {
			nwd.closeWebDriver();
			nwd = null;
		}
	}

	/***
	 * Before each test method, have the browser load the page specified by the
	 * "Test.*" entries in the config file, specifically the IP, port and
	 * Context Root.
	 */
	@BeforeMethod
	public void beforeMethod() {
		//TODO Make the protocol a configurable argument
		nwd.openTestDefaultWithHTTPSAtBase();
	}
	
	/***
	 * Subclasses of the base test can utilise this to have their
	 * {@code @DataProvider} return an {@code Object[collection.size()][1]}
	 * type from a collection of some object
	 * @param c
	 * @return
	 */
	public static <K> Object[][] wrapCollectionToDataProvider(Collection<K> collection) {
		Object[][] objArr = new Object[collection.size()][1];
		int iter = 0;
		for(K k : collection) {
			objArr[iter][0] = k;
			iter++;
		}
		return objArr;
	}
	
	/***
	 * Subclasses of the base test can utilise this to have their
	 * {@code @DataProvider} return an {@code Object[list.size()][1]} type from
	 * a list of some object
	 * @param c
	 * @return
	 */
	public static <K> Object[][] wrapListToDataProvider(List<K> list) {
		Object[][] objArr = new Object[list.size()][1];
		int iter = 0;
		for(K k : list) {
			objArr[iter][0] = k;
			iter++;
		}
		return objArr;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Unwrappers : Getters for the private final fields if the methods here
 * do not fully encapsulate the behavour necessary of the NiceWebDriver
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Get the underlying NiceWebDriver
	 * @return
	 */
	public NiceWebDriver unwrapNiceWebDriver() {
		return nwd;
	}
	
	/***
	 * Get the underlying NiceWebDriverFactory
	 * @return
	 */
	public NiceWebDriverFactory unwrapNiceWebDriverFactory() {
		return nwdf;
	}
	
	/***
	 * Get the underlying SeleniumNode
	 * @return
	 */
	public SeleniumNode unwrapSeleniumNode() {
		return seleniumNode;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Functionality that allows for a test to self declare that it is "under
 * development" for the purposes of debugging the browser if a test throws an
 * exception, and to declare a test as being "demonstrated" which will prompt
 * periodic sleeps for each action that should be slowed down for demonstration
 * purposes! Also turn verbose output on or off
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Call this at the start of an {@code @Test} annotated method to have it
	 * not shut the browser when the test ends, such that if the test ends with
	 * an exception, the browser will remain open for debugging
	 */
	public void declareThisTestAsCurrentlyBeingUnderDevelopment() {
		this.testInDevelopment = true;
	}

	/***
	 * Call this at the start of an {@code @Test} annotated method to have it
	 * slow down actions such as clicking and typing keys.
	 */
	public void declareThisTestAsCurrentlyBeingDemonstrated() {
		this.testIsBeingDemonstrated = true;
	}
	
	/***
	 * Call this at the start of an {@code @Test} annotated method to have
	 * verbose output!
	 */
	public void declaseThisTestAsHavingVerboseOutput() {
		this.testOutputIsVerbose = true;
		assignVerbosityToTheNWDFAndNWD();
	}
	
	/***
	 * Call this at the start of an {@code @Test} annotated method to have
	 * verbose output that was previously turned on, turned off!
	 */
	public void declaseThisTestAsNotHavingVerboseOutput() {
		this.testOutputIsVerbose = false;
		assignVerbosityToTheNWDFAndNWD();
	}
	
	/***
	 * Trickle down the verbosity declared by an individual test to the
	 * NiceWebDriver and NiceWebDriverFactory
	 */
	private void assignVerbosityToTheNWDFAndNWD() {
		nwdf.setVerboseModeOn(this.testOutputIsVerbose);
		nwd.getThisWithVerbositySetTo(this.testOutputIsVerbose);
	}

	/***
	 * Call this during a test to have the browser temporarily pause, until at
	 * least either the local timeframe has elapsed or the remote instance is
	 * triggered again by pressing enter on the machine running the test, as
	 * an enhanced "demonstration" which requires explaining static views.
	 * @throws InterruptedException
	 */
	public void promptPauseSoft() throws InterruptedException{
		if(this.testIsBeingDemonstrated) {
			if(nwd.isRunningRemotely()) {
				promptPauseHard();
			} else {
				sleepForTheDurationOfAPrompt();
			}
		}
	}
	
	/***
	 * Call this during a test to have the browser paused until user input is
	 * provided to the running class.
	 * @throws InterruptedException
	 */
	public void promptPauseHard() throws InterruptedException{
		System.out.println("Press \"ENTER\" to continue...");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		scanner.close();
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Sleep, if the @Test is currently being demonstrated
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Sleep before a click
	 * @throws InterruptedException
	 */
	private void sleepBeforeClicking() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testSleeps.MilliSecondsBeforeClick);
		}
	}

	/***
	 * Sleep after a click
	 * @throws InterruptedException
	 */
	private void sleepAfterClicking() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testSleeps.MilliSecondsAfterClick);
		}
	}

	/***
	 * Sleep between keystrokes
	 * @throws InterruptedException
	 */
	private void sleepBetweenKeyStrokes() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testSleeps.MilliSecondsBetweenKeyStrokes);
		}
	}

	/***
	 * Sleep for the duration of a prompted pause
	 * @throws InterruptedException
	 */
	private void sleepForTheDurationOfAPrompt() throws InterruptedException {
		Thread.sleep(DomainConstants.testSleeps.MilliSecondSimulateInteractivePause);
	}

	/***
	 * Call this in {@code @Test} annotated methods to sleep for the duration a
	 * success message will be displayed for by the page, as this may block the
	 * WebDriver from immediately following a success message
	 * @throws InterruptedException
	 */
	public void sleepForTheDurationOfASuccessMessagePrompt() throws InterruptedException {
		nwdf.getDomainConstants();
		Thread.sleep(DomainConstants.testSleeps.MilliSecondDurationOfSuccessMessage);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Click on things
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Clicks on a WebElement found using a CSS Selector, wrapped with 
	 * demonstration sleeps.
	 * @param cssSelector
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement clickOnCSSElementIfExists(String cssSelector) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnCSSElementIfExists(cssSelector);
		sleepAfterClicking();
		return we;
	}

	/***
	 * Clicks on a WebElement found using an XPath, wrapped with 
	 * demonstration sleeps.
	 * @param xpath
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement clickOnXPathElementIfExists(String xpath) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnXPathElementIfExists(xpath);
		sleepAfterClicking();
		return we;
	}

	/***
	 * Clicks on a WebElement found using a link text, wrapped with 
	 * demonstration sleeps.
	 * @param linkText
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement clickOnLinkTextElementIfExists(String linkText) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnLinkTextElementIfExists(linkText);
		sleepAfterClicking();
		return we;
	}

	/***
	 * Clicks on a WebElement found using an ID, wrapped with 
	 * demonstration sleeps.
	 * @param id
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement clickOnIdElementIfExists(String id) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnIdElementIfExists(id);
		sleepAfterClicking();
		return we;
	}

	/***
	 * Clicks on a WebElement found using an href, wrapped with 
	 * demonstration sleeps.
	 * @param href
	 * @param visibleOnly
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement clickOnAnchorHrefElementIfExists(String href, boolean visibleOnly) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnAnchorHrefElementIfExists(href,visibleOnly);
		sleepAfterClicking();
		return we;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Send keys! : Strings
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Sends keys to a WebElement found using a CSS Selector, wrapped with 
	 * demonstration sleeps.
	 * @param cssSelector
	 * @return
	 * @throws InterruptedException 
	 */
	public WebElement sendKeysToCSSElementIfExists(String cssSelector, String keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToCSSElementIfExists(cssSelector,"");
		for(char keyStroke : keyStrokes.toCharArray()) {
			nwd.sendKeysToANonNullWebElement(we,keyStroke+"");
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using an XPath, wrapped with 
	 * demonstration sleeps.
	 * @param xpath
	 * @return
	 * @throws InterruptedException 
	 */
	public WebElement sendKeysToXPathElementIfExists(String xpath, String keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToXPathElementIfExists(xpath,"");
		for(char keyStroke : keyStrokes.toCharArray()) {
			nwd.sendKeysToANonNullWebElement(we,keyStroke+"");
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using a link text, wrapped with 
	 * demonstration sleeps.
	 * @param linkText
	 * @return
	 * @throws InterruptedException 
	 */
	public WebElement sendKeysToLinkTextElementIfExists(String linkText, String keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToLinkTextElementIfExists(linkText,"");
		for(char keyStroke : keyStrokes.toCharArray()) {
			nwd.sendKeysToANonNullWebElement(we,keyStroke+"");
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using an ID, wrapped with 
	 * demonstration sleeps.
	 * @param id
	 * @return
	 * @throws InterruptedException 
	 */
	public WebElement sendKeysToIdElementIfExists(String id, String keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToIdElementIfExists(id,"");
		for(char keyStroke : keyStrokes.toCharArray()) {
			nwd.sendKeysToANonNullWebElement(we,keyStroke+"");
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using an href, wrapped with 
	 * demonstration sleeps.
	 * @param href
	 * @param visibleOnly
	 * @return
	 * @throws InterruptedException 
	 */
	public WebElement sendKeysToAnchorHrefElementIfExists(String href, String keyStrokes, boolean visibleOnly) throws InterruptedException {
		WebElement we = nwd.sendKeysToAnchorHrefElementIfExists(href,"",visibleOnly);
		for(char keyStroke : keyStrokes.toCharArray()) {
			nwd.sendKeysToANonNullWebElement(we,keyStroke+"");
			sleepBetweenKeyStrokes();
		}
		return we;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Send keys! : Keys[]
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Sends keys to a WebElement found using a CSS Selector, wrapped with
	 * demonstration sleeps.
	 * @param cssSelector
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysToCSSElementIfExists(String cssSelector, Keys[] keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToCSSElementIfExists(cssSelector, "");
		for (Keys keyStroke : keyStrokes) {
			nwd.sendKeysToANonNullWebElement(we, new Keys[]{keyStroke});
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using an XPath, wrapped with demonstration
	 * sleeps.
	 * @param xpath
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysToXPathElementIfExists(String xpath, Keys[] keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToXPathElementIfExists(xpath, "");
		for (Keys keyStroke : keyStrokes) {
			nwd.sendKeysToANonNullWebElement(we, new Keys[]{keyStroke});
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using a link text, wrapped with
	 * demonstration sleeps.
	 * @param linkText
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysToLinkTextElementIfExists(String linkText, Keys[] keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToLinkTextElementIfExists(linkText, "");
		for (Keys keyStroke : keyStrokes) {
			nwd.sendKeysToANonNullWebElement(we, new Keys[]{keyStroke});
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using an ID, wrapped with demonstration
	 * sleeps.
	 * @param id
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysToIdElementIfExists(String id, Keys[] keyStrokes) throws InterruptedException {
		WebElement we = nwd.sendKeysToIdElementIfExists(id, "");
		for (Keys keyStroke : keyStrokes) {
			nwd.sendKeysToANonNullWebElement(we, new Keys[]{keyStroke});
			sleepBetweenKeyStrokes();
		}
		return we;
	}

	/***
	 * Sends keys to a WebElement found using an href, wrapped with demonstration
	 * sleeps.
	 * @param href
	 * @param visibleOnly
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysToAnchorHrefElementIfExists(String href, Keys[] keyStrokes, boolean visibleOnly) throws InterruptedException {
		WebElement we = nwd.sendKeysToAnchorHrefElementIfExists(href, "", visibleOnly);
		for (Keys keyStroke : keyStrokes) {
			nwd.sendKeysToANonNullWebElement(we, new Keys[]{keyStroke});
			sleepBetweenKeyStrokes();
		}
		return we;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Send keys after clicking an element! : Strings
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Sends keys to a WebElement found using a CSS Selector, wrapped with
	 * demonstration sleeps, after clicking the WebElement, and clearing it.
	 * @param cssSelector
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToCSSElementIfExists(String cssSelector, String keyStrokes) throws InterruptedException {
		clickOnCSSElementIfExists(cssSelector).clear();
		return sendKeysToCSSElementIfExists(cssSelector,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an XPath, wrapped with demonstration
	 * sleeps, after clicking the WebElement, and clearing it.
	 * @param xpath
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToXPathElementIfExists(String xpath, String keyStrokes) throws InterruptedException {
		clickOnXPathElementIfExists(xpath).clear();
		return sendKeysToXPathElementIfExists(xpath,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using a link text, wrapped with
	 * demonstration sleeps, after clicking the WebElement, and clearing it.
	 * @param linkText
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToLinkTextElementIfExists(String linkText, String keyStrokes) throws InterruptedException {
		clickOnLinkTextElementIfExists(linkText).clear();
		return sendKeysToLinkTextElementIfExists(linkText,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an ID, wrapped with demonstration
	 * sleeps, after clicking the WebElement, and clearing it.
	 * @param id
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToIdElementIfExists(String id, String keyStrokes) throws InterruptedException {
		clickOnIdElementIfExists(id).clear();
		return sendKeysToIdElementIfExists(id,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an href, wrapped with demonstration
	 * sleeps, after clicking the WebElement, and clearing it.
	 * @param href
	 * @param visibleOnly
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToAnchorHrefElementIfExists(String href, String keyStrokes, boolean visibleOnly) throws InterruptedException {
		clickOnAnchorHrefElementIfExists(href,visibleOnly).clear();
		return sendKeysToAnchorHrefElementIfExists(href,keyStrokes,visibleOnly);
	}

///////////////////////////////////////////////////////////////////////////////
/*
 * Send keys after clicking an element! : Keys[]
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Sends keys to a WebElement found using a CSS Selector, wrapped with
	 * demonstration sleeps, after clicking the WebElement, and clearing it.
	 * @param cssSelector
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToCSSElementIfExists(String cssSelector, Keys[] keyStrokes) throws InterruptedException {
		clickOnCSSElementIfExists(cssSelector).clear();
		return sendKeysToCSSElementIfExists(cssSelector,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an XPath, wrapped with demonstration
	 * sleeps, after clicking the WebElement, and clearing it.
	 * @param xpath
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToXPathElementIfExists(String xpath, Keys[] keyStrokes) throws InterruptedException {
		clickOnXPathElementIfExists(xpath).clear();
		return sendKeysToXPathElementIfExists(xpath,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using a link text, wrapped with
	 * demonstration sleeps, after clicking the WebElement, and clearing it.
	 * @param linkText
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToLinkTextElementIfExists(String linkText, Keys[] keyStrokes) throws InterruptedException {
		clickOnLinkTextElementIfExists(linkText).clear();
		return sendKeysToLinkTextElementIfExists(linkText,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an ID, wrapped with demonstration
	 * sleeps, after clicking the WebElement, and clearing it.
	 * @param id
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToIdElementIfExists(String id, Keys[] keyStrokes) throws InterruptedException {
		clickOnIdElementIfExists(id).clear();
		return sendKeysToIdElementIfExists(id,keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an href, wrapped with demonstration
	 * sleeps, after clicking the WebElement, and clearing it.
	 * @param href
	 * @param visibleOnly
	 * @return
	 * @throws InterruptedException
	 */
	public WebElement sendKeysAfterClickingToAnchorHrefElementIfExists(String href, Keys[] keyStrokes, boolean visibleOnly) throws InterruptedException {
		clickOnAnchorHrefElementIfExists(href,visibleOnly).clear();
		return sendKeysToAnchorHrefElementIfExists(href,keyStrokes,visibleOnly);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Assertions that may be relevant in locating common page elements
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Assert that loading the default web page under test with some subroot
	 * does not result in an Http404
	 * @param subroot
	 */
	public void AssertSubrootDoesNotLeadTo404(String subroot) {
		nwd.openTestDefaultWithHTTPSAndSubroot(subroot);
		Assert.assertFalse(nwd.isWebPage404());
	}
	
	/*
	 * Assert for Href : does it exist or is it visible?
	 */

	/***
	 * Asserts that a href does exist
	 * @param methodName
	 * @param href
	 */
	public void AssertHrefExists(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to pass");
		Assert.assertTrue(nwd.AnchorExistsWithHREF(href, false));
	}

	/***
	 * Assert that the href is not visible, whether or not it exists
	 * @param methodName
	 * @param href
	 */
	public void AssertHrefNotVisible(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to fail");
		Assert.assertFalse(nwd.AnchorExistsWithHREF(href, true));
	}

	/***
	 * Asser that the href exists and is visible
	 * @param methodName
	 * @param href
	 */
	public void AssertHrefExistsAndIsVisible(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to pass");
		Assert.assertTrue(nwd.AnchorExistsWithHREF(href, true));
	}

	/***
	 * Assert that the href exists but is explicitly hidden
	 * @param methodName
	 * @param href
	 */
	public void AssertHrefExistsButIsHidden(String methodName, String href) {
		AssertHrefExists(methodName,href);
		AssertHrefNotVisible(methodName,href);
	}

	/***
	 * Asserts that a href does not exist
	 * @param methodName
	 * @param href
	 */
	public void AssertHrefDoesNotExist(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to fail");
		Assert.assertFalse(nwd.AnchorExistsWithHREF(href, false));
	}
	
	/*
	 * Assert hyperlink is clickable
	 */

	/***
	 * Asserts that a hyperlink can be clicked
	 * @param href
	 */
	public void AssertHyperlinkExistsAndIsClickable(String href) {
		String query = nwd.AnchorQueryStringForHREF(href, true);
		Assert.assertNotNull(nwd.clickOnCSSElementIfExists(query));
	}

	/***
	 * Asserts that a hyperlink can be clicked and does not result in a Http404
	 * @param href
	 */
	public void AssertHyperlinkExistsAndIsClickableAndDoesNotLeadTo404(String href) {
		AssertHyperlinkExistsAndIsClickable(href);
		Assert.assertFalse(nwd.isWebPage404());
	}

}
