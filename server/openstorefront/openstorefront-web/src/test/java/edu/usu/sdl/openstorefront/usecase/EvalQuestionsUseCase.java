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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class EvalQuestionsUseCase
{

	@Test
	public void testParseQuestions() throws IOException
	{
		Document doc = Jsoup.parse(new File("C:\\temp\\newquestions.txt"), null);

		Map<String, ChecklistQuestion> questionMap = new HashMap<>();

		Element main = doc.getElementById("main-content");
		Elements tables = main.select("table");
		for (Element table : tables) {
			Elements rows = table.select("tr");
			for (Element row : rows) {
				Elements cols = row.select("td");
				Set<String> skipSet = new HashSet<>();
				skipSet.add("TECHNICAL QUESTIONS");
				skipSet.add("PROGRAMMATIC QUESTIONS");
				skipSet.add("");

				Map<String, String> evalMap = new HashMap<>();
				evalMap.put("Discoverable", "DIS");
				evalMap.put("Accessible", "ACC");
				evalMap.put("Documentation", "DOC");
				evalMap.put("Deployable", "DEP");
				evalMap.put("Usable", "USAB");
				evalMap.put("Error Handling", "ERR");
				evalMap.put("Integrable", "INT");
				evalMap.put("I/O Validation", "IO");
				evalMap.put("Testing", "TEST");
				evalMap.put("Monitoring", "MON");
				evalMap.put("Performance", "PER");
				evalMap.put("Scalability", "SC");
				evalMap.put("Security", "SEC");
				evalMap.put("Maintainability", "MAIN");
				evalMap.put("Community", "COM");
				evalMap.put("Change Management", "CM");
				evalMap.put("CA", "CA");
				evalMap.put("Licensing", "LIC");
				evalMap.put("Roadmap", "RM");
				evalMap.put("Willingness", "WIL");
				evalMap.put("Architecture Alignment", "AA");

//				for (int i = 0; i < cols.size(); i++) {
//					if (skipSet.contains(cols.get(i).text()) == false) {
//						ChecklistQuestion question = new ChecklistQuestion();
//						question.setQid(cols.get(i++).text());
//
//						String section = cols.get(i++).text();
//
//						question.setEvaluationSection(evalMap.get(section));
//						question.setQuestion(cols.get(i++).text());
//						question.setObjective(cols.get(i).text());
//						question.setScoreCriteria(cols.get(i).text());
//						questionMap.put(question.getQid(), question);
//
//					}
//				}
				for (Element col : cols) {
					System.out.println("col = " + col.text());
				}
			}
		}

		//StringProcessor.defaultObjectMapper().writeValue(new File("C:\\temp\\newQuestions.json"), questionMap.values());
	}

	@Test
	public void testParseNewQuestions() throws IOException
	{
		Document doc = Jsoup.parse(new File("C:\\temp\\newquestions.txt"), null);

		Map<String, ChecklistQuestion> questionMap = new HashMap<>();

		Set<String> skipSet = new HashSet<>();
		skipSet.add("Search Terms:");
		skipSet.add("URL:");
		skipSet.add("");

		Map<String, String> evalMap = new HashMap<>();
		evalMap.put("Discoverable", "DIS");
		evalMap.put("Accessible", "ACC");
		evalMap.put("Documentation", "DOC");
		evalMap.put("Deployable", "DEP");
		evalMap.put("Usable", "USAB");
		evalMap.put("Error Handling", "ERR");
		evalMap.put("Integrable", "INT");
		evalMap.put("I/O Validation", "IO");
		evalMap.put("Testing", "TEST");
		evalMap.put("Monitoring", "MON");
		evalMap.put("Performance", "PER");
		evalMap.put("Scalability", "SC");
		evalMap.put("Security", "SEC");
		evalMap.put("Maintainability", "MAIN");
		evalMap.put("Community", "COM");
		evalMap.put("Change Management", "CM");
		evalMap.put("CA", "CA");
		evalMap.put("Licensing", "LIC");
		evalMap.put("Roadmap", "RM");
		evalMap.put("Willingness", "WIL");
		evalMap.put("Architecture Alignment", "AA");

		boolean start = false;

		Element main = doc.getElementById("main-content");
		Elements tables = main.select("table");
		for (Element table : tables) {
			Elements rows = table.select("tr");
			for (Element row : rows) {
				Elements cols = row.select("td");

				for (int i = 0; i < cols.size(); i++) {
					if (!start && "1".equals(cols.get(i).text())) {
						start = true;
					}
					if (skipSet.contains(cols.get(i).text()) == false
							&& start) {
						ChecklistQuestion question = new ChecklistQuestion();
						question.setQid(cols.get(i++).text());

						String section = cols.get(i++).text();

						question.setEvaluationSection(evalMap.get(section));
						question.setQuestion(cols.get(i++).text());
						question.setObjective(cols.get(i).text());
						//question.setScoreCriteria(cols.get(i).text());
						questionMap.put(question.getQid(), question);

					}
				}
//				for (Element col : cols) {
//					System.out.println("col = " + col.text());
//				}
			}
		}

		StringProcessor.defaultObjectMapper().writeValue(new File("C:\\temp\\newQuestions.json"), questionMap.values());
	}

}
