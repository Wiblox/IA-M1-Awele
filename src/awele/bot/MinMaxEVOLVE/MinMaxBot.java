package awele.bot.MinMaxEVOLVE;

import awele.bot.Bot;
import awele.bot.DemoBot;
import awele.core.Board;
import awele.core.InvalidBotException;
import com.google.common.collect.Lists;
import quickml.data.instances.ClassifierInstance;
import quickml.supervised.ensembles.randomForest.randomDecisionForest.RandomDecisionForest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Alexandre Blansché
 * Bot qui prend ses décisions selon le MinMax
 */
public class MinMaxBot extends DemoBot
{
    
    private RandomDecisionForest randomForest;
    
    
    /** Profondeur maximale */
    //Possible bug nb pair
    private static final int MAX_DEPTH = 4;
	
    /**
     * @throws InvalidBotException
     */
    public MinMaxBot () throws InvalidBotException
    {
        this.setBotName ("MinMaxBOOSTED");
        this.addAuthor ("Vivien KORPYS");
    }

    /**
     * Rien à faire
     */
    @Override
    public void initialize ()
    {
    }

    /**
     * Pas d'apprentissage
     */
    @Override
    public void learn ()
    {

    }
    
    private void entrainement(int secondes) throws NoSuchFieldException, IllegalAccessException, InvalidBotException {
        Field currentPlayerField = Board.class.getDeclaredField("currentPlayer");
        currentPlayerField.setAccessible(true);
        List<ClassifierInstance> dataset = null;
    
        long startTime = System.currentTimeMillis();
        int iterations = 0;
        Deque<AweleGame> nextGames = new ArrayDeque<AweleGame>();
        nextGames.add(new AweleGame());
        while (System.currentTimeMillis() < startTime + secondes*1000)
        {
            boolean end = false;
            AweleGame aweleGame = nextGames.remove();
            int currentPlayer = aweleGame.currentPlayer;
            double[] score = aweleGame.score;
            List<List<Map.Entry<Board, Integer>>> situations = new ArrayList<List<Map.Entry<Board, Integer>>>(2);
            situations.add(new ArrayList<Map.Entry<Board, Integer>>());
            situations.add(new ArrayList<Map.Entry<Board, Integer>>());
            Board game = aweleGame.board;
            this.initialize();
            while (!end)
            {
                currentPlayerField.set(game, currentPlayer);
                double[] decision = this.getDecision(game);
                
                // On détermine une décision alternative (on choisit la deuxième meilleure solution).
                int decisionMax = 0;
                for(int i = 1; i < decision.length; i++)
                    decisionMax = (decision[decisionMax] < decision[i]) ? i : decisionMax;
                double[] alternativeDecision = Arrays.copyOf(decision, decision.length);
                alternativeDecision[decisionMax] = 0;
                Board alternativeBoard = game.playMoveSimulationBoard(currentPlayer, alternativeDecision);
                
                situations.get(currentPlayer).add(new AbstractMap.SimpleEntry<Board, Integer>((Board) game.clone(), decisionMax));
                
                double moveScore = game.playMoveSimulationScore(currentPlayer, decision);
                if (moveScore < 0)
                    end = true;
                else
                {
                    score[currentPlayer] += moveScore;
                    game = game.playMoveSimulationBoard(currentPlayer, decision);
                }
                
                if ((score[currentPlayer] >= 25) || (game.getNbSeeds() <= 6))
                    end = true;
                else
                    currentPlayer = Board.otherPlayer(currentPlayer);
                
                if (!end)
                {
                    nextGames.add(new AweleGame(score, currentPlayer, game));
                    nextGames.add(new AweleGame(score, currentPlayer, alternativeBoard));
                }
            }
            
            if (score[currentPlayer] < score[Board.otherPlayer(currentPlayer)])
                currentPlayer = Board.otherPlayer(currentPlayer);
            
            // currentPlayer a gagné !
            List<Map.Entry<Board, Integer>> winSituations = situations.get(currentPlayer);
            List<Map.Entry<Board, Integer>> loseSituations = situations.get(Board.otherPlayer(currentPlayer));
            ClassifierInstance test =null;
            //dataset.add(1,situations.get(currentPlayer));
            
            
            iterations++;
        }
        
        System.out.println(iterations + " itérations.");
    
    
       
    }
    public static  List<ClassifierInstance> load(Board b) throws IOException {
        final List<ClassifierInstance> instances = Lists.newLinkedList();
        
        String[] headings = new String[]{"sepal-length", "sepal-width", "petal-length", "petal-width"};
        
        
        
        return null;
        
    }
    
    private class AweleGame
    {
        public double[] score = new double[]{0, 0};
        public int currentPlayer = 0;
        public Board board = new Board();
        
        public AweleGame()
        {}
        public AweleGame(double[] score, int currentPlayer, Board board)
        {
            this();
            this.score = score.clone();
            this.currentPlayer = currentPlayer;
            this.board = (Board) board.clone();
        }
    }
    /**
     * Sélection du coup selon l'algorithme MinMax
     */
    @Override
    public double [] getDecision (Board board)
    {
        MinMaxNode.initialize (board, MinMaxBot.MAX_DEPTH,randomForest);
        return new MaxNode(board).getDecision ();
    }

    /**
     * Rien à faire
     */
    @Override
    public void finish ()
    {
    }
}
