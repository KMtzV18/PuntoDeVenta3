
import javax.swing.JTable;
import com.mysql.cj.jdbc.CallableStatement;
import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author kevmt
 */
public class Reportes {
 
    //Estos metodos consultan la base de datos para traer la informacion de las vistas creadas
    // asi teniendo un mejor control de lo que queremos ver
    
    //Este metodo se encarga de cargar la tabla de reportes por mes
    // teniendo como parametros la tabla y el numero de mes que se eligio
    public static void CargarReporteMes(JTable tabla,int mes) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); 
        String[] columnas = {"id_venta", "Fecha", "Cliente", "Empleado", "Total", "Cant_Detalles"};
        String m ="";
        if (mes<=9) {
            m = "0"+mes;
        }else{m = mes+"";}
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // URL de conexión con la base de datos
            String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
            String dbUser = "root";  // Usuario de la base de datos
            String dbPassword = "root";  // Contraseña de la base de datos

            // Establecer la conexión
            conn = DriverManager.getConnection(url, dbUser, dbPassword);

            // Consulta SQL para obtener los datos de empleados

            String fechaI = "2024-" + m + "-01";
            String fechaF = "2024-" + m + "-31";
            
            String sql = "SELECT * FROM reporte_ventas_mensual WHERE Fecha BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaI);
            pstmt.setString(2, fechaF);
            rs = pstmt.executeQuery();

            // Procesar los resultados
            while (rs.next()) {
                Object[] fila = new Object[columnas.length];

                for (int j = 0; j < columnas.length; j++) {
                    fila[j] = rs.getString(columnas[j]);
                }

                modelo.addRow(fila);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
    
     //Este metodo se encarga de cargar la tabla de reportes por empleado
    // teniendo como parametro la tabla 
    public static void CargarReporteEmpleado(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); 
        String[] columnas = {"Empleado", "Total_Vendido", "Cantidad_Ventas"};
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // URL de conexión con la base de datos
            String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
            String dbUser = "root";  // Usuario de la base de datos
            String dbPassword = "root";  // Contraseña de la base de datos

            // Establecer la conexión
            conn = DriverManager.getConnection(url, dbUser, dbPassword);

            // Consulta SQL para obtener los datos de empleados

            
            
            String sql = "SELECT * FROM reporte_ventas_empleado";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();

            // Procesar los resultados
            while (rs.next()) {
                Object[] fila = new Object[columnas.length];

                for (int j = 0; j < columnas.length; j++) {
                    fila[j] = rs.getString(columnas[j]);
                }

                modelo.addRow(fila);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
    
     //Este metodo se encarga de cargar la tabla de reportes por trimestre
    // teniendo como parametro la tabla 
    public static void CargarReporteTrimestral(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); 
        String[] columnas = {"Producto", "Trim1", "Trim2", "Trim3", "Trim4"};
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // URL de conexión con la base de datos
            String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
            String dbUser = "root";  // Usuario de la base de datos
            String dbPassword = "root";  // Contraseña de la base de datos

            // Establecer la conexión
            conn = DriverManager.getConnection(url, dbUser, dbPassword);

            // Consulta SQL para obtener los datos de empleados

            
            
            String sql = "select * from reporte_ventas_trimestrales";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();

            // Procesar los resultados
            while (rs.next()) {
                Object[] fila = new Object[columnas.length];

                for (int j = 0; j < columnas.length; j++) {
                    fila[j] = rs.getString(columnas[j]);
                }

                modelo.addRow(fila);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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


