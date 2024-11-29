

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author kevmt
 */
public class CRUDPuntoDeVenta {
    int renglon = 0;
    
    public static int NumVenta(String usuario) throws Exception{
        // Conectar a la base de datos
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    PreparedStatement pstmt2 = null;
    ResultSet rs2 = null;
    int id;
    int numVentas=1;
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
        String sql = "select id_empleado from empleados where usuario = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, usuario);
        
        // Ejecutar la consulta
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            
            id = rs.getInt("id_empleado");
            
            String sql2 = "select count(id_empleado)as totalVentas from ventas where id_empleado = ?";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, id);
            
            rs2 = pstmt2.executeQuery();
            if(rs2.next()){
                numVentas = rs2.getInt("totalVentas");
                return numVentas+1;
            }
            
        }return numVentas;
        
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        throw new Exception("Error al conectar a la base de datos: " + e.getMessage());
    } finally {
        // Cerrar la conexión y liberar recursos
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (rs2 != null) rs2.close();
            if (pstmt2 != null) pstmt2.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
    public int idProducto(String codigo) throws ClassNotFoundException, SQLException{
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
        String consulta = "select id_producto from productos where codigo = ? ";
        pstmt = conn.prepareStatement(consulta);
        pstmt.setString(1, codigo);
        
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            id = rs.getInt("id_producto");
            return id;
        }else{return id;}
    
    
}
    public boolean estadescontinuado(String codigo) throws ClassNotFoundException, SQLException{
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int esta = -1;
    
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String dbUser = "root"; 
        String dbPassword = "root"; 

        // Establecer la conexión
        conn = DriverManager.getConnection(url, dbUser, dbPassword);
        String consulta = "select descontinuado from productos where codigo = ? ";
        pstmt = conn.prepareStatement(consulta);
        pstmt.setString(1, codigo);
        
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            esta = rs.getInt("descontinuado");
            if (esta==1) {
                return true;
            }
        }
    
    return false;
}
    public int idVenta() throws ClassNotFoundException, SQLException{
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
        String consulta = "select count(id_venta) as id_venta from ventas ";
        pstmt = conn.prepareStatement(consulta);
        
        
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            id = rs.getInt("id_venta");
            return id+1;
        }else{return id;}
    
    
}
    public int stock(String codigo) throws ClassNotFoundException, SQLException{
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
        String consulta = "select stock from productos where codigo = ?";
        pstmt = conn.prepareStatement(consulta);
        pstmt.setString(1, codigo);
        
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            id = rs.getInt("stock");
            return id;
        }else{return id;}
    
    
}

    public void añadirTabla(String codigo,JTable tabla,int cantidad) throws Exception{
        PuntoDeVenta pv = new PuntoDeVenta();
        boolean añadir = false;
        
        
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
             

            
                String sql = "SELECT codigo, descripcion, precio, descontinuado FROM productos where codigo = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, codigo.trim());
                
                // Ejecutar la consulta
                rs = pstmt.executeQuery();
                
                DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                //modelo.setRowCount(0);
                boolean descontinuado=false;
//                if (rs.next()) {
//                    if (rs.getInt("descontinuado")==1) {
//                        descontinuado = true;
//                    }else{descontinuado = false;}
//                } 
                
                
                if (rs.next()) {
                    if (rs.getInt("descontinuado")==1) {
                        descontinuado = true;
                    }else{descontinuado = false;}
                    
                    
                    String codigoTabla = rs.getString("codigo");
                    String descripcion = rs.getString("descripcion");
                    double precio = rs.getDouble("precio");
                    
                    if (modelo.getRowCount()==0) {
                        Object objeto = new Object[]{codigoTabla,cantidad,descripcion,precio,precio*cantidad};
                        modelo.addRow((Object[]) objeto);
                        //System.out.println(modelo.getRowCount());
                    }else{
                        for (int i = 0; i < modelo.getRowCount(); i++) {
                            
                            if (modelo.getValueAt(i, 0).equals(codigo)) {
                                //System.out.println(i);
                                modelo.setValueAt((int)modelo.getValueAt(i, 1)+cantidad, i, 1);
                                modelo.setValueAt((int)modelo.getValueAt(i, 1)*precio, i, 4);
                                //System.out.println("si es igual");
                                añadir = false;
                                break;
                            }else{
                                añadir =true;
                            }
                        }
                        if (añadir && !descontinuado) {
                            
                                Object objeto = new Object[]{codigoTabla,cantidad,descripcion,precio,precio*cantidad};
                                modelo.addRow((Object[]) objeto);
                                
                        }
                        
                    }
                    
                    
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "No Existe Ese Producto");
                }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
    
    public static double total(JTable tabla){
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        double total=0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += (double)modelo.getValueAt(i, 4);
        }
        return total;
    }
    
    public static int CantidadProductos(JTable tabla){
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        int cantProd=0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            cantProd += (int)modelo.getValueAt(i, 1);
        }
        return cantProd;
    }
    
    
    
    public static void CargarEditar(int renglon,JTable tabla) throws ClassNotFoundException, SQLException, Exception{
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        String codigo = (String)modelo.getValueAt(renglon, 0);
        Agregar_Modificar am = new Agregar_Modificar();
        if (codigo == null) {
            System.out.println("no tiene nada");
        }
        
        
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
        String sql = "select codigo,nombre, descripcion,precio,stock,stockminimo,costo from productos where codigo = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, codigo);
        
        // Ejecutar la consulta
        rs = pstmt.executeQuery();
        
         if (rs.next()) {
            // Obtenemos el hash almacenado en la base de datos
            //id = rs.getInt("id_empleado");
//            am.llenar(codigo, rs.getString("nombre"), rs.getString("descripcion"),rs.getString("precio"), rs.getString("stock"), rs.getString("stockminimo"), rs.getString("costo"));
//            am.setVisible(true);
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
    
    
    //----------------------------------------------------------------------------
    public void realizarTransaccion(DefaultTableModel  tabla,int empleadoId, String fecha,double total,String user,int cliente) throws ClassNotFoundException, SQLException, Exception  {
          DefaultTableModel modelo = tabla;
         List<String> queries = new ArrayList<>();
         
         String sale = "INSERT INTO ventas (id_venta,fecha,id_empleado, total,id_cliente) VALUES (" + null + ",'" + fecha + "'," + empleadoId + ", " + total + ", " + cliente + ")";
                    queries.add(sale);
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object CodDelProducto = modelo.getValueAt(i, 0); 
            Object cantidadDelProducto = modelo.getValueAt(i, 1); 
            Object importeDelProducto = modelo.getValueAt(i, 4); 
            Object precioDelProducto=modelo.getValueAt(i,3);
            if (CodDelProducto != null && cantidadDelProducto != null) {
                String Cod = (String)CodDelProducto;
                int cantidad = (int) cantidadDelProducto;
                double importe=(double)importeDelProducto;
                double precio=(double)precioDelProducto;
                int stock = stock(Cod);
                if (cantidad <= stock) {
                    
                    queries.add("SET @saleId = LAST_INSERT_ID();");
                    String query = "UPDATE productos SET stock = stock - " + cantidad + 
                                   " WHERE codigo = '" + Cod + "' AND stock >= " + cantidad;
                    queries.add(query);
                    String Descontinuar = "UPDATE productos SET descontinuado = 1 WHERE codigo = '" + Cod + "' AND stock <= 0";
                    queries.add(Descontinuar);
                    String sale_details = "INSERT INTO detalle_ventas (id_detalle, id_venta,id_empleado, id_producto,id_cliente, cantidad, precio_unitario, subtotal) " +
                                                "VALUES (" + null + ", " + idVenta() + "," + empleadoId + "," + idProducto(Cod) + "," + cliente +", " + cantidad + ", " + precio + ", " + importe + ")";
                    queries.add(sale_details);
                }else{
                    JOptionPane.showMessageDialog(null, "No cuentas con esa cantidad de producto", "Error en la compra", JOptionPane.ERROR_MESSAGE);


                }
                
            }
        }

        ImprimirTicket(modelo,user,fecha,total);
        //Conexion conexion = new Conexion("tienda");
        ejecutarTransaccion(queries.toArray(new String[0]));
        
    }
    
    
    
    
    public void ejecutarTransaccion(String[] queries) throws ClassNotFoundException, SQLException {
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn;
        // URL de conexión con la base de datos
        String url = "jdbc:mysql://localhost:3306/PuntoDeVenta?serverTimezone=America/Mexico_City&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String dbUser = "root";  // Usuario de la base de datos
        String dbPassword = "root";  // Contraseña de la base de datos

        // Establecer la conexión
        conn = DriverManager.getConnection(url, dbUser, dbPassword);
        try {
            
            
            conn.setAutoCommit(false); 
            Statement stmt = conn.createStatement();
            System.out.println(idVenta());
            for (String query : queries) {
                System.out.println(query);
                stmt.executeUpdate(query);
                System.out.println(query);
            }
            conn.commit();
            System.out.println("Transaccion ejecutada exitosamente.");
            
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Transacción revertida.");
            } catch (SQLException ex1) {
                System.out.println(ex1);
            }
            
        } finally {
            conn.close();
        }
    }
    //----------------------------------------------------------------------------
    
    public void ImprimirTicket(DefaultTableModel  tabla,String user, String fecha,double total) {
            LocalDateTime f = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHoraFormateada = f.format(formato);
            int n  =contarTickets();
            String nombreArchivo = "ticket"+n+".txt";
            File Archivo=new File(nombreArchivo);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Archivo))) {
                writer.write("===== Ticket de Venta =====");
                writer.newLine();
                writer.write("Empleado: " + user);
                writer.newLine();
                writer.write("Fecha: " + fechaHoraFormateada);
                writer.newLine();
                writer.write("===========================");
                writer.newLine();
                writer.write("Detalles de la Venta:");
                writer.newLine();
                writer.write(String.format("%-15s %-15s %-15s %-15s", "Codigo Producto", "Cantidad", "Precio Unit.", "Total"));
                writer.newLine();
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    String Cod = (String) tabla.getValueAt(i, 0);
                    int cantidad = (int) tabla.getValueAt(i, 1);
                    double precio = (double) tabla.getValueAt(i, 3);
                    double importe = (double) tabla.getValueAt(i, 4);

                    writer.write(String.format("%-15s %-15d %-15.2f %-15.2f", Cod, cantidad, precio, importe));
                    writer.newLine();
           
                }
                writer.write("===========================");
                writer.newLine();
//                writer.write(String.format("IVA 16p:  %.2f",iva));
//                writer.newLine();
                writer.write(String.format("Total de la venta: %.2f", total));
                writer.newLine();
                writer.write("===========================");
                writer.newLine();
                writer.write("Gracias por su compra!");
                writer.newLine();
        
                System.out.println("Ticket generado exitosamente en " + nombreArchivo);
            } catch (IOException e) {
                e.printStackTrace();
            }

}
    
    private int contarTickets() {
        int n = 1;
        File archivoDelTicket = new File("ticket" + n + ".txt");
        // busca el ticket que hay y obtiene el siguiente
        while (archivoDelTicket.exists()) {
            n++;
            archivoDelTicket = new File("ticket" + n + ".txt");
        }
        
        return n;
    }
    
    
    
}

