/*
 *  Copyright (C) 2017 FALLET Nathan
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 */

package fr.zabricraft.zabripermission.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.zabricraft.zabripermission.ZabriPermission;

public class ZabriGroup {
	
	private String name;
	private String prefix;
	private String suffix;
	private ArrayList<String> permissions;
	private ArrayList<String> inherits;
	private boolean isDefault;
	
	public ZabriGroup(String name){
		this.name = name;
		FileConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		prefix = config.getString("prefix");
		if(prefix == null){
			prefix = "";
		}
		suffix = config.getString("suffix");
		if(suffix == null){
			suffix = "";
		}
		permissions = (ArrayList<String>) config.getStringList("permissions");
		if(permissions == null){
			permissions = new ArrayList<String>();
		}
		inherits = (ArrayList<String>) config.getStringList("inherits");
		if(inherits == null){
			inherits = new ArrayList<String>();
		}
		isDefault = config.getBoolean("default");
	}
	
	public void save(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		config.set("prefix", prefix);
		config.set("suffix", suffix);
		config.set("permissions", permissions);
		config.set("inherits", inherits);
		config.set("default", isDefault);
		try {
			config.save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
	
	public File getFile(){
		return new File("plugins/ZabriPermission/groups/"+name+".yml");
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public ArrayList<String> getPermissions() {
		ArrayList<String> result = new ArrayList<String>();
		for(String i : inherits){
			ZabriGroup g = ZabriPermission.getInstance().getGroup(i);
			if(g != null){
				result.addAll(g.getPermissions());
			}
		}
		result.addAll(permissions);
		return result;
	}
	
	public void addPermission(String permission){
		if(!getPermissions().contains(permission)){
			permissions.add(permission);
		}
	}
	
	public void removePermission(String permission){
		if(permissions.contains(permission)){
			permissions.remove(permission);
		}
	}

	public ArrayList<String> getInherits() {
		return inherits;
	}

	public void setInherits(ArrayList<String> inherits) {
		this.inherits = inherits;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
