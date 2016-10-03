var dialog = {shade:.3,time:2000,success:1, error:2};
$(function () {
	// checkbox
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
	$('.i-checks .checkbox-inline input[name="all"]').on('ifChanged',function (e) {
		var _this    = $(this);
		var parent   = _this.parent('div');
		var checkbox = _this.parents('.i-checks').find('input:gt(0)');// .checkbox-inline
		if (parent.hasClass('checked') == true) {
			$(checkbox).iCheck('uncheck');
		}
		else {
			$(checkbox).iCheck('check');
		}
	});
	
	// name
	$('input#name').change(function (e) {
		var name = $(this).val();
		var config  = {option:{action:'existGroupField.html'}, data: 'data={"name":"' + name + '"}'};
		
		var qur = new QUR(config);
		qur.post(function (data) {
			if (data.exist) {
				$('#exist').html('存在');
				$('#exist').addClass('label-warning');
				$('#exist').removeClass('label-primary');
			}
			else {
				$('#exist').html('OK');
				$('#exist').addClass('label-primary');
				$('#exist').removeClass('label-warning');
			}
		},
		function (data) {
			
		});
	});
	
	// module level
	$('#module_level').change (function (e) {
		var _this = $(this);
		var module_level = parseFloat(_this.val());
		
		if (isNaN(module_level)) {
			_this.val('0')
		}
		else if (module_level < 1){
			_this.val(1);
		}
		else {
			_this.val(module_level);
		}
	});
	
	$('#module_cancel').click (function (e) {
		window.parent.layer.closeAll();
	});
	
	$('#module_add').click (function (e) {
		var data = [];
		$('#module_content div.form-group').each (function (index,value) {
			var _this = $(this);
			var module_id = _this.find('input[name="module_id"]');
			var checkbox  = _this.find('input[type="checkbox"]');
			
			var d = {};
			var flag = false;
			d['module_id'] = module_id.val();
			checkbox.each(function (index, value) {
				var _thisBox = $(value);
				var div      = _thisBox.parent('div'); 
				if (div.hasClass('checked') == true) {
					flag = true;
					d[value.name] = true;
				}
			});
			
			if (flag == true) {
				data[index] = d;
			}
		});
		
		var config = {option : {action:'addGroup.html',form:'#simple_information'},data:{csrf:'xxx'}};
		var ajax = new Ajax(config);
		
		// set data
		ajax.fieldFn(function (d) {
			d['authorities'] = data;
		});
		
		ajax.post(function (msg) {
			layer.msg(msg.message, {shade:dialog.shade, time: dialog.time, icon:dialog.success}, function (e) {
				// get parent dataTable
				window.parent.dataTable.rows.add([]).draw(false);
				// close add
				window.parent.layer.closeAll();
			});
		},
		function (msg) {
			window.parent.window.parent.layer.msg(msg.message, {shade:dialog.shade, time: dialog.time, icon:dialog.error});
		});
	});
});