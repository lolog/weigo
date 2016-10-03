$(document).ready(function($) {

$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});

var _addCfg = {
		option : {form:"#brand_form",action:"addBrand.html"},
		data: {csrf:"xxx"}
	},
	_editCfg = {
		option : {form:"#brand_form",action:"editBrand.html"},
		data: {csrf:"xxx"}
	},
	_deleteCfg = {
		option : {action:"deleteBrand.html"},
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

// add brand
$("#brand_form_btn").on("click",function (e) {
	var brand_id = $(_addCfg.option['form'] + ' input[name="brand_id"]').val();
	
	// edit brand classify data
	if(brand_id
		&& $.trim(brand_id).length > 1) {
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

$('#brand_add').on('click', function (e) {
	// clear modal form
	if (addOrEditAjax) {
		addOrEditAjax.clearForm();
		// clear textarea value
		$(_editCfg.option.form + ' textarea').val('');
	}
	
	_tools.setText([
        {'selecter':'h4.modal-title','val':'添加品牌',"isOnly":true},
        {'selecter':'#brand_form_btn strong','val':'确认添加'}
	]);
	
	$('#modal-form').modal('show');
});

$('#brand_edit').on('click', function (e) {
	// edit modal 
	var brand_id = $(_addCfg.option['form'] + ' input[name="brand_id"]').val();
	if(brand_id) {
		_tools.setText([
            {'selecter':'h4.modal-title','val':'修改品牌'},
            {'selecter':'#brand_form_btn strong','val':'确认修改'}
		]);
	}
	
	if (!addOrEditAjax) {
		addOrEditAjax = new Ajax(_addCfg);;
	}
	var count = dataTable.row('.selected').length;
	
	if (count < 1) {
		layer.tips('Please select edit rows', '#brand_edit', {tips:3});
	}
	else {
		$('#modal-form').modal('show');
		
		var data = dataTable.row('.selected').data();
		var formData = [
            {'value':data['brand_id'], 'name':"brand_id"},
            {'value':data['name'], 'name':"name"},
            {'value':data['information'], 'name':"information",textarea:true},
            {'value':data['is_forbidden'], 'name':"is_forbidden", 'box':true},
        ];
		addOrEditAjax.setFormData(formData);
	}
});

$('#brand_delete').on('click',  function (e) {
	var row = dataTable.row('.selected');
	if (row.count() < 1) {
		layer.tips('Please select delete rows', '#brand_delete');
	}
	else if (row.count() > 1) {
		layer.tips('Please select only one row to delete', '#brand_delete');
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
				_deleteCfg.data.brand_id = data['brand_id'];
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

$('#brand_refresh').on('click',  function (e) {
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
var dataTable = $("#brand_table").DataTable( {
    "processing": true,
    "serverSide": true,
    "ajax": {
        "url": "loadBrandData.html",
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
      { "data": "","orderable":false},
      { "data": "brand_id" },
      { "data": "name" },
      { "data": "information" },
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
