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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 *
 * @author dshurtleff
 */
public enum WebDriverType
{
	ALL(),
	FIREFOX(),
	IE(),
	EDGE(),
	CHROME();

	private WebDriverType()
	{
	}

	public WebDriver[] getDrivers()
	{
		WebDriver drivers[] = null;
		switch (this) {
			case ALL:
				drivers = new WebDriver[]{
					new FirefoxDriver(),
					new InternetExplorerDriver(),
					new EdgeDriver(),
					new ChromeDriver()
				};
				break;
			case FIREFOX:
				drivers = new WebDriver[]{new FirefoxDriver()};
				break;
			case IE:
				drivers = new WebDriver[]{new InternetExplorerDriver()};
				break;
			case EDGE:
				drivers = new WebDriver[]{new EdgeDriver()};
				break;
			case CHROME:
				drivers = new WebDriver[]{new ChromeDriver()};
				break;
		}
		return drivers;
	}

}
