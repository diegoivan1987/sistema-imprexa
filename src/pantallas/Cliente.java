package pantallas;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class Cliente extends javax.swing.JFrame {//guardar los datos del cliente
    Principal prin;
    Pedido ped;
    
    Connection con;
    ResultSet rs;
    Statement st;
    
    String sql;
    String nomSt, ageSt, telSt, celSt, domSt, corrSt,razSocSt,rfcSt,colSt,codPosSt,
           ciuSt,usoCfdSt,metDePagSt,contactoSt,domDeEntSt;
    
    
    public Cliente(Connection con) {
       
        initComponents();
        
        this.con = con;
        
        this.setResizable(false);
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jButton3 = new javax.swing.JButton();
        paIni = new javax.swing.JPanel();
        agenC = new javax.swing.JTextField();
        celuC = new javax.swing.JTextField();
        domC = new javax.swing.JTextField();
        corrC = new javax.swing.JTextField();
        teleC = new javax.swing.JTextField();
        nomC = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        guardar = new javax.swing.JButton();
        regresar = new javax.swing.JButton();
        pedido = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        razSocC = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        rfcC = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        colC = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        ciuC = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        usoCfdC = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        metDePagC = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        contactoC = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        domDeEntC = new javax.swing.JTextField();
        codPosC2 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();

        jButton3.setText("jButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cliente");
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        paIni.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        agenC.setForeground(new java.awt.Color(0, 153, 153));
        agenC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                agenCKeyTyped(evt);
            }
        });

        celuC.setForeground(new java.awt.Color(0, 153, 153));
        celuC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                celuCKeyTyped(evt);
            }
        });

        domC.setForeground(new java.awt.Color(0, 153, 153));
        domC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                domCKeyTyped(evt);
            }
        });

        corrC.setForeground(new java.awt.Color(0, 153, 153));
        corrC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                corrCKeyTyped(evt);
            }
        });

        teleC.setForeground(new java.awt.Color(0, 153, 153));
        teleC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                teleCKeyTyped(evt);
            }
        });

        nomC.setForeground(new java.awt.Color(0, 153, 153));
        nomC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomCKeyTyped(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Nombre:");

        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setText("Agente:");

        jLabel3.setForeground(new java.awt.Color(0, 102, 153));
        jLabel3.setText("Teléfono:");

        jLabel4.setForeground(new java.awt.Color(0, 102, 153));
        jLabel4.setText("Celular:");

        jLabel5.setForeground(new java.awt.Color(0, 102, 153));
        jLabel5.setText("Domicilio:");

        jLabel6.setForeground(new java.awt.Color(0, 102, 153));
        jLabel6.setText("Correo:");

        guardar.setBackground(new java.awt.Color(51, 51, 51));
        guardar.setForeground(new java.awt.Color(255, 255, 255));
        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        regresar.setBackground(new java.awt.Color(51, 51, 51));
        regresar.setForeground(new java.awt.Color(255, 255, 255));
        regresar.setText("Regresar");
        regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarActionPerformed(evt);
            }
        });

        pedido.setBackground(new java.awt.Color(51, 51, 51));
        pedido.setForeground(new java.awt.Color(255, 255, 255));
        pedido.setText("Pedido");
        pedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedidoActionPerformed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 102, 153));
        jLabel8.setText("Razón social:");

        razSocC.setForeground(new java.awt.Color(0, 153, 153));
        razSocC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                razSocCKeyTyped(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(0, 102, 153));
        jLabel9.setText("RFC:");

        rfcC.setForeground(new java.awt.Color(0, 153, 153));
        rfcC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                rfcCKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 102, 153));
        jLabel10.setText("Colonia:");

        colC.setForeground(new java.awt.Color(0, 153, 153));
        colC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                colCKeyTyped(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(0, 102, 153));
        jLabel11.setText("CP:");

        jLabel12.setForeground(new java.awt.Color(0, 102, 153));
        jLabel12.setText("Ciudad:");

        ciuC.setForeground(new java.awt.Color(0, 153, 153));
        ciuC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ciuCKeyTyped(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(0, 102, 153));
        jLabel13.setText("Uso CFDI:");

        usoCfdC.setForeground(new java.awt.Color(0, 153, 153));
        usoCfdC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                usoCfdCKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 153));
        jLabel14.setText("Método Pago:");

        metDePagC.setForeground(new java.awt.Color(0, 153, 153));
        metDePagC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                metDePagCKeyTyped(evt);
            }
        });

        jLabel15.setForeground(new java.awt.Color(0, 102, 153));
        jLabel15.setText("Contacto:");

        contactoC.setForeground(new java.awt.Color(0, 153, 153));
        contactoC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                contactoCKeyTyped(evt);
            }
        });

        jLabel16.setForeground(new java.awt.Color(0, 102, 153));
        jLabel16.setText("Entrega:");

        domDeEntC.setForeground(new java.awt.Color(0, 153, 153));
        domDeEntC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                domDeEntCKeyTyped(evt);
            }
        });

        codPosC2.setForeground(new java.awt.Color(0, 153, 153));
        codPosC2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codPosC2KeyTyped(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Datos del cliente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
        );

        javax.swing.GroupLayout paIniLayout = new javax.swing.GroupLayout(paIni);
        paIni.setLayout(paIniLayout);
        paIniLayout.setHorizontalGroup(
            paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paIniLayout.createSequentialGroup()
                .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paIniLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(paIniLayout.createSequentialGroup()
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paIniLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)))
                            .addGroup(paIniLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(regresar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paIniLayout.createSequentialGroup()
                                .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(razSocC)
                                    .addComponent(domC)
                                    .addComponent(codPosC2)
                                    .addComponent(nomC)
                                    .addComponent(celuC)
                                    .addComponent(teleC)
                                    .addComponent(usoCfdC)
                                    .addComponent(contactoC, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(agenC)
                                    .addComponent(colC, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rfcC, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ciuC, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(corrC, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(metDePagC, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(domDeEntC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(paIniLayout.createSequentialGroup()
                                .addComponent(pedido)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(guardar)))))
                .addContainerGap())
        );
        paIniLayout.setVerticalGroup(
            paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paIniLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paIniLayout.createSequentialGroup()
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(razSocC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(domC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(codPosC2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(teleC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usoCfdC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(contactoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(celuC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(paIniLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rfcC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(colC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ciuC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(corrC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(metDePagC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(domDeEntC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(7, 7, 7)
                        .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agenC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(35, 35, 35)
                .addGroup(paIniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(guardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(paIni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(paIni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agenCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_agenCKeyTyped
        limitarInsercion(40, evt, agenC);
    }//GEN-LAST:event_agenCKeyTyped

    private void celuCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_celuCKeyTyped
        soloEnteros(evt);
        limitarInsercion(19, evt, celuC);
    }//GEN-LAST:event_celuCKeyTyped

    private void domCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_domCKeyTyped
        limitarInsercion(40, evt, domC);
    }//GEN-LAST:event_domCKeyTyped

    private void corrCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_corrCKeyTyped
        limitarInsercion(50, evt, corrC);
    }//GEN-LAST:event_corrCKeyTyped

    private void teleCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teleCKeyTyped
        soloEnteros(evt);
        limitarInsercion(19, evt, teleC);
    }//GEN-LAST:event_teleCKeyTyped

    private void nomCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomCKeyTyped
        limitarInsercion(40, evt, nomC);
    }//GEN-LAST:event_nomCKeyTyped

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        guardar.setSelected(false);
        comprobarVacio();//se rellenan los campos vacios
        nomSt = nomC.getText();
        ageSt = agenC.getText();
        telSt = teleC.getText();
        celSt = celuC.getText();
        corrSt = corrC.getText();
        domSt = domC.getText();
        razSocSt = razSocC.getText();
        rfcSt = rfcC.getText();
        colSt = colC.getText();
        codPosSt = codPosC2.getText();
        ciuSt = ciuC.getText();
        usoCfdSt = usoCfdC.getText();
        metDePagSt = metDePagC.getText();
        contactoSt = contactoC.getText();
        domDeEntSt = domDeEntC.getText();
        sql = "insert into cliente (agente, nom, tel, cel, mail, dir, razSoc, rfc, col, codPos, ciu, usoCfd, metDePag, contacto, domDeEnt)"
                + "values('"+ageSt+"', '"+nomSt+"', "+telSt+", "+celSt+", '"+corrSt+"', '"+domSt+"', "
        +"'"+razSocSt+"','"+rfcSt+"','"+colSt+"','"+codPosSt+"','"+ciuSt+"','"+usoCfdSt+"','"+metDePagSt+"', "
        + "'"+contactoSt+"','"+domDeEntSt+"')";

        try {
            st = con.createStatement();
            st.execute(sql);
            st.close();
            JOptionPane.showMessageDialog(null, "Se han guardado los datos: ", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_guardarActionPerformed

    private void regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarActionPerformed
        regresar.setSelected(false);
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_regresarActionPerformed

    private void pedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedidoActionPerformed
        pedido.setSelected(false);
        Inicio.pd.setLocationRelativeTo(null);
        Inicio.pd.vacearComponentes();
        Inicio.pd.sugerenciaFecha();
        Inicio.pd.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_pedidoActionPerformed

    private void razSocCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_razSocCKeyTyped
        limitarInsercion(50, evt, razSocC);
    }//GEN-LAST:event_razSocCKeyTyped

    private void rfcCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rfcCKeyTyped
        limitarInsercion(15, evt, rfcC);
    }//GEN-LAST:event_rfcCKeyTyped

    private void colCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colCKeyTyped
        limitarInsercion(30, evt, colC);
    }//GEN-LAST:event_colCKeyTyped

    private void ciuCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ciuCKeyTyped
        limitarInsercion(30, evt, ciuC);
    }//GEN-LAST:event_ciuCKeyTyped

    private void usoCfdCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usoCfdCKeyTyped
        limitarInsercion(30, evt, usoCfdC);
    }//GEN-LAST:event_usoCfdCKeyTyped

    private void metDePagCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_metDePagCKeyTyped
        limitarInsercion(20, evt, metDePagC);
    }//GEN-LAST:event_metDePagCKeyTyped

    private void contactoCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactoCKeyTyped
        limitarInsercion(30, evt, contactoC);
    }//GEN-LAST:event_contactoCKeyTyped

    private void domDeEntCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_domDeEntCKeyTyped
        limitarInsercion(50, evt, domDeEntC);
    }//GEN-LAST:event_domDeEntCKeyTyped

    private void codPosC2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codPosC2KeyTyped
        limitarInsercion(10, evt, codPosC2);
    }//GEN-LAST:event_codPosC2KeyTyped

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
    }//GEN-LAST:event_formWindowClosing
    
    //Funcion para limitar lo ingresado y evitar errores en la base de datos
    private void limitarInsercion(int tamSQL, KeyEvent evt, JTextField campo){
        int tamCampo = campo.getText().length() + 1;

        if(tamCampo > tamSQL){
            evt.consume();
        }
    }
    
    //Solo numeros enteros en campos
    private void soloEnteros(KeyEvent evt){
        char c = evt.getKeyChar();
        if(c < '0' || c > '9') evt.consume();
    }
    
    //rellena los campos vacios
    private void comprobarVacio(){
        
        if(teleC.getText().equals("")){
            teleC.setText("0");
        }
        if(celuC.getText().equals("")){
            celuC.setText("0");
        }
    }
    
    //vacia los campos
    public void vacearComponentes(){
        razSocC.setText("");
        rfcC.setText("");
        domC.setText("");
        colC.setText("");
        codPosC2.setText("");
        ciuC.setText("");
        teleC.setText("");
        corrC.setText("");
        usoCfdC.setText("");
        metDePagC.setText("");
        contactoC.setText("");
        domDeEntC.setText("");
        nomC.setText("");
        agenC.setText("");
        celuC.setText("");
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField agenC;
    private javax.swing.JTextField celuC;
    private javax.swing.JTextField ciuC;
    private javax.swing.JTextField codPosC2;
    private javax.swing.JTextField colC;
    private javax.swing.JTextField contactoC;
    private javax.swing.JTextField corrC;
    private javax.swing.JTextField domC;
    private javax.swing.JTextField domDeEntC;
    private javax.swing.JButton guardar;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField metDePagC;
    private javax.swing.JTextField nomC;
    private javax.swing.JPanel paIni;
    private javax.swing.JButton pedido;
    private javax.swing.JTextField razSocC;
    private javax.swing.JButton regresar;
    private javax.swing.JTextField rfcC;
    private javax.swing.JTextField teleC;
    private javax.swing.JTextField usoCfdC;
    // End of variables declaration//GEN-END:variables
}
