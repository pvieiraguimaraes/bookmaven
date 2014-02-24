package br.ueg.tcc.bookway.view.util;

import br.com.vexillum.model.enums.Sexo;

/**
 * Classe que converte o enum de sexo para radio group
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
public class SexoEnumRadiogroupConverter extends EnumRadiogroupConverter<Sexo> {

	public SexoEnumRadiogroupConverter() {
		super(Sexo.class);
	}

}
