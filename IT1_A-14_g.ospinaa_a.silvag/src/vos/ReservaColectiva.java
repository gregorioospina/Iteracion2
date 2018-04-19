package vos;

import org.codehaus.jackson.annotate.*;

public class ReservaColectiva {
	
	@JsonProperty(value="idreserva")
	private Long idreserva;
	
	@JsonProperty(value="idcolectiva")
	private Long idcolectiva;

	public ReservaColectiva(@JsonProperty(value="idreserva") Long idreserva, @JsonProperty(value="idcolectiva") Long idcolectiva) {
		super();
		this.idreserva = idreserva;
		this.idcolectiva = idcolectiva;
	}

	public Long getIdreserva() {
		return idreserva;
	}

	public void setIdreserva(Long idreserva) {
		this.idreserva = idreserva;
	}

	public Long getIdcolectiva() {
		return idcolectiva;
	}

	public void setIdcolectiva(Long idcolectiva) {
		this.idcolectiva = idcolectiva;
	}
	
	
	
}
