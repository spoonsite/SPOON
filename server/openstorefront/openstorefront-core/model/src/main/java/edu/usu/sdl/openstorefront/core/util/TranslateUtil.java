/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * This class handles translating lookup code into descriptions
 *
 * @author dshurtleff
 */
public class TranslateUtil
{

	private static final Logger log = Logger.getLogger(TranslateUtil.class.getName());

	public static <T extends LookupEntity> String translate(Class<T> lookupClass, String code)
	{
		String translated = code;
		if (StringUtils.isNotBlank(code)) {
			Service serviceProxy = ServiceProxyFactory.getServiceProxy();
			LookupEntity lookupEntity = serviceProxy.getLookupService().getLookupEnity(lookupClass, code);
			if (lookupEntity != null) {
				translated = lookupEntity.getDescription();
			} else {
				log.log(Level.WARNING, MessageFormat.format("Unable to find: {0} in lookup: {1}", new Object[]{code, lookupClass.getName()}));
			}
		}
		return translated;
	}

}
