package com.skenvy.SeleniumNG;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public class DomainConstants {
	
	/***
	 * Stores the properties read in from the config file.
	 */
	private final static Properties properties = new Properties();
	
	/***
	 * Maps a DriverExtension enum value K to the path Domain Constant required
	 * for the System.setProperty(...,K)
	 */
	public static HashMap<DriverType,String> webDriverSystemPaths = new HashMap<DriverType,String>();
	
	public static Local local = null;
	
	public static Test test = null;
	
	public static TestSleeps testSleeps = null;
	
	public static int seleniumNodeCount = 0;
	
	public static SeleniumNode[] seleniumNodes = null;
	
	
	/***
	 * Construct the properties and assign the values to the dictionaries
	 * returned by this class.
	 * @param configFilePath
	 * @throws IOException
	 */
	public DomainConstants(String configFilePath) throws IOException{
		loadPropertiesFromXMLConfigFile(configFilePath);
		assignWebDriverSystemPaths();
		local = assignLocal();
		test = assignTest();
		testSleeps = assignTestSleeps();
		seleniumNodeCount = assignSeleniumNodeCount();
		assignSeleniumNodes();
	}
	
	private void loadPropertiesFromXMLConfigFile(String configFilePath) throws IOException{
		//Read in the properties then close the file
		FileInputStream in = new FileInputStream(configFilePath);
		properties.loadFromXML(in);
		in.close();
	}
	
	private void assignWebDriverSystemPaths() {
		for(DriverType dt : DriverType.values()) {
			webDriverSystemPaths.put(dt,properties.getProperty(DomainConstantsProperties.webDriverSystemPathPerDriverType.get(dt)));
		}
	}
	
	private Local assignLocal() {
		int environPort = getPropertyInteger(DomainConstantsProperties.LocalEnvironPort,DomainConstantsProperties.defaultEnvironPort);
		String webContextRoot = properties.getProperty(DomainConstantsProperties.LocalWebContextRoot,DomainConstantsProperties.defaultWebContextRoot);
		int waitSeconds = getPropertyInteger(DomainConstantsProperties.LocalWaitSeconds,DomainConstantsProperties.defaultWaitSeconds);
		int instantiationMaxRetry = getPropertyInteger(DomainConstantsProperties.LocalInstantiationMaxRetry,DomainConstantsProperties.defaultInstantiationMaxRetry);
		return new Local(environPort,webContextRoot,waitSeconds,instantiationMaxRetry);
	}
	
	private Test assignTest() {
		String environIP = properties.getProperty(DomainConstantsProperties.TestEnvironIP,DomainConstantsProperties.defaultEnvironIP);
		int environPort = getPropertyInteger(DomainConstantsProperties.TestEnvironPort,DomainConstantsProperties.defaultEnvironPort);
		String webContextRoot = properties.getProperty(DomainConstantsProperties.TestWebContextRoot,DomainConstantsProperties.defaultWebContextRoot);
		int waitSeconds = getPropertyInteger(DomainConstantsProperties.TestWaitSeconds,DomainConstantsProperties.defaultWaitSeconds);
		int instantiationMaxRetry = getPropertyInteger(DomainConstantsProperties.TestInstantiationMaxRetry,DomainConstantsProperties.defaultInstantiationMaxRetry);
		return new Test(environIP,environPort,webContextRoot,waitSeconds,instantiationMaxRetry);
	}
	
	private TestSleeps assignTestSleeps() {
		int milliSecondsBetweenKeyStrokes = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondsBetweenKeyStrokes, DomainConstantsProperties.defaultMilliSecondsBetweenKeyStrokes);
		int milliSecondsBeforeClick = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondsBeforeClick, DomainConstantsProperties.defaultMilliSecondsBeforeClick);
		int milliSecondsAfterClick = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondsAfterClick, DomainConstantsProperties.defaultMilliSecondsAfterClick);
		int milliSecondSimulateInteractivePause = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondSimulateInteractivePause, DomainConstantsProperties.defaultMilliSecondSimulateInteractivePause);
		int milliSecondDurationOfSuccessMessage = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondDurationOfSuccessMessage, DomainConstantsProperties.defaultMilliSecondDurationOfSuccessMessage);
		return new TestSleeps(milliSecondsBetweenKeyStrokes,milliSecondsBeforeClick,milliSecondsAfterClick,milliSecondSimulateInteractivePause,milliSecondDurationOfSuccessMessage);
	}
	
	private int assignSeleniumNodeCount() {
		return getPropertyInteger(DomainConstantsProperties.SeleniumNodeCount, DomainConstantsProperties.defaultSeleniumNodeCount);
	}
	
	private void assignSeleniumNodes() throws MalformedURLException {
		seleniumNodes = new SeleniumNode[seleniumNodeCount];
		for(int k = 1; k <= seleniumNodeCount; k++) {
			String localNodeString = properties.getProperty(DomainConstantsProperties.SeleniumNodeLocal+k);
			boolean localNode = (localNodeString.equalsIgnoreCase("True"));
			String dcString = properties.getProperty(DomainConstantsProperties.SeleniumNodeDriverType+k);
			DriverType dt = DriverType.valueOf(dcString);
			URL nodeUrl = null;
			if(!localNode) {
				nodeUrl = new URL(properties.getProperty(DomainConstantsProperties.SeleniumNodeRemoteURL+k));
			} 
			seleniumNodes[k-1] = new SeleniumNode(localNode, nodeUrl, dt);
		}
	}
	
	private DesiredCapabilities getDesiredCapabilitiesForDriverType(DriverType dt) {
		switch(dt) {
			case Chrome:
				return DesiredCapabilities.chrome();
			case Firefox:
				return DesiredCapabilities.firefox();
			case IE:
				return DesiredCapabilities.internetExplorer();
			case Edge:
				return DesiredCapabilities.edge();
			case Opera:
				return DesiredCapabilities.opera();
			case Safari:
				return DesiredCapabilities.safari();
			case iOS_iPhone:
				return DesiredCapabilities.iphone();
			case iOS_iPad:
				return DesiredCapabilities.ipad();
			case Android:
				return DesiredCapabilities.android();
			case HtmlUnit:
				return DesiredCapabilities.htmlUnit();
			default:
				return null;
		}
	}
	
	/***
	 * Returns static string components of urls.
	 */
	public class UrlConstants {
		public final static String HTTP = "http";
		public final static String HTTPS = "https";
	}
	
	public class Local {
		
		public final int environPort;
		public final String webContextRoot;
		public final int waitSeconds;
		public final int instantiationMaxRetry;
		
		protected Local(int environPort, String webContextRoot, int waitSeconds, int instantiationMaxRetry){
			this.environPort = environPort;
			this.webContextRoot = webContextRoot;
			if(waitSeconds <= 0) {
				throw new ValueException("Local.WaitSeconds must be greater than 0");
			}
			this.waitSeconds = waitSeconds;
			if(instantiationMaxRetry <= 0) {
				throw new ValueException("Local.InstantiationMaxRetry must be greater than 0");
			}
			this.instantiationMaxRetry = instantiationMaxRetry;
		}
		
	}
	
	public class Test {
		
		public final String environIP;
		public final int environPort;
		public final String webContextRoot;
		public final int waitSeconds;
		public final int instantiationMaxRetry;
		
		protected Test(String environIP, int environPort, String webContextRoot, int waitSeconds, int instantiationMaxRetry) {
			this.environIP = environIP;
			this.environPort = environPort;
			this.webContextRoot = webContextRoot;
			if(waitSeconds <= 0) {
				throw new ValueException("Test.WaitSeconds must be greater than 0");
			}
			this.waitSeconds = waitSeconds;
			if(instantiationMaxRetry <= 0) {
				throw new ValueException("Test.InstantiationMaxRetry must be greater than 0");
			}
			this.instantiationMaxRetry = instantiationMaxRetry;
		}
		
	}
	
	public class TestSleeps {
		
		public final int MilliSecondsBetweenKeyStrokes;
		public final int MilliSecondsBeforeClick;
		public final int MilliSecondsAfterClick;
		public final int MilliSecondSimulateInteractivePause;
		public final int MilliSecondDurationOfSuccessMessage;
		
		protected TestSleeps(int milliSecondsBetweenKeyStrokes, int milliSecondsBeforeClick, int milliSecondsAfterClick, int milliSecondSimulateInteractivePause, int milliSecondDurationOfSuccessMessage) {
			if(milliSecondsBetweenKeyStrokes <= 0) {
				throw new ValueException("TestSleeps.MilliSecondsBetweenKeyStrokes must be greater than 0");
			}
			if(milliSecondsBeforeClick <= 0) {
				throw new ValueException("TestSleeps.MilliSecondsBeforeClick must be greater than 0");
			}
			if(milliSecondsAfterClick <= 0) {
				throw new ValueException("TestSleeps.MilliSecondsAfterClick must be greater than 0");
			}
			if(milliSecondSimulateInteractivePause <= 0) {
				throw new ValueException("TestSleeps.MilliSecondSimulateInteractivePause must be greater than 0");
			}
			if(milliSecondDurationOfSuccessMessage <= 0) {
				throw new ValueException("TestSleeps.MilliSecondDurationOfSuccessMessage must be greater than 0");
			}
			this.MilliSecondsBetweenKeyStrokes = milliSecondsBetweenKeyStrokes;
			this.MilliSecondsBeforeClick = milliSecondsBeforeClick;
			this.MilliSecondsAfterClick = milliSecondsAfterClick;
			this.MilliSecondSimulateInteractivePause = milliSecondSimulateInteractivePause;
			this.MilliSecondDurationOfSuccessMessage = milliSecondDurationOfSuccessMessage;
		}
		
	}
	
	private int getPropertyInteger(String searchString, int defaultInt) {
		return Integer.parseInt(properties.getProperty(searchString,String.valueOf(defaultInt)));
	}
	
	public static class SeleniumNode{
		
		public final boolean local;
		public final URL nodeUrl;
		public final DriverType dt;
		
		public SeleniumNode(boolean local, URL nodeUrl, DriverType dt) {
			this.local = local;
			this.nodeUrl = nodeUrl;
			this.dt = dt;
		}
		
	}
	
}
