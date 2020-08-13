import javafx.util.Pair;

public class Board {

    private char[][] board;
    private static int ROWS = 6;
    private static int COLUMNS = 7;

    public Board(){
        board = new char[ROWS][COLUMNS];
    }
    public char[][] getBoard(){
        return board;
    }
    public boolean[] getValidColumns(){
        boolean [] columns = new boolean[COLUMNS];
        for(int j = 0; j < board[0].length; j++){
            if(board[0][j] == '\0'){
                columns[j] = true;
            }
            else{
                columns[j] = false;
            }
        }
        return columns;
    }

    public void showBoard(){
        System.out.println("----------------------");
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == '\0'){
                    System.out.printf("%3s",'-');
                }
                else{
                    System.out.printf("%3s",board[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println("----------------------");
    }

    public Pair<Integer,Integer> makeMove(int column, char playerSign){
        int j;
        for(j = board.length-1; j >= 0 ; j--){
            if(board[j][column] == '\0'){
                board[j][column] = playerSign;
                return new Pair<>(j,column);
            }
        }
        if(j == -1){
            System.out.println("ILLEGAL MOVE NO SPACE IN COLUMN" + column);
        }
        return null;
    }
    public void undoMove(int row, int column){
        board[row][column] = '\0';
    }

    public boolean checkWin(char playerSign){
        int winningSeriesSize = 4;
        int rows = board.length;
        int columns = board[0].length;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){

                if(board[i][j] == playerSign){

                    int seriesSize = 1;
                    int ii = i;
                    int jj = j;

                    //check right
                    while(ii < ROWS && jj+1 < COLUMNS && board[ii][jj+1] == playerSign){
                        seriesSize++;
                        jj++;
                        if(seriesSize == winningSeriesSize){
                            //System.out.println(ii+"R "+jj);
                            return true;
                        }
                    }
                    //check down
                    seriesSize = 1;
                    ii = i;
                    jj = j;
                    while(ii+1 < ROWS && jj < COLUMNS && board[ii+1][jj] == playerSign){
                        seriesSize++;
                        ii++;
                        if(seriesSize == winningSeriesSize){
                            //System.out.println(ii+"D "+jj);
                            return true;
                        }
                    }
                    //check diagonal_right
                    seriesSize = 1;
                    ii = i;
                    jj = j;
                    while(ii+1 < ROWS && jj+1 < COLUMNS && board[ii+1][jj+1] == playerSign){
                        seriesSize++;
                        ii++;
                        jj++;
                        if(seriesSize == winningSeriesSize){
                            //System.out.println(ii+" DR"+jj);
                            return true;
                        }
                    }
                    //check diagonal_left
                    seriesSize = 1;
                    ii = i;
                    jj = j;
                    while(ii+1 < ROWS && jj-1 >= 0 && board[ii+1][jj-1] == playerSign){
                        seriesSize++;
                        ii++;
                        jj--;
                        if(seriesSize == winningSeriesSize){
                            //System.out.println(i+"DL "+j);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkDraw(){
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j] == '\0'){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isAllowed(int column){
        for(int i = 0; i < board.length; i++){
            if(board[i][column] == '\0'){
                return true;
            }
        }
        return false;
    }
    public boolean isEmpty(){
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j] != '\0'){
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args){
       Board b = new Board();
       b.showBoard();

       b.makeMove(3,'X');
        b.makeMove(3,'X');
        b.makeMove(3,'X');
        b.makeMove(5,'G');
        b.makeMove(5,'G');
        b.makeMove(6,'G');
        b.makeMove(6,'G');
        b.makeMove(6,'G');
        b.makeMove(6,'X');
        b.makeMove(6,'G');
        b.makeMove(6,'X');

        b.showBoard();
        boolean[] c = b.getValidColumns();
        for(int i = 0; i < 7;i++){
            System.out.println(c[i]);
        }
        System.out.println(b.checkWin('G'));
    }
}
