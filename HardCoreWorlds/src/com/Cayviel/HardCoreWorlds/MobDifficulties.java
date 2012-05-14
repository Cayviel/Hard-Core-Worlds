package com.Cayviel.HardCoreWorlds;

import java.io.File;

import java.io.IOException;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.entity.EntityType;


public class MobDifficulties {
	HardCoreWorlds hcw;

	public static File MobDifficultiesFile;
    public static FileConfiguration MD = new YamlConfiguration(); //MobDifficulties	
	
	public static void init() {
		File MDF = new File(HardCoreWorlds.datafolder, "MobDifficulties.yml");
		MobDifficultiesFile = MDF;
		
		FileSetup.initialize(MDF, MD, new Defaultable(){
			public void setDefs(File f) {
				Defs(f);
			}
		});
	}

	private static void Defs(File MDF){
		MD.set("Priority","Normal");
		
		MD.set("Hard.BLAZE",4);
		MD.set("Hard.CAVE_SPIDER",3);
		MD.set("Hard.ENDER_DRAGON",10);
		MD.set("Hard.ENDERMAN",10);
		MD.set("Hard.SKELETON",6);
		MD.set("Hard.SPIDER",3);
		MD.set("Hard.ZOMBIE",6);
		MD.set("Hard.PIG_ZOMBIE",6);
		MD.set("Hard.MAGMA_CUBE",4);
		MD.set("Hard.GHAST",17);
		MD.set("Hard.GIANT",17);
		MD.set("Hard.SILVERFISH",1);
		MD.set("Hard.WOLF",2);
		
		MD.set("Very Hard.BLAZE",6);
		MD.set("Very Hard.CAVE_SPIDER",5);
		MD.set("Very Hard.ENDER_DRAGON",15);
		MD.set("Very Hard.ENDERMAN",15);
		MD.set("Very Hard.SKELETON",9);
		MD.set("Very Hard.SPIDER",5);
		MD.set("Very Hard.ZOMBIE",9);
		MD.set("Very Hard.PIG_ZOMBIE",9);
		MD.set("Very Hard.MAGMA_CUBE",6);
		MD.set("Very Hard.GHAST",19);
		MD.set("Very Hard.GIANT",19);
		MD.set("Very Hard.SILVERFISH",2);
		MD.set("Very Hard.WOLF",3);		
		
		try { MD.save(MDF);} catch (IOException e) {e.printStackTrace();}
		
	}

	public static int getDamage(Entity damager, String worldN){
		int damage;

		damage = MD.getInt(Config.getDif(worldN)+"."+ creatureTypeFromEntity(damager).toString(),-1);
		return damage;
	}
	
	public static EntityType creatureTypeFromEntity(Entity entity) {
	    if ( ! (entity instanceof Creature)) {
	        return null;
	    }

	    String name = entity.getClass().getSimpleName();
	    name = name.substring(5); // Remove "Craft"
	    name.toUpperCase();

	    return EntityType.fromName(name);
	}
}
