package com.function.comunicacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    
    private static final String DB_URL = "jdbc:postgresql://20.81.232.159:5432/duoc";
    private static final String DB_USER = "duoc";
    private static final String DB_PASSWORD = "84oL4mK6cM8w7SK";
    
    public List<MensajeDTO> getAllMensajes() throws SQLException {
        List<MensajeDTO> mensajes = new ArrayList<>();
        
        String sql = "SELECT mensaje_id, cliente_id, fecha_envio, contenido, estado " +
                    "FROM vt_mensajes ORDER BY mensaje_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                mensajes.add(mapResultSetToMensaje(rs));
            }
        }
        
        return mensajes;
    }
    
    public MensajeDTO getMensajeById(Long mensajeId) throws SQLException {
        String sql = "SELECT mensaje_id, cliente_id, fecha_envio, contenido, estado " +
                    "FROM vt_mensajes WHERE mensaje_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mensajeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMensaje(rs);
            }
        }
        
        return null;
    }
    
    public List<MensajeDTO> getMensajesByClienteId(Long clienteId) throws SQLException {
        List<MensajeDTO> mensajes = new ArrayList<>();
        
        String sql = "SELECT mensaje_id, cliente_id, fecha_envio, contenido, estado " +
                    "FROM vt_mensajes WHERE cliente_id = ? ORDER BY fecha_envio DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                mensajes.add(mapResultSetToMensaje(rs));
            }
        }
        
        return mensajes;
    }
    
    public List<MensajeDTO> getMensajesByEstado(String estado) throws SQLException {
        List<MensajeDTO> mensajes = new ArrayList<>();
        
        String sql = "SELECT mensaje_id, cliente_id, fecha_envio, contenido, estado " +
                    "FROM vt_mensajes WHERE estado = ? ORDER BY fecha_envio DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                mensajes.add(mapResultSetToMensaje(rs));
            }
        }
        
        return mensajes;
    }
    
    public MensajeDTO createMensaje(MensajeDTO mensaje) throws SQLException {
        String sql = "INSERT INTO vt_mensajes (cliente_id, fecha_envio, contenido, estado) " +
                    "VALUES (?, ?, ?, ?) RETURNING mensaje_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mensaje.getClienteId());
            
            if (mensaje.getFechaEnvio() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(mensaje.getFechaEnvio()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            stmt.setString(3, mensaje.getContenido());
            stmt.setString(4, mensaje.getEstado() != null ? mensaje.getEstado() : "ENVIADO");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mensaje.setMensajeId(rs.getLong("mensaje_id"));
                return mensaje;
            }
        }
        
        return null;
    }
    
    public boolean updateMensaje(MensajeDTO mensaje) throws SQLException {
        String sql = "UPDATE vt_mensajes SET cliente_id = ?, fecha_envio = ?, " +
                    "contenido = ?, estado = ? WHERE mensaje_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mensaje.getClienteId());
            
            if (mensaje.getFechaEnvio() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(mensaje.getFechaEnvio()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            stmt.setString(3, mensaje.getContenido());
            stmt.setString(4, mensaje.getEstado());
            stmt.setLong(5, mensaje.getMensajeId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean deleteMensaje(Long mensajeId) throws SQLException {
        String sql = "DELETE FROM vt_mensajes WHERE mensaje_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mensajeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    private MensajeDTO mapResultSetToMensaje(ResultSet rs) throws SQLException {
        MensajeDTO mensaje = new MensajeDTO();
        mensaje.setMensajeId(rs.getLong("mensaje_id"));
        mensaje.setClienteId(rs.getLong("cliente_id"));
        
        Timestamp fechaEnvio = rs.getTimestamp("fecha_envio");
        if (fechaEnvio != null) {
            mensaje.setFechaEnvio(fechaEnvio.toLocalDateTime());
        }
        
        mensaje.setContenido(rs.getString("contenido"));
        mensaje.setEstado(rs.getString("estado"));
        
        return mensaje;
    }
    
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}
