package CRUDS;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;

/**
 *
 * @author kevmt
 */
public class CRUDLogin {
    // Función para hashear con SHA-256
//    public static String hashPasswordSHA256(String password) throws Exception {
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] hash = digest.digest(password.getBytes("UTF-8"));
//        StringBuilder hexString = new StringBuilder();
//
//        for (byte b : hash) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//
//        return hexString.toString();
//    }
    
public static boolean conexion(String username, String password) throws Exception {
    // Conectar a la base de datos
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    PreparedStatement pstmt2 = null;
    ResultSet rs2 = null;
    boolean act = false;
    
    try {
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");

        // URL de conexión con la base de datos
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String dbUser = "root";  // Usuario de la base de datos
        String dbPassword = "root";  // Contraseña de la base de datos

        // Establecer la conexión
        conn = DriverManager.getConnection(url, dbUser, dbPassword);

        // Consulta SQL para verificar el usuario y contraseña (hash almacenado)
        String sql = "SELECT password FROM empleados WHERE usuario = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        
        String activo = "select activo from empleados where usuario = ?";
        pstmt2 = conn.prepareStatement(activo);
        pstmt2.setString(1, username);
        // Ejecutar la consulta
        rs = pstmt.executeQuery();
        rs2 = pstmt2.executeQuery();
        if (rs2.next()) {
            act = rs2.getBoolean("activo");
        }
        if (rs.next()) {
            // Obtenemos el hash almacenado en la base de datos
            String storedHash = rs.getString("password");
            

            // Verificar si el hash de la contraseña ingresada coincide con el almacenado
            if (password.equals(storedHash) && act) {
                return true;  // Login exitoso
            } else {
                return false; // Contraseña incorrecta
            }
        } else {
            return false;  // Usuario no encontrado
        }
        
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        throw new Exception("Error al conectar a la base de datos: " + e.getMessage());
    } finally {
        // Cerrar la conexión y liberar recursos
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




}
