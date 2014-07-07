<?php namespace wa\dao;
if(!defined('PORTAL_LOADED')) {echo '<header><meta http-equiv="refresh" content="1;url=../"></header>'; echo '<font size="+2" style="color: black; background-color: white;">Access Denied!!</font>'; exit(1);}
\psm\ClassLoader::increment();
class mcItem {

	private $rowId  = -1;
	private $id     = -1;
	private $damage = -1;
	private $qty    = 0;
//	private $enchantments = array();



	public function __construct($rowId=-1) {
		$this->rowId = $rowId;
	}



	public function rowId() {
		return((int) $this->rowId);
	}



	public function itemId($id=NULL) {
		if($id !== NULL)
			$this->id = $id;
		return((int) $this->id);
	}
	public function setItemId($id) {
		$this->itemId($id);
		return($this);
	}



	public function damage($value=NULL) {
		if($value !== NULL)
			$this->damage = $value;
		return((int) $this->damage);
	}
	public function setDamage($value) {
		$this->damage($value);
		return($this);
	}



	public function qty($qty=NULL) {
		if($qty !== NULL)
			$this->qty = $qty;
		return((int) $this->qty);
	}
	public function setQty($qty) {
		$this->qty($qty);
		return($this);
	}



	public function getDisplay() {

	}
	public function getItemName() {

	}
	public function getItemTitle() {

	}
	public function getItemImageUrl() {

	}
	public function getDamagedChargedStr() {

	}



}
?>
