package br.edu.ufcg.copin.riso.tot.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import br.edu.ufcg.copin.riso.tot.constants.ConstantsRisoTOT;
import br.edu.ufcg.copin.riso.tot.dao.DBPediaDAO;
import br.edu.ufcg.copin.riso.tot.entities.Cidade;
import br.edu.ufcg.copin.riso.tot.entities.Empresa;
import br.edu.ufcg.copin.riso.tot.entities.EntidadeEvento;
import br.edu.ufcg.copin.riso.tot.entities.Estado;
import br.edu.ufcg.copin.riso.tot.entities.Feriado;
import br.edu.ufcg.copin.riso.tot.entities.Instituicao;
import br.edu.ufcg.copin.riso.tot.entities.Local;
import br.edu.ufcg.copin.riso.tot.entities.Pais;
import br.edu.ufcg.copin.riso.tot.entities.Pessoa;
import br.edu.ufcg.copin.riso.tot.jena.JenaOWL;

public class RisoTotMain2 {


	private static String conteudo = "";

	private static String[] listaConjuncoes = {"and/CC","or/CC","but/CC"};
	
	private static String TAG_PONTO_E_VIRGULA = "#TAGPTVRG";
	
	private static String[] listaVerbos = {"VBD", "VB", "VBG", "VBN", "VBP", "VBZ"};
	
	private static String[] listaTodasAsFrasesTexto;
	
	public static ArrayList<String> listaFrasesTemporaisTexto = new ArrayList<String>();
	
	private static ArrayList<String> listaEntidadesTexto = new ArrayList<String>();
	
	public static ArrayList<String> entidadesTemporalizadas = new ArrayList<String>();
	
	private static HashMap<String, ArrayList<String>> hashEntidadesDatas = new HashMap();
	
	private static HashMap<String, ArrayList<String>> hashEntidadesDatasNaoNormalizadas = new HashMap();
	
	private static boolean verificaArquivoAnterior(int numInicio){
		boolean retorno = true;
		File dir = new File ("C:\\workspace_mestrado\\riso-tcg\\ctrl\\");
		for (File arquivo : dir.listFiles()){
			int id = new Integer(arquivo.getName()).intValue();
			if (id < numInicio){
				retorno = false;
			}
		}
		
		
		return retorno;
		
		
		
	}
	private static void addNoArquivoDatasEspeciaisRisoTT(ArrayList<String> listaEntidades, int numInicio) throws Exception{

		File arquivoDeControle = new File ("C:\\workspace_mestrado\\riso-tcg\\ctrl\\" + numInicio);
		// boolean continua = true;
		System.out.println("Aguardando arquivo de controle: " + new Date());
		while (!verificaArquivoAnterior(numInicio)){
			// System.out.println("Esperando --> " + new Date());
			Thread.sleep(1);
		}
		
		System.out.println("Escrevendo --> " + new Date());
		
		for (String entidadeTexto: listaEntidades){
			
			BufferedReader buffRead;
			
			try {
				buffRead = new BufferedReader(new FileReader(ConstantsRisoTOT.CAMINHO_ARQUIVO_ENTIDADES_TEMPORALIZADAS));
				String conteudoArquivo = ""; 
				while (true) {
					String linha = buffRead.readLine();
					if (null != linha){
						if (linha.indexOf("</simbolo>") >= 0) { 
							break; 
						}
						
					}else{
						break;
					}
					conteudoArquivo = conteudoArquivo.concat(linha).concat(System.getProperty("line.separator")); 
				}
				buffRead.close();
				
				if (conteudoArquivo.indexOf("<expressao>\""+entidadeTexto+"\"</expressao>") < 0){
					
					BufferedWriter buffWrite = new BufferedWriter(new FileWriter("C:\\workspace_mestrado\\RisoTemporalTagger_soprasubir\\padroes\\datas_especiais.xml")); 
					buffWrite.write(conteudoArquivo + "    <expressao>\""+entidadeTexto+"\"</expressao>"+System.getProperty("line.separator") + "</simbolo>"); 
//					buffWrite.flush();
					buffWrite.close();
					
				}else{
					BufferedWriter buffWrite = new BufferedWriter(new FileWriter("C:\\workspace_mestrado\\RisoTemporalTagger_soprasubir\\padroes\\datas_especiais.xml")); 
					buffWrite.write(conteudoArquivo + "</simbolo>"); 
					// buffWrite.flush();
					buffWrite.close();					
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new Exception(e);
			} 
			
			
		}
		
		arquivoDeControle.delete();
		System.out.println("Deletou --> " + new Date());
		System.out.println("arquivo de controle deletado!" );
		
	}
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
	
	

	public static boolean verificaParavraEntidade (String palavra){
		
		if (listaEntidadesTexto.contains(palavra)){
			return true;
		}
		return false;
	}
	
	public static ArrayList<String> getEntidadesFrases(String frase){
		ArrayList<String> retorno = new ArrayList<String>();
		
		for (int i =0; i < listaEntidadesTexto.size(); i++){
			
			if (frase.indexOf(listaEntidadesTexto.get(i)) >= 0){
				retorno.add(listaEntidadesTexto.get(i));
			}
		}
		return retorno;
		
	}
	
	public static ArrayList<String> getEntidadesTemporalizadasDBPedia (String frase){
		ArrayList<String> listaRetorno = new ArrayList<String>();
		
		int inicioPos = 0;
		int fimPos = 0;

		boolean continua = true;
		while (continua){
			
			inicioPos = frase.indexOf(ConstantsRisoTOT.TAG_DE);
			fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL, inicioPos);
			
			if (inicioPos == -1){
				continua = false;
			}else{
				
				String entidade = frase.substring(inicioPos + ConstantsRisoTOT.TAG_DE.length(), fimPos);
				entidade = entidade.replace("_", " ");
				listaRetorno.add(entidade);
				
				frase = frase.substring(fimPos + ConstantsRisoTOT.TAG_RISO_TEMPORAL.length());
			}
			
		}
	
		
		return listaRetorno;
	}
	
	public static String getFraseSemTags(String frase){
		String retorno = "";
		
		boolean leuBarraTagOuVirgula = false;
		for (int i = 0; i < frase.length(); i++){
//			if (frase.subSequence(i, i+1).equals("<")){
//				System.out.println("aqui");
//			}
			String aux = frase.substring(i,i+1);
			String auxVerificaVirgula = "";
			String auxRisoTime = "";
			if (i+3 <= frase.length()){
				auxVerificaVirgula = frase.substring(i,i+3);
			}
			if (i+10 <= frase.length()){
				auxRisoTime = frase.substring(i,i+10);
			}			 

			if (!leuBarraTagOuVirgula){
				if (auxVerificaVirgula.equals(":/:")){
					retorno = retorno.concat(",");
					leuBarraTagOuVirgula = true;
				}else if(!aux.equals("/")){
					retorno = retorno.concat(aux);
				}else{
					if (!auxRisoTime.equals("/RISOTime>")){
						leuBarraTagOuVirgula = true;
					}
					
				}
				
			}else{
				if (aux.equals(" ")){
					leuBarraTagOuVirgula = false;
					retorno = retorno.concat(" ");
				}
			}
			
			
		}		

		return retorno;
	
	}

	public static ArrayList<String> retornaDatasFrase (String frase, ArrayList<String> listaEntidadesDBPedia, boolean recuperaDataDE){
		/* TODO - corrigir esse metodo - ta pegando data errada */
		
		
		ArrayList<String> listaRetorno = new ArrayList<String>();
		
		int inicioPos = 0;
		int fimPos = 0;

		boolean continua = true;
		while (continua){
			
			inicioPos = frase.indexOf(ConstantsRisoTOT.TAG_GENERICA);
			
			String indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL;
			fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL, inicioPos);
			if (fimPos == -1){
				fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI, inicioPos);
				indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI;
			}
			
			if (inicioPos == -1){
				continua = false;
				break;
			}else{
				
				String entidade = frase.substring(inicioPos, fimPos + indFim.length());
				
				String entidadeSemTag = getDataSemTag(entidade);
				// george remover - inicio 
				if (entidadeSemTag.equals("Vladivostok")){
					System.out.println();
				}
				// george remover - fim 
				
				if (listaEntidadesDBPedia.contains(entidadeSemTag)){
					
					if (recuperaDataDE){
						//ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidadeSemTag);
						ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidadeSemTag);
						
						//ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidadeSemTag, listaEntidadesTemporalizadas);
						//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);
						
						for (String entidadeFormatada : listaEntidadesTemporalizadas){
							listaRetorno.add(entidadeFormatada);
						}						
					}
				}else{
					listaRetorno.add(entidade);					
				}
				
				frase = frase.substring(fimPos + indFim.length());
			}
			
		}
	
		
		return listaRetorno;
		
		
		
	}
	
	
	public static ArrayList<String> retornaDatasFrase (String frase, ArrayList<String> listaEntidadesDBPedia){
		/* TODO - corrigir esse metodo - ta pegando data errada */
//		ArrayList<String> retorno = new ArrayList<String>();
//		
//		boolean lendoData = false;
//		boolean ehPrimeiroMenor = true;
//		String aux = "";
//		for (int i = 0; i < frase.length(); i++){
//			if (frase.substring(i,i+1).equals("<") || lendoData){
//				lendoData = true;
//				aux = aux.concat(frase.substring(i,i+1));
//				
//			}
//			
//			if (frase.substring(i,i+1).equals(">")){
//				if (!ehPrimeiroMenor){
//					lendoData = false;
//					ehPrimeiroMenor = true;
//					retorno.add(aux);
//					aux = "";
//				}else{
//					ehPrimeiroMenor = false;
//				}
//			}
//		}
//		
//		return retorno;
		
		boolean recuperaDataDE = false;
		
		if (contaOcorrenciasDatasTotal(frase, listaEntidadesDBPedia) == contaOcorrenciasDE(frase, listaEntidadesDBPedia)){
			recuperaDataDE = true;
		}
		
		ArrayList<String> listaRetorno = new ArrayList<String>();
		
		int inicioPos = 0;
		int fimPos = 0;

		boolean continua = true;
		while (continua){
			
			inicioPos = frase.indexOf(ConstantsRisoTOT.TAG_GENERICA);
			
			String indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL;
			fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL, inicioPos);
			if (fimPos == -1){
				fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI, inicioPos);
				indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI;
			}
			
			if (inicioPos == -1){
				continua = false;
				break;
			}else{
				
				String entidade = frase.substring(inicioPos, fimPos + indFim.length());
				
				String entidadeSemTag = getDataSemTag(entidade);
				// george remover - inicio 
				if (entidadeSemTag.equals("Vladivostok")){
					System.out.println();
				}
				// george remover - fim 
				
				if (listaEntidadesDBPedia.contains(entidadeSemTag)){
					
					if (recuperaDataDE){
						ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidadeSemTag);
						ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidadeSemTag, listaEntidadesTemporalizadas);
						ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaTempoEntidade);
						
						for (String entidadeFormatada : listaTemporalFormatada){
							listaRetorno.add(entidadeFormatada);
						}						
					}
				}else{
					listaRetorno.add(entidade);					
				}
				
				frase = frase.substring(fimPos + indFim.length());
			}
			
		}
	
		
		return listaRetorno;
		
		
		
	}
	
	public static int contaOcorrenciasDE (String frase, ArrayList<String> listaEntidadesDBPedia){
		
		int inicioPos = 0;
		int fimPos = 0;
		
		int count = 0;

		boolean continua = true;
		while (continua){
			
			inicioPos = frase.indexOf(ConstantsRisoTOT.TAG_DE);
			
			String indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL;
			fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL, inicioPos);
			if (fimPos == -1){
				fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI, inicioPos);
				indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI;
			}
			
			if (inicioPos == -1){
				continua = false;
				break;
			}else{
				count++;
				frase = frase.substring(fimPos + indFim.length());
			}
			
		}
	
		
		return count;
	
	}	
	
	
	public static int contaOcorrenciasDatasTotal (String frase, ArrayList<String> listaEntidadesDBPedia){
		
		int inicioPos = 0;
		int fimPos = 0;
		
		int count = 0;

		boolean continua = true;
		while (continua){
			
			inicioPos = frase.indexOf(ConstantsRisoTOT.TAG_GENERICA);
			
			String indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL;
			fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL, inicioPos);
			if (fimPos == -1){
				fimPos = frase.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI, inicioPos);
				indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI;
			}
			
			if (inicioPos == -1){
				continua = false;
				break;
			}else{
				count++;
				frase = frase.substring(fimPos + indFim.length());
			}
			
		}
	
		
		return count;
	
	}	
		
	public static ArrayList<String> retornaFraseEmPartes(String frase){
		ArrayList<String> retorno = new ArrayList<String>();
		
		String[] divisaoPorVirgula = frase.split(":/:");
		
		ArrayList<String> divisaoPorPontoEVirgula = new ArrayList<String>();
		
		
		for (int i = 0; i < divisaoPorVirgula.length; i++){
			String aux = divisaoPorVirgula[i];
			String [] aux1 = aux.split(";/:");
			
			boolean first = true;
			for (int j = 0; j < aux1.length; j++){
				if (aux1.length > 1){
					if (first){
						divisaoPorPontoEVirgula.add(aux1[j]);
						divisaoPorPontoEVirgula.add(TAG_PONTO_E_VIRGULA);
						first = false;
					}else{
						divisaoPorPontoEVirgula.add(aux1[j]);
						first = true;
					}
					
				}else{
					first = true;
					divisaoPorPontoEVirgula.add(aux1[j]);
				}
				
			}
		}
		
		ArrayList<String> divisaoPorConjuncoes = new ArrayList<String>();
		divisaoPorConjuncoes.addAll(divisaoPorPontoEVirgula);
		
		for (int i = 0; i < listaConjuncoes.length; i++){
			String regex = listaConjuncoes[i];
			
			ArrayList<String> listaAux =  new ArrayList<String>();
			
			for (String parte : divisaoPorConjuncoes){
				String[] aux2 = parte.split(regex);
			
				for (int k = 0; k < aux2.length; k++){
					if (!aux2[k].trim().isEmpty()){
						listaAux.add(aux2[k]);
					}
					
				}
			}
			divisaoPorConjuncoes = new ArrayList<String>();
			divisaoPorConjuncoes.addAll(listaAux);
		}
		
		retorno = divisaoPorConjuncoes;
		
		return retorno;
	}
	
	public static void addHashEntidadesDatas(String entidade, ArrayList<String> tagsTemporais){
		// george remover - inicio 
		if (entidade.equals("Japan") && tagsTemporais.size() > 0){
			System.out.println();
		}
		
		if (entidade.equals("Napoleon")){
			System.out.println();
		}
		
		// george remover - fim 
		if (!hashEntidadesDatas.containsKey(entidade)){
			hashEntidadesDatas.put(entidade, tagsTemporais);
		}else{
			ArrayList<String> datasAux = hashEntidadesDatas.get(entidade);
			ArrayList<String> datasFinal = new ArrayList<>();
			datasFinal.addAll(datasAux);
			for (int k = 0; k < tagsTemporais.size(); k++){
				if (!datasFinal.contains(tagsTemporais.get(k)) && !tagsTemporais.get(k).isEmpty()){
					datasFinal.add(tagsTemporais.get(k));
				}
			}
			
			hashEntidadesDatas.put(entidade, datasFinal);
		}
		
		
	}

	public static void addHashEntidadesDatasNaoNormalizadas(String entidade, ArrayList<String> tagsTemporais){
		
		if (!hashEntidadesDatasNaoNormalizadas.containsKey(entidade)){
			hashEntidadesDatasNaoNormalizadas.put(entidade, tagsTemporais);
		}else{
			ArrayList<String> datasAux = hashEntidadesDatasNaoNormalizadas.get(entidade);
			ArrayList<String> datasFinal = new ArrayList<>();
			datasFinal.addAll(datasAux);
			for (int k = 0; k < tagsTemporais.size(); k++){
				if (!datasFinal.contains(tagsTemporais.get(k)) && !tagsTemporais.get(k).isEmpty()){
					datasFinal.add(tagsTemporais.get(k));
				}
			}
			
			hashEntidadesDatasNaoNormalizadas.put(entidade, datasFinal);
		}
		
		
	}
	
	public static void addHashEntidadesDatas(String entidade, ArrayList<String> tagsTemporais, ArrayList<String> entidadesTempDBPedia){
		
		if (entidade.equals("Napoleon")){
			System.out.println();
		}		
		if (!hashEntidadesDatas.containsKey(entidade)){
			for (int i = 0; i < tagsTemporais.size(); i++){
				if (entidadesTempDBPedia.contains(getDataSemTag(tagsTemporais.get(i)))){
					//ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(tagsTemporais.get(i));
					ArrayList<String> listaTemporalFormatada = DBPediaDAO.buscaDataEntidades(tagsTemporais.get(i));
					
					// ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(tagsTemporais.get(i), listaEntidadesTemporalizadas);
					// ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

					addHashEntidadesDatas(entidade, listaTemporalFormatada);	
				}else{
					ArrayList<String> listaAux = new ArrayList<String>();
					listaAux.add(tagsTemporais.get(i));
					addHashEntidadesDatas(entidade, listaAux);					
					
				}
			}
		}else{
			ArrayList<String> datasAux = hashEntidadesDatas.get(entidade);
			ArrayList<String> datasFinal = new ArrayList<>();
			datasFinal.addAll(datasAux);
			for (int k = 0; k < tagsTemporais.size(); k++){
				if (!datasFinal.contains(tagsTemporais.get(k)) && !tagsTemporais.get(k).isEmpty()){
					datasFinal.add(tagsTemporais.get(k));
				}
			}
			
			for (int i = 0; i < datasFinal.size(); i++){
				if (entidadesTempDBPedia.contains(getDataSemTag(datasFinal.get(i)))){
					ArrayList<String> listaTemporalFormatada = DBPediaDAO.buscaDataEntidades(datasFinal.get(i));
//					ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(datasFinal.get(i), listaEntidadesTemporalizadas);
//					ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaTempoEntidade);

					addHashEntidadesDatas(entidade, listaTemporalFormatada);					
				}else{
					ArrayList<String> listaAux = new ArrayList<String>();
					listaAux.add(datasFinal.get(i));
					addHashEntidadesDatas(entidade, listaAux);					
					
				}
			}
			
			
			// hashEntidadesDatas.put(entidade, datasFinal);
		}
		
		
	}
	public static String getTipoPalavra(String fraseComTags, String palavra){
//		String retorno = "";
//		int index = fraseComTags.indexOf(palavra);
//		int indexAux = index + palavra.length() + 1;
//		for (int i = indexAux; i < fraseComTags.length(); i++){
//			String caractere = fraseComTags.substring(i,i+1);
//			if (!caractere.equals(" ")){
//				retorno = retorno.concat(caractere);
//			}else{
//				break;
//			}
//		}
//		return retorno;
		String[] palavraTipo = palavra.split("/");
		return palavraTipo[palavraTipo.length-1];
	}
	
	public static boolean fraseSemEntidade(String fraseComTags){
		
		String fraseSemTag = getFraseSemTags(fraseComTags);
		ArrayList<String> listaEntidadesFrase = getEntidadesFrases(fraseSemTag);
		if (listaEntidadesFrase.isEmpty()){
			return true;
		}else{
			for (String entidade : listaEntidadesFrase){
				
				if (!ConstantsRisoTOT.LISTA_VERBOS_SEM_BARRA().contains(getTipoPalavra(fraseComTags, entidade))){
					if (getTipoPalavra(fraseComTags, entidade).indexOf("RISOTime") == -1){
						return false;
					}
					
				}
			}
		}
		return true;
	}
	
	public static boolean fraseSemVerbo(String fraseComTags){
		
		String[] fraseDividePorEspacos = fraseComTags.split(" ");
		
		for (String palavra : fraseDividePorEspacos){
			
			if (ConstantsRisoTOT.LISTA_VERBOS_SEM_BARRA().contains(getTipoPalavra(fraseComTags, palavra))){
				return false;
			}
		}

		return true;
		
	}
	
	private static boolean verificaSequenciaPartesSemVerbo(ArrayList<String> parteFrases){
		
		int count = 0;
		boolean first = true;
		for (String parte : parteFrases){
			count++;
			
			if (!fraseSemVerbo(parte)){
				
				if (parte.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
					
				}
				if (!first){
					first = false;
					if (count < 2){
						return false;
					}
					
				}
				
			}
			
		}

		return true;
	}
	
	public static String getDataSemTag (String dataComTag){
		String dataSemTag = "";
		
		//dataSemTag = dataComTag.replace(ConstantsRisoTOT.tag, arg1)
		
		int inicioPos = dataComTag.indexOf(">");
		
		String indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL;
		int fimPos = dataComTag.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL, inicioPos);
		if (fimPos == -1){
			fimPos = dataComTag.indexOf(ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI, inicioPos);
			indFim = ConstantsRisoTOT.TAG_RISO_TEMPORAL_INI;
		}
		
		if (inicioPos != -1 && dataComTag.indexOf(ConstantsRisoTOT.TAG_GENERICA) >= 0){
			try{
				dataSemTag = dataComTag.substring(inicioPos + 1, fimPos);
			}catch (Exception e){
				System.out.println();
			}
			
		}
		if (dataSemTag.isEmpty()){
			dataSemTag = dataComTag;
		}
		
		dataSemTag = dataSemTag.replace("_", " ");
		return dataSemTag;
	}
	private static String substituiTypeDate (String data, String newTagType){
		String tagData = "";
		String novaData = "";
		for (int i =0; i < data.length(); i++){
			String caractere = data.substring(i, i+1);
			tagData = tagData.concat(caractere);
			if (caractere.equals(">")){
				break;
			}
		}
		
		novaData = data.replaceAll(tagData, newTagType);
		return novaData;
	}
	
	
	public static Date getStringAsDate (String dataAsString){
		
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy"); 
		
		String[] campos = dataAsString.split("-");
		String dia = campos[0];
		String mes = campos[1];
		String ano = campos[2];
		String novoDia = "";
		String novoMes = "";
		String novoAno = "";
		
		if (dia.length() == 1){
			novoDia = "0" + dia;
		}else{
			novoDia = dia;
		}
		
		if (mes.length() == 1){
			novoMes = "0" + mes;
		}else{
			novoMes = mes;
		}
		
		if (ano.length()== 2 && new Integer (ano) > 20 && new Integer (ano) < 00){
			novoAno = "20" + ano;
		}else if (ano.length()== 2 && new Integer(ano) < 20) {
			novoAno = "19" + ano;
		}else{
			novoAno = ano;
		}
		
		String dataFormatada = novoDia + "-" + novoMes + "-" + novoAno;
		
		Date data;
		try {
			data = dt.parse(dataFormatada);
		} catch (ParseException e) {
			data = new Date();
		} 
		
		return data;
	}

	public static String getDataPosCompare (String dataEntidade, String dataTexto){
		String retorno  ="";
		
		try{
			if (dataEntidade.indexOf("[?]") == -1 && dataTexto.indexOf("[?]")== -1){
				
				
				if (dataEntidade.indexOf(" < X < ") != -1 && dataTexto.indexOf(" < X < ") == -1  && dataTexto.indexOf("X >") == -1 && dataTexto.indexOf("X <") == -1){
					
					retorno = dataEntidade;
					
				}else if (dataEntidade.indexOf(" < X < ") == -1  && dataEntidade.indexOf("X >") == -1 && dataEntidade.indexOf("X <") == -1 && dataTexto.indexOf(" < X < ") != -1){
					
					retorno = dataEntidade;
					
				}else if (dataEntidade.indexOf(" < X < ") != -1 && dataTexto.indexOf(" < X < ") != -1){
					String[] camposDataEntidade = dataEntidade.split(" < X < ");
					String[] camposDataTexto = dataTexto.split(" < X < ");
					
					Date data1Entidade = getStringAsDate(camposDataEntidade[0]);
					Date data2Entidade = getStringAsDate(camposDataEntidade[1]);
					
					Date data1Texto = getStringAsDate(camposDataTexto[0]);
					Date data2Texto = getStringAsDate(camposDataTexto[1]);
					
					
					if (data1Entidade.before(data1Texto) && data2Entidade.before(data1Texto)){
						retorno = dataEntidade;
					}else if ((data1Entidade.before(data1Texto) || data1Entidade.equals(data1Texto))  && data2Entidade.after(data1Texto)  && (data2Entidade.before(data2Texto) || data2Entidade.equals(data2Texto))){
						retorno = camposDataTexto[0] + " < X < " + camposDataEntidade[1];
					}else if (data1Entidade.after(data1Texto) &&  data1Entidade.before(data2Texto) && data2Entidade.after(data1Texto)  && data2Entidade.before(data2Texto)){
						retorno = dataEntidade;
					}else if (data1Entidade.after(data1Texto) &&  data1Entidade.after(data2Texto) && data2Entidade.after(data1Texto)  && data2Entidade.after(data2Texto)){
						retorno = dataEntidade;
					}
					
				}else if (dataEntidade.indexOf(" < X < ") != -1 && dataTexto.indexOf("X < ") != -1 && dataTexto.indexOf(" < X < ") == -1){
					
					
					retorno = dataEntidade;
					String[] camposDataEntidade = dataEntidade.split(" < X < ");
					String[] camposDataTexto = dataTexto.split("X < ");
					
					Date data1Entidade = getStringAsDate(camposDataEntidade[0]);
					Date data2Entidade = getStringAsDate(camposDataEntidade[1]);
					
					Date data1Texto = getStringAsDate(camposDataTexto[1]);
					
					if (data1Entidade.before(data1Texto) && data2Entidade.before(data1Texto)){
						retorno = dataEntidade;
					}else if ((data1Entidade.before(data1Texto) || data1Entidade.equals(data1Texto)) && (data2Entidade.after(data1Texto) || data2Entidade.equals(data1Texto))){
						retorno = camposDataEntidade[0] + " < X < " + camposDataTexto[1];
					}else if (data1Entidade.after(data1Texto) && data2Entidade.after(data1Texto)){
						retorno = dataEntidade;
					}					
					
				}else if (dataEntidade.indexOf(" < X < ") != -1 && dataTexto.indexOf("X > ") != -1 && dataTexto.indexOf(" < X < ") == -1){
					
					String[] camposDataEntidade = dataEntidade.split(" < X < ");
					String[] camposDataTexto = dataTexto.split("X > ");
					
					Date data1Entidade = getStringAsDate(camposDataEntidade[0]);
					Date data2Entidade = getStringAsDate(camposDataEntidade[1]);
					
					Date data1Texto = getStringAsDate(camposDataTexto[1]);
					
					if (data1Entidade.before(data1Texto) && data2Entidade.before(data1Texto)){
						retorno = dataEntidade;
					}else if ((data1Entidade.before(data1Texto) || data1Entidade.equals(data1Texto)) && (data2Entidade.after(data1Texto) || data2Entidade.equals(data1Texto))){
						retorno = camposDataTexto[1] + " < X < " + camposDataEntidade[1];
					}else if (data1Entidade.after(data1Texto) && data2Entidade.after(data1Texto)){
						retorno = dataEntidade;
					}						
					
				}else if (dataEntidade.indexOf("X < ") != -1 && dataEntidade.indexOf(" < X < ") == -1 && dataTexto.indexOf(" < X < ") != -1){
					String[] camposDataEntidade = dataEntidade.split("X < ");
					String[] camposDataTexto = dataTexto.split(" < X < ");
					
					Date data1Entidade = getStringAsDate(camposDataEntidade[1]);
					
					Date data1Texto = getStringAsDate(camposDataTexto[0]);
					Date data2Texto = getStringAsDate(camposDataTexto[1]);
					
					if (data1Entidade.before(data1Texto)){
						retorno = dataEntidade;
						
					}else if ((data1Entidade.after(data1Texto) || data1Entidade.equals(data1Texto)) && (data1Entidade.before(data2Texto) || data1Entidade.equals(data2Texto) )){
						retorno = camposDataTexto[0] + " < X < " + camposDataEntidade[1];
					}else if (data1Entidade.after(data1Texto) && data1Entidade.after(data2Texto)){
						retorno = dataTexto;
					}					
					
				}else if (dataEntidade.indexOf("X > ") != -1 && dataEntidade.indexOf(" < X < ") == -1 && dataTexto.indexOf(" < X < ") != -1){
					String[] camposDataEntidade = dataEntidade.split("X > ");
					String[] camposDataTexto = dataTexto.split(" < X < ");
					
					Date data1Entidade = getStringAsDate(camposDataEntidade[1]);
					
					Date data1Texto = getStringAsDate(camposDataTexto[0]);
					Date data2Texto = getStringAsDate(camposDataTexto[1]);
					
					if (data1Entidade.before(data1Texto)){
						retorno = dataTexto;
					}else if ((data1Entidade.after(data1Texto) || data1Entidade.equals(data1Texto)) && (data1Entidade.before(data2Texto) || data1Entidade.equals(data2Texto))){
						retorno = camposDataEntidade[1] + " < X < " + camposDataTexto[1];					
					}else if (data1Entidade.after(data1Texto) && data1Entidade.after(data2Texto)){
						retorno = dataEntidade;
					}					
					
				}
				
				
			}else{
				retorno = dataEntidade;
			}
			
		}catch (Exception e){
			System.out.println("Data <"+dataEntidade+">possui formato não normalizado");
			retorno = dataEntidade;
		}
		
		return retorno;
		
		
	}
	

	
	private static void criaRelacionamentos(String nomeArquivoEntrada, String nomeArquivoOriginal, String nomeArquivoSemPrefixoExtensao){
		for (int i = 0; i < listaFrasesTemporaisTexto.size(); i++){
			System.out.println("-------------------");
			System.out.println("Processando frase ["+(i+1)+"] de ["+listaFrasesTemporaisTexto.size()+"]");
			System.out.println("-------------------");

			System.out.println("Processando: " + listaFrasesTemporaisTexto.get(i));
			System.out.println("Frase sem tag: " + getFraseSemTags(listaFrasesTemporaisTexto.get(i)));
			// george remover
			if (listaFrasesTemporaisTexto.get(i).indexOf(("<RISOTime_type=Pre-EBT>On_10_August_1904</RISOTime> :/: the/DT <RISOTime_type=DE>Russia</RISOTime>n/NNP fleet/NN again/RB attempted/VBD to/TO break/VB out/VB and/CC proceed/VB to/TO <RISOTime_type=DE>Vladivostok</RISOTime> :/: but/CC upon/IN reaching/VBG the/DT open/JJ sea/NN were/VBD confronted/VBN by/IN Admiral/NNP <RISOTime_type=DE>Togo</RISOTime> battleship/NN squadron/NN")) >= 0){
				System.out.println();
			}
			// george remover
			String[] listaAux = listaFrasesTemporaisTexto.get(i).split(ConstantsRisoTOT.TAG_RISO_TEMPORAL);
			int contaOcorrenciasTemporais = listaAux.length-1;
			if ( getFraseSemTags(listaFrasesTemporaisTexto.get(i)).indexOf("associated wars") >= 0){
				System.out.println();
			}
			
			switch (contaOcorrenciasTemporais) {

			case 0:
				String fraseSemTags = getFraseSemTags(listaFrasesTemporaisTexto.get(i));
				ArrayList<String> listaEntidadesFrase = getEntidadesFrases(fraseSemTags);
				
				
				
				// ArrayList<String> tagsTemporais = new ArrayList<String>();
				
				
				for (int j = 0; j < listaEntidadesFrase.size(); j++){
					
					String entidade = listaEntidadesFrase.get(j);
					
//					ArrayList<EntidadeEvento> listaEntidadesTempo = DBPediaDAO.getDatasEntidadesEventos(entidade);
					ArrayList<String> tagsTemporais = DBPediaDAO.buscaDataEntidades(entidade);
					
					for (int k = 0; k < tagsTemporais.size(); k++){
						
						addHashEntidadesDatas(listaEntidadesFrase.get(j), tagsTemporais);
						
					}
					
				}
				
				Set<String> chaves = hashEntidadesDatas.keySet();
				
				
				for (String chave : chaves){
					System.out.print(chave + ";");
					ArrayList<String> listaDatas = hashEntidadesDatas.get(chave);
					
					for (String data : listaDatas){
						System.out.print(data);
					}
					System.out.println("");
					
				}
				
				break;
				
				
			case 1:
				
				String fraseSemTagsCase1 = getFraseSemTags(listaFrasesTemporaisTexto.get(i));
				ArrayList<String> listaEntidadesFraseCase1 = getEntidadesFrases(fraseSemTagsCase1);
				
				
				ArrayList<String> listaEntDBPediaCase1 = getEntidadesTemporalizadasDBPedia(listaFrasesTemporaisTexto.get(i));
				
				
				
				String[] listaPalavrasCase1 = listaFrasesTemporaisTexto.get(i).split(" ");
				
				ArrayList<String> tagsTemporaisCase1 = new ArrayList<String>();
				
				boolean isDE = false;
				for (int j = 0; j < listaPalavrasCase1.length; j++){
					if(listaPalavrasCase1[j].contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
						String entidadeAux = getDataSemTag(listaPalavrasCase1[j]);
						if (listaPalavrasCase1[j].indexOf(ConstantsRisoTOT.TAG_DE) > -1){
							isDE = true;
						}
						tagsTemporaisCase1.add (entidadeAux); 
					}
				}
				
				if (isDE){
					listaEntidadesFraseCase1 = new ArrayList<String>();
					listaEntidadesFraseCase1.addAll(tagsTemporaisCase1);
				}
				
				for (int j = 0; j < listaEntidadesFraseCase1.size(); j++){
					addHashEntidadesDatas(listaEntidadesFraseCase1.get(j), tagsTemporaisCase1, listaEntDBPediaCase1);
					if (listaEntDBPediaCase1.contains(getDataSemTag(listaEntidadesFraseCase1.get(j)))){
						
						if (contaOcorrenciasDatasTotal(listaEntidadesFraseCase1.get(j), listaEntidadesFraseCase1) == contaOcorrenciasDE(listaEntidadesFraseCase1.get(j), listaEntidadesFraseCase1)){
//							ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(listaEntidadesFraseCase1.get(j));
							ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(listaEntidadesFraseCase1.get(j));
							
							
//							ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(listaEntidadesFraseCase1.get(j), listaEntidadesTemporalizadas);
							//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

							addHashEntidadesDatas(listaEntidadesFraseCase1.get(j), listaEntidadesTemporalizadas);							
						}
						
						
					}
				}
				
				Set<String> chavesCaseUm = hashEntidadesDatas.keySet();
				
				
//				for (String chave : chavesCaseUm){
//					System.out.print(chave + ";");
//					ArrayList<String> listaDatas = hashEntidadesDatas.get(chave);
//					
//					for (String data : listaDatas){
//						System.out.print(data);
//					}
//					System.out.println("");
//					
//				}
				
				break;

			default:
				
				String frase = listaFrasesTemporaisTexto.get(i);
				
				ArrayList<String> partesFrase = retornaFraseEmPartes(frase);
				ArrayList<String> listaEntidadesDaFrase = getEntidadesFrases(getFraseSemTags(frase));
				
				ArrayList<String> listaEntDBPedia = getEntidadesTemporalizadasDBPedia(listaFrasesTemporaisTexto.get(i));
				
				if (contaOcorrenciasTemporais == listaEntDBPedia.size() && listaEntDBPedia.size() > 0){
					for (String entidade : listaEntDBPedia){
						
						if (contaOcorrenciasDatasTotal(frase, listaEntidadesDaFrase) == contaOcorrenciasDE(frase, listaEntidadesDaFrase)){
							
							// george remover
							if(entidade.equals("Incheon")){
								System.out.println();
							}
							// george remover
//							ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidade);
							ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidade);
							
							// ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidade, listaEntidadesTemporalizadas);
							//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

							addHashEntidadesDatas(entidade, listaEntidadesTemporalizadas);
							
						}

						
					}
					
					break;
				}

				if (listaFrasesTemporaisTexto.get(i).toLowerCase().startsWith("after") || listaFrasesTemporaisTexto.get(i).toLowerCase().startsWith("before")){
					boolean leuData = false;
					ArrayList<String> listaEntidadesAux = new ArrayList<String>();
					ArrayList<String> listaDatasAux = new ArrayList<String>();
					
					int count = 0;
					ArrayList<String> listaDatas = new ArrayList<String>();
					for (String parte : partesFrase){
						count++;
//						if (count == partesFrase.size()){
//							
//							System.out.println("aqui");
//							
//						}
						
						if (parte.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
							String parteSemTags = getFraseSemTags(parte);
							ArrayList<String> listaEntidades = getEntidadesFrases(parteSemTags);
							if (contaOcorrenciasDatasTotal(frase, listaEntDBPedia) == contaOcorrenciasDE(frase, listaEntDBPedia)){
								listaDatas = retornaDatasFrase(parteSemTags, listaEntDBPedia, true);								
							}else{
								listaDatas = retornaDatasFrase(parteSemTags, listaEntDBPedia, false);								
							}
							if (count != partesFrase.size()){
								for (String data : listaDatas){
									String newData = "";
									if (listaFrasesTemporaisTexto.get(i).toLowerCase().startsWith("after") ){
										newData = substituiTypeDate(data, ConstantsRisoTOT.TAG_AFTER );										
									}else{
										newData = substituiTypeDate(data, ConstantsRisoTOT.TAG_BEFORE );																				
									}
									listaDatasAux.add(newData);
								}
							}else{
								listaDatasAux.addAll(listaDatas);								
							}
							
							if (count == partesFrase.size()){
								
								listaDatas = listaDatasAux;
								
							}
							if (!listaEntidadesAux.isEmpty()){
								listaEntidades.addAll(listaEntidadesAux);
								listaEntidadesAux = new ArrayList<String>();
							}
							for (String entidade : listaEntidades){
								addHashEntidadesDatas(entidade, listaDatas, listaEntDBPedia);
								if (listaEntDBPedia.contains(getDataSemTag(entidade))){
									if (contaOcorrenciasDatasTotal(frase, listaEntidades) == contaOcorrenciasDE(frase, listaEntidades)){
										//ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidade);
										ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidade);

										// ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidade, listaEntidadesTemporalizadas);
										//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

										addHashEntidadesDatas(entidade, listaEntidadesTemporalizadas);										
									}
									
								}
								
							}
							
							leuData = true;
						}else{
							leuData = false;
							String parteSemTags = getFraseSemTags(parte);
							ArrayList<String> listaEntidades = getEntidadesFrases(parteSemTags);
							listaEntidadesAux.addAll(listaEntidades); // guarda para utilizar depois
						}
						
						
					}
					if (!listaEntidadesAux.isEmpty()){
						for (String entidade : listaEntidadesAux){
							addHashEntidadesDatas(entidade, listaDatasAux, listaEntDBPedia);
							if (listaEntDBPedia.contains(getDataSemTag(entidade))){
								if (contaOcorrenciasDatasTotal(frase, listaEntidadesAux) == contaOcorrenciasDE(frase, listaEntidadesAux)){
									//ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidade);
									ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidade);
									
									//ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidade, listaEntidadesTemporalizadas);
									//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

									addHashEntidadesDatas(entidade, listaEntidadesTemporalizadas);
									
								}
								
							}
						}
						listaEntidadesAux = new ArrayList<String>();
					}
					
				}else{

					boolean leuData = false;
					boolean parteAtualSequencia = false;
					ArrayList<String> listaEntidadesAux = new ArrayList<String>();
					ArrayList<String> listaEntidadesAuxDatasEmSequencia = new ArrayList<String>();
					ArrayList<String> listaDatasPresentesEmPartesComDataAux = new ArrayList<String>();
					
					int count = 0;
					ArrayList<String> listaDatas = new ArrayList<String>();
					for (String parte : partesFrase){
						count++;
//						if (count == partesFrase.size()){
//							
//							System.out.println("aqui");
//							
//						}
						
						if (parte.equals(TAG_PONTO_E_VIRGULA)){
							
							for (String entidade : listaEntidadesAux){
								
								if (hashEntidadesDatas.get(entidade) == null){
									addHashEntidadesDatas(entidade, listaDatasPresentesEmPartesComDataAux, listaEntDBPedia);
									
								}else{
									ArrayList<String> datasAnteriores = hashEntidadesDatas.get(entidade);
									// datasAnteriores.addAll(listaDatas);
									ArrayList<String> aux = new ArrayList<String>();
									aux.addAll(datasAnteriores);
									aux.addAll(listaDatasPresentesEmPartesComDataAux);
									addHashEntidadesDatas(entidade, aux, listaEntDBPedia);
									if (listaEntDBPedia.contains(getDataSemTag(entidade))){
										
										if (contaOcorrenciasDatasTotal(frase, listaEntidadesAux) == contaOcorrenciasDE(frase, listaEntidadesAux)){
											//ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidade);
											ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidade);

											//ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidade, listaEntidadesTemporalizadas);
											//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

											addHashEntidadesDatas(entidade, listaEntidadesTemporalizadas);
											
										}
										
									}
									
											
								}
								
							}
							
							listaEntidadesAux = new ArrayList<String>();
							listaEntidadesAuxDatasEmSequencia = new ArrayList<String>();
							listaDatasPresentesEmPartesComDataAux = new ArrayList<String>();
							
							
						}else if (parte.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
							String parteSemTags = getFraseSemTags(parte);
							ArrayList<String> listaEntidades = getEntidadesFrases(parteSemTags);
							
							if (contaOcorrenciasDatasTotal(frase, listaEntDBPedia) == contaOcorrenciasDE(frase, listaEntDBPedia)){
								listaDatas = retornaDatasFrase(parteSemTags, listaEntDBPedia, true);								
							}else{
								listaDatas = retornaDatasFrase(parteSemTags, listaEntDBPedia, false);								
							}
							
							for (int w = 0; w < listaEntDBPedia.size(); w++){
								if (parteSemTags.contains(listaEntDBPedia.get(w))){
									if (!listaEntidades.contains(listaEntDBPedia.get(w))){
										listaEntidades.add(listaEntDBPedia.get(w));
									}
								}
							}
							listaEntidadesAuxDatasEmSequencia.addAll(listaEntidades);
							listaDatasPresentesEmPartesComDataAux.addAll(listaDatas);
							
							if (fraseSemEntidade(parte) && fraseSemVerbo(parte) || (parte.indexOf("<RISOTime_type=Pre-EMT>of") > -1 && parte.indexOf("<RISOTime_type=Pre-EMT>in".toUpperCase()) == -1
									&& parte.indexOf("<RISOTime_type=Pre-EMT>on".toUpperCase()) == -1)){
								parteAtualSequencia = true;
								
								ArrayList<String> listaEntidadesAuxDatasEmSequenciaCopy  = new ArrayList<String>();
										
								listaEntidadesAuxDatasEmSequenciaCopy.addAll(listaEntidadesAuxDatasEmSequencia);
								
								boolean processouDatasEmSequencia = false;
								for (String entidadeAnterior : listaEntidadesAuxDatasEmSequencia){
									processouDatasEmSequencia = true;
									String[] palavras = getFraseSemTags(parte).split(" ");
									
//									if (entidadeAnterior.equals("Budget Control Act")){
//										
//										System.out.println("aqui");
//									}
									for (int z = 0; z < palavras.length; z++){
										
										if (entidadeAnterior.indexOf(palavras[z]) > -1){
											if (palavras.length  > z+1){
												if (palavras[z+1].toUpperCase().indexOf("<RISOTime_type=Pre-EMT>of".toUpperCase()) > -1){
													listaEntidadesAuxDatasEmSequenciaCopy.remove(entidadeAnterior);
												}
												
											}
										}
									}
									listaEntidadesAuxDatasEmSequencia = listaEntidadesAuxDatasEmSequenciaCopy;
									
									if (listaEntidades.contains(entidadeAnterior) || listaEntidadesAuxDatasEmSequencia.contains(entidadeAnterior)){
										if (hashEntidadesDatas.get(entidadeAnterior) == null){
											addHashEntidadesDatas(entidadeAnterior, listaDatas, listaEntDBPedia);
											
										}else{
											ArrayList<String> datasAnteriores = hashEntidadesDatas.get(entidadeAnterior);
											// datasAnteriores.addAll(listaDatas);
											ArrayList<String> aux = new ArrayList<String>();
											aux.addAll(datasAnteriores);
											aux.addAll(listaDatas);
											addHashEntidadesDatas(entidadeAnterior, aux, listaEntDBPedia);
											if (listaEntDBPedia.contains(getDataSemTag(entidadeAnterior))){
												
												if (contaOcorrenciasDatasTotal(frase, listaEntidadesAuxDatasEmSequencia) == contaOcorrenciasDE(frase, listaEntidadesAuxDatasEmSequencia)){
													
													//ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidadeAnterior);
													ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidadeAnterior);

													// ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidadeAnterior, listaEntidadesTemporalizadas);
													//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

													addHashEntidadesDatas(entidadeAnterior, listaEntidadesTemporalizadas);
													
												}
												
												
												
											}
											
													
										}
										
									}
									
									
								}
								if (processouDatasEmSequencia){
									listaEntidades= new ArrayList<String>();
								}
							}else {
								parteAtualSequencia = false;
							}
							
							if (count == partesFrase.size()){
								
								// 20150520 - george.marcelo.alves - INICIO 
								listaDatas = new ArrayList<String>();
								for (String data : listaDatasPresentesEmPartesComDataAux){
									
									if (listaEntDBPedia.contains(getDataSemTag(data))){
										if (contaOcorrenciasDatasTotal(frase, listaEntDBPedia) == contaOcorrenciasDE(frase, listaEntDBPedia)){
//											ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(data);
											ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(data);
											//ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(data, listaEntidadesTemporalizadas);
											//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);		
											
											for (String dataFormatadaString : listaEntidadesTemporalizadas){
												listaDatas.add(dataFormatadaString);
											}											
										}
									}else{
										listaDatas.add(data);
									}
								}
								
								//listaDatas = listaDatasPresentesEmPartesComDataAux; 
								// 20150520 - george.marcelo.alves - FIM
								
							}
							
							ArrayList<String> listaFinal = new ArrayList<String>();
							
							// 20150520 - george.marcelo.alves - INICIO 
							// listaDatas = new ArrayList<String>();
							listaDatasPresentesEmPartesComDataAux = listaDatas;
							for (String data : listaDatasPresentesEmPartesComDataAux){
								// george remover
								if (data.indexOf("Japan") > 0){
									System.out.println();
								}
								// george remover
								if (listaEntDBPedia.contains(getDataSemTag(data))){
//									ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(data);
									ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(data);
									
									//ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(data, listaEntidadesTemporalizadas);
									//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);		
									
									for (String dataFormatadaString : listaEntidadesTemporalizadas){
										listaFinal.add(dataFormatadaString);
									}
								}else{
									listaFinal.add(data);
								}
							}
							
							fraseSemTagsCase1 = getFraseSemTags(listaFrasesTemporaisTexto.get(i));							
							
							if (fraseSemTagsCase1.startsWith("<RISOTime_type") && (count > 1)){ // pra funcionar o cenario do mesmo if la em baixo
								listaDatasPresentesEmPartesComDataAux = listaFinal;								
							}
							
							//listaDatas = listaDatasPresentesEmPartesComDataAux; 
							// 20150520 - george.marcelo.alves - FIM
							
							
							if(!parteAtualSequencia){
								if (!listaEntidadesAux.isEmpty()){
									listaEntidades.addAll(listaEntidadesAux);
									listaEntidadesAux = new ArrayList<String>();
								}
								
							}
							for (String entidade : listaEntidades){
								// george remover
								if (entidade.equals("caught")){
									System.out.println();
								}
								// george remover
								fraseSemTagsCase1 = getFraseSemTags(listaFrasesTemporaisTexto.get(i));
								if (fraseSemTagsCase1.startsWith("<RISOTime_type") && (count > 1)){ // eh o primeira iteracao do loop
									addHashEntidadesDatas(entidade, listaDatasPresentesEmPartesComDataAux, listaEntDBPedia);
								}else{
									addHashEntidadesDatas(entidade, listaDatas, listaEntDBPedia);	
								}
								
								if (listaEntDBPedia.contains(getDataSemTag(entidade))){
									if (contaOcorrenciasDatasTotal(frase, listaEntidades) == contaOcorrenciasDE(frase, listaEntidades)){
										//ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidade);
										ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidade);
										
										// ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidade, listaEntidadesTemporalizadas);
										//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

										addHashEntidadesDatas(entidade, listaEntidadesTemporalizadas);										
									}
									
									
								}
								
								
							}
							if (count != 1){ // nao eh primeira vez
								if (!listaEntidades.isEmpty()){
									listaDatasPresentesEmPartesComDataAux = new ArrayList<String>();
								}
																
							}
							
							leuData = true;
						}else{
							leuData = false;
							String parteSemTags = getFraseSemTags(parte);
							ArrayList<String> listaEntidades = getEntidadesFrases(parteSemTags);
							listaEntidadesAux.addAll(listaEntidades); // guarda para utilizar depois
							listaEntidadesAuxDatasEmSequencia.addAll(listaEntidades); // guarda para utilizar depois -- 2 - datas utilizado no fluxo de datas em sequencia
							
						}
						
						
					}
					if (!listaEntidadesAux.isEmpty()){
						for (String entidade : listaEntidadesAux){
							addHashEntidadesDatas(entidade, listaDatasPresentesEmPartesComDataAux, listaEntDBPedia);
							
							if (contaOcorrenciasDatasTotal(frase, listaEntidadesAuxDatasEmSequencia) == contaOcorrenciasDE(frase, listaEntidadesAuxDatasEmSequencia)){
								
							}
							
							if (listaEntDBPedia.contains(getDataSemTag(entidade))){
								
								// ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidade);
								ArrayList<String> listaEntidadesTemporalizadas = DBPediaDAO.buscaDataEntidades(entidade);

								//ArrayList<String> listaTempoEntidade = DBPediaDAO.montaEstruturaTemporal(entidade, listaEntidadesTemporalizadas);
								//ArrayList<String> listaTemporalFormatada = DBPediaDAO.formataEntidadesFinal(listaEntidadesTemporalizadas);

								addHashEntidadesDatas(entidade, listaEntidadesTemporalizadas);
								
							}
							
						}
						listaEntidadesAux = new ArrayList<String>();
					}
					
					
					
									
					
				}
				
				
//				if (frase.contains(":/:")){
//					String[] lista = frase.split(":/:");
//					for (int b = 0; b < lista.length; b++){
//						String oracao = lista[b];
//						if (oracao.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
//							if (!(oracao.trim()).startsWith("and")){
//								if (oracao.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
//									String[] listaPalavrasOracao = oracao.split(" ");
//									ArrayList<String> listaEntidadesRelacionadas = new ArrayList<String>();
//									String expressaoTemporal = "";
//									for (int j = 0; j < listaPalavrasOracao.length; j++){
//										String palavra = listaPalavrasOracao[j];
//										
//										if (!palavra.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
//											listaEntidadesRelacionadas.add(palavra);
//										}else{
//											expressaoTemporal = palavra;
//										}
//									}
//									
//									if (!listaEntidadesRelacionadas.isEmpty()){
//										System.out.print(expressaoTemporal+";");
//										for (int x = 0; x < listaEntidadesRelacionadas.size(); x++){
//											if (listaEntidadesTexto.contains(listaEntidadesRelacionadas.get(x)))
//											System.out.print(listaEntidadesRelacionadas.get(x) + " ");
//										}
//										System.out.println("");
//										
//										frase = frase.replace(oracao, "");
//										
//										
//									}
//									
//								}
//								
//							}
//						}
//					}
//				}
//				
//				if (frase.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
//					String[] listaPalavras1 = frase.split(" ");
//					HashMap<String, ArrayList<String>> mapa = new HashMap<String, ArrayList<String>>();
//					ArrayList<String> listaPalavrasAux = new ArrayList<String>();
//					ArrayList<String> listaDeDatas = new ArrayList<String>(); 
//					
//					boolean leuData = false;
//					String dataAtual = "";
//					boolean leuVerbo = false;
//					for (int x = 0; x < listaPalavras1.length; x++){
//						String palavra = listaPalavras1[x];
//						if (palavra.contains(ConstantsRisoTOT.TAG_RISO_TEMPORAL)){
//							if (listaPalavrasAux.size() == 0){
//								listaDeDatas.add(palavra);
//								dataAtual = palavra;
//								mapa.put(dataAtual, new ArrayList<String>());
//								leuData = true;								
//							}else{
//								if (leuVerbo){
//									dataAtual = palavra;
//									listaPalavrasAux.add(palavra);
//									mapa.put(dataAtual, (ArrayList<String>)listaPalavrasAux.clone());
//									listaPalavrasAux = new ArrayList<String>();
//									leuData = false;
//									leuVerbo = false;
//									
//								}else{
//									dataAtual = palavra;
//									listaPalavrasAux.add(palavra);									
//								}
//								
//							}
//						}else{
//							if (!leuData){
//								listaPalavrasAux.add(palavra);
//							}else{
//								if (verificaVerbo(palavra)){
//									leuVerbo = true;
//								}
//								listaPalavrasAux.add(palavra);
//							}
//						}
//					}
//					
//					if (!listaPalavrasAux.isEmpty()){
//						mapa.put(dataAtual, listaPalavrasAux);
//					}
//					for (int o = 0;o < listaDeDatas.size(); o++){
//						ArrayList<String> palavras = mapa.get(listaDeDatas.get(o));
//						if (!palavras.isEmpty()){
//							System.out.print(listaDeDatas.get(o)+";");
//							for (int h = 0; h < palavras.size(); h++){
//								System.out.print(palavras.get(h) + " ");
//							}
//							System.out.println("");
//								
//						}
//					}
//					
//					
//				}
				
				break;
			}
			
		}
	    SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddhh24mmss");
		String nomeArquivo = "Entidades_Datas_"+dt.format(new Date())+".csv";

        File file = new File("C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\"+nomeArquivo);  
        long begin = System.currentTimeMillis();  
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			
			
			for (String entity : hashEntidadesDatas.keySet()) {
				String line = entity + ";";
				String datasNaoNormalizadas = ""; 
				
				int cnt = 0;
		        for (String data : hashEntidadesDatas.get(entity)){
		        	cnt++;
		        	
		        	if (!data.isEmpty()){
		        		
		        		String dataNormalizada = DBPediaDAO.buscaDataNormalizada(getDataSemTag(data), nomeArquivoOriginal, data, entity);

		        		dataNormalizada = dataNormalizada.replaceAll(" > X < ", " < X < "); // corrige formato antigo e errado
		        		
			        	line = line.concat(dataNormalizada);

			        	datasNaoNormalizadas = datasNaoNormalizadas.concat(getDataSemTag(data));
			        	
			        	dataNormalizada = dataNormalizada.replace("[?]", "__");
			        	
			    		dataNormalizada = dataNormalizada.replace(" < X < ", "_to_");

			    		if (dataNormalizada.indexOf("_to_") == -1){
			    			if (dataNormalizada.split("-").length == 3){
			    				dataNormalizada = dataNormalizada + "_to_" + dataNormalizada;
			    			}
			    			
			    		}
			        	
			        	// inclusao na ontologia - inicio
			    		JenaOWL jena2 = new JenaOWL();
			    		OntModel model2;
			    		// try {
			    		try {
			    			model2 = jena2.lerOntologia(new File("C:\\Users\\george.marcelo.alves\\Documents\\RDF_RISOTCG\\"+entity+".txt").toURL().toString());
			    		} catch (Exception e) {
			    			model2 = jena2.criaOntologia("/c/en/"+entity, null);
			    		}
			    		
			    		if (model2.toString().indexOf(dataNormalizada) == -1){
				    		model2 = jena2.criaOntologia("/c/en/"+entity, "/r/HasDate ","/c/en/"+dataNormalizada, model2);			    			
			    		}
			    		// model2.write(System.out);
			    		FileOutputStream out2;
			    		try {
			    			File arquivoSaida = new File("C:\\Users\\george.marcelo.alves\\Documents\\RDF_RISOTCG\\"+entity+".txt");
			    			arquivoSaida.createNewFile();

			    			out2 = new FileOutputStream(arquivoSaida);
			    			model2.write(out2);
			    		} catch (IOException e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}			        	
			        	
			        	// inclusao na ontologia - fim
			        	
			        	
			        	
			        	if (hashEntidadesDatas.get(entity).size() != cnt){
				        	line = line.concat("|");
			        	}
			        	
			        	if (hashEntidadesDatas.get(entity).size() != cnt){
				        	datasNaoNormalizadas = datasNaoNormalizadas.concat("|");
			        	}
			        	

		        		
		        	}
		        	
		        }
		        if (cnt == 0){
		        	line = line + "Data Especial não mapeada na DBPedia";
		        }
		        writer.write(line+";"+datasNaoNormalizadas);
		        writer.newLine();  						
			}				

	        writer.flush(); 
	        writer.close(); 
	        
	        
	        
	        
	        // escreve ontologia arquivo - inicio
	        
	        
	        String[] camposAuxArquivo = nomeArquivoEntrada.split("\\\\");
	        
	        String nomeArquivoSemCaminho = camposAuxArquivo[camposAuxArquivo.length-1];
	        
//	        String nomeArquivoSemExtensao = nomeArquivoSemCaminho.replace(".txt", "");
//	        nomeArquivoSemExtensao = nomeArquivoSemExtensao.replace("saidaUnificada_TesteProfessor_", "");
//	        nomeArquivoSemExtensao = nomeArquivoSemExtensao.replaceAll("_", " ");
	        
	        boolean encontrouDataArquivo = false;
	        ArrayList<String> listaDatas = new ArrayList<String>();;
	        try {
	        	listaDatas = DBPediaDAO.recuperaEInsereDatasEntidadesEventos(nomeArquivoSemPrefixoExtensao.replace("_", " "));
	        	System.out.println("qtd retornada ["+nomeArquivoSemPrefixoExtensao+"] --- ["+listaDatas.size()+"]"); // george remover
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        String dataNomeArquivo;
	        if (null != listaDatas && listaDatas.size() > 0){
	        	dataNomeArquivo = listaDatas.get(0);
	        }else{
		        dataNomeArquivo = DBPediaDAO.buscaDataNormalizada(nomeArquivoSemPrefixoExtensao.replace("_", " "), nomeArquivoOriginal, "", "");
	        }
	        
	        boolean encontrou = false;
	        
	        
	        
	        
	        
	        if ((nomeArquivoOriginal.replace(".txt", "")).equals(dataNomeArquivo)){
	        	encontrou = true;
	        }
	        
	        
	        
			for (String entity : hashEntidadesDatas.keySet()) {
				
				int cnt = 0;
		        for (String data : hashEntidadesDatas.get(entity)){
		        	cnt++;
		        	
		        	if (!data.isEmpty()){
		        		
		        		String dataNormalizada = DBPediaDAO.buscaDataNormalizada(getDataSemTag(data), nomeArquivoOriginal, data, entity);
		        		
		        		
		        		dataNormalizada = getDataPosCompare(dataNormalizada, dataNomeArquivo);

		        		dataNormalizada = dataNormalizada.replaceAll(" > X < ", " < X <"); // corrige formato antigo e errado
		        		
			        	dataNormalizada = dataNormalizada.replace("[?]", "__");
			        	// inclusao na ontologia - inicio
			    		JenaOWL jena2 = new JenaOWL();
			    		OntModel model2;
			    		// try {
			    		try {
//			    			model2 = jena2.lerOntologia(new File("C:\\workspace_mestrado\\riso-master\\reuters\\results\\"+nomeArquivoSemCaminho+".txt").toURL().toString());
			    			model2 = jena2.lerOntologia(new File("C:\\Users\\george.marcelo.alves\\Documents\\RDF_RISOTCG\\"+nomeArquivoSemCaminho).toURL().toString());
			    			
			    		} catch (Exception e) {
			    			model2 = jena2.criaOntologia("/c/en/"+nomeArquivoSemCaminho, null);
			    		}
			    		
			    		dataNormalizada = dataNormalizada.replace(" < X < ", "_to_");
			    		
			    		
			    		if (dataNormalizada.indexOf("_to_") == -1){
			    			if (dataNormalizada.split("-").length == 3){
			    				dataNormalizada = dataNormalizada + "_to_" + dataNormalizada;
			    			}
			    			
			    		}
			    		System.out.println(dataNormalizada);
			    		if (model2.toString().indexOf(dataNormalizada) == -1){
				    		model2 = jena2.criaOntologia("/c/en/"+entity, "/r/HasDate ","/c/en/"+dataNormalizada, model2);			    			
			    		}
			    		// model2.write(System.out);
			    		FileOutputStream out2;
			    		try {
			    			File arquivoSaida = new File("C:\\Users\\george.marcelo.alves\\Documents\\RDF_RISOTCG\\"+nomeArquivoSemCaminho);
			    			arquivoSaida.createNewFile();

			    			out2 = new FileOutputStream(arquivoSaida);
			    			model2.write(out2);
			    		} catch (IOException e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}			        	
			        	
			        	// inclusao na ontologia - fim
			        			        		
		        	}
		        	
		        }
			}		        
	        
	        // escreve ontologia arquivo - fim

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	public static HashMap entidadesDbpedia = new HashMap();
	
	private static void extracaoDeFrases(){
		listaTodasAsFrasesTexto = conteudo.split("\\./.");
		System.out.println("Texto possui ["+ (listaTodasAsFrasesTexto.length - 1) +"] frases.");
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
		BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));   
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
		
//		getDataSemTag("<RISOTime_type=D-EMT-Adv> X > 1204<RISOTime>");
		
		// retornaDatasFrase("<RISOTime type=Pre-EBT>in_January</RISOTime> oi sdfasf <RISOTime type=Pre-EBT>in_January</RISOTime> <data>");
		// getEntidadesTemporalizadasDBPedia("<RISOTime type=DE>Paris</RISOTime> oi asdsad <RISOTime type=DE>Recife</RISOTime>");
		
//		ArrayList<EntidadeEvento> listaEnt = new ArrayList<EntidadeEvento>();
//		Cidade cidade1 = new Cidade();
//		cidade1.setNome("Alianca");
//		listaEnt.add(cidade1);
//		ArrayList<String> listaTempoEntidade1 = new ArrayList<String>();
//		listaTempoEntidade1.add("January, 10");
//		DBPediaDAO.insereDataEntidade("teste",listaEnt , listaTempoEntidade1);
		
		//System.out.println(retornaFraseEmPartes("You/PRP and/CC me/PRP ;/: or/CC Me/PRP and/CC You/PRP but/CC without/IN :/: nobody/NN"));

//		ArrayList<String> lista = retornaDatasFrase("In/IN North/NNP Africa/NNP :/: the/DT Germans/NNPS launched/VBN an/DT offensive/JJ <RISOTime_type=Pre-EBT>in_January</RISOTime> :/: pushing/VBG the/DT British/JJ back/IN to/TO positions/NNS at/IN the/DT Gazala/NNP Line/NNP <RISOTime_type=EPT-EBT>by_early_February</RISOTime> followed/VBD by/IN temporary/JJ lull/NN in/IN combat/NN which/WDT Germany/NNP used/VBD to/TO prepare/VB for/IN their/PRP$ upcoming/JJ offensives/NNS and <RISOTime_type=EPT-EBT>In_early_May_1942</RISOTime> the/DT tide/NN turns/VBZ Japan/NNP initiated/VBD operations/NNS to/TO capture/VB Port/NNP Moresby/NNP by/IN amphibious/JJ assault/NN and/CC thus/RB sever/VB communications/NNS and/CC supply/NN lines/NNS between/IN the/DT");
//		String testando = "george marcelo george marcelo";
//		String[] lista = testando.split("or.*[^or.*e]e");
//		System.out.println(lista);
		
//		listaFrasesTemporaisTexto.add("After/IN being/VBG captured/VBN by/IN the/DT Fourth/NNP Crusade/NNP in/IN <RISOTime_type=EPT-EBT>1204</RISOTime>and/CC then/RB recaptured/VBN by/IN the/DT forces/NNS of/IN Nicaea/NNP :/: under/IN the/DT command/NN of/IN Michael/NNP VIII/NNP Palaeologus/NNP in/IN <RISOTime_type=EPT-EBT>1261</RISOTime>:/: Constantinople/NNP and/CC the/DT Byzantine/NNP Empire/NNP were/VBD taken/VBN by/IN the/DT Ottoman/NNP Empire/NNP <RISOTime_type=EPT-EBT>May, 1453</RISOTime> ./.");
//		listaFrasesTemporaisTexto.add("Byzantine/NNP Constantinople/NNP had/VBD been/VBN the/DT capital/NN of/IN Christianity/NNP :/: the/DT successor/NN of/IN ancient/JJ Greece/NNP and/CC Rome/NNP ./.");
//
//		// listaFrasesTemporaisTexto.add("After/IN she/PRP died/VBD <RISOTime type=Pre-EMT>in 1847</RISOTime> and/CC Johann/NNP Georg/NNP Hiedler/NNP <RISOTime type=Pre-EMT>in 1856</RISOTime> :/: Alois/NNP was/VBD brought/VBN up/IN in/IN the/DT family/NN of/IN Hiedler/NNP brother/NN :/: Johann/NNP Nepomuk/NNP Hiedler/NNP ./.");
//
//		//listaFrasesTemporaisTexto.add("As/IN Napoleon/NNP :/: he/PRP was/VBD Emperor/NNP of/IN the/DT French/JJ <RISOTime type=Pre-EMT>from 1804 to 1814</RISOTime> and/CC again/RB <RISOTime type=Pre-EMT>in 1815</RISOTime> ./.");

		 // carregaEntidadestexto("C:\\Users\\George\\Dropbox\\SaidaMontyExtractor\\Teste4_p_entities.txt");
		//carregaEntidadestexto("C:\\Users\\George\\Dropbox\\Documentos_MoutyLingua\\Teste3.txt");
		
//		criaRelacionamentos();
        System.out.println("RISO-TT 1.0 - georgealves@copin.ufcg.edu.br");
		String nomeArquivo = "";
		String caminhoArquivoEntidades = "";
		Scanner scanner = new Scanner(System.in);
		System.out.print("Digite o nome do arquivo que sera lido: ");
		//nomeArquivo = "C:\\Users\\George\\Dropbox\\Documentos_MoutyLingua\\Teste5.txt";
		
//		nomeArquivo = "C:\\Users\\George\\Dropbox\\RISOTCG_saida\\saidaUnificada2.txt";
//		caminhoArquivoEntidades = "C:\\Users\\George\\Dropbox\\SaidaMontyExtractor\\Teste7_entities.txt";
//		nomeArquivo = "C:\\Users\\George\\Dropbox\\RISOTCG_saida\\saidaUnificada1.txt";
//		caminhoArquivoEntidades = "C:\\Users\\George\\Dropbox\\SaidaMontyExtractor\\Teste6_entities.txt";
		// nomeArquivo = scanner.nextLine();
		//nomeArquivo = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\SaidaUnificadaExSlide.txt";
//		nomeArquivo = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_RussoJap.txt";
//		nomeArquivo = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_TesteProfessor.txt";
//		nomeArquivo = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_NapoleonReduzida.txt";
		nomeArquivo = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida\\saidaUnificada_Napoleon_final.txt";
		String nomeArquivoOriginal = "Napoleon.txt";
		String nomeArquivoSemPrefixo = "Napoleon";
		
		
		
		
		//nomeArquivo = "C:\\Users\\george.marcelo.alves\\Dropbox\\RISOTCG_saida_linhas\\26	.txt";
		// 04_AmRevWarReduzida_entities
//		caminhoArquivoEntidades = "C:\\Users\\george.marcelo.alves\\Dropbox\\SaidaMontyExtractor\\TesteProfessor_entities.txt";
		//caminhoArquivoEntidades = "C:\\Users\\george.marcelo.alves\\Dropbox\\SaidaMontyExtractor\\TestesProfessor_entities.txt";
		
		//caminhoArquivoEntidades = "C:\\Users\\george.marcelo.alves\\Dropbox\\SaidaMontyExtractor\\04_AmRevWarReduzida_entities.txt";
		// Para rodar esse modo de execucao, o montylingua devera ter extraido os termos do texto previamente.
		// Lembrar de alterar o nome dos arquivos e seus respectivos caminhos.
		
		if (args.length >= 3){
			if (args[0].equals("-extraiTempoEntidades")){
//				try {
//					leituraDoArquivo(nomeArquivo);
					caminhoArquivoEntidades = "C:\\Users\\george.marcelo.alves\\Dropbox\\SaidaMontyExtractor\\"+ args[3];
					carregaEntidadestexto(caminhoArquivoEntidades);
					
					int qtdEntidades = listaEntidadesTexto.size();
					System.out.println("Quantidade de Entidades: "+qtdEntidades);

					int inicio = new Integer(args[1]).intValue();
					int fim = new Integer(args[2]).intValue();
					File arquivoDeControle = new File("C:\\workspace_mestrado\\riso-tcg\\ctrl\\"+args[1]);
					try {
						arquivoDeControle.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					for (int j = inicio; j < listaEntidadesTexto.size(); j++){
						
						if (j <= fim){
							String entidade = listaEntidadesTexto.get(j);
							
							System.out.println("Processando entidade "+(j+1)+" de "+qtdEntidades + " -- Thread INICIO = "+inicio+" FIM = "+fim+".");
							//if (i >= 1440){
								boolean processou = false;
								while (!processou){
									try{
										DBPediaDAO.recuperaEInsereDatasEntidadesEventos(entidade);					
										processou = true;
									}catch (Exception e){
										System.out.println("Erro na consulta. Vai ser realizada uma nova tentativa");
									}
									
								}
							//}
							
						}else{
							System.out.println("Thread finalizada!");
							break;
						}
//						j++;

					}
					try{
						addNoArquivoDatasEspeciaisRisoTT(entidadesTemporalizadas, new Integer(args[1]).intValue());											
					}catch (Exception e){
						System.out.println("Erro ao adicionar no arquivo datas_especiais.xml" + e.getMessage());
						e.printStackTrace();
					}

//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
		}else{
			
			// sem leitura do arquivo - INICIO 
			//try {
				//leituraDoArquivo(nomeArquivo);
				
				// descomentar
				//carregaEntidadestexto(caminhoArquivoEntidades);
				//extracaoDeFrases();
				//criaRelacionamentos();
				// descomentar
				
				
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			// sem leitura do arquivo - FIM

			caminhoArquivoEntidades = "C:\\Users\\george.marcelo.alves\\Dropbox\\SaidaMontyExtractor\\"+ args[0];
				
			// com leitura do arquivo - INICIO 
			try {
				leituraDoArquivo(nomeArquivo);
				carregaEntidadestexto(caminhoArquivoEntidades);
				extracaoDeFrases();
				criaRelacionamentos(nomeArquivo, nomeArquivoOriginal, nomeArquivoSemPrefixo);
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// com leitura do arquivo - FIM
				
				
		}
		
		
	}
}
