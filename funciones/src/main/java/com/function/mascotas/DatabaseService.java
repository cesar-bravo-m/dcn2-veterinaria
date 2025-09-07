package com.function.mascotas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    
    private static final String DB_URL = "jdbc:postgresql://20.81.232.159:5432/duoc";
    private static final String DB_USER = "duoc";
    private static final String DB_PASSWORD = "84oL4mK6cM8w7SK";
    

    
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Mascota CRUD operations
    public List<MascotaDTO> getAllMascotas() throws SQLException {
        List<MascotaDTO> mascotas = new ArrayList<>();
        
        String sql = "SELECT mascota_id, cliente_id, nombre, especie, edad, domestico " +
                    "FROM vt_mascotas ORDER BY mascota_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                mascotas.add(mapResultSetToMascota(rs));
            }
        }
        
        return mascotas;
    }
    
    public MascotaDTO getMascotaById(Long mascotaId) throws SQLException {
        String sql = "SELECT mascota_id, cliente_id, nombre, especie, edad, domestico " +
                    "FROM vt_mascotas WHERE mascota_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mascotaId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMascota(rs);
            }
        }
        
        return null;
    }
    
    public List<MascotaDTO> getMascotasByClienteId(Long clienteId) throws SQLException {
        List<MascotaDTO> mascotas = new ArrayList<>();
        
        String sql = "SELECT mascota_id, cliente_id, nombre, especie, edad, domestico " +
                    "FROM vt_mascotas WHERE cliente_id = ? ORDER BY mascota_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                mascotas.add(mapResultSetToMascota(rs));
            }
        }
        
        return mascotas;
    }
    
    public MascotaDTO createMascota(MascotaDTO mascota) throws SQLException {
        String sql = "INSERT INTO vt_mascotas (cliente_id, nombre, especie, edad, domestico) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING mascota_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mascota.getClienteId());
            stmt.setString(2, mascota.getNombre());
            stmt.setString(3, mascota.getEspecie());
            
            if (mascota.getEdad() != null) {
                stmt.setInt(4, mascota.getEdad());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.setBoolean(5, mascota.getDomestico() != null ? mascota.getDomestico() : true);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mascota.setMascotaId(rs.getLong("mascota_id"));
                return mascota;
            }
        }
        
        return null;
    }
    
    public boolean updateMascota(MascotaDTO mascota) throws SQLException {
        String sql = "UPDATE vt_mascotas SET cliente_id = ?, nombre = ?, especie = ?, " +
                    "edad = ?, domestico = ? WHERE mascota_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mascota.getClienteId());
            stmt.setString(2, mascota.getNombre());
            stmt.setString(3, mascota.getEspecie());
            
            if (mascota.getEdad() != null) {
                stmt.setInt(4, mascota.getEdad());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.setBoolean(5, mascota.getDomestico() != null ? mascota.getDomestico() : true);
            stmt.setLong(6, mascota.getMascotaId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean deleteMascota(Long mascotaId) throws SQLException {
        String sql = "DELETE FROM vt_mascotas WHERE mascota_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mascotaId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    private MascotaDTO mapResultSetToMascota(ResultSet rs) throws SQLException {
        MascotaDTO mascota = new MascotaDTO();
        mascota.setMascotaId(rs.getLong("mascota_id"));
        mascota.setClienteId(rs.getLong("cliente_id"));
        mascota.setNombre(rs.getString("nombre"));
        mascota.setEspecie(rs.getString("especie"));
        
        int edad = rs.getInt("edad");
        if (!rs.wasNull()) {
            mascota.setEdad(edad);
        }
        
        mascota.setDomestico(rs.getBoolean("domestico"));
        
        return mascota;
    }
}
