package de.unihannover.hci.quiz.db;

import de.unihannover.hci.quiz.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
  private static final String DB_URL = "jdbc:sqlite:quiz.db";

  private static Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(DB_URL);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  public static List<Category> getCategories() {
    String sql = "SELECT id, name FROM Categories";
    List<Category> categories = new ArrayList<>();
    try (Connection conn = connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        categories.add(new Category(rs.getInt("id"), rs.getString("name")));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return categories;
  }

  public static List<Question> getQuestionsForCategory(int categoryId) {
    String sql = "SELECT id, question_text FROM Questions WHERE category_id = ?";
    List<Question> questions = new ArrayList<>();

    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, categoryId);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        int questionId = rs.getInt("id");
        String questionText = rs.getString("question_text");
        List<Answer> answers = getAnswersForQuestion(questionId);
        questions.add(new Question(questionText, answers));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return questions;
  }

  private static List<Answer> getAnswersForQuestion(int questionId) {
    String sql = "SELECT answer_text, is_correct FROM Answers WHERE question_id = ?";
    List<Answer> answers = new ArrayList<>();
    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, questionId);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        answers.add(new Answer(rs.getString("answer_text"), rs.getInt("is_correct") == 1));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return answers;
  }

  public static void saveHighScore(String categoryName, int score, int totalQuestions) {
    String sql = "INSERT INTO HighScores(category_name, score, total_questions) VALUES(?,?,?)";
    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, categoryName);
      pstmt.setInt(2, score);
      pstmt.setInt(3, totalQuestions);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static List<HighScore> getHighScores(String categoryName) {
    String sql = "SELECT category_name, score, total_questions, timestamp " +
        "FROM HighScores WHERE category_name = ? " +
        "ORDER BY score DESC, timestamp DESC LIMIT 10";
    List<HighScore> highScores = new ArrayList<>();
    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, categoryName);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        highScores.add(new HighScore(
            rs.getString("category_name"),
            rs.getInt("score"),
            rs.getInt("total_questions"),
            rs.getString("timestamp")));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return highScores;
  }
}
