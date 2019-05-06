package com.skenvy.SeleniumNG.NiceWebDriver;

import java.net.UnknownHostException;

import org.openqa.selenium.remote.RemoteWebDriver;

public class NiceChrome extends NiceWebDriver {

	public NiceChrome() throws UnknownHostException {
		// TODO Auto-generated constructor stub
	}

	public NiceChrome(int waitSeconds) throws UnknownHostException {
		super(waitSeconds);
		// TODO Auto-generated constructor stub
	}

	public NiceChrome(String chromeDriverFilePath, int waitSeconds) throws UnknownHostException {
		super(chromeDriverFilePath, waitSeconds);
		// TODO Auto-generated constructor stub
	}

	public NiceChrome(String chromeDriverFilePath, int waitSeconds, RemoteWebDriver rwd) throws UnknownHostException {
		super(chromeDriverFilePath, waitSeconds, rwd);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
