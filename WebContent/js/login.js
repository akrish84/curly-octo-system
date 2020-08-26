$(document).ready(function () {            
    $("#your_email").focus();
});


$(".form_input").on("keyup",(function(event) { 
    if (event.keyCode === 13) { 
        $("#signin").click(); 
    }
    else{
		buttonActivator();
	}
}));
        
function buttonActivator()        
{    	
    $('#errorMessage')[0].style.display = 'none';
    if($("#your_email").val().length && $("#your_pass").val().length && ValidateEmail($("#your_email").val()))
    {                
        $('#signin')[0].classList.remove('customDisabled');
    }

}
$('#signin').on('click', function () {
    var login_variables = {};
    login_variables['email'] = $("#your_email").val();
    login_variables['password'] = $("#your_pass").val();            

    if (ValidateEmail(login_variables['email']))
    {
        var url = makeURL("loginAction", login_variables);                
        sendAjaxRequest(url, function(resp){                                
            if (resp['responseMessage'].includes('Error'))
            {        
                $('#errorMessage')[0].style.display = 'block';
                $('#errorMessage')[0].innerHTML = resp['responseMessage'];
            }
            else
            {
                $('#successMessage')[0].style.display = 'block';
                $('#successMessage')[0].innerHTML = resp['responseMessage'];
                setTimeout(function(){ window.location.href = "dashboard"; }, 500);                       
            }
        });
    }
});