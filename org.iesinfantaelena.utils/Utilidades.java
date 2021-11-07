package org.iesinfantaelena.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description Clase que establece una conexión a BD utilizando la clase
 * DriverManager. lee los datos de u archivo de propiedades
 */

public class Utilidades {

    public String dbms;
    public String dbName;
    public String userName;
    public String password;
    public String urlString;

    private String driver;
    private String serverName;
    private int portNumber;
    private Properties prop;

    private static final String PROPERTIES_FILE = System.getProperty("user.dir") + "/resources/h2-properties.xml";

    public Utilidades() throws IOException {
        super();
        this.setProperties(PROPERTIES_FILE);
    }

    public Utilidades(String propertiesFileName)
            throws IOException {
        super();
        this.setProperties(propertiesFileName);
    }

    /**
     * Asignación de propiedades de conexión de xml a atributos de clase
     */
    private void setProperties(String fileName) throws IOException {
        this.prop = new Properties();
        prop.loadFromXML(Files.newInputStream(Paths.get(fileName)));

        this.dbms = this.prop.getProperty("dbms");
        this.driver = this.prop.getProperty("driver");
        this.dbName = this.prop.getProperty("database_name");
        this.userName = this.prop.getProperty("user_name");
        this.password = this.prop.getProperty("password");
        this.serverName = this.prop.getProperty("server_name");
        this.portNumber = Integer.parseInt(this.prop.getProperty("port_number"));
    /*
		System.out.println("Set the following properties:");
		System.out.println("dbms: " + dbms);
		System.out.println("driver: " + driver);
		System.out.println("dbName: " + dbName);
		System.out.println("userName: " + userName);
		System.out.println("serverName: " + serverName);
		System.out.println("portNumber: " + portNumber);
    */
    }

    /**
     * Conexion a Base de Datos
     */
    public Connection getConnection() throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        switch (this.dbms) {
            case "mariadb":
                /* Solicito a DriverManager una conexión con la base de datos */
                /*
                 * Para identificar el controldador a usar se le proporciona una URL, si no lo
                 * encuentra lanza SQLException
                 */
                /* formato de URL: jdbc:[host][:port]/[database] */
                /*
                 * La URL varia según el gestor de BD, jdbc:mysql://127.0.0.1:3306/libros,
                 * jdbc:oracle:thin:@192.168.239.142:1521:libros
                 */
                conn = DriverManager.getConnection(
                        "jdbc:" + this.dbms + "://" + this.serverName + ":" + this.portNumber + "/" + this.dbName,
                        connectionProps);
                break;
            case "derby":
                conn = DriverManager.getConnection("jdbc:" + this.dbms + ":" + this.dbName + ";create=true",
                        connectionProps);

                break;
            case "sqlite":
                conn = DriverManager
                        .getConnection("jdbc:" + this.dbms + ":" + System.getProperty("user.dir") + this.dbName);
                break;
            case "h2":
                conn = DriverManager
                        .getConnection("jdbc:" + this.dbms + ":" + this.dbName + "," + this.userName + ",");
                break;
        }
        System.out.println("Connectado a BD");
        return conn;
    }

    /**
     * Cierre de conexión a BD
     */
    public static void closeConnection(Connection connArg) {
        System.out.println("Releasing all open resources ...");
        try {
            if (connArg != null) {
                connArg.close();
            }
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /**
     * Metodo para imprimir la información de una Excepción SQL y poder depurar errores fácilmente
     */
    public static void printSQLException(SQLException e) {

        while (e != null) {
            //Estado ANSI
            e.printStackTrace(System.err);
            System.err.println("SQLState: "
                    + e.getSQLState());
            //Código de error propio de cada gestor de BD
            System.err.println("Error Code: "
                    + e.getErrorCode());
            //Mensaje textual
            System.err.println("Message: " + e.getMessage());

            //Objetos desencadenantes de la excepción
            Throwable t = e.getCause();
            while (t != null) {
                System.out.println("Cause: " + t);
                t = t.getCause();
            }
            //Cualquier otra excepción encadenada
            e = e.getNextException();

        }
    }

}
