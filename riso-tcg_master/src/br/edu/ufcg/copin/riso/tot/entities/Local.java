package br.edu.ufcg.copin.riso.tot.entities;

public class Local implements EntidadeEvento{
	// ?label ?openingDate   ?opened ?created   ?opening ?startDate ?completionDate
	private String nome;
	
	private String openingDate;
	
	private String opened;
	
	private String created;
	
	private String opening;
	
	private String startDate;
	
	private String completionDate;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public String getOpened() {
		return opened;
	}

	public void setOpened(String opened) {
		this.opened = opened;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getOpening() {
		return opening;
	}

	public void setOpening(String opening) {
		this.opening = opening;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}

	@Override
	public String toString() {
		return "Local [nome=" + nome + ", openingDate=" + openingDate
				+ ", opened=" + opened + ", created=" + created + ", opening="
				+ opening + ", startDate=" + startDate + ", completionDate="
				+ completionDate + "]";
	}
	
	

}
