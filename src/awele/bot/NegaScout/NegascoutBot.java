package awele.bot.NegaScout;

import awele.bot.Bot;
import awele.bot.DemoBot;
import awele.core.Awele;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre Blansché
 * Bot qui prend ses décisions selon le MinMax
 */
public class NegascoutBot extends DemoBot
{
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 5;
    private boolean train =false;
    private static final int PRACTICE_TIME = 2 * 1000;
    
    /** Variable de genetique  */
    private static int i;
    private static int i1;
    private static int i2;
    private static int i3;
    private static int i4;
    HashMap<Long, NegamaxNode> HashMap = new HashMap <Long, NegamaxNode>();
    
    /**
     * @throws InvalidBotException
     */
    public NegascoutBot() throws InvalidBotException
    {
        this.setBotName(" Negascout V1");
        this.addAuthor("Quentin BEAUPUY & Vivien KORPYS");
        
    }
    
    public void trainningMode(){
        this.train=true;
    }
    protected static int degenerescence() {
        int nombreAleatoire = (int)(Math.random() * (6 + 1))-3;
        return nombreAleatoire;
    }
    public void trainningModeV(int i,int i1, int i2,int i3,int i4){
        NegamaxNode.initialize( NegascoutBot.MAX_DEPTH,i,i1,i2,i3,i4,HashMap);
        this.i=i;
        this.i1=i1;
        this.i2=i2;
        this.i3=i3;
        this.i4=i4;
    }
    
    /**
     * Fonction d'initalisation du bot
     * Cette fonction est appelée avant chaque affrontement
     */
    @Override
    public void initialize() {
    if(this.train==false){
        NegamaxNode.initialize( NegascoutBot.MAX_DEPTH,0,0,0,0,0,HashMap);
    }
    }
    
    
 

    /**
     * Pas d'apprentissage
     */
    @Override
    public void learn () {
        
    
        // trainning();
        //working();
        
    }
    
    
    private void working() {
        NegascoutBot[] champions = new NegascoutBot[2];
        int practice_games = 0;
        long timer = System.currentTimeMillis ();
        int nbBots = 2;
        try {
            champions[1] = new NegascoutBot(); // Ce sont 20 clones de notre MLP
        } catch (InvalidBotException e) {
            e.printStackTrace();
        }
        
        champions[0]=this;
    
        do {
            // Les champions sont envoyés dans un tournois pour s'affronter et reviennent ordonnés dans l'ordre croissant du plus fort au plus faible
            try {
                champions = tournament(champions,nbBots);
            } catch (InvalidBotException e) {
                e.printStackTrace();
            }
        
            practice_games++;
        }
        while(System.currentTimeMillis () - timer < PRACTICE_TIME);
        System.out.println("taille arbre this   = " + this.HashMap.size());
        System.out.println("taille arbre this   = " +  champions[0].HashMap.size());
        System.out.println("taille arbre  = " + champions[1].HashMap.size());
        this.HashMap=champions[1].HashMap;
        
    }
    
    
    private void trainning() {
    
        NegascoutBot[] champions = new NegascoutBot[20];
        int practice_games = 0;
        long timer = System.currentTimeMillis ();
        int nbBots = 20;
        // Initilisation pour la manche 1
        for(int i =0; i < nbBots; i++) {
            try {
                champions[i] = new NegascoutBot(); // Ce sont 20 clones de notre MLP
            } catch (InvalidBotException e) {
                e.printStackTrace();
            }
            champions[i].trainningMode();
    
            if(i > 10) {
                
            
                champions[i].trainningModeV(0+degenerescence(),0+degenerescence(),0+degenerescence(),0+degenerescence(),0+degenerescence());
            
            }
        }
    
        do {
            // Les champions sont envoyés dans un tournois pour s'affronter et reviennent ordonnés dans l'ordre croissant du plus fort au plus faible
            try {
                champions = tournament(champions,nbBots);
            } catch (InvalidBotException e) {
                e.printStackTrace();
            }
        
            for(int i = 10; i < 15; i++) {
                champions[i].reproduction(champions[i-10], champions[i-9]);
            }
            for(int i = 15; i < nbBots; i++) {
                champions[i].trainningModeV(0+degenerescence(),0+degenerescence(),0+degenerescence(),0+degenerescence(),0+degenerescence());
            }
        
            practice_games++;
        }
        while(System.currentTimeMillis () - timer < PRACTICE_TIME);  // Tant que l'heure - l'heure Ã  laquelle le timer s'est lancÃ© est infÃ©rieur au temps d'entrainement
    
        //A la fin du timer, notre IA this récupère le MLP de la meilleure IA de notre championnat (qui se trouve Ã  l'indice 0)
        this.trainningMode();
        this.copyModel(champions[0]);
        System.out.println("VAR :  " +this.i+" "+this.i1+" "+this.i2+" "+this.i3+" "+this.i4);
        System.out.println( "Parties d'entrainement efféctuées : " + practice_games);
    }
    
    
    private void reproduction(NegascoutBot champion, NegascoutBot champion1) {
        trainningModeV((champion.i+champion1.i)/2,(champion.i1+champion1.i1)/2,(champion.i2+champion1.i2)/2,(champion.i3+champion1.i3)/2,(champion.i4+champion1.i4)/2);
    }
    
    
    private NegascoutBot[] tournament(NegascoutBot[] champions, int nbBots) throws InvalidBotException {
        
            
            SimpleDateFormat df = new SimpleDateFormat("mm:ss.SSS");
            final double [] points = new double [nbBots];
            for (int i = 0; i < nbBots; i++)
                for (int j = i + 1; j < nbBots; j++)
                {
                    double [] localPoints = new double [2];
                    double nbMoves = 0;
                    long runningTime = 0;
                
                    Awele awele = new Awele (champions[i], champions[j]);
                    awele.play();
                    nbMoves += awele.getNbMoves ();
                    runningTime += awele.getRunningTime ();
                    if (awele.getWinner () >= 0)
                        localPoints [awele.getWinner ()] += 3;
                    else
                    {
                        localPoints [0]++;
                        localPoints [1]++;
                    }

                    points [i] += localPoints [0];
                    points [j] += localPoints [1];
                  
                }
            for (int i = 0; i < points.length; i++)
                points [i] = Math.round (points [i] * 100) / 100.;
 
        
            final Map<String, Integer> map = new HashMap<String, Integer>();
            for (int i = 0; i < nbBots; i++){
                map.put (champions[i].getName (), i);}
            Arrays.sort(champions, new Comparator<Bot>()
            {
                @Override
                public int compare(Bot bot1, Bot bot2)
                {
                    Integer index1 = map.get (bot1.getName ());
                    Integer index2 = map.get (bot2.getName ());
                    return Double.compare (points [index1], points [index2]);
                }
            });
            Arrays.sort (points);

        
            return champions;
        }
    
    
    
    
    private void copyModel(NegascoutBot champion) {
        this.trainningModeV(champion.i,champion.i1,champion.i2,champion.i3,champion.i4);
    }
    
    /**
     * Sélection du coup selon l'algorithme MinMax
     */
    @Override
    public double [] getDecision (Board board)
    { long start = System.currentTimeMillis();
        double[] res = new NegamaxNode(board, 0, board.getCurrentPlayer(), Board.otherPlayer(board.getCurrentPlayer()),-9999,9999).getDecision();
    
    
        long end = System.currentTimeMillis();
        try {
            File myObj = new File("log_negamaxV6.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj.getAbsoluteFile(), true);
            myWriter.write((end - start)+"\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;}

    /**
     * Rien à faire
     */
    @Override
    public void finish () {
    }
}
