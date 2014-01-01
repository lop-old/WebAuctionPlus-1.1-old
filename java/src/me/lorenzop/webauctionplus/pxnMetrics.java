package me.lorenzop.webauctionplus;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;


public class pxnMetrics extends Metrics {


	public pxnMetrics(Plugin plugin) throws IOException {
		super(plugin);
		if(!isOptOut())
            Bukkit.getLogger().log(Level.INFO, "[Metrics] Starting metrics..");
	}


	@Override
	protected String get_BASE_URL() {
		return "http://metrics.poixson.com";
	}


}