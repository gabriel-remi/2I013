
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteLoader {
	public static final int SPRITE_SIZE = 20; // 32

	//pour pour les tableaux d'Image pour l'environnement :
		//0 = droite	1 = en bas a droite		2 = en bas		3 = en bas a gauche
		//4 = a gauche	5 = en haut a gauche	6 = en haut		7 = en haut a doite
		//8 = au centre
	//
	
	
	public static SpriteLoader loader = new SpriteLoader();
	public Image summer_grass;
	public Image spring_grass;
	public Image fall_grass;
	public Image ice;
	public Image snow;
	public Image tree;
	public Image fall_tree;

	public Image [] m1_right = new Image[2];
	public Image [] m1_bottom = new Image[2];
	public Image [] m1_left = new Image[2];
	public Image [] m1_top = new Image[2];
	
	public Image spring_left_and_right, spring_bottom_and_top, spring_surrounded;
	
	public Image water1, water2, deep_water1, deep_water2;
	
	public Image water_rain1, water_rain2, deep_water_rain1, deep_water_rain2;
	
	public Image lava;
	public Image cold_lava;
	public Image cratere;
	
	//public Image lava[] = new Image[9];
	public Image lava_left_side;
	public Image lava_right_side;
	public Image lava_top_side;
	public Image lava_bottom_side;
	
	public Image lava_top_left_side;
	public Image lava_top_right_side;
	public Image lava_bottom_left_side;
	public Image lava_bottom_right_side;
	
	//si j'ai le temps
	public Image coulee_lave_vertical;
	public Image coulee_lave_horizontal;
	
	
	public Image[] player= new Image[4];
	public Image player_dead;
	public Image[] monster = new Image[4];
	public Image mini_robot;
	public Image robot;
	
	
	public Image[] spawn = new Image[4];

	public Image burning_tree;
	public Image snow_tree;
	public Image ashes;
	public Image water;
		
	//SPRING
	public Image spring_shore_left, spring_shore_right, spring_shore_up, spring_shore_down;	
	public Image ssc_bottom_left, ssc_bottom_right, ssc_top_left, ssc_top_right;	
	public Image ssc_bottom_left_B, ssc_bottom_right_B, ssc_top_left_B, ssc_top_right_B;

	//WINTER
	public Image winter_shore_left, winter_shore_right, winter_shore_up, winter_shore_down;	
	public Image wsc_bottom_left, wsc_bottom_right, wsc_top_left, wsc_top_right;	
	public Image wsc_bottom_left_B, wsc_bottom_right_B, wsc_top_left_B, wsc_top_right_B;
	
	
	//FALL
	public Image fall_shore_left, fall_shore_right, fall_shore_up, fall_shore_down;	
	public Image fsc_bottom_left, fsc_bottom_right, fsc_top_left, fsc_top_right;
	public Image fsc_bottom_left_B, fsc_bottom_right_B, fsc_top_left_B, fsc_top_right_B;
	
	
	//SUMMER
	public Image summer_shore_left, summer_shore_right, summer_shore_up, summer_shore_down;	
	public Image sum_sc_bottom_left, sum_sc_bottom_right, sum_sc_top_left, sum_sc_top_right;	
	public Image sum_sc_bottom_left_B, sum_sc_bottom_right_B, sum_sc_top_left_B, sum_sc_top_right_B;



	
	public SpriteLoader() {
		load();
	}

	private void load() {
		
		try {
			
			
			this.spawn[0] = ImageIO.read(new File("res/texture/portal_0.png"));
			this.spawn[1] = ImageIO.read(new File("res/texture/portal_1-min.png"));
			this.spawn[2] = ImageIO.read(new File("res/texture/portal_2-min.png"));
			this.spawn[3] = ImageIO.read(new File("res/texture/portal_3-min.png"));
			
			
			lava = (ImageIO.read(new File("res/texture/lava/lava.png")));
			cold_lava = (ImageIO.read(new File("res/texture/lava/cold_lava.png")));

			cratere = (ImageIO.read(new File("res/texture/lava/cratere.png")));

			lava_left_side = (ImageIO.read(new File("res/texture/lava/lava_left_side.png")));
			lava_right_side = (ImageIO.read(new File("res/texture//lava/lava_right_side.png")));
			lava_top_side = (ImageIO.read(new File("res/texture/lava/lava_top_side.png")));
			lava_bottom_side = (ImageIO.read(new File("res/texture/lava/lava_bottom_side.png")));
			lava_top_left_side = (ImageIO.read(new File("res/texture/lava/lava_top_left.png")));
			lava_top_right_side = (ImageIO.read(new File("res/texture/lava/lava_top_right.png")));
			lava_bottom_left_side = (ImageIO.read(new File("res/texture/lava/lava_bottom_left.png")));
			lava_bottom_right_side = (ImageIO.read(new File("res/texture/lava/lava_bottom_right.png")));
			coulee_lave_vertical = (ImageIO.read(new File("res/texture/lava/lava.png")));
			coulee_lave_horizontal = (ImageIO.read(new File("res/texture/lava/lava.png")));



			water1 =  (ImageIO.read(new File("res/texture/water1.png")));
			water_rain1 = (ImageIO.read(new File("res/texture/water_rain1.png")));
			water2 =  (ImageIO.read(new File("res/texture/water3.png")));
			water_rain2 = (ImageIO.read(new File("res/texture/water_rain2.png")));

			deep_water1 =  (ImageIO.read(new File("res/texture/deep_water1.png")));
			deep_water_rain1 = (ImageIO.read(new File("res/texture/deep_water_rain1.png")));
			deep_water2 =  (ImageIO.read(new File("res/texture/deep_water2.png")));
			deep_water_rain2 = (ImageIO.read(new File("res/texture/deep_water_rain2.png")));

			//General Textures
			fall_grass = (ImageIO.read(new File("res/texture/fall/fall_grass.png")));
			summer_grass = (ImageIO.read(new File("res/texture/summer/summer_grass.png")));
			snow = (ImageIO.read(new File("res/texture/winter/snow.png")));
			water = (ImageIO.read(new File("res/texture/water.png")));
			
			//Spring Textures
			//ssc = spring_shore_corner
			spring_left_and_right = (ImageIO.read(new File("res/texture/spring/spring_left_and_right.png")));
			spring_bottom_and_top = (ImageIO.read(new File("res/texture/spring/spring_bottom_and_top.png")));
			spring_surrounded = (ImageIO.read(new File("res/texture/spring/spring_bottom_and_top.png")));
			
			spring_grass = (ImageIO.read(new File("res/texture/spring/spring_grass.png")));
			spring_shore_left = (ImageIO.read(new File("res/texture/spring/spring_shore_up.png")));
			spring_shore_right = (ImageIO.read(new File("res/texture/spring/spring_shore_down.png")));
			spring_shore_up = (ImageIO.read(new File("res/texture/spring/spring_shore_left.png")));
			spring_shore_down = (ImageIO.read(new File("res/texture/spring/spring_shore_right.png")));
			
			ssc_bottom_left = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_top_right.png")));
			ssc_bottom_right = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_bottom_right.png")));	//must be modified
			ssc_top_left = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_top_left.png")));
			ssc_top_right = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_bottom_left.png")));
			
			ssc_bottom_left_B = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_bottom_left_B.png")));
			ssc_bottom_right_B = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_top_left_B.png")));	//must be modified
			ssc_top_left_B = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_bottom_right_B.png")));
			ssc_top_right_B = (ImageIO.read(new File("res/texture/spring/spring_shore_corner_top_right_B.png")));

			//winter Textures
			ice = (ImageIO.read(new File("res/texture/ice.png")));
			winter_shore_left = (ImageIO.read(new File("res/texture/winter/winter_shore_left.png")));
			winter_shore_right = (ImageIO.read(new File("res/texture/winter/winter_shore_right.png")));
			winter_shore_up = (ImageIO.read(new File("res/texture/winter/winter_shore_up.png")));
			winter_shore_down = (ImageIO.read(new File("res/texture/winter/winter_shore_down.png")));
			
			wsc_bottom_left = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_bottom_left.png")));
			wsc_bottom_right = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_bottom_right.png")));
			wsc_top_left = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_top_left.png")));
			wsc_top_right = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_top_right.png")));
			
			wsc_bottom_left_B = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_bottom_left_B.png")));
			wsc_bottom_right_B = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_bottom_right_B.png")));
			wsc_top_left_B = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_top_left_B.png")));
			wsc_top_right_B = (ImageIO.read(new File("res/texture/winter/winter_shore_corner_top_right_B.png")));
			
			//fall Textures
			fall_grass = (ImageIO.read(new File("res/texture/fall/fall_grass.png")));
			fall_shore_left = (ImageIO.read(new File("res/texture/fall/fall_shore_left.png")));
			fall_shore_right = (ImageIO.read(new File("res/texture/fall/fall_shore_right.png")));
			fall_shore_up = (ImageIO.read(new File("res/texture/fall/fall_shore_up.png")));
			fall_shore_down = (ImageIO.read(new File("res/texture/fall/fall_shore_down.png")));
			
			fsc_bottom_left = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_bottom_left.png")));
			fsc_bottom_right = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_top_left.png")));
			fsc_top_left = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_bottom_right.png")));
			fsc_top_right = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_top_right.png")));
			
			fsc_bottom_left_B = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_bottom_left_B.png")));
			fsc_bottom_right_B = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_top_left_B.png")));
			fsc_top_left_B = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_bottom_right_B.png")));
			fsc_top_right_B = (ImageIO.read(new File("res/texture/fall/fall_shore_corner_top_right_B.png")));

			//summer Textures
			summer_grass = (ImageIO.read(new File("res/texture/summer/summer_grass.png")));
			summer_shore_left = (ImageIO.read(new File("res/texture/summer/summer_shore_down.png")));
			summer_shore_right = (ImageIO.read(new File("res/texture/summer/summer_shore_right.png")));
			summer_shore_up = (ImageIO.read(new File("res/texture/summer/summer_shore_up.png")));
			summer_shore_down = (ImageIO.read(new File("res/texture/summer/summer_shore_left.png")));
			
			sum_sc_bottom_left = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_bottom_left.png")));
			sum_sc_bottom_right = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_bottom_right.png")));
			sum_sc_top_left = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_top_left.png")));
			sum_sc_top_right = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_top_right.png")));
			
						
			sum_sc_bottom_left_B = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_bottom_left_B.png")));
			sum_sc_bottom_right_B = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_bottom_right_B.png")));
			sum_sc_top_left_B = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_top_left_B.png")));
			sum_sc_top_right_B = (ImageIO.read(new File("res/texture/summer/summer_shore_corner_top_right_B.png")));


			
			//Entities
			tree = (ImageIO.read(new File("res/entities/tree2.png")));
			fall_tree = (ImageIO.read(new File("res/entities/fall_tree.png")));

			burning_tree = (ImageIO.read(new File("res/entities/burning_tree.png")));
			ashes = (ImageIO.read(new File("res/entities/ashes.png")));
			snow_tree = (ImageIO.read(new File("res/entities/snow_tree.png")));
			
			//agents
			player[2] =(ImageIO.read(new File("res/agents/player_left.png")));
			player[1] =(ImageIO.read(new File("res/agents/player_down.png")));
			player[0] =(ImageIO.read(new File("res/agents/player_right.png")));
			player[3] =(ImageIO.read(new File("res/agents/player_up.png")));
			
			player_dead = ImageIO.read(new File("res/agents/player_dead.png"));
			
			monster[2] =(ImageIO.read(new File("res/agents/monster_left.png")));
			monster[1] =(ImageIO.read(new File("res/agents/monster_down.png")));
			monster[0] =(ImageIO.read(new File("res/agents/monster_right.png")));
			monster[3] =(ImageIO.read(new File("res/agents/monster_up.png")));
			
		
			mini_robot = (ImageIO.read(new File("res/agents/monster.png")));
			
			robot = (ImageIO.read(new File("res/agents/robot.png")));
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

	public Image getPlayer(int i, int j) {
		// TODO Auto-generated method stub
		if (i == 0) {
			if (j == 1) {
				return player[1];
			}
			if (j == -1) {
				return player[3];
			}
		}
		if (j == 0) {
			if (i == 1) {
				return player[0];
			}

			if (i == -1) {
				return player[2];
			}
		}
		return null;
	}

	public Image getRobot() {

		return robot;
	}

	public Image getMiniRobot(int i, int j) {
	
		return mini_robot;
	}
}
