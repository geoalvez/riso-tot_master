package br.edu.ufcg.copin.riso.tot.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.edu.ufcg.copin.riso.tot.constants.ConstantsRisoTOT;
import br.edu.ufcg.copin.riso.tot.entities.Cidade;
import br.edu.ufcg.copin.riso.tot.entities.Empresa;
import br.edu.ufcg.copin.riso.tot.entities.EntidadeEvento;
import br.edu.ufcg.copin.riso.tot.entities.Estado;
import br.edu.ufcg.copin.riso.tot.entities.Evento;
import br.edu.ufcg.copin.riso.tot.entities.Feriado;
import br.edu.ufcg.copin.riso.tot.entities.Instituicao;
import br.edu.ufcg.copin.riso.tot.entities.Local;
import br.edu.ufcg.copin.riso.tot.entities.Pais;
import br.edu.ufcg.copin.riso.tot.entities.Pessoa;
import br.edu.ufcg.copin.riso.tot.main.RisoTotMain2;
import br.edu.ufcg.copin.riso.tot.utils.RisoTcgUtil;
import br.edu.ufcg.copin.tot.connection.postgres.DBConexion;
import br.edu.ufcg.copin.tot.connection.postgres.PostgresConnectionManager;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class DBPediaDAO {
	


	public static ArrayList<String> montaEstruturaTemporal(String entidadeTexto, ArrayList<EntidadeEvento> listaEntidadesTempo){
		ArrayList<String> listaRetorno = new ArrayList<String>();
		
		// ArrayList<EntidadeEvento> listaEntidadesTempo = DBPediaDAO.getDatasEntidadesEventos(entidadeTexto);
		
		for (EntidadeEvento ent:listaEntidadesTempo ){
			
			if (ent instanceof Cidade){
				Cidade cidade = (Cidade) ent;
				if (!cidade.getFoundingDate().isEmpty()){
					if (!listaRetorno.contains(cidade.getFoundingDate())){
						listaRetorno.add(cidade.getFoundingDate());
					}
					
				}
				
			}else if (ent instanceof Empresa){
				Empresa empresa = (Empresa) ent;
				if (!empresa.getFoundingDate().isEmpty()){
					if (!listaRetorno.contains(empresa.getFoundingDate())){
						listaRetorno.add(empresa.getFoundingDate());
					}
					
				}
			}else if (ent instanceof Estado){
				Estado estado = (Estado) ent;
				if (!estado.getAdmittanceDate().isEmpty()){
					if (!listaRetorno.contains(estado.getAdmittanceDate())){
						listaRetorno.add(estado.getAdmittanceDate());
					}					
				}
				
			}else if (ent instanceof Feriado){
				Feriado feriado = (Feriado) ent;
				if (!feriado.getDate().isEmpty()){
					if (!listaRetorno.contains(feriado.getDate())){
						listaRetorno.add(feriado.getDate());
					}
					
				}
				
				
			}else if (ent instanceof Instituicao){
				Instituicao instituicao  = (Instituicao) ent;
				if (!listaRetorno.contains(instituicao.getEstablished())){
					listaRetorno.add(instituicao.getEstablished());
				}
			}else if (ent instanceof Local){
				Local local = (Local) ent;
				if (!local.getCreated().isEmpty()){
					if (!listaRetorno.contains(local.getCreated())){
						listaRetorno.add(local.getCreated());
					}
					continue;
				}
				
				if (!local.getOpened().isEmpty()){
					if (!listaRetorno.contains(local.getOpened())){
						listaRetorno.add(local.getOpened());
					}
					continue;
				}
				
				if (!local.getOpening().isEmpty()){
					if (!listaRetorno.contains(local.getOpening())){
						listaRetorno.add(local.getOpening());
					}
					continue;
					
				}
				
				if (!local.getOpeningDate().isEmpty()){
					if (!listaRetorno.contains(local.getOpeningDate())){
						listaRetorno.add(local.getOpeningDate());
					}
					continue;
				}
				
				if (!local.getStartDate().isEmpty()){
					if (!listaRetorno.contains(local.getStartDate())){
						listaRetorno.add(local.getStartDate());
					}
					continue;
				}
				
				if (!local.getCompletionDate().isEmpty()){
					if (!listaRetorno.contains(local.getCompletionDate())){
						listaRetorno.add(local.getCompletionDate());
					}
					continue;
				}
				
				
			}else if (ent instanceof Pais){
				Pais pais = (Pais) ent;
				
				if (pais.getEstablishedDate()!=null && !pais.getEstablishedDate().isEmpty()){
					if (!listaRetorno.contains(pais.getEstablishedDate())){
						listaRetorno.add(pais.getEstablishedDate());
					}
					continue;
				}
				
				if (pais.getFoundingDate()!=null && !pais.getFoundingDate().isEmpty()){
					if (!listaRetorno.contains(pais.getFoundingDate())){
						listaRetorno.add(pais.getFoundingDate());
					}
					continue;
				}
				
				
			}else if (ent instanceof Pessoa){
				Pessoa pessoa = (Pessoa) ent;
				
				String dataASerAdicionada = "";
				
				if (!pessoa.getBirthDate().isEmpty()){
					dataASerAdicionada = pessoa.getBirthDate(); 
				}
				
				if (!pessoa.getDeathDate().isEmpty()){
					if (!pessoa.getBirthDate().isEmpty()){
						dataASerAdicionada = pessoa.getDeathDate() + " < X < " + dataASerAdicionada;
					}else{
						dataASerAdicionada = pessoa.getDeathDate();
					}
				}
				
				if (dataASerAdicionada.isEmpty()){
					if (!pessoa.getActiveYearsStartDate().isEmpty()){
						dataASerAdicionada = pessoa.getActiveYearsStartDate();
					}
					
					if (!pessoa.getActiveYearsEndDate().isEmpty()){
						if (!pessoa.getActiveYearsStartDate().isEmpty()){
							dataASerAdicionada = pessoa.getActiveYearsEndDate() + " < X < "+dataASerAdicionada;
						}
					}
					
				}
				
				if (dataASerAdicionada.isEmpty()){
					if (!pessoa.getActiveYearsStartYear().isEmpty()){
						dataASerAdicionada = pessoa.getActiveYearsStartYear();
					}
					
					if (!pessoa.getActiveYearsEndYear().isEmpty()){
						if (!pessoa.getActiveYearsStartYear().isEmpty()){
							dataASerAdicionada = pessoa.getActiveYearsEndYear() + " < X < "+dataASerAdicionada;
						}
					}
					
				}
//				if (!listaTempoEntidade.contains(pessoa.getActiveYearsEndDate())){
//					listaTempoEntidade.add(pessoa.getActiveYearsEndDate());
//				}
				
				if (!listaRetorno.contains(dataASerAdicionada)){
					listaRetorno.add(dataASerAdicionada);
				}
				
			}else if (ent instanceof Evento){
				Evento evento = (Evento) ent;
				
				if (!listaRetorno.contains(evento.getPeriodDate())){
					listaRetorno.add(evento.getPeriodDate());
				}
				
				
			}
		}
		
		return listaRetorno;
	}
	
	
	public static void recuperaEInsereDatasEntidadesEventos (String entidadeTexto) throws Exception{
		ArrayList<EntidadeEvento> listaEntidadesTemporalizadas = DBPediaDAO.getDatasEntidadesEventos(entidadeTexto);

		ArrayList<String> listaTempoEntidade = montaEstruturaTemporal(entidadeTexto, listaEntidadesTemporalizadas);
		
		
		
		if (!listaEntidadesTemporalizadas.isEmpty()){
		
			if (!RisoTotMain2.entidadesTemporalizadas.contains(entidadeTexto)){
				RisoTotMain2.entidadesTemporalizadas.add(entidadeTexto);
			}
			
			
		}
			
			
	
		//TODO - Verificar quais datas serão realmente importante para as entidades
		if (!listaEntidadesTemporalizadas.isEmpty()){
			DBPediaDAO.formataEInsereDataEntidade(entidadeTexto, listaEntidadesTemporalizadas, listaTempoEntidade);			
		}
	}
	
	public static String obtemDemaisFormatos(String data){
		String retorno = "";
		
		String dia = "";
		String mes = "";
		String ano = "";
		
		String[]  campos = data.split(" ");
		
		if (campos.length > 1){
			boolean mesOk = false;
			for (int i = 0; i < ConstantsRisoTOT.MESES.length; i++){
				if (campos[0].toLowerCase().equals(ConstantsRisoTOT.MESES[i])){
					mesOk = true;
					mes = new Integer(i).toString();					
				}
			}
			
			if (mesOk){
				ano = campos[1].replace("@en", ""); 
				ano = ano.replace("@e", ""); 
				retorno = ano + "-" + mes + "-[?]";
			}
		
		}else{
			if (data.length() == 4){
				retorno = data + "-[?]-[?]";
			}else if (data.length() == 5){
				retorno = "[?]-"+data;
			}
		}
		
		if (retorno.isEmpty()){
			retorno = data;
		}
		return retorno;
	}
	
	public static ArrayList<String> formataEntidadesFinal (ArrayList<String> listaTemporal){
		ArrayList<String> listaRetorno = new ArrayList<String>();
		
		String dataAux = "";
		for (String data: listaTemporal){
			dataAux = data;
			//"2014-09-12 to 2014-09-12"
			if (!data.isEmpty()){
				String dataFormatada1 = data.substring(0, 10);
				dataFormatada1 = dataFormatada1.split("\\^\\^")[0];
				
				dataFormatada1 = dataFormatada1.replace("@en", "");
				
				if (dataFormatada1.split("-").length == 1){
					
					if (dataFormatada1.split(" ").length == 1){
						if (dataFormatada1.length() >= 8){
							dataFormatada1 = dataFormatada1.substring(0, 4)+"-"+dataFormatada1.substring(4, 6)+"-"+dataFormatada1.substring(6, 8);								
						}else{
							dataFormatada1 = dataFormatada1+"-[?]-[?]";															
						}
					}else{
							dataFormatada1 = data.split(" < X < ")[0];
							dataFormatada1 = dataFormatada1.replace("@en", "");

							if (!obtemDemaisFormatos(dataFormatada1).isEmpty()){
								dataFormatada1 = obtemDemaisFormatos(dataFormatada1);
							}else{
								if (dataFormatada1.split(" ").length > 1){
									String ano = "[?]";
									String mes = "[?]";
									String dia = "[?]";
									
									String[] camposAux = dataFormatada1.split(" ");
									for (int j = 0; j < camposAux.length; j++ ){
										boolean ehAno = false;
										boolean ehMes = false;
										boolean ehDia = false;
										for (int k = 0; k < ConstantsRisoTOT.MESES.length; k++){
											if (camposAux[j].toLowerCase().equals(ConstantsRisoTOT.MESES[k])){
												ehMes = true;
												mes = new Integer(k).toString();
											}
											
										}
										if (ehMes){
											continue;
										}
										if (!ehMes){
											boolean mesOk = false;
											try{
												Integer mesAux = new Integer(camposAux[j]);
												if (mesAux <= 12){
													mesOk = true;
													mes = camposAux[j];
													ehMes= true;
												}
											}catch (Exception e){
												mesOk = false;
											}
										}
										if (ehMes){
											continue;
										}
										if (!ehMes){
											boolean diaOk = false;
											try{
												Integer diaAux = new Integer(camposAux[j]);
												if (diaAux <= 31){
													diaOk = true;
													ehDia = true;
													dia = camposAux[j];
												}
											}catch (Exception e){
												diaOk = false;
											}
										}
										if (ehDia){
											continue;
										}
										if (!ehDia){
											ano = camposAux[j];
										}
										

									}
									dataFormatada1 = ano = "-" + mes + "-" + dia;
								}
								
							}
					}
					
				}else{
					if (!obtemDemaisFormatos(dataFormatada1).isEmpty()){
						dataFormatada1 = obtemDemaisFormatos(dataFormatada1);
					}else{
						String[] camposAux = dataFormatada1.split("-");
						if (camposAux.length == 3){
							if (camposAux[0].length() <= 2){
								dataFormatada1 = camposAux[2] + "-" + camposAux[1] + "-" + camposAux[0];
							}
						}
					}
					
				}
				
				String dataFormatada2 = "";
				
				if (data.indexOf(" < X < ")>0){
					int posFim = data.indexOf("@", data.indexOf(" < X < "));
					if (posFim == -1){
						posFim = data.indexOf(" < X < ")+7+10;
					}
					
					dataFormatada2 = data.substring(data.indexOf(" < X < ")+7, posFim);
					dataFormatada2 = dataFormatada2.split("\\^\\^")[0];
					dataFormatada2 = dataFormatada2.replace("@en", "");

					if (dataFormatada2.split("-").length == 1){
						
						if (dataFormatada2.split(" ").length == 1){
							if (dataFormatada2.length() >= 8){
								dataFormatada2 = dataFormatada2.substring(0, 4)+"-"+dataFormatada2.substring(4, 6)+"-"+dataFormatada2.substring(6, 8);								
							}else{
								dataFormatada2 = dataFormatada2 + "-[?]-[?]";
							}
						}else{
								dataFormatada2 = data.split(" < X < ")[0];
								dataFormatada2 = dataFormatada1.replace("@en", "");

								if (!obtemDemaisFormatos(dataFormatada2).isEmpty()){
									dataFormatada2 = obtemDemaisFormatos(dataFormatada2);
								}else{
									if (dataFormatada2.split(" ").length > 1){
										String ano = "[?]";
										String mes = "[?]";
										String dia = "[?]";
										
										String[] camposAux = dataFormatada2.split(" ");
										for (int j = 0; j < camposAux.length; j++ ){
											boolean ehAno = false;
											boolean ehMes = false;
											boolean ehDia = false;
											for (int k = 0; k < ConstantsRisoTOT.MESES.length; k++){
												if (camposAux[j].toLowerCase().equals(ConstantsRisoTOT.MESES[k])){
													ehMes = true;
													mes = new Integer(k).toString();
												}
												
											}
											if (ehMes){
												continue;
											}
											if (!ehMes){
												boolean mesOk = false;
												try{
													Integer mesAux = new Integer(camposAux[j]);
													if (mesAux <= 12){
														mesOk = true;
														mes = camposAux[j];
														ehMes= true;
													}
												}catch (Exception e){
													mesOk = false;
												}
											}
											if (ehMes){
												continue;
											}
											if (!ehMes){
												boolean diaOk = false;
												try{
													Integer diaAux = new Integer(camposAux[j]);
													if (diaAux <= 31){
														diaOk = true;
														ehDia = true;
														dia = camposAux[j];
													}
												}catch (Exception e){
													diaOk = false;
												}
											}
											if (ehDia){
												continue;
											}
											if (!ehDia){
												ano = camposAux[j];
											}
											

										}
										dataFormatada2 = ano = "-" + mes + "-" + dia;
									}
									
								}
						}
						
					}else{
						if (!obtemDemaisFormatos(dataFormatada2).isEmpty()){
							dataFormatada2 = obtemDemaisFormatos(dataFormatada1);
						}else{
							String[] camposAux = dataFormatada2.split("-");
							if (camposAux.length == 3){
								if (camposAux[0].length() <= 2){
									dataFormatada2 = camposAux[2] + "-" + camposAux[1] + "-" + camposAux[0];
								}
							}
						}
						
					}
					
				}
				
//				dataFormatada2 = dataFormatada2.split("\\^\\^")[0];
//				String[] campos1 = dataFormatada1.split(" ");
//				
//				if (campos1.length == 2){
//					dataFormatada1 = data.substring(0, data.indexOf("@en < X < "));
//					
//					if (dataFormatada1.split("-").length == 1){
//						if (dataFormatada1.length() >= 8){
//							dataFormatada1 = dataFormatada1.substring(0, 4)+"-"+dataFormatada1.substring(4, 6)+"-"+dataFormatada1.substring(6, 8);	
//						}
//						
//					}else{
//						if (!obtemDemaisFormatos(dataFormatada1).isEmpty()){
//							dataFormatada1 = obtemDemaisFormatos(dataFormatada1);
//						}
//						
//					}
//					
//					campos1 = dataFormatada1.split(" ");
//					
//					Integer aux = null;
//					boolean conversaoSucedida = false;
//					
//					try{
//						aux = new Integer(campos1[1]);	
//						conversaoSucedida = true;
//					}catch(NumberFormatException e){
//						conversaoSucedida = false;
//					}
//					
//					if (conversaoSucedida){
//						String mes = "";
//						
//						String mesAsString = campos1[0];
//						Integer mesAsInteger = null;
//						
//						boolean mesEhInteiro = false;
//						try{
//							mesAsInteger = new Integer(mesAsString);
//							mesEhInteiro = true;
//							
//						}catch (NumberFormatException e){
//							mesEhInteiro = false;
//						}
//						
//						if (!mesEhInteiro){
//							
//							mes = RisoTcgUtil.getNumMes(mesAsString);
//						}
//						if (aux > 12){
//							dataFormatada1 = aux + "-" + mes+"-[?]";
//						}else{
//							dataFormatada1 = "[?]-"+mes+"-"+aux;
//						}
//						
//					}
//
//				}else if (campos1.length == 1){
//					
//					if (campos1[0].split("-").length == 1){
//						dataFormatada1 = campos1[0] + "-[?]-[?]";
//					}
//					
//				}
////				String[] campos2 = dataFormatada2.split(" ");
//
//				if (!dataFormatada2.isEmpty()){
//					String[] campos2 = dataFormatada2.split(" ");
//					
//					if (campos2.length == 2){
//						
//						dataFormatada2 = data.substring(data.indexOf(" < X < ")+7, data.indexOf("@en", data.indexOf(" < X < ")));
//						
//						if (dataFormatada2.split("-").length == 1){
//							if (dataFormatada2.length() >= 8){
//								dataFormatada2 = dataFormatada1.substring(0, 4)+"-"+dataFormatada2.substring(4, 6)+"-"+dataFormatada2.substring(6, 8);
//							}else{
//								if (!obtemDemaisFormatos(dataFormatada2).isEmpty()){
//									dataFormatada2 = obtemDemaisFormatos(dataFormatada2);
//								}
//								
//							}
//							
//						}
//						
//						campos2 = dataFormatada2.split(" ");
//						
//						Integer aux = null;
//						boolean conversaoSucedida = false;
//						
//						try{
//							aux = new Integer(campos2[1]);	
//							conversaoSucedida = true;
//						}catch(NumberFormatException e){
//							conversaoSucedida = false;
//						}
//						
//						if (conversaoSucedida){
//							String mes = "";
//							
//							String mesAsString = campos1[0];
//							Integer mesAsInteger = null;
//							
//							boolean mesEhInteiro = false;
//							try{
//								mesAsInteger = new Integer(mesAsString);
//								mesEhInteiro = true;
//								
//							}catch (NumberFormatException e){
//								mesEhInteiro = false;
//							}
//							
//							if (!mesEhInteiro){
//								
//								mes = RisoTcgUtil.getNumMes(mesAsString);
//							}
//							if (aux > 12){
//								dataFormatada2= aux + "-" + mes+"-[?]";
//							}else{
//								dataFormatada2 = "[?]-"+mes+"-"+aux;
//							}
//							
//						}
//
//					}else if (campos2.length == 1){
//						if (campos2[0].split("-").length == 1){
//							dataFormatada2 = campos1[0] + "-[?]-[?]";	
//						}
//						
//					}
//					
//					
//				}
				
				
				
				
				String dataFormatadaFinal = "";
				while (dataFormatada1.startsWith("-")){
					dataFormatada1 = dataFormatada1.substring(1);
				}
				
				while (dataFormatada2.startsWith("-")){
					dataFormatada2 = dataFormatada2.substring(1);
				}
				if (!dataFormatada2.isEmpty()){
					if (RisoTcgUtil.inverteParaDDMMYYYY(dataFormatada1).isEmpty()){
						dataFormatada1 = obtemDemaisFormatos(dataFormatada1);
					}
					
					if (RisoTcgUtil.inverteParaDDMMYYYY(dataFormatada2).isEmpty()){
						dataFormatada2 = obtemDemaisFormatos(dataFormatada2);
					}
					
					dataFormatadaFinal = RisoTcgUtil.inverteParaDDMMYYYY(dataFormatada2) + " < X < " + RisoTcgUtil.inverteParaDDMMYYYY(dataFormatada1);
				}else{
					if (RisoTcgUtil.inverteParaDDMMYYYY(dataFormatada1).isEmpty()){
						dataFormatada1 = obtemDemaisFormatos(dataFormatada1);
					}
					dataFormatadaFinal = RisoTcgUtil.inverteParaDDMMYYYY(dataFormatada1);
				}
				
				if (!dataFormatadaFinal.isEmpty()){
					listaRetorno.add(dataFormatadaFinal);
				}
				
									
			}
		}
		
		
		return listaRetorno;
	}
	public static void formataEInsereDataEntidade(String nomeEntidadeNoTexto, ArrayList<EntidadeEvento> entidades, ArrayList<String> listaTemporal){
		
		PreparedStatement pstm = null;
		Statement stm = null;
		Connection con = null;
		java.sql.ResultSet rs = null;
		
		String dataAux = "";
		try{
			if (entidades.size() > 0){
				String nomeEntidadeWiki = entidades.get(0).getNome();
				nomeEntidadeWiki = nomeEntidadeWiki.replace("@en", "");
				ArrayList<String> listaTemporalFormatada = formataEntidadesFinal(listaTemporal);
				for (String data: listaTemporalFormatada){
						
					try{
							
						con = PostgresConnectionManager.getInstance().getConnection();
						stm = con.createStatement();
						// stm = DBConexion.getInstance().getConnection().createStatement();
						pstm = con.prepareStatement("SELECT COUNT(*) qts_result FROM dataentidades WHERE nome_entidade = '"+nomeEntidadeWiki+"' and data = '"+data+"' ");

							rs = pstm.executeQuery();
						int qtdResult = 0;
						while (rs.next()){
							qtdResult = rs.getInt("qts_result");	
						}
							
							
						if (qtdResult == 0){
							System.out.println("Data Inserida: " + data);
							String sql = "INSERT INTO dataentidades VALUES ('"+nomeEntidadeWiki+"','"+data+"')";
							stm.execute(sql);
						
						}

					}catch(SQLException e){
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						DBConexion.closeStatement(stm);  
						DBConexion.closePreparedStatement(pstm);  
						DBConexion.closeResult(rs);  
						DBConexion.closeConnection(con);
						
					}
				}
				insereEntidadesNomesAlternativos(nomeEntidadeWiki, nomeEntidadeNoTexto);

			}			
		}catch (Exception e){
			System.out.println("Padrao nao mapeado --> " + dataAux);
		}

		
	}

	
	public static String buscaDataNormalizada(String dataNaoNormalizada){
		
		PreparedStatement pstm = null;
		Statement stm = null;
		Connection con = null;
		java.sql.ResultSet rs = null;
		
		String retorno = dataNaoNormalizada;
		try{
				try{
						
					con = PostgresConnectionManager.getInstance().getConnection();
					stm = con.createStatement();
					// stm = DBConexion.getInstance().getConnection().createStatement();
					pstm = con.prepareStatement("SELECT datanormalizada FROM datanorm WHERE data = '"+dataNaoNormalizada+"' ");
							rs = pstm.executeQuery();
					while (rs.next()){
						
						retorno = rs.getString("datanormalizada");	
					}
						
						
				}catch(SQLException e){
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					DBConexion.closeStatement(stm);  
					DBConexion.closePreparedStatement(pstm);  
					DBConexion.closeResult(rs);  
					DBConexion.closeConnection(con);
					
				}

		}catch (Exception e){
			System.out.println("Erro ao buscar nada normalizada para referente à data --> " + dataNaoNormalizada);
		}
		
		return retorno;

		
	}
	
	public static void insereEntidadesNomesAlternativos (String nomeEntidade, String nomeAlternativo){
		Statement stm = null;
		Connection con = null;
		java.sql.ResultSet rs = null;
		PreparedStatement pstm = null;
		
		boolean sucesso = false;
		int numTentativas = 0;
		while (!sucesso){
			numTentativas++;
			sucesso = true;
			
			
			try{
				
				con = PostgresConnectionManager.getInstance().getConnection();
				stm = con.createStatement();
//				stm = DBConexion.getInstance().getConnection().createStatement();
				pstm = con.prepareStatement("SELECT COUNT(*) qts_result FROM entidadesnomesalt WHERE nome_entidade = '"+nomeEntidade+"' and nomealt = '"+nomeAlternativo+"' ");

				rs = pstm.executeQuery();
				int qtdResult = 0;
				while (rs.next()){
					qtdResult = rs.getInt("qts_result");	
				}
				
				
				if (qtdResult == 0){
					String sql = "INSERT INTO entidadesnomesalt VALUES ('"+nomeEntidade+"','"+nomeAlternativo+"')";
					stm.execute(sql);
				
				}
				
				

			}catch(SQLException e){
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Sera realizada tentativa #"+numTentativas+1);
				sucesso = false;
				e.printStackTrace();
			}finally{
				DBConexion.closeStatement(stm);  
				DBConexion.closeStatement(pstm);  
				DBConexion.closeResult(rs);  
				DBConexion.closeConnection(con);
				
			}
			
			
			
			
			
			
			
//			try{
//				stm = DBConexion.getInstance().getConnection().createStatement();
//
//				nomeEntidade = nomeEntidade.replace("@en", "");
//				rs = stm.executeQuery("SELECT COUNT(*) qts_result FROM entidadesnomesalt WHERE nome_entidade = '"+nomeEntidade+"' and nomealt = '"+nomeAlternativo+"' ");
//				
//				int qtdResult = 0;
//				while (rs.next()){
//					qtdResult = rs.getInt("qts_result");
//				}
//					
//				
//				if (qtdResult == 0){
//					String sql = "INSERT INTO entidadesnomesalt VALUES ('"+nomeEntidade+"','"+nomeAlternativo+"')";
//					stm.execute(sql);
//				
//				}
//				
//			}catch(SQLException e){
//				System.out.println("Sera realizada tentativa #"+numTentativas+1);
//				sucesso = false;
//				e.printStackTrace();
//			}finally{
//				DBConexion.closeStatement(stm);  
//				DBConexion.closeResult(rs);  
//				DBConexion.closeConnection(con);
//				
//			}
			
			
		}
	}
	
	public static ArrayList<EntidadeEvento> getDatasEntidadesEventos(String label){
		
		ArrayList retorno = new ArrayList();
		boolean processou = false;
		while (!processou){
			try{
				System.out.println("entidade: --> "+label); // george remover;
				retorno = getDatasInstituicoesEmGeral(label);
				if (retorno.isEmpty()){
					retorno = getDatasCidades(label);
					if (retorno.isEmpty()){
						retorno = getDatasEmpresas(label);
						if (retorno.isEmpty()){
							retorno = getDatasEstados(label);
							if (retorno.isEmpty()){
								retorno = getDatasFeriados(label);
								if (retorno.isEmpty()){
									retorno = getDatasLocais(label);
									if (retorno.isEmpty()){
										retorno = getDatasPaises(label);
										if (retorno.isEmpty()){
											retorno = getDatasPessoa(label);
											if(retorno.isEmpty()){
												retorno = getDatasEventos(label);
												ArrayList<EntidadeEvento> arrayAux = getDatasEventos2(label);
												if (!arrayAux.isEmpty()){
													retorno.addAll(arrayAux);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				processou = true;
			}catch (Exception e){
				processou = false;
				System.out.println("Erro na busca das datas das entidades. Será feita uma nova tentativa.");
			}
			
		}
		
		return retorno;
		
	}
	
	public static ArrayList<EntidadeEvento> getDatasInstituicoesEmGeral (String label){
		
		String queryPrincipal = "      PREFIX owl: <http://www.w3.org/2002/07/owl#>       						"+        							
				"      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                          "+           
				"      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                     "+           
				"      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>                "+           
				"      PREFIX foaf: <http://xmlns.com/foaf/0.1/>                                "+           
				"      PREFIX dc: <http://purl.org/dc/elements/1.1/>                            "+           
				"      PREFIX : <http://dbpedia.org/resource/>                                  "+           
				"      PREFIX dbpedia2: <http://dbpedia.org/property/>                          "+           
				"      PREFIX dbpedia: <http://dbpedia.org/>                                    "+           
				"      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                      "+           
				"      PREFIX owl2: <http://dbpedia.org/ontology/>                              "+           
				"      PREFIX dbpprop: <http://dbpedia.org/property/>                           "+           
				"          SELECT ?label ?established                                          "+
				"          WHERE {                                                              "+           
				"            ?thing a owl:Thing.                                                "+        
				"       ?thing  rdfs:label ?label                                               "+
				"                                                                               "+
				"      optional{                                                                "+
				"            ?thing dbpprop:established ?established.                           "+
				"                                                                               "+
				"      }                                                                        "+
				"                                                                               "+
				"            FILTER (?label = \""+label.trim()+"\"@en).       "+
				"      }  																		";
		
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Instituicao instituicao = new Instituicao();
	    	
	    	instituicao.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	instituicao.setEstablished(linha.get("established")==null?"":linha.get("established").toString());
	    	
	    	
//	    	System.out.println(instituicao);
	    	if (!instituicao.getEstablished().isEmpty()){
		    	listaDados.add(instituicao);	    		
	    	}
	    	
	    }
	    
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasInstituicoesEmGeral(labelRedirect);
	    	}
	    	
	    }
	    return listaDados;

	}
	
	public static ArrayList<EntidadeEvento> getDatasEventos2 (String label){
		
		String queryPrincipal = "      PREFIX owl: <http://www.w3.org/2002/07/owl#>       				        "+							
				"      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                          "+   
				"      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                     "+   
				"      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>                "+   
				"      PREFIX foaf: <http://xmlns.com/foaf/0.1/>                                "+   
				"      PREFIX dc: <http://purl.org/dc/elements/1.1/>                            "+   
				"      PREFIX : <http://dbpedia.org/resource/>                                  "+   
				"      PREFIX dbpedia2: <http://dbpedia.org/property/>                          "+   
				"      PREFIX dbpedia: <http://dbpedia.org/>                                    "+   
				"      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                      "+   
				"      PREFIX owl2: <http://dbpedia.org/ontology/>                              "+   
				"      PREFIX dbpprop: <http://dbpedia.org/property/>                           "+   
				"          SELECT ?label ?date                                                  "+
				"          WHERE {                                                              "+   
				"            ?thing a owl:Thing.                                                "+
				"       ?thing  rdfs:label ?label                                               "+
				"                                                                               "+
				"      optional{                                                                "+
				"            ?thing owl2:date ?date.                                         "+
				"                                                                               "+
				"      }                                                                        "+
				"                                                                               "+
				"            FILTER (?label = \""+label.trim()+"\"@en).                        "+
				"      }  																        ";
		
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    String dataInicio = "";
	    String dataFim = "";
	    
    	Evento evento = new Evento();
    	
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	evento.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	if (countResult == 1){
		    	dataInicio = linha.get("date")==null?"":linha.get("date").toString();
	    	}else{
	    		dataFim = linha.get("date")==null?"":linha.get("date").toString();
	    	}
	    	
	    }
	    
	    if (dataFim.isEmpty()){
	    	dataFim = dataInicio;
	    }
	    
	    if (!dataInicio.isEmpty()){
		    evento.setPeriodDate(dataFim + " < X < "+dataInicio);	    	
	    }
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (null != evento.getNome()){
	    	if (null != evento.getPeriodDate()){
	    		listaDados.add(evento);	    		
	    	}
	    }
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasEventos2(labelRedirect);
	    	}
	    	
	    }
	    
//	    System.out.println(evento);
	    
	    return listaDados;

	}
	public static ArrayList<EntidadeEvento> getDatasEventos (String label){
		
		String queryPrincipal = "      PREFIX owl: <http://www.w3.org/2002/07/owl#>       				        "+							
				"      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                          "+   
				"      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                     "+   
				"      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>                "+   
				"      PREFIX foaf: <http://xmlns.com/foaf/0.1/>                                "+   
				"      PREFIX dc: <http://purl.org/dc/elements/1.1/>                            "+   
				"      PREFIX : <http://dbpedia.org/resource/>                                  "+   
				"      PREFIX dbpedia2: <http://dbpedia.org/property/>                          "+   
				"      PREFIX dbpedia: <http://dbpedia.org/>                                    "+   
				"      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                      "+   
				"      PREFIX owl2: <http://dbpedia.org/ontology/>                              "+   
				"      PREFIX dbpprop: <http://dbpedia.org/property/>                           "+   
				"          SELECT ?label ?date                                                  "+
				"          WHERE {                                                              "+   
				"            ?thing a owl:Thing.                                                "+
				"       ?thing  rdfs:label ?label                                               "+
				"                                                                               "+
				"      optional{                                                                "+
				"            ?thing dbpprop:date ?date.                                         "+
				"                                                                               "+
				"      }                                                                        "+
				"                                                                               "+
				"            FILTER (?label = \""+label.trim()+"\"@en).                        "+
				"      }  																        ";
		
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    String dataInicio = "";
	    String dataFim = "";
	    
    	Evento evento = new Evento();
    	
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	evento.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	if (countResult == 1){
		    	dataInicio = linha.get("date")==null?"":linha.get("date").toString();
	    	}else{
	    		dataFim = linha.get("date")==null?"":linha.get("date").toString();
	    	}
	    	
	    }
	    
	    if (dataFim.isEmpty()){
	    	dataFim = dataInicio;
	    }
	    
	    if (!dataInicio.isEmpty()){
		    evento.setPeriodDate(dataFim + " < X < "+dataInicio);	    	
	    }
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (null != evento.getNome()){
	    	if (null != evento.getPeriodDate()){
	    		listaDados.add(evento);	    		
	    	}
	    }
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasEventos(labelRedirect);
	    	}
	    	
	    }
	    
//	    System.out.println(evento);
	    
	    return listaDados;

	}
	
	public static ArrayList<EntidadeEvento> getDatasCidades (String label){
		
		String queryPrincipal ="      PREFIX owl: <http://www.w3.org/2002/07/owl#>     					"+				       							
				"      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                      "+            
				"      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                 "+            
				"      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>            "+            
				"      PREFIX foaf: <http://xmlns.com/foaf/0.1/>                            "+            
				"      PREFIX dc: <http://purl.org/dc/elements/1.1/>                        "+            
				"      PREFIX : <http://dbpedia.org/resource/>                              "+            
				"      PREFIX dbpedia2: <http://dbpedia.org/property/>                      "+            
				"      PREFIX dbpedia: <http://dbpedia.org/>                                "+            
				"      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                  "+            
				"      PREFIX owl2: <http://dbpedia.org/ontology/>                          "+            
				"      PREFIX dbpprop: <http://dbpedia.org/property/>                       "+            
				"          SELECT ?label ?foundingDate                                      "+
				"          WHERE {                                                          "+            
				"            ?city a owl2:Place.                                            "+         
				"       ?city rdfs:label ?label                                             "+
				"                                                                           "+
				"      optional{                                                            "+
				"            ?city owl2:foundingDate	?foundingDate.                      "+
				"                                                                           "+
				"      }                                                                    "+
				"                                                                           "+
				"                                                                           "+
				"            FILTER (?label = \""+label.trim()+"\"@en).                            "+
				"      }  		                                                            ";
		
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Cidade cidade = new Cidade();
	    	
	    	cidade.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	cidade.setFoundingDate(linha.get("foundingDate")==null?"":linha.get("foundingDate").toString());
	    	
//	    	System.out.println(cidade);
	    	
	    	if (!cidade.getFoundingDate().isEmpty()){
		    	listaDados.add(cidade);	    		
	    	}
	    	
	    }
	    
	    // segunda consulta 
	    
	    
		queryPrincipal ="      PREFIX owl: <http://www.w3.org/2002/07/owl#>     				"+ 						
				"      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                  "+ 
				"      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>             "+ 
				"      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>        "+ 
				"      PREFIX foaf: <http://xmlns.com/foaf/0.1/>                        "+ 
				"      PREFIX dc: <http://purl.org/dc/elements/1.1/>                    "+ 
				"      PREFIX : <http://dbpedia.org/resource/>                          "+ 
				"      PREFIX dbpedia2: <http://dbpedia.org/property/>                  "+ 
				"      PREFIX dbpedia: <http://dbpedia.org/>                            "+ 
				"      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>              "+ 
				"      PREFIX owl2: <http://dbpedia.org/ontology/>                      "+ 
				"	  PREFIX rc: <http://umbel.org/umbel/rc/>                           "+ 
				"      PREFIX dbpprop: <http://dbpedia.org/property/>                   "+ 
				"          SELECT ?label ?foundingDate ?currentCatDate                  "+          
				"          WHERE {                                                      "+ 
				"            ?city a rc:PopulatedPlace.                                 "+     
				"       ?city rdfs:label ?label                                         "+ 
				"                                                                       "+ 
				"      optional{                                                        "+ 
				"            ?city owl2:foundingDate	?foundingDate.                  "+ 
				"                                                                       "+ 
				"      }                                                                "+ 
				"      optional{                                                        "+ 
				"            ?city dbpedia2:currentCatDate	?currentCatDate.            "+       
				"                                                                       "+ 
				"      }                                                                "+ 
				"                                                                       "+ 
				"                                                                       "+ 
				"            FILTER (?label = \""+label.trim()+"\"@en).      		    "+   
				"      }  		                                                        ";
		
	    query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    results = qExe.execSelect();
	    
	    countResult = 0;
	    
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Cidade cidade = new Cidade();
	    	
	    	cidade.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	cidade.setFoundingDate(linha.get("currentCatDate")==null?"":linha.get("currentCatDate").toString());
	    	
//	    	System.out.println(cidade);
	    	
	    	if (!cidade.getFoundingDate().isEmpty()){
		    	listaDados.add(cidade);	    		
	    	}
	    	
	    }
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasCidades(labelRedirect);
	    	}
	    	
	    	
	    }
	    return listaDados;
		
				
	}

	public static ArrayList<EntidadeEvento> getDatasLocais (String label){
		
		String queryPrincipal =  "    PREFIX owl: <http://www.w3.org/2002/07/owl#>										"+     					      							
		    "  PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                                                          "+
		    "     PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                                                  "+
		    "     PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>       										"+	  
		    "     PREFIX foaf: <http://xmlns.com/foaf/0.1/>                                                             "+
		    "     PREFIX dc: <http://purl.org/dc/elements/1.1/>                                                         "+
		    "     PREFIX : <http://dbpedia.org/resource/>                                                               "+
		    "     PREFIX dbpedia2: <http://dbpedia.org/property/>                                                       "+
		    "     PREFIX dbpedia: <http://dbpedia.org/>                                                                 "+
		    "     PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                                                   "+
		    "     PREFIX owl2: <http://dbpedia.org/ontology/>                                                           "+
		    "     PREFIX dbpprop: <http://dbpedia.org/property/>                                                        "+
		    "         SELECT ?label ?openingDate   ?opened ?created   ?opening ?startDate ?completionDate               "+
		    "         WHERE {                                                                                           "+
		    "           ?place a owl2:Place.                                                                            "+
		    "      ?place rdfs:label ?label                                                                             "+
		    "                                                                                                           "+
		    "     optional{                                                                                             "+
		    "           ?place owl2:openingDate	?openingDate.                                                           "+
		    "                                                                                                           "+
		    "     }                                                                                                     "+
		    "     optional{                                                                                             "+
		    "           ?place dbpprop:openingDate	?opened.                                                            "+
		    "                                                                                                           "+
		    "     }                                                                                                     "+
		    "                                                                                                           "+
		    "     optional{                                                                                             "+
		    "           ?place dbpprop:created	?created.                                                               "+
		    "                                                                                                           "+
		    "     }                                                                                                     "+
		    "     optional{                                                                                             "+
		    "           ?place owl2:opening	?opening.                                                                   "+
		    "                                                                                                           "+
		    "     }                                                                                                     "+
		    "     optional{                                                                                             "+
		    "           ?place dbpprop:startDate	?startDate.                                                         "+
		    "                                                                                                           "+
		    "     }                                                                                                     "+
		    "     optional{                                                                                             "+
		    "           ?place dbpprop:completionDate	?completionDate.                                                "+
		    "                                                                                                           "+
		    "     }                                                                                                     "+
		    "                                                                                                           "+
		    "           FILTER (?label = \""+label.trim()+"\"@en).                                                            "+
		    "     }  		                                                                                            ";
		
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Local local = new Local();
	    	
	    	local.setNome(linha.get("label").toString());

	    	// ?label ?openingDate   ?opened ?created   ?opening ?startDate ?completionDate
	    	
	    	local.setOpeningDate(linha.get("openingDate")==null?"":linha.get("openingDate").toString());
	    	local.setOpened(linha.get("opened")==null?"":linha.get("opened").toString());
	    	local.setCreated(linha.get("created")==null?"":linha.get("created").toString());
	    	local.setOpening(linha.get("opening")==null?"":linha.get("opening").toString());
	    	local.setStartDate(linha.get("startDate")==null?"":linha.get("startDate").toString());
	    	local.setCompletionDate(linha.get("completionDate")==null?"":linha.get("completionDate").toString());
	    	
//	    	System.out.println(local);
	    	if (!local.getOpeningDate().isEmpty() || !local.getOpened().isEmpty() || !local.getCreated().isEmpty() || !local.getOpening().isEmpty() || !local.getStartDate().isEmpty() || !local.getCompletionDate().isEmpty() ){
		    	listaDados.add(local);	    		
	    	}
	    	
	    }
	    
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasLocais(labelRedirect);
	    	}
	    	
	    }
	    return listaDados;
		
				
	}
	

	public static ArrayList<EntidadeEvento> getDatasEstados (String label){
		
		String queryPrincipal =  "     PREFIX owl: <http://www.w3.org/2002/07/owl#>						"+							     					      							
				"     PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                   "+                                       
				"     PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>              "+                                    
				"     PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>       	"+										  
				"     PREFIX foaf: <http://xmlns.com/foaf/0.1/>                         "+                                    
				"     PREFIX dc: <http://purl.org/dc/elements/1.1/>                     "+                                    
				"     PREFIX : <http://dbpedia.org/resource/>                           "+                                    
				"     PREFIX dbpedia2: <http://dbpedia.org/property/>                   "+                                    
				"     PREFIX dbpedia: <http://dbpedia.org/>                             "+                                    
				"     PREFIX skos: <http://www.w3.org/2004/02/skos/core#>               "+                                    
				"     PREFIX owl2: <http://dbpedia.org/ontology/>                       "+                                    
				"     PREFIX dbpprop: <http://dbpedia.org/property/>                    "+                                    
				"         SELECT ?label ?admittancedate                                 "+
				"         WHERE {                                                       "+                                    
				"           ?place a owl:Thing.                                         "+                                   
				"      ?place rdfs:label ?label                                         "+                                    
				"                                                                       "+                                    
				"     optional{                                                         "+                                    
				"           ?place dbpedia2:admittancedate	?admittancedate.            "+                                               
				"                                                                       "+                                    
				"     }                                                                 "+                                    
				"                                                                       "+                                    
				"           FILTER (?label = \""+label.trim()+"\"@en).                         "+                               
				"     }  		                                                        ";

		
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Estado estado = new Estado();
	    	
	    	estado.setNome(linha.get("label").toString());

	    	
	    	estado.setAdmittanceDate(linha.get("admittancedate")==null?"":linha.get("admittancedate").toString());
//	    	System.out.println(estado);
	    	if (!estado.getAdmittanceDate().isEmpty()){
		    	listaDados.add(estado);	    		
	    	}
	    	
	    }
	    
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasEstados(labelRedirect);
	    	}

	    	
	    }
	    return listaDados;
		
				
	}
	
	
	
	
	public static ArrayList<EntidadeEvento> getDatasPaises (String label){
		
		String queryPrincipal ="     PREFIX owl: <http://www.w3.org/2002/07/owl#>     						"+				       							
				"     PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                       "+           
				"     PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                  "+           
				"     PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>             "+           
				"     PREFIX foaf: <http://xmlns.com/foaf/0.1/>                             "+           
				"     PREFIX dc: <http://purl.org/dc/elements/1.1/>                         "+           
				"     PREFIX : <http://dbpedia.org/resource/>                               "+           
				"     PREFIX dbpedia2: <http://dbpedia.org/property/>                       "+           
				"     PREFIX dbpedia: <http://dbpedia.org/>                                 "+           
				"     PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                   "+           
				"     PREFIX owl2: <http://dbpedia.org/ontology/>                           "+           
				"     PREFIX dbpprop: <http://dbpedia.org/property/>                        "+           
				"         SELECT ?label ?establishedDate ?foundingDate                      "+         
				"         WHERE {                                                           "+           
				"           ?company a owl2:Country.                                        "+             
				"      ?company rdfs:label ?label                                           "+
				"                                                                           "+
				"     optional{                                                             "+
				"           ?company dbpedia2:establishedDate ?establishedDate.             "+      
				"                                                                           "+
				"     }                                                                     "+
				"     optional{                                                             "+
				"           ?company owl2:foundingDate	?foundingDate.                      "+
				"                                                                           "+
				"     }                                                                     "+
				"                                                                           "+
				"                                                                           "+
				"           FILTER (?label = \""+label.trim()+"\"@en).                             "+
				"     }  		                                                            ";
		
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Pais pais = new Pais();
	    	
	    	pais.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	pais.setFoundingDate(linha.get("foundingDate")==null?"":linha.get("foundingDate").toString());
	    	pais.setEstablishedDate(linha.get("establishedDate")==null?"":linha.get("establishedDate").toString());
	    	
//	    	System.out.println(pais);
	    	if (!pais.getFoundingDate().isEmpty() || !pais.getEstablishedDate().isEmpty()){
		    	listaDados.add(pais);	    		
	    	}
	    	
	    }
	    
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasPaises(labelRedirect);
	    	}
	    	
	    }
	    return listaDados;
		
				
	}
	
	public static ArrayList<EntidadeEvento> getDatasEmpresas(String label){
		
		String queryPrincipal = "      PREFIX owl: <http://www.w3.org/2002/07/owl#>     					"+           							
				"      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                      "+                
				"      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                 "+                
				"      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>            "+                
				"      PREFIX foaf: <http://xmlns.com/foaf/0.1/>                            "+                
				"      PREFIX dc: <http://purl.org/dc/elements/1.1/>                        "+                
				"      PREFIX : <http://dbpedia.org/resource/>                              "+                
				"      PREFIX dbpedia2: <http://dbpedia.org/property/>                      "+                
				"      PREFIX dbpedia: <http://dbpedia.org/>                                "+                
				"      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                  "+                
				"      PREFIX owl2: <http://dbpedia.org/ontology/>                          "+                
				"      PREFIX dbpprop: <http://dbpedia.org/property/>                       "+                
				"          SELECT ?label ?foundingDate                                      "+ 
				"          WHERE {                                                          "+                
				"            ?company a owl2:Company.                                       "+                  
				"       ?company rdfs:label ?label                                          "+ 
				"                                                                           "+ 
				"      optional{                                                            "+ 
				"            ?company owl2:foundingDate ?foundingDate.                      "+ 
				"                                                                           "+ 
				"      }                                                                    "+ 
				"                                                                           "+ 
				"            FILTER (?label = \""+label.trim()+"\"@en).                              "+ 
				"      }  		                                                            ";
		

	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Empresa empresa = new Empresa();
	    	
	    	empresa.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	empresa.setFoundingDate(linha.get("foundingDate")==null?"":linha.get("foundingDate").toString());
	    	
//	    	System.out.println(empresa);
	    	if (!empresa.getFoundingDate().isEmpty()){
		    	listaDados.add(empresa);	    		
	    	}
	    	
	    }
	    
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasEmpresas(labelRedirect);
	    	}
	    	
	    }
	    return listaDados;

		
	}

	
	public static ArrayList<EntidadeEvento> getDatasFeriados(String label){
		
		String queryPrincipal = " PREFIX owl: <http://www.w3.org/2002/07/owl#>     									"+   							
				" PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                                   "+ 
				" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                              "+ 
				" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>                         "+ 
				" PREFIX foaf: <http://xmlns.com/foaf/0.1/>                                         "+ 
				" PREFIX dc: <http://purl.org/dc/elements/1.1/>                                     "+ 
				" PREFIX : <http://dbpedia.org/resource/>                                           "+ 
				" PREFIX dbpedia2: <http://dbpedia.org/property/>                                   "+ 
				" PREFIX dbpedia: <http://dbpedia.org/>                                             "+ 
				" PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                               "+ 
				" PREFIX owl2: <http://dbpedia.org/ontology/>                                       "+ 
				" PREFIX dbpprop: <http://dbpedia.org/property/>                                    "+ 
				" PREFIX yago: <http://dbpedia.org/class/yago/>                                     "+ 
				"     SELECT ?label ?date                                                           "+ 
				"     WHERE {                                                                       "+ 
				"       ?vacation a yago:Vacation115137890.                                         "+    
				"  ?vacation rdfs:label ?label                                                      "+ 
				"                                                                                   "+ 
				" optional{                                                                         "+ 
				"       ?vacation dbpprop:date ?date.                                                "+ 
				"                                                                                   "+ 
				" }                                                                                 "+ 
				"                                                                                   "+ 
				"       FILTER (?label = \""+label.trim()+"\"@en).                      "+  
				" }  		                                                                        ";
		

	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
	    ResultSet results = qExe.execSelect();
	    
	    int countResult = 0;
	    
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	countResult++;
	    	
	    	QuerySolution linha = results.next();
	    	
	    	Feriado feriado = new Feriado();
	    	
	    	feriado.setNome(linha.get("label")==null?"":linha.get("label").toString());
	    	feriado.setDate(linha.get("date")==null?"":linha.get("date").toString());
	    	
//	    	System.out.println(feriado);
	    	if (!feriado.getDate().isEmpty()){
		    	listaDados.add(feriado);	    		
	    	}
	    	
	    }
	    
	    
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasFeriados(labelRedirect);
	    	}
	    	
	    }
	    return listaDados;

		
	}
	
	public static String getRedirect(String label){
		
		String queryRedirect= "     PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>		"+
		"     PREFIX foaf: <http://xmlns.com/foaf/0.1/>                     "+
		"     PREFIX dbo: <http://dbpedia.org/ontology/>                    "
		+ "	  PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX owl: <http://www.w3.org/2002/07/owl#> "+
		"                                                                   "+
		"     SELECT ?s WHERE {                                             "+
		"       {                                                           "+
		"         ?s rdfs:label \""+label.trim()+"\"@en ;                                 "+
		"            a owl:Thing .                                          "+
		"       }                                                           "+
		"       UNION                                                       "+
		"       {                                                           "+
		"         ?altName rdfs:label \""+label.trim()+"\"@en ;                           "+
		"                  dbo:wikiPageRedirects ?s .                       "+
		"       }                                                           "+
		"     }					                                            ";			
    	
	    Query queryAux = QueryFactory.create(queryRedirect); //s2 = the query above
	    QueryExecution qExeAux = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", queryAux );
	    ResultSet resultsAux = qExeAux.execSelect();
	    
    	while(resultsAux.hasNext()){
    	    QuerySolution linhaAux = resultsAux.next();

    	    String url = (linhaAux.get("s") == null)? "":linhaAux.get("s").toString();
    	    
//    	    System.out.println("-------------------------");
//    	    System.out.println(url);
    	    
    	    String labelCorreta = RisoTcgUtil.removeUrl(url);
    	    String labelFormatada = RisoTcgUtil.formataString(labelCorreta);
    	    
    	    return labelFormatada;
    		
    	}

    	return ""; 
	}
	
	public static ArrayList<EntidadeEvento> getDatasPessoa(String label){
		
		
		String queryPrincipal = "     PREFIX owl: <http://www.w3.org/2002/07/owl#>   																								"+            							
		"     PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>                                                                                               "+
		"     PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>                                                                                          "+
		"     PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>                                                                                     "+
		"     PREFIX foaf: <http://xmlns.com/foaf/0.1/>                                                                                                     "+
		"     PREFIX dc: <http://purl.org/dc/elements/1.1/>                                                                                                 "+
		"     PREFIX : <http://dbpedia.org/resource/>                                                                                                       "+
		"     PREFIX dbpedia2: <http://dbpedia.org/property/>                                                                                               "+
		"     PREFIX dbpedia: <http://dbpedia.org/>                                                                                                         "+
		"     PREFIX skos: <http://www.w3.org/2004/02/skos/core#>                                                                                           "+
		"     PREFIX owl2: <http://dbpedia.org/ontology/>                                                                                                   "+
		"     PREFIX dbpprop: <http://dbpedia.org/property/>                                                                                                "+
		"         SELECT ?label ?birthDate ?deathDate ?activeYearsEndDate ?activeYearsStartDate  ?activeYearsEndYear  ?activeYearsStartYear   ?reign        "+   
		"         WHERE {                                                                                                                                   "+
		"           ?person a foaf:Person.                                                                                                                  "+
		"      ?person rdfs:label ?label                                                                                                                    "+
		"                                                                                                                                                   "+
		"     optional{                                                                                                                                     "+
		"         ?person owl2:birthDate ?birthDate.                                                                                                        "+
		"                                                                                                                                                   "+
		"     }                                                                                                                                             "+
		"     optional{                                                                                                                                     "+
		"         ?person owl2:deathDate ?deathDate.                                                                                                        "+
		"     }                                                                                                                                             "+
		"     optional{                                                                                                                                     "+
		"     	?person owl2:activeYearsEndDate ?activeYearsEndDate.                                                                                        "+
		"     }                                                                                                                                             "+
		"                                                                                                                                                   "+
		"     optional{                                                                                                                                     "+
		"     	?person owl2:activeYearsStartDate ?activeYearsStartDate.                                                                                    "+
		"     }                                                                                                                                             "+
		"                                                                                                                                                   "+
		"                                                                                                                                                   "+
		"                                                                                                                                                   "+
		"     optional{                                                                                                                                     "+
		"     	?person owl2:activeYearsEndYear ?activeYearsEndYear.                                                                                        "+
		"     }                                                                                                                                             "+
		"     optional{                                                                                                                                     "+
		"     	?person owl2:activeYearsStartYear ?activeYearsStartYear.                                                                                    "+
		"     }                                                                                                                                             "+
		"     optional{                                                                                                                                     "+
		"     	?person dbpprop:reign ?reign.                                                                                                               "+
		"     }                                                                                                                                             "+
		"                                                                                                                                                   "+
		"           FILTER (?label = \""+label+"\"@en).                                                                                       "+
		"     }  		                                                                                                                                    ";		
			
			
			
	    Query query = QueryFactory.create(queryPrincipal); //s2 = the query above
	    QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );

	    ResultSet results = qExe.execSelect();
	    
	    ArrayList listaDados = new ArrayList();
	    
	    while (results.hasNext()){
	    	
	    	// ?label ?birthDate ?deathDate ?activeYearsEndDate ?activeYearsStartDate  ?activeYearsEndYear  ?activeYearsStartYear   ?reign
	    	QuerySolution linha = results.next();
	    	Pessoa pessoa = new Pessoa();
	    	pessoa.setNome(linha.get("label").toString());
	    	pessoa.setBirthDate((linha.get("birthDate") == null)?"":linha.get("birthDate").toString());
	    	pessoa.setDeathDate((linha.get("deathDate") == null)? "":linha.get("deathDate").toString());
	    	pessoa.setActiveYearsEndDate((linha.get("activeYearsEndDate") == null)? "":linha.get("activeYearsEndDate").toString());
	    	pessoa.setActiveYearsStartDate((linha.get("activeYearsStartDate") == null)? "":linha.get("activeYearsStartDate").toString());
	    	pessoa.setActiveYearsEndYear((linha.get("activeYearsEndYear") == null)? "":linha.get("activeYearsEndYear").toString());
	    	pessoa.setActiveYearsStartYear((linha.get("activeYearsStartYear") == null)? "":linha.get("activeYearsStartYear").toString());
	    	pessoa.setReign((linha.get("reign") == null)?"":linha.get("reign").toString());
	    	
//	    	System.out.println(pessoa);
	    	
	    	if (!pessoa.getBirthDate().isEmpty() || 
	    			!pessoa.getDeathDate().isEmpty() || 
	    			!pessoa.getActiveYearsEndDate().isEmpty() || 
	    			!pessoa.getActiveYearsStartDate().isEmpty() || 
	    			!pessoa.getActiveYearsStartYear().isEmpty() || 
	    			!pessoa.getActiveYearsEndYear().isEmpty() || 
	    			!pessoa.getReign().isEmpty() ){
		    	listaDados.add(pessoa);	    		
	    	}
	    	
	    }
	    
//	    System.out.println("-------------------------");
//	    ResultSetFormatter.out(System.out, results, query) ;
	    
	    if (listaDados.isEmpty()){
	    	String labelRedirect = getRedirect(label);
	    	if (!labelRedirect.isEmpty() && !label.equals(labelRedirect)){
	    		listaDados = getDatasPessoa(labelRedirect);
	    	}
	    	
	    }
	    return listaDados;
    
    
	}
	
	public static void main(String[] args) {

//		String label = "Lula";
//		(new DBPediaDAO()).getDatasPessoa(label);
//		String oi = "ze antonio oi tim";
//		int index = oi.indexOf("oi");
//		System.out.println(index);
		
		String data = "2014-09-12 to 2014-09-12";
		String dataFormatada1 = data.substring(0, 10);
		String dataFormatada2 = data.substring(data.indexOf(" < X < ")+3, data.indexOf(" < X < ")+3+10);
		
		System.out.println("["+dataFormatada1+"]");
		System.out.println("["+dataFormatada2+"]");
	
	}

}
