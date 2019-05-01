package com.skenvy.SeleniumNG;

import java.net.UnknownHostException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;


public abstract class WebDriverExtensibleWrapper {
	
	public static void main(String[] args) throws UnknownHostException {
		
		
	}
	
	
	/***
	 * Maps a DriverExtension enum value K to the string required for the System.setProperty(K,...)
	 */
	private static final HashMap<DriverExtension,String> webDriverSystemProperties = new HashMap<DriverExtension,String>(){{
		put(DriverExtension.Chrome,"webdriver.chrome.driver");
		put(DriverExtension.Firefox,"webdriver.gecko.driver");
		put(DriverExtension.IE,"webdriver.ie.driver");
		put(DriverExtension.Edge,"webdriver.edge.driver");
		put(DriverExtension.Opera,"webdriver.opera.driver");
		//TODO
		put(DriverExtension.Safari,null);
		put(DriverExtension.iOS,null);
		put(DriverExtension.Android,null);
		//Removed?
		//put(DriverExtension.HtmlUnit,null);
	}};
	
	/***
	 * An enum to define the driver types as per
	 * https://www.seleniumhq.org/docs/03_webdriver.jsp#selenium-webdriver-s-drivers
	 */
	private enum DriverExtension {
		Chrome,  //WebDriver driver = new ChromeDriver();
		Firefox, //WebDriver driver = new FirefoxDriver();
		IE,      //WebDriver driver = new InternetExplorerDriver();
		Edge,    //WebDriver driver = new EdgeDriver();
		Opera,   //WebDriver driver = new OperaDriver();
		//TODO
		Safari, //SafariDriver requires Safari 10 running on OSX El Capitan or greater.
		iOS,    // ?? http://ios-driver.github.io/ios-driver/
		Android // ?? http://selendroid.io/
		//Removed?
		//HtmlUnit //WebDriver driver = new HtmlUnitDriver();
	}

}
