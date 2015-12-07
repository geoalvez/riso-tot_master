package br.edu.ufcg.copin.riso.tot.entities;

public class Evento implements EntidadeEvento{

	private String nome;
	
	private String periodDate;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(String periodDate) {
		this.periodDate = periodDate;
	}

	@Override
	public String toString() {
		return "Evento [nome=" + nome + ", periodDate=" + periodDate + "]";
	}

	
	
}
