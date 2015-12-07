package br.edu.ufcg.copin.riso.tot.entities;

public class Cidade implements EntidadeEvento{

	private String nome;
	
	private String foundingDate;

	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public String getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(String foundingDate) {
		this.foundingDate = foundingDate;
	}

	@Override
	public String toString() {
		return "Cidade [nome=" + nome + ", foundingDate=" + foundingDate + "]";
	}
	
	
}
