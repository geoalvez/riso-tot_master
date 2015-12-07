package br.edu.ufcg.copin.riso.tot.entities;

public class Instituicao implements EntidadeEvento{
	
	private String nome;
	private String established;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEstablished() {
		return established;
	}
	public void setEstablished(String established) {
		this.established = established;
	}
	
	@Override
	public String toString() {
		return "Instituicao [nome=" + nome + ", established=" + established
				+ "]";
	}
	
	

}
