package test.skenvy.SeleniumNG.NiceWebDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import com.skenvy.SeleniumNG.DomainConstants;
import com.skenvy.SeleniumNG.DomainConstantsProperties;
import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriver;
import com.skenvy.SeleniumNG.NiceWebDriver.NiceWebDriverFactory;

public class NiceWebDriverFactoryTest {

	public NiceWebDriverFactoryTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		System.out.println((new DomainConstants("C:\\workspaces\\MYGITHUB\\SeleniumNG\\config.xml")).webDriverSystemPaths.get(DriverType.Chrome));
		//NiceWebDriver a = NiceWebDriverFactory.getFactory().getNiceWebDriver(DriverType.Chrome, "--incognito --start-maximized");
		//a.openWebPage("https://www.google.com");
	}

}
