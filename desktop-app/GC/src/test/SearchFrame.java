/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on Apr 21, 2012, 2:14:12 PM
 */
package test;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Bamburuncing
 */
public class SearchFrame extends javax.swing.JFrame {

    /** Creates new form MainFrame */
    private String folder;
    private static DefaultTableModel model;
        
    public SearchFrame() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width-getWidth())/2;
        int y = (dim.height-getHeight())/2;
        setLocation(x, y);
        
        model = new DefaultTableModel();
        
        tabelData.setModel(model);
        
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Gambar");
        model.addColumn("Keterangan");
        model.addColumn("Tgl Upload");
        model.addColumn("Lokasi");
        model.addColumn("Latitude");
        model.addColumn("Longitude");
        model.addColumn("Status");
        model.addColumn("Vote");
        
        //DetailFrame.getPanel().
                
    }
    
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        
        String i = inputSearch.getText();
        
        try{
            Connection c = KoneksiDatabase.getKoneksi();
            Statement s = c.createStatement();
            
            String sql = "SELECT * FROM garbage WHERE lokasi = '" + i + "'";
            PreparedStatement p = c.prepareStatement(sql);           
            
            ResultSet q = s.executeQuery(sql);
            
            while(q.next()){
                Object[] o = new Object[10];
                
                
                o[0] = q.getString("id");
                o[1] = q.getString("nama");
                o[2] = q.getBlob("gambar");
                o[3] = q.getString("keterangan");
                o[4] = q.getDate("tanggal");
                o[5] = q.getString("lokasi");
                o[6] = q.getDouble("lat");
                o[7] = q.getDouble("long");
                o[8] = q.getInt("status");
                o[9] = q.getInt("vote");
                               
                model.addRow(o);
            }
            q.close();
            s.close();
            
        }catch(SQLException e){
            System.out.println("Terjadi Error Load");
        }
        
        
    }

    public static JTable getTabelData() {
        return tabelData;
    }

    public static DefaultTableModel getModel() {
        return model;
    }
    
    
    public void dataUpdate() throws Exception{
        int i = SearchFrame.getTabelData().getSelectedRow();
        if(i == -1){
        //tidak ada baris yang diseleksi
            return;
        }
    
        //ambil nim yg diseleksi
        /*
        String perintah = "select * from data_pegawai where id = '" + id + "'";
        //= perintah.;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        */
        
        Connection conn = KoneksiDatabase.getKoneksi();
        
        byte[] data = getBLOB(i, conn);
        
        
    }
    
    public static byte[] getBLOB(int id, Connection conn) throws Exception {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String query = "SELECT gambar FROM garbage WHERE id = ?";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            Blob blob = rs.getBlob("photo");
            //DetailFrame.getPanel().set
            
        // materialize BLOB onto client
            return blob.getBytes(1, (int) blob.length());
        }finally {
            rs.close();
            pstmt.close();
            conn.close();
        }
    }


    
    
    public void loadDataUpdate(){
        int i = SearchFrame.getTabelData().getSelectedRow();
        if(i == -1){
            //tak ada baris terseleksi
            return;
        }
    
        String nami = (String) SearchFrame.getModel().getValueAt(i, 1);
        DetailFrame.getNama().setText(nami);
    
        SimpleDateFormat tanggal = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        Date date = new Date();
        date = (Date) SearchFrame.getModel().getValueAt(i, 4);
        DetailFrame.getDate().setText(tanggal.format(date)+"   ");
        
        String lokasi = (String) SearchFrame.getModel().getValueAt(i, 5);
        DetailFrame.getLokasi().setText(lokasi);
        
        Double latitude = (Double) SearchFrame.getModel().getValueAt(i, 6);
        String lat = Double.toString(latitude);
        DetailFrame.getLatitude().setText(lat);
        
        Double longitude = (Double) SearchFrame.getModel().getValueAt(i, 7);
        String lg = Double.toString(longitude);
        DetailFrame.getLongitude().setText(lg);
        
        Integer status = (Integer) SearchFrame.getModel().getValueAt(i, 8);
        String st = Integer.toString(status);
        DetailFrame.getStatus().setText(st);
        
        Integer vote = (Integer) SearchFrame.getModel().getValueAt(i, 9);
        String vo = Integer.toString(vote);
        DetailFrame.getVote().setText(vo);
        
        String ket = (String) SearchFrame.getModel().getValueAt(i, 3);
        DetailFrame.getKeterangan().setText(ket);
        
        
        
    }
    
    public class NIOCopier {

        public NIOCopier(String asal, String tujuan) throws IOException {
            FileInputStream inFile = new FileInputStream(asal);
            FileOutputStream outFile = new FileOutputStream(tujuan);
            FileChannel inChannel = inFile.getChannel();
            FileChannel outChannel = outFile.getChannel();
            for (ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                    inChannel.read(buffer) != -1;
                    buffer.clear()) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    outChannel.write(buffer);
                }
            }
            inChannel.close();
            outChannel.close();
        }
    }
//Class untuk menampilkan gambar

    public class Painter extends Canvas {

        Image image;

        public void setImage(String file) {
            URL url = null;
            try {
                url = new File(file).toURI().toURL();
            } catch (Exception ex) {
                cetak(ex.toString());
            }
            image = getToolkit().getImage(url);
            repaint();
        }
        public void setImageIcon(ImageIcon file) {
            image = file.getImage();
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            double d = image.getHeight(this) / this.getHeight();
            double w = image.getWidth(this) / d;
            double x = this.getWidth() / 2 - w / 2;
            g.drawImage(image, (int) x, 0, (int) (w), this.getHeight(), this);
        }
    }
    
    private String gambar(String id) {
        return folder + File.separator + id.trim() + ".jpg";
    }
    
    private void cetak(String str) {
        System.out.println(str);
    }
    
    

    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSpinField1 = new com.toedter.components.JSpinField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        inputSearch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelData = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jLabel3.setText("jLabel3");

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Garbage Collector Application\n");
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/images/world.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel2.setText("Garbage Collector");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)))
        );

        jLabel4.setText("Enter your city");

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tabelData.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabelData);

        jButton2.setText("Detail");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/images/home.png"))); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/images/input2.png"))); // NOI18N
        jButton4.setMaximumSize(new java.awt.Dimension(40, 40));
        jButton4.setMinimumSize(new java.awt.Dimension(40, 40));
        jButton4.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(349, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(inputSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(168, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    loadData();
    //dataUpdate();
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // TODO add your handling code here:
            DetailFrame f = new DetailFrame();
            f.setVisible(true);
            loadDataUpdate();
        //dataUpdate();
}//GEN-LAST:event_jButton2ActionPerformed
        catch (Exception ex) {
            Logger.getLogger(SearchFrame.class.getName()).log(Level.SEVERE, null, ex);
        }}
private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    // TODO add your handling code here:
    HomeFrame f = new HomeFrame();
    f.setVisible(true);
    this.hide();
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    // TODO add your handling code here:
    MainFrame f = new MainFrame();
    f.setVisible(true);
    this.hide();
}//GEN-LAST:event_jButton4ActionPerformed


    /**
     * @param args the command line arguments
     */

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField inputSearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.components.JSpinField jSpinField1;
    private javax.swing.JTextField jTextField1;
    private static javax.swing.JTable tabelData;
    // End of variables declaration//GEN-END:variables
}
