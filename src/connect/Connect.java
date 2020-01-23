package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class Connect {
    
    public String driver;
    public String database;
    public String hostname;
    public String port;
    public String url;
    public String username;
    public String password;
    
    
    public Connect(String username, String password){
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.database = "imprexa2";//nombre de la base de datos
        this.hostname = "192.168.0.5";
        this.port = "3306";
        this.url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.username = username;
        this.password = password;
    }
    
    //metodo para crear la conexion
    public Connection conectar(){
        
        Connection con = null;
        
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            JOptionPane.showMessageDialog(null, "Conexion Exitosa", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        }catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, "No se conecto, contraseña o usuario incorrectos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return con;
    }
    
    
}
