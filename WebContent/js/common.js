
loadHeaderAndFooter();

function loadHeaderAndFooter() {
	fetch("header")
	    .then(response => {
	        return response.text()
	    })
	    .then(data => {
	        document.querySelector("header").innerHTML = data;
	});
	fetch("footer")
	    .then(response => {
	        return response.text()
	    })
	    .then(data => {
	        document.querySelector("footer").innerHTML = data;
	});
}

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

function sendAjaxRequest(url, successCallBackFn, errorCallBackFn) {
	$.ajax({
		type: "POST",
		url:url, 
		success: successCallBackFn,
		error: errorCallBackFn
	});
}

function ValidateEmail(mail) {	
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

function showElementById(id){
	$("#" + id).show();
}

function hideElementById(id) {
	$("#" + id).hide();
}

function showModalById(id) {
	$("#" + id ).modal().show();
}

function hideModalById(id) {
	$("#" + id ).modal().hide();
	$('.modal-backdrop').hide();
}



function showStatusMessage(msg) {
	document.getElementById("statusMessageDiv").className="alert alert-dismissible";
	$('#statusMessage').html(msg);
	$("#statusMessageDiv").css("display", "inline-block");
	hideFunction = setTimeout(function(){ $("#statusMessageDiv").fadeOut(1000);}, 10000);
}

function closeStatusMsg(status)
{
	document.getElementById(status).style.display = "none";
}