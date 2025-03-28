package trivia;

public enum Category {
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
