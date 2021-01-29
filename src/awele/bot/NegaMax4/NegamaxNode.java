package awele.bot.NegaMax4;

import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.HashMap;

/**
 * @author Alexandre Blansché Noeud d'un arbre MinMax
 */
public class NegamaxNode {
    public static boolean train;
    /** Profondeur maximale */
    private static int maxDepth;
    
    /** Variable de genetique  */
    private static int i;
    private static int i1;
    private static int i2;
    // private static int i3;
    private static int i4;
    
    private double depth;
    
    /** L'évaluation du noeud */
    private double evaluation;
    
    /** Nodes */
    private static HashMap<Long, NegamaxNode> nodes;
    
    
    /** Évaluation des coups selon MinMax */
    private double[] decision;
    /**
     * Returns an unique hash identifier for the current board. The
     * produced hash code is a 44 bit number which uniquely identifies
     * a position and turn.
     *
     * @return  The hash code value for this object
     */
    public long hash(Board board) {
        long test = board.hashCode();
        return test;
    }
    /**
     * Constructeur...
     * 
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud

     */

    public NegamaxNode(Board board, double depth, int myTour, int opponentTour) {
        this.depth=depth;
        /* On crée index de notre situation */
    
    
    
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
                Board copy = (Board) board.clone();
                try {
                    int score_tmp = copy.playMoveSimulationScore(copy.getCurrentPlayer(), decision);
                    copy = copy.playMoveSimulationBoard(copy.getCurrentPlayer(), decision);
                    /*
                     * Si la nouvelle situation de jeu est un coup qui met fin à la partie, on
                     * évalue la situation actuelle
                     */
                    if ((score_tmp < 0) || (copy.getScore(Board.otherPlayer(copy.getCurrentPlayer())) >= 25)
                            || (copy.getNbSeeds() <= 6))
                        this.decision[i] = scoreEntireBoardById(copy, myTour);
                    /* Sinon, on explore les coups suivants */
                    else {
                        /* Si la profondeur maximale n'est pas atteinte */
                        if (depth < NegamaxNode.maxDepth) {
    
    
                         
                                /* On construit le noeud suivant */
                                NegamaxNode child =  negamax(copy, depth+1, opponentTour, myTour);
                         
                            
                            /* On récupère l'évaluation du noeud fils */
                            this.decision[i] = -child.getEvaluation();
                        }
                        /*
                         * Sinon (si la profondeur maximale est atteinte), on évalue la situation
                         * actuelle
                         */
                        else
                            this.decision[i] = scoreEntireBoardById(copy, myTour);
                    }
                    /*
                     * L'évaluation courante du noeud est mise à jour, selon le type de noeud
                     * (MinNode ou MaxNode)
                     */
                    if (this.decision[i] > this.evaluation) {
                        this.evaluation = this.decision[i];
                    }
                } catch (InvalidBotException e) {
                    this.decision[i] = 0;
                }
            }
        }
    }
    
    
    // private double getDepth() {
    //     return  depth;
    // }
    
    
    private int scoreEntireBoardById(Board board, int myTour) {
    
    
        int score;
        int seeds;
    
        score = (25+i4) * (board.getScore(myTour) - board.getScore(Board.otherPlayer(myTour)));
        int total = 0;
        
        for (int i = 0; i < 6; i++) {
            seeds = board.getPlayerHoles()[i];
            if (seeds > 12)
                total += 28+ NegamaxNode.i;
            else if (seeds == 0)
                total -= 54+i1;
            else if (seeds < 3)
                total -= 36+i2;
        }
    
        for (int i = 0; i < 6; i++) {
            seeds = board.getOpponentHoles()[i];
        
            if (seeds > 12)
                total -= 28+NegamaxNode.i;
            else if (seeds == 0)
                total += 54+i1;
            else if (seeds < 3)
                total += 36+i2;
        }
    
    return score-total;
    }

    private NegamaxNode negamax(Board board, double depth, int myTour, int opponentTour) {
        return new NegamaxNode(board, depth, myTour, opponentTour);
    }

    /**
     * Initialisation
     */
    protected static void initialize(int maxDepth) {
        NegamaxNode.maxDepth = maxDepth;
    }
    
    /**
     * Récupération d'un noeud déjà calculé
     * @param index L'indice du noeud
     * @return Le noeud qui a l'indice indiqué ou null s'il n'a pas encore été calculé
     */
    public static NegamaxNode getNode (long index)
    {
        return NegamaxNode.nodes.get (index);
    }
    
    /**
     * @return Le nombre de noeuds calculées
     */
    public static int getNbNodes ()
    {
        return NegamaxNode.nodes.size ();
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
