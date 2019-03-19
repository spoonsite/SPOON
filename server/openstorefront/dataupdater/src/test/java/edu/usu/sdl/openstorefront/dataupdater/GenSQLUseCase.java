/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.dataupdater;

import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class GenSQLUseCase
{

	@Test
	public void genSQL()
	{
		String keys[] = new String[]{
			"TEMP#-MINONC",
			"TEMP#-MAXONC",
			"TEMP#-MINOFFC",
			"TEMP#-MAXOFFC",
			"RANDOMWALKDEGHR^0#5",
			"PIXELS#X",
			"PIXELS#Y",
			"F-NUMBERF#",
			"MAX#PROPELLANTLOADKG",
			"MAX#PROPELLANTLOADLITERS",
			"MAX#FILLFRACTION"
		};

		for (String key : keys) {
			String keyClean = key.replace("#", "").replace("^", "");
			//String sql = "update AttributeType set attributeType='" + keyClean + "' where attributeType='" + key + "';";
			//String sql = "update ComponentAttribute set componentAttributePk.attributeType='" + keyClean + "' where componentAttributePk.attributeType = '" + key + "'";

			String sql = "update AttributeCode set attributeCodePk.attributeType='" + keyClean + "' where attributeCodePk.attributeType = '" + key + "'";

			System.out.println(sql);

		}

	}

}
