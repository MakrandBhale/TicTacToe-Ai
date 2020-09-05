import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    final static int[] board = new int[9];
    final static int x = 1;
    final static int o = 2;
    final static int MAX = 9;
    final static int WINNING_SCORE = 60;
    final static int BLOCKING_SCORE = 50;
    final static int WIN = 0;
    final static int BLOCK = 1;
    final static int DIAG = -1;
    final static int[][] winning_pos = {
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {2,4,6},

    };

    public static void main(String[] args) {

        if(!fillBoard()) return;


        print("\n\n All Possible Position");
        var possibleMoves = possibleMoveGenerator();
        for(int[] move : possibleMoves){
            printBoard(move);
        }
        nodeEvaluator(possibleMoves);
        print("\nNode Evaluation");
        for(int[] move : possibleMoves){
            printBoard(move);
        }
        //print("\n index is: " + calculateIndex());
    }


    public static void nodeEvaluator(int [][] possibleMoves) {
        int nowPlaying = nowPlaying();
        int res = 0;
        for(int[] move : possibleMoves) {
            if( (res = isWinning(move, nowPlaying)) > 0) {
                move[MAX] = res;
                //print("Winning Position");
                //break;
            } else if((res = isBlocking(move, nowPlaying)) > 0){
                move[MAX] = res;
            } else {
                move[MAX] = totalBlocksIncurred(move);
            }
        }

    }

    public static int totalBlocksIncurred (int[] move) {
        int count = 0;
        for (int i = 0; i < move.length; i++) {
            if(move[i] == board [i]) continue;
            for(int[] winningMove : winning_pos) {
                if(contains(winningMove, i)) count++;
            }
            break;
        }
        return count;
    }

    public static boolean contains(int[] arr, int val){
        for (int i : arr) {
            if(i == val) return true;
        }
        return false;
    }


    private static int isBlocking(int[] move, int nowPlaying) {
        for (int[] blockingMove : winning_pos) {
            if (checkSetExists(move, blockingMove)) continue;
            int nowPlayerCount = 0, oppoCount = 0;
            for(int i : blockingMove) {
                if(move[i] == nowPlaying) nowPlayerCount++;
                if(move[i] != nowPlaying && move[i] != 0) oppoCount++;
            }
            if(oppoCount == 2) return BLOCKING_SCORE;
        }
        return 0;
    }

    private static boolean checkSetExists(int[] set, int[] indices){
        for(int i : indices){
            if(set[i] != board[i]) return false;
        }
        return true;
    }

    public static int isWinning(int[] move, int nowPlaying) {
        boolean won = true;
        for (int[] winMove : winning_pos ) {

            for (int value : winMove) {
                if (move[value] != nowPlaying) {
                    won = false;
                    break;
                }
            }
            if (won) return WINNING_SCORE;
        }
        return 0;
    }

    public static int[][] possibleMoveGenerator() {
        int blankSpaces = calculateBlankSpace();
        int nowPlaying = nowPlaying();
        int [][] allPossibleMoves = new int[blankSpaces][MAX];

        int j = 0;
        for (int i = 0; i < board.length; i++) {
            if(board[i] != x && board[i] != o) {
                int[] possibleMove = copyArray(board);
                possibleMove[i] = nowPlaying;
                allPossibleMoves[j] = possibleMove;
                j++;

            }
        }
        return allPossibleMoves;
    }

    public static int[] copyArray(int [] source) {
        int [] target = new int[source.length + 1];
        System.arraycopy(source, 0, target, 0, source.length);
        return target;
    }

    private static int calculateBlankSpace() {
        int emptySpace = 0;
        for (int value : board) {
            if (value != x && value != o)
                emptySpace++;
        }
        return emptySpace;
    }

    public static boolean valid(String input) {
        if(input.length() != 9) return false;

        int xCount = 0, oCount = 0, emptyCount = 0;

        for (char c : input.toCharArray()) {
            if(c == 'x') xCount++;
            else if(c =='o') oCount++;
            else if(c == '.') emptyCount++;
            else return false;
        }

        return  (Math.abs(xCount - oCount) <= 1);
    }


    public static boolean fillBoard(){
        //String chars = input("Enter a board pos");
        //String chars = ".........";
        String chars = "xx.oo.ox.";
        char[] boardPos = chars.toCharArray();

        if(!valid(chars)) {
            print("Invalid board");
            return false;
        }

        for(int i = 0; i< board.length;i++){
            if(boardPos[i] == 'x') {
                board[i] = x;
            } else if(boardPos[i] == 'o') {
                board[i] = o;
            }
        }
        return true;
    }

    public static int calculateIndex(){
        int res = 0;
        int j = 0, i = 0;
        for(i = 0, j = 8;i<board.length;i++, j --){
            if(j == 0) continue;
            res = (int) (res + (board[i] * Math.pow(3, j)));
        }
        return res;
    }

    public static int nowPlaying() {
        int xCount = 0, oCount = 0;
        int firstPlayed = -1;
        for (int value : board) {
            if (firstPlayed == -1) firstPlayed = value;
            if (value == x) xCount++;
            else if (value == o) oCount++;
        }

        if(xCount > oCount) return o;
        else if(xCount < oCount) return x;
        else return firstPlayed;

    }

    public static void printBoard(){
        for (int i = 0; i < board.length; i++) {
            if(i%3 == 0) print("");
            if(board[i] == 0) System.out.print(".");
            if(board[i] == 1) System.out.print("x");
            if(board[i] == 2) System.out.print("o");

        }
    }

    public static void printBoard(int[] move) {
        for (int value : move) {
            //if(i%3 == 0) System.out.println();
            System.out.print(value + " ");
        }
        System.out.println();
    }
    public static String input(String message){
        Scanner myObj = new Scanner(System.in);
        System.out.println(message);
        //int i = Integer.parseInt();
        return myObj.nextLine();
    }

    public static void print(String message){
        System.out.println(message);
    }

}
