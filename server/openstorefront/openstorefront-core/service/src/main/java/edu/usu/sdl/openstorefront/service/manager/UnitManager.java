/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import org.jscience.economics.money.Currency;

/**
 *
 * @author rfrazier
 */
public class UnitManager
		implements Initializable
{
	private static AtomicBoolean started = new AtomicBoolean(false);
		
	public static void init()
	{

	}

	public static void cleanup()
	{

	}


	@Override
	public void initialize()
	{
		UnitFormat instance = UnitFormat.getInstance();
		instance.alias(NonSI.BYTE.times(1.0E3),  "KB");
		instance.alias(NonSI.BYTE.times(1.0E6),  "MB");
		instance.alias(NonSI.BYTE.times(1.0E9),  "GB");
		instance.alias(NonSI.BYTE.times(1.0E12), "TB");
		instance.alias(NonSI.BYTE.times(1.0E15), "PB");
		instance.alias(SI.BIT.times(1.0E3),      "Kb");
		instance.alias(SI.BIT.times(1.0E6),      "Mb");
		instance.alias(SI.BIT.times(1.0E9),      "Gb");
		instance.alias(SI.BIT.times(1.0E12),     "Tb");
		instance.alias(SI.BIT.times(1.0E15),     "Pb");
		instance.alias(SI.BIT.times(1.0E3).divide(SI.SECOND), "kbps");
		instance.alias(SI.BIT.times(1.0E6).divide(SI.SECOND), "Mbps");
		instance.alias(SI.BIT.divide(SI.SECOND), "bps");
		instance.alias(NonSI.REVOLUTION.divide(NonSI.MINUTE), "rpm");
		// instance.alias(NonSI.REVOLUTION.divide(NonSI.MINUTE), "RPM");
		instance.alias(NonSI.INCH, "inch");
		instance.alias(NonSI.SECOND_ANGLE, "arcsec");
		instance.alias(NonSI.MINUTE_ANGLE, "arcmin");
		instance.alias(NonSI.G, "Grav"); // G is the Gauss, need to disambiguate
		instance.alias(NonSI.G.times(1.0E-3), "mGrav");
		instance.alias(NonSI.GAUSS.times(1.0E-3), "mG");
		instance.alias(Unit.valueOf("°"), "deg");
		instance.alias(NonSI.DECIBEL.times(SI.WATT), "dBW");
		instance.alias(NonSI.DECIBEL.times(SI.WATT.times(1.0E-3)), "dBm"); // dB milli-Watts
		instance.alias(NonSI.DECIBEL, "dBi");
		instance.alias(NonSI.DECIBEL, "dBd");
		instance.alias(NonSI.DECIBEL, "dBc");
		instance.alias(NonSI.DECIBEL, "dBC");
		instance.alias(NonSI.HOUR, "hr");
		instance.alias(NonSI.HOUR, "hour");
		instance.alias(Currency.USD, "$");
		instance.alias(SI.SECOND.pow(-1).times(1.0E6), "MIPS");
		instance.alias(NonSI.G.pow(2).divide(SI.HERTZ), "grms"); // G_rms root mean square of power spectral density
		instance.alias(NonSI.G.pow(2).divide(SI.HERTZ), "Grms");
		instance.alias(SI.MICRO(SI.METER), "micron");
		instance.alias(SI.MICRO(SI.METER), "μm");
		instance.alias(SI.MICRO(SI.GRAM), "μg");
		instance.alias(SI.MICRO(SI.NEWTON), "μN");

		started.set(true);
	}

	@Override
	public void shutdown()
	{
		started.set(false);		
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}	
}
