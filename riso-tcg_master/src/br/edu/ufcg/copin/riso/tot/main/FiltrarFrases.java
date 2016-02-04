package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class FiltrarFrases {

	public static void main(String[] args) {
		String[] arquivos = {"C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_Napoleon_final.txt",
							"C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_WW1_final.txt",
							"C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_IraqWar_final.txt",
							};
		
		HashMap<String, int[]> mapa = new HashMap<String, int[]>();
		int[] frasesNapoleon = {1,2,3,4,8,9,10,11,13,14,15,16,17,18,19,20,21,22,23,24,25,27,29,30,31,32,33,36,37,38,39,40,43,44,46,47,48,50,53,54,55,61,63,64,70,72,73,74,75,76,82,84};
		mapa.put("saidaUnificada_Napoleon_final.txt", frasesNapoleon);
		
		int[] frasesWW1 = {4,5,6,7,9,12,13,16,21,22,25,30,31,32,39,50,52,53,54,57,58,118,132,134,136,139};
		mapa.put("saidaUnificada_WW1_final.txt", frasesWW1);
		
		int[] frasesIraqWar = {1,5,8,9,18,20,23,24,28,30,31,32,34,36,37,44,47,50,51,57,58,60,64,74,79,80,82,83,84,88,89,90,92,94,95,96} ;
		mapa.put("saidaUnificada_IraqWar_final.txt", frasesIraqWar);
		
		
		for (int i = 0; i < arquivos.length; i++){
			
			BufferedReader brReader;
			File arquivo = new File(arquivos[i]);
			
			int[] frasesSelecionadas = mapa.get(arquivo.getName());
			
			System.out.println("Arquivo: " +arquivo.getName());
			String saida = "";
			try {
				brReader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivos[i]),"Cp1252"));
				int contador = 0;
				while (brReader.ready()){
					contador++;
					String linha = brReader.readLine();
					System.out.println("Linha: " + linha);
					boolean achou = false;
					for (int j = 0; j < frasesSelecionadas.length; j++){
						if (contador == frasesSelecionadas[j]){
							achou = true;
							System.out.println("Selecionada!!!");
							break;
						}
					}
					
					if (achou){
						saida = saida.concat(linha) + System.getProperty("line.separator");
					}
					
				}
				brReader.close();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\"+arquivo.getName().replace(".txt","")+"_filtro.txt"));
				out.write(saida);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
	}
}
