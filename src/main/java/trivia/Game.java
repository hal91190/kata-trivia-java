package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Player {
   private final String name;
   private int position;
   private int purse;
   private boolean inPenaltyBox;

   public Player(String name) {
      this.name = name;
      this.position = 0;
      this.purse = 0;
      this.inPenaltyBox = false;
   }

   public String getName() {
      return name;
   }

   public boolean isInPenaltyBox() {
      return inPenaltyBox;
   }

   public void advances(int roll) {
      this.position = (this.position + roll) % Game.NUMBER_OF_PLACES;
   }

   public int getPosition() {
      return position + 1;
   }

   public void gainsCoin() {
      this.purse++;
   }

   public int getPurse() {
      return purse;
   }

   public void sendToPenaltyBox() {
      this.inPenaltyBox = true;
   }

   public boolean hasWon() {
      return getPurse() < Game.NUMBER_OF_COINS_TO_WIN;
   }

   public boolean isFree() {
      return !isInPenaltyBox();
   }
}

enum Category {
   POP("Pop"), SCIENCE("Science"), SPORTS("Sports"), ROCK("Rock");

   private String name;

   Category(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }
}

class Board {
   private final List<Category> categories =
      List.of(
         Category.POP, Category.SCIENCE, Category.SPORTS, Category.ROCK,
         Category.POP, Category.SCIENCE, Category.SPORTS, Category.ROCK,
         Category.POP, Category.SCIENCE, Category.SPORTS, Category.ROCK
         );

   public Category getCategory(Player player) {
      return getCategory(player.getPosition());
   }

   public Category getCategory(int position) {
      position--;
      if (position < 0 || position >= categories.size()) {
         throw new IllegalArgumentException("Invalid index");
      }
      return categories.get(position);
   }
}

public class Game implements GameInterface {
   public static final int NUMBER_OF_COINS_TO_WIN = 6;

   public static final int NUMBER_OF_PLACES = 12;

   List<Player> players = new ArrayList<>();

   List<String> popQuestions = new LinkedList<>();
   List<String> scienceQuestions = new LinkedList<>();
   List<String> sportsQuestions = new LinkedList<>();
   List<String> rockQuestions = new LinkedList<>();

   int currentPlayerIndex = 0;
   boolean isGettingOutOfPenaltyBox;

   private Board board = new Board();

   public Game() {
      for (int i = 0; i < 50; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast("Science Question " + i);
         sportsQuestions.addLast("Sports Question " + i);
         rockQuestions.addLast("Rock Question " + i);
      }
   }

   public boolean add(String playerName) {
      var player = new Player(playerName);
      players.add(player);

      System.out.println(player.getName() + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public void roll(int roll) {
      var currentPlayer = players.get(currentPlayerIndex);
      System.out.println(currentPlayer.getName() + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (currentPlayer.isInPenaltyBox()) {
         if (roll % 2 != 0) {
            System.out.println(currentPlayer.getName() + " is getting out of the penalty box");
            isGettingOutOfPenaltyBox = true;
         } else {
            System.out.println(currentPlayer.getName() + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
            return;
         }
      }
      currentPlayer.advances(roll);

      System.out.println(currentPlayer.getName()
                           + "'s new location is "
                           + currentPlayer.getPosition());
      System.out.println("The category is " + board.getCategory(currentPlayer));
      askQuestion();
   }

   private void askQuestion() {
      var currentPlayer = players.get(currentPlayerIndex);
      var category = board.getCategory(currentPlayer.getPosition());
      switch (category) {
         case POP -> System.out.println(popQuestions.removeFirst());
         case SCIENCE -> System.out.println(scienceQuestions.removeFirst());
         case SPORTS -> System.out.println(sportsQuestions.removeFirst());
         case ROCK -> System.out.println(rockQuestions.removeFirst());
      }
   }

   public boolean handleCorrectAnswer() {
      var currentPlayer = players.get(currentPlayerIndex);
      boolean hasWon = true;
      if (currentPlayer.isFree() || isGettingOutOfPenaltyBox) {
         System.out.println("Answer was correct!!!!");
         currentPlayer.gainsCoin();
         System.out.println(currentPlayer.getName()
                              + " now has "
                              + currentPlayer.getPurse()
                              + " Gold Coins.");

         hasWon = currentPlayer.hasWon();
      }
      currentPlayerIndex++;
      if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
      return hasWon;
   }

   public boolean wrongAnswer() {
      var currentPlayer = players.get(currentPlayerIndex);
      System.out.println("Question was incorrectly answered");
      System.out.println(currentPlayer.getName() + " was sent to the penalty box");
      currentPlayer.sendToPenaltyBox();

      currentPlayerIndex++;
      if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
      return true;
   }
}
