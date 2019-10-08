
package utilidades;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MiRender extends DefaultTableCellRenderer{
    
    @Override
    public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column)
   {
      super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      if (isSelected)
      {
         this.setOpaque(true);
         this.setBackground(Color.BLACK);
         this.setForeground(Color.white);
      } else {
         this.setOpaque(true);
         this.setBackground(Color.cyan);
         this.setForeground(Color.BLACK);
      }

      return this;
   }
  
    
}
