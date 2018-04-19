package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC1 {

	@JsonProperty(value = "nombre")
	private String nombre;
	
	@JsonProperty(value="operador")
	private Integer operador;
	
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the operador
	 */
	public Integer getOperador() {
		return operador;
	}

	/**
	 * @param operador the operador to set
	 */
	public void setOperador(Integer operador) {
		this.operador = operador;
	}

	/**
	 * @return the ganancia
	 */
	public Integer getGanancia() {
		return ganancia;
	}

	/**
	 * @param ganancia the ganancia to set
	 */
	public void setGanancia(Integer ganancia) {
		this.ganancia = ganancia;
	}

	@JsonProperty(value="ganancia")
	private Integer ganancia;

	public RFC1(@JsonProperty(value = "nombre") String nombre, @JsonProperty(value="operador") Integer operador, @JsonProperty(value="ganancia") Integer ganancia) {
		super();
		this.nombre = nombre;
		this.operador = operador;
		this.ganancia = ganancia;
	}
	
	
	
}
