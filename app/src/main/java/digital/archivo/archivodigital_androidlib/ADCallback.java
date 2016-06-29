package digital.archivo.archivodigital_androidlib;

/**
 * @author https://archivo.digital
 */
public interface ADCallback<T> {

    public void ok(T obj);
    public void err(String msg);
}