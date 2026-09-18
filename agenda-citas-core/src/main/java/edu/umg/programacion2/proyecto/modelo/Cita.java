package edu.umg.programacion2.proyecto.modelo;
import java.time.LocalDateTime;

public class Cita {
    private int id;
    private String cliente;
    private LocalDateTime fechaHora;
    private String servicio;
    private int duracionMinutos;
    private String estado; 

    public Cita() {
    }

    public Cita(int id, String cliente, LocalDateTime fechaHora, String servicio, int duracionMinutos, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
    }

    // Constructor sin ID (para crear citas nuevas donde la BD asigna el ID)
    public Cita(String cliente, LocalDateTime fechaHora, String servicio, int duracionMinutos, String estado) {
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", cliente='" + cliente + '\'' +
                ", fechaHora=" + fechaHora +
                ", servicio='" + servicio + '\'' +
                ", duracionMinutos=" + duracionMinutos +
                ", estado='" + estado + '\'' +
                '}';
    }
}
