package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.*;

public class Reserva {
	////////////////////////////////////////////
	//////////////// ATRIBUTOS///////////////////
	////////////////////////////////////////////

	/**
	 * Fecha y hora en la que se efectuo la reserva.
	 */
	@JsonProperty(value = "fechaInicio")
	private Date fechaInicio;

	/**
	 * Cancelado, booleano que especifica si ha sido cancelada la reserva
	 */
	@JsonProperty(value = "cancelado")
	private boolean cancelado;

	/**
	 * idUsuario del usuario que efectuo la reserva
	 */
	@JsonProperty(value = "idUsuario")
	private Long idUsuario;

	/**
	 * idOperador del operador ofreciendo el servicio
	 */
	@JsonProperty(value = "idOperador")
	private Long idOperador;

	/**
	 * idHabitacion reservado por el usuario
	 */
	@JsonProperty(value = "idHabitacion")
	private Long idHabitacion;

	/**
	 * Valor a pagar por la reserva
	 */
	@JsonProperty(value = "precio")
	private Double precio;

	/**
	 * Id unico de la reserva
	 */
	@JsonProperty(value = "idReserva")
	private Long idReserva;
	
	@JsonProperty(value = "fechaFinal") 
	private Date fechaFinal;
	
	@JsonProperty(value = "horaCreacion")
	private Date horaCreacion;
	
	
	

	////////////////////////////////////////////
	//////////////// CONSTRUCTOR/////////////////
	////////////////////////////////////////////

	/**
	 * Metodo constructor de la clase Reserva <b> post: </b> Crea la reserva con
	 * los valores por parametro.
	 * 
	 * @param fechaInicio
	 * @param cancelado2
	 * @param idUsuario
	 * @param idOperador
	 * @param idHabitacion
	 * @param idReserva
	 */
	public Reserva(@JsonProperty(value = "idReserva") Long idReserva, 
			@JsonProperty(value = "idUsuario") Long idUsuario,
			@JsonProperty(value = "idOperador") Long idOperador,
			@JsonProperty(value = "cancelado") boolean cancelado2,
			@JsonProperty(value = "precio") Double precio,
			@JsonProperty(value = "idHabitacion") Long idHabitacion,
			@JsonProperty(value = "fechaInicio") Date fechaInicio,
			@JsonProperty(value = "fechaFinal") Date fechaFinal,
			@JsonProperty(value = "horaCreacion") Date horaCreacion) {
		this.fechaFinal = fechaFinal;
		this.idReserva = idReserva;
		this.fechaInicio = fechaInicio;
		this.cancelado = cancelado2;
		this.idUsuario = idUsuario;
		this.idOperador = idOperador;
		this.idHabitacion = idHabitacion;
		this.precio = precio;
		this.horaCreacion = horaCreacion;
	}

	////////////////////////////////////////////
	//////////// GETTERS AND SETTERS/////////////
	////////////////////////////////////////////

	/**
	 * @return the fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio
	 *            the fechaInicio to set
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the precio
	 */
	public Double getPrecio() {
		return precio;
	}

	/**
	 * @param precio
	 *            the precio to set
	 */
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	/**
	 * @return the cancelado
	 */
	public boolean getCancelado() {
		return cancelado;
	}

	/**
	 * @param cancelado
	 *            the cancelado to set
	 */
	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}





	/**
	 * @return the idReserva
	 */
	public Long getIdReserva() {
		return idReserva;
	}


	/**
	 * @return the fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @return the idUsuario
	 */
	public Long getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the idOperador
	 */
	public Long getIdOperador() {
		return idOperador;
	}

	/**
	 * @param idOperador the idOperador to set
	 */
	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}

	/**
	 * @return the idHabitacion
	 */
	public Long getIdHabitacion() {
		return idHabitacion;
	}

	/**
	 * @param idHabitacion the idHabitacion to set
	 */
	public void setIdHabitacion(Long idHabitacion) {
		this.idHabitacion = idHabitacion;
	}

	/**
	 * @return the horaCreacion
	 */
	public Date getHoraCreacion() {
		return horaCreacion;
	}

	/**
	 * @param horaCreacion the horaCreacion to set
	 */
	public void setHoraCreacion(Date horaCreacion) {
		this.horaCreacion = horaCreacion;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

}
