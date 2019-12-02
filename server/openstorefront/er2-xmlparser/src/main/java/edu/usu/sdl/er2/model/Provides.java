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
public class Provides {

    @Attribute
    public Integer id;

    @Attribute
    public String direction;

    @Attribute(name="handled-by-plugin")
    public String handledByPlugin;

    @Attribute
    public String extractable;

    @Attribute
    public Integer activestatus;

    @Attribute
    public String immutable;

    @Element
    public Primary primary;

    @Element
    public Secondary secondary;

    @Override
    public String toString() {

        String retStr = "{\n\tid:" + id + "\n\tdirection:" + direction + "\n\thandledByPlugin:" + handledByPlugin
                + "\n\textractable:" + extractable + "\n\tactivestatus:" + activestatus + "\n\timmutable:" + immutable
                + "\n\tprimary:" + primary.toString() + "\n\tsecondary:" + secondary.toString() + "\n\t}";
        return retStr;

    }
}
