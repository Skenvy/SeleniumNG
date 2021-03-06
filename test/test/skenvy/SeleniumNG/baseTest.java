package test.skenvy.SeleniumNG;

import org.testng.Assert;
import org.testng.annotations.Test;

public class baseTest extends com.skenvy.SeleniumNG.baseTest {

	@Test
	public void CanOpenDefaultWebContextRoot() {
		unwrapNiceWebDriver().openWebPage("http://www.google.com");
		Assert.assertTrue(true);
	}

	@Override
	public String getPathToDomainConstantsConfig() {
		return System.getProperty("user.dir")+"\\config_test.xml";
	}

}
