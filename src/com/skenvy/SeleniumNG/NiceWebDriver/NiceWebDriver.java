package com.skenvy.SeleniumNG.NiceWebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.skenvy.SeleniumNG.DomainConstants;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

/***
 * An abstract class to encapsulate the notions of a Selenium WebDriver coupled
 * with an innate WebDriverWait used to auto-wrap "wait" behaviour on failed
 * web page interactions, and an innate JavascriptExecutor to wrap page
 * interactivity expected to be executed against the page of the WebDriver.
 */
public abstract class NiceWebDriver {
	
///////////////////////////////////////////////////////////////////////////////
/*
 * WebDriver, Wait, and JSExe
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Instance of WebDriver, instantiated as a ChromeDriver
	 */
	private final WebDriver webDriver;
	
	/***
	 * Instance of the WebDriverWait, instantiated as a waiter on 
	 * this class's WebDriver
	 */
	private final WebDriverWait wait;
	
	/***
	 * Instance of the JavascriptExecutor, instantiated as an executor 
	 * on this class's WebDriver
	 */
	private final JavascriptExecutor jsExecutor;
	
	/***
	 * Record the local IP addess of the machine on which this is executed
	 */
	private String localIP = null;
	
	/***
	 * Determines whether or not verbose messages will be printed
	 */
	private boolean verboseMode = false;
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Constructors
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * An argumentless instance for the purposes of calling the 
	 * UnderloadedNiceChromeDriverConstructor, which cannot be static to allow
	 * it to rely on the output values of abstracted methods, which themselves
	 * cannot be static! Contains only null properties
	 */
	protected NiceWebDriver() {
		this.webDriver = null;
		this.wait = null;
		this.jsExecutor = null;
	}
	
	/***
	 * Constructor which takes a boolean to differentiate the two 
	 * "parameterless" constructors, one for a local instance, and 
	 * one for a remote instance
	 * @param localInstance
	 */
	protected NiceWebDriver(boolean localInstance){
		if(localInstance) {
			this.webDriver = getDriver();
		} else {
			this.webDriver = new RemoteWebDriver(getRemoteCapability());
		}
		this.wait = getWaiter(DomainConstants.local.waitSeconds);
		this.jsExecutor = getJSExecutor();
	}
	
	/***
	 * Constructor which takes a boolean to differentiate the two 
	 * "parameterless" constructors, one for a local instance, 
	 * and one for a remote instance
	 * @param localInstance
	 */
	protected NiceWebDriver(boolean localInstance, int waitSeconds){
		if(localInstance) {
			this.webDriver = getDriver();
		} else {
			this.webDriver = new RemoteWebDriver(getRemoteCapability());
		}
		this.wait = getWaiter(waitSeconds);
		this.jsExecutor = getJSExecutor();
	}
	
	/***
	 * Constructor which only takes the option arguments to be 
	 * handed to the "Browser"-Options
	 * @param optionArgs
	 */
	protected NiceWebDriver(String optionArgs){
		MutableCapabilities mutableCapabilities = makeBrowserOptions(optionArgs);
		this.webDriver = getDriver(mutableCapabilities);
		this.wait = getWaiter(DomainConstants.local.waitSeconds);
		this.jsExecutor = getJSExecutor();
	}
	
	/***
	 * Constructor which only takes an URL to make a remote webdriver
	 * @param remoteAddress
	 */
	protected NiceWebDriver(URL remoteAddress){
		this.webDriver = new RemoteWebDriver(remoteAddress,getRemoteCapability());
		this.wait = getWaiter(DomainConstants.local.waitSeconds);
		this.jsExecutor = getJSExecutor();
	}
	
	/***
	 * Constructor which takes the option arguments 
	 * to be handed to the "Browser"-Options,
	 * as well as a defined wait time in seconds
	 * @param optionArgs
	 * @param waitSeconds
	 */
	protected NiceWebDriver(String optionArgs, int waitSeconds){
		MutableCapabilities mutableCapabilities = makeBrowserOptions(optionArgs);
		this.webDriver = getDriver(mutableCapabilities);
		this.wait = getWaiter(waitSeconds);
		this.jsExecutor = getJSExecutor();
	}
	
	/***
	 * Constructor which only takes an URL to make a remote webdriver,
	 * as well as a defined wait time in seconds
	 * @param remoteAddress
	 * @param waitSeconds
	 */
	protected NiceWebDriver(URL remoteAddress, int waitSeconds){
		this.webDriver = new RemoteWebDriver(remoteAddress,getRemoteCapability());
		this.wait = getWaiter(waitSeconds);
		this.jsExecutor = getJSExecutor();
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Abstracts
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Returns a subclass of MutableCapabilities according to the subclass of this NiceWebDriver
	 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/MutableCapabilities.html
	 * @param optionArgs
	 * @return
	 */
	protected abstract MutableCapabilities makeBrowserOptions(String optionArgs);
	
	/***
	 * Returns a local webdriver appropriate to the subclass of this NiceWebDriver
	 * @param mutableCapabilities
	 * @return
	 */
	protected abstract WebDriver getDriver(MutableCapabilities mutableCapabilities);
	
	/***
	 * Returns a local webdriver appropriate to the subclass of this NiceWebDriver
	 * @param mutableCapabilities
	 * @return
	 */
	protected abstract WebDriver getDriver();
	
	/***
	 * Returns the capability request for a remote webdriver appropriate to the subclass of this NiceWebDriver
	 * @param mutableCapabilities
	 * @return
	 */
	protected abstract Capabilities getRemoteCapability();
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Abstract wrappers on the constructors
 * to return the return of the constructors 
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Return an instance of invoke the subclass constructor 
	 * that matches this method's arguments
	 * @param localInstance
	 * @return
	 */
	protected abstract NiceWebDriver InvokeContsructorWithArguments(boolean localInstance);

	/***
	 * Return an instance of invoke the subclass constructor 
	 * that matches this method's arguments
	 * @param localInstance
	 * @param waitSeconds
	 * @return
	 */
	protected abstract NiceWebDriver InvokeContsructorWithArguments(boolean localInstance, int waitSeconds);

	/***
	 * Return an instance of invoke the subclass constructor 
	 * that matches this method's arguments
	 * @param optionArgs
	 * @return
	 */
	protected abstract NiceWebDriver InvokeContsructorWithArguments(String optionArgs);

	/***
	 * Return an instance of invoke the subclass constructor 
	 * that matches this method's arguments
	 * @param optionArgs
	 * @param waitSeconds
	 * @return
	 */
	protected abstract NiceWebDriver InvokeContsructorWithArguments(String optionArgs, int waitSeconds);

	/***
	 * Return an instance of invoke the subclass constructor 
	 * that matches this method's arguments
	 * @param remoteAddress
	 * @return
	 */
	protected abstract NiceWebDriver InvokeContsructorWithArguments(URL remoteAddress);

	/***
	 * Return an instance of invoke the subclass constructor 
	 * that matches this method's arguments
	 * @param remoteAddress
	 * @param waitSeconds
	 * @return
	 */
	protected abstract NiceWebDriver InvokeContsructorWithArguments(URL remoteAddress, int waitSeconds);
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Underloaded Constructor to decouple the switches in the factory
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Takes an object array and parses them into suitable 
	 * arrangements for the different constructors
	 * @param oArgs
	 * @return
	 */
	protected NiceWebDriver UnderloadedNiceWebDriverConstructor(Object[] oArgs) {
		//Wrap the top length at 2 and 1
		if(oArgs.length < 3 && oArgs.length > 0){
			//The first argument can be of three types
			if(oArgs[0].getClass().getName().equals("java.lang.Boolean")) {
				if(oArgs.length == 2) {
					if(oArgs[1].getClass().getName().equals("java.lang.Integer")) {
						return InvokeContsructorWithArguments((boolean) oArgs[0], (int) oArgs[1]);
					}
				} else {
					return InvokeContsructorWithArguments((boolean) oArgs[0]);
				}
			} else if(oArgs[0].getClass().getName().equals("java.lang.String")) {
				if(oArgs.length == 2) {
					if(oArgs[1].getClass().getName().equals("java.lang.Integer")) {
						return InvokeContsructorWithArguments((String) oArgs[0], (int) oArgs[1]);
					}
				} else {
					return InvokeContsructorWithArguments((String) oArgs[0]);
				}
			} else if(oArgs[0].getClass().getName().equals("java.net.URL")) {
				if(oArgs.length == 2) {
					if(oArgs[1].getClass().getName().equals("java.lang.Integer")) {
						return InvokeContsructorWithArguments((URL) oArgs[0], (int) oArgs[1]);
					}
				} else {
					return InvokeContsructorWithArguments((URL) oArgs[0]);
				}
			}
		}
		return null;
	}
	
	/***
	 * Gets the current instance of the NiceWebDriver after adjusting the
	 * verbostiy.
	 * @param verbosity
	 * @return
	 */
	public NiceWebDriver getThisWithVerbositySetTo(boolean verboseMode) {
		this.verboseMode = verboseMode;
		return this;
	}
	
	/***
	 * Writes a verbose message to sysout
	 * @param message
	 */
	private void writeVerboseMessageToSysOut(String message) {
		if(this.verboseMode) {
			System.out.println(message);
		}
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Unwrappers : Getters for the private final fields if the methods here
 * do not fully encapsulate the behavour necessary of the WebDriver
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Get the underlying WebDriver
	 * @return
	 */
	public WebDriver unwrapWebDriver() {
		return webDriver;
	}

	/***
	 * Get the underlying WebDriverWait
	 * @return
	 */
	public WebDriverWait unwrapWebDriverWait() {
		return wait;
	}

	/***
	 * Get the underlying JavascriptExecutor
	 * @return
	 */
	public JavascriptExecutor unwrapJavascriptExecutor() {
		return jsExecutor;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Niceties, necessities and simple encapsulations that don't relate to page
 * interactions or abstract construction, although does include argumentless
 * driver interaction and "constructor helpers"
 */
///////////////////////////////////////////////////////////////////////////////

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
		return (webDriver instanceof RemoteWebDriver);
	}
	
	/*
	 * Constructor helpers
	 */
	
	/***
	 * Simplify the constructor call to instantiate the WebDriverWait
	 * @param seconds
	 * @return
	 */
	private WebDriverWait getWaiter(int seconds) {
		return new WebDriverWait(this.webDriver,seconds);
	}
	
	/***
	 * Simplify the constructor call to instantiate the JavascriptExecutor
	 * @return
	 */
	private JavascriptExecutor getJSExecutor() {
		return (JavascriptExecutor)this.webDriver;
	}
	
	/*
	 * Check for the presence of an <a [href ...] ...>
	 */
	
	/***
	 * Used by the action to "get a web element" if one can be found with 
	 * a CSS selector that finds an "a" node with an href
	 * @param href
	 * @param visibleOnly
	 * @return
	 */
	public String AnchorQueryStringForHREF(String href, boolean visibleOnly) {
		String query = "a[href*=\""+href+"\"]";
		if(visibleOnly) {
			query = ".show > "+query;
		}
		return query;
	}
	
	/***
	 * Confirm the existence of a visible or invisible "a" with href
	 * on the current page
	 * @param href
	 * @param visibleOnly
	 * @return
	 */
	public boolean AnchorExistsWithHREF(String href, boolean visibleOnly) {
		if(visibleOnly) {
			System.out.println("Confirming that there exists the VISIBLE ONLY href: "+href);
		} else {
			System.out.println("Confirming that there exists the VISIBLE OR INVISIBLE href: "+href);
		}
		return (getWebElementByAnchorWithHrefIfExists(href,visibleOnly) != null);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Check the current page open in the WebDriver and handle error codes
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Get the current URL of the open web page as a string
	 * @return
	 */
	public String getCurrentUrlAsString() {
		return this.webDriver.getCurrentUrl();
	}

	/***
	 * Get the current URL of the open web page as a URI
	 * @return
	 */
	public URI getCurrentUrlAsURI() {
		try {
			return new URI(getCurrentUrlAsString());
		} catch (URISyntaxException e) {
			System.err.println("Failed to obtain the default authority of the current webdriver's open URL");
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * Confirm that the current web page's subroot matches an expected pattern
	 * @param expectedSubroot
	 * @return
	 */
	public boolean confirmCurrentPageIs(String expectedSubroot) {
		String subroot = getCurrentUrlAsURI().getPath();
		writeVerboseMessageToSysOut(subroot+" || "+expectedSubroot);
		return (subroot.equals(expectedSubroot));
	}

	/***
	 * Checks the page for HTTP 404 Status message
	 * TODO Is this relevant to all pages?
	 * TODO Maybe find a more appropriate way of checking for a range of errors
	 * @return
	 */
	public boolean isWebPage404() {
		return this.webDriver.getPageSource().contains("HTTP Status 404");
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * The generic .get(...) and URL string casts, and the
 * call used by all later "customised" equivalents
 * to the ".get(...)"
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Open a web page! Offers the most generic call to the WebDriver's
	 * .get(String) method, with the least cleaned customisation
	 * @param url
	 */
	public void openWebPage(String url) {
		this.webDriver.get(url);
	}
	
	/***
	 * Casts an URL Authority
	 * @param protocol
	 * @param userinfo
	 * @param host
	 * @param port
	 * @return
	 */
	private String castToUrlAuthority(String protocol, String userinfo, String host, int port) {
		String userTag = "";
		if(userinfo != null && !userinfo.equals("")) {
			userTag = userinfo+"@";
		}
		String portTag = "";
		if(port >= 0) {
			portTag = ":"+port;
		}
		return (protocol+"://"+userTag+host+portTag+"/");
	}
	
	/***
	 * Casts an Url string
	 * @param protocol
	 * @param userinfo
	 * @param host
	 * @param port
	 * @param contextRoot
	 * @param subrootQuery
	 * @return
	 */
	private String castToUrl(String protocol, String userinfo, String host, int port, String contextRoot, String subrootQuery) {
		if(contextRoot == null || contextRoot == "") {
			if(subrootQuery == null || subrootQuery == "") {
				return castToUrlAuthority(protocol,userinfo,host,port);
			} else {
				return (castToUrlAuthority(protocol,userinfo,host,port)+subrootQuery);
			}
		} else {
			if(subrootQuery == null || subrootQuery == "") {
				return (castToUrlAuthority(protocol,userinfo,host,port)+contextRoot);
			} else {
				return (castToUrlAuthority(protocol,userinfo,host,port)+contextRoot+"/"+subrootQuery);
			}
		}
	}

	/***
	 * Open an url by specific protocol, user, host, port, context root and 
	 * subroot. The polar opposite to the openWebPage method, this being
	 * the most customised in terms of input to specific fields of the URL
	 * @param protocol
	 * @param userinfo
	 * @param host
	 * @param port
	 * @param contextRoot
	 * @param subrootQuery
	 */
	public void openPageOnAuthHostPortContextRoot(String protocol, String userinfo, String host, int port, String contextRoot, String subrootQuery) {
		this.openWebPage(castToUrl(protocol,userinfo,host,port,contextRoot,subrootQuery));
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
* Opening a web page - Generic Web Pages (Non-local)
* 
*/
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Open a web page using http, the host and context root
	 * @param host
	 * @param contextRoot
	 * @param subrootQuery
	 */
	public void openHTTPOnHostContextRoot(String host, String contextRoot) {
		this.openHTTPOnHostContextRoot(host,contextRoot,null);
	}
	
	/***
	 * Open a web page using http, the host, 
	 * context root, and any sub-root query
	 * @param host
	 * @param contextRoot
	 * @param subrootQuery
	 */
	public void openHTTPOnHostContextRoot(String host, String contextRoot, String subrootQuery) {
		this.openHTTPOnHostPortContextRoot(host,-1,contextRoot,subrootQuery);
	}
	
	/***
	 * Open a web page using http, the host, 
	 * the port (non 80), context root, 
	 * and any sub-root query
	 * @param host
	 * @param port
	 * @param contextRoot
	 * @param subrootQuery
	 */
	public void openHTTPOnHostPortContextRoot(String host, int port, String contextRoot, String subrootQuery) {
		this.openPageOnAuthHostPortContextRoot(DomainConstants.UrlConstants.HTTP,null,host,port,contextRoot,subrootQuery);
	}
	
	/***
	 * Open a web page using http, the host and context root
	 * @param host
	 * @param contextRoot
	 * @param subrootQuery
	 */
	public void openHTTPSOnHostContextRoot(String host, String contextRoot) {
		this.openHTTPSOnHostContextRoot(host,contextRoot,null);
	}
	
	/***
	 * Open a web page using http, the host, 
	 * context root, and any sub-root query
	 * @param host
	 * @param contextRoot
	 * @param subrootQuery
	 */
	public void openHTTPSOnHostContextRoot(String host, String contextRoot, String subrootQuery) {
		this.openHTTPSOnHostPortContextRoot(host,-1,contextRoot,subrootQuery);
	}
	
	/***
	 * Open a web page using http, the host, 
	 * the port (non 443),  context root,
	 * and any sub-root query
	 * @param host
	 * @param port
	 * @param contextRoot
	 * @param subrootQuery
	 */
	public void openHTTPSOnHostPortContextRoot(String host, int port, String contextRoot, String subrootQuery) {
		this.openPageOnAuthHostPortContextRoot(DomainConstants.UrlConstants.HTTPS,null,host,port,contextRoot,subrootQuery);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Opening a web page - Any localhost page
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Will either initialise or return the initialised local IP
	 * @return
	 * @throws UnknownHostException
	 */
	public String getLocalIP() throws UnknownHostException {
		if(this.localIP == null) {
			this.localIP = InetAddress.getLocalHost().getHostAddress();
		}
		return this.localIP;
	}
	
	/***
	 * Open a web resource served from the local machine over HTTP on a default
	 * "environment port" with context and subroot query
	 * @param contextRoot
	 * @param subrootQuery
	 * @throws UnknownHostException
	 */
	public void openLocalHTTPWebPageWithSubroot(String contextRoot, String subrootQuery) throws UnknownHostException {
		this.openHTTPOnHostPortContextRoot(this.getLocalIP(),DomainConstants.local.environPort,contextRoot,subrootQuery);
	}
	
	/***
	 * Open a web resource served from the local machine over HTTP on a default
	 * "environment port" with context
	 * @param contextRoot
	 * @throws UnknownHostException
	 */
	public void openLocalWebPageAtBase(String contextRoot) throws UnknownHostException {
		this.openLocalHTTPWebPageWithSubroot(contextRoot,null);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Opening a web page - Configured Local Page
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Open a web resource served from the local machine over HTTP on a default
	 * "environment port" and a default "web context root" with a subroot query
	 * @param subrootQuery
	 * @throws UnknownHostException
	 */
	public void openLocalHTTPDefaultWebContextRootWithSubroot(String subrootQuery) throws UnknownHostException {
		this.openLocalHTTPWebPageWithSubroot(DomainConstants.local.webContextRoot,subrootQuery);
	}
	
	/***
	 * Open a web resource served from the local machine over HTTP on a default
	 * "environment port" and a default "web context root"
	 * @throws UnknownHostException
	 */
	public void openLocalHTTPDefaultWebContextRootAtBase() throws UnknownHostException {
		this.openLocalHTTPDefaultWebContextRootWithSubroot(null);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Opening a web page - Configured Test Page
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Opens a web resource defined by the "test default" parameters which 
	 * specify an environ IP/Host, port, and WebContextRoot, 
	 * with a subroot query
	 * @param subrootQuery
	 */
	public void openTestDefaultWithHTTPAndSubroot(String subrootQuery) {
		this.openHTTPOnHostPortContextRoot(DomainConstants.test.environIP,DomainConstants.test.environPort,DomainConstants.test.webContextRoot,subrootQuery);
	}
	
	/***
	 * Opens a web resource defined by the "test default" parameters which 
	 * specify an environ IP/Host, port, and WebContextRoot, 
	 * with a subroot query
	 * @param subrootQuery
	 */
	public void openTestDefaultWithHTTPSAndSubroot(String subrootQuery) {
		this.openHTTPSOnHostPortContextRoot(DomainConstants.test.environIP,DomainConstants.test.environPort,DomainConstants.test.webContextRoot,subrootQuery);
	}
	
	/***
	 * Opens a web resource defined by the "test default" parameters which 
	 * specify an environ IP/Host, port, and WebContextRoot
	 */
	public void openTestDefaultWithHTTPAtBase() {
		this.openTestDefaultWithHTTPAndSubroot(null);
	}
	
	/***
	 * Opens a web resource defined by the "test default" parameters which 
	 * specify an environ IP/Host, port, and WebContextRoot
	 */
	public void openTestDefaultWithHTTPSAtBase() {
		this.openTestDefaultWithHTTPSAndSubroot(null);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Get Element | The good stuff
 */
///////////////////////////////////////////////////////////////////////////////
	
	//<When clicking>: org.openqa.selenium.ElementNotVisibleException: element not interactable
	//<When typing>:   org.openqa.selenium.ElementNotInteractableException: element not interactable
	
	/***
	 * Returns a WebElement by using "BY", if it exists, 
	 * after waiting for its presence
	 * @param cssSelectorQuery
	 * @return
	 */
	private WebElement getWebElementIfExists(By by, String selectorType) {
		String byString = "by "+selectorType+" String: "+by.toString();
		try {
			WebElement we = this.webDriver.findElement(by);
			writeVerboseMessageToSysOut("Succeeded in locating WebElement (first attempt) "+byString);
			return we;
		} catch (NoSuchElementException e) {
			writeVerboseMessageToSysOut("Failed to locate WebElement (first attempt) "+byString);
			try{
				this.wait.until(ExpectedConditions.presenceOfElementLocated(by));
				try {
					WebElement we = this.webDriver.findElement(by);
					writeVerboseMessageToSysOut("Succeeded in locating WebElement (second attempt) "+byString);
					return we;
				} catch (NoSuchElementException e2) {
					writeVerboseMessageToSysOut("Failed to locate WebElement (second attempt : after a successful wait_until!) "+byString);
					return null;
				}
			} catch (TimeoutException te) {
				System.err.println("Failed to locate WebElement (second attempt : failed the wait_until) "+byString);
				return null;
			}
		}
	}
	
	/***
	 * Returns the WebElement parsed if it is viewable (for .click()).
	 * @param we
	 * @return
	 */
	private WebElement handleElementNotVisibleException(WebElement we) {
		//<When clicking>: org.openqa.selenium.ElementNotVisibleException: element not interactable
		return this.wait.until(ExpectedConditions.visibilityOf(we));
	}
	
	/***
	 * Returns the WebElement parsed if it is clickable (for .sendKeys()).
	 * @param we
	 * @return
	 */
	private WebElement handleElementNotInteractableException(WebElement we) {
		//<When typing>: org.openqa.selenium.ElementNotInteractableException: element not interactable
		return this.wait.until(ExpectedConditions.elementToBeClickable(we));
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Get Element | By!
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Gets a WebElement using the CSS Selector
	 * @param cssSelector
	 * @return
	 */
	public WebElement getWebElementByCSSIfExists(String cssSelector) {
		return getWebElementIfExists(By.cssSelector(cssSelector),"CSS Selector");
	}

	/***
	 * Gets a WebElement using the XPath
	 * @param xpath
	 * @return
	 */
	public WebElement getWebElementByXPathIfExists(String xpath) {
		return getWebElementIfExists(By.xpath(xpath),"XPath");
	}

	/***
	 * Gets a WebElement using the Link Text
	 * @param linkText
	 * @return
	 */
	public WebElement getWebElementByLinkTextIfExists(String linkText) {
		return getWebElementIfExists(By.linkText(linkText),"Link Text");
	}

	/***
	 * Gets a WebElement using the ID
	 * @param id
	 * @return
	 */
	public WebElement getWebElementByIdIfExists(String id) {
		return getWebElementIfExists(By.id(id),"ID");
	}

	/***
	 * Gets a WebElement using the href
	 * @param href
	 * @param visibleOnly
	 * @return
	 */
	public WebElement getWebElementByAnchorWithHrefIfExists(String href, boolean visibleOnly) {
		return getWebElementByCSSIfExists(AnchorQueryStringForHREF(href, visibleOnly));
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Click on a web element
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Clicks on a WebElement if it is not null
	 * @param we
	 * @return
	 */
	private WebElement clickANonNullWebElement(WebElement we){
		if(we != null) {
			try {
				we.click();
			} catch(ElementNotVisibleException e1) {
				//Make sure we wait until it is visible!
				try {
					handleElementNotVisibleException(we).click();
				} catch (TimeoutException e2) {
					/* The page might still be dynamically loading despite the
					 * element already being "viewable." The only option now
					 * is a "Thread.sleep(); Eww. Call the sleep, then try the
					 * handler again, with no try catch protection, anything
					 * after this is a genuine error. Was the page Angular7? */
					try {
						Thread.sleep(DomainConstants.local.waitSeconds*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handleElementNotVisibleException(we).click();
				}
			} catch (WebDriverException e2) {
				/* The page might still be dynamically loading despite the
				 * element already being "present." The only option now
				 * is a "Thread.sleep(); Eww. Call the sleep, then try the
				 * handler again, with no try catch protection, anything
				 * after this is a genuine error. Was the page Angular7? */
				try {
					Thread.sleep(DomainConstants.local.waitSeconds*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handleElementNotVisibleException(we).click();
			}
		}
		return we;
	}
	
	/***
	 * Clicks on a WebElement found using a CSS Selector
	 * @param cssSelector
	 * @return
	 */
	public WebElement clickOnCSSElementIfExists(String cssSelector) {
		return clickANonNullWebElement(getWebElementByCSSIfExists(cssSelector));
	}

	/***
	 * Clicks on a WebElement found using an XPath
	 * @param xpath
	 * @return
	 */
	public WebElement clickOnXPathElementIfExists(String xpath) {
		return clickANonNullWebElement(getWebElementByXPathIfExists(xpath));
	}

	/***
	 * Clicks on a WebElement found using a link text
	 * @param linkText
	 * @return
	 */
	public WebElement clickOnLinkTextElementIfExists(String linkText) {
		return clickANonNullWebElement(getWebElementByLinkTextIfExists(linkText));
	}

	/***
	 * Clicks on a WebElement found using an ID
	 * @param id
	 * @return
	 */
	public WebElement clickOnIdElementIfExists(String id) {
		return clickANonNullWebElement(getWebElementByIdIfExists(id));
	}

	/***
	 * Clicks on a WebElement found using an href
	 * @param href
	 * @param visibleOnly
	 * @return
	 */
	public WebElement clickOnAnchorHrefElementIfExists(String href, boolean visibleOnly) {
		return clickANonNullWebElement(getWebElementByAnchorWithHrefIfExists(href,visibleOnly));
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Send keys : CharSequence... [deduplicate "String" and "Keys[]"]
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Sends keys to a WebElement if it is not null
	 * @param we
	 * @return
	 */
	public WebElement sendKeysToANonNullWebElement(WebElement we, CharSequence...  keyStrokes) {
		if(we != null) {
			try {
				we.sendKeys(keyStrokes);
			} catch(ElementNotInteractableException e1) {
				try {
					handleElementNotInteractableException(we).sendKeys(keyStrokes);
				} catch (TimeoutException e2) {
					/* The page might still be dynamically loading despite the
					 * element already being "clickable." The only option now
					 * is a "Thread.sleep(); Eww. Call the sleep, then try the
					 * handler again, with no try catch protection, anything
					 * after this is a genuine error. Was the page Angular7? */
					try {
						Thread.sleep(DomainConstants.local.waitSeconds*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handleElementNotInteractableException(we).sendKeys(keyStrokes);
				}
			} catch (WebDriverException e2) {
				/* The page might still be dynamically loading despite the
				 * element already being "present." The only option now
				 * is a "Thread.sleep(); Eww. Call the sleep, then try the
				 * handler again, with no try catch protection, anything
				 * after this is a genuine error. Was the page Angular7? */
				try {
					Thread.sleep(DomainConstants.local.waitSeconds*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handleElementNotInteractableException(we).sendKeys(keyStrokes);
			}
		}
		return we;
	}
	
	/***
	 * Sends keys to a WebElement found using a CSS Selector
	 * @param cssSelector
	 * @return
	 */
	public WebElement sendKeysToCSSElementIfExists(String cssSelector, CharSequence... keyStrokes) {
		return sendKeysToANonNullWebElement(getWebElementByCSSIfExists(cssSelector),keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an XPath
	 * @param xpath
	 * @return
	 */
	public WebElement sendKeysToXPathElementIfExists(String xpath, CharSequence... keyStrokes) {
		return sendKeysToANonNullWebElement(getWebElementByXPathIfExists(xpath),keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using a link text
	 * @param linkText
	 * @return
	 */
	public WebElement sendKeysToLinkTextElementIfExists(String linkText, CharSequence... keyStrokes) {
		return sendKeysToANonNullWebElement(getWebElementByLinkTextIfExists(linkText),keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an ID
	 * @param id
	 * @return
	 */
	public WebElement sendKeysToIdElementIfExists(String id, CharSequence... keyStrokes) {
		return sendKeysToANonNullWebElement(getWebElementByIdIfExists(id),keyStrokes);
	}

	/***
	 * Sends keys to a WebElement found using an href
	 * @param href
	 * @param visibleOnly
	 * @return
	 */
	public WebElement sendKeysToAnchorHrefElementIfExists(String href, boolean visibleOnly, CharSequence... keyStrokes) {
		return sendKeysToANonNullWebElement(getWebElementByAnchorWithHrefIfExists(href,visibleOnly),keyStrokes);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Scroll the page into view of a WebElement
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Scroll the page into view of a WebElement
	 * @param we
	 * @return
	 */
	public WebElement scrollThePageIntoViewOfAWebElement(WebElement we) {
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", we);
		return we;
	}
	
	/***
	 * Scroll the page into view of a WebElement if it is not null
	 * @param we
	 * @return
	 */
	public WebElement scrollThePageIntoViewOfANonNullWebElement(WebElement we) {
		if(we != null) {
			try {
				scrollThePageIntoViewOfAWebElement(we);
			} catch(ElementNotVisibleException e1) {
				try {
					scrollThePageIntoViewOfAWebElement(handleElementNotVisibleException(we));
				} catch (TimeoutException e2) {
					/* The page might still be dynamically loading despite the
					 * element already being "clickable." The only option now
					 * is a "Thread.sleep(); Eww. Call the sleep, then try the
					 * handler again, with no try catch protection, anything
					 * after this is a genuine error. Was the page Angular7? */
					try {
						Thread.sleep(DomainConstants.local.waitSeconds*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					scrollThePageIntoViewOfAWebElement(handleElementNotVisibleException(we));
				}
			} catch (WebDriverException e2) {
				/* The page might still be dynamically loading despite the
				 * element already being "present." The only option now
				 * is a "Thread.sleep(); Eww. Call the sleep, then try the
				 * handler again, with no try catch protection, anything
				 * after this is a genuine error. Was the page Angular7? */
				try {
					Thread.sleep(DomainConstants.local.waitSeconds*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				scrollThePageIntoViewOfAWebElement(handleElementNotVisibleException(we));
			}
		}
		return we;
	}
	
	/***
	 * Scroll the page into view of a WebElement found using a CSS Selector
	 * @param cssSelector
	 * @return
	 */
	public WebElement scrollThePageIntoViewOfACSSElementIfExists(String cssSelector) {
		return scrollThePageIntoViewOfANonNullWebElement(getWebElementByCSSIfExists(cssSelector));
	}

	/***
	 * Scroll the page into view of a WebElement found using an XPath
	 * @param xpath
	 * @return
	 */
	public WebElement scrollThePageIntoViewOfAnXPathElementIfExists(String xpath) {
		return scrollThePageIntoViewOfANonNullWebElement(getWebElementByXPathIfExists(xpath));
	}

	/***
	 * Scroll the page into view of a WebElement found using a link text
	 * @param linkText
	 * @return
	 */
	public WebElement scrollThePageIntoViewOfALinkTextElementIfExists(String linkText) {
		return scrollThePageIntoViewOfANonNullWebElement(getWebElementByLinkTextIfExists(linkText));
	}

	/***
	 * Scroll the page into view of a WebElement found using an ID
	 * @param id
	 * @return
	 */
	public WebElement scrollThePageIntoViewOfAnIdElementIfExists(String id) {
		return scrollThePageIntoViewOfANonNullWebElement(getWebElementByIdIfExists(id));
	}

	/***
	 * Scroll the page into view of a WebElement found using an href
	 * @param href
	 * @param visibleOnly
	 * @return
	 */
	public WebElement scrollThePageIntoViewOfAnAnchorHrefElementIfExists(String href, boolean visibleOnly) {
		return scrollThePageIntoViewOfANonNullWebElement(getWebElementByAnchorWithHrefIfExists(href, visibleOnly));
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Minutiae/Niche functionality
 */
///////////////////////////////////////////////////////////////////////////////
	
	public WebElement setValueOnInstanceOfAClass(String className, int nthInstance, String valueToSet) {
		WebElement classInstance = webDriver.findElements(By.cssSelector("."+className)).get(nthInstance);
		jsExecutor.executeScript("arguments[0]."+className+".setValue(\"" + valueToSet + "\");", classInstance);
		return classInstance;
	}
}
