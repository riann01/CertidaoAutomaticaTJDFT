package screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class InitAssistant extends javax.swing.JFrame implements ActionListener {

    private File arq;
    private File f;
    private Properties p;

    public InitAssistant() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(verifyFile());
        btnLoad.addActionListener(this);
        btnNext.addActionListener(this);
        btnQuit.addActionListener(this);
        btnNext.setEnabled(false);
    }

    public boolean verifyFile() {
        f = new File("C:\\CertidaoAutomatica\\");
        File file = new File("C:\\CertidaoAutomatica\\config.properties");
        if (!f.exists()) {
            if (!file.exists()) {
                return true;
            }
        }
        p = new Properties();
        try {
            InputStream is = new FileInputStream(file.getAbsoluteFile());
            p.load(is);
            new MainScreen(p.getProperty("path"));
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Erro", "Erro ao abrir o arquivo, o programa será fechado.", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro", "Erro ao abrir o arquivo, o programa será fechado.", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e
    ) {
        if (e.getSource() == btnLoad) {
            JFileChooser fc = new JFileChooser(".");
            fc.setMultiSelectionEnabled(false);
            FileFilter type1 = new FileNameExtensionFilter("Arquivos do Microsoft Word 2010-2019", "docx");
            fc.setFileFilter(type1);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int state = fc.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                arq = fc.getSelectedFile();
                lblFileName.setText("Nome do arquivo:\n " + arq.getName());
                btnNext.setEnabled(true);
            } else {
                if (state == JFileChooser.ERROR_OPTION) {
                    JOptionPane.showMessageDialog(this, "Ocorreu um erro ao carregar o arquivo.", "Ocorreu um Erro", JOptionPane.ERROR_MESSAGE);
                    arq = null;
                    btnNext.setEnabled(false);
                    lblFileName.setText("Nome do arquivo:\n");
                } else {
                    arq = null;
                    btnNext.setEnabled(false);
                    lblFileName.setText("Nome do arquivo:\n");
                }
            }
        }
        if (e.getSource() == btnNext) {
            try {
                File dir = new File("C:\\CertidaoAutomatica\\");
                dir.mkdir();
                File file = new File("C:\\CertidaoAutomatica\\config.properties");

                OutputStream output = new FileOutputStream(file);
                Properties prop = new Properties();

                // set the properties value
                prop.setProperty("path", arq.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Gravando local em " + file.getAbsolutePath());

                // save properties to project root folder
                prop.store(output, null);
                new MainScreen(arq.getAbsolutePath());

            } catch (IOException io) {
                JOptionPane.showMessageDialog(this, io.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            if (e.getSource() == btnQuit) {
                System.exit(0);
            }
        }
    }

        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnLoad = new javax.swing.JButton();
        lblFileName = new javax.swing.JLabel();
        btnNext = new javax.swing.JButton();
        btnQuit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Assistente");
        setResizable(false);

        jLabel1.setText("Por favor, selecione o template da certidão");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Olá, detectei que esta é a primeira inicialização");

        btnLoad.setText("Carregar Template");

        lblFileName.setText("Nome do arquivo:");

        btnNext.setText("Avançar");

        btnQuit.setText("Sair");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblFileName)
                        .addGap(132, 132, 132))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnLoad)
                        .addGap(111, 111, 111))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(btnQuit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNext))
                            .addComponent(jLabel1))
                        .addGap(60, 60, 60)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btnLoad)
                .addGap(18, 18, 18)
                .addComponent(lblFileName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNext)
                    .addComponent(btnQuit))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnQuit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblFileName;
    // End of variables declaration//GEN-END:variables
}
