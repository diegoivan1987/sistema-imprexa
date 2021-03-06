
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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

public class Procesos extends javax.swing.JFrame {
    //medidas de la ventana
    private int WD = 0;
    private int HG = 0;
    
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Connection con;
    ResultSet rs;
    Statement st;
    Pedido pd;
    
    String[] modoMaterial = {"Produccion", "Compra", "Ambos"}; //arreglo con las maneras de generar procesos
    
    //Variables de extrusion
    String pM1St=null, pM2St=null, provE1St=null, precioKgE1St=null, prov2St=null, precioKg2St=null;
    //Variables de Impreso
    String prodISt=null, provI1St=null, precioKgI1St=null, stickySt=null, cDiseSt=null, cGrabSt=null, stcSt=null;
    //Variables de Bolseo
    String  prodBSt=null, prodPzSt=null, provB1St=null, precioKgB1St=null;
    
    
    //Variables de OperadorExtrusion
    String kgESt=null, greniaESt=null, opESt=null, nMESt=null, hIniESt=null, fIniESt=null, hFinESt=null, fFinESt=null, 
            tMuESt=null, totHESt=null, exESt=null, costoOpExSt=null;
    
    //Variables de OperadorImpresion
    String kgISt=null, greniaISt=null, opISt=null, nMISt=null, hIniISt=null, fIniISt=null, hFinISt=null, fFinISt=null, 
            tMuISt=null, totHISt=null, exISt=null, costoOpImSt=null;
    
    //Variables de OperadorBolseo
    String kgBSt=null, greniaBSt=null, suajeBSt=null, opBSt=null, nMBSt=null, hIniBSt=null, fIniBSt=null, hFinBSt=null, fFinBSt=null, 
            tMuBSt=null, totHBSt=null, exBSt=null, costoOpBoSt=null;
    
    //Variables de las tintas
    String t1St=null, pI1St=null, pF1St=null, t2St=null, pI2St=null, pF2St=null, t3St=null, pI3St=null, pF3St=null, 
            t4St=null, pI4St=null, pF4St=null, t5St=null, pI5St=null, pF5St=null, t6St=null, pI6St=null, pF6St=null, 
            iniMezSt=null, finMezSt=null, iniAceSt=null, finAceSt=null, iniRetSt=null, finRetSt=null;
    
    //Vaiables que almacenan claves principales
    int folio = 0;
    int idPart = 0;
    int idEx = 0;
    int idBo = 0;
    int idIm = 0;
    float idPe = 0f;
    int idImPrimera = 0;//Id primaria de impreso para ingresar tintas
    
    DefaultTableModel modPed, modPart;
    JTableHeader thPed, thPart;
    Font fuenteTablas;
    
    ArrayList<String> datosImpreso;
    
    public Procesos(Connection con) {
        
        initComponents();
        
        costoGrab.setEditable(false);//no se podra escribir sobre el recuadro que muestra los costos de grabados
        
        cambioMod.setVisible(false);//se hace invisible el boton para añadir mas campos al generar procesos
        
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.setResizable(false);
        fuenteTablas = new Font("Dialog", Font.BOLD, 12);
        //Tamaño de la pantalla
        WD = this.getSize().width;
        HG = this.getSize().height;
        this.setSize(new Dimension(WD/2, HG));//se establece el tamaño de la pantalla a la mitad
        
        onChangeTextField();//Creacion del listener para el campo de folio
        listenersJTime();//agrupa los listeners de los jtime
        
        //Se desactivan los botones de guardar procesos y de agegar
        savePro.setEnabled(false);
        agE.setEnabled(false);
        agI.setEnabled(false);
        agB.setEnabled(false);
        
        gdEx.setEnabled(false);
        gdIm.setEnabled(false);
        gdBo.setEnabled(false);
        
        btnCostos.setEnabled(false);
        
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
        
        //Se desactivar los textfield del folio y la id de partida
        foVis.setEnabled(false);
        idPartida.setEnabled(false);
        costoOpExt.setEditable(false);
        costoOpImp.setEditable(false);
        costoOpBol.setEditable(false);
       
        paPro.setBackground(Color.LIGHT_GRAY);
        paIm.setBackground(Color.LIGHT_GRAY);
        //se hacen invisibles los paneles de maquila
        panMaqExt.setVisible(false);
        panMaqImp.setVisible(false);
        panMaqBol.setVisible(false);   
        
        paPro.setVisible(false);//se hace invisible el panel que contiene los campos de capturas
        
        totalHrExt.getTimeField().setEditable(false);
        totalHrImp.getTimeField().setEditable(false);
        totalHrBol.getTimeField().setEditable(false);
        
        this.con = con;
        
        estilosTablas();//se les da estilo a las tablas
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paPro = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        panEx = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        opExt = new javax.swing.JTextField();
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
        paIm = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        panImp = new javax.swing.JPanel();
        jLabel134 = new javax.swing.JLabel();
        opImp = new javax.swing.JTextField();
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
        panMaqImp = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        provImp = new javax.swing.JTextField();
        jLabel146 = new javax.swing.JLabel();
        porKgImp = new javax.swing.JTextField();
        kgImp = new javax.swing.JTextField();
        jLabel132 = new javax.swing.JLabel();
        gdIm = new javax.swing.JToggleButton();
        panelCostos = new javax.swing.JPanel();
        costoGrab = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        costoDise = new javax.swing.JTextField();
        stc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnCostos = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel99 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        t1 = new javax.swing.JTextField();
        kgIniT1 = new javax.swing.JTextField();
        kgFinT1 = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        t2 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        kgIniT2 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        kgFinT2 = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        t3 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        kgIniT3 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        kgFinT3 = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        t4 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        kgIniT4 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        kgFinT4 = new javax.swing.JTextField();
        jLabel103 = new javax.swing.JLabel();
        t5 = new javax.swing.JTextField();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        kgIniT5 = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        kgFinT5 = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        t6 = new javax.swing.JTextField();
        jLabel112 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        kgIniT6 = new javax.swing.JTextField();
        jLabel114 = new javax.swing.JLabel();
        kgFinT6 = new javax.swing.JTextField();
        jLabel115 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        mezIni = new javax.swing.JTextField();
        jLabel118 = new javax.swing.JLabel();
        mezFin = new javax.swing.JTextField();
        jLabel119 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        aceIni = new javax.swing.JTextField();
        jLabel122 = new javax.swing.JLabel();
        aceFin = new javax.swing.JTextField();
        jLabel123 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        retIni = new javax.swing.JTextField();
        jLabel126 = new javax.swing.JLabel();
        retFin = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        panBol = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        opBol = new javax.swing.JTextField();
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
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton3 = new javax.swing.JButton();
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
        savePro = new javax.swing.JToggleButton();
        eliminarP = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
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
        paPro.setForeground(new java.awt.Color(0, 0, 0));

        panEx.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel36.setForeground(new java.awt.Color(0, 102, 153));
        jLabel36.setText("Operador:");

        opExt.setForeground(new java.awt.Color(0, 153, 153));
        opExt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                opExtKeyTyped(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panExLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panExLayout.createSequentialGroup()
                                .addComponent(opExt)
                                .addGap(18, 18, 18))
                            .addGroup(panExLayout.createSequentialGroup()
                                .addComponent(kgOpExt, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(141, 141, 141)))
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
                            .addComponent(opExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
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

        jLabel134.setForeground(new java.awt.Color(0, 102, 153));
        jLabel134.setText("Operador:");

        opImp.setForeground(new java.awt.Color(0, 153, 153));
        opImp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                opImpKeyTyped(evt);
            }
        });

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

        javax.swing.GroupLayout panImpLayout = new javax.swing.GroupLayout(panImp);
        panImp.setLayout(panImpLayout);
        panImpLayout.setHorizontalGroup(
            panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panImpLayout.createSequentialGroup()
                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel134)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(opImp)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel133))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel136))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(kgOpIm, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(greImp, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel135)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maqImp, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panImpLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(agI))))
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
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel138)
                                    .addComponent(jLabel140))
                                .addGap(28, 28, 28)
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fFinImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fIniImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(jLabel143)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(extHrImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(costoOpImp, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(13, 13, 13)))
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
                        .addComponent(opImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(kgOpIm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panImpLayout.createSequentialGroup()
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panImpLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panImpLayout.createSequentialGroup()
                                        .addComponent(jLabel136)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fIniImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel138))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panImpLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel137)
                                .addGap(8, 8, 8)))
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fFinImp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel139, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel140, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(extHrImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel143))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(costoOpImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(agI))
                    .addGroup(panImpLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(hrIniImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(hrFinImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel141, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hrMuertoImp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(panImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel142)
                            .addComponent(totalHrImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)))
                .addContainerGap())
        );

        panMaqImp.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        javax.swing.GroupLayout panMaqImpLayout = new javax.swing.GroupLayout(panMaqImp);
        panMaqImp.setLayout(panMaqImpLayout);
        panMaqImpLayout.setHorizontalGroup(
            panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqImpLayout.createSequentialGroup()
                .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMaqImpLayout.createSequentialGroup()
                        .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panMaqImpLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel146)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(porKgImp, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel132)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kgImp, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panMaqImpLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel145)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(provImp, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panMaqImpLayout.createSequentialGroup()
                                .addGap(214, 214, 214)
                                .addComponent(jLabel144)))
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panMaqImpLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(gdIm)))
                .addContainerGap())
        );
        panMaqImpLayout.setVerticalGroup(
            panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMaqImpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel144)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel145)
                    .addComponent(provImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panMaqImpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel146)
                    .addComponent(porKgImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kgImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel132))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(gdIm)
                .addContainerGap())
        );

        panelCostos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        costoGrab.setForeground(new java.awt.Color(0, 153, 153));
        costoGrab.setText("0");
        costoGrab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                costoGrabKeyTyped(evt);
            }
        });

        jLabel31.setForeground(new java.awt.Color(0, 102, 153));
        jLabel31.setText("Costo Grabado: $");

        jLabel32.setForeground(new java.awt.Color(0, 102, 153));
        jLabel32.setText("Costo Diseño: $");

        costoDise.setForeground(new java.awt.Color(0, 153, 153));
        costoDise.setText("0");
        costoDise.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                costoDiseKeyTyped(evt);
            }
        });

        stc.setForeground(new java.awt.Color(0, 153, 153));
        stc.setText("0");
        stc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stcKeyTyped(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 102, 153));
        jLabel7.setText("Sticky (CM): $");

        btnCostos.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btnCostos.setText("Guardar Costos");
        btnCostos.setToolTipText("Guardar solo los costos");
        btnCostos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCostosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCostosLayout = new javax.swing.GroupLayout(panelCostos);
        panelCostos.setLayout(panelCostosLayout);
        panelCostosLayout.setHorizontalGroup(
            panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCostosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(costoGrab, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addGroup(panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(costoDise, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(54, 54, 54)
                .addGroup(panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stc, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCostos, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelCostosLayout.setVerticalGroup(
            panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCostosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCostosLayout.createSequentialGroup()
                        .addGroup(panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCostosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(stc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(costoGrab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(costoDise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnCostos, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panImp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panMaqImp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCostos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCostos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panMaqImp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paIm.addTab("Datos", jPanel9);

        jLabel99.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(0, 102, 153));
        jLabel99.setText("Tinta 1");

        jLabel52.setForeground(new java.awt.Color(0, 102, 153));
        jLabel52.setText("Nombre");

        jLabel54.setForeground(new java.awt.Color(0, 102, 153));
        jLabel54.setText("Inicio");

        jLabel55.setForeground(new java.awt.Color(0, 102, 153));
        jLabel55.setText("Final");

        t1.setForeground(new java.awt.Color(0, 153, 153));
        t1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t1KeyTyped(evt);
            }
        });

        kgIniT1.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT1KeyTyped(evt);
            }
        });

        kgFinT1.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT1KeyTyped(evt);
            }
        });

        jLabel100.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(0, 102, 153));
        jLabel100.setText("Tinta 2");

        t2.setForeground(new java.awt.Color(0, 153, 153));
        t2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t2KeyTyped(evt);
            }
        });

        jLabel56.setForeground(new java.awt.Color(0, 102, 153));
        jLabel56.setText("Nombre");

        jLabel57.setForeground(new java.awt.Color(0, 102, 153));
        jLabel57.setText("Inicio");

        kgIniT2.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT2KeyTyped(evt);
            }
        });

        jLabel58.setForeground(new java.awt.Color(0, 102, 153));
        jLabel58.setText("Final");

        kgFinT2.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT2KeyTyped(evt);
            }
        });

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(0, 102, 153));
        jLabel101.setText("Tinta 3");

        t3.setForeground(new java.awt.Color(0, 153, 153));
        t3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t3KeyTyped(evt);
            }
        });

        jLabel59.setForeground(new java.awt.Color(0, 102, 153));
        jLabel59.setText("Nombre");

        jLabel60.setForeground(new java.awt.Color(0, 102, 153));
        jLabel60.setText("Inicio");

        kgIniT3.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT3KeyTyped(evt);
            }
        });

        jLabel61.setForeground(new java.awt.Color(0, 102, 153));
        jLabel61.setText("Final");

        kgFinT3.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT3KeyTyped(evt);
            }
        });

        jLabel102.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(0, 102, 153));
        jLabel102.setText("Tinta 4");

        t4.setForeground(new java.awt.Color(0, 153, 153));
        t4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t4KeyTyped(evt);
            }
        });

        jLabel62.setForeground(new java.awt.Color(0, 102, 153));
        jLabel62.setText("Nombre");

        jLabel63.setForeground(new java.awt.Color(0, 102, 153));
        jLabel63.setText("Inicio");

        kgIniT4.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT4KeyTyped(evt);
            }
        });

        jLabel64.setForeground(new java.awt.Color(0, 102, 153));
        jLabel64.setText("Final");

        kgFinT4.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT4KeyTyped(evt);
            }
        });

        jLabel103.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(0, 102, 153));
        jLabel103.setText("Tinta 5");

        t5.setForeground(new java.awt.Color(0, 153, 153));
        t5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t5KeyTyped(evt);
            }
        });

        jLabel104.setForeground(new java.awt.Color(0, 102, 153));
        jLabel104.setText("Nombre");

        jLabel105.setForeground(new java.awt.Color(0, 102, 153));
        jLabel105.setText("Inicio");

        kgIniT5.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT5KeyTyped(evt);
            }
        });

        jLabel106.setForeground(new java.awt.Color(0, 102, 153));
        jLabel106.setText("Final");

        kgFinT5.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT5KeyTyped(evt);
            }
        });

        jLabel107.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(0, 102, 153));
        jLabel107.setText("Tinta 6");

        t6.setForeground(new java.awt.Color(0, 153, 153));
        t6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t6KeyTyped(evt);
            }
        });

        jLabel112.setForeground(new java.awt.Color(0, 102, 153));
        jLabel112.setText("Nombre");

        jLabel113.setForeground(new java.awt.Color(0, 102, 153));
        jLabel113.setText("Inicio");

        kgIniT6.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT6KeyTyped(evt);
            }
        });

        jLabel114.setForeground(new java.awt.Color(0, 102, 153));
        jLabel114.setText("Final");

        kgFinT6.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT6KeyTyped(evt);
            }
        });

        jLabel115.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel115.setForeground(new java.awt.Color(0, 102, 153));
        jLabel115.setText("Mezcla 80/20");

        jLabel117.setForeground(new java.awt.Color(0, 102, 153));
        jLabel117.setText("Inicio");

        mezIni.setForeground(new java.awt.Color(0, 153, 153));
        mezIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mezIniKeyTyped(evt);
            }
        });

        jLabel118.setForeground(new java.awt.Color(0, 102, 153));
        jLabel118.setText("Final");

        mezFin.setForeground(new java.awt.Color(0, 153, 153));
        mezFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mezFinKeyTyped(evt);
            }
        });

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(0, 102, 153));
        jLabel119.setText("Acetato");

        jLabel121.setForeground(new java.awt.Color(0, 102, 153));
        jLabel121.setText("Inicio");

        aceIni.setForeground(new java.awt.Color(0, 153, 153));
        aceIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                aceIniKeyTyped(evt);
            }
        });

        jLabel122.setForeground(new java.awt.Color(0, 102, 153));
        jLabel122.setText("Final");

        aceFin.setForeground(new java.awt.Color(0, 153, 153));
        aceFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                aceFinKeyTyped(evt);
            }
        });

        jLabel123.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(0, 102, 153));
        jLabel123.setText("Retardante");

        jLabel125.setForeground(new java.awt.Color(0, 102, 153));
        jLabel125.setText("Inicio");

        retIni.setForeground(new java.awt.Color(0, 153, 153));
        retIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                retIniKeyTyped(evt);
            }
        });

        jLabel126.setForeground(new java.awt.Color(0, 102, 153));
        jLabel126.setText("Final");

        retFin.setForeground(new java.awt.Color(0, 153, 153));
        retFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                retFinKeyTyped(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Guardar Tintas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgIniT1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel55)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgFinT1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgIniT2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel58)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgFinT2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgIniT3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgFinT3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgIniT4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgFinT4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel104)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t5)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel105)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgIniT5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel106)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kgFinT5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel112)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t6)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel113)
                        .addGap(10, 10, 10)
                        .addComponent(kgIniT6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel114)
                        .addGap(11, 11, 11)
                        .addComponent(kgFinT6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel115)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel117)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mezIni, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel118)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mezFin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel99)
                            .addComponent(jLabel100)
                            .addComponent(jLabel101)
                            .addComponent(jLabel102)
                            .addComponent(jLabel103)
                            .addComponent(jLabel107)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel125)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(retIni, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel126)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(retFin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel123))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel119)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel121)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(aceIni, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel122)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(aceFin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel99)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel54)
                        .addComponent(kgIniT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel55)
                        .addComponent(kgFinT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel52)
                        .addComponent(t1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addComponent(jLabel100)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(t2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57)
                    .addComponent(kgIniT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58)
                    .addComponent(kgFinT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel101)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(t3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(kgIniT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61)
                    .addComponent(kgFinT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel102)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(t4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63)
                    .addComponent(kgIniT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64)
                    .addComponent(kgFinT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel103)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel104)
                    .addComponent(t5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel105)
                    .addComponent(kgIniT5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel106)
                    .addComponent(kgFinT5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel107)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel112)
                    .addComponent(t6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel113)
                    .addComponent(kgIniT6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel114)
                    .addComponent(kgFinT6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel115)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel117)
                            .addComponent(mezIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel118)
                            .addComponent(mezFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel119)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel121)
                            .addComponent(aceIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel122)
                            .addComponent(aceFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel123)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel125)
                    .addComponent(retIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel126)
                    .addComponent(retFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        paIm.addTab("Tintas", jPanel8);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paIm)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paIm)
        );

        paPro.addTab("Impresión", jPanel10);

        panBol.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel69.setForeground(new java.awt.Color(0, 102, 153));
        jLabel69.setText("Operador:");

        opBol.setForeground(new java.awt.Color(0, 153, 153));
        opBol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                opBolKeyTyped(evt);
            }
        });

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
                                .addComponent(opBol, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(opBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70)
                    .addComponent(maqBol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
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

        jToggleButton1.setBackground(new java.awt.Color(51, 51, 51));
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setText("Cerrar");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
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
                .addComponent(jToggleButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Buscar Pedido");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
        tablaPed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaPedKeyPressed(evt);
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
        tablaPart.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaPartKeyPressed(evt);
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

        savePro.setBackground(new java.awt.Color(51, 51, 51));
        savePro.setForeground(new java.awt.Color(255, 255, 255));
        savePro.setText("Generar Procesos");
        savePro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveProActionPerformed(evt);
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

        jLabel12.setForeground(new java.awt.Color(0, 102, 153));
        jLabel12.setText("Costos de operación por hora.");

        jLabel13.setForeground(new java.awt.Color(0, 102, 153));
        jLabel13.setText("Extrusión: $75");

        jLabel14.setForeground(new java.awt.Color(0, 102, 153));
        jLabel14.setText("Impresión: $65");

        jLabel15.setForeground(new java.awt.Color(0, 102, 153));
        jLabel15.setText("Bolseo: $50");

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
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(cambioMod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(eliminarP)
                                .addGap(32, 32, 32)
                                .addComponent(savePro))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton3)
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
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15))
                                .addGap(0, 0, Short.MAX_VALUE)))))
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
                            .addComponent(jButton3)
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
                        .addGap(30, 30, 30)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(savePro)
                            .addComponent(eliminarP)
                            .addComponent(cambioMod)))
                    .addComponent(paPro))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    //Cerrar ventana
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        jToggleButton1.setSelected(false);
        dispose();
    }//GEN-LAST:event_jToggleButton1ActionPerformed
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
    
    //Evento: cuando la tabla de pedidos es clickeada
    private void tablaPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedMouseClicked
 
        obtenerSeleccionTablaPedido();
    }//GEN-LAST:event_tablaPedMouseClicked
    
    private void obtenerSeleccionTablaPedido(){
        vaciarCamposProcesos();//Vaciar campos, ya que si clickea la tabla significa que quiere visualizar otro pedido o proceso
                               //Puede que la partida seleccionada aun no tenga procesos establecidos, es decir, estan vacios los campos
        
        String imp;
        String fol;
        
        try
        {
                    imp = modPed.getValueAt(tablaPed.getSelectedRow(), 1).toString();//Se obtiene el nombre de impresion
                    fol = modPed.getValueAt(tablaPed.getSelectedRow(), 0).toString().replace("A", "");//Se obtiene el folio del pedido

                    //Se desactivan los botones de guardar procesos y agregar, pues si esta tabla es clickeada significa que aun no...
                    //se requiere reslizar acciones con las partidas, esos botones solo interaccionan con las partidas y sus derivaciones
                    savePro.setEnabled(false);
                    agE.setEnabled(false);
                    agI.setEnabled(false);
                    agB.setEnabled(false);
                    gdEx.setEnabled(false);
                    gdIm.setEnabled(false);
                    gdBo.setEnabled(false);
                    btnCostos.setEnabled(false);

                    paPro.setVisible(false);
                    this.setSize(new Dimension(WD/2, HG));
                    this.setLocationRelativeTo(null);

                    modPart.setRowCount(0);//se vacia la tabla de partidas

                    foVis.setText(fol);//El textfield se establece con el valor obtenido del folio, para despues utilizarlo, Es como dejarlo
                                        //guardado y visible. Ese textfield esta desactivado para que no pueda ser borrado o modificado
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
        }  
    }
    
    
    //Boton: busqueda de los pedidos mediante la impresion
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jButton3.setSelected(false);
        llenarTablaPedido();
    }//GEN-LAST:event_jButton3ActionPerformed
    
    
    public void llenarTablaPedido(){
        
        paPro.setVisible(false);
        this.setSize(new Dimension(WD/2, HG));
        this.setLocationRelativeTo(null);
        
        modPed.setRowCount(0);//Para que no se acumulen los resultados de las consultas en la tabla
        modPart.setRowCount(0);
        String nompedido;//Para guardar el nombre de Impresion a buscar
        
        nompedido = impBus.getText().toString();//Se establece la variable a base del campo de busqueda por impresion
        
        //Se utiliza join para que tambien se muestren los datos de clientes relacionados con el pedido
        //En este caso solo para mostrar el nombre del cliente relacionado a su pedido, 2 tablas distintas.
        String sql = "select folio, impresion, nom, fIngreso from pedido join cliente on idC_fk = idC where impresion like '%"+nompedido+"%' "
                + "order by fIngreso desc limit 0, 30";
        
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){//Se van insertando los resultados de la consulta en la tabla de pedidos
                modPed.addRow(new Object[]{rs.getString("folio")+"A", rs.getString("impresion"), rs.getString("nom"), 
                    rs.getString("fIngreso")});
            }
            tablaPed.setModel(modPed);//Se establece la tabla a base del modelo modificado
            st.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla de pedidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
    //Rellenar la tabla de partidas respecto al folio seleccionado, Se acciona cuando el campo del folio se actualiza
    private void setTablePartidas(){
        
        modPart.setRowCount(0);
        
        folio = Integer.parseInt(foVis.getText());//Se obtiene el folio guardado en el textfield
        String sql = "select idPar, piezas, medida, mat1, mat2, pigmento, tipo, "
                + "piezas, precioUnitaro from partida where folio_fk = "+folio+" limit 0,30";//Busqueda de todas la prtidas relacionadas al folio
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){//Se rellena la tabla con las partidas encontradas
                modPart.addRow(new Object[]{rs.getString("idPar"), rs.getString("piezas"), rs.getString("medida"), rs.getString("mat1"), 
                    rs.getString("mat2"), rs.getString("pigmento"), rs.getString("tipo"), rs.getString("piezas"), rs.getString("precioUnitaro")});
            }
            
            tablaPart.setModel(modPart);
            st.close();
            
            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla de partidas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //si cambia el folio, se muestran las partidas de ese pedido en la tabla
    public void onChangeTextField(){
        foVis.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                setTablePartidas();//Se ejecuta cuando el campo folio se actualiza
            }

            @Override
            public void removeUpdate(DocumentEvent e) {   
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
    
   
    
    private void obtenerSeleccionTablaPartida()
    {
        String idPartSt;//Para guardar la id de la partida que se clickee
        
        String modo = "";
        String idPartSt2 = "";
        try
        {
            idPartSt = modPart.getValueAt(tablaPart.getSelectedRow(), 0).toString();
            idPartSt2 = idPartSt;
        
            idPartida.setText(idPartSt);//El campo desabilitado para las partidas se establece con la id de la partida ya guardada
            idPart = Integer.parseInt(idPartSt);//A esa varible global se le asigna el resultado de a seleccion, se pasa a entero.

            //se habilita el boton para eliminar partidas
            eliminarP.setEnabled(true);
            
            btnCostos.setEnabled(true);
            comprobarProcesos(Integer.parseInt(idPartSt));//Verificar si las partidas ya tienen generados los procesos
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
        }
        
        try
        {
            String sql = "select modoMat from partida where idPar = "+Integer.parseInt(idPartSt2)+"";//se busca si fue compra o produccion de material
            st =  con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                modo = rs.getString("modoMat");
            }
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
    
    
    //Verificar si ciertas partidas y tienen generados los procesos, se habilita o deshabilita el boton. Se obtienen las primary key de cada proceso
    private void comprobarProcesos(int idP){
        
        //Se busca en extrusion, respecto al folio, si ya hay algun proceso generado
        String sql = "select idExt from extrusion where idPar_fk = "+idP+"";
        int idE = 0;//Para guardar la id de extrusion en caso de haber registros
        
        try {//Extrusion: Obtencion de id principal
            st =  con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                idE = rs.getInt("idExt");//Se guardara la id de extrusion en caso de haber resultados
                idEx = idE;//La variable global de la id primaria de extrusion tabien se establece
            }
            
            //Si todavia no se an generado los procesos
            if(idE == 0){//Si idE es igual a 0, significa que no hay registros en extrusion, ni en otro proceso
                //Si no hay la pantalla se reducira
                this.setSize(new Dimension(WD/2, HG));
                paPro.setVisible(false);
                this.setLocationRelativeTo(null);
                
                savePro.setEnabled(true);//El boton para generar procesos se activa para que deje generarlos
                
                //Los de agregar se desactivan, pues todavia no se han generado procesos
                agE.setEnabled(false);
                agI.setEnabled(false);
                agB.setEnabled(false);
                
                //Tambien los que actualizan los procesos
                gdEx.setEnabled(false);
                gdIm.setEnabled(false);
                gdBo.setEnabled(false);
                
                btnCostos.setEnabled(false);
                
                jButton1.setEnabled(false);
                
                vaciarCamposProcesos();//Se establecen los campos de los procesos nulos, ya que no hay registros
                comprobarModoMat();
                st.close();
                
            }
            else{//Si ya fueron generados los procesos
                //Si hay, la pantalla se agrandara para mostrarlos
                this.setSize(new Dimension(WD, HG));
                paPro.setVisible(true);
                this.setLocationRelativeTo(null);
                
                //Significa que idE se reestablecio, hubo un registro de la consulta y idE ya no vale 0
                vaciarCamposProcesos();//Vacearlos para ingresar nuevos datos
                savePro.setEnabled(false);//El boton para guardar procesos se desactiva, ya se han generado y no es necesario volver a generar
                agE.setEnabled(true);//Se habilitan los botones para agrear operadores, pues ya hay procesos generados
                agI.setEnabled(true);
                agB.setEnabled(true);
                gdEx.setEnabled(true);
                gdIm.setEnabled(true);
                gdBo.setEnabled(true);
                
                btnCostos.setEnabled(true);
                jButton1.setEnabled(true);
                comprobarModoMat();//Comprobar que modalidad de produccion es. (Produccion, Compra, ambas)
                establecerCamposPartida();//Se establecen los campos de los procesos de la partida,obteniendolos de la database
                //Tambien se obtienen las primary key de los procesos
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la busqueda de procesos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //habilita ciertas partes de la pantalla deacuerdo a si fueron compras o produccion
    public void comprobarModoMat(){
        String sql = "select modoMat from partida where idPar = "+idPart+"";//se obtiene de la database
        String modo = "";
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                modo = rs.getString("modoMat");
                if(modo == null){//Si esta en null significa que no se han generado procesos
                    modo = "";
                }
            }
            
            rs.close();
            
            if(modo.equals(modoMaterial[0])){//Produccion
                panEx.setVisible(true);
                panImp.setVisible(true);
                panBol.setVisible(true);
                
                panMaqExt.setVisible(false);
                panMaqImp.setVisible(false);
                panMaqBol.setVisible(false);
                panelCostos.setVisible(true);
            }
            else if(modo.equals(modoMaterial[1])){//Compra
                panEx.setVisible(false);
                panImp.setVisible(false);
                panBol.setVisible(false);
                
                panMaqExt.setVisible(true);
                panMaqImp.setVisible(true);
                panMaqBol.setVisible(true);
                panelCostos.setVisible(true);
                
            }
            else if(modo.equals(modoMaterial[2])){//Ambos
                panEx.setVisible(true);
                panImp.setVisible(true);
                panBol.setVisible(true);
                panMaqExt.setVisible(true);
                panMaqImp.setVisible(true);
                panMaqBol.setVisible(true);
                panelCostos.setVisible(true);
            }
            else if(modo.equals("")){//Si no se han generado procesos
                
                panEx.setVisible(false);
                panImp.setVisible(false);
                panBol.setVisible(false);
                panMaqExt.setVisible(false);
                panMaqImp.setVisible(false);
                panMaqBol.setVisible(false);
                panelCostos.setVisible(false);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Esteblecer los campos de los procesos respecto a los clicks en la tabla de partidas
    private void establecerCamposPartida(){
        
        String sql = "select pocM1, pocM2, prov1, precioKg1, prov2, precioKg2, idExt from extrusion where idPar_fk = "+idPart+"";
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                proM1.setText(rs.getString("pocM1"));
                proM2.setText(rs.getString("pocM2"));
                prov1Ext.setText(rs.getString("prov1"));
                porKg1Ext.setText(rs.getString("precioKg1"));
                prov2Ext.setText(rs.getString("prov2"));
                porKg2Ext.setText(rs.getString("precioKg2"));
                idEx = rs.getInt("idExt");
            }
            
            rs.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al establecer los datos de extrusion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        sql = "select produccion, prov1, precioKg1, sticky, costoDiseno, idImp from impreso where idPar_fk = "+idPart+"";
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                kgImp.setText(rs.getString("produccion"));
                provImp.setText(rs.getString("prov1"));
                porKgImp.setText(rs.getString("precioKg1"));
                stc.setText(rs.getString("sticky"));
                costoDise.setText(rs.getString("costoDiseno"));
                idIm = rs.getInt("idImp");
            }
            
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al establecer los datos de impreso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        sql = "select produccion, produccionPz, prov1, precioKg1, idBol from bolseo where idPar_fk = "+idPart+"";
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                kgBol.setText(rs.getString("produccion"));
                pzsBol.setText(rs.getString("produccionPz"));
                provBol.setText(rs.getString("prov1"));
                porKgBol.setText(rs.getString("precioKg1"));
                idBo = rs.getInt("idBol");
            }
            
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de bolseo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        obtenerPrincipalImpreso();//Obtencion de la primary key de impreso para obtener y establecer los campos las tintas de impreso
        
        sql = "select * from tintas where idImp_fk = "+idImPrimera+"";//Llave primaria de impreso
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                t1.setText(rs.getString("tinta1"));
                kgIniT1.setText(rs.getString("pIni1"));
                kgFinT1.setText(rs.getString("pFin1"));
                t2.setText(rs.getString("tinta2"));
                kgIniT2.setText(rs.getString("pIni2"));
                kgFinT2.setText(rs.getString("pFin2"));
                t3.setText(rs.getString("tinta3"));
                kgIniT3.setText(rs.getString("pIni3")); 
                kgFinT3.setText(rs.getString("pFin3")); 
                t4.setText(rs.getString("tinta4"));
                kgIniT4.setText(rs.getString("pIni4"));
                kgFinT4.setText(rs.getString("pFin4")); 
                t5.setText(rs.getString("tinta5"));
                kgIniT5.setText(rs.getString("pIni5"));
                kgFinT5.setText(rs.getString("pFin5"));
                t6.setText(rs.getString("tinta6"));
                kgIniT6.setText(rs.getString("pIni6"));
                kgFinT6.setText(rs.getString("pFin6"));

                mezIni.setText(rs.getString("iniMezcla"));
                mezFin.setText(rs.getString("finMezcla")); 
                aceIni.setText(rs.getString("iniAcetato")); 
                aceFin.setText(rs.getString("finAcetato"));
                retIni.setText(rs.getString("iniRetard")); 
                retFin.setText(rs.getString("finRetard"));
            }
            
            rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al establecer los campos de las tintas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        //se muestra los costos de grabados del pedido en impresion
        sql = "select grabados from pedido where folio = "+folio+"";
        try
        {
           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next())
           {
               costoGrab.setText(rs.getString("grabados"));
           }
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        
    }
    
    //Se establecen en "" o "0" los campos de los procesos que aun no se han generado
    private void vaciarCamposProcesos(){
        
        proM1.setText("0");
        proM2.setText("0");
        prov1Ext.setText("");
        porKg1Ext.setText("0");
        prov2Ext.setText("");
        porKg2Ext.setText("0");
        
        kgImp.setText("0");
        provImp.setText("");
        porKgImp.setText("0");
        stc.setText("0");
        costoDise.setText("0");
        costoGrab.setText("0");
        
        kgBol.setText("0");
        pzsBol.setText("0");
        provBol.setText("");
        porKgBol.setText("0");
        
        t1.setText("");
        kgIniT1.setText("0");
        kgFinT1.setText("0");
        t2.setText("");
        kgIniT2.setText("0");
        kgFinT2.setText("0");
        t3.setText("");
        kgIniT3.setText("0"); 
        kgFinT3.setText("0"); 
        t4.setText("");
        kgIniT4.setText("0");
        kgFinT4.setText("0"); 
        t5.setText("");
        kgIniT5.setText("0");
        kgFinT5.setText("0");
        t6.setText("");
        kgIniT6.setText("0");
        kgFinT6.setText("0");
            
        mezIni.setText("0");
        mezFin.setText("0"); 
        aceIni.setText("0"); 
        aceFin.setText("0");
        retIni.setText("0"); 
        retFin.setText("0");
        
    }
    
    //Generar procesos
    private void saveProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveProActionPerformed
        savePro.setSelected(false);
        String elegido = (String) JOptionPane.showInputDialog(null, "Compra o Produccion de Material?", "Modo de generacion", 
                JOptionPane.QUESTION_MESSAGE, null, modoMaterial,  modoMaterial[0]);
        
        if(!(elegido == null)){
            
            comprobarVacio();//Establecer en 0 ciertos campos para evitar errors en la base de datos
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
            stickySt = stc.getText();
            cDiseSt = costoDise.getText();
            cGrabSt = costoGrab.getText();
            stcSt = stc.getText();

            //Bolseo
            prodBSt = kgBol.getText();
            prodPzSt = pzsBol.getText();
            provB1St = provBol.getText();
            precioKgB1St = porKgBol.getText();

            //Tintas de impreso
            t1St = t1.getText();
            pI1St = kgIniT1.getText(); 
            pF1St = kgFinT1.getText(); 
            t2St = t2.getText();
            pI2St = kgIniT2.getText(); 
            pF2St = kgFinT2.getText(); 
            t3St = t3.getText();
            pI3St = kgIniT3.getText(); 
            pF3St = kgFinT3.getText(); 
            t4St = t4.getText();
            pI4St = kgIniT4.getText(); 
            pF4St = kgFinT4.getText(); 
            t5St = t5.getText();
            pI5St = kgIniT5.getText(); 
            pF5St = kgFinT5.getText();
            t6St = t6.getText();
            pI6St = kgIniT6.getText(); 
            pF6St = kgFinT6.getText(); 

            iniMezSt = mezIni.getText(); 
            finMezSt = mezFin.getText(); 
            iniAceSt = aceIni.getText(); 
            finAceSt = aceFin.getText(); 
            iniRetSt = retIni.getText(); 
            finRetSt = retFin.getText();



            //Extrusion
                String sql = "insert into extrusion(pocM1, pocM2, prov1, precioKg1, prov2, precioKg2, idPar_fk, costoUnitarioExt, kgTotales, greniaExt, "
                        + "costoOpTotalExt) "
                        + "values("+pM1St+","+pM2St+",'"+provE1St+"',"+precioKgE1St+",'"+prov2St+"',"+precioKg2St+","+idPart+", 0, 0, 0, 0)";

                try {
                    st = con.createStatement();
                    st.execute(sql);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo generar extrusion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

                //Impreso
                sql = "insert into impreso(produccion, prov1, precioKg1, idPar_fk, kgTotales, costoOpTotalImp, greniaImp, "
                        + "costoUnitarioImp) "
                        + "values("+prodISt+",'"+provI1St+"',"+precioKgI1St+","+idPart+",0,0,0,0)";

                try {
                    st.execute(sql);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo generar impreso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                //Bolseo
                sql = "insert into bolseo(produccion, produccionPz, prov1, precioKg1, idPar_fk, kgTotales, costoOpTotalBol, greniaBol, costoUnitarioBol) "
                        + "values("+prodBSt+","+prodPzSt+",'"+provB1St+"',"+precioKgB1St+","+idPart+",0,0,0,0)";

                try {
                    st.execute(sql);

                    st.close();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo generar bolseo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                //Consulta para obtener la clave principal generada en impreso
                obtenerPrincipalImpreso();
                //Con la clave de impreso obtenida ahora si puedo insertar tintas

                //Tintas
                sql = "insert into tintas(" +
                    " tinta1, pIni1, pFin1, tinta2, pIni2, pFin2," +
                    " tinta3, pIni3, pFin3, tinta4, pIni4, pFin4," +
                    " tinta5, pIni5, pFin5, tinta6, pIni6, pFin6," +
                    " iniMezcla, finMezcla," +
                    " iniAcetato, finAcetato," +
                    " iniRetard, finRetard," +
                    " idImp_fk)" +
                    " values('"+t1St+"', "+pI1St+", "+pF1St+", '"+t2St+"', "+pI2St+", "+pF2St+", '"+t3St+"', "+pI3St+", "+pF3St+", '"+t4St+"', "+pI4St+", "
                        + ""+pF4St+", '"+t5St+"', "+pI5St+", "+pF5St+", '"+t6St+"', "+pI6St+", "+pF6St+", "+iniMezSt+", "+finMezSt+", "+iniAceSt+", "+finAceSt+", "
                        + ""+iniRetSt+", "+finRetSt+", "+idImPrimera+")";

                try {
                    st.execute(sql);

                     JOptionPane.showMessageDialog(null, "Procesos Generados: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "No se pudieron generar las tintas de impreso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }   

                sql = "update partida set modoMat = '"+elegido+"' where idPar = "+idPart+"";
                try {
                    st.execute(sql);
                    st.close();
                    if(elegido.equals(modoMaterial[0]))
                    {
                        cambioMod.setVisible(true);
                        cambioMod.setText("Agregar compras");
         
                    }else if(elegido.equals(modoMaterial[1]))
                    {
                        cambioMod.setVisible(true);
                        cambioMod.setText("Agregar producción");
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                }

                comprobarProcesos(idPart);//En el momento en el que se introduce la extrusion llamo a la funcion para que
                                              //compruebe la generacion de procesos y me desactive el boton de generar procesos
                                              //Pues ya no es necesario tenerlo activado por que ya estoy generandolos
                
        }else{
            
            JOptionPane.showMessageDialog(null, "No se eligio ningun modo", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        

    }//GEN-LAST:event_saveProActionPerformed
    
    //Obtener clave principal de impreso para añadir tintas
    private void obtenerPrincipalImpreso(){
        
        //Se consulta impreso con la primary key de la partida que ya esta guardada
        String sql = "select * from impreso where idPar_fk = "+idPart+"";
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                idImPrimera = rs.getInt("idImp");//Se establece la variable global de la id de Impreso
            }
            rs.close();
             
        } catch (SQLException ex) {
            
             JOptionPane.showMessageDialog(null, "No se pudo obtener la clave de impeso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //Boton: agregar operadores de impreso
    private void agIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agIActionPerformed
            agI.setSelected(false);
            comprobarVacio();
            kgISt = kgOpIm.getText();
            greniaISt = greImp.getText();
            opISt = opImp.getText();
            nMISt = maqImp.getText();
            hIniISt = hrIniImp.getTimeField().getText();
            fIniISt = fIniImp.getText();
            hFinISt = hrFinImp.getTimeField().getText();
            fFinISt = fFinImp.getText();
            tMuISt = hrMuertoImp.getTimeField().getText();
            totHISt = totalHrImp.getTimeField().getText();
            exISt = extHrImp.getTimeField().getText();
            costoOpImSt = costoOpImp.getText();
            
            String sql = "insert into operadorImp(costoOpImp, kgUniI, grenia, operador, numMaquina, horaIni, fIni, horaFin, fFin, tiempoMuerto, totalHoras, extras, idImp_fk)" +
                    "values("+costoOpImSt+", "+kgISt+","+greniaISt+",'"+opISt+"',"+nMISt+",'"+hIniISt+"', '"+fIniISt+"', '"+hFinISt+"', '"+fFinISt+"', '"+tMuISt+"',"
                    + " '"+totHISt+"', '"+exISt+"', "+idIm+")";
        try { 
            st = con.createStatement();
            st.execute(sql);
            
             JOptionPane.showMessageDialog(null, "Se ha guardado el operador en Impreso: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            
            sumarKilosIm(st);//se calcula la sumatoria de los kg producidos
            sumarCostosOperacionales(st, "impreso", "operadorImp", "costoOpImp", "costoOpTotalImp", "idImp", "idImp_fk", idIm);//calcula el costo de operacion de impresion
            sumarGrenias("operadorImp", "grenia", "idImp_fk", "impreso", "greniaImp", "idImp", idIm);//se hace la sumatoria de grenias
            calculaCostoPartida();//se calcula e inserta el costo de partida
            calculaHrTotalesPartida("operadorImp", "idImp_fk", idIm,"impreso");//se hace la sumatoria de horas de procesos de la partida
            float material = queryForPesosMaterialMultiuso("impreso", "idImp", idIm, "kgUniI", "operadorImp", "idImp_fk");//se hace la suma de peso de lo comprado y producido
            calcularCostoUnitarioMultiuso(material, "costoOpTotalImp", "impreso", "idImp", idIm, "costoUnitarioImp");//se calcula e inserta el costo unitario de impresion
            calcularCostoTotalPe();//se calcula el costo total del pedido
            calculaPyG();//se calculan las perdidas y ganancias
            vaciarOpI();//se establece en 0 los recuadros que permiten seleccionar horas
        } catch (SQLException ex) {
            
             JOptionPane.showMessageDialog(null, "Error al intentar agregar el operador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }//GEN-LAST:event_agIActionPerformed
    
    //hace la sumatoria de los kg producidos en impreso
    private void sumarKilosIm(Statement st){
        
        String sql = "select kgUniI from operadorImp where idImp_fk = "+idIm+"";
        float kilosImp = 0f;
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                kilosImp = kilosImp + Float.parseFloat(rs.getString("kgUniI"));
            }
            
            actualizarImpreso(st, kilosImp);//se inserta el dato en la base
            
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //inserta la sumatoria de los kg de impreso
    private void actualizarImpreso(Statement st, float kgTot){
        String sql = "update impreso set kgTotales = "+kgTot+" where idImp = "+idIm+"";
        
        try {
            st.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**Kilos de desperdicio partida**/
    
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
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return materiales;
    }
    
    //Devuelve la grenia total de extrusion por partida
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
        catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
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
        }
        catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sql = "select kgUniB from operadorBol where idBol_fk = "+idBo+"";
        try
        {
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                producido = producido + Float.parseFloat(rs.getString("kgUniB"));
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        total = comprado + producido;
        return total;
    }
    
    //calcula y actualiza los kg de desperdicio en la partida
    private void actualizarKgDes(){
        float KgDesperdicio = 0f;
        float materialesComprados = queryForPesosMaterialPartida(idPart);
        float materialesProducidos = 0f;
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
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**porcentaje de desperdicio de la partida**/
    
    //devuelve los kg de desperdicio de una partida
    private float queryKgDesperdicio()
    {
        String sql = "select kgDesperdicio from partida where idPar = "+idPart+""; 
        float peso = 0f;
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                peso = Float.parseFloat(rs.getString("kgDesperdicio"));
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return peso;
    }
    
    //Actualizacion del porcentaje de desperdicio con los kg de derperdicio ya obtenidos
    private void actualizarPorcentajeDes(){
        float kgDes = 0;
        kgDes = queryKgDesperdicio();
        float materiales = 0;
        materiales = queryForPesosMaterialPartida(idPart);
        //si no hubo material 1 y 2 se hace la sumatoria de lo producido
        if(materiales == 0)
        {
            String sql = "select kgUniE from operadorExt where idExt_fk = "+idEx+"";
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
        String sql = "update partida set porcentajeDesp = "+porcentajeDesp+" where idPar = "+idPart+"";
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } 
        catch(SQLException ex) 
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    /**costo de material NOTA: solo es de extrusion**/
    
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
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return costo;
    }
    
    //calcula y devuelve el costo  de los materiales producidos
    float calculaCostoMaterialProducidoExt()
    {
        String sql = "select kgUniE,grenia from operadorExt where idExt_fk = "+idEx+"";
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
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**funciones de costo partida**/
    
    //suma los costos de operacion de cada proceso y el costo de material y los inserta en la tabla partida
    void calculaCostoPartida()
    {
        float costoExt = 0f, costoImp = 0f, costoBol = 0f, costoMat = 0f, suma = 0f;
        String sql = "select costoOpTotalExt from extrusion where idPar_fk = "+idPart+"";
        try{
            st  = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
               costoExt = costoExt + Float.parseFloat(rs.getString("costoOpTotalExt"));
            }
            rs.close();
        }
        catch(SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        sql = "select costoOpTotalImp from impreso where idPar_fk = "+idPart+"";
        try{
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoImp = costoImp + Float.parseFloat(rs.getString("costoOpTotalImp"));
            }
            rs.close();
        }
        catch(SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        sql = "select costoOpTotalBol from bolseo where idPar_fk = "+idPart+"";
        try{
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoBol = costoBol + Float.parseFloat(rs.getString("costoOpTotalBol"));
            }
            rs.close();
        }
        catch(SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        sql = "select costoMaterialTotal from partida where idPar = "+idPart+"";
        try{
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoMat = Float.parseFloat(rs.getString("costoMaterialTotal"));
            }
            rs.close();
        }
        catch(SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        suma = costoExt + costoImp + costoBol + costoMat;
        sql = "update partida set costoPartida = "+suma+" where idPar = "+idPart+"";
        try{
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**costo unitario**/
    
    //hace la sumatoria de los pesos de material producido y comprado para impreso o bolseo
    private float queryForPesosMaterialMultiuso(String tabla, String idDeProceso, int variableGlobalIdProceso, String kgUniDelProceso, String tablaOperador,String idFkTablaOperador){
        String sql = "select produccion from "+tabla+" where "+idDeProceso+" = "+variableGlobalIdProceso+"";
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
                matC = Float.parseFloat(rs.getString("produccion"));
                materiales = materiales + matC;
            }          
            rs.close();
        }catch(SQLException ex) 
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sql = "select "+kgUniDelProceso+" from "+tablaOperador+" where "+idFkTablaOperador+" = "+variableGlobalIdProceso+"";
        try 
        {
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                matP = Float.parseFloat(rs.getString(""+kgUniDelProceso+""));
                 //sumatoria de material producido
                materiales = materiales + matP;
            }          
            rs.close();
            st.close();
            } 
        catch(SQLException ex) 
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return materiales;
    }
    
    //hace la suma de los materiales comprados y producidos por partida de extrusion
    private float pesosExtCYP()
    {
        float sumatoriaC = 0f, sumatoriaP = 0f, sumatoriaT = 0f;
        String sql = "select pocM1, pocM2 from extrusion where idExt = "+idEx+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                sumatoriaC = sumatoriaC + Float.parseFloat(rs.getString("pocM1")) + Float.parseFloat(rs.getString("pocM2"));
            }
            rs.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.out.println("pesosExtCYP: error al hacer sumatoria de materiales comprados");
        }
        
        sql = "select kgUniE from operadorExt where idExt_fk = "+idEx+"";
        try
        {
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                sumatoriaP = sumatoriaP + Float.parseFloat(rs.getString("kgUniE"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        sumatoriaT = sumatoriaC + sumatoriaP;
        
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
        }
        catch(SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        kgtotales = pesosExtCYP();
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
                Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
            } 
    }
    
    //calcula el costo unitario de impreso o bolseo
    private void calcularCostoUnitarioMultiuso(float material, String costoOpTotalProceso, String proceso, String idProceso, int variableGlobalIdProceso, String costoUnitarioProceso){
        float costo = 0f, kgtotales = material, resultado = 0f;
        String sql = "select "+costoOpTotalProceso+" from "+proceso+" where "+idProceso+" = "+variableGlobalIdProceso+"";
        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costo = Float.parseFloat(rs.getString(""+costoOpTotalProceso+""));
            }
            rs.close();
        }
        catch(SQLException ex) 
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        if(costo != 0 && kgtotales != 0)
        {
            resultado = costo / kgtotales;
        }
        
        sql = "update "+proceso+" set "+costoUnitarioProceso+" = "+resultado+" where "+idProceso+" = "+variableGlobalIdProceso+"";
        try
        {
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) 
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    /**kg de desperdicio del pedido**/
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
        }
        catch(SQLException ex) 
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sql = "update pedido set kgDesperdicioPe = "+sumatoria+" where folio = "+folio+"";
        try
        {
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**porcentaje de desperdicio del pedido**/
    //calcula la sumatoria de los kg comprados o producidos de extrusion y lo inserta en pedido pedido
    private void sumaMaterialesPedido()
    {
        float sumatoria = 0f;
        float producido = 0f, comprado = 0f;
        int idPartida = 0;
        String sql2 = "";
        Statement st2;
        ResultSet rs2;
        String sql3 = "";
        Statement st3;
        ResultSet rs3;
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
                    Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
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
                            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    rs2.close();
                    st2.close();
                }
                catch(SQLException ex)
                {
                    Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sql = "update pedido set matComPe = "+comprado+",matProPe = "+producido+" where folio = "+folio+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        sumatoria = comprado + producido;
        
        calculaPorcentajeDesperdicioPe(sumatoria);
    }
    
    //calcula el porcentaje de desperdicio del pedido y lo inserta en la tabla pedido
    private void calculaPorcentajeDesperdicioPe(float sumatoria)
    {
        float kgDesp = 0f, porcentaje = 0f;
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
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(kgDesp != 0 && sumatoria != 0)
        {
            porcentaje = kgDesp/sumatoria;
        }
        
        sql = "update pedido set porcentajeDespPe = "+porcentaje+" where folio = "+folio+"";
        try
        {
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**costo total**/
    //calcula e inserta el costo total del pedido
    private void calcularCostoTotalPe()
    {
        String sql = "select costoPartida from partida where folio_fk = "+folio+" and costoPartida is not null";
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
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        sql = "update pedido set costoTotal  = "+sumatoria+" where folio = "+folio+"";
        try
        {
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   /**perdidas y ganancias**/
    
    //calcula los kg finales del pedido
    private float calculaKgFinalesPedido()
    {
        //entra al pedido
        float sumatoria = 0f;
        
                    String sql2 = "select idPar from partida where folio_fk = "+folio+"";//obtiene el folio de cada partida
                    try
                    {
                        Statement st2 = con.createStatement();
                        ResultSet rs2 = st2.executeQuery(sql2);
                        while(rs2.next())
                        {
                            int idPart2 =   Integer.parseInt(rs2.getString("idPar"));
                            
                            if(sumatoria <= 0)
                            {
                                String sql3 = "select produccion from bolseo where idPar_fk = "+idPart2+"";//obtiene lo comprado de bolseo
                                try
                                {
                                    Statement st3 = con.createStatement();
                                    ResultSet rs3 = st3.executeQuery(sql3);
                                    while(rs3.next())
                                    {
                                        float comprado = Float.parseFloat(rs3.getString("produccion"));
                                        if(comprado > 0)//verifica que lo comprado no sea null
                                        {
                                            sumatoria = sumatoria + comprado;//hace la sumatoria de lo comprado de bolseo 
                                        }
                                
                                        String sql4 = "select idBol from bolseo where idPar_fk = "+idPart2+"";//selecciona el id de bolseo del proceso
                                        try
                                        {
                                            Statement st4 = con.createStatement();
                                            ResultSet rs4 = st4.executeQuery(sql4);
                                            while(rs4.next())
                                            {
                                                int idBo2 = Integer.parseInt(rs4.getString("idBol"));
                                                
                                                String sql5 = "select kgUniB from operadorBol where idBol_fk = "+idBo2+"";//selecciona lo producido de bolseo
                                                try
                                                {
                                                    Statement st5 = con.createStatement();
                                                    ResultSet rs5 = st5.executeQuery(sql5);
                                                    while(rs5.next())
                                                    {
                                                        float producido = Float.parseFloat(rs5.getString("kgUniB"));
                                                        
                                                        if(producido > 0)//verifica que lo producido no sea null
                                                        {
                                                            sumatoria += producido;//hace la sumatoria de lo producido
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
                            if(sumatoria <= 0)
                            {
                                String sql3 = "select produccion from impreso where idPar_fk = "+idPart2+"";//obtiene lo comprado de impreso
                                try
                                {
                                    Statement st3 = con.createStatement();
                                    ResultSet rs3 = st3.executeQuery(sql3);
                                    while(rs3.next())
                                    {
                                        float comprado = Float.parseFloat(rs3.getString("produccion"));
                                        if(comprado > 0)//verifica que lo comprado no sea null
                                        {
                                            sumatoria = sumatoria + comprado;//hace la sumatoria de lo comprado de impreso 
                                        }
                                
                                        String sql4 = "select idImp from impreso where idPar_fk = "+idPart2+"";//selecciona el id de impreso del proceso
                                        try
                                        {
                                            Statement st4 = con.createStatement();
                                            ResultSet rs4 = st4.executeQuery(sql4);
                                            while(rs4.next())
                                            {
                                                int idIm2 = Integer.parseInt(rs4.getString("idImp"));
                                                
                                                String sql5 = "select kgUniI from operadorImp where idImp_fk = "+idIm2+"";//selecciona lo producido de impreso
                                                try
                                                {
                                                    Statement st5 = con.createStatement();
                                                    ResultSet rs5 = st5.executeQuery(sql5);
                                                    while(rs5.next())
                                                    {
                                                        float producido = Float.parseFloat(rs5.getString("kgUniI"));
                                                        if(producido > 0)//verifica que lo producido  o la grenia no sea null
                                                        {
                                                            sumatoria += producido;//hace la sumatoria de lo producido y la grenia
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
                            if(sumatoria <= 0)
                            {
                                String sql3 = "select pocM1,pocM2 from extrusion where idPar_fk = "+idPart2+"";//obtiene lo comprado de extrusion
                                try
                                {
                                    Statement st3 = con.createStatement();
                                    ResultSet rs3 = st3.executeQuery(sql3);
                                    while(rs3.next())
                                    {
                                        float comprado1 = Float.parseFloat(rs3.getString("pocM1"));
                                        float comprado2 = Float.parseFloat(rs3.getString("pocM2"));
                                        if(comprado1 > 0 || comprado2 > 0)//verifica que lo comprado no sea null
                                        {
                                            sumatoria = sumatoria + (comprado1 + comprado2);//hace la sumatoria de lo comprado de extrusion 
                                        }
                                
                                        String sql4 = "select idExt from extrusion where idPar_fk = "+idPart2+"";//selecciona el id de extrusion del proceso
                                        try
                                        {
                                            Statement st4 = con.createStatement();
                                            ResultSet rs4 = st4.executeQuery(sql4);
                                            while(rs4.next())
                                            {
                                                int idEx2 = Integer.parseInt(rs4.getString("idExt"));
                                                
                                                String sql5 = "select kgUniE from operadorExt where idExt_fk = "+idEx2+"";//selecciona lo producido de extrusion
                                                try
                                                {
                                                    Statement st5 = con.createStatement();
                                                    ResultSet rs5 = st5.executeQuery(sql5);
                                                    while(rs5.next())
                                                    {
                                                        float producido = Float.parseFloat(rs5.getString("kgUniE"));
                                                        if(producido > 0)//verifica que lo producido  o la grenia no sea null
                                                        {
                                                            sumatoria += producido;//hace la sumatoria de lo producido y la grenia
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
                        }
                        rs2.close();
                        st2.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
        return sumatoria;
    }
    
    //calcula los gastos fijos por kg del pedido
    private float calculaGfKg()
    {
        float gfkg = 0f, gfr = 0f, sumatoriaRango = 0f;
        String sql = "select gastosFijos,kgFinalesRango from pedido where folio = "+folio+" and gastosFijos is not null and kgFinalesRango is not null";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                gfr = Float.parseFloat(rs.getString("gastosFijos"));
                sumatoriaRango = Float.parseFloat(rs.getString("kgFinalesRango"));//esto se inserta cuando se insertan los gastos fijos de rango
                if(sumatoriaRango != 0)
                {
                    gfkg = gfr / sumatoriaRango;
                }    
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        if(gfkg != 0)
            return gfkg;
        else
            return 0;
    }
    
    private void calculaPyG()
    {
        Statement st2;
        ResultSet rs2;
        Statement st3;
        ResultSet rs3;
        float subtotal = 0f, costoTotal = 0f, descuento = 0f;
        float PyG = 0;
        String sql2 = "";
        String sql = "select fTermino from pedido where folio = "+folio+"";//busca si ya fueron terminados los pedidos
        try
        {
            st3 = con.createStatement();
            rs3 = st3.executeQuery(sql);
            while(rs3.next())
            {
                if(rs3.getString("fTermino").equals("2018-01-01") == false)//solo lo hara con pedidos que ya hayan sido terminados
                {
                    sql2 = "select subtotal, costoTotal, descuento from pedido where folio = "+folio+"";
                    try
                    {
                        st2 = con.createStatement();
                        rs2 = st2.executeQuery(sql2);
                        while(rs2.next())
                        {
                            subtotal = Float.parseFloat(rs2.getString("subtotal"));
                            costoTotal = Float.parseFloat(rs2.getString("costoTotal"));
                            descuento = Float.parseFloat(rs2.getString("descuento"));
                        }
                        rs2.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                    float kgFnPe = calculaKgFinalesPedido();
                    float gf = calculaGfKg();
                    PyG = subtotal - costoTotal - descuento - ( kgFnPe * gf);
                }
            }
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        sql2 = "update pedido set perdidasYGanancias = "+PyG+" where folio = "+folio+"";
        try
        {
            st2 = con.createStatement();
            st2.execute(sql2);
            st2.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
      
    private void eliminarPartida()
    {
        String sql = "select idPar from partida where folio_fk = "+folio+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                if(JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar esta partida") == 0)
                {
                    sql = "delete from tintas where idImp_fk = "+idIm+"";
                try
                {
                    st.execute(sql);
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                sql = "delete from operadorBol where idBol_fk = "+idBo+"";
                try
                {
                    st.execute(sql);
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                sql = "delete from operadorImp where idImp_fk = "+idIm+"";
                try
                {
                    st.execute(sql);
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
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                sql = "delete from bolseo where idPar_fk = "+idPart+"";
                try
                {
                    st.execute(sql);
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                sql = "delete from impreso where idPar_fk = "+idPart+"";
                try
                {
                    st.execute(sql);
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                sql = "delete from extrusion where idPar_fk = "+idPart+"";
                try
                {
                    st.execute(sql);
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                sql = "delete from partida where idPar = "+idPart+"";
                try
                {
                    st.execute(sql);
                    JOptionPane.showMessageDialog(null,"Se ha borrado la partida");
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                }
            }
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    //Se consultan todos los registros de operadores para hacer una suma
    private void sumaKilosEx(Statement st){
        
        String sql = "select * from operadorExt where idExt_fk = "+idEx+"";
        float kilosExt = 0f;
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                kilosExt = kilosExt + Float.parseFloat(rs.getString("kgUniE"));
            }
            
            actualizarExtrusion(st, kilosExt);
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
   
    private void actualizarExtrusion(Statement st, float kgTot){
        
        String sql = "update extrusion set kgTotales = "+kgTot+" where idExt = "+idEx+"";
        
        try {
            st.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Boton: agregar operadores de bolseo
    private void agBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agBActionPerformed
        agB.setSelected(false);
        comprobarVacio();
        kgBSt = kgOpBol.getText();
        greniaBSt = greBol.getText();
        suajeBSt = suaje.getText();
        opBSt = opBol.getText();
        nMBSt = maqBol.getText();
        hIniBSt = hrIniBol.getTimeField().getText();
        fIniBSt = fIniBol.getText();
        hFinBSt = hrFinBol.getTimeField().getText();
        fFinBSt = fFinBol.getText();
        tMuBSt = hrMuertoBol.getTimeField().getText();
        totHBSt = totalHrBol.getTimeField().getText();
        exBSt = extBol.getTimeField().getText();
        costoOpBoSt = costoOpBol.getText();
        
        String sql = "insert into operadorBol(costoOpBol, kgUniB, grenia, suaje, operador, numMaquina, horaIni, fIni, horaFin, fFin, tiempoMuerto, totalHoras, extras, idBol_fk)" +
                "values("+costoOpBoSt+", "+kgBSt+","+greniaBSt+", '"+suajeBSt+"','"+opBSt+"',"+nMBSt+",'"+hIniBSt+"', '"+fIniBSt+"', '"+hFinBSt+"', '"+fFinBSt+"', '"+tMuBSt+"',"
                + " '"+totHBSt+"', '"+exBSt+"', "+idBo+")";
        
        try {
            st = con.createStatement();
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Se ha guardado el operador en Bolseo: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            
            sumarKilosBol(st);
            sumarCostosOperacionales(st, "bolseo", "operadorBol", "costoOpBol", "costoOpTotalBol", "idBol", "idBol_fk", idBo);
            sumarGrenias("operadorBol", "grenia", "idBol_fk", "bolseo", "greniaBol", "idBol", idBo);
            actualizarKgDes();
            actualizarPorcentajeDes();
            calculaCostoPartida();
            calculaHrTotalesPartida("operadorBol", "idBol_fk", idBo, "bolseo");
            float material = queryForPesosMaterialMultiuso("bolseo", "idBol", idBo, "kgUniB", "operadorBol","idBol_fk");
            calcularCostoUnitarioMultiuso(material, "costoOpTotalBol", "bolseo", "idBol", idBo, "costoUnitarioBol");
            calculaKgDesperdicioPedido();
            sumaMaterialesPedido();
            calcularCostoTotalPe();
            calculaPyG();
            vaciarOpB();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el operador: " + ex.getMessage(), "Error", 5555555);
        }
    }//GEN-LAST:event_agBActionPerformed
    
    
    
    private void sumarKilosBol(Statement st){
        
        String sql = "select * from operadorBol where idBol_fk = "+idBo+"";
        float kilosBol = 0f;
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                kilosBol = kilosBol + Float.parseFloat(rs.getString("kgUniB"));
            }
            
            actualizarBolseo(st, kilosBol);
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void actualizarBolseo(Statement st, float kgTot){
        String sql = "update bolseo set kgTotales = "+kgTot+" where idBol = "+idBo+"";
        
        try {
            st.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //hace la sumatoria de los costos operacionales
    private void sumarCostosOperacionales(Statement st, String tablaPro, String tablaOp, String campo, String campoProceso, String idCampo, String idFk, int idValor){
        
        float costosTotal = 0f;
        String sql = "select "+campo+" from "+tablaOp+" where "+idFk+" = "+idValor+"";
        
        try {
            rs = st.executeQuery(sql);
            while(rs.next()){
                costosTotal = costosTotal + Float.parseFloat(rs.getString(campo));
            }
           
            actualizarCostoExtrusion(st, tablaPro, costosTotal, campoProceso, idCampo, idValor);//se inserta en la base de datos
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //inserta el costo de extrusion en la base de datos
    private void actualizarCostoExtrusion(Statement st,  String tablaPro, float costoTotal, String campoProceso, String idCampo, int idValor){
        
        String sql = "update "+tablaPro+" set "+campoProceso+" = "+costoTotal+" where "+idCampo+" = "+idValor+"";
        
        try {
            st.execute(sql);
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     //Sumar greniasde los registros de operador
    private void sumarGrenias(String tablaOperador,String greniaOp, String idFk, String tablaProceso, String greniaProceso, String nomCampoId,int idPGlobal){
        
        String sql = "select "+greniaOp+" from "+tablaOperador+" where "+idFk+" = "+idPGlobal+"";
        float kilosGre = 0f;
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                kilosGre = kilosGre + Float.parseFloat(rs.getString(greniaOp));
            }
              
            actualizarGreniaProceso(st, tablaProceso, greniaProceso, kilosGre, nomCampoId, idPGlobal);//se inserta en la base de datos
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Actualizacion de grenia total de cada proceso
    private void actualizarGreniaProceso(Statement st, String tablaProceso,String greniaProceso,float sumatoriaGrenia,String campoIdDelProceso,int idDelProceso){
        String sql = "update "+tablaProceso+" set "+greniaProceso+" = "+sumatoriaGrenia+" where "+campoIdDelProceso+" = "+idDelProceso+"";
        
        try {
            st.execute(sql);
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"error al actualizar greña del proceso");
        }
    }
    
    
    
    //Actualizar datos de extrusion
    private void gdExActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gdExActionPerformed
        gdEx.setSelected(false);
        //Extrusion
        comprobarVacio();
        pM1St = proM1.getText();
        pM2St = proM2.getText();
        provE1St = prov1Ext.getText();
        precioKgE1St = porKg1Ext.getText();
        prov2St = prov2Ext.getText();
        precioKg2St = porKg2Ext.getText();
        
        String sql = "update extrusion set pocM1 = "+pM1St+", pocM2 = "+pM2St+", prov1 = '"+provE1St+"', precioKg1 = "+precioKgE1St+", "
                + "prov2 = '"+prov2St+"', precioKg2 = "+precioKg2St+" where idPar_fk = "+idPart+"";
        
        try {
            st = con.createStatement();
            st.execute(sql);
            actualizarKgDes();
            actualizarPorcentajeDes();
            calculaCostoMaterialTotalExt();
            calculaCostoPartida();
            calcularCostoUnitarioExt();
            calculaKgDesperdicioPedido();
            sumaMaterialesPedido();
            calcularCostoTotalPe();
            calculaPyG();
            st.close();
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_gdExActionPerformed
    
    //Actualizar datos de impresion
    private void gdImActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gdImActionPerformed
        gdIm.setSelected(false);
        //Impreso
        comprobarVacio();
        prodISt = kgImp.getText();
        provI1St = provImp.getText();
        precioKgI1St = porKgImp.getText();
        
        String sql = "update impreso set produccion = "+prodISt+", prov1 = '"+provI1St+"', precioKg1 = "+precioKgI1St+""
                + " where idPar_fk = "+idPart+"";
        
        try {
            st = con.createStatement();
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro de impreso: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            obtenerPrincipalImpreso();//Para poder actualizar las tintas del impreso actualizado
            float material = queryForPesosMaterialMultiuso("impreso", "idImp", idIm, "kgUniI", "operadorImp", "idImp_fk");
            calcularCostoUnitarioMultiuso(material, "costoOpTotalImp", "impreso", "idImp", idIm, "costoUnitarioImp");
            calculaPyG();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro de impreso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_gdImActionPerformed

    private void gdBoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gdBoActionPerformed
        gdBo.setSelected(false);
        //Bolseo
        comprobarVacio();
        prodBSt = kgBol.getText();
        prodPzSt = pzsBol.getText();
        provB1St = provBol.getText();
        precioKgB1St = porKgBol.getText();

        String sql = "update bolseo set produccion = "+prodBSt+", produccionPz = "+prodPzSt+", prov1 = '"+provB1St+"', precioKg1 = "+precioKgB1St+""
        + " where idPar_fk = "+idPart+"";

        try {
            st = con.createStatement();
            st.execute(sql);
            actualizarKgDes();
            actualizarPorcentajeDes();
            float material = queryForPesosMaterialMultiuso("bolseo", "idBol", idBo, "kgUniB", "operadorBol","idBol_fk");
            calcularCostoUnitarioMultiuso(material, "costoOpTotalBol", "bolseo", "idBol", idBo, "costoUnitarioBol");
            calculaKgDesperdicioPedido();
            sumaMaterialesPedido();
            calculaPyG();
            //st.close();
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_gdBoActionPerformed

    
    //Funcion para que solo se ingresen numeros flotantes en campos
    private void soloFlotantes(KeyEvent evt, JTextField campo){
        
        char c = evt.getKeyChar();//Obtener el caracter de la tecla presionada
        int contPuntos = 0;// contador de los ".", pues solo debe de haber uno en un numero flotante
        String cadena = campo.getText();//Guardar la cadena del campo para contar los puntos que contiene
        char aux = 0;//Para ir comprobando cada caracter de la cadena
        
        if((c < '0' || c > '9') && c != '.') {
            evt.consume();//Si el caracter recibido es una letra o caracter, exepto puntos, lo comsume
        }else{//Si es un numero o punto
            for(int i = 0; i < cadena.length(); i++){
                aux = cadena.charAt(i);//Recorre la cadena para contar los puntos
                if(aux == '.'){
                    contPuntos++;//Si encuentra un punto se suma el contadorPuntos
                }
            }
            if(contPuntos == 1 && c == '.'){
                    evt.consume();//Si el contador de puntos es 1, significa que ya hay un punto y consumira cualquier otro
                                  //Tambien es importante detectar si se recibe un punto, ya que tambien se reciben numeros en el else,
                                  //los numeros no se consumen, con c == '.' confirmo de que solo consumira puntos
            }
        }
    }
    
    //Solo numeros enteros en campos
    private void soloEnteros(KeyEvent evt){
        char c = evt.getKeyChar();
        if(c < '0' || c > '9') evt.consume();
    }
    
    //Limitacion de logitud de datos
    private void limitarInsercion(int tamSQL, KeyEvent evt, JTextField campo){
        int tamCampo = campo.getText().length() + 1;

        if(tamCampo > tamSQL){
            evt.consume();
        }
    }
    
    
    //Eentos KeyTyped para solo enteros y flotantes
    //Extrusion
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
    //Tintas
    private void kgIniT1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgIniT1KeyTyped
        soloFlotantes(evt, kgIniT1);
    }//GEN-LAST:event_kgIniT1KeyTyped

    private void kgFinT1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgFinT1KeyTyped
        soloFlotantes(evt, kgFinT1);
    }//GEN-LAST:event_kgFinT1KeyTyped

    private void kgIniT2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgIniT2KeyTyped
        soloFlotantes(evt, kgIniT2);
    }//GEN-LAST:event_kgIniT2KeyTyped

    private void kgFinT2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgFinT2KeyTyped
        soloFlotantes(evt, kgFinT2);
    }//GEN-LAST:event_kgFinT2KeyTyped

    private void kgIniT3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgIniT3KeyTyped
        soloFlotantes(evt, kgIniT3);
    }//GEN-LAST:event_kgIniT3KeyTyped

    private void kgFinT3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgFinT3KeyTyped
        soloFlotantes(evt, kgFinT3);
    }//GEN-LAST:event_kgFinT3KeyTyped

    private void kgIniT4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgIniT4KeyTyped
        soloFlotantes(evt, kgIniT4);
    }//GEN-LAST:event_kgIniT4KeyTyped

    private void kgFinT4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgFinT4KeyTyped
        soloFlotantes(evt, kgFinT4);
    }//GEN-LAST:event_kgFinT4KeyTyped

    private void kgIniT5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgIniT5KeyTyped
        soloFlotantes(evt, kgIniT5);
    }//GEN-LAST:event_kgIniT5KeyTyped

    private void kgFinT5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgFinT5KeyTyped
        soloFlotantes(evt, kgFinT5);
    }//GEN-LAST:event_kgFinT5KeyTyped

    private void kgIniT6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgIniT6KeyTyped
       soloFlotantes(evt, kgIniT6);
    }//GEN-LAST:event_kgIniT6KeyTyped

    private void kgFinT6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgFinT6KeyTyped
        soloFlotantes(evt, kgFinT6);
    }//GEN-LAST:event_kgFinT6KeyTyped

    private void mezIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mezIniKeyTyped
        soloFlotantes(evt, mezIni);
    }//GEN-LAST:event_mezIniKeyTyped

    private void mezFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mezFinKeyTyped
        soloFlotantes(evt, mezFin);
    }//GEN-LAST:event_mezFinKeyTyped

    private void aceIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_aceIniKeyTyped
        soloFlotantes(evt, aceIni);
    }//GEN-LAST:event_aceIniKeyTyped

    private void aceFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_aceFinKeyTyped
        soloFlotantes(evt, aceFin);
    }//GEN-LAST:event_aceFinKeyTyped

    private void retIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_retIniKeyTyped
        soloFlotantes(evt, retIni);
    }//GEN-LAST:event_retIniKeyTyped

    private void retFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_retFinKeyTyped
        soloFlotantes(evt, retFin);
    }//GEN-LAST:event_retFinKeyTyped
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

    private void stcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stcKeyTyped
        soloFlotantes(evt, stc);
    }//GEN-LAST:event_stcKeyTyped

    private void costoGrabKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costoGrabKeyTyped
        soloFlotantes(evt, costoGrab);
    }//GEN-LAST:event_costoGrabKeyTyped

    private void costoDiseKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costoDiseKeyTyped
        soloFlotantes(evt, costoDise);
    }//GEN-LAST:event_costoDiseKeyTyped

    private void impBusMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_impBusMousePressed
        impBus.setText("");
    }//GEN-LAST:event_impBusMousePressed

    private void btnCostosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCostosActionPerformed
        
        comprobarVacio();
        String sql = "update impreso set sticky = "+stc.getText()+", costoDiseno = "+costoDise.getText()+" where idPar_fk = "+idPart+"";
        try {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            
            JOptionPane.showMessageDialog(null, "Se han guardado solo los costos", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al guardar costos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }    
    }//GEN-LAST:event_btnCostosActionPerformed

    private void impBusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impBusKeyPressed
        if(evt.getKeyCode() == com.sun.glass.events.KeyEvent.VK_ENTER){
            llenarTablaPedido();
            
            if(!(tablaPed.getRowCount() == 0)){
                tablaPed.requestFocus();
                tablaPed.changeSelection(0, 0, false, false);
            }
        }
    }//GEN-LAST:event_impBusKeyPressed

    private void tablaPedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPedKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_SPACE){
            obtenerSeleccionTablaPedido();
            
            if(!(tablaPart.getRowCount() == 0)){
                tablaPart.requestFocus();
                tablaPart.changeSelection(0, 0, false, false);
            }
        }
    }//GEN-LAST:event_tablaPedKeyPressed

    private void tablaPartKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPartKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_SPACE){
            obtenerSeleccionTablaPartida();
        }
    }//GEN-LAST:event_tablaPartKeyPressed

    private void tablaPartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPartMouseClicked

        obtenerSeleccionTablaPartida();
    }//GEN-LAST:event_tablaPartMouseClicked

    //Calcular monto total del pedido a base de los importes de todas las partidas
    private void establecerSubtotalPedido(Statement st){
        
        String sql = "select importe from partida where folio_fk = "+folio+"";
        float subtotalVar = 0;
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                //Aqui se empiezan a acumular los importes de las partidas
                subtotalVar = subtotalVar + Float.parseFloat(rs.getString("importe"));
            } 
            //System.out.println("Subtotal directo de importe: " + subtotalVar);
            actualizarSubtotalPedido(subtotalVar, st);//Update al campo subtotal para ingresar el nuevo monto
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar la partida (sub)" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //Se actualiza el subtotal de pedido
    private void actualizarSubtotalPedido(float sub, Statement st){
        
        float iva = 0.16f;
        float subGrab = sub;
        float anti  = 0f;
        float descuento = 0f;
        
        //Consulta para obtener los valores de: grabados, anticipo y descuento. Se realizaran operaciones
        String sql = "select grabados, anticipo, descuento from pedido where folio = "+folio+"";
        
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                //Se le suma el coste de grabados al subtotal
                subGrab = subGrab + Float.parseFloat(rs.getString("grabados"));
                //System.out.println("Grabados: "+rs.getString("grabados"));
                //Se guarda el valor de anticipo para usu posterior
                anti = Float.parseFloat(rs.getString("anticipo"));
                //Se guarda el vlor de descuento para uso posterior
                descuento = Float.parseFloat(rs.getString("descuento"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de costos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //Cuando ya se sumaron los grabados y el subtotal ahora se guarda en la base de datos
        sql = "update pedido set subtotal = "+subGrab+" where folio = "+folio+"";
        
        try {
            st.execute(sql);
            
            //System.out.println("Subtotal mas los grabados: " + subGrab);
            //Ahora paso por parametros el subtotal con grabados, anticipo y descuento
            calcularCostosDeSub(subGrab, anti, descuento, st);
            st.close();
            rs.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el subtotal", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Aqui se reciben todos los valores necesarios para realizar los calculos y actualizar la base de datos
    private void calcularCostosDeSub(float sub, float anticipo, float descuento, Statement st){
        
        float total = 0f;
        float iva = 0.16f;
        float subIva = 0f;
        float rest = 0f;
        
        subIva = sub * iva;//Se obtiene el iva del subtotal
        total = sub + subIva;//Se suma el iva y el subtotal para obtener el total
        
        rest = total - anticipo;//Se le resta el anticipo al total para obtener el resto
        rest = rest - descuento;//Se le resta el descuento al resto
        
        //total = (total - anticipo) - descuento;
        
        //System.out.println("Descuento: " + descuento);
        //System.out.println("Aniticipo: " + anticipo);
        //System.out.println("Total: "+total);
        //System.out.println("Resto: " + rest);
        String sql= "update pedido set total = "+total+", resto = "+rest+" where folio = "+folio+"";
        
        try {
            st.execute(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el total y resto", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    
    //Enumerar las opartidas existentes con los datos Hojas y De
    private void setHojas(){
        
        String sql = "select idPar from partida where folio_fk = "+folio+"";
        String sql1;
        int contadorRs = 0;
        int idPartida;
        
        Statement stAux = null;
        Statement st = null;
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.isAfterLast())
            {
                while(rs.next()){
                contadorRs++;
                idPartida = rs.getInt("idPar");
                sql1 = "update partida set hoja = "+contadorRs+" where idPar = "+idPartida+"";
                
                stAux = con.createStatement();
                stAux.execute(sql1);  
                stAux.close();
            }
            }
            
            
            rs = st.executeQuery(sql);
            if(rs.isAfterLast())
            {
                while(rs.next()){
                idPartida = rs.getInt("idPar");               
                sql1 = "update partida set de = "+contadorRs+" where idPar = "+idPartida+"";
                
                stAux = con.createStatement();
                stAux.execute(sql1);
                stAux.close();
            }
            }
            
            
            
            st.close();
            rs.close();
            //JOptionPane.showMessageDialog(null, "Las hojas se han actualizado", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar las hojas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }   
    }
    
    
    
    private void eliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarPActionPerformed
        eliminarP.setSelected(false);
        eliminarPartida();
        setTablePartidas(); 
        
        String sql = "select idPar from partida where folio_fk = "+folio+"";
        try
        {
            Statement stSub = con.createStatement();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                establecerSubtotalPedido(stSub);
                calculaKgDesperdicioPedido();
                sumaMaterialesPedido();
                calcularCostoTotalPe();
                calculaPyG();
            }
            
            setHojas();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_eliminarPActionPerformed

    private void opExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_opExtKeyTyped
        limitarInsercion(45, evt, opExt);
    }//GEN-LAST:event_opExtKeyTyped

    private void prov1ExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prov1ExtKeyTyped
        limitarInsercion(40, evt, prov1Ext);
    }//GEN-LAST:event_prov1ExtKeyTyped

    private void prov2ExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prov2ExtKeyTyped
        limitarInsercion(40, evt, prov2Ext);
    }//GEN-LAST:event_prov2ExtKeyTyped

    private void opImpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_opImpKeyTyped
        limitarInsercion(45, evt, opImp);
    }//GEN-LAST:event_opImpKeyTyped

    private void provImpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_provImpKeyTyped
        limitarInsercion(40, evt, provImp);
    }//GEN-LAST:event_provImpKeyTyped

    private void opBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_opBolKeyTyped
        limitarInsercion(45, evt, opBol);
    }//GEN-LAST:event_opBolKeyTyped

    private void provBolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_provBolKeyTyped
        limitarInsercion(40, evt, provBol);
    }//GEN-LAST:event_provBolKeyTyped

    private void impBusKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impBusKeyTyped
        limitarInsercion(40, evt, impBus);
    }//GEN-LAST:event_impBusKeyTyped

    private void t1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t1KeyTyped
        limitarInsercion(30, evt, t1);
    }//GEN-LAST:event_t1KeyTyped

    private void t2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t2KeyTyped
        limitarInsercion(30, evt, t2);
    }//GEN-LAST:event_t2KeyTyped

    private void t3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t3KeyTyped
        limitarInsercion(30, evt, t3);
    }//GEN-LAST:event_t3KeyTyped

    private void t4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t4KeyTyped
        limitarInsercion(30, evt, t4);
    }//GEN-LAST:event_t4KeyTyped

    private void t5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t5KeyTyped
        limitarInsercion(30, evt, t5);
    }//GEN-LAST:event_t5KeyTyped

    private void t6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t6KeyTyped
        limitarInsercion(30, evt, t6);
    }//GEN-LAST:event_t6KeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.setSelected(false);
        //Tintas de impreso
        comprobarVacio();
        t1St = t1.getText();
        pI1St = kgIniT1.getText();
        pF1St = kgFinT1.getText();
        t2St = t2.getText();
        pI2St = kgIniT2.getText();
        pF2St = kgFinT2.getText();
        t3St = t3.getText();
        pI3St = kgIniT3.getText();
        pF3St = kgFinT3.getText();
        t4St = t4.getText();
        pI4St = kgIniT4.getText();
        pF4St = kgFinT4.getText();
        t5St = t5.getText();
        pI5St = kgIniT5.getText();
        pF5St = kgFinT5.getText();
        t6St = t6.getText();
        pI6St = kgIniT6.getText();
        pF6St = kgFinT6.getText();
            
        iniMezSt = mezIni.getText();
        finMezSt = mezFin.getText();
        iniAceSt = aceIni.getText();
        finAceSt = aceFin.getText();
        iniRetSt = retIni.getText();
        finRetSt = retFin.getText();
        
        
        String sql = "update tintas set tinta1 = '"+t1St+"', pIni1 = "+pI1St+", pFin1 = "+pF1St+", tinta2 = '"+t2St+"', pIni2 = "+pI2St+", pFin2 = "+pF2St+"," +
            " tinta3 = '"+t3St+"', pIni3 = "+pI3St+", pFin3 = "+pF3St+", tinta4 = '"+t4St+"', pIni4 = "+pI4St+", pFin4 = "+pF4St+"," +
            " tinta5 = '"+t5St+"', pIni5 = "+pI5St+", pFin5 = "+pF5St+", tinta6 = '"+t6St+"', pIni6 = "+pI6St+", pFin6 = "+pF6St+"," +
            " iniMezcla = "+iniMezSt+", finMezcla = "+finMezSt+"," +
            " iniAcetato = "+iniAceSt+", finAcetato = "+finAceSt+"," +
            " iniRetard = "+iniRetSt+", finRetard = "+finRetSt+"" +
            " where idImp_fk = "+idImPrimera+"";
        
        try {
            st = con.createStatement();
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro de tintas: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            st.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro de tintas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void agEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agEActionPerformed
        agE.setSelected(false);
        comprobarVacio();
        kgESt = kgOpExt.getText();
        greniaESt = greExt.getText();
        opESt = opExt.getText();
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
            JOptionPane.showMessageDialog(null, "Se ha guardado el operador en Extrusion: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            
            sumaKilosEx(st);
            sumarCostosOperacionales(st, "extrusion", "operadorExt", "costoOpExt", "costoOpTotalExt", "idExt", "idExt_fk", idEx);
            sumarGrenias("operadorExt", "grenia", "idExt_fk", "extrusion", "greniaExt", "idExt", idEx);
            actualizarKgDes();
            actualizarPorcentajeDes();
            calculaCostoMaterialTotalExt();
            calculaHrTotalesPartida("operadorExt", "idExt_fk", idEx,"extrusion");
            calculaCostoPartida();
            calcularCostoUnitarioExt();
            calculaKgDesperdicioPedido();
            sumaMaterialesPedido();
            calcularCostoTotalPe();
            calculaPyG();
            
            vaciarOpE();
            //Aqui se sumaran las greñas();   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el operador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_agEActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
    }//GEN-LAST:event_formWindowClosing

    private void cambioModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambioModActionPerformed
        if(cambioMod.getText().equals("Agregar compras"))
        {
                panMaqExt.setVisible(true);
                panMaqImp.setVisible(true);
                panMaqBol.setVisible(true);
                panelCostos.setVisible(true);
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
                    ex.printStackTrace();
                }
    }//GEN-LAST:event_cambioModActionPerformed

    
    //Se vacean los campos de agregar operadores
    private void vaciarOpE(){
        Calendar cal = Calendar.getInstance(); 
        kgOpExt.setText("0");
        greExt.setText("0");
        opExt.setText("");
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
    
    private void vaciarOpI(){
        Calendar cal = Calendar.getInstance(); 
        kgOpIm.setText("0");
        greImp.setText("0");
        opImp.setText("");
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
    
    private void vaciarOpB(){
        Calendar cal = Calendar.getInstance(); 
        kgOpBol.setText("0");
        greBol.setText("0");
        suaje.setText("0");
        opBol.setText("");
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
   
    
    
    public void comprobarVacio(){
        
        //Campos de procesos
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
        if(porKgImp.getText().equals("") || porKgImp.getText().equals(".")){
            porKgImp.setText("0");
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
        if(costoDise.getText().equals("") || costoDise.getText().equals(".")){
            costoDise.setText("0");
        }
        if(costoGrab.getText().equals("") || costoGrab.getText().equals(".")){
            costoGrab.setText("0");
        }
        if(stc.getText().equals("") || stc.getText().equals(".")){
            stc.setText("0");
        }
        
        //Campos de tintas
        if(kgIniT1.getText().equals("") || kgIniT1.getText().equals(".")){
            kgIniT1.setText("0");
        }
        if(kgFinT1.getText().equals("") || kgFinT1.getText().equals(".")){
            kgFinT1.setText("0");
        }
        if(kgIniT2.getText().equals("") || kgIniT2.getText().equals(".")){
            kgIniT2.setText("0");
        }
        if(kgFinT2.getText().equals("") || kgFinT2.getText().equals(".")){
            kgFinT2.setText("0");
        }
        if(kgIniT3.getText().equals("") || kgIniT3.getText().equals(".")){
            kgIniT3.setText("0");
        }
        if(kgFinT3.getText().equals("") || kgFinT3.getText().equals(".")){
            kgFinT3.setText("0");
        }
        if(kgIniT4.getText().equals("") || kgIniT4.getText().equals(".")){
            kgIniT4.setText("0");
        }
        if(kgFinT4.getText().equals("") || kgFinT4.getText().equals(".")){
            kgFinT4.setText("0");
        }
        if(kgIniT5.getText().equals("") || kgIniT5.getText().equals(".")){
            kgIniT5.setText("0");
        }
        if(kgFinT5.getText().equals("") || kgFinT5.getText().equals(".")){
            kgFinT5.setText("0");
        }
        if(kgIniT6.getText().equals("") || kgIniT6.getText().equals(".")){
            kgIniT6.setText("0");
        }
        if(kgFinT6.getText().equals("") || kgFinT6.getText().equals(".")){
            kgFinT6.setText("0");
        }
        if(mezIni.getText().equals("") || mezIni.getText().equals(".")){
            mezIni.setText("0");
        }
        if(mezFin.getText().equals("") || mezFin.getText().equals(".")){
            mezFin.setText("0");
        }
        if(aceIni.getText().equals("") || aceIni.getText().equals(".")){
            aceIni.setText("0");
        }
        if(aceFin.getText().equals("") || aceFin.getText().equals(".")){
            aceFin.setText("0");
        }
        if(retIni.getText().equals("") || retIni.getText().equals(".")){
            retIni.setText("0");
        }
        if(retFin.getText().equals("") || retFin.getText().equals(".")){
            retFin.setText("0");
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
        if(costoGrab.getText().equals("") || costoGrab.getText().equals(".")){
            costoGrab.setText("0");
        }
        if(costoDise.getText().equals("") || costoDise.getText().equals(".")){
            costoDise.setText("0");
        }
        if(stc.getText().equals("") || stc.getText().equals(".")){
            stc.setText("0");
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
    
  
    //Listeners de los JTimeChoser
    int totalDeHoras = 0;
    
    private void listenersJTime(){
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
    
    //timechosers de extrusion
    private void hrIniEChange(){
        hrIni.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, 75f, costoOpExt);
                
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
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, 75f, costoOpExt);
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
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, 75f, costoOpExt);
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
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, 75f, costoOpExt); 
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
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, 75f, costoOpExt);
            }
        });
    }
    
    private void fFinExtChange(){
        fFinExt.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                calcularTotalhr(hrIni, hrFin, totalHrExt, hrMuertoExt, fIniExt, fFinExt, extHrExt, 75f, costoOpExt);
            }
        });
    }
    
    
    //timechosers de impreso
    private void hrIniIChange(){
        hrIniImp.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, 65f, costoOpImp);
                
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
                calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, 65f, costoOpImp);
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
                calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, 65f, costoOpImp);
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
                calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, 65f, costoOpImp);
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
               calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, 65f, costoOpImp);
            }
        });
    }
    
    private void fFinImpChange(){
        fFinImp.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                calcularTotalhr(hrIniImp, hrFinImp, totalHrImp, hrMuertoImp, fIniImp, fFinImp, extHrImp, 65f, costoOpImp);
            }
        });
    }
    
    
    //timechosers de bolseo
    private void hrIniBChange(){
        hrIniBol.getTimeField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, 50f, costoOpBol);
                
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
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, 50f, costoOpBol);
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
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, 50f, costoOpBol);
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
                calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, 50f, costoOpBol);
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
                 calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, 50f, costoOpBol);
             }
         });
     }
     
     private void fFinBolChange(){
         fFinBol.addCommitListener(new CommitListener() {

             @Override
             public void onCommit(CommitEvent ce) {
                 calcularTotalhr(hrIniBol, hrFinBol, totalHrBol, hrMuertoBol, fIniBol, fFinBol, extBol, 50f, costoOpBol);
             }
         });
     }
    
    DateTimeFormatter dtf;
    DateTime dtIni;
    DateTime dtFin;
    DateTime dtMuerto;
    DateTime aux;
    DateTime extTime;
    Period diff;
       
    private void calcularTotalhr(JTimeChooser tIni, JTimeChooser tFin, JTimeChooser total, JTimeChooser muerto, DateChooserCombo fini, DateChooserCombo fFin,
            JTimeChooser extra, float costoPorHr, JTextField costo){
         
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
        
        /*System.out.println("----------------");
        System.out.println("Minutos: "+minutos);
        System.out.println("Horas: "+horas);*/
        
        if(horas > 9 && minNoAcum > 9){
            horasSt = (horas + ":" + minNoAcum + ":00");
        }else if(horas < 10 && minNoAcum < 10){
            horasSt = ("0" + horas + ":" + "0" + minNoAcum + ":00");
        }else if(horas > 9 && minNoAcum < 10){
            horasSt = (horas + ":" + "0" + minNoAcum + ":00");
        }else if(horas < 10 && minNoAcum > 9){
            horasSt = ("0" + horas + ":" + minNoAcum + ":00");
        }
        
        /*
        System.out.println("----------------");
        System.out.println("Minutos: "+minutos);
        System.out.println("Horas: "+horas);
        System.out.println(horasSt);*/
        
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
                //System.out.println(horasSt);

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
        
        calcularCostoOpMinutos(minutosMenosExtra, costoPorHr, tiempoExtra, minutos, costo);
        
    }
    
    private void calculaHrTotalesPartida(String tablaOperadorProceso,String idProcesoForaneo, int idProcesoGlobal, String tablaProceso)
    {
        String horaSt = "0";
        int horaInt = 0;
        int minInt = 0;
        int sumatoriaHr = 0;
        int sumatoriaMin = 0;
        String sql = "select totalHoras from "+tablaOperadorProceso+" where "+idProcesoForaneo+" = "+idProcesoGlobal+"";
        try
        {
           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next())
           {
               //para utilizar el .substring se debe anotrar la posicion el la que empieza y una posicion despues de donde termina
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
    
    
    private String getHrDT(String tiempo){
        
        String hora = "";
        hora = hora + tiempo.charAt(0) + tiempo.charAt(1);
        return hora;
    }
    
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
    
    
    private void calcularCostoOpMinutos(int minutos, float costoPorHora, int minutosExtra, int minutosTotales, JTextField costoField){
        
        float costoPorMinutos = 0f;
        float costoTotal = 0f;
        
        if(minutosExtra > minutosTotales){
            costoTotal = 0;
        }else{
            costoPorMinutos = costoPorHora / 60;
            //System.out.println("__________________________");
            //System.out.println(costoPorHora + " = " + costoPorHora + " / " + minutos);
            costoTotal = (costoPorMinutos * minutos) + ((costoPorMinutos * minutosExtra) * 2);
            //System.out.println(costoTotal + " = " + costoPorMinutos + " X " + minutos);
        }
        
        costoField.setText(String.valueOf(costoTotal));
    }
    
    
    public void vacearComponentes(){
        
        opExt.setText("");
        opImp.setText("");
        opBol.setText("");
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
        costoDise.setText("");
        costoGrab.setText("");
        stc.setText("");
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
        provBol.setText("");
        porKgImp.setText("");
        porKgBol.setText("");
        kgImp.setText("");
        kgBol.setText("");
        pzsBol.setText("");
        
        t1.setText("");
        t2.setText("");
        t3.setText("");
        t4.setText("");
        t5.setText("");
        t6.setText("");
        kgIniT1.setText("");
        kgIniT2.setText("");
        kgIniT3.setText("");
        kgIniT4.setText("");
        kgIniT5.setText("");
        kgIniT6.setText("");
        kgFinT1.setText("");
        kgFinT2.setText("");
        kgFinT3.setText("");
        kgFinT4.setText("");
        kgFinT5.setText("");
        kgFinT6.setText("");
        mezIni.setText("");
        mezFin.setText("");
        aceIni.setText("");
        aceFin.setText("");
        retIni.setText("");
        retFin.setText("");  
        
        modPed.setRowCount(0);
        modPart.setRowCount(0);
        
        savePro.setEnabled(false);
        eliminarP.setEnabled(false);
        btnCostos.setEnabled(false);
        jButton1.setEnabled(false);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aceFin;
    private javax.swing.JTextField aceIni;
    private javax.swing.JButton agB;
    private javax.swing.JButton agE;
    private javax.swing.JButton agI;
    private javax.swing.JButton btnCostos;
    private javax.swing.JButton cambioMod;
    private javax.swing.JTextField costoDise;
    private javax.swing.JTextField costoGrab;
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
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
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField kgBol;
    private javax.swing.JTextField kgFinT1;
    private javax.swing.JTextField kgFinT2;
    private javax.swing.JTextField kgFinT3;
    private javax.swing.JTextField kgFinT4;
    private javax.swing.JTextField kgFinT5;
    private javax.swing.JTextField kgFinT6;
    private javax.swing.JTextField kgImp;
    private javax.swing.JTextField kgIniT1;
    private javax.swing.JTextField kgIniT2;
    private javax.swing.JTextField kgIniT3;
    private javax.swing.JTextField kgIniT4;
    private javax.swing.JTextField kgIniT5;
    private javax.swing.JTextField kgIniT6;
    private javax.swing.JTextField kgOpBol;
    private javax.swing.JTextField kgOpExt;
    private javax.swing.JTextField kgOpIm;
    private javax.swing.JLabel labelxd;
    private javax.swing.JComboBox listaMat;
    private javax.swing.JTextField maqBol;
    private javax.swing.JTextField maqExt;
    private javax.swing.JTextField maqImp;
    private javax.swing.JTextField mezFin;
    private javax.swing.JTextField mezIni;
    private javax.swing.JTextField opBol;
    private javax.swing.JTextField opExt;
    private javax.swing.JTextField opImp;
    private javax.swing.JTabbedPane paIm;
    private javax.swing.JTabbedPane paPro;
    private javax.swing.JPanel panBol;
    private javax.swing.JPanel panEx;
    private javax.swing.JPanel panImp;
    private javax.swing.JPanel panMaqBol;
    private javax.swing.JPanel panMaqExt;
    private javax.swing.JPanel panMaqImp;
    private javax.swing.JPanel panelCostos;
    private javax.swing.JTextField porKg1Ext;
    private javax.swing.JTextField porKg2Ext;
    private javax.swing.JTextField porKgBol;
    private javax.swing.JTextField porKgImp;
    private javax.swing.JTextField proM1;
    private javax.swing.JTextField proM2;
    private javax.swing.JTextField prov1Ext;
    private javax.swing.JTextField prov2Ext;
    private javax.swing.JTextField provBol;
    private javax.swing.JTextField provImp;
    private javax.swing.JTextField pzsBol;
    private javax.swing.JTextField retFin;
    private javax.swing.JTextField retIni;
    private javax.swing.JToggleButton savePro;
    private javax.swing.JTextField stc;
    private javax.swing.JTextField suaje;
    private javax.swing.JTextField t1;
    private javax.swing.JTextField t2;
    private javax.swing.JTextField t3;
    private javax.swing.JTextField t4;
    private javax.swing.JTextField t5;
    private javax.swing.JTextField t6;
    private javax.swing.JTable tablaPart;
    private javax.swing.JTable tablaPed;
    private lu.tudor.santec.jtimechooser.JTimeChooser totalHrBol;
    private lu.tudor.santec.jtimechooser.JTimeChooser totalHrExt;
    private lu.tudor.santec.jtimechooser.JTimeChooser totalHrImp;
    // End of variables declaration//GEN-END:variables
}





