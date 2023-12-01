
package asuHelloWorldJavaFX;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ASUHelloWorldJavaFX extends Application {




    private VBox cardView;
    private TableView<String[]> tableView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f0f0f0;");
        layout.setPadding(new Insets(40));

        Label titleLabel = new Label("View Effort Logs");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        BorderPane titlePane = new BorderPane();
        titlePane.setCenter(titleLabel);
        titlePane.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 5");

        cardView = new VBox(20);
        cardView.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(titlePane, createTableView(), createSearchButton(), createShowPieChartButton(), cardView);

        Scene scene = new Scene(layout, 800, 600);

        primaryStage.setTitle("Effort Logger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<String[]> createTableView() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(effortLogs);

        TableColumn<String[], String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> timeColumn = new TableColumn<>("Start/Stop Time");
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));

        TableColumn<String[], String> projectTypeColumn = new TableColumn<>("Project Type");
        projectTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

        TableColumn<String[], String> effortCategoryColumn = new TableColumn<>("Effort Category");
        effortCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        TableColumn<String[], String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));

        TableColumn<String[], Integer> weightColumn = new TableColumn<>("Weight");
        weightColumn.setCellValueFactory(cellData -> {
            String value = cellData.getValue()[6];
            return new SimpleIntegerProperty(Integer.parseInt(value)).asObject();
        });

        TableColumn<String[], String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[7]));

        tableView.getColumns().addAll(numberColumn, dateColumn, timeColumn, projectTypeColumn, effortCategoryColumn, descriptionColumn, weightColumn, statusColumn);

        HBox layout = new HBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f0f0f0;");
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(tableView);

        return tableView;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", 14));
        return button;
    }

    private Button createSearchButton() {
        Button searchButton = createStyledButton("Search");
        searchButton.setOnAction(e -> searchRecords());

        return searchButton;
    }



    private void searchRecords() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Records");
        dialog.setHeaderText("Enter a keyword to search for records:");
        dialog.setContentText("Keyword:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(keyword -> {
            List<String[]> searchResults = effortLogs.stream()
                    .filter(record -> {
                        for (int i = 0; i < record.length; i++) {
                            if (record[i].contains(keyword)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

            showSearchResults(searchResults);
        });
    }

    private void showSearchResults(List<String[]> searchResults) {
        tableView.getItems().clear();
        tableView.getItems().addAll(searchResults);
    }
    private Button createShowPieChartButton() {
        Button showPieChartButton = createStyledButton("Show Pie Chart");
        showPieChartButton.setOnAction(e -> showPieChart());

        return showPieChartButton;
    }

    private void showPieChart() {
    	PieChart pieChart = new PieChart();

        // Count occurrences of each effort category
        long totalEfforts = effortLogs.size();
        Map<String, Long> categoryCounts = effortLogs.stream().collect(Collectors.groupingBy(record -> record[4], Collectors.counting()));

        // Add data to the pie chart
        categoryCounts.forEach((effortCategory, count) -> {
            double percentage = (count / (double) totalEfforts) * 100.0;
            PieChart.Data data = new PieChart.Data(effortCategory + ": " + String.format("%.2f%%", percentage), count);
            pieChart.getData().add(data);
        });

        Stage pieChartStage = new Stage();
        pieChartStage.setTitle("Effort Category Distribution");
        pieChartStage.setScene(new Scene(new VBox(pieChart), 400, 400));
        pieChartStage.show();
    }
    
	private ObservableList<String[]> effortLogs = FXCollections.observableArrayList(
            new String[]{"1", "2023-01-01", "09:00 - 12:00", "Business", "Plans", "Planning Description", "4", "Finished"},
            new String[]{"2", "2023-01-02", "14:00 - 16:00", "Development", "Plans", "Implementation Description", "3", "Unfinished"},
            new String[]{"3", "2023-01-03", "10:00 - 11:30", "Business", "Interupption", "Random Description", "5", "Finished"},
            new String[]{"4", "2023-01-04", "13:30 - 15:00", "Development", "Defects", "Defect Description", "2", "Unfinished"},
            new String[]{"5", "2023-01-05", "16:00 - 17:30", "Business", "Other", "Other Description Reporting", "4", "Finished"},
            new String[]{"6", "2023-01-21", "09:30 - 11:00", "Development", "Plans", "Code Review Description", "3", "Unfinished"},
            new String[]{"7", "2023-01-22", "14:30 - 16:00", "Business", "Deliverable", "Client Meeting Description", "5", "Finished"},
            new String[]{"8", "2023-01-23", "11:00 - 12:30", "Development", "Interupption", "Feature Enhancement Description", "2", "Unfinished"},
            new String[]{"9", "2023-01-24", "13:00 - 14:30", "Business", "Defects", "Training Description", "4", "Finished"},
            new String[]{"10", "2023-01-25", "15:30 - 17:00", "Development", "Defects", "Database Design Description", "3", "Unfinished"},
            new String[]{"11", "2023-01-26", "10:30 - 12:00", "Business", "Plans", "Project Review Description", "2", "Finished"},
            new String[]{"12", "2023-01-27", "14:00 - 15:30", "Development", "Defects", "Refactoring Description", "4", "Unfinished"},
            new String[]{"13", "2023-01-28", "16:30 - 18:00", "Business", "Interupption", "Team Building Description", "5", "Finished"},
            new String[]{"14", "2023-01-29", "09:00 - 10:30", "Development", "Defects", "Code Deployment Description", "2", "Unfinished"},
            new String[]{"15", "2023-01-30", "11:30 - 13:00", "Business", "Other", "Vendor Negotiation Description", "3", "Finished"},
            new String[]{"16", "2023-01-31", "13:30 - 15:00", "Development", "Plans", "Usability Testing Description", "4", "Unfinished"},
            new String[]{"17", "2023-02-01", "15:30 - 17:00", "Business", "Other", "Feature Testing Description", "5", "Finished"},
            new String[]{"18", "2023-02-02", "10:00 - 11:30", "Development", "Plans", "Meeting Description", "2", "Unfinished"},
            new String[]{"19", "2023-02-03", "12:00 - 13:30", "Business", "Defects", "Presentation Description", "3", "Finished"},
            new String[]{"20", "2023-02-04", "14:30 - 16:00", "Development", "Other", "Analysis Description", "4", "Unfinished"}
    );

}

