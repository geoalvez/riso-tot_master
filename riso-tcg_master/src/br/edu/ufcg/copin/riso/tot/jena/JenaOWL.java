package br.edu.ufcg.copin.riso.tot.jena;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaOWL {

	public static OntModel lerOntologia(String url) {
		/*
		 * Utilizando uma ontologia existente...
		 */
		OntModel model;
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		model.read(url);
		return model;
	}

	public static OntModel criaOntologia(String sujeito, String predicado,
			String objeto, OntModel base) {

		if (base == null) {
			base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		}

		String baseUri = "http://lsi.dsc.ufcg.edu.br/riso.owl#";

		OntClass suj = base.createClass(baseUri + sujeito.trim() + " "
				+ predicado.trim());
		OntClass obj = base.createClass(baseUri + objeto.trim() + " "
				+ predicado.trim());
		suj.addSuperClass(obj);

		return base;
	}

	public static OntModel criaOntologia(String sujeito, OntModel base) {

		if (base == null) {
			base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		}

		String baseUri = "http://lsi.dsc.ufcg.edu.br/riso.owl#";
		OntClass suj = base.createClass(baseUri + sujeito.trim());

		return base;
	}

	public static void main(String args[]) {

		JenaOWL jena2 = new JenaOWL();
		OntModel model2;
		// try {
		try {
			model2 = jena2.lerOntologia(new File("C:\\Users\\george.marcelo.alves\\git\\riso-master_git\\riso-master_ok\\reuters\\results\\111.txt").toURL().toString());
		} catch (Exception e) {
			model2 = jena2.criaOntologia("/c/en/Speedboat", null);
		}
		model2 = jena2.criaOntologia("/c/en/Speedboat", "/r/HasDate ","/c/en/01-02-2015", model2);
		// model2.write(System.out);
		FileOutputStream out2;
		try {
			File arquivoSaida = new File("C:\\Users\\george.marcelo.alves\\Documents\\Speedboat_ontoMinimal.txt");
			arquivoSaida.createNewFile();

			out2 = new FileOutputStream(arquivoSaida);
			model2.write(out2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		/*
		 * Criando uma ontologia...
		 */
		// JenaOWL jena = new JenaOWL();
		// OntModel model = jena.criaOntologia("/c/en/jaguar_xf", "/r/IsA",
		// "/c/en/car",null);
		// jena.criaOntologia("/c/en/jaguar_xf", "/r/IsA ",
		// "/c/en/mean_of_transportation",model);
		// jena.criaOntologia("/c/en/jaguar_xf", "/r/IsA",
		// "/c/en/jaguar",model);
		//
		// jena.criaOntologia("/c/en/jaguar_xf", "/r/IsA",
		// "/c/en/product",model);
		//
		// jena.criaOntologia("/c/en/car", "/r/IsA",
		// "/c/en/mean_of_transportation",model);
		// jena.criaOntologia("/c/en/car", "/r/IsA", "/c/en/product",model);
		// jena.criaOntologia("/c/en/mean_of_transportation",
		// "/r/IsA","/c/en/product" ,model);
		// jena.criaOntologia("/c/en/door", "/r/PartOf", "/c/en/car",model);
		// jena.criaOntologia("/c/en/window", "/r/PartOf", "/c/en/door",model);
		// jena.criaOntologia("/c/en/auto", "/r/UsedFor", "/c/en/drive",model);
		//
		// jena.criaOntologia("/c/en/jaguar_cat", "/r/IsA",
		// "/c/en/Panthera",model);
		// // george
		// jena.criaOntologia("/c/en/jaguar_cat", "/r/HasDate",
		// "/c/en/01-02-2015",model);
		// // george
		// jena.criaOntologia("/c/en/Panthera", "/r/MemberOf",
		// "/c/en/mammal",model);
		//
		// jena.criaOntologia("/c/en/vertebrado", "/r/IsA",
		// "/c/en/animal",model);
		// jena.criaOntologia("/c/en/jaguar_cat", "/r/IsA",
		// "/c/en/mammal",model);
		// jena.criaOntologia("/c/en/jaguar_cat", "/r/IsA",
		// "/c/en/vertebrado",model);
		// jena.criaOntologia("/c/en/jaguar_cat", "/r/IsA",
		// "/c/en/jaguar",model);
		//
		// jena.criaOntologia("/c/en/mammal", "/r/IsA", "/c/en/animal",model);
		// jena.criaOntologia("/c/en/mammal", "/r/IsA",
		// "/c/en/vertebrado",model);

	}
}
