package com.skenvy.SeleniumNG.NiceWebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.skenvy.SeleniumNG.DomainConstants;
import com.skenvy.SeleniumNG.DomainConstants.localDefault;
import com.skenvy.SeleniumNG.DomainConstants.testDefault;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public abstract class NiceWebDriver {
	
	/***
	 * Instance of WebDriver, instantiated as a ChromeDriver
	 */
	private final WebDriver webDriver;
	
	/***
	 * Instance of the WebDriverWait, instantiated as a waiter on this class's WebDriver
	 */
	private final WebDriverWait wait;
	
	/***
	 * Instance of the JaascriptExecutor, instantiated as an executor on this class's WebDriver
	 */
	private final JavascriptExecutor jsExecutor;
	
	/***
	 * Stores the local host address.
	 */
	private final String localIP;
	
	//Constructors
	
	public NiceWebDriver(String optionArgs) throws UnknownHostException{
		MutableCapabilities mutableCapabilities = makeBrowserOptions(optionArgs);
		this.webDriver = getDriver(mutableCapabilities);
		this.wait = new WebDriverWait(this.webDriver,DomainConstants.localDefault.defaultWaitSeconds);
		this.jsExecutor = (JavascriptExecutor)this.webDriver;
		this.localIP = InetAddress.getLocalHost().getHostAddress();
	}
	
	public NiceWebDriver(int waitSeconds, String optionArgs) throws UnknownHostException{
		MutableCapabilities mutableCapabilities = makeBrowserOptions(optionArgs);
		this.webDriver = getDriver(mutableCapabilities);
		this.wait = new WebDriverWait(this.webDriver,waitSeconds);
		this.jsExecutor = (JavascriptExecutor)this.webDriver;
		this.localIP = InetAddress.getLocalHost().getHostAddress();
	}
	
	public NiceWebDriver(int waitSeconds, RemoteWebDriver rwd) throws UnknownHostException{
		this.webDriver = rwd;
		this.wait = new WebDriverWait(this.webDriver,waitSeconds);
		this.jsExecutor = (JavascriptExecutor)this.webDriver;
		this.localIP = InetAddress.getLocalHost().getHostAddress();
	}
	
	//Abstracts
	
	/***
	 * Returns a subclass of MutableCapabilities according to the subclass of this NiceWebDriver
	 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/MutableCapabilities.html
	 * @param optionArgs
	 * @return
	 */
	protected abstract MutableCapabilities makeBrowserOptions(String optionArgs);
	
	/***
	 * Returns a webdriver appropriate to the subclass of this NiceWebDriver
	 * @param mutableCapabilities
	 * @return
	 */
	protected abstract WebDriver getDriver(MutableCapabilities mutableCapabilities);
	
	//Niceties
	
	/***
	 * Close the web driver
	 */
	public void closeWebDriver() {
		this.webDriver.close();
	}
	
	/***
	 * Check whether the web driver is a remote instance or not.
	 * @return
	 */
	public boolean isRunningRemotely() {
		return (webDriver.getClass().getSimpleName().equals("RemoteWebDriver"));
	}
	
	//Open a page
	
	/***
	 * Open a webpage!
	 * @param url
	 */
	public void openWebPage(String url) {
		this.webDriver.get(url);
	}
	
	/***
	 * Open a web page using the IP, Port, WebContextRoot, and any sub-root query
	 * @param IP
	 * @param port
	 * @param contextRoot
	 * @param subrootQuery
	 */
	private void openPageOnIPPortContextRoot(String IP, int port, String contextRoot, String subrootQuery) {
		this.openWebPage("http://"+IP+":"+port+"/"+contextRoot+"/"+subrootQuery);
	}
	
	//Get Element
	
	/***
	 * Returns a WebElement by using "BY", if it exists, after waiting for it's presence
	 * @param cssSelectorQuery
	 * @return
	 */
	private WebElement getWebElementIfExists(By by, String selectorType) {
		String byString = "by "+selectorType+" String: "+by.toString();
		try {
			WebElement we = this.webDriver.findElement(by);
			System.out.println("Succeeded in locating WebElement (first attempt) "+byString);
			return we;
		} catch (NoSuchElementException e) {
			System.out.println("Failed to locate WebElement (first attempt) "+byString);
			try{
				this.wait.until(ExpectedConditions.presenceOfElementLocated(by));
				try {
					WebElement we = this.webDriver.findElement(by);
					System.out.println("Succeeded in locating WebElement (second attempt) "+byString);
					return we;
				} catch (NoSuchElementException e2) {
					System.out.println("Failed to locate WebElement (second attempt : after a successful wait_until!) "+byString);
					return null;
				}
			} catch (TimeoutException te) {
				System.out.println("Failed to locate WebElement (second attempt : failed the wait_until) "+byString);
				return null;
			}
		}
	}
	
	
	protected WebElement getWebElementByCSSIfExists(String cssSelector) {
		return getWebElementIfExists(By.cssSelector(cssSelector),"CSS Selector");
	}
	
	protected WebElement getWebElementByXPathIfExists(String xpath) {
		return getWebElementIfExists(By.xpath(xpath),"XPath");
	}
	
	protected WebElement getWebElementByLinkTextIfExists(String linkText) {
		return getWebElementIfExists(By.linkText(linkText),"Link Text");
	}
	
	protected WebElement getWebElementByIdIfExists(String id) {
		return getWebElementIfExists(By.id(id),"ID");
	}
	
	protected WebElement getWebElementByAnchorWithHrefIfExists(String href, boolean visibleOnly) {
		return getWebElementByCSSIfExists(AnchorQueryStringForHREF(href, visibleOnly));
	}
	
	//Click on a web element
	
	/*
	 * Click !
	 */
	
	protected WebElement clickANonNullWebElement(WebElement we){
		if(we != null) {
			we.click();
		}
		return we;
	}
	
	public WebElement clickOnCSSElementIfExists(String cssSelector) {
		return clickANonNullWebElement(getWebElementByCSSIfExists(cssSelector));
	}
	
	public WebElement clickOnXPathElementIfExists(String xpath) {
		return clickANonNullWebElement(getWebElementByXPathIfExists(xpath));
	}
	
	public WebElement clickOnLinkTextElementIfExists(String linkText) {
		return clickANonNullWebElement(getWebElementByLinkTextIfExists(linkText));
	}
	
	public WebElement clickOnIdElementIfExists(String id) {
		return clickANonNullWebElement(getWebElementByIdIfExists(id));
	}
	
	public WebElement clickOnAnchorHrefElementIfExists(String href, boolean visibleOnly) {
		return clickANonNullWebElement(getWebElementByAnchorWithHrefIfExists(href,visibleOnly));
	}
	
	//Send keys!
	
	public void sendKeysToAWebElement(WebElement we, String keyStrokes) {
		we.sendKeys(keyStrokes);
	}
	
	//Continue to format
	
	/*
	 * Check for the presence of an <a [href ...] ...>
	 */
	
	public String AnchorQueryStringForHREF(String href, boolean visibleOnly) {
		String query = "a[href*=\""+href+"\"]";
		if(visibleOnly) {
			query = ".show > "+query;
		}
		return query;
	}
	
	public boolean AnchorExistsWithHREF(String href, boolean visibleOnly) {
		if(visibleOnly) {
			System.out.println("Confirming that there exists the VISIBLE ONLY href: "+href);
		} else {
			System.out.println("Confirming that there exists the VISIBLE OR INVISIBLE href: "+href);
		}
		return (getWebElementByAnchorWithHrefIfExists(href,visibleOnly) != null);
	}
	
	public boolean confirmCurrentPageIs(String expectedSubroot) {
		try {
			String subroot = (new URI(this.webDriver.getCurrentUrl())).getPath();
			System.out.println(subroot+" || "+expectedSubroot);
			return (subroot.equals(expectedSubroot));
		} catch (URISyntaxException e) {
			System.err.println("Failed to obtain the default authority of the current webdriver's open URL");
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * Open specified webContextRoot
	 */
	
	public void openLocalWebPageWithSubroot(String webContextRoot, String subrootQuery) {
		this.openPageOnIPPortContextRoot(this.localIP, DomainConstants.localDefault.environPort, webContextRoot,subrootQuery);
	}
	
	public void openLocalWebPageAtBase(String webContextRoot) {
		this.openLocalWebPageWithSubroot(webContextRoot,"");
	}
	
	/*
	 * Defaulted webContextRoot
	 */
	
	public void openLocalDefaultWebContextRootWithSubroot(String subrootQuery) {
		this.openLocalWebPageWithSubroot(DomainConstants.localDefault.webContextRoot,subrootQuery);
	}
	
	public void openLocalDefaultWebContextRootAtBase() {
		this.openLocalDefaultWebContextRootWithSubroot("");
	}
	
	/*
	 * Test generics
	 */
	
	public void openTestDefaultWithSubroot(String subrootQuery) {
		this.openPageOnIPPortContextRoot(DomainConstants.testDefault.environIP, DomainConstants.testDefault.environPort, DomainConstants.testDefault.webContextRoot, subrootQuery);
	}
	
	public void openTestDefaultAtBase() {
		this.openTestDefaultWithSubroot("");
	}
	
	public boolean isWebPage404() {
		return this.webDriver.getPageSource().contains("HTTP Status 404");
	}

}
