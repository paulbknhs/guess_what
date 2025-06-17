package de.unihannover.hci.quiz.model;

import java.util.Collections;
import java.util.List;

public class Question {
    private final String questionText;
    private final List<Answer> answers;

    public Question(String questionText, List<Answer> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<Answer> getAnswers() {
        // Shuffle answers so the correct one isn't always in the same spot
        Collections.shuffle(answers);
        return answers;
    }
}
