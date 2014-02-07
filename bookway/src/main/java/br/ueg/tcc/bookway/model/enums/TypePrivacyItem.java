package br.ueg.tcc.bookway.model.enums;

public enum TypePrivacyItem{
	PUBLICO("Público"), PRIVADO("Privado"), ONLY_FRIENDS("Somente Amigos");
	//0, 1, 2;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private TypePrivacyItem(String name) {
		this.name = name;
	}

	public static TypePrivacyItem getTypePrivacyById(Integer id) {
		return TypePrivacyItem.values()[id];
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
