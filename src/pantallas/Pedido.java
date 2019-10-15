
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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
    
    
    String impSt=null, desSt=null, kgSt=null, medSt=null, estSt=null, devSt=null, disSt=null, pigSt=null, tipoSt=null, selloSt=null, 
            hojaSt=null, deSt=null, mat1St=null, cal1St=null, mat2St=null, cal2St=null, tinta1St=null, tinta2St=null, fInSt=null, fComSt=null, 
            fPagoSt=null, fTerminoSt = null, grabSt=null, pUnitSt=null, subSt=null, totSt=null, antiSt=null, restoSt=null, descuSt=null, costeGrabSt=null, 
            costeDisSt=null, piezasSt=null, importeSt=null, kgParidaSt=null, pzFinalesSt = null;
    
    float grabados = 0, precioUnitario=0, kilos=0, totalF=0, restoF=0, subF=0, iva=0 ,antiF=0, descuF=0;
    int piezasI = 0, pzFinalesI = 0;
    float importeF = 0;
    
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
        comprobarVacio();
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
        
        llenarListas();//se llenan las listas desplegables
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
        jTimeChooser1 = new lu.tudor.santec.jtimechooser.JTimeChooser();
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
        jScrollPane5 = new javax.swing.JScrollPane();
        imp = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        dev = new javax.swing.JTextField();
        auto = new javax.swing.JTextField();
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
        jLabel167 = new javax.swing.JLabel();
        sta = new javax.swing.JComboBox();
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
        panelCliente.setPreferredSize(new java.awt.Dimension(512, 140));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(panelClienteLayout.createSequentialGroup()
                        .addComponent(nomCli, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelClienteLayout.setVerticalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelMod.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelMod.setPreferredSize(new java.awt.Dimension(469, 140));

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
                    .addComponent(jScrollPane1)
                    .addGroup(panelModLayout.createSequentialGroup()
                        .addComponent(btnBusPed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textBusPed, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelModLayout.setVerticalGroup(
            panelModLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelModLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBusPed)
                    .addComponent(textBusPed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel97.setForeground(new java.awt.Color(0, 102, 153));
        jLabel97.setText("Impresión:");

        imp.setColumns(20);
        imp.setForeground(new java.awt.Color(0, 153, 153));
        imp.setRows(5);
        imp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                impKeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(imp);

        jLabel5.setForeground(new java.awt.Color(0, 102, 153));
        jLabel5.setText("Autorizó:");

        jLabel150.setForeground(new java.awt.Color(0, 102, 153));
        jLabel150.setText("Devolución:");

        dev.setForeground(new java.awt.Color(0, 153, 153));
        dev.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                devKeyTyped(evt);
            }
        });

        auto.setForeground(new java.awt.Color(0, 153, 153));
        auto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                autoKeyTyped(evt);
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
        anti.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                antiKeyTyped(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(0, 102, 153));
        jLabel30.setText("Descuento: $");

        decu.setForeground(new java.awt.Color(0, 153, 153));
        decu.setText("0");
        decu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                decuKeyTyped(evt);
            }
        });

        grab.setForeground(new java.awt.Color(0, 153, 153));
        grab.setText("0");
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grab, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anti, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decu, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnMod, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(fIn2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                    .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(fC2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(fP2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel97)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel150, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dev, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(auto, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel99)
                                    .addComponent(fT, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(sug, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(auto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel150)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel97)))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel95)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fIn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel158)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel99)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel157)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fC2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sug)
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel30)
                    .addComponent(decu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(grab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(panelCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addComponent(panelMod, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paPed.addTab("Pedido", jPanel4);

        panPart.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
            .addGap(0, 593, Short.MAX_VALUE)
            .addGroup(panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panPartLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3)
                        .addGroup(panPartLayout.createSequentialGroup()
                            .addComponent(busPedido)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(nomImpresion, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        panPartLayout.setVerticalGroup(
            panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 113, Short.MAX_VALUE)
            .addGroup(panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panPartLayout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addGroup(panPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(busPedido)
                        .addComponent(nomImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        cal2.setForeground(new java.awt.Color(0, 153, 153));
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
        pig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pigKeyTyped(evt);
            }
        });

        tp.setForeground(new java.awt.Color(0, 153, 153));
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
        med.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                medKeyTyped(evt);
            }
        });

        jLabel98.setForeground(new java.awt.Color(0, 102, 153));
        jLabel98.setText("Desarrollo:");

        desa.setForeground(new java.awt.Color(0, 153, 153));
        desa.setText("0");
        desa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                desaKeyTyped(evt);
            }
        });

        jLabel167.setForeground(new java.awt.Color(0, 102, 153));
        jLabel167.setText("Estatus:");

        sta.setForeground(new java.awt.Color(0, 153, 153));
        sta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Estado" }));

        jLabel169.setForeground(new java.awt.Color(0, 102, 153));
        jLabel169.setText("Sello:");

        sello.setForeground(new java.awt.Color(0, 153, 153));
        sello.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Sello" }));

        jLabel171.setForeground(new java.awt.Color(0, 102, 153));
        jLabel171.setText("Material 1:");

        mat1.setForeground(new java.awt.Color(0, 153, 153));
        mat1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Material" }));

        cal1.setForeground(new java.awt.Color(0, 153, 153));
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
        c11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c11KeyTyped(evt);
            }
        });

        c21.setForeground(new java.awt.Color(0, 153, 153));
        c21.setBorder(null);
        c21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c21KeyTyped(evt);
            }
        });

        c12.setForeground(new java.awt.Color(0, 153, 153));
        c12.setBorder(null);
        c12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c12KeyTyped(evt);
            }
        });

        c22.setForeground(new java.awt.Color(0, 153, 153));
        c22.setBorder(null);
        c22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c22KeyTyped(evt);
            }
        });

        c13.setForeground(new java.awt.Color(0, 153, 153));
        c13.setBorder(null);
        c13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c13KeyTyped(evt);
            }
        });

        c23.setForeground(new java.awt.Color(0, 153, 153));
        c23.setBorder(null);
        c23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c23KeyTyped(evt);
            }
        });

        c14.setForeground(new java.awt.Color(0, 153, 153));
        c14.setBorder(null);
        c14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c14KeyTyped(evt);
            }
        });

        c24.setForeground(new java.awt.Color(0, 153, 153));
        c24.setBorder(null);
        c24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c24KeyTyped(evt);
            }
        });

        c15.setForeground(new java.awt.Color(0, 153, 153));
        c15.setBorder(null);
        c15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c15KeyTyped(evt);
            }
        });

        c25.setForeground(new java.awt.Color(0, 153, 153));
        c25.setBorder(null);
        c25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c25KeyTyped(evt);
            }
        });

        c16.setForeground(new java.awt.Color(0, 153, 153));
        c16.setBorder(null);
        c16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c16KeyTyped(evt);
            }
        });

        c26.setForeground(new java.awt.Color(0, 153, 153));
        c26.setBorder(null);
        c26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c26KeyTyped(evt);
            }
        });

        precioUni.setForeground(new java.awt.Color(0, 153, 153));
        precioUni.setText("0");
        precioUni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precioUniKeyTyped(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 102, 153));
        jLabel8.setText("$");

        pz.setForeground(new java.awt.Color(0, 153, 153));
        pz.setText("0");
        pz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pzKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 102, 153));
        jLabel10.setText("Kg");

        kgPart.setForeground(new java.awt.Color(0, 153, 153));
        kgPart.setText("0");
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
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(precioUni, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel38))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(checkPz, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pz, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(kgPart)
                                            .addComponent(checkKg, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(pzFinales, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(checkPzFinales))))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(318, 318, 318)
                                        .addComponent(limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(savePart)))
                                .addGap(0, 7, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(folioVis, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(17, 17, 17)
                                        .addComponent(jLabel170)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pig, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel167))
                                            .addComponent(jLabel171, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel172, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(mat2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(mat1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(med)
                                                .addComponent(sta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel174)
                                            .addComponent(jLabel168, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel98, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel169, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sello, 0, 154, Short.MAX_VALUE)
                                    .addComponent(desa)
                                    .addComponent(tp)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel173)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cal2, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                    .addComponent(cal1))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(jLabel3))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(c11)
                                .addComponent(c12)
                                .addComponent(c13)
                                .addComponent(c14)
                                .addComponent(c15, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(c16, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(114, 114, 114)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel4))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(c21)
                                .addComponent(c22)
                                .addComponent(c23)
                                .addComponent(c24)
                                .addComponent(c25, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(c26, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(folioVis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel168)
                    .addComponent(jLabel170)
                    .addComponent(pig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel166)
                            .addComponent(desa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel98))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel174))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel173)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel167))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel171))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel172)))
                    .addComponent(med, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(precioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(pz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkPz))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(kgPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(pzFinales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(checkKg)
                            .addComponent(checkPzFinales))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(savePart, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panPart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paPed.addTab("Partida", jPanel2);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(regP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(abreProcesos, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
                        .addComponent(modPed))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paPed))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paPed, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        paPed.getAccessibleContext().setAccessibleName("ll");

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    //llena las listas desplegables
    private void llenarListas(){
        mat1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALTA", "BAJA", "BOPP", "CPP","BOPP/BOPP","BAJA/BOPP","BAJA/PET","BOPP/PET","CPP/PET"}));
        mat2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NO APLICA","ALTA", "BAJA", "BOPP", "CPP","BOPP/BOPP","BAJA/BOPP","BAJA/PET","BOPP/PET","CPP/PET"}));
        sta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DISEÑO", "PROCESO", "COBRANZA", "PAGADO"}));
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

        devSt = dev.getText();//
        fInSt = fIn2.getText();//
        fComSt = fC2.getText();//
        fPagoSt = fP2.getText();//
        fTerminoSt = fT.getText();

        grabSt = grab.getText();
        grabados = Float.parseFloat(grabSt);//

        antiSt = anti.getText();
        antiF = Float.parseFloat(antiSt);//

        descuSt = decu.getText();
        descuF = Float.parseFloat(descuSt);
        //se guardan los datos en la base
        String sql = " insert into pedido(impresion, fIngreso, fCompromiso, fPago, fTermino, grabados, subtotal, total, anticipo, resto, devolucion," +
        " descuento, idC_fk, kgDesperdicioPe, porcentajeDespPe, costoTotal, gastosFijos, perdidasYGanancias, autorizo, sumatoriaBolseoP, "
                + "matComPe, matProPe) "
        + "values('"+impSt+"', '"+fInSt+"', '"+fComSt+"', '"+fPagoSt+"', '"+fTerminoSt+"', "+grabados+", 0, 0, "+antiF+", 0, '"+devSt+"'," +
        " "+descuF+", "+idCliente+", 0, 0, 0, 0, 0, '"+auto.getText()+"', 0, 0, 0)";

        try 
        {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            JOptionPane.showMessageDialog(null, "Se ha guardado el pedido", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Hay un error con los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_savePActionPerformed

   
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
            st.close();
            rs.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_nomCliCaretUpdate
    
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
            st.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la tabla de pedidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Boton: Guardar Partida
    private void savePartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePartActionPerformed
        savePart.setSelected(false);
        comprobarVacio();
        String sql;
            medSt = med.getText();
            estSt = sta.getSelectedItem().toString();
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
            sql = "insert into partida(estatus, medida, piezas, tipo, sello, pigmento, mat1, calibre1, mat2, calibre2, precioUnitaro," +
            "importe, kgPartida, folio_fk, c1t1, c1t2, c1t3, c1t4, c1t5, c1t6, c2t1, c2t2, c2t3, c2t4, c2t5, c2t6, desarrollo, kgDesperdicio, porcentajeDesp, "
            + "costoMaterialTotal, costoPartida, pzFinales, manera) "
            + "values('"+estSt+"', "
                    + "'"+medSt+"', "
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
                setHojas();//enumera la partida
                establecerSubtotalPedido(st);//Para sumar todos los importes de las partidas y guargar el total en pedido
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
            stAux = con.createStatement();
            while(rs.next())
            {
                contadorRs++;
                idPartida = rs.getInt("idPar");
                sql1 = "update partida set hoja = "+contadorRs+" where idPar = "+idPartida+"";//se inserta el numero de hoja de la partida
                stAux.execute(sql1);            
            }
            
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                idPartida = rs.getInt("idPar");               
                sql1 = "update partida set de = "+contadorRs+" where idPar = "+idPartida+"";//se inserta el numero De de la partida
                stAux.execute(sql1);
            }
            stAux.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar las hojas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }   
    }
    
    
    //Calcula el monto del pedido a base de los importes de todas las partidas, no es el subtotal
    private void establecerSubtotalPedido(Statement st){
        
        String sql = "select importe from partida where folio_fk = "+folioId+"";
        float subtotalVar = 0;
        try 
        {
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //Aqui se empiezan a acumular los importes de las partidas
                subtotalVar = subtotalVar + Float.parseFloat(rs.getString("importe"));
            } 
            actualizarSubtotalPedido(subtotalVar, st);//Update al campo subtotal para ingresar el nuevo monto
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al buscar la partida (sub)" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Se calcula e ingresa el subtotal de pedido en la base de datos
    private void actualizarSubtotalPedido(float sub, Statement st){
        float subGrab = sub;
        float antif  = 0f;
        float descuento = 0f;
        
        //Consulta para obtener los valores de: grabados, anticipo y descuento. Se realizaran operaciones
        String sql = "select grabados, anticipo, descuento from pedido where folio = "+folioId+"";
        
        try 
        {
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
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de costos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //Cuando ya se sumaron los grabados y el subtotal ahora se guarda en la base de datos
        sql = "update pedido set subtotal = "+subGrab+" where folio = "+folioId+"";
        
        try 
        {
            st.execute(sql);
            //Ahora paso por parametros el subtotal con grabados, anticipo y descuento para calcular el total
            calcularTotalPed(subGrab, antif, descuento, st);
            st.close();
            rs.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el subtotal", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Aqui se reciben todos los valores necesarios para calcular el total y el resto y actualizar la base de datos
    private void calcularTotalPed(float sub, float anticipo, float descuento, Statement st){
        
        float total = 0f;
        float ivaf = 0.16f;
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
            st.execute(sql);
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar el total y resto", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    private void tablaPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPMouseClicked
        obtenerSeleccionTablaPedido();//se inserte al folio de la partida en un campo
    }//GEN-LAST:event_tablaPMouseClicked
    
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
        soloEnteros(evt);
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
            auto.setText("");
            dev.setText("");
            grab.setText("");
            anti.setText("");
            decu.setText("");
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
            st.close();
            rs.close();
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
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void btnModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModActionPerformed
        btnMod.setSelected(false);
        //Estos calculos se hacen ya que si se modifica el pedido, exactamente los datos de grabados, descuento y anticipo, afecta tambien al total...
        //subtotal y el resto. Se hacen de nuevo los calculos con los nuevos datos y se guardan
        float subtotal = respaldoSub + Float.parseFloat(grab.getText());
        float subiva = subtotal * 0.16f;
        float total = subtotal + subiva;
        
        float resto = total - (Float.parseFloat(anti.getText()) + Float.parseFloat(decu.getText()));
        //se guardan los datos actualizados
        String sql = "update pedido set impresion = '"+imp.getText()+"', "
                + "autorizo = '"+auto.getText()+"', "
                + "devolucion = '"+dev.getText()+"', "
                + "fIngreso = '"+fIn2.getText()+"', "
                + "fCompromiso = '"+fC2.getText()+"', "
                + "fPago = '"+fP2.getText()+"', "
                + "fTermino = '"+fT.getText()+"', "
                + "grabados = "+grab.getText()+", "
                + "anticipo = "+anti.getText()+", "
                + "descuento = "+decu.getText()+", "
                + "subtotal = "+subtotal+", "
                + "total = "+total+", "
                + "resto = "+resto+" where folio = "+folioMod+"";
        
        try {
            st = con.createStatement();
            st.execute(sql);
            calculaPyG(folioMod);
            st.close();
            
            JOptionPane.showMessageDialog(null, "Se han guardado los cambios", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        this.setEnabled(false);
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
        }
    }//GEN-LAST:event_tablaPKeyPressed

    private void nomCliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomCliKeyTyped
        limitarInsercion(40, evt, nomCli);
    }//GEN-LAST:event_nomCliKeyTyped

    private void textBusPedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBusPedKeyTyped
        limitarInsercion(40, evt, textBusPed);
    }//GEN-LAST:event_textBusPedKeyTyped

    private void impKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impKeyTyped
        int tamCampo = imp.getText().length() + 1;

        if(tamCampo > 40){
            evt.consume();
        }
    }//GEN-LAST:event_impKeyTyped

    private void autoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_autoKeyTyped
        limitarInsercion(40, evt, auto);
    }//GEN-LAST:event_autoKeyTyped

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
        //cambiaImporteSoloPzFinales();
    }//GEN-LAST:event_pzFinalesKeyTyped

    /*funcion auxiliar para resolver el porque el importe no cambia al cambiar las pz finales
    private void cambiaImporteSoloPzFinales()
    {
        pzfGlobal = Integer.parseInt(pzFinales.getText());;
        float puni = Float.parseFloat(precioUni.getText());;
        float importef = pzfGlobal * puni;
        importe.setText(String.valueOf(importef));
    }*/
    
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
        sta.setSelectedIndex(0);
        sello.setSelectedIndex(0);
    }//GEN-LAST:event_limpiarActionPerformed
    
    private void onChangeFini(){
        
        
        fIn2.addCommitListener(new CommitListener() {

            @Override
            public void onCommit(CommitEvent ce) {
                sugerenciaFecha();
            }
        });
    }
    
    
    public void sugerenciaFecha(){
        
                DateTime date = new DateTime(fIn2.getText()); 
                DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MMMM-dd");
                
                java.util.Date dtUtil = null;
                Calendar cal = Calendar.getInstance(); 
                
                date = date.plusWeeks(3);
                sug.setText("Sugerencia: "+dtf.print(date));
                
                
                dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
                try {
                    dtUtil = new SimpleDateFormat("yyyy-MM-dd").parse(dtf.print(date));
                } catch (ParseException ex) {
                    Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
                }
                cal.setTime(dtUtil);
                fC2.setSelectedDate(cal);
    }
    
    
    
    //Se llenan los campos de pedido para modificacion
    public void llenarCamposMod(int folio) {
        
        String sql = "select impresion, autorizo, devolucion, fIngreso, fCompromiso, fPago, fTermino, grabados, anticipo, descuento, "
                + "gastosFijos, subtotal"
                + " from pedido where folio = "+folio+"";
        java.util.Date date = null;
        Calendar cal = Calendar.getInstance();   
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            
            while(rs.next()){
                
                imp.setText(rs.getString("impresion"));
                auto.setText(rs.getString("autorizo"));
                dev.setText(rs.getString("devolucion"));
                 
                //Para establecer las fechas obtenidas de la base de datos
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fIngreso"));
                } catch (ParseException ex) {
                    Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
                }
                cal.setTime(date);
                fIn2.setSelectedDate(cal);
         
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fCompromiso"));
                } catch (ParseException ex) {
                    Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
                }
                cal.setTime(date);
                fC2.setSelectedDate(cal);
                
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fPago"));
                } catch (ParseException ex) {
                    Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
                }
                cal.setTime(date);
                fP2.setSelectedDate(cal);
                
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("fTermino"));
                } catch (ParseException ex) {
                    Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
                }
                cal.setTime(date);
                fT.setSelectedDate(cal);
                
                grab.setText(rs.getString("grabados"));
                anti.setText(rs.getString("anticipo"));
                decu.setText(rs.getString("descuento"));
                
                respaldoSub = Float.parseFloat(rs.getString("subtotal")) - Float.parseFloat(rs.getString("grabados"));
            }
            
            st.close();
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    private void calcularImporteKilos(){
        float pUni;
        float kgPa;
        float total;
        
        if(precioUni.getText().equals("") || precioUni.getText().equals(".")){
            pUni = 0;
        }else{
            pUni = Float.parseFloat(precioUni.getText());
        }
        
        if(kgPart.getText().equals("") || kgPart.getText().equals(".")){
            kgPa = 0; 
        }else{
            kgPa = Float.parseFloat(kgPart.getText());
        }
        total = pUni * kgPa;
        importe.setText(String.valueOf(total));
    }
    
    //Comprobar si ciertos campos estan vacios, los inicializa
    private void comprobarVacio(){
        
        if(desa.getText().equals("")){
            desa.setText("0");
        }
        //Costos
        if(grab.getText().equals("") || grab.getText().equals(".")){
            grab.setText("0.0");
        }
        if(precioUni.getText().equals("") || precioUni.getText().equals(".")){
            precioUni.setText("0.0");
        }
        if(anti.getText().equals("") || anti.getText().equals(".")){
            anti.setText("0.0");
        }
        if(decu.getText().equals("") || decu.getText().equals(".")){
            decu.setText("0.0");
        }
        if(pz.getText().equals("")){
            pz.setText("0");
        }
        if(pzFinales.getText().equals("")){
            pzFinales.setText("0");
        }
        if(importe.getText().equals("") || importe.getText().equals(".")){
            importe.setText("0.0");
        }
        if(kgPart.getText().equals("") || kgPart.getText().equals(".")){
            kgPart.setText("0.0");
        }
    }
  
    public void vacearComponentes(){
        
        modPed.setSelected(false);
        checkPz.setSelected(false);
        checkKg.setSelected(false);
        
        nomCli.setText("Buscar un Cliente");
        cliente.setText("Selecciona un Cliente");
        textBusPed.setText("Nombre de impresion");
        imp.setText("");
        auto.setText("");
        dev.setText("");
        grab.setText("0.0");
        anti.setText("0.0");
        decu.setText("0.0");
        sug.setText("Sugerencia");
        nomImpresion.setText("Impresion");
        folioVis.setText("Folio");
        pig.setText("");
        tp.setText("");
        med.setText("");
        desa.setText("0");
        sta.setSelectedIndex(0);
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
        precioUni.setText("0.0");
        pz.setText("0");
        pzFinales.setText("0");
        kgPart.setText("0.0");
        importe.setText("0.0");
         
        modelo.setRowCount(0);
        modeloMod.setRowCount(0);
        modeloP.setRowCount(0);
        
        panelMod.setVisible(false); 
        btnMod.setEnabled(false);
        btnMod.setVisible(false);
        saveP.setVisible(true);
        saveP.setEnabled(false);
        savePart.setEnabled(false);
       
        idCliente = 0;
        folioId = 0;
        folioMod = 0;
        
    }
    
    //calcula los kg finales del pedido
    private float calculaKgFinalesPedido(int folio)
    {
        //entra al pedido
        String sql = "select pagado from pedido where folio = "+folio+"";
        float sumatoria = 0f;
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String pagado = rs.getString("pagado");
                if(pagado.equals("Si"))//verifica que este pagado
                {
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
                                                            sumatoria = sumatoria + producido;//hace la sumatoria de lo producido
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
                                                        if(producido > 0)//verifica que lo producido no sea null
                                                        {
                                                            sumatoria = sumatoria + producido;//hace la sumatoria de lo producido
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
                                                        if(producido > 0)//verifica que lo producido no sea null
                                                        {
                                                            sumatoria = sumatoria + producido;//hace la sumatoria de lo producido
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
                }
                else
                    sumatoria = 0;
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return sumatoria;
    }
    
    //calcula los gastos fijos por kg del pedido
    private float calculaGfKg(int folio)
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
                sumatoriaRango = Float.parseFloat(rs.getString("kgFinalesRango"));
                if(sumatoriaRango != 0)
                    gfkg = gfr / sumatoriaRango;
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
    
    private void calculaPyG(int folio)
    {
        Statement st2;
        ResultSet rs2;
        float subtotal = 0f, costoTotal = 0f, descuento = 0f;
        String sql = "select pagado from pedido where folio = "+folio+"";
        try
        {
            Statement st3 = con.createStatement();
            ResultSet rs3 = st3.executeQuery(sql);
            while(rs3.next())
            {
                if(rs3.getString("pagado").equals("Si"))
                {
                    String sql2 = "select subtotal, costoTotal, descuento from pedido where folio = "+folio+"";
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
                    float kgFnPe = calculaKgFinalesPedido(folio);
                    float gf = calculaGfKg(folio);
                    float PyG = subtotal - costoTotal - descuento - ( kgFnPe * gf);
        
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
                else
                {
                    String sql2 = "update pedido set perdidasYGanancias = 0 where folio = "+folio+"";
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
            }
            rs3.close();
            st3.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton abreProcesos;
    private javax.swing.JTextField anti;
    private javax.swing.JTextField auto;
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
    private datechooser.beans.DateChooserCombo fC2;
    private datechooser.beans.DateChooserCombo fIn2;
    private datechooser.beans.DateChooserCombo fP2;
    private datechooser.beans.DateChooserCombo fT;
    private javax.swing.JTextField folioVis;
    private javax.swing.JTextField grab;
    private javax.swing.JTextArea imp;
    private javax.swing.JTextField importe;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTextField jTextField83;
    private javax.swing.JTextField jTextField84;
    private javax.swing.JTextField jTextField85;
    private lu.tudor.santec.jtimechooser.JTimeChooser jTimeChooser1;
    private javax.swing.JTextField kgPart;
    private javax.swing.JButton limpiar;
    private javax.swing.JComboBox mat1;
    private javax.swing.JComboBox mat2;
    private javax.swing.JTextField med;
    private javax.swing.JCheckBox modPed;
    private javax.swing.JTextField nomCli;
    private javax.swing.JTextField nomImpresion;
    private javax.swing.JTabbedPane paPed;
    private javax.swing.JPanel panPart;
    private javax.swing.JPanel panelCliente;
    private javax.swing.JPanel panelMod;
    private javax.swing.JTextField pig;
    private javax.swing.JTextField precioUni;
    private javax.swing.JTextField pz;
    private javax.swing.JTextField pzFinales;
    private javax.swing.JButton regP;
    private javax.swing.JButton saveP;
    private javax.swing.JToggleButton savePart;
    private javax.swing.JComboBox sello;
    private javax.swing.JComboBox sta;
    private javax.swing.JLabel sug;
    private javax.swing.JTable tablaC;
    private javax.swing.JTable tablaModPed;
    private javax.swing.JTable tablaP;
    private javax.swing.JTextField textBusPed;
    private javax.swing.JTextField tp;
    // End of variables declaration//GEN-END:variables
}
