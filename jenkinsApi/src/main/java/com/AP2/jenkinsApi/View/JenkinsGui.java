package com.AP2.jenkinsApi.View;

import com.AP2.jenkinsApi.AppProperties;
import com.AP2.jenkinsApi.Controller.DatabaseClientController;
import com.AP2.jenkinsApi.Controller.JenkinsClientController;
import com.AP2.jenkinsApi.Service.MetricsCalculatorService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JenkinsGui extends Application {

    private MetricsCalculatorService metricsCalculator;

    @Override
    public void start(Stage primaryStage) {
        AppProperties appProperties = new AppProperties();

        String jenkinsUrl = appProperties.getProperty("jenkins.url");
        String jenkinsUser = appProperties.getProperty("jenkins.user");
        String jenkinsToken = appProperties.getProperty("jenkins.token");

        String dbUrl = appProperties.getProperty("jdbc.url");
        String dbUser = appProperties.getProperty("jdbc.user");
        String dbPassword = appProperties.getProperty("jdbc.password");

        JenkinsClientController jenkinsClient = new JenkinsClientController(jenkinsUrl, jenkinsUser, jenkinsToken);
        DatabaseClientController databaseClient = new DatabaseClientController(dbUrl, dbUser, dbPassword);
        metricsCalculator = new MetricsCalculatorService(jenkinsUrl, jenkinsClient, databaseClient);

        Label instructionLabel = new Label("Bitte geben Sie den Jobnamen ein:");
        TextField jobNameInput = new TextField();
        Button calculateButton = new Button("Berechnen");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        calculateButton.setOnAction(event -> {
            String jobName = jobNameInput.getText().trim();
            if (jobName.isEmpty()) {
                outputArea.setText("Fehler: Der Jobname darf nicht leer sein.");
                return;
            }

            try {
                double deploymentFrequency = metricsCalculator.calculateDeploymentFrequency(jobName);
                double leadTimeForChanges = metricsCalculator.calculateLeadTimeForChanges(jobName);
                double changeFailureRate = metricsCalculator.calculateChangeFailureRate(jobName);
                double timeToRestoreService = metricsCalculator.calculateTimeToRestoreService(jobName);

                StringBuilder result = new StringBuilder();
                result.append("Job Name: ").append(jobName).append("\n")
                        .append("Deployment Frequency: ").append(deploymentFrequency).append("\n")
                        .append("Lead Time for Changes: ").append(leadTimeForChanges).append(" Minuten\n")
                        .append("Change Failure Rate: ").append(changeFailureRate).append("%\n")
                        .append("Time to Restore Service: ").append(timeToRestoreService).append(" Minuten\n")
                        .append("----------\n");

                outputArea.setText(result.toString());
            } catch (Exception e) {
                outputArea.setText("Fehler bei der Berechnung: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(instructionLabel, jobNameInput, calculateButton, outputArea);

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jenkins Metrics Calculator");
        primaryStage.show();
    }
}