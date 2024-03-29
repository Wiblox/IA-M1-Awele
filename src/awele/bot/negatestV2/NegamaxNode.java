package awele.bot.negatestV2;

import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * @author Alexandre Blansché Noeud d'un arbre MinMax
 */
public class NegamaxNode {
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

    public NegamaxNode(Board board, double depth, int myTour, int opponentTour,double a,double b) {
        this.depth=depth;
        /* On crée index de notre situation */
    
    
        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        this.decision = new double [Board.NB_HOLES];
        /* Initialisation de l'évaluation courante */
        this.evaluation = -Double.MAX_VALUE;
        Board copy;
        double[] decisionTemp = new double[Board.NB_HOLES];

        for (int i = 0; i < Board.NB_HOLES; i++) {
            /* Si le coup est jouable */
            if (board.getPlayerHoles()[i] != 0) {
                /* Sélection du coup à jouer */
                decisionTemp[i] = 1*(i+1);
                /* On copie la grille de jeu et on joue le coup sur la copie */
               // Board copy = (Board) board.clone();
                try {
                    //int score_tmp = copy.playMoveSimulationScore(copy.getCurrentPlayer(), decision);
                    copy = board.playMoveSimulationBoard(myTour, decisionTemp);
                    int score_tmp = copy.getScore(myTour);
           
                    
                    
                    if ((score_tmp < 0) || (copy.getScore(opponentTour) >= 25)
                            || (copy.getNbSeeds() <= 6) || !(depth < NegamaxNode.maxDepth))
                        this.decision[i] = scoreEntireBoardById(copy, myTour,opponentTour);
                    /* Sinon, on explore les coups suivants */
                    else {
    
    
                            
                            /* Si le noeud n'a pas encore été calculé, on le construit */
                                /* On construit le noeud suivant */
                        NegamaxNode child = new NegamaxNode(copy, depth + 1, opponentTour, myTour, -b, -a);
                          
                            /* On récupère l'évaluation du noeud fils */
                            this.decision[i] = -child.getEvaluation();
                        
                        /*
                         * Sinon (si la profondeur maximale est atteinte), on évalue la situation
                         * actuelle
                         */
                      
                    }
                    /*
                     * L'évaluation courante du noeud est mise à jour, selon le type de noeud
                     * (MinNode ou MaxNode)
                     */
                    if (this.decision[i] > this.evaluation) {
                        this.evaluation = this.decision[i];
                    }
    
                    if(depth>1){
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
    
    

    
    private int scoreEntireBoardById(Board board, int myTour,int opponentTour) {
    
    
        int score;
        int seeds;
    
        score = 25 * (board.getScore(myTour) - board.getScore(opponentTour));
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


    /**
     * Initialisation
     */
    protected static void initialize(int maxDepth) {
        NegamaxNode.maxDepth = maxDepth;
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
