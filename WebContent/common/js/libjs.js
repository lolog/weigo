function Ajax (option) {
    /**
     * {
     *      option : {action:'',form:'',file:'',isArray:''},
     *      data   : {csrf:''}
     * }
     */
	
    this._option = option ? option['option'] : null;
    this._form   = this._option['form'] ? $(this._option['form']) : null;
    this._data   = option['data'] ? option['data'] : [];
    this._fields();
}

Ajax.prototype._join = function (dist,dest){
	for(var item in dest){
		dist[item] = dest[item];	
	}
	return dist;
};

Ajax.prototype._link = function (dest){
	if (!dest || dest.length <1) {
		return '';
	}
	
	var _str = '';
	for(var item in dest){
		_str = '&' + item + '=' + dest[item];	
	}
	return _str;
};

Ajax.prototype._fields = function () {
    if(!this._option['form']) {
        return;
    }
    
    var input = this._form.find("input");
    for (var i=0; i<input.size(); i++) {
        this._data[input[i].name] = input[i].value;
    }
    
    input = this._form.find('textarea');
    for (var i=0; i<input.size(); i++) {
        this._data[input[i].name] = input[i].value;
    }
    
    input = this._form.find("select");
    for (var i=0; i<input.size(); i++) {
        this._data[input[i].name] = input[i].value;
    }
};

Ajax.prototype.fieldFn = function (fn) {
	var _this = this;
	if (typeof fn == 'function') {
		fn(_this._data);
	}
};

Ajax.prototype.setData = function (data) {
	this._data = data;
	return true;
};

Ajax.prototype.getData = function () {
	return this._data;
};

Ajax.prototype.getForm = function () {
	return this._form;
};

Ajax.prototype.clearForm = function () {
	if(!this._option['form']) {
        return;
    }
	for (var i=0; i<this._form.size(); i++) {
		this._form[i].reset();
    }
	
	// remove iCheck class
	$('.i-checks .checked').removeClass('checked');
};

Ajax.prototype.setFormData = function (data) {
	/**
	 * [
	 * 	{'value':'lolog', selector:'input', name='name', id:'name', textarea:true, box:false}
	 * ]
	 */
	if (!data 
			|| data instanceof  Array == false
			|| !this._option['form'] ) {
		return;
	}
	
	var _this = this;
	
	$.each(data, function (key, val) {
		if (!val) {
			return;
		}
		// textarea
		if(val['textarea'] == true) {
			if (val['id']) {
				$(_this._option['form'] +' textarea#' + val['id']).html(val['value']);
			}
			else if (val['name']){
				$(_this._option['form'] +' textarea[name="' + val['name']+'"]').html(val['value'])
			}
		}
		// checkbox or radio
		else if(val['box'] == true) {
			val['value'] = (val['value'] == true) ? true : false;
			if (val['id']) {
				if (val['value']) {
					$('.i-checks .checked').addClass('checked');
				}
				$(_this._option['form'] +' input#' + val['id']).attr("selected", val['value']);
			}
			else if (val['name']){
				if (val['value']) {
					$('.i-checks div').addClass('checked');
				}
				$(_this._option['form'] +' input[name="' + val['name']+'"]').attr("selected", val['value']);
			}
		}
		// other
		else if(!val['selector'] || val['selector'] == 'input') {
			if (val['id']) {
				$(_this._option['form'] +' input#' + val['id']).val(val['value']);
			}
			else if (val['name']){
				$(_this._option['form'] +' input[name="' + val['name']+'"]').val(val['value']);
			}
		}
		else {
			
		}
	});
	
};


Ajax.prototype.post = function (success,error) {
	var _this = this;
	if (!_this._option['action']) {
		return;
	}
	// JSON
	if (!_this._option['isArray']) {
		_this._data = {data: JSON.stringify(_this._data)};
    }
    $.ajax({
        url:_this._option['action'],
        data: _this._data,
        dataType:"JSON",
        type: 'POST',
        success:function(msg) {
            if (msg && msg.error == 1) {
                window.location.reload();
            }
        	success(msg);
        },
        error: function(msg){
            if(error) {
        	    error(msg);
            }
            else {
                // 刷新界面
                window.location.reload();
            }
        }
    });
};

Ajax.prototype.get = function (success,error) {
	if (!_this._option['action']) {
		return;
	}
	
	var _this = this;
    $.ajax({
        url:_this._option['action'],
        data: _this._data,
        dataType:"JSON",
        type: 'POST',
        success:function(msg) {
            // 登录超时，刷新界面
            if(msg && msg.error == 1) {
                window.location.reload();
            }
        	success(msg);
        },
        error: function(msg){
            if(error) {
        	    error(msg);
            }
        }
    });
};

Ajax.prototype.uploadfile = function (success,error) {
	if (!_this._option['action'] || !_this._option['file']) {
		return;
	}
	
    var _this = this;
    $.ajaxFileUpload({
        // 请求的地址
        url  : _this._option['action'],
        // 请求的数据类型
        type : "JSON",
        // 请求的数据
        data : _this._data,
        secureuri     : false,
        // 上传的图片
        fileElementId : _this._option['file'],
        // 返回的数据类型
        dataType      : "JSON",
        // 成功处理函数
        success : function (msg) {
            success(msg);
        },
        // 错误处理函数
        error   : function (msg) {
            if (error) {
                error(msg);    
            }
            else {
                // 刷新页面
                window.location.reload();
            }
            
        }
    });
};

Ajax.prototype.synDefer = function (success,error) {
	var defer = $.Deferred();
    var _this = this;
    $.ajax({
        url:_this._option['action'],
        data: _this._data,
        dataType:"JSON",
        type: 'POST',
        success:function(msg) {
        	defer.resolve(msg);
        	$.when(defer.promise()).done(function (_data) {
        		if (_data && _data.error == 1) {
                    window.location.reload();
                }
            	success(_data);
        	});
        },
        error: function(msg){
        	defer.resolve(msg);
        	$.when(defer.promise()).done(function (_data) {
        		if (_data && _data.error == 1) {
                    window.location.reload();
                }
        		error(_data);
        	});
        }
    });
};

var QUR = function (config) {
	/**
     * {
     *      option : {action:''},
     *      data   : {csrf:''}
     * }
     */
	this.action = (config && config.option)? config.option.action : null;
	this.data   = config ? config.data   : {};
};

QUR.prototype.setData = function (data) {
	if (data) {
		this.data = data;
		return true;
	}
	return false;
};

QUR.prototype.setAction = function (action) {
	if (action) {
		this.action = action;
		return true;
	}
	return false;
};

QUR.prototype.post = function (success,error,config) {
	if (config) {
		this.action = (config && config.option)? config.option.action : null;
		this.data   = config ? config.data   : {};
	}
	
	var _this = this;
    $.ajax({
        url:_this.action,
        data: _this.data,
        dataType:"JSON",
        type: 'POST',
        success:function(msg) {
        	success(msg);
        },
        error: function(msg){
            if(error) {
        	    error(msg);
            }
            else {
                // 刷新界面
                window.location.reload();
            }
        }
    });
};

QUR.prototype.get = function (success,error,config) {
	if (config) {
		this.action = (config && config.option)? config.option.action : null;
		this.data   = config ? config.data   : {};
	}
	
	var _this = this;
	$.ajax({
		url:_this.action,
		data: _this.data,
		dataType:"JSON",
		type: 'POST',
		success:function(msg) {
			success(msg);
		},
		error: function(msg){
			if(error) {
				error(msg);
			}
			else {
				// 刷新界面
				window.location.reload();
			}
		}
	});
};

QUR.prototype.synDefer = function (success,error,config) {
	if (config) {
		this.action = (config && config.option)? config.option.action : null;
		this.data   = config ? config.data   : {};
	}
	
	var defer = $.Deferred();
    var _this = this;
    $.ajax({
        url:_this.action,
        data: _this.data,
        dataType:"JSON",
        type: 'POST',
        success:function(msg) {
        	defer.resolve(msg);
        	$.when(defer.promise()).done(function (_data) {
            	success(_data);
        	});
        },
        error: function(msg){
        	defer.resolve(msg);
        	$.when(defer.promise()).done(function (_data) {
        		if (error) {
        			error(_data);
                }
        		else {
        			window.location.reload();
        		}
        	});
        }
    });
};


Array.prototype.remove = function(dx) 
{ 
    if(isNaN(dx) || dx > this.length) {return false;} 
    for(var i=0,n=0;i<this.length;i++) 
    { 
        if(this[i]!=this[dx]) 
        { 
            this[n++]=this[i] 
        } 
    } 
    this.length-=1;
} 

var Tools = function (option) {
	this._option = option ? option['option'] : null;
	
	// form 
	this._form = this._option ? $(this._option['form']) : null;
};

Tools.prototype.curentTime = function()  
{   
    var now = new Date();  
         
    var year = now.getFullYear();       // year
    var month = now.getMonth() + 1;     // month
    var day = now.getDate();            // day
         
    var hh = now.getHours();            // hour
    var mm = now.getMinutes();          // minutes
    var ss=now.getSeconds();            // seconds
         
    var clock = year + "-";  
         
    if(month < 10) clock += "0";         
    clock += month + "-";  
         
    if(day < 10) clock += "0";   
    clock += day + " ";  
         
    if(hh < 10) clock += "0";  
    clock += hh + ":";  
  
    if (mm < 10) clock += '0';   
    clock += mm+ ":";  
          
    if (ss < 10) clock += '0';   
    clock += ss;  
  
    return (clock);   
}  

Tools.prototype.setText = function (data) {
	/**
	 * [
	 * 	{"selecter":".active", "val":"title",isOnly:true}
	 * ]
	 */
	if (!data
		|| !this._option['form']
		|| data instanceof Array == false) {
		return;
	}
	
	var _this = this;
	
	$.each(data, function (key, val) {
		if(val['isOnly']) {
			$(val['selecter']).html(val['val']);
		}
		else {
			$(_this._option['form'] + ' ' + val['selecter']).html(val['val']);
		}
	});
};

var Win = function () {
	
};
Win.open = function (title,url) {
	var index = layer.open({
		type: 2,
		title: title,
		content: url
	});
	layer.full(index);
};

Array.prototype.remove = function(dx) 
{ 
    if(isNaN(dx) || dx > this.length) {return false;} 
    for(var i=0,n=0;i<this.length;i++) 
    { 
        if(this[i]!=this[dx]) 
        { 
            this[n++]=this[i] 
        } 
    } 
    this.length-=1;
};
Array.prototype.each=function(callback)
{
    for (var i=0;i<this.length;i++)
    {
       callback.call(this, this[i], i);
    }
};
Array.prototype.unique = function()
{
	var n = []; //一个新的临时数组
	for(var i = 0; i < this.length; i++) //遍历当前数组
	{
		//如果当前数组的第i已经保存进了临时数组，那么跳过，
		//否则把当前项push到临时数组里面
		if (n.indexOf(this[i]) == -1) n.push(this[i]);
	}
	return n;
};