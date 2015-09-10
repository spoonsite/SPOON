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
@Root(strict=false)
public class Primary {

    @Attribute
    public String display;

    @Attribute
    public String promptnotify;

    @ElementList(entry = "asset", inline = true, required = false)
    public List<RelationshipAsset> primaryAssets;

    @Override
    public String toString() {

        String primaryA = "";
        if (primaryAssets != null) {
            for (RelationshipAsset item : primaryAssets) {
                primaryA += item.toString();
            }
        }

        String retStr = "{\n\tdisplay:" + display
                + "\n\tpromptnotify:" + promptnotify
                + "\n\tprimaryAssets:" + primaryA
                + "\n\t}";
        return retStr;

    }
}
