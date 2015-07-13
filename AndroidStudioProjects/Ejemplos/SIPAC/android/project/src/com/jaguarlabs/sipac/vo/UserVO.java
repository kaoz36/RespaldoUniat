package com.jaguarlabs.sipac.vo;

public class UserVO {
	
	private String agente, nombre, sexo, rfc, cedula, nombreCorto, ingreso, 
	meta, status, nick, correo, telefono, foto, telefonoFijo,password;

	public UserVO() {
	}

	public UserVO(String agente, String nombre, String sexo, String rfc,
			String cedula, String nombreCorto, String ingreso, String meta,
			String status, String nick, String correo, String telefono,
			String foto, String telefonoFijo,String pPassword) {
		super();
		this.agente = agente;
		this.nombre = nombre;
		this.sexo = sexo;
		this.rfc = rfc;
		this.cedula = cedula;
		this.nombreCorto = nombreCorto;
		this.ingreso = ingreso;
		this.meta = meta;
		this.status = status;
		this.nick = nick;
		this.correo = correo;
		this.telefono = telefono;
		this.foto = foto;
		this.telefonoFijo = telefonoFijo;
		this.password = pPassword;
	}

	public String getAgente() {
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getIngreso() {
		return ingreso;
	}

	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getTelefonoFijo() {
		return telefonoFijo;
	}

	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
//	private String id;
//	private String fullName; 
//	private String fechaExpiracion; 
//	private String rfc; 
//	private String monedero;
//	private String fechaNacimiento;
//	
//	public String getFullName() {
//		return fullName;
//	}
//	public void setFullName(String fullName) {
//		this.fullName = fullName;
//	}
//	public String getFechaExpiracion() {
//		return fechaExpiracion;
//	}
//	public void setFechaExpiracion(String fechaExpiracion) {
//		this.fechaExpiracion = fechaExpiracion;
//	}
//	public String getRfc() {
//		return rfc;
//	}
//	public void setRfc(String rfc) {
//		this.rfc = rfc;
//	}
//	public String getMonedero() {
//		return monedero;
//	}
//	public void setMonedero(String monedero) {
//		this.monedero = monedero;
//	}
//	public String getFechaNacimiento() {
//		return fechaNacimiento;
//	}
//	public void setFechaNacimiento(String fechaNacimiento) {
//		this.fechaNacimiento = fechaNacimiento;
//	}
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
	
	

}
