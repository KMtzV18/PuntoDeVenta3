

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author kevmt
 */
public class CRUDRegistroEmpleados {
    
    public int mod() throws Exception{
        Empleados e = new Empleados();
        int m = e.getMod();
        return m ;
    }
    
  // Método para obtener la conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String usuario = "root"; // Usuario de la base de datos
        String contraseña = "root"; // Contraseña de la base de datos

        // Establece y devuelve la conexión
        return DriverManager.getConnection(url, usuario, contraseña);
    }
    
  public boolean registrar(String nombre, String usuario, String password) throws Exception {
      String user = new registro().getUser();
      if (usuario.length()<4) {
          JOptionPane.showMessageDialog(null, "La longitud del usuario debe ser de 8 caracteres minimo");
          return false;
      }
      int mod = mod();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String dbUser = "root"; 
        String dbPassword = "root"; 

        // Establecer la conexión
        conn = DriverManager.getConnection(url, dbUser, dbPassword);

        // Verificar si el usuario ya existe
        String checkUserSql = "SELECT COUNT(*) FROM empleados WHERE usuario = ?";
        PreparedStatement checkUserPstmt = conn.prepareStatement(checkUserSql);
        checkUserPstmt.setString(1, usuario);
        ResultSet checkUserRs = checkUserPstmt.executeQuery();
        if (mod == 1) {

            String sql = "update empleados set nombre_completo = ? , usuario = ?, password = ?, activo = ? where usuario = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, usuario);
            pstmt.setString(3, password);
            pstmt.setInt(4, mod);
            pstmt.setString(5, user);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modificado Correctamente");
            return true;
        }else{
        if (checkUserRs.next() && checkUserRs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.");
            return false;  // Usuario ya existe, salimos del método
        }else{

        // Insertar nuevo usuario
        String sql = "INSERT INTO empleados (nombre_completo, usuario, password) VALUES (?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombre);
        pstmt.setString(2, usuario);
        pstmt.setString(3, password);
//        pstmt.setString(4, claveSecreta);

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Registro exitoso.");
            return true;  // Retorna true si se registró exitosamente
        } else {
            JOptionPane.showMessageDialog(null, "Error en el registro.");
            return false;  // Registro fallido
        }
        }}
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
        return false;  // Registro fallido

    } finally {
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
