var dialog = {shade:.3,time:2000,success:1, error:2};
$(function () {
	// checkbox
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
	
	// name
	$('input#name').change(function (e) {
		var name = $(this).val();
		var config  = {option:{action:'existModuleField.html'}, data: 'data={"name":"' + name + '"}'};
		
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
		var data = {};
		$('.i-checks label.checkbox-inline div').each (function (index,value) {
			var _this = $(this);
			var checked = _this.hasClass('checked');
			var checkbox  = $(this).find('input[type="checkbox"]')[0];
			if (checked) {
				data[checkbox.name] = true;
			}
			else {
				data[checkbox.name] = false;
			}
		});
		
		var config = {option : {action:'addModule.html',form:'#simple_information'},data:{csrf:'xxx'}};
		var ajax = new Ajax(config);
		
		// set data
		ajax.fieldFn(function (d) {
			if (data['is_expand']) {
				d['is_expand']    = data['is_expand'];
			}
			else {
				delete d['is_expand'];
			}
			
			if (d['is_forbidden']) {
				d['is_forbidden'] = data['is_forbidden'];
			}
			else {
				delete d['is_forbidden'];
			}
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