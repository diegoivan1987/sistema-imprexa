
package pantallas;

import detalles.DatosPartida;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import report.PedidoATT;

public class DetallesPartida extends javax.swing.JFrame {//permitira hacer cambios en algunos datos de las partidas
    int indicadorParaImporte = 0;//indica la manera en la que se calcula el importe

    ArrayList<DatosPartida> dp = new ArrayList<DatosPartida>();
    ArrayList<PedidoATT> datosReporte = new ArrayList<PedidoATT>();
    
    int indicePartida = 0;
    
    Statement st;
    ResultSet rs;
    Connection con;
    Visualizacion vis;
    
    //se utilizaran para ser mandadas al reporte de produccion
    float sub = 0f;
    float iva = 0f;
    float total = 0f;
    float resto = 0f;
    
    boolean indicadorCambios = false;//indica si se cambiaron datos
    
    public DetallesPartida(ArrayList<DatosPartida> dp, Connection con, ArrayList<PedidoATT> datosReporte, Visualizacion vis) {
        initComponents();
        llenarListas();//llenamos las listas desplegables
        //se activan si llega a haber cambios en los checkbox
        onChangePU();
        onChangeKilos();
        onChangePz();
        onChangePzFin();
        
        this.dp = dp;
        this.datosReporte = datosReporte;
        this.con = con;
        this.vis = vis;
        this.setIconImage (new ImageIcon(getClass().getResource("/Images/iconoCab.png")).getImage());
        this.setResizable(false);
        
        //se establecen los campos que no se podran modificar
        folio.setEditable(false);
        hj.setEditable(false);
        de.setEditable(false);
        modoPart.setEditable(false);
        cosPart.setEditable(false);
        cosMat.setEditable(false);
        kgDes.setEditable(false);
        porDes.setEditable(false);
        imp.setEditable(false);
        mat1.setEditable(false);
        mat2.setEditable(false);

        mostrarPartida(indicePartida);//muestra los datos de la partida actua
    }
    
    //llena las listas desplegables
    private void llenarListas(){
        sta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DISEÑO", "PROCESO", "COBRANZA", "PAGADO"}));
        sello.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FONDO", "LATERAL", "TRASLAPE"}));
    }

    private void mostrarPartida(int indice){
        //se obtienen los datos del arreglo
        try
        {
            folio.setText(dp.get(indice).getFolio());
            hj.setText(dp.get(indice).getHoja());
            de.setText(dp.get(indice).getDe());
            modoPart.setText(dp.get(indice).getModopart());
            desa.setText(dp.get(indice).getDesa());
            tipo.setText(dp.get(indice).getTipo());
            setLista(sta,dp.get(indice).getEstatus());
            setLista(sello,dp.get(indice).getSello());
            med.setText(dp.get(indice).getMedida());
            pig.setText(dp.get(indice).getPig());
            mat1.setText(dp.get(indice).getMat1());
            mat2.setText(dp.get(indice).getMat2());
            cal1.setText(dp.get(indice).getCal1());
            cal2.setText(dp.get(indice).getCal2());
            cosPart.setText(dp.get(indice).getCospart());
            cosMat.setText(dp.get(indice).getCosmat());
            kgDes.setText(dp.get(indice).getKgdes());
            porDes.setText(dp.get(indice).getPordes());
            pUnit.setText(dp.get(indice).getPuni());
            pz.setText(dp.get(indice).getPzs());
            kg.setText(dp.get(indice).getKgs());
            imp.setText(dp.get(indice).getImporte());
            pzFinales.setText(dp.get(indice).getPzFinales());
            //si el precio unitario por el numero de piezas es igual al importe, se selecciona el chechkbox de pz
            if(Float.parseFloat(dp.get(indice).getPuni()) * Integer.parseInt(dp.get(indice).getPzs()) == Float.parseFloat(dp.get(indice).getImporte()))
            {
                checkPz.setSelected(true);
            }//si el precio unitario por el numero de kg es igual al importe, se selecciona el chechkbox de kg
            else  if(Float.parseFloat(dp.get(indice).getPuni()) * Float.parseFloat(dp.get(indice).getKgs()) == Float.parseFloat(dp.get(indice).getImporte()))
            {
                checkKg.setSelected(true);
            }//si el precio unitario por el numero de piezas finales es igual al importe, se selecciona el chechkbox de pz finales
            else
            {
                checkPzFinales.setSelected(true);
            }
        }
        catch(IndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "No hay partidas en este pedido", "Avertencia", JOptionPane.WARNING_MESSAGE);
            
        }
    }
    
    //muestra el elemento que selecciono el usuario en las listas desplegables
    private void setLista(JComboBox lista, String campo){
        for(int in = 0; in<lista.getItemCount();in++)
            {
                if(campo.equals(lista.getItemAt(in)))
                {
                    lista.setSelectedIndex(in);
                }
            }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        folio = new javax.swing.JTextField();
        hj = new javax.swing.JTextField();
        de = new javax.swing.JTextField();
        modoPart = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        desa = new javax.swing.JTextField();
        tipo = new javax.swing.JTextField();
        med = new javax.swing.JTextField();
        pig = new javax.swing.JTextField();
        cosPart = new javax.swing.JTextField();
        cosMat = new javax.swing.JTextField();
        kgDes = new javax.swing.JTextField();
        porDes = new javax.swing.JTextField();
        cal1 = new javax.swing.JTextField();
        cal2 = new javax.swing.JTextField();
        sello = new javax.swing.JComboBox();
        sta = new javax.swing.JComboBox();
        mat1 = new javax.swing.JTextField();
        mat2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pUnit = new javax.swing.JTextField();
        pz = new javax.swing.JTextField();
        kg = new javax.swing.JTextField();
        imp = new javax.swing.JTextField();
        pzFinalesLb = new javax.swing.JLabel();
        pzFinales = new javax.swing.JTextField();
        checkPz = new javax.swing.JCheckBox();
        checkKg = new javax.swing.JCheckBox();
        saveMod = new javax.swing.JButton();
        checkPzFinales = new javax.swing.JCheckBox();
        sig = new javax.swing.JToggleButton();
        ant = new javax.swing.JToggleButton();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Partidas");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(51, 255, 255), new java.awt.Color(0, 204, 204)));

        jLabel15.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 102));
        jLabel15.setText("Folio:");

        jLabel11.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 102));
        jLabel11.setText("Hoja:");

        jLabel12.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 102));
        jLabel12.setText("De:");

        jLabel22.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 102));
        jLabel22.setText("Modalidad de Generacion de la Partida:");

        folio.setBackground(new java.awt.Color(0, 153, 153));
        folio.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        folio.setForeground(new java.awt.Color(255, 255, 255));
        folio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        folio.setBorder(null);

        hj.setBackground(new java.awt.Color(0, 153, 153));
        hj.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        hj.setForeground(new java.awt.Color(255, 255, 255));
        hj.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hj.setBorder(null);

        de.setBackground(new java.awt.Color(0, 153, 153));
        de.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        de.setForeground(new java.awt.Color(255, 255, 255));
        de.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        de.setBorder(null);

        modoPart.setBackground(new java.awt.Color(0, 153, 153));
        modoPart.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        modoPart.setForeground(new java.awt.Color(255, 255, 255));
        modoPart.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        modoPart.setBorder(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hj, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(de, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modoPart, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel22)
                    .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(de, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modoPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("*Estatus:");

        jLabel2.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setText("*Medida:");

        jLabel4.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 153));
        jLabel4.setText("*Tipo:");

        jLabel5.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 153));
        jLabel5.setText("*Pigmento:");

        jLabel6.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 153));
        jLabel6.setText("*Sello:");

        jLabel7.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 153));
        jLabel7.setText("Material 1:");

        jLabel8.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 153));
        jLabel8.setText("Material 2:");

        jLabel9.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 153));
        jLabel9.setText("Calibre 1:");

        jLabel10.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 153));
        jLabel10.setText("Calibre 2:");

        jLabel18.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 153));
        jLabel18.setText("Kilos de Desperdicio:");

        jLabel19.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 102, 153));
        jLabel19.setText("Porcentaje de Desperdicio:");

        jLabel20.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 153));
        jLabel20.setText("Costo Total del Material:");

        jLabel21.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 153));
        jLabel21.setText("Costo de la Partida:");

        jLabel17.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 153));
        jLabel17.setText("*Desarrollo:");

        desa.setBackground(new java.awt.Color(0, 153, 204));
        desa.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        desa.setForeground(new java.awt.Color(255, 255, 255));
        desa.setBorder(null);
        desa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                desaKeyTyped(evt);
            }
        });

        tipo.setBackground(new java.awt.Color(0, 153, 204));
        tipo.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        tipo.setForeground(new java.awt.Color(255, 255, 255));
        tipo.setBorder(null);
        tipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tipoKeyTyped(evt);
            }
        });

        med.setBackground(new java.awt.Color(0, 153, 204));
        med.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        med.setForeground(new java.awt.Color(255, 255, 255));
        med.setBorder(null);
        med.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                medKeyTyped(evt);
            }
        });

        pig.setBackground(new java.awt.Color(0, 153, 204));
        pig.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        pig.setForeground(new java.awt.Color(255, 255, 255));
        pig.setBorder(null);
        pig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pigKeyTyped(evt);
            }
        });

        cosPart.setBackground(new java.awt.Color(0, 153, 204));
        cosPart.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        cosPart.setForeground(new java.awt.Color(255, 255, 255));
        cosPart.setBorder(null);

        cosMat.setBackground(new java.awt.Color(0, 153, 204));
        cosMat.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        cosMat.setForeground(new java.awt.Color(255, 255, 255));
        cosMat.setBorder(null);

        kgDes.setBackground(new java.awt.Color(0, 153, 204));
        kgDes.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        kgDes.setForeground(new java.awt.Color(255, 255, 255));
        kgDes.setBorder(null);

        porDes.setBackground(new java.awt.Color(0, 153, 204));
        porDes.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        porDes.setForeground(new java.awt.Color(255, 255, 255));
        porDes.setBorder(null);

        cal1.setBackground(new java.awt.Color(0, 153, 204));
        cal1.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        cal1.setForeground(new java.awt.Color(255, 255, 255));
        cal1.setBorder(null);
        cal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cal1KeyTyped(evt);
            }
        });

        cal2.setBackground(new java.awt.Color(0, 153, 204));
        cal2.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        cal2.setForeground(new java.awt.Color(255, 255, 255));
        cal2.setBorder(null);
        cal2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cal2KeyTyped(evt);
            }
        });

        sello.setBackground(new java.awt.Color(0, 153, 204));
        sello.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        sello.setForeground(new java.awt.Color(255, 255, 255));
        sello.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Sello" }));

        sta.setBackground(new java.awt.Color(0, 153, 204));
        sta.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        sta.setForeground(new java.awt.Color(255, 255, 255));
        sta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona Un Estado" }));

        mat1.setBackground(new java.awt.Color(0, 153, 204));
        mat1.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        mat1.setForeground(new java.awt.Color(255, 255, 255));
        mat1.setBorder(null);
        mat1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mat1KeyTyped(evt);
            }
        });

        mat2.setBackground(new java.awt.Color(0, 153, 204));
        mat2.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        mat2.setForeground(new java.awt.Color(255, 255, 255));
        mat2.setBorder(null);
        mat2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mat2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pig))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel17)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(desa)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel6)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sello, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(med)))))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(mat2))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(mat1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(porDes))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cosMat))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kgDes))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cosPart, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cal2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cal1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(80, 80, 80)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(desa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(cosPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cosMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(kgDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(porDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(med, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(pig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(mat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(cal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(mat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(51, 255, 255), new java.awt.Color(0, 204, 204)));

        jLabel13.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 102));
        jLabel13.setText("*Precio Unitario:");

        jLabel3.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 102));
        jLabel3.setText("*Piezas:");

        jLabel16.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 102));
        jLabel16.setText("*KgPartida:");

        jLabel14.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 102));
        jLabel14.setText("Importe:");

        pUnit.setBackground(new java.awt.Color(0, 153, 153));
        pUnit.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        pUnit.setForeground(new java.awt.Color(255, 255, 255));
        pUnit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pUnit.setBorder(null);
        pUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pUnitKeyTyped(evt);
            }
        });

        pz.setBackground(new java.awt.Color(0, 153, 153));
        pz.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        pz.setForeground(new java.awt.Color(255, 255, 255));
        pz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pz.setBorder(null);
        pz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pzKeyTyped(evt);
            }
        });

        kg.setBackground(new java.awt.Color(0, 153, 153));
        kg.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        kg.setForeground(new java.awt.Color(255, 255, 255));
        kg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        kg.setBorder(null);
        kg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kgKeyTyped(evt);
            }
        });

        imp.setBackground(new java.awt.Color(0, 153, 153));
        imp.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        imp.setForeground(new java.awt.Color(255, 255, 255));
        imp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        imp.setBorder(null);
        imp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                impKeyTyped(evt);
            }
        });

        pzFinalesLb.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        pzFinalesLb.setForeground(new java.awt.Color(0, 51, 102));
        pzFinalesLb.setText("*Piezas Finales:");

        pzFinales.setBackground(new java.awt.Color(0, 153, 153));
        pzFinales.setFont(new java.awt.Font("Gulim", 1, 12)); // NOI18N
        pzFinales.setForeground(new java.awt.Color(255, 255, 255));
        pzFinales.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pzFinales.setBorder(null);
        pzFinales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pzFinalesKeyTyped(evt);
            }
        });

        checkPz.setBackground(new java.awt.Color(153, 255, 255));
        checkPz.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        checkPz.setForeground(new java.awt.Color(0, 51, 102));
        checkPz.setText("Piezas");
        checkPz.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checkPz.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkPzItemStateChanged(evt);
            }
        });

        checkKg.setBackground(new java.awt.Color(153, 255, 255));
        checkKg.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        checkKg.setForeground(new java.awt.Color(0, 51, 102));
        checkKg.setText("Kilos");
        checkKg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checkKg.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkKgItemStateChanged(evt);
            }
        });

        saveMod.setBackground(new java.awt.Color(51, 51, 51));
        saveMod.setForeground(new java.awt.Color(255, 255, 255));
        saveMod.setText("Guardar Cambios");
        saveMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveModActionPerformed(evt);
            }
        });

        checkPzFinales.setBackground(new java.awt.Color(153, 255, 255));
        checkPzFinales.setFont(new java.awt.Font("Gulim", 1, 11)); // NOI18N
        checkPzFinales.setForeground(new java.awt.Color(0, 51, 102));
        checkPzFinales.setText("Pz Finales");
        checkPzFinales.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkPzFinalesItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(pzFinalesLb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pzFinales, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(checkPzFinales)))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pz, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkPz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkKg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imp, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(saveMod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel3)
                    .addComponent(jLabel16)
                    .addComponent(jLabel14)
                    .addComponent(pUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkPz)
                            .addComponent(pzFinalesLb)
                            .addComponent(pzFinales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkKg))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(18, 26, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveMod, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkPzFinales))
                        .addContainerGap())))
        );

        sig.setBackground(new java.awt.Color(102, 204, 255));
        sig.setFont(new java.awt.Font("Gulim", 1, 9)); // NOI18N
        sig.setForeground(new java.awt.Color(0, 51, 51));
        sig.setText(">");
        sig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sigActionPerformed(evt);
            }
        });

        ant.setBackground(new java.awt.Color(102, 204, 255));
        ant.setFont(new java.awt.Font("Gulim", 1, 9)); // NOI18N
        ant.setForeground(new java.awt.Color(0, 51, 51));
        ant.setText("<");
        ant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                antActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ant, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sig, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ant, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //cambia los datos de los campos cada que avanzamos de partida
    private void sigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sigActionPerformed
        
        indicePartida++;
        sig.setSelected(false);
        if(indicePartida <= (dp.size() - 1))//hasta la penultima partida
        {
            mostrarPartida(indicePartida);
        }
        else
        {
            indicePartida=0;
            mostrarPartida(indicePartida);
        }
    }//GEN-LAST:event_sigActionPerformed

    //cambia los datos de los campos cada que retrocedemos de partida
    private void antActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_antActionPerformed
        
        indicePartida--;
        ant.setSelected(false);
        if(indicePartida >= 0)//hasta la segunda partida
        {
            mostrarPartida(indicePartida);
        }
        else
        {
            indicePartida = (dp.size() - 1);
            mostrarPartida(indicePartida);
        }
    }//GEN-LAST:event_antActionPerformed

    private void pUnitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pUnitKeyTyped
        soloFlotantes(evt, pUnit);
    }//GEN-LAST:event_pUnitKeyTyped

    private void pzKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pzKeyTyped
        limitarInsercion(10, evt, pz);
        soloEnteros(evt);
    }//GEN-LAST:event_pzKeyTyped

    private void kgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kgKeyTyped
        soloFlotantes(evt, kg);
    }//GEN-LAST:event_kgKeyTyped

    private void impKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_impKeyTyped
        soloFlotantes(evt, imp);
    }//GEN-LAST:event_impKeyTyped

    private void saveModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveModActionPerformed
        saveMod.setSelected(false);
        comprobarVacio();
        String manera = determinaManera();
        //se ingresan los cambios en la base de datos
        String sql = "update partida set desarrollo = '"+desa.getText()+"',"
                + "tipo = '"+tipo.getText()+"', "
                + "estatus = '"+sta.getSelectedItem()+"',"
                + "sello = '"+sello.getSelectedItem()+"', "
                + "medida = '"+med.getText()+"',"
                + " pigmento = '"+pig.getText()+"',"
                + "mat1 = '"+mat1.getText()+"',"
                + " mat2 = '"+mat2.getText()+"',"
                + "calibre1 = '"+cal1.getText()+"',"
                + "calibre2 = '"+cal2.getText()+"',"
                + "precioUnitaro = "+pUnit.getText()+", "
                + "piezas = "+pz.getText()+", "
                + "pzFinales = "+pzFinales.getText()+","
                + "kgPartida = "+kg.getText()+", "
                + "importe = "+imp.getText()+", "
                + "manera = '"+manera+"' "
                + "where idPar = "+dp.get(indicePartida).getId()+"";
                try 
                {
                    st = con.createStatement();
                    st.execute(sql);
                    indicadorCambios = true;//se indica que hubo cambios
                    Statement st2 = con.createStatement();
                    establecerSubtotalPedido(st2);//calcula el nuevo subtotal del pedido
                    calculaPyG();//calcula las perdidas y ganancias
                    st.close();
                    actualizarArreglo();
                    actualizarDatosParaReporte();
                    JOptionPane.showMessageDialog(null, "Se guardaron los cambios");
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                
    }//GEN-LAST:event_saveModActionPerformed

    //devuelve la manera en la que se calculo el importe
    private String determinaManera()
    {
        if(checkPz.isSelected())
        {
            return "pz";
        }else if(checkKg.isSelected())
        {
            return "kg";
        }else
        {
            return "pzF";
        }
    }
    
    private void actualizarArreglo(){
        //se consultan los datos actualizados de la base de datos
        String sql = "select * from partida where idPar = "+dp.get(indicePartida).getId()+"";
        try
        {
            st = con.createStatement();
            this.rs = st.executeQuery(sql);
            while(this.rs.next())//se llena la lista de datos de una partida especifica con los datos de la base de datos
            {
                dp.get(indicePartida).setDesa(rs.getString("desarrollo"));
                dp.get(indicePartida).setTipo(rs.getString("tipo"));
                dp.get(indicePartida).setEstatus(rs.getString("estatus"));
                dp.get(indicePartida).setSello(rs.getString("sello"));
                dp.get(indicePartida).setMedida(rs.getString("medida"));
                dp.get(indicePartida).setPig(rs.getString("pigmento"));
                dp.get(indicePartida).setMat1(rs.getString("mat1"));
                dp.get(indicePartida).setMat2(rs.getString("mat2"));
                dp.get(indicePartida).setCal1(rs.getString("calibre1"));
                dp.get(indicePartida).setCal2(rs.getString("calibre2"));
                dp.get(indicePartida).setPuni(rs.getString("precioUnitaro"));
                dp.get(indicePartida).setPzs(rs.getString("piezas"));
                dp.get(indicePartida).setKgs(rs.getString("kgPartida"));
                dp.get(indicePartida).setImporte(rs.getString("importe"));
                dp.get(indicePartida).setPzFinales(rs.getString("pzFinales"));
            }
            
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
      }
    
    private void actualizarDatosParaReporte(){
        //llena los datos del reporte con los de la lista de partidas
        for(int i = 0; i < datosReporte.size(); i++){
            datosReporte.get(i).setImporte(dp.get(i).getImporte());
            datosReporte.get(i).setpUni(dp.get(i).getPuni());
            datosReporte.get(i).setPiezas(dp.get(i).getPzs());
            datosReporte.get(i).setMedida(dp.get(i).getMedida());
            datosReporte.get(i).setCalibre(dp.get(i).getCal1() + "-" + dp.get(i).getCal2());
            datosReporte.get(i).setMaterial(dp.get(i).getMat1() + "-" + dp.get(i).getMat2());
            datosReporte.get(i).setSello(dp.get(i).getSello());
            datosReporte.get(i).setTipo(dp.get(i).getTipo());
            datosReporte.get(i).setImporte(dp.get(i).getImporte());
            //manda los datos del final del reporte con las variables globales
            datosReporte.get(i).setSub(String.valueOf(this.sub));
            datosReporte.get(i).setIva(String.valueOf(this.iva));
            datosReporte.get(i).setTotal(String.valueOf(this.total));
            datosReporte.get(i).setResto(String.valueOf(this.resto));
        }
        
    }
    
    private void calculaPyG()
    {
        Statement st2;
        ResultSet rs2;
        Statement st3;
        ResultSet rs3;
        float subtotal = 0f, costoTotal = 0f, descuento = 0f;
        float PyG = 0;
        String sql = "select fTermino from pedido where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
        String sql2 =  "";
        try
        {
            st3 = con.createStatement();
            rs3 = st3.executeQuery(sql);
            while(rs3.next())
            {
                if(rs3.getString("fTermino").equals("2018-01-01") == false)
                {
                    sql2 = "select subtotal, costoTotal, descuento from pedido where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
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
                    
                    float kgFnPe = calculaKgFinalesPedido();
                    float gf = calculaGfKg();
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
                    
        sql2 = "update pedido set perdidasYGanancias = "+PyG+" where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
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
    
    private float calculaGfKg()
    {
        Statement st2;
        ResultSet rs2;
        float gfkg = 0f, gfr = 0f, sumatoriaRango = 0f;
        String sql = "select fTermino from pedido where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
        try
        {
            st2 = con.createStatement();
            rs2 = st2.executeQuery(sql);
            while(rs2.next())
            {
                if(rs.getString("fTermino").equals("2018-01-01") == false)
                {
                    sql = "select gastosFijos,kgFinalesRango from pedido where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+" and gastosFijos is not null and kgFinalesRango is not null";
                    try
                    {
                        st = con.createStatement();
                        this.rs = st.executeQuery(sql);
                        while(this.rs.next())
                        {
                            gfr = Float.parseFloat(this.rs.getString("gastosFijos"));
                            sumatoriaRango = Float.parseFloat(this.rs.getString("kgFinalesRango"));
                            if(sumatoriaRango != 0)
                                gfkg = gfr / sumatoriaRango;
                        }
                        this.rs.close();
                        st.close();
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
        
        if(gfkg != 0)
            return gfkg;
        else
            return 0;
    }
    
    private float calculaKgFinalesPedido()
    {
        //entra al pedido
        float sumatoria = 0f;
        String sql = "select fTermino from pedido where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                if(rs.getString("fTermino").equals("2018-01-01") == false)
                {
                    String sql2 = "select idPar from partida where folio_fk = "+dp.get(indicePartida).getFolio().replace("A", "")+"";//obtiene el folio de cada partida
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
            }
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return sumatoria;
    }
    
    //habilita el boton el boton de guardado cuando ya se ingresaron los datos necesarios
    public void comprobarCondPed(){
        
        if(!(folio.getText().equals("")) && (checkPz.isSelected() || checkKg.isSelected() || checkPzFinales.isSelected())){
            saveMod.setEnabled(true);
        }
        else
        {
             saveMod.setEnabled(false);
        }
    }
    
    private void checkPzItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkPzItemStateChanged

        if(checkPz.isSelected())
        {
            //habilita todo lo relacionado con pz
            checkPz.setSelected(true);
            pz.setEnabled(true);
            //deshabilita todo lo que no esta relacionado con pz
            checkKg.setSelected(false);
            kg.setEnabled(false);
            pzFinales.setEnabled(false);
            checkPzFinales.setSelected(false);
            indicadorParaImporte = 1;
            calcularEnCampoPU(pz);
        }
        else
        {
            pz.setEnabled(false);
        }
        comprobarCondPed();
    }//GEN-LAST:event_checkPzItemStateChanged

    
    private void checkKgItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkKgItemStateChanged

        if(checkKg.isSelected())
        {
            //habilita todo lo relacionado con kg
            checkKg.setSelected(true);
            kg.setEnabled(true);
            //deshabilita todo lo no relacionado con kg
            checkPz.setSelected(false);
            pz.setEnabled(false);
            pzFinales.setEnabled(false);
            checkPzFinales.setSelected(false);
            indicadorParaImporte = 2;
            calcularEnCampoPU(pz);
        }
        else
        {
            kg.setEnabled(false);
        }
        comprobarCondPed();
    }//GEN-LAST:event_checkKgItemStateChanged

    private void pzFinalesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pzFinalesKeyTyped
        limitarInsercion(10, evt, pz);
        soloEnteros(evt);
    }//GEN-LAST:event_pzFinalesKeyTyped

    private void desaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_desaKeyTyped
        limitarInsercion(10, evt, desa);
        soloEnteros(evt);
    }//GEN-LAST:event_desaKeyTyped

    private void tipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tipoKeyTyped
        limitarInsercion(40, evt, tipo);
    }//GEN-LAST:event_tipoKeyTyped

    private void medKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_medKeyTyped
        limitarInsercion(15, evt, med);
    }//GEN-LAST:event_medKeyTyped

    private void pigKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pigKeyTyped
        limitarInsercion(40, evt, pig);
    }//GEN-LAST:event_pigKeyTyped

    private void cal1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cal1KeyTyped
        limitarInsercion(5, evt, cal1);
        soloEnteros(evt);
    }//GEN-LAST:event_cal1KeyTyped

    private void cal2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cal2KeyTyped
        limitarInsercion(5, evt, cal2);
        soloEnteros(evt);
    }//GEN-LAST:event_cal2KeyTyped

    private void mat1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mat1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_mat1KeyTyped

    private void mat2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mat2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_mat2KeyTyped

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //si hubo cambios en las partidas se vuelven a llenar las tablas de la pantalla de visualizacion
        if(indicadorCambios == true){
            vis.llenarTablaPedido();
            vis.llenarTablaPartidas();
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        vis.setEnabled(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vis.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void checkPzFinalesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkPzFinalesItemStateChanged
        
        if(checkPzFinales.isSelected())
        {
            //se habilita todo lo relacionado con piezas finales
            pzFinales.setEnabled(true);
            checkPzFinales.setSelected(true);
            //se deshabilita todo lo no relacionado con piezas finales
            checkPz.setSelected(false);
            pz.setEnabled(false);
            checkKg.setSelected(false);
            kg.setEnabled(false);
            indicadorParaImporte = 3;
            calcularEnCampoPU(pz);
        }
        else
        {
             pzFinales.setEnabled(false);
        }
        comprobarCondPed();
    }//GEN-LAST:event_checkPzFinalesItemStateChanged

    private void onChangePU(){
        pUnit.getDocument().addDocumentListener(new DocumentListener() {               
            @Override
            public void insertUpdate(DocumentEvent e) {              
                calcularEnCampoPU(pz);
                
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                calcularEnCampoPU(pz);
               
            }
            @Override
            public void changedUpdate(DocumentEvent e) {             
            }
        });
    }
    
    
    private void onChangeKilos(){
        kg.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularEnCampoPU(pz);
            }

            @Override
            public void removeUpdate(DocumentEvent e) { 
                calcularEnCampoPU(pz);
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
                calcularEnCampoPU(pz);
            }

            @Override
            public void removeUpdate(DocumentEvent e) { 
                
                calcularEnCampoPU(pz);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
               
            }
        });
    }
    
    private void onChangePzFin(){
        pzFinales.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularEnCampoPU(pz);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calcularEnCampoPU(pz);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }
    
    private void limitarInsercion(int tamSQL, KeyEvent evt, JTextField campo){
        int tamCampo = campo.getText().length() + 1;

        if(tamCampo > tamSQL){
            evt.consume();
        }
    }
    
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
    
    //calcula el importe dependiendo de que checkboz se seleccione
    private void calcularEnCampoPU(JTextField pzLocal){
        float pUni;
        int pzs;
        float kgPartida;
        float total;
        
        if(indicadorParaImporte == 1)//hace calculos si se seleccionaron las piezas
        {
            if(pUnit.getText().equals("") || pUnit.getText().equals("."))
            {
                pUni = 0;
            }
            else
            {
                pUni = Float.parseFloat(pUnit.getText());
            }

            if(pzLocal.getText().equals("") || pzLocal.getText().equals("")){
                pzs = 0;
            }
            else
            {
                pzs = Integer.parseInt(pzLocal.getText());
            }
            total = pUni * pzs;
            imp.setText(String.valueOf(total));
        }
        else if(indicadorParaImporte == 2)//hace calculos si se seleccionaron los kg
        {
                    if(pUnit.getText().equals("") || pUnit.getText().equals("."))
                    {
                        pUni = 0;
                    }
                    else
                    {
                        pUni = Float.parseFloat(pUnit.getText());
                    }

                    if(kg.getText().equals("") || kg.getText().equals("."))
                    {
                        kgPartida = 0;
                    }
                    else
                    {
                        kgPartida = Float.parseFloat(kg.getText());
                    }
                    total = pUni * kgPartida;
                    imp.setText(String.valueOf(total));
        }
        else if(indicadorParaImporte == 3)//hace calculos si se seleccionaron las piezas finales
        {
                    if(pUnit.getText().equals("") || pUnit.getText().equals("."))
                    {
                        pUni = 0;
                    }
                    else
                    {
                        pUni = Float.parseFloat(pUnit.getText());
                    }

                    if(pzFinales.getText().equals("") || pzFinales.getText().equals(""))
                    {
                        pzs = 0;
                    }
                    else
                    {
                        pzs = Integer.parseInt(pzFinales.getText());
                    }
                    total = pUni * pzs;
                    imp.setText(String.valueOf(total));
        }  
    }
    
    private void comprobarVacio(){
        
        if(desa.getText().equals("")){
            desa.setText("0");
        }
        if(pUnit.getText().equals("") || pUnit.getText().equals(".")){
            pUnit.setText("0.0");
        }
        if(pz.getText().equals("")){
            pz.setText("0");
        }
        if(pzFinales.getText().equals("")){
            pzFinales.setText("0");
        }
        if(imp.getText().equals("") || imp.getText().equals(".")){
            imp.setText("0.0");
        }
        if(kg.getText().equals("") || kg.getText().equals(".")){
            kg.setText("0.0");
        }
    }
    
    //Calcular monto total del pedido a base de los importes de todas las partidas
    private void establecerSubtotalPedido(Statement st){
        
        String sql = "select importe from partida where folio_fk = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
        float subtotalVar = 0;
        try
        {
            this.rs = st.executeQuery(sql);
            while(this.rs.next())
            {
                //Aqui se empiezan a acumular los importes de las partidas
                subtotalVar = subtotalVar + Float.parseFloat(this.rs.getString("importe"));
            } 
            actualizarSubtotalPedido(subtotalVar, st);//Update al campo subtotal para ingresar el nuevo monto
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al buscar la partida (sub)" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //obtiene el porcentaje de iva del pedido
    private float obtenerIva()
    {
        Statement st2;
        ResultSet rs2;
        int ivaI = 0;
        float ivaF = 0f;
        String sql = "select porcentajeIVA from pedido where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";//se consulta el iva en la base
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
    
    //Se actualiza el subtotal de pedido
    private void actualizarSubtotalPedido(float sub, Statement st){
        
        float subGrab = sub;
        float anti  = 0f;
        float descuento = 0f;
        
        //Consulta para obtener los valores de: grabados, anticipo y descuento. Se realizaran operaciones
        String sql = "select grabados, anticipo, descuento from pedido where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
        
        try 
        {
           
            this.rs = st.executeQuery(sql);
            while(this.rs.next())
            {
                //Se le suma el coste de grabados al subtotal
                subGrab = subGrab + Float.parseFloat(this.rs.getString("grabados"));
                //System.out.println("Grabados: "+this.rs.getString("grabados"));
                //Se guarda el valor de anticipo para usu posterior
                anti = Float.parseFloat(this.rs.getString("anticipo"));
                //Se guarda el vlor de descuento para uso posterior
                descuento = Float.parseFloat(this.rs.getString("descuento"));
            }
            this.rs.close();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de costos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        //Cuando ya se sumaron los grabados y el subtotal ahora se guarda en la base de datos
        sql = "update pedido set subtotal = "+subGrab+" where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
        this.sub = subGrab;//Global
        try 
        {
            st.execute(sql);
            //Ahora paso por parametros el subtotal con grabados, anticipo y descuento
            calcularCostosDeSub(subGrab, anti, descuento, st);
            st.close();
            
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar el subtotal", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    //Aqui se reciben todos los valores necesarios para realizar los calculos y actualizar la base de datos
    private void calcularCostosDeSub(float sub, float anticipo, float descuento, Statement st){
        
        float total = 0f;
        float iva = obtenerIva();//se obtiene el porcentaje de iva de ese pedido especificamente
        float subIva = 0f;
        float rest = 0f;
        
        subIva = sub * iva;//Se obtiene el iva del subtotal
        this.iva = subIva;//Global
        total = sub + subIva;//Se suma el iva y el subtotal para obtener el total
        this.total = total;//Global
        
        rest = total - anticipo;//Se le resta el anticipo al total para obtener el resto
        rest = rest - descuento;//Se le resta el descuento al resto
        this.resto = rest;//Global
        
        String sql= "update pedido set total = "+total+", resto = "+rest+" where folio = "+dp.get(indicePartida).getFolio().replace("A", "")+"";
        
        try 
        {
            st.execute(sql);
        } catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error al actualizar el total y resto", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton ant;
    private javax.swing.JTextField cal1;
    private javax.swing.JTextField cal2;
    private javax.swing.JCheckBox checkKg;
    private javax.swing.JCheckBox checkPz;
    private javax.swing.JCheckBox checkPzFinales;
    private javax.swing.JTextField cosMat;
    private javax.swing.JTextField cosPart;
    private javax.swing.JTextField de;
    private javax.swing.JTextField desa;
    private javax.swing.JTextField folio;
    private javax.swing.JTextField hj;
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
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField kg;
    private javax.swing.JTextField kgDes;
    private javax.swing.JTextField mat1;
    private javax.swing.JTextField mat2;
    private javax.swing.JTextField med;
    private javax.swing.JTextField modoPart;
    private javax.swing.JTextField pUnit;
    private javax.swing.JTextField pig;
    private javax.swing.JTextField porDes;
    private javax.swing.JTextField pz;
    private javax.swing.JTextField pzFinales;
    private javax.swing.JLabel pzFinalesLb;
    private javax.swing.JButton saveMod;
    private javax.swing.JComboBox sello;
    private javax.swing.JToggleButton sig;
    private javax.swing.JComboBox sta;
    private javax.swing.JTextField tipo;
    // End of variables declaration//GEN-END:variables
}
