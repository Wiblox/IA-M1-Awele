package awele.bot.NegaScoutV2;

import awele.bot.NegaScout.NegamaxNode;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.HashMap;

/**
 * @author Alexandre Blansché Noeud d'un arbre MinMax
 */
public class NegaScoutV2Node {
    public static boolean train;
    /** Profondeur maximale */
    private static int maxDepth;
    
    /** Variable de genetique  */
    private static int i;
    private static int i1;
    private static int i2;
    private static int i3;
    private static int i4;
    private final long index;
    
    private double depth;
    
    /** L'évaluation du noeud */
    private double evaluation;
    
    /** Nodes */
    private static HashMap<Long, NegaScoutV2Node> nodes;
    
    
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

    public NegaScoutV2Node(Board board, double depth, int myTour, int opponentTour, double a, double b) {
        this.depth=depth;
        /* On crée index de notre situation */
    
        this.index = hash(board);//134234
        //this.nodes.put (this.index, this);
    
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
                            || (copy.getNbSeeds() <= 6) || !(depth < NegaScoutV2Node.maxDepth))
                        this.decision[i] = scoreEntireBoardById(copy, myTour);
                    /* Sinon, on explore les coups suivants */
                    else {
    
    
                            
                            /* Si le noeud n'a pas encore été calculé, on le construit */
                                /* On construit le noeud suivant */
                        if(i==0) {
                            NegaScoutV2Node child = negamax(copy, depth + 1, opponentTour, myTour, -b, -a);
                            this.decision[i] = -child.getEvaluation();
    
                        }else {
                            NegaScoutV2Node child = negamax(copy, depth + 1, opponentTour, myTour, -a-1, -a);
                            this.decision[i] = -child.getEvaluation();
                            if (a < this.decision[i] && this.decision[i] < b) {
                                child = negamax(copy, depth + 1, opponentTour, myTour, -b, -this.decision[i]);
                                this.decision[i] = -child.getEvaluation();
        
                            }
                        }
                            /* On récupère l'évaluation du noeud fils */
                           // this.decision[i] = -child.getEvaluation();
                        
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
    
                    if(depth>0){
                    a = Double.max(a, this.decision[i]);
                    if (a >= b) {
                        //System.out.println("Exit ");
                        break;
                    }
                }
                    
                } catch (InvalidBotException e) {
                    this.decision[i] = 0;
                }
            }
        }
    }
    
    
    private double getDepth() {
        
        return  depth;
    }
    
    
    private int scoreEntireBoardById(Board board, int myTour) {
    
    
        int score;
        int seeds;
    
        score = (25+i4) * (board.getScore(myTour) - board.getScore(Board.otherPlayer(myTour)));
        int total = 0;
        
        for (int i = 0; i < 6; i++) {
            seeds = board.getPlayerHoles()[i];
            if (seeds > 12)
                total += 28+this.i;
            else if (seeds == 0)
                total -= 54+i1;
            else if (seeds < 3)
                total -= 36+i2;
        }
    
        for (int i = 0; i < 6; i++) {
            seeds = board.getOpponentHoles()[i];
        
            if (seeds > 12)
                total -= 28+this.i;
            else if (seeds == 0)
                total += 54+i1;
            else if (seeds < 3)
                total += 36+i2;
        }
    
    return score-total;
    }

    private NegaScoutV2Node negamax(Board board, double depth, int myTour, int opponentTour, double i, double j) {
        return new NegaScoutV2Node(board, depth, myTour, opponentTour,i,j);
    }

    /**
     * Initialisation
     */
    protected static void initialize(int maxDepth, int i4, int i3, int i2, int i1, int i,HashMap <Long, NegaScoutV2Node> HashMap) {
        NegaScoutV2Node.nodes = HashMap;
        NegaScoutV2Node.maxDepth = maxDepth;
        NegaScoutV2Node.i=i;
        NegaScoutV2Node.i1=i1;
        NegaScoutV2Node.i2=i2;
        NegaScoutV2Node.i3=i3;
        NegaScoutV2Node.i4=i4;
    }
    
    /**
     * Récupération d'un noeud déjà calculé
     * @param index L'indice du noeud
     * @return Le noeud qui a l'indice indiqué ou null s'il n'a pas encore été calculé
     */
    public static NegaScoutV2Node getNode (long index)
    {
        return NegaScoutV2Node.nodes.get (index);
    }
    
    /**
     * @return Le nombre de noeuds calculées
     */
    public static int getNbNodes ()
    {
        return NegaScoutV2Node.nodes.size ();
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
