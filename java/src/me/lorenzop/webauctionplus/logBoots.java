package me.lorenzop.webauctionplus;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;


public class logBoots {

	private final Logger logger;
	private final String prefix;
	private volatile boolean debug = false;


	public logBoots(final Plugin plugin, final String prefix) {
		this.logger = plugin.getLogger();
		this.prefix = prefix;
	}


	public void log(final Level level, final String msg) {
		if(this.prefix == null || this.prefix.isEmpty())
			this.logger.log(level, msg);
		else
			this.logger.log(level, prefix + msg);
	}


	/**
	 * Debug mode.
	 * @return boolean Debug messages are displayed if this returns true.
	 */
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(final boolean value) {
		this.debug = value;
	}
	public void setDebug() {
		this.debug = true;
	}


	public void finest(final String msg) {
		log(Level.FINEST, msg);
	}
	public void finer(final String msg) {
		log(Level.FINER, msg);
	}
	public void fine(final String msg) {
		log(Level.FINE, msg);
	}
	public void debug(final String msg) {
		if(!isDebug()) return;
		log(Level.INFO, msg);
	}
	public void config(final String msg) {
		log(Level.CONFIG, msg);
	}
	public void info(final String msg) {
		log(Level.INFO, msg);
	}
	public void warning(final String msg) {
		log(Level.WARNING, msg);
	}
	public void severe(final String msg) {
		log(Level.SEVERE, msg);
	}


}
