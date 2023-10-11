//Fernando San José Domínguez
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
//necesitas tener los archivos del fichero entrada en el pc
//La propiedad que he comparado es el tamaño de los ficheros
public class buildhash{
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        String linea;
        String nombreFicheroLeido;
        String estadisticasFichero;
        String hashtext="";
        String texto="";
        FileReader ficheroEntrada = new FileReader(args[0]);
        BufferedReader bufferEntrada = new BufferedReader(ficheroEntrada);
        
        FileWriter ficheroSalida = new FileWriter (args[1]);
        PrintWriter printWriterSalida = new PrintWriter(ficheroSalida);
        byte[] messageDigest;
        BigInteger no;

    try{
        while((linea=bufferEntrada.readLine())!=null){//Leo el fichero entrada
            //hash de nombre
            nombreFicheroLeido = linea.replace('\n', ' ');//quito los saltos de linea que tenga el nombre
            MessageDigest md = MessageDigest.getInstance("MD5");//llamo a un objeto MD5

            FileReader archivo=new FileReader(nombreFicheroLeido);
            BufferedReader bufferArchivo = new BufferedReader(archivo);
            while((linea=bufferArchivo.readLine())!=null){//leo el contenido del fichero obtenido del fichero parametro
                texto = texto+linea;//almaceno el contenido del fichero
            }
                messageDigest = md.digest(texto.getBytes());//inserto los Bytes en un array
                no = new BigInteger(1, messageDigest);//creamos numeros grandes de mas de 64 bits
                hashtext = no.toString(16);//convierto a hexadecimal
            
                while (hashtext.length() < 32) {//hago los 32 bits del hascode
                    hashtext = "0" + hashtext;
                }
            texto="";
            
            //hash de propiedades
            Properties propiedadesFichero = new Properties();//creo objeto propiedades
            Long sizeArchivo = Files.size(Path.of(nombreFicheroLeido));
            propiedadesFichero.setProperty("sizeArchivo", Long.toString(sizeArchivo));//inserto una propiedad nueva
            estadisticasFichero=propiedadesFichero.getProperty("sizeArchivo");//obtengoo la propiedad

            messageDigest = md.digest(estadisticasFichero.getBytes());
            no = new BigInteger(1, messageDigest);
            String hashtextestadisticas = no.toString(16);
            
            while (hashtextestadisticas.length() < 32) {
                hashtextestadisticas = "0" + hashtextestadisticas;
            }
               
            printWriterSalida.println(nombreFicheroLeido+";"+hashtext+";"+hashtextestadisticas);
            bufferArchivo.close();
        }
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        
        
        ficheroSalida.close();
        bufferEntrada.close();
    }
}
