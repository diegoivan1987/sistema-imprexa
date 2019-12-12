
package pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CostoMaterial extends javax.swing.JFrame {//permite cambiarle el precio a los materiales utilizados en producion

    Connection con;
    ResultSet rs;
    Statement st;
    Principal prin;
    
    String tipo;//guardara el tipo de material cada que se le de clic a la tabla
    
    DefaultTableModel modPart;//modelo de la tabla
    
    public CostoMaterial(Connection con) {
        initComponents();
        
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.con = con;
        this.setResizable(false);
        
        //se indica que las celdas no son editables
        modPart = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //llena la tabla desde el principio
        modPart.setColumnIdentifiers(new Object[]{"Tipo", "Precio"});//agrega los titulos a las columnas
        tablaMat.setModel(modPart);//establece el modelo de la tabla
        tipo = "";//inicialisa el tipo en nada
        
        String sql = "select * from costosMaterial";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                modPart.addRow(new Object[]{rs.getString("tipo"), rs.getString("precio")});
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Error al llenar las tablas"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        guardar.setEnabled(false);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cerrar = new javax.swing.JButton();
        guardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMat = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nPrecio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Costo de material");

        cerrar.setBackground(new java.awt.Color(51, 51, 51));
        cerrar.setForeground(new java.awt.Color(255, 255, 255));
        cerrar.setText("Cerrar");
        cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarActionPerformed(evt);
            }
        });

        guardar.setBackground(new java.awt.Color(51, 51, 51));
        guardar.setForeground(new java.awt.Color(255, 255, 255));
        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        tablaMat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaMat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMat);

        jLabel1.setText("Selecciona un material:");

        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setText("Ingresa su nuevo precio:");

        nPrecio.setForeground(new java.awt.Color(0, 153, 153));

        jLabel3.setText("$");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(cerrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(guardar)
                .addGap(33, 33, 33))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cerrar)
                    .addComponent(guardar))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarActionPerformed
        cerrar.setSelected(false);
        dispose();
    }//GEN-LAST:event_cerrarActionPerformed
    //obtiene el tipo de material cuando le dan clic a la tabla
    private void tablaMatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMatMouseClicked
        try
        {
            tipo = modPart.getValueAt(tablaMat.getSelectedRow(), 0).toString();
            guardar.setEnabled(true);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Selecciona con el boton izquierdo del raton", "Avertencia", JOptionPane.WARNING_MESSAGE);
        }    
    }//GEN-LAST:event_tablaMatMouseClicked

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        // TODO add your handling code here:
        guardar.setSelected(false);
        guardar();
        actualizar();
    }//GEN-LAST:event_guardarActionPerformed

    //inserta el nuevo precio en la base de datos
    private void guardar(){
        String nuevo = nPrecio.getText();//nuevo precio en cadena
        Float fNuevo = Float.parseFloat(nuevo);//nuevo precio en flotante
        String sql = "update costosMaterial set precio = "+fNuevo+" where tipo = '"+tipo+"'";
        try
        {
            
            st = con.createStatement();
            st.execute(sql);
            st.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error al guardar" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //actuaiza la tabla
    private void actualizar()
    {
        String sql = "";
        modPart.setRowCount(0);//vacia la tabla
        //llena la tabla
        sql = "select * from costosMaterial";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                modPart.addRow(new Object[]{rs.getString("tipo"), rs.getString("precio")});
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Error al volver a llenar la tabla" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cerrar;
    private javax.swing.JButton guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nPrecio;
    private javax.swing.JTable tablaMat;
    // End of variables declaration//GEN-END:variables
}
