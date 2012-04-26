package puArcade.princetonTD.main;

import java.util.Enumeration;

import puArcade.princetonTD.animations.Animation;
import puArcade.princetonTD.creatures.Creature;
import puArcade.princetonTD.creatures.CreatureWave;
import puArcade.princetonTD.exceptions.BlockedPathException;
import puArcade.princetonTD.exceptions.GameFullException;
import puArcade.princetonTD.exceptions.InaccessibleZoneException;
import puArcade.princetonTD.exceptions.NoMoneyException;
import puArcade.princetonTD.game.Game;
import puArcade.princetonTD.game.GameResult;
import puArcade.princetonTD.game.GameSolo;
import puArcade.princetonTD.game.GameState;
import puArcade.princetonTD.maps.Default;
import puArcade.princetonTD.maps.Map;
import puArcade.princetonTD.players.Player;
import puArcade.princetonTD.players.Team;
import puArcade.princetonTD.towers.Tower;
import puArcade.princetonTD.towers.TowerType;
import puArcade.princetonTD.util.MethodTimer;
import puArcade.princetonTD.util.StringResources;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Scroller;

public class GameThread extends Thread implements GameState {

	/** Indicate whether the surface has been created & is ready to draw */
	private boolean runThread = false;

	private Context context;
	private SurfaceHolder surfaceHolder;
	private static final MethodTimer drawTimer = new MethodTimer();

	private Scroller mScroller;
	private GestureDetector mGD;

	private Bitmap mapBM;
	private Bitmap[] towerBM;
	private Bitmap activeTowerBM;
	private Bitmap activeCreatureBM;

	private static final long SLEEP_TIME = 20;

	private final int WIDTH;
	private final int HEIGHT;

	private int xOrigin;
	private int yOrigin;

	private Game game;
	private Map map;
	private Team team;
	private Player player;

	private Tower activeTower;
	private Enumeration<Creature> eCreatures;
	

	public GameThread(SurfaceHolder surfaceHolder, final int w, final int h, Context context) {

		this.context = context;
		this.surfaceHolder = surfaceHolder;

		this.game = new GameSolo();
		map = new Default(game);
		mapBM = StringResources.toBitmap(context, map.getBgImage());

		map.initialize();
		WIDTH = map.getWidth();
		HEIGHT = map.getHeight();  
		xOrigin = 0;
		yOrigin = 0;

		game.setMap(map);
		team = game.getTeams().get(0);
		player = new Player("Player");

		try{
			team.addPlayer(player);
		} 
		catch (GameFullException e){
			e.printStackTrace();
		}

		game.setMainPlayer(player);
		game.initialize();

		//-------------------------
		// Scrolling

		mScroller = new Scroller(context);
		mGD = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() {

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				xOrigin -= distanceX;
				if (WIDTH < w)
					xOrigin = Math.max(Math.min(xOrigin,w-WIDTH), 0);
				else
					xOrigin = Math.max(Math.min(xOrigin,0), w-WIDTH);
				yOrigin -= distanceY;
				if (HEIGHT < h)
					yOrigin = Math.max(Math.min(yOrigin,h-HEIGHT), 0);
				else
					yOrigin = Math.max(Math.min(yOrigin,0), h-HEIGHT);
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
				mScroller.fling(xOrigin, yOrigin,
						-(int)vX, -(int)vY, 0, (int)WIDTH, 0, (int)HEIGHT);
				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				if(!mScroller.isFinished() ) { // is flinging
					mScroller.forceFinished(true); // to stop flinging on touch
				}
				return true; // else won't work
			}
		});
		
		//------------------------------
		// Get tower bitmaps
		towerBM = new Bitmap[TowerType.getN()];
		for (int i = 0; i < towerBM.length; i++)
		{
			towerBM[i] = StringResources.toBitmap(context, TowerType.getTower(i+1).getImage());
		}
	}

	public void setActiveTower(Tower tower)
	{
		activeTower = tower;
	}

	public void acceleratorShaken() {
		game.launchNewWave(player, team);
	}

	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		game.launchNewWave(player, team);
//		return super.onKeyDown(keyCode, event);
//	}
	
	public boolean doOnKeyDown(int keyCode, KeyEvent event) {
		game.launchNewWave(player, team);
		return true;
	}

	public void onTouch(MotionEvent event) {
		mGD.onTouchEvent(event);

		if (activeTower != null)
		{
			int xTouch = (int)event.getX() - activeTower.width()/2;
			int yTouch = (int)event.getY() - activeTower.height()/2;
			if ((xTouch-xOrigin>=0 && xTouch-xOrigin<=WIDTH) && (yTouch-yOrigin>=0 && yTouch-yOrigin<=HEIGHT))
			{
				try
				{
					activeTower.setX(xTouch-xOrigin);
					activeTower.setY(yTouch-yOrigin);
					activeTower.setOwner(player);
					game.addTower(activeTower);
					activeTower = null;
				}
				catch (NoMoneyException e)
				{

				}
				catch (BlockedPathException e)
				{

				}
				catch (InaccessibleZoneException e)
				{

				}
			}
		}
	}

	public void setRunning(boolean b) {
		this.runThread = b;
	}

	@Override
	public void run() {
		game.start();
		while (runThread) {

			Canvas canvas = null;

			try {
				canvas = surfaceHolder.lockCanvas(null);
				synchronized (surfaceHolder) {
					onDraw(canvas);
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void onDraw(Canvas canvas) {

		// background
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(mapBM,xOrigin,yOrigin,null);


		// animations


		//		// start and end zones
		//
		//		for(Team team : game.getTeams())
		//		{
		//			Rect r;
		//
		//			if(team.getEndZone() != null)
		//			{
		//				r = team.getEndZone();
		//
		//				g2.setColor(team.getCouleur());
		//				g2.fillRect(r.x+MARGES_CHATEAU, r.y+MARGES_CHATEAU, r.width-(2*MARGES_CHATEAU), r.height-(2*MARGES_CHATEAU)); 
		//				g2.drawImage(I_CHATEAU, r.x, r.y, r.width, r.height, null);
		//			}
		//		}


		// draw towers

		for(Tower tower : game.getTowers())
			drawTower(tower,canvas);


		// draw creatures
		Creature creature;
		Enumeration<Creature> eCreatures = game.getCreatures().elements();
		while(eCreatures.hasMoreElements())
		{
			creature = eCreatures.nextElement();
			drawHealthBar(creature, canvas);
			drawCreature(creature,canvas);
		}


		//		traitTmp = g2.getStroke();
		//
		//		
		//		// selected tower
		//		
		//		if(towerSelectionnee != null)
		//		{
		//			drawRange(towerSelectionnee,g2,COULEUR_RAYON_PORTEE);
		//
		//			g2.setColor(COULEUR_SELECTION);
		//			g2.setStroke(TRAIT_TILLE);
		//			g2.drawRect(towerSelectionnee.getXi(), towerSelectionnee.getYi(),
		//					(int) (towerSelectionnee.getWidth()),
		//					(int) (towerSelectionnee.getHeight()));
		//		}
		//
		//		g2.setStroke(traitTmp);
		//
		//		
		//		// tower ranges
		//		
		//		if(afficherRayonsDePortee)
		//			for(Tower tower : game.getTowers())
		//				drawRange(tower,g2,COULEUR_RAYON_PORTEE);
		//
		//		// animations
		//		game.drawAnimations(g2, Animation.HEIGHT_AIR);
		//
		//
		//		// added tower
		//		
		//		if(addedTower != null && sourisSurMap)
		//		{
		//			g2.drawImage(I_CADRILLAGE,addedTower.x-40,addedTower.y-40,null);
		//
		//			// modification de la transparence
		//			setTransparence(ALPHA_TOUR_A_AJOUTER,g2);
		//
		//			// dessin de la tower
		//			dessinerTower(addedTower,g2,false);
		//
		//			// positionnnable ou non
		//			if(!game.laTowerPeutEtrePosee(addedTower))
		//				drawRange(addedTower,g2,COULEUR_POSE_IMPOSSIBLE);
		//			else
		//				// affichage du rayon de portee
		//				drawRange(addedTower,g2,COULEUR_RAYON_PORTEE);
		//		}
		//
		//		
		//		// paused
		//		
		//		if(game.isPaused())
		//		{
		//			setTransparence(0.3f, g2);
		//			g2.setColor(Color.DARK_GRAY);
		//			g2.fillRect(0, 0, WIDTH, HEIGHT);
		//			setTransparence(1.0f, g2);
		//
		//			g2.setColor(Color.WHITE);
		//			Font policeTmp = g2.getFont();
		//			g2.setFont(GestionnaireDesPolices.POLICE_TITRE);
		//			g2.drawString("[ PAUSE ]", WIDTH / 2 - 80, HEIGHT / 2 - 50);
		//			g2.setFont(policeTmp);
		//		}
		//
		//
		//		if(afficherFps)
		//		{
		//			g2.setColor(Color.BLACK);
		//			g2.drawString("fps : "+fps, 0, 12);
		//			g2.setColor(Color.WHITE);
		//			g2.drawString("fps : "+fps, 1, 12+1);
		//		}

	}


	private void drawCreature(Creature creature, Canvas canvas) {

		// rotation - too slow with matrix?
		// Matrix tx = new Matrix();

		// tx.setRotate((float)(creature.getAngle()+Math.PI/2),(float)creature.width()/2,(float)creature.height()/2);
		// tx.preTranslate((float)(xOrigin+creature.x()), (float)(yOrigin+creature.y()));
		
		activeCreatureBM = StringResources.toBitmap(context, creature.getImage());
		canvas.drawBitmap(activeCreatureBM,xOrigin+creature.x(),yOrigin+creature.y(),null);
	}

	private void drawHealthBar(Creature creature, Canvas canvas) {

		int bar = (int) (creature.width() * 1.5);
		int xBar = (int) (creature.x() - (bar - creature.width()) / 2);
		int yBar  = (int)(creature.y() + creature.height());
		
		int left = xOrigin + xBar;
		int top = yOrigin + yBar;
		int right = xOrigin + xBar + bar;
		int bottom = yOrigin + yBar+4;
		
		Paint paint = new Paint();
		paint.setColor(creature.getOwner().getTeam().getColor());
		canvas.drawRect(left, top, right, bottom, paint);
		paint.setColor(Color.GREEN);
		canvas.drawRect(left+1, top+1, left+1+(int)(creature.getHealth()*(bar - 2)/creature.getHealthMax()), bottom-1, paint);
	}

	private void drawTower(Tower tower, Canvas canvas) {
		canvas.drawBitmap(towerBM[TowerType.getTowerType(tower)-1],xOrigin+tower.x(),yOrigin+tower.y(),null);
	}
	
	public int getScore()
	{
		return player.getScore();
	}
	public String getWave()
	{
		return game.getWave().toString();
	}
	public int getLives()
	{
		return player.getTeam().getLives();
	}
	public int getGold()
	{
		return (int) player.getGold();
	}

	public void initialize() {
	}

	public void startGame() {
	}

	public void end(GameResult gameResult) {
		// TODO Auto-generated method stub

	}

	public void modifyCoeffSpeed(double coeffSpeed) {
		// TODO Auto-generated method stub

	}

	public void addPlayer(Player player) {
		// TODO Auto-generated method stub

	}

	public void updatePlayer(Player player) {
		// TODO Auto-generated method stub

	}

	public void lostTeam(Team team) {
		// TODO Auto-generated method stub

	}

	public void addTower(Tower tower) {
		// TODO Auto-generated method stub

	}

	public void sellTower(Tower tower) {
		// TODO Auto-generated method stub

	}

	public void upgradeTower(Tower tower) {
		// TODO Auto-generated method stub

	}

	public void launchWave(CreatureWave wave) {
		// TODO Auto-generated method stub

	}

	public void addCreature(Creature creature) {
		// TODO Auto-generated method stub

	}

	public void damagedCreature(Creature creature) {
		// TODO Auto-generated method stub

	}

	public void killedCreature(Creature creature, Player player) {
		// TODO Auto-generated method stub

	}

	public void reachedCreature(Creature creature) {
		// TODO Auto-generated method stub

	}

	public void addAnimation(Animation animation) {
		// TODO Auto-generated method stub

	}

	public void endAnimation(Animation animation) {
		// TODO Auto-generated method stub

	}

}
