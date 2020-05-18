function sendAjaxRequest(url, successCallBackFn) {
	$.ajax({
		type: "POST",
		url:url, 
		success: successCallBackFn,
		statusCode: {
		    419: function() {
		    	showPage("");
		    }
		},
		error: function(){
			alert("Unable to process your request.");
		}
	});
}


function checkAndAlertError(message) {
	if(message.includes("fail") || message.includes("error")) {
		alert(message);
	}
}

function makeURL(url, params) {
	var paramsAsURL = url + "?";
	var separator = ""
	for(var key in params) {
		paramsAsURL = paramsAsURL + separator + key + "=" + params[key];
		separator = "&";
	}
	return paramsAsURL;
}