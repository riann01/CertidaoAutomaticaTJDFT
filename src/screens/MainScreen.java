package screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.ListModel;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import certidaoautomatica.ListModelArrayList;
import certidaoautomatica.Process;
import java.text.DateFormat;
import java.util.Locale;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import java.awt.Desktop;

public class MainScreen extends javax.swing.JFrame implements ActionListener {

    private Locale l = new Locale("pt", "BR");
    private DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, l);
    private Calendar c = Calendar.getInstance();
    private String[] split = df.format(c.getTime()).split(" ");
    private String year = split[split.length - 1];
    private ListModelArrayList lm = new ListModelArrayList();
    private List<XWPFDocument> certidoes = new ArrayList<>();
    private List<String> prep = new ArrayList<>();
    private File certidao = new File("C:\\Users\\Rian\\Documents\\NetBeansProjects\\CertidaoAutomatica\\src\\etc\\CERTIDÃO.docx");
    private String folder = "C:/temp/";
    private String fileName = "saida.docx";

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
        createPrepList();
    }

    public void loadFile(Process process) {

        XWPFDocument doc;
        try {
            doc = new XWPFDocument(OPCPackage.open(certidao.getCanonicalPath()));
            List<XWPFTable> headerTables = doc.getTables();
            List<XWPFTableRow> headerTableRow = headerTables.get(0).getRows();
            List<XWPFTableCell> headerTableCell = headerTableRow.get(0).getTableCells();
            List<XWPFParagraph> headerParagraphsCell = headerTableCell.get(0).getParagraphs();
            for (int i = 0; i < headerParagraphsCell.size(); i++) {
                if (headerParagraphsCell.get(i).getText().contains("[SQ_NUM]")) {
                    String returnV = headerParagraphsCell.get(i).getText().replace("[SQ_NUM]", String.valueOf(process.getPgNum() + 1));
                    List<XWPFRun> runs = headerParagraphsCell.get(i).getRuns();
                    runs.get(1).setText("", 0);
                    runs.get(0).setText(returnV, 0);
                }
            }
            /*List<XWPFParagraph> headerParagraphs = doc.getHeaderList().get(0).getParagraphs();
            for (int i = 0; i < headerParagraphs.size(); i++) {
                if (!headerParagraphs.get(i).isEmpty()) {
                    System.out.println(headerParagraphs.get(i).getText());

                }
            }*/

            List<XWPFParagraph> docParagraphs = doc.getParagraphs();
            for (int i = 0; i < docParagraphs.size(); i++) {
                if (!docParagraphs.get(i).isEmpty()) {
                    if (docParagraphs.get(i).getText().contains("[TWORD]")) {
                        String returnV = docParagraphs.get(i).getText().replace("[TWORD]", prep.get(cbBox.getSelectedIndex()));
                        System.out.println(returnV);
                        XWPFParagraph current = docParagraphs.get(i);
                        XWPFRun run = current.getRuns().get(0);
                        current.getRuns().get(1).setText("", 0);
                        current.getRuns().get(2).setText("", 0);
                        run.setText(returnV, 0);
                    }
                    if (docParagraphs.get(i).getText().contains("[CIRCUNSCRIÇÃO1]")) {
                        String returnV = docParagraphs.get(i).getText().replace("[CIRCUNSCRIÇÃO1]", getCircunscricao(3));
                        System.out.println(returnV);
                        XWPFParagraph current = docParagraphs.get(i);
                        XWPFRun run = current.getRuns().get(0);
                        run.setText("", 0);
                        current.getRuns().get(1).setText("", 0);
                        current.getRuns().get(2).setText("", 0);
                        run.setText(returnV, 0);
                    }
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
                    if (docParagraphs.get(i).getText().contains("[PG_NUMBER]")) {
                        String returnV = docParagraphs.get(i).getText().replace("[PG_NUMBER]", String.valueOf(process.getPgNum()));
                        System.out.println(returnV);
                        XWPFParagraph current = docParagraphs.get(i);
                        XWPFRun run = current.getRuns().get(0);
                        current.getRuns().get(1).setText("", 0);
                        current.getRuns().get(2).setText("", 0);
                        current.getRuns().get(3).setText("", 0);
                        run.setText(returnV, 0);
                    }
                    if (docParagraphs.get(i).getText().contains("[DATE]")) {
                        String returnV = docParagraphs.get(i).getText().replace("[DATE]", df.format(c.getTime()));
                        System.out.println(returnV);
                        XWPFParagraph current = docParagraphs.get(i);
                        XWPFRun run = current.getRuns().get(0);
                        current.getRuns().get(1).setText("", 0);
                        run.setText(returnV, 0);
                    }
                }
            }
            certidoes.add(doc);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void write(XWPFDocument doc) {
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
        splitC[0] = splitC[0].replaceFirst(" ", "");
        switch (prop) {
            case 1:
                return splitC[0];
            case 2:
                if (prop == 2 && splitC[0].equals("01")) {
                    return "BRASÍLIA - DF";
                } else {
                    return splitC[splitC.length - 1].replaceFirst(" ", "") + " - DF";
                }
            case 3:
                return splitC[splitC.length - 1].replaceFirst(" ", "");
        }
        return null;

    }

    public XWPFDocument mergeFiles(List<XWPFDocument> certidoes) {
        XWPFDocument file = certidoes.get(0);
        for (int i = 1; i < certidoes.size(); i++) {
            /*
            Copy the body from de document
             */
            file.getDocument().addNewBody().set(certidoes.get(i).getDocument().getBody());
        }
        return file;
    }

    public void createPrepList() {
        prep.add("");
        prep.add("");
        prep.add("");
        prep.add("DE");
        prep.add("DE");
        prep.add("DO");
        prep.add("DE");
        prep.add("DE");
        prep.add("DE");
        prep.add("DO");
        prep.add("DE");
        prep.add("DE");
        prep.add("DO");
        prep.add("DE");
        prep.add("DO");
        prep.add("DO");
        prep.add("DO");
        prep.add("DE");
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
                int num = Integer.parseInt(txtFieldQtdPag.getText());
                if (num < 1 || num > 9999) {
                    JOptionPane.showMessageDialog(this, "Por favor, digite um número entre 1 e 9999.");
                    txtFieldQtdPag.setText("");
                } else {
                    Process process = new Process();
                    process.setNumber(txtFieldProcess.getText());
                    process.setPgNum(Integer.parseInt(txtFieldQtdPag.getText()));
                    lm.addElement(process);
                    processList.setModel((ListModel) lm);
                    processList.updateUI();
                    txtFieldProcess.setText(year + getCircunscricao(1) + "10000000");
                    txtFieldQtdPag.setText("");
                }
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
            if (lm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum processo foi inserido.");
            } else {
                for (int i = 0; i < lm.getSize(); i++) {
                    loadFile(lm.getElementAt(i));
                }
                write(mergeFiles(certidoes));
            }
            File f = new File(folder + fileName);
            if (!Desktop.isDesktopSupported()) {
                System.out.println("Desktop is not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            try {
                if (f.exists()) {
                    desktop.open(f);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "O arquivo criado não existe mais.");
            }
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
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        processList = new javax.swing.JList<>();
        btnDelete = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
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

        jCheckBox1.setText("Limpar a lista ao terminar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtFieldProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFieldQtdPag, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(218, 218, 218)
                        .addComponent(btnInclude)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(jCheckBox1)
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
                .addGap(18, 18, 18)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
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

        btnClear.setText("Limpar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnDown, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGap(18, 18, 18)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 78, Short.MAX_VALUE))))
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
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnInclude;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnUp;
    private javax.swing.JComboBox<String> cbBox;
    private javax.swing.JCheckBox jCheckBox1;
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
