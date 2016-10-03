$(document).ready(function($) {
$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});

var _addCfg = {
		option : {form:"#festival_from",action:"addFestival.html"},
		data: {csrf:"xxx"}
	},
	_editCfg = {
		option : {form:"#festival_from",action:"editFestival.html"},
		data: {csrf:"xxx"}
	},
	_deleteCfg = {
		option : {action:"deleteFestival.html"},
		data: {csrf:"xxx"}
	},
	_tools = new Tools(_addCfg),
	_layerCfg = {
		successIcon:1,
		errorIcon  :2,
		shade      :.05,
		waitTime   : 2000,
	};

var addOrEditAjax = null;

// add product classify
$("#festival_from_btn").on("click",function (e) {
	var classify_id = $(_addCfg.option['form'] + ' input[name="festival_id"]').val();
	
	// edit product classify data
	if(classify_id
		&& $.trim(classify_id).length > 1) {
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

$('#festival_add').on('click', function (e) {
	// edit modal 
	// clear form data
	if (addOrEditAjax) {
		addOrEditAjax.clearForm();
	}
	
	_tools.setText([
        {'selecter':'h4.modal-title','val':'添加节日',"isOnly":true},
        {'selecter':'#festival_from_btn strong','val':'确认添加'}
	]);
	
	$('#modal-form').modal('show');
});

$('#festival_edit').on('click', function (e) {
	// edit modal 
	var classify_id = $(_addCfg.option['form'] + ' input[name="festival_id"]').val();
	if(classify_id) {
		_tools.setText([
            {'selecter':'h4.modal-title','val':'修改节日'},
            {'selecter':'#festival_from_btn strong','val':'确认修改'}
		]);
	}
	
	if (!addOrEditAjax) {
		addOrEditAjax = new Ajax(_addCfg);;
	}
	var count = dataTable.row('.selected').length;
	
	if (count < 1) {
		layer.tips('Please select edit rows', '#festival_edit', {tips:3});
	}
	else {
		$('#modal-form').modal('show');
		
		var data = dataTable.row('.selected').data();
		var formData = [
            {'value':data['festival_id'], 'name':"festival_id"},
            {'value':data['name'], 'name':"name"},
            {'value':data['start_string'], 'name':"start_string"},
            {'value':data['end_string'], 'name':"end_string"},
            {'value':data['is_forbidden'], 'name':"is_forbidden", 'box':true},
        ];
		addOrEditAjax.setFormData(formData);
	}
});

$('#festival_delete').on('click',  function (e) {
	var row = dataTable.row('.selected');
	if (row.count() < 1) {
		layer.tips('Please select delete rows', '#festival_delete');
	}
	else if (row.count() > 1) {
		layer.tips('Please select only one row to delete', '#festival_delete');
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
				_deleteCfg.data.festival_id = data['festival_id'];
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

$('#festival_refresh').on('click',  function (e) {
	dataTable.rows.add([]).draw(false);
});

$('#modal-form').on('hidden.bs.modal', function () {
	// create a new object if addOrEditAjax is null 
	if (!addOrEditAjax) {
		addOrEditAjax = new Ajax(_addCfg)
	}
	addOrEditAjax.clearForm();
});

// initial DataTable
var dataTable = $("#festival_table").DataTable( {
    "processing": true,
    "serverSide": true,
    "ajax": {
        "url": "loadFestival.html",
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
			"targets": [3,4,5],
			"data": ["start_string","end_string","add_time"],
			"render": function (data, type, full, meta ) {
				if (typeof data == 'undefined' 
						|| data == null) {
					return '--';
				}
				else {
					return data;
				}
			}
		}, 
	    {
	        "targets": [6],
	        "data": "is_forbidden",
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
      { "data": "festival_id" },
      { "data": "name" },
      { "data": "start_string" },
      { "data": "end_string" },
      { "data": "add_time" },
      { "data": "is_forbidden" },
    ],
    "order"    : [],
    "language" : {
        "lengthMenu"  : "Display _MENU_ records per page",
        "zeroRecords" : "No records",
        "info"        : "Showing page _PAGE_ of _PAGES_",
        "infoEmpty"   : "No records available",
        "infoFiltered": "(filtered from _MAX_ total records)"
    }
});
});
