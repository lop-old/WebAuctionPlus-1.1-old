<?php namespace wa\dao;
if(!defined('PORTAL_LOADED')) {echo '<header><meta http-equiv="refresh" content="1;url=../"></header>'; echo '<font size="+2" style="color: black; background-color: white;">Access Denied!!</font>'; exit(1);}
\psm\ClassLoader::increment();
final class QueryItem {
private function __construct() {}



	public static function fetchItem(&$db) {
		$row = $db->fetch();
		if($row === FALSE) return(FALSE);
		// item dao
		$item = new \wa\dao\mcItem((int) $row['id']);
		$item->setItemId((int) $row['itemId']);
		$item->setDamage((int) $row['itemDamage']);
		$item->setQty(   (int) $row['qty']);
		return($item);
	}



	/**
	 * Query a single stack from a mailbox.
	 * @return mcItem - Query results, or NULL if failure, FALSE if no results.
	 */
	public static function QuerySingle($player, $id) {
		$playerName = \UserClass::toString($player);
		if(empty($playerName)) return(NULL);
		$id = (int) $id;
		if($id < 1) return(NULL);
		// query single item
		$db = \psm\db\dbPool::get();
		$where = "LOWER(`playerName`) = :PLAYERNAME AND `id` = :ID";
		self::BuildQuery($db, $where, "LIMIT 1");
		$db->bind(':PLAYERNAME', strtolower($playerName), 'str');
		$db->bind(':ID',         $id,                     'int');
		if($db->exec() == NULL) return(NULL);
		// record not found
		if($db->result() != 1) return(FALSE);
		// item dao
		$item = self::fetchItem($db);
		$db->clean();
		return($item);
	}



	/**
	 * Query items of the same type from a players mailbox.
	 * @return QueryItems - Query results, or NULL if failure.
	 */
	public static function QuerySimilar($player, $id) {
		$playerName = \UserClass::toString($player);
		if(empty($playerName)) return(NULL);
		$id = (int) $id;
		if($id < 1) return(NULL);
		// query single item
		$item = self::QuerySingle($player, $id);
		if($item == NULL) return(NULL);
		// query similar to above
		$db = \psm\db\dbPool::get();
		$where = "LOWER(`playerName`) = :PLAYERNAME AND `itemId` = :ITEMID AND `itemDamage` = :DAMAGE";
//			"`enchantments` = :ENCHANTMENTS";
		self::BuildQuery($db, $where, NULL);
		$db->bind(':PLAYERNAME', strtolower($playerName), 'str');
		$db->bind(':ITEMID',     $item->itemId(),         'int');
		$db->bind(':DAMAGE',     $item->damage(),         'int');
		if($db->exec() == NULL) return(NULL);
		// record not found
		if($db->result() < 1) return(NULL);
		$item->qty(0);
		$qty = 0;
		while( ($it = self::fetchItem($db)) !== FALSE )
			$qty += $it->qty();
		$item->qty($qty);
		$db->clean();
		return($item);
	}



	/**
	 * Query players mailbox for items.
	 * @return QueryItems - Query results, or NULL if failure, FALSE if no results.
	 * @example:
	 *     $item = self::fetchItem($db);
	 */
	public static function QueryInventory($player) {
		$playerName = \UserClass::toString($player);
		if(empty($playerName)) return(NULL);
		// query player inventory
		$db = \psm\db\dbPool::get();
		$where = "LOWER(`playerName`) = :PLAYERNAME";
		self::BuildQuery($db, $where, NULL);
		$db->bind(':PLAYERNAME', strtolower($playerName), 'str');
		if($db->exec() == NULL) return(NULL);
		// record not found
		if($db->result() < 1) return(FALSE);
		return($db);
	}



	private static function BuildQuery(&$db, $where, $limit=NULL) {
		if(empty($where)) return(NULL);
		if(empty($limit)) $limit = '';
		return($db->prepare(
				"SELECT `id`, `itemId`, `itemDamage`, `qty`, `enchantments` ".
				"FROM `".$db->tablePrefix()."Items` ".
				"WHERE ".$where." ORDER BY `id` ASC ".$limit
		));
	}



}
?>
