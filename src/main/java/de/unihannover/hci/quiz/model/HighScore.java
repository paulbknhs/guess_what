package de.unihannover.hci.quiz.model;

public class HighScore {
  private final String categoryName;
  private final int score;
  private final int totalQuestions;
  private final String timestamp;

  public HighScore(String categoryName, int score, int totalQuestions, String timestamp) {
    this.categoryName = categoryName;
    this.score = score;
    this.totalQuestions = totalQuestions;
    this.timestamp = timestamp;
  }

  // Getters
  public String getCategoryName() {
    return categoryName;
  }

  public int getScore() {
    return score;
  }

  public int getTotalQuestions() {
    return totalQuestions;
  }

  public String getTimestamp() {
    return timestamp;
  }
}
