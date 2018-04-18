package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC4 {

	@JsonProperty(value="operador")
	private Integer operador;

	public Integer getOperador() {
		return operador;
	}

	public void setOperador(Integer operador) {
		this.operador = operador;
	}

	public RFC4(Integer operador) {
		super();
		this.operador = operador;
	}
	
	
}
