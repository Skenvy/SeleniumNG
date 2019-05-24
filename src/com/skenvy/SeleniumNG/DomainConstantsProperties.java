package com.skenvy.SeleniumNG;

import java.util.HashMap;

import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;

/***
 * A class to hold all "final static" string names for fields and string and 
 * integer defaults. All protected members are for destringifying the 
 * DomainConstants class
 */
final class DomainConstantsProperties {

	protected final static String webDriverSystemPaths = "WebDriverSystemPaths";
	
	@SuppressWarnings("serial")
	protected final static HashMap<DriverType,String> webDriverSystemPathPerDriverType = new HashMap<DriverType,String>(){{
		for(DriverType dt : DriverType.values()) {
			put(dt,(webDriverSystemPaths+"."+dt.toString()));
		}
	}};
	
	private final static String Local = "Local";
	private final static String Test = "Test";
	private final static String TestSleeps = "TestSleeps";
	
	private final static String EnvironIP = "EnvironIP";
	private final static String EnvironPort = "EnvironPort";
	private final static String WebContextRoot = "WebContextRoot";
	private final static String WaitSeconds = "WaitSeconds";
	private final static String InstantiationMaxRetry = "InstantiationMaxRetry";
	
	protected final static String defaultEnvironIP = "localhost";
	protected final static int defaultEnvironPort = 8080;
	protected final static String defaultWebContextRoot = "";
	protected final static int defaultWaitSeconds = 1;
	protected final static int defaultInstantiationMaxRetry = 5;
	
	protected final static String LocalEnvironPort = Local+"."+EnvironPort;
	protected final static String LocalWebContextRoot = Local+"."+WebContextRoot;
	protected final static String LocalWaitSeconds = Local+"."+WaitSeconds;
	protected final static String LocalInstantiationMaxRetry = Local+"."+InstantiationMaxRetry;
	
	protected final static String TestEnvironIP = Test+"."+EnvironIP;
	protected final static String TestEnvironPort = Test+"."+EnvironPort;
	protected final static String TestWebContextRoot = Test+"."+WebContextRoot;
	protected final static String TestWaitSeconds = Test+"."+WaitSeconds;
	protected final static String TestInstantiationMaxRetry = Test+"."+InstantiationMaxRetry;
	
	private final static String MilliSecondsBetweenKeyStrokes = "MilliSecondsBetweenKeyStrokes";
	private final static String MilliSecondsBeforeClick = "MilliSecondsBeforeClick";
	private final static String MilliSecondsAfterClick = "MilliSecondsAfterClick";
	private final static String MilliSecondSimulateInteractivePause = "MilliSecondSimulateInteractivePause";
	private final static String MilliSecondDurationOfSuccessMessage = "MilliSecondDurationOfSuccessMessage";
	
	protected final static int defaultMilliSecondsBetweenKeyStrokes = 100;
	protected final static int defaultMilliSecondsBeforeClick = 100;
	protected final static int defaultMilliSecondsAfterClick = 100;
	protected final static int defaultMilliSecondSimulateInteractivePause = 2000;
	protected final static int defaultMilliSecondDurationOfSuccessMessage = 3500;
	
	protected final static String TestSleepsMilliSecondsBetweenKeyStrokes = TestSleeps+"."+MilliSecondsBetweenKeyStrokes;
	protected final static String TestSleepsMilliSecondsBeforeClick = TestSleeps+"."+MilliSecondsBeforeClick;
	protected final static String TestSleepsMilliSecondsAfterClick = TestSleeps+"."+MilliSecondsAfterClick;
	protected final static String TestSleepsMilliSecondSimulateInteractivePause = TestSleeps+"."+MilliSecondSimulateInteractivePause;
	protected final static String TestSleepsMilliSecondDurationOfSuccessMessage = TestSleeps+"."+MilliSecondDurationOfSuccessMessage;
	
	private final static String SeleniumNode = "SeleniumNode";
	protected final static String SeleniumNodeCount = SeleniumNode+"Count";
	protected final static int defaultSeleniumNodeCount = 0;
	protected final static String SeleniumNodeLocal = SeleniumNode+"Local_";
	protected final static String SeleniumNodeRemoteURL = SeleniumNode+"RemoteURL_";
	protected final static String SeleniumNodeDriverType = SeleniumNode+"DriverType_";
	
}
