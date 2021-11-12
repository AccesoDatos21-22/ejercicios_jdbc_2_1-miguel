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
    private void crearTablaLibros() {
        try {
            stmt = con.createStatement();
            String sql = "create table libros" +
                    "(isbn integer not null," +
                    "   titulo varchar(50) not null," +
                    "   autor varchar(50) not null," +
                    "   editorial varchar(25) not null," +
                    "   paginas integer not null," +
                    "   copias integer not null," +
                    "   constraint isbn_pk primary key (isbn))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Created table in given database...");
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

    public String[] getCamposLibro() throws AccesoDatosException {
        return new String[]{"isbn","titulo","autor","editorial","paginas","copias"};
    }


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


}

