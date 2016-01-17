package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import sun.text.normalizer.Trie.DataManipulate;

import br.edu.ufcg.copin.riso.tot.utils.RisoTcgUtil;

public class CompareResults {
	
	public static String caminhoMarcacaoManual = "C:\\Users\\george.marcelo.alves\\Dropbox\\Marcacao Manual\\Marcação Manual.csv";

	public static final String diretorioResultados = "C:\\Users\\george.marcelo.alves\\Dropbox\\Resultados_RISO-TCG_Final\\<DOC_DIRETORIO>\\csv\\";
	
	public static void main(String[] args) {

		double qtdTotal = 0;
		double qtdAcertosNormalizado = 0;
		double qtdAcertosNaoNormalizado = 0;
		
		double qtdTotalSemDBP = 0;
		double qtdAcertosNormalizadoSemDBP = 0;
		double qtdAcertosNaoNormalizadoSemDBP = 0;
		
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
				   
				   if (!dataNormalizada.equals(dataNaoNormalizada)){
					   qtdTotalSemDBP++;
				   }
				   
				   if (entidade.equals("Joséphine")){
					   if (dataNaoNormalizada.indexOf("in 1796") >= 0){
						   System.out.println();
					   }
				   }
				   
				   
				   String diretorioArquivoResultados = diretorioResultados.replace("<DOC_DIRETORIO>", nomeTexto);
				   if (nomeTexto.equals("WW1")){
					   System.out.println();
				   }
				   File fileDiretorioArquivoResultados = new File (diretorioArquivoResultados);
				   
				   if (fileDiretorioArquivoResultados.isDirectory()){
					   
					   String[] arquivosEntidadesDatas = fileDiretorioArquivoResultados.list();
					   
					   Arrays.sort(arquivosEntidadesDatas);
					   
					   String arquivoRecente = arquivosEntidadesDatas[arquivosEntidadesDatas.length-1];
					   if (new File(diretorioArquivoResultados + arquivoRecente).isDirectory()){
						   arquivoRecente = arquivosEntidadesDatas[arquivosEntidadesDatas.length-2];
					   }
						BufferedReader brAut = new BufferedReader(new InputStreamReader(new FileInputStream(diretorioArquivoResultados + arquivoRecente),"Cp1252"));
						boolean achouNorm = false;
						boolean achouNaoNorm = false;
						int cont = 0;
						while(brAut.ready()){
							cont++;
							
							if (cont == 498){
								System.out.println();
							}
							String linhaAux = brAut.readLine();
							
							if (linhaAux.indexOf("Egypt") >= 0){
								System.out.println();
							}
							
							if (linhaAux.split(";").length != 3){
								continue;
							}
							String entidadeAut = linhaAux.split(";")[0];
							String datasNormalizadasAut = linhaAux.split(";")[1];
							String datasNaoNormalizadasAut = linhaAux.split(";")[2];
							
							String linhaOk = entidadeAut + ";"+"|"+datasNormalizadasAut+"|"+";"+"|"+datasNaoNormalizadasAut+"|";
							
							if (linhaOk.indexOf("Joséphine") >= 0){
								System.out.println();
							}
							if (linhaOk.indexOf(entidade.trim()+";") >= 0 && linhaOk.indexOf("|"+dataNormalizada.trim()+"|") >= 0){
								qtdAcertosNormalizado++;
								achouNorm = true;
							   if (!dataNormalizada.equals(dataNaoNormalizada)){
								   qtdAcertosNormalizadoSemDBP++;
							   }
								
							}else{
								String linhaFormataComZero = RisoTcgUtil.incluiZero(linhaOk);
								String dataNormalizadaFormataComZero = RisoTcgUtil.incluiZero(dataNormalizada);
								
								if (linhaFormataComZero.indexOf(entidade.trim()+";") >= 0 && linhaOk.indexOf("|"+dataNormalizadaFormataComZero.trim()+"|") >= 0){
									achouNorm = true;
									qtdAcertosNormalizado++;									
									if (!dataNormalizada.equals(dataNaoNormalizada)){
										qtdAcertosNormalizadoSemDBP++;
									}
								}
								
								
							}
							
							if (linhaOk.indexOf(entidade.trim()+";") >= 0 && linhaOk.indexOf("|"+dataNaoNormalizada.trim()+"|") >= 0){
								qtdAcertosNaoNormalizado++;
								achouNaoNorm = true;
							   if (!dataNormalizada.equals(dataNaoNormalizada)){
								   qtdAcertosNaoNormalizadoSemDBP++;
							   }
							}else{
								if (RisoTcgUtil.validaSemPreposicoes(linhaOk, entidade, dataNaoNormalizada)){
									qtdAcertosNaoNormalizado++;
									achouNaoNorm = true;
									if (!dataNormalizada.equals(dataNaoNormalizada)){
										qtdAcertosNaoNormalizadoSemDBP++;
									}
									
								}else{
									
									String  datasNaoNormalizadasAux = linhaOk.split("\\;")[2];
									String[] listaDatasNormalizadasAux = datasNaoNormalizadasAux.split("\\|");
									for (int i = 0; i < listaDatasNormalizadasAux.length; i++){
										if (linhaOk.indexOf(entidade.trim()+";") >= 0 && listaDatasNormalizadasAux[i].indexOf(dataNaoNormalizada) >= 0 && !listaDatasNormalizadasAux[i].isEmpty()){
											qtdAcertosNaoNormalizado++;
											achouNaoNorm = true;
											if (!dataNormalizada.equals(dataNaoNormalizada)){
												qtdAcertosNaoNormalizadoSemDBP++;
											}
											break;
										}
									}
									
								}
							}
							
							if (achouNorm || achouNaoNorm){
								break;
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
						
						if (!achouNorm){
							if (!dataNormalizada.equals(dataNaoNormalizada)){
								System.out.println("Norm (N DBP): "+entidade + "---"+dataNormalizada );								
							}
						}
						if (!achouNaoNorm){
							if (!dataNormalizada.equals(dataNaoNormalizada)){
								System.out.println("N Norm (N DBP): "+entidade + "---"+dataNaoNormalizada );								
							}
						}

						System.out.println("-----");

				   }
				   
				   
				}
				br.close();			
				
			   System.out.println("Qtd linhas total = " + qtdTotal);
			   System.out.println("Qtd acerto normalizadas = " + qtdAcertosNormalizado);
			   System.out.println("Qtd acerto nao-normalizadas = " + qtdAcertosNaoNormalizado);

			   
			   System.out.println("Qtd linhas total (Sem DBPEdia)= " + qtdTotalSemDBP);
			   System.out.println("Qtd acerto normalizadas (Sem DBPEdia)= " + qtdAcertosNormalizadoSemDBP);
			   System.out.println("Qtd acerto nao-normalizadas (Sem DBPEdia)= " + qtdAcertosNaoNormalizadoSemDBP);
			   
			   System.out.println("% acertos norm = " + (qtdAcertosNormalizado / qtdTotal)*100+"%");
			   System.out.println("% acertos ñ norm = " + (qtdAcertosNaoNormalizado / qtdTotal)*100+"%");
				   
			   System.out.println("% acertos norm (Sem DBPEdia)= " + (qtdAcertosNormalizadoSemDBP / qtdTotalSemDBP)*100+"%");
			   System.out.println("% acertos ñ norm (Sem DBPEdia)= " + (qtdAcertosNaoNormalizadoSemDBP / qtdTotalSemDBP)*100+"%");
				   
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//}
	}

}
