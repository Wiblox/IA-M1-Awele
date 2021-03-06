package awele.bot.NegaScout;

import awele.bot.NegaMaxAlphaBetaV3.ttEntry;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.*;

/**
 * @author Alexandre Blansché Noeud d'un arbre MinMax
 */
public class NegaScoutV2Node {
    /** Profondeur maximale */
    private static int maxDepth;
    private static HashMap<String, Integer> nodes;
    
    
    private LinkedList <Integer> coup;
    
    /** L'évaluation du noeud */
    private double evaluation;
    

    /** Évaluation des coups selon MinMax */
    private double[] decision;
  
    /**
     * Constructeur...
     * 
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud

     */

   
    public NegaScoutV2Node(Board board, double depth, int myTour, int opponentTour, double a, double b) {
    
        int bestnodes=0;
        Integer te = this.nodes.get(Arrays.toString(board.getPlayerHoles())+board.getScore(board.getCurrentPlayer())
                + Arrays.toString(board.getOpponentHoles())+board.getScore(Board.otherPlayer(board.getCurrentPlayer()))
        );
      
        coup = new LinkedList<Integer>  ();
        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        this.decision = new double [Board.NB_HOLES];
        /* Initialisation de l'évaluation courante */
        this.evaluation = -Double.MAX_VALUE;
        for (int i = 0; i < Board.NB_HOLES; i++) {
            coup.add(i);
        }  if(te!=null){
            coup.remove(te);
            coup.addFirst(te);
        }
    
        int i;
            for (int j = 0; j < Board.NB_HOLES; j++) {
                i=coup.get(j);
            /* Si le coup est jouable */
            if (board.getPlayerHoles()[i] != 0) {
                /* Sélection du coup à jouer */
                double[] decision = new double[Board.NB_HOLES];
                decision[i] = 1;
                /* On copie la grille de jeu et on joue le coup sur la copie */
                Board copy;
                try {
                    copy = board.playMoveSimulationBoard(myTour, decision);
                    /*
                     * Si la nouvelle situation de jeu est un coup qui met fin à la partie, on
                     * évalue la situation actuelle
                     */
                    if ((copy.getScore(myTour) < 0) || (copy.getScore(opponentTour) >= 25)
                            || (copy.getNbSeeds() <= 6) || !(depth < NegaScoutV2Node.maxDepth))
                        this.decision[i] = scoreEntireBoardById(copy, myTour,opponentTour);
                        /* Sinon, on explore les coups suivants */
                    else {
                        /* Si le noeud n'a pas encore été cal culé, on le construit */
                        /* On construit le noeud suivant */
                        if(i==0) {
                            NegaScoutV2Node child = new NegaScoutV2Node(copy, depth + 1, opponentTour, myTour, -b, -a);
                            this.decision[i] = -child.getEvaluation();
                        } else {
                            NegaScoutV2Node child = new NegaScoutV2Node(copy, depth + 1, opponentTour, myTour, -a-1, -a);
                            this.decision[i] = -child.getEvaluation();
                            if (a < this.decision[i] && this.decision[i] < b) {
                                child = new NegaScoutV2Node(copy, depth + 1, opponentTour, myTour, -b, -this.decision[i]);
                                this.decision[i] = -child.getEvaluation();
                            }
                        }
            
                    
                    }
                    /*
                     * L'évaluation courante du noeud est mise à jour, selon le type de noeud
                     */
                    if (this.decision[i] > this.evaluation) {
                        this.evaluation = this.decision[i];
                        bestnodes=i;
                    }
                    a = Double.max(a, this.decision[i]);
    
                    if(depth>0){
                        if (a >= b) {
                            break;
                        }
                    }
                
                } catch (InvalidBotException e) {
                    this.decision[i] = 0;
                }
            }
        }
        //this.nodes.put(Arrays.toString(board.getPlayerHoles())+board.getScore(board.getCurrentPlayer())
          //      + Arrays.toString(board.getOpponentHoles())+board.getScore(Board.otherPlayer(board.getCurrentPlayer()))  , bestnodes);
    }
    
    
    
    
    
    private int scoreEntireBoardById(Board board, int myTour,int opponentTour) {
    
        int total = 0;
        int[] seedsPlayer = board.getPlayerHoles(),
                seedsOpponent = board.getOpponentHoles();
    
        for (int i = 0; i < 6; i++) {
            int seedP = seedsPlayer[i],
                    seedO = seedsOpponent[i];
            if (seedP > 12 ^ seedO > 12)
                total += seedP > 12 ? 28 : -28;
            else if (seedP == 0 ^ seedO == 0)
                total += seedP == 0 ? -54 : 54;
            else if (seedP < 3 ^ seedO < 3)
                total += seedP < 3 ? -36 : 36;
        }
    
        return 25 * (board.getScore(myTour) - board.getScore(opponentTour) )- total;
    }


    /**
     * Initialisation
     */
    protected static void initialize(int maxDepth) {
        NegaScoutV2Node.maxDepth = maxDepth;
        NegaScoutV2Node.nodes=new HashMap<String, Integer>();
    }
    



    /**
     * L'évaluation du noeud
     * 
     * @return L'évaluation du noeud
     */
    double getEvaluation() {
        return this.evaluation;
    }

    /**
     * L'évaluation de chaque coup possible pour le noeud
     * 
     * @return
     */
    double[] getDecision() {
        return this.decision;
    }
}
