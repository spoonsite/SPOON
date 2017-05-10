/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.selenium.util;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author dshurtleff
 */
public class WebDriverUtil
{

	private static final Logger LOG = Logger.getLogger(WebDriverUtil.class.getName());

	private List<WebDriver> drivers = new ArrayList<>();
	private String server;
	private Properties properties = new Properties();

	public WebDriverUtil()
	{
		init();
	}

	private void init()
	{
		//load config
		DriverPathLoaderFix.loadDriverPaths(null);

		File propertyFile = FileSystemManager.getConfig("testconfig.properties");
		try (InputStream in = new FileInputStream(propertyFile)) {
			properties.load(in);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to load Configuration file.");
		}
		server = properties.getProperty("test.server", "http://store-accept.usu.di2e.net/openstorefront/");
		String driverKey = properties.getProperty("test.drivers", "EDGE");
		try {
			WebDriverType webDriverType = WebDriverType.valueOf(driverKey.toUpperCase().trim());
			drivers.addAll(Arrays.asList(webDriverType.getDrivers()));
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException("Unable to find web driver or it not supported.", "Check configuration.  Key test.drivers = " + driverKey);
		}
		for (WebDriver driver : drivers){
			setBrowserSize(driver);
			setBrowserPosition(driver);
			setImplicitWaitTimeSeconds(driver);
		}
	}

	private void setBrowserSize(WebDriver driver) {
		String width = properties.getProperty("browser.size.width", "1200");
		String height = properties.getProperty("browser.size.height", "1080");
		driver.manage().window().setSize(new Dimension(Convert.toInteger(width), Convert.toInteger(height)));	
	}
	
	private void setBrowserPosition(WebDriver driver) {
		String xPos = properties.getProperty("browser.position.x.fromTopLeft", "0");
		String yPos = properties.getProperty("browser.position.x.fromTopLeft", "0");
		driver.manage().window().setPosition(new Point(Convert.toInteger(xPos), Convert.toInteger(yPos)));
	}
	
	private void setImplicitWaitTimeSeconds(WebDriver driver) {
		String iWaitSecs = properties.getProperty("implicitWait.default.seconds","10");
		driver.manage().timeouts().implicitlyWait(Convert.toInteger(iWaitSecs), TimeUnit.SECONDS);
	}
	
	
	/**
	 * This will close the stream when done.
	 *
	 * @param in
	 */
	public void saveReportArtifact(InputStream in)
	{
		//TODO: Save artifact to report directory
	}

	public String getPage(String page)
	{
		return server + page;
	}

	public List<WebDriver> getDrivers()
	{
		return drivers;
	}

	public void closeDrivers()
	{
		for (WebDriver driver : drivers) {
			if (driver instanceof FirefoxDriver) {
				driver.close();
			} else {
				driver.quit();
			}
		}
	}

	public String getServer()
	{
		return server;
	}

}
