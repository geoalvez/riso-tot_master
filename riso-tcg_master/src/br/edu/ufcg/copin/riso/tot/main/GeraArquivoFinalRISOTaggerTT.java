package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeraArquivoFinalRISOTaggerTT {

	public static String removeBranco(String stringARemover){
		
		stringARemover = stringARemover.replaceAll(" ", "_");

		return stringARemover;
		
	}
	public static void main(String[] args) {
        String saida = "";
        String conteudo = "";
        ArrayList<String> arrayDeDatas = new ArrayList<String>();
		
//        String arquivoGeradoPeloRisoTT = "RussoJap.txt";
        String arquivoGeradoPeloRisoTT = "Napoleon_final.txt";
        
//		String saidaArquivoParaPosTagger = "RussoJap.txt";
		String saidaArquivoParaPosTagger = "Napoleon_final.txt";
		
//		String arquivoGeradoPosTaggerParaLeituraDoTCG = "RussoJap.txt";
		String arquivoGeradoPosTaggerParaLeituraDoTCG = "Napoleon_final.txt";
		

//		String arquivoSaidaTCG = "saidaUnificada_RussoJap.txt";
		String arquivoSaidaTCG = "saidaUnificada_Napoleon_final.txt";

        try {
			/* Lendo arquivo com a saida do RISO-TT */
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\george.marcelo.alves\\Dropbox\\RisoTT_Saida\\"+arquivoGeradoPeloRisoTT));   
				while(br.ready()){   
					conteudo = conteudo.concat(br.readLine()).concat(" ");   
				}
		        br.close();   		
		} catch (IOException e) {
			System.out.println("Erro ao ler o arquivo. ");
			e.printStackTrace();
		}
		
		/* Substituindo as marcacoes de tags temporais pela contante #DATA_RISO,
		 * para geracao do arquivo que sera ligo pelo POS TAGGER */
		boolean lendoData = false;
		int contaOcorrenciasMenor = 0;
		int contaOcorrenciasMaior = 0;
		String data = "";
		for (int i = 0; i < conteudo.length(); i ++){
			String caracter = conteudo.substring(i, i+1);
			if (caracter.equals("<")){
				contaOcorrenciasMenor ++;
				if (contaOcorrenciasMenor == 1){
					saida = saida + "#DATA_RISO";
					lendoData = true;					
				}
				data = data + caracter;
			}else{
				if (!lendoData){
					saida = saida + caracter;
				}else{
					data = data + caracter;
					if (caracter.equals(">")){
						contaOcorrenciasMaior++;
						if (contaOcorrenciasMaior == 2){
							lendoData = false;
							contaOcorrenciasMaior = 0;
							contaOcorrenciasMenor = 0;
							arrayDeDatas.add(data);
							
							data = "";
						}
					}
				}
				
			}
			
		}
		/* Geracao do arquivo que sera lido pelo POS Tagger */
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter("C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\" + saidaArquivoParaPosTagger));
			out.write(saida);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        /* Lendo arquivo gerado pelo POS Tagger (o pos tagger devera ter sido executado para prosseguir para o passo a seguir ) */
		conteudo = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\george.marcelo.alves\\Dropbox\\MountyTagger_saida\\"+arquivoGeradoPosTaggerParaLeituraDoTCG));   
				while(br.ready()){   
					conteudo = conteudo.concat(br.readLine()).concat(" ");   
				}
		        br.close();   		
		} catch (IOException e) {
			System.out.println("Erro ao ler o arquivo. ");
			e.printStackTrace();
		}

		int j = 0;
		while(j < 10000){ /* Checar este loop */
			conteudo = conteudo.replaceFirst("DATA_RISO/NNP", "DATA_RISO");
			j ++;
		}

		
		/* Este arquivo nao eh utilizado. Foi criado apenas para verificacao dos resultados */
		try {
			out = new BufferedWriter(new FileWriter(".//saidatestando.txt"));
			out.write(conteudo);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		/* Geracao do arquivo final, contendo as marcacoes do POS TAGGER e o RISO-TT */
		for (int i = 0; i < arrayDeDatas.size(); i ++){
			
			conteudo = conteudo.replaceFirst("DATA_RISO", removeBranco(arrayDeDatas.get(i)));
		}
		
		try {
			out = new BufferedWriter(new FileWriter("C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\"+arquivoSaidaTCG));
			out.write(conteudo);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
