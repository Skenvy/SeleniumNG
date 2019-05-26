package com.skenvy.SeleniumNG;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Scanner;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import com.skenvy.SeleniumNG.DomainConstants.SeleniumNode;
import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceChrome;
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
	protected NiceWebDriver nwd;
	
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
 * and a Data providers Helper function
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
		nwd.openTestDefaultAtBase();
	}
	
	/***
	 * Subclasses of the base test can utilise this to have their
	 * {@code @DataProvider} return an Object[][] type from a
	 * collection of Strings
	 * @param c
	 * @return
	 */
	public static Object[][] wrapStringCollectionToDataProvider(Collection<String> c) {
		Object[][] objArr = new Object[c.size()][1];
		int iter = 0;
		for(String s : c) {
			objArr[iter][0] = s;
			iter++;
		}
		return objArr;
	}
	
	//TODO Demarcation point
	
///////////////////////////////////////////////////////////////////////////////
/*
 * 
 */
///////////////////////////////////////////////////////////////////////////////
	
	public void declareThisTestAsCurrentlyBeingUnderDevelopment() {
		this.testInDevelopment = true;
	}
	
	public void declareThisTestAsCurrentlyBeingDemonstrated() {
		this.testIsBeingDemonstrated = true;
	}
	
	public void promptEnterKey() throws InterruptedException{
		if(this.testIsBeingDemonstrated) {
			if(nwd.isRunningRemotely()) {
				System.out.println("Press \"ENTER\" to continue...");
				Scanner scanner = new Scanner(System.in);
				scanner.nextLine();
			} else {
				Thread.sleep(DomainConstants.testSleeps.MilliSecondSimulateInteractivePause);
			}
		}
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Clicks and keys
 */
///////////////////////////////////////////////////////////////////////////////
	
	private void sleepBeforeClicking() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testSleeps.MilliSecondsBeforeClick);
		}
	}
	
	private void sleepAfterClicking() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testSleeps.MilliSecondsAfterClick);
		}
	}
	
	private void sleepBetweenKeyStrokes() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testSleeps.MilliSecondsBetweenKeyStrokes);
		}
	}
	
	protected void sleepForTheDurationOfAPrompt() throws InterruptedException {
		Thread.sleep(DomainConstants.testSleeps.MilliSecondSimulateInteractivePause);
	}
	
	protected void sleepForTheDurationOfASuccessMessagePrompt() throws InterruptedException {
		nwdf.getDomainConstants();
		Thread.sleep(DomainConstants.testSleeps.MilliSecondDurationOfSuccessMessage);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * 
 */
///////////////////////////////////////////////////////////////////////////////
	
	protected WebElement clickOnCSSElementIfExists(String cssSelector) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnCSSElementIfExists(cssSelector);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnXPathElementIfExists(String xpath) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnXPathElementIfExists(xpath);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnLinkTextElementIfExists(String linkText) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnLinkTextElementIfExists(linkText);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnIdElementIfExists(String id) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnIdElementIfExists(id);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnAnchorHrefElementIfExists(String href, boolean visibleOnly) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = nwd.clickOnAnchorHrefElementIfExists(href,visibleOnly);
		sleepAfterClicking();
		return we;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * 
 */
///////////////////////////////////////////////////////////////////////////////
	
	protected void sendKeysToAWebElement(WebElement we, String keyStrokes) throws InterruptedException {
		for(char keyStroke : keyStrokes.toCharArray()) {
			nwd.sendKeysToAWebElement(we,keyStroke+"");
			sleepBetweenKeyStrokes();
		}
	}
	
	protected void sendKeysToAWebElement(WebElement we, Keys[] keyStrokes) throws InterruptedException {
		for(Keys keyStroke : keyStrokes) {
			we.sendKeys(keyStroke);
			sleepBetweenKeyStrokes();
		}
	}
	
	protected void backspaceKeysFromAWebElement(WebElement we, int amountOfBackspaces) throws InterruptedException {
		for(int k = 0; k < amountOfBackspaces; k++) {
			sendKeysToAWebElement(we,"\u0008");
		}
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Get hyperlink collections
 */
///////////////////////////////////////////////////////////////////////////////
	
	protected void AssertSubrootDoesNotLeadTo404(String subroot) {
		nwd.openTestDefaultWithSubroot(subroot);
		Assert.assertFalse(nwd.isWebPage404());
	}
	
	/*
	 * Assert for Href
	 */
	
	protected void AssertHrefExists(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to pass");
		Assert.assertTrue(nwd.AnchorExistsWithHREF(href, false));
	}
	
	protected void AssertHrefNotVisible(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to fail");
		Assert.assertFalse(nwd.AnchorExistsWithHREF(href, true));
	}
	
	protected void AssertHrefExistsAndIsVisible(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to pass");
		Assert.assertTrue(nwd.AnchorExistsWithHREF(href, true));
	}
	
	protected void AssertHrefExistsButIsHidden(String methodName, String href) {
		AssertHrefExists(methodName,href);
		AssertHrefNotVisible(methodName,href);
	}
	
	protected void AssertHrefDoesNotExist(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to fail");
		Assert.assertFalse(nwd.AnchorExistsWithHREF(href, false));
	}
	
	/*
	 * Assert dropdown clickable
	 */
	
	protected void AssertDropdownMenuClickableAndExists(String anchorId) {
		Assert.assertNotNull(nwd.clickOnCSSElementIfExists(anchorId));
	}
	
	protected void AssertHyperlinkExistsAndIsClickable(String href) {
		String query = nwd.AnchorQueryStringForHREF(href, true);
		Assert.assertNotNull(nwd.clickOnCSSElementIfExists(query));
	}
	
	protected void AssertHyperlinkExistsAndIsClickableAndDoesNotLeadTo404(String href) {
		AssertHyperlinkExistsAndIsClickable(href);
		Assert.assertFalse(nwd.isWebPage404());
	}

}
