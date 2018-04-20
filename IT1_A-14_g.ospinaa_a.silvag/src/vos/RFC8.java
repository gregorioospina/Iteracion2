package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC8 {
	@JsonProperty(value = "codigo")
	private Long codigo;

	@JsonProperty(value = "correo")
	private String correo;

	@JsonProperty(value = "nombre")
	private String nombre;

	public RFC8(@JsonProperty(value = "codigo") Long codigo, @JsonProperty(value = "correo") String correo,
			@JsonProperty(value = "nombre") String nombre) {
		super();
		this.codigo = codigo;
		this.correo = correo;
		this.nombre = nombre;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
