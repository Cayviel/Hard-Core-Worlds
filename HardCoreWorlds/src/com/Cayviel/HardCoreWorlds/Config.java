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
		config.addDefault("Op Commands",true);
		config.addDefault("Priority", "Highest");
		config.addDefault("Server.Ban Duration", -1);
		config.addDefault("Server.Use Lives", false);
		config.addDefault("Server.Lives", 5);
		config.options().header("'Ban Duration:' is in real hours. '-1' indicates a perma-ban.  A negative server lives value indicates");
		config.options().copyDefaults(true);
		
		FileSetup.saveconfig(config, cf);
	}
	
	public static String getPriority(){
		String priority = config.getString("Priority","High");
		priority = priority.substring(0,1).toUpperCase()+priority.substring(1).toLowerCase();
		return  priority;
	}
	
	public static String getDif(String worldN){FileSetup.load(config,configfile); return (config.getString("Worlds."+worldN+".Mob Difficulty","Hard"));}
	public static double getBanL(String worldN){FileSetup.load(config,configfile); return config.getDouble("Worlds."+  worldN +".Ban Duration",-1);}
	public static boolean getHc(String worldN){FileSetup.load(config,configfile); return (config.getBoolean("Worlds."+ worldN+".Hardcore",false));}
	public static boolean getHc(World world, Player player){FileSetup.load(config,configfile); FileSetup.load(BanManager.BannedList,BanManager.BannedListFile); return (config.getBoolean("Worlds."+ world.getName()+".Hardcore",false)||BanManager.hasWorldBan(player, world));}
	public static boolean getOC(){FileSetup.load(config,configfile); return config.getBoolean("Op Commands",true);}
	public static int getWorldLives(String worldN){FileSetup.load(config,configfile); return config.getInt("Worlds."+worldN+".Lives",1);}
	public static double getServerBanDuration(){FileSetup.load(config,configfile); return config.getDouble("Server.Ban Duration",-1);}
	public static boolean getUseServerLives(){FileSetup.load(config,configfile); return config.getBoolean("Server.Use Lives",false);}
	public static int getServerLives(){FileSetup.load(config,configfile); return config.getInt("Server.Lives",5);}
	public static int getWorldMinHP(String worldN){FileSetup.load(config, configfile); return (config.getInt("Worlds."+worldN+".MinHP",0));}
	
	public static void setHc(String worldN, boolean bool){FileSetup.load(config,configfile);  config.set("Worlds."+ worldN+".Hardcore",bool); FileSetup.saveconfig(config, configfile);}
	public static void setWorldLives(String worldN, int lives){FileSetup.load(config,configfile);  config.set("Worlds."+worldN+".Lives",lives); FileSetup.saveconfig(config, configfile);}
	public static void setBanL(String worldN, double dur){FileSetup.load(config,configfile);  config.set("Worlds."+  worldN +".Ban Duration",dur); FileSetup.saveconfig(config, configfile);}
	public static void setDif(String worldN, String dif){FileSetup.load(config,configfile);  config.set("Worlds."+worldN+".Mob Difficulty",dif); FileSetup.saveconfig(config, configfile);}
	public static void setUseServerLives(boolean bool){FileSetup.load(config,configfile);  config.set("Server.Use Lives",bool); FileSetup.saveconfig(config, configfile);}
	public static void setServerLives(int lives){FileSetup.load(config,configfile);  config.set("Server.Lives",lives); FileSetup.saveconfig(config, configfile);}
	public static void setWorldMinHP(String worldN, int minhp){FileSetup.load(config, configfile); config.set("Worlds."+worldN+".MinHP",minhp); FileSetup.saveconfig(config, configfile);}

}
