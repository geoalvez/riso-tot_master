package br.edu.ufcg.copin.riso.tot.entities;

public class Estado implements EntidadeEvento{
	
	private String nome;
	
	private String admittanceDate;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAdmittanceDate() {
		return admittanceDate;
	}

	public void setAdmittanceDate(String admittanceDate) {
		this.admittanceDate = admittanceDate;
	}

	@Override
	public String toString() {
		return "Estado [nome=" + nome + ", admittanceDate=" + admittanceDate
				+ "]";
	}
	

}
