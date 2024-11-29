Create database if not exists PuntoDeVenta;
use puntodeventa;
drop database puntodeventa;

create table Empleados(
id_empleado	int	not null	primary key unique	auto_increment,
nombre_completo	varchar(100)	not null,
usuario	varchar(20) not null unique,
password	varchar(64) not null,
activo		tinyint(1) default 1 not null
);

-- drop table empleados;
-- corre este comando para tener un usuario k y contraseña 123:
select * from empleados;
insert into empleados values (null,'Kevin Martinez Vargas', 'k',123,1);
update empleados set nombre_completo = 'KOKOKO' , usuario = 'KOKOKO', password = 321 where usuario = 'KOKOKO';

create table ventas(
id_venta	int not null	primary key auto_increment,
fecha datetime not null,
id_empleado int not null,
total double	not null,
id_cliente int not null,

foreign key (id_cliente) references clientes(id_cliente),
foreign key (id_empleado) references Empleados(id_empleado)
);

drop table ventas;


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
-- drop table productos;

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
select * from detalle_ventas;

drop table detalle_ventas;

INSERT INTO ventas (id_venta,fecha,id_empleado, total,id_cliente) VALUES (null,'2024-11-28 15:46:52',1, 32.0, 1);
INSERT INTO detalle_ventas (id_detalle, id_venta,id_empleado, id_producto,id_cliente, cantidad, precio_unitario, subtotal) VALUES (null, 1304,1,2,1, 1, 32.0, 32.0);
SELECT * FROM detalle_ventas;
SELECT * FROM ventas;
SELECT * FROM empleados WHERE id_empleado = 1;
SELECT * FROM productos WHERE id_producto = 2;
SELECT * FROM clientes WHERE id_cliente = 2;


CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,         
    nombre VARCHAR(100) NOT NULL,                      
    correo VARCHAR(150),                               
    telefono char(10),                              
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP, 
    nivel_lealtad ENUM('Mostrador','Bronce', 'Plata', 'Oro') DEFAULT 'Mostrador', 
    activo BOOLEAN DEFAULT TRUE                        
);
drop table clientes;
update clientes set nombre = 'Mostrador' where id_cliente = 1;
select * from clientes;

		DELIMITER $$

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
        DELIMITER $$

		CREATE PROCEDURE EliminarCliente(
			IN p_id int    
		)
		BEGIN
				update clientes set activo = false where id_cliente = p_id;
		END$$

		DELIMITER ;
        -- ---------------------------------------------------------
        
        DELIMITER $$

		CREATE PROCEDURE ActualizarCliente(
			IN p_nombre VARCHAR(100),       
			IN p_correo VARCHAR(150),       
			IN p_telefono char(10),      
			IN p_nivel_lealtad ENUM('Mostrador','Bronce', 'Plata', 'Oro'),
            IN p_id int
		)
		BEGIN
                update clientes set nombre = p_nombre, correo = p_correo, telefono = p_telefono,
                fecha_registro = now(), nivel_lealtad = p_nivel_lealtad where id_cliente = p_id;
		END$$

		DELIMITER ;
        
        drop procedure InsertarCliente;
        call InsertarCliente('xim','pipi@gmail.com',445,'Mostrador');
        
        select * from clientes;



/*
use puntodeventa;
DELIMITER $$ 

CREATE PROCEDURE ventaAleatoria(IN num INT)
NOT DETERMINISTIC
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE id_vent INT; -- id de la venta
    DECLARE fecha DATE; -- fecha de la venta
    DECLARE id_emplead INT; -- id del empleado
    DECLARE total DOUBLE; -- total de la venta
    DECLARE id_client INT; -- id del cliente
    DECLARE id_detalle INT; -- id del detalle de la venta
    DECLARE id_product INT; -- id del producto
    DECLARE cantidad INT; -- cantidad de productos
    DECLARE precio_unitario DOUBLE; -- precio unitario del producto
	DECLARE dif_prod int; -- productos diferentes
    
    
    -- Bucle para insertar 'num' ventas aleatorias
    WHILE i < num DO
        -- Generar fecha aleatoria
        SELECT DATE_ADD('2024-01-01', INTERVAL FLOOR(RAND() * (DATEDIFF('2024-12-31', '2024-01-01') + 1)) DAY) INTO fecha;

        -- Obtener id_empleado aleatorio
        SELECT id_empleado INTO id_emplead FROM empleados ORDER BY RAND() LIMIT 1;

        -- Si no se obtiene un id_empleado, lanzar error
        IF id_emplead IS NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo obtener id_empleado';
        END IF;

        -- Obtener id_producto aleatorio
        SELECT id_producto INTO id_product FROM productos ORDER BY RAND() LIMIT 1;

        -- Obtener id_cliente aleatorio
        SELECT id_cliente INTO id_client FROM clientes ORDER BY RAND() LIMIT 1;

        -- Obtener precio del producto
        SELECT precio INTO precio_unitario FROM productos WHERE id_producto = id_product;

        -- Generar cantidad aleatoria entre 1 y 25
        SELECT FLOOR(1 + (RAND() * 25)) INTO cantidad;

        -- Calcular total
        SET total = precio_unitario * cantidad;
		select FLOOR(1 + (RAND()*9))into dif_prod;
	
        while dif_prod > 0 do 
        
        -- Insertar venta
        INSERT INTO ventas (fecha, id_empleado, total, id_cliente) 
        VALUES (fecha, id_emplead, total, id_client);

        -- Obtener el último id_venta insertado
        SELECT LAST_INSERT_ID() INTO id_vent;

        -- Insertar detalle de la venta
        INSERT INTO detalle_ventas (id_venta, id_empleado, id_producto, id_cliente, cantidad, precio_unitario, subtotal)
        VALUES (id_vent, id_emplead, id_product, id_client, cantidad, precio_unitario, total);
		
        set dif_prod = dif_prod-1;
        end while;
        -- Incrementar el contador
        SET i = i + 1;
    END WHILE;

END $$

DELIMITER ;

    
    delimiter ;
    drop procedure ventaAleatoria;
    call ventaAleatoria(200);
    use puntodeventa;
    select * from ventas;
    select * from detalle_ventas;
    select FLOOR(1 + (RAND()*9));
    -- vista del reporte de ventas por mes
	select sum(total) from ventas where id_empleado = 1;


    
    SELECT id_empleado  FROM empleados ORDER BY RAND() LIMIT 1;
    select max(id_venta) from ventas;
    select precio from productos where id_producto = 1;
    select * from ventas;
    insert into ventas values (null, now(), 1, 12.50, 1);
	SELECT id_empleado 
        
        FROM Empleados
        WHERE activo = 1  -- Solo seleccionamos empleados activos
        ORDER BY RAND()
        LIMIT 1;
 */       
        

-- -----------------------------------------------------------------------
/*delimiter //
create procedure insertar_ventas_aleatorias(in num_ventas int)
begin
    declare contador int default 1;
    declare contador_productos int;
    
    declare cliente_id int;
    declare empleado_id int;
    declare producto_id int;
    declare nueva_venta_id int;
    declare num_productos int;
    declare subtotal decimal(10, 2);
    declare total decimal(10, 2);
    declare cantidad_producto int;
    declare precio_producto decimal(10, 2);
    declare base_calculo decimal(10, 2);
    declare metodo_pago varchar(30);
    declare duplicado int;

    while contador <= num_ventas do
        set cliente_id = (select id from clientes order by rand() limit 1);
        set empleado_id = (select id from empleados order by rand() limit 1);
        set num_productos = floor(rand() * (15 - 1 + 1)) + 1;
        
        set contador_productos = 1;
        set nueva_venta_id = (select
                                case
                                when (select count(*) from ventas) = 0 then 1
                                else (select id from ventas order by id desc limit 1) + 1
                                end);
        set subtotal = 0;
        set total = 0;

        set foreign_key_checks = 0;
        
        while contador_productos <= num_productos do
            set producto_id = (select id from productos order by rand() limit 1);
            set duplicado = (select count(*) from detalles_venta where id_venta = nueva_venta_id and id_producto = producto_id);
            if duplicado = 0 then
                set cantidad_producto = floor(rand() * (10 - 1 + 1)) + 1;
                set precio_producto = (select precio from productos where id = producto_id);
                insert into detalles_venta(id_venta, id_producto, cantidad_producto, precio_unitario) values(nueva_venta_id, producto_id, cantidad_producto, precio_producto);
                set base_calculo = cantidad_producto * precio_producto;
                set subtotal = subtotal + base_calculo;
                set total = total + base_calculo + (base_calculo * (select iva from productos where id = producto_id) / 100);
                set contador_productos = contador_productos + 1;
            end if;
        end while;
        set foreign_key_checks = 1;

        insert into ventas(id_empleado, id_cliente, importe, subtotal, total, fecha, metodo_pago)
        values(empleado_id, cliente_id, subtotal, subtotal, total, (select date_add('2024-01-01', interval floor(rand() * 366) day)), metodo_pago);

        set contador = contador + 1;
    end while;
end //
delimiter ;*/


-- ----------------------------------------------------------------------------------------
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

drop procedure insertar_ventas_aleatorias;
call insertar_ventas_aleatorias(50);
select * from ventas;
select * from detalle_ventas;



-- ------------------------------------------------------------------
set @mes = 1;
set @anio =1;
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
-- WHERE 
    -- MONTH(v.fecha) = @mes
    -- AND YEAR(v.fecha) = @anio
GROUP BY 
    v.id_venta, v.fecha, c.nombre, e.nombre_completo, v.total
order by v.fecha asc; 
-- ------------------------------------------------------------------
drop view reporte_ventas_mensual;
select * from ventas;
SELECT * FROM reporte_ventas_mensual WHERE Fecha BETWEEN '2024-2-1' AND '2024-3-31' ;
describe reporte_ventas_mensual;

-- ------------------------------------------------------------------
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
drop view reporte_ventas_empleado;
select * from reporte_ventas_empleado limit 5;
select sum(total) from ventas;
use puntodeventa;
select * from detalle_ventas;

-- ----------------------------------------------------------
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
drop view reporte_ventas_trimestrales_productos;
select * from reporte_ventas_trimestrales;
select * from productos;
select * from detalle_ventas;
select id_producto, sum(cantidad) from detalle_ventas where id_producto = 1;



-- ------------------------------------------------------
CREATE TABLE auditoria_ventas (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT,
    accion VARCHAR(10), -- 'INSERT', 'UPDATE' o 'DELETE'
    usuario VARCHAR(100), -- Usuario que realizó la acción
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora del cambio
    datos_anteriores TEXT, -- Datos antes del cambio (solo para UPDATE y DELETE)
    datos_nuevos TEXT -- Datos después del cambio (solo para INSERT y UPDATE)
);


DELIMITER //
CREATE TRIGGER ventas_insert
AFTER INSERT ON ventas
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_ventas (id_venta, accion, usuario, datos_nuevos)
    VALUES (
        NEW.id_venta,
        'INSERT',
        USER(), -- Usuario que ejecutó el cambio
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
        USER(), -- Usuario que ejecutó el cambio
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

select * from auditoria_ventas;
select * from ventas;
update ventas set id_cliente = 1 where id_venta = 1;
-- ------------------------------------------------------
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
-- ----------------------------------------------------------------------------------------

insert into productos values(null,'1234','', 'esto es una prueba', 10.50,160,1,2.10,0);

select * from detalle_ventas;