package br.ueg.tcc.bookway.model.enums;

/**
 * Enumerador dos valores de �rea de Interesse utilizado no cadastro de usu�rio
 * 
 * @author pedro
 * 
 */
public enum AreaOfInterest {
	CET("Ci�ncias Exatas e da Terra"), CB("Ci�ncias Biol�gicas"), CS(
			"Ci�ncias da Sa�de"), CA("Ci�ncias Agr�rias"), CSA(
			"Ci�ncias Sociais Aplicadas"), CH("Ci�ncias Humanas");

	private String name;

	public String getNome() {
		return name;
	}

	public void setNome(String nome) {
		this.name = nome;
	}

	private AreaOfInterest(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
