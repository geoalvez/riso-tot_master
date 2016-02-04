package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import br.edu.ufcg.copin.riso.tot.constants.ConstantsRisoTOT;
import br.edu.ufcg.copin.riso.tot.dao.DBPediaDAO;

public class RisoTotMain {


	private static String conteudo = "";
	
	private static String[] listaTodasAsFrasesTexto;
	private static ArrayList<String> listaFrasesTemporaisTexto = new ArrayList<String>();
	
	
	private static ArrayList<String> listaEntidadesTexto = new ArrayList<String>();
	
	private static void carregaEntidadestexto(String caminhoArquivo){
		try {
			FileReader fr = new FileReader(caminhoArquivo);
			BufferedReader br = new BufferedReader( fr );
			while( br.ready() ){
				String linha = br.readLine();
				listaEntidadesTexto.add(linha);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void verificaExpressaoTemporalDentroDeVirgula(String frase){
		if (frase.contains(":/:")){
			String[] lista = frase.split(":/:");
			for (int i = 0; i < lista.length; i++){
				String oracao = lista[i];
				if (oracao.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
					if (!(oracao.trim()).startsWith("and")){
						String[] listaPalavrasOracao = oracao.split(" ");
						ArrayList<String> listaAux = new ArrayList<String>();
						String data = "";
						for (int j = 0; i < listaPalavrasOracao.length; j++){
							String palavra = listaPalavrasOracao[i];
							
							if (!palavra.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
								listaAux.add(palavra);
							}else{
								data = palavra;
							}
						}
						
						System.out.println("["+data+"] ---> [");
						for (int x = 0; x < listaAux.size(); x++){
							System.out.println(listaAux.get(x));
						}
						System.out.println("]");
					}
				}
			}
		}
	}
	
	private static boolean verificaVerbo(String stringAVerificar){
		for (int i = 0; i < ConstantsRisoTOT.LISTA_VERBOS().size(); i++){
			if (stringAVerificar.contains(ConstantsRisoTOT.LISTA_VERBOS().get(i))){
				return true;
			}
		}
		return false;
		
	}
	
	private static boolean verificaParavraEntidade (String palavra){
		
		if (listaEntidadesTexto.contains(palavra)){
			return true;
		}
		return false;
	}
	private static void criaRelacionamentos(){
		for (int i = 0; i < listaFrasesTemporaisTexto.size(); i++){
			String[] listaAux = listaFrasesTemporaisTexto.get(i).split(ConstantsRisoTOT.TAG_RISO_TEMPORAL);
			int contaOcorrenciasTemporais = listaAux.length-1;
			switch (contaOcorrenciasTemporais) {
			case 1:
				
				String[] listaPalavras = listaFrasesTemporaisTexto.get(i).split(" ");
				
				String tagTemporal = "";
				ArrayList<String> listaPalavrasRelacionadas = new ArrayList<String>();
				for (int j = 0; j < listaPalavras.length; j++){
					if(listaPalavras[j].contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
						tagTemporal = listaPalavras[j]; 
					}else{
						if (verificaParavraEntidade(listaPalavras[j])){
							listaPalavrasRelacionadas.add(listaPalavras[j]);
						}
						
					}
				}
				System.out.print(tagTemporal+";");
				for (int x = 0; x < listaPalavrasRelacionadas.size(); x++){
					System.out.print(listaPalavrasRelacionadas.get(x) + " ");
				}
				System.out.println("");
				break;

			default:
				
				String frase = listaFrasesTemporaisTexto.get(i);
				
				if (frase.contains(":/:")){
					String[] lista = frase.split(":/:");
					for (int b = 0; b < lista.length; b++){
						String oracao = lista[b];
						if (oracao.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
							if (!(oracao.trim()).startsWith("and")){
								if (oracao.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
									String[] listaPalavrasOracao = oracao.split(" ");
									ArrayList<String> listaEntidadesRelacionadas = new ArrayList<String>();
									String expressaoTemporal = "";
									for (int j = 0; j < listaPalavrasOracao.length; j++){
										String palavra = listaPalavrasOracao[j];
										
										if (!palavra.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
											listaEntidadesRelacionadas.add(palavra);
										}else{
											expressaoTemporal = palavra;
										}
									}
									
									if (!listaEntidadesRelacionadas.isEmpty()){
										System.out.print(expressaoTemporal+";");
										for (int x = 0; x < listaEntidadesRelacionadas.size(); x++){
////											String dataNormalizada = DBPediaDAO.buscaDataNormalizada(listaEntidadesRelacionadas.get(x));
//											if (listaEntidadesTexto.contains(dataNormalizada));
//											System.out.print(dataNormalizada + " ");
										}
										System.out.println("");
										
										frase = frase.replace(oracao, "");
										
										
									}
									
								}
								
							}
						}
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				if (frase.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
					String[] listaPalavras1 = frase.split(" ");
					HashMap<String, ArrayList<String>> mapa = new HashMap<String, ArrayList<String>>();
					ArrayList<String> listaPalavrasAux = new ArrayList<String>();
					ArrayList<String> listaDeDatas = new ArrayList<String>(); 
					
					boolean leuData = false;
					String dataAtual = "";
					boolean leuVerbo = false;
					for (int x = 0; x < listaPalavras1.length; x++){
						String palavra = listaPalavras1[x];
						if (palavra.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
							if (listaPalavrasAux.size() == 0){
								listaDeDatas.add(palavra);
								dataAtual = palavra;
								mapa.put(dataAtual, new ArrayList<String>());
								leuData = true;								
							}else{
								if (leuVerbo){
									dataAtual = palavra;
									listaPalavrasAux.add(palavra);
									mapa.put(dataAtual, (ArrayList<String>)listaPalavrasAux.clone());
									listaPalavrasAux = new ArrayList<String>();
									leuData = false;
									leuVerbo = false;
									
								}else{
									dataAtual = palavra;
									listaPalavrasAux.add(palavra);									
								}
								
							}
						}else{
							if (!leuData){
								listaPalavrasAux.add(palavra);
							}else{
								if (verificaVerbo(palavra)){
									leuVerbo = true;
								}
								listaPalavrasAux.add(palavra);
							}
						}
					}
					
					if (!listaPalavrasAux.isEmpty()){
						mapa.put(dataAtual, listaPalavrasAux);
					}
					for (int o = 0;o < listaDeDatas.size(); o++){
						ArrayList<String> palavras = mapa.get(listaDeDatas.get(o));
						if (!palavras.isEmpty()){
							System.out.print(listaDeDatas.get(o)+";");
							for (int h = 0; h < palavras.size(); h++){
								System.out.print(palavras.get(h) + " ");
							}
							System.out.println("");
								
						}
					}
					
					
				}
				
				break;
			}
			
		}
		
	}
	private static void extracaoDeFrases(){
		listaTodasAsFrasesTexto = conteudo.split("\\./.");
		System.out.println("Texto possui ["+listaTodasAsFrasesTexto.length+"] frases.");
		for (int i = 0; i < listaTodasAsFrasesTexto.length; i++){
			String frase = listaTodasAsFrasesTexto[i];
			if (frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL) > 0){
				listaFrasesTemporaisTexto.add(listaTodasAsFrasesTexto[i]);
			}
		}
		System.out.println("Foram encontrados ["+listaFrasesTemporaisTexto.size()+"] frases temporais.");
	}
	
	
	private static void leituraDoArquivo(String nomeArquivo) throws FileNotFoundException{
        System.out.println("Lendo arquivo ["+nomeArquivo+"].");
		BufferedReader br = new BufferedReader(new FileReader("./"+nomeArquivo));   
        try {
			while(br.ready()){   
				conteudo = conteudo.concat(br.readLine()).concat(" ");   
			}
	        br.close();   		
		} catch (IOException e) {
			System.out.println("Erro ao ler o arquivo ["+nomeArquivo+"]. ");
			e.printStackTrace();
		}   
	}
	
	public static void main(String[] args) {
		
//		String testando = "george marcelo george marcelo";
//		String[] lista = testando.split("or.*[^or.*e]e");
//		System.out.println(lista);
		
        System.out.println("RISO-TT 1.0 - georgealves@copin.ufcg.edu.br");
		String nomeArquivo = "";
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Digite o nome do arquivo que sera lido: ");
		nomeArquivo = "saida4.txt";
		// nomeArquivo = scanner.nextLine();
		
		
		try {
			leituraDoArquivo(nomeArquivo);
			extracaoDeFrases();
			criaRelacionamentos();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
