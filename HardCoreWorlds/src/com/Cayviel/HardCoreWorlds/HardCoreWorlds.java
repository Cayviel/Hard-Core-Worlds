package com.Cayviel.HardCoreWorlds;



import java.io.File;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;



public class HardCoreWorlds extends JavaPlugin{
	public static Logger log = Logger.getLogger("Minecraft");
	public Listener vh = new VirtualHunger(this);
	public Listener newp = new playerL();
	public Listener CreatureDamage = new EntListen(this);
	public static File datafolder;
	public static boolean usePerm = false;
	public static PermissionHandler permissionHandler;
	public static PermissibleBase pb;
	public static boolean OpCommands = false;
	public static Server server;
	
	public void onEnable(){
		server = getServer();
		datafolder  = getDataFolder();
		FileSetup.initfiles(datafolder);
    	Config.initconfig();
    	BanManager.init();
    	MobDifficulties.init();
    	String priority = MobDifficulties.getPriority();
    	OpCommands = Config.getOC();
    	
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvent(Type.ENTITY_DAMAGE, vh, Priority.Normal, this);
    	pm.registerEvent(Type.FOOD_LEVEL_CHANGE, vh, Priority.Normal, this);
    	pm.registerEvent(Type.PLAYER_LOGIN, newp, Priority.Normal, this);
    	pm.registerEvent(Type.PLAYER_PRELOGIN, newp, Priority.Normal, this);
    	pm.registerEvent(Type.PLAYER_CHANGED_WORLD, newp, Priority.Normal, this);
    	pm.registerEvent(Type.ENTITY_DEATH, CreatureDamage, Priority.Normal, this);
    	if (isPriority(priority)){
    		pm.registerEvent(Type.ENTITY_DAMAGE, CreatureDamage, Priority.valueOf(priority), this);
    		}else{
        	log.info("[HardCoreWorlds]: 'Priority' field in 'Mob Difficulties.yml' not recognized, reverting to Priority 'Normal'");
    		pm.registerEvent(Type.ENTITY_DAMAGE, CreatureDamage, Priority.Normal, this);
    	}

		// set up our permissions
		if (pm.getPlugin("Permissions")!=null){
			setupPermissions();
			usePerm = true;
		}
    	
	}
    
	public void onDisable(){
		
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
		boolean success =  Commands.ParseCommand(sender,command,commandLabel,args);
		return success;
	}
	
	private void setupPermissions() {
		// if our permissions handler isn't null we shall return
		if (permissionHandler != null) return;

		// grab an instance of the permissions plugin
		Plugin permissionsPlugin = getServer().getPluginManager().getPlugin("Permissions");
		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		log.info("[HardCoreWorlds]: Found and will use plugin " + ((Permissions) permissionsPlugin).getDescription().getFullName());
	}

	public static boolean getPerm(String permission, OfflinePlayer player, boolean def){
		if (usePerm){
			return permissionHandler.has(player.getName(), "hcw."+permission, BanManager.getubWN());
		}
		return def;
	}
	
	public static boolean getPerm(String permission, Player player, boolean def){
		if (usePerm){
			return permissionHandler.has(player, "hcw."+permission);
		}
		return def;
	}
	
	private static boolean isPriority(String string){
		   for(Priority p : Priority.values()) {
			      if(p.toString().equalsIgnoreCase(string)) {
			         return true;
			      }
			   }
			   return false;
	}
}
