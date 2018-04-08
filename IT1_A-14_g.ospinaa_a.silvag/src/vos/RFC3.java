package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC3 {

	@JsonProperty(value = "indice")
	private Double indice;
	
	@JsonProperty(value = "id")
	private Integer id;

	public RFC3(Double indice, Integer id) {
		super();
		this.indice = indice;
		this.id = id;
	}

	public Double getIndice() {
		return indice;
	}

	public void setIndice(Double indice) {
		this.indice = indice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
