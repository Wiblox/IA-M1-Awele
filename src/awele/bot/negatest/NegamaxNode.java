package awele.bot.negatest;

import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * @author Alexandre Blansché Noeud d'un arbre MinMax
 */
public class NegamaxNode {
    // Profondeur maximale
    private static int maxDepth;
    // L'évaluation du noeud
    private double evaluation;
    // Évaluation des coups selon MinMax
    private double[] decision;

    /**
     * Constructeur
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     */
    public NegamaxNode(Board board, double depth, int myTour, int opponentTour, double a, double b) {
        // On crée un tableau des évaluations des coups à jouer pour chaque situation possible
        this.decision = new double[Board.NB_HOLES];
        // Initialisation de l'évaluation courante
        this.evaluation = -Double.MAX_VALUE;
        double[] decisionTemp = new double[Board.NB_HOLES];

        for (int i = 0; i < Board.NB_HOLES; i++) {
            // Si le coup est jouable
            if (board.getPlayerHoles()[i] != 0) {
                // Sélection du coup à jouer
                decisionTemp[i] = 1 * (i + 1);
                try {
                    Board copy = board.playMoveSimulationBoard(myTour, decisionTemp);

                    if (copy.getScore(myTour) < 0 || copy.getScore(opponentTour) >= 25
                            || copy.getNbSeeds() <= 6 || !(depth < NegamaxNode.maxDepth)) {
                        this.decision[i] = scoreEntireBoardById(copy, myTour, opponentTour);
                    } else { // Sinon, on explore les coups suivants
                        // Si le noeud n'a pas encore été calculé, on le construit
                        // On construit le noeud suivant
                        NegamaxNode child = new NegamaxNode(copy, depth + 1, opponentTour, myTour, -b, -a);

                        // On récupère l'évaluation du noeud fils */
                        this.decision[i] = -child.getEvaluation();

                        // Sinon (si la profondeur maximale est atteinte), on évalue la situation actuelle
                    }
                    // L'évaluation courante du noeud est mise à jour, selon le type de noeud (MinNode ou MaxNode)
                    if (this.decision[i] > this.evaluation) {
                        this.evaluation = this.decision[i];
                    }

                    if (depth > 0) {
                        a = Double.max(a, this.decision[i]);
                        if (a >= b) break;
                    }
                } catch (InvalidBotException e) {
                    this.decision[i] = 0;
                }
            }
        }
    }

    // Initialisation
    protected static void initialize(int maxDepth) {
        NegamaxNode.maxDepth = maxDepth;
    }


    private int scoreEntireBoardById(Board board, int myTour, int opponentTour) {
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

        return 25 * (board.getScore(myTour) - board.getScore(opponentTour)) - total;
    }

    /**
     * L'évaluation du noeud
     * @return L'évaluation du noeud
     */
    double getEvaluation() {
        return this.evaluation;
    }


    /**
     * L'évaluation de chaque coup possible pour le noeud
     * @return
     */
    double[] getDecision() {
        return this.decision;
    }
}
