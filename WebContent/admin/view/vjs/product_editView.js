var descriptionEditor;
var dialog = {shade:.3,time:2000,success:1, error:2};

$(document).ready(function($) {
	KindEditor('#product_add').click(function() {
		var festivals    = getArrayMutiData("festival_content");
		var shops        = getArrayMutiData("shop_content");
		var labels       = getArrayJustOne("labels");
		var banner       = getArrayJustOne("banner");
		var info         = getSimpleForm("simple_information");
		var description  = descriptionEditor.html();
		
		var data            = info;
		data['festivals']   = festivals;
		data['shops']       = shops;
		data['labels']      = labels;
		data['banner']      = banner;
		data['description'] = description;
		
		var addCfg = _addCfg = {
			option : {action:"editProduct.html"},
			data   : data,
		};
		
		var addAjax = new Ajax(addCfg);
		addAjax.post(function (msg) {
			if(msg.success) {
				layer.msg(msg.message, {shade:dialog.shade, time: dialog.time, icon:dialog.success}, function () {
					// get parent dataTable
					window.parent.dataTable.rows.add([]).draw(false);
					// close add
					window.parent.layer.closeAll();
				});
			}
			else {
				window.parent.window.parent.layer.msg(msg.message, {shade:dialog.shade, time: dialog.time, icon:dialog.error});
			}
		})
	});
	
	// cancel
	KindEditor('#product_cancel').click(function() {
		// get parent dataTable
		window.parent.dataTable.rows.add([]).draw(false);
		// close add
		window.parent.layer.closeAll();
	});
});

$(document).ready(function($) {
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
	
	// select option value
	var selectOption = JSON.parse($('#OBJ').html()),
	 	appName      = $('#app').html();
	var festival_content  = $('#festival_content'),
		shop_content      = $('#shop_content');
	// add new festival
	$('#festival_add_btn').click (function () {
		if (festival_content.children('.row').length >= selectOption.festivals.length) {
			$(this).hide();
		}
		else {
			// add
			var festivalOption = festivalDialog(selectOption);
			festival_content.append(festivalOption);
			
			if (festival_content.children('.row').length >= selectOption.festivals.length) {
				$(this).hide();
			}
		}
		
	});
	// add new festival
	$('#shop_form_btn button').click (function () {
		if (shop_content.children('.row').length >= selectOption.shops.length) {
			$(this).hide();
		}
		else {
			// add 
			var shopOption = shopDialog(selectOption);
			shop_content.append(shopOption);
			
			if (shop_content.children('.row').length >= selectOption.shops.length) {
				$(this).hide();
			}
		}
	});
	
	// product description and image, kindEditor
	descriptionEditor = KindEditor.create('textarea[name="description"]', {
		uploadJson: appName + "file/uploadFile.html?type=product",
		fileManagerJson: appName + 'file/fileManage.html?type=product',
		allowFileManager : true,
		minHeight:400
	});
	// cycle image
	KindEditor('#image_add_btn').click(function() {
		descriptionEditor.loadPlugin('image', function() {
			descriptionEditor.plugin.imageDialog({
				clickFn : function(url, title, width, height, border, align) {
					addImage('#imgContent', '#inputContent', url);
					descriptionEditor.hideDialog();
				}
			});
		});
	});
	
	KindEditor('#image_delete_btn').click(function() {
		var index = $("#index").val();
		removeImage('#imgContent', '#inputContent', (parseInt(index)-1));
	});
});
