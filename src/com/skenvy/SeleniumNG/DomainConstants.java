package com.skenvy.SeleniumNG;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import com.skenvy.SeleniumNG.NiceWebDriver.DriverType;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

/***
 * Handles storing Domain Constants read from an external configuration file.
 * The config keys {@code <entry key="WebDriverSystemPaths.<Browser>"></entry>}
 * generic on {@code <Browser>} are used by the NiceWebDriverFactory, while the
 * "local" and "test" prefaced configuration values are utilised by some of the
 * NiceWebDriver's more narrowly scoped "open web page" methods, which
 * encapsulate the access of some site that is intended as the primary focus of
 * the test suite that has subclassed the baseTest or invokes the
 * NiceWebDriverFactory to substantiate tests in another testing framework.
 */
public final class DomainConstants {
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Static Vars
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Stores the properties read in from the config file.
	 */
	private static final Properties properties = new Properties();
	
	/***
	 * Maps a DriverExtension enum value K to the path Domain Constant required
	 * for the System.setProperty(...,K)
	 */
	public static HashMap<DriverType,String> webDriverSystemPaths = new HashMap<DriverType,String>();
	
	public static Local local = null;
	
	public static Test test = null;
	
	public static TestSleeps testSleeps = null;
	
	public static int seleniumNodeCount = 0;
	
	public static SeleniumNode[] seleniumNodes = null;
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Invoke the constructor to overwrite the static fields
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Construct the properties and assign the values to the dictionaries
	 * returned by this class.
	 * @param configFilePath
	 * @throws IOException
	 */
	public DomainConstants(String configFilePath) throws IOException{
		loadPropertiesFromXMLConfigFile(configFilePath);
		assignWebDriverSystemPaths();
		local = assignLocal();
		test = assignTest();
		testSleeps = assignTestSleeps();
		seleniumNodeCount = assignSeleniumNodeCount();
		assignSeleniumNodes();
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Assign the vars
 */
///////////////////////////////////////////////////////////////////////////////
	
	private void loadPropertiesFromXMLConfigFile(String configFilePath) throws IOException{
		FileInputStream in = new FileInputStream(configFilePath);
		properties.loadFromXML(in);
		in.close();
	}
	
	private void assignWebDriverSystemPaths() {
		for(DriverType dt : DriverType.values()) {
			webDriverSystemPaths.put(dt,properties.getProperty(DomainConstantsProperties.webDriverSystemPathPerDriverType.get(dt)));
		}
	}
	
	private Local assignLocal() {
		int environPort = getPropertyInteger(DomainConstantsProperties.LocalEnvironPort,DomainConstantsProperties.defaultEnvironPort);
		String webContextRoot = properties.getProperty(DomainConstantsProperties.LocalWebContextRoot,DomainConstantsProperties.defaultWebContextRoot);
		int waitSeconds = getPropertyInteger(DomainConstantsProperties.LocalWaitSeconds,DomainConstantsProperties.defaultWaitSeconds);
		int instantiationMaxRetry = getPropertyInteger(DomainConstantsProperties.LocalInstantiationMaxRetry,DomainConstantsProperties.defaultInstantiationMaxRetry);
		return new Local(environPort,webContextRoot,waitSeconds,instantiationMaxRetry);
	}
	
	private Test assignTest() {
		String environIP = properties.getProperty(DomainConstantsProperties.TestEnvironIP,DomainConstantsProperties.defaultEnvironIP);
		int environPort = getPropertyInteger(DomainConstantsProperties.TestEnvironPort,DomainConstantsProperties.defaultEnvironPort);
		String webContextRoot = properties.getProperty(DomainConstantsProperties.TestWebContextRoot,DomainConstantsProperties.defaultWebContextRoot);
		int waitSeconds = getPropertyInteger(DomainConstantsProperties.TestWaitSeconds,DomainConstantsProperties.defaultWaitSeconds);
		int instantiationMaxRetry = getPropertyInteger(DomainConstantsProperties.TestInstantiationMaxRetry,DomainConstantsProperties.defaultInstantiationMaxRetry);
		return new Test(environIP,environPort,webContextRoot,waitSeconds,instantiationMaxRetry);
	}
	
	private TestSleeps assignTestSleeps() {
		int milliSecondsBetweenKeyStrokes = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondsBetweenKeyStrokes, DomainConstantsProperties.defaultMilliSecondsBetweenKeyStrokes);
		int milliSecondsBeforeClick = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondsBeforeClick, DomainConstantsProperties.defaultMilliSecondsBeforeClick);
		int milliSecondsAfterClick = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondsAfterClick, DomainConstantsProperties.defaultMilliSecondsAfterClick);
		int milliSecondSimulateInteractivePause = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondSimulateInteractivePause, DomainConstantsProperties.defaultMilliSecondSimulateInteractivePause);
		int milliSecondDurationOfSuccessMessage = getPropertyInteger(DomainConstantsProperties.TestSleepsMilliSecondDurationOfSuccessMessage, DomainConstantsProperties.defaultMilliSecondDurationOfSuccessMessage);
		return new TestSleeps(milliSecondsBetweenKeyStrokes,milliSecondsBeforeClick,milliSecondsAfterClick,milliSecondSimulateInteractivePause,milliSecondDurationOfSuccessMessage);
	}
	
	private int assignSeleniumNodeCount() {
		return getPropertyInteger(DomainConstantsProperties.SeleniumNodeCount, DomainConstantsProperties.defaultSeleniumNodeCount);
	}
	
	private void assignSeleniumNodes() throws MalformedURLException {
		seleniumNodes = new SeleniumNode[seleniumNodeCount];
		for(int k = 1; k <= seleniumNodeCount; k++) {
			String localNodeString = properties.getProperty(DomainConstantsProperties.SeleniumNodeLocal+k);
			boolean localNode = (localNodeString.equalsIgnoreCase("True"));
			String dcString = properties.getProperty(DomainConstantsProperties.SeleniumNodeDriverType+k);
			DriverType dt = DriverType.valueOf(dcString);
			URL nodeUrl = null;
			if(!localNode) {
				nodeUrl = new URL(properties.getProperty(DomainConstantsProperties.SeleniumNodeRemoteURL+k));
			} 
			seleniumNodes[k-1] = new SeleniumNode(localNode, nodeUrl, dt);
		}
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Public inner classes
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Returns static string components of urls; for destringification
	 */
	public static class UrlConstants {
		public final static String HTTP = "http";
		public final static String HTTPS = "https";
	}
	
	public static class Local {
		
		public final int environPort;
		public final String webContextRoot;
		public final int waitSeconds;
		public final int instantiationMaxRetry;
		
		/***
		 * Constructs a Local object, accessed through the DomainConstants
		 * static member "local"
		 * @param environPort
		 * @param webContextRoot
		 * @param waitSeconds
		 * @param instantiationMaxRetry
		 */
		protected Local(int environPort, String webContextRoot, int waitSeconds, int instantiationMaxRetry){
			this.environPort = environPort;
			this.webContextRoot = webContextRoot;
			validateIntIsGreaterThan(waitSeconds,DomainConstantsProperties.LocalWaitSeconds,0);
			this.waitSeconds = waitSeconds;
			validateIntIsGreaterThan(instantiationMaxRetry,DomainConstantsProperties.LocalInstantiationMaxRetry,0);
			this.instantiationMaxRetry = instantiationMaxRetry;
		}
		
	}
	
	public static class Test {
		
		public final String environIP;
		public final int environPort;
		public final String webContextRoot;
		public final int waitSeconds;
		public final int instantiationMaxRetry;
		
		/***
		 * Constructs a Test object, accessed through the DomainConstants
		 * static member "test"
		 * @param environIP
		 * @param environPort
		 * @param webContextRoot
		 * @param waitSeconds
		 * @param instantiationMaxRetry
		 */
		protected Test(String environIP, int environPort, String webContextRoot, int waitSeconds, int instantiationMaxRetry) {
			this.environIP = environIP;
			this.environPort = environPort;
			this.webContextRoot = webContextRoot;
			validateIntIsGreaterThan(waitSeconds,DomainConstantsProperties.TestWaitSeconds,0);
			this.waitSeconds = waitSeconds;
			validateIntIsGreaterThan(instantiationMaxRetry,DomainConstantsProperties.TestInstantiationMaxRetry,0);
			this.instantiationMaxRetry = instantiationMaxRetry;
		}
		
	}
	
	/***
	 * Values used to indicate millisecond thread sleeps that should be used to
	 * mock a running test as a "demonstration" by faking a slower speed.
	 */
	protected static class TestSleeps {

		/***
		 * How many milliseconds to pause between keystrokes
		 */
		public final int MilliSecondsBetweenKeyStrokes;
		/***
		 * How many milliseconds to pause before a mouse click
		 */
		public final int MilliSecondsBeforeClick;
		/***
		 * How many milliseconds to pause after a mouse click
		 */
		public final int MilliSecondsAfterClick;
		/***
		 * How many milliseconds to pause to simulate a web page interaction
		 */
		public final int MilliSecondSimulateInteractivePause;
		/***
		 * How many milliseconds to pause after a success message
		 */
		public final int MilliSecondDurationOfSuccessMessage;
		
		/***
		 * Constructs a TestSleeps object, accessed through the DomainConstants
		 * static member "testSleeps"
		 * @param milliSecondsBetweenKeyStrokes
		 * @param milliSecondsBeforeClick
		 * @param milliSecondsAfterClick
		 * @param milliSecondSimulateInteractivePause
		 * @param milliSecondDurationOfSuccessMessage
		 */
		protected TestSleeps(int milliSecondsBetweenKeyStrokes, int milliSecondsBeforeClick, int milliSecondsAfterClick, int milliSecondSimulateInteractivePause, int milliSecondDurationOfSuccessMessage) {
			validateIntIsGreaterThan(milliSecondsBetweenKeyStrokes,DomainConstantsProperties.TestSleepsMilliSecondsBetweenKeyStrokes,0);
			this.MilliSecondsBetweenKeyStrokes = milliSecondsBetweenKeyStrokes;
			validateIntIsGreaterThan(milliSecondsBeforeClick,DomainConstantsProperties.TestSleepsMilliSecondsBeforeClick,0);
			this.MilliSecondsBeforeClick = milliSecondsBeforeClick;
			validateIntIsGreaterThan(milliSecondsAfterClick,DomainConstantsProperties.TestSleepsMilliSecondsAfterClick,0);
			this.MilliSecondsAfterClick = milliSecondsAfterClick;
			validateIntIsGreaterThan(milliSecondSimulateInteractivePause,DomainConstantsProperties.TestSleepsMilliSecondSimulateInteractivePause,0);
			this.MilliSecondSimulateInteractivePause = milliSecondSimulateInteractivePause;
			validateIntIsGreaterThan(milliSecondDurationOfSuccessMessage,DomainConstantsProperties.TestSleepsMilliSecondDurationOfSuccessMessage,0);
			this.MilliSecondDurationOfSuccessMessage = milliSecondDurationOfSuccessMessage;
		}
		
	}
	
	/***
	 * Handles the instantiation of several concurrently remote or consecutive
	 * local instances of subclasses of the baseTest
	 */
	protected static class SeleniumNode{
		
		/***
		 * Is the SeleniumNode a local instance or a remote connection?
		 */
		public final boolean local;
		/***
		 * If the SeleniumNode is a remote connection, what is the node's URL?
		 */
		public final URL nodeUrl;
		/***
		 * What is the DriverType?
		 */
		public final DriverType dt;
		
		/***
		 * Constructs a SeleniumNode to match configuration input. Is it local
		 * or remote? If it's remote, what is the node url? In either case,
		 * what is the DriverType?
		 * @param local
		 * @param nodeUrl
		 * @param dt
		 */
		public SeleniumNode(boolean local, URL nodeUrl, DriverType dt) {
			this.local = local;
			this.nodeUrl = nodeUrl;
			this.dt = dt;
		}
		
	}
	
///////////////////////////////////////////////////////////////////////////////
/*
 * Minifying helpers
 */
///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * Searches for a string property in the config file and integer casts its
	 * value, or assigns a default integer
	 * @param searchString
	 * @param defaultInt
	 * @return
	 */
	private int getPropertyInteger(String searchString, int defaultInt) {
		return Integer.parseInt(properties.getProperty(searchString,String.valueOf(defaultInt)));
	}
	
	/***
	 * Throw a ValueException if a field is less than some value it must be
	 * validated as being greater than
	 * @param field
	 * @param name
	 * @param greaterThanThis
	 */
	protected static void validateIntIsGreaterThan(int field, String name, int greaterThanThis) {
		if(field <= greaterThanThis) {
			throw new ValueException(name+" must be greater than "+greaterThanThis);
		}
	}
	
}
