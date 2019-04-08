
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Ecosystem extends JPanel implements KeyListener {
	// serialization
	private static final long serialVersionUID = 1L;

	// frame
	private JFrame frame;
	private ZoomAndPanListener zoomAndPanListener;

	// dimensions
	public static final int P_HEIGHT = 1000;
	public static final int P_WIDTH = 1000;
	public static final int SPRITE_SIZE = SpriteLoader.SPRITE_SIZE;
	public static final int WINTER = 0, SPRING = 1, SUMMER = 2, FALL = 3;

	// probability concerning trees update
	public static final double P_SET_ON_FIRE = 0.0, P_TREE_INIT = 0.3, P_TREE_BIRTH = 0.01;

	public static final int mapWidth = P_WIDTH / SPRITE_SIZE;
	public static final int mapHeight = P_HEIGHT / SPRITE_SIZE;

	// interactions with the keyboard
	boolean key_zone, key_aggro, key_season, key_lava, key_rain, key_path, key_agents;

	public static int darkness = 0;
	public static WorldMap map;

	// Environment
	public static int cpt, season;

	// agents
	public static ArrayList<Agent> agents;
	public static ArrayList<Agent> tmp;
	public static ArrayList<Elementaire> elements;

	public Ecosystem() {
		map = new WorldMap();
		addKeyListener(this);
		setFocusable(true);
		season = (int) (Math.random() * 4);
		initAgents();
		initElementaires();
		initPanel();
		initFrame();

		key_zone = key_season = key_aggro = key_lava = key_rain = false;
		this.key_agents = true;
	}

	private void initElementaires() {
		Ecosystem.elements = new ArrayList<Elementaire>();
		Ecosystem.elements.add(new Elementaire(1, new Position(0, 0)));
		Ecosystem.elements.add(new Elementaire(1, new Position(0, 0)));
		Ecosystem.elements.add(new Elementaire(1, new Position(0, 0)));
		Ecosystem.elements.add(new Elementaire(1, new Position(0, 0)));

		Ecosystem.elements.add(new Elementaire(2, new Position(0, 0)));
		Ecosystem.elements.add(new Elementaire(2, new Position(0, 0)));
		Ecosystem.elements.add(new Elementaire(2, new Position(0, 0)));

		Ecosystem.elements.add(new Elementaire(3, new Position(0, 0)));
		Ecosystem.elements.add(new Elementaire(3, new Position(0, 0)));
		Ecosystem.elements.add(new Elementaire(3, new Position(0, 0)));

	}

	private void initAgents() {

		Ecosystem.agents = new ArrayList<Agent>();
		Ecosystem.tmp = new ArrayList<Agent>();
		ArrayList<Position> legalPosition = Ecosystem.map.getAccessiblePositionMiniRobot();
		if (legalPosition.size() > 0) {
			Position robotPosition = legalPosition.remove((int) (legalPosition.size() * Math.random()));
			Ecosystem.agents.add(new Robot(robotPosition));
		}
		if (legalPosition.size() > 0) {
			Position playerPosition = legalPosition.remove((int) (legalPosition.size() * Math.random()));
			Ecosystem.agents.add(new Player(playerPosition));
		}
		if (legalPosition.size() > 0) {
			Position revivingPosition = legalPosition.remove((int) (legalPosition.size() * Math.random()));
			Player.revivingPosition = revivingPosition;
		}
		if (legalPosition.size() > 0) {
			Position playerPosition = legalPosition.remove((int) (legalPosition.size() * Math.random()));
			Ecosystem.agents.add(new Player(playerPosition));
		}

	}

	private void initPanel() {
		this.setPreferredSize(new Dimension(P_WIDTH, P_HEIGHT));

	}

	private void initFrame() {
		frame = new JFrame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		this.zoomAndPanListener = new ZoomAndPanListener(this);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
	}

	// method called by the main class Life, in charge of updating the ecosystem and
	// drawing on the screen
	public void run() {
		this.repaint();
		Toolkit.getDefaultToolkit().sync();
		update();

		int cpt = SPRITE_SIZE;

		while (cpt > 0) {
			repaint();

			try {
				Thread.sleep(Life.delai);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			cpt--;
			Toolkit.getDefaultToolkit().sync();
		}
		for (Agent agent : agents) {
			agent.pos_offset.x = 0;
			agent.pos_offset.y = 0;
			agent.cur_pos.x = agent.next_pos.x;
			agent.cur_pos.y = agent.next_pos.y;

		}

	}

	private void updateSeason() {
		cpt++;
		if (cpt == 100) {
			cpt = 0;
			switch (season) {
			case WINTER:
				season = SPRING;
				break;
			case SPRING:
				season = SUMMER;
				break;
			case SUMMER:
				season = FALL;
				break;
			case FALL:
				season = WINTER;
				break;
			}
		}
	}

	public void changeSeason() {
		switch (season) {
		case WINTER:
			season = SPRING;
			break;
		case SPRING:
			season = SUMMER;
			break;
		case SUMMER:
			season = FALL;
			break;
		case FALL:
			season = WINTER;
			break;
		}
	}

	public void trees_birth() {
		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				if (Math.random() < P_TREE_BIRTH)
					map.setTree(i, j);
			}
		}
	}

	private void update() {

		updateSeason();
		map.update();
		updateAgents();
		updateElements();

	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		clearScreen(g2);
		g2.setTransform(zoomAndPanListener.getCoordTransform());

		g2.setFont(new Font("TimesRoman", Font.PLAIN, SPRITE_SIZE / 2));

		drawMap(g2);
		drawPlayerRevivingZone(g2);
		if (key_zone)
			drawZones(g2);
		if (key_aggro)
			drawDetectionArea(g2);
		drawDayNight(g2);
		Toolkit.getDefaultToolkit().sync();
		if (this.key_agents) {
			drawAgents(g2);
			Toolkit.getDefaultToolkit().sync();
		}
		if (key_path) {
			drawPath(g2);
		}
		Toolkit.getDefaultToolkit().sync();
		drawElements(g2);
		Toolkit.getDefaultToolkit().sync();
		drawagentSkills(g2);
		Toolkit.getDefaultToolkit().sync();

		drawDamage(g2);

		Toolkit.getDefaultToolkit().sync();

	}

	private synchronized void drawElements(Graphics2D g2) {
		for (Elementaire element : elements) {
			int x = element.p.x;
			int y = element.p.y;
			switch (element.type) {
			case 1:
				g2.setColor(new Color(255,0,0, 130));

				break;
			case 2:
				g2.setColor(new Color(0,0,255, 130));

				break;
			case 3:
				g2.setColor(new Color(0,255,0, 130));
				break;
			default:
				break;

			}
			g2.fillOval(x * this.SPRITE_SIZE, y * this.SPRITE_SIZE, this.SPRITE_SIZE, this.SPRITE_SIZE);
		}

	}

	private synchronized void drawDamage(Graphics2D g2) {
		for (Agent agent : this.agents) {
			if (agent instanceof Player && ((Player) agent).damageDealt != null) {
				g2.setColor(new Color(255, 0, 120));
				Player player = (Player) agent;
				g2.setFont(new Font("TimesRoman", Font.BOLD, SPRITE_SIZE));
				g2.drawString("-" + Integer.toString(player.damageDealt), player.target.cur_pos.x * this.SPRITE_SIZE,
						(player.target.cur_pos.y - 1) * this.SPRITE_SIZE);

			}
			if (agent instanceof Player && ((Player) agent).miss) {
				g2.setColor(new Color(255, 0, 120));
				Player player = (Player) agent;
				g2.setFont(new Font("TimesRoman", Font.BOLD, SPRITE_SIZE));
				g2.drawString("MISSSSS", player.target.cur_pos.x * this.SPRITE_SIZE,
						(player.target.cur_pos.y - 1) * this.SPRITE_SIZE);
			}

		}

	}

	private synchronized void drawagentSkills(Graphics2D g2) {
		for (Agent agent : this.agents) {
			if (agent instanceof Robot && ((Robot) agent).laserBeam) {
				g2.setColor(new Color(255, 0, 0, 180));
				g2.setStroke(new BasicStroke(this.SPRITE_SIZE));
				Robot robt = (Robot) agent;
				Player player = (Player) robt.laserTarget;
				g2.draw(new Line2D.Float(robt.cur_pos.x * this.SPRITE_SIZE, this.SPRITE_SIZE * robt.cur_pos.y,
						this.SPRITE_SIZE * player.cur_pos.x, this.SPRITE_SIZE * player.cur_pos.y));
			}

		}

	}

	private void drawPlayerRevivingZone(Graphics2D g2) {
		g2.drawImage(SpriteLoader.loader.spawn[0], Player.revivingPosition.x * this.SPRITE_SIZE - this.SPRITE_SIZE / 2,
				Player.revivingPosition.y * this.SPRITE_SIZE - this.SPRITE_SIZE / 2, this.SPRITE_SIZE * 2,
				this.SPRITE_SIZE * 2, this);

	}

	private void drawDayNight(Graphics2D g2) {

		if (map.is_raining) {
			darkness++;
			if (darkness > 100)
				darkness = 100;
		} else {
			if (darkness == 0)
				darkness = 0;
			else
				darkness--;
		}

		g2.setColor(new Color(0, 0, 0, darkness));
		g2.fillRect(-1000, -1000, Ecosystem.P_WIDTH * 2, Ecosystem.P_HEIGHT * 2);
	}

	private void clearScreen(Graphics2D g2) {
		super.paint(g2);
		g2.setColor(Color.black);
		g2.fillRect(-1000, -1000, 2 * P_WIDTH, 2 * P_HEIGHT);

	}

	// Drawing the environment
	private synchronized void drawMap(Graphics2D g2) {
		switch (season) {
		case SPRING:
			drawMapTextureSpring(g2);
			break;
		case SUMMER:
			drawMapTextureSummer(g2);
			break;
		case FALL:
			drawMapTextureFall(g2);
			break;
		case WINTER:
			drawMapTextureWinter(g2);
			break;
		}
		drawMapEntities(g2);
	}

	private synchronized void drawDetectionArea(Graphics2D g2) {

		for (Agent a : Ecosystem.agents) {
			if (a.life > 0) {
				if (a instanceof Robot) {
					Robot robot = (Robot) a;
					g2.setColor(new Color(255, 130, 0, 130));

					g2.fillOval((robot.cur_pos.x - robot.detection_area) * Ecosystem.SPRITE_SIZE,
							((robot.cur_pos.y - robot.detection_area) * Ecosystem.SPRITE_SIZE),
							robot.detection_area * Ecosystem.SPRITE_SIZE * 2,
							Ecosystem.SPRITE_SIZE * robot.detection_area * 2);

				}
				if (a instanceof Player) {
					Player player = (Player) a;
					g2.setColor(new Color(0, 255, 0, 130));

					g2.fillOval((player.cur_pos.x - player.detection_area) * Ecosystem.SPRITE_SIZE,
							((player.cur_pos.y - player.detection_area) * Ecosystem.SPRITE_SIZE),
							player.detection_area * Ecosystem.SPRITE_SIZE * 2,
							Ecosystem.SPRITE_SIZE * player.detection_area * 2);

				}
				if (a instanceof MiniRobot) {
					MiniRobot minirobot = (MiniRobot) a;
					g2.setColor(new Color(255, 0, 0, 130));

					g2.fillOval((int) ((minirobot.cur_pos.x - minirobot.detection_area + 0.5) * Ecosystem.SPRITE_SIZE),
							(int) ((minirobot.cur_pos.y - minirobot.detection_area + 0.5) * Ecosystem.SPRITE_SIZE),
							minirobot.detection_area * Ecosystem.SPRITE_SIZE * 2,
							Ecosystem.SPRITE_SIZE * minirobot.detection_area * 2);
				}
			}
		}

	}

	private void drawPath(Graphics2D g2) {

		for (Iterator<Agent> iterator = agents.iterator(); iterator.hasNext();) {
			Agent agent = (Agent) iterator.next();
			if (agent.path != null) {
				g2.setColor(new Color(255, 201, 93));
				for (Position p : agent.path) {
					g2.drawRect(p.x * Ecosystem.SPRITE_SIZE, p.y * Ecosystem.SPRITE_SIZE, Ecosystem.SPRITE_SIZE,
							Ecosystem.SPRITE_SIZE);
				}
			}
		}

	}

	private void drawZones(Graphics2D g2) {
		drawHealingZone(g2);
		drawSecurityZone(g2);
	}

	private void drawSecurityZone(Graphics2D g2) {

		g2.setColor(new Color(255, 30, 153, Zone.securityZone.animationTime));
		g2.fillRect(Zone.securityZone.x * Ecosystem.SPRITE_SIZE, Ecosystem.SPRITE_SIZE * Zone.securityZone.y,
				Ecosystem.SPRITE_SIZE * Zone.securityZone.width, Ecosystem.SPRITE_SIZE * Zone.securityZone.height);

		Zone.securityZone.updateAnimationTime();
	}

	private void drawHealingZone(Graphics2D g2) {
		g2.setColor(new Color(255, 255, 153, Zone.healingZone.animationTime));
		g2.fillRect(Zone.healingZone.x * Ecosystem.SPRITE_SIZE, Ecosystem.SPRITE_SIZE * Zone.healingZone.y,
				Ecosystem.SPRITE_SIZE * Zone.healingZone.width, Ecosystem.SPRITE_SIZE * Zone.healingZone.height);

		Zone.healingZone.updateAnimationTime();
	}

	/////////////////////////////////////////////////////////////////////////
	//// SPRING TEXTURES ////
	/////////////////////////////////////////////////////////////////////////

	private void drawMapTextureSpring(Graphics2D g2) {

		for (int x = 0; x < map.getMapTexture().length; x++) {
			for (int y = 0; y < map.getMapTexture()[0].length; y++) {
				switch (map.getMapTexture()[x][y]) {
				case MapTextureID.GROUND:
					if (map.is_corner_B(x, y))
						draw_spring_corner_shore_B(g2, x, y);
					else if (map.is_corner_A(x, y))
						draw_spring_corner_shore_A(g2, x, y);
					else
						draw_spring_ground(g2, x, y);
					break;
				case MapTextureID.WATER:
					if (map.is_raining) {
						if (map.mapAltitude[x][y] < -25) {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.deep_water_rain1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.deep_water_rain2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						} else {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.water_rain1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.water_rain2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						}

					} else {
						if (map.mapAltitude[x][y] < -25) {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.deep_water1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.deep_water2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						} else {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.water1, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
										SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.water2, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
										SPRITE_SIZE, this);
						}
					}

					break;

				case MapTextureID.MOUNTAIN:
					if (map.getAltitude(x, y) < 10) {
						g2.setColor(Color.decode("#6e3300"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else if (map.getAltitude(x, y) < 17 && map.getAltitude(x, y) >= 10) {
						g2.setColor(Color.decode("#582900"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else {
						g2.setColor(Color.decode("#462100"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					}
					break;
				case MapTextureID.LAVA:
					if (map.lave_coule)
						g2.drawImage(SpriteLoader.loader.lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					else
						g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					if (x == map.getHighest_pX() && y == map.getHighest_pY())
						g2.drawImage(SpriteLoader.loader.cratere, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					break;
				case MapTextureID.CRATERE:
					g2.drawImage(SpriteLoader.loader.cratere, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;
				case MapTextureID.COLD_LAVA:
					g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;

				}
			}
		}
	}

	private void draw_spring_ground(Graphics g2, int x, int y) {
		if (map.getTextureLeft(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.spring_shore_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureRight(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.spring_shore_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureUp(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.spring_shore_down, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureDown(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.spring_shore_up, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else
			g2.drawImage(SpriteLoader.loader.spring_grass, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
	}

	private void draw_spring_corner_shore_A(Graphics g2, int x, int y) {
		switch (map.select_corner_typeA(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.ssc_bottom_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.ssc_bottom_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.ssc_top_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.ssc_top_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		}
	}

	public void draw_spring_corner_mount_A(Graphics g2, int x, int y) {
		switch (map.select_corner_typeA(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.ssc_bottom_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.ssc_bottom_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.ssc_top_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.ssc_top_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		}
	}

	private void draw_spring_corner_shore_B(Graphics g2, int x, int y) {
		switch (map.select_corner_typeB(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.ssc_top_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.ssc_top_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.ssc_bottom_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.ssc_bottom_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		}
	}

	/////////////////////////////////////////////////////////////////////////
	//// SUMMER TEXTURES /////
	/////////////////////////////////////////////////////////////////////////

	private void drawMapTextureSummer(Graphics2D g2) {
		for (int x = 0; x < map.getMapTexture().length; x++) {
			for (int y = 0; y < map.getMapTexture()[0].length; y++) {
				switch (map.getMapTexture()[x][y]) {
				case MapTextureID.GROUND:
					if (map.getMapTexture()[x][y] == MapTextureID.WATER) {
						g2.drawImage(SpriteLoader.loader.water, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					} else {
						if (map.is_corner_B(x, y))
							draw_summer_corner_shore_B(g2, x, y);
						else if (map.is_corner_A(x, y))
							draw_summer_corner_shore_A(g2, x, y);
						else
							draw_summer_ground(g2, x, y);
					}
					break;
				case MapTextureID.WATER:
					if (map.is_raining) {
						if (map.mapAltitude[x][y] < -25) {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.deep_water_rain1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.deep_water_rain2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						} else {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.water_rain1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.water_rain2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						}

					} else {
						if (map.mapAltitude[x][y] < -25) {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.deep_water1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.deep_water2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						} else {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.water1, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
										SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.water2, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
										SPRITE_SIZE, this);
						}
					}

					break;

				case MapTextureID.MOUNTAIN:
					if (map.getAltitude(x, y) < 10) {
						g2.setColor(Color.decode("#6e3300"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else if (map.getAltitude(x, y) < 17 && map.getAltitude(x, y) >= 10) {
						g2.setColor(Color.decode("#582900"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else {
						g2.setColor(Color.decode("#462100"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					}
					break;
				case MapTextureID.LAVA:
					if (map.lave_coule)
						g2.drawImage(SpriteLoader.loader.lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					else
						g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					break;
				case MapTextureID.CRATERE:
					g2.drawImage(SpriteLoader.loader.cratere, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;
				case MapTextureID.COLD_LAVA:
					g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;

				}

			}
		}
	}

	private void draw_summer_ground(Graphics g2, int x, int y) {
		if (map.getTextureLeft(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.summer_shore_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureRight(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.summer_shore_up, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureDown(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.summer_shore_down, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureUp(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.summer_shore_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else
			g2.drawImage(SpriteLoader.loader.summer_grass, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
	}

	private void draw_summer_corner_shore_A(Graphics g2, int x, int y) {
		switch (map.select_corner_typeA(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.sum_sc_top_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.sum_sc_bottom_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.sum_sc_bottom_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.sum_sc_top_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		}
	}

	private void draw_summer_corner_shore_B(Graphics g2, int x, int y) {
		switch (map.select_corner_typeB(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.sum_sc_bottom_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.sum_sc_top_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.sum_sc_bottom_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.sum_sc_top_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		}
	}
	/////////////////////////////////////////////////////////////////////////
	//// FALL TEXTURES /////
	/////////////////////////////////////////////////////////////////////////

	private void drawMapTextureFall(Graphics2D g2) {

		for (int x = 0; x < map.getMapTexture().length; x++) {
			for (int y = 0; y < map.getMapTexture()[0].length; y++) {
				switch (map.getMapTexture()[x][y]) {
				case MapTextureID.GROUND:
					if (map.getMapTexture()[x][y] == MapTextureID.WATER) {
						g2.drawImage(SpriteLoader.loader.water, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					} else {
						if (map.is_corner_B(x, y))
							draw_fall_corner_shore_B(g2, x, y);
						else if (map.is_corner_A(x, y))
							draw_fall_corner_shore_A(g2, x, y);
						else
							draw_fall_ground(g2, x, y);
					}

					break;
				case MapTextureID.WATER:
					if (map.is_raining) {
						if (map.mapAltitude[x][y] < -25 + map.hauteur_pluie) {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.deep_water_rain1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.deep_water_rain2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						} else {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.water_rain1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.water_rain2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						}

					} else {
						if (map.mapAltitude[x][y] < -25) {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.deep_water1, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.deep_water2, x * SPRITE_SIZE, y * SPRITE_SIZE,
										SPRITE_SIZE, SPRITE_SIZE, this);
						} else {
							if (Math.random() < 0.5)
								g2.drawImage(SpriteLoader.loader.water1, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
										SPRITE_SIZE, this);
							else
								g2.drawImage(SpriteLoader.loader.water2, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
										SPRITE_SIZE, this);
						}
					}

					break;

				case MapTextureID.MOUNTAIN:
					if (map.getAltitude(x, y) < 10) {
						g2.setColor(Color.decode("#6e3300"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else if (map.getAltitude(x, y) < 17 && map.getAltitude(x, y) >= 10) {
						g2.setColor(Color.decode("#582900"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else {
						g2.setColor(Color.decode("#462100"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					}
					break;
				case MapTextureID.LAVA:
					if (map.lave_coule)
						g2.drawImage(SpriteLoader.loader.lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					else
						g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					break;
				case MapTextureID.CRATERE:
					g2.drawImage(SpriteLoader.loader.cratere, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;
				case MapTextureID.COLD_LAVA:
					g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;
				}
			}
		}
	}

	private void draw_fall_ground(Graphics g2, int x, int y) {
		if (map.getTextureLeft(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.fall_shore_down, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureRight(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.fall_shore_up, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
		else if (map.getTextureDown(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.fall_shore_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getTextureUp(x, y) == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.fall_shore_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else
			g2.drawImage(SpriteLoader.loader.fall_grass, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
	}

	private void draw_fall_corner_shore_A(Graphics g2, int x, int y) {
		switch (map.select_corner_typeA(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.fsc_top_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.fsc_top_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.fsc_bottom_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.fsc_bottom_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		}
	}

	private void draw_fall_corner_shore_B(Graphics g2, int x, int y) {
		switch (map.select_corner_typeB(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.fsc_top_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.fsc_top_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.fsc_bottom_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.fsc_bottom_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		}
	}

	/////////////////////////////////////////////////////////////////////////
	//// WINTER TEXTURES /////
	/////////////////////////////////////////////////////////////////////////

	private void drawMapTextureWinter(Graphics2D g2) {

		for (int x = 0; x < map.getMapTexture().length; x++) {
			for (int y = 0; y < map.getMapTexture()[0].length; y++) {
				switch (map.getMapTexture()[x][y]) {
				case MapTextureID.GROUND:
					if (map.getMapTexture()[x][y] == MapTextureID.WATER) {
						g2.drawImage(SpriteLoader.loader.water, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					} else {
						if (map.is_corner_B(x, y))
							draw_winter_corner_shore_B(g2, x, y);
						else if (map.is_corner_A(x, y))
							draw_winter_corner_shore_A(g2, x, y);
						else
							draw_winter_ground(g2, x, y);
					}
					break;
				case MapTextureID.WATER:
					g2.drawImage(SpriteLoader.loader.ice, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
							this);
					break;
				case MapTextureID.MOUNTAIN:
					if (map.getAltitude(x, y) < 10) {
						g2.setColor(Color.decode("#6e3300"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else if (map.getAltitude(x, y) < 17 && map.getAltitude(x, y) >= 10) {
						g2.setColor(Color.decode("#582900"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					} else {
						g2.setColor(Color.decode("#462100"));
						g2.fillRect(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					}
					break;
				case MapTextureID.LAVA:
					if (map.lave_coule)
						g2.drawImage(SpriteLoader.loader.lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					else
						g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					break;
				case MapTextureID.CRATERE:
					g2.drawImage(SpriteLoader.loader.cratere, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;
				case MapTextureID.COLD_LAVA:
					g2.drawImage(SpriteLoader.loader.cold_lava, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;

				}
			}
		}
	}

	private void draw_winter_corner_shore_B(Graphics g2, int x, int y) {
		switch (map.select_corner_typeB(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.wsc_bottom_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.wsc_top_right_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.wsc_bottom_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.wsc_top_left_B, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		}
	}

	private void draw_winter_corner_shore_A(Graphics g2, int x, int y) {
		switch (map.select_corner_typeA(x, y)) {
		case 0:
			g2.drawImage(SpriteLoader.loader.wsc_top_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		case 1:
			g2.drawImage(SpriteLoader.loader.wsc_bottom_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 2:
			g2.drawImage(SpriteLoader.loader.wsc_bottom_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
			break;
		case 3:
			g2.drawImage(SpriteLoader.loader.wsc_top_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
					this);
			break;
		}
	}

	private void draw_winter_ground(Graphics g2, int x, int y) {
		if (map.getMapTexture()[(x + 1 + map.getHeight()) % map.getHeight()][y] == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.winter_shore_left, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getMapTexture()[(x - 1 + map.getHeight()) % map.getHeight()][y] == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.winter_shore_right, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getMapTexture()[x][(y - 1 + map.getWidth()) % map.getWidth()] == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.winter_shore_down, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else if (map.getMapTexture()[x][(y + 1 + map.getWidth()) % map.getWidth()] == MapTextureID.WATER)
			g2.drawImage(SpriteLoader.loader.winter_shore_up, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
					SPRITE_SIZE, this);
		else
			g2.drawImage(SpriteLoader.loader.snow, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, this);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Drawing the second level of environment on the screen : trees, house,
	// monuments etc..
	private void drawMapEntities(Graphics2D g2) {

		for (int x = 0; x < map.getMapEntities().length; x++) {
			for (int y = 0; y < map.getMapEntities()[0].length; y++) {
				switch (map.getMapEntities()[x][y]) {
				case MapEntitiesID.BABY_GREEN_TREE:
					g2.drawImage(SpriteLoader.loader.tree, x * SPRITE_SIZE + (SPRITE_SIZE / 2),
							y * SPRITE_SIZE + (SPRITE_SIZE / 2), SPRITE_SIZE / 4, SPRITE_SIZE / 4, this);
					break;
				case MapEntitiesID.YOUNG_GREEN_TREE:
					g2.drawImage(SpriteLoader.loader.tree, x * SPRITE_SIZE + (SPRITE_SIZE / 4), y * SPRITE_SIZE,
							SPRITE_SIZE / 2 + (SPRITE_SIZE / 4), SPRITE_SIZE / 2, this);
					break;
				case MapEntitiesID.GREEN_TREE:
					if (season == WINTER)
						g2.drawImage(SpriteLoader.loader.snow_tree, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					else if (season == FALL)
						g2.drawImage(SpriteLoader.loader.fall_tree, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					else
						g2.drawImage(SpriteLoader.loader.tree, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
								SPRITE_SIZE, this);
					break;
				case MapEntitiesID.BURNING_TREE:
					g2.drawImage(SpriteLoader.loader.burning_tree, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE,
							SPRITE_SIZE, this);
					break;
				case MapEntitiesID.ASHES:
					g2.drawImage(SpriteLoader.loader.ashes, x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE,
							this);
					break;
				default:
					break;
				}

			}

		}

	}

	/*
	 * DRAWING THE AGENTS
	 */
	private synchronized void drawAgents(Graphics2D g2) {

		for (Agent a : Ecosystem.agents) {
			if (a.life > 0) {
				if (a instanceof MiniRobot) {

					drawMiniRobot(g2, (MiniRobot) a);

				}
				if (a instanceof Player) {
					drawPlayer(g2, (Player) a, a.pos_direction.x, a.pos_direction.y);
				}
				if (a instanceof Healer) {

					g2.setColor(Color.BLUE);
					g2.fillRect(a.cur_pos.x * this.SPRITE_SIZE, this.SPRITE_SIZE * a.cur_pos.y, this.SPRITE_SIZE,
							this.SPRITE_SIZE);

				}
				if (a instanceof Mage) {

					g2.setColor(Color.RED);
					g2.fillRect(this.SPRITE_SIZE * a.cur_pos.x, this.SPRITE_SIZE * a.cur_pos.y, this.SPRITE_SIZE,
							this.SPRITE_SIZE);
				}
				if (a instanceof Robot) {
					drawRobot(g2, (Robot) a, Ecosystem.SPRITE_SIZE * 10);
				}

				if (a instanceof Robot) {
					drawNameBoss(g2, a, 10);
				} else {
					drawName(g2, a);
				}

				if (a instanceof Robot) {
					drawHealthBarBoss(g2, a, 10);
				} else {
					drawHealthBar(g2, a);
				}
			}
		}

	}

	private void drawPlayer(Graphics2D g2, Player player, int i, int j) {

		g2.drawImage(SpriteLoader.loader.getPlayer(i, j),
				(player.cur_pos.x * Ecosystem.SPRITE_SIZE + player.pos_offset.x + Ecosystem.P_WIDTH)
						% Ecosystem.P_WIDTH,
				(player.cur_pos.y * Ecosystem.SPRITE_SIZE + player.pos_offset.y + Ecosystem.P_HEIGHT)
						% Ecosystem.P_HEIGHT,
				Ecosystem.SPRITE_SIZE, Ecosystem.SPRITE_SIZE, this);

	}

	private void drawRobot(Graphics2D g2, Robot robot, int taille) {

		g2.drawImage(SpriteLoader.loader.getRobot(), (robot.cur_pos.x * Ecosystem.SPRITE_SIZE - taille / 2 + 1),
				(robot.cur_pos.y * Ecosystem.SPRITE_SIZE - taille / 2), taille, taille, this);

	}

	private void drawMiniRobot(Graphics2D g2, MiniRobot minirobot) {

		g2.drawImage(SpriteLoader.loader.getRobot(),
				(minirobot.cur_pos.x * Ecosystem.SPRITE_SIZE + minirobot.pos_offset.x + Ecosystem.P_WIDTH)
						% Ecosystem.P_WIDTH,
				(minirobot.cur_pos.y * Ecosystem.SPRITE_SIZE + minirobot.pos_offset.y + Ecosystem.P_HEIGHT)
						% Ecosystem.P_HEIGHT,
				Ecosystem.SPRITE_SIZE, Ecosystem.SPRITE_SIZE, this);

	}

	/*
	 * DRAWING THE ATTRIBUTES OF THE AGENTS
	 */

	private void drawNameBoss(Graphics2D g2, Agent player, int taille) {
		g2.setColor(Color.BLUE);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, Ecosystem.SPRITE_SIZE * taille / 4));
		g2.drawString(player.name + " lv " + player.level,
				Ecosystem.SPRITE_SIZE * player.cur_pos.x - Ecosystem.SPRITE_SIZE * taille / 2,
				Ecosystem.SPRITE_SIZE * player.cur_pos.y - Ecosystem.SPRITE_SIZE * taille / 2);

	}

	private void drawHealthBarBoss(Graphics2D g2, Agent player, int taille) {

		g2.setColor(Color.RED);
		if (player.life > 0 && player.life <= player.max_life) {
			g2.fillRect(Ecosystem.SPRITE_SIZE * player.cur_pos.x - Ecosystem.SPRITE_SIZE * taille / 2,
					Ecosystem.SPRITE_SIZE * player.cur_pos.y - Ecosystem.SPRITE_SIZE * taille / 2,
					(int) (((double) player.life / (double) player.max_life) * Ecosystem.SPRITE_SIZE * taille),
					Ecosystem.SPRITE_SIZE);
		}
		g2.setColor(Color.BLACK);
		g2.drawRect(Ecosystem.SPRITE_SIZE * player.cur_pos.x - Ecosystem.SPRITE_SIZE * taille / 2,
				Ecosystem.SPRITE_SIZE * player.cur_pos.y - Ecosystem.SPRITE_SIZE * taille / 2,
				(int) (((double) player.life / (double) player.max_life) * Ecosystem.SPRITE_SIZE * taille),
				Ecosystem.SPRITE_SIZE);

	}

	private synchronized void drawHealthBar(Graphics2D g2, Agent player) {

		g2.setColor(Color.RED);
		if (player.life > 0 && player.life <= player.max_life) {
			g2.fillRect(Ecosystem.SPRITE_SIZE * player.cur_pos.x + player.pos_offset.x,
					Ecosystem.SPRITE_SIZE * player.cur_pos.y + player.pos_offset.y - Ecosystem.SPRITE_SIZE / 3,
					(int) (((double) player.life / (double) player.max_life) * Ecosystem.SPRITE_SIZE),
					Ecosystem.SPRITE_SIZE / 3);
		}
		g2.setColor(Color.BLACK);
		g2.drawRect(Ecosystem.SPRITE_SIZE * player.cur_pos.x + player.pos_offset.x,
				Ecosystem.SPRITE_SIZE * player.cur_pos.y + player.pos_offset.y - Ecosystem.SPRITE_SIZE / 3,
				Ecosystem.SPRITE_SIZE, Ecosystem.SPRITE_SIZE / 3);

	}

	private void drawName(Graphics2D g2, Agent player) {
		g2.setColor(Color.YELLOW);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, Ecosystem.SPRITE_SIZE / 2));
		g2.drawString(player.name + " lv " + player.level,
				Ecosystem.SPRITE_SIZE * player.cur_pos.x + player.pos_offset.x,
				Ecosystem.SPRITE_SIZE * player.cur_pos.y + player.pos_offset.y - Ecosystem.SPRITE_SIZE / 3);

	}

	// update the agents :
	private synchronized void updateAgents() {

		for (Agent a : Ecosystem.agents) {
			if (a instanceof MiniRobot) {
				((MiniRobot) a).update();
			}
			if (a instanceof Player) {
				((Player) a).update();
			}
			if (a instanceof Robot) {
				((Robot) a).update();
			}
			if (a instanceof Mage) {
				((Mage) a).update();
			}
			if (a instanceof Healer) {
				((Healer) a).update();
			}

		}

		Ecosystem.agents.addAll(Ecosystem.tmp);
		Ecosystem.agents.removeIf(a -> a instanceof MiniRobot && a.life <= 0);
		Ecosystem.tmp.clear();

	}

	private void updateElements() {
		elementsCreation();
		for (Elementaire element : elements) {
			element.update();
		}
		this.elements.removeIf(element -> element.energy == 0);

	}

	private void elementsCreation() {
		int[] elementsNumber = getElementsNumber();
		int created1=0;
		int created2 = 0;
		int created3 = 0;
		for (int x = 0; x < map.getMapEntities().length; x++) {
			for (int y = 0; y < map.getMapEntities()[0].length; y++) {
				if (this.map.getMapEntities()[x][y] == MapEntitiesID.GREEN_TREE && elementsNumber[2] < 10 && created3 < 10) {
					this.elements.add(new Elementaire(3, new Position(x, y)));
					created3++;
				}

				if (this.map.getMapEntities()[x][y] == MapEntitiesID.BURNING_TREE && elementsNumber[0] < 10 && created1 < 10) {
					this.elements.add(new Elementaire(1, new Position(x, y)));
					created1++;
				}

				if ((this.map.getMapTexture()[x][y] == MapTextureID.LAVA
						|| this.map.getMapTexture()[x][y] == MapTextureID.CRATERE) && elementsNumber[0] < 10 && created1 < 10) {
					this.elements.add(new Elementaire(1, new Position(x, y)));
					created1++;
				}
				if (this.map.getMapTexture()[x][y] == MapTextureID.WATER && elementsNumber[1] < 10 && created2 < 10) {
					this.elements.add(new Elementaire(2, new Position(x, y)));
					created2++;
				}
				
			}

		}

	}

	private int[] getElementsNumber() {
		int[] tmp = new int[3];
		for (Elementaire element : elements) {
			if (element.type == 1) {
				tmp[0] = tmp[0] + 1;
			}
			if (element.type == 2) {
				tmp[1] = tmp[1] + 1;
			}
			if (element.type == 3) {
				tmp[2] = tmp[2] + 1;
			}

		}
		return tmp;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_P) {
			map.is_raining = !map.is_raining;

		} else if (e.getKeyCode() == KeyEvent.VK_S)
			changeSeason();
		else if (e.getKeyCode() == KeyEvent.VK_L) {
			map.lave_coule = !map.lave_coule;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			Life.delai += 2;
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (Life.delai == 0)
				Life.delai = 0;
			else
				Life.delai -= 2;
		} else if (e.getKeyCode() == KeyEvent.VK_Z)
			key_zone = !key_zone;
		else if (e.getKeyCode() == KeyEvent.VK_A)
			key_aggro = !key_aggro;
		else if (e.getKeyCode() == KeyEvent.VK_E)
			key_path = !this.key_path;
		else if (e.getKeyCode() == KeyEvent.VK_R)
			this.key_agents = !this.key_agents;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
