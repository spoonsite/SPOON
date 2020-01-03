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
@Root(strict = false)
public class Asset {

    @Attribute
    public Integer id;

    @Attribute(name = "load-date")
    public String loadDate;

    @Element(name = "asset-type")
    public AssetType assetType;

    @Element(name = "mandatory-data")
    public MandatoryData mandatoryData;

    @Element(name = "custom-data", required = false)
    public CustomData customData;

    @Element(name = "admin-data", required = false)
    public AdminData adminData;

    @Override
    public String toString() {

        String cusData = "";
        String admData = "";

        if (customData != null) {
            cusData = customData.toString();
        }

        if (adminData != null) {
            admData = adminData.toString();
        }

        String retStr = "{\nid:" + id + "\nloadDate:" + loadDate + "\nassetType:" + assetType.toString()
                + "\nmandatoryData:" + mandatoryData.toString() + "\ncustomData:" + cusData + "\nadminData:" + admData
                + "\n}";

        return retStr;

    }
}
