package br.ueg.tcc.bookway.model.enums;

/**
 * Enumerador dos valores de Área de Interesse utilizado no cadastro de usuário
 * 
 * @author pedro
 * 
 */
public enum AreaOfInterest {
	CET("Ciências Exatas e da Terra"), CB("Ciências Biológicas"), CS(
			"Ciências da Saúde"), CA("Ciências Agrárias"), CSA(
			"Ciências Sociais Aplicadas"), CH("Ciências Humanas");

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
