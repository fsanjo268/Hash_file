//Fernando San José Domínguez
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class checkhash{
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        String linea;
        String nombreFicheroLeido;
        String estadisticasFichero;
        String hashtext="";
        String texto="";
        FileReader ficheroEntrada = new FileReader(args[0]);
        BufferedReader bufferEntrada = new BufferedReader(ficheroEntrada);
        MessageDigest md;
        byte[] messageDigest;
        BigInteger no;

    try{
        while((linea=bufferEntrada.readLine())!=null){//Leo el fichero entrada
            //hash de nombre
            nombreFicheroLeido = linea.replace('\n', ' ');//quito los saltos de linea que tenga el nombre
            String[]separacion = nombreFicheroLeido.split(";");
            md = MessageDigest.getInstance("MD5");//llamo a un objeto MD5

            FileReader archivo=new FileReader(separacion[0]);
            BufferedReader bufferArchivo = new BufferedReader(archivo);
            while((linea=bufferArchivo.readLine())!=null){//leo cada linea del archivo
                texto = texto+linea;
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
            Long sizeArchivo = Files.size(Path.of(separacion[0]));
            propiedadesFichero.setProperty("sizeArchivo", Long.toString(sizeArchivo));//inserto una propiedad nueva
            estadisticasFichero=propiedadesFichero.getProperty("sizeArchivo");//obtengoo la propiedad

            messageDigest = md.digest(estadisticasFichero.getBytes());
            no = new BigInteger(1, messageDigest);
            String hashtextestadisticas = no.toString(16);
            
            while (hashtextestadisticas.length() < 32) {
                hashtextestadisticas = "0" + hashtextestadisticas;
            }
           

            if(hashtext.equals(separacion[1])){
                System.out.println("El contenido de "+separacion[0]+" no ha sufrido una modificacion");
            }else{
                    System.out.println("El contenido de "+separacion[0]+" si ha sufrido una modificacion");
                }
            
            if(hashtextestadisticas.equals(separacion[2])){
                System.out.println("las propiedades de "+separacion[0]+" no ha sufrido una modificacion");
            }else{
                    System.out.println("las propiedades de "+separacion[0]+" si han sufrido una modificacion");
                }
            
                bufferArchivo.close(); 
        }
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        bufferEntrada.close();
    }
}
