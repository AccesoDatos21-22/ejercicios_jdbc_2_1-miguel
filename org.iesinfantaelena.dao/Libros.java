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
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pstmt;

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
        List<Libro> libros=null;
        Libro libro = new Libro();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM libros");
            while(rs.next()){
                libro.setISBN(rs.getInt("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setEditorial(rs.getString("editorial"));
                libro.setPaginas(rs.getInt("paginas"));
                libro.setCopias(rs.getInt("copias"));
                libros.add(libro);
                libro=new Libro();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            liberar();
            cerrar();
        }
        return libros;

    }

    public void actualizarCopias(Libro libro) throws AccesoDatosException {
        try {
            stmt = con.createStatement();
            String sql = "UPDATE libros SET copias = "+libro.getCopias()+" WHERE isbn = "+libro.getISBN();
            stmt.executeUpdate(sql);
            System.out.println("Libro actualizado");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            liberar();
            cerrar();
        }
    }

    public void anadirLibro(Libro libro) throws AccesoDatosException {
        try {
            stmt = con.createStatement();
            String sql = "insert into libros VALUES("
                    + libro.getISBN() + ","
                    + libro.getTitulo() + ","
                    + libro.getAutor() + ","
                    + libro.getEditorial() + ","
                    + libro.getPaginas() + ","
                    + libro.getCopias() + ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            liberar();
            cerrar();
        }

    }

    public void borrar(Libro libro) throws AccesoDatosException {
        try {
            stmt = con.createStatement();
            String sql = "DELETE FROM libros WHERE isbn = "+libro.getISBN();
            stmt.executeUpdate(sql);
            System.out.println("Libro eliminado");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            liberar();
            cerrar();
        }
    }

    /*public String[] getCamposLibro() throws AccesoDatosException {
        return new String[]{"isbn","titulo","autor","editorial","paginas","copias"};
    }*/


    public void obtenerLibro(int ISBN) throws AccesoDatosException {
        Libro libro = new Libro();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM libros WHERE isbn = "+ISBN);
            while(rs.next()){
                libro.setISBN(rs.getInt("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setEditorial(rs.getString("editorial"));
                libro.setPaginas(rs.getInt("paginas"));
                libro.setCopias(rs.getInt("copias"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            liberar();
            cerrar();
        }
    }

    private static final String SELECT_CAMPOS_QUERY = "SELECT * FROM LIBROS LIMIT 1";
    public String[] getCamposLibro() throws AccesoDatosException {

        /*Sentencia sql con parámetros de entrada*/
        pstmt = null;
        /*Conjunto de Resultados a obtener de la sentencia sql*/
        rs= null;
        ResultSetMetaData rsmd = null;
        String[] campos = null;
        try {
            //Solicitamos a la conexion un objeto stmt para nuestra consulta
            pstmt = con.prepareStatement(SELECT_CAMPOS_QUERY);

            //Le solicitamos al objeto stmt que ejecute nuestra consulta
            //y nos devuelve los resultados en un objeto ResultSet
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            campos = new String[columns];
            for (int i = 0; i < columns; i++) {
                //Los indices de las columnas comienzan en 1
                campos[i] = rsmd.getColumnLabel(i + 1);
            }
            return campos;
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally{
            liberar();
        }
    }
}

