/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.model;

import java.util.List;
import org.simpleframework.xml.*;

/**
 *
 * @author rnethercott
 */
@Root
public class Secondary {

    @Attribute
    public String display;

    @Attribute
    public String promptnotify;

    @ElementList(entry = "asset", inline = true, required = false)
    public List<RelationshipAsset> secondaryAssets;

    @Override
    public String toString() {

        String secondaryA = "";
        if (secondaryAssets != null) {
            for (RelationshipAsset item : secondaryAssets) {
                secondaryA += item.toString();
            }
        }

        String retStr = "{\n\tdisplay:" + display
                + "\n\tpromptnotify:" + promptnotify
                + "\n\tsecondaryAssets:" + secondaryA
                + "\n\t}";
        return retStr;

    }
}
