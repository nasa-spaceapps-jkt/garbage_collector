package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Seluruh isi source code ini bukan untuk dicopy tanpa izin pemilik
 * @author Bamburuncing
 */
public class KoneksiDatabase {
    
    private static Connection koneksi;
    
    public static Connection getKoneksi(){
        if(koneksi == null){
            try{
                
                String url = "jdbc:mysql://localhost:3306/gcdb";
                String user = "root";
                String password = "";
                
                
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                
                koneksi = DriverManager.getConnection(url, user, password); 
                
            } 
            catch(SQLException t){
                System.out.println("Error membuat koneksi");
            } 
        }
        return koneksi;
    }
    
}
