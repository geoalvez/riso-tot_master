package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Arquivo {

	public static void main(String[] args) {

		String path = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_RussoJap_linhas.txt";

		//
		BufferedReader buffRead;
		try {
			buffRead = new BufferedReader(new FileReader(path));
			ArrayList<String> linhas = new ArrayList<String>();
			while (true) {

				String linha = buffRead.readLine();

				if (null == linha) {
					break;
				} else {
					linhas.add(linha);
				}
			}
			buffRead.close();

			
			path = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida_linhas\\";			
			
			int i = 0;
			for (String linhaAux : linhas){
				i++;
				String pathAux = path + i + ".txt";
				File arquivo = new File(pathAux);
				arquivo.createNewFile();
				BufferedWriter buffWrite = new BufferedWriter(new FileWriter(pathAux));
				buffWrite.append(linhaAux + "\n");
				buffWrite.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
