package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC6_2 {
	
	@JsonProperty(value = "megaString")
	private String megaString;
	
	public RFC6_2(String megaString) {
		super();
		this.megaString = megaString;
	}

	public String getMegaString() {
		return megaString;
	}

	public void setMegaString(String megaString) {
		this.megaString = megaString;
	}
	
	
}