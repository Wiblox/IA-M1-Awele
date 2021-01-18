package awele.bot.alpha_awele.mon_awele;

import awele.bot.Bot;
import awele.bot.alpha_awele.Bot_MLP;
import awele.bot.minmax.MinMaxBot;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.ArrayList;

/**
 *
 * Une copie quasi conforme de la classe Awele
 *
 * J'aurais pr�f�r� faire une classe d�rivant de Awele pour ne red�finir
 * que les m�thodes play et game, mais toutes les m�thodes ne sont pas
 * accessibles.
 *
 * Cette classe me permet de g�n�rer des donn�es directement dans le format
 * que souhaite plutot que de g�n�rer un log pour le parser immediatement
 * apr�s
 *
 * Les modifications me permettent de r�cuperer une liste d'observations
 * contenant l'�tat du plateau le coup jou� et si la partie a �t� gagn�e
 *
 */
public class TrainMinMax
{
	private Bot[] players;
	private int [] scores;
	
	private ArrayList<int[]> board_states;
	private ArrayList<Integer> coups;
	
	/**
	 * @param player1 Le premier joueur
	 * @param player2 Le second joueur
	 */
	public TrainMinMax (Bot_MLP player1, Bot player2)
	{
		this.players = new Bot [2];
		this.players [0] = (Bot)player1;
		this.players [1] = (Bot) player2;
		this.scores = new int [2];
		
		board_states = new ArrayList<int[]>();
		coups = new ArrayList<Integer>();
	}
	

	
	
	private static int otherPlayer (int player)
	{
		return 1 - player;
	}
	
	private int [] game (int firstPlayer)
	{
		boolean end = false;
		Board board = new Board ();
		int [] score = new int [2];
		int currentPlayer = firstPlayer;
		board.setCurrentPlayer(currentPlayer);
		while (!end)
		{
			int[] board_state = new int[TrainBoard.NB_HOLES*2];
			
			for(int i = 0; i < TrainBoard.NB_HOLES; i++){
				board_state[i] = board.getPlayerHoles()[i];
				board_state[TrainBoard.NB_HOLES+i] = board.getOpponentHoles()[i];
			}
			
			board_states.add(board_state); // Garde les �tats du board en m�moire
			
				double[] decision = ( this.players[currentPlayer]).getDecision(board);
				
				try {
					coups.add(board.selectMove(currentPlayer, decision)); // garde les coups jou�s en m�moire.
				} catch (InvalidBotException e) {
					e.printStackTrace();
				}
				
				int moveScore = 0;
				try {
					moveScore = board.playMove(currentPlayer, decision);
				} catch (InvalidBotException e) {
					e.printStackTrace();
				}
				
				
				if (moveScore < 0)
					end = true;
				else
					score[currentPlayer] += moveScore;
				
				
			if ((score [currentPlayer] >= 25) || (board.getNbSeeds () <= 6))
				end = true;
			else
			{
				currentPlayer = TrainMinMax.otherPlayer (currentPlayer);
				board.setCurrentPlayer (currentPlayer);
			}
			if (board.getNbSeeds () <= 6)
				score [currentPlayer] += board.getNbSeeds (currentPlayer);
		}
		return score;
	}
	
	/**
	 * Fait jouer une parties d'Awele entre les deux bots
	 */
	public void play ()
	{
		scores = this.game (0);
	}
	
	public ArrayList<Observation> getObservations(boolean playerOneWon){
		
		ArrayList<Observation> observations = new ArrayList<Observation>();
		
		for(int i = 0; i < coups.size();i++){
			
			boolean won;
			
			if(playerOneWon){
				won = i%2 ==0;
			}
			else{
				won = i%2 == 1;
			}
			
			observations.add(new Observation(board_states.get(i),coups.get(i),won));
		}
		
		return observations;
	}
	
	/**
	 * @return 0 si le premier bot a gagn�, 1 si le second a gagn�, -1 s'il y a �galit�
	 */
	public int getWinner ()
	{
		int winner = -1;
		if (this.scores [0] > this.scores [1])
			winner = 0;
		else if (this.scores [1] > this.scores [0])
			winner = 1;
		return winner;
	}
}
