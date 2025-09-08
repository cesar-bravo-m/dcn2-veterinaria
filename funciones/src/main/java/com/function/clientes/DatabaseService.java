package com.function.clientes;

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
    
    public List<ClienteDTO> getAllClientes() throws SQLException {
        List<ClienteDTO> clientes = new ArrayList<>();
        
        String sql = "SELECT cliente_id, nombre, paterno, materno, rut from clientes";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        }
        
        return clientes;
    }
    
    public ClienteDTO getClienteById(Long clienteId) throws SQLException {
        String sql = "SELECT cliente_id, nombre, paterno, materno, rut FROM clientes WHERE cliente_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCliente(rs);
            }
        }
        
        return null;
    }
    
    public ClienteDTO createCliente(ClienteDTO cliente) throws SQLException {
        String sql = "INSERT INTO clientes (cliente_id, nombre, paterno, materno, rut) " +
                    "VALUES (nextval('clientes_cliente_id_seq'), ?, ?, ?, ?) RETURNING cliente_id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getPaterno());
            stmt.setString(3, cliente.getMaterno());
            stmt.setString(4, cliente.getRut());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente.setClienteId(rs.getLong("cliente_id"));
                return cliente;
            }
        }
        
        return null;
    }
    
    public boolean updateCliente(ClienteDTO cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, paterno = ?, materno = ?, rut = ? " +
                    "WHERE cliente_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getPaterno());
            stmt.setString(3, cliente.getMaterno());
            stmt.setString(4, cliente.getRut());
            stmt.setLong(5, cliente.getClienteId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean deleteCliente(Long clienteId) throws SQLException {
        String sql = "DELETE FROM clientes WHERE cliente_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    private ClienteDTO mapResultSetToCliente(ResultSet rs) throws SQLException {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setClienteId(rs.getLong("cliente_id"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setPaterno(rs.getString("paterno"));
        cliente.setMaterno(rs.getString("materno"));
        cliente.setRut(rs.getString("rut"));
        
        return cliente;
    }
    
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}
