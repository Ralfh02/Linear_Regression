/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package salespredictionsimplelinearreg_lindog;

import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Instance;

import weka.core.converters.ConverterUtils.DataSource;

import weka.classifiers.Evaluation;
import java.util.Random;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 *
 * @author Reign
 */
public class FrmEmployeePromotionLindog extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmEmployeePromotionLindog.class.getName());

    // Custom controls (built in applyFullscreenLayout, outside the Form Editor's generated code)
    private JComboBox<String> cboDataset;
    private JButton btnMainMenu;
    private JButton btnExit;
    private JLabel lblDatasetSize;
    /**
     * Creates new form FrmEmployeePromotionLindog
     */
    public FrmEmployeePromotionLindog() {
         initComponents();
        applyFullscreenLayout();
        setExtendedState(JFrame.MAXIMIZED_BOTH);// fullscreen + responsive layout (runs after the generated code)
        loaddataset();
    }
     private JPanel labeledPanel(JLabel label, Component content) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        p.add(label, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);
        return p;
    }
     private void applyFullscreenLayout() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 650));

        // ----- Header: centered titles -----
        jPanel1.removeAll();
        jPanel1.setLayout(new GridBagLayout());
        jLabel1.setFont(jLabel1.getFont().deriveFont(Font.BOLD, 24f));
        jLabel2.setFont(jLabel2.getFont().deriveFont(Font.PLAIN, 16f));
        GridBagConstraints h = new GridBagConstraints();
        h.gridx = 0; h.gridy = 0; h.insets = new Insets(14, 10, 2, 10);
        jPanel1.add(jLabel1, h);
        h.gridy = 1; h.insets = new Insets(0, 10, 14, 10);
        jPanel1.add(jLabel2, h);

        // ----- Top row: ARFF view | Tabular view (50% / 50%) -----
        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 13);
        txtARFF.setFont(mono);
        txtTable.setFont(mono);
        txtEvaluation.setFont(mono);
        txtEvaluation.setEditable(false);
        jScrollPane1.setPreferredSize(new Dimension(100, 120));
        jScrollPane2.setPreferredSize(new Dimension(100, 120));
        jScrollPane3.setPreferredSize(new Dimension(100, 120));

        JPanel topRow = new JPanel(new GridLayout(1, 2, 12, 0));
        topRow.add(labeledPanel(lblARFF, jScrollPane1));
        topRow.add(labeledPanel(lblTable, jScrollPane2));

        // ----- Bottom row: inputs | evaluation | metric descriptions -----
        JPanel input = new JPanel(new GridBagLayout());
        input.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 0; input.add(jLabel3, c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; input.add(txtEducation, c);
        c.gridx = 0; c.gridy = 1; c.weightx = 0; c.fill = GridBagConstraints.NONE; input.add(jLabel4, c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; input.add(txtYearService, c);
        c.gridx = 0; c.gridy = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE; input.add(jLabel5, c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; input.add(txtPerformance, c);
        btnAnalyze.setPreferredSize(new Dimension(115, 38));
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        input.add(btnAnalyze, c);
        lblPrediction.setFont(lblPrediction.getFont().deriveFont(Font.BOLD, 15f));
        c.gridy = 4; input.add(lblPrediction, c);
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        c.gridy = 5; c.weighty = 1; c.fill = GridBagConstraints.BOTH; input.add(filler, c);

        JPanel evalPanel = new JPanel(new BorderLayout());
        evalPanel.setBorder(BorderFactory.createTitledBorder("Model Evaluation"));
        evalPanel.add(jScrollPane3, BorderLayout.CENTER);

        // Metric descriptions
        JEditorPane descriptions = new JEditorPane("text/html",
            "<html><body style='font-family:Dialog; font-size:12pt; margin:4px;'>"
          + "<b>Accuracy</b> - Porsyento ng lahat ng tamang prediction ng model.<br><br>"
          + "<b>Precision</b> - Kung gaano karaming positive prediction ang tama.<br><br>"
          + "<b>Recall</b> - Kung gaano karaming actual positive cases ang natukoy nang tama.<br><br>"
          + "<b>F1-Score</b> - Pinagsamang sukatan ng Precision at Recall<br><br>"
          + "<b>Confusion Matrix</b> - Ipinapakita kung alin ang tama at maling prediction ng model."
          + "</body></html>");
        descriptions.setEditable(false);
        descriptions.setCaretPosition(0);
        JScrollPane descScroll = new JScrollPane(descriptions);
        descScroll.setPreferredSize(new Dimension(100, 120));
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(BorderFactory.createTitledBorder("Metric Descriptions"));
        descPanel.add(descScroll, BorderLayout.CENTER);

        JPanel bottomRow = new JPanel(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.fill = GridBagConstraints.BOTH; b.weighty = 1; b.gridy = 0;
        b.gridx = 0; b.weightx = 0.26; b.insets = new Insets(0, 0, 0, 12); bottomRow.add(input, b);
        b.gridx = 1; b.weightx = 0.34; bottomRow.add(evalPanel, b);
        b.gridx = 2; b.weightx = 0.40; b.insets = new Insets(0, 0, 0, 0); bottomRow.add(descPanel, b);

        // ----- Center: top row 55% / bottom row 45% -----
        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints m = new GridBagConstraints();
        m.fill = GridBagConstraints.BOTH; m.weightx = 1; m.gridx = 0;
        m.gridy = 0; m.weighty = 0.55; m.insets = new Insets(0, 0, 12, 0); center.add(topRow, m);
        m.gridy = 1; m.weighty = 0.45; m.insets = new Insets(0, 0, 0, 0); center.add(bottomRow, m);

        // ----- Footer bar: dataset selector (left) | Main Menu + Exit buttons (right) -----
        cboDataset = new JComboBox<>(new String[] { "Good Dataset", "Bad Dataset" });
        cboDataset.setSelectedIndex(0);
        cboDataset.setPreferredSize(new Dimension(180, 34));
        cboDataset.addActionListener(e -> cboDatasetActionPerformed());
        JLabel lblDataset = new JLabel("Dataset:");
        lblDataset.setFont(lblDataset.getFont().deriveFont(Font.BOLD, 14f));

        btnMainMenu = new JButton("Back to Main Menu");
        btnMainMenu.setPreferredSize(new Dimension(170, 38));
        btnMainMenu.addActionListener(e -> btnMainMenuActionPerformed());

        btnExit = new JButton("Exit");
        btnExit.setPreferredSize(new Dimension(115, 38));
        btnExit.addActionListener(e -> btnExitActionPerformed());

        JPanel footerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

lblDatasetSize = new JLabel("Dataset Size: 0 instances, 0 attributes");
lblDatasetSize.setFont(lblDatasetSize.getFont().deriveFont(Font.PLAIN, 14f));

footerLeft.add(lblDataset);
footerLeft.add(cboDataset);
footerLeft.add(lblDatasetSize);
        JPanel footerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerRight.add(btnMainMenu);
        footerRight.add(btnExit);
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(footerLeft, BorderLayout.WEST);
        footer.add(footerRight, BorderLayout.EAST);

        // ----- Assemble the frame -----
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(0, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));
        getContentPane().add(jPanel1, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);
        revalidate();
        repaint();
        
    }
    /**
     * Returns the ARFF file for the dataset currently chosen in the combo box.
     * TODO: point "Bad Dataset" to its own ARFF file once it is ready.
     */
    private String getSelectedDatasetPath() {
        if (cboDataset != null && "Bad Dataset".equals(cboDataset.getSelectedItem())) {
            return "Bad_employee_promotion.arff"; // TODO: replace with the bad dataset file
        }
        return "employee_promotion.arff"; // good dataset
    }

    private void cboDatasetActionPerformed() {
        // Load the selected dataset
    loaddataset();

    // Clear the previous prediction
    lblPrediction.setText("Predicted Promotion:");

    // Automatically evaluate the selected dataset
    randomforestpredictionwithmetrics_ui();
    }

    private void btnMainMenuActionPerformed() {
        // NOTE: change FrmMainMenuLindog to the actual class name of your main menu form
        new MainMenu().setVisible(true);
        dispose();
    }

    private void btnExitActionPerformed() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the application?",
                "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void loaddataset()
    {
        try {

        // Load the selected dataset
        Instances data = DataSource.read(getSelectedDatasetPath());
        
        lblDatasetSize.setText(
    "Dataset Size: " + data.numInstances()
    + " instances, " + data.numAttributes() + " attributes"
);

        // Display the ARFF contents
        txtARFF.setText(data.toString());

        // Display the dataset in tabular form
        StringBuilder output = new StringBuilder();

        output.append("Education\tYears Service\tPerformance\tPromotion\n");

        for (int i = 0; i < data.numInstances(); i++) {

            String education = data.instance(i).stringValue(0);

            double years = data.instance(i).value(1);

            double performance = data.instance(i).value(2);

            String promotion = data.instance(i).stringValue(3);

            output.append(education)
                  .append("\t")
                  .append(years)
                  .append("\t")
                  .append(performance)
                  .append("\t")
                  .append(promotion)
                  .append("\n");
        }

        txtTable.setText(output.toString());

    } catch (Exception ex) {

        txtARFF.setText("");
        txtTable.setText("");
        txtEvaluation.setText("");

        JOptionPane.showMessageDialog(
                this,
                "Failed to load dataset:\n"
                + getSelectedDatasetPath()
                + "\n\nError: "
                + ex.getMessage(),
                "Dataset Loading Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    }
    
    private void randomforestpredictionwithmetrics_ui() {

    try {

        // ==========================================
        // 1. LOAD THE SELECTED DATASET
        // ==========================================

        Instances data = DataSource.read(getSelectedDatasetPath());

        if (data == null || data.numInstances() == 0) {

            throw new Exception("The selected dataset is empty.");
        }

        // Set the class attribute
        data.setClassIndex(data.numAttributes() - 1);

        // ==========================================
        // 2. BUILD THE RANDOM FOREST MODEL
        // ==========================================

        RandomForest model = new RandomForest();

        model.setNumIterations(100);

        model.buildClassifier(data);

        // ==========================================
        // 3. EVALUATE THE SELECTED DATASET
        // ==========================================

        Evaluation eval = new Evaluation(data);

        eval.crossValidateModel(
                model,
                data,
                10,
                new Random(1)
        );

        // ==========================================
        // 4. CALCULATE EVALUATION METRICS
        // ==========================================

        double accuracy = eval.pctCorrect();

        double precision = eval.weightedPrecision();

        double recall = eval.weightedRecall();

        double f1Score = eval.weightedFMeasure();

        // ==========================================
        // 5. DISPLAY METRICS AND CONFUSION MATRIX
        // ==========================================

        displayEvaluation(
                txtEvaluation,
                accuracy,
                precision,
                recall,
                f1Score,
                eval
        );

        // ==========================================
        // 6. DISPLAY WHICH DATASET WAS EVALUATED
        // ==========================================

        txtEvaluation.setText(
                "Selected Dataset: "
                + cboDataset.getSelectedItem()
                + "\n\n"
                + txtEvaluation.getText()
        );

        // ==========================================
        // 7. PREDICT EMPLOYEE PROMOTION
        // ==========================================

        // Do not predict until all input fields are filled.
        if (txtEducation.getText().trim().isEmpty()
                || txtYearService.getText().trim().isEmpty()
                || txtPerformance.getText().trim().isEmpty()) {

            lblPrediction.setText(
                    "Enter employee details to predict promotion."
            );

            return;
        }

        // Create an instance for the employee
        Instance employee = new DenseInstance(data.numAttributes());

        employee.setDataset(data);

        // Set the employee's input values
        employee.setValue(
                0,
                txtEducation.getText().trim()
        );

        employee.setValue(
                1,
                Double.parseDouble(
                        txtYearService.getText().trim()
                )
        );

        employee.setValue(
                2,
                Double.parseDouble(
                        txtPerformance.getText().trim()
                )
        );

        // Predict the promotion status
        double prediction = model.classifyInstance(employee);

        String result = data.classAttribute().value((int) prediction);

        lblPrediction.setText("Predicted Promotion: " + result);

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error during dataset evaluation or prediction:\n"
                + ex.getMessage(),
                "Evaluation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    public void displayEvaluation(JTextArea textArea, double accuracy, double precision, double recall, double f1Score, Evaluation eval) {
    StringBuilder sb = new StringBuilder();
    sb.append("=== Model Evaluation Report ===\n");
    sb.append(String.format("Accuracy:  %.2f%% -> %s\n", accuracy, MetricInterpreter.interpretAccuracy(accuracy)));
    sb.append(String.format("Precision: %.4f -> %s\n", precision, MetricInterpreter.interpretPrecision(precision)));
    sb.append(String.format("Recall:    %.4f -> %s\n", recall, MetricInterpreter.interpretRecall(recall)));
    sb.append(String.format("F1-Score:  %.4f -> %s\n", f1Score, MetricInterpreter.interpretF1Score(f1Score)));
    // Confusion Matrix
    sb.append("\n=== CONFUSION MATRIX ===\n");

    double[][] matrix = eval.confusionMatrix();

    // Display class labels
    sb.append("Rows = Actual | Columns = Predicted\n\n");

    sb.append(String.format("%-18s", "Actual/Predicted"));

    for (int i = 0; i < eval.getHeader().classAttribute().numValues(); i++) {
        sb.append(String.format(
                "%-15s",
                eval.getHeader().classAttribute().value(i)));
    }

    sb.append("\n");

    for (int i = 0; i < matrix.length; i++) {

        sb.append(String.format(
                "%-18s",
                eval.getHeader().classAttribute().value(i)));

        for (int j = 0; j < matrix[i].length; j++) {
            sb.append(String.format("%-15.0f", matrix[i][j]));
        }

        sb.append("\n");
    }

    textArea.setText(sb.toString());
    textArea.setCaretPosition(0);
    
    // Set the text in the text area (use sb.toString() to append if you want to keep history)
    textArea.setText(sb.toString());
}
    public class MetricInterpreter {

    public static String interpretAccuracy(double accuracy) {
        if (accuracy < 0 || accuracy > 100)
            throw new IllegalArgumentException("Accuracy must be 0-100");
        if (accuracy >= 90) return "Outstanding";
        if (accuracy >= 80) return "Highly Acceptable";
        if (accuracy >= 70) return "Acceptable";
        if (accuracy >= 60) return "Moderately Acceptable";
        return "Low";
    }

    private static String interpretRatio(double value) {
        if (value < 0 || value > 1)
            throw new IllegalArgumentException("Value must be 0.0-1.0");
        if (value >= 0.90) return "Outstanding";
        if (value >= 0.80) return "Highly Acceptable";
        if (value >= 0.70) return "Acceptable";
        if (value >= 0.60) return "Moderately Acceptable";
        return "Low";
    }

    public static String interpretPrecision(double p) { return interpretRatio(p); }
    public static String interpretRecall(double r)    { return interpretRatio(r); }
    public static String interpretF1Score(double f1)  { return interpretRatio(f1); }
}   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblARFF = new javax.swing.JLabel();
        lblTable = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtARFF = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtTable = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtEducation = new javax.swing.JTextField();
        txtYearService = new javax.swing.JTextField();
        txtPerformance = new javax.swing.JTextField();
        lblPrediction = new javax.swing.JLabel();
        btnAnalyze = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtEvaluation = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));

        jLabel1.setText("Knowledge Analysis - Lindog | Caligayahan");

        jLabel2.setText("Employee Promotion Prediction Using Random Forest");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(30, 30, 30)))
                .addGap(174, 174, 174))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        lblARFF.setText("Attribute Relation File Format (ARFF) ");

        lblTable.setText("Tabular View");

        txtARFF.setColumns(20);
        txtARFF.setRows(5);
        jScrollPane1.setViewportView(txtARFF);

        txtTable.setColumns(20);
        txtTable.setRows(5);
        jScrollPane2.setViewportView(txtTable);

        jLabel3.setText("Education");

        jLabel4.setText("Year Service");

        jLabel5.setText("Performance");

        txtEducation.addActionListener(this::txtEducationActionPerformed);

        txtYearService.addActionListener(this::txtYearServiceActionPerformed);

        lblPrediction.setText("Predicted Promotion:");

        btnAnalyze.setText("Analyze");
        btnAnalyze.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAnalyzeMouseEntered(evt);
            }
        });
        btnAnalyze.addActionListener(this::btnAnalyzeActionPerformed);

        txtEvaluation.setColumns(20);
        txtEvaluation.setRows(5);
        jScrollPane3.setViewportView(txtEvaluation);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblARFF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTable)
                        .addGap(229, 229, 229))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(24, 24, 24)
                                    .addComponent(txtYearService, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel5))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(txtEducation))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, Short.MAX_VALUE)
                                                .addComponent(txtPerformance, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(122, 122, 122)
                                .addComponent(btnAnalyze, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(118, 118, 118)
                                        .addComponent(lblPrediction)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblARFF)
                    .addComponent(lblTable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtEducation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrediction))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtYearService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPerformance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnAnalyze, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        lblPrediction.getAccessibleContext().setAccessibleName("Predicted Promotion: ");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtYearServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtYearServiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtYearServiceActionPerformed

    private void btnAnalyzeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnalyzeMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAnalyzeMouseEntered

    private void txtEducationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEducationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEducationActionPerformed

    private void btnAnalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalyzeActionPerformed
        // TODO add your handling code here:
        randomforestpredictionwithmetrics_ui();
    }//GEN-LAST:event_btnAnalyzeActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmEmployeePromotionLindog().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalyze;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblARFF;
    private javax.swing.JLabel lblPrediction;
    private javax.swing.JLabel lblTable;
    private javax.swing.JTextArea txtARFF;
    private javax.swing.JTextField txtEducation;
    private javax.swing.JTextArea txtEvaluation;
    private javax.swing.JTextField txtPerformance;
    private javax.swing.JTextArea txtTable;
    private javax.swing.JTextField txtYearService;
    // End of variables declaration//GEN-END:variables
}
