package com.AP2.jenkinsApi;

import com.AP2.jenkinsApi.Controller.DatabaseClientController;
import com.AP2.jenkinsApi.Controller.JenkinsClientController;
import com.AP2.jenkinsApi.Service.MetricsCalculatorService;
import com.AP2.jenkinsApi.View.JenkinsGui;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

@SpringBootApplication
public class JenkinsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JenkinsApiApplication.class, args);

		AppProperties appProperties = new AppProperties();
		String jenkinsUrl = appProperties.getProperty("jenkins.url");
		String jenkinsUser = appProperties.getProperty("jenkins.user");
		String jenkinsToken = appProperties.getProperty("jenkins.token");
		String dbUrl = appProperties.getProperty("jdbc.url");
		String dbUser = appProperties.getProperty("jdbc.user");
		String dbPassword = appProperties.getProperty("jdbc.password");

		JenkinsClientController jenkinsClient = new JenkinsClientController(jenkinsUrl, jenkinsUser, jenkinsToken);
		DatabaseClientController databaseClient = new DatabaseClientController(dbUrl, dbUser, dbPassword);
		MetricsCalculatorService metricsService = new MetricsCalculatorService(jenkinsUrl, jenkinsClient, databaseClient);

		JenkinsGui gui = new JenkinsGui();
		Application.launch(gui.getClass());
	}
}