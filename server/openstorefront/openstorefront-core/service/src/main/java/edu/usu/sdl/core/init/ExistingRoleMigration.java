/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research
 * Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

 package edu.usu.sdl.core.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.usu.sdl.openstorefront.core.entity.SecurityRole;

/**
 *
 * @author cyearsley
 */

public class ExistingRoleMigration
		extends ApplyOnceInit
{
	private static final Logger LOG = Logger.getLogger(SecurityInit.class.getName());

	public ExistingRoleMigration()
	{
		super("Existing-Role-Migration");
	}

	@Override
	protected String internalApply()
	{
		SecurityRole exampleRole = new SecurityRole();
		List<String> defaultRoles = Arrays.asList(SecurityRole.DEFAULT_GROUP, SecurityRole.GUEST_GROUP,
				SecurityRole.ADMIN_ROLE, SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE);
		exampleRole.findByExample().forEach(role -> {
			// if the role is not a default role, remove it's permissions
			if (!defaultRoles.contains(role.getRoleName())) {
				role.setPermissions(new ArrayList<>());
				role.save();
			}
		});
		LOG.log(Level.CONFIG, "Wiped the permissions of non-default roles");

		return "Clear existing roles' permissions";
	}

	@Override
	public int getPriority()
	{
		return 14;
	}
}
