package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC5 {
	
	@JsonProperty(value = "tipo")
	private String tipo;
	
	@JsonProperty(value = "codigouniandes")
	private Long codigouniandes;
	
	@JsonProperty(value = "diasalquilados")
	private Integer diasalquilados;
	
	@JsonProperty(value = "tipooperador")
	private String tipooperador;
	
	@JsonProperty(value = "tipohabitacion")
	private String tipohabitacion;
	
	@JsonProperty(value = "precio")
	private Double precio;

	public RFC5(Long codigouniandes, Integer diasalquilados, String tipooperador, String tipohabitacion,
			Double precio, String tipo) {
		super();
		this.codigouniandes = codigouniandes;
		this.diasalquilados = diasalquilados;
		this.tipooperador = tipooperador;
		this.tipohabitacion = tipohabitacion;
		this.precio = precio;
		this.tipo = tipo;
	}

	public Long getCodigouniandes() {
		return codigouniandes;
	}

	public void setCodigouniandes(Long codigouniandes) {
		this.codigouniandes = codigouniandes;
	}

	public Integer getDiasalquilados() {
		return diasalquilados;
	}

	public void setDiasalquilados(Integer diasalquilados) {
		this.diasalquilados = diasalquilados;
	}

	public String getTipooperador() {
		return tipooperador;
	}

	public void setTipooperador(String tipooperador) {
		this.tipooperador = tipooperador;
	}

	public String getTipohabitacion() {
		return tipohabitacion;
	}

	public void setTipohabitacion(String tipohabitacion) {
		this.tipohabitacion = tipohabitacion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
	

}
