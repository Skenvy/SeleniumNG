# SeleniumNG : *TL;DR*
***The execution of Selenium scripts. Within a TestNG framework.*** If you've already spent time finessing your own *Selenium wrapping* classes, and building a framework of TestNG classes per **layers** *of abstraction*, that you are comfortable with as carbon copyable, this likely wont be your jam. But, if you're just starting out with a new project, or looking for an already set up basic collection of common functionalities, this **is** *your jam!*
# How to use
## Git y'all's local
Although not yet up to being made available as a maven jar, you can clone/download the repo locally, import as a project, assign the build paths for Selenium (tested against Selenium 3), and TestNG as a library/view (tested against TestNG 6.4).
## But wait; configuration!
Copy and paste a local instance of the configuration XML example that comes with the repo! Depending on whether you're using the **NiceWebDriverFactory** with or without the **baseTest**, you'll need to put in different information! To fill it out;
1. **WebDriverSystemPaths.\<Browser\>** fields should be filled in with the path to your local instances of the driver executables
2. **Local.\*** *and/or* **Test.\*** can be used to dictate the expected IP/hostname (**Test** only, not in **Local**), port, "WebContextRoot" (URL path after host:port, before query strings, if one is commonly used) and **WaitSeconds** for "waiting for element's responsiveness."
3. **TestSleeps.\*** are sleep durations of events that relate to the "demonstration declaration" outlined under the **baseTest** description below. 
4. ***SeleniumNodeCount*** is used to define the number of expected instacenes of {**SeleniumNodeLocal_\<k\>**, **SeleniumNodeRemoteURL_\<k\>**, **SeleniumNodeDriverType_\<k\>**}. If *Local* is set to true, there is no need to provide an URL, otherwise you'll need to provide the URL of a Selenium Grid/Node to run on as the *RemoteURL*. *DriverType* currently can only be *Chrome* until other browsers are extended in this.
## And Override the path to the configuration
As the configuration can be made as unique or globally as you want, you may want individual classes that subclass the **baseTest** to be capable of overriding the ***getPathToDomainConstantsConfig()*** to maintain configuration unique to individual classes, which may be beneficial, although the safest approach is to have a single intermediary abstract class that subclasses the **baseTest** and overrides the ***getPathToDomainConstantsConfig()*** (**and "final"'ises it**) similar to the below, then all local test classes should subclass your own intermediary base class;
```java
@Override //sub of baseTest
public final String getPathToDomainConstantsConfig() {
	return System.getProperty("user.dir")+"\\config_test.xml";
}
```
# The main classes
## NiceWebDriver
*A common framework, that offers simple functionality, wrapped to handle the usual suspects that prevent common Selenium WebDriver operations.* Must be created through the NiceWebDriverFactory. Contains self maintained instances of the WebDriver, JavascriptExecutor and WebDriverWait. While the underlying fields can be "unwrapped" to access or utilise them in a way not explicitly provided for in this implementation. The main operations of the NiceWebDriver are currently the basics of clicking, typing keys, and scrolling a page.
## NiceWebDriverFactory
A singleton factory that reads from an accompanying configuration XML (primarily to handle paths to the various browser's drivers, and wait time duration) to produce instances of NiceWebDriver, utilising the ***DriverType*** enum.
*So far, only the Chrome one has been included,* **and by default, every window starts maximised in incognito, to provide consistent expectations as to the operation of tests written against sites that involve logging in, or otherwise reauthorising the user**.
## baseTest
An abstract base class that defines the TestNG annotated common functionality, and **utilises class reinstantiation to handle utilising a common, shared, @Factory annotated method.** This implementation makes the consistent running of tests outside of using TestNG's XML suites similar to those as if they were run with minimal set up, as a single class run in a suite, if the class is run as a TestNG test, or by utilising the intelligent running within an IDE that allows running individually annotated tests, to allow consistency in running the test *method*, as the same as running it as an individual test *per the containing class* in a suite XML. The **baseTest** also provides a handful of functions to alter the behaviour of test running which can be invoked at the start of any test to debug the behaviors of tests running in isolation;
1. **declareThisTestAsCurrentlyBeingDemonstrated()** : Will intentionally slow down the operations of clicking and typing to attempt to mimic the slow speed of a human clicking or typing.
2. **declareThisTestAsCurrentlyBeingUnderDevelopment()** : Will leave the browser open when the test finishes instead of closing it, such that the state the browser was in when the test failed can be debugged, such as for the most common example, of locating or testing a failed WebElement locator.
