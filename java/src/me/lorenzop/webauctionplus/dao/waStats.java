package me.lorenzop.webauctionplus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.lorenzop.webauctionplus.WebAuctionPlus;
import me.lorenzop.webauctionplus.mysql.DataQueries;

public class waStats {

	// long cycle stats
	private volatile int totalBuyNowCount  = 0;
	private volatile int totalAuctionCount = 0;
	private volatile int maxAuctionId      =-1;

	private volatile long lastUpdate = -1;
	private final Object lock = new Object();


	public waStats() {}


	private boolean Update() {
		synchronized(lock) {
			final long tim = WebAuctionPlus.getCurrentMilli();
			final long sinceLast = tim - lastUpdate;
			// update no more than every 5 seconds
			if(lastUpdate == -1 || sinceLast >= 5000) {
				lastUpdate = tim;
				doUpdate();
				return true;
			}
		}
		return false;
	}


	private void doUpdate() {
		if(WebAuctionPlus.isDebug()) WebAuctionPlus.log.info(WebAuctionPlus.logPrefix+"Updating stats..");
		Connection conn = WebAuctionPlus.dataQueries.getConnection();

		// total buy nows
		{
			PreparedStatement st = null;
			ResultSet rs = null;
			this.totalBuyNowCount = 0;
			try {
				if(WebAuctionPlus.isDebug()) WebAuctionPlus.log.info("WA Query: Stats::count buy nows");
				st = conn.prepareStatement("SELECT COUNT(*) FROM `"+WebAuctionPlus.dataQueries.dbPrefix()+"Auctions` WHERE `allowBids` = 0");
				rs = st.executeQuery();
				if(rs.next())
					this.totalBuyNowCount = rs.getInt(1);
			} catch (SQLException e) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix + "Unable to get total buy now count");
				e.printStackTrace();
			} finally {
				DataQueries.closeResources(st, rs);
			}
		}

		// total auctions
		{
			PreparedStatement st = null;
			ResultSet rs = null;
			this.totalAuctionCount = 0;
			try {
				if(WebAuctionPlus.isDebug()) WebAuctionPlus.log.info("WA Query: Stats::count auctions");
				st = conn.prepareStatement("SELECT COUNT(*) FROM `"+WebAuctionPlus.dataQueries.dbPrefix()+"Auctions` WHERE `allowBids` != 0");
				rs = st.executeQuery();
				if(rs.next())
					this.totalAuctionCount = rs.getInt(1);
			} catch (SQLException e) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix + "Unable to get total auction count");
				e.printStackTrace();
			} finally {
				DataQueries.closeResources(st, rs);
			}
		}

		// get max auction id
		{
			PreparedStatement st = null;
			ResultSet rs = null;
			this.maxAuctionId = -1;
			try {
				if(WebAuctionPlus.isDebug()) WebAuctionPlus.log.info("WA Query: Stats::getMaxAuctionID");
				st = conn.prepareStatement("SELECT MAX(`id`) AS `id` FROM `"+WebAuctionPlus.dataQueries.dbPrefix()+"Auctions`");
				rs = st.executeQuery();
				if(rs.next())
					this.maxAuctionId = rs.getInt("id");
			} catch (SQLException e) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix + "Unable to query for max Auction ID");
				e.printStackTrace();
			} finally {
				DataQueries.closeResources(st, rs);
			}
		}

		WebAuctionPlus.dataQueries.closeResources(conn);
	}


	// data access layer
	public int getTotalBuyNows() {
		Update();
		return totalBuyNowCount;
	}
	public int getTotalAuctions() {
		Update();
		return totalAuctionCount;
	}
	public int getMaxAuctionID() {
		Update();
		return maxAuctionId;
	}


}
