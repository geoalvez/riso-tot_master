package br.edu.ufcg.copin.riso.tot.entities;

public class Feriado implements EntidadeEvento{
	
	private String nome;
	
	private String date;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Feriado [nome=" + nome + ", date=" + date + "]";
	}
	
	

}
