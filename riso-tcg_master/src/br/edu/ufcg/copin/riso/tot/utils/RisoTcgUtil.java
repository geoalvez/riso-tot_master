package br.edu.ufcg.copin.riso.tot.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ufcg.copin.riso.tot.constants.ConstantsRisoTOT;

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
		if (campos[0].length() == 2 || campos[0].length() == 3){
			return dataFormatada;
		}else{
			if (campos.length == 3){
				saidaInvertida = campos[2] + "-" + campos[1] +"-"+campos[0];
			}
		}
		
		
		return saidaInvertida;
	}
	
	public static String[] MESES = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
	
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
	
	public static String retiraCaracteres(String caracteres){
		
		caracteres = caracteres.replaceAll(":", "");
		caracteres = caracteres.replaceAll(",", "");
		caracteres = caracteres.replaceAll(";", "");
		caracteres = caracteres.replaceAll("$", "");
		caracteres = caracteres.replaceAll("%", "");
		caracteres = caracteres.replaceAll("@", "");
		caracteres = caracteres.replaceAll("\\(", "");
		caracteres = caracteres.replaceAll("\\)", "");
		caracteres = caracteres.replaceAll("\\[", "");
		caracteres = caracteres.replaceAll("\\]", "");
		caracteres = caracteres.replaceAll("\\{", "");
		caracteres = caracteres.replaceAll("\\}", "");
		caracteres = caracteres.replaceAll("\\\\", "");
		caracteres = caracteres.replaceAll("/", "");

		caracteres = caracteres.replaceAll("\\?", "");
		caracteres = caracteres.replaceAll("\\!", "");

		return caracteres;
	}
	

	public static boolean naoPossuiCaracteresEspeciais(String caracteres){
		caracteres = caracteres.toLowerCase();
		
		//String dataAux = data.split(" < X < ")[0];
		
		
		Pattern p = Pattern.compile("[A-Za-z0-9]+");
		Matcher m = p.matcher(caracteres);
		
		return m.matches();
		
	}
	
	public static boolean ehTagRisoES (String caracteres){
		
		String[] lista = {"CC","CD","DT","EX","FW","IN","JJ","JJR","JJS","LS","MD","NN","NNS","NNP","NNPS","PDT","POS","PRP","PRPS","RB","RBR","RBS","RP","TO","UH","VB","VBD","VBG","VBN","VBP","VBZ","WDT","WP","WPS","WRB", "NP"};
		
		for (int i =0; i < lista.length; i++){
			if (caracteres.equals(lista[i])){
				return true;
			}
			
		}
		return false;
		
	}
	
	public static int contaOcorrencias(String data, String caractere){
		
		int total=0;
		for (int i=0;i<=data.length()-1;i++) {
			if (data.substring(i,i+1).equalsIgnoreCase(caractere))
				total=total+1;
		}
		return total;		
	}
	
	public static String incluiZero (String data){
		
		if (contaOcorrencias(data, "-") == 2 || contaOcorrencias(data, "-") == 4){
			data = data.replace("-1-", "-01-");
			data = data.replace("-2-", "-02-");
			data = data.replace("-3-", "-03-");
			data = data.replace("-4-", "-04-");
			data = data.replace("-5-", "-05-");
			data = data.replace("-6-", "-06-");
			data = data.replace("-7-", "-07-");
			data = data.replace("-8-", "-08-");
			data = data.replace("-9-", "-09-");

			return data;
			
		}
		
		return data;
		
	}
	
	
	
	
}
