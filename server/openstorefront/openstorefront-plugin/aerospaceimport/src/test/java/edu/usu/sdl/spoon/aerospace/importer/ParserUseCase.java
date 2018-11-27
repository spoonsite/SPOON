/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.spoon.aerospace.importer;

import edu.usu.sdl.spoon.aerospace.importor.model.Services;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author dshurtleff
 */
public class ParserUseCase
{

	@Test
	public void parseTest()
	{
		Serializer serializer = new Persister();
		try {
			Services services = serializer.read(Services.class, new File("c:/test/aerospace/products_091118.xml"));

			Set<String> categories = new HashSet<>();
			Set<String> specCats = new HashSet<>();
			Set<String> attributeTypes = new HashSet<>();
			services.getProducts().forEach(p -> {
				if (p.getProductRevision().getProductFamily() != null) {
					if (p.getProductRevision().getProductFamily().getClassification() != null) {
						categories.add(p.getProductRevision().getProductFamily().getClassification().get(0).getCategoryName());
						//System.out.println(p.getLongName() + " - " + p.getProductRevision().getProductFamily().getClassification().get(0).getCategoryName());
					}
				}

				if (p.getProductRevision().getProductType() != null) {
					if (p.getProductRevision().getProductType().getClassification() != null
							&& !p.getProductRevision().getProductType().getClassification().isEmpty()) {
						specCats.add(p.getProductRevision().getProductType().getClassification().get(0).getCategoryName());

					}
				}

				if (p.getProductRevision().getShape() != null) {
					p.getProductRevision().getShape().getFloatFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " (" + f.getUnitAbbr() + ")");
					});
					p.getProductRevision().getShape().getIntFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " (" + f.getUnitAbbr() + ")");
					});
					p.getProductRevision().getShape().getTextFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " -> " + f.getValue());
					});
				}

				if (p.getProductRevision().getSpecs() != null) {
					p.getProductRevision().getSpecs().getFloatFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " (" + f.getUnitAbbr() + ")");
					});
					p.getProductRevision().getSpecs().getIntFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " (" + f.getUnitAbbr() + ")");
					});
					p.getProductRevision().getSpecs().getTextFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " -> " + f.getValue());
					});
				}

				if (p.getProductRevision().getAdditional() != null) {
					p.getProductRevision().getAdditional().getFloatFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " (" + f.getUnitAbbr() + ")");
					});
					p.getProductRevision().getAdditional().getIntFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " (" + f.getUnitAbbr() + ")");
					});
					p.getProductRevision().getAdditional().getTextFeatures().forEach(f -> {
						attributeTypes.add(f.getName() + " -> " + f.getValue());
					});
				}

			});

			List<String> sortedCats = new ArrayList<>(categories);
			sortedCats.sort(null);

//			sortedCats.forEach(cat -> {
//				System.out.println(cat);
//			});
			sortedCats = new ArrayList<>(specCats);
			sortedCats.sort(null);
//
//			sortedCats.forEach(cat -> {
//				System.out.println(cat);
//			});

			attributeTypes.forEach(attr -> {
				System.out.println(attr);
			});

		} catch (Exception ex) {
			Logger.getLogger(ParserUseCase.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
