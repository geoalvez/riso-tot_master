package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ufcg.copin.riso.tot.entities.Entidade;

public class IncluiMarcacaoEntidadesRisoVTD {
	
	public static void main(String[] args) {
		String caminhoArquivoEntidades = "C:\\Users\\george.marcelo.alves\\Dropbox\\SaidaMontyExtractor\\";
		String caminhoArquivoOriginal = "C:\\Users\\george.marcelo.alves\\Dropbox\\Documentos_MoutyLingua\\";
		String caminhoArquivoSaida = "C:\\Users\\george.marcelo.alves\\Dropbox\\RisoTT_Saida\\";
		String EXTESAO = ".txt";
		
		System.out.println("Nome do arquivo de entidades: ");
		Scanner sc1 = new Scanner(System.in); 
		String nomeArquivo = sc1.next();
		String nomeArquivoOriginal = nomeArquivo;

		
		//equanto houver mais linhas
		try {
			File arquivoEntidades = new File (caminhoArquivoEntidades + nomeArquivo + "_entities" + EXTESAO);
			
			FileReader fr = new FileReader( arquivoEntidades );
			BufferedReader br = new BufferedReader( fr );
			
			ArrayList<Entidade> entidades = new ArrayList<Entidade>(); 
					
			while( br.ready() ){
				
				//lê a proxima linha
				String linha = br.readLine();
				Entidade entidade = new Entidade (linha);
				entidades.add(entidade);
			
			}
			
			br.close();
			fr.close();
			
			
			Object[] entidadeAux = entidades.toArray();
			
			Arrays.sort(entidadeAux);
			
			ArrayList<Entidade> entidadesOrdenadas = new ArrayList<Entidade>(); 
			for (int i = 0; i < entidadeAux.length; i++){
				
				entidadesOrdenadas.add((Entidade)entidadeAux[i]);
			}
			

			File arquivoOriginal = new File (caminhoArquivoOriginal + nomeArquivoOriginal  + EXTESAO);
			
			FileReader fr1 = new FileReader( arquivoOriginal );
			BufferedReader br1 = new BufferedReader( fr1 );
			
			String conteudoArquivo = "";
			while( br1.ready() ){
				
				//lê a proxima linha
				String linha = br1.readLine();
				conteudoArquivo = conteudoArquivo.concat(linha + System.getProperty("line.separator"));
			
			}
			
			
		    String regex = "(\\[(\\w+)\\])+";  
		    Pattern p = Pattern.compile(regex);  
		    Matcher m = p.matcher(conteudoArquivo);  
		    while (m.find()) {
		    	String teste = m.group(2);
		    	System.out.println(teste);
//		    	if (teste.equals("[note 1]")){
//		    		System.out.println();
//		    	}
		    	conteudoArquivo = conteudoArquivo.replaceAll("\\["+teste+"\\]", "");		    			
		    }
		    
	    	conteudoArquivo = conteudoArquivo.replaceAll("\\[", "");		    			
	    	conteudoArquivo = conteudoArquivo.replaceAll("\\]", "");		    			
		    
			
			
			
			br1.close();
			fr1.close();
			for (int i = 0; i < entidadesOrdenadas.size(); i++){
				
				if (entidadesOrdenadas.get(i).toString().equals("May.")){
					System.out.println();
				}
				int index = conteudoArquivo.indexOf(entidadesOrdenadas.get(i).toString());
				String nome = "";
				
				if (entidadesOrdenadas.get(i).toString().equals("name")){
					System.out.println();
				}
				
//				if (index != -1){
//					int countMax = 20;
//					int count = 0;
//					for (int j = index; j >= 0; j--){
//						count++;
//						// index, index + entidadesOrdenadas.get(i).toString().length()
//						if (conteudoArquivo.substring(j, j+1).equals("[")){
//							taMarcado = true;
//						}else if (conteudoArquivo.substring(j, j+1).equals("]")){
//							taMarcado = false;
//							break;
//						}
//						
//						if (count == countMax){
//							break;
//						}
//					}
//
//					if (!taMarcado){
//						countMax = 20;
//						count = 0;
//						for (int j = index; j < conteudoArquivo.length(); j++){
//							count ++;
//							if (conteudoArquivo.substring(j, j+1).equals("]")){
//								taMarcado = true;
//							}else if (conteudoArquivo.substring(j, j+1).equals("[")){
//								taMarcado = false;
//								break;
//							}
//							if (count == countMax){
//								break;
//							}
//						}
//						
//						
//					}
//					nome = conteudoArquivo.substring(index, index + entidadesOrdenadas.get(i).toString().length());					
//				}
//				
				if (entidadesOrdenadas.get(i).toString().indexOf(".") == -1){
					conteudoArquivo = conteudoArquivo.replaceAll(entidadesOrdenadas.get(i).toString(), "["+entidadesOrdenadas.get(i).toStringSpecial()+"]");										
				}
				
			}
			
		
				
			conteudoArquivo = conteudoArquivo.replaceAll(Entidade.CARACTER_ESPECIAL, "");
			System.out.println(caminhoArquivoSaida +nomeArquivo + "_marcado" + EXTESAO);
			File arquivoSaida = new File(caminhoArquivoSaida +nomeArquivo + "_marcado_aux"  + EXTESAO);
			arquivoSaida.createNewFile();
			
			FileWriter fw = new FileWriter( arquivoSaida );
			BufferedWriter bw = new BufferedWriter(fw);
			String[] linhas = conteudoArquivo.split("\\.");
			for (int i = 0; i < linhas.length; i++){
				bw.write(linhas[i] + System.getProperty("line.separator"));				
			}
			
			System.out.println("Fim!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
