package trivia;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

   public int getPosition() {
      return position + 1;
   }

   public void advances(int roll) {
      this.position = (this.position + roll) % Game.NUMBER_OF_PLACES;
   }

   public int getPurse() {
      return purse;
   }

   public void gainsCoin() {
      this.purse++;
   }

   public boolean hasNotWon() {
      return getPurse() < Game.NUMBER_OF_COINS_TO_WIN;
   }

   public boolean isInPenaltyBox() {
      return inPenaltyBox;
   }

   public boolean isFree() {
      return !isInPenaltyBox();
   }

   public void sendToPenaltyBox() {
      this.inPenaltyBox = true;
   }

   public void leavePenaltyBox() {
      if (isFree()) {
         throw new IllegalStateException(name + " is not in the penalty box");
      }
      this.inPenaltyBox = false;
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

   private List<Player> players = new ArrayList<>();

   private Map<Category, Deque<String>> questions = new HashMap<>();
   {
      Stream.of(Category.values())
         .forEach(category -> questions.put(category, new ArrayDeque<>()));
   }

   private Player currentPlayer;
   boolean isGettingOutOfPenaltyBox;

   private Board board = new Board();

   public Game() {
      for (int i = 0; i < 50; i++) {
         questions.get(Category.POP).addLast("Pop Question " + i);
         questions.get(Category.SCIENCE).addLast("Science Question " + i);
         questions.get(Category.SPORTS).addLast("Sports Question " + i);
         questions.get(Category.ROCK).addLast("Rock Question " + i);
      }
   }

   public boolean add(String playerName) {
      var player = new Player(playerName);
      players.add(player);
      currentPlayer = players.getFirst();

      System.out.println(player.getName() + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public void roll(int roll) {
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
      var category = board.getCategory(currentPlayer);
      System.out.println("The category is " + category);
      System.out.println(questions.get(category).removeFirst());
   }

   public boolean handleCorrectAnswer() {
      boolean hasNotWon = true;
      if (currentPlayer.isFree() || isGettingOutOfPenaltyBox) {
         System.out.println("Answer was correct!!!!");
         currentPlayer.gainsCoin();
         System.out.println(currentPlayer.getName()
                              + " now has "
                              + currentPlayer.getPurse()
                              + " Gold Coins.");

         hasNotWon = currentPlayer.hasNotWon();
      }
      nextPlayer();
      return hasNotWon;
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(currentPlayer.getName() + " was sent to the penalty box");
      currentPlayer.sendToPenaltyBox();

      nextPlayer();
      return true;
   }

   private void nextPlayer() {
      int currentPlayerIndex = players.indexOf(currentPlayer);
      currentPlayer = currentPlayerIndex < players.size() - 1 ?
         players.get(currentPlayerIndex + 1) : players.get(0);
   }
}
