/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

/**
 *
 * @author dshurtleff
 */
public class Util
{
	public static String blankIfNull(String in)
	{
		if (in == null) {
			return "";
		}
		return in;
	}
}
