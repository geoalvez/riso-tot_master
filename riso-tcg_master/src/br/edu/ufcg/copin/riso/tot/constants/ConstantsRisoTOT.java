package br.edu.ufcg.copin.riso.tot.constants;

import java.util.ArrayList;

public class ConstantsRisoTOT {

	public static final String TAG_RISO_TEMPORAL = "</RISOTime>";
	public static final String TAG_RISO_TEMPORAL_INI = "<RISOTime>";
	
	public static final String TAG_BEFORE = "<RISOTime type=D-EMT-Adv> X < ";
	public static final String TAG_AFTER = "<RISOTime type=D-EMT-Adv> X > ";
	
	public static final String TAG_DE = "<RISOTime_type=DE>";
	public static final String TAG_GENERICA = "<RISOTime_type=";
	
	public static ArrayList<String> LISTA_VERBOS(){
		ArrayList<String> listaRetorno = new ArrayList<String>();
		
		listaRetorno.add("/VB");
		listaRetorno.add("/VBD");
		listaRetorno.add("/VBG");
		listaRetorno.add("/VBN");
		listaRetorno.add("/VBP");
		listaRetorno.add("/VBZ");
		
		return listaRetorno;
	}
	
	public static ArrayList<String> LISTA_VERBOS_SEM_BARRA(){
		ArrayList<String> listaRetorno = new ArrayList<String>();
		
		listaRetorno.add("VB");
		listaRetorno.add("VBD");
		listaRetorno.add("VBG");
		listaRetorno.add("VBN");
		listaRetorno.add("VBP");
		listaRetorno.add("VBZ");
		
		return listaRetorno;
	}
	
	public static final String CAMINHO_ARQUIVO_ENTIDADES_TEMPORALIZADAS = "C:\\workspace_mestrado\\RisoTemporalTagger_soprasubir\\padroes\\datas_especiais.xml";
	
	public static final String[] MESES = {"", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};

	public static final String REGEX_MESES = "(january|february|march|april|may|june|july|august|september|october|november|december)";
	public static final String REGEX_DIAS = "\\d{4}";

	public static final String REGEX_MES_ANO = "^" + REGEX_MESES + " " + REGEX_DIAS + "$";
	
	
}
