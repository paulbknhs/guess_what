package de.unihannover.hci.quiz.controller;

import de.unihannover.hci.quiz.QuizApplication;
import de.unihannover.hci.quiz.db.DatabaseManager;
import de.unihannover.hci.quiz.model.Category;
import de.unihannover.hci.quiz.model.HighScore;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ResultController {

  @FXML
  private Label scoreLabel;
  @FXML
  private TableView<HighScore> highScoreTable;
  @FXML
  private TableColumn<HighScore, String> scoreColumn;
  @FXML
  private TableColumn<HighScore, String> dateColumn;

  public void initData(Category category, int score, int totalQuestions) {
    scoreLabel.setText("Du hast " + score + " von " + totalQuestions + " Punkten erreicht!");

    // Save the new score
    DatabaseManager.saveHighScore(category.getName(), score, totalQuestions);

    // Display high scores
    loadHighScores(category.getName());
  }

  private void loadHighScores(String categoryName) {
    scoreColumn.setCellValueFactory(cellData -> {
      String scoreText = cellData.getValue().getScore() + " / " + cellData.getValue().getTotalQuestions();
      return new javafx.beans.property.SimpleStringProperty(scoreText);
    });
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

    List<HighScore> highScores = DatabaseManager.getHighScores(categoryName);
    highScoreTable.setItems(FXCollections.observableArrayList(highScores));
  }

  @FXML
  void onPlayAgain(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(QuizApplication.class.getResource("start-view.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  void onExit() {
    Platform.exit();
  }
}
