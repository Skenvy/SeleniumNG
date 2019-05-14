package com.skenvy.SeleniumNG.NiceWebDriver;

/***
 * An enum to define the driver types as per
 * https://www.seleniumhq.org/docs/03_webdriver.jsp#selenium-webdriver-s-drivers
 */
public enum DriverType {

	Chrome,  //WebDriver driver = new ChromeDriver();
	Firefox, //WebDriver driver = new FirefoxDriver();
	IE,      //WebDriver driver = new InternetExplorerDriver();
	Edge,    //WebDriver driver = new EdgeDriver();
	Opera,   //WebDriver driver = new OperaDriver();
	//TODO
	Safari, //SafariDriver requires Safari 10 running on OSX El Capitan or greater.
	iOS_iPhone, // ?? http://ios-driver.github.io/ios-driver/
	iOS_iPad,   // ?? http://ios-driver.github.io/ios-driver/
	Android, // ?? http://selendroid.io/
	HtmlUnit //WebDriver driver = new HtmlUnitDriver();
	
}
