package br.edu.ufcg.copin.riso.tot.entities;

public class Entidade implements Comparable {

	String nomeEntidade;
	public static String CARACTER_ESPECIAL = "%%%%%";
	
	public Entidade(String nomeEntidade) {
		this.nomeEntidade = nomeEntidade;
	}

	public String getNomeEntidade() {
		return nomeEntidade;
	}

	public void setNomeEntidade(String nomeEntidade) {
		this.nomeEntidade = nomeEntidade;
	}

	@Override
	public int compareTo(Object arg0) {
		// 
		Entidade outra = (Entidade)arg0;
		return  outra.getNomeEntidade().length() - this.nomeEntidade.length();
	}

	@Override
	public String toString() {
		return nomeEntidade;
	}
	
	
	public String toStringSpecial() {
		String saida = "";
		
		for (int i = 0; i < nomeEntidade.length(); i++){
			String aux = nomeEntidade.substring(i, i+1);
			
			saida = saida.concat(aux + CARACTER_ESPECIAL);
		}
		
		return saida;
				
				
	}
	
	
	
	
}
