package br.ueg.tcc.bookway.model.enums;

/**
 * Enumerador utilizado para os tipos de privacidade que os itens do sistema
 * poderá ter
 * 
 * @author pedro
 * 
 */
public enum TypePrivacy {
	PUBLICO("Público"), PRIVADO("Privado");

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private TypePrivacy(String name) {
		this.name = name;
	}

	public static TypePrivacy getTypePrivacyById(Integer id) {
		return TypePrivacy.values()[id];
	}

	@Override
	public String toString() {
		return this.name;
	}
}
