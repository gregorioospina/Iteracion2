package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC12_1 {
	
	@JsonProperty(value = "semana")
	private Integer semana;
	
	@JsonProperty(value = "id_operador")
	private Long id_operador;
	
	@JsonProperty(value = "solicitado")
	private Integer solicitado;

	public RFC12_1(@JsonProperty(value = "semana") Integer semana, @JsonProperty(value = "id_operador") Long id_operador, @JsonProperty(value = "solicitado") Integer solicitado) {
		super();
		this.semana = semana;
		this.id_operador = id_operador;
		this.solicitado = solicitado;
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

	public Integer getSolicitado() {
		return solicitado;
	}

	public void setSolicitado(Integer solicitado) {
		this.solicitado = solicitado;
	}
	
	
	
	
	

}
