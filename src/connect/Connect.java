package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class Connect {
    
    public String driver = "com.mysql.cj.jdbc.Driver";
    public String database = "imprexa2";
    public String hostname = "192.168.0.5";
    public String port = "3306";
    public String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public String username = "";
    public String password = "";
    
    
    public Connect(String username, String password){
        
        this.username = username;
        this.password = password;
    }
    
    public Connection conectar(){
        
        Connection con = null;
        
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            JOptionPane.showMessageDialog(null, "Conexion Exitosa", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        }catch(ClassNotFoundException | SQLException e){
            //e.printStackTrace();
        
            JOptionPane.showMessageDialog(null, "No se conecto, contraseña o usuario incorrectos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return con;
    }
    
    
}
