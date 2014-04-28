package me.lorenzop.webauctionplus;

import java.security.Permissions;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;


public class Plugins3rdParty {

	private final logBoots log;


	public Plugins3rdParty(final logBoots log) {
		if(log == null) throw new NullPointerException();
		this.log = log;
		if(setupVault())
			log.info("Found Vault.");
		else
			log.warning("Failed to find Vault.");
	}


	/**
	 * Vault
	 */
	private Plugin vault = null;
	private Chat        chat = null;
	private Economy     econ = null;
	private Permissions perm = null;


	private boolean setupVault() {
		this.vault = Bukkit.getPluginManager().getPlugin("Vault");
		if(this.vault == null) return false;
		final ServicesManager service = Bukkit.getServicesManager();
		// chat
		{
			final RegisteredServiceProvider<Chat> provider = service.getRegistration(Chat.class);
			this.chat = provider.getProvider();
			if(this.chat == null)
				log.info("Found chat plugin.");
			else
				log.warning("Failed to find chat plugin.");
		}
		// economy
		{
			final RegisteredServiceProvider<Economy> provider = service.getRegistration(Economy.class);
			this.econ = provider.getProvider();
			if(this.econ == null)
				log.info("Found economy plugin.");
			else
				log.warning("Failed to find economy plugin.");
		}
		// permissions
		{
			final RegisteredServiceProvider<Permissions> provider = service.getRegistration(Permissions.class);
			this.perm = provider.getProvider();
			if(this.perm == null)
				log.info("Found permissions plugin.");
			else
				log.warning("Failed to find permissions plugin.");
		}
		return isLoaded_Vault();
	}
	public boolean isLoaded_Vault() {
		return (this.vault != null);
	}
	public Chat getChat() {
		return this.chat;
	}
	public Economy getEconomy() {
		return this.econ;
	}
	public Permissions getPerms() {
		return this.perm;
	}


	/**
	 * SignLink
	 */
	private Plugin signlink = null;


	private boolean setupSignLink() {
		this.signlink = Bukkit.getPluginManager().getPlugin("SignLink");
		if(this.signlink == null) return false;

		return isLoaded_SignLink();
	}
	public boolean isLoaded_SignLink() {
		return (this.signlink != null);
	}


}
