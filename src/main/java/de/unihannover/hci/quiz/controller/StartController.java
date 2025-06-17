package de.unihannover.hci.quiz.controller;

import de.unihannover.hci.quiz.QuizApplication;
import de.unihannover.hci.quiz.db.DatabaseManager;
import de.unihannover.hci.quiz.model.Category;
import de.unihannover.hci.quiz.model.Question;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class StartController {

    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private Button startButton;

    @FXML
    public void initialize() {
        List<Category> categories = DatabaseManager.getCategories();
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        startButton.setDisable(true);
        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            startButton.setDisable(newValue == null);
        });
    }

    @FXML
    void onStartQuiz(ActionEvent event) throws IOException {
        Category selectedCategory = categoryComboBox.getValue();
        if (selectedCategory == null) return;

        List<Question> questions = DatabaseManager.getQuestionsForCategory(selectedCategory.getId());

        if (questions.isEmpty()) {
            // Handle case with no questions
            System.out.println("No questions found for this category.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(QuizApplication.class.getResource("quiz-view.fxml"));
        Parent root = loader.load();

        QuizController quizController = loader.getController();
        quizController.initData(selectedCategory, questions);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
