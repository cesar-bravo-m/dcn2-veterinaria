package com.function.laboratorio;

import java.math.BigDecimal;
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
    
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Laboratorio CRUD operations
    public List<LaboratorioDTO> getAllLaboratorios() throws SQLException {
        List<LaboratorioDTO> laboratorios = new ArrayList<>();
        
        String sql = "SELECT laboratorio_id, mascota_id, fecha, examen, resultado, valor, unidad " +
                    "FROM vt_laboratorio ORDER BY fecha DESC, laboratorio_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                laboratorios.add(mapResultSetToLaboratorio(rs));
            }
        }
        
        return laboratorios;
    }
    
    public LaboratorioDTO getLaboratorioById(Long laboratorioId) throws SQLException {
        String sql = "SELECT laboratorio_id, mascota_id, fecha, examen, resultado, valor, unidad " +
                    "FROM vt_laboratorio WHERE laboratorio_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, laboratorioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLaboratorio(rs);
            }
        }
        
        return null;
    }
    
    public List<LaboratorioDTO> getLaboratoriosByMascotaId(Long mascotaId) throws SQLException {
        List<LaboratorioDTO> laboratorios = new ArrayList<>();
        
        String sql = "SELECT laboratorio_id, mascota_id, fecha, examen, resultado, valor, unidad " +
                    "FROM vt_laboratorio WHERE mascota_id = ? ORDER BY fecha DESC, laboratorio_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, mascotaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                laboratorios.add(mapResultSetToLaboratorio(rs));
            }
        }
        
        return laboratorios;
    }
    
    public List<LaboratorioDTO> getLaboratoriosByExamen(String examen) throws SQLException {
        List<LaboratorioDTO> laboratorios = new ArrayList<>();
        
        String sql = "SELECT laboratorio_id, mascota_id, fecha, examen, resultado, valor, unidad " +
                    "FROM vt_laboratorio WHERE LOWER(examen) LIKE LOWER(?) ORDER BY fecha DESC, laboratorio_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + examen + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                laboratorios.add(mapResultSetToLaboratorio(rs));
            }
        }
        
        return laboratorios;
    }
    
    public LaboratorioDTO createLaboratorio(LaboratorioDTO laboratorio) throws SQLException {
        String sql = "INSERT INTO vt_laboratorio (mascota_id, fecha, examen, resultado, valor, unidad) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING laboratorio_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, laboratorio.getMascotaId());
            
            if (laboratorio.getFecha() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(laboratorio.getFecha()));
            } else {
                stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            }
            
            stmt.setString(3, laboratorio.getExamen());
            stmt.setString(4, laboratorio.getResultado());
            
            if (laboratorio.getValor() != null) {
                stmt.setBigDecimal(5, laboratorio.getValor());
            } else {
                stmt.setNull(5, java.sql.Types.DECIMAL);
            }
            
            stmt.setString(6, laboratorio.getUnidad());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                laboratorio.setLaboratorioId(rs.getLong("laboratorio_id"));
                return laboratorio;
            }
        }
        
        return null;
    }
    
    public boolean updateLaboratorio(LaboratorioDTO laboratorio) throws SQLException {
        String sql = "UPDATE vt_laboratorio SET mascota_id = ?, fecha = ?, examen = ?, " +
                    "resultado = ?, valor = ?, unidad = ? WHERE laboratorio_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, laboratorio.getMascotaId());
            
            if (laboratorio.getFecha() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(laboratorio.getFecha()));
            } else {
                stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            }
            
            stmt.setString(3, laboratorio.getExamen());
            stmt.setString(4, laboratorio.getResultado());
            
            if (laboratorio.getValor() != null) {
                stmt.setBigDecimal(5, laboratorio.getValor());
            } else {
                stmt.setNull(5, java.sql.Types.DECIMAL);
            }
            
            stmt.setString(6, laboratorio.getUnidad());
            stmt.setLong(7, laboratorio.getLaboratorioId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean deleteLaboratorio(Long laboratorioId) throws SQLException {
        String sql = "DELETE FROM vt_laboratorio WHERE laboratorio_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, laboratorioId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    private LaboratorioDTO mapResultSetToLaboratorio(ResultSet rs) throws SQLException {
        LaboratorioDTO laboratorio = new LaboratorioDTO();
        laboratorio.setLaboratorioId(rs.getLong("laboratorio_id"));
        laboratorio.setMascotaId(rs.getLong("mascota_id"));
        
        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            laboratorio.setFecha(fecha.toLocalDateTime());
        }
        
        laboratorio.setExamen(rs.getString("examen"));
        laboratorio.setResultado(rs.getString("resultado"));
        
        BigDecimal valor = rs.getBigDecimal("valor");
        if (!rs.wasNull()) {
            laboratorio.setValor(valor);
        }
        
        laboratorio.setUnidad(rs.getString("unidad"));
        
        return laboratorio;
    }
}
