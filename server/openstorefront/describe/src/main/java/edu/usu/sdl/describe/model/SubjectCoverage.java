/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.SubjectCoverageConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
@Convert(SubjectCoverageConverter.class)
public class SubjectCoverage
{	
	private List<String> keywords = new ArrayList<>();

	public SubjectCoverage()
	{
	}

	public List<String> getKeywords()
	{
		return keywords;
	}

	public void setKeywords(List<String> keywords)
	{
		this.keywords = keywords;
	}
	
}
