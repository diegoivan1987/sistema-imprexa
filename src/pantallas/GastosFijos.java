
package pantallas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class GastosFijos extends javax.swing.JFrame {
    
    Connection con;
    Statement st;
    ResultSet rs;
    Pedido pd;
    
    int wd = 0;
    int hg = 0;
    
    DefaultTableModel modeloAf;
     JTableHeader thAf;
     Font fuenteTablas;
    
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public GastosFijos(Connection con, Pedido pd) {
        initComponents();
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        fuenteTablas = new Font("Dialog", Font.BOLD, 12);
        wd = this.getWidth();
        hg = this.getHeight();
        
        this.setResizable(false);
        this.setSize(wd/2 , hg);
        panAfectado.setVisible(false);
        this.con = con;
        this.pd = pd;
        
        modeloAf = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloAf.setColumnIdentifiers(new Object[]{"Folio", "Impresion", "Fecha Pago", "GastosFijos"});
        tablaAf.setModel(modeloAf);
        
        
        thAf = tablaAf.getTableHeader();
        thAf.setFont(fuenteTablas);
        thAf.setBackground(Color.black);
        thAf.setForeground(Color.white);
        
        fMenorQue.setDateFormat(df);
        fMayorQue.setDateFormat(df);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        fMayorQue = new datechooser.beans.DateChooserCombo();
        fMenorQue = new datechooser.beans.DateChooserCombo();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        gfField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        panAfectado = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAf = new javax.swing.JTable();

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gastos fijos");
        setSize(new java.awt.Dimension(400, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Malgun Gothic", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Gastos Fijos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setForeground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Mayor/Igual que...");

        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setText("Menor/Igual que...");

        jLabel4.setText(">=");

        jLabel5.setText("<=");

        gfField.setForeground(new java.awt.Color(0, 153, 153));
        gfField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                gfFieldKeyTyped(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Guadar Gastos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 102, 153));
        jLabel7.setText("Selecciona un rango para modificacion.");

        jLabel8.setForeground(new java.awt.Color(0, 102, 153));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Gastos");

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Cerrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Ver Afectados");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fMayorQue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fMenorQue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gfField, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fMayorQue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel4)))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fMenorQue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel5)))
                .addGap(18, 18, 18)
                .addComponent(gfField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        panAfectado.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setForeground(new java.awt.Color(0, 102, 153));
        jLabel9.setText("Registros Afectados");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(126, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(123, 123, 123))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        tablaAf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaAf);

        javax.swing.GroupLayout panAfectadoLayout = new javax.swing.GroupLayout(panAfectado);
        panAfectado.setLayout(panAfectadoLayout);
        panAfectadoLayout.setHorizontalGroup(
            panAfectadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAfectadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panAfectadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        panAfectadoLayout.setVerticalGroup(
            panAfectadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAfectadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panAfectado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panAfectado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jButton2.setSelected(false);
        pd.setEnabled(true);
        pd.setLocationRelativeTo(null);
        dispose(); 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.setSelected(false);
        String sql = "select folio from pedido where fIngreso >= '"+fMayorQue.getText()+"' AND fIngreso <= '"+fMenorQue.getText()+"' "
                + "AND pagado = 'Si'";
        Statement st1;
        Statement st2;
        comprobarVacio();
        float sumatoriaRango = calculaKgFRango();
        try {
            st1 = con.createStatement();
            st2 = con.createStatement();
            rs = st1.executeQuery(sql);
            while(rs.next())
            {
                sql = "update pedido set gastosFijos = "+gfField.getText()+", kgFinalesRango = "+sumatoriaRango+" "
                        + "where folio = "+rs.getInt("folio")+"";
                st2.execute(sql);
                calculaPyG(rs.getInt("folio"));
            }
            
            rs.close();
            st1.close();
            st2.close();
            JOptionPane.showMessageDialog(null, "Se han actualizado los gastos", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            mostrarTablaAfectados();
        } catch (SQLException ex) {
            Logger.getLogger(GastosFijos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        pd.setEnabled(true);
        pd.setVisible(true);
        pd.setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowClosed

    private void gfFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gfFieldKeyTyped
        soloFlotantes(evt, gfField);
    }//GEN-LAST:event_gfFieldKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jButton3.setSelected(false);
        mostrarTablaAfectados();
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void mostrarTablaAfectados(){
        this.setSize(wd, hg);
        panAfectado.setVisible(true);
        modeloAf.setRowCount(0);
        this.setLocationRelativeTo(null);
        
        String sql = "select folio, impresion, fIngreso, gastosFijos from pedido where fIngreso >= '"+fMayorQue.getText()+"' "
                + "AND fIngreso <= '"+fMenorQue.getText()+"' AND pagado = 'Si'";
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                 modeloAf.addRow(new Object[]{rs.getString("folio"), rs.getString("impresion"), rs.getString("fIngreso"), 
                     rs.getString("gastosFijos")});
            }
            tablaAf.setModel(modeloAf);
            st.close();
            rs.close();     
        } catch (SQLException ex) {
            Logger.getLogger(GastosFijos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**perdidas y ganancias**/
    
      //calcula la sumatoria de kg comprados y producidos de bolseo del pedido
      private void calculaSumatoriaKgBolseoPe(int folio)
      {
          String sql = "select idPar from partida where folio_fk = "+folio+"";
          int idPartida = 0;
          float sumatoria = 0f;
          Statement st;
          ResultSet rs;
          Statement st2;
          ResultSet rs2;
          int idBo2 = 0;
          Statement st3;
          ResultSet rs3;
          try
          {
              st = con.createStatement();
              rs = st.executeQuery(sql);
              while(rs.next())
              {
                  idPartida = Integer.parseInt(rs.getString("idPar"));
                  //suma de lo comprado
                  String sql2 = "select produccion from bolseo where idPar_fk = "+idPartida+"";
                  try
                  {
                      st2 = con.createStatement();
                      rs2 = st2.executeQuery(sql2);
                      while(rs2.next())
                      {
                          sumatoria = sumatoria + Float.parseFloat(rs2.getString("produccion"));
                      }
                      rs2.close();
                      st2.close();
                    }
                    catch(SQLException ex)
                    {
                        Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("calculaSumatoriaKgBolseoPe: error al sumar los kg comprados");
                    }
                  
                    //obtiene el idBo
                    sql2 = "select idBol from bolseo where idPar_fk = "+idPartida+"";
                    try
                    {
                        st2 = con.createStatement();
                        rs2 = st2.executeQuery(sql2);
                        while(rs2.next())
                        {
                            idBo2 = Integer.parseInt(rs2.getString("idBol"));
                            //suma de lo producido
                            String sql3 = "select kgUniB from operadorBol where idBol_fk = "+idBo2+"";
                            try
                            {
                                st3 = con.createStatement();
                                rs3 = st3.executeQuery(sql3);
                                while(rs3.next())
                                {
                                    sumatoria = sumatoria + (Float.parseFloat(rs3.getString("kgUniB")));
                                }
                                rs3.close();
                                st3.close();
                            }
                            catch(SQLException ex)
                            {
                                Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println("calculaSumatoriaKgBolseoPe:error al sumar los kg producidos y la grenia");
                            }
                        }
                        rs2.close();
                        st2.close();
                    }
                    catch(SQLException ex)
                    {
                        Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("calculaSumatoriaKgBolseoPe: error al obtener el idBo2");
                    }
                }
                rs.close();
            }
            catch(SQLException ex)
            {
                Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("calculaSumatoriaKgBolseoPe : error al sacar la sumatoria");
            }
          
            sql = "update pedido set sumatoriaBolseoP = "+sumatoria+"where folio = "+folio+"";
            try
            {
                st = con.createStatement();
                st.execute(sql);
                st.close();
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
                System.out.println("calculaSumatoriaKgBolseoPe: Error al insertar la sumatoria de bolseo en pedido");
            }
            calculaPerdidasYGanancias(sumatoria, folio);
      }
      
      // calcula e inseeta perdidas y ganancias en pedido
      private void calculaPerdidasYGanancias(float sumatoria, int folio)
      {
          float subtotal = 0f, gf = 0f, costoTotal = 0f, descuentos = 0f, resta = 0f, pyg = 0f;
          Statement st;
          ResultSet rs;
          String sql = "select subtotal,costoTotal,descuento from pedido where folio = "+folio+"";
          try
          {
              st = con.createStatement();
              rs = st.executeQuery(sql);
              while(rs.next())
              {
                  subtotal = Float.parseFloat(rs.getString("subtotal"));
                  costoTotal = Float.parseFloat(rs.getString("costoTotal"));
                  descuentos = Float.parseFloat(rs.getString("descuento"));
              }
              rs.close();
          }
          catch(SQLException ex)
          {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
              System.out.println("calculaPerdidasYGanancias:error al obtener el subtotal, el costo total o los descuentos");
          }
         
          sql = "select gastosFijos from pedido where folio = "+folio+"";
          try
          {
              st = con.createStatement();
              rs = st.executeQuery(sql);
              while(rs.next())
              {
                  gf = Float.parseFloat(rs.getString("gastosFijos"));
              }
              rs.close();
          }
          catch(SQLException ex)
          {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("calculaPerdidasYGanancias:error al obtener los gf");
          }
          
          resta = (sumatoria*gf) + costoTotal + descuentos;
          pyg = subtotal - resta;
          
          sql = "update pedido set perdidasYGanancias = "+pyg+" where folio = "+folio+"";
          try
          {
              st = con.createStatement();
              st.execute(sql);
              st.close();
          }
          catch(SQLException ex)
          {
            Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("calculaPerdidasYGanancias: error al insertar pyg");
          }
      }
    
      
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
    
    
    private void comprobarVacio(){
        if(gfField.getText().equals("") || gfField.getText().equals(".")){
            gfField.setText("0");
        }
    }
    
    /**perdidas y ganancias**/
    
    //calcula los kg finales del pedido
    private float calculaKgFinalesPedido(int folio)
    {
        //entra al pedido
        String sql = "select pagado from pedido where folio = "+folio+"";
        float sumatoria = 0f;
        try
        {
            Statement st6 = con.createStatement();
            ResultSet rs6 = st6.executeQuery(sql);
            while(rs6.next())
            {
                String pagado = rs6.getString("pagado");
                if(pagado.equals("Si"))//verifica que este pagado
                {
                    String sql2 = "select idPar from partida where folio_fk = "+folio+"";//obtiene el folio de cada partida
                    try
                    {
                        Statement st7 = con.createStatement();
                        ResultSet rs7 = st7.executeQuery(sql2);
                        while(rs7.next())
                        {
                            int idPart2 =   Integer.parseInt(rs7.getString("idPar"));
                            
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
                        rs7.close();
                        st7.close();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }
                else
                    sumatoria = 0;
            }
            rs6.close();
            st6.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return sumatoria;
    }
    
    private float calculaKgFRango()
    {
        String sql = "select folio from pedido where fIngreso >= '"+fMayorQue.getText()+"' AND fIngreso <= '"+fMenorQue.getText()+"' "
                + "AND pagado = 'Si'";
        float sumatoria = 0f;
        Statement st3;
        ResultSet rs3;
        try
        {
            st3 = con.createStatement();
            rs3 = st3.executeQuery(sql);
            while(rs3.next())
            {
                sumatoria = sumatoria + calculaKgFinalesPedido(Integer.parseInt(rs3.getString("folio")));
            }
        }
        catch(SQLException ex)
        {
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
            Statement st3 = con.createStatement();
            ResultSet rs3 = st3.executeQuery(sql);
            while(rs3.next())
            {
                gfr = Float.parseFloat(rs3.getString("gastosFijos"));
                sumatoriaRango = Float.parseFloat(rs3.getString("kgFinalesRango"));
                if(sumatoriaRango != 0)
                    gfkg = gfr / sumatoriaRango;
            }
            rs3.close();
            st3.close();
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
        Statement st4;
        ResultSet rs4;
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
                        st4 = con.createStatement();
                        rs4 = st4.executeQuery(sql2);
                        while(rs4.next())
                        {
                            subtotal = Float.parseFloat(rs4.getString("subtotal"));
                            costoTotal = Float.parseFloat(rs4.getString("costoTotal"));
                            descuento = Float.parseFloat(rs4.getString("descuento"));
                        }
                        rs4.close();
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
                        st4 = con.createStatement();
                        st4.execute(sql2);
                        st4.close();
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
                        st4 = con.createStatement();
                        st4.execute(sql);
                        st4.close();
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
    private datechooser.beans.DateChooserCombo fMayorQue;
    private datechooser.beans.DateChooserCombo fMenorQue;
    private javax.swing.JTextField gfField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panAfectado;
    private javax.swing.JTable tablaAf;
    // End of variables declaration//GEN-END:variables
}
