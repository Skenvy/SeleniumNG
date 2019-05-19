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

public abstract class baseTest {
	
	protected NiceWebDriver nwd;
	private SeleniumNode seleniumNode = null;
	private boolean testInDevelopment = false;
	private boolean testIsBeingDemonstrated = false;
	private final NiceWebDriverFactory nwdf;
	
	public baseTest() {
		nwdf = NiceWebDriverFactory.getFactory(getPathToDomainConstantsConfig());
	}
	
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
	
	public abstract String getPathToDomainConstantsConfig();
	
	public baseTest newSubClassInstance() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		// Get the Class object, cast to an extension of this class, from the class name of the object calling this method
		Class<? extends baseTest> thisClass = (Class<? extends baseTest>) Class.forName(this.getClass().getName());
		// Create a new instance of the sub class from its parameterless constructor, typed to this baseTest class
		baseTest newObj = thisClass.getConstructor().newInstance();
		return newObj;
	}
	
	public baseTest newLocal(DriverType driverType) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		baseTest newObj = this.newSubClassInstance();
		newObj.seleniumNode = new SeleniumNode(true,null,driverType);
		return newObj;
	}
	
	public baseTest newRemote(URL nodeUrl, DriverType driverType) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		baseTest newObj = this.newSubClassInstance();
		newObj.seleniumNode = new SeleniumNode(false,nodeUrl,driverType);
		return newObj;
	}
	
	
	
	//@BeforeMethod < not super'd
	//This has been removed from here so as to implement a default factory
	//public abstract void beforeMethod();
  
	@BeforeClass
	public void beforeClass() throws MalformedURLException, FileNotFoundException {
		if(seleniumNode.local) {
			nwd = nwdf.getNiceWebDriver(seleniumNode.dt,"--incognito --start-maximized",DomainConstants.test.waitSeconds);
		} else {
			nwd = nwdf.getNiceWebDriverRemote(seleniumNode.dt, seleniumNode.nodeUrl ,DomainConstants.test.waitSeconds);
		}
	}

	@AfterClass
	public void afterClass() {
		if(this.testInDevelopment) {
			//This does nothing currently to block the closing of the window
		} else {
			nwd.closeWebDriver();
			nwd = null;
		}
	}
	
	@BeforeMethod
	public void beforeMethod() {
		nwd.openTestDefaultAtBase();
	}
	
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
	
	/*
	 * Clicks and keys
	 */
	
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
	
	/*
	 * Get hyperlink collections
	 */
	
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
	
	/*
	 * Data providers
	 */
	
	protected static Object[][] wrapStringCollectionToDataProvider(Collection<String> c) {
		Object[][] objArr = new Object[c.size()][1];
		int iter = 0;
		for(String s : c) {
			objArr[iter][0] = s;
			iter++;
		}
		return objArr;
	}

}
