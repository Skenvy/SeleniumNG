package com.skenvy.SeleniumNG;

import java.util.HashMap;

import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;

/***
 * A class to hold all "final static" string names for fields and string and 
 * integer defaults. All protected members are for destringifying the 
 * DomainConstants class
 */
final class DomainConstantsProperties {

///////////////////////////////////////////////////////////////////////////////
/*
 * DriverType Strings mapped to Config key names
 */
///////////////////////////////////////////////////////////////////////////////

	/***WebDriverSystemPaths*/
	private final static String webDriverSystemPaths = "WebDriverSystemPaths";
	
	/***
	 * Maps DriverType enum strings to the config name for the value of the 
	 * path to the driver for each browser
	 */
	@SuppressWarnings("serial")
	protected final static HashMap<DriverType,String> webDriverSystemPathPerDriverType = new HashMap<DriverType,String>(){{
		for(DriverType dt : DriverType.values()) {
			put(dt,(webDriverSystemPaths+"."+dt.toString()));
		}
	}};
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Private Final Static : suffix/prefixes of the config names
 */
///////////////////////////////////////////////////////////////////////////////

	/***Local*/
	private final static String Local = "Local";
	/***Test*/
	private final static String Test = "Test";
	/***TestSleeps*/
	private final static String TestSleeps = "TestSleeps";

	/***EnvironIP*/
	private final static String EnvironIP = "EnvironIP";
	/***EnvironPort*/
	private final static String EnvironPort = "EnvironPort";
	/***WebContextRoot*/
	private final static String WebContextRoot = "WebContextRoot";
	/***WaitSeconds*/
	private final static String WaitSeconds = "WaitSeconds";

	/***MilliSecondsBetweenKeyStrokes*/
	private final static String MilliSecondsBetweenKeyStrokes = "MilliSecondsBetweenKeyStrokes";
	/***MilliSecondsBeforeClick*/
	private final static String MilliSecondsBeforeClick = "MilliSecondsBeforeClick";
	/***MilliSecondsAfterClick*/
	private final static String MilliSecondsAfterClick = "MilliSecondsAfterClick";
	/***MilliSecondSimulateInteractivePause*/
	private final static String MilliSecondSimulateInteractivePause = "MilliSecondSimulateInteractivePause";
	/***MilliSecondDurationOfSuccessMessage*/
	private final static String MilliSecondDurationOfSuccessMessage = "MilliSecondDurationOfSuccessMessage";

	/***SeleniumNode*/
	private final static String SeleniumNode = "SeleniumNode";
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Protected Final Static : Domain Constant defaults
 */
///////////////////////////////////////////////////////////////////////////////

	/*
	 * Domain Constant defaults : Local/Test
	 */

	/***localhost*/
	protected final static String defaultEnvironIP = "localhost";
	/***8080*/
	protected final static int defaultEnvironPort = 8080;
	/****/
	protected final static String defaultWebContextRoot = "";
	/***1*/
	protected final static int defaultWaitSeconds = 1;
	/***5*/
	protected final static int defaultInstantiationMaxRetry = 5;
	
	/*
	 * Domain Constant defaults : "TestSleep"
	 */

	/***100*/
	protected final static int defaultMilliSecondsBetweenKeyStrokes = 100;
	/***100*/
	protected final static int defaultMilliSecondsBeforeClick = 100;
	/***100*/
	protected final static int defaultMilliSecondsAfterClick = 100;
	/***2000*/
	protected final static int defaultMilliSecondSimulateInteractivePause = 2000;
	/***3500*/
	protected final static int defaultMilliSecondDurationOfSuccessMessage = 3500;
	
	/*
	 * Domain Constant defaults : Selenium Nodes
	 */

	/***0*/
	protected final static int defaultSeleniumNodeCount = 0;
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Protected Final Static : Config names
 */
///////////////////////////////////////////////////////////////////////////////

	/*
	 * Config names : Local
	 */

	/***Local.EnvironPort*/
	protected final static String LocalEnvironPort = Local+"."+EnvironPort;
	/***Local.WebContextRoot*/
	protected final static String LocalWebContextRoot = Local+"."+WebContextRoot;
	/***Local.WaitSeconds*/
	protected final static String LocalWaitSeconds = Local+"."+WaitSeconds;
	
	/*
	 * Config names : Test
	 */

	/***Test.EnvironIP*/
	protected final static String TestEnvironIP = Test+"."+EnvironIP;
	/***Test.EnvironPort*/
	protected final static String TestEnvironPort = Test+"."+EnvironPort;
	/***Test.WebContextRoot*/
	protected final static String TestWebContextRoot = Test+"."+WebContextRoot;
	/***Test.WaitSeconds*/
	protected final static String TestWaitSeconds = Test+"."+WaitSeconds;
	
	/*
	 * Config names : TestSleep
	 */

	/***TestSleeps.MilliSecondsBetweenKeyStrokes*/
	protected final static String TestSleepsMilliSecondsBetweenKeyStrokes = TestSleeps+"."+MilliSecondsBetweenKeyStrokes;
	/***TestSleeps.MilliSecondsBeforeClick*/
	protected final static String TestSleepsMilliSecondsBeforeClick = TestSleeps+"."+MilliSecondsBeforeClick;
	/***TestSleeps.MilliSecondsAfterClick*/
	protected final static String TestSleepsMilliSecondsAfterClick = TestSleeps+"."+MilliSecondsAfterClick;
	/***TestSleeps.MilliSecondSimulateInteractivePause*/
	protected final static String TestSleepsMilliSecondSimulateInteractivePause = TestSleeps+"."+MilliSecondSimulateInteractivePause;
	/***TestSleeps.MilliSecondDurationOfSuccessMessage*/
	protected final static String TestSleepsMilliSecondDurationOfSuccessMessage = TestSleeps+"."+MilliSecondDurationOfSuccessMessage;
	
	/*
	 * Config names : Selenium Nodes
	 */
	
	/***SeleniumNodeCount*/
	protected final static String SeleniumNodeCount = SeleniumNode+"Count";
	/***SeleniumNodeLocal_{@code<# from 1 to SeleniumNodeCount>}*/
	protected final static String SeleniumNodeLocal = SeleniumNode+"Local_";
	/***SeleniumNodeRemoteURL_{@code<# from 1 to SeleniumNodeCount>}*/
	protected final static String SeleniumNodeRemoteURL = SeleniumNode+"RemoteURL_";
	/***SeleniumNodeDriverType_{@code<# from 1 to SeleniumNodeCount>}*/
	protected final static String SeleniumNodeDriverType = SeleniumNode+"DriverType_";
	
}
