import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

public class Main {

    public static void main(String[] args) {


        try {
            /*Cafes cafes = new Cafes();
            cafes.insertar("Cafetito", 150, 1.0f, 100, 1000);
            cafes.insertar("Cafe tacilla", 150, 2.0f, 100, 1000);
            cafes.verTabla();
            cafes.buscar("tacilla");
            cafes.cafesPorProveedor(150);
            cafes.borrar("Cafe tacilla");
            cafes.verTabla();*/
            Libros libros = new Libros();
            Libro libro = new Libro(12345,"Sistemas Operativos","Tanembaun","Informática",156,3);
            Libro libro1 = new Libro(12453,"Minix","Stallings","Informática",345,4);
            Libro libro2 = new Libro(1325,"Linux","Richard Stallman","FSF",168,10);
            Libro libro3 = new Libro(1725,"Java","Juan Garcia","Programación",245,9);
            libros.anadirLibro(libro);
            libros.anadirLibro(libro1);
            libros.anadirLibro(libro2);
            libros.anadirLibro(libro3);

        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }

}