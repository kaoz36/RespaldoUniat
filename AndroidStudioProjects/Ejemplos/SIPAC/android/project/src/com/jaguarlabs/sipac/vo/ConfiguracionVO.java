package com.jaguarlabs.sipac.vo;

public class ConfiguracionVO {

	private String expiracion;
	private String salario;

	public ConfiguracionVO(String expiracion, String salario) {
		super();
		this.expiracion = expiracion;
		this.salario = salario;
	}

	public String getExpiracion() {
		return expiracion;
	}

	public void setExpiracion(String expiracion) {
		this.expiracion = expiracion;
	}

	public String getSalario() {
		return salario;
	}

	public void setSalario(String salario) {
		this.salario = salario;
	}
	
	
}
