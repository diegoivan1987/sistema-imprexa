
package utilidades;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenSize {
    
    public Dimension tamano;
    public int width;
    public int height;
    
    public ScreenSize(){
        
        tamano = Toolkit.getDefaultToolkit().getScreenSize();
        width = tamano.width;
        height = tamano.height;
    }
    
}
