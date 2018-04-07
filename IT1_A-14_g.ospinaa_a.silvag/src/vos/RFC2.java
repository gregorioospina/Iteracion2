package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC2 {

	@JsonProperty(value="operador")
	private Integer operador;
	
	@JsonProperty(value="habitacion")
	private Integer habitacion;

	/**
	 * @param operador
	 * @param habitacion
	 */
	public RFC2(@JsonProperty(value="operador") Integer operador, @JsonProperty(value="habitacion") Integer habitacion) {
		super();
		this.operador = operador;
		this.habitacion = habitacion;
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
	 * @return the habitacion
	 */
	public Integer getHabitacion() {
		return habitacion;
	}

	/**
	 * @param habitacion the habitacion to set
	 */
	public void setHabitacion(Integer habitacion) {
		this.habitacion = habitacion;
	}
	
}
