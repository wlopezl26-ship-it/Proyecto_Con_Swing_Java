package edu.umg.programacion2.proyecto.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.umg.programacion2.proyecto.modelo.Cita;

public class CitaDAO {

	 private static final String URL = "jdbc:mysql://localhost:3306/prog2_db?useSSL=false&serverTimezone=UTC";
	    private static final String USUARIO = "root";
	    private static final String PASSWORD = "";
   
    public Cita crear(Cita cita) throws SQLException {
        String sql = "INSERT INTO cita (cliente, fecha_hora, servicio, duracion_minutos, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cita.getCliente());
            stmt.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            stmt.setString(3, cita.getServicio());
            stmt.setInt(4, cita.getDuracionMinutos());
            stmt.setString(5, cita.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cita.setId(rs.getInt(1));
                }
            }
        }
        return cita;
    }

  
    public List<Cita> listarTodos() throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT id, cliente, fecha_hora, servicio, duracion_minutos, estado FROM cita ORDER BY fecha_hora ASC";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cita cita = mapearCita(rs);
                citas.add(cita);
            }
        }
        return citas;
    }

 
    public Optional<Cita> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, cliente, fecha_hora, servicio, duracion_minutos, estado FROM cita WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCita(rs));
                }
            }
        }
        return Optional.empty();
    }

  
    public boolean actualizar(Cita cita) throws SQLException {
        String sql = "UPDATE cita SET cliente = ?, fecha_hora = ?, servicio = ?, duracion_minutos = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cita.getCliente());
            stmt.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            stmt.setString(3, cita.getServicio());
            stmt.setInt(4, cita.getDuracionMinutos());
            stmt.setString(5, cita.getEstado());
            stmt.setInt(6, cita.getId());

            return stmt.executeUpdate() > 0;
        }
    }


    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM cita WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }


    private Cita mapearCita(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String cliente = rs.getString("cliente");
        LocalDateTime fechaHora = rs.getTimestamp("fecha_hora").toLocalDateTime();
        String servicio = rs.getString("servicio");
        int duracion = rs.getInt("duracion_minutos");
        String estado = rs.getString("estado");

        return new Cita(id, cliente, fechaHora, servicio, duracion, estado);
    }
}