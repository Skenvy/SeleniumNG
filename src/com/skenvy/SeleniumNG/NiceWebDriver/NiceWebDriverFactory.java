package com.skenvy.SeleniumNG.NiceWebDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.skenvy.SeleniumNG.DomainConstants;

/***
 * A singleton factory used to instantiate subclasses of the abstract
 * NiceWebDriver, either as local or remote. Use the getFactory method to
 * obtain the factory, supplying the getFactory method with a path to the 
 * config file associated with the DomainConstants class. The only fields
 * expected by this class are those that set the full system path to the 
 * drivers'
 */
public class NiceWebDriverFactory {
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Singleton definition
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Keeps track of the list of drivers that have already 
	 * been set, through creation by this factory
	 */
	private static ArrayList<DriverType> driversSet;
	
	/***
	 * The internal instance of a singleton (private static this)
	 */
	private static NiceWebDriverFactory instance = null;
	
	/***
	 * Instantiate the NiceWebDriverFactory singleton
	 * @throws IOException 
	 */
	private NiceWebDriverFactory(String configFilePath){
		driversSet = new ArrayList<DriverType>();
		try {
			domainConstants = new DomainConstants(configFilePath);
		} catch (IOException e) {
			domainConstants = null;
			e.printStackTrace();
		}
	}
	
	/***
	 * Instantiate the NiceWebDriverFactory singleton
	 * @throws IOException 
	 */
	private NiceWebDriverFactory(DomainConstants domainConstantsIn){
		driversSet = new ArrayList<DriverType>();
		domainConstants = domainConstantsIn;
	}
	
	/***
	 * Get the singleton factory NiceWebDriverFactory, requiring the path to
	 * the configuration file.
	 * @return NiceWebDriverFactory
	 * @throws IOException 
	 */
	public static NiceWebDriverFactory getFactory(String configFilePath){
		if(instance == null) {
			instance = new NiceWebDriverFactory(configFilePath);
		}
		return instance;
	}
	
	/***
	 * Get the singleton factory NiceWebDriverFactory, requiring the domainConstants
	 * @return NiceWebDriverFactory
	 * @throws IOException 
	 */
	public static NiceWebDriverFactory getFactory(DomainConstants domainConstants){
		if(instance == null) {
			instance = new NiceWebDriverFactory(domainConstants);
		}
		return instance;
	}
	
	/***
	 * Get the singleton factory NiceWebDriverFactory.
	 * @return NiceWebDriverFactory
	 * @throws FileNotFoundException 
	 */
	public static NiceWebDriverFactory getFactory() throws FileNotFoundException {
		if(instance == null) {
			throw new FileNotFoundException("Before you can call the parameterless getFactory, you must call it once with path to the configuration file, or a reference to a staticly fielded DomainConstants");
		}
		return instance;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Must include domain constants
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * An instance of the DomainConstants class. All fields of the Domain
	 * Constants class are accessed statically. This instance is simply used in
	 * for nullity checking, to confirm that the class's constructor has been
	 * called, thus appropriately assigning values to the static fields.
	 */
	private static DomainConstants domainConstants;
	
	/***
	 * Get the DomainConstants field associated with the
	 * NiceWebDriverFactory singleton
	 * @return
	 */
	public DomainConstants getDomainConstants() {
		return domainConstants;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Must allow for declaring verbose output or turning it off
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Determines whether the factory or instantiated NiceWebDriver's will
	 * print verbose messages
	 */
	private static boolean outputIsVerbose = false;

	/***
	 * Get the verbosity
	 * @return
	 */
	public boolean getOutputIsVerbose() {
		return outputIsVerbose;
	}
	
	/***
	 * Sets the verbosity of the NiceWebDriverFactory
	 * @param verboseMode
	 */
	public void setVerboseModeOn(boolean verboseMode) {
		outputIsVerbose = verboseMode;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Define the factory methods | Get Local
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Returns an instance of the NiceWebDriver of a specific DriverType
	 * @param driverType
	 * @return NiceWebDriver
	 * @throws FileNotFoundException
	 */
	public NiceWebDriver getNiceWebDriver(DriverType driverType) throws FileNotFoundException {
		return getNiceWebDriver(driverType,"");
	}
	
	/***
	 * Returns an instance of the NiceWebDriver of a specific DriverType, with
	 * browser options specified by a string of option arguments
	 * @param driverType
	 * @param optionArgs
	 * @return NiceWebDriver
	 * @throws FileNotFoundException
	 */
	public NiceWebDriver getNiceWebDriver(DriverType driverType, String optionArgs) throws FileNotFoundException {
		return getNiceWebDriver(driverType,optionArgs,-1);
	}
	
	/***
	 * Returns an instance of the NiceWebDriver of a specific DriverType, with
	 * browser options specified by a string of option arguments, and a non
	 * default number of seconds to wait for the presence of web elements.
	 * @param driverType
	 * @param optionArgs
	 * @param waitSeconds
	 * @return NiceWebDriver
	 * @throws FileNotFoundException
	 */
	public NiceWebDriver getNiceWebDriver(DriverType driverType, String optionArgs, int waitSeconds) throws FileNotFoundException {
		setSystemPropertyWebDriver(driverType);
		return getNiceWebDriverInstanceForDriver(driverType,optionArgs,waitSeconds);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Define the factory methods | Get Remote
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Returns a remote instance of the NiceWebDriver of a specific DriverType
	 * @param driverType
	 * @return NiceWebDriver
	 * @throws FileNotFoundException
	 */
	public NiceWebDriver getNiceWebDriverRemote(DriverType driverType) throws FileNotFoundException {
		return getNiceWebDriverRemote(driverType,null);
	}

	/***
	 * Returns a remote instance of the NiceWebDriver of a specific DriverType,
	 * with a remote address specified by an URL
	 * @param driverType
	 * @param remoteAddress
	 * @return NiceWebDriver
	 * @throws FileNotFoundException
	 */
	public NiceWebDriver getNiceWebDriverRemote(DriverType driverType, URL remoteAddress) throws FileNotFoundException {
		return getNiceWebDriverRemote(driverType,remoteAddress,-1);
	}

	/***
	 * Returns a remote instance of the NiceWebDriver of a specific DriverType,
	 * with a remote address specified by an URL, and a non default number of 
	 * seconds to wait for the presence of web elements.
	 * @param driverType
	 * @param remoteAddress
	 * @param waitSeconds
	 * @return NiceWebDriver
	 * @throws FileNotFoundException
	 */
	public NiceWebDriver getNiceWebDriverRemote(DriverType driverType, URL remoteAddress, int waitSeconds) throws FileNotFoundException {
		setSystemPropertyWebDriver(driverType);
		return getNiceWebDriverInstanceForRemote(driverType,remoteAddress,waitSeconds);
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Define the factory methods | Internal Switch Cases | Local
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Returns a local NiceWebDriver appropriate for the driver type, browser
	 * option arguments, and time to wait for the presence of web elements.
	 * @param driverType
	 * @param optionArgs
	 * @param waitSeconds
	 * @return NiceWebDriver
	 */
	private NiceWebDriver getNiceWebDriverInstanceForDriver(DriverType driverType, String optionArgs, int waitSeconds) {
		Object[] oArgs = getUnderloadedConstructorArrayForDriver(optionArgs,waitSeconds);
		return getNiceWebDriverInstance(driverType,oArgs);
	}

	/***
	 * Returns the Object[] appropriate for creating a local NiceWebDriver on
	 * calling the UnderloadedNiceChromeDriverConstructor(Object[])
	 * @param optionArgs
	 * @param waitSeconds
	 * @return Object[]
	 */
	private Object[] getUnderloadedConstructorArrayForDriver(String optionArgs, int waitSeconds) {
		Object[] oArgs = new Object[] {optionArgs,waitSeconds};
		if(optionArgs == null || optionArgs.equals("")) {
			oArgs[0] = true;
		}
		if(waitSeconds < 1) {
			oArgs = new Object[] {oArgs[0]};
		}
		return oArgs;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Define the factory methods | Internal Switch Cases | Remote
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Returns a remote NiceWebDriver appropriate for the driver type, remote
	 * URL, and time to wait for the presence of web elements.
	 * @param driverType
	 * @param remoteAddress
	 * @param waitSeconds
	 * @return NiceWebDriver
	 */
	private NiceWebDriver getNiceWebDriverInstanceForRemote(DriverType driverType, URL remoteAddress, int waitSeconds) {
		Object[] oArgs = getUnderloadedConstructorArrayForRemote(remoteAddress,waitSeconds);
		return getNiceWebDriverInstance(driverType,oArgs);
	}

	/***
	 * Returns the Object[] appropriate for creating a remote NiceWebDriver on
	 * calling the UnderloadedNiceChromeDriverConstructor(Object[])
	 * @param remoteAddress
	 * @param waitSeconds
	 * @return Object[]
	 */
	private Object[] getUnderloadedConstructorArrayForRemote(URL remoteAddress, int waitSeconds) {
		Object[] oArgs = new Object[] {remoteAddress,waitSeconds};
		if(remoteAddress == null) {
			oArgs[0] = false;
		}
		if(waitSeconds < 1) {
			oArgs = new Object[] {oArgs[0]};
		}
		return oArgs;
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Define the factory methods | Internal Switch Cases | Both
 */
///////////////////////////////////////////////////////////////////////////////

	/***
	 * Handles the switch case of handing the Object[] to the method
	 * UnderloadedNiceChromeDriverConstructor, switching on the driver type
	 * @param driverType
	 * @param oArgs
	 * @return NiceWebDriver
	 */
	private NiceWebDriver getNiceWebDriverInstance(DriverType driverType, Object[] oArgs){
		switch(driverType) {
			case Chrome:
				return new NiceChrome().UnderloadedNiceWebDriverConstructor(oArgs).getThisWithVerbositySetTo(outputIsVerbose);
			case Firefox:
				return null; //TODO: Make Firefox subclass
			case IE:
				return null; //TODO: Make IE subclass
			case Edge:
				return null; //TODO: Make Edge subclass
			case Opera:
				return null; //TODO: Make Opera subclass
			case Safari:
				return null; //TODO: Make Safari subclass
			case iOS_iPhone:
				return null; //TODO: Make iOS_iPhone subclass
			case iOS_iPad:
				return null; //TODO: Make iOS_iPad subclass
			case Android:
				return null; //TODO: Make Android subclass
			case HtmlUnit:
				return null; //TODO: Make HtmlUnit subclass
			default:
				return null;
		}
	}
	
	/***
	 * Maps a DriverType enum value K to the DesiredCapabilities required to
	 * make a RemoteWebDriver
	 * TODO - Deprecate and move into getRemoteCapability() overrides
	 */
	@SuppressWarnings({ "deprecation", "serial", "unused" })
	private static final HashMap<DriverType,Capabilities> webDriverCapabilites = new HashMap<DriverType,Capabilities>(){{
		put(DriverType.Chrome,DesiredCapabilities.chrome());
		put(DriverType.Firefox,DesiredCapabilities.firefox());
		put(DriverType.IE,DesiredCapabilities.internetExplorer());
		put(DriverType.Edge,DesiredCapabilities.edge());
		put(DriverType.Opera,DesiredCapabilities.opera());
		put(DriverType.Safari,DesiredCapabilities.safari());
		put(DriverType.iOS_iPhone,DesiredCapabilities.iphone());
		put(DriverType.iOS_iPad,DesiredCapabilities.ipad());
		put(DriverType.Android,DesiredCapabilities.android());
		put(DriverType.HtmlUnit,DesiredCapabilities.htmlUnit());
	}};
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Define the System Path setting encapsulation
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * If the driver system property is known, and the driver file can be 
	 * found, and the driver has not yet already been set, will set the 
	 * system path to the driver file for this driver type.
	 * @param driverType
	 * @throws FileNotFoundException
	 * @throws NullPointerException
	 */
	private void setSystemPropertyWebDriver(DriverType driverType) throws FileNotFoundException, NullPointerException {
		if(domainConstants != null) {
			String driverPath = DomainConstants.webDriverSystemPaths.get(driverType);
			setSystemPropertyWebDriver(driverType, driverPath);
		} else {
			throw new NullPointerException("The NiceWebDriverFactory has been accessed in a way that did not satisfactorially assign value to the static fields of the DomainConstants class");
		}
	}
	
	/***
	 * If the driver system property is known, and the driver file can be 
	 * found, and the driver has not yet already been set, will set the 
	 * system path to the driver file for this driver type.
	 * @param driverType
	 * @param driverPath
	 * @throws FileNotFoundException
	 * @throws NullPointerException
	 */
	private void setSystemPropertyWebDriver(DriverType driverType, String driverPath) throws FileNotFoundException, NullPointerException {
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
	 * Maps a DriverType enum value K to the string 
	 * required for the System.setProperty(K,...)
	 */
	@SuppressWarnings("serial")
	private static final HashMap<DriverType,String> webDriverSystemProperties = new HashMap<DriverType,String>(){{
		put(DriverType.Chrome,"webdriver.chrome.driver");
		put(DriverType.Firefox,"webdriver.gecko.driver");
		put(DriverType.IE,"webdriver.ie.driver");
		put(DriverType.Edge,"webdriver.edge.driver");
		put(DriverType.Opera,"webdriver.opera.driver");
		put(DriverType.Safari,null); //TODO: Investigate the Safari driver's system prop path
		put(DriverType.iOS_iPhone,null); //TODO: Investigate the iOS_iPhone driver's system prop path
		put(DriverType.iOS_iPad,null); //TODO: Investigate the iOS_iPad driver's system prop path
		put(DriverType.Android,null); //TODO: Investigate the Android driver's system prop path
		put(DriverType.HtmlUnit,null); //TODO: Investigate the HtmlUnit driver's system prop path
	}};
	
}
