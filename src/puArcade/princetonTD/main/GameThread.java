package puArcade.princetonTD.main;

import java.util.ArrayList;
import java.util.Enumeration;

import puArcade.princetonTD.animations.Animation;
import puArcade.princetonTD.animations.Ice;
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
import puArcade.princetonTD.maps.Campus;
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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.Toast;

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

	private static final int SELECT_RANGE = 2500;	// squared

	private final int WIDTH;
	private final int HEIGHT;

	private int xOrigin;
	private int yOrigin;

	private Game game;
	private Map map;
	private Team team;
	private Player player;

	private Tower activeTower;
	private Tower selectTower;
	private Enumeration<Creature> eCreatures;


	public GameThread(SurfaceHolder surfaceHolder, final int w, final int h, Context context) {

		this.context = context;
		this.surfaceHolder = surfaceHolder;

		this.game = new GameSolo();
		map = new Campus(game);
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
		selectTower = null;
	}

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
					selectTower = activeTower;
					activeTower = null;
				}
				catch (NoMoneyException e)
				{

				}
				catch (BlockedPathException e)
				{
					CharSequence text = "Cannot block path!";
					Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
					toast.show();
				}
				catch (InaccessibleZoneException e)
				{
					CharSequence text = "Cannot place tower there!";
					Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		}
		else
		{
			// select closest tower in range
			selectTower = null;
			for(Tower tower : game.getTowers())
			{
				int xTouch = (int)event.getX() - tower.width()/2;
				int yTouch = (int)event.getY() - tower.height()/2;
				int newDist = dist2(xTouch-xOrigin,yTouch-yOrigin,tower.x(),tower.y());
				if (selectTower == null)
				{
					if (newDist < SELECT_RANGE)
						selectTower = tower;
				}
				else
				{
					int oldDist = dist2(xTouch-xOrigin,yTouch-yOrigin,selectTower.x(),selectTower.y());
					if (newDist < oldDist)
					{
						selectTower = tower;
					}
				}
			}
		}
	}

	private int dist2(int x1, int y1, int x2, int y2) {
		return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
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


		// draw tower range
		if (selectTower != null)
		{
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setAlpha(100);
			canvas.drawCircle(xOrigin+selectTower.x()+selectTower.width()/2, 
					yOrigin+selectTower.y()+selectTower.height()/2, 
					(float) selectTower.getRange(), paint);
		}


		// animations


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

	}


	private void drawCreature(Creature creature, Canvas canvas) {

		// rotation - too slow with matrix?
		// Matrix tx = new Matrix();

		// tx.setRotate((float)(creature.getAngle()+Math.PI/2),(float)creature.width()/2,(float)creature.height()/2);
		// tx.preTranslate((float)(xOrigin+creature.x()), (float)(yOrigin+creature.y()));

		activeCreatureBM = StringResources.toBitmap(context, creature.getImage());
		canvas.drawBitmap(activeCreatureBM,xOrigin+creature.x(),yOrigin+creature.y(),null);

		if (creature.getCoeffSlow() > 0)
		{
			Bitmap iceBM = StringResources.toBitmap(context, "drawable/ice");
			int left = xOrigin+creature.x();
			int top = yOrigin+creature.y();
			int right = left + creature.width();
			int bottom = top + creature.height();
			canvas.drawBitmap(iceBM,null,(new Rect(left,top,right,bottom)),null);
		}
	}

	private void drawHealthBar(Creature creature, Canvas canvas) {

		int bar = (int) (creature.width() * 1.5);
		int xBar = (int) (creature.x() - (bar - creature.width()) / 2);
		int yBar = (int)(creature.y() + creature.height());

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
	public boolean canPlaceTowers()
	{
		return game.getCreatures().isEmpty();
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

	@Override
	public void waveHasLaunched(CreatureWave wave) {
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