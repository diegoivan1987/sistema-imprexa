
package pantallas;

import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CopiasSeguridad extends javax.swing.JFrame {//permite crear o restaurar copias de seguridad

    public CopiasSeguridad() {
        initComponents();
        this.setResizable(false);
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        hacerCop = new javax.swing.JButton();
        restaurarCop = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        regresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Copia de seguridad");

        jPanel1.setName(""); // NOI18N

        hacerCop.setText("Crear copia de seguridad");
        hacerCop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hacerCopActionPerformed(evt);
            }
        });

        restaurarCop.setText("Restaurar copia de seguridad");
        restaurarCop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restaurarCopActionPerformed(evt);
            }
        });

        jTextPane1.setEditable(false);
        jTextPane1.setText("Para elegir que copia se va a restaurar, escribir la fecha que indica el archivo, solo los numeros, sin espacios. El archivo de la copia debe estar en el escritorio de la computadora actual, fuera de cualquier carpeta.");
        jScrollPane1.setViewportView(jTextPane1);

        jTextPane2.setEditable(false);
        jTextPane2.setText("La copia de seguridad creada se guardará en el escritorio del servidor y de la computadora actual, si al crearla no pide o se ingresó mal la contraseña, la copia saldra vacía.");
        jScrollPane2.setViewportView(jTextPane2);

        regresar.setText("Regresar");
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
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(restaurarCop)
                    .addComponent(hacerCop)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(regresar)
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hacerCop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(restaurarCop)
                .addGap(5, 5, 5)
                .addComponent(regresar)
                .addContainerGap())
        );

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

    private void hacerCopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hacerCopActionPerformed
        hacerCopia();
    }//GEN-LAST:event_hacerCopActionPerformed

    private void restaurarCopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restaurarCopActionPerformed
        restaurarCopia();
    }//GEN-LAST:event_restaurarCopActionPerformed

    private void regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarActionPerformed
       Inicio.prin.setLocationRelativeTo(null);
       Inicio.prin.setVisible(true);
       this.dispose(); 
    }//GEN-LAST:event_regresarActionPerformed

    //crea una copia de seguridad
    private void hacerCopia()
    {
        try
        {
            Runtime.getRuntime().exec("C:\\windows\\System32\\cmd.exe /k start hacerCopia.bat");//ejecuta con cmd el archivo hacerCopia que debe estar en la carpeta raiz del programa 
        }
        catch(IOException Error)
        {
            JOptionPane.showMessageDialog(null, "Problema al hacer la copia" + Error.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
            Error.printStackTrace();
        }
    }
    
    //restaura la copia de seguridad en el escritorio
    private void restaurarCopia()
    {
        try
        {
            Runtime.getRuntime().exec("C:\\windows\\System32\\cmd.exe /k start restaurarCopia.bat");//ejecuta con cmd el archivo restaurarCopia que debe estar en la carpeta raiz del programa
        }
        catch(IOException Error)
        {
            JOptionPane.showMessageDialog(null,"Problema al restaurar la copia" + Error.getLocalizedMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            Error.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton hacerCop;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JButton regresar;
    private javax.swing.JButton restaurarCop;
    // End of variables declaration//GEN-END:variables
}
