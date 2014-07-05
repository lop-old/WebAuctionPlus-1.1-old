<?php if(!defined('DEFINE_INDEX_FILE')){if(headers_sent()){echo '<header><meta http-equiv="refresh" content="0;url=../"></header>';}else{header('HTTP/1.0 301 Moved Permanently'); header('Location: ../');} die("<font size=+2>Access Denied!!</font>");}
// my items page
$outputs=array();


$outputs['header']='
<script type="text/javascript" language="javascript" charset="utf-8">
$(document).ready(function() {
	$(\'#mainTable\').dataTable({
		"oLanguage": {
			"sSearch"         : "Search:",
			"sProcessing"     : "Processing...",
			"sLengthMenu"     : "Show _MENU_ items per page",
			"sInfo"           : "Showing items _START_ to _END_ of _TOTAL_",
			"sInfoEmpty"      : "No matching items found",
			"sInfoFiltered"   : "(filtered from _MAX_ total items)",
			"sEmptyTable"     : "&nbsp;<br />No items to display<br />&nbsp;",
			"sZeroRecords"    : "&nbsp;<br />No items to display<br />&nbsp;",
			"oPaginate": {
				"sFirst"    : "&lt;&lt;",
				"sNext"     : "&gt;",
				"sPrevious" : "&lt;",
				"sLast"     : "&gt;&gt;",
			},
		},
		"bJQueryUI"         : true,
		"bStateSave"        : true,
		"iDisplayLength"    : 5,
		"aLengthMenu"       : [[5, 10, 30, 100, -1], [5, 10, 30, 100, "All"]],
		"sPaginationType"   : "full_numbers",
		"sPagePrevEnabled"  : true,
		"sPageNextEnabled"  : true,
	});
} );
</script>
';
//      "bProcessing"       : true,
//      "sAjaxSource"       : "./?page={page}&server_processing=true",
//  var info = $(\'.dataTables_info\')
//  $(\'tfoot\').append(info);


$outputs['body top']='
{messages}
<table border="0" cellpadding="0" cellspacing="0" class="display" id="mainTable">
	<thead>
		<tr style="text-align: center; vertical-align: bottom;">
			<th>Item</th>
			<th>Qty</th>
			<th>Market Price (Each)</th>
			<th>Market Price (Total)</th>
{if permission[canSell]}
			<th>Sell Item</th>
{endif}
		</tr>
	</thead>
	<tbody>
';


$outputs['body row']='
		<tr class="{rowclass}" style="height: 120px;">
			<td style="padding-bottom: 10px; text-align: center;">{item display}</td>
			<td style="text-align: center;"><b>{item qty}</b></td>
			<td style="text-align: center;">{market price each}</td>
			<td style="text-align: center;">{market price total}</td>
{if permission[canSell]}
			<td style="text-align: center;"><a href="./?page=sell&amp;id={item row id}&amp;lastpage=page-myitems" class="button">Sell it</a></td>
{endif}
';
//			<td style="padding-bottom: 10px; text-align: center;">'.
//// add enchantments to this link!
////			'<a href="./?page=graph&amp;name='.$Item->itemId.'&amp;damage='.$Item->itemDamage.'">'.
//				'<img src="'.$Item->getItemImageUrl().'" alt="'.$Item->getItemTitle().'" style="margin-bottom: 5px;" />'.
//				'<br /><b>'.$Item->getItemName().'</b>';
//	if($Item->itemType=='tool') {
//		$output.='<br />'.$Item->getDamagedChargedStr();
//		foreach($Item->getEnchantmentsArray() as $ench)
//			$output.='<br /><span style="font-size: smaller;"><i>'.$ench['enchName'].' '.numberToRoman($ench['level']).'</i></span>';
//	}
//$output.='</a></td>


//<td style="text-align: center;"><a href="./?id='.((int)$Item->itemId).'" class="button">Cancel</a></td>

// add enchantments to this link!
//		'<a href="./?page=graph&amp;name='.$Item->itemId.'&amp;damage='.$Item->itemDamage.'"></a>'.

//	if($Item->itemType=='tool') {
//		$output.='<br />'.$Item->getDamagedChargedStr();
//		foreach($Item->getEnchantmentsArray() as $ench)
//			$output.='<br /><span style="font-size: smaller;"><i>'.$ench['enchName'].' '.numberToRoman($ench['level']).'</i></span>';
//	}


$outputs['body bottom']='
</tbody>
</table>
';


$outputs['error']='
<h2 style="color: #ff0000; text-align: center;">{message}</h2>
';
$outputs['success']='
<h2 style="color: #00ff00; text-align: center;">{message}</h2>
';


return($outputs);
?>
