import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
/**
    * Contains all of the static statistics (such as total minions sent per game) to be saved and/or shown at the end of the game.
    * @author Ben Cuan
    * @since 10 May 2017
*/
public class Statistics {

    /** *Total number of records being kept */
    private static final int ENTRIES = 8;

    public static int[]
        //Records: Best scores for the game that was just played. These are not saved unless they beat the global records (below).
        gameMinions = new int[2],
        gameMoney = new int[2],
        mostExpensiveMinion = new int[2],
        gameDamageDealt = new int[2];

    public static int
        winner,

        //Totals which include every game played on this computer.
        totalMinions,
        totalMoney,
        mostExpensiveMinionEver,
        totalDamageDealt,
        
        //Records: Best scores for a single game.
        gameMinionsRecord,
        gameMoneyRecord,
        mostExpensiveMinionRecord,
        gameDamageDealtRecord;

    private static Scanner fileScanner;
    private static PrintWriter pw;
    public Statistics () {
        //FileIO: Read from records.stats
        refreshScanner();
    }

    /**
     * Writes given statistic based on ID.
     * @param statID The id of the statistic to be written. SEE BELOW FOR ID VALUES.
     * @param newValue The value of the statistic (ex. number of minions). 
     * @param recordHolder Name of the person who achieved the record.
     * @param IDVALUES:
     * @param 1 totalMinions
     * @param 2 totalMoney
     * @param 3 mostExpensiveMinionEver
     * @param 4 totalDamageDealt
     * @param 5 gameMinionsRecord
     * @param 6 gameMoneyRecord
     * @param 7 mostExpensiveMinionRecord
     * @param 8 gameDamageDealtRecord
    */
    public static void writeStatistic (int statID, int newValue, String recordHolder) {
        //intialize fileIO
        refreshScanner();

        String[] tempList = new String[ENTRIES];

        for(int i = 0; i < ENTRIES; i++)
            tempList[i] = readStatisticString(i + 1);
        
        tempList[statID - 1] = newValue + " | " + recordHolder;

        try {
            pw = new PrintWriter(new FileWriter(new File("records.stats")));
            for(int i = 0; i < ENTRIES; i++)
                pw.println(tempList[i]);
            pw.close();
        } catch (IOException e) {
            System.err.println("[ERROR] writeStatistic() failed: " + e);
        }
    }

     /**
     *Reads given statistic based on ID. This overload is used for comparing integer values.
     * @return int
     * @param statID The id of the statistic to be written. SEE BELOW FOR ID VALUES.
     * @param IDVALUES:
     * @param 1 totalMinions
     * @param 2 totalMoney
     * @param 3 mostExpensiveMinionEver
     * @param 4 totalDamageDealt
     * @param 5 gameMinionsRecord
     * @param 6 gameMoneyRecord
     * @param 7 mostExpensiveMinionRecord
     * @param 8 gameDamageDealtRecord
    */
    public static int readStatistic (int statID) {
        try {
            int temp1 = Integer.parseInt((readStatisticString(statID).substring(0, readStatisticString(statID).indexOf('|')).trim()));
            return(temp1);
        } catch (Exception e) { //No value- return 0
            return 0;
        }
    }

    /**
     *Reads given statistic based on ID. This overload is used to get a formatted string of information for displaying.
     * @return String
     * @param statID The id of the statistic to be written. SEE BELOW FOR ID VALUES.
     * @param IDVALUES:
     * @param 1 totalMinions
     * @param 2 totalMoney
     * @param 3 mostExpensiveMinionEver
     * @param 4 totalDamageDealt
     * @param 5 gameMinionsRecord
     * @param 6 gameMoneyRecord
     * @param 7 mostExpensiveMinionRecord
     * @param 8 gameDamageDealtRecord
    */
    public static String readStatisticString (int statID) {
        
        refreshScanner();

            try {

            for(int i = 0; i < statID - 1; i++)
                fileScanner.nextLine();
            
            String temp = fileScanner.nextLine();
            return temp;
        } catch (NoSuchElementException e) {resetStatistics();} //Corrupt or invalid records.stats- requires refresh 

        return "";
    }

    /** *Resets all stats to 0. */
    public static void resetStatistics() {
        try {
             pw = new PrintWriter(new FileWriter(new File("records.stats")));
             for(int i = 0; i < ENTRIES; i++) {
                 pw.println("---");
             }
             pw.close();
            } catch(IOException ex) { System.err.println("[ERROR] IOException while writing a new records.stats: " + ex);}
    }

    /** *Only resets per-game statistics. Run at the start of each game. */
    public static void softResetStatistics() {
        gameMinions = new int[2];
        gameMoney = new int[2];
        mostExpensiveMinion = new int[2];
        gameDamageDealt = new int[2];
        winner = 0;
    }

    /**
     *Reads given statistic based on ID. This overload is used to get a formatted string of information for displaying.
     * @param statID The id of the statistic to be written. SEE BELOW FOR ID VALUES.
     * @param valueToAdd Integer value to add onto the existing value.
     * @param IDVALUES:
     * @param 1 totalMinions
     * @param 2 totalMoney
     * @param 3 mostExpensiveMinionEver
     * @param 4 totalDamageDealt
     * @param 5 gameMinionsRecord
     * @param 6 gameMoneyRecord
     * @param 7 mostExpensiveMinionRecord
     * @param 8 gameDamageDealtRecord
    */
    public static void addToValue (int statID, int valueToAdd) {
        if(valueToAdd != 0) writeStatistic(statID, (readStatistic(statID) + valueToAdd), "");
    }

    //Resets the scanner.
    public static void refreshScanner () {
        try { fileScanner.close(); } catch (Exception e) {} //Scanner not initialized if this does not run

        try { //Create a new scanner
             fileScanner = new Scanner (new File("records.stats"));
        } catch (FileNotFoundException e) {
            //File doesn't exist. That's ok, just make a new one! :D
            resetStatistics();
        }
    }

}