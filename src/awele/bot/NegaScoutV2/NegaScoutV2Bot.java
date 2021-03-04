package awele.bot.NegaScoutV2;

import awele.bot.DemoBot;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Alexandre Blansché
 * Bot qui prend ses décisions selon le MinMax
 */
public class NegaScoutV2Bot extends DemoBot {
    /**
     * Profondeur maximale
     */
    private static final int MAX_DEPTH = 6;


    /**
     * Variable de genetique
     */
    HashMap<Long, NegaScoutV2Node> HashMap;

    /**
     * @throws InvalidBotException
     */
    public NegaScoutV2Bot() throws InvalidBotException {
        this.setBotName("NegaScout  V2");
        this.addAuthor("Quentin BEAUPUY & Vivien KORPYS");

    }


    /**
     * Fonction d'initalisation du bot
     * Cette fonction est appelée avant chaque affrontement
     */
    @Override
    public void initialize() {
        HashMap = new HashMap<Long, NegaScoutV2Node>();
        NegaScoutV2Node.initialize(NegaScoutV2Bot.MAX_DEPTH, 0, 0, 0, 0, 0, HashMap);

    }


    /**
     * Pas d'apprentissage
     */
    @Override
    public void learn() {
        // trainning();
        //working();
    }


    /**
     * Sélection du coup selon l'algorithme MinMax
     */
    @Override
    public double[] getDecision(Board board) {
        long start = System.currentTimeMillis();
        double[] res = new NegaScoutV2Node(board, 0, board.getCurrentPlayer(), Board.otherPlayer(board.getCurrentPlayer()), -9999, 9999).getDecision();

        long end = System.currentTimeMillis();
        try {
            File myObj = new File("log_negascoutV2.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj.getAbsoluteFile(), true);
            myWriter.write((end - start) + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Rien à faire
     */
    @Override
    public void finish() {
    }
}