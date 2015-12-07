package br.edu.ufcg.copin.riso.tot.entities;

public class Pais implements EntidadeEvento{
	
	private String establishedDate;
	
	private String foundingDate;
	
	private String nome;
	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEstablishedDate() {
		return establishedDate;
	}

	public void setEstablishedDate(String establishedDate) {
		this.establishedDate = establishedDate;
	}

	public String getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(String foundingDate) {
		this.foundingDate = foundingDate;
	}

	@Override
	public String toString() {
		return "Pais [establishedDate=" + establishedDate + ", foundingDate="
				+ foundingDate + ", nome=" + nome + "]";
	}
	
	

}
