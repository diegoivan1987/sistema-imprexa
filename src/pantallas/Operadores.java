
package pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Operadores extends javax.swing.JFrame {
    //variables para la conexion de la base de datos
    Connection con;
    ResultSet rs;
    Statement st;
    String nombreTabla = "";//guardara el nombre del operador seleccionado en la tabla
    DefaultTableModel modOp;//guardara el modelo de la tabla
    
    public Operadores(Connection con) {
        initComponents();
        this.con = con;
        vaciar();//inicializamos todo en default
        //se deshabilita el boton de modificacion
        btnMod.setEnabled(false);
        btnMod.setVisible(false);
        checkboxMod.setSelected(false);//se establece el checkbox en no seleccionado
        formatoTabla();//le damos formato a la tabla de operadores
        llenarTabla();//se llena la tabla de operadores
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaOp = new javax.swing.JTable();
        sueldo_48_hrs = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        listAyudante = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        checkboxMod = new javax.swing.JCheckBox();
        guardar = new javax.swing.JButton();
        btnMod = new javax.swing.JButton();
        regresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro de operador");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaOp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaOp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaOpMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaOp);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 490, 210));

        sueldo_48_hrs.setText("jTextField1");
        jPanel1.add(sueldo_48_hrs, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 100, -1));

        nombre.setText("jTextField1");
        jPanel1.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 190, -1));

        listAyudante.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "si", "no" }));
        jPanel1.add(listAyudante, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 300, 50, -1));

        jLabel1.setText("Nombre:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, -1, -1));

        jLabel2.setText("sueldo(por 48 hrs):");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, -1, 20));

        jLabel4.setText("¿Será ayudante de impreso?");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 300, -1, -1));

        checkboxMod.setText("Modificar datos");
        jPanel1.add(checkboxMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        jPanel1.add(guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 340, -1, -1));

        btnMod.setText("Guardar cambios");
        jPanel1.add(btnMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 340, -1, -1));

        regresar.setText("Regresar");
        regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarActionPerformed(evt);
            }
        });
        jPanel1.add(regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        String nombreSt = nombre.getText();
        float sueldo48 = Float.parseFloat(sueldo_48_hrs.getText());
        float sueldoHr = sueldo48 / 48;//sueldo por hora
        String ayudanteSt = listAyudante.getSelectedItem().toString();//obtiene si o no
        boolean ayudante = false;
        if(ayudanteSt.equals("si"))//si sera ayudante, asigna true, si no, asigna false
        {
            ayudante = true;
        }
        else if(ayudanteSt.equals("no"))
        {
            ayudante = false;
        }
        //datos a guardar
        String sql = "insert into operadores(nombre, sueldo_48_hrs, sueldo_hr, ayudante)values('"+nombreSt+"',"+sueldo48+","+sueldoHr+","+ayudante+")";
        try
        {
            st = con.createStatement();
            st.execute(sql);//guardamos en la base de datos
            st.close();
            JOptionPane.showMessageDialog(null, "Se guardo el operador");
            vaciar();//se establecen los campos por default
            actualizarTabla();//se actualiza la tabla de operadores
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_guardarActionPerformed

    private void regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarActionPerformed
        Inicio.prin.setLocationRelativeTo(null);
        Inicio.prin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_regresarActionPerformed

    //cuando le dan clic a la tabla
    private void tablaOpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaOpMouseClicked
        if(checkboxMod.isEnabled())//si el checkbox de modificar los datos esta activado
        {
            mostrarDatosOperador();//se muestran los datos del operador seleccionado
        }
    }//GEN-LAST:event_tablaOpMouseClicked

    private void mostrarDatosOperador()
    {
        String nombreOLD = modOp.getValueAt(tablaOp.getSelectedRow(), 0).toString();//obtenemos el nombre del operador seleccionado
        boolean ayudanteOLD;
        String sql = "select * from operadores where nombre = '"+nombreOLD+"'";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                nombre.setText(rs.getString("nombre"));
                sueldo_48_hrs.setText(rs.getString("sueldo_48_hrs").toString());
                ayudanteOLD = rs.getBoolean("ayudante");
            }
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void vaciar()//inicializa todo en el default
    {
        nombre.setText("");
        sueldo_48_hrs.setText("");
        listAyudante.setSelectedIndex(0);
    }
    
    private void formatoTabla()//le da formato a las tablas
    {
        modOp = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }    
        };
        modOp.setColumnIdentifiers(new Object[]{"Nombre","Sueldo x 48 hrs", "Es ayudante"});
        tablaOp.setModel(modOp);
    }
    
    private void llenarTabla()//llena la tabla de operadores
    {
        String ayudanteSt = "";//servira para mostrar si o no en ayudante
        String sql = "select * from operadores where nombre is not null";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                if(rs.getBoolean("ayudante") == false)
                {
                    ayudanteSt = "no";
                }
                else if(rs.getBoolean("ayudante") == true)
                {
                    ayudanteSt = "si";
                }
                modOp.addRow(new Object[]{rs.getString("nombre"), rs.getString("sueldo_48_hrs"), ayudanteSt});
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void actualizarTabla()//hace lo mismo que llenar tabla, pero la vacía al principio
    {
        modOp.setRowCount(0);
        String ayudanteSt = "";//servira para mostrar si o no en ayudante
        String sql = "select * from operadores where nombre is not null";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                if(rs.getBoolean("ayudante") == false)
                {
                    ayudanteSt = "no";
                }
                else if(rs.getBoolean("ayudante") == true)
                {
                    ayudanteSt = "si";
                }
                modOp.addRow(new Object[]{rs.getString("nombre"), rs.getString("sueldo_48_hrs"), ayudanteSt});
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMod;
    private javax.swing.JCheckBox checkboxMod;
    private javax.swing.JButton guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox listAyudante;
    private javax.swing.JTextField nombre;
    private javax.swing.JButton regresar;
    private javax.swing.JTextField sueldo_48_hrs;
    private javax.swing.JTable tablaOp;
    // End of variables declaration//GEN-END:variables
}
