package awele.bot.negamax;

import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * @author Alexandre Blansché Noeud d'un arbre MinMax
 */
public class NegamaxNode {
    /** Numéro de joueur de l'IA */
    private static int player;

    /** Profondeur maximale */
    private static int maxDepth;

    /** L'évaluation du noeud */
    private double evaluation;

    /** Évaluation des coups selon MinMax */
    private double[] decision;

    /**
     * Constructeur...
     * 
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta  Le seuil pour la coupe beta
     */
    
    public NegamaxNode(Board board, double depth, int color) {
        this.decision = new double[Board.NB_HOLES];
        /* Initialisation de l'évaluation courante */
        this.evaluation = -Double.MAX_VALUE;
        /* On parcourt toutes les coups possibles */
        for (int i = 0; i < Board.NB_HOLES; i++) {
            /* Si le coup est jouable */
            if (board.getPlayerHoles()[i] != 0) {
                /* Sélection du coup à jouer */
                double[] decision = new double[Board.NB_HOLES];
                decision[i] = 1;
                /* On copie la grille de jeu et on joue le coup sur la copie */
                Board copy = (Board) board.clone();
                try {
                    int score = copy.playMoveSimulationScore(copy.getCurrentPlayer(), decision);
                    copy = copy.playMoveSimulationBoard(copy.getCurrentPlayer(), decision);
                    /*
                     * Si la nouvelle situation de jeu est un coup qui met fin à la partie, on
                     * évalue la situation actuelle
                     */
                    if ((score < 0) || (copy.getScore(Board.otherPlayer(copy.getCurrentPlayer())) >= 25)
                            || (copy.getNbSeeds() <= 6))
                        this.decision[i] = color * this.diffScore(copy,color);
                    /* Sinon, on explore les coups suivants */
                    else {
                        /* Si la profondeur maximale n'est pas atteinte */
                        if (depth < NegamaxNode.maxDepth) {
                            /* On construit le noeud suivant */
                            NegamaxNode child = negamax(copy, depth+1,-color);
                            /* On récupère l'évaluation du noeud fils */
                            this.decision[i] = color * child.getEvaluation();
                        }
                        /*
                         * Sinon (si la profondeur maximale est atteinte), on évalue la situation
                         * actuelle
                         */
                        else
                            this.decision[i] = color * this.diffScore(copy,color);
                    }
                    /*
                     * L'évaluation courante du noeud est mise à jour, selon le type de noeud
                     */
                    
                    this.evaluation = Math.max(this.evaluation, this.decision[i]);
                } catch (InvalidBotException e) {
                    System.out.println("ERREUR ");
    
                    this.decision[i] = 0;
                }
            }
        }
    
        
    
    }

    private NegamaxNode negamax(Board copy, double d, int color) {
        return new NegamaxNode(copy, d, color);
    }

    /**
     * Initialisation
     */
    protected static void initialize(Board board, int maxDepth) {
        NegamaxNode.maxDepth = maxDepth;
        NegamaxNode.player =board.getCurrentPlayer();
    }

    private int diffScore(Board board,int color) {
        int test = color * board.getScore(NegamaxNode.player) - board.getScore(Board.otherPlayer(NegamaxNode.player));
        System.out.println("diffscore " + test);
        return test;
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
