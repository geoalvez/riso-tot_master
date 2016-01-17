package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CompareResults {
	
	public static String caminhoMarcacaoManual = "C:\\Users\\george.marcelo.alves\\Dropbox\\Marcacao Manual\\Marcação Manual.csv";

	public static final String diretorioResultados = "C:\\Users\\george.marcelo.alves\\Dropbox\\Resultados_RISO-TCG_Final\\<DOC_DIRETORIO>\\csv\\";
	
	public static void main(String[] args) {

		double qtdTotal = 0;
		double qtdAcertosNormalizado = 0;
		double qtdAcertosNaoNormalizado = 0;
		//for (int i = 0; i < args.length; i++){
			
			//String nomeArquivo = args[i];
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(caminhoMarcacaoManual));
				boolean cabecalhoLido = false;
				while(br.ready()){
				   String linha = br.readLine();
				   
				   if (!cabecalhoLido){
					   cabecalhoLido = true;
					   continue;
				   }
				   System.out.println("---" + linha);
				   if (linha.split(";").length != 5){
					   continue;
				   }
				   
				   if (linha.indexOf("Napoleon") < 0){
					   continue;
				   }
				   
				   String nomeTexto = linha.split(";")[0];
				   String entidade  = linha.split(";")[2];
				   String dataNormalizada = linha.split(";")[3];
				   String dataNaoNormalizada = linha.split(";")[4];
				   qtdTotal++;
				   
				   String diretorioArquivoResultados = diretorioResultados.replace("<DOC_DIRETORIO>", nomeTexto);
				   if (nomeTexto.equals("WW1")){
					   System.out.println();
				   }
				   File fileDiretorioArquivoResultados = new File (diretorioArquivoResultados);
				   
				   if (fileDiretorioArquivoResultados.isDirectory()){
					   String[] arquivosEntidadesDatas = fileDiretorioArquivoResultados.list();
					   Arrays.sort(arquivosEntidadesDatas);
					   
					   String arquivoRecente = arquivosEntidadesDatas[arquivosEntidadesDatas.length-1];
					   
						BufferedReader brAut = new BufferedReader(new FileReader(diretorioArquivoResultados + arquivoRecente));
						boolean achouNorm = false;
						boolean achouNaoNorm = false;
						
						while(brAut.ready()){
							String linhaAux = brAut.readLine();
							
							
							if (linhaAux.split(";").length != 3){
								continue;
							}
							String entidadeAut = linhaAux.split(";")[0];
							String datasNormalizadasAut = linhaAux.split(";")[1];
							String datasNaoNormalizadasAut = linhaAux.split(";")[2];
							
							String linhaOk = entidadeAut + ";"+"|"+datasNormalizadasAut+"|"+";"+"|"+datasNaoNormalizadasAut+"|";
							
							if (linhaOk.indexOf(entidade+";") >= 0 && linhaOk.indexOf("|"+dataNormalizada+"|") >= 0){
								qtdAcertosNormalizado++;
								achouNorm = true;
							}
							if (linhaOk.indexOf(entidade+";") >= 0 && linhaOk.indexOf("|"+dataNaoNormalizada+"|") >= 0){
								qtdAcertosNaoNormalizado++;
								achouNaoNorm = true;
							}
							
						}
						brAut.close();
						System.out.println("Arquivo ----- " + arquivoRecente);
						if (!achouNorm){
							System.out.println("Norm: "+entidade + "---"+dataNormalizada );
						}
						if (!achouNaoNorm){
							System.out.println("N Norm: "+entidade + "---"+dataNaoNormalizada );
						}
						System.out.println("-----");

				   }
				   
				   
				}
				br.close();			
				
			   System.out.println("Qtd linhas total = " + qtdTotal);
			   System.out.println("Qtd acerto normalizadas = " + qtdAcertosNormalizado);
			   System.out.println("Qtd acerto nao-normalizadas = " + qtdAcertosNaoNormalizado);
				   
			   System.out.println("% acertos norm = " + (qtdAcertosNormalizado / qtdTotal)*100+"%");
			   System.out.println("% acertos ñ norm = " + (qtdAcertosNaoNormalizado / qtdTotal)*100+"%");
				   
				   
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//}
	}

}
