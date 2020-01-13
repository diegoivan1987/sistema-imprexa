
package pantallas;

import com.sun.glass.events.KeyEvent;
import detalles.DatosPartida;
import detalles.DatosPedido;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import report.DatosDataSource;
import report.PedidoATT;
import report.PedidoDatos;
import utilidades.MiRender;
import utilidades.ScreenSize;

public class Visualizacion extends javax.swing.JFrame {
    
    String[] modoMaterial = {"Produccion", "Compra", "Ambos"};
    
    Connection con;
    Statement st;
    ResultSet rs;
    
    String nomCl;
    
    Principal prin;
    
    DefaultTableModel modeloPedido;
    DefaultTableModel modeloPart;
    DefaultTableModel modeloCli;
    DefaultTableModel modeloRegE;
    DefaultTableModel modeloRegI;
    DefaultTableModel modeloRegB;
    DefaultTableModel modeloProd;
    DefaultTableModel modeloCompra;
    
    JTableHeader thPart;
    JTableHeader thPed;
    JTableHeader thCli;
    JTableHeader thRE;
    JTableHeader thRI;
    JTableHeader thRB;
    JTableHeader thProd;
    JTableHeader thCompra;
    
    Font fuenteTablas;
    
    int folioVar = 0; 
    int idCliente = 0;
    int idParVar = 0;
    int idEx = 0;
    int idIm = 0;
    int idBo = 0;
    
    String impresion = "";
    
    ArrayList<PedidoATT> pedidosGlobal = new ArrayList<PedidoATT>();
    
    ScreenSize tam;
    
    MiRender miRender;
    
    //Para la generacion del reporte
    DatosDataSource datosDataSource;
    PedidoDatos datosPedido;
    
    //Para las pantallas de mostrar detalles, de pedido y partidda
    DatosPedido dp = new DatosPedido();
    ArrayList<DatosPartida> datosPartidas = new ArrayList<DatosPartida>();
            
    public Visualizacion(Connection con) {
        initComponents();
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.con = con;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
        miRender = new MiRender();
        tam = new ScreenSize();
        
        fuenteTablas = new Font("Dialog", Font.BOLD, 12);
        setDTModel();
        btnReport.setEnabled(false);
        btnDetPed.setEnabled(false);
        btnDetPart.setEnabled(false);
        
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        tabedPart = new javax.swing.JTabbedPane();
        panelPed = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedido = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaCli = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPart = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCompra = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaProd = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        panelPro = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaRegI = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaRegB = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaRegE = new javax.swing.JTable();
        buscar = new javax.swing.JButton();
        impScan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnReport = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        bar = new javax.swing.JProgressBar();
        btnDetPed = new javax.swing.JButton();
        btnDetPart = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visualización");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane9.setEnabled(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabedPart.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaPedido.setBackground(new java.awt.Color(204, 255, 255));
        tablaPedido.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPedidoMouseClicked(evt);
            }
        });
        tablaPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaPedidoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaPedidoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPedido);

        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel1.setText("Pedidos");

        tablaCli.setBackground(new java.awt.Color(153, 255, 255));
        tablaCli.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tablaCli);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel2.setText("Partidas");

        tablaPart.setBackground(new java.awt.Color(204, 255, 255));
        tablaPart.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Procesos");

        tablaCompra.setBackground(new java.awt.Color(204, 255, 255));
        tablaCompra.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tablaCompra);

        tablaProd.setBackground(new java.awt.Color(204, 255, 255));
        tablaProd.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tablaProd);

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("PRODUCCION");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("COMPRA");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPedLayout = new javax.swing.GroupLayout(panelPed);
        panelPed.setLayout(panelPedLayout);
        panelPedLayout.setHorizontalGroup(
            panelPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelPedLayout.setVerticalGroup(
            panelPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabedPart.addTab("Pedidos", panelPed);

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel5.setText("Registros de Producción de Impresión.");

        tablaRegI.setBackground(new java.awt.Color(204, 255, 255));
        tablaRegI.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaRegI.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(tablaRegI);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel6.setText("Registros de Producción de Bolseo.");

        tablaRegB.setBackground(new java.awt.Color(204, 255, 255));
        tablaRegB.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaRegB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane8.setViewportView(tablaRegB);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel4.setText("Registros de Producción de Extrusión.");

        tablaRegE.setBackground(new java.awt.Color(204, 255, 255));
        tablaRegE.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tablaRegE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(tablaRegE);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelProLayout = new javax.swing.GroupLayout(panelPro);
        panelPro.setLayout(panelProLayout);
        panelProLayout.setHorizontalGroup(
            panelProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelProLayout.setVerticalGroup(
            panelProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        tabedPart.addTab("Registros de producción", panelPro);

        buscar.setBackground(new java.awt.Color(51, 51, 51));
        buscar.setForeground(new java.awt.Color(255, 255, 255));
        buscar.setText("Buscar pedido");
        buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarActionPerformed(evt);
            }
        });

        impScan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        impScan.setText("Nombre Impresión");
        impScan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                impScanMousePressed(evt);
            }
        });
        impScan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                impScanKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                impScanKeyTyped(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("$  =  Costo de...");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("% = Porcentaje de...");

        btnReport.setBackground(new java.awt.Color(51, 51, 51));
        btnReport.setFont(new java.awt.Font("Dialog", 1, 9)); // NOI18N
        btnReport.setForeground(new java.awt.Color(255, 255, 255));
        btnReport.setText("Reporte Operador");
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Cerrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        bar.setForeground(new java.awt.Color(0, 204, 204));

        btnDetPed.setBackground(new java.awt.Color(51, 51, 51));
        btnDetPed.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btnDetPed.setForeground(new java.awt.Color(255, 255, 255));
        btnDetPed.setText("Detalles Pedido");
        btnDetPed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetPedActionPerformed(evt);
            }
        });

        btnDetPart.setBackground(new java.awt.Color(51, 51, 51));
        btnDetPart.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btnDetPart.setForeground(new java.awt.Color(255, 255, 255));
        btnDetPart.setText("Detalles Partidas");
        btnDetPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetPartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(impScan, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnDetPed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDetPart))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabedPart)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabedPart)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(buscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(impScan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addGap(378, 378, 378)
                        .addComponent(bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetPart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetPed, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );

        jScrollPane9.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1451, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setDTModel(){
        //Pedidos
        modeloPedido = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }    
        };
        modeloPedido.setColumnIdentifiers(new Object[]{"Folio", "Impresión", "Autorizó", "FIngreso", "Estatus","FCompromiso", "FPago", "FTermino", "Devolución", "Grabados", "$Diseño","Anticipo", 
            "Descuento", "Subtotal",  "Total", "Resto", "KgDesperdicio", "%Desperdicio", "$Total", "$Fijos", "Perdidas/Ganancias"});
        tablaPedido.setModel(modeloPedido);
        thPed = tablaPedido.getTableHeader();
        thPed.setFont(fuenteTablas);
        thPed.setBackground(Color.BLUE);
        thPed.setForeground(Color.white);
          
        //Partidas
        modeloPart = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloPart.setColumnIdentifiers(new Object[]{"Id", "Hoja", "De", "Medida", "Tipo", "Sello", "Pigmento", "Desarrollo", "Mat1", "Cal1", 
            "Mat2", "Cal2", "PUnitario", "Piezas", "PzFinales", "KG", "Importe", "KgDesperdicio", "%Desperdicio", "$Material", "$Partida"});
        tablaPart.setModel(modeloPart);
        thPart = tablaPart.getTableHeader();
        thPart.setFont(fuenteTablas);
        thPart.setBackground(Color.BLUE);
        thPart.setForeground(Color.white);
        
        
        //Clientes
        modeloCli = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloCli.setColumnIdentifiers(new Object[]{"Cliente", "Telefono", "Celular", "Correo", "Domicilio", "DomEntrega", "Razon Social", "RFC", "Colonia", 
        "Ciudad", "CP", "UsoCFDI", "MetodoPago", "Contacto", "Agente"});
        tablaCli.setModel(modeloCli);
        thCli = tablaCli.getTableHeader();
        thCli.setFont(fuenteTablas);
        thCli.setBackground(Color.BLUE);
        thCli.setForeground(Color.white);
        
        //Registros de producion
        modeloRegE = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloRegE.setColumnIdentifiers(new Object[]{"Operador", "No.Maquina", "HrIni", "FechaIni", "HrFin", "FechaFin", "TotalHr", "TiempoMuerto", "HrExtras", 
            "Kg",  "Greña", "$Operacion"});
        tablaRegE.setModel(modeloRegE);
        thRE = tablaRegE.getTableHeader();
        thRE.setFont(fuenteTablas);
        thRE.setBackground(Color.BLUE);
        thRE.setForeground(Color.white);
        
        
        modeloRegI = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloRegI.setColumnIdentifiers(new Object[]{"Operador","Ayudante", "No.Maquina", "HrIni", "FechaIni", "HrFin", "FechaFin", "TotalHr", "TiempoMuerto", "HrExtras", 
            "Kg",  "Greña", "$Operacion"});
        tablaRegI.setModel(modeloRegI);
        thRI = tablaRegI.getTableHeader();
        thRI.setFont(fuenteTablas);
        thRI.setBackground(Color.BLUE);
        thRI.setForeground(Color.white);
        
        
        modeloRegB = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloRegB.setColumnIdentifiers(new Object[]{"Operador", "No.Maquina", "HrIni", "FechaIni", "HrFin", "FechaFin", "TotalHr", "TiempoMuerto", "HrExtras", 
            "Kg",  "Greña", "Suaje", "$Operacion"});
        tablaRegB.setModel(modeloRegB);
        thRB = tablaRegB.getTableHeader();
        thRB.setFont(fuenteTablas);
        thRB.setBackground(Color.BLUE);
        thRB.setForeground(Color.white);
        
        
        //Tabla Procesos: Compra
        modeloCompra = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloCompra.setColumnIdentifiers(new Object[]{"Proceso", "Provedor(1)", "Precio*Kg(1)", "CompraMat(1)", "Provedor(2)", 
            "Precio*Kg(2)", "CompraMat(2)"});
        tablaCompra.setModel(modeloCompra);
        thCompra = tablaCompra.getTableHeader();
        thCompra.setFont(fuenteTablas);
        thCompra.setBackground(Color.BLUE);
        thCompra.setForeground(Color.white);
        
        
        //Tabla Procesos: Produccion
        modeloProd = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloProd.setColumnIdentifiers(new Object[]{"Proceso", "KgTotales","HrTotales","$Operacion","GreñaTotal","$Unitario","Sticky","ProdPZS"});
        tablaProd.setModel(modeloProd);
        thProd = tablaProd.getTableHeader();
        thProd.setFont(fuenteTablas);
        thProd.setBackground(Color.BLUE);
        thProd.setForeground(Color.white);
        
        
        tablaPedido.setDefaultRenderer(Object.class, miRender);
        tablaPart.setDefaultRenderer(Object.class, miRender);
        tablaProd.setDefaultRenderer(Object.class, miRender);
        tablaCompra.setDefaultRenderer(Object.class, miRender);
        tablaRegB.setDefaultRenderer(Object.class, miRender);
        tablaRegE.setDefaultRenderer(Object.class, miRender);
        tablaRegI.setDefaultRenderer(Object.class, miRender);
        //modeloPedido.get
    }
    
    //Rellenar la tabla de pedidos
    private void buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarActionPerformed
              
      //this.setSize(tam.tamano);
      //this.setLocationRelativeTo(null);
        buscar.setSelected(false);
      llenarTablaPedido();
    }//GEN-LAST:event_buscarActionPerformed

    public void llenarTablaPedido(){
        vacearTablas();
        resetBar();
        btnReport.setEnabled(false);
        btnDetPed.setEnabled(false);
        btnDetPart.setEnabled(false);
        String sql = "select * from pedido where impresion like '%"+impScan.getText()+"%' order by fIngreso desc limit 0,30";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            setBar(35);
            String fechaIngreso;
            String fechaIngresoSub;
            String fechaCompromiso;
            String fechaCompromisoSub;
            String fechaPago;
            String fechaPagoSub;
            String fechaTermino;
            String fechaTerminoSub;
            
            while(rs.next())
            {
                fechaIngreso = rs.getString("fIngreso");
                fechaIngresoSub  = fechaIngreso.substring(8, 10)+"/"+fechaIngreso.substring(5, 7)+"/"+fechaIngreso.substring(0, 4);
                fechaCompromiso = rs.getString("fCompromiso");
                fechaCompromisoSub  = fechaCompromiso.substring(8, 10)+"/"+fechaCompromiso.substring(5, 7)+"/"+fechaCompromiso.substring(0, 4);
                fechaPago = rs.getString("fPago");
                fechaPagoSub  = fechaPago.substring(8, 10)+"/"+fechaPago.substring(5, 7)+"/"+fechaPago.substring(0, 4);
                fechaTermino = rs.getString("fTermino");
                fechaTerminoSub  = fechaTermino.substring(8, 10)+"/"+fechaTermino.substring(5, 7)+"/"+fechaTermino.substring(0, 4);
                
                modeloPedido.addRow(new Object[]{rs.getString("folio")+"A", rs.getString("impresion"), rs.getString("autorizo"), fechaIngresoSub, rs.getString("estatus"),fechaCompromisoSub, 
                fechaPagoSub, fechaTerminoSub, rs.getString("devolucion"), rs.getString("grabados"), rs.getString("costoDisenio"), rs.getString("anticipo"), rs.getString("descuento"), 
                rs.getString("subtotal"), rs.getString("total"), rs.getString("resto"), rs.getString("kgDesperdicioPe"), rs.getString("porcentajeDespPe"),
                rs.getString("costoTotal"), rs.getString("gastosFijos"), rs.getString("perdidasYGanancias")});
                
                idCliente = Integer.parseInt(rs.getString("idC_fk"));
            }
            setBar(35);
            tablaPedido.setModel(modeloPedido);
            
            rs.close();
            st.close();
            setBar(35);
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al llenar la tabla de pedidos" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    public void vacearTablas(){
        modeloPedido.setRowCount(0);
        modeloPart.setRowCount(0);
        //modeloPro.setRowCount(0);
        modeloCli.setRowCount(0);
        modeloRegE.setRowCount(0);
        modeloRegI.setRowCount(0);
        modeloRegB.setRowCount(0);
        modeloProd.setRowCount(0);
        modeloCompra.setRowCount(0);
        
    }
    
    //no quitar el rs parametro
    private void llenarDatosPedido(ResultSet rs) throws SQLException{
        Statement st8;
        ResultSet rs8;
        String sql = "select sticky from tintas where folio_fk = "+rs.getString("folio")+"";
        try
        {
            st8 = con.createStatement();
            rs8 = st8.executeQuery(sql);
            while(rs8.next())
            {
                dp.setSticky(rs8.getString("sticky"));
            }
            rs8.close();
            st8.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar sticky");
        }
        dp.setFolio(rs.getString("folio")+"A");
        dp.setImp(rs.getString("impresion"));
        dp.setAuto(rs.getString("autorizo"));
        dp.setDev(rs.getString("devolucion"));
        dp.setFin(rs.getString("fIngreso"));
        dp.setEstatus(rs.getString("estatus"));
        dp.setFcom(rs.getString("fCompromiso"));
        dp.setFpag(rs.getString("fPago"));
        dp.setFterm(rs.getString("fTermino"));
        dp.setGrab(rs.getString("grabados"));
        dp.setDise(rs.getString("costoDisenio"));
        dp.setAnti(rs.getString("anticipo"));
        dp.setDesc(rs.getString("descuento"));
        dp.setSub(rs.getString("subtotal"));
        dp.setTot(rs.getString("total"));
        dp.setRes(rs.getString("resto"));
        dp.setKdes(rs.getString("kgDesperdicioPe"));
        dp.setPdes(rs.getString("porcentajeDespPe"));
        dp.setGf(rs.getString("gastosFijos"));
        dp.setCtot(rs.getString("costoTotal"));
        dp.setPyg(rs.getString("perdidasYGanancias"));
    }
    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jButton2.setSelected(false);
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
        this.dispose();
        
        /*prin =  new Principal(con);
        prin.setLocationRelativeTo(null);
        prin.setVisible(true);*/
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    //Tabla partidas es clickeada
    private void tablaPartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPartMouseClicked
        
        obtenerSeleccionTablaPartida();
    }//GEN-LAST:event_tablaPartMouseClicked

    private void obtenerSeleccionTablaPartida(){
        String modo = "";
        resetBar();
        
        try{
            
            idParVar = Integer.parseInt(modeloPart.getValueAt(tablaPart.getSelectedRow(), 0).toString());
            
            modo = leerModoGeneracionPedido();//Obtener modo
            setBar(50);
            mostrarModoGeneracionPedido(modo);//Mostrar datos segun el modo
            setBar(50);
            
            
        }catch(ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    //Se consulta el modo de generacion del pedido
    public String leerModoGeneracionPedido(){
        
        String sql = "select modoMat from partida where idPar = "+idParVar+"";
        String modo = "";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                modo = rs.getString("modoMat");
                if(modo == null){
                    modo = "";
                }
            }
            rs.close();
            st.close();
            return modo;
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            return "";
        }
    }
    
    //Segun el modo se mustran los campos de alguna o las dos tablas
    public void mostrarModoGeneracionPedido(String modo){
        
        if(modo.equals(modoMaterial[0])){//Produccion
            tablaCompra.setVisible(false);
            llenarProduccion();
            llenarTablasRegistros();
            
        }else if(modo.equals(modoMaterial[1])){//Compra
            tablaProd.setVisible(false);
            llenarCompra();
            llenarTablasRegistros();
        }else if(modo.equals(modoMaterial[2])){//Ambos
            llenarProduccion();
            llenarCompra();
            llenarTablasRegistros();
        }else{
            tablaCompra.setVisible(false);
            tablaProd.setVisible(false);
        }
    }
    
    public void llenarProduccion(){
        modeloProd.setRowCount(0);
        String sqlExt = "select idExt, kgTotales, hrTotalesPar, costoOpTotalExt, greniaExt, costoUnitarioExt from extrusion where idPar_fk = "+idParVar+"";
        String sqlImp = "select idImp, kgTotales, hrTotalesPar, costoOpTotalImp, greniaImp, costoUnitarioImp from impreso where idPar_fk = "+idParVar+"";
        String sqlBol = "select idBol, kgTotales, hrTotalesPar,costoOpTotalBol, greniaBol, costoUnitarioBol, produccionPz from bolseo where idPar_fk = "+idParVar+"";
        String sqlSticky = "select sticky from tintas where folio_fk = "+folioVar+"";
        String stickyAux = "";
        
        tablaProd.setVisible(true);
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sqlExt);
            while(rs.next())
            {
                modeloProd.addRow(new Object[]{"EXTRUSION", rs.getString("kgTotales"), rs.getString("hrTotalesPar"),rs.getString("costoOpTotalExt"), rs.getString("greniaExt"), 
                rs.getString("costoUnitarioExt"), "Inexistente","Inexistente"});
                
                idEx = rs.getInt("idExt");
            }
            rs.close();
            st.close();
        } 
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sqlSticky);
            while(rs.next())
            {
                stickyAux = rs.getString("sticky");
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sqlImp);
            while(rs.next())
            {
                modeloProd.addRow(new Object[]{"IMPPRESO", rs.getString("kgTotales"), rs.getString("hrTotalesPar"),rs.getString("costoOpTotalImp"), rs.getString("greniaImp"), 
                rs.getString("costoUnitarioImp"), stickyAux,"Inexistente"});
                
                idIm = rs.getInt("idImp");
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sqlBol);
            while(rs.next())
            {
                modeloProd.addRow(new Object[]{"BOLSEO", rs.getString("kgTotales"), rs.getString("hrTotalesPar"),rs.getString("costoOpTotalBol"), rs.getString("greniaBol"), 
                rs.getString("costoUnitarioBol"), "Inexistente",rs.getString("produccionPz")});
                
                idBo = rs.getInt("idBol");
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void llenarCompra(){
        modeloCompra.setRowCount(0);
        String sqlExt = "select idExt, pocM1, pocM2, prov1, precioKg1, prov2, precioKg2 from extrusion where idPar_fk = "+idParVar+"";
        String sqlImp = "select idImp, produccion, prov1, precioKg1, produccion2, prov2, precioKg2 from impreso where idPar_fk = "+idParVar+"";
        String sqlBol = "select idBol, produccion, prov1, precioKg1 from bolseo where idPar_fk = "+idParVar+"";
        
        tablaCompra.setVisible(true);

        try {
            st = con.createStatement();
            rs = st.executeQuery(sqlExt);
            
            while(rs.next()){
                modeloCompra.addRow(new Object[]{"EXTRUSION", rs.getString("prov1"), rs.getString("precioKg1"),rs.getString("pocM1"),
                 rs.getString("prov2"), rs.getString("precioKg2"), rs.getString("pocM2")});
                
                idEx = rs.getInt("idExt");
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sqlImp);
            while(rs.next())
            {
                modeloCompra.addRow(new Object[]{"IMPRESO", rs.getString("prov1"), rs.getString("precioKg1"),rs.getString("produccion"),
                 rs.getString("prov2"), rs.getString("precioKg2"),rs.getString("produccion2")});
                
                idIm = rs.getInt("idImp");
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sqlBol);
            while(rs.next())
            {
                modeloCompra.addRow(new Object[]{"BOLSEO", rs.getString("prov1"), rs.getString("precioKg1"),rs.getString("produccion"),
                 "Inexistente", "Inexistente", "Inexistente"});
                
                idBo = rs.getInt("idBol");
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    
    //Tabla pedido es clickeada
    private void tablaPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedidoMouseClicked

        obtenerSeleccionTablaPedido();
        
    }//GEN-LAST:event_tablaPedidoMouseClicked

    
    private void obtenerSeleccionTablaPedido(){
        try{
            folioVar = Integer.parseInt(modeloPedido.getValueAt(tablaPedido.getSelectedRow(), 0).toString().replace("A", ""));
            impresion = modeloPedido.getValueAt(tablaPedido.getSelectedRow(), 1).toString();
            btnReport.setEnabled(true);
            btnDetPed.setEnabled(true);
            btnDetPart.setEnabled(true);
            datosPartidas = new ArrayList<>();
        
        llenarTablaPartidas();
        }catch(ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    //Boton para generar reporte
    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        String fechaSub = "";//servira para reacomodar el formato de la fecha
        btnReport.setSelected(false);
        int respuesta = 0;
        File archivoElegido = null;
        String directorio = "";
        resetBar();
        
        JFileChooser fc = new JFileChooser();
        fc.setApproveButtonText("Guardar");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        respuesta = fc.showOpenDialog(this);
        
        if(respuesta == JFileChooser.APPROVE_OPTION){
            archivoElegido = fc.getSelectedFile();
            directorio = archivoElegido.getPath();
            
            try {
            datosDataSource = new DatosDataSource();
            
            setBar(20);
            
            //Se repite dependiendo de el numero de partidas de el pedido
            for(int i = 0; i < pedidosGlobal.size(); i++){
                //Esto se utilizaba para llenar el reporte de operador cuando el subtotal se calculaba
                //con piezas iniciales o finales, basicamente si elegia las finales solo Mostraba una F 
                //mas las piezas finales donde ahora se muestran kg
                /*String manera = pedidosGlobal.get(i).getManera();
                if(manera.equals("pzF"))
                {
                    datosPedido = new PedidoDatos(pedidosGlobal.get(i).getFolio(), pedidosGlobal.get(i).getFecha(), pedidosGlobal.get(i).getImpresion(), 
                    pedidosGlobal.get(i).getAutorizo(), pedidosGlobal.get(i).getGrabados(), pedidosGlobal.get(i).getSub(), pedidosGlobal.get(i).getIva(), 
                    pedidosGlobal.get(i).getTotal(), pedidosGlobal.get(i).getAnticipo(), pedidosGlobal.get(i).getDirEnt(), pedidosGlobal.get(i).getTel(),
                    pedidosGlobal.get(i).getCel(), pedidosGlobal.get(i).getCiudad(), pedidosGlobal.get(i).getAgente(), "F"+pedidosGlobal.get(i).getPzFinales(),
                    pedidosGlobal.get(i).getMaterial(), pedidosGlobal.get(i).getMedida(), pedidosGlobal.get(i).getCalibre(), pedidosGlobal.get(i).getSello(), 
                    pedidosGlobal.get(i).getTipo(), pedidosGlobal.get(i).getPigmento(), pedidosGlobal.get(i).getpUni(), pedidosGlobal.get(i).getImporte(), 
                    pedidosGlobal.get(i).getC1t1(), pedidosGlobal.get(i).getC1t2(),  pedidosGlobal.get(i).getC1t3(), pedidosGlobal.get(i).getC1t4(), 
                    pedidosGlobal.get(i).getC1t5(), pedidosGlobal.get(i).getC1t6(), pedidosGlobal.get(i).getC2t1(), pedidosGlobal.get(i).getC2t2(), 
                    pedidosGlobal.get(i).getC2t3(), pedidosGlobal.get(i).getC2t4(), pedidosGlobal.get(i).getC2t5(), pedidosGlobal.get(i).getC2t6(), 
                    pedidosGlobal.get(i).getDescuento(), pedidosGlobal.get(i).getResto(), pedidosGlobal.get(i).getManera(), pedidosGlobal.get(i).getPzFinales());
                }else
                {
                    datosPedido = new PedidoDatos(pedidosGlobal.get(i).getFolio(), pedidosGlobal.get(i).getFecha(), pedidosGlobal.get(i).getImpresion(), 
                    pedidosGlobal.get(i).getAutorizo(), pedidosGlobal.get(i).getGrabados(), pedidosGlobal.get(i).getSub(), pedidosGlobal.get(i).getIva(), 
                    pedidosGlobal.get(i).getTotal(), pedidosGlobal.get(i).getAnticipo(), pedidosGlobal.get(i).getDirEnt(), pedidosGlobal.get(i).getTel(),
                    pedidosGlobal.get(i).getCel(), pedidosGlobal.get(i).getCiudad(), pedidosGlobal.get(i).getAgente(), pedidosGlobal.get(i).getPiezas(),
                    pedidosGlobal.get(i).getMaterial(), pedidosGlobal.get(i).getMedida(), pedidosGlobal.get(i).getCalibre(), pedidosGlobal.get(i).getSello(), 
                    pedidosGlobal.get(i).getTipo(), pedidosGlobal.get(i).getPigmento(), pedidosGlobal.get(i).getpUni(), pedidosGlobal.get(i).getImporte(), 
                    pedidosGlobal.get(i).getC1t1(), pedidosGlobal.get(i).getC1t2(),  pedidosGlobal.get(i).getC1t3(), pedidosGlobal.get(i).getC1t4(), 
                    pedidosGlobal.get(i).getC1t5(), pedidosGlobal.get(i).getC1t6(), pedidosGlobal.get(i).getC2t1(), pedidosGlobal.get(i).getC2t2(), 
                    pedidosGlobal.get(i).getC2t3(), pedidosGlobal.get(i).getC2t4(), pedidosGlobal.get(i).getC2t5(), pedidosGlobal.get(i).getC2t6(), 
                    pedidosGlobal.get(i).getDescuento(), pedidosGlobal.get(i).getResto(), pedidosGlobal.get(i).getManera(), pedidosGlobal.get(i).getPzFinales());
                }*/
                fechaSub = pedidosGlobal.get(i).getFecha().substring(8, 10)+"/"+pedidosGlobal.get(i).getFecha().substring(5, 7)+"/"+pedidosGlobal.get(i).getFecha().substring(0, 4);//cambiamos el formato de la fecha
                datosPedido = new PedidoDatos(pedidosGlobal.get(i).getFolio(), fechaSub, pedidosGlobal.get(i).getImpresion(), 
                pedidosGlobal.get(i).getAutorizo(), pedidosGlobal.get(i).getGrabados(), pedidosGlobal.get(i).getSub(), pedidosGlobal.get(i).getIva(), 
                pedidosGlobal.get(i).getTotal(), pedidosGlobal.get(i).getAnticipo(), pedidosGlobal.get(i).getDirEnt(), pedidosGlobal.get(i).getTel(),
                pedidosGlobal.get(i).getCel(), pedidosGlobal.get(i).getCiudad(), pedidosGlobal.get(i).getAgente(), pedidosGlobal.get(i).getKg(),
                pedidosGlobal.get(i).getMaterial(), pedidosGlobal.get(i).getMedida(), pedidosGlobal.get(i).getCalibre(), pedidosGlobal.get(i).getSello(), 
                pedidosGlobal.get(i).getTipo(), pedidosGlobal.get(i).getPigmento(), pedidosGlobal.get(i).getpUni(), pedidosGlobal.get(i).getImporte(), 
                pedidosGlobal.get(i).getC1t1(), pedidosGlobal.get(i).getC1t2(),  pedidosGlobal.get(i).getC1t3(), pedidosGlobal.get(i).getC1t4(), 
                pedidosGlobal.get(i).getC1t5(), pedidosGlobal.get(i).getC1t6(), pedidosGlobal.get(i).getC2t1(), pedidosGlobal.get(i).getC2t2(), 
                pedidosGlobal.get(i).getC2t3(), pedidosGlobal.get(i).getC2t4(), pedidosGlobal.get(i).getC2t5(), pedidosGlobal.get(i).getC2t6(), 
                pedidosGlobal.get(i).getDescuento(), pedidosGlobal.get(i).getResto(), pedidosGlobal.get(i).getManera(), pedidosGlobal.get(i).getPzFinales());
                    
                datosDataSource.addParticipante(datosPedido);
                setBar(50 / pedidosGlobal.size());
            }
            
            
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Reportes\\PedidoCabeceraTabla.jasper");
            
            reporte.getResourceBundle();
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, datosDataSource);
            
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File(directorio + "\\" 
                    + "F" + (pedidosGlobal.get(pedidosGlobal.size()-1).getFolio()
                    + "_" + pedidosGlobal.get(pedidosGlobal.size()-1).getFecha()) + ".pdf"));
            exporter.exportReport();
            
            setBar(100 - bar.getValue());
            JOptionPane.showMessageDialog(null, "Reporte Generado en: " + directorio, "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            resetBar();
            } catch (JRException ex) {
                resetBar();
                JOptionPane.showMessageDialog(null, "No se pudo generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            
        } 
        
    }//GEN-LAST:event_btnReportActionPerformed

    private void impScanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_impScanMousePressed
        impScan.setText("");
    }//GEN-LAST:event_impScanMousePressed

    private void impScanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impScanKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            llenarTablaPedido();
            
            if(!(tablaPedido.getRowCount() == 0)){
                tablaPedido.requestFocus();
                tablaPedido.changeSelection(0, 0, false, false);
            }
        }
    }//GEN-LAST:event_impScanKeyPressed

    private void tablaPedidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPedidoKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_SPACE){
            obtenerSeleccionTablaPedido();
            
            if(!(tablaPart.getRowCount() == 0)){
                tablaPart.requestFocus();
                tablaPart.changeSelection(0, 0, false, false);
            }
        }
    }//GEN-LAST:event_tablaPedidoKeyPressed

    private void tablaPartKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPartKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_SPACE){
            obtenerSeleccionTablaPartida();
        }
    }//GEN-LAST:event_tablaPartKeyPressed

    private void impScanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impScanKeyTyped
        limitarInsercion(40, evt, impScan);
    }//GEN-LAST:event_impScanKeyTyped

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void btnDetPedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetPedActionPerformed
        btnDetPed.setSelected(false);

        DetallesPedido dt = new DetallesPedido(dp, con, pedidosGlobal, Inicio.vis);
        dt.setLocationRelativeTo(null);
        dt.setVisible(true);
        
        this.setEnabled(false);
    }//GEN-LAST:event_btnDetPedActionPerformed

    private void btnDetPartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetPartActionPerformed
        btnDetPart.setSelected(false);
        
        if(modeloPart.getRowCount() == 0){
            JOptionPane.showMessageDialog(null, "No aparecen partidas", "Avertencia", JOptionPane.WARNING_MESSAGE);
        }else{
            DetallesPartida dPart = new DetallesPartida(datosPartidas, con, pedidosGlobal, Inicio.vis);
            dPart.setLocationRelativeTo(null);
            dPart.setVisible(true); 
            this.setEnabled(false);
        }
    }//GEN-LAST:event_btnDetPartActionPerformed

    private void tablaPedidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPedidoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaPedidoKeyTyped

    
    //Relleado de tabla y arreglo de partidas, importante, tiene una funcion
    //dentro del while, no quitarle el rs como parametro, solo pasa la conexion
    //no la crea ni la cierra
    public void llenarTablaPartidas(){
        //modeloPro.setRowCount(0);
        modeloPart.setRowCount(0);
        modeloRegE.setRowCount(0);
        modeloRegI.setRowCount(0);
        modeloRegB.setRowCount(0);
        
        modeloCompra.setRowCount(0);
        modeloProd.setRowCount(0);
        
        resetBar();
        int contadorPartidas = 0;
        ArrayList<PedidoATT> pedidos = new ArrayList<PedidoATT>();
        //Arreglo de pedidos para crear reportes
  
        String sql = "select * from partida where folio_fk = "+folioVar+"";
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            setBar(40);
            while(rs.next())
            {
                
                modeloPart.addRow(new Object[]{rs.getString("idPar"), 
                rs.getString("hoja"), rs.getString("de"), rs.getString("medida"), 
                rs.getString("tipo"), rs.getString("sello"), rs.getString("pigmento"), 
                rs.getString("desarrollo"), rs.getString("mat1"), 
                rs.getString("calibre1"), rs.getString("mat2"), 
                rs.getString("calibre2"),rs.getString("precioUnitaro"), 
                rs.getString("piezas"), rs.getString("pzFinales"),
                rs.getString("kgPartida"), rs.getString("importe"), 
                rs.getString("kgDesperdicio"), rs.getString("porcentajeDesp"), 
                rs.getString("costoMaterialTotal"), rs.getString("costoPartida")});

                pedidos.add(new PedidoATT());//Se crean pedidos respeto al numero de partidas encontrdas
                //Cuando se crea un pedido se guardan los datos de la partida correspondiente
                pedidos.get(contadorPartidas).setKg(rs.getString("kgPartida"));
                pedidos.get(contadorPartidas).setPiezas(rs.getString("piezas"));
                pedidos.get(contadorPartidas).setMaterial(rs.getString("mat1") + "-" + rs.getString("mat2"));
                pedidos.get(contadorPartidas).setMedida(rs.getString("medida"));
                pedidos.get(contadorPartidas).setCalibre(rs.getString("calibre1") + "-" + rs.getString("calibre2"));
                pedidos.get(contadorPartidas).setSello(rs.getString("sello"));
                pedidos.get(contadorPartidas).setTipo(rs.getString("tipo"));
                pedidos.get(contadorPartidas).setPigmento(rs.getString("pigmento"));
                pedidos.get(contadorPartidas).setpUni(rs.getString("precioUnitaro"));
                pedidos.get(contadorPartidas).setImporte(rs.getString("importe"));
                
                pedidos.get(contadorPartidas).setC1t1(rs.getString("c1t1"));
                pedidos.get(contadorPartidas).setC1t2(rs.getString("c1t2"));
                pedidos.get(contadorPartidas).setC1t3(rs.getString("c1t3"));
                pedidos.get(contadorPartidas).setC1t4(rs.getString("c1t4"));
                pedidos.get(contadorPartidas).setC1t5(rs.getString("c1t5"));
                pedidos.get(contadorPartidas).setC1t6(rs.getString("c1t6"));
                pedidos.get(contadorPartidas).setC2t1(rs.getString("c2t1"));
                pedidos.get(contadorPartidas).setC2t2(rs.getString("c2t2"));
                pedidos.get(contadorPartidas).setC2t3(rs.getString("c2t3"));
                pedidos.get(contadorPartidas).setC2t4(rs.getString("c2t4"));
                pedidos.get(contadorPartidas).setC2t5(rs.getString("c2t5"));
                pedidos.get(contadorPartidas).setC2t6(rs.getString("c2t6"));
                
                pedidos.get(contadorPartidas).setManera(rs.getString("manera"));
                pedidos.get(contadorPartidas).setPzFinales(rs.getString("pzFinales"));
                
                establecerDatosPartidasDetalles(contadorPartidas, rs);
                
                contadorPartidas++;//Para recorrer el arreglo
            }
            tablaPart.setModel(modeloPart);
            rs.close();
            st.close();
            
            if(pedidos.size() == 0){
                btnReport.setEnabled(false);
            }else{
                btnReport.setEnabled(true);
            }
            setBar(40);
            
            setIdCliente();//Se obtiene la id del cliente a base de la foranea de pedido para previo uso
            pedidos = datosPedidoRep(pedidos.size(), pedidos);//Se manda el arreglo y lo devuelve actualizado con los datos de pedido
            pedidos = llenarTablaCliente(pedidos);//Se manda el arreglo y lo devuelve actualizado con los datos de cliente
            setBar(20);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    private void establecerDatosPartidasDetalles(int contador, ResultSet rs){
        
        datosPartidas.add(new DatosPartida());
        try {
            datosPartidas.get(contador).setPzFinales(rs.getString("pzFinales"));
            datosPartidas.get(contador).setId(rs.getString("idPar"));
            datosPartidas.get(contador).setFolio(rs.getString("folio_fk")+"A");
            datosPartidas.get(contador).setHoja(rs.getString("hoja"));
            datosPartidas.get(contador).setDe(rs.getString("de"));
            datosPartidas.get(contador).setModopart(rs.getString("modoMat"));
            datosPartidas.get(contador).setDesa(rs.getString("desarrollo"));
            datosPartidas.get(contador).setTipo(rs.getString("tipo"));
            datosPartidas.get(contador).setSello(rs.getString("sello"));
            datosPartidas.get(contador).setMedida(rs.getString("medida"));
            datosPartidas.get(contador).setPig(rs.getString("pigmento"));
            datosPartidas.get(contador).setMat1(rs.getString("mat1"));
            datosPartidas.get(contador).setMat2(rs.getString("mat2"));
            datosPartidas.get(contador).setCal1(rs.getString("calibre1"));
            datosPartidas.get(contador).setCal2(rs.getString("calibre2"));
            datosPartidas.get(contador).setCospart(rs.getString("costoPartida"));
            datosPartidas.get(contador).setCosmat(rs.getString("costoMaterialTotal"));
            datosPartidas.get(contador).setKgdes(rs.getString("kgDesperdicio"));
            datosPartidas.get(contador).setPordes(rs.getString("porcentajeDesp"));
            datosPartidas.get(contador).setPuni(rs.getString("precioUnitaro"));
            datosPartidas.get(contador).setPzs(rs.getString("piezas"));
            datosPartidas.get(contador).setKgs(rs.getString("kgPartida"));
            datosPartidas.get(contador).setManera(rs.getString("manera"));
            datosPartidas.get(contador).setImporte(rs.getString("importe"));
            
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    
    
    //Consulta del cliente relacionado al pedido
    public void setIdCliente(){
        
        String sql = "select idC_fk from pedido where folio = "+folioVar+"";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                idCliente = rs.getInt("idC_fk");
            }
            rs.close();
            st.close();
            
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public ArrayList datosPedidoRep(int contador, ArrayList<PedidoATT> pedidos){
        
        String sql = "select * from pedido where folio = "+folioVar+"";
        String folio = "";
        String fecha = "";
        String impresionSt = "";
        String autorizo = "";
        String grabados = "";
        String sub = "";
        int ivaEnteroInt = 0;//guardara el iva entero
        float porcentajeDeIvaFlotante = 0f;//sera el iva pero ya ocnvertido en porcentaje
        String iva = "";
        float ivaf = 0f;
        String total = "";
        String anticipo = "";
        String descuento = "";
        String resto = "";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                folio = rs.getString("folio")+"A";
                fecha = rs.getString("fIngreso");
                impresionSt = rs.getString("impresion");
                autorizo = rs.getString("autorizo");
                grabados = rs.getString("grabados");
                sub = rs.getString("subtotal");
                total = rs.getString("total");
                anticipo = rs.getString("anticipo");
                descuento = rs.getString("descuento");
                resto = rs.getString("resto");
                ivaEnteroInt = Integer.parseInt(rs.getString("porcentajeIVA"));
                
                llenarDatosPedido(rs);//no quitar el rs como parametro
            }
            rs.close();
            st.close();
            
            porcentajeDeIvaFlotante = ivaEnteroInt / 100f;//convertimos el iva obtenido a un porcentaje flotante
            
            ivaf = Float.parseFloat(sub)*porcentajeDeIvaFlotante;
            iva = String.valueOf(ivaf);
            //Se rellenan todos los campos respecto a pedido de todos los indices del arreglo, ya que estos aumentan...
            //debido al numero de partidas
            for(int u = 0; u < pedidos.size(); u++)
            {
                pedidos.get(u).setFolio(folio);
                pedidos.get(u).setFecha(fecha);
                pedidos.get(u).setImpresion(impresionSt);
                pedidos.get(u).setAutorizo(autorizo);
                pedidos.get(u).setGrabados(grabados);
                pedidos.get(u).setSub(sub);
                pedidos.get(u).setIva(iva);
                pedidos.get(u).setTotal(total);
                pedidos.get(u).setAnticipo(anticipo);
                pedidos.get(u).setDescuento(descuento);
                pedidos.get(u).setResto(resto);
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return pedidos;
    }
    
    public ArrayList llenarTablaCliente(ArrayList<PedidoATT> pedidos){
        modeloCli.setRowCount(0);
        String sql = "select * from cliente where idC = "+idCliente+"";
        
        String dirEnt = "";
        String tel = "";
        String cel = "";
        String ciudad = "";
        String agente = "";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                modeloCli.addRow(new Object[]{rs.getString("nom"), rs.getString("tel"), rs.getString("cel"), rs.getString("mail")
                , rs.getString("dir"), rs.getString("domDeEnt"), rs.getString("razSoc"), rs.getString("rfc"), rs.getString("col")
                , rs.getString("ciu"), rs.getString("codPos"), rs.getString("usoCfd"), rs.getString("metDePag"), rs.getString("contacto"),
                rs.getString("agente")});
                
                dirEnt = rs.getString("domDeEnt");
                tel = rs.getString("tel");
                cel = rs.getString("cel");
                ciudad = rs.getString("ciu");
                agente = rs.getString("agente");
                
                dp.setCli(rs.getString("nom"));
            }
            tablaCli.setModel(modeloCli);
            rs.close();
            st.close();
            
            for(int i = 0; i < pedidos.size(); i++)
            {
                pedidos.get(i).setDirEnt(dirEnt);
                pedidos.get(i).setTel(tel);
                pedidos.get(i).setCel(cel);
                pedidos.get(i).setCiudad(ciudad);
                pedidos.get(i).setAgente(agente);
            }          
            
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        pedidosGlobal = pedidos;
        return pedidos;
    }
    
    
    //Se llenan las tablas de los registros de produccion de extrusion, impreso y bolseo
    private void llenarTablasRegistros(){
        
        modeloRegE.setRowCount(0);
        modeloRegI.setRowCount(0);
        modeloRegB.setRowCount(0);
        
        String sql = "select * from operadorExt where idExt_fk = "+idEx+"";
        
        String fechaIni;
            String fechaIniSub;
            String fechaFin;
            String fechaFinSub;
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next())
            {
                fechaIni = rs.getString("fIni");
                fechaIniSub = fechaIni.substring(8, 10)+"/"+fechaIni.substring(5, 7)+"/"+fechaIni.substring(0, 4);
                fechaFin = rs.getString("fFin");
                fechaFinSub = fechaFin.substring(8, 10)+"/"+fechaFin.substring(5, 7)+"/"+fechaFin.substring(0, 4);
                
                modeloRegE.addRow(new Object[]{rs.getString("operador"), rs.getString("numMaquina"), rs.getString("horaIni"), fechaIniSub
                , rs.getString("horaFin"), fechaFinSub, rs.getString("totalHoras"), rs.getString("tiempoMuerto"), rs.getString("extras")
                , rs.getString("kgUniE"), rs.getString("grenia"), rs.getString("costoOpExt")});
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        sql = "select * from operadorImp where idImp_fk = "+idIm+"";
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                fechaIni = rs.getString("fIni");
                fechaIniSub = fechaIni.substring(8, 10)+"/"+fechaIni.substring(5, 7)+"/"+fechaIni.substring(0, 4);
                fechaFin = rs.getString("fFin");
                fechaFinSub = fechaFin.substring(8, 10)+"/"+fechaFin.substring(5, 7)+"/"+fechaFin.substring(0, 4);
                
                modeloRegI.addRow(new Object[]{rs.getString("operador"), rs.getString("ayudante"),rs.getString("numMaquina"), rs.getString("horaIni"), fechaIniSub
                , rs.getString("horaFin"), fechaFinSub, rs.getString("totalHoras"), rs.getString("tiempoMuerto"), rs.getString("extras")
                , rs.getString("kgUniI"), rs.getString("grenia"), rs.getString("costoOpImp")});
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        sql = "select * from operadorBol where idBol_fk = "+idBo+"";
        
        try 
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                fechaIni = rs.getString("fIni");
                fechaIniSub = fechaIni.substring(8, 10)+"/"+fechaIni.substring(5, 7)+"/"+fechaIni.substring(0, 4);
                fechaFin = rs.getString("fFin");
                fechaFinSub = fechaFin.substring(8, 10)+"/"+fechaFin.substring(5, 7)+"/"+fechaFin.substring(0, 4);
                
                modeloRegB.addRow(new Object[]{rs.getString("operador"), rs.getString("numMaquina"), rs.getString("horaIni"), fechaIniSub
                , rs.getString("horaFin"), fechaFinSub, rs.getString("totalHoras"), rs.getString("tiempoMuerto"), rs.getString("extras")
                , rs.getString("kgUniB"), rs.getString("grenia"), rs.getString("suaje"), rs.getString("costoOpBol")});
            }
            rs.close();
            st.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    
    //Limitacion de logitud de datos
    private void limitarInsercion(int tamSQL, java.awt.event.KeyEvent evt, JTextField campo){
        int tamCampo = campo.getText().length() + 1;

        if(tamCampo > tamSQL){
            evt.consume();
        }
    }
    
    public void vacearComponentes(){
        
        impScan.setText("Nombre Impresion");
        btnReport.setEnabled(false);
        btnDetPart.setEnabled(false);
        btnDetPed.setEnabled(false);
        modeloPedido.setRowCount(0);
        modeloCli.setRowCount(0);
        modeloPart.setRowCount(0);
        modeloProd.setRowCount(0);
        modeloCompra.setRowCount(0);
        modeloRegE.setRowCount(0);
        modeloRegI.setRowCount(0);
        modeloRegB.setRowCount(0);
        //resetBar();
    }
    
    private void setBar(int avance){
        
        int progreso = bar.getValue() + avance;
         
        bar.setValue(progreso);
        bar.update(bar.getGraphics());
    }
    
    private void resetBar(){
        bar.setValue(0);
        bar.update(bar.getGraphics());
    }
    
    public void choser(){
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Visualizacion.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Visualizacion.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Visualizacion.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Visualizacion.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar bar;
    private javax.swing.JToggleButton btnDetPart;
    private javax.swing.JButton btnDetPed;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton buscar;
    private javax.swing.JTextField impScan;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPanel panelPed;
    private javax.swing.JPanel panelPro;
    private javax.swing.JTabbedPane tabedPart;
    private javax.swing.JTable tablaCli;
    private javax.swing.JTable tablaCompra;
    private javax.swing.JTable tablaPart;
    private javax.swing.JTable tablaPedido;
    private javax.swing.JTable tablaProd;
    private javax.swing.JTable tablaRegB;
    private javax.swing.JTable tablaRegE;
    private javax.swing.JTable tablaRegI;
    // End of variables declaration//GEN-END:variables
}
