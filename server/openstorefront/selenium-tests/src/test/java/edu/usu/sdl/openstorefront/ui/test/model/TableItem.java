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
package edu.usu.sdl.openstorefront.ui.test.model;

/**
 *
 * @author besplin
 */
public class TableItem
{
	private int rRow = -1;
	private int rCol = -1;

	public TableItem(int rRow, int rCol)
	{
		this.rRow = rRow;
		this.rCol = rCol;
	}

	public int getrRow()
	{
		return rRow;
	}

	public void setrRow(int rRow)
	{
		this.rRow = rRow;
	}

	public int getrCol()
	{
		return rCol;
	}

	public void setrCol(int rCol)
	{
		this.rCol = rCol;
	}
	
}
