function buttonActivator() { 
    var passMatch = false;
    $('#errorMessage')[0].style.display = 'none';
    document.getElementById('agree-term').checked = false;
    $('#signup').prop('disabled', 'true');
    $('#agree-term').prop('disabled', 'true');
    if($("#pass").val().length && $("#re_pass").val().length){
        if ($("#pass").val() === $("#re_pass").val())
        {
            $('#re_pass')[0].classList.add('alert-success'); 
            $('#pass')[0].classList.add('alert-success');
            $('#re_pass')[0].classList.remove('alert-danger');
            $('#pass')[0].classList.remove('alert-danger');
            passMatch = true;
        }
        else
        {
            $('#re_pass')[0].classList.remove('alert-success')
            $('#pass')[0].classList.remove('alert-success');
            $('#re_pass')[0].classList.add('alert-danger')
            $('#pass')[0].classList.add('alert-danger');                
        }
    }
    else
    {
        $('#re_pass')[0].classList.remove('alert-danger')
        $('#pass')[0].classList.remove('alert-danger');
        $('#re_pass')[0].classList.remove('alert-success')
        $('#pass')[0].classList.remove('alert-success');
    }
    if ($("#name").val().length && $("#last_name").val().length && $("#email").val().length && $("#pass").val().length && $("#re_pass").val().length && passMatch)
    {
        $('#agree-term').removeAttr('disabled');
        $('#agree-term')[0].classList.remove('customDisabled');
    }
    else
    {
        document.getElementById('agree-term').checked = false;
        $('#signup').prop('disabled', 'true');
        $('#agree-term').prop('disabled', 'true');
        $('#agree-term')[0].classList.add('customDisabled');
    }
}
function submitActivator() {
    if(document.getElementById('agree-term').checked)
    {
        $('#signup').removeAttr('disabled');               
        $('#signup')[0].classList.remove('customDisabled');
        $('#successMessage')[0].style.display = 'none';
        $('#errorMessage')[0].style.display = 'none';
    }
    else
    {
        $('#signup').prop('disabled', 'true');
        $('#signup')[0].classList.add('customDisabled');
    }
}        
$('#signup').on('click', function () {
    
    var registerVariables = {};
    registerVariables['firstName'] = $("#name").val();
    registerVariables['lastName'] = $("#last_name").val();
    registerVariables['email'] = $("#email").val();
    registerVariables['password'] = $("#pass").val();
    //registerVariables['re_password'] = $("#re_pass").val();
    //registerVariables['agree'] = $("#agree-term").val();
    console.log(registerVariables);

    if (ValidateEmail(registerVariables['email']) && ValidateName(registerVariables['firstName']) && ValidateName(registerVariables['lastName']))
    {
        // Make an AJAX
        var url = makeURL("signupAction", registerVariables);
        sendAjaxRequest(url, function(resp){
            console.log(resp);            	
            if (resp['responseMessage'].includes('Error'))
            {
                // Error
                // console.log($('#errorMessage'));
                $('#errorMessage')[0].style.display = 'block';
                $('#errorMessage')[0].innerHTML = resp['responseMessage'];
            }
            else
            {
                // Successfull Registration
                $('#successMessage')[0].style.display = 'block';
                $('#successMessage')[0].innerHTML = resp['responseMessage'];
                setTimeout(function(){ window.location.href = "login"; }, 500);                        
            }
        });            
    }
    else
    {
        $('#errorMessage')[0].style.display = 'block';
        $('#errorMessage')[0].innerHTML = 'Please enter valid Information';
    }
});
function showPasswordProgress()
{            
    document.getElementById('password-progress').style.display = 'block';
}
function hidePasswordProgress()
{            
    document.getElementById('password-progress').style.display = 'none';
}