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
	
}
