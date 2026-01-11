package proyecto.entity;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Noticia extends DomainEntity{
	@NotBlank
	private String encabezado;
	
	@NotBlank
	private String cuerpoNoticia;
	
	@NotBlank
	@URL
	private String imagenNoticia;

	public Noticia() {
		super();
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public String getCuerpoNoticia() {
		return cuerpoNoticia;
	}

	public void setCuerpoNoticia(String cuerpoNoticia) {
		this.cuerpoNoticia = cuerpoNoticia;
	}

	public String getImagenNoticia() {
		return imagenNoticia;
	}

	public void setImagenNoticia(String imagenNoticia) {
		this.imagenNoticia = imagenNoticia;
	}
}
