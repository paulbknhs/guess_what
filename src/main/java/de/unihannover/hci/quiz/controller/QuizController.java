package de.unihannover.hci.quiz.controller;

import de.unihannover.hci.quiz.QuizApplication;
import de.unihannover.hci.quiz.model.Answer;
import de.unihannover.hci.quiz.model.Category;
import de.unihannover.hci.quiz.model.Question;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class QuizController {

    @FXML private Label categoryLabel;
    @FXML private Label questionNumberLabel;
    @FXML private Label questionTextLabel;
    @FXML private VBox answersVBox;
    @FXML private Button answerButton1, answerButton2, answerButton3, answerButton4;
    @FXML private Label feedbackLabel;

    private List<Button> answerButtons;
    private Category currentCategory;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @FXML
    public void initialize() {
        answerButtons = Arrays.asList(answerButton1, answerButton2, answerButton3, answerButton4);
    }

    public void initData(Category category, List<Question> questions) {
        this.currentCategory = category;
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0;
        categoryLabel.setText("Kategorie: " + currentCategory.getName());
        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        feedbackLabel.setVisible(false);
        setAnswerButtonsDisabled(false);

        Question currentQuestion = questions.get(currentQuestionIndex);
        questionTextLabel.setText(currentQuestion.getQuestionText());
        questionNumberLabel.setText("Frage " + (currentQuestionIndex + 1) + " von " + questions.size());

        List<Answer> answers = currentQuestion.getAnswers();
        for (int i = 0; i < answerButtons.size(); i++) {
            if (i < answers.size()) {
                answerButtons.get(i).setText(answers.get(i).getText());
                answerButtons.get(i).setUserData(answers.get(i)); // Store Answer object in button
                answerButtons.get(i).setVisible(true);
                answerButtons.get(i).setStyle(""); // Reset style
            } else {
                answerButtons.get(i).setVisible(false);
            }
        }
    }

    @FXML
    void handleAnswer(ActionEvent event) {
        setAnswerButtonsDisabled(true);

        Button clickedButton = (Button) event.getSource();
        Answer selectedAnswer = (Answer) clickedButton.getUserData();
        String correctAnswerText = "";

        // Find the correct answer to display it if the user is wrong
        for(Question q : questions) {
             for (Answer a : q.getAnswers()){
                 if(a.isCorrect()){
                     correctAnswerText = a.getText();
                 }
             }
        }


        if (selectedAnswer.isCorrect()) {
            score++;
            feedbackLabel.setText("Richtig!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            clickedButton.setStyle("-fx-base: lightgreen;");
        } else {
            feedbackLabel.setText("Falsch! Die richtige Antwort war: " + findCorrectAnswerText());
            feedbackLabel.setStyle("-fx-text-fill: red;");
            clickedButton.setStyle("-fx-base: #ffc0c0;"); // light red
        }
        feedbackLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            currentQuestionIndex++;
            displayQuestion();
        });
        pause.play();
    }

    private String findCorrectAnswerText() {
        for (Answer answer : questions.get(currentQuestionIndex).getAnswers()) {
            if (answer.isCorrect()) {
                return answer.getText();
            }
        }
        return "";
    }


    private void setAnswerButtonsDisabled(boolean disabled) {
        for (Button button : answerButtons) {
            button.setDisable(disabled);
        }
    }

    private void showResults() {
        try {
            FXMLLoader loader = new FXMLLoader(QuizApplication.class.getResource("result-view.fxml"));
            Parent root = loader.load();

            ResultController resultController = loader.getController();
            resultController.initData(currentCategory, score, questions.size());

            Stage stage = (Stage) categoryLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
