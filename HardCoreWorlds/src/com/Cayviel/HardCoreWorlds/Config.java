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
		config.addDefault("Server.Use Lives", false);
		config.addDefault("Server.Lives", 5);
		config.options().header("'Ban Duration:' is in real hours. '-1' indicates a perma-ban.  A negative server lives value indicates");
		config.options().copyDefaults(true);
		
		FileSetup.saveconfig(config, cf);
	}
	
	public static String getDif(String worldN){FileSetup.load(config,configfile); return (config.getString("Worlds."+worldN+".Mob Difficulty","Hard"));}
	public static int getBanL(String world){FileSetup.load(config,configfile); return config.getInt("Worlds."+  world +".Ban Duration",-1);}
	public static boolean getHc(String worldN){FileSetup.load(config,configfile); return (config.getBoolean("Worlds."+ worldN+".Hardcore",false));}
	public static boolean getHc(World world, Player player){FileSetup.load(config,configfile); FileSetup.load(BanManager.BannedList,BanManager.BannedListFile); return (config.getBoolean("Worlds."+ world.getName()+".Hardcore",false)||BanManager.hasWorldBan(player, world));}
	public static boolean getOC(){FileSetup.load(config,configfile); return config.getBoolean("Op Commands",false);}
	public static int getWorldLives(String worldN){FileSetup.load(config,configfile); return config.getInt("Worlds."+worldN+".Lives",1);}
	public static int getServerBanDuration(){FileSetup.load(config,configfile); return config.getInt("Server.Ban Duration",-1);}
	public static boolean getUseServerLives(){FileSetup.load(config,configfile); return config.getBoolean("Server.Use Lives",false);}
	public static int getServerLives(){FileSetup.load(config,configfile); return config.getInt("Server.Lives",5);}
}
