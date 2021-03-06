
package pantallas;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import javax.swing.ImageIcon;

public class Principal extends javax.swing.JFrame {

    Cliente cli;
    Pedido pd;
    Procesos pro;
    Visualizacion vis;
    CostoMaterial cos;
    Connection con;
    Inicio in;
    
    public Principal(Connection con) {
        
        initComponents();
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.con = con;
        this.setResizable(false);
       
        agreC.setBackground(Color.gray);
        agreP.setBackground(Color.gray);
        jButton2.setBackground(Color.gray);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        agreC = new javax.swing.JButton();
        agreP = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pantalla principal");
        setBackground(new java.awt.Color(0, 51, 51));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        agreC.setBackground(new java.awt.Color(102, 102, 102));
        agreC.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        agreC.setForeground(new java.awt.Color(0, 255, 204));
        agreC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cl2.png"))); // NOI18N
        agreC.setText("Registrar un cliente");
        agreC.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        agreC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                agreCMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                agreCMouseExited(evt);
            }
        });
        agreC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agreCActionPerformed(evt);
            }
        });

        agreP.setBackground(new java.awt.Color(102, 102, 102));
        agreP.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        agreP.setForeground(new java.awt.Color(0, 255, 204));
        agreP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/pd.png"))); // NOI18N
        agreP.setText("Registrar un pedido");
        agreP.setActionCommand("");
        agreP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        agreP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                agrePMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                agrePMouseExited(evt);
            }
        });
        agreP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agrePActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 51));

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 255, 204));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ojo.png"))); // NOI18N
        jButton2.setText("      Revisar registros");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2MouseExited(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(agreC, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                    .addComponent(agreP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(agreC, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(agreP, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setForeground(new java.awt.Color(0, 255, 204));

        jMenu1.setBackground(new java.awt.Color(0, 0, 0));
        jMenu1.setForeground(new java.awt.Color(0, 255, 204));
        jMenu1.setText("Acciones");

        jMenuItem2.setBackground(new java.awt.Color(51, 51, 51));
        jMenuItem2.setForeground(new java.awt.Color(255, 255, 255));
        jMenuItem2.setText("Procesos");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Modificar precio de material");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem1.setText("Copias de seguridad");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agrePActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agrePActionPerformed
        agreP.setSelected(false);
        Inicio.pd.setLocationRelativeTo(null);
        Inicio.pd.vacearComponentes();
        Inicio.pd.sugerenciaFecha();
        Inicio.pd.setVisible(true);
        this.dispose();
        
        /*pd = new Pedido(con);
        pd.setLocationRelativeTo(null);
        pd.setVisible(true);
        dispose();*/     
    }//GEN-LAST:event_agrePActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        
        Inicio.pro.setLocationRelativeTo(null);
        Inicio.pro.vacearComponentes();
        Inicio.pro.setVisible(true);
        
        /*pro = new Procesos(con);
        pro.setLocationRelativeTo(null);
        pro.setVisible(true);*/
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        cos = new CostoMaterial(con);
        cos.setLocationRelativeTo(null);
        cos.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jButton2.setSelected(false);
        Inicio.vis.setLocationRelativeTo(null);
        Inicio.vis.vacearComponentes();
        Inicio.vis.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void agreCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agreCActionPerformed
        agreC.setSelected(false);
        Inicio.cl.setLocationRelativeTo(null);
        Inicio.cl.vacearComponentes();
        Inicio.cl.setVisible(true);
        this.dispose();

        this.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent evt ) {
                System.out.println("cierre");
                System.exit( 0 );
            }
        } );

        /*cli = new Cliente(con);
        cli.setLocationRelativeTo(null);
        cli.setVisible(true);
        dispose();*/
    }//GEN-LAST:event_agreCActionPerformed

    private void agreCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agreCMouseEntered
        
        agreC.setBackground(Color.white);
    }//GEN-LAST:event_agreCMouseEntered

    private void agreCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agreCMouseExited
        agreC.setBackground(Color.gray);
    }//GEN-LAST:event_agreCMouseExited

    private void agrePMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agrePMouseEntered
        agreP.setBackground(Color.white);
    }//GEN-LAST:event_agrePMouseEntered

    private void agrePMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agrePMouseExited
        agreP.setBackground(Color.gray);
    }//GEN-LAST:event_agrePMouseExited

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        jButton2.setBackground(Color.white);
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        jButton2.setBackground(Color.gray);
    }//GEN-LAST:event_jButton2MouseExited

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        CopiasSeguridad cop = new CopiasSeguridad();
        cop.setVisible(true);
        cop.setLocationRelativeTo(null);
        dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agreC;
    private javax.swing.JButton agreP;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
