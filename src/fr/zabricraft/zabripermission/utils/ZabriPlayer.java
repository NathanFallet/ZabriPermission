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
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import fr.zabricraft.zabripermission.ZabriPermission;

public class ZabriPlayer {
	
	private UUID uuid;
	private PermissionAttachment attachment;
	private String group;
	private ArrayList<String> permissions;

	public ZabriPlayer(Player p){
		uuid = p.getUniqueId();
		attachment = p.addAttachment(ZabriPermission.getInstance());
		FileConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		group = config.getString("group");
		if(group == null){
			group = "";
		}
		ZabriGroup g = ZabriPermission.getInstance().getGroup(group);
		if(g == null){
			g = ZabriPermission.getInstance().getDefaultGroup();
		}
		if(g != null){
			for(String perm : g.getPermissions()){
				if(perm.startsWith("-")){
					attachment.setPermission(perm.substring(1), false);
				}else{
					attachment.setPermission(perm, true);
				}
			}
			p.setDisplayName(ChatColor.translateAlternateColorCodes('&', g.getPrefix()+p.getName()+g.getSuffix()+"&r"));
		}
		permissions = (ArrayList<String>) config.getStringList("permissions");
		if(permissions != null){
			for(String perm : permissions){
				if(perm.startsWith("-")){
					attachment.setPermission(perm.substring(1), false);
				}else{
					attachment.setPermission(perm, true);
				}
			}
		}
	}
	
	public void save(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		config.set("group", group);
		config.set("permissions", permissions);
		try {
			config.save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UUID getUuid() {
		return uuid;
	}

	public PermissionAttachment getAttachment() {
		return attachment;
	}
	
	public File getFile(){
		return new File("plugins/ZabriPermission/players/"+uuid+".yml");
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public ArrayList<String> getPermissions() {
		return permissions;
	}

}
