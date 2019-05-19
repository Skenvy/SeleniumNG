package com.skenvy.SeleniumNG;

import java.util.HashMap;

import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;

public class DomainConstantsProperties {

	public final static String webDriverSystemPaths = "WebDriverSystemPaths";
	
	public final static HashMap<DriverType,String> webDriverSystemPathPerDriverType = new HashMap<DriverType,String>(){{
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
	
	public final static String defaultEnvironIP = "localhost";
	public final static int defaultEnvironPort = 8080;
	public final static String defaultWebContextRoot = "";
	public final static int defaultWaitSeconds = 1;
	public final static int defaultInstantiationMaxRetry = 5;
	
	public final static String LocalEnvironPort = Local+"."+EnvironPort;
	public final static String LocalWebContextRoot = Local+"."+WebContextRoot;
	public final static String LocalWaitSeconds = Local+"."+WaitSeconds;
	public final static String LocalInstantiationMaxRetry = Local+"."+InstantiationMaxRetry;
	
	public final static String TestEnvironIP = Test+"."+EnvironIP;
	public final static String TestEnvironPort = Test+"."+EnvironPort;
	public final static String TestWebContextRoot = Test+"."+WebContextRoot;
	public final static String TestWaitSeconds = Test+"."+WaitSeconds;
	public final static String TestInstantiationMaxRetry = Test+"."+InstantiationMaxRetry;
	
	private final static String MilliSecondsBetweenKeyStrokes = "MilliSecondsBetweenKeyStrokes";
	private final static String MilliSecondsBeforeClick = "MilliSecondsBeforeClick";
	private final static String MilliSecondsAfterClick = "MilliSecondsAfterClick";
	private final static String MilliSecondSimulateInteractivePause = "MilliSecondSimulateInteractivePause";
	private final static String MilliSecondDurationOfSuccessMessage = "MilliSecondDurationOfSuccessMessage";
	
	public final static int defaultMilliSecondsBetweenKeyStrokes = 100;
	public final static int defaultMilliSecondsBeforeClick = 100;
	public final static int defaultMilliSecondsAfterClick = 100;
	public final static int defaultMilliSecondSimulateInteractivePause = 2000;
	public final static int defaultMilliSecondDurationOfSuccessMessage = 3500;
	
	public final static String TestSleepsMilliSecondsBetweenKeyStrokes = TestSleeps+"."+MilliSecondsBetweenKeyStrokes;
	public final static String TestSleepsMilliSecondsBeforeClick = TestSleeps+"."+MilliSecondsBeforeClick;
	public final static String TestSleepsMilliSecondsAfterClick = TestSleeps+"."+MilliSecondsAfterClick;
	public final static String TestSleepsMilliSecondSimulateInteractivePause = TestSleeps+"."+MilliSecondSimulateInteractivePause;
	public final static String TestSleepsMilliSecondDurationOfSuccessMessage = TestSleeps+"."+MilliSecondDurationOfSuccessMessage;
	
	private final static String SeleniumNode = "SeleniumNode";
	public final static String SeleniumNodeCount = SeleniumNode+"Count";
	public final static int defaultSeleniumNodeCount = 0;
	public final static String SeleniumNodeLocal = SeleniumNode+"Local_";
	public final static String SeleniumNodeRemoteURL = SeleniumNode+"RemoteURL_";
	public final static String SeleniumNodeDriverType = SeleniumNode+"DriverType_";
	
}
