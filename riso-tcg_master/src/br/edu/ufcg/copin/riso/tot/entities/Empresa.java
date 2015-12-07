package br.edu.ufcg.copin.riso.tot.entities;

public class Empresa implements EntidadeEvento{
	
	String nome;
	
	String foundingDate;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(String foundingDate) {
		this.foundingDate = foundingDate;
	}

	@Override
	public String toString() {
		return "Empresa [nome=" + nome + ", foundingDate=" + foundingDate + "]";
	}
	
	

}
