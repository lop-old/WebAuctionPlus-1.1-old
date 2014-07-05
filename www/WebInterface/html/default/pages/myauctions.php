<?php if(!defined('DEFINE_INDEX_FILE')){if(headers_sent()){echo '<header><meta http-equiv="refresh" content="0;url=../"></header>';}else{header('HTTP/1.0 301 Moved Permanently'); header('Location: ../');} die("<font size=+2>Access Denied!!</font>");}
// my auctions page
$outputs=array();


$outputs['header']='
<script type="text/javascript" language="javascript" charset="utf-8">
$(document).ready(function() {
	$(\'#mainTable\').dataTable({
		"oLanguage": {
			"sSearch"         : "Search:",
			"sProcessing"     : "Processing...",
			"sLengthMenu"     : "Show _MENU_ auctions per page",
			"sInfo"           : "Showing auctions _START_ to _END_ of _TOTAL_",
			"sInfoEmpty"      : "No matching auctions found",
			"sInfoFiltered"   : "(filtered from _MAX_ total auctions)",
			"sEmptyTable"     : "&nbsp;<br />No auctions to display<br />&nbsp;",
			"sZeroRecords"    : "&nbsp;<br />No auctions to display<br />&nbsp;",
			"oPaginate": {
				"sFirst"    : "&lt;&lt;",
				"sNext"     : "&gt;",
				"sPrevious" : "&lt;",
				"sLast"     : "&gt;&gt;",
			},
		},
		"bJQueryUI"         : true,
		"bStateSave"        : true,
		"aoColumnDefs": [
			// column data names
			{ "sName": "item",        "aTargets": [ "column_item"        ] },
			{ "sName": "created",     "aTargets": [ "column_created"     ] },
			{ "sName": "expires",     "aTargets": [ "column_expires"     ] },
			{ "sName": "price_each",  "aTargets": [ "column_price_each"  ] },
			{ "sName": "price_total", "aTargets": [ "column_price_total" ] },
			{ "sName": "qty",         "aTargets": [ "column_qty"         ] },
'./*			{ "sName": "market",      "aTargets": [ "column_market"      ] },*/'
			{ "sName": "cancel",      "aTargets": [ "column_cancel"      ] },
			// field type
			{ "sType": "html", "aTargets": [ "column_item" ] },
			{ "sType": "currency", "aTargets": [ "column_price_each", "column_price_total" ] },
			{ "sType": "numeric",  "aTargets": [ "column_qty" ] },
			// not searchable
			{ "bSearchable": false, "aTargets": [ "column_market", "column_qty", "column_cancel" ] },
			// not sortable
			{ "bSortable": false, "aTargets": [ "column_cancel" ] },
			// modify sorting
			{ "asSorting": [ "desc", "asc" ], "aTargets": [ "column_created", "column_expires", "column_qty" ] },
		],
		// default sorting
		"aaSorting"         : [[ 1, "desc" ]],
		// items per page
		"iDisplayLength"    : 5,
		// pagination
		"sPaginationType"   : "full_numbers",
			"aLengthMenu"       : [[5, 10, 30, 100, -1], [5, 10, 30, 100, "All"]],
			"sPagePrevEnabled"  : true,
			"sPageNextEnabled"  : true,
//		"bServerSide"       : true,
//			"bProcessing"       : true,
//			"sAjaxSource"       : "./?page={page}&ajax=true",
	});
} );
</script>
';


$outputs['body top']='
{messages}
<table border="0" cellpadding="0" cellspacing="0" class="display" id="mainTable">
	<thead>
		<tr style="text-align: center; vertical-align: bottom;">
			<th class="column_item">Item</th>
			<th class="column_created">Created</th>
			<th class="column_expires">Expires</th>
			<th class="column_price_each">Price (Each)</th>
			<th class="column_price_total">Price (Total)</th>
			<th class="column_qty">Qty</th>
'./*<!--		<th class="column_market">Percent of<br />Market Price</th>-->*/'
			<th class="column_cancel"></th>
		</tr>
	</thead>
	<tbody>
';


$outputs['body row']='
		<tr class="{rowclass}" style="height: 120px;">
			<td style="padding-bottom: 10px; text-align: center;">{item}</td>
			<td style="text-align: center;">{created}</td>
			<td style="text-align: center;">{expires}</td>
			<td style="text-align: center;">{price each}</td>
			<td style="text-align: center;">{price total}</td>
			<td style="text-align: center;"><b>{qty}</b></td>
'./*<!--		<td style="text-align: center;">{market price percent}</td>-->*/'
			<td style="text-align: center;">
				<form action="./" method="post">
				{token form}
				<input type="hidden" name="page"      value="{page}" />
				<input type="hidden" name="action"    value="cancel" />
				<input type="hidden" name="auctionid" value="{auction id}" />
				<input type="submit" value="Cancel" class="button" />
				</form>
		</tr>
';
//		($quantity==0?'Never':date('jS M Y H:i:s', $timeCreated + $auctionDurationSec) ).'</td>
//		<td class="center">'.($marketPercent=='N/A'?'N/A':number_format($marketPercent,1).' %').'</td>
// add enchantments to this link!
//		'<a href="./?page=graph&amp;name='.$Item->itemId.'&amp;damage='.$Item->itemDamage.'">'.

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
