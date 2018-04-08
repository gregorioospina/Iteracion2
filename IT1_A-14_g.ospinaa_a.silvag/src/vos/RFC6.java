package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC6 {
	
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

	public RFC6(Long codigouniandes, Integer diasalquilados, String tipooperador, String tipohabitacion,
			Double precio) {
		super();
		this.codigouniandes = codigouniandes;
		this.diasalquilados = diasalquilados;
		this.tipooperador = tipooperador;
		this.tipohabitacion = tipohabitacion;
		this.precio = precio;
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
	
	
	
	

}
