
package pantallas;

import datechooser.beans.DateChooserCombo;
import datechooser.events.CommitEvent;
import datechooser.events.CommitListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import lu.tudor.santec.jtimechooser.JTimeChooser;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Procesos extends javax.swing.JFrame {//Permite llevar un control de los registros de produccion
    Connection con;
    ResultSet rs;
    Statement st;
    
    Pedido pd;
    
    //medidas de la ventana
    private int WD;
    private int HG;
    
    DateFormat df;
    
    String[] modoMaterial; 
    
    //Variables de extrusion
    String pM1St, pM2St, provE1St, precioKgE1St, prov2St, precioKg2St;
    
    //Variables de Impreso
    String prodISt, provI1St, precioKgI1St, prodI2St, provI2St, precioKgI2St, stickySt,
           cDiseSt, cGrabSt, stcSt;
    
    //Variables de Bolseo
    String  prodBSt, prodPzSt, provB1St, precioKgB1St;
    
    
    //Variables de OperadorExtrusion
    String kgESt, greniaESt, opESt, nMESt, hIniESt, fIniESt, hFinESt, fFinESt, 
            tMuESt, totHESt, exESt, costoOpExSt;
    
    //Variables de OperadorImpresion
    String kgISt, greniaISt, opISt, ayudanteSt,nMISt, hIniISt, fIniISt, hFinISt, 
            fFinISt,tMuISt, totHISt, exISt, costoOpImSt;
    
    //Variables de OperadorBolseo
    String kgBSt, greniaBSt, suajeBSt, opBSt, nMBSt, hIniBSt, fIniBSt, hFinBSt, 
            fFinBSt, tMuBSt, totHBSt, exBSt, costoOpBoSt;
    
    //Vaiables que almacenan claves principales
    int folio;
    int idPart;
    int idEx;
    int idBo;
    int idIm;
    
    DefaultTableModel modPed, modPart;
    JTableHeader thPed, thPart;
    Font fuenteTablas;
    
    ArrayList<String> datosImpreso;
    
    //costos por hora que cambiaran deacuerdo al sueldo del operador elegido
    Float sueldoPorHoraE;
    Float sueldoPorHoraI;
    Float sueldoPorHoraB;
    Float sueldoPorHoraAyudanteI;
    
    public Procesos(Connection con) {
        initComponents();
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.setResizable(false);
        this.con = con;
        
        //Tamaño de la pantalla
        WD = this.getSize().width;
        HG = this.getSize().height;
        this.setSize(new Dimension(WD/2, HG));//se establece el tamaño de la pantalla a la mitad

        df = new SimpleDateFormat("yyyy-MM-dd");

        this.modoMaterial = new String[]{"Produccion", "Compra", "Ambos"};//arreglo con las maneras de generar procesos

        //Vaiables que almacenan claves principales
        this.folio = 0;
        this.idPart = 0;
        this.idEx = 0;
        this.idBo = 0;
        this.idIm = 0;
        
        //inicializo los costos por hora
        this.sueldoPorHoraE = 0f;
        this.sueldoPorHoraI = 0f;
        this.sueldoPorHoraB = 0f;
        this.sueldoPorHoraAyudanteI = 0f;
    
        cambioMod.setVisible(false);//se hace invisible el boton para cambiar el modo de generacion de los procesos
        
        fuenteTablas = new Font("Dialog", Font.BOLD, 12);
        
        //Se desactivan los botones de guardar procesos y de agregar
        generarPro.setEnabled(false);
        //maquila
        agE.setEnabled(false);
        agI.setEnabled(false);
        agB.setEnabled(false);
        //produccion
        gdEx.setEnabled(false);
        gdIm.setEnabled(false);
        gdBo.setEnabled(false);
        
        eliminarP.setEnabled(false);
        
        //Tabla que muestra pedidos
        modPed = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modPed.setColumnIdentifiers(new Object[]{"Folio", "Impresion", "Cliente", "Fecha Ingreso"});//titulos de la tabla pedido
        tablaPed.setModel(modPed);
        
        //Tabla que muestra partidas
        modPart = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modPart.setColumnIdentifiers(new Object[]{"Id", "Pz", "Medida", "Material 1","Material 2","Pigmento","Tipo","Precio Unitario"});//titulos tabla partidas
        tablaPart.setModel(modPart);
        
        //Se estalece un formato a campos de fechas
        fIniExt.setDateFormat(df);
        fFinExt.setDateFormat(df);
        
        fIniImp.setDateFormat(df);
        fFinImp.setDateFormat(df);
        
        fIniBol.setDateFormat(df);
        fFinBol.setDateFormat(df);
        
        //Se desactivan los textfield del folio y la id de partida
        foVis.setEnabled(false);
        idPartida.setEnabled(false);
        //tambien los que muestran los costos de operacion
        costoOpExt.setEditable(false);
        costoOpImp.setEditable(false);
        costoOpBol.setEditable(false);
       
        paPro.setBackground(Color.LIGHT_GRAY);
        paPro.setVisible(false);//se hace invisible el panel que contiene los campos de capturas
        
        //se hacen invisibles los paneles de maquila
        panMaqExt.setVisible(false);
        panMaqImp.setVisible(false);
        panMaqBol.setVisible(false);   
        
        //se inhabilitan los timefield de horas totales
        totalHrExt.getTimeField().setEditable(false);
        totalHrImp.getTimeField().setEditable(false);
        totalHrBol.getTimeField().setEditable(false);
        
        estilosTablas();//se les da estilo a las tablas
        
        onChangeTextField();//Creacion del listener para el campo de folio
        listenersProcesos();//agrupa los listeners de los procesos
    }
    
    //le da color a las tablas
    private void estilosTablas(){
        thPed = tablaPed.getTableHeader();
        thPed.setFont(fuenteTablas);
        thPed.setBackground(Color.black);
        thPed.setForeground(Color.white);
        
        thPart = tablaPart.getTableHeader();
        thPart.setFont(fuenteTablas);
        thPart.setBackground(Color.black);
        thPart.setForeground(Color.white);
    }
    
    //si cambia el folio, se muestran las partidas de ese pedido en la tabla
    private void onChangeTextField(){
        foVis.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                setTablePartidas();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {   
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    //Rellenar la tabla de partidas respecto al folio, Se acciona cuando el campo del folio se actualiza
    private void setTablePartidas(){
        
        modPart.setRowCount(0);
        
        folio = Integer.parseInt(foVis.getText());//Se obtiene el folio
        
        String sql = "select idPar, piezas, medida, mat1, mat2, pigmento, tipo, "
                + "precioUnitaro from partida where folio_fk = "+folio+" limit 0,30";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())//Se rellena la tabla con las partidas encontradas
            {
                modPart.addRow(new Object[]{rs.getString("idPar"), 
                    rs.getString("piezas"), rs.getString("medida"), 
                    rs.getString("mat1"), rs.getString("mat2"), 
                    rs.getString("pigmento"), rs.getString("tipo"), 
                    rs.getString("precioUnitaro")});
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla de partidas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //Listeners de los JTimeChoser y datechooser
    private void listenersProcesos(){
        hrIniEChange();
        hrFinEChange();
        tmMuertoEChange();
        fIniExtChange();
        fFinExtChange();
        extraEChange();
        
        hrIniIChange();
        hrFinIChange();
        tmMuertoIChange();
        fIniImpChange();
        fFinImpChange();
        extraIChange();
        
        hrIniBChange();
        hrFinBChange();
        tmMuertoBChange();
        fIniBolChange();
        fFinBolChange();
        extraBChange();
    }
    
    //listener de extrusion
    private void hrIniEChange(){
        hrIni.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, sueldoPorHoraE, costoOpExt);
                
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    private void hrFinEChange(){
        hrFin.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, sueldoPorHoraE, costoOpExt);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    private void tmMuertoEChange(){
        hrMuertoExt.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, sueldoPorHoraE, costoOpExt);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        }); 
    }
    
    private void extraEChange(){
        extHrExt.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, sueldoPorHoraE, costoOpExt); 
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        }); 
    }
    
    private void fIniExtChange(){
        
        fIniExt.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, sueldoPorHoraE, costoOpExt);
            }
        });
    }
    
    private void fFinExtChange(){
        fFinExt.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, sueldoPorHoraE, costoOpExt);
            }
        });
    }
    
    //listeners de impreso
    private void hrIniIChange(){
        hrIniImp.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                int maquina = obtieneMaquina();
                if(maquina == 3)/*si usan la maquina 3, utiliza la funcion exclusiva de impresion*/
                {
                    calcularTotalhrImp(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, sueldoPorHoraAyudanteI, costoOpImp);
                }
                else/*si no,usa la misma funcion que los otros procesos*/
                {
                    calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, costoOpImp);
                }
                
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    private void hrFinIChange(){
        hrFinImp.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                int maquina = obtieneMaquina();
                if(maquina == 3)/*si usan la maquina 3, utiliza la funcion exclusiva de impresion*/
                {
                    calcularTotalhrImp(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, sueldoPorHoraAyudanteI, costoOpImp);
                }
                else/*si no,usa la misma funcion que los otros procesos*/
                {
                    calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, costoOpImp);
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    private void tmMuertoIChange(){
        hrMuertoImp.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                int maquina = obtieneMaquina();
                if(maquina == 3)/*si usan la maquina 3, utiliza la funcion exclusiva de impresion*/
                {
                    calcularTotalhrImp(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, sueldoPorHoraAyudanteI, costoOpImp);
                }
                else/*si no,usa la misma funcion que los otros procesos*/
                {
                    calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, costoOpImp);
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        }); 
    }
    
    private void extraIChange(){
        extHrImp.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                int maquina = obtieneMaquina();
                if(maquina == 3)/*si usan la maquina 3, utiliza la funcion exclusiva de impresion*/
                {
                    calcularTotalhrImp(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, sueldoPorHoraAyudanteI, costoOpImp);
                }
                else/*si no,usa la misma funcion que los otros procesos*/
                {
                    calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, costoOpImp);
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        }); 
    }
    
    private void fIniImpChange(){
        fIniImp.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                int maquina = obtieneMaquina();
                if(maquina == 3)/*si usan la maquina 3, utiliza la funcion exclusiva de impresion*/
                {
                    calcularTotalhrImp(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, sueldoPorHoraAyudanteI, costoOpImp);
                }
                else/*si no,usa la misma funcion que los otros procesos*/
                {
                    calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, costoOpImp);
                }
            }
        });
    }
    
    private void fFinImpChange(){
        fFinImp.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                int maquina = obtieneMaquina();
                if(maquina == 3)/*si usan la maquina 3, utiliza la funcion exclusiva de impresion*/
                {
                    calcularTotalhrImp(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, sueldoPorHoraAyudanteI, costoOpImp);
                }
                else/*si no,usa la misma funcion que los otros procesos*/
                {
                    calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, sueldoPorHoraI, costoOpImp);
                }
            }
        });
    }
    
    //listeners de bolseo
    private void hrIniBChange(){
        hrIniBol.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, sueldoPorHoraB, costoOpBol);
                
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    private void hrFinBChange(){
        hrFinBol.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, sueldoPorHoraB, costoOpBol);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    private void tmMuertoBChange(){
        hrMuertoBol.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, sueldoPorHoraB, costoOpBol);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        }); 
    }
    
    private void extraBChange(){
        extBol.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, sueldoPorHoraB, costoOpBol);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        }); 
    }
     
    private void fIniBolChange(){
         fIniBol.addCommitListener(new CommitListener() {

             @Override
             public void onCommit(CommitEvent ce) {
                 calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, sueldoPorHoraB, costoOpBol);
             }
         });
     }
     
    private void fFinBolChange(){
         fFinBol.addCommitListener(new CommitListener() {

             @Override
             public void onCommit(CommitEvent ce) {
                 calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, sueldoPorHoraB, costoOpBol);
             }
         });
     }
    
    //calcula y establece la hora total de los procesos
    private void calcularTotalhr(JTimeChooser tIni, JTimeChooser tFin, JTimeChooser total, JTimeChooser muerto, DateChooserCombo fini, DateChooserCombo fFin,
            JTimeChooser extra, float sueldoOperador, JTextField costo){
        DateTimeFormatter dtf;
        DateTime dtIni;
        DateTime dtFin;
        DateTime dtMuerto;
        DateTime aux;
        DateTime extTime;
        Period diff; 
        
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        dtIni= new DateTime(fini.getText() + "T" + tIni.getTimeField().getText());
        dtFin = new DateTime(fFin.getText() + "T" + tFin.getTimeField().getText());
        dtMuerto = new DateTime("T" + muerto.getTimeField().getText());   
        extTime = new DateTime("T" + extra.getTimeField().getText());
        diff = new Period(dtIni, dtFin);
        
        int minNoAcum = diff.getMinutes();
        String horasSt = "00:00:00";
        int minOfHour = 0;
        int hrOfDay = 0;
        int tiempoExtra = 0;
        int minutosMenosExtra = 0;
        
        int horas = Hours.hoursBetween(dtIni, dtFin).getHours();
        int minutos = Minutes.minutesBetween(dtIni, dtFin).getMinutes();
        
        horas = horas - dtMuerto.getHourOfDay();
        minutos = minutos - dtMuerto.getMinuteOfDay();
        
        
        if(horas < 0){
            horas = 0;
        } 
        
        if(minutos < 0){
            minutos = 0;
        }
        if(minNoAcum < 0){
            minNoAcum = 0;
        }
        
        if(horas > 9 && minNoAcum > 9)
        {
            horasSt = (horas + ":" + minNoAcum + ":00");
        }
        else if(horas < 10 && minNoAcum < 10)
        {
            horasSt = ("0" + horas + ":" + "0" + minNoAcum + ":00");
        }
        else if(horas > 9 && minNoAcum < 10)
        {
            horasSt = (horas + ":" + "0" + minNoAcum + ":00");
        }
        else if(horas < 10 && minNoAcum > 9)
        {
            horasSt = ("0" + horas + ":" + minNoAcum + ":00");
        }
        
        String horasTotal = getHrDT(horasSt); 
        char auxHr = 0;
        
        if(Integer.parseInt(horasTotal) > 23)
        {
            try
            {
                auxHr = horasTotal.charAt(0);
                horasSt = horasSt.replace(horasSt.charAt(0), '1');

                aux = new DateTime("T"+horasSt);
                aux = aux.plusMinutes(-muerto.getMinutes());
                minOfHour = aux.getMinuteOfHour();
                hrOfDay = Integer.parseInt(horasTotal);

                horasSt = horasSt.replace(horasSt.charAt(0), auxHr);

                comprobarTiempos(minOfHour, hrOfDay, horasSt, aux, total);
            }
            catch(java.lang.IllegalArgumentException ex)
            {
                JOptionPane.showMessageDialog(null, "No se pueden sobrepasar las horas totales, maximo: 99 horas", "Advertencia", 
                        JOptionPane.INFORMATION_MESSAGE);
                ex.printStackTrace();
            }
        }
        else
        {
            
            try
            {
                aux = new DateTime("T"+horasSt);
                aux = aux.plusMinutes(-muerto.getMinutes());
                minOfHour = aux.getMinuteOfHour();
                hrOfDay = aux.getHourOfDay();

                comprobarTiempos(minOfHour, hrOfDay, horasSt, aux, total);
            }
            catch(java.lang.IllegalArgumentException ex)
            {
                JOptionPane.showMessageDialog(null, "No se pueden sobrepasar las horas totales, maximo: 99 horas", "Advertencia", 
                        JOptionPane.INFORMATION_MESSAGE);
                ex.printStackTrace();
            }
        }
        
        aux = new DateTime("T00:00:00");
        tiempoExtra = Minutes.minutesBetween(aux, extTime).getMinutes();
        minutosMenosExtra = minutos - tiempoExtra;
        
        if(minutosMenosExtra < 0){
            minutos = 0;
        }
        if(tiempoExtra < 0){
            tiempoExtra = 0;
        }
        
        calcularCostoOpMinutos(minutosMenosExtra, sueldoOperador, tiempoExtra, minutos, costo);   
    }
   
    //devuelve solo el numero de horas
    private String getHrDT(String tiempo){
        
        String hora = "";
        hora = hora + tiempo.charAt(0) + tiempo.charAt(1);
        return hora;
    }
    
    //establece las horas totales en el recuadros de horas
    private void comprobarTiempos(int minOfHour, int hrOfDay, String horasSt, DateTime aux, JTimeChooser total){

        if(minOfHour > 9 && hrOfDay > 9){
            horasSt = (hrOfDay + ":" + minOfHour + ":00");
        }else if(minOfHour < 10 && hrOfDay < 10){
            horasSt = ("0" + hrOfDay + ":0" + minOfHour + ":00");
        }else if(minOfHour < 10 && hrOfDay > 9){
            horasSt = (hrOfDay + ":0" + minOfHour + ":00");
        }else if(minOfHour > 9 && hrOfDay < 10){
            horasSt = ("0" + hrOfDay + ":" + minOfHour + ":00");
        }
        
        total.getTimeField().setText(horasSt);
    }
    
    //calcula el costo de operacion por registro de operador
    private void calcularCostoOpMinutos(int minutos, float sueldoOperador, int minutosExtra, int minutosTotales, JTextField costoField){
        
        float costoPorMinutos = 0f;
        float costoTotal = 0f;
        
        if(minutosExtra > minutosTotales)
        {
            costoTotal = 0;
        }else
        {
            costoPorMinutos = sueldoOperador / 60;
            costoTotal = (costoPorMinutos * minutos) + ((costoPorMinutos * minutosExtra) * 2);
        }
        
        costoField.setText(String.valueOf(costoTotal));
    }
    
    //calcula el total de horas de impreso
    private void calcularTotalhrImp(JTimeChooser tIni, JTimeChooser tFin, JTimeChooser total, JTimeChooser muerto, DateChooserCombo fini, DateChooserCombo fFin,
            JTimeChooser extra, float sueldoOperador, float sueldoAyudante,JTextField costo){
        DateTimeFormatter dtf;
        DateTime dtIni;
        DateTime dtFin;
        DateTime dtMuerto;
        DateTime aux;
        DateTime extTime;
        Period diff;
        
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        dtIni= new DateTime(fini.getText() + "T" + tIni.getTimeField().getText());
        dtFin = new DateTime(fFin.getText() + "T" + tFin.getTimeField().getText());
        dtMuerto = new DateTime("T" + muerto.getTimeField().getText());   
        extTime = new DateTime("T" + extra.getTimeField().getText());
        diff = new Period(dtIni, dtFin);
        
        int minNoAcum = diff.getMinutes();
        String horasSt = "00:00:00";
        int minOfHour = 0;
        int hrOfDay = 0;
        int tiempoExtra = 0;
        int minutosMenosExtra = 0;
        
        int horas = Hours.hoursBetween(dtIni, dtFin).getHours();
        int minutos = Minutes.minutesBetween(dtIni, dtFin).getMinutes();
        
        horas = horas - dtMuerto.getHourOfDay();
        minutos = minutos - dtMuerto.getMinuteOfDay();
        
        
        if(horas < 0){
            horas = 0;
        } 
        
        if(minutos < 0){
            minutos = 0;
        }
        if(minNoAcum < 0){
            minNoAcum = 0;
        }
        
        if(horas > 9 && minNoAcum > 9){
            horasSt = (horas + ":" + minNoAcum + ":00");
        }else if(horas < 10 && minNoAcum < 10){
            horasSt = ("0" + horas + ":" + "0" + minNoAcum + ":00");
        }else if(horas > 9 && minNoAcum < 10){
            horasSt = (horas + ":" + "0" + minNoAcum + ":00");
        }else if(horas < 10 && minNoAcum > 9){
            horasSt = ("0" + horas + ":" + minNoAcum + ":00");
        }
        
        String horasTotal = getHrDT(horasSt); 
        char auxHr = 0;
        
        if(Integer.parseInt(horasTotal) > 23){
            
            try{
                auxHr = horasTotal.charAt(0);
                horasSt = horasSt.replace(horasSt.charAt(0), '1');

                aux = new DateTime("T"+horasSt);
                aux = aux.plusMinutes(-muerto.getMinutes());
                minOfHour = aux.getMinuteOfHour();
                hrOfDay = Integer.parseInt(horasTotal);

                horasSt = horasSt.replace(horasSt.charAt(0), auxHr);

                comprobarTiempos(minOfHour, hrOfDay, horasSt, aux, total);
            }catch(java.lang.IllegalArgumentException ex){
                JOptionPane.showMessageDialog(null, "No se pueden sobrepasar las horas totales, maximo: 99 horas", "Advertencia", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
            
        }else{
            
            try{
                aux = new DateTime("T"+horasSt);
                aux = aux.plusMinutes(-muerto.getMinutes());
                minOfHour = aux.getMinuteOfHour();
                hrOfDay = aux.getHourOfDay();

                //System.out.println(horasSt);
                comprobarTiempos(minOfHour, hrOfDay, horasSt, aux, total);
            }catch(java.lang.IllegalArgumentException ex){
                JOptionPane.showMessageDialog(null, "No se pueden sobrepasar las horas totales, maximo: 99 horas", "Advertencia", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
    
        }
        
        aux = new DateTime("T00:00:00");
        tiempoExtra = Minutes.minutesBetween(aux, extTime).getMinutes();
        minutosMenosExtra = minutos - tiempoExtra;
        
        if(minutosMenosExtra < 0){
            minutos = 0;
        }
        if(tiempoExtra < 0){
            tiempoExtra = 0;
        }
        
        calcularCostoOpMinutosImp(minutosMenosExtra, sueldoOperador, sueldoAyudante, tiempoExtra, minutos, costo);
        
    }
    
    //calcula el costo total por cada registro de operador en impresion
    private void calcularCostoOpMinutosImp(int minutos, float sueldoOperador, float sueldoAyudante,int minutosExtra, int minutosTotales, JTextField costoField){
        
        float costoPorMinutos = 0f;
        float costoTotal = 0f;
        
        if(minutosExtra > minutosTotales){
            costoTotal = 0;
        }else{
            //los parseamos porque si no marca error y les sumamos 8.33 porque eso es igual a los 400 que se le suman entre las 48 horas
            sueldoOperador = (float) (8.33+sueldoOperador);
            sueldoAyudante = (float) (8.33+sueldoAyudante);
            costoPorMinutos = (sueldoOperador+sueldoAyudante) / 60;
            
            costoTotal = (costoPorMinutos * minutos) + ((costoPorMinutos * minutosExtra) * 2);
        }
        
        costoField.setText(String.valueOf(costoTotal));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paPro = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        panEx = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        greExt = new javax.swing.JTextField();
        maqExt = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        hrIni = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel124 = new javax.swing.JLabel();
        fIniExt = new datechooser.beans.DateChooserCombo();
        jLabel127 = new javax.swing.JLabel();
        hrFin = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel128 = new javax.swing.JLabel();
        fFinExt = new datechooser.beans.DateChooserCombo();
        jLabel130 = new javax.swing.JLabel();
        totalHrExt = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel129 = new javax.swing.JLabel();
        hrMuertoExt = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel131 = new javax.swing.JLabel();
        extHrExt = new lu.tudor.santec.jtimechooser.JTimeChooser();
        kgOpExt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        costoOpExt = new javax.swing.JTextField();
        listaMat = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        agE = new javax.swing.JButton();
        listOperadorE = new javax.swing.JComboBox();
        panMaqExt = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        prov1Ext = new javax.swing.JTextField();
        porKg1Ext = new javax.swing.JTextField();
        proM1 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        prov2Ext = new javax.swing.JTextField();
        porKg2Ext = new javax.swing.JTextField();
        proM2 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        gdEx = new javax.swing.JToggleButton();
        jPanel10 = new javax.swing.JPanel();
        panImp = new javax.swing.JPanel();
        jLabel134 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        greImp = new javax.swing.JTextField();
        jLabel135 = new javax.swing.JLabel();
        maqImp = new javax.swing.JTextField();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        hrIniImp = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel138 = new javax.swing.JLabel();
        fIniImp = new datechooser.beans.DateChooserCombo();
        jLabel139 = new javax.swing.JLabel();
        hrFinImp = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel140 = new javax.swing.JLabel();
        fFinImp = new datechooser.beans.DateChooserCombo();
        jLabel142 = new javax.swing.JLabel();
        totalHrImp = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel141 = new javax.swing.JLabel();
        hrMuertoImp = new lu.tudor.santec.jtimechooser.JTimeChooser();
        agI = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        kgOpIm = new javax.swing.JTextField();
        costoOpImp = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        extHrImp = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel143 = new javax.swing.JLabel();
        listOperadorI = new javax.swing.JComboBox();
        jLabel147 = new javax.swing.JLabel();
        listAyudanteI = new javax.swing.JComboBox();
        panMaqImp = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        provImp = new javax.swing.JTextField();
        jLabel146 = new javax.swing.JLabel();
        porKgImp = new javax.swing.JTextField();
        kgImp = new javax.swing.JTextField();
        jLabel132 = new javax.swing.JLabel();
        gdIm = new javax.swing.JToggleButton();
        jLabel148 = new javax.swing.JLabel();
        provImp2 = new javax.swing.JTextField();
        jLabel149 = new javax.swing.JLabel();
        porKgImp2 = new javax.swing.JTextField();
        jLabel150 = new javax.swing.JLabel();
        kgImp2 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        panBol = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        greBol = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        maqBol = new javax.swing.JTextField();
        suaje = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        hrIniBol = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel73 = new javax.swing.JLabel();
        fIniBol = new datechooser.beans.DateChooserCombo();
        jLabel74 = new javax.swing.JLabel();
        hrFinBol = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel75 = new javax.swing.JLabel();
        fFinBol = new datechooser.beans.DateChooserCombo();
        jLabel77 = new javax.swing.JLabel();
        totalHrBol = new lu.tudor.santec.jtimechooser.JTimeChooser();
        jLabel76 = new javax.swing.JLabel();
        hrMuertoBol = new lu.tudor.santec.jtimechooser.JTimeChooser();
        agB = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        kgOpBol = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        costoOpBol = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        extBol = new lu.tudor.santec.jtimechooser.JTimeChooser();
        listOperadorB = new javax.swing.JComboBox();
        panMaqBol = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        provBol = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        porKgBol = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        kgBol = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        pzsBol = new javax.swing.JTextField();
        gdBo = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        regresar = new javax.swing.JToggleButton();
        busquedaImp = new javax.swing.JButton();
        impBus = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPed = new javax.swing.JTable();
        foVis = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPart = new javax.swing.JTable();
        labelxd = new javax.swing.JLabel();
        idPartida = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        generarPro = new javax.swing.JToggleButton();
        eliminarP = new javax.swing.JButton();
        cambioMod = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Procesos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        paPro.setBackground(new java.awt.Color(51, 51, 51));
        paPro.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        panEx.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel36.setForeground(new java.awt.Color(0, 102, 153));
        jLabel36.setText("Operador:");

        jLabel35.setForeground(new java.awt.Color(0, 102, 153));
        jLabel35.setText("Greña:");

        greExt.setForeground(new java.awt.Color(0, 153, 153));
        greExt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                greExtKeyTyped(evt);
            }
        });

        maqExt.setForeground(new java.awt.Color(0, 153, 153));
        maqExt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                maqExtKeyTyped(evt);
            }
        });

        jLabel37.setForeground(new java.awt.Color(0, 102, 153));
        jLabel37.setText("Máquina:");

        jLabel116.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel116.setForeground(new java.awt.Color(0, 102, 153));
        jLabel116.setText("Tiempo de trabajo");

        jLabel120.setForeground(new java.awt.Color(0, 102, 153));
        jLabel120.setText("Hora inicial:");

        hrIni.setForeground(new java.awt.Color(0, 153, 153));

        jLabel124.setForeground(new java.awt.Color(0, 102, 153));
        jLabel124.setText("Fecha inicial:");

        jLabel127.setForeground(new java.awt.Color(0, 102, 153));
        jLabel127.setText("Hora final:");

        hrFin.setForeground(new java.awt.Color(0, 153, 153));

        jLabel128.setForeground(new java.awt.Color(0, 102, 153));
        jLabel128.setText("Fecha final:");

        jLabel130.setForeground(new java.awt.Color(0, 102, 153));
        jLabel130.setText("Total de horas:");

        totalHrExt.setForeground(new java.awt.Color(0, 153, 153));

        jLabel129.setForeground(new java.awt.Color(0, 102, 153));
        jLabel129.setText("Tiempo muerto:");

        hrMuertoExt.setForeground(new java.awt.Color(0, 153, 153));

        jLabel131.setForeground(new java.awt.Color(0, 102, 153));
        jLabel131.setText("De las cuales son extras:");

        extHrExt.setForeground(new java.awt.Color(0, 153, 153));

        kgOpExt.setForeground(new java.awt.Color(0, 153, 153));
        kgOpExt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgOpExtKeyTyped(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(0, 102, 153));
        jLabel4.setText("Kilos:");

        jLabel8.setForeground(new java.awt.Color(0, 102, 153));
        jLabel8.setText("Costo de Operación: $");

        costoOpExt.setForeground(new java.awt.Color(0, 153, 153));

        listaMat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALTA", "BAJA" }));

        jLabel11.setForeground(new java.awt.Color(0, 102, 153));
        jLabel11.setText("Tipo de material:");

        agE.setBackground(new java.awt.Color(51, 51, 51));
        agE.setForeground(new java.awt.Color(255, 255, 255));
        agE.setText("Agregar Registro a Extrusión");
        agE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agEActionPerformed(evt);
            }
        });

        listOperadorE.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listOperadorEItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panExLayout = new javax.swing.GroupLayout(panEx);
        panEx.setLayout(panExLayout);
        panExLayout.setHorizontalGroup(
            panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panExLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panExLayout.createSequentialGroup()
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel4))
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panExLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(kgOpExt, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panExLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(listOperadorE, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panExLayout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(greExt, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel37)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(maqExt, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panExLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(listaMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(panExLayout.createSequentialGroup()
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalHrExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panExLayout.createSequentialGroup()
                                .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel129)
                                    .addComponent(jLabel127)
                                    .addComponent(jLabel120)
                                    .addComponent(jLabel130))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hrIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hrFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hrMuertoExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panExLayout.createSequentialGroup()
                                .addComponent(jLabel131)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(extHrExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panExLayout.createSequentialGroup()
                                    .addComponent(jLabel124)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(fIniExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panExLayout.createSequentialGroup()
                                    .addComponent(jLabel128)
                                    .addGap(18, 18, 18)
                                    .addComponent(fFinExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panExLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(costoOpExt, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26))
                    .addGroup(panExLayout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(jLabel116)
                        .addGap(184, 184, 184))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panExLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(agE, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panExLayout.setVerticalGroup(
            panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panExLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(maqExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(listOperadorE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel35)
                    .addComponent(greExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panExLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(kgOpExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(panExLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listaMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel116)
                .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panExLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel120)
                            .addComponent(hrIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel127)
                            .addComponent(hrFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel129)
                            .addComponent(hrMuertoExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel130)
                            .addComponent(totalHrExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panExLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fIniExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel124))
                        .addGap(6, 6, 6)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fFinExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel128))
                        .addGap(7, 7, 7)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel131)
                            .addComponent(extHrExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(costoOpExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(agE)
                .addContainerGap())
        );

        panMaqExt.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel47.setForeground(new java.awt.Color(0, 102, 153));
        jLabel47.setText("Proveedor(1):");

        jLabel50.setForeground(new java.awt.Color(0, 102, 153));
        jLabel50.setText("Proveedor(2):");

        jLabel48.setForeground(new java.awt.Color(0, 102, 153));
        jLabel48.setText("Precio por kg(1): $");

        jLabel51.setForeground(new java.awt.Color(0, 102, 153));
        jLabel51.setText("Precio por kg(2): $");

        prov1Ext.setForeground(new java.awt.Color(0, 153, 153));
        prov1Ext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                prov1ExtKeyTyped(evt);
            }
        });

        porKg1Ext.setForeground(new java.awt.Color(0, 153, 153));
        porKg1Ext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                porKg1ExtKeyTyped(evt);
            }
        });

        proM1.setForeground(new java.awt.Color(0, 153, 153));
        proM1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                proM1KeyTyped(evt);
            }
        });

        jLabel33.setForeground(new java.awt.Color(0, 102, 153));
        jLabel33.setText("(KG)Compra de material 1: ");

        prov2Ext.setForeground(new java.awt.Color(0, 153, 153));
        prov2Ext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                prov2ExtKeyTyped(evt);
            }
        });

        porKg2Ext.setForeground(new java.awt.Color(0, 153, 153));
        porKg2Ext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                porKg2ExtKeyTyped(evt);
            }
        });

        proM2.setForeground(new java.awt.Color(0, 153, 153));
        proM2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                proM2KeyTyped(evt);
            }
        });

        jLabel34.setForeground(new java.awt.Color(0, 102, 153));
        jLabel34.setText("(KG)Compra de material 2: ");

        gdEx.setBackground(new java.awt.Color(51, 51, 51));
        gdEx.setForeground(new java.awt.Color(255, 255, 255));
        gdEx.setText("Guardar Cambios de Extrusión");
        gdEx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gdExActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panMaqExtLayout = new javax.swing.GroupLayout(panMaqExt);
        panMaqExt.setLayout(panMaqExtLayout);
        panMaqExtLayout.setHorizontalGroup(
            panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqExtLayout.createSequentialGroup()
                .addGroup(panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMaqExtLayout.createSequentialGroup()
                        .addGroup(panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(prov2Ext, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panMaqExtLayout.createSequentialGroup()
                                        .addGap(234, 234, 234)
                                        .addComponent(jLabel47))
                                    .addGroup(panMaqExtLayout.createSequentialGroup()
                                        .addGap(101, 101, 101)
                                        .addComponent(prov1Ext, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panMaqExtLayout.createSequentialGroup()
                                .addGap(235, 235, 235)
                                .addComponent(jLabel50)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panMaqExtLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMaqExtLayout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(porKg1Ext, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(proM1))
                            .addGroup(panMaqExtLayout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(porKg2Ext, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(proM2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panMaqExtLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(gdEx)))
                .addContainerGap())
        );
        panMaqExtLayout.setVerticalGroup(
            panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqExtLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prov1Ext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(porKg1Ext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel48)
                    .addComponent(proM1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prov2Ext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panMaqExtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(porKg2Ext, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(proM2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(gdEx)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panMaqExt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panEx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panEx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panMaqExt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paPro.addTab("Extrusión", jPanel2);

        panImp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panImp.setPreferredSize(new java.awt.Dimension(565, 304));

        jLabel134.setForeground(new java.awt.Color(0, 102, 153));
        jLabel134.setText("Operador:");

        jLabel133.setForeground(new java.awt.Color(0, 102, 153));
        jLabel133.setText("Greña:");

        greImp.setForeground(new java.awt.Color(0, 153, 153));
        greImp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                greImpKeyTyped(evt);
            }
        });

        jLabel135.setForeground(new java.awt.Color(0, 102, 153));
        jLabel135.setText("Máquina:");

        maqImp.setForeground(new java.awt.Color(0, 153, 153));
        maqImp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                maqImpKeyTyped(evt);
            }
        });

        jLabel136.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(0, 102, 153));
        jLabel136.setText("Tiempo de trabajo");

        jLabel137.setForeground(new java.awt.Color(0, 102, 153));
        jLabel137.setText("Hora inicial:");

        hrIniImp.setForeground(new java.awt.Color(0, 153, 153));

        jLabel138.setForeground(new java.awt.Color(0, 102, 153));
        jLabel138.setText("Fecha inicial:");

        jLabel139.setForeground(new java.awt.Color(0, 102, 153));
        jLabel139.setText("Hora final:");

        hrFinImp.setForeground(new java.awt.Color(0, 153, 153));

        jLabel140.setForeground(new java.awt.Color(0, 102, 153));
        jLabel140.setText("Fecha final:");

        jLabel142.setForeground(new java.awt.Color(0, 102, 153));
        jLabel142.setText("Total de horas:");

        totalHrImp.setForeground(new java.awt.Color(0, 153, 153));

        jLabel141.setForeground(new java.awt.Color(0, 102, 153));
        jLabel141.setText("Tiempo muerto:");

        hrMuertoImp.setForeground(new java.awt.Color(0, 153, 153));

        agI.setBackground(new java.awt.Color(51, 51, 51));
        agI.setForeground(new java.awt.Color(255, 255, 255));
        agI.setText("Agregar Registro a Impreso");
        agI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agIActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 102, 153));
        jLabel5.setText("Kilos:");

        kgOpIm.setForeground(new java.awt.Color(0, 153, 153));
        kgOpIm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgOpImKeyTyped(evt);
            }
        });

        costoOpImp.setForeground(new java.awt.Color(0, 153, 153));

        jLabel9.setForeground(new java.awt.Color(0, 102, 153));
        jLabel9.setText("Costo de Operación: $");

        extHrImp.setForeground(new java.awt.Color(0, 153, 153));

        jLabel143.setForeground(new java.awt.Color(0, 102, 153));
        jLabel143.setText("De las cuales son extras:");

        listOperadorI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listOperadorIItemStateChanged(evt);
            }
        });

        jLabel147.setForeground(new java.awt.Color(0, 102, 153));
        jLabel147.setText("Ayudante:");

        listAyudanteI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listAyudanteIItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panImpLayout = new javax.swing.GroupLayout(panImp);
        panImp.setLayout(panImpLayout);
        panImpLayout.setHorizontalGroup(
            panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panImpLayout.createSequentialGroup()
                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel142)
                            .addComponent(jLabel139)
                            .addComponent(jLabel141)
                            .addComponent(jLabel137))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totalHrImp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hrFinImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hrMuertoImp, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hrIniImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(jLabel138)
                                .addGap(28, 28, 28)
                                .addComponent(fIniImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(costoOpImp, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(jLabel140)
                                .addGap(28, 28, 28)
                                .addComponent(fFinImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(jLabel143)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(extHrImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(13, 13, 13))
                    .addGroup(panImpLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel147)
                            .addComponent(jLabel134))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel136, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                                        .addComponent(listOperadorI, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                        .addComponent(jLabel133)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(greImp, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel135)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maqImp, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(listAyudanteI, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                                        .addGap(60, 60, 60)
                                        .addComponent(kgOpIm, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panImpLayout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(103, 103, 103))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(agI)))
                .addContainerGap())
        );
        panImpLayout.setVerticalGroup(
            panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panImpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel133)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maqImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel135)
                            .addComponent(greImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel134)
                        .addComponent(listOperadorI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel147)
                            .addComponent(listAyudanteI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panImpLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(kgOpIm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(5, 5, 5)))
                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panImpLayout.createSequentialGroup()
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jLabel137)
                                .addGap(14, 14, 14)
                                .addComponent(jLabel139))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel136)
                                .addGap(5, 5, 5)
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fIniImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel138))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fFinImp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel140, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(extHrImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel143))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(costoOpImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panImpLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(hrIniImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(hrFinImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel141, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hrMuertoImp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel142)
                            .addComponent(totalHrImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(agI)
                .addContainerGap())
        );

        panMaqImp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMaqImp.setPreferredSize(new java.awt.Dimension(435, 239));

        jLabel144.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(0, 102, 153));
        jLabel144.setText("Maquila o compra ");

        jLabel145.setForeground(new java.awt.Color(0, 102, 153));
        jLabel145.setText("Proveedor:");

        provImp.setForeground(new java.awt.Color(0, 153, 153));
        provImp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                provImpKeyTyped(evt);
            }
        });

        jLabel146.setForeground(new java.awt.Color(0, 102, 153));
        jLabel146.setText("Precio por kg: $");

        porKgImp.setForeground(new java.awt.Color(0, 153, 153));
        porKgImp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                porKgImpKeyTyped(evt);
            }
        });

        kgImp.setForeground(new java.awt.Color(0, 153, 153));
        kgImp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgImpKeyTyped(evt);
            }
        });

        jLabel132.setForeground(new java.awt.Color(0, 102, 153));
        jLabel132.setText("Producción kg:");

        gdIm.setBackground(new java.awt.Color(51, 51, 51));
        gdIm.setForeground(new java.awt.Color(255, 255, 255));
        gdIm.setText("Guardar Cambios de Impreso");
        gdIm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gdImActionPerformed(evt);
            }
        });

        jLabel148.setForeground(new java.awt.Color(0, 102, 153));
        jLabel148.setText("Proveedor:");

        provImp2.setForeground(new java.awt.Color(0, 153, 153));
        provImp2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                provImp2KeyTyped(evt);
            }
        });

        jLabel149.setForeground(new java.awt.Color(0, 102, 153));
        jLabel149.setText("Precio por kg: $");

        porKgImp2.setForeground(new java.awt.Color(0, 153, 153));
        porKgImp2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                porKgImp2KeyTyped(evt);
            }
        });

        jLabel150.setForeground(new java.awt.Color(0, 102, 153));
        jLabel150.setText("Producción kg:");

        kgImp2.setForeground(new java.awt.Color(0, 153, 153));
        kgImp2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgImp2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panMaqImpLayout = new javax.swing.GroupLayout(panMaqImp);
        panMaqImp.setLayout(panMaqImpLayout);
        panMaqImpLayout.setHorizontalGroup(
            panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqImpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panMaqImpLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(gdIm))
                    .addGroup(panMaqImpLayout.createSequentialGroup()
                        .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMaqImpLayout.createSequentialGroup()
                                .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panMaqImpLayout.createSequentialGroup()
                                        .addComponent(jLabel145)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(provImp, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel146))
                                    .addComponent(jLabel144))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(porKgImp, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel132))
                            .addGroup(panMaqImpLayout.createSequentialGroup()
                                .addComponent(jLabel148)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(provImp2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel149)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(porKgImp2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel150)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMaqImpLayout.createSequentialGroup()
                                .addComponent(kgImp, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE))
                            .addGroup(panMaqImpLayout.createSequentialGroup()
                                .addComponent(kgImp2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        panMaqImpLayout.setVerticalGroup(
            panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqImpLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel144)
                .addGap(52, 52, 52)
                .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel145)
                    .addComponent(provImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel146)
                    .addComponent(porKgImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel132)
                    .addComponent(kgImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel148)
                    .addComponent(provImp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel149)
                    .addComponent(porKgImp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel150)
                    .addComponent(kgImp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(gdIm)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panMaqImp, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panImp, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(329, 329, 329)
                .addComponent(panMaqImp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(264, Short.MAX_VALUE)))
        );

        paPro.addTab("Impresión", jPanel10);

        panBol.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel69.setForeground(new java.awt.Color(0, 102, 153));
        jLabel69.setText("Operador:");

        jLabel68.setForeground(new java.awt.Color(0, 102, 153));
        jLabel68.setText("Greña:");

        greBol.setForeground(new java.awt.Color(0, 153, 153));
        greBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                greBolKeyTyped(evt);
            }
        });

        jLabel70.setForeground(new java.awt.Color(0, 102, 153));
        jLabel70.setText("Máquina:");

        maqBol.setForeground(new java.awt.Color(0, 153, 153));
        maqBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                maqBolKeyTyped(evt);
            }
        });

        suaje.setForeground(new java.awt.Color(0, 153, 153));
        suaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                suajeKeyTyped(evt);
            }
        });

        jLabel83.setForeground(new java.awt.Color(0, 102, 153));
        jLabel83.setText("Suaje:");

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(0, 102, 153));
        jLabel71.setText("Tiempo de trabajo");

        jLabel72.setForeground(new java.awt.Color(0, 102, 153));
        jLabel72.setText("Hora inicial:");

        hrIniBol.setForeground(new java.awt.Color(0, 153, 153));

        jLabel73.setForeground(new java.awt.Color(0, 102, 153));
        jLabel73.setText("Fecha inicial:");

        jLabel74.setForeground(new java.awt.Color(0, 102, 153));
        jLabel74.setText("Hora final:");

        hrFinBol.setForeground(new java.awt.Color(0, 153, 153));

        jLabel75.setForeground(new java.awt.Color(0, 102, 153));
        jLabel75.setText("Fecha final:");

        jLabel77.setForeground(new java.awt.Color(0, 102, 153));
        jLabel77.setText("Total de horas:");

        totalHrBol.setForeground(new java.awt.Color(0, 153, 153));

        jLabel76.setForeground(new java.awt.Color(0, 102, 153));
        jLabel76.setText("Tiempo muerto:");

        hrMuertoBol.setForeground(new java.awt.Color(0, 153, 153));

        agB.setBackground(new java.awt.Color(51, 51, 51));
        agB.setForeground(new java.awt.Color(255, 255, 255));
        agB.setText("Agregar Registro a Bolseo");
        agB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agBActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 102, 153));
        jLabel6.setText("Kilos:");

        kgOpBol.setForeground(new java.awt.Color(0, 153, 153));
        kgOpBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgOpBolKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 102, 153));
        jLabel10.setText("Costo de Operación: $");

        costoOpBol.setForeground(new java.awt.Color(0, 153, 153));

        jLabel78.setForeground(new java.awt.Color(0, 102, 153));
        jLabel78.setText("De las cuales son extras:");

        extBol.setForeground(new java.awt.Color(0, 153, 153));

        listOperadorB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listOperadorBItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panBolLayout = new javax.swing.GroupLayout(panBol);
        panBol.setLayout(panBolLayout);
        panBolLayout.setHorizontalGroup(
            panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panBolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel71)
                            .addGroup(panBolLayout.createSequentialGroup()
                                .addComponent(jLabel69)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listOperadorB, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                        .addComponent(jLabel70)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(maqBol, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(greBol, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel83)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(suaje, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgOpBol, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panBolLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(agB)))
                .addContainerGap())
            .addGroup(panBolLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72)
                    .addComponent(jLabel74)
                    .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panBolLayout.createSequentialGroup()
                            .addComponent(jLabel76)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(hrMuertoBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panBolLayout.createSequentialGroup()
                                .addComponent(jLabel77)
                                .addGap(18, 18, 18)
                                .addComponent(totalHrBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panBolLayout.createSequentialGroup()
                                .addGap(101, 101, 101)
                                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hrIniBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hrFinBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addGap(27, 27, 27)
                        .addComponent(fFinBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addComponent(jLabel73)
                        .addGap(18, 18, 18)
                        .addComponent(fIniBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(costoOpBol, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
        );
        panBolLayout.setVerticalGroup(
            panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panBolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(jLabel70)
                    .addComponent(maqBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listOperadorB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(greBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel83)
                    .addComponent(suaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(kgOpBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addGap(13, 13, 13)
                        .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(fIniBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel72)
                            .addComponent(hrIniBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)))
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel75)
                    .addComponent(hrFinBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74)
                    .addComponent(fFinBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hrMuertoBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel76))
                        .addGap(18, 18, 18)
                        .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalHrBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel77)))
                    .addGroup(panBolLayout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addGap(18, 18, 18)
                        .addGroup(panBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(costoOpBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(extBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(agB)
                .addContainerGap())
        );

        panMaqBol.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel79.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(0, 102, 153));
        jLabel79.setText("Maquila o compra ");

        jLabel80.setForeground(new java.awt.Color(0, 102, 153));
        jLabel80.setText("Proveedor:");

        provBol.setForeground(new java.awt.Color(0, 153, 153));
        provBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                provBolKeyTyped(evt);
            }
        });

        jLabel81.setForeground(new java.awt.Color(0, 102, 153));
        jLabel81.setText("Precio por kg: $");

        porKgBol.setForeground(new java.awt.Color(0, 153, 153));
        porKgBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                porKgBolKeyTyped(evt);
            }
        });

        jLabel53.setForeground(new java.awt.Color(0, 102, 153));
        jLabel53.setText("Producción kg:");

        kgBol.setForeground(new java.awt.Color(0, 153, 153));
        kgBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgBolKeyTyped(evt);
            }
        });

        jLabel82.setForeground(new java.awt.Color(0, 102, 153));
        jLabel82.setText("Producción piezas:");

        pzsBol.setForeground(new java.awt.Color(0, 153, 153));
        pzsBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pzsBolKeyTyped(evt);
            }
        });

        gdBo.setBackground(new java.awt.Color(51, 51, 51));
        gdBo.setForeground(new java.awt.Color(255, 255, 255));
        gdBo.setText("Guardar Cambios de Bolseo");
        gdBo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gdBoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panMaqBolLayout = new javax.swing.GroupLayout(panMaqBol);
        panMaqBol.setLayout(panMaqBolLayout);
        panMaqBolLayout.setHorizontalGroup(
            panMaqBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqBolLayout.createSequentialGroup()
                .addGap(216, 216, 216)
                .addComponent(jLabel79)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panMaqBolLayout.createSequentialGroup()
                .addGroup(panMaqBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMaqBolLayout.createSequentialGroup()
                        .addComponent(jLabel80)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(provBol))
                    .addGroup(panMaqBolLayout.createSequentialGroup()
                        .addComponent(jLabel81)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(porKgBol, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kgBol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel82)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pzsBol, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panMaqBolLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(gdBo)))
                .addContainerGap())
        );
        panMaqBolLayout.setVerticalGroup(
            panMaqBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqBolLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel79)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panMaqBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(provBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(panMaqBolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(porKgBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53)
                    .addComponent(kgBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82)
                    .addComponent(pzsBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addComponent(gdBo)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panBol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panMaqBol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panMaqBol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paPro.addTab("Bolseo", jPanel6);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Andalus", 0, 18)); // NOI18N
        jLabel1.setText("Procesos");

        regresar.setBackground(new java.awt.Color(51, 51, 51));
        regresar.setForeground(new java.awt.Color(255, 255, 255));
        regresar.setText("Cerrar");
        regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(regresar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regresar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        busquedaImp.setBackground(new java.awt.Color(51, 51, 51));
        busquedaImp.setForeground(new java.awt.Color(255, 255, 255));
        busquedaImp.setText("Buscar Pedido");
        busquedaImp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busquedaImpActionPerformed(evt);
            }
        });

        impBus.setForeground(new java.awt.Color(0, 153, 153));
        impBus.setText("Nombre Impresión");
        impBus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                impBusMousePressed(evt);
            }
        });
        impBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                impBusKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                impBusKeyTyped(evt);
            }
        });

        tablaPed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaPed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPedMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPed);

        foVis.setBackground(new java.awt.Color(255, 255, 153));
        foVis.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        foVis.setForeground(new java.awt.Color(51, 102, 0));

        tablaPart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaPart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPartMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaPart);

        labelxd.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelxd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelxd.setText("Partidas");
        labelxd.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        idPartida.setBackground(new java.awt.Color(255, 255, 153));
        idPartida.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        idPartida.setForeground(new java.awt.Color(51, 102, 0));

        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setText("Folio Seleccionado:");

        jLabel3.setForeground(new java.awt.Color(0, 102, 153));
        jLabel3.setText("Partida Elegida:");

        generarPro.setBackground(new java.awt.Color(51, 51, 51));
        generarPro.setForeground(new java.awt.Color(255, 255, 255));
        generarPro.setText("Generar Procesos");
        generarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarProActionPerformed(evt);
            }
        });

        eliminarP.setBackground(new java.awt.Color(51, 51, 51));
        eliminarP.setForeground(new java.awt.Color(255, 255, 255));
        eliminarP.setText("Eliminar partida");
        eliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarPActionPerformed(evt);
            }
        });

        cambioMod.setBackground(new java.awt.Color(51, 51, 51));
        cambioMod.setForeground(new java.awt.Color(255, 255, 255));
        cambioMod.setText("Cambiar modo");
        cambioMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambioModActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(paPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(busquedaImp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(impBus)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(foVis, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(labelxd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(cambioMod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(79, 79, 79)
                                .addComponent(eliminarP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(generarPro)
                                .addGap(9, 9, 9)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(busquedaImp)
                            .addComponent(foVis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(impBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(labelxd)
                            .addComponent(idPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(generarPro)
                            .addComponent(eliminarP)
                            .addComponent(cambioMod)))
                    .addComponent(paPro))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    //boton de regresar
    private void regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarActionPerformed
        regresar.setSelected(false);
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_regresarActionPerformed
    
    //cuando la tabla de pedidos es clickeada
    private void tablaPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedMouseClicked
        obtenerSeleccionTablaPedido();//cambia el folio, lo que rellena la tabla de partidas
    }//GEN-LAST:event_tablaPedMouseClicked
    
    //obtiene y cambia el folio que aparece en el interfaz
    private void obtenerSeleccionTablaPedido(){
        vaciarCamposMaquila();
        String fol;
        try
        {
            fol = modPed.getValueAt(tablaPed.getSelectedRow(), 0).toString().replace("A", "");//Se obtiene el folio del pedido
                        
            //Se desactivan los botones de registros de operadores y maquila
            generarPro.setEnabled(false);
            agE.setEnabled(false);
            agI.setEnabled(false);
            agB.setEnabled(false);
            gdEx.setEnabled(false);
            gdIm.setEnabled(false);
            gdBo.setEnabled(false);

            //regresa el tamaño de la ventana a la mitad
            paPro.setVisible(false);
            this.setSize(new Dimension(WD/2, HG));
            this.setLocationRelativeTo(null);

            modPart.setRowCount(0);//se vacia la tabla de partidas

            foVis.setText(fol);
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        }  
    }
    
    //Boton: busqueda de los pedidos mediante la impresion
    private void busquedaImpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busquedaImpActionPerformed
        busquedaImp.setSelected(false);
        llenarTablaPedido();
    }//GEN-LAST:event_busquedaImpActionPerformed
    
    //llena la tabla de pedidos
    public void llenarTablaPedido(){
        
        paPro.setVisible(false);
        this.setSize(new Dimension(WD/2, HG));
        this.setLocationRelativeTo(null);
        
        modPed.setRowCount(0);//Para que no se acumulen los resultados de las consultas en la tabla
        modPart.setRowCount(0);//vacía la tabla de partidas
        String nompedido = impBus.getText();//Para guardar el nombre de Impresion a buscar
        
        //Se utiliza join para que tambien se muestren los datos de clientes relacionados con el pedido
        //En este caso solo para mostrar el nombre del cliente relacionado a su pedido, 2 tablas distintas.
        String sql = "select folio, impresion, nom, fIngreso from pedido join cliente on idC_fk = idC where impresion like '%"+nompedido+"%' "
                + "order by fIngreso desc limit 0, 30";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                modPed.addRow(new Object[]{rs.getString("folio")+"A", rs.getString("impresion"), rs.getString("nom"), 
                    rs.getString("fIngreso")});
            }
            rs.close();
            st.close();
         } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado ningún pedido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //obtiene el id de la partida
    private void obtenerSeleccionTablaPartida()
    {
        String idPartSt;//Para guardar la id de la partida que se clickee
        try
        {
            idPartSt = modPart.getValueAt(tablaPart.getSelectedRow(), 0).toString();
            
            idPartida.setText(idPartSt);//El campo desabilitado para las partidas se establece con la id de la partida ya guardada
            idPart = Integer.parseInt(idPartSt);//A esa varible global se le asigna el resultado de a seleccion, se pasa a entero.

            //se habilita el boton para eliminar partidas
            eliminarP.setEnabled(true);
            
            comprobarProcesos(idPart);//Verificar si las partidas ya tienen generados los procesos
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        }
        cambiaTextoBoton();//cambia el boton, deacuerdo al modo de generacion   
    }
    
    //cambia el texto del boton de cambio de modo, deacuerdo a lo que le falte
    private void cambiaTextoBoton()
    {
        String modo = "";
        String sql = "select modoMat from partida where idPar = "+idPart+"";//se busca si fue compra o produccion de material
        try
        {
            st =  con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                modo = rs.getString("modoMat");
            }
            rs.close();
            st.close();
                if(modo == null)//si no se han generado procesos no aparece el boton
                {
                    cambioMod.setVisible(false);
                }
                else if(modo.equals(modoMaterial[0]))//si fue produccion
                {
                    cambioMod.setVisible(true);
                    cambioMod.setText("Agregar compras");
                }
                else if(modo.equals(modoMaterial[1]))//si fueron compras
                {
                    cambioMod.setVisible(true);
                    cambioMod.setText("Agregar producción");
                }
                else if(modo.equals(modoMaterial[2]))//si fueron ambos no aparece el boton
                {
                    cambioMod.setVisible(false);
                }
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    //Genera procesos
    private void generarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarProActionPerformed
        generarPro.setSelected(false);
        
        //pregunta en que modo se generaran los procesos
        String elegido = (String) JOptionPane.showInputDialog(null, "Compra o Produccion de Material?", "Modo de generacion", 
        JOptionPane.QUESTION_MESSAGE, null, modoMaterial,  modoMaterial[0]);
        
        if(!(elegido == null)){//si eligio una opcion valida
            
            comprobarVacio();//Establece en 0 los campos
            //preguarda o inicializa los datos de maquila en la base en 0
            //Extrusion
            pM1St = proM1.getText();
            pM2St = proM2.getText();
            provE1St = prov1Ext.getText();
            precioKgE1St = porKg1Ext.getText();
            prov2St = prov2Ext.getText();
            precioKg2St = porKg2Ext.getText();
            //Impreso
            prodISt = kgImp.getText();
            provI1St = provImp.getText();
            precioKgI1St = porKgImp.getText();
            prodI2St = kgImp2.getText();
            provI2St = provImp2.getText();
            precioKgI2St = porKgImp2.getText();
            //Bolseo
            prodBSt = kgBol.getText();
            prodPzSt = pzsBol.getText();
            provB1St = provBol.getText();
            precioKgB1St = porKgBol.getText();
            //Extrusion
            String sql = "insert into extrusion(pocM1, pocM2, prov1, precioKg1,"
                    + "prov2, precioKg2, kgTotales, costoOpTotalExt, greniaExt,"
                    + "costoUnitarioExt, hrTotalesPar, idPar_fk) values("+pM1St+","
                    + ""+pM2St+",'"+provE1St+"',"+precioKgE1St+",'"+ prov2St+"',"
                    + precioKg2St+",0,0,0,0,0,"+idPart+")";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "No se pudo generar extrusion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            //Impreso
            sql = "insert into impreso(produccion, prov1, precioKg1, produccion2, "
                    + "prov2, precioKg2, kgTotales, costoOpTotalImp, greniaImp, "
                    + "costoUnitarioImp, hrTotalesPar, idPar_fk) values("
                    + prodISt+",'"+provI1St+"',"+precioKgI1St+","+prodI2St+","
                    + "'"+provI2St+"',"+precioKgI2St+",0,0,0,0,0,"+idPart+")";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "No se pudo generar impreso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            //Bolseo
            sql = "insert into bolseo(produccion, produccionPz, prov1, precioKg1, "
                    + "kgTotales, costoOpTotalBol, greniaBol, costoUnitarioBol, "
                    + "hrTotalesPar, idPar_fk) values("+prodBSt+","+prodPzSt+",'"
                    +provB1St+"',"+precioKgB1St+",0,0,0,0,0,"+idPart+")";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
                JOptionPane.showMessageDialog(null, "Procesos Generados: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "No se pudo generar bolseo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            sql = "update partida set modoMat = '"+elegido+"' where idPar = "+idPart+"";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al crear el modo de produccion:"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
            
            //cambiamos el texto del boton
            if(elegido.equals(modoMaterial[0]))//si eligio produccion
            {
                cambioMod.setVisible(true);
                cambioMod.setText("Agregar compras");
            }
            else if(elegido.equals(modoMaterial[1]))//si eligio maquila
            {
                cambioMod.setVisible(true);
                cambioMod.setText("Agregar producción");
            }

            comprobarProcesos(idPart);//comprueba si extrusion ya tiene id
        }
        else//si no eligio nada
        {
            JOptionPane.showMessageDialog(null, "No se eligio ningun modo", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_generarProActionPerformed
    
    //Verificar si una partida ya genero extrusion
    private void comprobarProcesos(int idP){
        
        String sql = "select idExt from extrusion where idPar_fk = "+idP+"";
        int idE = 0;
        
        try {//Extrusion: Obtencion de id principal
            st =  con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                idE = rs.getInt("idExt");//Se guardara la id de extrusion en caso de haber resultados
                idEx = idE;//variable global de extrusion
            }
            rs.close();
            st.close();
            
            //Si todavia no se an generado los procesos
            if(idE == 0){//Si idE es igual a 0, significa que no hay registros en extrusion, ni en otro proceso
                //Si no hay la pantalla se reducira
                this.setSize(new Dimension(WD/2, HG));
                paPro.setVisible(false);
                this.setLocationRelativeTo(null);
                
                generarPro.setEnabled(true);//El boton para generar procesos se activa para que deje generarlos
                
                //Los de registro de produccion se desactivan, pues todavia no se han generado procesos
                agE.setEnabled(false);
                agI.setEnabled(false);
                agB.setEnabled(false);
                
                //Tambien los de maquila
                gdEx.setEnabled(false);
                gdIm.setEnabled(false);
                gdBo.setEnabled(false);
            }
            else
            {//Si ya fueron generados los procesos
                
                llenarListasOperadores();//se llenan las listas de operadores
                llenarListaAyudantes();//se llena la lista de ayudantes
                
                this.setSize(new Dimension(WD, HG));//se agranda la pantalla
                paPro.setVisible(true);//se muestra el panel de los registros
                this.setLocationRelativeTo(null);
                
                vaciarCamposMaquila();
                generarPro.setEnabled(false);//boton generar procesos se desactiva
                //Se habilitan los botones para guardar registros
                agE.setEnabled(true);
                agI.setEnabled(true);
                agB.setEnabled(true);
                gdEx.setEnabled(true);
                gdIm.setEnabled(true);
                gdBo.setEnabled(true);
                
                comprobarModoMat();//habilitamos los paneles correspondientes
                establecerCamposPartida();//Establece los datos de maquila y establece las id globales de los procesos
            }
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la busqueda de procesos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //llena las listas de operadores cuando ya se crearon los procesos
    private void llenarListasOperadores()
    {
        //borramos los elementos que ya tenian las listas
        listOperadorE.removeAllItems();
        listOperadorI.removeAllItems();
        listOperadorB.removeAllItems();
        String nombreSt = "";//nombre individual
        ArrayList<String> nombres = new ArrayList<String>();//lista de nombres
        /*utiliza una lista ya que habia conflictos al agregarlos directamente a 
        las listas desplegables dentro del while*/
        //llenamos la lista de nombres
        String sql = "select nombre from operadores";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())//llena las listas de operadores
            {
                nombreSt = rs.getString("nombre");
                nombres.add(nombreSt);
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        //llenamos las listas desplegables
        for(int i=0;i<nombres.size();i++)
        {
            nombreSt = nombres.get(i);
            listOperadorE.addItem(nombreSt);
            listOperadorI.addItem(nombreSt);
            listOperadorB.addItem(nombreSt);
        }    
    }
    
    //llena la lista de ayudantes cuando ya se crearon los procesos
    private void llenarListaAyudantes()
    {
      listAyudanteI.removeAllItems();//se vacian los elementos anteriores
      String sql = "select nombre from operadores";
      String nombre = "";
      ArrayList<String> nombres = new ArrayList<String>();//lista de nombres
      /*utiliza una lista ya que habia conflictos al agregarlos directamente a 
      las listas desplegables dentro del while*/
      //llenamos la lista de nombres
      try
      {
          st = con.createStatement();
          rs = st.executeQuery(sql);
          while(rs.next())
          {
              nombre = rs.getString("nombre");
              nombres.add(nombre);
          }
          rs.close();
          st.close();
      }
      catch(SQLException ex)
      {
          ex.printStackTrace();
      }
      
      //llenamos la lista desplegable
        for(int i=0;i<nombres.size();i++)
        {
            nombre = nombres.get(i);
            listAyudanteI.addItem(nombre);
        } 
    }
    
    //Se establecen en "" o "0" los campos de maquila
    private void vaciarCamposMaquila(){
        proM1.setText("0");
        proM2.setText("0");
        prov1Ext.setText("");
        porKg1Ext.setText("0");
        prov2Ext.setText("");
        porKg2Ext.setText("0");
        
        kgImp.setText("0");
        provImp.setText("");
        porKgImp.setText("0");
        kgImp2.setText("0");
        provImp2.setText("");
        porKgImp2.setText("0");
        
        kgBol.setText("0");
        pzsBol.setText("0");
        provBol.setText("");
        porKgBol.setText("0");
    }
    
    //habilita ciertas partes de la pantalla deacuerdo a si fueron compras o produccion
    public void comprobarModoMat(){
        String sql = "select modoMat from partida where idPar = "+idPart+"";
        String modo = "";
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                modo = rs.getString("modoMat");
                if(modo == null){//Si esta en null significa que no se han generado procesos
                    modo = "";
                }
            }
            rs.close();
            st.close();
            
            if(modo.equals(modoMaterial[0])){//Produccion
                //paneles de produccion
                panEx.setVisible(true);
                panImp.setVisible(true);
                panBol.setVisible(true);
                //paneles de maquila 
                panMaqExt.setVisible(false);
                panMaqImp.setVisible(false);
                panMaqBol.setVisible(false);
            }
            else if(modo.equals(modoMaterial[1])){//Compra
                //paneles de produccion
                panEx.setVisible(false);
                panImp.setVisible(false);
                panBol.setVisible(false);
                //paneles de maquila
                panMaqExt.setVisible(true);
                panMaqImp.setVisible(true);
                panMaqBol.setVisible(true);
                
            }
            else if(modo.equals(modoMaterial[2])){//Ambos
                //paneles de produccion
                panEx.setVisible(true);
                panImp.setVisible(true);
                panBol.setVisible(true);
                //paneles de maquila
                panMaqExt.setVisible(true);
                panMaqImp.setVisible(true);
                panMaqBol.setVisible(true);
            }
            else if(modo.equals("")){//Si no se han generado procesos
                //paneles de produccion
                panEx.setVisible(false);
                panImp.setVisible(false);
                panBol.setVisible(false);
                //paneles de maquila
                panMaqExt.setVisible(false);
                panMaqImp.setVisible(false);
                panMaqBol.setVisible(false);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    //Establece los datos de maquila y establece las id globales de los procesos
    private void establecerCamposPartida(){
        
        String sql = "select pocM1, pocM2, prov1, precioKg1, prov2, precioKg2, idExt from extrusion where idPar_fk = "+idPart+"";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                proM1.setText(rs.getString("pocM1"));
                proM2.setText(rs.getString("pocM2"));
                prov1Ext.setText(rs.getString("prov1"));
                porKg1Ext.setText(rs.getString("precioKg1"));
                prov2Ext.setText(rs.getString("prov2"));
                porKg2Ext.setText(rs.getString("precioKg2"));
                idEx = rs.getInt("idExt");//id global
            }
            rs.close();
            st.close();
        } 
        catch(SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al establecer los datos de extrusion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        sql = "select produccion, prov1, precioKg1, produccion2, prov2, precioKg2, sticky, idImp from impreso where idPar_fk = "+idPart+"";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                kgImp.setText(rs.getString("produccion"));
                provImp.setText(rs.getString("prov1"));
                porKgImp.setText(rs.getString("precioKg1"));
                kgImp2.setText(rs.getString("produccion2"));
                provImp2.setText(rs.getString("prov2"));
                porKgImp2.setText(rs.getString("precioKg2"));
                idIm = rs.getInt("idImp");//id global
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al establecer los datos de impreso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        sql = "select produccion, produccionPz, prov1, precioKg1, idBol from bolseo where idPar_fk = "+idPart+"";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                kgBol.setText(rs.getString("produccion"));
                pzsBol.setText(rs.getString("produccionPz"));
                provBol.setText(rs.getString("prov1"));
                porKgBol.setText(rs.getString("precioKg1"));
                idBo = rs.getInt("idBol");//id global
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de bolseo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    //Boton: agregar operadores de impreso
    private void agIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agIActionPerformed
            agI.setSelected(false);
            comprobarVacio();//establece en 0 los campos vacios, de produccion o maquila
            kgISt = kgOpIm.getText();
            greniaISt = greImp.getText();
            opISt = listOperadorI.getSelectedItem().toString();
            ayudanteSt = listAyudanteI.getSelectedItem().toString();
            nMISt = maqImp.getText();
            hIniISt = hrIniImp.getTimeField().getText();
            fIniISt = fIniImp.getText();
            hFinISt = hrFinImp.getTimeField().getText();
            fFinISt = fFinImp.getText();
            tMuISt = hrMuertoImp.getTimeField().getText();
            totHISt = totalHrImp.getTimeField().getText();
            exISt = extHrImp.getTimeField().getText();
            costoOpImSt = costoOpImp.getText();
            
            String sql = "insert into operadorImp(costoOpImp, kgUniI, grenia, "
                    + "operador, ayudante, numMaquina, horaIni, fIni, horaFin, "
                    + "fFin, tiempoMuerto, totalHoras, extras, idImp_fk) values("
                    +costoOpImSt+", "+kgISt+","+greniaISt+",'"+opISt+"','"
                    +ayudanteSt+"',"+nMISt+",'"+hIniISt+"', '"+fIniISt+"', '"
                    +hFinISt+"', '"+fFinISt+"', '"+tMuISt+"', '"+totHISt+"', '"
                    +exISt+"', "+idIm+")";
        try { 
            st = con.createStatement();
            st.execute(sql);
            st.close();
            
            sumarKilosIm();//hace la sumatoria de los kg producidos en impreso y actualiza la base
            sumarCostosOperacionales("impreso", "operadorImp", "costoOpImp", "costoOpTotalImp", "idImp", "idImp_fk", idIm);//hace la sumatoria de los costos operacionales y la actualiza en la base
            sumarGrenias("operadorImp", "grenia", "idImp_fk", "impreso", "greniaImp", "idImp", idIm);//Sumar y actualiza la suma de grenias de los registros de operador
            calculaCostoPartida();//suma los costos de operacion de cada proceso y el costo de material y los inserta en la tabla partida
            calculaHrTotalesProceso("operadorImp", "idImp_fk", idIm,"impreso");//se hace la sumatoria de horas de procesos de la partida
            calcularCostoUnitarioImp();//se calcula e inserta el costo unitario de impresion
            calcularCostoTotalPe();//calcula e inserta el costo total del pedido
            calculaPyG();//se calculan las perdidas y ganancias
            vaciarOpI();//establece en 0 los campos de captura de impresion
            JOptionPane.showMessageDialog(null, "Se ha guardado el operador en Impreso: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el operador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_agIActionPerformed
    
    //Boton: agregar operadores de bolseo
    private void agBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agBActionPerformed
        agB.setSelected(false);
        comprobarVacio();
        kgBSt = kgOpBol.getText();
        greniaBSt = greBol.getText();
        suajeBSt = suaje.getText();
        opBSt = listOperadorB.getSelectedItem().toString();
        nMBSt = maqBol.getText();
        hIniBSt = hrIniBol.getTimeField().getText();
        fIniBSt = fIniBol.getText();
        hFinBSt = hrFinBol.getTimeField().getText();
        fFinBSt = fFinBol.getText();
        tMuBSt = hrMuertoBol.getTimeField().getText();
        totHBSt = totalHrBol.getTimeField().getText();
        exBSt = extBol.getTimeField().getText();
        costoOpBoSt = costoOpBol.getText();
        
        String sql = "insert into operadorBol(costoOpBol, kgUniB, grenia, suaje, "
                + "operador, numMaquina, horaIni, fIni, horaFin, fFin, tiempoMuerto,"
                + "totalHoras, extras, idBol_fk) values("+costoOpBoSt+", "+kgBSt+","
                +greniaBSt+", '"+suajeBSt+"','"+opBSt+"',"+nMBSt+",'"+hIniBSt+"', '"
                +fIniBSt+"', '"+hFinBSt+"', '"+fFinBSt+"', '"+tMuBSt+"',"
                +" '"+totHBSt+"', '"+exBSt+"', "+idBo+")";
        
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            
            sumarKilosBol();
            sumarCostosOperacionales("bolseo", "operadorBol", "costoOpBol", "costoOpTotalBol", "idBol", "idBol_fk", idBo);//hace la sumatoria de los costos operacionales y la actualiza en la base
            sumarGrenias("operadorBol", "grenia", "idBol_fk", "bolseo", "greniaBol", "idBol", idBo);//Sumar y actualiza la suma de grenias de los registros de operador
            actualizarKgDes();
            actualizarPorcentajeDes();
            calculaCostoPartida();//suma los costos de operacion de cada proceso y el costo de material y los inserta en la tabla partida
            calculaHrTotalesProceso("operadorBol", "idBol_fk", idBo, "bolseo");
            calcularCostoUnitarioBol();
            calculaKgDesperdicioPedido();
            calculaPorcentajeDesperdicioPe();
            calcularCostoTotalPe();//calcula e inserta el costo total del pedido
            calculaPyG();
            vaciarOpB();
            JOptionPane.showMessageDialog(null, "Se ha guardado el operador en Bolseo: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el operador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_agBActionPerformed
    
    //Actualizar la maquila de extrusion
    private void gdExActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gdExActionPerformed
        gdEx.setSelected(false);
        comprobarVacio();
        pM1St = proM1.getText();
        pM2St = proM2.getText();
        provE1St = prov1Ext.getText();
        precioKgE1St = porKg1Ext.getText();
        prov2St = prov2Ext.getText();
        precioKg2St = porKg2Ext.getText();
        
        String sql = "update extrusion set pocM1 = "+pM1St+", pocM2 = "+pM2St+", "
                + "prov1 = '"+provE1St+"', precioKg1 = "+precioKgE1St+", "
                + "prov2 = '"+prov2St+"', precioKg2 = "+precioKg2St+""
                + " where idPar_fk = "+idPart+"";
        
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            actualizarKgDes();
            actualizarPorcentajeDes();
            calculaCostoMaterialTotalExt();
            calculaCostoPartida();//suma los costos de operacion de cada proceso y el costo de material y los inserta en la tabla partida
            calcularCostoUnitarioExt();
            calculaKgDesperdicioPedido();
            calculaPorcentajeDesperdicioPe();
            calcularCostoTotalPe();//calcula e inserta el costo total del pedido
            calculaPyG();
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_gdExActionPerformed
    
    //Actualizar la maquila de impresion
    private void gdImActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gdImActionPerformed
        gdIm.setSelected(false);
        comprobarVacio();
        prodISt = kgImp.getText();
        provI1St = provImp.getText();
        precioKgI1St = porKgImp.getText();
        prodI2St = kgImp2.getText();
        provI2St = provImp2.getText();
        precioKgI2St = porKgImp2.getText();
        
        String sql = "update impreso set produccion = "+prodISt+", prov1 = '"
                +provI1St+"', precioKg1 = "+precioKgI1St+", produccion2 = "
                +prodI2St+", prov2 = '"+provI2St+"', precioKg2 = "
                +precioKgI2St+" where idPar_fk = "+idPart+"";
        
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            calcularCostoUnitarioImp();
            calculaPyG();
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro de impreso: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro de impreso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_gdImActionPerformed

    //Actualizar la maquila de bolseo
    private void gdBoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gdBoActionPerformed
        gdBo.setSelected(false);
        comprobarVacio();
        prodBSt = kgBol.getText();
        prodPzSt = pzsBol.getText();
        provB1St = provBol.getText();
        precioKgB1St = porKgBol.getText();

        String sql = "update bolseo set produccion = "+prodBSt+", produccionPz = "
                +prodPzSt+", prov1 = '"+provB1St+"', precioKg1 = "+precioKgB1St+""
                + " where idPar_fk = "+idPart+"";

        try {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            actualizarKgDes();
            actualizarPorcentajeDes();
            calcularCostoUnitarioBol();
            calculaKgDesperdicioPedido();
            calculaPorcentajeDesperdicioPe();
            calculaPyG();
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_gdBoActionPerformed

    private void eliminarPartida()
    {
        String sql = "";
        if(JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar esta partida") == 0)
       {
           sql = "delete from operadorBol where idBol_fk = "+idBo+"";
           try
           {
               st = con.createStatement();
               st.execute(sql);
               st.close();
           }
           catch(SQLException ex)
           {
               ex.printStackTrace();
           }
           
           sql = "delete from operadorImp where idImp_fk = "+idIm+"";
           try
           {
               st = con.createStatement();
               st.execute(sql);
               st.close();
           }
           catch(SQLException ex)
           {
               ex.printStackTrace();
           }
           
           sql = "delete from operadorExt where idExt_fk = "+idEx+"";
           try
           {
               st = con.createStatement();
               st.execute(sql);
               st.close();
           }
           catch(SQLException ex)
           {
               ex.printStackTrace();
           }
           
           sql = "delete from bolseo where idPar_fk = "+idPart+"";
           try
           {
               st = con.createStatement();
               st.execute(sql);
               st.close();
           }
           catch(SQLException ex)
           {
               ex.printStackTrace();
           }
           
           sql = "delete from impreso where idPar_fk = "+idPart+"";
           try
           {
               st = con.createStatement();
               st.execute(sql);
               st.close();
           }
           catch(SQLException ex)
           {
               ex.printStackTrace();
           }
           
           sql = "delete from extrusion where idPar_fk = "+idPart+"";
           try
           {
               st = con.createStatement();
               st.execute(sql);
               st.close();
           }
           catch(SQLException ex)
           {
               ex.printStackTrace();
           }
           
           sql = "delete from partida where idPar = "+idPart+"";
           try
           {
               st = con.createStatement();
               st.execute(sql);
               st.close();
               JOptionPane.showMessageDialog(null,"Se ha borrado la partida");
           }
           catch(SQLException ex)
           {
               ex.printStackTrace();
           }
       }
    }
    
    //hace la sumatoria de los kg producidos y los actualiza en la base
    private void sumaKilosEx(){
        
        String sql = "select kgUniE from operadorExt where idExt_fk = "+idEx+"";
        float kilosExt = 0f;
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next())
            {
                kilosExt = kilosExt + Float.parseFloat(rs.getString("kgUniE"));
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        } 
        
        sql = "update extrusion set kgTotales = "+kilosExt+" where idExt = "+idEx+"";
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    //hace la sumatoria de los kg producidos en impreso y actualiza la base
    private void sumarKilosIm(){
        
        String sql = "select kgUniI from operadorImp where idImp_fk = "+idIm+"";
        float kilosImp = 0f;
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                kilosImp = kilosImp + Float.parseFloat(rs.getString("kgUniI"));
            }
            rs.close();
            st.close();
            
            sql = "update impreso set kgTotales = "+kilosImp+" where idImp = "+idIm+"";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    //hace la sumatoria de los kg producidos y los actualiza en la base
    private void sumarKilosBol(){
        
        float kilosBol = 0f;
        String sql = "select kgUniB from operadorBol where idBol_fk = "+idBo+"";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                kilosBol = kilosBol + Float.parseFloat(rs.getString("kgUniB"));
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        sql = "update bolseo set kgTotales = "+kilosBol+" where idBol = "+idBo+"";
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    //Sumar y actualiza la suma de grenias de los registros de operador
    private void sumarGrenias(String tablaOperador,String greniaOp, String idFk, String tablaProceso, String greniaProceso, String nomCampoId, int idPGlobal){
        
        String sql = "select "+greniaOp+" from "+tablaOperador+" where "+idFk+" = "+idPGlobal+"";
        float kilosGre = 0f;
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                 kilosGre = kilosGre + Float.parseFloat(rs.getString(greniaOp));
            }
            rs.close();
            st.close();
            
            sql = "update "+tablaProceso+" set "+greniaProceso+" = "+kilosGre+" where "+nomCampoId+" = "+idPGlobal+"";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    //Funcion para que solo se ingresen numeros flotantes en campos
    private void soloFlotantes(KeyEvent evt, JTextField campo){
        
        char c = evt.getKeyChar();//Obtener el caracter de la tecla presionada
        int contPuntos = 0;// contador de los ".", pues solo debe de haber uno en un numero flotante
        String cadena = campo.getText();//Guardar la cadena del campo para contar los puntos que contiene
        char aux = 0;//Para ir comprobando cada caracter de la cadena
        
        if((c < '0' || c > '9') && c != '.') 
        {
            evt.consume();//Si el caracter recibido es una letra o caracter, exepto puntos, lo comsume
        }
        else
        {//Si es un numero o punto
            for(int i = 0; i < cadena.length(); i++)
            {
                aux = cadena.charAt(i);//Recorre la cadena para contar los puntos
                if(aux == '.')
                {
                    contPuntos++;//Si encuentra un punto se suma el contadorPuntos
                }
            }
            if(contPuntos == 1 && c == '.')
            {
                    evt.consume();//Si el contador de puntos es 1, significa que ya hay un punto y consumira cualquier otro
                                  //Tambien es importante detectar si se recibe un punto, ya que tambien se reciben numeros en el else,
                                  //los numeros no se consumen, con c == '.' confirmo de que solo consumira puntos
            }
        }
    }
    
    //Funcion para que solo se ingresen numeros enteros en campos
    private void soloEnteros(KeyEvent evt){
        char c = evt.getKeyChar();
        if(c < '0' || c > '9') evt.consume();
    }
    
    //Limitacion de logitud de datos
    private void limitarInsercion(int tamSQL, KeyEvent evt, JTextField campo)
    {
        int tamCampo = campo.getText().length() + 1;
        if(tamCampo > tamSQL)
        {
            evt.consume();
        }
    }
    
    private void proM1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proM1KeyTyped
        soloFlotantes(evt, proM1);
    }//GEN-LAST:event_proM1KeyTyped

    private void proM2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proM2KeyTyped
        soloFlotantes(evt, proM2);
    }//GEN-LAST:event_proM2KeyTyped

    private void porKg1ExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_porKg1ExtKeyTyped
        soloFlotantes(evt, porKg1Ext);
    }//GEN-LAST:event_porKg1ExtKeyTyped

    private void porKg2ExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_porKg2ExtKeyTyped
        soloFlotantes(evt, porKg2Ext);
    }//GEN-LAST:event_porKg2ExtKeyTyped
    //Impreso
    private void kgImpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgImpKeyTyped
        soloFlotantes(evt, kgImp);
    }//GEN-LAST:event_kgImpKeyTyped

    private void porKgImpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_porKgImpKeyTyped
        soloFlotantes(evt, porKgImp);
    }//GEN-LAST:event_porKgImpKeyTyped
    //Bolseo
    private void kgBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgBolKeyTyped
        soloFlotantes(evt, kgBol);
    }//GEN-LAST:event_kgBolKeyTyped

    private void pzsBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pzsBolKeyTyped
        limitarInsercion(8, evt, pzsBol);
        soloEnteros(evt);
    }//GEN-LAST:event_pzsBolKeyTyped

    private void porKgBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_porKgBolKeyTyped
        soloFlotantes(evt, porKgBol);
    }//GEN-LAST:event_porKgBolKeyTyped
    //Operador Extruido
    private void greExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_greExtKeyTyped
        soloFlotantes(evt, greExt);
    }//GEN-LAST:event_greExtKeyTyped

    private void maqExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maqExtKeyTyped
        limitarInsercion(5, evt, maqExt);
        soloEnteros(evt);
    }//GEN-LAST:event_maqExtKeyTyped

    private void kgOpExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgOpExtKeyTyped
        soloFlotantes(evt, kgOpExt);
    }//GEN-LAST:event_kgOpExtKeyTyped
    //Operador Impreso
    private void greImpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_greImpKeyTyped
        soloFlotantes(evt, greImp);
    }//GEN-LAST:event_greImpKeyTyped

    private void maqImpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maqImpKeyTyped
        limitarInsercion(5, evt, maqImp);
        soloEnteros(evt);
    }//GEN-LAST:event_maqImpKeyTyped

    private void kgOpImKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgOpImKeyTyped
        soloFlotantes(evt, kgOpIm);
    }//GEN-LAST:event_kgOpImKeyTyped
    //Operador Bolseo
    private void maqBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maqBolKeyTyped
        limitarInsercion(5, evt, maqBol);
        soloEnteros(evt);
    }//GEN-LAST:event_maqBolKeyTyped

    private void greBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_greBolKeyTyped
        soloFlotantes(evt, greBol);
    }//GEN-LAST:event_greBolKeyTyped

    private void suajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_suajeKeyTyped
        soloFlotantes(evt, suaje);
    }//GEN-LAST:event_suajeKeyTyped

    private void kgOpBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgOpBolKeyTyped
        soloFlotantes(evt, kgOpBol);
    }//GEN-LAST:event_kgOpBolKeyTyped

    //recuadro de busqueda de impresion
    private void impBusMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_impBusMousePressed
        impBus.setText("");
    }//GEN-LAST:event_impBusMousePressed

    //cuando presiona enter al escribir en el recuadro de busqueda de impresion
    private void impBusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impBusKeyPressed
        //listener de la tecla enter
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            llenarTablaPedido();
            
            if(!(tablaPed.getRowCount() == 0)){
                tablaPed.requestFocus();//pasa la seleccion a la tabla de pedidos
            }
        }
    }//GEN-LAST:event_impBusKeyPressed

    private void tablaPartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPartMouseClicked
        obtenerSeleccionTablaPartida();
    }//GEN-LAST:event_tablaPartMouseClicked

    //Calcular monto total del pedido a base de los importes de todas las partidas
    private void establecerSubtotalPedido(){
        
        String sql = "select importe from partida where folio_fk = "+folio+"";
        float subtotalVar = 0;
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //Aqui se empiezan a acumular los importes de las partidas
                subtotalVar = subtotalVar + Float.parseFloat(rs.getString("importe"));
            } 
            rs.close();
            st.close();
            actualizarSubtotalPedido(subtotalVar);//Update al campo subtotal para ingresar el nuevo monto
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al buscar la partida (sub)" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //calcula y actualiza el subtotal del pedido
    private void actualizarSubtotalPedido(float sub){
        
        float subGrab = sub;
        float anti  = 0f;
        float descuento = 0f;
        
        //Consulta para obtener los valores de: grabados, anticipo y descuento. Se realizaran operaciones
        String sql = "select grabados, anticipo, descuento from pedido where folio = "+folio+"";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //Se le suma el coste de grabados al subtotal
                subGrab = subGrab + Float.parseFloat(rs.getString("grabados"));
                //System.out.println("Grabados: "+rs.getString("grabados"));
                //Se guarda el valor de anticipo para usu posterior
                anti = Float.parseFloat(rs.getString("anticipo"));
                //Se guarda el vlor de descuento para uso posterior
                descuento = Float.parseFloat(rs.getString("descuento"));
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        //Cuando ya se sumaron los grabados y el subtotal ahora se guarda en la base de datos
        sql = "update pedido set subtotal = "+subGrab+" where folio = "+folio+"";
        
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            
            //Ahora paso por parametros el subtotal con grabados, anticipo y descuento
            calcularCostosDeSub(subGrab, anti, descuento);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    //obtiene el porcentaje de iva del pedido
    private float obtenerIva()
    {
        int ivaI = 0;
        float ivaF = 0f;
        String sql = "select porcentajeIVA from pedido where folio = "+folio+"";
        
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                ivaI = Integer.parseInt(rs.getString("porcentajeIVA"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        ivaF = ivaI/100f;//convertimos el iva consultado en un porcentaje flotante
        
        return ivaF;
    }
    
    //calcula y actualiza el total
    private void calcularCostosDeSub(float sub, float anticipo, float descuento){
        
        float total = 0f;
        float iva = obtenerIva();
        float subIva = 0f;
        float rest = 0f;
        
        subIva = sub * iva;
        total = sub + subIva;
        
        rest = total - anticipo;
        rest = rest - descuento;
        
        String sql= "update pedido set total = "+total+", resto = "+rest+" where folio = "+folio+"";
        
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }

    }
    
    
    //Enumerar las partidas existentes con los datos Hojas y De
    private void setHojas(){
        Statement st2;
        String sql = "select idPar from partida where folio_fk = "+folio+"";
        String sql1;
        int contadorRs = 0;
        int idPartida;
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.isAfterLast())
            {
                while(rs.next())
                {
                    contadorRs++;
                    idPartida = rs.getInt("idPar");
                    
                    sql1 = "update partida set hoja = "+contadorRs+" where idPar = "+idPartida+"";
                    st2 = con.createStatement();
                    st2.execute(sql1);
                    st2.close();
                }
            }
            rs.close();
            st.close();
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.isAfterLast())
            {
                while(rs.next())
                {
                    idPartida = rs.getInt("idPar");               

                    sql1 = "update partida set de = "+contadorRs+" where idPar = "+idPartida+"";
                    st2 = con.createStatement();
                    st2.execute(sql1);
                    st2.close();
                }
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar las hojas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }   
    }
    
    private void eliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarPActionPerformed
        //se usa rs2 y st porque hay funciones dentro del while
        Statement st2;
        ResultSet rs2;
        eliminarP.setSelected(false);
        eliminarPartida();
        setTablePartidas(); 
        
        String sql = "select idPar from partida where folio_fk = "+folio+"";
        try
        {
            st2 = con.createStatement();
            rs2 = st2.executeQuery(sql);
            if(rs2.next())//si tiene alguna partida
            {
                establecerSubtotalPedido();
                calculaKgDesperdicioPedido();
                calculaPorcentajeDesperdicioPe();
                calcularCostoTotalPe();
                calculaPyG();
            }
            rs2.close();
            st2.close();
            setHojas();
            JOptionPane.showMessageDialog(null, "Partida eliminada");
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar los datos del pedido" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_eliminarPActionPerformed

    private void prov1ExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prov1ExtKeyTyped
        limitarInsercion(40, evt, prov1Ext);
    }//GEN-LAST:event_prov1ExtKeyTyped

    private void prov2ExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prov2ExtKeyTyped
        limitarInsercion(40, evt, prov2Ext);
    }//GEN-LAST:event_prov2ExtKeyTyped

    private void provImpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_provImpKeyTyped
        limitarInsercion(40, evt, provImp);
    }//GEN-LAST:event_provImpKeyTyped

    private void provBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_provBolKeyTyped
        limitarInsercion(40, evt, provBol);
    }//GEN-LAST:event_provBolKeyTyped

    private void impBusKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impBusKeyTyped
        limitarInsercion(40, evt, impBus);
    }//GEN-LAST:event_impBusKeyTyped

    private void agEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agEActionPerformed
        agE.setSelected(false);
        comprobarVacio();
        kgESt = kgOpExt.getText();
        greniaESt = greExt.getText();
        opESt = listOperadorE.getSelectedItem().toString();
        nMESt = maqExt.getText();
        hIniESt = hrIni.getTimeField().getText();
        fIniESt = fIniExt.getText();
        hFinESt = hrFin.getTimeField().getText();
        fFinESt = fFinExt.getText();
        tMuESt = hrMuertoExt.getTimeField().getText();
        totHESt = totalHrExt.getTimeField().getText();
        exESt = extHrExt.getTimeField().getText();
        costoOpExSt = costoOpExt.getText();
        
        String sql = "insert into operadorExt(costoOpExt, kgUniE, grenia, operador, numMaquina, horaIni, fIni, horaFin, fFin, tiempoMuerto, totalHoras, extras, idExt_fk)" +
                "values("+costoOpExSt+", "+kgESt+","+greniaESt+",'"+opESt+"',"+nMESt+",'"+hIniESt+"', '"+fIniESt+"', '"+hFinESt+"', '"+fFinESt+"', '"+tMuESt+"',"
                + " '"+totHESt+"', '"+exESt+"', "+idEx+")";
        
        try {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            
            sumaKilosEx();
            sumarCostosOperacionales("extrusion", "operadorExt", "costoOpExt", "costoOpTotalExt", "idExt", "idExt_fk", idEx);//hace la sumatoria de los costos operacionales y la actualiza en la base
            sumarGrenias("operadorExt", "grenia", "idExt_fk", "extrusion", "greniaExt", "idExt", idEx);//Sumar y actualiza la suma de grenias de los registros de operador
            actualizarKgDes();
            actualizarPorcentajeDes();
            calculaCostoMaterialTotalExt();
            calculaHrTotalesProceso("operadorExt", "idExt_fk", idEx,"extrusion");
            calculaCostoPartida();//suma los costos de operacion de cada proceso y el costo de material y los inserta en la tabla partida
            calcularCostoUnitarioExt();
            calculaKgDesperdicioPedido();
            calculaPorcentajeDesperdicioPe();
            calcularCostoTotalPe();//calcula e inserta el costo total del pedido
            calculaPyG();
            
            vaciarOpE();
            JOptionPane.showMessageDialog(null, "Se ha guardado el operador en Extrusion: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch(SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el operador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_agEActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void cambioModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambioModActionPerformed
        if(cambioMod.getText().equals("Agregar compras"))
        {
                panMaqExt.setVisible(true);
                panMaqImp.setVisible(true);
                panMaqBol.setVisible(true);
        }
        else if(cambioMod.getText().equals("Agregar producción"))
        {
                panEx.setVisible(true);
                panImp.setVisible(true);
                panBol.setVisible(true);
        }   
        //se cambia el modo de los procesos en la base de datos de produccion o compra a ambos
        try
        {
            String sql = "update partida set modoMat = '"+modoMaterial[2]+"' where idPar = "+modPart.getValueAt(tablaPart.getSelectedRow(), 0).toString()+"";
            st = con.createStatement();
            st.execute(sql);
            st.close();
            cambioMod.setVisible(false);
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "No se registro el cambio de modo" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cambioModActionPerformed

    //Cada que cambia la lista, obtiene el sueldoXHora del indice
    private void listOperadorEItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listOperadorEItemStateChanged
      //primero se obtiene el numero de nombres de operadores
      String sql = "select nombre from operadores";
      int contador = 0;
      try
      {
          st = con.createStatement();
          rs = st.executeQuery(sql);
          while(rs.next())
          {
              contador += 1;
          }
          rs.close();
          st.close();
      }
      catch(SQLException ex)
      {
          ex.printStackTrace();
      }
      
      /*si ya hay un operador en la lista, se obtiene su sueldo, ya que cada que
      cada que se agrega un operador, lo detecta como cambio*/
      if (listOperadorE.getItemCount() == 1)
      {
          sueldoPorHoraE = obtenerSueldoXHora(listOperadorE.getItemAt(0).toString());
      }
      
      /*si ya se termino de llenar la lista, se obtiene el sueldo del operador
      elejido*/
      if (listOperadorE.getItemCount() == contador)
      {
          sueldoPorHoraE = obtenerSueldoXHora(listOperadorE.getSelectedItem().toString());
      }
    }//GEN-LAST:event_listOperadorEItemStateChanged

    //Cada que cambia la lista, obtiene el sueldoXHora del indice
    private void listOperadorIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listOperadorIItemStateChanged
      //primero se obtiene el numero de nombres de operadores  
      String sql = "select nombre from operadores";
      int contador = 0;
      try
      {
          st = con.createStatement();
          rs = st.executeQuery(sql);
          while(rs.next())
          {
              contador += 1;
          }
          rs.close();
          st.close();
      }
      catch(SQLException ex)
      {
          ex.printStackTrace();
      }
      /*si ya hay un operador en la lista, se obtiene su sueldo, ya que cada que
      cada que se agrega un operador, lo detecta como cambio*/
      if (listOperadorI.getItemCount() == 1)
      {
          sueldoPorHoraI = obtenerSueldoXHora(listOperadorI.getItemAt(0).toString());
      }
      /*si ya se termino de llenar la lista, se obtiene el sueldo del operador
      elejido*/
      if (listOperadorI.getItemCount() == contador)
      {
          sueldoPorHoraI = obtenerSueldoXHora(listOperadorI.getSelectedItem().toString());
      }
    }//GEN-LAST:event_listOperadorIItemStateChanged

    //Cada que cambia la lista, obtiene el sueldoXHora del indice
    private void listOperadorBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listOperadorBItemStateChanged
      //primero se obtiene el numero de nombres de operadores  
      String sql = "select nombre from operadores";
      int contador = 0;
      try
      {
          st = con.createStatement();
          rs = st.executeQuery(sql);
          while(rs.next())
          {
              contador += 1;
          }
          rs.close();
          st.close();
      }
      catch(SQLException ex)
      {
          ex.printStackTrace();
      }
      /*si ya hay un operador en la lista, se obtiene su sueldo, ya que cada que
      cada que se agrega un operador, lo detecta como cambio*/
      if (listOperadorB.getItemCount() == 1)
      {
          sueldoPorHoraB = obtenerSueldoXHora(listOperadorB.getItemAt(0).toString());
      }
      /*si ya se termino de llenar la lista, se obtiene el sueldo del operador
      elejido*/
      if (listOperadorB.getItemCount() == contador)
      {
          sueldoPorHoraB = obtenerSueldoXHora(listOperadorB.getSelectedItem().toString());
      }
    }//GEN-LAST:event_listOperadorBItemStateChanged

    //Cada que cambia la lista, obtiene el sueldoXHora del indice
    private void listAyudanteIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listAyudanteIItemStateChanged
      //primero se obtiene el numero de ayudantes  
      String sql = "select ayudante from operadores where ayudante = 1";
      int contador = 0;
      try
      {
          st = con.createStatement();
          rs = st.executeQuery(sql);
          while(rs.next())
          {
              contador += 1;
          }
          rs.close();
          st.close();
      }
      catch(SQLException ex)
      {
          ex.printStackTrace();
      }
      /*si ya hay un ayudante en la lista, se obtiene su sueldo, ya que cada que
      cada que se agrega un ayudante, lo detecta como cambio*/
      if (listAyudanteI.getItemCount() == 1)
      {
          sueldoPorHoraAyudanteI = obtenerSueldoXHora(listAyudanteI.getItemAt(0).toString());
          
      }
      /*si ya se termino de llenar la lista, se obtiene el sueldo del ayudante
      elejido, por alguna extraña razon, se repite este if dos veces*/
      if (listAyudanteI.getItemCount() == contador)
      {
          sueldoPorHoraAyudanteI = obtenerSueldoXHora(listAyudanteI.getSelectedItem().toString());
          
      }
    }//GEN-LAST:event_listAyudanteIItemStateChanged

    private void provImp2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_provImp2KeyTyped
        limitarInsercion(40, evt, provImp2);
    }//GEN-LAST:event_provImp2KeyTyped

    private void porKgImp2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_porKgImp2KeyTyped
        soloFlotantes(evt, porKgImp2);
    }//GEN-LAST:event_porKgImp2KeyTyped

    private void kgImp2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgImp2KeyTyped
        soloFlotantes(evt, kgImp2);
    }//GEN-LAST:event_kgImp2KeyTyped

    
    //establece en 0 los campos de produccion de extrusion
    private void vaciarOpE(){
        Calendar cal = Calendar.getInstance(); 
        kgOpExt.setText("0");
        greExt.setText("0");
        maqExt.setText("0");
        hrIni.getTimeField().setText("00:00:00");
        hrFin.getTimeField().setText("00:00:00");
        extHrExt.getTimeField().setText("00:00:00");
        hrMuertoExt.getTimeField().setText("00:00:00");
        costoOpExt.setText("0.0");
        fIniExt.setSelectedDate(cal);
        fFinExt.setSelectedDate(cal);
        totalHrExt.getTimeField().setText("00:00:00");
    }
    
    //establece en 0 los campos de produccion de impresion
    private void vaciarOpI(){
        Calendar cal = Calendar.getInstance(); 
        kgOpIm.setText("0");
        greImp.setText("0");
        maqImp.setText("0");
        hrIniImp.getTimeField().setText("00:00:00");
        hrFinImp.getTimeField().setText("00:00:00");
        extHrImp.getTimeField().setText("00:00:00");
        hrMuertoImp.getTimeField().setText("00:00:00");
        costoOpImp.setText("0.0");
        fIniImp.setSelectedDate(cal);
        fFinImp.setSelectedDate(cal);
        totalHrImp.getTimeField().setText("00:00:00");
    }
    
    //establece en 0 los campos de produccion de bolseo
    private void vaciarOpB(){
        Calendar cal = Calendar.getInstance(); 
        kgOpBol.setText("0");
        greBol.setText("0");
        suaje.setText("0");
        maqBol.setText("0");
        hrIniBol.getTimeField().setText("00:00:00");
        hrFinBol.getTimeField().setText("00:00:00");
        extBol.getTimeField().setText("00:00:00");
        hrMuertoBol.getTimeField().setText("00:00:00");
        costoOpBol.setText("0.0");
        fIniBol.setSelectedDate(cal);
        fFinBol.setSelectedDate(cal);
        totalHrBol.getTimeField().setText("00:00:00");
    }
   
    //establece en 0 los campos vacios, de produccion o maquila
    public void comprobarVacio(){
        
        //Campos de maquila
        if(proM1.getText().equals("") || proM1.getText().equals(".")){
            proM1.setText("0");
        }
        if(proM2.getText().equals("") || proM2.getText().equals(".")){
            proM2.setText("0");
        }
        if(porKg1Ext.getText().equals("") || porKg1Ext.getText().equals(".")){
            porKg1Ext.setText("0");
        }
        if(porKg2Ext.getText().equals("") || porKg2Ext.getText().equals(".")){
            porKg2Ext.setText("0");
        }
        if(kgImp.getText().equals("") || kgImp.getText().equals(".")){
            kgImp.setText("0");
        }
        if(kgImp2.getText().equals("") || kgImp2.getText().equals(".")){
            kgImp2.setText("0");
        }
        if(porKgImp.getText().equals("") || porKgImp.getText().equals(".")){
            porKgImp.setText("0");
        }
        if(porKgImp2.getText().equals("") || porKgImp2.getText().equals(".")){
            porKgImp2.setText("0");
        }
        if(kgBol.getText().equals("") || kgBol.getText().equals(".")){
            kgBol.setText("0");
        }
        if(pzsBol.getText().equals("")){
            pzsBol.setText("0");
        }
        if(porKgBol.getText().equals("") || porKgBol.getText().equals(".")){
            porKgBol.setText("0");
        }
        
        //Campos de operadores
        if(greExt.getText().equals("") || greExt.getText().equals(".")){
            greExt.setText("0");
        } 
        if(maqExt.getText().equals("")){
            maqExt.setText("0");
        } 
        if(kgOpExt.getText().equals("") || kgOpExt.getText().equals(".")){
            kgOpExt.setText("0");
        }
        if(costoOpExt.getText().equals("") || costoOpExt.getText().equals(".")){
            costoOpExt.setText("0");
        }
        
        if(greImp.getText().equals("") || greImp.getText().equals(".")){
            greImp.setText("0");
        } 
        if(maqImp.getText().equals("")){
            maqImp.setText("0");
        } 
        if(kgOpIm.getText().equals("") || kgOpIm.getText().equals(".")){
            kgOpIm.setText("0");
        } 
        if(costoOpImp.getText().equals("") || costoOpImp.getText().equals(".")){
            costoOpImp.setText("0");
        }
        
        if(greBol.getText().equals("") || greBol.getText().equals(".")){
            greBol.setText("0");
        } 
        if(maqBol.getText().equals("")){
            maqBol.setText("0");
        } 
        if(kgOpBol.getText().equals("") || kgOpBol.getText().equals(".")){
            kgOpBol.setText("0");
        } 
        if(suaje.getText().equals("") || suaje.getText().equals(".")){
            suaje.setText("0");
        } 
        if(costoOpBol.getText().equals("") || costoOpBol.getText().equals(".")){
            costoOpBol.setText("0");
        }
    }
    
    //devuelve la maquina utilizadas en impresion
    private int obtieneMaquina()
    {
        int maquina;
        try
        {
            maquina = Integer.parseInt(maqImp.getText());
        }
        catch(Exception ex)
        {
            maquina = 1;
        }
        return maquina;
    }
    
    //solo es de muestra, no afecta formulas
    private void calculaHrTotalesProceso(String tablaOperador, String idProcesoForaneo, int idProcesoGlobal, String tablaProceso)
    {
        String horaSt = "0";
        int horaInt = 0;
        int minInt = 0;
        int sumatoriaHr = 0;
        int sumatoriaMin = 0;
        
        String sql = "select totalHoras from "+tablaOperador+" where "+idProcesoForaneo+" = "+idProcesoGlobal+"";
        try
        {
           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next())
           {
               //para utilizar el .substring se debe anotrar la posicion en la que empieza y una posicion despues de donde termina
               horaSt = rs.getString("totalHoras");
               horaInt = Integer.valueOf(horaSt.substring(0, 2));
               minInt = Integer.valueOf(horaSt.substring(3, 5));
               sumatoriaMin+=minInt;
               if(sumatoriaMin>59)
               {
                   while(sumatoriaMin>59)
                   {
                       sumatoriaHr+=1;
                       sumatoriaMin -= 60;
                   }
               }
               sumatoriaHr+=horaInt;
           }
           rs.close();
           st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        horaSt = Integer.toString(sumatoriaHr) + ":" + Integer.toString(sumatoriaMin);
        
        sql = "update "+tablaProceso+" set hrTotalesPar = '"+horaSt+"' where idPar_fk = "+idPart+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    //establece todo en 0 y reduce la ventana a la mitad, se usa en Pedido y Principal
    public void vacearComponentes(){
        
        greExt.setText("");
        greImp.setText("");
        greBol.setText("");
        maqExt.setText("");
        maqImp.setText("");
        maqBol.setText("");
        kgOpExt.setText("");
        kgOpIm.setText("");
        kgOpBol.setText("");
        hrIni.getTimeField().setText("00:00:00");
        hrIniImp.getTimeField().setText("00:00:00");
        hrIniBol.getTimeField().setText("00:00:00");
        hrFin.getTimeField().setText("00:00:00");
        hrFinImp.getTimeField().setText("00:00:00");
        hrIniBol.getTimeField().setText("00:00:00");
        hrMuertoExt.getTimeField().setText("00:00:00");
        hrMuertoImp.getTimeField().setText("00:00:00");
        hrMuertoBol.getTimeField().setText("00:00:00");
        totalHrExt.getTimeField().setText("00:00:00");
        totalHrImp.getTimeField().setText("00:00:00");
        totalHrBol.getTimeField().setText("00:00:00");
        extHrExt.getTimeField().setText("00:00:00");
        extHrImp.getTimeField().setText("00:00:00");
        extBol.getTimeField().setText("00:00:00");
        costoOpExt.setText("");
        costoOpImp.setText("");
        costoOpBol.setText("");
        
        suaje.setText("");
        impBus.setText("Nombre Impresion");
        foVis.setText("");
        idPartida.setText("");
        
        prov1Ext.setText("");
        prov2Ext.setText("");
        porKg1Ext.setText("");
        porKg2Ext.setText("");
        proM1.setText("");
        proM2.setText("");
        provImp.setText("");
        provImp2.setText("");
        provBol.setText("");
        porKgImp.setText("");
        porKgImp2.setText("");
        porKgBol.setText("");
        kgImp.setText("");
        kgImp2.setText("");
        kgBol.setText("");
        pzsBol.setText("");
        
        modPed.setRowCount(0);
        modPart.setRowCount(0);
        
        generarPro.setEnabled(false);
        eliminarP.setEnabled(false);
        agE.setEnabled(false);
        agI.setEnabled(false);
        agB.setEnabled(false);
        gdEx.setEnabled(false);
        gdIm.setEnabled(false);
        gdBo.setEnabled(false);
        
        panMaqExt.setVisible(false);
        panMaqImp.setVisible(false);
        panMaqBol.setVisible(false);  
        paPro.setVisible(false);
        
        this.setSize(new Dimension(WD/2, HG));
        this.setLocationRelativeTo(null);
        
    }
    
    /*COSTO DE OPERACION*/
    //devuelve el sueldo por hora de cierto operador
    private float obtenerSueldoXHora(String nombre)
    {
        Statement st7;
        ResultSet rs7;
        Float sueldo = 0f;
        String sql = "select sueldo_hr from operadores where nombre = '"+nombre+"'";
        try
        {
            st7 = con.createStatement();
            rs7 = st7.executeQuery(sql);
            while(rs7.next())
            {
                sueldo = Float.parseFloat(rs7.getString("sueldo_hr"));
            }
            rs7.close();
            st7.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return sueldo;
    }
    
    //hace la sumatoria de los costos operacionales y la actualiza en la base
    private void sumarCostosOperacionales(String tablaProduccion, String tablaOperador, String costoOperacionProceso, String costoOperacionTotalProceso, String nombreIdProcesos, String nombreIdForaneoProcesos, int idGlobalProceso){
        
        float costoTotal = 0f;
        String sql = "select "+costoOperacionProceso+" from "+tablaOperador+" "
                + "where "+nombreIdForaneoProcesos+" = "+idGlobalProceso+"";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoTotal = costoTotal + Float.parseFloat(rs.getString(costoOperacionProceso));
            }
            rs.close();
            st.close();
            
            sql = "update "+tablaProduccion+" set "+costoOperacionTotalProceso+" = "
                    + ""+costoTotal+" where "+nombreIdProcesos+" = "+idGlobalProceso+"";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /*KG DESPERDICIO PARTIDA*/
    //Devuelve la suma de los materiales 1 y 2 de compra de material de extrusion por partida
    private float queryForPesosMaterialPartida(int idPartida){
        
        String sql = "select pocM1,pocM2 from extrusion where idPar_fk = "+idPartida+"";
        float materiales = 0f;
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                materiales = materiales + Float.parseFloat(rs.getString("pocM1"));
                materiales = materiales + Float.parseFloat(rs.getString("pocM2"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return materiales;
    }
    
    //Devuelve la sumatoria de la grenia de extrusion por partida
    private float queryForGreniaExtPartida(int idPartida){
        float grenia = 0f;
        String sql = "select grenia from operadorExt where idExt_fk = "+idPartida+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                grenia = grenia + Float.parseFloat(rs.getString("grenia"));
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return grenia;
    }
    
    //Devuelve los kg producidos y comprados de bolseo por partida
    private float queryForKgTotalesBolPartida(int idPartida){
        float total = 0f, producido = 0f, comprado = 0f;
        String sql = "select produccion from bolseo where idPar_fk = "+idPartida+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                comprado = comprado + Float.parseFloat(rs.getString("produccion"));
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        //hace la sumatoria de lo producido
        sql = "select kgUniB from operadorBol where idBol_fk = "+idBo+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                producido = producido + Float.parseFloat(rs.getString("kgUniB"));
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        total = comprado + producido;
        return total;
    }
    
    //calcula y actualiza los kg de desperdicio en la partida
    private void actualizarKgDes(){
        float KgDesperdicio = 0f;
        float materialesComprados = queryForPesosMaterialPartida(idPart);
        float materialesProducidos = 0f;//usado en caso de que no haya maquila
        float grenia = queryForGreniaExtPartida(idPart);
        float kgFinalesBolseo = queryForKgTotalesBolPartida(idPart);
        //si no hay material 1 y 2 se hace la sumatoria de lo producido.
        if(materialesComprados == 0)
        {
            String sql = "select kgUniE from operadorExt where idExt_fk = "+idEx+"";
            try
            {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    materialesProducidos += Float.parseFloat(rs.getString("kgUniE"));
                }
                rs.close();
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
            KgDesperdicio = (materialesProducidos + grenia) - kgFinalesBolseo;
        }
        else
        {
            KgDesperdicio = (materialesComprados + grenia) - kgFinalesBolseo;
        }
        
        String sql = "update partida set kgDesperdicio = "+KgDesperdicio+" where idPar = "+idPart+"";
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } 
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    /*PORCENTAJE DE DESPERDICIO DE PARTIDA*/
    //Calcula y actualiza el porcentage de desperdicio
    private void actualizarPorcentajeDes(){
        float kgDes = 0;
        String sql = "select kgDesperdicio from partida where idPar = "+idPart+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                kgDes = Float.parseFloat(rs.getString("kgDesperdicio"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        float materiales = 0;//maquila de extrusion
        materiales = queryForPesosMaterialPartida(idPart);
        
        //si no hubo material 1 y 2 se hace la sumatoria de lo producido
        if(materiales == 0)
        {
            sql = "select kgUniE from operadorExt where idExt_fk = "+idEx+"";
            try
            {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    materiales += Float.parseFloat(rs.getString("kgUniE"));
                }
                rs.close();
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
        }
        
        float porcentajeDesp = 0;
        if(materiales>0)
        {
            porcentajeDesp = kgDes / materiales;
        }
        
        sql = "update partida set porcentajeDesp = "+porcentajeDesp+" where idPar = "+idPart+"";
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } 
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
          
    }
    
    /*COSTO DE MATERIAL*/
    /*NOTA: solo es de extrusion*/
    //calcula y devuelve el costo  de los materiales comprados
    float calculaCostoMaterialMaquilaExt()
    {
        String sql = "select pocM1,pocM2,precioKg1,precioKg2 from extrusion where idExt = "+idEx+"";
        float peso1 = 0f,peso2 = 0f,precio1 = 0f,precio2 = 0f,total = 0f;
        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                peso1 = Float.parseFloat(rs.getString("pocM1"));
                peso2 = Float.parseFloat(rs.getString("pocM2"));
                precio1 = Float.parseFloat(rs.getString("precioKg1"));
                precio2 = Float.parseFloat(rs.getString("precioKg2"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }  
        total = (peso1*precio1)+(peso2*precio2);
        return total;
    }
    
    //consulta y devuelve el costo de un tipo de material
    private float consultaCostoMaterial(String tipoIngresado)
    {
        float costo = 0f;//costo de prueba
        String sql = "select precio from costosMaterial where tipo = '"+tipoIngresado+"'";
        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costo = Float.parseFloat(rs.getString("precio"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return costo;
    }
    
    //calcula y devuelve el costo  de los materiales producidos
    float calculaCostoMaterialProducidoExt()
    {
        String sql = "select kgUniE, grenia from operadorExt where idExt_fk = "+idEx+"";
        float pesoP = 0f,pesoGrenia = 0f, costoMaterial= 0f,costoCalculado = 0f,sumatoriaCosto = 0f;
        String tipo = listaMat.getSelectedItem().toString();
        costoMaterial = consultaCostoMaterial(tipo);
        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                pesoP = Float.parseFloat(rs.getString("kgUniE"));
                pesoGrenia = Float.parseFloat(rs.getString("grenia"));
                //calcula el costo de lo producido mas la greña.
                costoCalculado = (pesoP+pesoGrenia)*costoMaterial;
                //hace la sumatoria por cada registro de produccion de la partida.
                sumatoriaCosto = sumatoriaCosto + costoCalculado;
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return sumatoriaCosto;
    }
    
    //suma los costos de la maquila y lo producido y lo inserta en partida
    void calculaCostoMaterialTotalExt()
    {
        float costoMaq = calculaCostoMaterialMaquilaExt();
        float costoPro = calculaCostoMaterialProducidoExt();
        float suma = 0;
        suma = costoMaq + costoPro;
        String sql = "update partida set costoMaterialTotal = "+suma+" where idPar = "+idPart+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }   
    }
    
    /*COSTO DE PARTIDA*/
    //suma los costos de operacion de cada proceso y el costo de material y los inserta en la tabla partida
    void calculaCostoPartida()
    {
        float costoExt = 0f, costoImp = 0f, costoBol = 0f, costoMat = 0f, suma = 0f;
        String sql = "select costoOpTotalExt from extrusion where idPar_fk = "+idPart+"";
        try
        {
            st  = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
               costoExt = costoExt + Float.parseFloat(rs.getString("costoOpTotalExt"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        } 
        
        sql = "select costoOpTotalImp from impreso where idPar_fk = "+idPart+"";
        try
        {
            st  = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoImp = costoImp + Float.parseFloat(rs.getString("costoOpTotalImp"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        } 
        
        sql = "select costoOpTotalBol from bolseo where idPar_fk = "+idPart+"";
        try
        {
            st  = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoBol = costoBol + Float.parseFloat(rs.getString("costoOpTotalBol"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        } 
        
        sql = "select costoMaterialTotal from partida where idPar = "+idPart+"";
        try
        {
            st  = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoMat = Float.parseFloat(rs.getString("costoMaterialTotal"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        } 
        
        suma = costoExt + costoImp + costoBol + costoMat;
        
        sql = "update partida set costoPartida = "+suma+" where idPar = "+idPart+"";
        try
        {
            st  = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al calcular el costo de la partida","Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } 
    }
    
    /*COSTO UNITARIO*/
    //devuelve la sumatoria de los pesos de material producido y comprado para extrusion
    private float sumatoriaMaquilaProduccionExtrusion()
    {
        float sumatoriaT = 0f;
        String sql = "select pocM1, pocM2 from extrusion where idExt = "+idEx+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                sumatoriaT = sumatoriaT + Float.parseFloat(rs.getString("pocM1")) + Float.parseFloat(rs.getString("pocM2"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        sql = "select kgUniE from operadorExt where idExt_fk = "+idEx+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                sumatoriaT = sumatoriaT + Float.parseFloat(rs.getString("kgUniE"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        return sumatoriaT;
    }
    
    //calcula e inserta el costo unitario de extrusion
    private void calcularCostoUnitarioExt(){
        float costo = 0f, kgtotales = 0f, resultado = 0f;
        String sql = "select costoOpTotalExt from extrusion where idExt = "+idEx+"";
        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costo = Float.parseFloat(rs.getString("costoOpTotalExt"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        } 
        
        kgtotales = sumatoriaMaquilaProduccionExtrusion();
        if(costo != 0 && kgtotales != 0)
        {
            resultado = costo / kgtotales;
        }
        
        sql = "update extrusion set costoUnitarioExt = "+resultado+" where idExt = "+idEx+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        } 
    }

    //devuelve la sumatoria de los pesos de material producido y comprado para impreso
    private float sumatoriaMaquilaProduccionImpreso(){
        String sql = "select produccion,produccion2 from impreso where idImp = "+idIm+"";
        float matC = 0f;
        float matP = 0f;
        float materiales = 0f;
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //sumatoria del material comprado
                matC = Float.parseFloat(rs.getString("produccion"))+Float.parseFloat(rs.getString("produccion2"));
                materiales = materiales + matC;
            }          
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        sql = "select kgUniI from operadorImp where idImp_fk = "+idIm+"";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                matP = Float.parseFloat(rs.getString("kgUniI"));
                 //sumatoria de material producido
                materiales = materiales + matP;
            }          
            rs.close();
            st.close();
        } 
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        return materiales;
    }
    
    //calcula el costo unitario de impreso
    private void calcularCostoUnitarioImp()
    {
        float costo = 0f, kgtotales = 0, resultado = 0f;
        String sql = "select costoOpTotalImp from impreso where idImp = "+idIm+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costo = Float.parseFloat(rs.getString("costoOpTotalImp"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        } 
        
        kgtotales = sumatoriaMaquilaProduccionImpreso();
        
        if(costo != 0 && kgtotales != 0)
        {
            resultado = costo / kgtotales;
        }
        
        sql = "update impreso set costoUnitarioImp = "+resultado+" where idImp = "+idIm+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }  
    }

    //devuelve la sumatoria de los pesos de material producido y comprado para bolseo
    private float sumatoriaMaquilaProduccionBolseo(){
        String sql = "select produccion from bolseo where idBol = "+idBo+"";
        float materiales = 0f;
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //sumatoria del material comprado
                materiales = Float.parseFloat(rs.getString("produccion"));
            }          
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        sql = "select kgUniB from operadorBol where idBol_fk = "+idBo+"";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //sumatoria de material producido
                materiales = materiales + Float.parseFloat(rs.getString("kgUniB"));
            }          
            rs.close();
            st.close();
            } 
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
        return materiales;
    }
    
    //calcula el costo unitario de bolseo
    private void calcularCostoUnitarioBol(){
        float costo = 0f, kgtotales = 0, resultado = 0f;
        String sql = "select costoOpTotalBol from bolseo where idBol = "+idBo+"";
        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costo = Float.parseFloat(rs.getString("costoOpTotalBol"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        } 
        
        kgtotales = sumatoriaMaquilaProduccionBolseo();
        
        if(costo != 0 && kgtotales != 0)
        {
            resultado = costo / kgtotales;
        }
        
        sql = "update bolseo set costoUnitarioBol = "+resultado+" where idBol = "+idBo+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }  
    }
    
    /*KG DESPERDICIO PEDIDO*/
    private void calculaKgDesperdicioPedido()
    {
        float sumatoria = 0f;
        String sql = "select kgDesperdicio from partida where folio_fk = "+folio+" and kgDesperdicio is not null";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                sumatoria = sumatoria + Float.parseFloat(rs.getString("kgDesperdicio"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        sql = "update pedido set kgDesperdicioPe = "+sumatoria+" where folio = "+folio+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
   
    /*PORCENTAJE DESPERDICIO PEDIDO*/
    //calcula, devuelve y actualiza la sumatoria de los kg comprados o producidos de extrusion en pedido
    private float sumaMaterialesPedido()
    {
        float sumatoria = 0f;
        float producido = 0f, comprado = 0f;
        int idPartida = 0;
        String sql2 = "";
        String sql3 = "";
        Statement st2, st3;
        ResultSet rs2, rs3;
        int idEx2 = 0;
        String sql = "select idPar from partida where folio_fk = "+folio+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                idPartida = Integer.parseInt(rs.getString("idPar"));
                
                sql2 = "select pocM1,pocM2 from extrusion where idPar_fk = "+idPartida+"";
                try
                {
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(sql2);
                    while(rs2.next())
                    {
                        comprado = comprado + (Float.parseFloat(rs2.getString("pocM1"))+Float.parseFloat(rs2.getString("pocM2")));
                    }
                    rs2.close();
                    st2.close();
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
               
                sql2 = "select idExt from extrusion where idPar_fk = "+idPartida+"";
                try
                {
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(sql2);
                    while(rs2.next())
                    {
                        idEx2 = Integer.parseInt(rs2.getString("idExt"));
                        
                        sql3 = "select kgUniE from operadorExt where idExt_fk = "+idEx2+"";
                        try
                        {
                            st3 = con.createStatement();
                            rs3 = st3.executeQuery(sql3);
                            while(rs3.next())
                            {
                                producido = producido + Float.parseFloat(rs3.getString("kgUniE"));
                            }
                            rs3.close();
                            st3.close();
                        }
                        catch(SQLException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                    rs2.close();
                    st2.close();
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        sql = "update pedido set matComPe = "+comprado+",matProPe = "+producido+" where folio = "+folio+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        sumatoria = comprado + producido;
        return sumatoria;
    }
    
    //calcula y actualiza el porcentaje de desperdicio del pedido
    private void calculaPorcentajeDesperdicioPe()
    {
        float kgDesp = 0f, porcentaje = 0f, kgProCo = 0f;
        kgProCo = sumaMaterialesPedido();
        
        String sql = "select kgDesperdicioPe from pedido where folio="+folio+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                kgDesp = Float.parseFloat(rs.getString("kgDesperdicioPe"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        if(kgDesp != 0 && kgProCo != 0)
        {
            porcentaje = kgDesp/kgProCo;
        }
        
        sql = "update pedido set porcentajeDespPe = "+porcentaje+" where folio = "+folio+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /*COSTO TOTAL*/
    /*calcula e inserta el costo total del pedido, no suma las tintas
    pero tiene una validacion para eso*/ 
    private void calcularCostoTotalPe()
    {
        //busca si ya se habia registrado tintas antes
        String sql = "select seSumoAPedido from tintas where folio_fk = "+folio+"";
        int yaSeSumo = 1;
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                yaSeSumo =  Integer.parseInt(rs.getString("seSumoAPedido"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        if(yaSeSumo == 0)//si no, hace la sumatoria de costos de partidas
        {
            sql = "select costoPartida from partida where folio_fk = "+folio+" and costoPartida is not null";
            float sumatoria = 0f;
            try
            {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    sumatoria = sumatoria + Float.parseFloat(rs.getString("costoPartida"));
                }
                rs.close();
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
            
            sql = "update pedido set costoTotal  = "+sumatoria+" where folio = "+folio+"";
            try
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
        }
        else//si ya se habia registrado tintas antes, el costoTotal se sobreescribe
            //sin el de tintas, asi que se lo volvemos a sumar y le indicamos
            //al usuario que vaya a la modificacion de tintas
        {
            sql = "select costoPartida from partida where folio_fk = "+folio+" and costoPartida is not null";
            float sumatoria = 0f;
            try
            {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    sumatoria = sumatoria + Float.parseFloat(rs.getString("costoPartida"));
                }
                rs.close();
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }

            sql = "select costoDeTintas from tintas where folio_fk = "+folio+"";
            try
            {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    sumatoria = sumatoria + Float.parseFloat(rs.getString("costoDeTintas"));
                }
                rs.close();
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }

            sql = "update pedido set costoTotal  = "+sumatoria+" where folio = "+folio+"";
            try
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Debes ir a la seccion de tintas y volver a presionar el boton de guardar cambios, ya que la tintas deben de registrarse despues de los reportes","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /*PERDIDAS Y GANANCIAS*/
    //calcula los kg finales del pedido
    private float calculaKgFinalesPedido(int folio)
    {
        Statement st2, st3, st4, st5;
        ResultSet rs2, rs3, rs4, rs5;
        float sumatoriaPartida = 0f;
        float sumatoriaPedido = 0f;
        String sql2 = "select idPar from partida where folio_fk = "+folio+"";//obtiene el folio de cada partida
        try
        {
            st2 = con.createStatement();
            rs2 = st2.executeQuery(sql2);
            while(rs2.next())
            {
                sumatoriaPartida = 0;
                int idPart2 =   Integer.parseInt(rs2.getString("idPar"));
                
                if(sumatoriaPartida <= 0)//si aun no se hace la sumatoria de una partida anterior, entra
                {
                    String sql3 = "select produccion from bolseo where idPar_fk = "+idPart2+"";//obtiene lo comprado de bolseo
                    try
                    {
                        st3 = con.createStatement();
                        rs3 = st3.executeQuery(sql3);
                        while(rs3.next())
                        {
                            float comprado = Float.parseFloat(rs3.getString("produccion"));
                            if(comprado > 0)//verifica que lo comprado no sea null
                            {
                                sumatoriaPartida = sumatoriaPartida + comprado;//hace la sumatoria de lo comprado de bolseo 
                            }
                    
                            String sql4 = "select idBol from bolseo where idPar_fk = "+idPart2+"";//selecciona el id de bolseo del proceso
                            try
                            {
                                st4 = con.createStatement();
                                rs4 = st4.executeQuery(sql4);
                                while(rs4.next())
                                {
                                    int idBo2 = Integer.parseInt(rs4.getString("idBol"));
                                    
                                    String sql5 = "select kgUniB from operadorBol where idBol_fk = "+idBo2+"";//selecciona lo producido de bolseo
                                    try
                                    {
                                        st5 = con.createStatement();
                                        rs5 = st5.executeQuery(sql5);
                                        while(rs5.next())
                                        {
                                            float producido = Float.parseFloat(rs5.getString("kgUniB"));
                                            if(producido > 0)//verifica que lo producido no sea null
                                            {
                                                sumatoriaPartida = sumatoriaPartida + producido;//hace la sumatoria de lo producido
                                            }
                                        }
                                        rs5.close();
                                        st5.close();
                                    }
                                    catch(SQLException ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                                rs4.close();
                                st4.close();
                            }
                            catch(SQLException ex)
                            {
                                ex.printStackTrace();
                            }
                    
                        }
                        rs3.close();
                        st3.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }
                if(sumatoriaPartida <= 0)//si aun no se hace la sumatoria de una partida anterior, entra
                {
                    String sql3 = "select produccion from impreso where idPar_fk = "+idPart2+"";//obtiene lo comprado de impreso
                    try
                    {
                        st3 = con.createStatement();
                        rs3 = st3.executeQuery(sql3);
                        while(rs3.next())
                        {
                            float comprado = Float.parseFloat(rs3.getString("produccion"));
                            if(comprado > 0)//verifica que lo comprado no sea null
                            {
                                sumatoriaPartida = sumatoriaPartida + comprado;//hace la sumatoria de lo comprado de impreso 
                            }
                    
                            String sql4 = "select idImp from impreso where idPar_fk = "+idPart2+"";//selecciona el id de impreso del proceso
                            try
                            {
                                st4 = con.createStatement();
                                rs4 = st4.executeQuery(sql4);
                                while(rs4.next())
                                {
                                    int idIm2 = Integer.parseInt(rs4.getString("idImp"));
                                    
                                    String sql5 = "select kgUniI from operadorImp where idImp_fk = "+idIm2+"";//selecciona lo producido de impreso
                                    try
                                    {
                                        st5 = con.createStatement();
                                        rs5 = st5.executeQuery(sql5);
                                        while(rs5.next())
                                        {
                                            float producido = Float.parseFloat(rs5.getString("kgUniI"));
                                            if(producido > 0)//verifica que lo producido no sea null
                                            {
                                                sumatoriaPartida = sumatoriaPartida + producido;//hace la sumatoria de lo producido
                                            }
                                        }
                                        rs5.close();
                                        st5.close();
                                    }
                                    catch(SQLException ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                                rs4.close();
                                st4.close();
                            }
                            catch(SQLException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        rs3.close();
                        st3.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }
                if(sumatoriaPartida <= 0)//si aun no se hace la sumatoria de una partida anterior, entra
                {
                    String sql3 = "select pocM1,pocM2 from extrusion where idPar_fk = "+idPart2+"";//obtiene lo comprado de extrusion
                    try
                    {
                        st3 = con.createStatement();
                        rs3 = st3.executeQuery(sql3);
                        while(rs3.next())
                        {
                            float comprado1 = Float.parseFloat(rs3.getString("pocM1"));
                            float comprado2 = Float.parseFloat(rs3.getString("pocM2"));
                            if(comprado1 > 0 || comprado2 > 0)//verifica que lo comprado no sea null
                            {
                                sumatoriaPartida = sumatoriaPartida + (comprado1 + comprado2);//hace la sumatoria de lo comprado de extrusion 
                            }
                    
                            String sql4 = "select idExt from extrusion where idPar_fk = "+idPart2+"";//selecciona el id de extrusion del proceso
                            try
                            {
                                st4 = con.createStatement();
                                rs4 = st4.executeQuery(sql4);
                                while(rs4.next())
                                {
                                    int idEx2 = Integer.parseInt(rs4.getString("idExt"));
                                    
                                    String sql5 = "select kgUniE from operadorExt where idExt_fk = "+idEx2+"";//selecciona lo producido de extrusion
                                    try
                                    {
                                        st5 = con.createStatement();
                                        rs5 = st5.executeQuery(sql5);
                                        while(rs5.next())
                                        {
                                            float producido = Float.parseFloat(rs5.getString("kgUniE"));
                                            if(producido > 0)//verifica que lo producido no sea null
                                            {
                                                sumatoriaPartida = sumatoriaPartida + producido;//hace la sumatoria de lo producido
                                            }
                                        }
                                        rs5.close();
                                        st5.close();
                                    }
                                    catch(SQLException ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                                rs4.close();
                                st4.close();
                            }
                            catch(SQLException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        rs3.close();
                        st3.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            sumatoriaPedido += sumatoriaPartida;
            }
            rs2.close();
            st2.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
                
        return sumatoriaPedido;
    }
    
    //calcula los gastos fijos por kg del pedido
    private float calculaGfKg()
    {
        float gfkg = 0f;//gastos fijos x kg
        float gfr = 0f;//gatos fijos del renago
        float sumatoriaKGRango = 0f;
        String sql = "select gastosFijos,kgFinalesRango from pedido where folio = "+folio+" and gastosFijos is not null and kgFinalesRango is not null";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                gfr = Float.parseFloat(rs.getString("gastosFijos"));
                sumatoriaKGRango = Float.parseFloat(rs.getString("kgFinalesRango"));//esto se inserta cuando se insertan los gastos fijos de rango
                if(sumatoriaKGRango != 0)
                {
                    gfkg = gfr / sumatoriaKGRango;
                }    
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        return gfkg;
    }
    
    //calcula y actualiza las perdidas y ganancias
    private void calculaPyG()
    {
        float subtotal = 0f, costoTotal = 0f, descuento = 0f;
        float PyG = 0;
        String sql2 = "select fTermino from pedido where folio = "+folio+"";//busca si ya fueron terminados los pedidos
        String sql3 = "";
        //no usar de st a st5
        Statement st6, st7;
        ResultSet rs6, rs7;
        try
        {
            st6 = con.createStatement();
            rs6 = st6.executeQuery(sql2);
            while(rs6.next())
            {
                if(rs6.getString("fTermino").equals("2018-01-01") == false)//solo lo hara con pedidos que ya hayan sido terminados
                {
                    sql3 = "select subtotal, costoTotal, descuento from pedido where folio = "+folio+"";
                    try
                    {
                        st7 = con.createStatement();
                        rs7 = st7.executeQuery(sql3);
                        while(rs7.next())
                        {
                            subtotal = Float.parseFloat(rs7.getString("subtotal"));
                            costoTotal = Float.parseFloat(rs7.getString("costoTotal"));
                            descuento = Float.parseFloat(rs7.getString("descuento"));
                        }
                        rs7.close();
                        st7.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                    
                    float kgFnPe = calculaKgFinalesPedido(folio);
                    float gf = calculaGfKg();
                    PyG = subtotal - costoTotal - descuento - ( kgFnPe * gf);
                }
            }
            rs6.close();
            st6.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        sql2 = "update pedido set perdidasYGanancias = "+PyG+" where folio = "+folio+"";
        try
        {
            st6 = con.createStatement();
            st6.execute(sql2);
            st6.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agB;
    private javax.swing.JButton agE;
    private javax.swing.JButton agI;
    private javax.swing.JButton busquedaImp;
    private javax.swing.JButton cambioMod;
    private javax.swing.JTextField costoOpBol;
    private javax.swing.JTextField costoOpExt;
    private javax.swing.JTextField costoOpImp;
    private javax.swing.JButton eliminarP;
    private lu.tudor.santec.jtimechooser.JTimeChooser extBol;
    private lu.tudor.santec.jtimechooser.JTimeChooser extHrExt;
    private lu.tudor.santec.jtimechooser.JTimeChooser extHrImp;
    private datechooser.beans.DateChooserCombo fFinBol;
    private datechooser.beans.DateChooserCombo fFinExt;
    private datechooser.beans.DateChooserCombo fFinImp;
    private datechooser.beans.DateChooserCombo fIniBol;
    private datechooser.beans.DateChooserCombo fIniExt;
    private datechooser.beans.DateChooserCombo fIniImp;
    private javax.swing.JTextField foVis;
    private javax.swing.JToggleButton gdBo;
    private javax.swing.JToggleButton gdEx;
    private javax.swing.JToggleButton gdIm;
    private javax.swing.JToggleButton generarPro;
    private javax.swing.JTextField greBol;
    private javax.swing.JTextField greExt;
    private javax.swing.JTextField greImp;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrFin;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrFinBol;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrFinImp;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrIni;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrIniBol;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrIniImp;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrMuertoBol;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrMuertoExt;
    private lu.tudor.santec.jtimechooser.JTimeChooser hrMuertoImp;
    private javax.swing.JTextField idPartida;
    private javax.swing.JTextField impBus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField kgBol;
    private javax.swing.JTextField kgImp;
    private javax.swing.JTextField kgImp2;
    private javax.swing.JTextField kgOpBol;
    private javax.swing.JTextField kgOpExt;
    private javax.swing.JTextField kgOpIm;
    private javax.swing.JLabel labelxd;
    private javax.swing.JComboBox listAyudanteI;
    private javax.swing.JComboBox listOperadorB;
    private javax.swing.JComboBox listOperadorE;
    private javax.swing.JComboBox listOperadorI;
    private javax.swing.JComboBox listaMat;
    private javax.swing.JTextField maqBol;
    private javax.swing.JTextField maqExt;
    private javax.swing.JTextField maqImp;
    private javax.swing.JTabbedPane paPro;
    private javax.swing.JPanel panBol;
    private javax.swing.JPanel panEx;
    private javax.swing.JPanel panImp;
    private javax.swing.JPanel panMaqBol;
    private javax.swing.JPanel panMaqExt;
    private javax.swing.JPanel panMaqImp;
    private javax.swing.JTextField porKg1Ext;
    private javax.swing.JTextField porKg2Ext;
    private javax.swing.JTextField porKgBol;
    private javax.swing.JTextField porKgImp;
    private javax.swing.JTextField porKgImp2;
    private javax.swing.JTextField proM1;
    private javax.swing.JTextField proM2;
    private javax.swing.JTextField prov1Ext;
    private javax.swing.JTextField prov2Ext;
    private javax.swing.JTextField provBol;
    private javax.swing.JTextField provImp;
    private javax.swing.JTextField provImp2;
    private javax.swing.JTextField pzsBol;
    private javax.swing.JToggleButton regresar;
    private javax.swing.JTextField suaje;
    private javax.swing.JTable tablaPart;
    private javax.swing.JTable tablaPed;
    private lu.tudor.santec.jtimechooser.JTimeChooser totalHrBol;
    private lu.tudor.santec.jtimechooser.JTimeChooser totalHrExt;
    private lu.tudor.santec.jtimechooser.JTimeChooser totalHrImp;
    // End of variables declaration//GEN-END:variables
}





