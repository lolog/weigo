var dataTable;

$(document).ready(function($) {

$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});

var _deleteCfg = {
		option : {action:"deleteShop.html"},
		data: {csrf:"xxx"}
	},
	_layerCfg = {
		successIcon:1,
		errorIcon  :2,
		shade      :.05,
		waitTime   : 2000,
	};

$('#product_edit').on('click', function (e) {
	var count = dataTable.row('.selected').length;
	
	if (count < 1) {
		layer.tips('Please select edit rows', '#product_edit', {tips:3});
	}
	else {
		var data = dataTable.row('.selected').data();
		Win.open('修改商品','editView.html?product_id='+data['product_id']);
	}
});
$('#product_scan').on('click', function (e) {
	var count = dataTable.row('.selected').length;
	
	if (count < 1) {
		layer.tips('Please select scan rows', '#product_scan', {tips:3});
	}
	else {
		var data = dataTable.row('.selected').data();
		Win.open('商品查看','scanView.html?product_id='+data['product_id']);
	}
});

$('#product_delete').on('click',  function (e) {
	var row = dataTable.row('.selected');
	if (row.count() < 1) {
		layer.tips('Please select delete rows', '#product_delete');
	}
	else if (row.count() > 1) {
		layer.tips('Please select only one row to delete', '#product_delete');
	}
	else {
		var data = row.data();
		layer.open({
			title:'Delete',
			icon:_layerCfg.errorIcon,
			content: '确认需要删除 “'+data['name']+'” 吗？',
			btn: ['Yes', 'No'],
			shadeClose: false,
			yes: function(){
			layer.open({content: '你点了确认', time: 1});
				_deleteCfg.data.product_id = data['product_id'];
				var deleteAjax = new Ajax(_deleteCfg);
				deleteAjax.post(function (msg) {
					// close all
					layer.closeAll();
					// load data
					dataTable.rows.add([]).draw(false);
					layer.msg(msg.message, {icon: _layerCfg.successIcon, shade:_layerCfg.shade, time:_layerCfg.waitTime});
				});
			}, 
			no: function(){
			}
		});
	}
});

$('#product_refresh').on('click',  function (e) {
	dataTable.rows.add([]).draw(false);
});

// initial DataTable
dataTable = $("#product_table").DataTable( {
    "processing": true,
    "serverSide": true,
    "ajax": {
        "url": "loadProductData.html",
        "type": "POST"
    },
    "columnDefs" : [
        {
        	"targets": [0],
        	"className": "select-checkbox"
        },
		{
		    "targets": [1],
		    "visible": false,
		    "searchable": false
		},
		{
			"targets": [6],
			"data": "add_time",
			"render": function (data, type, full, meta ) {
				if (typeof data == 'undefined' 
						|| data == null) {
					return _tools.curentTime();
				}
				else {
					return data;
				}
			}
		}, 
	    {
	        "targets": [7,8],
	        "data": ["is_recommand", "is_forbidden"],
	        "render": function ( data, type, full, meta ) {
	            if (typeof data == 'undefined' 
	            	|| data == null 
	            	|| data == false) {
	            	return 'N';
	            }
	            else {
	            	return 'Y';
	            }
	          }
	     }, 
    ],
    "select": {
        "style":    "os",
        "selector": "td:first-child"
    },
    "lengthMenu": [[10, 15, 25, 50], [10,15, 25, 50]],
    "pagingType": "full_numbers",
    "columns": [
      { "data": "","orderable":false},
      { "data": "product_id" },
      { "data": "name" },
      { "data": "classify_name" },
      { "data": "price" },
      { "data": "stock" },
      { "data": "add_time" },
      { "data": "is_recommand" },
      { "data": "is_forbidden" },
    ],
    "order"   : [],
    "language": {
        "lengthMenu"  : "Display _MENU_ records per page",
        "zeroRecords" : "No records",
        "info"        : "Showing page _PAGE_ of _PAGES_",
        "infoEmpty"   : "No records available",
        "infoFiltered": "(filtered from _MAX_ total records)"
    }
});
});