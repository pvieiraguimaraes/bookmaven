package br.ueg.tcc.bookway.model.enums;

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
