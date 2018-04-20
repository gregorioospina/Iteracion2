package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC7 {
	@JsonProperty(value = "precio")
	private Double precio;

	@JsonProperty(value = "fecha")
	private Date fecha;

	@JsonProperty(value = "reserva")
	private Long reserva;

	public RFC7(@JsonProperty(value = "precio") Double precio, @JsonProperty(value = "fecha") Date fecha,
			@JsonProperty(value = "reserva") Long reserva) {
		super();
		this.precio = precio;
		this.fecha = fecha;
		this.reserva = reserva;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getReserva() {
		return reserva;
	}

	public void setReserva(Long reserva) {
		this.reserva = reserva;
	}

}
