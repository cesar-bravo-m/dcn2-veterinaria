package com.function.facturas;

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
    
    public List<DocumentoDTO> getAllDocumentos() throws SQLException {
        List<DocumentoDTO> documentos = new ArrayList<>();
        
        String sql = "SELECT documento_id, documento_tipo_id, impuesto_id, documento_numero, " +
                    "documento_fecha, registro_fecha, usuario_id " +
                    "FROM documentos ORDER BY documento_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                documentos.add(mapResultSetToDocumento(rs));
            }
        }
        
        return documentos;
    }
    
    public DocumentoDTO getDocumentoById(Long documentoId) throws SQLException {
        String sql = "SELECT documento_id, documento_tipo_id, impuesto_id, documento_numero, " +
                    "documento_fecha, registro_fecha, usuario_id " +
                    "FROM documentos WHERE documento_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, documentoId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToDocumento(rs);
            }
        }
        
        return null;
    }
    
    public DocumentoDTO createDocumento(DocumentoDTO documento) throws SQLException {
        String sql = "INSERT INTO documentos (documento_tipo_id, impuesto_id, documento_numero, " +
                    "documento_fecha, registro_fecha, usuario_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING documento_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, documento.getDocumentoTipoId());
            stmt.setLong(2, documento.getImpuestoId());
            stmt.setLong(3, documento.getDocumentoNumero());
            
            if (documento.getDocumentoFecha() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(documento.getDocumentoFecha()));
            } else {
                stmt.setNull(4, java.sql.Types.TIMESTAMP);
            }
            
            stmt.setTimestamp(5, Timestamp.valueOf(documento.getRegistroFecha()));
            stmt.setLong(6, documento.getUsuarioId());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                documento.setDocumentoId(rs.getLong("documento_id"));
                return documento;
            }
        }
        
        return null;
    }
    
    public boolean updateDocumento(DocumentoDTO documento) throws SQLException {
        String sql = "UPDATE documentos SET documento_tipo_id = ?, impuesto_id = ?, " +
                    "documento_numero = ?, documento_fecha = ?, usuario_id = ? " +
                    "WHERE documento_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, documento.getDocumentoTipoId());
            stmt.setLong(2, documento.getImpuestoId());
            stmt.setLong(3, documento.getDocumentoNumero());
            
            if (documento.getDocumentoFecha() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(documento.getDocumentoFecha()));
            } else {
                stmt.setNull(4, java.sql.Types.TIMESTAMP);
            }
            
            stmt.setLong(5, documento.getUsuarioId());
            stmt.setLong(6, documento.getDocumentoId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean deleteDocumento(Long documentoId) throws SQLException {
        String sql = "DELETE FROM documentos WHERE documento_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, documentoId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    private DocumentoDTO mapResultSetToDocumento(ResultSet rs) throws SQLException {
        DocumentoDTO documento = new DocumentoDTO();
        documento.setDocumentoId(rs.getLong("documento_id"));
        documento.setDocumentoTipoId(rs.getLong("documento_tipo_id"));
        documento.setImpuestoId(rs.getLong("impuesto_id"));
        documento.setDocumentoNumero(rs.getLong("documento_numero"));
        
        Timestamp docFecha = rs.getTimestamp("documento_fecha");
        if (docFecha != null) {
            documento.setDocumentoFecha(docFecha.toLocalDateTime());
        }
        
        Timestamp regFecha = rs.getTimestamp("registro_fecha");
        documento.setRegistroFecha(regFecha.toLocalDateTime());
        
        documento.setUsuarioId(rs.getLong("usuario_id"));
        
        return documento;
    }
    
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}
