package br.edu.ufcg.copin.riso.tot.utils;

import java.util.HashMap;

public class RisoTcgUtil {
	
	public static String formataString (String entrada){
		String saida = "";
		
		
		saida = entrada.replace("%C3%A1", "á");
		saida = saida.replace("%C3%81", "Á");
		
		saida = saida.replace("_", " ");
		return saida;
	}
	
	public static String removeUrl (String url){
		String saida = "";
		
		int posicao = url.lastIndexOf("/");
		saida = url.substring(posicao+1);
		
		return saida;
	}

	public static String inverteParaDDMMYYYY (String dataFormatada){
		String saidaInvertida = "";
		
		String[] campos = dataFormatada.split("-");
		
		if (campos.length == 3){
			saidaInvertida = campos[2] + "-" + campos[1] +"-"+campos[0];
		}
		
		
		return saidaInvertida;
	}
	
	public static String[] MESES = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMPER", "OCTOBER", "NOVEMBER", "DECEMBER"};
	
	public static String JANUARY = "01";
	public static String FEBRUARY = "02";
	public static String MARCH = "03";
	public static String APRIL = "04";
	public static String MAY = "05";
	public static String JUNE = "06";
	public static String JULY = "07";
	public static String AUGUST = "08";
	public static String SEPTEMBER = "09";
	public static String OCTOBER = "10";
	public static String NOVEMBER = "11";
	public static String DECEMBER = "12";
	
	public static String getNumMes(String mesAsString){
		HashMap<String, String> hashMesNumMes= new HashMap<String, String>();
		
		String numMes = mesAsString.toUpperCase();
				
		hashMesNumMes.put(MESES[0], JANUARY);
		hashMesNumMes.put(MESES[1], FEBRUARY);
		hashMesNumMes.put(MESES[2], MARCH);
		hashMesNumMes.put(MESES[3], APRIL);
		hashMesNumMes.put(MESES[4], MAY);
		hashMesNumMes.put(MESES[5], JUNE);
		hashMesNumMes.put(MESES[6], JULY);
		hashMesNumMes.put(MESES[7], AUGUST);
		hashMesNumMes.put(MESES[8], SEPTEMBER);
		hashMesNumMes.put(MESES[9], OCTOBER);
		hashMesNumMes.put(MESES[10], NOVEMBER);
		hashMesNumMes.put(MESES[11], DECEMBER);
		
		numMes = hashMesNumMes.get(numMes);
				
		return numMes;
		
	}
	
	
	
	
}
