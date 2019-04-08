
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteLoader {
	public static final int SPRITE_SIZE = 20; // 32
	
	
	public static SpriteLoader loader = new SpriteLoader();
	public Image summer_grass;
	public Image spring_grass;
	public Image fall_grass;
	public Image ice;
	public Image snow;
	public Image tree;
	public Image tree2;
	public Image fall_tree;
	
	public Image water1, water2, deep_water1, deep_water2;
	public Image water_rain1, water_rain2, deep_water_rain1, deep_water_rain2;
	
	public Image lava;
	public Image cold_lava;
	public Image cratere;
	
	
	public Image mont1, mont2, mont3;
	public Image mont1_rain1, mont1_rain2, mont1_snow1, mont1_snow2;
	public Image mont2_rain1, mont2_rain2, mont2_snow1, mont2_snow2;
	public Image mont3_rain1, mont3_rain2, mont3_snow1, mont3_snow2;

	public Image[] player= new Image[4];
	public Image player_dead;
	public Image[] monster = new Image[4];
	public Image mini_robot;
	public Image robot;
	
	
	public Image spring_rain, spring_rain2;
	public Image summer_rain, summer_rain2;
	public Image fall_rain, fall_rain2;
	public Image winter_snow, winter_snow2;


	public Image spawn;

	public Image burning_tree;
	public Image snow_tree;
	public Image ashes;
	public Image water;
		
	
	
	public SpriteLoader() {
		load();
	}

	private void load() {
		
		try {
			
			
			spawn = ImageIO.read(new File("res/texture/portal_0.png"));

			
			
			lava = (ImageIO.read(new File("res/texture/lava/lava.png")));
			cold_lava = (ImageIO.read(new File("res/texture/lava/cold_lava.png")));

			cratere = (ImageIO.read(new File("res/texture/lava/cratere.png")));


			
			mont1 =(ImageIO.read(new File("res/texture/mont1.png")));
			mont2=(ImageIO.read(new File("res/texture/mont2.png")));
			mont3=(ImageIO.read(new File("res/texture/mont3.png")));
			
			mont1_rain1 = (ImageIO.read(new File("res/texture/mont1_rain1.png")));
			mont1_rain2=(ImageIO.read(new File("res/texture/mont1_rain2.png")));
			mont1_snow1=(ImageIO.read(new File("res/texture/mont1_snow1.png")));
			mont1_snow2=(ImageIO.read(new File("res/texture/mont1_snow2.png")));
			
			
			mont2_rain1 = (ImageIO.read(new File("res/texture/mont2_rain1.png")));
			mont2_rain2=(ImageIO.read(new File("res/texture/mont2_rain2.png")));
			mont2_snow1=(ImageIO.read(new File("res/texture/mont2_snow1.png")));
			mont2_snow2=(ImageIO.read(new File("res/texture/mont2_snow2.png")));
			
			mont3_rain1 = (ImageIO.read(new File("res/texture/mont3_rain1.png")));
			mont3_rain2=(ImageIO.read(new File("res/texture/mont3_rain2.png")));
			mont3_snow1=(ImageIO.read(new File("res/texture/mont3_snow1.png")));
			mont3_snow2=(ImageIO.read(new File("res/texture/mont3_snow2.png")));
			
			spring_rain = (ImageIO.read(new File("res/texture/spring/spring_rain.png")));
			spring_rain2 = (ImageIO.read(new File("res/texture/spring/spring_rain2.png")));
			
			summer_rain = (ImageIO.read(new File("res/texture/summer/summer_rain.png")));
			summer_rain2 = (ImageIO.read(new File("res/texture/summer/summer_rain2.png")));
			
			fall_rain = (ImageIO.read(new File("res/texture/fall/fall_rain1.png")));
			fall_rain2 = (ImageIO.read(new File("res/texture/fall/fall_rain2.png")));
			
			winter_snow =  (ImageIO.read(new File("res/texture/winter/winter_snow.png")));
			winter_snow2 =  (ImageIO.read(new File("res/texture/winter/winter_snow2.png")));

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

			spring_grass = (ImageIO.read(new File("res/texture/spring/spring_grass.png")));

			//winter Textures
			ice = (ImageIO.read(new File("res/texture/ice.png")));

			//fall Textures
			fall_grass = (ImageIO.read(new File("res/texture/fall/fall_grass.png")));

			//summer Textures
			summer_grass = (ImageIO.read(new File("res/texture/summer/summer_grass.png")));

			//Entities
			tree = (ImageIO.read(new File("res/entities/tree2.png")));

			tree2 = (ImageIO.read(new File("res/entities/tree2.png")));
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
			
		
			mini_robot = (ImageIO.read(new File("res/agents/robot.png")));
			
			robot = (ImageIO.read(new File("res/texture/spawn.png")));
			
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