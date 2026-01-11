package proyecto.entity;



import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Cliente extends Actor {
	private String datosMedicos;
	
	@NotBlank
	private String direccionPostal;
	
	@OneToMany
	private List<Servicio> serviciosContratados;

	public Cliente() {
		super();
	}

	public String getDatosMedicos() {
		return datosMedicos;
	}

	public void setDatosMedicos(String datosMedicos) {
		this.datosMedicos = datosMedicos;
	}

	public String getDireccionPostal() {
		return direccionPostal;
	}

	public void setDireccionPostal(String direccionPostal) {
		this.direccionPostal = direccionPostal;
	}

	public List<Servicio> getServiciosContratados() {
		return serviciosContratados;
	}

	public void setServiciosContratados(List<Servicio> serviciosContratados) {
		this.serviciosContratados = serviciosContratados;
	}
	
	
}
