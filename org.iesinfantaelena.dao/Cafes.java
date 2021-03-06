package org.iesinfantaelena.dao;

import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.utils.Utilidades;

import java.io.IOException;
import java.sql.*;

public class Cafes {

    private static Connection con;
    // Consultas a realizar en BD
    private static final String SELECT_CAFES_QUERY = "select CAF_NOMBRE, PROV_ID, PRECIO, VENTAS, TOTAL from CAFES";
    private static final String SEARCH_CAFE_QUERY = "select * from CAFES WHERE CAF_NOMBRE= ?";
    private static final String INSERT_CAFE_QUERY = "insert into CAFES values (?,?,?,?,?)";
    private static final String DELETE_CAFE_QUERY = "delete from CAFES WHERE CAF_NOMBRE = ?";
    private static final String SEARCH_CAFES_PROVEEDOR = "select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID";
    private static final String CREATE_TABLE_PROVEEDORES = "create table if not exists proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));";
    private static final String CREATE_TABLE_CAFES = "create table if not exists CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));";

    private Connection conection;
    private ResultSet rs;
    private PreparedStatement pstmt;
    private Statement stmt;

    public Cafes() {
        try {
            this.conection = new Utilidades().getConnection();
            this.rs = null;
            this.pstmt = null;
            this.stmt = null;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrar() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void liberar() {
        try {
            rs.close();
            pstmt.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*public Cafes() {

        Statement stmt = null;

        try {
            con = new Utilidades().getConnection();
            stmt = con.createStatement();

            stmt.executeUpdate(CREATE_TABLE_PROVEEDORES);

            stmt.executeUpdate(CREATE_TABLE_CAFES);

            stmt.executeUpdate("insert into proveedores values(49, 'PROVerior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');");
            stmt.executeUpdate("insert into proveedores values(101, 'Acme, Inc.', '99 mercado CALLE', 'Groundsville', 'CA', '95199');");
            stmt.executeUpdate("insert into proveedores values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');");

        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicaci??n real, escribo en el log y delego
            //System.err.println(e.getMessage());

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            //Utilidades.printSQLException(sqle);


        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }*/

    public void verTabla() throws AccesoDatosException {
        new Cafes();
        try {
            // Creaci??n de la sentencia
            stmt = con.createStatement();
            // Ejecuci??n de la consulta y obtenci??n de resultados en un ResultSet
            rs = stmt.executeQuery(SELECT_CAFES_QUERY);
            // Recuperaci??n de los datos del ResultSet
            while (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total);
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            liberar();
            cerrar();
        }
    }

    public void buscar(String nombre) throws AccesoDatosException {
        new Cafes();
        try {
            // Creaci??n de la sentencia
            pstmt = con.prepareStatement(SEARCH_CAFE_QUERY);
            pstmt.setString(1, nombre);
            // Ejecuci??n de la consulta y obtenci??n de resultados en un
            // ResultSet
            rs = pstmt.executeQuery();

            // Recuperaci??n de los datos del ResultSet
            if (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", " + PRECIO + ", " + VENTAS + ", " + total);
            }
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException("Ocurri?? un error al acceder a los datos");
        } finally {
            liberar();
            cerrar();
        }
    }

    public void insertar(String nombre, int provid, float precio, int ventas, int total) throws AccesoDatosException {
        new Cafes();
        try {
            pstmt = con.prepareStatement(INSERT_CAFE_QUERY);
            pstmt.setString(1, nombre);
            pstmt.setInt(2, provid);
            pstmt.setFloat(3, precio);
            pstmt.setInt(4, ventas);
            pstmt.setInt(5, total);
            // Ejecuci??n de la inserci??n
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException("Ocurri?? un error al acceder a los datos");
        } finally {
            liberar();
            cerrar();
        }
    }

    public void borrar(String nombre) throws AccesoDatosException {
        new Cafes();
        try {
            // Creaci??n de la sentencia
            pstmt = con.prepareStatement(DELETE_CAFE_QUERY);
            pstmt.setString(1, nombre);
            // Ejecuci??n del borrado
            pstmt.executeUpdate();
            System.out.println("caf?? " + nombre + " ha sido borrado.");

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException("Ocurri?? un error al acceder a los datos");
        } finally {
            liberar();
            cerrar();
        }
    }

    public void cafesPorProveedor(int provid) throws AccesoDatosException {
        new Cafes();
        try {
            // Creaci??n de la sentencia
            pstmt = con.prepareStatement(SEARCH_CAFES_PROVEEDOR);
            pstmt.setInt(1, provid);
            // Ejecuci??n de la consulta y obtenci??n de resultados en un
            // ResultSet
            rs = pstmt.executeQuery();
            // Recuperaci??n de los datos del ResultSet
            while (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                String provName = rs.getString("PROV_NOMBRE");
                String calle = rs.getString("CALLE");
                String ciudad = rs.getString("CIUDAD");
                String pais = rs.getString("PAIS");
                int cp = rs.getInt("CP");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total
                        + ",Y el proveedor es:" + provName + "," + calle + ","
                        + ciudad + "," + pais + "," + cp);
            }
        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException("Ocurri?? un error al acceder a los datos");
        } finally {
            liberar();
            cerrar();
        }

    }
}
