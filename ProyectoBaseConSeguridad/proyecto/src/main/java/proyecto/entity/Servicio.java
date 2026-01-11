package proyecto.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Entity
public class Servicio extends DomainEntity {
	@NotNull
	private Date momentoContratacion;
	
	@NotNull
	private LocalDate fechaServicio;
	
	private String comentario;
	
	@Max(5)
	private Integer valoracionServicio;

	public Servicio() {
		super();
	}

	public Servicio(@NotNull Date momentoContratacion, @NotNull LocalDate fechaServicio, String comentario,
			@Max(5) Integer valoracionServicio) {
		super();
		this.momentoContratacion = momentoContratacion;
		this.fechaServicio = fechaServicio;
		this.comentario = comentario;
		this.valoracionServicio = valoracionServicio;
	}

	public Date getMomentoContratacion() {
		return momentoContratacion;
	}

	public void setMomentoContratacion(Date momentoContratacion) {
		this.momentoContratacion = momentoContratacion;
	}

	public LocalDate getFechaServicio() {
		return fechaServicio;
	}

	public void setFechaServicio(LocalDate fechaServicio) {
		this.fechaServicio = fechaServicio;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Integer getValoracionServicio() {
		return valoracionServicio;
	}

	public void setValoracionServicio(Integer valoracionServicio) {
		this.valoracionServicio = valoracionServicio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servicio other = (Servicio) obj;
		return Objects.equals(getId(), other.getId());
	}
}
