

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kevmt
 */
public class CRUDAgregar_ModificarP {
    
    // La funcion de este metodo es agregar o actualizar productos, pide como prametros
    //toda la informacion del producto para insertarlo en la base de datos
    public static boolean Agregar(String codigo, String nombre, String descripcion, double precio, int stock, int stockminimo, double costo, boolean descontinuado) throws Exception {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    boolean Añadir_N = true;
    
    try {
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");

        // URL de conexión con la base de datos
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String dbUser = "root";  
        String dbPassword = "root";

        // Establecer una sola conexión
        conn = DriverManager.getConnection(url, dbUser, dbPassword);
        
        // Verificar si el código ya existe
        String verificarCodigo = "SELECT codigo FROM productos WHERE codigo = ?";
        pstmt = conn.prepareStatement(verificarCodigo);
        pstmt.setString(1, codigo);
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            // Si el código existe, se establece Añadir_N a false
            Añadir_N = false;
        }
        
        // Cerrar el ResultSet y PreparedStatement de la consulta de verificación
        rs.close();
        pstmt.close();

        if (Añadir_N) {
            // Si el código no existe, se realiza la inserción
            
            String sql = "INSERT INTO productos (codigo, nombre, descripcion, precio, stock, stockminimo, costo, descontinuado) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigo);
            pstmt.setString(2, nombre);
            pstmt.setString(3, descripcion);
            pstmt.setDouble(4, precio);
            pstmt.setInt(5, stock);
            pstmt.setInt(6, stockminimo);
            pstmt.setDouble(7, Double.isNaN(costo) ? 0.0 : costo);  // Valor predeterminado si costo es NaN
            pstmt.setBoolean(8,descontinuado);

            pstmt.executeUpdate();
            
            return true;
        } else {
            // Si el código existe, se realiza la actualización
            
            String update = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, stockminimo = ?, costo = ?, descontinuado = ? WHERE codigo = ?";
            pstmt = conn.prepareStatement(update);
            pstmt.setString(1, nombre);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precio);
            pstmt.setInt(4, stock);
            pstmt.setInt(5, stockminimo);
            pstmt.setDouble(6, Double.isNaN(costo) ? 0.0 : costo);
            pstmt.setBoolean(7,descontinuado);
            pstmt.setString(8, codigo);

            pstmt.executeUpdate();
            
            return true;
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
    
    
    //--------------------------PRODUCTOS----------------------------------------------------------------
    // Este metodo es para cargar la tabla de la vista de productos
    //donde podemos ver los productos que tenemos registrados, el parametro es la tabla donde vamos a ver los productos
    public static void CargarTabla(JTable tabla) throws ClassNotFoundException, SQLException, Exception{
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        //String codigo = (String)modelo.getValueAt(renglon, 0);
        Agregar_Modificar am = new Agregar_Modificar();
        modelo.setRowCount(0); 
        String[] columnas = {"codigo", "nombre", "descripcion","precio","stock","stockminimo","costo","descontinuado"};

        
        
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

        // Consulta SQL para verificar el usuario y contraseña (hash almacenado)
        String sql = "select * from productos";
        pstmt = conn.prepareStatement(sql);
        //pstmt.setString(1, codigo);
        
        // Ejecutar la consulta
        rs = pstmt.executeQuery();
        
         while (rs.next()) {
            Object[] fila = new Object[columnas.length];  
            
            for (int j = 0; j < columnas.length; j++) {
                fila[j] = rs.getObject(columnas[j]); 
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
    
    //Este metodo carga la vista de insertar productos para ver su informacion y asi poder ver que vamos a modificar
    //el parametro es el codigo del producto que sera visible en la tabla
    public static void Editar(String codigo)throws ClassNotFoundException, SQLException, Exception{
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Agregar_Modificar am = new Agregar_Modificar();
        
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
        String sql = "select codigo,nombre, descripcion,precio,stock,stockminimo,costo, descontinuado from productos where codigo = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, codigo);
        
        // Ejecutar la consulta
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            // Obtenemos el hash almacenado en la base de datos
            //id = rs.getInt("id_empleado");
            am.llenar(codigo, rs.getString("nombre"), rs.getString("descripcion"),rs.getString("precio"), rs.getString("stock"), rs.getString("stockminimo"), rs.getString("costo"), rs.getBoolean("descontinuado"));
            am.setVisible(true);
            
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
    
    //Este metodo hace un borrado logico ya que solo marca como descontinuado
    //el producto para que ya no se puedan hacer mas ventas de ese producto
    //Como parametro pide el codigo del producto
    public static void EliminarProducto(String codigo){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Agregar_Modificar am = new Agregar_Modificar();
        
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
        String sql = "update productos set descontinuado = 1 where codigo = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, codigo);
        
        // Ejecutar la consulta
        pstmt.executeUpdate();
    
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
