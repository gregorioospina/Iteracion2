package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC10_11 {


	
	@JsonProperty(value = "fechaInicio")
	private String fechaInicio;
	
	@JsonProperty(value = "fechaFinal")
	private String fechaFinal;
	
	@JsonProperty(value = "ordenamientos")
	private ArrayList<String> orden;
	
	@JsonProperty(value = "agrupamientos")
	private ArrayList<String> grupos;
	
	

	public RFC10_11( 
			@JsonProperty(value = "fechaInicio") String fechaInicio, 
			@JsonProperty(value = "fechaFinal") String fechaFinal, 
			@JsonProperty(value = "ordenamientos")ArrayList<String> orden,
			@JsonProperty(value = "agrupamientos")ArrayList<String> grupos) {
		
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.orden = orden;
		this.grupos = grupos;
	}

	/**
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the orden
	 */
	public ArrayList<String> getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(ArrayList<String> orden) {
		this.orden = orden;
	}

	/**
	 * @return the grupos
	 */
	public ArrayList<String> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos the grupos to set
	 */
	public void setGrupos(ArrayList<String> grupos) {
		this.grupos = grupos;
	}
	
	
	
}
