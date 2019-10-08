//Clase de ayuda para DatosDataSource (Rellenado de los campos del reporte de pedido)
package report;

 public class PedidoDatos{
     
     /*Datos Pedido*/
    public String folio;
    public String fecha;
    public String impresion;
    public String autorizo;
    public String grabados;
    public String sub;
    public String iva;
    public String total;
    public String anticipo;
    public String descuento;
    public String resto;
    
    /*Datos Cliente*/
    public String dirEnt;
    public String tel;
    public String cel;
    public String ciudad;
    public String agente;
    
    /*Datos Partida*/
    public String piezas;
    public String material;
    public String medida;
    public String calibre;
    public String sello;
    public String tipo;
    public String pigmento;
    public String pUni;
    public String importe;
    /*Cara1*/
    public String c1t1;
    public String c1t2;
    public String c1t3;
    public String c1t4;
    public String c1t5;
    public String c1t6; 
    /*Cara2*/
    public String c2t1;
    public String c2t2;
    public String c2t3;
    public String c2t4;
    public String c2t5;
    public String c2t6; 
    
    public String manera;
    public String pzFinales;

    

     public PedidoDatos(String folio, String fecha, String impresion, String autorizo, String grabados,
                        String sub, String iva, String total, String anticipo, String dirEnt, String tel, 
                        String cel, String ciudad, String agente, String piezas, String material, String medida, 
                        String calibre, String sello, String tipo, String pigmento,
                        String pUni, String importe, String c1t1, String c1t2, String c1t3, String c1t4, String c1t5,
                        String c1t6, String c2t1, String c2t2, String c2t3, String c2t4, String c2t5, String c2t6, 
                        String descuento, String resto, String manera, String pzFinales){
         
         this.folio = folio;
         this.fecha = fecha;
         this.impresion = impresion;
         this.autorizo = autorizo;
         this.grabados = grabados;
         this.sub = sub;
         this.iva = iva;
         this.total = total;
         this.anticipo = anticipo;     
         this.dirEnt = dirEnt;
         this.tel = tel;
         this.cel = cel;
         this.ciudad = ciudad;
         this.agente = agente;
         this.piezas = piezas;
         this.material = material;
         this.medida = medida;
         this.calibre = calibre;
         this.sello = sello;      
         this.tipo = tipo;
         this.pigmento = pigmento;
         this.pUni = pUni;
         this.importe = importe;
         this.c1t1 = c1t1;
         this.c1t2 = c1t2;
         this.c1t3 = c1t3;        
         this.c1t4 = c1t4;
         this.c1t5 = c1t5;
         this.c1t6 = c1t6;
         this.c2t1 = c2t1;
         this.c2t2 = c2t2;
         this.c2t3 = c2t3;
         this.c2t4 = c2t4;
         this.c2t5 = c2t5;
         this.c2t6 = c2t6;
         this.descuento = descuento;
         this.resto = resto;
         this.manera = manera;
         this.pzFinales = pzFinales;
     }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImpresion() {
        return impresion;
    }

    public void setImpresion(String impresion) {
        this.impresion = impresion;
    }

    public String getAutorizo() {
        return autorizo;
    }

    public void setAutorizo(String autorizo) {
        this.autorizo = autorizo;
    }

    public String getGrabados() {
        return grabados;
    }

    public void setGrabados(String grabados) {
        this.grabados = grabados;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(String anticipo) {
        this.anticipo = anticipo;
    }

    public String getDirEnt() {
        return dirEnt;
    }

    public void setDirEnt(String dirEnt) {
        this.dirEnt = dirEnt;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getPiezas() {
        return piezas;
    }

    public void setPiezas(String piezas) {
        this.piezas = piezas;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getSello() {
        return sello;
    }

    public void setSello(String sello) {
        this.sello = sello;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPigmento() {
        return pigmento;
    }

    public void setPigmento(String pigmento) {
        this.pigmento = pigmento;
    }

    public String getpUni() {
        return pUni;
    }

    public void setpUni(String pUni) {
        this.pUni = pUni;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getC1t1() {
        return c1t1;
    }

    public void setC1t1(String c1t1) {
        this.c1t1 = c1t1;
    }

    public String getC1t2() {
        return c1t2;
    }

    public void setC1t2(String c1t2) {
        this.c1t2 = c1t2;
    }

    public String getC1t3() {
        return c1t3;
    }

    public void setC1t3(String c1t3) {
        this.c1t3 = c1t3;
    }

    public String getC1t4() {
        return c1t4;
    }

    public void setC1t4(String c1t4) {
        this.c1t4 = c1t4;
    }

    public String getC1t5() {
        return c1t5;
    }

    public void setC1t5(String c1t5) {
        this.c1t5 = c1t5;
    }

    public String getC1t6() {
        return c1t6;
    }

    public void setC1t6(String c1t6) {
        this.c1t6 = c1t6;
    }

    public String getC2t1() {
        return c2t1;
    }

    public void setC2t1(String c2t1) {
        this.c2t1 = c2t1;
    }

    public String getC2t2() {
        return c2t2;
    }

    public void setC2t2(String c2t2) {
        this.c2t2 = c2t2;
    }

    public String getC2t3() {
        return c2t3;
    }

    public void setC2t3(String c2t3) {
        this.c2t3 = c2t3;
    }

    public String getC2t4() {
        return c2t4;
    }

    public void setC2t4(String c2t4) {
        this.c2t4 = c2t4;
    }

    public String getC2t5() {
        return c2t5;
    }

    public void setC2t5(String c2t5) {
        this.c2t5 = c2t5;
    }

    public String getC2t6() {
        return c2t6;
    }

    public void setC2t6(String c2t6) {
        this.c2t6 = c2t6;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getResto() {
        return resto;
    }

    public void setResto(String resto) {
        this.resto = resto;
    }
     
     public String getManera() {
        return manera;
    }

    public void setManera(String manera) {
        this.manera = manera;
    }

    public String getPzFinales() {
        return pzFinales;
    }

    public void setPzFinales(String pzFinales) {
        this.pzFinales = pzFinales;
    }
}
