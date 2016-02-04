package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import sun.text.normalizer.Trie.DataManipulate;

import br.edu.ufcg.copin.riso.tot.entities.Documento;
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
		HashMap<String, Integer> listaQtd = new HashMap<String, Integer>();
		HashMap<String, Integer> listaQtdNDB = new HashMap<String, Integer>();
		HashMap<String, ArrayList<String>> mapNaoAchou = new HashMap<String, ArrayList<String>>();
		
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


// descomentar para filtrar
				   if (linha.indexOf("WW1") < 0){
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
						   if (arquivoRecente.indexOf("bkp") >= 0){
							   arquivoRecente = arquivosEntidadesDatas[arquivosEntidadesDatas.length-3];							   
						   }
					   }
					   
					   if (!listaQtd.containsKey(nomeTexto)){
						   BufferedReader brCount = new BufferedReader(new InputStreamReader(new FileInputStream(diretorioArquivoResultados + arquivoRecente),"Cp1252"));
						   while (brCount.ready()){
							   String linhaCount = brCount.readLine();
							   if (linhaCount.indexOf("Napoleonic Wars") >= 0){
								   System.out.println();
							   }
							   String[] campos = linhaCount.split("\\;");
							   String datasNorm = campos[1];
							   String datasNaoNorm = campos[2];
							   //TODO
							   
							   String entidadeVerifica = campos[0];
							   
							   boolean achou = false;
								BufferedReader brVerifica = new BufferedReader(new FileReader(caminhoMarcacaoManual));
								while(brVerifica.ready()){
									   String linhaVerifica = brVerifica.readLine();
									   
									   if (linhaVerifica.indexOf(";"+entidadeVerifica+";") >= 0){
										   
										   achou = true;
										   break;
									   }
									
								}
								
								if (!achou){
									if (!mapNaoAchou.containsKey(nomeTexto)){
										ArrayList<String> arrayAux = new ArrayList<String>();
										
										String linhaAux = nomeTexto + ";" + "X" + ";" + entidadeVerifica + ";";
										String[] datasNormList = datasNorm.split("\\|");
										String[] datasNaoNormList = datasNaoNorm.split("\\|");
										
										int count = 0;
										for (String dataNormAux : datasNormList){
											String linhaInd = linhaAux + "";
											
											if (!dataNormAux.isEmpty()){
												linhaInd = linhaInd+dataNormAux+";";
											}
											if (!datasNaoNormList[count].isEmpty()){
												linhaInd = linhaInd+datasNaoNormList[count];												
											}
											count++;
											
											if (!linhaInd.equals(linhaAux)){
												arrayAux.add(linhaInd);												
												mapNaoAchou.put(nomeTexto, arrayAux);
											}
										}
										
										
									}else{
										
										ArrayList<String>  listaAùx = mapNaoAchou.get(nomeTexto);
										String linhaAux = nomeTexto + ";" + "X" + ";" + entidadeVerifica + ";";
										String[] datasNormList = datasNorm.split("\\|");
										String[] datasNaoNormList = datasNaoNorm.split("\\|");
										
										int count = 0;
										for (String dataNormAux : datasNormList){
											String linhaInd = linhaAux + "";
											
											if (!dataNormAux.isEmpty()){
												linhaInd = linhaInd+dataNormAux+";";
											}
											if (!datasNaoNormList[count].isEmpty()){
												linhaInd = linhaInd+datasNaoNormList[count];												
											}
											count++;
											if (!linhaInd.equals(linhaAux)){
												listaAùx.add(linhaInd);												
												mapNaoAchou.put(nomeTexto, listaAùx);
											}
										}										
									}
								}
							   
							   
							   String[] datasList = datasNorm.split("\\|");
							   int qtdDatasNorm = datasList.length - 1;
							   int qtdNaoNorm = 0;
							   for (int i = 0; i < datasList.length; i++){
								   String data = datasList[i];
								   if (data.split("-").length == 5 || data.split("-").length == 3){
									   if (datasNaoNorm.indexOf(data) < 0){
										   qtdNaoNorm++;
									   }
								   }
							   }
							   
							   // n norm
							   if (!listaQtdNDB.containsKey(nomeTexto)){
								   
								   listaQtdNDB.put(nomeTexto, qtdNaoNorm);
							   }else{
								   Integer qtdExistente = listaQtdNDB.get(nomeTexto);
								   Integer qtdFinal = qtdExistente + qtdNaoNorm;
								   
								   listaQtdNDB.put(nomeTexto, qtdFinal);
								   
							   }
							   
							   
							   // n norm
							   if (!listaQtd.containsKey(nomeTexto)){
								   
								   listaQtd.put(nomeTexto, qtdDatasNorm);
							   }else{
								   Integer qtdExistente = listaQtd.get(nomeTexto);
								   Integer qtdFinal = qtdExistente + qtdDatasNorm;
								   
								   listaQtd.put(nomeTexto, qtdFinal);
								   
							   }
							   
							   
						   }
						   brCount.close();
						   
					   }
					   if (linha.indexOf("miles west of San Diego") >= 0){
						   System.out.println();
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
							   if (linhaAux.indexOf("miles west of San Diego") >= 0){
								   System.out.println();
							   }
							
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
								
								if (linhaFormataComZero.indexOf(entidade.trim()+";") >= 0 && linhaFormataComZero.indexOf("|"+dataNormalizadaFormataComZero.trim()+"|") >= 0){
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
				   
			
			   for (Entry<String, Integer> pair : listaQtd.entrySet()) {
				   String documento = pair.getKey();
				   Integer qtdDocumento = pair.getValue();
				
				   System.out.println("Qtd relacionamentos criados para documento ["+documento+"]: " + qtdDocumento);
			   }
			   for (Entry<String, Integer> pair : listaQtdNDB.entrySet()) {
				   String documento = pair.getKey();
				   Integer qtdDocumento = pair.getValue();
				
				   System.out.println("Qtd relacionamentos criados para documento ["+documento+"] (Excluindo DBPedia): " + qtdDocumento);
			   }
			   
			   
			   System.out.println("-------------");
			   System.out.println("Entidades nao encontrou ");
			   System.out.println("-------------");
			   for (Entry<String, ArrayList<String>> pair : mapNaoAchou.entrySet()) {
				   String documento = pair.getKey();
				   ArrayList<String> listaNaoAchou = pair.getValue();
				   System.out.println("Documento: " + documento);
				   for (String entidadeNaoEncontrou : listaNaoAchou){
					   System.out.println(entidadeNaoEncontrou);
				   }
				   
			   }
			   
			   
			   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//}
	}

}
