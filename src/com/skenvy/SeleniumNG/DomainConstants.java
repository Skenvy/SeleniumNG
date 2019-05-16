package com.skenvy.SeleniumNG;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;

public class DomainConstants {
	
	private final Properties properties = new Properties();
	
	public DomainConstants(String configFilePath) throws IOException{
		//Read in the properties then close the file
		FileInputStream in = new FileInputStream(configFilePath);
		properties.loadFromXML(in);
		in.close();
		//Assign the properties to the hashtables
		webDriverSystemPaths = new HashMap<DriverType,String>(){{
			put(DriverType.Chrome,properties.getProperty("webDriverSystemPaths.Chrome"));
			put(DriverType.Firefox,properties.getProperty("defaultWebDriverSystemPaths.Firefox"));
			put(DriverType.IE,properties.getProperty("defaultWebDriverSystemPaths.IE"));
			put(DriverType.Edge,properties.getProperty("defaultWebDriverSystemPaths.Edge"));
			put(DriverType.Opera,properties.getProperty("defaultWebDriverSystemPaths.Opera"));
			put(DriverType.Safari,properties.getProperty("defaultWebDriverSystemPaths.Safari"));
			put(DriverType.iOS_iPhone,properties.getProperty("defaultWebDriverSystemPaths.iOS_iPhone"));
			put(DriverType.iOS_iPad,properties.getProperty("defaultWebDriverSystemPaths.iOS_iPad"));
			put(DriverType.Android,properties.getProperty("defaultWebDriverSystemPaths.Android"));
			put(DriverType.HtmlUnit,properties.getProperty("defaultWebDriverSystemPaths.HtmlUnit"));
		}};
	}
	
	/***
	 * Maps a DriverExtension enum value K to the path Domain Constant required for the System.setProperty(...,K)
	 */
	public final HashMap<DriverType,String> webDriverSystemPaths;
	
	//Demarcation - Above is dynamic, below is static
	
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
