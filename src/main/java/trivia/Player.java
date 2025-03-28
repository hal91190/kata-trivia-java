package trivia;

public class Player {
   private final String name;
   private int position;
   private int coins;
   private boolean inPenaltyBox;

   public Player(String name) {
      this.name = name;
      this.position = 0;
      this.coins = 0;
      this.inPenaltyBox = false;
   }

   public String name() {
      return name;
   }

   public int position() {
      return position + 1;
   }

   public void advances(int roll) {
      this.position = (this.position + roll) % Game.NUMBER_OF_PLACES;
   }

   public int coins() {
      return coins;
   }

   public void gainsCoin() {
      this.coins++;
   }

   public boolean hasNotWon() {
      return coins() < Game.NUMBER_OF_COINS_TO_WIN;
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
