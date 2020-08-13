public class Heuristic {


    private int[][] evaluationTable =
            {{3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3}};

    //SOURCE
    // https://github.com/coderodde/ConnectFour/blob/master/src/main/java/net/coderodde/games/connect/four/impl/WeightMatrixConnectFourStateEvaluatorFunction.java
    public int evaluate(Board board, char maxSign, char minSign) {
        //board.showBoard();
        if (board.checkWin(maxSign)) {

            return Integer.MAX_VALUE-1;
        } else if (board.checkWin(minSign)) {

            return Integer.MIN_VALUE+1;
        }

        if(board.checkDraw()){
            return 0;
        }

        return points(maxSign, board.getBoard()) - (points(minSign, board.getBoard()));


    }

    private int points(char playerSign, char[][] board) {

        int value = 0;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++){
                if (board[i][j] == playerSign){
                    value += evaluationTable[i][j];
                }
            }

        return value;

    }

    //SOURCE
    //http://web.media.mit.edu/~msaveski/projects/2009_connect-four.html
    public int evaluate2(Board boardObj, char maxSign, char minSign){

        int [] weights = {0,1,4,9,10000};
        int [] maxOccurences = new int [5];
        int [] minOccurences = new int [5];
        char empty = '\0';


        char [][] board = boardObj.getBoard();
        int ROWS = board.length;
        int COLUMNS = board[0].length;




        for(int i = 0; i < board[0].length;i++){
            for(int j = 0; j < board.length-3;j++){
                int maxCount = 0;
                int minCount = 0;
                int ii = i;
                int jj = j;

                if(maxCount != 0 && minCount == 0){
                    maxOccurences[maxCount]++;
                }
                else if(maxCount == 0 && minCount != 0){
                    minOccurences[minCount]++;
                }
            }
        }

        //dright
        //dleft

        for(int i = 0; i < ROWS; i ++){
            for(int j = 0; j < COLUMNS ; j++){


                int maxCount = 0;
                int minCount = 0;
                //right
                if(j+3<COLUMNS){
                    for(int k = 0; k < 4; k++){
                        char boardSign = board[i][j+k];
                        if(boardSign == maxSign){
                            maxCount++;
                        }
                        else if(boardSign == minSign) {
                            minCount++;
                        }
                    }
                    if(maxCount != 0 && minCount == 0){
                        maxOccurences[maxCount]++;

                    }
                    else if(maxCount == 0 && minCount != 0){
                        minOccurences[minCount]++;
                    }
                }
                //down
                maxCount = 0;
                minCount = 0;
                if(i+3<ROWS){
                    for(int k = 0; k < 4; k++){
                        char boardSign = board[i+k][j];
                        if(boardSign == maxSign){
                            maxCount++;
                        }
                        else if(boardSign == minSign) {
                            minCount++;
                        }
                    }
                    if(maxCount != 0 && minCount == 0){
                        maxOccurences[maxCount]++;

                    }
                    else if(maxCount == 0 && minCount != 0){
                        minOccurences[minCount]++;
                    }
                }

                //diagr
                maxCount = 0;
                minCount = 0;
                if(i+3<ROWS && j+3<COLUMNS){
                    for(int k = 0; k < 4; k++){
                        char boardSign = board[i+k][j+k];
                        if(boardSign == maxSign){
                            maxCount++;
                        }
                        else if(boardSign == minSign) {
                            minCount++;
                        }
                    }
                    if(maxCount != 0 && minCount == 0){
                        maxOccurences[maxCount]++;

                    }
                    else if(maxCount == 0 && minCount != 0){
                        minOccurences[minCount]++;
                    }
                }

                //diagl
                maxCount = 0;
                minCount = 0;
                if(i+3<ROWS && j>=COLUMNS-4){
                    for(int k = 0; k < 4; k++){
                        char boardSign = board[i+k][j-k];
                        if(boardSign == maxSign){
                            maxCount++;
                        }
                        else if(boardSign == minSign) {
                            minCount++;
                        }
                    }
                    if(maxCount != 0 && minCount == 0){

                        maxOccurences[maxCount]++;
                    }
                    else if(maxCount == 0 && minCount != 0){
                        minOccurences[minCount]++;
                    }
                }


            }
        }

        
        int maxScore = maxOccurences[1]*weights[1] +
                        maxOccurences[2]*weights[2]+
                        maxOccurences[3]*weights[3]+
                        maxOccurences[4]*weights[4];
        int minScore = minOccurences[1]*weights[1] +
                minOccurences[2]*weights[2]+
                minOccurences[3]*weights[3]+
                minOccurences[4]*weights[4];

        return maxScore - minScore;

    }






    public static void main(String[] args){
        Board b = new Board();
        Heuristic h= new Heuristic();
        b.showBoard();

        b.makeMove(1,'X');
        b.makeMove(1,'X');


        b.showBoard();

        System.out.println(h.evaluate2(b,'X','G'));
    }


}
