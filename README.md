***
**¿Dónde llamas a cerrar y liberar? ¿Por qué?**\
LLamo al constructor al principio de cada función para inicializar las variables y en el finally llamo a la funcion liberar para cerrarlos.
***
**¿Qué hace este método?**\
Devuelve los campos de la tabla libros. 
Para esto ejecutas un select de un libro, ahora con el ResultSetMetaData consigues el valor designado de cada columna y cuantas columnas hay en total. Después creas un array para los campos y mediante un for y el rsmd consigues los nombres de cada columna. Por último devuelves el array de campos.