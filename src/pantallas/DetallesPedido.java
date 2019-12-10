
package pantallas;

import detalles.DatosPedido;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import report.PedidoATT;


public class DetallesPedido extends javax.swing.JFrame {//permite cambiar algunos datos del pedido
    Connection con;
    Statement st;
    DatosPedido dp;
    Visualizacion vis;
    
    float respaldoSub;
    
    boolean indicadorCambios = false;//indica si hubo cambios
    
    ArrayList<PedidoATT> datosReporte = new ArrayList<PedidoATT>();
    
    public DetallesPedido(DatosPedido dp, Connection con, ArrayList<PedidoATT> datosReporte, Visualizacion vis) {
        initComponents();
        this.con = con;
        this.dp = dp;
        this.datosReporte = datosReporte;
        this.vis = vis;
        
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.setResizable(false);
        
        llenarCampos();//llena la pantalla con los datos del pedido
        desactivarCampos();//inhabilita los campos que no deben modificarse
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jTimeChooserDemo1 = new lu.tudor.santec.jtimechooser.demo.JTimeChooserDemo();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        folio = new javax.swing.JTextField();
        imp = new javax.swing.JTextField();
        cli = new javax.swing.JTextField();
        au = new javax.swing.JTextField();
        dev = new javax.swing.JTextField();
        fin = new javax.swing.JTextField();
        fcom = new javax.swing.JTextField();
        fpa = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        fT = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        estatus = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        grab = new javax.swing.JTextField();
        anti = new javax.swing.JTextField();
        desc = new javax.swing.JTextField();
        sub = new javax.swing.JTextField();
        tot = new javax.swing.JTextField();
        resto = new javax.swing.JTextField();
        dise = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        stc = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        kgdes = new javax.swing.JTextField();
        pordes = new javax.swing.JTextField();
        gf = new javax.swing.JTextField();
        ctot = new javax.swing.JTextField();
        pyg = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        gCambios = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pedido");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(0, 204, 204), new java.awt.Color(0, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("Folio:");

        jLabel2.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("*Impresion:");

        jLabel20.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 102));
        jLabel20.setText("Cliente:");

        jLabel4.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("Fecha de Ingreso:");

        jLabel5.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 102));
        jLabel5.setText("Fecha de Compromiso:");

        jLabel6.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 102));
        jLabel6.setText("Fecha de Pago:");

        jLabel3.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("*Autorizo:");

        jLabel7.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 102));
        jLabel7.setText("*Devolucion:");

        folio.setBackground(new java.awt.Color(0, 153, 153));
        folio.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        folio.setForeground(new java.awt.Color(255, 255, 255));
        folio.setBorder(null);

        imp.setBackground(new java.awt.Color(0, 153, 153));
        imp.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        imp.setForeground(new java.awt.Color(255, 255, 255));
        imp.setBorder(null);
        imp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                impKeyTyped(evt);
            }
        });

        cli.setBackground(new java.awt.Color(0, 153, 153));
        cli.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        cli.setForeground(new java.awt.Color(255, 255, 255));
        cli.setBorder(null);

        au.setBackground(new java.awt.Color(0, 153, 153));
        au.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        au.setForeground(new java.awt.Color(255, 255, 255));
        au.setBorder(null);
        au.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                auKeyTyped(evt);
            }
        });

        dev.setBackground(new java.awt.Color(0, 153, 153));
        dev.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        dev.setForeground(new java.awt.Color(255, 255, 255));
        dev.setBorder(null);
        dev.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                devKeyTyped(evt);
            }
        });

        fin.setBackground(new java.awt.Color(0, 153, 153));
        fin.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        fin.setForeground(new java.awt.Color(255, 255, 255));
        fin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fin.setBorder(null);

        fcom.setBackground(new java.awt.Color(0, 153, 153));
        fcom.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        fcom.setForeground(new java.awt.Color(255, 255, 255));
        fcom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fcom.setBorder(null);

        fpa.setBackground(new java.awt.Color(0, 153, 153));
        fpa.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        fpa.setForeground(new java.awt.Color(255, 255, 255));
        fpa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fpa.setBorder(null);

        jLabel21.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 102));
        jLabel21.setText("*Campos modificables");

        jLabel22.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 102, 102));
        jLabel22.setText("Fecha de Termino:");

        fT.setBackground(new java.awt.Color(0, 153, 153));
        fT.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        fT.setForeground(new java.awt.Color(255, 255, 255));
        fT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fT.setBorder(null);

        jLabel23.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 102, 102));
        jLabel23.setText("*Estatus:");

        estatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DISEÑO", "PROCESO", "COBRANZA", "PAGADO" }));
        estatus.setPreferredSize(new java.awt.Dimension(80, 18));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dev))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(au))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imp))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cli)))
                .addGap(85, 85, 85)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel22)
                                .addComponent(jLabel6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(fT, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fpa, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                                .addComponent(jLabel4))
                            .addGap(4, 4, 4)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fin, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(fcom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                    .addComponent(estatus, 0, 154, Short.MAX_VALUE))))))
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(imp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(au, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(dev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(fin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(estatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(fcom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fpa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(fT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)))
                .addComponent(jLabel21)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 255, 204), new java.awt.Color(51, 255, 102)));

        jLabel8.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 153));
        jLabel8.setText("*Grabados:");

        jLabel9.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 153));
        jLabel9.setText("*Anticipo:");

        jLabel10.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 153));
        jLabel10.setText("*Descuento:");

        jLabel11.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 153));
        jLabel11.setText("Subtotal:");

        jLabel12.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 153));
        jLabel12.setText("Total:");

        jLabel13.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 153));
        jLabel13.setText("Resto:");

        grab.setBackground(new java.awt.Color(0, 204, 204));
        grab.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        grab.setForeground(new java.awt.Color(255, 255, 255));
        grab.setBorder(null);
        grab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                grabKeyTyped(evt);
            }
        });

        anti.setBackground(new java.awt.Color(0, 204, 204));
        anti.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        anti.setForeground(new java.awt.Color(255, 255, 255));
        anti.setBorder(null);
        anti.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                antiKeyTyped(evt);
            }
        });

        desc.setBackground(new java.awt.Color(0, 204, 204));
        desc.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        desc.setForeground(new java.awt.Color(255, 255, 255));
        desc.setBorder(null);
        desc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descKeyTyped(evt);
            }
        });

        sub.setBackground(new java.awt.Color(0, 204, 204));
        sub.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        sub.setForeground(new java.awt.Color(255, 255, 255));
        sub.setBorder(null);

        tot.setBackground(new java.awt.Color(0, 204, 204));
        tot.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        tot.setForeground(new java.awt.Color(255, 255, 255));
        tot.setBorder(null);

        resto.setBackground(new java.awt.Color(0, 204, 204));
        resto.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        resto.setForeground(new java.awt.Color(255, 255, 255));
        resto.setBorder(null);

        dise.setBackground(new java.awt.Color(0, 204, 204));
        dise.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        dise.setForeground(new java.awt.Color(255, 255, 255));
        dise.setBorder(null);
        dise.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                diseKeyTyped(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 102, 153));
        jLabel19.setText("*Diseño");

        jLabel24.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 102, 153));
        jLabel24.setText("*Sticky:");

        stc.setBackground(new java.awt.Color(0, 204, 204));
        stc.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        stc.setForeground(new java.awt.Color(255, 255, 255));
        stc.setBorder(null);
        stc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stcKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resto)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(desc))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tot))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sub))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(grab, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel19))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(stc)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel9)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(anti, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dise, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(6, 6, 6))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(dise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(grab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(anti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(stc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(desc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(sub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(tot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(resto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 255), new java.awt.Color(102, 102, 255)));

        jLabel14.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 255));
        jLabel14.setText("Kilos de Desperdicio:");

        jLabel15.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 0, 255));
        jLabel15.setText("% de Desperdicio:");

        jLabel17.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 0, 255));
        jLabel17.setText("Gastos Fijos:");

        jLabel16.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 0, 255));
        jLabel16.setText("Costo Total:");

        jLabel18.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 0, 255));
        jLabel18.setText("Perdidas y Ganancias:");

        kgdes.setBackground(new java.awt.Color(102, 102, 255));
        kgdes.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        kgdes.setForeground(new java.awt.Color(255, 255, 255));
        kgdes.setBorder(null);

        pordes.setBackground(new java.awt.Color(102, 102, 255));
        pordes.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        pordes.setForeground(new java.awt.Color(255, 255, 255));
        pordes.setBorder(null);

        gf.setBackground(new java.awt.Color(102, 102, 255));
        gf.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        gf.setForeground(new java.awt.Color(255, 255, 255));
        gf.setBorder(null);

        ctot.setBackground(new java.awt.Color(102, 102, 255));
        ctot.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        ctot.setForeground(new java.awt.Color(255, 255, 255));
        ctot.setBorder(null);

        pyg.setBackground(new java.awt.Color(102, 102, 255));
        pyg.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        pyg.setForeground(new java.awt.Color(255, 255, 255));
        pyg.setBorder(null);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kgdes))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pordes))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gf))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pyg, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ctot)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(kgdes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(pordes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(gf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(ctot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(pyg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(153, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(0, 204, 204), new java.awt.Color(0, 204, 204)));

        gCambios.setBackground(new java.awt.Color(51, 51, 51));
        gCambios.setForeground(new java.awt.Color(255, 255, 255));
        gCambios.setText("Guardar cambios");
        gCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gCambiosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(278, 278, 278)
                .addComponent(gCambios)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 20, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(153, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(0, 204, 204), new java.awt.Color(0, 204, 204)));
        jPanel6.setPreferredSize(new java.awt.Dimension(45, 136));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 41, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(153, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(0, 204, 204), new java.awt.Color(0, 204, 204)));
        jPanel7.setPreferredSize(new java.awt.Dimension(45, 136));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 41, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 638, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //obtiene el porcentaje de iva del pedido
    private float obtenerIva()
    {
        Statement st2;
        ResultSet rs2;
        int ivaI = 0;
        float ivaF = 0f;
        String sql = "select porcentajeIVA from pedido where folio = "+dp.getFolio().replace("A", "")+"";//se consulta el iva en la base
        try
        {
            st2 = con.createStatement();
            rs2 = st2.executeQuery(sql);
            while(rs2.next())
            {
                ivaI = Integer.parseInt(rs2.getString("porcentajeIVA"));//guardamos el iva
            }
            rs2.close();
            st2.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        ivaF = ivaI/100f;//convertimos el iva consultado en un porcentaje flotante
        
        return ivaF;
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
    
    private void gCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gCambiosActionPerformed
       
        gCambios.setSelected(false);
        
        float subtotal = respaldoSub + Float.parseFloat(grab.getText());
        float subiva = subtotal * obtenerIva();
        float total = subtotal + subiva;
        float resto = total - (Float.parseFloat(anti.getText()) + Float.parseFloat(desc.getText()));
        //guardara esos campos en la base de datos
        String sql = "update pedido set impresion = '"+imp.getText()+"', "
                + "estatus = '"+estatus.getSelectedItem().toString()+"',"
                + "autorizo = '"+au.getText()+"', "
                + "devolucion = '"+dev.getText()+"', "
                + "grabados = "+grab.getText()+", "
                + "costoDisenio = "+dise.getText()+","
                + "anticipo = "+anti.getText()+", "
                + "descuento = "+desc.getText()+", "
                + "subtotal = "+subtotal+", "
                + "total = "+total+", "
                + "resto = "+resto+", "
                + "sticky = "+Float.parseFloat(stc.getText())+""
                + "where folio = "+dp.getFolio().replace("A", "")+"";
        
       try {
            st = con.createStatement();
            st.execute(sql);
            
            indicadorCambios = true;
            //se insertan los datos que se cambiaron en el arreglo de pedidos
            dp.setImp(imp.getText());
            dp.setEstatus(estatus.getSelectedItem().toString());
            dp.setAuto(au.getText());
            dp.setDev(dev.getText());
            dp.setGrab(grab.getText());
            dp.setDise(dise.getText());
            dp.setSticky(stc.getText());
            dp.setAnti(anti.getText());
            dp.setDesc(desc.getText());
            dp.setSub(String.valueOf(subtotal));
            dp.setTot(String.valueOf(total));
            dp.setRes(String.valueOf(resto));
            
            calculaPyG(Integer.parseInt(dp.getFolio().replace("A", "")));
            st.close();
            llenarCampos();//se actualizan los datos mostrados en pantalla
            actualizarDatosReporte(subtotal, subiva, resto, total);
            JOptionPane.showMessageDialog(null, "Se han guardado los cambios", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(Pedido.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al modificar: "+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }  
    }//GEN-LAST:event_gCambiosActionPerformed

    //actualiza los datos del pedido en el reporte
    private void actualizarDatosReporte(float sub, float iva, float resto, float total){
        
        for(int i = 0; i < datosReporte.size(); i++){
            
            datosReporte.get(i).setImpresion(dp.getImp());
            datosReporte.get(i).setAutorizo(dp.getAuto());
            datosReporte.get(i).setGrabados(dp.getGrab());
            datosReporte.get(i).setAnticipo(dp.getAnti());
            datosReporte.get(i).setDescuento(dp.getDesc());
            datosReporte.get(i).setSub(String.valueOf(sub));
            datosReporte.get(i).setTotal(String.valueOf(total));
            datosReporte.get(i).setIva(String.valueOf(iva));
            datosReporte.get(i).setResto(String.valueOf(resto));
        }
    }
    
    private void impKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impKeyTyped
        int tamCampo = imp.getText().length() + 1;

        if(tamCampo > 40){
            evt.consume();
        }
    }//GEN-LAST:event_impKeyTyped

    private void auKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_auKeyTyped
        limitarInsercion(40, evt, au);
    }//GEN-LAST:event_auKeyTyped

    private void devKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_devKeyTyped
        limitarInsercion(10, evt, dev);
    }//GEN-LAST:event_devKeyTyped

    private void grabKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grabKeyTyped
        soloFlotantes(evt, grab);
    }//GEN-LAST:event_grabKeyTyped

    private void antiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_antiKeyTyped
        soloFlotantes(evt, anti);
    }//GEN-LAST:event_antiKeyTyped

    private void descKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descKeyTyped
        soloFlotantes(evt, desc);
    }//GEN-LAST:event_descKeyTyped

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        if(indicadorCambios == true){
            vis.llenarTablaPedido();
        }
        
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
        vis.setEnabled(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vis.setVisible(true);
        
    }//GEN-LAST:event_formWindowClosed

    private void diseKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diseKeyTyped
        soloFlotantes(evt, dise);
    }//GEN-LAST:event_diseKeyTyped

    private void stcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stcKeyTyped
        soloFlotantes(evt, stc);
    }//GEN-LAST:event_stcKeyTyped

    //llena la pantalla con los datos del pedido
    private void llenarCampos(){
        
        folio.setText(dp.getFolio());
        imp.setText(dp.getImp());
        cli.setText(dp.getCli());
        au.setText(dp.getAuto());
        dev.setText(dp.getDev());
        fin.setText(dp.getFin());
        estableceEstatus(dp.getEstatus());
        fcom.setText(dp.getFcom());
        fpa.setText(dp.getFpag());
        fT.setText(dp.getFterm());
        grab.setText(dp.getGrab());
        dise.setText(dp.getDise());
        stc.setText(dp.getSticky());
        anti.setText(dp.getAnti());
        desc.setText(dp.getDesc());
        sub.setText(dp.getSub());
        tot.setText(dp.getTot());
        resto.setText(dp.getRes());
        kgdes.setText(dp.getKdes());
        pordes.setText(dp.getPdes());
        gf.setText(dp.getGf());
        ctot.setText(dp.getCtot());
        pyg.setText(dp.getPyg());
        respaldoSub = Float.parseFloat(dp.getSub()) - Float.parseFloat(dp.getGrab());
    }
    
    //desactiva los campos que no deben modificarse
    private void desactivarCampos(){
        
        folio.setEditable(false);
        cli.setEditable(false);
        fin.setEditable(false);
        fcom.setEditable(false);
        fpa.setEditable(false);
        fT.setEditable(false);
        sub.setEditable(false);
        tot.setEditable(false);
        resto.setEditable(false);
        kgdes.setEditable(false);
        pordes.setEditable(false);
        gf.setEditable(false);
        ctot.setEditable(false);
        pyg.setEditable(false);
        
    }
    
    //calcula las perdidas y ganancias con los nuevos datos ingresados
    private void calculaPyG(int folio)
    {
        Statement st2;
        ResultSet rs2;
        Statement st3;
        ResultSet rs3;
        float subtotal = 0f, costoTotal = 0f, descuento = 0f;
        float PyG = 0;
        String sql = "select fTermino from pedido where folio = "+folio+"";
        String sql2 = "";
        try
        {
            st3 = con.createStatement();
            rs3 = st3.executeQuery(sql);
            while(rs3.next())
            {
                if(rs3.getString("fTermino").equals("2018-01-01") == false)
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
                        st2.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                    float kgFnPe = calculaKgFinalesPedido(folio);
                    float gf = calculaGfKg(folio);
                    PyG = subtotal - costoTotal - descuento - ( kgFnPe * gf);
                }
            }
            rs3.close();
            st3.close();
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
    
    //calcula los kg finales del pedido
    private float calculaKgFinalesPedido(int folio)
    {
        float sumatoriaPartida = 0f;
        float sumatoriaPedido = 0f;//se inicializ en 0 para que si no hace calculos se retorna 0
                    String sql2 = "select idPar from partida where folio_fk = "+folio+"";//obtiene el folio de cada partida
                    try
                    {
                        Statement st2 = con.createStatement();
                        ResultSet rs2 = st2.executeQuery(sql2);
                        while(rs2.next())
                        {
                            sumatoriaPartida = 0;
                            int idPart2 =   Integer.parseInt(rs2.getString("idPar"));
                            
                            if(sumatoriaPartida <= 0)//si aun no se hace la sumatoria de una partida anterior, entra
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
                                            sumatoriaPartida = sumatoriaPartida + comprado;//hace la sumatoria de lo comprado de bolseo 
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
                                    Statement st3 = con.createStatement();
                                    ResultSet rs3 = st3.executeQuery(sql3);
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
                                    Statement st3 = con.createStatement();
                                    ResultSet rs3 = st3.executeQuery(sql3);
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
    private float calculaGfKg(int folio)
    {
        float gfkg = 0f, gfr = 0f, sumatoriaRango = 0f;
        Statement st2;
        ResultSet rs2;
        String sql2 = "select fTermino from pedido where folio = "+folio+"";
        try
        {
            st2 = con.createStatement();
            rs2 = st2.executeQuery(sql2);
            while(rs2.next())
            {
                if(rs2.getString("fTermino").equals("2018-01-01") == false)
                {
                    String sql = "select gastosFijos,kgFinalesRango from pedido where folio = "+folio+" and gastosFijos is not null and kgFinalesRango is not null";
                    try
                    {
                        st = con.createStatement();
                        ResultSet rs = st.executeQuery(sql);
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
                }
            }
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
    
    private void limitarInsercion(int tamSQL, KeyEvent evt, JTextField campo){
        int tamCampo = campo.getText().length() + 1;

        if(tamCampo > tamSQL){
            evt.consume();
        }
    }
    
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anti;
    private javax.swing.JTextField au;
    private javax.swing.JTextField cli;
    private javax.swing.JTextField ctot;
    private javax.swing.JTextField desc;
    private javax.swing.JTextField dev;
    private javax.swing.JTextField dise;
    private javax.swing.JComboBox estatus;
    private javax.swing.JTextField fT;
    private javax.swing.JTextField fcom;
    private javax.swing.JTextField fin;
    private javax.swing.JTextField folio;
    private javax.swing.JTextField fpa;
    private javax.swing.JButton gCambios;
    private javax.swing.JTextField gf;
    private javax.swing.JTextField grab;
    private javax.swing.JTextField imp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private lu.tudor.santec.jtimechooser.demo.JTimeChooserDemo jTimeChooserDemo1;
    private javax.swing.JTextField kgdes;
    private javax.swing.JTextField pordes;
    private javax.swing.JTextField pyg;
    private javax.swing.JTextField resto;
    private javax.swing.JTextField stc;
    private javax.swing.JTextField sub;
    private javax.swing.JTextField tot;
    // End of variables declaration//GEN-END:variables
}
