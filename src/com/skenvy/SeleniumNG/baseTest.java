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

import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceChrome;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriver;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriverFactory;

public abstract class baseTest {
	
	protected NiceWebDriver chrome;
	private RemoteConfig remoteConfig = null;
	private boolean testInDevelopment = false;
	private boolean testIsBeingDemonstrated = false;
	private final NiceWebDriverFactory nwdf;
	
	baseTest(String configFilePath) throws IOException{
		nwdf = NiceWebDriverFactory.getFactory(configFilePath);
	}
	
	private class RemoteConfig{
		
		String nodeUrl;
		DesiredCapabilities capabilities;
		
	}
	
	public baseTest startAsRemote(String nodeUrl, DesiredCapabilities capabilities) {
		this.remoteConfig = new RemoteConfig();
		this.remoteConfig.nodeUrl = nodeUrl;
		this.remoteConfig.capabilities = capabilities;
		return this;
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
		Object[] result = new Object[DomainConstants.seleniumNodes.size()];
		// We need to translate the iteration of a set into an array
		// So we use an incrementer ++ and for(any:all)
		int incr = 0;
		for(String nodeUrl : DomainConstants.seleniumNodes.keySet()) {
			if(DomainConstants.seleniumNodes.get(nodeUrl) == null) {
				// If the node maps to a null DesiredCapabilites then it must be a local instance
				result[incr] = this.newLocal(getPathToDomainConstantsConfig());
			} else {
				// Otherwise we can procure a new remote instance using the de
				result[incr] = this.newRemote(getPathToDomainConstantsConfig(),nodeUrl,DomainConstants.seleniumNodes.get(nodeUrl));
			}
			incr++;
		}
        return result;
    }
	
	public abstract String getPathToDomainConstantsConfig();
	
	public baseTest newSubClassInstance(String configFilePath) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		// Get the Class object, cast to an extension of this class, from the class name of the object calling this method
		Class<? extends baseTest> thisClass = (Class<? extends baseTest>) Class.forName(this.getClass().getName());
		// Create a new instance of the sub class from its parameterless constructor, typed to this baseTest class
		baseTest newObj = thisClass.getConstructor(new Class[] { String.class }).newInstance(configFilePath);
		return newObj;
	}
	
	public baseTest newLocal(String configFilePath) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		baseTest newObj = this.newSubClassInstance(configFilePath);
		newObj.remoteConfig = null;
		return newObj;
	}
	
	public baseTest newRemote(String configFilePath, String nodeUrl, DesiredCapabilities capabilities) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		baseTest newObj = this.newSubClassInstance(configFilePath);
		newObj.remoteConfig = new RemoteConfig();
		newObj.remoteConfig.nodeUrl = nodeUrl;
		newObj.remoteConfig.capabilities = capabilities;
		return newObj;
	}
	
	
	
	//@BeforeMethod < not super'd
	//This has been removed from here so as to implement a default factory
	//public abstract void beforeMethod();
  
	@BeforeClass
	public void beforeClass() throws MalformedURLException, FileNotFoundException {
		if(remoteConfig == null) {
			chrome = nwdf.getNiceWebDriver(DriverType.Chrome,"--incognito --start-maximized",DomainConstants.testDefault.defaultWaitSeconds);
		} else {
			chrome = nwdf.getNiceWebDriverRemote(DriverType.Chrome, new URL(remoteConfig.nodeUrl),DomainConstants.testDefault.defaultWaitSeconds);
		}
	}

	@AfterClass
	public void afterClass() {
		if(this.testInDevelopment) {
			//This does nothing currently to block the closing of the window
		} else {
			chrome.closeWebDriver();
			chrome = null;
		}
	}
	
	@BeforeMethod
	public void beforeMethod() {
		chrome.openTestDefaultAtBase();
	}
	
	public void declareThisTestAsCurrentlyBeingUnderDevelopment() {
		this.testInDevelopment = true;
	}
	
	public void declareThisTestAsCurrentlyBeingDemonstrated() {
		this.testIsBeingDemonstrated = true;
	}
	
	public void promptEnterKey() throws InterruptedException{
		if(this.testIsBeingDemonstrated) {
			if(chrome.isRunningRemotely()) {
				System.out.println("Press \"ENTER\" to continue...");
				Scanner scanner = new Scanner(System.in);
				scanner.nextLine();
			} else {
				Thread.sleep(DomainConstants.testDefault.SleepMilliSeconds.simulateInteractivePause);
			}
		}
	}
	
	/*
	 * Clicks and keys
	 */
	
	private void sleepBeforeClicking() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testDefault.SleepMilliSeconds.beforeClick);
		}
	}
	
	private void sleepAfterClicking() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testDefault.SleepMilliSeconds.afterClick);
		}
	}
	
	private void sleepBetweenKeyStrokes() throws InterruptedException {
		if(this.testIsBeingDemonstrated) {
			Thread.sleep(DomainConstants.testDefault.SleepMilliSeconds.betweenKeyStrokes);
		}
	}
	
	protected void sleepForTheDurationOfAPrompt() throws InterruptedException {
		Thread.sleep(DomainConstants.testDefault.SleepMilliSeconds.simulateInteractivePause);
	}
	
	protected void sleepForTheDurationOfASuccessMessagePrompt() throws InterruptedException {
		Thread.sleep(DomainConstants.testDefault.SleepMilliSeconds.durationOfSuccessMessage);
	}
	
	protected WebElement clickOnCSSElementIfExists(String cssSelector) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = chrome.clickOnCSSElementIfExists(cssSelector);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnXPathElementIfExists(String xpath) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = chrome.clickOnXPathElementIfExists(xpath);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnLinkTextElementIfExists(String linkText) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = chrome.clickOnLinkTextElementIfExists(linkText);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnIdElementIfExists(String id) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = chrome.clickOnIdElementIfExists(id);
		sleepAfterClicking();
		return we;
	}
	
	protected WebElement clickOnAnchorHrefElementIfExists(String href, boolean visibleOnly) throws InterruptedException {
		sleepBeforeClicking();
		WebElement we = chrome.clickOnAnchorHrefElementIfExists(href,visibleOnly);
		sleepAfterClicking();
		return we;
	}
	
	protected void sendKeysToAWebElement(WebElement we, String keyStrokes) throws InterruptedException {
		for(char keyStroke : keyStrokes.toCharArray()) {
			chrome.sendKeysToAWebElement(we,keyStroke+"");
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
		chrome.openTestDefaultWithSubroot(subroot);
		Assert.assertFalse(chrome.isWebPage404());
	}
	
	/*
	 * Assert for Href
	 */
	
	protected void AssertHrefExists(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to pass");
		Assert.assertTrue(chrome.AnchorExistsWithHREF(href, false));
	}
	
	protected void AssertHrefNotVisible(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to fail");
		Assert.assertFalse(chrome.AnchorExistsWithHREF(href, true));
	}
	
	protected void AssertHrefExistsAndIsVisible(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to pass");
		Assert.assertTrue(chrome.AnchorExistsWithHREF(href, true));
	}
	
	protected void AssertHrefExistsButIsHidden(String methodName, String href) {
		AssertHrefExists(methodName,href);
		AssertHrefNotVisible(methodName,href);
	}
	
	protected void AssertHrefDoesNotExist(String methodName, String href) {
		System.out.println(methodName+": expect next web element locator to fail");
		Assert.assertFalse(chrome.AnchorExistsWithHREF(href, false));
	}
	
	/*
	 * Assert dropdown clickable
	 */
	
	protected void AssertDropdownMenuClickableAndExists(String anchorId) {
		Assert.assertNotNull(chrome.clickOnCSSElementIfExists(anchorId));
	}
	
	protected void AssertHyperlinkExistsAndIsClickable(String href) {
		String query = chrome.AnchorQueryStringForHREF(href, true);
		Assert.assertNotNull(chrome.clickOnCSSElementIfExists(query));
	}
	
	protected void AssertHyperlinkExistsAndIsClickableAndDoesNotLeadTo404(String href) {
		AssertHyperlinkExistsAndIsClickable(href);
		Assert.assertFalse(chrome.isWebPage404());
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
