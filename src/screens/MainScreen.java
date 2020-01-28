package screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.swing.ListModel;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import java.awt.print.*;
import certidaoautomatica.ListModelArrayList;
import certidaoautomatica.Process;

public class MainScreen extends javax.swing.JFrame implements ActionListener {

    private String dest = "etc/CERTIDAO-2.pdf";
    private Calendar novo = Calendar.getInstance(TimeZone.getTimeZone("America/Brasilia"));
    private String year = novo.getTime().toString().substring(novo.getTime().toString().length() - 4, novo.getTime().toString().length());
    private ListModelArrayList lm = new ListModelArrayList();
    private List<XWPFDocument> certidoes = new ArrayList<>();
    private File certidao = new File("etc/CERTIDÃO.docx");
    private int keyNum = 1;
    private int bckSpace = 1;
    private String buffer = "";

    public MainScreen() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setComboBoxItems();
        btnClose.addActionListener(this);
        btnInclude.addActionListener(this);
        btnDelete.addActionListener(this);
        btnPrint.addActionListener(this);
        cbBox.addActionListener(this);
        txtFieldProcess.setText(year + getCircunscricao(1) + "10000000");
        processList.setModel((ListModel) lm);
        txtFieldProcess.setEditable(false);
    }

    public void loadFile(Process process) {

        XWPFDocument doc;
        try {
            doc = new XWPFDocument(OPCPackage.open(certidao.getCanonicalPath()));
            List<XWPFParagraph> headerParagraphs = doc.getHeaderList().get(0).getParagraphs();
            for (int i = 0; i < headerParagraphs.size(); i++) {
                if (!headerParagraphs.get(i).isEmpty()) {
                    System.out.println(headerParagraphs.get(i).getText());
                    if (headerParagraphs.get(i).getText().contains("[CIRCUNSCRIÇÃO]")) {
                        String returnV = headerParagraphs.get(i).getText().replace("[CIRCUNSCRIÇÃO]", getCircunscricao(3));
                        System.out.println(returnV);
                        XWPFParagraph current = headerParagraphs.get(i);
                        XWPFRun run = current.getRuns().get(0);
                        current.getRuns().get(1).setText("", 0);
                        run.setText(returnV, 0);
                    }
                }
            }

            List<XWPFParagraph> docParagraphs = doc.getParagraphs();
            for (int i = 0; i < docParagraphs.size(); i++) {
                if (!docParagraphs.get(i).isEmpty()) {
                    System.out.println(docParagraphs.get(i).getText());
                    if (docParagraphs.get(i).getText().contains("[CIRCUNSCRIÇÃO]")) {
                        String returnV = docParagraphs.get(i).getText().replace("[CIRCUNSCRIÇÃO]", getCircunscricao(2));
                        System.out.println(returnV);
                        XWPFParagraph current = docParagraphs.get(i);
                        XWPFRun run = current.getRuns().get(0);
                        current.getRuns().get(1).setText("", 0);
                        run.setText(returnV, 0);
                    }
                    if (docParagraphs.get(i).getText().contains("[PROCESS_NUMBER]")) {
                        String returnV = docParagraphs.get(i).getText().replace("[PROCESS_NUMBER]", process.getNumber());
                        System.out.println(returnV);
                        XWPFParagraph current = docParagraphs.get(i);
                        XWPFRun run = current.getRuns().get(0);
                        current.getRuns().get(1).setText("", 0);
                        run.setText(returnV, 0);
                    }
                }
            }
            write(doc);
            certidoes.add(doc);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void write(XWPFDocument doc) {
        String folder = "C:/temp/";
        String fileName = "saida.docx";
        File f = new File(folder);
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(new File(folder + fileName));
            doc.write(out);
            out.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro", "Impossível criar as certidões.", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setComboBoxItems() {
        cbBox.setEditable(false);
        cbBox.removeAllItems();
        cbBox.addItem("01 - MILTON SEBASTIÃO BARBOSA");
        cbBox.addItem("01 - JOAQUIM NETO");
        cbBox.addItem("01 - LEAL FAGUNDES");
        cbBox.addItem("02 - BRAZLÂNDIA");
        cbBox.addItem("03 - CEILÂNDIA");
        cbBox.addItem("04 - GAMA");
        cbBox.addItem("05 - PLANALTINA");
        cbBox.addItem("06 - SOBRADINHO");
        cbBox.addItem("07 - TAGUATINGA");
        cbBox.addItem("08 - PARANOÁ");
        cbBox.addItem("09 - SAMAMBAIA");
        cbBox.addItem("10 - SANTA MARIA");
        cbBox.addItem("11 - NÚCLEO BANDEIRANTE");
        cbBox.addItem("12 - SÃO SEBASTIÃO");
        cbBox.addItem("13 - RIACHO FUNDO");
        cbBox.addItem("14 - GUARÁ");
        cbBox.addItem("15 - RECANTO DAS EMAS");
        cbBox.addItem("16 - ÁGUAS CLARAS");
    }

    public String getCircunscricao(int prop) {
        String splitC[] = cbBox.getSelectedItem().toString().split("-");
        switch (prop) {
            case 1:
                return splitC[0].replaceFirst(" ", "");
            case 2:
                if (prop == 2 && splitC[0].equals("01")) {
                    return "BRASÍLIA";
                }
            case 3:
                return splitC[splitC.length - 1].replaceFirst(" ", "");
        }
        return null;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnClose) {
            int op = JOptionPane.showConfirmDialog(null, "Deseja mesmo sair?", "", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
        if (e.getSource() == btnInclude) {
            try {
                //loadFile("C:\\Users\\Rian\\Documents\\NetBeansProjects\\CertidaoAutomatica\\src\\etc\\CERTIDÃO.docx");
                Process process = new Process();
                process.setNumber(txtFieldProcess.getText());
                process.setPgNum(Integer.parseInt(txtFieldQtdPag.getText()));
                lm.addElement(process);
                processList.setModel((ListModel) lm);
                processList.updateUI();
                txtFieldProcess.setText(year + getCircunscricao(1) + "10000000");
                txtFieldQtdPag.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um número entre 1 e 9999.");
            }

        }
        if (e.getSource() == cbBox) {
            txtFieldProcess.setText(year + getCircunscricao(1) + "10000000");
        }
        if (e.getSource() == btnDelete) {
            try {
                int index = processList.getSelectedIndex();
                Process process = lm.getElementAt(index);
                lm.removeElement(process);
                processList.setModel((ListModel) lm);
                processList.updateUI();
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(this, "Nenhum processo selecionado.");
            }
        }
        if (e.getSource() == btnPrint) {
            //PrinterJob job = PrinterJob.getPrinterJob();
            /*for (int i = 0; i < certidoes.size(); i++) {

            }*/
            if (lm.getSize() == 0) {
                JOptionPane.showMessageDialog(this, "Nenhum processo foi inserido.");
            }
            loadFile(lm.getElementAt(0));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFieldQtdPag = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnInclude = new javax.swing.JButton();
        txtFieldProcess = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        processList = new javax.swing.JList<>();
        btnDelete = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cbBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Incluir Processos"));

        jLabel1.setText("Numero do Processo");

        jLabel2.setText("Quantidade de Páginas");

        btnInclude.setText("Incluir");

        try {
            txtFieldProcess.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####.##.#.######-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFieldProcess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFieldProcessKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtFieldProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFieldQtdPag, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(btnInclude)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFieldQtdPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFieldProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btnInclude)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Controle"));

        processList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(processList);

        btnDelete.setText("Excluir");

        btnUp.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        btnUp.setText("˄");

        btnDown.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        btnDown.setText("˅");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnDown, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 156, Short.MAX_VALUE))))
        );

        btnPrint.setText("Imprimir");

        btnClose.setText("Fechar");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Distribuição"));

        cbBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Fórum");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(cbBox, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(cbBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(btnPrint)
                        .addGap(64, 64, 64)
                        .addComponent(btnClose)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrint)
                    .addComponent(btnClose))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFieldProcessKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFieldProcessKeyPressed
        buffer = buffer + evt.getKeyChar();
        ++keyNum;
        String processNumber = txtFieldProcess.getText();
        System.out.println(evt.getKeyChar());
        switch (keyNum) {
            case 1:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 1);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
            case 2:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 2);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
            case 3:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 3);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
            case 4:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 4);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
            case 5:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 5);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
            case 6:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 6);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
            case 7:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 7);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
            case 8:
                processNumber = processNumber.substring(0, txtFieldProcess.getText().length() - 8);
                processNumber = processNumber + buffer;
                txtFieldProcess.setText(processNumber);
                break;
        }
        if (evt.getKeyChar() == evt.VK_BACK_SPACE) {
            
        }

        if (keyNum == 8) {
            keyNum = 0;
            buffer = "";
        }
    }//GEN-LAST:event_txtFieldProcessKeyPressed

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
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnInclude;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnUp;
    private javax.swing.JComboBox<String> cbBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> processList;
    private javax.swing.JFormattedTextField txtFieldProcess;
    private javax.swing.JTextField txtFieldQtdPag;
    // End of variables declaration//GEN-END:variables
}
