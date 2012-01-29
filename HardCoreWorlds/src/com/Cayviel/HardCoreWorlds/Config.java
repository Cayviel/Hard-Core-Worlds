package com.Cayviel.HardCoreWorlds;

import java.io.File;
//import java.io.IOException;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Config{
	
	static File configfile;
	static FileConfiguration config = new YamlConfiguration();

	public static void initconfig(){
		File CF = new File(HardCoreWorlds.datafolder,"config.yml");
		configfile = CF;
		//boolean firstrun = (!CF.exists()); 
		FileSetup.initialize(CF, config, new Defaultable(){
			public void setDefs(File cf){
				Defs(cf);
				}
			});
		Defs(CF);
	}
	static void Defs(File cf){
		config.addDefault("Op Commands",false);
		config.addDefault("Server.Ban Duration", -1);
		/*config.addDefault("Worlds.world.Hardcore",false);
		config.addDefault("Worlds.Hardcore world 1.Hardcore",true);
		config.addDefault("Worlds.Hardcore world 1.Ban Duration",-1);
		config.addDefault("Worlds.Hardcore world 1.Mob Difficulty","Hard");
		config.addDefault("Worlds.Hardcore world 2.Hardcore",true);
		config.addDefault("Worlds.Hardcore world 2.Ban Duration",0);
		config.addDefault("Worlds.Hardcore world 2.Mob Difficulty","VeryHard");
		*/
		config.options().header("'Ban Duration:' is in real hours. '-1' or a value less than zero indicates a permanent ban.");
		config.options().copyDefaults(true);
		
		FileSetup.saveconfig(config, cf);
	}
	
	public static String getDif(String worldN){FileSetup.load(config,configfile); return (config.getString("Worlds."+worldN+".Mob Difficulty","Hard"));}
	public static int getBanL(String world){FileSetup.load(config,configfile); return config.getInt("Worlds."+  world +".Ban Duration",-1);}
	public static boolean getHc(String worldN){FileSetup.load(config,configfile); return (config.getBoolean("Worlds."+ worldN+".Hardcore",false));}
	public static boolean getHc(World world, Player player){FileSetup.load(config,configfile); return (config.getBoolean("Worlds."+ world.getName()+".Hardcore",false)||BanManager.hasWorldBan(player, world));}
	public static boolean getOC(){FileSetup.load(config,configfile); return config.getBoolean("Op Commands",false);}
	public static int getWorldLives(String worldN){FileSetup.load(config,configfile); return config.getInt("Worlds."+worldN+".Lives",1);}
	public static int getServerBanDuration(){FileSetup.load(config,configfile); return config.getInt("Server.Ban Duration",-1);}
}
