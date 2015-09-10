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
@Root(name="acceptablevalue")
public class AcceptableValue {
    @Attribute
    public Integer id;
    @Element
    public String value;
    @Element
    public String defaultvalue;

    @Override
    public String toString(){
        return "\n[id:"+id+" value:"+value+" defaultvalue:"+defaultvalue+"]";
    }
}
