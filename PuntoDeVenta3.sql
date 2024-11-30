-- Creamos esta Base de Datos para un punto de venta
Create database if not exists PuntoDeVenta;
use PuntoDeVenta;

-- En esta seccion se crearan las tablas necesarias para nuestro punto de venta
-- ---------------------------------------------------------------------------------------------------------------------------------------
create table Empleados(
id_empleado	int	not null	primary key unique	auto_increment,
nombre_completo	varchar(100)	not null,
usuario	varchar(20) not null unique,
password	varchar(64) not null,
activo		tinyint(1) default 1 not null
);


create table ventas(
id_venta	int not null	primary key auto_increment,
fecha datetime not null,
id_empleado int not null,
total double	not null,
id_cliente int not null,
foreign key (id_cliente) references clientes(id_cliente),
foreign key (id_empleado) references Empleados(id_empleado)
);


create table productos(
id_producto int not null primary key auto_increment,
codigo Varchar(13) not null unique,
nombre varchar(20) not null,
descripcion varchar(50),
precio double not null,
stock int not null,
stockMinimo int ,
costo double,
descontinuado tinyint(1) default 0 not null
);


create table detalle_ventas(
id_detalle	int not null	primary key	auto_increment,
id_venta int not null,
id_empleado int not null,
id_producto int not null,
id_cliente int not null,
cantidad int not null,
precio_unitario double not null,
subtotal double not null,

foreign key (id_cliente) references clientes(id_cliente),
foreign key (id_venta) references ventas(id_venta),
foreign key (id_producto) references productos(id_producto),
foreign key (id_empleado) references empleados(id_empleado)
);


CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,         
    nombre VARCHAR(100) NOT NULL,                      
    correo VARCHAR(150),                               
    telefono char(10),                              
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP, 
    nivel_lealtad ENUM('Mostrador','Bronce', 'Plata', 'Oro') DEFAULT 'Mostrador', 
    activo BOOLEAN DEFAULT TRUE                        
);


-- Se debe de crear este Cliente ya que sera el cliente de Mostrador
-- INSERT INTO clientes (id_cliente,nombre,correo,telefono) values(1,'Mostrador','correoMostrador@gmail.com',4451212121);
-- ---------------------------------------------------------------------------------------------------------------------------------------


-- En esta seccion se crean los stored procedures de Insert, Update y Delete para los Clientes
-- ---------------------------------------------------------------------------------------------------------------------------------------
-- ------------------------------------------------------
-- El stored procedure InsertarCliente pide como parametros un nombre,
-- correo, telefono y nivel de lealtad para poder insertarlo
-- ------------------------------------------------------
show procedure status;
DELIMITER //

		CREATE PROCEDURE InsertarCliente(
			IN p_nombre VARCHAR(100),       
			IN p_correo VARCHAR(150),       
			IN p_telefono char(10),      
			IN p_nivel_lealtad ENUM('Mostrador','Bronce', 'Plata', 'Oro')
		)
		BEGIN
				INSERT INTO clientes (nombre, correo, telefono, fecha_registro, nivel_lealtad)
				VALUES (p_nombre, p_correo, p_telefono, NOW(), p_nivel_lealtad);
		END$$

DELIMITER ;
        -- ---------------------------------------------------
-- El stored procedure EliminarCliente pide como parametro el id del cliente
-- que sera visible en la tabla de clientes
DELIMITER //

		CREATE PROCEDURE EliminarCliente(
			IN p_id int    
		)
		BEGIN
				update clientes set activo = false where id_cliente = p_id;
		END$$

DELIMITER ;
        -- ---------------------------------------------------------
-- El stored procedure ActualizarCliente pide como parametros un nombre,
-- correo, telefono , nivel de lealtad y el id para poder actualizarlo
DELIMITER //

		CREATE PROCEDURE ActualizarCliente(
			IN p_nombre VARCHAR(100),       
			IN p_correo VARCHAR(150),       
			IN p_telefono char(10),      
			IN p_nivel_lealtad ENUM('Mostrador','Bronce', 'Plata', 'Oro'),
            IN p_id int
		)
		BEGIN
                update clientes set nombre = p_nombre, correo = p_correo, telefono = p_telefono,
                fecha_registro = now(), nivel_lealtad = p_nivel_lealtad, activo = 1 where id_cliente = p_id;
		END$$

DELIMITER ;
drop procedure InsertarCliente;


-- ---------------------------------------------------------------------------------------------------------------------------------------


-- Esta seccion se crea el stored procedure para insertar ventas aleatorias
-- ---------------------------------------------------------------------------------------------------------------------------------------
-- este stored procedure solo pide como parametro cuantas ventas quieres que genere.
-- DEntro de el declara varias variables que seran para almacenar los resultados de las consultas
-- necesarias para poder hacer todo de manera aleatoria, insertando la venta en la tabla de ventas
-- y haciendo una insercion en la tabla de detalles de venta
DELIMITER //
CREATE PROCEDURE insertar_ventas_aleatorias(IN num_ventas INT)
BEGIN
    DECLARE contador INT DEFAULT 1;
    DECLARE contador_productos INT;
    
    DECLARE cliente_id INT;
    DECLARE empleado_id INT;
    DECLARE producto_id INT;
    DECLARE nueva_venta_id INT;
    DECLARE num_productos INT;
    DECLARE subtotal DECIMAL(10, 2);
    DECLARE total DECIMAL(10, 2);
    DECLARE cantidad_producto INT;
    DECLARE precio_producto DECIMAL(10, 2);
    DECLARE base_calculo DECIMAL(10, 2);
    DECLARE duplicado INT;

    WHILE contador <= num_ventas DO
        -- Seleccionar un cliente y empleado aleatorio
        SET cliente_id = (SELECT id_cliente FROM clientes ORDER BY RAND() LIMIT 1);
        SET empleado_id = (SELECT id_empleado FROM empleados ORDER BY RAND() LIMIT 1);
        
        -- Número aleatorio de productos por venta (entre 1 y 15)
        SET num_productos = FLOOR(RAND() * (15 - 1 + 1)) + 1;
        
        SET contador_productos = 1;
        SET subtotal = 0;
        SET total = 0;

        -- Insertar una nueva venta
        INSERT INTO ventas (fecha, id_empleado, id_cliente, total)
        VALUES (DATE_ADD('2024-01-01', INTERVAL FLOOR(RAND() * 366) DAY), empleado_id, cliente_id, 0);
        SET nueva_venta_id = LAST_INSERT_ID();

        -- Agregar detalles de venta
        WHILE contador_productos <= num_productos DO
            SET producto_id = (SELECT id_producto FROM productos ORDER BY RAND() LIMIT 1);
            
            -- Verificar si el producto ya está en el detalle de la venta
            SET duplicado = (SELECT COUNT(*) FROM detalle_ventas WHERE id_venta = nueva_venta_id AND id_producto = producto_id);
            IF duplicado = 0 THEN
                -- Generar cantidad y precio del producto
                SET cantidad_producto = FLOOR(RAND() * (10 - 1 + 1)) + 1;
                SET precio_producto = (SELECT precio FROM productos WHERE id_producto = producto_id);
                
                -- Calcular subtotal y agregar detalle
                SET base_calculo = cantidad_producto * precio_producto;
                INSERT INTO detalle_ventas (id_venta, id_producto, id_empleado, id_cliente, cantidad, precio_unitario, subtotal)
                VALUES (nueva_venta_id, producto_id, empleado_id, cliente_id, cantidad_producto, precio_producto, base_calculo);
                update productos set stock = stock-cantidad_producto where id_producto = producto_id;
                SET subtotal = subtotal + base_calculo;
            END IF;
            
            SET contador_productos = contador_productos + 1;
        END WHILE;

        -- Actualizar total de la venta
        UPDATE ventas SET total = subtotal WHERE id_venta = nueva_venta_id;

        SET contador = contador + 1;
    END WHILE;
END //
DELIMITER ;


-- ---------------------------------------------------------------------------------------------------------------------------------------


-- En esta seccion se crean las vistas necesarias para los reportes de ventas
-- ---------------------------------------------------------------------------------------------------------------------------------------
-- Esta vista nos genera las columnas necesarias para poder tener
-- un reporte de ventas mensual detallado.
-- Se utilizaron las tablas clientes, ventas, empleados y detalle de venta
CREATE VIEW reporte_ventas_mensual AS
SELECT 
    v.id_venta,
    DATE_FORMAT(v.fecha, '%Y-%m-%d') AS Fecha,
    c.nombre AS Cliente,
    e.nombre_completo AS Empleado,
    v.total AS Total,
    COUNT(dv.id_detalle) AS Cant_Detalles
    -- SUM(dv.cantidad) AS cantidad_total
FROM 
    ventas v
JOIN 
    clientes c ON v.id_cliente = c.id_cliente
JOIN 
    empleados e ON v.id_empleado = e.id_empleado
JOIN 
    detalle_ventas dv ON v.id_venta = dv.id_venta

GROUP BY 
    v.id_venta, v.fecha, c.nombre, e.nombre_completo, v.total
order by v.fecha asc; 
-- ------------------------------------------------------------------
-- ------------------------------------------------------------------
-- Esta vista solo muestra el empleado junto con todo lo que ha vendido y cuantas ventas tuvo
CREATE VIEW reporte_ventas_empleado AS
SELECT 
    e.nombre_completo AS Empleado,
    SUM(v.total) AS Total_Vendido,
    COUNT(v.id_venta) AS Cantidad_Ventas
FROM 
    empleados e
LEFT JOIN 
    ventas v ON e.id_empleado = v.id_empleado
GROUP BY 
    e.nombre_completo
ORDER BY 
    Total_Vendido DESC;
-- ----------------------------------------------------------
-- ----------------------------------------------------------
-- Este reporte solo muetra el producto y que tanto se vendio en cada trimestre
CREATE VIEW reporte_ventas_trimestrales AS
SELECT 
    p.nombre AS Producto,
    SUM(CASE WHEN QUARTER(v.fecha) = 1 THEN dv.cantidad ELSE 0 END) AS Trim1,
    SUM(CASE WHEN QUARTER(v.fecha) = 2 THEN dv.cantidad ELSE 0 END) AS Trim2,
    SUM(CASE WHEN QUARTER(v.fecha) = 3 THEN dv.cantidad ELSE 0 END) AS Trim3,
    SUM(CASE WHEN QUARTER(v.fecha) = 4 THEN dv.cantidad ELSE 0 END) AS Trim4
FROM 
    detalle_ventas dv
JOIN 
    ventas v ON dv.id_venta = v.id_venta
JOIN 
    productos p ON dv.id_producto = p.id_producto
GROUP BY 
    p.nombre
ORDER BY 
    Producto;

-- ----------------------------------------------------------
-- ----------------------------------------------------------
-- ---------------------------------------------------------------------------------------------------------------------------------------


-- Aqui se crea la tabla para la auditoria de la tabla Ventas
-- ---------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE auditoria_ventas (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT,
    accion VARCHAR(10), -- 'INSERT', 'UPDATE' o 'DELETE'
    usuario VARCHAR(100), -- Usuario que realizó la acción
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora del cambio
    datos_anteriores TEXT, -- Datos antes del cambio 
    datos_nuevos TEXT -- Datos después del cambio 
);
-- ---------------------------------------------------------------------------------------------------------------------------------------


-- Esta seccion es para los triggers de Insert, Update y Delete que se aplican en la tabla Ventas
-- ---------------------------------------------------------------------------------------------------------------------------------------
-- Estos triggers lo que hacen es insertar los datos de un cambio en la tabla ventas
-- esto incluye la fecha como tambien que cambio se hizo y quien lo hizo 
DELIMITER //
CREATE TRIGGER ventas_insert
AFTER INSERT ON ventas
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_ventas (id_venta, accion, usuario, datos_nuevos)
    VALUES (
        NEW.id_venta,
        'INSERT',
        USER(), 
        CONCAT(
            'Fecha: ', NEW.fecha, ', ',
            'Empleado: ', NEW.id_empleado, ', ',
            'Cliente: ', NEW.id_cliente, ', ',
            'Total: ', NEW.total
        )
    );
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER ventas_update
AFTER UPDATE ON ventas
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_ventas (id_venta, accion, usuario, datos_anteriores, datos_nuevos)
    VALUES (
        OLD.id_venta,
        'UPDATE',
        USER(), 
        CONCAT(
            'Fecha: ', OLD.fecha, ', ',
            'Empleado: ', OLD.id_empleado, ', ',
            'Cliente: ', OLD.id_cliente, ', ',
            'Total: ', OLD.total
        ),
        CONCAT(
            'Fecha: ', NEW.fecha, ', ',
            'Empleado: ', NEW.id_empleado, ', ',
            'Cliente: ', NEW.id_cliente, ', ',
            'Total: ', NEW.total
        )
    );
END;
//
DELIMITER ;


DELIMITER //
CREATE TRIGGER ventas_delete
AFTER DELETE ON ventas
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_ventas (id_venta, accion, usuario, datos_anteriores)
    VALUES (
        OLD.id_venta,
        'DELETE',
        USER(), -- Usuario que ejecutó el cambio
        CONCAT(
            'Fecha: ', OLD.fecha, ', ',
            'Empleado: ', OLD.id_empleado, ', ',
            'Cliente: ', OLD.id_cliente, ', ',
            'Total: ', OLD.total
        )
    );
END;
//
DELIMITER ;

-- ---------------------------------------------------------------------------------------------------------------------------------------


-- Este ultimo bloque es para los triggers de Insert y Update para la tabla Productos
-- ---------------------------------------------------------------------------------------------------------------------------------------
-- Estos triggers lo que hacen es cuidar que una insercion o una actualizacion
-- no tengan datos erroneos como el que un producto tenga un numero negativo
-- o que no se tenga un codigo o que no tenga la longitud correcta
DELIMITER //

CREATE TRIGGER validar_producto
BEFORE INSERT ON productos
FOR EACH ROW
BEGIN
    -- Validar que el precio no sea negativo
    IF NEW.precio < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio no puede ser negativo.';
    END IF;

    -- Validar que el nombre no esté vacío ni tenga solo espacios en blanco
    IF TRIM(NEW.nombre) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre del producto no puede estar vacío ni contener solo espacios.';
    END IF;

    -- Validar que el código de barras, si no es NULL, tenga entre 8 y 20 caracteres
    IF NEW.codigo IS NOT NULL AND (CHAR_LENGTH(NEW.codigo) < 8 OR CHAR_LENGTH(NEW.codigo) > 20) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El código de barras debe tener entre 8 y 20 caracteres.';
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER validar_producto_update
BEFORE UPDATE ON productos
FOR EACH ROW
BEGIN
    -- Validar que el precio no sea negativo
    IF NEW.precio < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio no puede ser negativo.';
    END IF;

    -- Validar que el nombre no esté vacío ni tenga solo espacios en blanco
    IF TRIM(NEW.nombre) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre del producto no puede estar vacío ni contener solo espacios.';
    END IF;

    -- Validar que el código de barras, si no es NULL, tenga entre 8 y 20 caracteres
    IF NEW.codigo IS NOT NULL AND (CHAR_LENGTH(NEW.codigo) < 8 OR CHAR_LENGTH(NEW.codigo) > 20) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El código de barras debe tener entre 8 y 20 caracteres.';
    END IF;
END;
//

DELIMITER ;
-- ---------------------------------------------------------------------------------------------------------------------------------------

