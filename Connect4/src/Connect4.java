import javafx.util.Pair;

import java.util.Random;
import java.util.Scanner;

public class Connect4 {

    Board board;
    private final int PLAYER1 = 1;
    private final int PLAYER2 = 2;

    private static int POSITION_HEURISTIC = 1;
    private static int ROWS = 7;
    private final char PLAYER1_SIGN = '1';
    private final char PLAYER2_SIGN = '2';
    private Heuristic heuristics;
    boolean gameOver = false;
    boolean draw = false;
    boolean AIvsAI = false;
    boolean isAlphabeta = false;
    private int turn;
    private int depth;
    private int nodes;
    private int p1Nodes;
    private int p2Nodes;
    private int heuristic;
    private int p1Heuristic;
    private int p2Heuristic;
    private long p1Time;
    private long p2Time;
    private Scanner scanner = new Scanner(System.in);

    public Connect4(boolean AivsAi, boolean isAlphabeta, int depth,int heuristic){
        this.AIvsAI = AivsAi;
        this.isAlphabeta = isAlphabeta;
        board = new Board();
        heuristics = new Heuristic();
        this.depth = depth;
        this.heuristic = heuristic;
    }

    public Connect4(boolean AivsAi, boolean isAlphabeta, int depth,int heuristic1, int heuristic2) {
        this.AIvsAI = AivsAi;
        this.isAlphabeta = isAlphabeta;
        board = new Board();
        heuristics = new Heuristic();
        this.depth = depth;
        this.p1Heuristic = heuristic1;
        this.p2Heuristic = heuristic2;
    }


    public Pair<Long,Integer> play(){


        long p1Time= 0;
        long p2Time = 0;
        int p1Moves = 0;
        int p2Moves = 0;
        //board.showBoard();
        turn = 1;//(new Random().nextInt(2)+1);
        char currentPlayerSign =' ';
        while(!gameOver){
            board.showBoard();
            System.out.println("TURA GRACZA " +turn );

            if(turn == PLAYER1){
                currentPlayerSign = PLAYER1_SIGN;
            }
            else{
                currentPlayerSign = PLAYER2_SIGN;
            }
            long start = System.currentTimeMillis();
            makeMove(turn,currentPlayerSign);
            if(turn == PLAYER1){
                p1Moves++;
                p1Nodes+= nodes;
                p1Time += System.currentTimeMillis() - start;
            }
            else{
                p2Moves++;
                p2Nodes += nodes;
                p2Time += System.currentTimeMillis() - start;
            }
            gameOver = board.checkWin(currentPlayerSign) || board.checkDraw();

            turn = (turn == 1) ? 2 : 1;
            nodes = 0;

        }
        board.showBoard();
        //gameover or draw
        turn = (turn == 1) ? 2 : 1;
        if(board.checkDraw()){
            System.out.println("REMIS");
        }
        else{
            System.out.println("GRACZ " + turn + " WYGRYWA (" + currentPlayerSign +")");
            long winTime;
            int winMoves;
            if(turn == PLAYER1){
                winTime = p1Time;
                winMoves = p1Moves;
            }
            else{
                winTime = p2Time;
                winMoves = p2Moves;

            }
            this.p1Time = p1Time;
            this.p2Time = p2Time;
 //           System.out.println("Czas wygrywajacego:"+winTime +"ms");
 //           System.out.println("Ilosc ruchow:"+winMoves);
            return new Pair<>(winTime,winMoves);
        }



        return new Pair<>((p1Time+p2Time)/2, (p1Moves+p2Moves)/2);
    }

    public void makeMove(int player, char playerSign){

        if(player == PLAYER1){
            if(!AIvsAI) {

                boolean allowedMove = false;
                while (!allowedMove) {
                    System.out.println("Wybierz kolumne:");
                    int column = scanner.nextInt();
                    if (board.isAllowed(column)) {
                        board.makeMove(column, playerSign);
                        allowedMove = true;
                    } else {
                        System.out.println("Kolumna jest pelna");
                    }
                }
            }
            else{
                if(board.isEmpty()){
                    board.makeMove(new Random().nextInt(ROWS),playerSign);
                    //board.makeMove(0,playerSign);
                }
                else{
                    Pair<Integer,Integer> move = minmax(depth, playerSign,true,Integer.MIN_VALUE,Integer.MAX_VALUE,p1Heuristic);
                    board.makeMove(move.getKey(),playerSign);
                }
            }
        }
        else if(player == PLAYER2){
            if(board.isEmpty()){
                board.makeMove(new Random().nextInt(ROWS),playerSign);
                //board.makeMove(6,playerSign);
            }
            else{
                Pair<Integer,Integer> move = minmax(depth, playerSign,true,Integer.MIN_VALUE,Integer.MAX_VALUE,p2Heuristic);
                board.makeMove(move.getKey(),playerSign);
            }
        }
    }

    private Pair<Integer,Integer> minmax(int depth, char maxSign, boolean maximize, int alpha, int beta, int heuristic){

        nodes++;
        char opponentSign;
        int value;
        opponentSign = (maxSign == PLAYER1_SIGN)? PLAYER2_SIGN : PLAYER1_SIGN;

        boolean[] validColumns = board.getValidColumns();
        if(depth == 0 || board.checkWin(maxSign) || board.checkWin(opponentSign) || board.checkDraw()){

            if(heuristic == POSITION_HEURISTIC){
                value = heuristics.evaluate(board,maxSign,opponentSign);
            }
            else{
                value = heuristics.evaluate2(board,maxSign,opponentSign);
            }
            return new Pair<>(null,value);
        }

        if(maximize){
            value = Integer.MIN_VALUE;
            Integer bestColumn = 0;
            for(int i = 6; i >=0; i--){
                if(validColumns[i]){
                    Pair<Integer,Integer> saveMove = board.makeMove(i,maxSign);
                    int newValue = minmax(depth-1, maxSign, false, alpha, beta,heuristic).getValue();

                    if(newValue > value){
                        value = newValue;
                        bestColumn = i;
                    }
                    board.undoMove(saveMove.getKey(),saveMove.getValue());

                    if(isAlphabeta){
                        alpha = Math.max(alpha,value);
                    }

                    if(isAlphabeta && alpha >= beta){
                        break;
                    }

                }

            }

            return new Pair<>(bestColumn,value);
        }
        else{
            value = Integer.MAX_VALUE;
            Integer bestColumn = 0;
            for(int i = 0; i < validColumns.length; i++){
                if(validColumns[i]){

                    Pair<Integer,Integer> saveMove = board.makeMove(i,opponentSign);
                    int newValue = minmax(depth-1, maxSign, true, alpha, beta,heuristic).getValue();

                    if(newValue < value){
                        value = newValue;
                        bestColumn = i;
                    }
                    board.undoMove(saveMove.getKey(),saveMove.getValue());

                    if(isAlphabeta){
                        beta = Math.min(beta,value);
                    }
                    if(isAlphabeta && alpha >= beta){
                        break;
                    }
                }
            }
            return new Pair<>(bestColumn,value);
        }
    }

    public static void testHeuristic(int heuristic, int iterations, int depth, boolean alphabeta){
        long timeSum = 0;
        int movesSum = 0;
        int nodes = 0;
        for(int i = 0; i < iterations; i++){
            Connect4 a = new Connect4(true,alphabeta,depth,heuristic);
            Pair<Long,Integer> results = a.play();
            timeSum += results.getKey();
            movesSum += results.getValue();
            nodes += (a.p1Nodes + a.p2Nodes)/2;
        }
        System.out.println(alphabeta +" "+ timeSum/iterations +" "+ movesSum/iterations +" "+ nodes/iterations);
    }

    public static void testHeuristic2(int heuristic1,int heuristic2, int iterations, int depth, boolean alphabeta){
        long timeSum = 0;
        int movesSum = 0;
        int nodes = 0;
        for(int i = 0; i < iterations; i++){
            Connect4 a = new Connect4(true,alphabeta,depth,heuristic1, heuristic2);
            Pair<Long,Integer> results = a.play();
            timeSum += results.getKey();
            movesSum += results.getValue();
            nodes += (a.p1Nodes + a.p2Nodes)/2;
        }
        System.out.println(alphabeta +" "+ timeSum/iterations +" "+ movesSum/iterations +" "+ nodes/iterations);
    }


    public static void main(String[] args){

        Connect4 test = new Connect4(false,true,8,1);
        test.play();
        int heuristic1 = 1;
        int heuristic2 = 2;
        int iterations = 7;
        int depth = 8;
        boolean alphabeta = true;
        //testHeuristic(heuristic1,iterations,depth,alphabeta);
        //testHeuristic(heuristic1,iterations,depth,!alphabeta);

        //testHeuristic2(heuristic1,heuristic2,iterations,depth,alphabeta);
    }

}
