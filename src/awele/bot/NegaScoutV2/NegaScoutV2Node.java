package awele.bot.NegaScoutV2;

import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.HashMap;

/**
 * @author Alexandre Blansché Noeud d'un arbre MinMax
 */
public class NegaScoutV2Node {
    /** Profondeur maximale */
    private static int maxDepth;
    
    
    private double depth;
    
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



        this.depth=depth;

        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        this.decision = new double [Board.NB_HOLES];
        /* Initialisation de l'évaluation courante */
        this.evaluation = -Double.MAX_VALUE;

        for (int i = 0; i < Board.NB_HOLES; i++) {
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
                        this.decision[i] = scoreEntireBoardById(copy, myTour);
                        /* Sinon, on explore les coups suivants */
                    else {
                        /* Si le noeud n'a pas encore été calculé, on le construit */
                        /* On construit le noeud suivant */
                        if(i==0) {
                            NegaScoutV2Node child = negamax(copy, depth + 1, opponentTour, myTour, -b, -a);
                            this.decision[i] = -child.getEvaluation();
                        } else {
                            NegaScoutV2Node child = negamax(copy, depth + 1, opponentTour, myTour, -a-1, -a);
                            this.decision[i] = -child.getEvaluation();
                            if (a < this.decision[i] && this.decision[i] < b) {
                                child = negamax(copy, depth + 1, opponentTour, myTour, -b, -this.decision[i]);
                                this.decision[i] = -child.getEvaluation();
                            }
                        }
                            /* On récupère l'évaluation du noeud fils */
                           // this.decision[i] = -child.getEvaluation();
                        
                      
                    }
                    /*
                     * L'évaluation courante du noeud est mise à jour, selon le type de noeud
                     */
                    if (this.decision[i] > this.evaluation) {
                        this.evaluation = this.decision[i];
                    }
    
                    if(depth>0){
                    a = Double.max(a, this.decision[i]);
                    if (a >= b) {
                        break;
                    }
                }
                    
                } catch (InvalidBotException e) {
                    this.decision[i] = 0;
                }
            }
        }
    }
    
    
    
    
    private int scoreEntireBoardById(Board board, int myTour) {
    
    
        int score;
        int seeds;
    
        score = (25) * (board.getScore(myTour) - board.getScore(Board.otherPlayer(myTour)));
        int total = 0;
        
        for (int i = 0; i < 6; i++) {
            seeds = board.getPlayerHoles()[i];
            if (seeds > 12)
                total += 28;
            else if (seeds == 0)
                total -= 54;
            else if (seeds < 3)
                total -= 36;
        }
    
        for (int i = 0; i < 6; i++) {
            seeds = board.getOpponentHoles()[i];
        
            if (seeds > 12)
                total -= 28;
            else if (seeds == 0)
                total += 54;
            else if (seeds < 3)
                total += 36;
        }
    
    return score-total;
    }

    private NegaScoutV2Node negamax(Board board, double depth, int myTour, int opponentTour, double i, double j) {
        return new NegaScoutV2Node(board, depth, myTour, opponentTour,i,j);
    }

    /**
     * Initialisation
     */
    protected static void initialize(int maxDepth,HashMap <Long, NegaScoutV2Node> HashMap) {
        NegaScoutV2Node.maxDepth = maxDepth;
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
