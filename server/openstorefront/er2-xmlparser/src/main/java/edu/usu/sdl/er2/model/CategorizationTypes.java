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
@Root
public class CategorizationTypes {
    
    @Element(name="AssetLifecycleStage",required=false)
    public CatType assetLifecycleStage;
    
    @Element(name="CollectionTaxonomy",required=false)
    public CatType collectionTaxonomy;
    
    @Element(name="ContentType",required=false)
    public CatType contentType;
    
    @Element(name="DI2E",required=false)
    public CatType DI2E;
    
    @Element(name="EnterpriseCompetencyModel",required=false)
    public CatType enterpriseCompetencyModel;
    
    @Element(name="JARM_ESL",required=false)
    public CatType JARM_ESL;
    
    @Element(name="IntelligenceType",required=false)
    public CatType intelligenceType;
    
    @Element(name="MarketplaceCategory",required=false)
    public CatType marketplaceCategory;
    
    @Element(name="NonStateActors",required=false)
    public CatType nonStateActors;
    
    @Element(name="AssetFunction",required=false)
    public CatType assetFunction;
    
    @Element(name="TST",required=false)
    public CatType TST;
    
    @Element(name="Topics",required=false)
    public CatType topics;
    
    @Override
    public String toString(){
        
        String retStr="{\n\tAssetLifecyleStage:"+assetLifecycleStage.toString()+
                      "\n\tCollectionTaxonomy:"+collectionTaxonomy.toString()+
                      "\n\tContentType:"+contentType.toString()+
                      "\n\tDI2E:"+DI2E.toString()+
                      "\n\tEnterpriseCompetencyModel:"+enterpriseCompetencyModel.toString()+
                      "\n\tJARM_ESL:"+JARM_ESL.toString()+
                      "\n\tIntelligenceType:"+intelligenceType.toString()+
                      "\n\tMarketplaceCategory:"+marketplaceCategory.toString()+
                      "\n\tNonStateActors:"+nonStateActors.toString()+
                      "\n\tAssetFunction:"+assetFunction.toString()+
                      "\n\tTST:"+TST.toString()+
                      "\n\tTopics:"+topics.toString()+
                      "\n\t}";
        return retStr;
        
    }
    
}
