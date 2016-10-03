var dataTable;

$(document).ready(function($) {

$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});

var _addCfg = {
		option : {form: "#module_form",action: "addModule.html"},
		data   : {csrf: "xxx"}
	},
	_editCfg = {
		option : {form: "#module_form",action: "editModule.html"},
		data   : {csrf: "xxx"}
	},
	_deleteCfg = {
		option : {action: "deleteModule.html"},
		data   : {csrf:"xxx"}
	},
	_tools = new Tools(_addCfg),
	_layerCfg = {
		successIcon:1,
		errorIcon  :2,
		shade      :.05,
		waitTime   : 2000,
	};

var addOrEditAjax = null;

// add group
$("#module_form_btn").on("click",function (e) {
	var module_id = $(_addCfg.option['form'] + ' input[name="module_id"]').val();
	
	// edit module classify data
	if(module_id
		&& $.trim(module_id).length > 1) {
		addOrEditAjax = new Ajax(_editCfg);
	}
	else {
		addOrEditAjax = new Ajax(_addCfg);
	}
	
	// get checkbox value
	addOrEditAjax.fieldFn(function (_data) {
		if (!_data) {
			return;
		}
		var _box = $(_editCfg.option.form + ' input[type="checkbox"]');
		
		for (var i=0; i<_box.size(); i++) {
			_div = $(_box[i]).parent('div');
			if (_div.hasClass('checked')) {
				_data[_box[i].name] = _box[i].value;
			}
			else {
				delete _data[_box[i].name];
			}
		 }
	});
	
	addOrEditAjax.post(function (data) {
		// success
		if (data.success) {
			// load data
			dataTable.rows.add([]).draw(false);
			// hide modal
			$('#modal-form').modal('hide');
		}
		else {
			layer.msg(data.message, {icon: _layerCfg.successIcon, shade:_layerCfg.shade, time:_layerCfg.waitTime});
		}
	});
});

$('#module_add').on('click', function (e) {
	Win.open('添加模块','addView.html');
});

$('#module_edit').on('click', function (e) {
	var row = dataTable.row('.selected');
	if (row.count() < 1) {
		layer.tips('Please select delete rows', '#module_edit');
	}
	else if (row.count() > 1) {
		layer.tips('Please select only one row to delete', '#module_edit');
	}
	else {
		var data = row.data();
		Win.open('修改模块','editView.html?module_id='+data['module_id']);
	}
});

$('#module_delete').on('click',  function (e) {
	var row = dataTable.row('.selected');
	if (row.count() < 1) {
		layer.tips('Please select delete rows', '#module_delete');
	}
	else if (row.count() > 1) {
		layer.tips('Please select only one row to delete', '#module_delete');
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
				_deleteCfg.data.module_id = data['module_id'];
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

$('#module_refresh').on('click',  function (e) {
	dataTable.rows.add([]).draw(false);
});

// initial DataTable
dataTable = $("#module_table").DataTable( {
    "processing": true,
    "serverSide": true,
    "ajax": {
        "url": "loadModuleData.html",
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
			"targets": [8],
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
	        "targets": [7,9],
	        "data": ["is_expand","is_forbidden"],
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
      { "data": "","orderable":false },
      { "data": "module_id" },
      { "data": "name"},
      { "data": "super_id"},
      { "data": "module_level"},
      { "data": "action_url"},
      { "data": "remark" },
      { "data": "is_expand" },
      { "data": "add_time" },
      { "data": "is_forbidden" },
    ],
    "order"   : [],
    "language": {
        "lengthMenu"   : "Display _MENU_ records per page",
        "zeroRecords"  : "No records",
        "info"         : "Showing page _PAGE_ of _PAGES_",
        "infoEmpty"    : "No records available",
        "infoFiltered" : "(filtered from _MAX_ total records)"
    }
});
});
