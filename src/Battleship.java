import java.util.*;
import java.io.*;

public class Battleship {
    private GameHelper helper = new GameHelper();
    private ArrayList<PlayBattleship> battleList = new ArrayList<PlayBattleship>();
    private int noOfGuesses = 0;

    private void setUpGame() {
        PlayBattleship ship1 = new PlayBattleship();
        ship1.setName("Maximus");
        PlayBattleship ship2 = new PlayBattleship();
        ship2.setName("Aloy");
        PlayBattleship ship3 = new PlayBattleship();
        ship3.setName("Agnes");
        battleList.add(ship1);
        battleList.add(ship2);
        battleList.add(ship3);
        System.out.println(battleList);

        System.out.println("Your goal is to sink the three ships: Maximus, Aloy and Agnes with the least number of guesses.");
        for (PlayBattleship shipToset : battleList) {
            ArrayList<String> newLocation = helper.placeShip(3);
            shipToset.setLocation(newLocation);
        }
    }

    private void startPlaying() {

        while(!battleList.isEmpty()) {
            String userGuess = helper.getUserInput("Enter a guess");
            checkUserGuess(userGuess);
        }
        finishGame();
    }


    private void checkUserGuess(String userGuess) {
        noOfGuesses++;
        String result = "miss";

        for(int i=0;i<battleList.size();i++) {
            result = battleList.get(i).checkTheGuess(userGuess);
            if(result.equals("hit")) {
                break;
            }
            if(result.equals("kill")) {
                battleList.remove(i);
                break;
            }
        }
        System.out.println(result);
    }

    void finishGame() {
        System.out.println("Wow!!! You sunk all the ships! Congrats!");
        if(noOfGuesses <= 18) {
            System.out.println("Score: Congrats, you sunk all ships in " + noOfGuesses + " guesses!");
        }
        else {
            System.out.println("You have exhausted your options! Sorry! Please try again!");
        }
    }

    public static void main(String[] args) {
        Battleship game = new Battleship();
        game.setUpGame();
        game.startPlaying();
    }
}




class PlayBattleship {
    private ArrayList<String> location;
    private String name;

    public void setLocation(ArrayList<String> battleList) {
        location = battleList;
    }

    public void setName(String n){
        name = n;
    }
    public String checkTheGuess(String choice) {
        String result = "miss";
        int index = location.indexOf(choice);
        if(index>=0) {
            location.remove(index);
            if (location.isEmpty()) {
                result = "kill";
                System.out.println("You sunk " + name + ":|");
            }
            else {
                result = "hit";
            }
        }
        return result;
    }
}


class GameHelper {
    private static final String alphabet = "abcdefg";
    private int gridlength = 7;
    private int gridSize = 49;
    private int[] grid = new int[gridSize];
    private int comcount = 0;

    public String getUserInput(String prompt) {
        String inputLine = null;
        System.out.println(prompt);
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            inputLine = input.readLine();
            if(inputLine.length() == 0) return null;
        }
        catch(IOException e){
            System.out.println("IOException: " + e);
        }
        return inputLine.toLowerCase();
    }

    public ArrayList<String> placeShip(int size) {
        ArrayList<String> cells = new ArrayList<String>();
        String temp = null;
        int [] curr = new int[size];
        int attempts = 0;
        boolean success = false;
        int location = 0;

        comcount++;
        int incr = 1;
        if((comcount % 2) == 0) {
            incr = gridlength;
        }

        while(!success & attempts++ < 200) {
            location = (int) (Math.random() * gridSize);
            int x = 0;
            success = true;
            while(success && x<size) {
                if(grid[location] == 0) {
                    curr[x++] = location;
                    location += incr;
                    if (location >= gridSize) {
                        success = false;
                    }
                    if (x>0 && (location % gridlength == 0)) {
                        success = false;
                    }
                }
                else {
                    success = false;
                }
            }
        }

        int x = 0;
        int row = 0;
        int column = 0;

        while (x < size) {
            grid[curr[x]] = 1;
            row = (int) (curr[x]/gridlength);
            column = curr[x] % gridlength;
            temp = String.valueOf(alphabet.charAt(column));

            cells.add(temp.concat(Integer.toString(row)));
            x++;
//            System.out.print(" co-ord "+x+" = " + cells.get(x-1).toUpperCase());
        }
        return cells;
    }
}