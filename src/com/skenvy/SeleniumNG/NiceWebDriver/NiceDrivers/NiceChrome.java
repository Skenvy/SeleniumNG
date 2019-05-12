package com.skenvy.SeleniumNG.NiceWebDriver.NiceDrivers;

import java.net.UnknownHostException;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriver;

public class NiceChrome extends NiceWebDriver {

	public NiceChrome(String optionArgs) throws UnknownHostException {
		super(optionArgs);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	protected MutableCapabilities makeBrowserOptions(String optionArgs) {
		ChromeOptions options = new ChromeOptions();
		//"--incognito ";
		//"--start-maximized ";
		options.addArguments(optionArgs);
		return options;
	}
		
		
	protected WebDriver getDriver(MutableCapabilities mutableCapabilities) {
		ChromeOptions options = (ChromeOptions) mutableCapabilities;
		WebDriver wd = (new ChromeDriver(options));
		return wd;
	}

}
