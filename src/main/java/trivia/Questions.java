package trivia;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Questions {
    private static final int NUMBER_OF_QUESTIONS = 50;

    private Map<Category, Deque<String>> questions = new HashMap<>();
    {
        Stream.of(Category.values())
            .forEach(category -> questions.put(category, new ArrayDeque<>()));
    }

    public Questions() {
        for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
            questions.get(Category.POP).addLast("Pop Question " + i);
            questions.get(Category.SCIENCE).addLast("Science Question " + i);
            questions.get(Category.SPORTS).addLast("Sports Question " + i);
            questions.get(Category.ROCK).addLast("Rock Question " + i);
        }
    }

    public String nextQuestion(Category category) {
        return questions.get(category).removeFirst();
    }
}
