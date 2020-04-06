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
package edu.usu.sdl.openstorefront.core.util;

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.view.AttributeUnitView;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.unit.Unit;
import org.apache.commons.lang3.StringUtils;
import org.jscience.physics.amount.Amount;

/**
 *
 * @author dshurtleff
 */
public class UnitConvertUtil
{

	private static final Logger LOG = Logger.getLogger(UnitConvertUtil.class.getName());

	private UnitConvertUtil()
	{
	}

	/**
	 * Converts the value from the BaseUnit to the UserUnit
	 *
	 * @param baseUnitUOM
	 * @param userUnitUOM
	 * @param originalValue
	 * @return AttributeUnitView with the conversion or null if can't convert
	 */
	public static AttributeUnitView convertBaseUnitToUserUnit(String baseUnitUOM, String userUnitUOM, String originalValue)
	{
		AttributeUnitView unitView = null;

		if (StringUtils.isNotBlank(userUnitUOM)
				&& StringUtils.isNotBlank(baseUnitUOM)) {
			try {
				// get the conversion factor between the baseUnit and the preferredUnit
				Unit userUnit = Unit.valueOf(userUnitUOM);
				Unit baseUnit = Unit.valueOf(baseUnitUOM);
				Amount factor = Amount.valueOf(1, baseUnit).to(userUnit);
				if (userUnit == null || baseUnit == null || factor == null) {
					LOG.log(Level.WARNING,
							"Unable to generate conversion factor between base unit and user unit.\nBase Unit: {0}. User Unit: {1}. Original Value: {2}",
							new Object[]{baseUnitUOM, userUnitUOM, originalValue});
					return null;
				}
				unitView = new AttributeUnitView(userUnitUOM, BigDecimal.valueOf(factor.getEstimatedValue()));

				//Convert user unit to base (multiply)
				final char seperator = new DecimalFormatSymbols().getDecimalSeparator(); // TODO: grab the locale specific seperator
				final String regex = "-?[^\\d" + seperator + "]";
				String cleaned = originalValue.replaceAll(regex, "");
				if (cleaned == null) {
					LOG.log(Level.WARNING,
							"Original value to convert did not contain any decimal digits.\nBase Unit: {0}. User Unit: {1}. Original Value: {2}",
							new Object[]{baseUnitUOM, userUnitUOM, originalValue});
					return null;
				}
				BigDecimal originalValueNumber = Convert.toBigDecimal(cleaned);
				if (originalValueNumber == null) {
					LOG.log(Level.WARNING,
							"Conversion of original value failure.\nBase Unit: {0}. User Unit: {1}. Original Value: {2}. Cleaned value {3}",
							new Object[]{baseUnitUOM, userUnitUOM, originalValue, cleaned});
					return null;
				}
				unitView.setConvertedValue(originalValueNumber.multiply(unitView.getConversionFactor()));

			} catch (IllegalArgumentException e) {
				LOG.log(Level.WARNING, 
						"Unable to process unit conversion factors for: {0} and {1}\n{2}", 
						new Object[]{userUnitUOM, baseUnitUOM, e.toString()});
			}
		}

		return unitView;
	}

	/**
	 * Converts the value from the UserUnit to the BaseUnit
	 *
	 * @param baseUnitUOM
	 * @param userUnitUOM
	 * @param originalValue
	 * @return the converted value or original value if it can't convert
	 */
	public static String convertUserUnitToBaseUnit(String baseUnitUOM, String userUnitUOM, String originalValue)
	{
		try {
			Unit userUnit = Unit.valueOf(userUnitUOM);
			Unit baseUnit = Unit.valueOf(baseUnitUOM);
			@SuppressWarnings("unchecked")
			Amount factor = Amount.valueOf(1, baseUnit).to(userUnit);

			BigDecimal originalValueNumber = Convert.toBigDecimal(originalValue);
			BigDecimal conversionFactor = BigDecimal.valueOf(factor.getEstimatedValue());

			BigDecimal newValue = originalValueNumber.divide(conversionFactor, MathContext.DECIMAL64);
			return newValue.stripTrailingZeros().toPlainString();
		} catch (Exception e) {
			return originalValue;
		}
	}

}
