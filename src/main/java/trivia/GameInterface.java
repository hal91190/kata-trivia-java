package trivia;

public interface GameInterface {
    boolean add(String playerName);

    void roll(int roll);

    boolean handleCorrectAnswer();
    
    boolean wrongAnswer();
}
