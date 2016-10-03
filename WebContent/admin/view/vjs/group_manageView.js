$(document).ready(function($) {

$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});

var _addCfg = {
		option : {form:"#group_form",action:"addGroup.html"},
		data: {csrf:"xxx"}
	},
	_editCfg = {
		option : {form:"#group_form",action:"editGroup.html"},
		data: {csrf:"xxx"}
	},
	_deleteCfg = {
		option : {action:"deleteGroup.html"},
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

// add group
$("#group_form_btn").on("click",function (e) {
	var group_id = $(_addCfg.option['form'] + ' input[name="group_id"]').val();
	
	// edit group classify data
	if(group_id
		&& $.trim(group_id).length > 1) {
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

$('#group_add').on('click', function (e) {
	// clear modal form
	if (addOrEditAjax) {
		addOrEditAjax.clearForm();
		// clear textarea value
		$(_editCfg.option.form + ' textarea').val('');
	}
	
	_tools.setText([
        {'selecter':'h4.modal-title','val':'添加品牌',"isOnly":true},
        {'selecter':'#group_form_btn strong','val':'确认添加'}
	]);
	
	$('#modal-form').modal('show');
});

$('#group_edit').on('click', function (e) {
	// edit modal 
	var group_id = $(_addCfg.option['form'] + ' input[name="group_id"]').val();
	if(group_id) {
		_tools.setText([
            {'selecter':'h4.modal-title','val':'修改管理员组'},
            {'selecter':'#group_form_btn strong','val':'确认修改'}
		]);
	}
	
	if (!addOrEditAjax) {
		addOrEditAjax = new Ajax(_addCfg);;
	}
	var count = dataTable.row('.selected').length;
	
	if (count < 1) {
		layer.tips('Please select edit rows', '#group_edit', {tips:3});
	}
	else {
		$('#modal-form').modal('show');
		
		var data = dataTable.row('.selected').data();
		var formData = [
            {'value':data['group_id'], 'name':"group_id"},
            {'value':data['name'], 'name':"name"},
            {'value':data['remark'], 'name':"remark",textarea:true},
            {'value':data['is_forbidden'], 'name':"is_forbidden", 'box':true},
        ];
		addOrEditAjax.setFormData(formData);
	}
});

$('#group_delete').on('click',  function (e) {
	var row = dataTable.row('.selected');
	if (row.count() < 1) {
		layer.tips('Please select delete rows', '#group_delete');
	}
	else if (row.count() > 1) {
		layer.tips('Please select only one row to delete', '#group_delete');
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
				_deleteCfg.data.group_id = data['group_id'];
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

$('#group_refresh').on('click',  function (e) {
	dataTable.rows.add([]).draw(false);
});

$('#modal-form').on('hidden.bs.modal', function () {
	// create a new object if addOrEditAjax is null 
	if (!addOrEditAjax) {
		addOrEditAjax = new Ajax(_addCfg)
	}
	addOrEditAjax.clearForm();
	// clear
	$('#exist').html('');
	$('#exist').addClass('label-primary');
	$('#exist').removeClass('label-warning');
});

$('#name').change (function (e) {
	var val      = $(this).val();
	var group_id = $('#group_id').val();
	var config  = {option:{action:'existGroupField.html'}, data: 'data={"name":"'+val+'"}'};
	
	var data = dataTable.row('.selected').data();
	if (group_id && $.trim(val) == $.trim(data['name'])) {
		$('#exist').html('√');
		$('#exist').addClass('label-primary');
		$('#exist').removeClass('label-warning');
		return;
	}
	
	var query   = new QUR(config);
	query.post(function (data) {
		// exist
		if (data.exist) {
			$('#exist').html('已存在');
			$('#exist').addClass('label-warning');
			$('#exist').removeClass('label-primary');
		}
		else {
			$('#exist').html('√');
			$('#exist').addClass('label-primary');
			$('#exist').removeClass('label-warning');
		}
	},
	function (e) {
		
	});
});

// initial DataTable
var dataTable = $("#group_table").DataTable( {
    "processing": true,
    "serverSide": true,
    "ajax": {
        "url": "loadGroupData.html",
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
			"targets": [4],
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
	        "targets": [5],
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
      { "data": "","orderable":false },
      { "data": "group_id" },
      { "data": "name" },
      { "data": "remark" },
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
