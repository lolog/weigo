var festivalDialog = function (festival) {
	if (!festival || !festival.festivals) {
		return '';
	}
	
	var festivalOptions = '';
	(festival.festivals).each(function (value, index) {
		festivalOptions += '<option value="'+value['festival_id']+'">'+value['name']+'</option>';
	});
	
	// cloud not empty
	if(!festivalOptions) {
		return '';
	}
	
	var dialog = ''
		+ '<div class="hr-line-dashed"></div>'
		+ '<div class="row">'
		    + '<div class="col-sm-10">'
				+ '<div class="form-group">'    
			    	+ '<label class="col-sm-3 control-label">节假日：</label>'
				    + '<div class="col-sm-3">'
					    + '<div class="input-group">'
						    + '<select class="form-control" name="festival_id">'
							    + '<option value="">请选择节假日</option>'
							    + festivalOptions
						    + '</select>'
					    + '</div>'
				    + '</div>'
			    + '</div>'
			    + '<div class="form-group">'
				    + '<label class="col-sm-3 control-label">促销金额：</label>'
				    + '<div class="col-sm-3">'
					    + '<div class="input-group">'
						    + '<span class="input-group-addon">&yen;</span>'
						    + '<input name="discount_money" type="text" class="form-control">' 
					    + '</div>'
				    + '</div>'
			    	+ '<label class="col-sm-3 control-label">促销折扣：</label>'
				    + '<div class="col-sm-3">'
					    + '<div class="input-group">'
						    + '<input name="discount" type="text" class="form-control">' 
						    + '<span class="input-group-addon">.00</span>'
					    + '</div>'
				    + '</div>'
			    + '</div>'
			    + '<div class="form-group">'
				    + '<label class="col-sm-3 control-label">促销信息：</label>'
				    + '<div class="col-sm-9">'
				    	+ '<textarea name="information" type="text" class="form-control"></textarea>'
				    + '</div>'
			    + '</div>'
		    + '</div>'
	    + '</div>';
	return dialog;
};

var shopDialog = function (shop) {
	if (!shop || !shop.shops) {
		return '';
	}
	
	var shopOptions = '';
	(shop.shops).each(function (value, index) {
		shopOptions += '<option value="'+value['shop_id']+'">'+value['name']+'</option>';
	});
	
	// cloud not empty
	if(!shopOptions) {
		return '';
	}
	
	var dialog = ''
		+ '<div class="hr-line-dashed"></div>'
		+'<div class="row">'
			+'<div class="col-sm-10">'
				+'<div class="form-group">'
					+'<label class="col-sm-3 control-label">商家：</label>'
					+'<div class="col-sm-3">'
						+ '<div class="input-group">'
						    + '<select class="form-control" name="shop_id">'
							    + '<option value="">请选择节假日</option>'
							    + shopOptions
						    + '</select>'
				    + '</div>'
					+'</div>'
				+'</div>'
				+'<div class="form-group">'
					+'<label class="col-sm-3 control-label">地址：</label>'
					+'<div class="col-sm-9">'
						+'<input name="address" type="text" class="form-control"/>'
					+'</div>'
				+'</div>'
				+'<div class="form-group">'
					+'<label class="col-sm-3 control-label">金额：</label>'
					+'<div class="col-sm-3">'
						+'<div class="input-group">'
							+'<span class="input-group-addon">&yen;</span>'
							+'<input name="money" type="text" class="form-control"> '
						+'</div>'
					+'</div>'
					+'<label class="col-sm-3 control-label">折扣：</label>'
					+'<div class="col-sm-3">'
						+'<div class="input-group">'
							+'<span class="input-group-addon">￥</span>'
							+'<input name="discount_money" type="text" class="form-control">'
						+'</div>'
					+'</div>'
				+'</div>'
				+'<div class="form-group">'
					+'<label class="col-sm-3 control-label">信息：</label>'
					+'<div class="col-sm-9">'
						+'<textarea name="information" type="text" class="form-control"></textarea>'
					+'</div>'
				+'</div>'
			+'</div>'
		+'</div>';
	return dialog;
};


var addImage = function (img, input, url) {
	var appName  = $('#app').html(),
		img      = $(img),
		input    = $(input);
	
	img.append('<img alt="image" src="' + appName + "upload/" + url.substr(url.lastIndexOf('/product')) + '">');
	input.append('<input name="banner" type="hidden" value="upload/' + url.substr(url.lastIndexOf('product')) +'"/>');
};

var removeImage = function (img, input, index) {
	var imgLength = $(img).find('img').length;
	if(imgLength < index) {
		return;
	}
	$($(img).find("img")[index]).remove();
	$($(input).find("input")[index]).remove();
}

var getArrayMutiData = function (formId) {
	var data     = {},
		field    = [];
	var formData = $('#' + formId).serializeArray();
	
	if (formData.length == 0) {
		return [];
	}
	
	formData.each(function (value, index) {
		field[index] = value['name'];
		if(!data[value['name']]) {
			data[value['name']] = [];
		}
		data[value['name']].push(value['value']);
	});
	
	// array unique value
	field.unique();
	
	var result = [];
	for(var i=0; i<data[field[0]].length; i++) {
		var tmp = {};
		field.each(function (value, index) {
			tmp[value] = data[value][i];
		});
		result.push(tmp);
	}
	
	return result;
};

var getArrayJustOne = function (formId) {
	var data     = [];
	var formData = $('#' + formId).serializeArray();
	
	if (formData.length == 0) {
		return [];
	}
	
	formData.each(function (value, index) {
		if (value['value']) {
			data.push(value['value']);
		}
	});
	
	return data;
};

var getSimpleForm = function (formId) {
	var data     = {};
	var formData = $('#' + formId).serializeArray();
	
	if (formData.length == 0) {
		return {};
	}
	
	formData.each(function (value, index) {
		if (value['value']) {
			data[value['name']] = value['value'];
		}
	});
	
	return data;
};