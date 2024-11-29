
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kevmt
 */
class Empleado {
    public static String usuario;
    public static int caja;
    public static int NumVenta;

    public Empleado() {
    }
    
    

    public Empleado(String usuario) {
        this.usuario = usuario;
    }
    
    

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        Empleado.usuario = usuario;
    }

    public static int getCaja() {
        return caja;
    }

    public static void setCaja(int caja) {
        Empleado.caja = caja;
    }

    public static int getNumVenta() {
        return NumVenta;
    }

    public static void setNumVenta(int NumVenta) {
        Empleado.NumVenta = NumVenta;
    }
    
    public int idEmpleado(String usuario) throws ClassNotFoundException, SQLException{
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int id = -1;
    
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String dbUser = "root"; 
        String dbPassword = "root"; 

        // Establecer la conexión
        conn = DriverManager.getConnection(url, dbUser, dbPassword);
        String consulta = "select id_empleado from empleados where usuario = ? ";
        pstmt = conn.prepareStatement(consulta);
        pstmt.setString(1, usuario);
        
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            id = rs.getInt("id_empleado");
            return id;
        }else{return id;}
    
    
}
    
}
