package br.edu.ufcg.copin.riso.tot.entities;

public class Documento {

	private String nomeDocumento;
	private int qtdRelacionamentos;
	public String getNomeDocumento() {
		return nomeDocumento;
	}
	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}
	public int getQtdRelacionamentos() {
		return qtdRelacionamentos;
	}
	public void setQtdRelacionamentos(int qtdRelacionamentos) {
		this.qtdRelacionamentos = qtdRelacionamentos;
	}
	
	public boolean equals(Documento aux){
		if (this.nomeDocumento.equals(aux.getNomeDocumento())){
			return true;
		}
		
		return false;
	}
	
}
