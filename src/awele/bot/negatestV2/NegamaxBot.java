package awele.bot.negatestV2;

import awele.bot.DemoBot;
import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * @author Alexandre Blansché
 * Bot qui prend ses décisions selon le MinMax
 */
public class NegamaxBot extends DemoBot
{
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 9 ;

    
    
    /**
     * @throws InvalidBotException
     */
    public NegamaxBot() throws InvalidBotException
    {
        this.setBotName("NegaMax  TEST elagage reduit");
        this.addAuthor("Quentin BEAUPUY & Vivien KORPYS");
        
    }
    


    
    /**
     * Fonction d'initalisation du bot
     * Cette fonction est appelée avant chaque affrontement
     */
    @Override
    public void initialize() {
        NegamaxNode.initialize( NegamaxBot.MAX_DEPTH);
    
    }
    
    
 

    /**
     * Pas d'apprentissage
     */
    @Override
    public void learn () {
    }
    

    

    /**
     * Sélection du coup selon l'algorithme MinMax
     */
    @Override
    public double [] getDecision (Board board)
    {
        double[] res = new NegamaxNode(board, 0, board.getCurrentPlayer(), Board.otherPlayer(board.getCurrentPlayer()),-9999,9999).getDecision();
    
       
        return res;}

    /**
     * Rien à faire
     */
    @Override
    public void finish () {
    }
}
