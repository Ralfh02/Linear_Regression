/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package salespredictionsimplelinearreg_lindog;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
public class FrmEmployeePromotionSaveLoadLindog extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmEmployeePromotionSaveLoadLindog.class.getName());

    // The assignment expects exactly ONE saved model shared by both datasets.
    private static final String MODEL_FILE = "employee_randomforest_lindog.model";

    // Custom controls (built in applyFullscreenLayout, outside the Form Editor's generated code)
    private JComboBox<String> cboDataset;
    private JButton btnMainMenu;
    private JButton btnExit;
    private JLabel lblDatasetSize;
    private JLabel lblStatus;
    private JLabel lblVerdict;
    /**
     * Creates new form FrmEmployeePromotionSaveLoadLindog
     */
    public FrmEmployeePromotionSaveLoadLindog() {
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

        // ----- Top row: CSV view | Tabular view (50% / 50%) -----
        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 13);
        txtARFF.setFont(mono);
        txtTable.setFont(mono);
        txtEvaluation.setFont(mono);
        txtEvaluation.setEditable(false);
        txtARFF.setEditable(false);
        txtTable.setEditable(false);
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

        // Final GOOD/POOR verdict shown under the evaluation report
        lblVerdict = new JLabel("Model Performance:");
        lblVerdict.setFont(lblVerdict.getFont().deriveFont(Font.BOLD, 15f));
        lblVerdict.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));
        evalPanel.add(lblVerdict, BorderLayout.SOUTH);

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

        // ----- Footer bar: dataset selector (left) | status (center) | Main Menu + Exit (right) -----
        cboDataset = new JComboBox<>(new String[] { "Good Dataset", "Bad Dataset" });
        cboDataset.setSelectedIndex(0);
        cboDataset.setPreferredSize(new Dimension(180, 34));
        cboDataset.addActionListener(e -> cboDatasetActionPerformed());
        JLabel lblDataset = new JLabel("Dataset:");
        lblDataset.setFont(lblDataset.getFont().deriveFont(Font.BOLD, 14f));

        lblDatasetSize = new JLabel("Dataset Size: 0 instances, 0 attributes");
        lblDatasetSize.setFont(lblDatasetSize.getFont().deriveFont(Font.PLAIN, 14f));

        lblStatus = new JLabel("Model: not evaluated yet");
        lblStatus.setFont(lblStatus.getFont().deriveFont(Font.ITALIC, 13f));

        btnMainMenu = new JButton("Back to Main Menu");
        btnMainMenu.setPreferredSize(new Dimension(170, 38));
        btnMainMenu.addActionListener(e -> btnMainMenuActionPerformed());

        btnExit = new JButton("Exit");
        btnExit.setPreferredSize(new Dimension(115, 38));
        btnExit.addActionListener(e -> btnExitActionPerformed());

        JPanel footerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        footerLeft.add(lblDataset);
        footerLeft.add(cboDataset);
        footerLeft.add(lblDatasetSize);

        JPanel footerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerRight.add(btnMainMenu);
        footerRight.add(btnExit);

        JPanel footer = new JPanel(new BorderLayout());
        footer.add(footerLeft, BorderLayout.WEST);
        footer.add(lblStatus, BorderLayout.CENTER);
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
     * Returns the CSV file for the dataset currently chosen in the combo box.
     */
    private String getSelectedCsvPath() {
        if (cboDataset != null && "Bad Dataset".equals(cboDataset.getSelectedItem())) {
            return "Bad_employee_promotion.csv"; // poor dataset
        }
        return "employee_promotion.csv"; // good dataset
    }

    private void cboDatasetActionPerformed() {
        // Load the selected dataset
        loaddataset();

        // Clear the previous prediction, status and verdict
        lblPrediction.setText("Predicted Promotion:");
        if (lblStatus != null) {
            lblStatus.setText("Model: not evaluated yet");
        }
        if (lblVerdict != null) {
            lblVerdict.setText("Model Performance:");
        }

        // Automatically evaluate the selected dataset
        randomforestpredictionwithmetrics_ui();
    }

    private void btnMainMenuActionPerformed() {
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

    /**
     * Loads a CSV dataset, converts any String attributes (education/promotion)
     * to nominal, and sets the class to the last attribute. The "yes" class is
     * never assumed to be at a fixed index because CSVLoader orders nominal
     * values by first appearance.
     */
    private Instances loadCsv(String path) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(path));
        Instances data = loader.getDataSet();

        if (data == null || data.numInstances() == 0) {
            throw new Exception("The selected dataset is empty.");
        }

        // Convert any String attribute (CSVLoader may read education/promotion as String)
        StringBuilder stringAttrs = new StringBuilder();
        for (int i = 0; i < data.numAttributes(); i++) {
            if (data.attribute(i).isString()) {
                if (stringAttrs.length() > 0) {
                    stringAttrs.append(",");
                }
                stringAttrs.append(i + 1); // StringToNominal ranges are 1-based
            }
        }
        if (stringAttrs.length() > 0) {
            StringToNominal filter = new StringToNominal();
            filter.setAttributeRange(stringAttrs.toString());
            filter.setInputFormat(data);
            data = Filter.useFilter(data, filter);
        }

        data.setClassIndex(data.numAttributes() - 1);

        // Debug: print attribute types to the console
        for (int i = 0; i < data.numAttributes(); i++) {
            System.out.println("Attribute " + i + ": "
                    + data.attribute(i).name() + " -> "
                    + Attribute.typeToString(data.attribute(i)));
        }

        return data;
    }

    private void loaddataset()
    {
        try {

        // Load the selected dataset
        String path = getSelectedCsvPath();
        Instances data = loadCsv(path);
        
        lblDatasetSize.setText(
    "Dataset Size: " + data.numInstances()
    + " instances, " + data.numAttributes() + " attributes"
);

        // Display the raw CSV contents (this frame does NOT show an ARFF preview)
        txtARFF.setText(new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8));

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
                + getSelectedCsvPath()
                + "\n\nError: "
                + ex.getMessage(),
                "Dataset Loading Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    }

    /**
     * Trains a Random Forest on the whole dataset with the same options as the
     * existing frame (numIterations = 100).
     */
    private Classifier trainFullModel(Instances data) throws Exception {
        RandomForest model = new RandomForest();
        model.setNumIterations(100);
        model.buildClassifier(data);
        return model;
    }

    /**
     * Saves the classifier, the dataset header (new Instances(data, 0)) and the
     * name of the CSV it was trained on into the single model file.
     */
    private void saveModel(Classifier model, Instances data, String csvName) throws Exception {
        Instances header = new Instances(data, 0);
        SerializationHelper.writeAll(MODEL_FILE, new Object[] { model, header, csvName });
    }

    /**
     * Compatibility check: same attribute names, types and nominal values as the
     * selected CSV.
     */
    private boolean headerCompatible(Instances modelHeader, Instances data) {
        if (modelHeader == null || modelHeader.numAttributes() != data.numAttributes()) {
            return false;
        }
        for (int i = 0; i < data.numAttributes(); i++) {
            Attribute actual = data.attribute(i);
            Attribute stored = modelHeader.attribute(i);
            if (stored == null) {
                return false;
            }
            if (!actual.name().equals(stored.name())) {
                return false;
            }
            if (actual.type() != stored.type()) {
                return false;
            }
            if (actual.isNominal() && stored.isNominal()) {
                if (actual.numValues() != stored.numValues()) {
                    return false;
                }
                for (int v = 0; v < actual.numValues(); v++) {
                    if (!actual.value(v).equals(stored.value(v))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Builds a GOOD/POOR verdict. There is no GOOD/POOR logic in the existing
     * frame, so this uses the fallback rule: GOOD if accuracy >= 70% AND
     * weighted F-measure >= 0.70 AND kappa >= 0.40; otherwise POOR. The text
     * names the metric(s) that drove the verdict.
     */
    private String verdictText(double accuracy, double f1Score, double kappa) {
        List<String> failed = new ArrayList<>();
        if (accuracy < 70.0) {
            failed.add("Accuracy " + String.format("%.2f%%", accuracy) + " < 70%");
        }
        if (f1Score < 0.70) {
            failed.add("Weighted F-measure " + String.format("%.4f", f1Score) + " < 0.70");
        }
        if (kappa < 0.40) {
            failed.add("Kappa " + String.format("%.4f", kappa) + " < 0.40");
        }

        if (failed.isEmpty()) {
            return "Model Performance: GOOD (Accuracy " + String.format("%.2f%%", accuracy)
                    + ", Weighted F-measure " + String.format("%.4f", f1Score)
                    + ", Kappa " + String.format("%.4f", kappa) + ").";
        }
        return "Model Performance: POOR (" + String.join("; ", failed) + ").";
    }
    
    private void randomforestpredictionwithmetrics_ui() {

    try {

        // ==========================================
        // 1. LOAD THE SELECTED DATASET
        // ==========================================

        String selectedCsv = getSelectedCsvPath();

        Instances data = loadCsv(selectedCsv);

        if (data == null || data.numInstances() == 0) {

            throw new Exception("The selected dataset is empty.");
        }

        // ==========================================
        // 2. LOAD OR TRAIN THE SINGLE SAVED MODEL
        // ==========================================

        File modelFile = new File(MODEL_FILE);

        Classifier model;

        String status;

        String trainedOn;

        if (!modelFile.exists()) {

            // 2a. No model yet -> train, evaluate, save
            model = trainFullModel(data);

            saveModel(model, data, selectedCsv);

            status = "Trained and saved";

            trainedOn = selectedCsv;

        } else {

            Classifier loadedModel = null;

            Instances modelHeader = null;

            String storedCsv = null;

            boolean loadFailed = false;

            String loadErrorMessage = "";

            boolean loadedOk = false;

            try {

                Object[] parts = SerializationHelper.readAll(MODEL_FILE);

                if (parts != null && parts.length >= 3) {

                    loadedModel = (Classifier) parts[0];

                    modelHeader = (Instances) parts[1];

                    storedCsv = (String) parts[2];

                    loadedOk = (loadedModel != null && modelHeader != null);
                }

            } catch (Exception ex) {

                loadedOk = false;

                loadFailed = true;

                loadErrorMessage = ex.getMessage();
            }

            if (loadedOk && selectedCsv.equals(storedCsv) && headerCompatible(modelHeader, data)) {

                // 2b. Same CSV and compatible header -> load without retraining
                model = loadedModel;

                status = "Loaded from file";

                trainedOn = storedCsv;

            } else {

                // 2c. Different CSV / incompatible / corrupt -> retrain and overwrite
                if (loadFailed) {

                    JOptionPane.showMessageDialog(
                            this,
                            "The saved model could not be read and may be corrupt:\n"
                            + loadErrorMessage
                            + "\n\nThe model will be retrained and overwritten.",
                            "Model Load Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }

                model = trainFullModel(data);

                saveModel(model, data, selectedCsv);

                String previous = "an unknown dataset";

                if (loadFailed) {

                    previous = "a corrupt model file";

                } else if (storedCsv != null) {

                    previous = storedCsv;
                }

                status = "Retrained on " + selectedCsv
                        + " and saved (previous model was for " + previous + ")";

                trainedOn = selectedCsv;
            }
        }

        // ==========================================
        // 3. CROSS-VALIDATION IS ALWAYS RECOMPUTED
        //    (crossValidateModel clones and retrains per fold, so the
        //    metrics never depend on the loaded model's weights)
        // ==========================================

        RandomForest cvModel = new RandomForest();

        cvModel.setNumIterations(100);

        Evaluation eval = new Evaluation(data);

        eval.crossValidateModel(
                cvModel,
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

        double kappa = eval.kappa();

        // The "yes" class index is looked up, never assumed.
        int yesIndex = data.classAttribute().indexOfValue("yes");

        double yesPrecision = (yesIndex >= 0) ? eval.precision(yesIndex) : Double.NaN;

        double yesRecall = (yesIndex >= 0) ? eval.recall(yesIndex) : Double.NaN;

        double yesF1 = (yesIndex >= 0) ? eval.fMeasure(yesIndex) : Double.NaN;

        double yesRoc = (yesIndex >= 0) ? eval.areaUnderROC(yesIndex) : Double.NaN;

        // ==========================================
        // 5. DISPLAY METRICS AND CONFUSION MATRIX
        // ==========================================

        displayEvaluation(
                txtEvaluation,
                accuracy,
                precision,
                recall,
                f1Score,
                eval,
                yesIndex,
                yesPrecision,
                yesRecall,
                yesF1,
                yesRoc
        );

        // ==========================================
        // 6. DISPLAY DATASET, MODEL STATUS AND VERDICT
        // ==========================================

        txtEvaluation.setText(
                "Selected Dataset: "
                + selectedCsv
                + "\nModel status: "
                + status
                + "\nModel trained on: "
                + trainedOn
                + "\n\n"
                + txtEvaluation.getText()
        );

        lblStatus.setText(
                "Model: " + status
                + "  |  Trained on: " + trainedOn
        );

        lblVerdict.setText(verdictText(accuracy, f1Score, kappa));

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
        int educationIndex = data.attribute(0).indexOfValue(txtEducation.getText().trim());

        if (educationIndex == -1) {

            lblPrediction.setText(
                    "Unknown education value: "
                    + txtEducation.getText().trim()
            );

            return;
        }

        employee.setValue(0, educationIndex);

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

        // Predict the promotion status using the loaded or trained model
        double prediction = model.classifyInstance(employee);

        String result = data.classAttribute().value((int) prediction);

        lblPrediction.setText("Predicted Promotion: " + result);

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error during dataset evaluation, prediction, or model save/load:\n"
                + ex.getMessage(),
                "Evaluation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    public void displayEvaluation(JTextArea textArea, double accuracy, double precision, double recall, double f1Score, Evaluation eval, int yesIndex, double yesPrecision, double yesRecall, double yesF1, double yesRoc) {
    StringBuilder sb = new StringBuilder();
    sb.append("=== Model Evaluation Report ===\n");
    sb.append(String.format("Accuracy:  %.2f%% -> %s\n", accuracy, MetricInterpreter.interpretAccuracy(accuracy)));
    sb.append(String.format("Precision: %.4f -> %s\n", precision, MetricInterpreter.interpretPrecision(precision)));
    sb.append(String.format("Recall:    %.4f -> %s\n", recall, MetricInterpreter.interpretRecall(recall)));
    sb.append(String.format("F1-Score:  %.4f -> %s\n", f1Score, MetricInterpreter.interpretF1Score(f1Score)));
    if (yesIndex >= 0) {
        sb.append("\n=== POSITIVE CLASS (yes) ===\n");
        sb.append(String.format("Precision: %.4f -> %s\n", yesPrecision, MetricInterpreter.interpretPrecision(yesPrecision)));
        sb.append(String.format("Recall:    %.4f -> %s\n", yesRecall, MetricInterpreter.interpretRecall(yesRecall)));
        sb.append(String.format("F1-Score:  %.4f -> %s\n", yesF1, MetricInterpreter.interpretF1Score(yesF1)));
        sb.append(String.format("ROC Area:  %.4f\n", yesRoc));
    }
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

        jLabel2.setText("Employee Promotion Using Random Forest  - Save and Load Model");

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

        lblARFF.setText("CSV Preview");

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
        java.awt.EventQueue.invokeLater(() -> new FrmEmployeePromotionSaveLoadLindog().setVisible(true));
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
