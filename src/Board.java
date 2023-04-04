
public class Board {
    private static final int NUMBRER_OF_ROWS = 6;
    private static final int NUMBER_OF_COLUMNS = 7;
    private static final int WINNING_SEQUENCE = 4;
    private static final char HUMAN_ID = 'X'; //Mänskliga-spelarens symbol
    private static final char AI_ID = 'O'; //AI-spelarens symbol
    private static final char EMPTY_ID = ' '; //Representerar en tom/ledigt plats
    private static final int MAX_SEARCH_DEPTH = 6;

    private char[][] board; //Används för illustration av den andra brädan under spelet. Värde ' ' -> Tom plats -- Värde 'O' -> AI -- Värde 'X' -> mänskliga spelaren
    private int[] columnHeight; //Håller info om varje kolumns höjd/antal element. Om höjden är 6 så kan spelaren inte använda kolumnen längre.

    /*
        Konstruktör. Initierar bräda-matrisen, arrayen som håller information om höjden av varje kolumn och de toma platserna i matrisen.
     */
    public Board(){
        this.board = new char[NUMBRER_OF_ROWS][NUMBER_OF_COLUMNS]; //Brädan är 6 x 7 i storlek då detta är standard för Connect4 spelet.
        this.columnHeight = new int[NUMBER_OF_COLUMNS];

        for(int i = 0; i < NUMBRER_OF_ROWS; i++){ //Initiering av värde i board-arrayen, alla är tomma från början.
            for(int j = 0; j < NUMBER_OF_COLUMNS; j++){
                this.board[i][j] = EMPTY_ID;
            }
        }
    }

    /*
        Placerar en symbol i brädan givet en kolumn och spelarsymbolen. Placerar symbolen i det nästa lediga platsen i kolumnen.
     */
    public void place(int column, char player) {
        int availableRow = NUMBRER_OF_ROWS - 1 - columnHeight[column]; //-1 då vi räknar från index 0
        if(player == HUMAN_ID){
            board[availableRow][column] = HUMAN_ID;
        } else {
            board[availableRow][column] = AI_ID;
        }
        columnHeight[column]++;
    }

    /*
        Tar bort en symbol i brädan givet en kolumn och spelarsymbolen.
     */
    public void unplace(int column) {
        int unplaceRow = NUMBRER_OF_ROWS - columnHeight[column];
        board[unplaceRow][column] = EMPTY_ID;
        columnHeight[column]--;
    }

    /*
        Kontrollerar om ett drag är tillåtet eller inte. Returnerar true om draget inte är tillåtet.
     */
    public boolean isIllegalMove(int column) {
        if(column < 0 || column >= NUMBER_OF_COLUMNS){
            return true;
        }
        return columnHeight[column] >= NUMBRER_OF_ROWS;
    }

    /*
        Kontrollerar om en vinst inträffade, givet en spelarsymbol.
        Returnerar true om spelaren har 4 symboler i rad i någon av horisontalerna, vertikalerna eller diagonalerna.
    */
    public boolean checkWinnerPlay(char player) {
        for(int i = 0; i < NUMBRER_OF_ROWS; i++){
            for(int j = 0; j < NUMBER_OF_COLUMNS; j++){
                char tmp = board[i][j];
                if(tmp == EMPTY_ID || tmp != player){
                    continue;
                }
                if(horizontalSequence(i, j, player) || verticalSequence(i, j, player) || diagonalLeftUpSequence(i, j, player) || diagonalRightUpSequence(i, j, player)){
                    return true;
                }
            }
        }
        return false;
    }

    /*
        Kontrollerar alla horisontaler som kan ge en sekvens av fyra symboler och räknar antalet symboler. Returnerar true om en av horisontalerna har en vinstsekvens.
    */
    private boolean horizontalSequence(int row, int column, int player) {
        if(column + 3 >= NUMBER_OF_COLUMNS) { //Returnerar false om sekvensen överstiger antalet kolumner.
            return false;
        }
        int count = 1; //platsen vi börjar på vet vi redan innehar spelarens symbol.
        for(int i = 1; i < WINNING_SEQUENCE; i++){
            if(board[row][column + i] == player){
                count++;
            } else {
                break;
            }
        }
        return count == WINNING_SEQUENCE;
    }

    /*
        Kontrollerar alla vertikaler som kan ge en sekvens av fyra symboler och räknar antalet symboler. Returnerar true om en av horisontalerna har en vinstsekvens.
    */
    private boolean verticalSequence(int row, int column, int player) {
        if(row + 3 >= NUMBRER_OF_ROWS) { //Returnerar false om sekvensen överstiger antalet rader.
            return false;
        }
        int count = 1;
        for(int i = 1; i < WINNING_SEQUENCE; i++){
            if(board[row + i][column] == player){
                count++;
            } else {
                break;
            }
        }
        return count == WINNING_SEQUENCE;
    }

    /*
        Kontrollerar alla diagonaler vänster om platsen som kan ge en sekvens av fyra symboler och räknar antalet symboler.
        Returnerar true om en av horisontalerna har en vinstsekvens.
    */
    private boolean diagonalLeftUpSequence(int row, int column, int player) {
        if(row - 3 < 0 || column - 3 < 0){ //Returnerar false om sekvensen har inte fyra i diagonalen.
            return false;
        }
        int count = 1;
        for(int i = 1; i < WINNING_SEQUENCE; i++){
            if(board[row - i][column - i] == player){
                count++;
            } else {
                break;
            }
        }
        return count == WINNING_SEQUENCE;
    }

    /*
        Kontrollerar alla diagonaler höger om platsen som kan ge en sekvens av fyra symboler och räknar antalet symboler.
        Returnerar true om en av horisontalerna har en vinstsekvens.
    */
    private boolean diagonalRightUpSequence(int row, int column, int player) {
        if(row - 3 < 0 || column + 3 >= NUMBER_OF_COLUMNS){ //Returnerar false om sekvensen har inte fyra i diagonalen.
            return false;
        }
        int count = 1;
        for(int i = 1; i < WINNING_SEQUENCE; i++){
            if(board[row - i][column + i] == player){
                count++;
            } else {
                break;
            }
        }
        return count == WINNING_SEQUENCE;
    }

    /*
        Returnerar den mänskliga spelarens symbol.
    */
    public char getHumanId() {
        return HUMAN_ID;
    }

    /*
        Returnerar den AI-spelarens symbol.
    */
    public char getAiId() {
        return AI_ID;
    }

    /*
        Givet en symbol, returnerar motståndsspelarens symbol.
    */
    public char getOpponentSymbol(int player) {
        return player == HUMAN_ID ? 'O' : 'X';
    }

    /*
        Evaluerar tillståndet av brädan givet en spelare symbol. Använder en betyg-mekanism från hjälpmetoden generateGrade() som returnerar ett betyg
        givet ett sekvens. Sekvensen räknar antal det angivna spelarens symboler och antal motståndsspelarens symboler.
        Metoden kollar varje möjligt sekvens av fyra i brädan, dvs horisontal, vertikal, diagonal från nedre vänster respektive högre vänster.
    */
    public int evaluateBoard(char player) {
        int grade = 0;
        //Check center, evaluerar den oxå då den ger bästa möjligheter för att koppla fyra i alla riktningar
        for(int i = 0; i < NUMBRER_OF_ROWS; i++){
            int count = 0;
            if(board[i][NUMBER_OF_COLUMNS - 4] == player){
                count++;
            }
            grade += count * 2;
        }
        //Horizontal, fyra stycken fyra positioner kollas i varje rad, från pos 0 till 3, 1 till 4, 2 till 5 och 3 till 6.
        for(int i = 0; i < NUMBRER_OF_ROWS; i++){
            for(int j = 0; j < NUMBER_OF_COLUMNS - 3; j++){ //-3 då bara första 4 kolumner i varje vad kan ge en sekvens av 4
                int playerCount = 0;
                int opponentCount = 0;
                for(int s = 0; s < WINNING_SEQUENCE; s++){
                    if(board[i][j + s] == player){
                        playerCount++;
                    } else if(board[i][j + s] == getOpponentSymbol(player)) {
                        opponentCount++;
                    }
                }
                grade += generateGrade(playerCount, opponentCount);
            }
        }
        //Vertical, samma logik som horizontel fast varje rad kollas från varje kollumn. Man går uppifrån ner.
        for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            for(int j = 0; j < NUMBRER_OF_ROWS - 3; j++){ //-3 då bara första 4 kolumner i varje vad kan ge en sekvens av 4
                int playerCount = 0;
                int opponentCount = 0;
                for(int s = 0; s < WINNING_SEQUENCE; s++){
                    if(board[j + s][i] == player){
                        playerCount++;
                    } else if(board[j + s][i] == getOpponentSymbol(player)) {
                        opponentCount++;
                    }
                }
                grade += generateGrade(playerCount, opponentCount);
            }
        }
        //Diagonal top-left to bottom-right.
        for(int i = 0; i < NUMBRER_OF_ROWS - 3; i++){
            for(int j = 0; j < NUMBER_OF_COLUMNS - 3; j++){ //-3 då bara första 4 kolumner i varje vad kan ge en sekvens av 4
                int playerCount = 0;
                int opponentCount = 0;
                for(int s = 0; s < WINNING_SEQUENCE; s++){
                    if(board[i + s][j + s] == player){
                        playerCount++;
                    } else if(board[i + s][j + s] == getOpponentSymbol(player)) {
                        opponentCount++;
                    }
                }
                grade += generateGrade(playerCount, opponentCount);
            }
        }
        //Diagonal bottom-left to top-right.
        for(int i = 3; i < NUMBRER_OF_ROWS; i++){
            for(int j = 0; j < NUMBER_OF_COLUMNS - 3; j++){ //-3 då bara första 4 kolumner i varje rad kan ge en sekvens av 4
                int playerCount = 0;
                int opponentCount = 0;
                for(int s = 0; s < WINNING_SEQUENCE; s++){
                    if(board[i - s][j + s] == player){
                        playerCount++;
                    } else if(board[i - s][j + s] == getOpponentSymbol(player)) {
                        opponentCount++;
                    }
                }
                grade += generateGrade(playerCount, opponentCount);
            }
        }

        return grade;
    }

    /*
        Beräknar och returnerar ett betyg beroende på hur många två, tre eller fyra i rad spelaren har, samt hur många tre i rad motståndsspelaren har.
        Motståndsspelarens tre i rad sekvenser kallas som threats(hot) och tar bort poäng från betyget.
     */
    private int generateGrade(int playerCount, int opponentCount) {
        int grade = 0;

        if(playerCount == 4) {
            grade += 100;
        } else if(playerCount == 3 && opponentCount == 0) {
            grade += 6;
        } else if(playerCount == 2 && opponentCount == 0) {
            grade += 2;
        } else if (opponentCount == 3 && playerCount == 0) { //Stor threat
            grade -= 4;
        }

        return grade;
    }

    /*
     *  Returnerar det bästa draget som AI-spelaren kan spela givet brädans nuvarande tillstånd.
     *  Om metoden inte är klar, dvs under de rekursiva anropen returnerar metoden bästa evalueringen av brädan.
     *  Argumentet player måste vara AI-spelarens symbol. Argumentet depth måste specificera det maximala djupet algoritmen rekursivt ska söka till.
     *  Argumenten alpha och beta ska vara ett extremt stor negativ respektive positiv tal.
     *
     *  Efter det mänskliga spelaren har spelat, anropas metoden och evaluerar alla möjliga drag givet ett nuvarande tillstånd av brädan
     *  samt det maximala djupet (dvs hur långt i framtiden metoden ska kolla), och returnerar det kolumnen som AI-spelaren ska spela.
     *
     *  @param  player  AI-spelarens symbol
     *  @param  depth   det maximala djupet för sökning
     *  @param  alpha   ett extremt stor negativ tal
     *  @param  beta    ett extremt stor positiv tal
     *  @return         kolumnen som producerar bästa möjliga draget eller det bästa evalueringen
     */
    public int minimax(char player, int depth, int alpha, int beta) {
        int bestEval = (player == AI_ID) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int columnToPlay = -1;
        if(checkWinnerPlay(AI_ID)) {
            return 100000000;
        }
        if(checkWinnerPlay(HUMAN_ID)) {
            return -100000000;
        }
        if(isDraw()) {
            return 0;
        }
        if(depth == 0) {
            return evaluateBoard(player);
        }

        for(int c = 0; c < NUMBER_OF_COLUMNS; c++){
            if(!isIllegalMove(c)){
                place(c, player);
                int eval = minimax(getOpponentSymbol(player), depth - 1, alpha, beta);
                unplace(c);

                if(player == AI_ID) {
                    if(eval > bestEval){
                        bestEval = eval;
                        columnToPlay = c;
                    }
                    alpha = Math.max(alpha, eval);
                } else {
                    if(eval < bestEval) {
                        bestEval = eval;
                        columnToPlay = c;
                    }
                    beta = Math.min(beta, eval);
                }
            }
            if(alpha >= beta) {
                break;
            }
        }
        return (depth == MAX_SEARCH_DEPTH) ? columnToPlay : bestEval;
    }

    private boolean gameOver() {
        return (isDraw() || checkWinnerPlay(AI_ID) || checkWinnerPlay(HUMAN_ID));
    }

    /*
        Returnerar true om det inte finns några lediga platser längre. Detta är sant om columnHeight arrayen har värdet 6 i varje index.
    */
    public boolean isDraw() {
        int countElements = 0;
        for(int i = 0; i < NUMBER_OF_COLUMNS; i++){
            countElements += columnHeight[i];
        }
        return countElements == (NUMBRER_OF_ROWS * NUMBER_OF_COLUMNS);
    }

    /*
        Returnerar symbolen på ett specifikt plats i brädan. Används för toString metoden.
    */
    private char getPositionValue(int r, int c) {
        return board[r][c];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("* ");
        for(int i = 0; i < NUMBER_OF_COLUMNS; i++){
            sb.append(" " + i + "  ");
        }
        sb.append("*\n");
        for(int i = 0; i < NUMBRER_OF_ROWS; i++){
            sb.append("*|");
            for(int j = 0; j < NUMBER_OF_COLUMNS; j++){
                sb.append(" " + getPositionValue(i, j));
                sb.append(" |");
            }
            sb.append("*\n");
        }
        sb.append("* ");
        for(int i = 0; i < NUMBER_OF_COLUMNS; i++){
            sb.append(" *  ");
        }
        sb.append("*");
        return sb.toString();
    }
}
