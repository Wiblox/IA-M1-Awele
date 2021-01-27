package awele.bot.NegaMax2;

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
    private static final int MAX_DEPTH = 3;
	
    /**
     * @throws InvalidBotException
     */
    public NegamaxBot() throws InvalidBotException
    {
        this.setBotName("NegaMax V2");
        this.addAuthor("Quentin BEAUPUY & Vivien KORPYS");
    }

    /**
     * Rien à faire
     */
    @Override
    public void initialize () { }

    /**
     * Pas d'apprentissage
     */
    @Override
    public void learn () { }

    /**
     * Sélection du coup selon l'algorithme MinMax
     */
    @Override
    public double [] getDecision (Board board)
    {
        NegamaxNode.initialize(board, NegamaxBot.MAX_DEPTH);
        return new NegamaxNode(board, 0, board.getCurrentPlayer(), Board.otherPlayer(board.getCurrentPlayer())).getDecision();
    }

    /**
     * Rien à faire
     */
    @Override
    public void finish () { }
}
