package com.skenvy.SeleniumNG.NiceWebDriver;

import java.net.URL;
import java.net.UnknownHostException;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class NiceChrome extends NiceWebDriver {

	//Super every constructor
	
	protected NiceChrome(boolean localInstance) {
		super(localInstance);
	}
	
	protected NiceChrome(boolean localInstance, int waitSeconds) {
		super(localInstance,waitSeconds);
	}
	
	protected NiceChrome(String optionArgs) {
		super(optionArgs);
	}
	
	protected NiceChrome(String optionArgs, int waitSeconds) {
		super(optionArgs, waitSeconds);
	}
	
	protected NiceChrome(URL remoteAddress) {
		super(remoteAddress);
	}
	
	protected NiceChrome(URL remoteAddress, int waitSeconds) {
		super(remoteAddress, waitSeconds);
	}

	//Overrides - Chrome
	
	@Override
	protected MutableCapabilities makeBrowserOptions(String optionArgs) {
		ChromeOptions options = new ChromeOptions();
		//"--incognito ";
		//"--start-maximized ";
		options.addArguments(optionArgs);
		return options;
	}
	
	@Override
	protected WebDriver getDriver(MutableCapabilities mutableCapabilities) {
		ChromeOptions options = (ChromeOptions) mutableCapabilities;
		WebDriver wd = (new ChromeDriver(options));
		return wd;
	}

	@Override
	protected Capabilities getRemoteCapability() {
		return DesiredCapabilities.chrome();
	}

	@Override
	protected WebDriver getDriver() {
		return (new ChromeDriver());
	}

}
