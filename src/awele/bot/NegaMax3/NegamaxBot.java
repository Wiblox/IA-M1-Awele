package awele.bot.NegaMax3;

import awele.bot.Bot;
import awele.bot.DemoBot;
import awele.core.Awele;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexandre Blansché
 * Bot qui prend ses décisions selon le MinMax
 */
public class NegamaxBot extends DemoBot
{
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 5;

    
    /** Variable de genetique  */
    HashMap<Long, NegamaxNode> HashMap ;
    
    /**
     * @throws InvalidBotException
     */
    public NegamaxBot() throws InvalidBotException
    {
        this.setBotName("NegaMax V3");
        this.addAuthor("Quentin BEAUPUY & Vivien KORPYS");
        
    }
    


    
    /**
     * Fonction d'initalisation du bot
     * Cette fonction est appelée avant chaque affrontement
     */
    @Override
    public void initialize() {
        HashMap  = new HashMap <Long, NegamaxNode>();
        NegamaxNode.initialize( NegamaxBot.MAX_DEPTH,0,0,0,0,0,HashMap);
    
    }
    
    
 

    /**
     * Pas d'apprentissage
     */
    @Override
    public void learn () {
        
    
        // trainning();
        //working();
        
    }
    

    

    /**
     * Sélection du coup selon l'algorithme MinMax
     */
    @Override
    public double [] getDecision (Board board)
    { long start = System.currentTimeMillis();
        double[] res = new NegamaxNode(board, 0, board.getCurrentPlayer(), Board.otherPlayer(board.getCurrentPlayer()),-9999,9999).getDecision();
    
        long end = System.currentTimeMillis();
        try {
            File myObj = new File("log_negamaxV3.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj.getAbsoluteFile(), true);
            myWriter.write((end - start)+"\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;}

    /**
     * Rien à faire
     */
    @Override
    public void finish () {
    }
}
