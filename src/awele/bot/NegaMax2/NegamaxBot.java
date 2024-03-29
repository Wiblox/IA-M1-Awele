package awele.bot.NegaMax2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import awele.bot.Bot;
import awele.bot.DemoBot;
import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * @author Alexandre Blansché Bot qui prend ses décisions selon le MinMax
 */
public class NegamaxBot extends Bot {
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 5;

    /**
     * @throws InvalidBotException
     */
    public NegamaxBot() throws InvalidBotException {
        this.setBotName("NegaMax V2");
        this.addAuthor("Quentin BEAUPUY & Vivien KORPYS");
    }

    /**
     * Rien à faire
     */
    @Override
    public void initialize() {
    }

    /**
     * Pas d'apprentissage
     */
    @Override
    public void learn() {
    }

    /**
     * Sélection du coup selon l'algorithme MinMax
     */
    @Override
    public double[] getDecision(Board board) {
        long start = System.currentTimeMillis();
        NegamaxNode.initialize(NegamaxBot.MAX_DEPTH );
        double[] res = new NegamaxNode(board, 0, board.getCurrentPlayer(), Board.otherPlayer(board.getCurrentPlayer()))
                .getDecision();
        long end = System.currentTimeMillis();
        try {
            File myObj = new File("log_negamaxV2.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj.getAbsoluteFile(), true);
            myWriter.write((end - start)+"\n");
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
