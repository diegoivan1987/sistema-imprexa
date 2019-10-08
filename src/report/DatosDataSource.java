
package report;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DatosDataSource implements JRDataSource{
    
    private List<PedidoDatos> listaDatos = new ArrayList<PedidoDatos>();
    int indiceDatosActual = -1;

    @Override
    public boolean next() throws JRException {
        return ++indiceDatosActual < listaDatos.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        
         Object valor = null;  
         
         if("folio".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getFolio();
         }else if("fechaIni".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getFecha();
         }else if("imp".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getImpresion();
         }else if("autorizo".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getAutorizo();
         }else if("dirEn".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getDirEnt();
         }else if("tel".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getTel();
         }else if("cel".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getCel();
         }else if("ciudad".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getCiudad();
         }else if("agente".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getAgente();
         }else if("piezas1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getPiezas();
         }else if("matP1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getMaterial();
         }else if("med1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getMedida();
         }else if("calP1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getCalibre();
         }else if("sello1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getSello();
         }else if("tipo1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getTipo();
         }else if("pig".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getPigmento();
         }else if("punit1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getpUni();
         }else if("import1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getImporte();
         }else if("grabados".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getGrabados();
         }else if("sub".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getSub();
         }else if("iva".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getIva();
         }else if("total".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getTotal();
         }else if("anti".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getAnticipo();
         }else if("c1t1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC1t1();
         }else if("c1t2".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC1t2();
         }else if("c1t3".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC1t3();
         }else if("c1t4".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC1t4();
         }else if("c1t5".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC1t5();
         }else if("c1t6".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC1t6();
         }else if("c2t1".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC2t1();
         }else if("c2t2".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC2t2();
         }else if("c2t3".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC2t3();
         }else if("c2t4".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC2t4();
         }else if("c2t5".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC2t5();
         }else if("c2t6".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getC2t6();
         }else if("descuento".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getDescuento();
         }else if("resto".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getResto();
         }else if("manera".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getManera();
         }else if("pzFinales".equals(jrf.getName())){
             valor = listaDatos.get(indiceDatosActual).getPzFinales();
         }
 
        return valor;
    }
    
    public void addParticipante(PedidoDatos datosReporte)
    {
        this.listaDatos.add(datosReporte);
    }
    
    
}
