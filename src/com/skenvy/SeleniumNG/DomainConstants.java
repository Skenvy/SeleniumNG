package com.skenvy.SeleniumNG;

import java.util.HashMap;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriverFactory.DriverExtension;

public class DomainConstants {
	
	/***
	 * Maps a DriverExtension enum value K to the path Domain Constant required for the System.setProperty(...,K)
	 */
	public static final HashMap<DriverExtension,String> defaultWebDriverSystemPaths = new HashMap<DriverExtension,String>(){{
		put(DriverExtension.Chrome,"C:\\Users\\i-am-\\Documents\\06. PROGRAMS\\Programming\\Selenium\\chromedriver.exe");
		put(DriverExtension.Firefox,"");
		put(DriverExtension.IE,"");
		put(DriverExtension.Edge,"");
		put(DriverExtension.Opera,"");
		put(DriverExtension.Safari,"");
		put(DriverExtension.iOS,"");
		put(DriverExtension.Android,"");
	}};
	
	public class localDefault {
		public final static int environPort = 8080;
		public final static String webContextRoot = "";
		public final static int defaultWaitSeconds = 1;
		public final static int defaultChromeInstantiationMaxRetry = 5;
	}
	
	public class testDefault {
		//These are the "host" environment IP and Port
		public final static String environIP = "localhost";
		public final static int environPort = 8080;
		public final static String webContextRoot = "";
		public final static int defaultWaitSeconds = 1;
		public final static int defaultChromeInstantiationMaxRetry = 5;
		
		public class SleepMilliSeconds {
			public final static int betweenKeyStrokes = 100;
			public final static int beforeClick = 100;
			public final static int afterClick = 100;
			public final static int simulateInteractivePause = 2000;
			public final static int durationOfSuccessMessage = 3500;
		}
	}
	
	public final static HashMap<String,DesiredCapabilities> seleniumNodes = new HashMap<String,DesiredCapabilities>() {{
		/*LOCAL-WEBDRIVER*/ put(null,null);
		///*LOCAL-GRIDNODE */ put("http://localhost:5555/wd/hub",DesiredCapabilities.chrome());
	}};
	
	public class UrlConstants {
		public final static String HTTP = "http";
		public final static String HTTPS = "https";
	}
}
