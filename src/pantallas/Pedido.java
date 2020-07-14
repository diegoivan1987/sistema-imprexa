
package pantallas;

import datechooser.events.CommitEvent;
import datechooser.events.CommitListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Pedido extends javax.swing.JFrame { //permite guadar o modificar un pedido y agregarle partidas
    
    
    //Variables de pantallas
    Principal prin;
    Procesos pro;
    GastosFijos gf;
    Connection con;
    //variables globales
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//formato general de fechas
    ResultSet rs;
    Statement st;
    Font fuenteTablas;
    
    
    String impSt=null, estatusSt=null,desSt=null, kgSt=null, medSt=null, devSt=null, disSt=null, pigSt=null, tipoSt=null, selloSt=null, 
            hojaSt=null, deSt=null, mat1St=null, cal1St=null, mat2St=null, cal2St=null, tinta1St=null, tinta2St=null, fInSt=null, fComSt=null, 
            fPagoSt=null, fTerminoSt = null, grabSt=null, pUnitSt=null, subSt=null, totSt=null, diseSt=null,antiSt=null, restoSt=null, descuSt=null, costeGrabSt=null, 
            costeDisSt=null, piezasSt=null, importeSt=null, kgParidaSt=null, pzFinalesSt = null, autorizoSt = null;
    
    float grabados = 0, precioUnitario=0, kilos=0, totalF=0, restoF=0, subF=0, iva=0 , diseF = 0, antiF=0, descuF=0;
    int porcentajeIVA = 0, piezasI = 0, pzFinalesI = 0;
    float importeF = 0;
    
    //Variables de las tintas
    String t1St=null, pI1St=null, pF1St=null, t2St=null, pI2St=null, pF2St=null, t3St=null, pI3St=null, pF3St=null, 
            t4St=null, pI4St=null, pF4St=null, t5St=null, pI5St=null, pF5St=null, t6St=null, pI6St=null, pF6St=null, 
            iniMezSt=null, finMezSt=null, iniAceSt=null, finAceSt=null, iniRetSt=null, finRetSt=null, iniSolventeSt=null, 
            finSolventeSt=null, iniBarSt = null, finBarSt = null, stickySt = null;
    
    int idCliente = 0;
    int folioId = 0;
    int folioMod = 0;
    
    float respaldoSub = 0f; //Para guardar subtotal con grabados restados, pues si se modifica grabados se tendra que sumar el valor nuevo
    
    String PocM1St=null, PocM2St = null, opeExtSt=null, greniaExtSt, maquinaExtSt, horaIniExtSt, fechaIniExtSt;
    
    DefaultTableModel modelo, modeloP, modeloMod;//se crean los modelos de las tablas
    JTableHeader thCli, thMod, thPed;//se crean los encabezados de las tablass
    
    int indicadorParaImporte = 0;
    
    int ancho, alto;//son las medidas de la ventana
    
    public Pedido(Connection con) {
        initComponents();
        
        //inhabilitamos boton de guardar cambios en tintas
        guardCamTintas.setEnabled(false);
        guardCamTintas.setVisible(false);
        comprobarVacio();
        porIVA.setSelectedIndex(0);//se inicializa el iva en 0
        estatus.setSelectedIndex(0);//se inicializa el estatus
        ancho = this.getSize().width;
        alto = this.getSize().height;
        
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.setResizable(false);
        fuenteTablas = new Font("Dialog", Font.BOLD, 12);
        
        onChangePU();
        onChangeKilos();
        onChangePz(); 
        onChangePzFinales();
        onChangeFini();
        
        llenarListas();//se llenan las listas desplegables de materiales, status y sello
        //Tabla que muestra los clientes buscados, se establece que no es editable
        modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.setColumnIdentifiers(new Object[]{"Id", "Nombre"});//se establecen los titulos de las columnas
        tablaC.setModel(modelo);//se le asigna el modelo a la tabla de clientes
        
        //Tabla que muestra los pedidos en cuanto a su nombre de impresion, se etablece que no es editable
        modeloP = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloP.setColumnIdentifiers(new Object[]{"Folio", "Impresion", "Cliente", "Fecha Ingreso"});//se le asignan nombres a las columnas
        tablaP.setModel(modeloP);//se le asigna modelo a la tabla de modelos
        
        //tabla de modificacion de pedidos, se establece que no es editable
        modeloMod = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloMod.setColumnIdentifiers(new Object[]{"Folio", "Impresion", "Cliente", "Fecha Ingreso"});//se le asignan titulos a las columnas
        tablaModPed.setModel(modeloMod);//se le asigna un modelo a la tabla de modificacion de pedidos
        
        disenoTablas();//se le asigna un diseño a las tablas
        
        //Se establece un formato de fecha a los campos de la ventana
        fIn2.setDateFormat(df);
        fC2.setDateFormat(df);
        fP2.setDateFormat(df);
        fT.setDateFormat(df);
        autoCalendario.setDateFormat(df);
        //se inicializan en una fecha imposible, para diferencias cuando se cambian
        setFechaImposible();
        
        importe.setEditable(false);//no se podran ingresar datos en el campo importe
        
        this.con = con;
        //se deshabilitan los botones de guardado al principio
        saveP.setEnabled(false);
        savePart.setEnabled(false);
        //elementos de la modificacion de pedido
        tablaModPed.setVisible(false);
        btnBusPed.setEnabled(false);
        textBusPed.setEnabled(false);
        btnMod.setVisible(false);
        btnMod.setEnabled(false);
        panelMod.setVisible(false);
        
        cliente.setEditable(false);//evita que el campo que muestra el cliente seleccionado sea editado
        folioVis.setEditable(false);
        panelCliente.setVisible(true);  
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane3 = new javax.swing.JTabbedPane();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jTextField83 = new javax.swing.JTextField();
        jTextField84 = new javax.swing.JTextField();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jTextField85 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        paPed = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        panelCliente = new javax.swing.JPanel();
        nomCli = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaC = new javax.swing.JTable();
        panelMod = new javax.swing.JPanel();
        btnBusPed = new javax.swing.JButton();
        textBusPed = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaModPed = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        dev = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        fIn2 = new datechooser.beans.DateChooserCombo();
        fC2 = new datechooser.beans.DateChooserCombo();
        jLabel157 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        fP2 = new datechooser.beans.DateChooserCombo();
        sug = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        anti = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        decu = new javax.swing.JTextField();
        grab = new javax.swing.JTextField();
        btnMod = new javax.swing.JButton();
        saveP = new javax.swing.JButton();
        fT = new datechooser.beans.DateChooserCombo();
        jLabel99 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        porIVA = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        disenio = new javax.swing.JTextField();
        imp = new javax.swing.JTextField();
        jLabel120 = new javax.swing.JLabel();
        estatus = new javax.swing.JComboBox();
        autoCalendario = new datechooser.beans.DateChooserCombo();
        jPanel2 = new javax.swing.JPanel();
        panPart = new javax.swing.JPanel();
        busPedido = new javax.swing.JToggleButton();
        nomImpresion = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaP = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        cal2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        folioVis = new javax.swing.JTextField();
        jLabel170 = new javax.swing.JLabel();
        pig = new javax.swing.JTextField();
        tp = new javax.swing.JTextField();
        jLabel168 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        med = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        desa = new javax.swing.JTextField();
        jLabel169 = new javax.swing.JLabel();
        sello = new javax.swing.JComboBox();
        jLabel171 = new javax.swing.JLabel();
        mat1 = new javax.swing.JComboBox();
        cal1 = new javax.swing.JTextField();
        jLabel174 = new javax.swing.JLabel();
        jLabel172 = new javax.swing.JLabel();
        mat2 = new javax.swing.JComboBox();
        jLabel173 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        c11 = new javax.swing.JTextField();
        c21 = new javax.swing.JTextField();
        c12 = new javax.swing.JTextField();
        c22 = new javax.swing.JTextField();
        c13 = new javax.swing.JTextField();
        c23 = new javax.swing.JTextField();
        c14 = new javax.swing.JTextField();
        c24 = new javax.swing.JTextField();
        c15 = new javax.swing.JTextField();
        c25 = new javax.swing.JTextField();
        c16 = new javax.swing.JTextField();
        c26 = new javax.swing.JTextField();
        precioUni = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        pz = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        kgPart = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        importe = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        checkPz = new javax.swing.JCheckBox();
        checkKg = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        savePart = new javax.swing.JToggleButton();
        pzFinales = new javax.swing.JTextField();
        checkPzFinales = new javax.swing.JCheckBox();
        limpiar = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        kgFinT3 = new javax.swing.JTextField();
        jLabel118 = new javax.swing.JLabel();
        retIni = new javax.swing.JTextField();
        kgIniT3 = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        aceIni = new javax.swing.JTextField();
        barIni = new javax.swing.JTextField();
        listaTintas1 = new javax.swing.JComboBox();
        listaTintas2 = new javax.swing.JComboBox();
        jLabel55 = new javax.swing.JLabel();
        kgFinT5 = new javax.swing.JTextField();
        jLabel112 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        kgIniT4 = new javax.swing.JTextField();
        jLabel152 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        kgIniT2 = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        kgFinT6 = new javax.swing.JTextField();
        aceFin = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        solventeIni = new javax.swing.JTextField();
        listaTintas3 = new javax.swing.JComboBox();
        barFin = new javax.swing.JTextField();
        jLabel154 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        retFin = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        listaTintas4 = new javax.swing.JComboBox();
        kgFinT1 = new javax.swing.JTextField();
        jLabel123 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        kgFinT4 = new javax.swing.JTextField();
        kgFinT2 = new javax.swing.JTextField();
        jLabel126 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        solventeFin = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        kgIniT5 = new javax.swing.JTextField();
        sticky = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        mezIni = new javax.swing.JTextField();
        jLabel114 = new javax.swing.JLabel();
        listaTintas6 = new javax.swing.JComboBox();
        jLabel116 = new javax.swing.JLabel();
        kgIniT1 = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        kgIniT6 = new javax.swing.JTextField();
        mezFin = new javax.swing.JTextField();
        jLabel159 = new javax.swing.JLabel();
        listaTintas5 = new javax.swing.JComboBox();
        guardCamTintas = new javax.swing.JButton();
        guardarTintas = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        regP = new javax.swing.JButton();
        abreProcesos = new javax.swing.JToggleButton();
        modPed = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        jLabel108.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel108.setText("Tinta 1");

        jLabel109.setText("Nombre");

        jTextField83.setText("jTextField41");

        jTextField84.setText("jTextField43");

        jLabel110.setText("Inicio");

        jLabel111.setText("Final");

        jTextField85.setText("jTextField44");

        jLabel9.setText("jLabel9");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pedidos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        paPed.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        paPed.setToolTipText("");

        panelCliente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelCliente.setPreferredSize(new java.awt.Dimension(573, 140));

        nomCli.setForeground(new java.awt.Color(0, 153, 153));
        nomCli.setText("Buscar un Cliente");
        nomCli.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                nomCliCaretUpdate(evt);
            }
        });
        nomCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nomCliMousePressed(evt);
            }
        });
        nomCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nomCliKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomCliKeyTyped(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 102, 153));
        jLabel6.setText("Cliente:");

        cliente.setBackground(new java.awt.Color(255, 255, 153));
        cliente.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cliente.setForeground(new java.awt.Color(51, 102, 0));
        cliente.setText("Selecciona un Cliente");
        cliente.setPreferredSize(new java.awt.Dimension(128, 20));

        tablaC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {}
            },
            new String [] {

            }
        ));
        tablaC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaCMouseClicked(evt);
            }
        });
        tablaC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaCKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tablaC);

        javax.swing.GroupLayout panelClienteLayout = new javax.swing.GroupLayout(panelCliente);
        panelCliente.setLayout(panelClienteLayout);
        panelClienteLayout.setHorizontalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClienteLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(panelClienteLayout.createSequentialGroup()
                        .addComponent(nomCli, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        panelClienteLayout.setVerticalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClienteLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(nomCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelMod.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelMod.setPreferredSize(new java.awt.Dimension(573, 140));

        btnBusPed.setBackground(new java.awt.Color(51, 51, 51));
        btnBusPed.setForeground(new java.awt.Color(255, 255, 255));
        btnBusPed.setText("Buscar");
        btnBusPed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusPedActionPerformed(evt);
            }
        });

        textBusPed.setForeground(new java.awt.Color(0, 153, 153));
        textBusPed.setText("Nombre de impresión");
        textBusPed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                textBusPedMousePressed(evt);
            }
        });
        textBusPed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textBusPedKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBusPedKeyTyped(evt);
            }
        });

        tablaModPed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaModPed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaModPedMouseClicked(evt);
            }
        });
        tablaModPed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaModPedKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tablaModPed);

        javax.swing.GroupLayout panelModLayout = new javax.swing.GroupLayout(panelMod);
        panelMod.setLayout(panelModLayout);
        panelModLayout.setHorizontalGroup(
            panelModLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelModLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModLayout.createSequentialGroup()
                        .addComponent(btnBusPed, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textBusPed, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelModLayout.setVerticalGroup(
            panelModLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelModLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelModLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(textBusPed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBusPed))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setPreferredSize(new java.awt.Dimension(573, 245));

        jLabel97.setForeground(new java.awt.Color(0, 102, 153));
        jLabel97.setText("Impresión:");

        jLabel5.setForeground(new java.awt.Color(0, 102, 153));
        jLabel5.setText("Autorizó:");

        jLabel150.setForeground(new java.awt.Color(0, 102, 153));
        jLabel150.setText("Devolución:");

        dev.setForeground(new java.awt.Color(0, 153, 153));
        dev.setPreferredSize(new java.awt.Dimension(70, 20));
        dev.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                devKeyTyped(evt);
            }
        });

        jLabel95.setForeground(new java.awt.Color(0, 102, 153));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("Fecha Ingreso:");

        jLabel157.setForeground(new java.awt.Color(0, 102, 153));
        jLabel157.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel157.setText("Fecha Compromiso:");

        jLabel158.setForeground(new java.awt.Color(0, 102, 153));
        jLabel158.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel158.setText("Fecha de Pago:");

        sug.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        sug.setForeground(new java.awt.Color(255, 51, 51));
        sug.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sug.setText("Sugerencia");

        jLabel24.setForeground(new java.awt.Color(0, 102, 153));
        jLabel24.setText("Grabados: $");

        jLabel28.setForeground(new java.awt.Color(0, 102, 153));
        jLabel28.setText("Anticipo: $");

        anti.setForeground(new java.awt.Color(0, 153, 153));
        anti.setText("0");
        anti.setPreferredSize(new java.awt.Dimension(40, 24));
        anti.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                antiKeyTyped(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(0, 102, 153));
        jLabel30.setText("Descuento: $");

        decu.setForeground(new java.awt.Color(0, 153, 153));
        decu.setText("0");
        decu.setPreferredSize(new java.awt.Dimension(40, 24));
        decu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                decuKeyTyped(evt);
            }
        });

        grab.setForeground(new java.awt.Color(0, 153, 153));
        grab.setText("0");
        grab.setPreferredSize(new java.awt.Dimension(56, 24));
        grab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                grabKeyTyped(evt);
            }
        });

        btnMod.setBackground(new java.awt.Color(51, 51, 51));
        btnMod.setForeground(new java.awt.Color(255, 255, 255));
        btnMod.setText("Guardar Cambios");
        btnMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModActionPerformed(evt);
            }
        });

        saveP.setBackground(new java.awt.Color(51, 51, 51));
        saveP.setForeground(new java.awt.Color(255, 255, 255));
        saveP.setText("Guardar Pedido");
        saveP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePActionPerformed(evt);
            }
        });

        jLabel99.setForeground(new java.awt.Color(0, 102, 153));
        jLabel99.setText("Fecha de termino:");

        jLabel25.setForeground(new java.awt.Color(0, 102, 153));
        jLabel25.setText("IVA de:");

        porIVA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "8", "16" }));
        porIVA.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel26.setForeground(new java.awt.Color(0, 102, 153));
        jLabel26.setText("%");

        jLabel27.setForeground(new java.awt.Color(0, 102, 153));
        jLabel27.setText("Diseño: $");

        disenio.setForeground(new java.awt.Color(0, 153, 153));
        disenio.setText("0");
        disenio.setPreferredSize(new java.awt.Dimension(40, 24));
        disenio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                disenioKeyTyped(evt);
            }
        });

        imp.setForeground(new java.awt.Color(0, 153, 153));
        imp.setPreferredSize(new java.awt.Dimension(173, 20));
        imp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                impKeyTyped(evt);
            }
        });

        jLabel120.setForeground(new java.awt.Color(0, 102, 153));
        jLabel120.setText("Estatus:");

        estatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DISEÑO", "PROCESO", "COBRANZA", "PAGADO" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel120)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(estatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel97)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(imp, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel150)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(autoCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnMod, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveP, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(grab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(disenio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(anti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(decu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(porIVA, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel26)))))
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel95)
                            .addComponent(fIn2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(sug))
                            .addComponent(jLabel157)
                            .addComponent(fC2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fP2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel158))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel99)
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(fT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel97)
                        .addComponent(imp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(autoCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel120)
                            .addComponent(estatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel95)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fIn2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel157)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fC2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel99)
                                .addGap(7, 7, 7)
                                .addComponent(fT, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel158)
                                .addGap(7, 7, 7)
                                .addComponent(fP2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sug, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel150))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(grab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(disenio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(decu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(porIVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveP)
                    .addComponent(btnMod))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        paPed.addTab("Pedido", jPanel4);

        panPart.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panPart.setPreferredSize(new java.awt.Dimension(573, 127));

        busPedido.setBackground(new java.awt.Color(51, 51, 51));
        busPedido.setForeground(new java.awt.Color(255, 255, 255));
        busPedido.setText("Buscar");
        busPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busPedidoActionPerformed(evt);
            }
        });

        nomImpresion.setForeground(new java.awt.Color(0, 153, 153));
        nomImpresion.setText("Impresion");
        nomImpresion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nomImpresionMousePressed(evt);
            }
        });
        nomImpresion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nomImpresionKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomImpresionKeyTyped(evt);
            }
        });

        tablaP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPMouseClicked(evt);
            }
        });
        tablaP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaPKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tablaP);

        javax.swing.GroupLayout panPartLayout = new javax.swing.GroupLayout(panPart);
        panPart.setLayout(panPartLayout);
        panPartLayout.setHorizontalGroup(
            panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                    .addGroup(panPartLayout.createSequentialGroup()
                        .addComponent(busPedido)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nomImpresion)))
                .addContainerGap())
        );
        panPartLayout.setVerticalGroup(
            panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panPartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(busPedido))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel6.setPreferredSize(new java.awt.Dimension(573, 419));

        cal2.setForeground(new java.awt.Color(0, 153, 153));
        cal2.setPreferredSize(new java.awt.Dimension(154, 20));
        cal2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cal2KeyTyped(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 102, 153));
        jLabel7.setText("Folio:");

        folioVis.setBackground(new java.awt.Color(255, 255, 153));
        folioVis.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        folioVis.setForeground(new java.awt.Color(51, 102, 0));
        folioVis.setText("Folio");

        jLabel170.setForeground(new java.awt.Color(0, 102, 153));
        jLabel170.setText("Pigmento:");

        pig.setForeground(new java.awt.Color(0, 153, 153));
        pig.setPreferredSize(new java.awt.Dimension(154, 20));
        pig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pigKeyTyped(evt);
            }
        });

        tp.setForeground(new java.awt.Color(0, 153, 153));
        tp.setPreferredSize(new java.awt.Dimension(154, 20));
        tp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tpKeyTyped(evt);
            }
        });

        jLabel168.setForeground(new java.awt.Color(0, 102, 153));
        jLabel168.setText("Tipo:");

        jLabel166.setForeground(new java.awt.Color(0, 102, 153));
        jLabel166.setText("Medida: CM");

        med.setForeground(new java.awt.Color(0, 153, 153));
        med.setPreferredSize(new java.awt.Dimension(154, 20));
        med.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                medKeyTyped(evt);
            }
        });

        jLabel98.setForeground(new java.awt.Color(0, 102, 153));
        jLabel98.setText("Desarrollo:");

        desa.setForeground(new java.awt.Color(0, 153, 153));
        desa.setText("0");
        desa.setPreferredSize(new java.awt.Dimension(154, 20));
        desa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                desaKeyTyped(evt);
            }
        });

        jLabel169.setForeground(new java.awt.Color(0, 102, 153));
        jLabel169.setText("Sello:");

        sello.setForeground(new java.awt.Color(0, 153, 153));
        sello.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Sello" }));
        sello.setPreferredSize(new java.awt.Dimension(154, 20));

        jLabel171.setForeground(new java.awt.Color(0, 102, 153));
        jLabel171.setText("Material 1:");

        mat1.setForeground(new java.awt.Color(0, 153, 153));
        mat1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Material" }));
        mat1.setPreferredSize(new java.awt.Dimension(154, 20));

        cal1.setForeground(new java.awt.Color(0, 153, 153));
        cal1.setPreferredSize(new java.awt.Dimension(154, 20));
        cal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cal1KeyTyped(evt);
            }
        });

        jLabel174.setForeground(new java.awt.Color(0, 102, 153));
        jLabel174.setText("Calibre 1:");

        jLabel172.setForeground(new java.awt.Color(0, 102, 153));
        jLabel172.setText("Material 2:");

        mat2.setForeground(new java.awt.Color(0, 153, 153));
        mat2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Material" }));
        mat2.setPreferredSize(new java.awt.Dimension(154, 20));

        jLabel173.setForeground(new java.awt.Color(0, 102, 153));
        jLabel173.setText("Calibre 2:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 153));
        jLabel3.setText("Cara1");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 153));
        jLabel4.setText("Cara2");

        c11.setForeground(new java.awt.Color(0, 153, 153));
        c11.setBorder(null);
        c11.setPreferredSize(new java.awt.Dimension(154, 20));
        c11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c11KeyTyped(evt);
            }
        });

        c21.setForeground(new java.awt.Color(0, 153, 153));
        c21.setBorder(null);
        c21.setPreferredSize(new java.awt.Dimension(154, 20));
        c21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c21KeyTyped(evt);
            }
        });

        c12.setForeground(new java.awt.Color(0, 153, 153));
        c12.setBorder(null);
        c12.setPreferredSize(new java.awt.Dimension(154, 20));
        c12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c12KeyTyped(evt);
            }
        });

        c22.setForeground(new java.awt.Color(0, 153, 153));
        c22.setBorder(null);
        c22.setPreferredSize(new java.awt.Dimension(154, 20));
        c22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c22KeyTyped(evt);
            }
        });

        c13.setForeground(new java.awt.Color(0, 153, 153));
        c13.setBorder(null);
        c13.setPreferredSize(new java.awt.Dimension(154, 20));
        c13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c13KeyTyped(evt);
            }
        });

        c23.setForeground(new java.awt.Color(0, 153, 153));
        c23.setBorder(null);
        c23.setPreferredSize(new java.awt.Dimension(154, 20));
        c23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c23KeyTyped(evt);
            }
        });

        c14.setForeground(new java.awt.Color(0, 153, 153));
        c14.setBorder(null);
        c14.setPreferredSize(new java.awt.Dimension(154, 20));
        c14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c14KeyTyped(evt);
            }
        });

        c24.setForeground(new java.awt.Color(0, 153, 153));
        c24.setBorder(null);
        c24.setPreferredSize(new java.awt.Dimension(154, 20));
        c24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c24KeyTyped(evt);
            }
        });

        c15.setForeground(new java.awt.Color(0, 153, 153));
        c15.setBorder(null);
        c15.setPreferredSize(new java.awt.Dimension(154, 20));
        c15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c15KeyTyped(evt);
            }
        });

        c25.setForeground(new java.awt.Color(0, 153, 153));
        c25.setBorder(null);
        c25.setPreferredSize(new java.awt.Dimension(154, 20));
        c25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c25KeyTyped(evt);
            }
        });

        c16.setForeground(new java.awt.Color(0, 153, 153));
        c16.setBorder(null);
        c16.setPreferredSize(new java.awt.Dimension(154, 20));
        c16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c16KeyTyped(evt);
            }
        });

        c26.setForeground(new java.awt.Color(0, 153, 153));
        c26.setBorder(null);
        c26.setPreferredSize(new java.awt.Dimension(154, 20));
        c26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c26KeyTyped(evt);
            }
        });

        precioUni.setForeground(new java.awt.Color(0, 153, 153));
        precioUni.setText("0");
        precioUni.setPreferredSize(new java.awt.Dimension(68, 20));
        precioUni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precioUniKeyTyped(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 102, 153));
        jLabel8.setText("$");

        pz.setForeground(new java.awt.Color(0, 153, 153));
        pz.setText("0");
        pz.setPreferredSize(new java.awt.Dimension(68, 20));
        pz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pzKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 102, 153));
        jLabel10.setText("Kg");

        kgPart.setForeground(new java.awt.Color(0, 153, 153));
        kgPart.setText("0");
        kgPart.setPreferredSize(new java.awt.Dimension(68, 20));
        kgPart.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgPartKeyTyped(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(0, 102, 153));
        jLabel11.setText("$");

        importe.setBackground(new java.awt.Color(255, 255, 153));
        importe.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        importe.setForeground(new java.awt.Color(51, 102, 0));
        importe.setText("0");
        importe.setPreferredSize(new java.awt.Dimension(68, 20));
        importe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                importeKeyTyped(evt);
            }
        });

        jLabel38.setForeground(new java.awt.Color(0, 102, 153));
        jLabel38.setText("Precio Unitario:");

        checkPz.setForeground(new java.awt.Color(0, 102, 153));
        checkPz.setText("Piezas");
        checkPz.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkPzItemStateChanged(evt);
            }
        });

        checkKg.setForeground(new java.awt.Color(0, 102, 153));
        checkKg.setText("Kilos");
        checkKg.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkKgItemStateChanged(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Importe:");

        savePart.setBackground(new java.awt.Color(51, 51, 51));
        savePart.setForeground(new java.awt.Color(255, 255, 255));
        savePart.setText("Guardar Partida");
        savePart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePartActionPerformed(evt);
            }
        });

        pzFinales.setForeground(new java.awt.Color(0, 153, 153));
        pzFinales.setText("0");
        pzFinales.setPreferredSize(new java.awt.Dimension(73, 20));
        pzFinales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pzFinalesKeyTyped(evt);
            }
        });

        checkPzFinales.setForeground(new java.awt.Color(0, 102, 153));
        checkPzFinales.setText("Pz Finales");
        checkPzFinales.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkPzFinalesItemStateChanged(evt);
            }
        });

        limpiar.setBackground(new java.awt.Color(51, 51, 51));
        limpiar.setForeground(new java.awt.Color(255, 255, 255));
        limpiar.setText("Limpiar campos");
        limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(precioUni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(checkPz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kgPart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(checkKg, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(importe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(checkPzFinales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pzFinales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(jLabel3))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(c11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 86, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel4))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(c21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(c26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel171)
                                    .addComponent(jLabel172))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mat2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mat1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(med, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(folioVis, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel170)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sello, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel168)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel98)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(desa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel174)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel173)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cal2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(savePart)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(folioVis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel170)
                            .addComponent(pig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel166)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(med, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(mat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel171))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(mat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel172)))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel168)
                            .addComponent(tp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(desa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel98))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel174)
                            .addComponent(cal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel173))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c21, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c22, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c23, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c24, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c25, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c26, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c11, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c12, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c13, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c14, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c15, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c16, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(pzFinales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkPzFinales))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(precioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(pz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(checkPz)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(kgPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(checkKg)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savePart, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        paPed.addTab("Partida", jPanel2);

        jPanel3.setPreferredSize(new java.awt.Dimension(573, 570));

        jLabel57.setForeground(new java.awt.Color(0, 102, 153));
        jLabel57.setText("Inicio");

        kgFinT3.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT3.setPreferredSize(new java.awt.Dimension(60, 20));
        kgFinT3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT3KeyTyped(evt);
            }
        });

        jLabel118.setForeground(new java.awt.Color(0, 102, 153));
        jLabel118.setText("Final");

        retIni.setForeground(new java.awt.Color(0, 153, 153));
        retIni.setPreferredSize(new java.awt.Dimension(60, 20));
        retIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                retIniKeyTyped(evt);
            }
        });

        kgIniT3.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT3.setPreferredSize(new java.awt.Dimension(60, 20));
        kgIniT3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT3KeyTyped(evt);
            }
        });

        jLabel106.setForeground(new java.awt.Color(0, 102, 153));
        jLabel106.setText("Inicio");

        jLabel105.setForeground(new java.awt.Color(0, 102, 153));
        jLabel105.setText("Nombre");

        jLabel103.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(0, 102, 153));
        jLabel103.setText("Tinta 4");

        jLabel64.setForeground(new java.awt.Color(0, 102, 153));
        jLabel64.setText("Final");

        aceIni.setForeground(new java.awt.Color(0, 153, 153));
        aceIni.setPreferredSize(new java.awt.Dimension(60, 20));
        aceIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                aceIniKeyTyped(evt);
            }
        });

        barIni.setForeground(new java.awt.Color(0, 153, 153));
        barIni.setPreferredSize(new java.awt.Dimension(60, 20));
        barIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                barIniKeyTyped(evt);
            }
        });

        listaTintas1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N/A", "Blanco Laminación", "Blanco Flexo Frente", "Amarillo Flexo Frente", "Naranja Flexo Frente", "Magenta Flexo Frente", "Rojo Medio Flexo Frente", "Azul Cyan Flexo Frente", "Azul Reflex Flexo Frente", "Verde Flexo Frente", "Violeta Flexo Frente", "Negro Flexo Frente" }));

        listaTintas2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N/A", "Blanco Laminación", "Blanco Flexo Frente", "Amarillo Flexo Frente", "Naranja Flexo Frente", "Magenta Flexo Frente", "Rojo Medio Flexo Frente", "Azul Cyan Flexo Frente", "Azul Reflex Flexo Frente", "Verde Flexo Frente", "Violeta Flexo Frente", "Negro Flexo Frente" }));

        jLabel55.setForeground(new java.awt.Color(0, 102, 153));
        jLabel55.setText("Final");

        kgFinT5.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT5.setPreferredSize(new java.awt.Dimension(60, 20));
        kgFinT5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT5KeyTyped(evt);
            }
        });

        jLabel112.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(0, 102, 153));
        jLabel112.setText("Tinta 6");

        jLabel60.setForeground(new java.awt.Color(0, 102, 153));
        jLabel60.setText("Inicio");

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(0, 102, 153));
        jLabel119.setText("Acetato");

        jLabel102.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(0, 102, 153));
        jLabel102.setText("Tinta 3");

        kgIniT4.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT4.setPreferredSize(new java.awt.Dimension(60, 20));
        kgIniT4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT4KeyTyped(evt);
            }
        });

        jLabel152.setForeground(new java.awt.Color(0, 102, 153));
        jLabel152.setText("Inicio");

        jLabel151.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(0, 102, 153));
        jLabel151.setText("Solvente acondicionador");

        kgIniT2.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT2.setPreferredSize(new java.awt.Dimension(60, 20));
        kgIniT2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT2KeyTyped(evt);
            }
        });

        jLabel153.setForeground(new java.awt.Color(0, 102, 153));
        jLabel153.setText("Final");

        kgFinT6.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT6.setPreferredSize(new java.awt.Dimension(60, 20));
        kgFinT6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT6KeyTyped(evt);
            }
        });

        aceFin.setForeground(new java.awt.Color(0, 153, 153));
        aceFin.setPreferredSize(new java.awt.Dimension(60, 20));
        aceFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                aceFinKeyTyped(evt);
            }
        });

        jLabel63.setForeground(new java.awt.Color(0, 102, 153));
        jLabel63.setText("Inicio");

        jLabel61.setForeground(new java.awt.Color(0, 102, 153));
        jLabel61.setText("Final");

        solventeIni.setForeground(new java.awt.Color(0, 153, 153));
        solventeIni.setPreferredSize(new java.awt.Dimension(60, 20));
        solventeIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                solventeIniKeyTyped(evt);
            }
        });

        listaTintas3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N/A", "Blanco Laminación", "Blanco Flexo Frente", "Amarillo Flexo Frente", "Naranja Flexo Frente", "Magenta Flexo Frente", "Rojo Medio Flexo Frente", "Azul Cyan Flexo Frente", "Azul Reflex Flexo Frente", "Verde Flexo Frente", "Violeta Flexo Frente", "Negro Flexo Frente" }));

        barFin.setForeground(new java.awt.Color(0, 153, 153));
        barFin.setPreferredSize(new java.awt.Dimension(60, 20));
        barFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                barFinKeyTyped(evt);
            }
        });

        jLabel154.setForeground(new java.awt.Color(0, 102, 153));
        jLabel154.setText("Inicio");

        jLabel56.setForeground(new java.awt.Color(0, 102, 153));
        jLabel56.setText("Nombre");

        jLabel122.setForeground(new java.awt.Color(0, 102, 153));
        jLabel122.setText("Final");

        retFin.setForeground(new java.awt.Color(0, 153, 153));
        retFin.setPreferredSize(new java.awt.Dimension(60, 20));
        retFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                retFinKeyTyped(evt);
            }
        });

        jLabel59.setForeground(new java.awt.Color(0, 102, 153));
        jLabel59.setText("Nombre");

        jLabel62.setForeground(new java.awt.Color(0, 102, 153));
        jLabel62.setText("Nombre");

        listaTintas4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N/A", "Blanco Laminación", "Blanco Flexo Frente", "Amarillo Flexo Frente", "Naranja Flexo Frente", "Magenta Flexo Frente", "Rojo Medio Flexo Frente", "Azul Cyan Flexo Frente", "Azul Reflex Flexo Frente", "Verde Flexo Frente", "Violeta Flexo Frente", "Negro Flexo Frente" }));

        kgFinT1.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT1.setPreferredSize(new java.awt.Dimension(60, 20));
        kgFinT1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT1KeyTyped(evt);
            }
        });

        jLabel123.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(0, 102, 153));
        jLabel123.setText("Retardante");

        jLabel117.setForeground(new java.awt.Color(0, 102, 153));
        jLabel117.setText("Inicio");

        kgFinT4.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT4.setPreferredSize(new java.awt.Dimension(60, 20));
        kgFinT4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT4KeyTyped(evt);
            }
        });

        kgFinT2.setForeground(new java.awt.Color(0, 153, 153));
        kgFinT2.setPreferredSize(new java.awt.Dimension(60, 20));
        kgFinT2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgFinT2KeyTyped(evt);
            }
        });

        jLabel126.setForeground(new java.awt.Color(0, 102, 153));
        jLabel126.setText("Final");

        jLabel125.setForeground(new java.awt.Color(0, 102, 153));
        jLabel125.setText("Inicio");

        solventeFin.setForeground(new java.awt.Color(0, 153, 153));
        solventeFin.setPreferredSize(new java.awt.Dimension(60, 20));
        solventeFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                solventeFinKeyTyped(evt);
            }
        });

        jLabel107.setForeground(new java.awt.Color(0, 102, 153));
        jLabel107.setText("Final");

        jLabel115.setForeground(new java.awt.Color(0, 102, 153));
        jLabel115.setText("Final");

        jLabel155.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel155.setForeground(new java.awt.Color(0, 102, 153));
        jLabel155.setText("Barniz");

        jLabel156.setForeground(new java.awt.Color(0, 102, 153));
        jLabel156.setText("Final");

        jLabel113.setForeground(new java.awt.Color(0, 102, 153));
        jLabel113.setText("Nombre");

        jLabel52.setForeground(new java.awt.Color(0, 102, 153));
        jLabel52.setText("Nombre");

        jLabel121.setForeground(new java.awt.Color(0, 102, 153));
        jLabel121.setText("Inicio");

        kgIniT5.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT5.setPreferredSize(new java.awt.Dimension(60, 20));
        kgIniT5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT5KeyTyped(evt);
            }
        });

        sticky.setForeground(new java.awt.Color(0, 153, 153));
        sticky.setPreferredSize(new java.awt.Dimension(60, 20));
        sticky.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stickyKeyTyped(evt);
            }
        });

        jLabel58.setForeground(new java.awt.Color(0, 102, 153));
        jLabel58.setText("Final");

        jLabel54.setForeground(new java.awt.Color(0, 102, 153));
        jLabel54.setText("Inicio");

        jLabel104.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(0, 102, 153));
        jLabel104.setText("Tinta 5");

        jLabel100.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(0, 102, 153));
        jLabel100.setText("Tinta 1");

        mezIni.setForeground(new java.awt.Color(0, 153, 153));
        mezIni.setPreferredSize(new java.awt.Dimension(60, 20));
        mezIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mezIniKeyTyped(evt);
            }
        });

        jLabel114.setForeground(new java.awt.Color(0, 102, 153));
        jLabel114.setText("Inicio");

        listaTintas6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N/A", "Blanco Laminación", "Blanco Flexo Frente", "Amarillo Flexo Frente", "Naranja Flexo Frente", "Magenta Flexo Frente", "Rojo Medio Flexo Frente", "Azul Cyan Flexo Frente", "Azul Reflex Flexo Frente", "Verde Flexo Frente", "Violeta Flexo Frente", "Negro Flexo Frente", "Acqua", "Verde 361", "Cafe 4625", "Cafe 732", "Rosa", "Cool Gray 9", "Cool Gray 8", "Plata", "Oro", "Ftalo" }));

        jLabel116.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel116.setForeground(new java.awt.Color(0, 102, 153));
        jLabel116.setText("Mezcla 80/20");

        kgIniT1.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT1.setPreferredSize(new java.awt.Dimension(60, 20));
        kgIniT1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT1KeyTyped(evt);
            }
        });

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(0, 102, 153));
        jLabel101.setText("Tinta 2");

        kgIniT6.setForeground(new java.awt.Color(0, 153, 153));
        kgIniT6.setPreferredSize(new java.awt.Dimension(60, 20));
        kgIniT6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgIniT6KeyTyped(evt);
            }
        });

        mezFin.setForeground(new java.awt.Color(0, 153, 153));
        mezFin.setPreferredSize(new java.awt.Dimension(60, 20));
        mezFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mezFinKeyTyped(evt);
            }
        });

        jLabel159.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel159.setForeground(new java.awt.Color(0, 102, 153));
        jLabel159.setText("Sticky");

        listaTintas5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N/A", "Blanco Laminación", "Blanco Flexo Frente", "Amarillo Flexo Frente", "Naranja Flexo Frente", "Magenta Flexo Frente", "Rojo Medio Flexo Frente", "Azul Cyan Flexo Frente", "Azul Reflex Flexo Frente", "Verde Flexo Frente", "Violeta Flexo Frente", "Negro Flexo Frente" }));

        guardCamTintas.setBackground(new java.awt.Color(51, 51, 51));
        guardCamTintas.setForeground(new java.awt.Color(255, 255, 255));
        guardCamTintas.setText("Guardar cambios");
        guardCamTintas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardCamTintasActionPerformed(evt);
            }
        });

        guardarTintas.setBackground(new java.awt.Color(51, 51, 51));
        guardarTintas.setForeground(new java.awt.Color(255, 255, 255));
        guardarTintas.setText("Guardar Tintas");
        guardarTintas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarTintasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel100)
                            .addComponent(jLabel123)
                            .addComponent(jLabel151)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel105)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(listaTintas4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel62)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(listaTintas5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel59)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(listaTintas3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel56)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(listaTintas2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel52)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(listaTintas1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel113)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(listaTintas6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel152)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(solventeIni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel125)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(retIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel126)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(retFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel153)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(solventeFin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jLabel116)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel117)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(mezIni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel118)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(mezFin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel101)
                                    .addComponent(jLabel102)
                                    .addComponent(jLabel103)
                                    .addComponent(jLabel104)
                                    .addComponent(jLabel112))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel54)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgIniT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel55)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgFinT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel57)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgIniT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel58)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgFinT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel60)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgIniT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel61)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgFinT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel63)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgIniT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel64)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgFinT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel106)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgIniT5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel107)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(kgFinT5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel119)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel121)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(aceIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel122)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(aceFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(1, 1, 1))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel155)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel154)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(barIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel156)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(barFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel159)
                                        .addComponent(sticky, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel114)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(kgIniT6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel115)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(kgFinT6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(guardCamTintas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guardarTintas)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel100)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel54)
                        .addComponent(kgIniT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel55)
                        .addComponent(kgFinT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(listaTintas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel101)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel57)
                        .addComponent(kgIniT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel58)
                        .addComponent(kgFinT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel56)
                        .addComponent(listaTintas2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel102)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel60)
                        .addComponent(kgIniT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel61)
                        .addComponent(kgFinT3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel59)
                        .addComponent(listaTintas3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel103)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel63)
                        .addComponent(kgIniT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel64)
                        .addComponent(kgFinT4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62)
                        .addComponent(listaTintas5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel104)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel106)
                        .addComponent(kgIniT5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel107)
                        .addComponent(kgFinT5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel105)
                        .addComponent(listaTintas4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel112)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel113)
                    .addComponent(listaTintas6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel114)
                    .addComponent(kgIniT6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel115)
                    .addComponent(kgFinT6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel116)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel117)
                                    .addComponent(mezIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel118)
                                    .addComponent(mezFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel119)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel121)
                                    .addComponent(aceIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel122)
                                    .addComponent(aceFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel123)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel125)
                            .addComponent(retIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel126)
                            .addComponent(retFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel155)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel154)
                            .addComponent(barIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel156)
                            .addComponent(barFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel151)
                    .addComponent(jLabel159))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(solventeIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel153)
                        .addComponent(solventeFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sticky, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel152))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardCamTintas)
                    .addComponent(guardarTintas))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        paPed.addTab("Tintas", jPanel8);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setPreferredSize(new java.awt.Dimension(605, 54));

        jLabel1.setFont(new java.awt.Font("Andalus", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pedido");

        regP.setBackground(new java.awt.Color(51, 51, 51));
        regP.setForeground(new java.awt.Color(255, 255, 255));
        regP.setText("Regresar");
        regP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regPActionPerformed(evt);
            }
        });

        abreProcesos.setBackground(new java.awt.Color(51, 51, 51));
        abreProcesos.setForeground(new java.awt.Color(255, 255, 255));
        abreProcesos.setText("Procesos");
        abreProcesos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abreProcesosActionPerformed(evt);
            }
        });

        modPed.setForeground(new java.awt.Color(0, 102, 153));
        modPed.setText("Modificar Pedido");
        modPed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                modPedItemStateChanged(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Gastos Fijos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(modPed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(abreProcesos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regP)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(regP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(abreProcesos, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(modPed))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paPed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paPed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paPed.getAccessibleContext().setAccessibleName("ll");

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    //llena las listas desplegables
    private void llenarListas(){
        mat1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALTA", "BAJA", "BOPP", "CPP","BOPP/BOPP","BAJA/BOPP","BAJA/PET","BOPP/PET","CPP/PET"}));
        mat2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NO APLICA","ALTA", "BAJA", "BOPP", "CPP","BOPP/BOPP","BAJA/BOPP","BAJA/PET","BOPP/PET","CPP/PET"}));
        sello.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FONDO", "LATERAL", "TRASLAPE"}));
    }
    
    //le da un diseño a las tablas
    private void disenoTablas(){
        
        thCli = tablaC.getTableHeader();
        thCli.setFont(fuenteTablas);
        thCli.setBackground(Color.black);
        thCli.setForeground(Color.white);
        
        thMod = tablaModPed.getTableHeader();
        thMod.setFont(fuenteTablas);
        thMod.setBackground(Color.black);
        thMod.setForeground(Color.white);
        
        thPed = tablaP.getTableHeader();
        thPed.setFont(fuenteTablas);
        thPed.setBackground(Color.black);
        thPed.setForeground(Color.WHITE);
    }
    
    //Boton: Regresar a la pantalla principal
    private void regPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regPActionPerformed
        regP.setSelected(false);
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_regPActionPerformed
    
    //Boton: Guardar un pedido
    private void savePActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePActionPerformed
        saveP.setSelected(false);
        comprobarVacio();//En caso de que el usuario borre un campo y lo deje vacio

        desSt = desa.getText();//

        impSt = imp.getText();//
        
        estatusSt = estatus.getSelectedItem().toString();

        devSt = dev.getText();//
        fInSt = fIn2.getText();//
        fComSt = fC2.getText();//
        fPagoSt = fP2.getText();//
        fTerminoSt = fT.getText();
        autorizoSt  = autoCalendario.getText();

        grabSt = grab.getText();
        grabados = Float.parseFloat(grabSt);//
        
        diseSt = disenio.getText();
        diseF = Float.parseFloat(diseSt);

        antiSt = anti.getText();
        antiF = Float.parseFloat(antiSt);//

        descuSt = decu.getText();
        descuF = Float.parseFloat(descuSt);
        
        porcentajeIVA = Integer.parseInt(porIVA.getSelectedItem().toString());
        //se guardan los datos en la base
        String sql = " insert into pedido(impresion, estatus,fIngreso, fCompromiso, fPago, fTermino, grabados, subtotal, total, costoDisenio,anticipo, resto, devolucion," +
        " descuento, idC_fk, kgDesperdicioPe, porcentajeDespPe, costoTotal, gastosFijos, perdidasYGanancias, autorizo, sumatoriaBolseoP, "
                + "matComPe, matProPe, porcentajeIVA) "
        + "values('"+impSt+"', '"+estatusSt+"','"+fInSt+"', '"+fComSt+"', '"+fPagoSt+"', '"+fTerminoSt+"', "+grabados+", 0, 0, "+diseF+","+antiF+", 0, '"+devSt+"'," +
        " "+descuF+", "+idCliente+", 0, 0, 0, 0, 0, '"+autoCalendario.getText()+"', 0, 0, 0,"+porcentajeIVA+")";

        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            preGuardadoTintas();
            JOptionPane.showMessageDialog(null, "Se ha guardado el pedido", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Hay un error con los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }    
    }//GEN-LAST:event_savePActionPerformed

    //se usa para el preregistro de tintas
    private int obtieneFolioUltimoPedido()
    {
        int folio = 0;
        String sql = "select folio from pedido order by folio desc limit 1";//obtiene el ultimo folio registrado
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                folio = Integer.parseInt(rs.getString("folio"));
            }
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return folio;
    }
    
    //inicializa los datos de tintas en la base en 0 para que no haya errores
    private void preGuardadoTintas()
    {
        int ultimoFolio = obtieneFolioUltimoPedido();//folio del ultimo pedido registrado
        
        String sql = "insert into tintas(tinta1, pIni1, pFin1, tinta2, pIni2, pFin2, tinta3, pIni3, pFin3, tinta4, pIni4, pFin4, tinta5, pIni5, pFin5, tinta6, "
                + "pIni6, pFin6, " +
                " iniMezcla, finMezcla," +
                " iniAcetato, finAcetato," +
                " iniRetard, finRetard," +
                " folio_fk ," +
                " iniSolvente, finSolvente," +
                " iniBarniz, finBarniz," +
                " costoDeTintas,"
                + "seSumoAPedido,sticky)"
                + "values('N/A',0,0,'N/A',0,0,'N/A',0,0,'N/A',0,0,'N/A',0,0,"
                + "'N/A',0,0,0,0,0,0,0,0,"+ultimoFolio+",0,0,0,0,0,0,0)";

        try {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
   
    //Evento: cuando la tabla de clientes es cliqueada
    private void tablaCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaCMouseClicked
        obtenerSeleccionTablaCliente();
    }//GEN-LAST:event_tablaCMouseClicked
   
    //obtiene el id y el nombre del cliente seleccionado
    private void obtenerSeleccionTablaCliente(){
        String nom;
        String id;
        try
        {
            id = modelo.getValueAt(tablaC.getSelectedRow(), 0).toString();//Lo mismo, pero ahora se guarda la columna 0(Id)
            nom = modelo.getValueAt(tablaC.getSelectedRow(), 1).toString();//Se guarda el campo ubicado en la fila presionada y en la columna 1(Que es nombre)
            idCliente = Integer.parseInt(id);//Se transforma el id a entero para poder guardarlo en la base de datos, pues es un entero
            cliente.setText(nom);//Se establece el noimbre de cliente en un textField, con fines visuales
            saveP.setEnabled(true);//se habilita el boton de guardado
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
        } 
    }
        
    //Evento: Cuando un caracter es ingresado al textField de busqueda de clientes e insercion a la tabla de clientes
    private void nomCliCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_nomCliCaretUpdate
        modelo.setRowCount(0);//Borrar resultados anteriores
        String nomcliente;
        nomcliente = nomCli.getText().toString();//Se obtiene el texto ingresado para busqueda
        String sql = "select idC, nom from cliente where nom like '%"+nomcliente+"%' limit 0,30";//Se realiza la busqueda en la base de datos con el nombre ya obtenido
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //Se añaden los datos a la tabla de clientes
                modelo.addRow(new Object[]{rs.getString("idC"), rs.getString("nom")});
            }
            tablaC.setModel(modelo);
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_nomCliCaretUpdate
    
    //selecciona una fecha de los datechooser
    private void setFechaImposible()
    {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy");
        DateTime date = new DateTime("2018");//esta es la fecha que se establecera
        java.util.Date dtUtil = null;
        Calendar cal = Calendar.getInstance(); 
        dtf = DateTimeFormat.forPattern("yyyy");
        try 
        {
            dtUtil = new SimpleDateFormat("yyyy").parse(dtf.print(date));
        } 
        catch (ParseException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        cal.setTime(dtUtil);
        fP2.setSelectedDate(cal);
        fT.setSelectedDate(cal);
    }
    
    //Boton: Busqueda de pedido e insercion en la tabla.
    private void busPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busPedidoActionPerformed
        busPedido.setSelected(false);
        llenarTablaPedido();
    }//GEN-LAST:event_busPedidoActionPerformed
    //se llena la tabla de pedidos
    public void llenarTablaPedido(){
        //Lo mismo que la tabla de clientes
        modeloP.setRowCount(0);//se vacía
        String nompedido;
        nompedido = nomImpresion.getText().toString();
        //Se utiliza join para que tambien se muestren los datos de clientes relacionados con el pedido
        String sql = "select folio, impresion, nom, fIngreso from pedido join cliente on idC_fk = idC where impresion like '%"+nompedido+"%' "
                + "order by fIngreso desc limit 0, 30";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                modeloP.addRow(new Object[]{rs.getString("folio")+"A", rs.getString("impresion"), rs.getString("nom"), rs.getString("fIngreso")});
            }
            tablaP.setModel(modeloP);
            rs.close();
            st.close();
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla de pedidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Boton: Guardar Partida
    private void savePartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePartActionPerformed
        savePart.setSelected(false);
        comprobarVacio();
        String sql;
            medSt = med.getText();
            pigSt = pig.getText();
            tipoSt = tp.getText();
            selloSt = sello.getSelectedItem().toString();
            cal1St = cal1.getText();
            mat1St = mat1.getSelectedItem().toString();
            cal2St = cal2.getText();
            mat2St = mat2.getSelectedItem().toString();

            pUnitSt = precioUni.getText();
            precioUnitario = Float.parseFloat(pUnitSt);

            piezasSt = pz.getText();
            piezasI = Integer.parseInt(piezasSt);

            kgParidaSt = kgPart.getText();
            
            pzFinalesSt = pzFinales.getText();
            pzFinalesI = Integer.parseInt(pzFinalesSt);
            
            String manera = determinaManera();//obtiene la manera como se calculo el importe
            //se insertara en la base de datos
            sql = "insert into partida(medida, piezas, tipo, sello, pigmento, mat1, calibre1, mat2, calibre2, precioUnitaro," +
            "importe, kgPartida, folio_fk, c1t1, c1t2, c1t3, c1t4, c1t5, c1t6, c2t1, c2t2, c2t3, c2t4, c2t5, c2t6, desarrollo, kgDesperdicio, porcentajeDesp, "
            + "costoMaterialTotal, costoPartida, pzFinales, manera) "
            + "values('"+medSt+"', "
                    + ""+piezasI+", "
                    + "'"+tipoSt+"', "
                    + "'"+selloSt+"', "
                    + "'"+pigSt+"', "
                    + "'"+mat1St+"', "
                    + "'"+cal1St+"', "
                    + "'"+mat2St+"', "
                    + "'"+cal2St+"',"
                    + ""+pUnitSt+", "
                    + ""+importe.getText()+", "
                    + ""+kgParidaSt+", "
                    + ""+folioId+", "
                    + "'"+c11.getText()+"', "
                    + "'"+c12.getText()+"', "
                    + "'"+c13.getText()+"', "
                    + "'"+c14.getText()+"', "
                    + "'"+c15.getText()+"', "
                    + "'"+c16.getText()+"', "
                    + "'"+c21.getText()+"', "
                    + "'"+c22.getText()+"', "
                    + "'"+c23.getText()+"', "
                    + "'"+c24.getText()+"', "
                    + "'"+c25.getText()+"', "
                    + "'"+c26.getText()+"', "
                    + ""+desa.getText()+", "
                    + "0, "//kgDesperdicio
                    + "0, "//porcentajeDesp
                    + "0, "//costoMaterialTotal
                    + "0, "//ostoPartida
                    + ""+pzFinalesI+","
                    + "'"+manera+"')";
            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
                setHojas();//enumera la partida
                establecerSubtotalPedido();//Para sumar todos los importes de las partidas y guargar el total en pedido
                JOptionPane.showMessageDialog(null, "Se ha guardado la partida", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Error al guardar la partida: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_savePartActionPerformed
    //determina la manera como se calculo el importe
    private String determinaManera()
    {
        if(checkPz.isSelected())
        {
            return "pz";
        }
        else if(checkKg.isSelected())
        {
            return "kg";
        }
        else
        {
            return "pzF";
        }
    }
    
    //Actualiza el numero se hoja y De de las partidas de un pedido
    private void setHojas(){
        
        String sql = "select idPar from partida where folio_fk = "+folioId+"";
        String sql1;
        int contadorRs = 0;//contador para las hojas
        int idPartida;
        
        Statement stAux;
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                contadorRs++;
                idPartida = rs.getInt("idPar");
                
                sql1 = "update partida set hoja = "+contadorRs+" where idPar = "+idPartida+"";//se inserta el numero de hoja de la partida
                stAux = con.createStatement();
                stAux.execute(sql1);
                stAux.close();
            }
            rs.close();
            st.close();
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                idPartida = rs.getInt("idPar");               
                sql1 = "update partida set de = "+contadorRs+" where idPar = "+idPartida+"";//se inserta el numero De de la partida
                stAux = con.createStatement();
                stAux.execute(sql1);
                stAux.close();
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar las hojas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }   
    }
    
    
    //Calcula el monto del pedido a base de los importes de todas las partidas, no es el subtotal
    private void establecerSubtotalPedido(){
        
        String sql = "select importe from partida where folio_fk = "+folioId+"";
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
        }
    }
    
    //Se calcula e ingresa el subtotal de pedido en la base de datos
    private void actualizarSubtotalPedido(float sub){
        float subGrab = sub;
        float antif  = 0f;
        float descuento = 0f;
        
        //Consulta para obtener los valores de: grabados, anticipo y descuento. Se realizaran operaciones
        String sql = "select grabados, anticipo, descuento from pedido where folio = "+folioId+"";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //Se le suma el coste de grabados al subtotal
                subGrab = subGrab + Float.parseFloat(rs.getString("grabados"));
                //Se guarda el valor de anticipo para uso posterior
                antif = Float.parseFloat(rs.getString("anticipo"));
                //Se guarda el valor de descuento para uso posterior
                descuento = Float.parseFloat(rs.getString("descuento"));
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de costos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //Cuando ya se sumaron los grabados y el subtotal ahora se guarda en la base de datos
        sql = "update pedido set subtotal = "+subGrab+" where folio = "+folioId+"";
        
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            //Ahora paso por parametros el subtotal con grabados, anticipo y descuento para calcular el total
            calcularTotalPed(subGrab, antif, descuento);
        } 
        catch(SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar el subtotal" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //obtiene el porcentaje de iva del pedido
    private float obtenerIva()
    {
        int ivaI = 0;
        float ivaF = 0f;
        String sql = "select porcentajeIVA from pedido where folio = "+folioId+"";//se consulta el iva en la base
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                ivaI = Integer.parseInt(rs.getString("porcentajeIVA"));//guardamos el iva
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        ivaF = ivaI/100f;//convertimos el iva consultado en un porcentaje flotante
        
        return ivaF;
    }
    
    //Aqui se reciben todos los valores necesarios para calcular el total y el resto y actualizar la base de datos
    private void calcularTotalPed(float sub, float anticipo, float descuento){
        
        float total = 0f;
        float ivaf = obtenerIva();
        float subIva = 0f;
        float rest = 0f;
        
        subIva = sub * ivaf;//Se obtiene el iva del subtotal
        total = sub + subIva;//Se suma el iva y el subtotal para obtener el total
        
        rest = total - anticipo;//Se le resta el anticipo al total para obtener el resto
        rest = rest - descuento;//Se le resta el descuento al resto
        //se inserta en la base de datos
        String sql= "update pedido set total = "+total+", resto = "+rest+" where folio = "+folioId+"";
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar el total y resto", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

    }
    
    private void tablaPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPMouseClicked
        obtenerSeleccionTablaPedido();//se inserte al folio de la partida en un campo
        obtenerDatosTintas();
    }//GEN-LAST:event_tablaPMouseClicked
    
    //selecciona una posicion de la lista desplegable deacuerdo a lo que esta en la base de datos
    private void establecerIndiceDeTintas(JComboBox listaTintas, String tintaDeLaBase)
    {
        if(tintaDeLaBase.equals("N/A"))
        {
            listaTintas.setSelectedIndex(0);
        }
        else if(tintaDeLaBase.equals("Blanco Laminación"))
        {
            listaTintas.setSelectedIndex(1);
        }
        else if(tintaDeLaBase.equals("Blanco Flexo Frente"))
        {
            listaTintas.setSelectedIndex(2);
        }
        else if(tintaDeLaBase.equals("Amarillo Flexo Frente"))
        {
            listaTintas.setSelectedIndex(3);
        }
        else if(tintaDeLaBase.equals("Naranja Flexo Frente"))
        {
            listaTintas.setSelectedIndex(4);
        }
        else if(tintaDeLaBase.equals("Magenta Flexo Frente"))
        {
            listaTintas.setSelectedIndex(5);
        }
        else if(tintaDeLaBase.equals("Rojo Medio Flexo Frente"))
        {
            listaTintas.setSelectedIndex(6);
        }
        else if(tintaDeLaBase.equals("Azul Cyan Flexo Frente"))
        {
            listaTintas.setSelectedIndex(7);
        }
        else if(tintaDeLaBase.equals("Azul Reflex Flexo Frente"))
        {
            listaTintas.setSelectedIndex(8);
        }
        else if(tintaDeLaBase.equals("Verde Flexo Frente"))
        {
            listaTintas.setSelectedIndex(9);
        }
        else if(tintaDeLaBase.equals("Violeta Flexo Frente"))
        {
            listaTintas.setSelectedIndex(10);
        }
        else if(tintaDeLaBase.equals("Negro Flexo Frente"))
        {
            listaTintas.setSelectedIndex(11);
        }
        else if(tintaDeLaBase.equals("Acqua"))
        {
            listaTintas.setSelectedIndex(12);
        }
        else if(tintaDeLaBase.equals("Verde 361"))
        {
            listaTintas.setSelectedIndex(13);
        }
        else if(tintaDeLaBase.equals("Cafe 4625"))
        {
            listaTintas.setSelectedIndex(14);
        }
        else if(tintaDeLaBase.equals("Cafe 732"))
        {
            listaTintas.setSelectedIndex(15);
        }
        else if(tintaDeLaBase.equals("Rosa"))
        {
            listaTintas.setSelectedIndex(16);
        }
        else if(tintaDeLaBase.equals("Cool Gray 9"))
        {
            listaTintas.setSelectedIndex(17);
        }
        else if(tintaDeLaBase.equals("Cool Gray 8"))
        {
            listaTintas.setSelectedIndex(18);
        }
        else if(tintaDeLaBase.equals("Plata"))
        {
            listaTintas.setSelectedIndex(19);
        }
        else if(tintaDeLaBase.equals("Oro"))
        {
            listaTintas.setSelectedIndex(20);
        }
        else if(tintaDeLaBase.equals("Ftalo"))
        {
            listaTintas.setSelectedIndex(21);
        }
    }
    
    //llena la ventana de tintas de los datos del pedido seleccionado
    private void obtenerDatosTintas()
    {
        int yaSeSumo = 0;
        String sql = "select * from tintas where folio_fk = "+folioId+"";
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                establecerIndiceDeTintas(listaTintas1, rs.getString("tinta1"));
                kgIniT1.setText(rs.getString("pIni1"));
                kgFinT1.setText(rs.getString("pFin1"));
                establecerIndiceDeTintas(listaTintas2, rs.getString("tinta2"));
                kgIniT2.setText(rs.getString("pIni2"));
                kgFinT2.setText(rs.getString("pFin2"));
                establecerIndiceDeTintas(listaTintas3, rs.getString("tinta3"));
                kgIniT3.setText(rs.getString("pIni3")); 
                kgFinT3.setText(rs.getString("pFin3")); 
                establecerIndiceDeTintas(listaTintas4, rs.getString("tinta4"));
                kgIniT4.setText(rs.getString("pIni4"));
                kgFinT4.setText(rs.getString("pFin4")); 
                establecerIndiceDeTintas(listaTintas5, rs.getString("tinta5"));
                kgIniT5.setText(rs.getString("pIni5"));
                kgFinT5.setText(rs.getString("pFin5"));
                establecerIndiceDeTintas(listaTintas6, rs.getString("tinta6"));
                kgIniT6.setText(rs.getString("pIni6"));
                kgFinT6.setText(rs.getString("pFin6"));

                mezIni.setText(rs.getString("iniMezcla"));
                mezFin.setText(rs.getString("finMezcla")); 
                aceIni.setText(rs.getString("iniAcetato")); 
                aceFin.setText(rs.getString("finAcetato"));
                retIni.setText(rs.getString("iniRetard")); 
                retFin.setText(rs.getString("finRetard"));
                solventeIni.setText(rs.getString("iniSolvente")); 
                solventeFin.setText(rs.getString("finSolvente"));
                barIni.setText(rs.getString("iniBarniz"));
                barFin.setText(rs.getString("finBarniz"));
                
                sticky.setText(rs.getString("sticky"));
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al establecer los campos de las tintas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        sql = "select seSumoAPedido from tintas where folio_fk = "+folioId+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                yaSeSumo = Integer.parseInt(rs.getString("seSumoAPedido"));
            }
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        if(yaSeSumo == 0)
        {
            guardarTintas.setEnabled(true);
            guardarTintas.setVisible(true);
            guardCamTintas.setEnabled(false);
            guardCamTintas.setVisible(false);
        }
        else if(yaSeSumo == 1)
        {
            guardarTintas.setEnabled(false);
            guardarTintas.setVisible(false);
            guardCamTintas.setEnabled(true);
            guardCamTintas.setVisible(true);
        }
    }
    
    private void obtenerSeleccionTablaPedido(){
        String folioSinA;
            try
            {
                folioSinA = modeloP.getValueAt(tablaP.getSelectedRow(), 0).toString().replace("A", "");//se guarda la columna 0(Id)
                folioId = Integer.parseInt(folioSinA);
                folioVis.setText(String.valueOf(folioId)+"A");//Se establece el folio del pedido en un textField, con fines visuales
                comprobarCondPed();//se comrpueba si se puede habilitar el boton de guardado
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
            }
    }
    
    //Funcion que revisa que se halla seleccionado un pedido y que alguna de las casillas de piezas y kilos tambien este seleccionada
    public void comprobarCondPed(){
        
        if(!(folioVis.getText().equals("Folio")) && (checkPz.isSelected() || checkKg.isSelected() || checkPzFinales.isSelected())){
            savePart.setEnabled(true);
        }else{
            savePart.setEnabled(false);
        }
    }
    
    
    //Boton a procesos
    private void abreProcesosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abreProcesosActionPerformed
        abreProcesos.setSelected(false);
        Inicio.pro.setLocationRelativeTo(null);
        Inicio.pro.vacearComponentes();
        Inicio.pro.setVisible(true);   
    }//GEN-LAST:event_abreProcesosActionPerformed
    
    //Funcion para que solo se ingresen numeros flotantes en campos
    private void soloFlotantes(KeyEvent evt, JTextField campo){
        
        char c = evt.getKeyChar();//Obtener el caracter de la tecla presionada
        String cadena = campo.getText();//Guardar la cadena del campo para contar los puntos que contiene
        int contPuntos = 0;// contador de los ".", pues solo debe de haber uno en un numero flotante
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
    
    
    //De aqui comienzan todos los eventos KeyTyped para que solo se ingresen valores enteros o flotantes a los campos
    //Pedido
    private void desaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_desaKeyTyped
        limitarInsercion(10, evt, desa);
        soloFlotantes(evt, desa);
    }//GEN-LAST:event_desaKeyTyped

    private void decuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_decuKeyTyped
        soloFlotantes(evt, decu);
    }//GEN-LAST:event_decuKeyTyped
    //Partida
    private void precioUniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precioUniKeyTyped
        soloFlotantes(evt, precioUni);
    }//GEN-LAST:event_precioUniKeyTyped

    private void pzKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pzKeyTyped
        limitarInsercion(10, evt, pz);
        soloEnteros(evt);   
    }//GEN-LAST:event_pzKeyTyped

    private void importeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_importeKeyTyped
        soloFlotantes(evt, importe);
    }//GEN-LAST:event_importeKeyTyped
    //calcula el importe deacuerdo a el checkbox que se haya marcado
    private void calcularEnCampoPU(){
        float pUni;
        int pzs;
        int pzsf;
        float kgPartida;
        float total;
        
        if(indicadorParaImporte == 1)//si se calculo utilizando las pz
        {
                    if(precioUni.getText().equals("") || precioUni.getText().equals("."))
                    {
                        pUni = 0;
                    }
                    else
                    {
                        pUni = Float.parseFloat(precioUni.getText());
                    }

                    if(pz.getText().equals("") || pz.getText().equals(""))
                    {
                        pzs = 0;
                    }
                    else
                    {
                        pzs = Integer.parseInt(pz.getText());
                    }
                    total = pUni * pzs;
                    importe.setText(String.valueOf(total));
                }
        else if(indicadorParaImporte == 2)//si se utilizo utilizando los kg
        {
                    if(precioUni.getText().equals("") || precioUni.getText().equals("."))
                    {
                        pUni = 0;
                    }
                    else
                    {
                        pUni = Float.parseFloat(precioUni.getText());
                    }

                    if(kgPart.getText().equals("") || kgPart.getText().equals("."))
                    {
                        kgPartida = 0;
                    }
                    else
                    {
                        kgPartida = Float.parseFloat(kgPart.getText());
                    }
                    total = pUni * kgPartida;
                    importe.setText(String.valueOf(total));
                }
        else if(indicadorParaImporte == 3)//si se utilizaron las pz finales
        {
                    if(precioUni.getText().equals("") || precioUni.getText().equals("."))
                    {
                        pUni = 0;
                    }
                    else
                    {
                        pUni = Float.parseFloat(precioUni.getText());
                    }

                    if(pzFinales.getText().equals("") || pzFinales.getText().equals(""))
                    {
                        pzsf = 0;
                    }
                    else
                    {
                        pzsf = Integer.parseInt(pzFinales.getText());
                    }
                    total = pUni * pzsf;
                    importe.setText(String.valueOf(total));
                } 
        
    }
    
    private void onChangePU(){
        precioUni.getDocument().addDocumentListener(new DocumentListener() {               
            @Override
            public void insertUpdate(DocumentEvent e) {              
                calcularEnCampoPU();
                
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                calcularEnCampoPU();
               
            }
            @Override
            public void changedUpdate(DocumentEvent e) {             
            }
        });
    }

    private void antiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_antiKeyTyped
        soloFlotantes(evt, anti);
    }//GEN-LAST:event_antiKeyTyped

    //Costos de pedido
    private void grabKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grabKeyTyped
        soloFlotantes(evt, grab);
    }//GEN-LAST:event_grabKeyTyped

    //cuando el estado del checkbox de pz cambia
    private void checkPzItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkPzItemStateChanged
        if(checkPz.isSelected()){
            //se habilia todo lo relacionado con pz
            checkPz.setSelected(true);
            pz.setEnabled(true);
            //se deshabilita lo que no este relacionado con pz
            checkKg.setSelected(false);
            kgPart.setEnabled(false);
            pzFinales.setEnabled(false);
            checkPzFinales.setSelected(false);
            
            indicadorParaImporte = 1;//se cambia el indicador de importe
            calcularEnCampoPU();//se calcula el importe
        }
        else
        {
            pz.setEnabled(false);
        }
        comprobarCondPed();//se comprueba si se debe habilitar el boton de guardado
    }//GEN-LAST:event_checkPzItemStateChanged

    private void checkKgItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkKgItemStateChanged
        if(checkKg.isSelected()){
            //se habilita todo lo relacionado con kg
            checkKg.setSelected(true);
            kgPart.setEnabled(true);
            //se deshabilita todo lo que no esta relacionado con kg
            checkPz.setSelected(false);
            pz.setEnabled(false);
            pzFinales.setEnabled(false);
            checkPzFinales.setSelected(false);
            
            indicadorParaImporte = 2;//se cambia el indicador de importe
            calcularEnCampoPU();//se calcula el subtotal
        }
        else
        {
            kgPart.setEnabled(false);
        }
        comprobarCondPed();//se comprueba si se debe habilitar el boton de guardado
    }//GEN-LAST:event_checkKgItemStateChanged

    private void kgPartKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgPartKeyTyped
        soloFlotantes(evt, kgPart);
    }//GEN-LAST:event_kgPartKeyTyped

    private void nomCliMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nomCliMousePressed
        nomCli.setText("");
    }//GEN-LAST:event_nomCliMousePressed
    
    private void modPedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_modPedItemStateChanged
        
        if(modPed.isSelected())
        {
            //se habilita todo lo relacionado con la modificacion del pedido
            tablaModPed.setVisible(true);
            btnBusPed.setEnabled(true);
            textBusPed.setEnabled(true);
            btnMod.setVisible(true);
            panelMod.setVisible(true);
            //se deshabilita todo lo no relacionado con la modificacion del pedido
            saveP.setEnabled(false);
            tablaC.setVisible(false);
            nomCli.setEnabled(false);
            saveP.setVisible(false);
            panelCliente.setVisible(false);
            llenarCamposMod(folioMod);
        }
        else
        {
            if(cliente.getText().equals("Selecciona un Cliente"))//si no se ha seleccionado un cliente
            {
                saveP.setEnabled(false);//se deshabilita el boton de guardado
            }
            else
            {
                saveP.setEnabled(true);//se habilita
            }
            //se habilita todo lo que no esta relacionado con la modificacion del pedido
            tablaC.setVisible(true);
            nomCli.setEnabled(true);
            saveP.setVisible(true);
            panelCliente.setVisible(true);
            //se deshabilita todo lo que esta relacionado con la modificacion del pedido
            tablaModPed.setVisible(false);
            btnBusPed.setEnabled(false);
            textBusPed.setEnabled(false);
            btnMod.setVisible(false);
            panelMod.setVisible(false);
            //se vuelven a inicializar los campos en vacio
            imp.setText("");
            estatus.setSelectedIndex(0);
            autoCalendario.setText("");
            dev.setText("");
            grab.setText("");
            disenio.setText("");
            anti.setText("");
            decu.setText("");
            porIVA.setSelectedIndex(0);
        }
    }//GEN-LAST:event_modPedItemStateChanged

    //Boton Para buscar pedido al querer modificar datos
    private void btnBusPedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusPedActionPerformed
        btnBusPed.setSelected(false);
        llenarTablaModPedido();//se buscan los pedidos
    }//GEN-LAST:event_btnBusPedActionPerformed

    public void llenarTablaModPedido(){
        modeloMod.setRowCount(0);//se vacia la tabla
        String nompedido;
        nompedido = textBusPed.getText().toString();//se obtiene el nombre de impresion del pedido
        //Se utiliza join para que tambien se muestren los datos de clientes relacionados con el pedido
        String sql = "select folio, impresion, nom, fIngreso from pedido join cliente on idC_fk = idC where impresion like '%"+nompedido+"%' "
                + "order by fIngreso desc limit 0, 30";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                modeloMod.addRow(new Object[]{rs.getString("folio")+"A", rs.getString("impresion"), rs.getString("nom"), rs.getString("fIngreso")});
            }
            tablaModPed.setModel(modeloMod);//se establece un modelo para la tabla
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla de pedidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tablaModPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaModPedMouseClicked
        obtenerSeleccionTablaPedidoMod();//se obtienen los datos del pedido   
    }//GEN-LAST:event_tablaModPedMouseClicked

    private void obtenerSeleccionTablaPedidoMod(){
        int folioModInt;
        try
        {    
            folioModInt = Integer.parseInt(modeloMod.getValueAt(tablaModPed.getSelectedRow(), 0).toString().replace("A", ""));//se optiene el folio del pedido a modificar
            llenarCamposMod(folioMod);//se llenan los campos con los datos del pedido
            this.folioMod = folioModInt;
            btnMod.setEnabled(true);  
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton" + ex.getMessage(), "Avertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void btnModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModActionPerformed
        btnMod.setSelected(false);
        //Estos calculos se hacen ya que si se modifica el pedido, exactamente los datos de grabados, descuento y anticipo, afecta tambien al total...
        //subtotal y el resto. Se hacen de nuevo los calculos con los nuevos datos y se guardan
        float subtotal = respaldoSub + Float.parseFloat(grab.getText());
        String porcentajeDeIvaEntero = porIVA.getSelectedItem().toString();
        float porcentajeDeIvaFlotante = Float.parseFloat(porcentajeDeIvaEntero)/100;
        float subiva = subtotal * porcentajeDeIvaFlotante;//obtenemos el iva asi porque el nuevo iva aun no se guarda en la base de datos, por lo que no se puede consultar
        float total = subtotal + subiva;
        
        float resto = total - (Float.parseFloat(anti.getText()) + Float.parseFloat(decu.getText()));
        if(estatus.getSelectedItem().toString().equals("PAGADO"))
        {
            resto = 0;
        }
        //se guardan los datos actualizados
        String sql = "update pedido set impresion = '"+imp.getText()+"', "
                + "estatus = '"+estatus.getSelectedItem().toString()+"',"
                + "autorizo = '"+autoCalendario.getText()+"', "
                + "devolucion = '"+dev.getText()+"', "
                + "fIngreso = '"+fIn2.getText()+"', "
                + "fCompromiso = '"+fC2.getText()+"', "
                + "fPago = '"+fP2.getText()+"', "
                + "fTermino = '"+fT.getText()+"', "
                + "grabados = "+grab.getText()+", "
                + "costoDisenio = "+Float.parseFloat(disenio.getText())+","
                + "anticipo = "+anti.getText()+", "
                + "descuento = "+decu.getText()+", "
                + "subtotal = "+subtotal+", "
                + "total = "+total+", "
                + "resto = "+resto+", porcentajeIVA = "
                + ""+Integer.parseInt(porIVA.getSelectedItem().toString())+" "
                + "where folio = "+folioMod+"";
        
        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            calculaPyG(folioMod);//se vuelven a calcular las pyg de la partida modificada
            
            JOptionPane.showMessageDialog(null, "Se han guardado los cambios", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch(SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al modificar: "+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }  
    }//GEN-LAST:event_btnModActionPerformed

    private void textBusPedMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textBusPedMousePressed
        textBusPed.setText("");
    }//GEN-LAST:event_textBusPedMousePressed

    private void nomImpresionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nomImpresionMousePressed
        nomImpresion.setText("");
    }//GEN-LAST:event_nomImpresionMousePressed

    private void textBusPedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBusPedKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            llenarTablaModPedido();
            
            if(!(tablaModPed.getRowCount() == 0)){
                tablaModPed.requestFocus();
                tablaModPed.changeSelection(0, 0, false, false);
            }
        }
    }//GEN-LAST:event_textBusPedKeyPressed

    private void nomImpresionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomImpresionKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            llenarTablaPedido();
            
            if(!(tablaP.getRowCount() == 0)){
                tablaP.requestFocus();
                tablaP.changeSelection(0, 0, false, false);
            }
        }
    }//GEN-LAST:event_nomImpresionKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.setSelected(false);
        gf = new GastosFijos(con, this);
        gf.setLocationRelativeTo(null);
        gf.setVisible(true);
        
        
        this.setEnabled(false);//se deshabilita est ventana
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tablaCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaCKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_SPACE){
            obtenerSeleccionTablaCliente();
        }
    }//GEN-LAST:event_tablaCKeyPressed

    private void nomCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomCliKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(!(tablaC.getRowCount() == 0)){
                tablaC.requestFocus();
                tablaC.changeSelection(0, 0, false, false);
            }
        }
    }//GEN-LAST:event_nomCliKeyPressed
    
    private void tablaModPedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaModPedKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_SPACE){
            obtenerSeleccionTablaPedidoMod();
        }
    }//GEN-LAST:event_tablaModPedKeyPressed

    private void tablaPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_SPACE){
            obtenerSeleccionTablaPedido();
            obtenerDatosTintas();
        }
    }//GEN-LAST:event_tablaPKeyPressed

    private void nomCliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomCliKeyTyped
        limitarInsercion(40, evt, nomCli);
    }//GEN-LAST:event_nomCliKeyTyped

    private void textBusPedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBusPedKeyTyped
        limitarInsercion(40, evt, textBusPed);
    }//GEN-LAST:event_textBusPedKeyTyped

    private void devKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_devKeyTyped
        limitarInsercion(10, evt, dev);
    }//GEN-LAST:event_devKeyTyped

    private void nomImpresionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomImpresionKeyTyped
        limitarInsercion(40, evt, nomImpresion);
    }//GEN-LAST:event_nomImpresionKeyTyped

    private void pigKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pigKeyTyped
        limitarInsercion(40, evt, pig);
    }//GEN-LAST:event_pigKeyTyped

    private void tpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tpKeyTyped
        limitarInsercion(40, evt, tp);
    }//GEN-LAST:event_tpKeyTyped

    private void medKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_medKeyTyped
        limitarInsercion(15, evt, med);
    }//GEN-LAST:event_medKeyTyped

    private void cal1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cal1KeyTyped
        limitarInsercion(5, evt, cal1);
        soloEnteros(evt);
    }//GEN-LAST:event_cal1KeyTyped

    private void cal2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cal2KeyTyped
        limitarInsercion(5, evt, cal2);
        soloEnteros(evt);
    }//GEN-LAST:event_cal2KeyTyped

    private void c11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c11KeyTyped
        limitarInsercion(15, evt, c11);
    }//GEN-LAST:event_c11KeyTyped

    private void c12KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c12KeyTyped
        limitarInsercion(15, evt, c12);
    }//GEN-LAST:event_c12KeyTyped

    private void c13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c13KeyTyped
        limitarInsercion(15, evt, c13);
    }//GEN-LAST:event_c13KeyTyped

    private void c14KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c14KeyTyped
        limitarInsercion(15, evt, c14);
    }//GEN-LAST:event_c14KeyTyped

    private void c15KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c15KeyTyped
        limitarInsercion(15, evt, c15);
    }//GEN-LAST:event_c15KeyTyped

    private void c16KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c16KeyTyped
        limitarInsercion(15, evt, c16);
    }//GEN-LAST:event_c16KeyTyped

    private void c21KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c21KeyTyped
        limitarInsercion(15, evt, c21);
    }//GEN-LAST:event_c21KeyTyped

    private void c22KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c22KeyTyped
        limitarInsercion(15, evt, c22);
    }//GEN-LAST:event_c22KeyTyped

    private void c23KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c23KeyTyped
        limitarInsercion(15, evt, c23);
    }//GEN-LAST:event_c23KeyTyped

    private void c24KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c24KeyTyped
        limitarInsercion(15, evt, c24);
    }//GEN-LAST:event_c24KeyTyped

    private void c25KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c25KeyTyped
        limitarInsercion(15, evt, c25);
    }//GEN-LAST:event_c25KeyTyped

    private void c26KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c26KeyTyped
        limitarInsercion(15, evt, c26);
    }//GEN-LAST:event_c26KeyTyped

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void pzFinalesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pzFinalesKeyTyped
        soloEnteros(evt);
        limitarInsercion(10, evt, pzFinales);
    }//GEN-LAST:event_pzFinalesKeyTyped

    private void checkPzFinalesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkPzFinalesItemStateChanged
        
        if(checkPzFinales.isSelected()){
            //se habilita todo lo relacionado con pz finales
            pzFinales.setEnabled(true);
            checkPzFinales.setSelected(true);
            //se deshabilita todo lo que no esta relacionado con las pz finales
            checkPz.setSelected(false);
            pz.setEnabled(false);
            checkKg.setSelected(false);
            kgPart.setEnabled(false);
            
            indicadorParaImporte = 3;//se cambia el indicador de importe
            calcularEnCampoPU();//se calcula el importe
        }
        else
        {
           pzFinales.setEnabled(false);
        }
        comprobarCondPed();//comprobamos si se debe de habilita el boton de guardado
    }//GEN-LAST:event_checkPzFinalesItemStateChanged

    //establece todos los campos de texto en vacio
    private void limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarActionPerformed
        folioVis.setText("Folio");
        pig.setText("");
        tp.setText("");
        med.setText("");
        desa.setText("0");
        cal1.setText("");
        cal2.setText("");
        c11.setText("");
        c12.setText("");
        c13.setText("");
        c14.setText("");
        c15.setText("");
        c16.setText("");
        c21.setText("");
        c22.setText("");
        c23.setText("");
        c24.setText("");
        c25.setText("");
        c26.setText("");
        precioUni.setText("0");
        pz.setText("0");
        kgPart.setText("0");
        importe.setText("0");
        pzFinales.setText("0");
        mat1.setSelectedIndex(0);
        mat2.setSelectedIndex(0);
        sello.setSelectedIndex(0);
    }//GEN-LAST:event_limpiarActionPerformed

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

    private void guardarTintasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarTintasActionPerformed
        jButton1.setSelected(false);
        comprobarVacio();
        if(folioId == 0)
        {
            JOptionPane.showMessageDialog(null, "Seleccionar un pedido primero en la pestaña anterior","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            //se hace el guardado de los datos
            t1St = listaTintas1.getSelectedItem().toString();
            pI1St = kgIniT1.getText();
            pF1St = kgFinT1.getText();
            t2St = listaTintas2.getSelectedItem().toString();
            pI2St = kgIniT2.getText();
            pF2St = kgFinT2.getText();
            t3St = listaTintas3.getSelectedItem().toString();
            pI3St = kgIniT3.getText();
            pF3St = kgFinT3.getText();
            t4St = listaTintas4.getSelectedItem().toString();
            pI4St = kgIniT4.getText();
            pF4St = kgFinT4.getText();
            t5St = listaTintas5.getSelectedItem().toString();
            pI5St = kgIniT5.getText();
            pF5St = kgFinT5.getText();
            t6St = listaTintas6.getSelectedItem().toString();
            pI6St = kgIniT6.getText();
            pF6St = kgFinT6.getText();

            iniMezSt = mezIni.getText();
            finMezSt = mezFin.getText();
            iniAceSt = aceIni.getText();
            finAceSt = aceFin.getText();
            iniRetSt = retIni.getText();
            finRetSt = retFin.getText();
            iniSolventeSt = solventeIni.getText();
            finSolventeSt = solventeFin.getText();
            iniBarSt = barIni.getText();
            finBarSt = barFin.getText();
            
            stickySt = sticky.getText();
            Float stickyFt = Float.parseFloat(stickySt);

            String sql = "update tintas set tinta1 = '"+t1St+"', pIni1 = "+pI1St+", pFin1 = "+pF1St+", tinta2 = '"+t2St+"', pIni2 = "+pI2St+", pFin2 = "+pF2St+"," +
            " tinta3 = '"+t3St+"', pIni3 = "+pI3St+", pFin3 = "+pF3St+", tinta4 = '"+t4St+"', pIni4 = "+pI4St+", pFin4 = "+pF4St+"," +
            " tinta5 = '"+t5St+"', pIni5 = "+pI5St+", pFin5 = "+pF5St+", tinta6 = '"+t6St+"', pIni6 = "+pI6St+", pFin6 = "+pF6St+"," +
            " iniMezcla = "+iniMezSt+", finMezcla = "+finMezSt+"," +
            " iniAcetato = "+iniAceSt+", finAcetato = "+finAceSt+"," +
            " iniRetard = "+iniRetSt+", finRetard = "+finRetSt+"," +
            " iniSolvente = "+iniSolventeSt+", finSolvente = "+finSolventeSt+"," +
            " iniBarniz = "+iniBarSt+", finBarniz = "+finBarSt+", sticky = "+stickyFt+"" +
            " where folio_fk = "+folioId+"";

            try 
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
                calculaCostoGlobalTintas();//se inserta sa sumatoria de los costos en la tabla de tintas
                sumaTintasAPedido();//se suma el costo de tintas al costoTotal del pedido
                calculaPyG(folioId);
                //reiniciarCamposTintas();
                JOptionPane.showMessageDialog(null, "Se ha actualizado el registro de tintas: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Error al actualizar el registro de tintas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_guardarTintasActionPerformed

    //suma el costo de las tintas al costo total del pedido
    private void sumaTintasAPedido()
    {
        float costoTotal = 0f;
        float costoTintas = 0f;
        int yaSeSumo = 0;
        
        //verificamos si ya se sumo o no el costo de tintas al costo total
        String sql = "select seSumoAPedido from tintas where folio_fk = "+folioId+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                yaSeSumo = Integer.parseInt(rs.getString("seSumoAPedido"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        if (yaSeSumo == 0)//si no se ha sumado
        {
            sql = "select costoTotal from pedido where folio = "+folioId+"";
            try
            {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    costoTotal = Float.parseFloat(rs.getString("costoTotal"));
                }
                rs.close();
                st.close();
            }
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            sql = "select costoDeTintas from tintas where folio_fk = "+folioId+"";
            try
            {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    costoTintas = Float.parseFloat(rs.getString("costoDeTintas"));
                }
                rs.close();
                st.close();
            }
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            costoTotal += costoTintas;

            sql = "update pedido set costoTotal = "+costoTotal+" where folio = "+folioId+"";
            try
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            }
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            
            sql = "update tintas set seSumoAPedido = 1 where folio_fk = "+folioId+"";
            try
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            }
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
            
    }
    
    //suma el costo de las tintas al costo total del pedido cuando hubo una modificacion
    //ya se le resto el costo de tintas viejo
    private void sumaTintasAPedidoModificacion()
    {
        float costoTotal = 0f;
        float costoTintas = 0f;
        
        String sql = "select costoTotal from pedido where folio = "+folioId+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoTotal = Float.parseFloat(rs.getString("costoTotal"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        sql = "select costoDeTintas from tintas where folio_fk = "+folioId+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoTintas = Float.parseFloat(rs.getString("costoDeTintas"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        costoTotal += costoTintas;

        sql = "update pedido set costoTotal = "+costoTotal+" where folio = "+folioId+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //vacia los campos de tintas despues de que hubo un registro
    /*private void reiniciarCamposTintas()
    {
        listaTintas1.setSelectedIndex(0);
        kgIniT1.setText("");
        kgFinT1.setText("");
        listaTintas2.setSelectedIndex(0);
        kgIniT2.setText("");
        kgFinT2.setText("");
        listaTintas3.setSelectedIndex(0);
        kgIniT3.setText(""); 
        kgFinT3.setText(""); 
        listaTintas4.setSelectedIndex(0);
        kgIniT4.setText("");
        kgFinT4.setText(""); 
        listaTintas5.setSelectedIndex(0);
        kgIniT5.setText("");
        kgFinT5.setText("");
        listaTintas6.setSelectedIndex(0);
        kgIniT6.setText("");
        kgFinT6.setText("");

        mezIni.setText("");
        mezFin.setText(""); 
        aceIni.setText(""); 
        aceFin.setText("");
        retIni.setText(""); 
        retFin.setText("");
        solventeIni.setText(""); 
        solventeFin.setText("");
        barIni.setText("");
        barFin.setText("");
    }*/
    
    //devuelve el precio por kg de una tinta en especifico
    private float devuelvePrecioTinta(String nombreTinta)
    {
        float precio = 0f;//se toma el dolar a 20 pesos mexicanos
        if(nombreTinta.equals("N/A"))
        {
            precio = 0f;
        }
        if(nombreTinta.equals("Blanco Flexo Frente"))
        {
            precio = 94.0f;
        }
        if(nombreTinta.equals("Blanco Laminación"))
        {
            precio = 94.0f;
        }
        if(nombreTinta.equals("Amarillo Flexo Frente"))
        {
            precio = 111.2f;
        }
        if(nombreTinta.equals("Naranja Flexo Frente"))
        {
            precio = 139.0f;
        }
        if(nombreTinta.equals("Magenta Flexo Frente"))
        {
            precio = 122.0f;
        }
        if(nombreTinta.equals("Rojo Medio Flexo Frente"))
        {
            precio = 118f;
        }
        if(nombreTinta.equals("Azul Cyan Flexo Frente"))
        {
            precio = 122.0f;
        }
        if(nombreTinta.equals("Azul Reflex Flexo Frente"))
        {
            precio = 150.0f;
        }
        if(nombreTinta.equals("Verde Flexo Frente"))
        {
            precio = 148.0f;
        }
        if(nombreTinta.equals("Violeta Flexo Frente"))
        {
            precio = 268.0f;
        }
        if(nombreTinta.equals("Negro Flexo Frente"))
        {
            precio = 104.0f;
        }
        if(nombreTinta.equals("Acqua"))
        {
            precio = 236.8f;
        }
        if(nombreTinta.equals("Verde 361"))
        {
            precio = 132.4f;
        }
        if(nombreTinta.equals("Cafe 4625"))
        {
            precio = 118.6f;
        }
        if(nombreTinta.equals("Cafe 732"))
        {
            precio = 110.8f;
        }
        if(nombreTinta.equals("Rosa"))
        {
            precio = 81.0f;
        }
        if(nombreTinta.equals("Cool Gray 9"))
        {
            precio = 80.92f;
        }
        if(nombreTinta.equals("Cool Gray 8"))
        {
            precio = 79.6f;
        }
        if(nombreTinta.equals("Plata"))
        {
            precio = 200f;
        }
        if(nombreTinta.equals("Oro"))
        {
            precio = 206.0f;
        }
        if(nombreTinta.equals("Ftalo"))
        {
            precio = 255.6f;
        }
        if(nombreTinta.equals("Mezcla 80/20"))
        {
            precio = 24.6f;
        }
        if(nombreTinta.equals("Acetato"))
        {
            precio = 40.0f;//precio de prueba
        }
        if(nombreTinta.equals("Retardante"))
        {
            precio = 24.6f;//precio de prueba
        }
        if(nombreTinta.equals("Barniz"))
        {
            precio = 76.0f;
        }
        if(nombreTinta.equals("Solvente acondicionador"))
        {
            precio = 27.4f;
        }
        
        return precio;
    }
    
    private float calculaCostoTintaIndividual(String nombreTinta, JTextField kgInicio, JTextField kgFinal)
    {
        float costo = 0f;//lo que gastaron de tinta en dinero
        float precio  = devuelvePrecioTinta(nombreTinta);//el precio por kg de la tinta
        float diferencia = 0f;//diferencia entre los pesos
        if(Float.parseFloat(kgFinal.getText()) <= Float.parseFloat(kgInicio.getText()))//si los datos son correctos
        {
            diferencia = Float.parseFloat(kgInicio.getText()) - Float.parseFloat(kgFinal.getText());
        }
        else if(Float.parseFloat(kgFinal.getText()) == 0 && Float.parseFloat(kgInicio.getText()) == 0)//si no ingreso datos
        {
            diferencia = 0f;
        }
        else//si ingresaron datos imposibles
        {
            JOptionPane.showMessageDialog(null, "Ingrese un peso final menor al peso inicial","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
        
        costo = diferencia * precio;//se calcula el costo de la tinta
        
        return costo;
    }
    
    /*reestablece el costo total del pedido a antes de que se le sumaran
    los costos de tintas, para asi poder agregarle el nuevo costo de tintas*/
    private void restaCostoTintasViejo()
    {
        float costoTotalViejo = 0f;
        float costoTintasViejo = 0f;
        float costoTotalReestablecido = 0f;
        
        String sql = "select costoTotal from pedido where folio = "+folioId+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoTotalViejo = Float.parseFloat(rs.getString("costoTotal"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        sql = "select costoDeTintas from tintas where folio_fk = "+folioId+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                costoTintasViejo = Float.parseFloat(rs.getString("costoDeTintas"));
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        costoTotalReestablecido = costoTotalViejo - costoTintasViejo;
        
        sql = "update pedido set costoTotal = "+costoTotalReestablecido+" where folio = "+folioId+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //calcula el costo de todas las tintas usadas y lo guarda
    private void calculaCostoGlobalTintas()
    {
        Float precioSticky = 0.1135f;//precio por centimetro cuadrado de sticky
        
        float costoGlobal = 0f;
        costoGlobal += calculaCostoTintaIndividual(listaTintas1.getSelectedItem().toString(), kgIniT1, kgFinT1);
        costoGlobal += calculaCostoTintaIndividual(listaTintas2.getSelectedItem().toString(), kgIniT2, kgFinT2);
        costoGlobal += calculaCostoTintaIndividual(listaTintas3.getSelectedItem().toString(), kgIniT3, kgFinT3);
        costoGlobal += calculaCostoTintaIndividual(listaTintas4.getSelectedItem().toString(), kgIniT4, kgFinT4);
        costoGlobal += calculaCostoTintaIndividual(listaTintas5.getSelectedItem().toString(), kgIniT5, kgFinT5);
        costoGlobal += calculaCostoTintaIndividual(listaTintas6.getSelectedItem().toString(), kgIniT6, kgFinT6);
        costoGlobal += calculaCostoTintaIndividual("Mezcla 80/20", mezIni, mezFin);
        costoGlobal += calculaCostoTintaIndividual("Acetato", aceIni, aceFin);
        costoGlobal += calculaCostoTintaIndividual("Retardante", retIni, retFin);
        costoGlobal += calculaCostoTintaIndividual("Barniz", barIni, barFin);
        costoGlobal += calculaCostoTintaIndividual("Solvente acondicionador", solventeIni, solventeFin);
        //el sticky es aparte porque no tiene un peso inicial y uno final, se calcula diferente
        costoGlobal += (precioSticky * Float.parseFloat(sticky.getText()));
        
        String sql = "update tintas set costoDeTintas = "+costoGlobal+" where folio_fk = "+folioId+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void solventeIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_solventeIniKeyTyped
        soloFlotantes(evt, solventeIni);
    }//GEN-LAST:event_solventeIniKeyTyped

    private void solventeFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_solventeFinKeyTyped
        soloFlotantes(evt, solventeFin);
    }//GEN-LAST:event_solventeFinKeyTyped

    private void barIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barIniKeyTyped
        soloFlotantes(evt, barIni);
    }//GEN-LAST:event_barIniKeyTyped

    private void barFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barFinKeyTyped
        soloFlotantes(evt, barFin);
    }//GEN-LAST:event_barFinKeyTyped

    private void guardCamTintasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardCamTintasActionPerformed
        jButton1.setSelected(false);
        comprobarVacio();
        //se hace el guardado de los datos
        t1St = listaTintas1.getSelectedItem().toString();
        pI1St = kgIniT1.getText();
        pF1St = kgFinT1.getText();
        t2St = listaTintas2.getSelectedItem().toString();
        pI2St = kgIniT2.getText();
        pF2St = kgFinT2.getText();
        t3St = listaTintas3.getSelectedItem().toString();
        pI3St = kgIniT3.getText();
        pF3St = kgFinT3.getText();
        t4St = listaTintas4.getSelectedItem().toString();
        pI4St = kgIniT4.getText();
        pF4St = kgFinT4.getText();
        t5St = listaTintas5.getSelectedItem().toString();
        pI5St = kgIniT5.getText();
        pF5St = kgFinT5.getText();
        t6St = listaTintas6.getSelectedItem().toString();
        pI6St = kgIniT6.getText();
        pF6St = kgFinT6.getText();

        iniMezSt = mezIni.getText();
        finMezSt = mezFin.getText();
        iniAceSt = aceIni.getText();
        finAceSt = aceFin.getText();
        iniRetSt = retIni.getText();
        finRetSt = retFin.getText();
        iniSolventeSt = solventeIni.getText();
        finSolventeSt = solventeFin.getText();
        iniBarSt = barIni.getText();
        finBarSt = barFin.getText();
        
        stickySt = sticky.getText();
        Float stickyFt = Float.parseFloat(stickySt);

        String sql = "update tintas set tinta1 = '"+t1St+"', pIni1 = "+pI1St+", pFin1 = "+pF1St+", tinta2 = '"+t2St+"', pIni2 = "+pI2St+", pFin2 = "+pF2St+"," +
        " tinta3 = '"+t3St+"', pIni3 = "+pI3St+", pFin3 = "+pF3St+", tinta4 = '"+t4St+"', pIni4 = "+pI4St+", pFin4 = "+pF4St+"," +
        " tinta5 = '"+t5St+"', pIni5 = "+pI5St+", pFin5 = "+pF5St+", tinta6 = '"+t6St+"', pIni6 = "+pI6St+", pFin6 = "+pF6St+"," +
        " iniMezcla = "+iniMezSt+", finMezcla = "+finMezSt+"," +
        " iniAcetato = "+iniAceSt+", finAcetato = "+finAceSt+"," +
        " iniRetard = "+iniRetSt+", finRetard = "+finRetSt+"," +
        " iniSolvente = "+iniSolventeSt+", finSolvente = "+finSolventeSt+"," +
        " iniBarniz = "+iniBarSt+", finBarniz = "+finBarSt+", sticky = "+stickyFt+"" +
        " where folio_fk = "+folioId+"";

        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            restaCostoTintasViejo();//reestablecemos el costo total a antes de que se sumara el costo de tintas
            calculaCostoGlobalTintas();//se inserta sa sumatoria de los costos en la tabla de tintas
            sumaTintasAPedidoModificacion();//se suma el costo de tintas al costoTotal del pedido cuando se hizo una modificacion
            calculaPyG(folioId);
            //reiniciarCamposTintas();
            JOptionPane.showMessageDialog(null, "Se ha actualizado el registro de tintas: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro de tintas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }    
    }//GEN-LAST:event_guardCamTintasActionPerformed

    private void disenioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_disenioKeyTyped
        soloFlotantes(evt, disenio);
    }//GEN-LAST:event_disenioKeyTyped

    private void impKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impKeyTyped
        limitarInsercion(40, evt, imp);
    }//GEN-LAST:event_impKeyTyped

    private void stickyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stickyKeyTyped
        soloFlotantes(evt, sticky);
    }//GEN-LAST:event_stickyKeyTyped
    
    //cuando cambia la fecha inicial, tambien la sugerencia de fecha de compromiso
    private void onChangeFini(){
        
        
        fIn2.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                sugerenciaFecha();//establece la fecha de compromiso
            }
        });
    }
    
    //establece la fecha de compromiso
    public void sugerenciaFecha(){
        
                DateTime date = new DateTime(fIn2.getText()); //se obtiene la fecha inicial
                DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MMMM-dd");
                
                java.util.Date dtUtil = null;
                Calendar cal = Calendar.getInstance(); 
                
                date = date.plusWeeks(3);//se le suman las tres semanas
                sug.setText("Sugerencia: "+dtf.print(date));
                
                
                dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
                try {
                    dtUtil = new SimpleDateFormat("yyyy-MM-dd").parse(dtf.print(date));
                } catch (ParseException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                cal.setTime(dtUtil);
                fC2.setSelectedDate(cal);//se establece la fecha de compromiso
    }
    
    
    
    //Se llenan los campos de pedido para modificacion
    public void llenarCamposMod(int folio) {
        
        String sql = "select impresion, "
                + "estatus, "
                + "autorizo, "
                + "devolucion, "
                + "fIngreso, "
                + "fCompromiso, "
                + "fPago, "
                + "fTermino, "
                + "grabados, "
                + "costoDisenio, "
                + "anticipo, "
                + "descuento, "
                + "porcentajeIVA,"
                + "gastosFijos, "
                + "subtotal"
                + " from pedido where folio = "+folio+"";
        java.util.Date date = null;
        Calendar cal = Calendar.getInstance();   
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {//a la vez que se hace la consulta se establecen los datos en la ventana
                
                imp.setText(rs.getString("impresion"));
                estableceEstatus(rs.getString("estatus"));
                dev.setText(rs.getString("devolucion"));
                 
                //Para establecer las fechas obtenidas de la base de datos
                try 
                {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fIngreso"));
                } 
                catch (ParseException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                cal.setTime(date);
                fIn2.setSelectedDate(cal);
         
                try 
                {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fCompromiso"));
                } 
                catch (ParseException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                cal.setTime(date);
                fC2.setSelectedDate(cal);
                
                try 
                {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fPago"));
                } 
                catch (ParseException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                cal.setTime(date);
                fP2.setSelectedDate(cal);
                
                try 
                {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fTermino"));
                } 
                catch (ParseException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                cal.setTime(date);
                fT.setSelectedDate(cal);
                
                try 
                {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("autorizo"));
                } 
                catch (ParseException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                cal.setTime(date);
                autoCalendario.setSelectedDate(cal);
                
                grab.setText(rs.getString("grabados"));
                disenio.setText(rs.getString("costoDisenio"));
                anti.setText(rs.getString("anticipo"));
                decu.setText(rs.getString("descuento"));
                
                int iva = Integer.parseInt(rs.getString("porcentajeIVA"));
                if (iva == 0)
                {
                    porIVA.setSelectedIndex(0);
                }
                else if(iva == 8)
                {
                    porIVA.setSelectedIndex(1);
                }
                else
                {
                    porIVA.setSelectedIndex(2);
                }
                
                respaldoSub = Float.parseFloat(rs.getString("subtotal")) - Float.parseFloat(rs.getString("grabados"));
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } 
    }
    
    private void estableceEstatus(String estatusConsultado)
    {
        if(estatusConsultado.equals("DISEÑO"))
        {
            estatus.setSelectedIndex(0);
        }
        else if(estatusConsultado.equals("PROCESO"))
        {
            estatus.setSelectedIndex(1);
        }
        else if(estatusConsultado.equals("COBRANZA"))
        {
            estatus.setSelectedIndex(2);
        }
        else if(estatusConsultado.equals("PAGADO"))
        {
            estatus.setSelectedIndex(3);
        }
    }
    
    private void onChangeKilos(){
        kgPart.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularEnCampoPU();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { 
                calcularEnCampoPU();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
               
            }
        });
    }
    
    
    private void onChangePz(){
        pz.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularEnCampoPU();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { 
                calcularEnCampoPU();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
               
            }
        });
    }
    
    private void onChangePzFinales(){
        pzFinales.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularEnCampoPU();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { 
                calcularEnCampoPU();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
               
            }
        });
    }
    
    //Comprobar si ciertos campos estan vacios, los inicializa
    private void comprobarVacio(){
        
        if(desa.getText().equals("") || desa.getText().equals(".")){
            desa.setText("0");
        }
        //Costos
        if(grab.getText().equals("") || grab.getText().equals(".")){
            grab.setText("0");
        }
        if(precioUni.getText().equals("") || precioUni.getText().equals(".")){
            precioUni.setText("0");
        }
        if(disenio.getText().equals("") || disenio.getText().equals(".")){
            disenio.setText("0");
        }
        if(anti.getText().equals("") || anti.getText().equals(".")){
            anti.setText("0");
        }
        if(decu.getText().equals("") || decu.getText().equals(".")){
            decu.setText("0");
        }
        if(pz.getText().equals("")){
            pz.setText("0");
        }
        if(pzFinales.getText().equals("")){
            pzFinales.setText("0");
        }
        if(importe.getText().equals("") || importe.getText().equals(".")){
            importe.setText("0");
        }
        if(kgPart.getText().equals("") || kgPart.getText().equals(".")){
            kgPart.setText("0");
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
        if(solventeIni.getText().equals("") || solventeIni.getText().equals(".")){
            solventeIni.setText("0");
        }
        if(solventeFin.getText().equals("") || solventeFin.getText().equals(".")){
            solventeFin.setText("0");
        } 
        if(barIni.getText().equals("") || barIni.getText().equals(".")){
            barIni.setText("0");
        }
        if(barFin.getText().equals("") || barFin.getText().equals(".")){
            barFin.setText("0");
        }
        if(sticky.getText().equals("") || sticky.getText().equals(".")){
            sticky.setText("0");
        }
    }
  
    //establece todos los campos y opciones en un estado de default
    public void vacearComponentes(){
        
        modPed.setSelected(false);
        checkPz.setSelected(false);
        checkKg.setSelected(false);
        
        nomCli.setText("Buscar un Cliente");
        cliente.setText("Selecciona un Cliente");
        textBusPed.setText("Nombre de impresion");
        imp.setText("");
        estatus.setSelectedIndex(0);
        autoCalendario.setText("");
        dev.setText("");
        grab.setText("0");
        disenio.setText("0");
        anti.setText("0");
        decu.setText("0");
        porIVA.setSelectedIndex(0);
        sug.setText("Sugerencia");
        nomImpresion.setText("Impresion");
        folioVis.setText("Folio");
        pig.setText("");
        tp.setText("");
        med.setText("");
        desa.setText("0");
        sello.setSelectedIndex(0);
        mat1.setSelectedIndex(0);
        mat2.setSelectedIndex(0);
        cal1.setText("");
        cal2.setText("");
        c11.setText("");
        c12.setText("");
        c13.setText("");
        c14.setText("");
        c15.setText("");
        c16.setText("");
        c21.setText("");
        c22.setText("");
        c23.setText("");
        c24.setText("");
        c25.setText("");
        c26.setText("");
        precioUni.setText("0");
        pz.setText("0");
        pzFinales.setText("0");
        kgPart.setText("0");
        importe.setText("0");
        //se vacian las tablas
        modelo.setRowCount(0);
        modeloMod.setRowCount(0);
        modeloP.setRowCount(0);
        //se hace invisible lo que tiene que ver con la modificacion del pedido
        panelMod.setVisible(false); 
        btnMod.setEnabled(false);
        btnMod.setVisible(false);
        saveP.setVisible(true);
        saveP.setEnabled(false);
        savePart.setEnabled(false);
       //se reinician estas varibles globales
        idCliente = 0;
        folioId = 0;
        folioMod = 0;
        
    }
    
    //calcula los kg finales del pedido
    private float calculaKgFinalesPedido(int folio)
    {
        Statement st2, st3, st4, st5;
        ResultSet rs2, rs3, rs4, rs5;
        float sumatoriaPartida = 0f;
        float sumatoriaPedido = 0f;//se inicializ en 0 para que si no hace calculos se retorna 0
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
                                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                        ex.printStackTrace();
                                    }
                                }
                                rs4.close();
                                st4.close();
                            }
                            catch(SQLException ex)
                            {
                                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                        rs3.close();
                        st3.close();
                    }
                    catch(SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                        ex.printStackTrace();
                                    }
                                }
                                rs4.close();
                                st4.close();
                            }
                            catch(SQLException ex)
                            {
                                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                        rs3.close();
                        st3.close();
                    }
                    catch(SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                        ex.printStackTrace();
                                    }
                                }
                                rs4.close();
                                st4.close();
                            }
                            catch(SQLException ex)
                            {
                                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                        rs3.close();
                        st3.close();
                    }
                    catch(SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
            
        return sumatoriaPedido;
    }
    
    //calcula los gastos fijos por kg del pedido
    private float calculaGfKg(int folio)
    {
        //gastosFijosPorKg,gastosFijosDelRango, SumatoriaDeKgFinalesDelRango
        float gfkg = 0f, gfr = 0f, sumatoriaRango = 0f;
        String sql = "select gastosFijos,kgFinalesRango from pedido where folio = "+folio+" and gastosFijos is not null and kgFinalesRango is not null";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                gfr = Float.parseFloat(rs.getString("gastosFijos"));//se guardan los gastos fijos del rango
                sumatoriaRango = Float.parseFloat(rs.getString("kgFinalesRango"));
                if(sumatoriaRango != 0)//si el denominador es diferente de 0, se hace el calculo
                    gfkg = gfr / sumatoriaRango;
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        if(gfkg != 0)//si se hizo el calculo correctamente, retorana los gastos fijos por kg
            return gfkg;
        else//si no, retorna 0
            return 0;
    }
    
    //no usar de st y rs 2 a 5
    private void calculaPyG(int folio)
    {
        Statement st6, st7;
        ResultSet rs6, rs7;
        float subtotal = 0f, costoTotal = 0f, descuento = 0f;//datos de la formula en la base de datos
        Float PyG = 0f;//se inicializa en 0 para que si hay un error de calculo, ingrese 0
        String sql = "select fTermino from pedido where folio = "+folio+"";//entra solo a los pedidos que ya hayan sido terminados
        String sql2 = "";
        try
        {
            st7 = con.createStatement();
            rs7 = st7.executeQuery(sql);
            while(rs7.next())
            {
                if(rs7.getString("fTermino").equals("2018-01-01") == false)
                {
                    sql2 = "select subtotal, costoTotal, descuento from pedido where folio = "+folio+"";
                    try
                    {
                        st6 = con.createStatement();
                        rs6 = st6.executeQuery(sql2);
                        while(rs6.next())
                        {
                            subtotal = Float.parseFloat(rs6.getString("subtotal"));
                            costoTotal = Float.parseFloat(rs6.getString("costoTotal"));
                            descuento = Float.parseFloat(rs6.getString("descuento"));
                        }
                        rs6.close();
                        st6.close();
                    }
                    catch(SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                    
                    float kgFnPe = calculaKgFinalesPedido(folio);//se obtienen los kg finales del pedido para la formula
                    float gf = calculaGfKg(folio);//se obtienen los gastos fijos por kg para la formula
                    PyG = subtotal - costoTotal - descuento - ( kgFnPe * gf);//se calculan las perdidas y ganancias con la formula mas nueva
                }
            }
            rs7.close();
            st7.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
                    
                    
        //se guarda en la base de datos
        sql = "update pedido set perdidasYGanancias = "+PyG+" where folio = "+folio+"";
        try
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }   
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton abreProcesos;
    private javax.swing.JTextField aceFin;
    private javax.swing.JTextField aceIni;
    private javax.swing.JTextField anti;
    private datechooser.beans.DateChooserCombo autoCalendario;
    private javax.swing.JTextField barFin;
    private javax.swing.JTextField barIni;
    private javax.swing.JButton btnBusPed;
    private javax.swing.JButton btnMod;
    private javax.swing.JToggleButton busPedido;
    private javax.swing.JTextField c11;
    private javax.swing.JTextField c12;
    private javax.swing.JTextField c13;
    private javax.swing.JTextField c14;
    private javax.swing.JTextField c15;
    private javax.swing.JTextField c16;
    private javax.swing.JTextField c21;
    private javax.swing.JTextField c22;
    private javax.swing.JTextField c23;
    private javax.swing.JTextField c24;
    private javax.swing.JTextField c25;
    private javax.swing.JTextField c26;
    private javax.swing.JTextField cal1;
    private javax.swing.JTextField cal2;
    private javax.swing.JCheckBox checkKg;
    private javax.swing.JCheckBox checkPz;
    private javax.swing.JCheckBox checkPzFinales;
    private javax.swing.JTextField cliente;
    private javax.swing.JTextField decu;
    private javax.swing.JTextField desa;
    private javax.swing.JTextField dev;
    private javax.swing.JTextField disenio;
    private javax.swing.JComboBox estatus;
    private datechooser.beans.DateChooserCombo fC2;
    private datechooser.beans.DateChooserCombo fIn2;
    private datechooser.beans.DateChooserCombo fP2;
    private datechooser.beans.DateChooserCombo fT;
    private javax.swing.JTextField folioVis;
    private javax.swing.JTextField grab;
    private javax.swing.JButton guardCamTintas;
    private javax.swing.JButton guardarTintas;
    private javax.swing.JTextField imp;
    private javax.swing.JTextField importe;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel52;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTextField jTextField83;
    private javax.swing.JTextField jTextField84;
    private javax.swing.JTextField jTextField85;
    private javax.swing.JTextField kgFinT1;
    private javax.swing.JTextField kgFinT2;
    private javax.swing.JTextField kgFinT3;
    private javax.swing.JTextField kgFinT4;
    private javax.swing.JTextField kgFinT5;
    private javax.swing.JTextField kgFinT6;
    private javax.swing.JTextField kgIniT1;
    private javax.swing.JTextField kgIniT2;
    private javax.swing.JTextField kgIniT3;
    private javax.swing.JTextField kgIniT4;
    private javax.swing.JTextField kgIniT5;
    private javax.swing.JTextField kgIniT6;
    private javax.swing.JTextField kgPart;
    private javax.swing.JButton limpiar;
    private javax.swing.JComboBox listaTintas1;
    private javax.swing.JComboBox listaTintas2;
    private javax.swing.JComboBox listaTintas3;
    private javax.swing.JComboBox listaTintas4;
    private javax.swing.JComboBox listaTintas5;
    private javax.swing.JComboBox listaTintas6;
    private javax.swing.JComboBox mat1;
    private javax.swing.JComboBox mat2;
    private javax.swing.JTextField med;
    private javax.swing.JTextField mezFin;
    private javax.swing.JTextField mezIni;
    private javax.swing.JCheckBox modPed;
    private javax.swing.JTextField nomCli;
    private javax.swing.JTextField nomImpresion;
    private javax.swing.JTabbedPane paPed;
    private javax.swing.JPanel panPart;
    private javax.swing.JPanel panelCliente;
    private javax.swing.JPanel panelMod;
    private javax.swing.JTextField pig;
    private javax.swing.JComboBox porIVA;
    private javax.swing.JTextField precioUni;
    private javax.swing.JTextField pz;
    private javax.swing.JTextField pzFinales;
    private javax.swing.JButton regP;
    private javax.swing.JTextField retFin;
    private javax.swing.JTextField retIni;
    private javax.swing.JButton saveP;
    private javax.swing.JToggleButton savePart;
    private javax.swing.JComboBox sello;
    private javax.swing.JTextField solventeFin;
    private javax.swing.JTextField solventeIni;
    private javax.swing.JTextField sticky;
    private javax.swing.JLabel sug;
    private javax.swing.JTable tablaC;
    private javax.swing.JTable tablaModPed;
    private javax.swing.JTable tablaP;
    private javax.swing.JTextField textBusPed;
    private javax.swing.JTextField tp;
    // End of variables declaration//GEN-END:variables
}
