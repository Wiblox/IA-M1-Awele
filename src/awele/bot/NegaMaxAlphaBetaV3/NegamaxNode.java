package awele.bot.NegaMaxAlphaBetaV3;


import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    private static Map<String, ttEntry> nodes;
    public static int nbutilisable;
    
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
        
    
        boolean exit =false;
        double alphaOrig = a;
        ttEntry te = this.nodes.get(Arrays.toString(board.getPlayerHoles())+board.getScore(board.getCurrentPlayer())
                + Arrays.toString(board.getOpponentHoles())+board.getScore(Board.otherPlayer(board.getCurrentPlayer()))
        );
       
        
        
        if(te != null && te.getDepth() <= depth ) {// A verifier
            nbutilisable++;
            if(te.getFlag() == Flag.EXACT ) {
                this.evaluation= te.getValue();
                exit =true;
            }
            else if(te.getFlag() == Flag.LOWERBOUND) {
                a = Double.max(a, te.getValue());
            }
            else if(te.getFlag() == Flag.UPPERBOUND) {
                b = Double.min(b, te.getValue());
            }
            if(a >= b) {
                this.evaluation= te.getValue();
                exit =true;
    
            }
        }
  
    
        this.decision = new double [Board.NB_HOLES];
        this.depth=depth;
    
        if(!exit){
        /* On crée index de notre situation */
            this.evaluation = -Double.MAX_VALUE;

    
        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        /* Initialisation de l'évaluation courante */
            double[] decision = new double[Board.NB_HOLES];

        for (int i = 0; i < Board.NB_HOLES; i++) {
            /* Si le coup est jouable */
            if (board.getPlayerHoles()[i] != 0) {
                /* Sélection du coup à jouer */
                decision[i] = 1*(i+1);
                /* On copie la grille de jeu et on joue le coup sur la copie */
                Board copy;
           
                try {
                    copy = board.playMoveSimulationBoard(myTour, decision);
                    /*
                     * Si la nouvelle situation de jeu est un coup qui met fin à la partie, on
                     * évalue la situation actuelle
                     */
                    if ((copy.getScore(myTour) < 0) || (copy.getScore(opponentTour) >= 25)
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
            if(te==null){
                te = new ttEntry();
            }
        te.setValue(this.evaluation);
        if(this.evaluation <= alphaOrig) {
            te.setFlag(Flag.UPPERBOUND);
        }
        else if(this.evaluation >= b) {
            te.setFlag(Flag.LOWERBOUND);
        }
        else {
            te.setFlag(Flag.EXACT);
        }
        te.setDepth(depth);
        this.nodes.put(Arrays.toString(board.getPlayerHoles())+board.getScore(board.getCurrentPlayer())
                + Arrays.toString(board.getOpponentHoles())+board.getScore(Board.otherPlayer(board.getCurrentPlayer()))  , te);
        }
        
    }
    
    

    
    
    private int scoreEntireBoardById(Board board, int myTour,int opponentTour) {
    
    
        int score;
        int seeds;
    
        score = (25+i4) * (board.getScore(myTour) - board.getScore(opponentTour));
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
    

    /**
     * Initialisation
     */
    protected static void initialize(int maxDepth, Map<String, ttEntry> nodees,int nbutilisable) {
        NegamaxNode.maxDepth = maxDepth;
        NegamaxNode.nodes=nodees;
        NegamaxNode.nbutilisable=nbutilisable;
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
