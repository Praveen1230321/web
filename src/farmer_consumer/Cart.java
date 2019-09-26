/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package farmer_consumer;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author archer
 */
public class Cart extends javax.swing.JFrame {

    /** Creates new form Cart */
    private final dbConnect db = new dbConnect();
    private static Session sess = null;
    public Cart(Session ses) {
        sess=ses;
        db.connect();
        initComponents();
        showCart();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        CartTable = new javax.swing.JTable();
        updateCart = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        placeOrder = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        finaltotal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("My Cart");

        CartTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cart id", "Farmer", "Product", "Quantity", "Price", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(CartTable);

        updateCart.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        updateCart.setText("Update Cart");
        updateCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCartActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("CART");

        placeOrder.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        placeOrder.setText("Place Order");
        placeOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placeOrderActionPerformed(evt);
            }
        });

        jLabel2.setText("Quantity can be updated in Table.");

        finaltotal.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        finaltotal.setText("Final Total:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(395, 395, 395)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateCart)
                .addGap(41, 41, 41)
                .addComponent(finaltotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addComponent(placeOrder)
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateCart)
                    .addComponent(placeOrder)
                    .addComponent(jLabel2)
                    .addComponent(finaltotal))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCartActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)CartTable.getModel();
        for(int i=0; i<model.getRowCount(); i++){
            int cid=(Integer)model.getValueAt(i,0);
            try {
                float qtt = (float)model.getValueAt(i,3);
                if(qtt<0){
                    throw new NullPointerException();
                }
                else if(qtt==0){
                    db.prestmt = db.con.prepareStatement("delete from cart where cart_id=?");
                    db.prestmt.setInt(1, cid);
                    db.rs = db.prestmt.executeQuery();
                }
                else{
                    db.prestmt = db.con.prepareStatement("update cart set quantity=? where cart_id=?");
                    db.prestmt.setFloat(1, qtt);
                    db.prestmt.setInt(2, cid);
                    db.rs = db.prestmt.executeQuery();
                }
            }
            catch (NullPointerException e){
                JOptionPane.showMessageDialog(this, "Press Enter on entering correct Quantity.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        showCart();
    }//GEN-LAST:event_updateCartActionPerformed

    private void placeOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placeOrderActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)CartTable.getModel();
        Boolean flag=false;
        for(int i=0; i<model.getRowCount(); i++){
            int cid=(Integer)model.getValueAt(i,0);
            try {
                flag=true;
                db.prestmt = db.con.prepareStatement("insert into orders(customer_id,farmer_id,stock_id,quantity,status) select customer_id,farmer_id,stock_id,quantity,'pending' from cart where cart_id=?");
                db.prestmt.setInt(1, cid);
                db.rs = db.prestmt.executeQuery();
                db.prestmt = db.con.prepareStatement("delete from cart where cart_id=?");
                db.prestmt.setInt(1, cid);
                db.rs = db.prestmt.executeQuery();
            }
            catch (NullPointerException e){
                JOptionPane.showMessageDialog(this, "Press Enter on entering correct Quantity.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        if(flag)
            JOptionPane.showMessageDialog(this, "Order has been placed.");
        showCart();
    }//GEN-LAST:event_placeOrderActionPerformed

    private void showCart(){
        float finTot=0;
        try {
            db.prestmt = db.con.prepareStatement("select c.cart_id,u.username,s.product_name,c.quantity,s.price from cart c,users u,stock s,farmer f where c.customer_id=? and f.farmer_id=c.farmer_id and u.uid=f.uid and s.stock_id=c.stock_id");
            db.prestmt.setInt(1,sess.typeid);
            db.rs = db.prestmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel)CartTable.getModel();
            model.setRowCount(0);
            while(db.rs.next()){
                float qtt = db.rs.getFloat("quantity");
                float prc = db.rs.getFloat("price");
                model.addRow(new Object[]{db.rs.getInt("cart_id"),db.rs.getString("username"),db.rs.getString("product_name"),qtt,prc,qtt*prc});
                finTot+=qtt*prc;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        finaltotal.setText("Final Total: "+finTot+"/-");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cart(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CartTable;
    private javax.swing.JLabel finaltotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton placeOrder;
    private javax.swing.JButton updateCart;
    // End of variables declaration//GEN-END:variables

}
