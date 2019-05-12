package com.skenvy.SeleniumNG.NiceWebDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;

import com.skenvy.SeleniumNG.DomainConstants;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceDrivers.NiceChrome;


public class NiceWebDriverFactory {
	
	/***
	 * Keeps track of the list of drivers that have already been set, through creation by this factory
	 */
	private static ArrayList<DriverExtension> driversSet;
	
	/***
	 * The internal instance of a singleton (private static this)
	 */
	private static NiceWebDriverFactory instance = null;
	
	/***
	 * Instantiate the NiceWebDriverFactory singleton
	 */
	private NiceWebDriverFactory(){
		driversSet = new ArrayList<DriverExtension>();
	}
	
	/***
	 * Get the singleton factory NiceWebDriverFactory.
	 * @return
	 */
	public static NiceWebDriverFactory getFactory() {
		if(instance == null) {
			instance = new NiceWebDriverFactory();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		//AbstractDriverOptions??
		System.out.println("哇");
	}
	
	//Singleton body is defined, now define the factory method
	
	public NiceWebDriver getNiceWebDriver(DriverExtension driverType) throws UnknownHostException, FileNotFoundException {
		setSystemPropertyWebDriver(driverType);
		return getNiceWebDriverInstanceForDriver(driverType);
	}
	
	public NiceWebDriver getNiceWebDriver(DriverExtension driverType, String driverPath) throws UnknownHostException, FileNotFoundException {
		setSystemPropertyWebDriver(driverType,driverPath);
		return getNiceWebDriverInstanceForDriver(driverType);
	}
	
	private NiceWebDriver getNiceWebDriverInstanceForDriver(DriverExtension driverType) throws UnknownHostException {
		return new NiceChrome("");
	}
	
	/***
	 * If the driver system property is known, and the driver file can be 
	 * found, and the driver has not yet already been set, will set the 
	 * system path to the driver file for this driver type.
	 * @param driverType
	 * @throws FileNotFoundException
	 */
	private void setSystemPropertyWebDriver(DriverExtension driverType) throws FileNotFoundException {
		String driverPath = DomainConstants.defaultWebDriverSystemPaths.get(driverType);
		setSystemPropertyWebDriver(driverType, driverPath);
	}
	
	/***
	 * If the driver system property is known, and the driver file can be 
	 * found, and the driver has not yet already been set, will set the 
	 * system path to the driver file for this driver type.
	 * @param driverType
	 * @param driverPath
	 * @throws FileNotFoundException
	 */
	private void setSystemPropertyWebDriver(DriverExtension driverType, String driverPath) throws FileNotFoundException {
		if(webDriverSystemProperties.get(driverType) != null) {
			if(!driversSet.contains(driverType)) {
				if((new File(driverPath)).exists()) {
					System.setProperty(webDriverSystemProperties.get(driverType),driverPath);
					driversSet.add(driverType);
				} else {
					throw new FileNotFoundException("Failed to locate the driver file at | "+driverPath);
				}
			}
		} else {
			throw new NullPointerException("The driver extensions "+driverType.toString()+" is currently not implemented");
		}
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
	public enum DriverExtension {
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
