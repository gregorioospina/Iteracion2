package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC12_2 {

	@JsonProperty(value = "semana")
	private Integer semana;
	
	@JsonProperty(value = "id_operador")
	private Long id_operador;
	
	@JsonProperty(value = "ocupacion")
	private Integer ocupacion;

	public RFC12_2(@JsonProperty(value = "semana") Integer semana, @JsonProperty(value = "id_operador") Long id_operador, @JsonProperty(value = "ocupacion") Integer ocupacion) {
		super();
		this.semana = semana;
		this.id_operador = id_operador;
		this.ocupacion = ocupacion;
	}

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

	public Long getId_operador() {
		return id_operador;
	}

	public void setId_operador(Long id_operador) {
		this.id_operador = id_operador;
	}

	public Integer getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(Integer ocupacion) {
		this.ocupacion = ocupacion;
	}
	
}
