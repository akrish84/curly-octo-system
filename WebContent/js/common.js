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

function ValidateEmail(mail) {
	console.log('Validate');
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
		return (true)
	}
	// alert("You have entered an invalid email address!")
	return (false)
}
function ValidateName(name) {
	if (/^([a-zA-Z ]){1,15}$/.test(name)) {
		return (true)
	}
	// alert("You have entered an invalid name!")
	return (false)
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

function makeLoginURL(url, params){
	var paramsAsURL = url + "?";
	var separator = ""
	for (var key in params) {
		paramsAsURL = paramsAsURL + separator + key + "=" + params[key];
		separator = "&";
	}
	return paramsAsURL;
}