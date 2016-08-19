/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.dataimport;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class DataCorrectionTest
	extends BaseTestCase
{

	public DataCorrectionTest()
	{
		this.description = "Data Correction";
	}

	@Override
	protected void runInternalTest()
	{
		
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		
//		Set<String> containsList = new HashSet<>(
//			Arrays.asList(
//"http://www.honeybeerobotics.com/wp-content/uploads/2014/03/Honeybee-Robotics-Microsat-CMGs.pdf",
//"http://www.epsondevice.com/webapp/docs_ic/DownloadServlet?id=ID002456",
//"http://bluecanyontech.com/wp-content/uploads/2013/06/BCT-Nano-Star-Tracker-datasheet-1.1.pdf",
//http://www.arduino.cc/en/Main/arduinoBoardMega2560
//http://beagleboard.org/static/BONESRM_latest.pdf?
//https://share.nasa.gov/teams/arc/mdc/parts/Part%20Catalogs/BRE_Brochure_2009.pdf
//https://share.nasa.gov/teams/arc/mdc/parts/Part%20Catalogs/BRE_Brochure_2009.pdf
//www.broadreachengineering.com
//http://www.moog.com/products/electronics/launch-vehicle-avionics/spacecraft-mechanism-controls/electronic-control-units/
//
//http://www.amptek.com/pdf/x123.pdf
//http://www.csbf.nasa.gov/documents/conventional/EC-500-20-D.C.pdf
//http://www.orbtronic.com/protected-3400mah-18650-li-ion-battery-panasonic-ncr18650B-orbtronichttp://industrial.panasonic.com/lecs/www-data/pdf2/ACA4000/ACA4000CE240.pdf
//http://industrial.panasonic.com/www-data/pdf2/ACA4000/ACA4000CE240.pdf
//http://www.my-batteries.net/camcorder-battery/canon-bp-930.htm
//http://www.globalstar.com/en/docs/gsp1720/GSP-1720%20Sell%20Sheet_FIN.pdf
//http://sunpowerinc.com/cryocoolers/ct.php
//http://sunpowerinc.com/cryocoolers/gen2.php
//			)
//		);
		
		List<Component> components = componentExample.findByExample();
		for (Component component : components) {
			
			
		}
		
		
	}
	
	
	
}
