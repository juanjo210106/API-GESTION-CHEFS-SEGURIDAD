package proyecto.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Chef extends Actor {
	@Min(0)
	private Double precioServicio;
	
	@Min(0)
	@Max(5)
	private Double valoracionChef;
	
	private boolean activo;
	
	@OneToMany
	private List<Servicio> serviciosOfrecidos;

	public Chef() {
		super();
	}

	public Chef(@Min(0) Double precioServicio, @Min(0) @Max(5) Double valoracionChef, boolean activo,
			List<Servicio> serviciosOfrecidos) {
		super();
		this.precioServicio = precioServicio;
		this.valoracionChef = valoracionChef;
		this.activo = activo;
		this.serviciosOfrecidos = serviciosOfrecidos;
	}

	public Double getPrecioServicio() {
		return precioServicio;
	}

	public void setPrecioServicio(Double precioServicio) {
		this.precioServicio = precioServicio;
	}

	public Double getValoracionChef() {
		return valoracionChef;
	}

	public void setValoracionChef(Double valoracionChef) {
		this.valoracionChef = valoracionChef;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<Servicio> getServiciosOfrecidos() {
		return serviciosOfrecidos;
	}

	public void setServiciosOfrecidos(List<Servicio> serviciosOfrecidos) {
		this.serviciosOfrecidos = serviciosOfrecidos;
	}
	
	
}
