
import com.mysql.cj.jdbc.CallableStatement;
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
public class CRUDClientes {
    
    public static void CargarTablaClientes(JTable tabla) throws ClassNotFoundException, SQLException, Exception {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0); 
    String[] columnas = {"id_cliente", "Nombre", "Correo", "Telefono", "Fecha_Registro", "Nivel_Lealtad", "Activo"};
    
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
        String sql = "SELECT * FROM clientes";
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
    
    public boolean insertar(String nombre, String correo, String telefono,String lealtad,int act) throws Exception{
        Connection conn = null;
        
        CallableStatement cstmt = null;
        if (nombre.isEmpty() || telefono.isEmpty()){
            JOptionPane.showMessageDialog(null, "LLena todos los campos");
            return false;
        }else if(correo.isEmpty() || !correo.endsWith("@gmail.com")){
            JOptionPane.showMessageDialog(null, "Verifica el correo");
            return false;
        }else if(telefono.length()>10 || telefono.length()<10){
            JOptionPane.showMessageDialog(null, "Verifica El Numero de Telefono");
            return false;
        }
        
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
        if (act == 0) {
                
        
        String sql = "{call InsertarCliente(?, ?, ?, ?)}";
        cstmt = (CallableStatement) conn.prepareCall(sql);
        cstmt.setString(1, nombre);
        cstmt.setString(2, correo);
        cstmt.setString(3, telefono);
        cstmt.setString(4, lealtad);
        
        cstmt.execute();
        return true;
        }else{
            String sql = "{call ActualizarCliente(?, ?, ?, ?,?)}";
            cstmt = (CallableStatement) conn.prepareCall(sql);
            cstmt.setString(1, nombre);
            cstmt.setString(2, correo);
            cstmt.setString(3, telefono);
            cstmt.setString(4, lealtad);
            cstmt.setInt(5,new Clientes().getMod());

            cstmt.execute();
            return true;
        }
    
        
        
        
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        // Cerrar la conexión y liberar recursos
        try {
            if (cstmt != null) cstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    }
    
    
    
    
    
    public boolean Eliminar(String id){
        Connection conn = null;
        
        CallableStatement cstmt = null;
        
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
        String sql = "{call EliminarCliente(?)}";
        cstmt = (CallableStatement) conn.prepareCall(sql);
        cstmt.setString(1, id);
        
        
        cstmt.execute();
        System.out.println("se elimino");
        return true;
    
        
        
        
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        // Cerrar la conexión y liberar recursos
        try {
            if (cstmt != null) cstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    }
    
    
public static void Editar(int id)throws ClassNotFoundException, SQLException, Exception{
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Agregar_ModificarClientes amc = new Agregar_ModificarClientes();
        int lealtad = -1;
        
        try {
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");

        // URL de conexión con la base de datos
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String dbUser = "root";  // Usuario de la base de datos
        String dbPassword = "root";  // Contraseña de la base de datos

        // Establecer la conexión
        conn = DriverManager.getConnection(url, dbUser, dbPassword);

        
        String sql = "select nombre, correo, telefono,nivel_lealtad, activo from clientes where id_cliente = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        
        // Ejecutar la consulta
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
           
            if (rs.getString("nivel_lealtad").equals("Mostrador")) {
                lealtad = 0;
            }else if(rs.getString("nivel_lealtad").equals("Bronce")){
                lealtad = 1;
            }else if(rs.getString("nivel_lealtad").equals("Plata")){
                lealtad = 2;
            }else if(rs.getString("nivel_lealtad").equals("Oro")){
                lealtad = 3;
            }
            amc.llenar(rs.getString("nombre"), rs.getString("correo"),rs.getString("telefono"),lealtad,rs.getBoolean("activo") );
            amc.setVisible(true);
            
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
