package asuHelloWorldJavaFX;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class ASUHelloWorldJavaFX extends Application {

	private Stage primaryStage;
    private BorderPane root;
    private VBox consoleBox;
    private HBox statusBox;
    private boolean isActivityStarted;
    private LocalDateTime startTime;
    private LinkedList<EffortLog> effortLogs;
    boolean loginPressed=true;

        @Override
        public void start(Stage primaryStage) {
            this.primaryStage = primaryStage;
            this.isActivityStarted = false;
            primaryStage.setTitle("EffortLogger - Login");

            // Create UI elements for Login Page
            Label titleLabel = new Label("EffortLogger");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

            Label usernameLabel = new Label("Username:");
            TextField usernameField = new TextField();

            Label passwordLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();

            Button loginButton = new Button("Login");
            loginButton.setOnAction(e -> {
                // Get entered username and password
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Check the entered credentials against predefined combinations
                if (isValidCredentials(username, password)) {
                    // If login is successful, switch to the home page
                    goToHomePage();
                } else {
                    // Display an alert for invalid login
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Login");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password. Please try again.");
                    alert.showAndWait();
                }
            });


            // Create layout for Login Page
            VBox loginBox = new VBox(10);
            loginBox.setAlignment(Pos.CENTER);
            loginBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

            root = new BorderPane();
            root.setPadding(new Insets(20));
            root.setTop(titleLabel);
            BorderPane.setAlignment(titleLabel, Pos.TOP_CENTER);
            root.setCenter(loginBox);
            
            	Scene scene = new Scene(root, 300, 250);
                primaryStage.setScene(scene);
                primaryStage.show();

    }
        
     // Method to check if entered credentials are valid
        private boolean isValidCredentials(String username, String password) {
            // Define the allowed username/password combinations
            String[] allowedUsernames = {"123", "Second@asu.edu"};
            String[] allowedPasswords = {"123", "Sec@1234"};

            for (int i = 0; i < allowedUsernames.length; i++) {
                if (allowedUsernames[i].equals(username) && allowedPasswords[i].equals(password)) {
                    return true; // Valid credentials
                }
            }
            return false; // Invalid credentials
        }
        
        private void createEffortLoggerConsole() {
        	// Create UI elements for Effortlogger Console
            Label consoleLabel = new Label("Effortlogger Console");
            consoleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            
            if (effortLogs == null) {
                effortLogs = new LinkedList<>();
            }

            
         // HBox for starting an activity
            Label startActivityLabel = new Label("Start an activity:");
            Button startActivityButton = new Button("Start an activity");
            HBox startActivityBox = new HBox(10);
            startActivityBox.setAlignment(Pos.CENTER);
            startActivityBox.getChildren().addAll(startActivityLabel, startActivityButton);

            // Create a dropdown menu for project selection
            Label selectProjectLabel = new Label("Select Project:");
            ComboBox<String> projectComboBox = new ComboBox<>();
            projectComboBox.getItems().addAll("Business Project", "Development Project");
            projectComboBox.setValue("Business Project"); // Set default selection

            HBox projectBox = new HBox(10);
            projectBox.setAlignment(Pos.CENTER);
            projectBox.getChildren().addAll(selectProjectLabel, projectComboBox);

            // Create a dropdown menu for lifecycle step selection
            Label selectLifecycleStepLabel = new Label("Select Lifecycle step:");
            ComboBox<String> lifecycleComboBox = new ComboBox<>();
            HBox lifecycleStepBox = new HBox(10);
            lifecycleStepBox.setAlignment(Pos.CENTER);
            lifecycleStepBox.getChildren().addAll(selectLifecycleStepLabel, lifecycleComboBox);

            // Create a dropdown menu for effort category selection
            Label selectEffortCategoryLabel = new Label("Effort Category:");
            ComboBox<String> effortCategoryComboBox = new ComboBox<>();
            HBox effortCategoryBox = new HBox(10);
            effortCategoryBox.setAlignment(Pos.CENTER);
            effortCategoryBox.getChildren().addAll(selectEffortCategoryLabel, effortCategoryComboBox);
            effortCategoryComboBox.getItems().addAll("Plans", "Deliverables", "Interruptions", "Defects", "Others");
            effortCategoryComboBox.setValue("Plans"); // Set default selection

            // Display selected effort category dynamically
            Label selectedEffortCategoryLabel = new Label();
            HBox selectedEffortCategoryBox = new HBox(10);
            selectedEffortCategoryBox.setAlignment(Pos.CENTER);
            selectedEffortCategoryBox.getChildren().addAll(new Label("Selected Effort Category: "), selectedEffortCategoryLabel);

            // Create a dropdown menu for specific subcategories based on the Effort Category
            ComboBox<String> subCategoryComboBox = new ComboBox<>();
            HBox subCategoryBox = new HBox(10);
            subCategoryBox.setAlignment(Pos.CENTER);
            subCategoryBox.getChildren().addAll(new Label("Specific Category: "), subCategoryComboBox);
            
         // HBox for stopping the activity
            Label stopActivityLabel = new Label("Stop This Activity:");
            Button stopActivityButton = new Button("Stop this activity");
            HBox stopActivityBox = new HBox(10);
            stopActivityBox.setAlignment(Pos.CENTER);
            stopActivityBox.getChildren().addAll(stopActivityLabel, stopActivityButton);

            statusBox = new HBox();
            statusBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            statusBox.setPrefHeight(10);
            
            HBox showLogsBox = new HBox(10);
            showLogsBox.setAlignment(Pos.CENTER);
            Button showLogsButton = new Button("Show Logs");
            Button filterButton = new Button("Filter");
            showLogsBox.getChildren().addAll(showLogsButton, filterButton);
            
            ComboBox<String> weightComboBox = new ComboBox<>(FXCollections.observableArrayList("1", "2", "3", "4"));
            HBox weightBox = new HBox(10);
            Label weightLabel = new Label("Weight");
            weightBox.getChildren().addAll(weightLabel, weightComboBox);
            weightBox.setAlignment(Pos.CENTER);
            
            VBox consoleBox = new VBox(20);
            consoleBox.setAlignment(Pos.CENTER);
            consoleBox.getChildren().addAll(consoleLabel,statusBox,startActivityBox ,projectBox, lifecycleStepBox, effortCategoryBox, selectedEffortCategoryBox, subCategoryBox, weightBox,stopActivityBox, showLogsBox);

            BorderPane consoleRoot = new BorderPane();
            consoleRoot.setPadding(new Insets(20));
            consoleRoot.setTop(consoleBox);
            BorderPane.setAlignment(consoleBox, Pos.TOP_CENTER);

            Scene consoleScene = new Scene(consoleRoot, 800, 600);
            primaryStage.setScene(consoleScene);
            
         // Event handlers for starting and stopping activity
            startActivityButton.setOnAction(event -> {
                isActivityStarted = true;
                startTime = LocalDateTime.now();
                statusBox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            stopActivityButton.setOnAction(event -> {
                if (isActivityStarted && startTime != null) {
                    LocalDateTime stopTime = LocalDateTime.now();
                    Duration duration = Duration.between(startTime, stopTime);
                    long seconds = duration.getSeconds();

                    EffortLog log = new EffortLog(
                            LocalDateTime.now(),
                            startTime,
                            stopTime,
                            seconds,
                            projectComboBox.getValue(),
                            lifecycleComboBox.getValue(),
                            effortCategoryComboBox.getValue(),
                            selectedEffortCategoryLabel.getText(),
                            weightComboBox.getValue()
                            
                    );

                    effortLogs.add(log);

                    isActivityStarted = false;
                    statusBox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });


            // Event handler to change options in Lifecycle Step based on Project selection
            projectComboBox.setOnAction(event -> {
                String selectedProject = projectComboBox.getValue();
                if (selectedProject.equals("Business Project")) {
                    lifecycleComboBox.getItems().setAll(
                            "Planning", "Information Gathering", "Information Understanding", "Verifying", "Outlining",
                            "Drafting", "Finalizing", "Team Meeting", "Coach Meeting", "Stakeholder Meeting"
                    );
                    lifecycleComboBox.setValue("Planning");
                } else if (selectedProject.equals("Development Project")) {
                    lifecycleComboBox.getItems().setAll(
                            "Problem Understanding", "Conceptual Design Plan", "Requirements", "Conceptual Design",
                            "Conceptual Design Review", "Detailed Design Plan", "Detailed Design Prototype",
                            "Detailed Design Review", "Implementation Plan", "Test Case Generation", "Solution Sepcification",
                            "Solution Review", "Solution Implementation", "Unit/System Test", "Reflection", "Repository Update"
                    );
                    lifecycleComboBox.setValue("Problem Understanding");
                }
            });

            // Event handler to display selected effort category dynamically
            effortCategoryComboBox.setOnAction(event -> {
                String selectedCategory = effortCategoryComboBox.getValue();
                selectedEffortCategoryLabel.setText(selectedCategory);

                // Change subcategories based on the selected Effort Category
                switch (selectedCategory) {
                    case "Plans":
                        subCategoryComboBox.getItems().setAll("Project Plan", "Risk Management Plan", "Conceptual Design Plan", "Detailed Design Plan", "Implementation Plan");
                        break;
                    case "Deliverables":
                        subCategoryComboBox.getItems().setAll("Conceptual design", "Test Cases", "Detailed Design", "Solution", "Reflection", "Outline", "Draft"
                        		, "Report", "User Defined", "Other");
                        break;
                    case "Interruptions":
                        subCategoryComboBox.getItems().setAll("Break", "Phone", "Visitor", "Teammate", "Other");
                        break;
                    case "Defects":
                        subCategoryComboBox.getItems().setAll("-There are no Defects-");
                        break;
                    case "Others":
                        subCategoryComboBox.getItems().setAll("");
                        break;
                    default:
                        subCategoryComboBox.getItems().clear();
                }
                subCategoryComboBox.setValue(subCategoryComboBox.getItems().get(0)); // Set default selection
            });
            
            showLogsButton.setOnAction(event -> showLogs());
            filterButton.setOnAction(event -> showFilterWindow());

        }
        
        private void goToHomePage() {
            // Create UI elements for Home Page
        	createEffortLoggerConsole();
        }
        
        private void showFilterWindow() {
            Stage filterStage = new Stage();
            filterStage.setTitle("Filter Logs");

            // Create UI elements for filter options (project, lifecycle step, weight)
            ComboBox<String> projectFilterComboBox = new ComboBox<>(FXCollections.observableArrayList("","Business Project", "Development Project"));
            ComboBox<String> lifecycleFilterComboBox = new ComboBox<>(FXCollections.observableArrayList(/* Add unique lifecycle steps */));
            ComboBox<String> weightFilterComboBox = new ComboBox<>(FXCollections.observableArrayList("","1", "2", "3", "4"));

            // Set up UI layout for filter window
            VBox filterLayout = new VBox(10);
            filterLayout.setPadding(new Insets(10));
            filterLayout.getChildren().addAll(
                    new Label("Project Filter"), projectFilterComboBox,
                    new Label("Lifecycle Step Filter"), lifecycleFilterComboBox,
                    new Label("Weight Filter"), weightFilterComboBox
            );

            Button applyFilterButton = new Button("Apply Filter");
            applyFilterButton.setOnAction(event -> {
                // Retrieve selected filter criteria
                String selectedProject = projectFilterComboBox.getValue();
                String selectedLifecycleStep = lifecycleFilterComboBox.getValue();
                String selectedWeight = weightFilterComboBox.getValue();

                // Filter logs based on selected criteria and display in a new window
                filterAndShowLogs(selectedProject, selectedLifecycleStep, selectedWeight);
            });

            filterLayout.getChildren().add(applyFilterButton);
            
            projectFilterComboBox.setOnAction(event -> {
                String selectedProject = projectFilterComboBox.getValue();
                if (selectedProject.equals("Business Project")) {
                	lifecycleFilterComboBox.getItems().setAll(
                            "","Planning", "Information Gathering", "Information Understanding", "Verifying", "Outlining",
                            "Drafting", "Finalizing", "Team Meeting", "Coach Meeting", "Stakeholder Meeting"
                    );
                	lifecycleFilterComboBox.setValue("Planning");
                } else if (selectedProject.equals("Development Project")) {
                	lifecycleFilterComboBox.getItems().setAll(
                            "","Problem Understanding", "Conceptual Design Plan", "Requirements", "Conceptual Design",
                            "Conceptual Design Review", "Detailed Design Plan", "Detailed Design Prototype",
                            "Detailed Design Review", "Implementation Plan", "Test Case Generation", "Solution Sepcification",
                            "Solution Review", "Solution Implementation", "Unit/System Test", "Reflection", "Repository Update"
                    );
                	lifecycleFilterComboBox.setValue("Problem Understanding");
                }
            });

            Scene filterScene = new Scene(filterLayout);
            filterStage.setScene(filterScene);
            filterStage.show();
        }

        private void filterAndShowLogs(String selectedProject, String selectedLifecycleStep, String selectedWeight) {
        	Stage filteredLogsStage = new Stage();
            filteredLogsStage.setTitle("Filtered Logs");

            TableView<EffortLog> filteredTable = new TableView<>();

            TableColumn<EffortLog, LocalDateTime> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));

            TableColumn<EffortLog, LocalDateTime> startTimeColumn = new TableColumn<>("Start Time");
            startTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartTime()));

            TableColumn<EffortLog, LocalDateTime> stopTimeColumn = new TableColumn<>("Stop Time");
            stopTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStopTime()));

            TableColumn<EffortLog, Long> totalTimeColumn = new TableColumn<>("Total Time (seconds)");
            totalTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalTimeTaken()));

            TableColumn<EffortLog, String> projectColumn = new TableColumn<>("Project");
            projectColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject()));

            TableColumn<EffortLog, String> lifecycleColumn = new TableColumn<>("Lifecycle Step");
            lifecycleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLifecycleStep()));

            TableColumn<EffortLog, String> effortCategoryColumn = new TableColumn<>("Effort Category");
            effortCategoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEffortCategory()));

            TableColumn<EffortLog, String> selectedEffortCategoryColumn = new TableColumn<>("Selected Effort Category");
            selectedEffortCategoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSelectedEffortCategory()));
            
            TableColumn<EffortLog, String> weightColumn = new TableColumn<>("Weight");
            weightColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getWeight()));


            // Create other columns and set their cell value factories similar to the showLogs method

            LinkedList<EffortLog> filteredLogs = new LinkedList<>();
            boolean matchFound = false;

            for (EffortLog log : effortLogs) {
                boolean projectMatch = selectedProject == null || log.getProject().equals(selectedProject) || selectedProject.equals("");
                boolean lifecycleStepMatch = selectedLifecycleStep == null || log.getLifecycleStep().equals(selectedLifecycleStep) ||selectedLifecycleStep.equals("");
                boolean weightMatch = selectedWeight == null || log.getWeight().equals(selectedWeight) ||selectedWeight.equals("");

                if (projectMatch && lifecycleStepMatch && weightMatch) {
                    filteredLogs.add(log);
                    matchFound = true;
                }
            }

            if (matchFound) {
                filteredTable.setItems(FXCollections.observableArrayList(filteredLogs));
                filteredTable.getColumns().addAll(dateColumn, startTimeColumn, stopTimeColumn, totalTimeColumn, projectColumn, lifecycleColumn
                		, effortCategoryColumn, selectedEffortCategoryColumn);

                Scene filteredScene = new Scene(filteredTable, 800, 400);
                filteredLogsStage.setScene(filteredScene);
                filteredLogsStage.show();
            } else {
                Label noRecordsLabel = new Label("No matching records found.");
                VBox noRecordsLayout = new VBox(noRecordsLabel);
                noRecordsLayout.setAlignment(Pos.CENTER);

                Scene noRecordsScene = new Scene(noRecordsLayout, 400, 200);
                filteredLogsStage.setScene(noRecordsScene);
                filteredLogsStage.show();
            }
        }
        
        private void showLogs() {
        	Stage logsStage = new Stage();
            logsStage.setTitle("Effort Logs");

            TableView<EffortLog> table = new TableView<>();
            table.setItems(FXCollections.observableArrayList(effortLogs));

            TableColumn<EffortLog, LocalDateTime> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));

            TableColumn<EffortLog, LocalDateTime> startTimeColumn = new TableColumn<>("Start Time");
            startTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartTime()));

            TableColumn<EffortLog, LocalDateTime> stopTimeColumn = new TableColumn<>("Stop Time");
            stopTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStopTime()));

            TableColumn<EffortLog, Long> totalTimeColumn = new TableColumn<>("Total Time (seconds)");
            totalTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalTimeTaken()));

            TableColumn<EffortLog, String> projectColumn = new TableColumn<>("Project");
            projectColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject()));

            TableColumn<EffortLog, String> lifecycleColumn = new TableColumn<>("Lifecycle Step");
            lifecycleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLifecycleStep()));

            TableColumn<EffortLog, String> effortCategoryColumn = new TableColumn<>("Effort Category");
            effortCategoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEffortCategory()));

            TableColumn<EffortLog, String> selectedEffortCategoryColumn = new TableColumn<>("Selected Effort Category");
            selectedEffortCategoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSelectedEffortCategory()));
            
            TableColumn<EffortLog, String> weightColumn = new TableColumn<>("Weight");
            weightColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getWeight()));

            table.getColumns().addAll(dateColumn, startTimeColumn, stopTimeColumn, totalTimeColumn,
                    projectColumn, lifecycleColumn, effortCategoryColumn, selectedEffortCategoryColumn, weightColumn);

            Button backToConsoleButton = new Button("Back to Console");
            backToConsoleButton.setOnAction(event -> {
                logsStage.close();
                primaryStage.show();
            });
            
            VBox logsLayout = new VBox(10);
            logsLayout.getChildren().addAll(table, backToConsoleButton);
            logsLayout.setAlignment(Pos.CENTER);

            Scene logsScene = new Scene(logsLayout, 800, 600);
            logsStage.setScene(logsScene);

            primaryStage.hide();
            logsStage.show();        }


    public static void main(String[] args) {
        launch(args);
    }
}

class EffortLog {
	public LocalDateTime date;
    public LocalDateTime startTime;
    public LocalDateTime stopTime;
    public long totalTimeTaken;
    public String project;
    public String lifecycleStep;
    public String effortCategory;
    public String selectedEffortCategory;
    public String weight;

    public EffortLog(LocalDateTime date, LocalDateTime startTime, LocalDateTime stopTime, long totalTimeTaken,
                     String project, String lifecycleStep, String effortCategory, String selectedEffortCategory,
                     String weight) {
        this.date = date;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.totalTimeTaken = totalTimeTaken;
        this.project = project;
        this.lifecycleStep = lifecycleStep;
        this.effortCategory = effortCategory;
        this.selectedEffortCategory = selectedEffortCategory;
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    // Getters and setters for EffortLog fields

    public LocalDateTime getDate() {
        return date;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public long getTotalTimeTaken() {
        return totalTimeTaken;
    }

    public String getProject() {
        return project;
    }

    public String getLifecycleStep() {
        return lifecycleStep;
    }

    public String getEffortCategory() {
        return effortCategory;
    }

    public String getSelectedEffortCategory() {
        return selectedEffortCategory;
    }
}