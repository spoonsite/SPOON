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
package edu.usu.sdl.openstorefront.sort;

import edu.usu.sdl.openstorefront.core.sort.RelevanceComparator;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * Tests the compare method for the RelevanceComparator
 * Use case: Collections.sort(views, new RelevanceComparator<>());
 * @author cyearsley
 */
@RunWith(Parameterized.class)
public class RelevanceComparatorTest
{
	private final ComponentSearchView view1, view2;
	private final int expectedOutput;
	
	public RelevanceComparatorTest (ComponentSearchView view1, ComponentSearchView view2, int expectedOutput)
	{
		this.view1 = view1;
		this.view2 = view2;
		this.expectedOutput = expectedOutput;
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> generateData()
	{
		// Setup
		ComponentSearchView nullComp = null;
		ComponentSearchView view1 = new ComponentSearchView();
		ComponentSearchView view2 = new ComponentSearchView();
		ComponentSearchView view3 = new ComponentSearchView();
		ComponentSearchView view4 = new ComponentSearchView();
		
		// Same name, different scores
		view1.setSearchScore(1);
		view1.setName("a");
		view2.setSearchScore(5);
		view2.setName("a");
		
		// Different name, same score
		view3.setSearchScore(10);
		view3.setName("x");
		view4.setSearchScore(10);
		view4.setName("y");
		
		// Test as many permutations as the RelevanceComparator considers.
		return Arrays.asList(new Object[][] {
			// compare nulls
			{nullComp, nullComp, 0},
			
			// compare not null and null views
			{view1, nullComp, 1},
			
			// compare null and not null views
			{nullComp, view1, -1},
			
			// compare same scores where arg1 name has higher alphabetical order than arg2 name
			{view3, view4, -1},
			
			// compare same scores where arg2 name has higher alphabetical order than arg1 name
			{view4, view3, 1},
			
			// compare same names where arg1 has lower scores than arg2
			{view1, view2, 1},
			
			// compare same names where arg2 has lower scores than arg1
			{view2, view1, -1},
			
			// compare different score and name (produces positive)
			{view4, view2, -1}
		});
	}
	
	@Test
	public void testCompare()
	{
		// Setup
		RelevanceComparator comparator = new RelevanceComparator();
		
		// Act
		int result = comparator.compare(view1, view2);
		
		// Assert
		assertEquals(expectedOutput, result);
	}
}
