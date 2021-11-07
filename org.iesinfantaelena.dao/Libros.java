package org.iesinfantaelena.dao;

import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;
import org.iesinfantaelena.utils.Utilidades;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class Libros {

    // Consultas a realizar en BD

    private final Connection con;
    private final Statement stmt;
    private final ResultSet rs;
    private final PreparedStatement pstmt;

    public Libros() throws AccesoDatosException {
        try {
            // Obtenemos la conexión
            this.con = new Utilidades().getConnection();
            this.stmt = null;
            this.rs = null;
            this.pstmt = null;
        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        }
    }

    public void cerrar() {

        if (con != null) {
            Utilidades.closeConnection(con);
        }

    }

    private void liberar() {
        try {
            // Liberamos todos los recursos pase lo que pase
            //Al cerrar un stmt se cierran los resultset asociados. Podíamos omitir el primer if. Lo dejamos por claridad.
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }

    public List<Libro> verCatalogo() throws AccesoDatosException {

        return null;

    }

    public void actualizarCopias(Libro libro) throws AccesoDatosException {

    }

    public void anadirLibro(Libro libro) throws AccesoDatosException {


    }

    public void borrar(Libro libro) throws AccesoDatosException {


    }

    public String[] getCamposLibro() throws AccesoDatosException {

        return null;
    }


    public void obtenerLibro(int ISBN) throws AccesoDatosException {

    }


}

