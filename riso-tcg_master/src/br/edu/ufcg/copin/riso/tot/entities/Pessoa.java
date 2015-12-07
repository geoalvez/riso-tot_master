package br.edu.ufcg.copin.riso.tot.entities;

import java.util.Date;


public class Pessoa implements EntidadeEvento{
	
	String nome;
	String birthDate;
	String deathDate;
	String activeYearsEndDate;
	String activeYearsStartDate;
	String activeYearsEndYear;
	String activeYearsStartYear;
	String reign;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String name) {
		this.nome = name;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}
	public String getActiveYearsEndDate() {
		return activeYearsEndDate;
	}
	public void setActiveYearsEndDate(String activeYearsEndDate) {
		this.activeYearsEndDate = activeYearsEndDate;
	}
	public String getActiveYearsStartDate() {
		return activeYearsStartDate;
	}
	public void setActiveYearsStartDate(String activeYearsStartDate) {
		this.activeYearsStartDate = activeYearsStartDate;
	}
	public String getActiveYearsEndYear() {
		return activeYearsEndYear;
	}
	public void setActiveYearsEndYear(String activeYearsEndYear) {
		this.activeYearsEndYear = activeYearsEndYear;
	}
	public String getActiveYearsStartYear() {
		return activeYearsStartYear;
	}
	public void setActiveYearsStartYear(String activeYearsStartYear) {
		this.activeYearsStartYear = activeYearsStartYear;
	}
	public String getReign() {
		return reign;
	}
	public void setReign(String reign) {
		this.reign = reign;
	}
	
	public Date getDateObject(String data){
		return new Date();
	}
	@Override
	public String toString() {
		return "Pessoa [birthDate=" + birthDate + ", deathDate=" + deathDate
				+ ", activeYearsEndDate=" + activeYearsEndDate
				+ ", activeYearsStartDate=" + activeYearsStartDate
				+ ", activeYearsEndYear=" + activeYearsEndYear
				+ ", activeYearsStartYear=" + activeYearsStartYear + ", reign="
				+ reign + "]";
	}
	
	
	
	

}
