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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.service.io.HelpImporter;
import edu.usu.sdl.openstorefront.storage.model.HelpSection;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class HelpUseCase
{

	@Test
	public void testHelpProcessing()
	{
		HelpImporter helpImporter = new HelpImporter();
		List<HelpSection> helpSections = helpImporter.processHelp(HelpImporter.class.getResourceAsStream("/userhelp.md"));
		helpSections.forEach(help -> {
			System.out.println("Title: " + help.getTitle());
			System.out.println("Admin: " + help.getAdminSection());
			System.out.println("Content: " + help.getContent());
			System.out.println("");
		});

	}

}
