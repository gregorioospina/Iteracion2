package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC5 {

	@JsonProperty(value = "tipo")
	private String tipo;

	@JsonProperty(value = "dias_totales")
	private Integer diasalquilados;

	@JsonProperty(value = "ganancia_total")
	private Double precio;

	public RFC5(@JsonProperty(value = "tipo") String tipo, @JsonProperty(value = "dias_totales") Integer diasalquilados,
			@JsonProperty(value = "ganancia_total") Double precio) {
		super();
		this.tipo = tipo;
		this.diasalquilados = diasalquilados;
		this.precio = precio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getDiasalquilados() {
		return diasalquilados;
	}

	public void setDiasalquilados(Integer diasalquilados) {
		this.diasalquilados = diasalquilados;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

}