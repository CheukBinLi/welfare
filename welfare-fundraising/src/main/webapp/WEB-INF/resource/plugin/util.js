var msgModel = {
	success : 1,
	fail : -1,
	getCode : function(data) {
		return data.code
	},
	getMsg : function(data) {
		if (undefined != data.msg)
			return data.msg;
		return '';
	},
	getData : function(data) {
		if (undefined != data.data)
			return data.data;
		return '';
	},
	getAttachment : function(data) {
		if (undefined != data.attachment)
			return data.attachment;
		return '';
	},
	isSuccess : function(data) {
		return this.getCode(data) == this.success;
	}
}

var messageModel = function(data) {
	// this.code = data.code;
	// this.msg = data.msg;
	// this.data = data.data;
	// this.attachment = data.attachment;

	var obj = new Object();
	obj.data = data
	obj.getCode = function() {
		return this.data.code;
	}
	obj.getmsg = function() {
		if (undefined != this.data.msg)
			return this.data.msg;
		return null;
	}
	obj.getData = function() {
		if (undefined != this.data.data)
			return this.data.data;
		return null;
	}
	obj.getAttachment = function() {
		if (undefined != this.data.attachment)
			return this.data.attachment;
		return null;
	}
	return obj;
}