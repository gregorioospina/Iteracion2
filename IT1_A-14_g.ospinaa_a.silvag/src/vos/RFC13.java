package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC13 {

	@JsonProperty(value="codigouniandino")
	private Long codigouniandino;
	
	@JsonProperty(value = "nombre")
	private String nombre;
	
	@JsonProperty(value="hab")
	private String hab;
	
	@JsonProperty(value= "precio_promedio")
	private Double precio_promedio;

	public RFC13(@JsonProperty(value="codigouniandino") Long codigouniandino, 
			@JsonProperty(value = "nombre") String nombre, 
			@JsonProperty(value="hab")String hab, 
			@JsonProperty(value= "precio_promedio") Double precio_promedio) {
		super();
		this.codigouniandino = codigouniandino;
		this.nombre = nombre;
		this.hab = hab;
		this.precio_promedio = precio_promedio;
	}

	public synchronized Long getCodigouniandino() {
		return codigouniandino;
	}

	public synchronized void setCodigouniandino(Long codigouniandino) {
		this.codigouniandino = codigouniandino;
	}

	public synchronized String getNombre() {
		return nombre;
	}

	public synchronized void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public synchronized String getHab() {
		return hab;
	}

	public synchronized void setHab(String hab) {
		this.hab = hab;
	}

	public synchronized Double getPrecio_promedio() {
		return precio_promedio;
	}

	public synchronized void setPrecio_promedio(Double precio_promedio) {
		this.precio_promedio = precio_promedio;
	}
	
	
	
	
}
