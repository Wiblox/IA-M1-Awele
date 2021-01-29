package awele.bot.NegaMax6;

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
    private static int i3;
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
    
    public NegamaxNode(Board board, double depth, int myTour, int opponentTour,double a,double b) {
        this.depth=depth;
        
        this.decision = new double [Board.NB_HOLES];
        this.evaluation = -Double.MAX_VALUE;
        
        for (int i = 0; i < Board.NB_HOLES; i++) {
            if (board.getPlayerHoles()[i] != 0) {
                double[] decision = new double[Board.NB_HOLES];
                decision[i] = 1;
                Board copy = (Board) board.clone();
                try {
                    int score_tmp = copy.playMoveSimulationScore(copy.getCurrentPlayer(), decision);
                    copy = copy.playMoveSimulationBoard(copy.getCurrentPlayer(), decision);
                    if ((score_tmp < 0) || (copy.getScore(Board.otherPlayer(copy.getCurrentPlayer())) >= 25)
                            || (copy.getNbSeeds() <= 6) || !(depth < NegamaxNode.maxDepth))
                        this.decision[i] = scoreEntireBoardById(copy, myTour);
                    else {
    
                       
                        if(i==0){
                            NegamaxNode child =   negamax(copy, depth+1, opponentTour, myTour,-b,-a);
                        this.decision[i] = -child.getEvaluation();
                        }else {
                            NegamaxNode child =   negamax(copy, depth+2, opponentTour, myTour,-b,-a);
                             this.decision[i] = -child.getEvaluation();
                             if(a < this.decision[i] && this.decision[i] <b){
                                  child =   negamax(copy, depth+1, opponentTour, myTour,-b,-this.decision[i]);
                                this.decision[i] = -child.getEvaluation();
                            }
                        }
                    }
    
    
                    if (this.decision[i] > this.evaluation) {
                        this.evaluation = this.decision[i];
                    }
                    if(depth>0){
        
                        a = Double.max(a, this.decision[i]);
                        if (a >= b) {
                            break;
                        }}
                } catch (InvalidBotException e) {
                    this.decision[i] = 0;
                    System.out.println("ERREUR");
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
    
    private NegamaxNode negamax(Board board, double depth, int myTour, int opponentTour,double i,double j) {
        return new NegamaxNode(board, depth, myTour, opponentTour,i,j);
    }
    
    /**
     * Initialisation
     */
    protected static void initialize(int maxDepth, int i4, int i3, int i2, int i1, int i,HashMap <Long, NegamaxNode> HashMap) {
        NegamaxNode.nodes = HashMap;
        NegamaxNode.maxDepth = maxDepth;
        NegamaxNode.i=i;
        NegamaxNode.i1=i1;
        NegamaxNode.i2=i2;
        NegamaxNode.i3=i3;
        NegamaxNode.i4=i4;
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
