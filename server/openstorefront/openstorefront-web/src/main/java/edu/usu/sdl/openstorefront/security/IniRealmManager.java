/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.security;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.core.api.ExternalUserManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;

/**
 *
 * @author dshurtleff
 */
public class IniRealmManager
		extends ExternalUserManager
{

	public IniRealmManager()
	{
	}

	@Override
	public UserRecord findUser(String username)
	{
		UserRecord record = null;

		List<String> usernames = new ArrayList<>();
		usernames.add(username);
		List<UserRecord> records = findUsers(usernames);
		if (records.isEmpty() == false) {
			record = records.get(0);
		}
		return record;
	}

	@Override
	public List<UserRecord> findUsers(List<String> users)
	{
		List<UserRecord> userRecords = new ArrayList<>();

		Set<String> userSet = new HashSet<>(users);

		//load config
		Ini ini = new Ini();
		try (InputStream in = new FileInputStream(FileSystemManager.getConfig("shiro.ini"))) {
			ini.load(in);
			Section section = ini.getSection("users");
			if (section != null) {
				for (String user : section.keySet()) {
					if (userSet.contains(user)) {
						UserRecord userRecord = new UserRecord();
						userRecord.setUsername(user);
						userRecords.add(userRecord);
					}
				}
			}
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to read shiro.ini file.", "Check config path and permissions", ex);
		}

		return userRecords;
	}

}
