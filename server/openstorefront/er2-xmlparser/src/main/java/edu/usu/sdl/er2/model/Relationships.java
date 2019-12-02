/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.model;

import org.simpleframework.xml.*;
/**
 *
 * @author rnethercott
 */
@Root(strict=false)
public class Relationships {

    @Element (name="Provides",required=false)
    public Provides provides;

    @Element (name="ResponsibleParty", required=false)
    public Provides responsibleParty;

    @Override
    public String toString() {

        String provStr = "";
        if (provides != null) {
            provStr += provides.toString();
        }

        String respParty = "";
        if (responsibleParty != null) {
            respParty += responsibleParty.toString();
        }

        String retStr = "{\n\tProvides:" + provStr + "\n\tResponsibleParty:" + respParty + "\n\t}";
        return retStr;

    }

}
