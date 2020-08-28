var kanbanObj;
var boardsMap;
var statusIdPrefix = "status-id-";
var jobApplicationIdPrefix = "application-id-";
var dataEIdAttribute = "data-eid";
var dataIdAttribute = "data-id";
var $jobAppCardTempalte;
var allAppData = {};
var statusesMap = {}
var statusIdSelectedToAddApplication=-1

$(function() {
    $("#addAppAppliedDate").datepicker();            
    init();
});

function init() {
	showKanbanLoading();
	$jobAppCardTempalte = $('.job-app-card-template').clone();
	$('.templates').remove();
	showUserBoards();
	hideKanbanLoading();
}

function refreshUserBoards() {
	document.getElementById("kdiv").innerHTML="";
	showUserBoards();
}

function showUserBoards() {
	var url = "jobStatuses";                
    sendAjaxRequest(url, function(resp){                                
        if (resp['responseMessage'] != null && resp['responseMessage'].includes('Error'))
        {               
            alert("Failed to fetch user info try again later");
        }
        else
        {
			statusesMap = resp.response['jobStatusesMap'];
			var boards = [];
			for (var statusId in statusesMap){
				var status = statusesMap[statusId];
				var item = {};
				item['id'] = statusIdPrefix+statusId;
				item['title'] = status.status;
				boards.push(item);
			}
			kanban.init(boards);
			addUserJobApplications();
        }
    });
}

function addUserJobApplications(){
	var url = "jobApplications";                
    sendAjaxRequest(url, function(resp){                                
        if (resp['responseMessage'] != null && resp['responseMessage'].includes('Error'))
        {               
            alert("Failed to fetch user info try again later");
        }
        else
        {
			var applications = resp.response['jobApplications'];
			for(var i = applications.length-1; i>=0 ;i--) {
				application = applications[i];
				kanban.buildAppCard(application);
			}
        }
    });
}

function showKanbanLoading() {
	showElementById("kanban-loading");
}

function hideKanbanLoading() {
	hideElementById("kanban-loading");
}

function getAppIdFromDataEId(dataEid) {
	return dataEid.replace(jobApplicationIdPrefix, "");
}

function getStatusIdFromDataId(dataId){
	return dataId.replace(statusIdPrefix, "");
}

function getDataId(element) {
	if(element != null ) {
		return element.getAttribute(dataIdAttribute)
	}
	return "";
}

function getDataEId(element) {
	if(element != null ) {
		return element.getAttribute(dataEIdAttribute)
	}
	return "";
}

function updateApplication(applicationId, oldStatusId, newStatusId, applicationIds) {
	var params = {}
	params['applicationId'] = applicationId;
	params['newStatusId'] = newStatusId;
	var url = makeURL("application/update", params);
	for(var i = 0 ; i < applicationIds.length; i++) {
		url += '&applicationIds=' + applicationIds[i];
	}
	sendAjaxRequest(url, function(resp){                                
        if (resp['responseMessage'] != null && resp['responseMessage'].includes('Error'))
        {               
			alert("Failed to save app reordering");
			refreshUserBoards();
        } else {
			console.log("Successfully saved app card change");
		}
    });
	
}

function handleJobApplicationDragEnd(application, source, target) {
	var applicationDataEId = getDataEId(application);
	var sourceDataId = getDataId(source.parentNode);
	var targetDataId = getDataId(target.parentNode);

	var items = kanbanObj.getBoardElements(targetDataId);
	var applicationIds = [];
	for(var i = 0 ; i < items.length; i++) {
		item = items[i];
		applicationIds.push(getAppIdFromDataEId(getDataEId(item)));
	}
	applicationId = getAppIdFromDataEId(applicationDataEId);
	// For now not needed. In future need to do front end validation before sending request to backend.
	sourceStatusId = getStatusIdFromDataId(sourceDataId); 
	targetStatusId = getStatusIdFromDataId(targetDataId);

	updateApplication(applicationId, sourceStatusId, targetStatusId, applicationIds);
}

function hideElement(id) {
	var x = document.getElementById(id);
	x.style.display = "none";
}

function showElement(id){
	var x = document.getElementById(id);
	x.style.display = "block";	
}

function setFirstStatusAsSelectBoxValue(){
	var statusesSelectElement = document.getElementById('modal-appStatuses');
	for (var statusId in statusesMap){
		var status = statusesMap[statusId];
		var statusId = status["id"]
		var statusValue = status["status"]
		statusesSelectElement.value = statusValue;
		return
	}
}
function populateStatusesForAddAppSelectBox() {
	var statusesSelectElement = document.getElementById('addAppJobStatus');
	var selectBoxValue = ""
	for (var statusId in statusesMap){
		var status = statusesMap[statusId];
		var statusId = status["id"]
		var statusValue = status["status"]
		var opt = document.createElement('option');
		opt.text = statusValue;
		opt.value = statusValue;
		opt.setAttribute("id", statusId);
		statusesSelectElement.appendChild(opt);
		if(selectBoxValue == "") {
			selectBoxValue = statusValue;
		}
	}
}

function setCurrentDateAsAppliedDate() {
	var now = new Date();
	var day = ("0" + now.getDate()).slice(-2);
	var month = ("0" + (now.getMonth() + 1)).slice(-2);
	var today = now.getFullYear()+"-"+(month)+"-"+(day) ;
	console.log(today)
	document.getElementById("editAppAppliedDate").value = today;
}


function addJobApplication() {
	var jobApplication = createApplicationObject($("#addAppCompanyName").val(), $("#addAppAppliedDate").val(), $("#addAppJobStatus").val(), $("#addAppJobTitle").val(), $("#addAppJobDescription").val(), $("#addAppAts").val(), $("#addAppNotes").val());
	var params = {};
    params['request'] = JSON.stringify(jobApplication);
    var url = makeURL("jobapplication/add", params);
    console.log(url);
    showElementById("addjobapplication-loading");
    successCallbackFn = function(resp) {
    	console.log(resp);
    	hideElementById("addjobapplication-loading");
    }
    
    errorCallbackFn = function(resp) {
    	console.log(resp);
    	hideElementById("addjobapplication-loading");
    }
    
    sendAjaxRequest(url, successCallbackFn, errorCallbackFn);
    
}


function createApplicationObject(companyName, appliedDate, jobStatus, jobTitle, jobDescription, ats, notes) {
	var jobApplication = new Object();
	jobApplication.companyName = companyName;
	jobApplication.appliedDate = appliedDate;
	jobApplication.jobStatus = jobStatus;
	jobApplication.jobTitle = jobTitle;
	jobApplication.jobDescription = jobDescription;
	jobApplication.ats = ats;
	jobApplication.nodes = notes;
	return jobApplication;
}

function populateJobApplicationModal(jobAppId){
	$("#editAppCompanyName").val("");	
	$("#editAppJobTitle").val("");
	$("#editAppAppliedDate").val("");
	$("#editAppJobDescription").val("");
	$("#editAppAps").val("");
	$("#editAppStatuses").val("");

	var appData = allAppData[jobAppId];
	$("#editAppCompanyName").val(appData.companyName);	
	$("#editAppJobTitle").val(appData.jobTitle);
	$("#editAppAppliedDate").val(appData.appliedDate);
	$("#editAppJobDescription").val(appData.jobDescription);
	$("#editAppAps").val(appData.aps);
	var statusesSelectElement = document.getElementById("editAppStatuses");
	statusesSelectElement.value = statusesMap[appData.statusId]["status"];
	showModalById("editJobApplicationModal");
}

$('#addAppForm').submit(function () {
	return false;
});
$('#editAppForm').submit(function () {
	return false;
});

var kanban = {
	init : function(boards)
	{
		kanbanObj = new jKanban({
		    element          : '#kdiv',                                      // selector of the kanban container
		    gutter           : '15px',                                       // gutter of the board
		    widthBoard       : '250px',                                      // width of the board
		    responsivePercentage: false,                                     // if it is true I use percentage in the width of the boards and it is not necessary gutter and widthBoard
		    dragItems        : true,                                         // if false, all items are not draggable
		    boards           : boards,                      					  // json of boards
		    dragBoards       : true,                                        // the boards are draggable, if false only item can be dragged
		    addItemButton    : false,                                         // add a button to board for easy item creation
		    buttonContent    : '',                                          // text or html content of the board button
		    itemHandleOptions: {
		        enabled             : false,                                 // if board item handle is enabled or not
//		        handleClass         : "item_handle",                         // css class for your custom item handle
//		        customCssHandler    : "drag_handler",                        // when customHandler is undefined, jKanban will use this property to set main handler class
//		        customCssIconHandler: "drag_handler_icon",                   // when customHandler is undefined, jKanban will use this property to set main icon handler class. If you want, you can use font icon libraries here
		        customHandler       : '<i class="fas fa-ellipsis-v"></i>'// your entirely customized handler. Use %s to position item title
		    },
		    click            : function (el) {click(el)},                             // callback when any board's item are clicked
		    dragEl           : function (el, source) {},                     // callback when any board's item are dragged
		    dragendEl        : function (el) {},                             // callback when any board's item stop drag
		    dropEl           : function (el, target, source, sibling) {dropEl(el, target, source, sibling)},    // callback when any board's item drop in a board
		    dragBoard        : function (el, source) {dragBoard(el)},                     // callback when any board stop drag
		    dragendBoard     : function (el) {dragendBoard(el)},                             // callback when any board stop drag
		    buttonClick      : function(el, boardId) {buttonClick(el, boardId)}                      // callback when the board's button is clicked				
		});
		
		function click(el){			
        }
        
        function dropEl(el, target, source, sibling){

			// console.log('Element: ',el);
        	handleJobApplicationDragEnd(el, source, target)
        }
		
		function dragBoard(el) {
			//console.log('Dragging: ',el);
		}

		function dragendBoard(el, source){
			console.log("Drag End: ",el," Source: ",source);
		}

		function buttonClick(el, boardId) {
			$("#addAppCompanyName").val("");	
			$("#addAppJobTitle").val("");
			$("#addAppAppliedDate").val("");
			$("#addAppJobDescription").val("");
			$("#addAppAps").val("");
			$("#addAppJobStatus").val(boardId);
			$("#addAppAppliedDate").datepicker('setDate', new Date());

			showModalById("addJobApplicationModal");
		}
	},

	buildAppCard : function(jobApplication) {
		var companyName = jobApplication.companyName;
		var id = jobApplication.id;
		var jobDescription = jobApplication.jobDescription;
		var jobTitle = jobApplication.jobDescription;
		var statusId = jobApplication.statusID;
		var aps = jobApplication.aps;
		var appliedDate = jobApplication.appliedDate;

		$appCard = $jobAppCardTempalte.clone();
		$appCard.find(".company-name").text(companyName);
		$appCard.find(".job-title").text(jobTitle);
		$appCard.find(".applied-date").text(appliedDate);

		var appItem = {}
		appItem['id'] = jobApplicationIdPrefix + id;
		var appValues = {}

		appValues.companyName = companyName;
		appValues.id = id;
		appValues.jobDescription = jobDescription;
		appValues.jobTitle = jobTitle;
		appValues.statusId = statusId;
		appValues.aps = aps;
		appValues.appliedDate = appliedDate;

		$appCard.attr("onclick", function (event) {			
			return "populateModal("+id+")";
		})		
		
		appItem['id'] = jobApplicationIdPrefix + id;		
		appItem['title'] = $appCard.wrap("<div />").parent().html()

		kanbanObj.addElement(statusIdPrefix+statusId, appItem);
		allAppData[id] = appValues;		
	}
	
};
