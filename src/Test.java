import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b);
        Scanner sc = new Scanner(System.in);
        char player = b.getHumanId(); //Human börjar

        //False från början, true om nån vinner (har 4 i rad, eller det blir draw, inga mer platser och ingen van)
        while(true){
            if(b.isDraw()){
                System.out.println(b);
                System.out.println("Game Over! It's a draw!");
                break;
            }

            if(player == b.getHumanId()){
                System.out.print("Player X is on the move. Select a column to play: ");
                int move = Integer.parseInt(sc.nextLine());
                if(b.isIllegalMove(move)){
                    System.out.println("Illegal move. Collumns to choose from are 0-6!");
                    continue;
                }
                b.place(move, player);
                System.out.println(b);
                if(b.checkWinnerPlay(player)){
                    System.out.println("Human has won! AI is a loser!");
                    break;
                }
                player = b.getAiId();
            } else {
                System.out.println("AI thinking...");
                int move = b.minimax(b.getAiId(), 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
                b.place(move, player);
                System.out.println(b);
                if(b.checkWinnerPlay(player)){
                    System.out.println("AI has won! AI is superior to the human!");
                    break;
                }
                player = b.getHumanId();
            }
        }
        sc.close();
    }
}
