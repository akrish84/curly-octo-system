var kanbanObj;
var boardsMap;
var statusIDPrefix = "status-id-";
var applicationIDPrefix = "application-id-";
var dataEIDAttribute = "data-eid";
var dataIDAttribute = "data-id";
var $appCardTempalte;
var allAppData = {};
var statusesMap = {}
var statusIDSelectedToAddApplication=-1


function init() {
	$appCardTempalte = $('.app-card-template').clone();
	$('.templates').remove();
}

function showUserDashboard() {
	init()
	showUserBoards();
}

function refreshUserBoards() {
	document.getElementById("kdiv").innerHTML="";
	showUserBoards();
}

function showUserBoards() {
	var url = "jobstatuses";                
    sendAjaxRequest(url, function(resp){                                
        if (resp['responseMessage'] != null && resp['responseMessage'].includes('Error'))
        {               
            alert("Failed to fetch user info try again later");
        }
        else
        {
			statusesMap = resp.response['statusesMap'];
			var boards = [];
			for (var statusID in statusesMap){
				var status = statusesMap[statusID];
				var item = {};
				item['id'] = statusIDPrefix+statusID;
				item['title'] = status.status;
				boards.push(item);
			}
			kanban.init(boards);
			addUserApplications();
			populateModalStatusesSelectBox();
        }
    });
}

function addUserApplications(){
	var url = "applications";                
    sendAjaxRequest(url, function(resp){                                
        if (resp['responseMessage'] != null && resp['responseMessage'].includes('Error'))
        {               
            alert("Failed to fetch user info try again later");
        }
        else
        {
			var applications = resp['applications'];
			for(var i = applications.length-1; i>=0 ;i--) {
				application = applications[i];
				kanban.buildAppCard(application);
			}
        }
    });
}

function getAppIDFromDataEID(dataEid) {
	return dataEid.replace(applicationIDPrefix, "");
}

function getStatusIDFromDataID(dataId){
	return dataId.replace(statusIDPrefix, "");
}

function getDataID(element) {
	if(element != null ) {
		return element.getAttribute(dataIDAttribute)
	}
	return "";
}

function getDataEID(element) {
	if(element != null ) {
		return element.getAttribute(dataEIDAttribute)
	}
	return "";
}

function updateApplication(applicationID, oldStatusID, newStatusID, applicationIDs) {
	var params = {}
	params['applicationID'] = applicationID;
	params['newStatusID'] = newStatusID;
	var url = makeURL("application/update", params);
	for(var i = 0 ; i < applicationIDs.length; i++) {
		url += '&applicationIDs=' + applicationIDs[i];
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

function addApplication() {
	companyName = $("#modal-companyName").val();
	jobTitle = $("#modal-jobTitle").val();
	appliedDate = $("#modal-appliedDate").val();
	jobDescription = $("#modal-jobDescription").val();
	aps = $("#modal-aps").val();
}


function handleApplicationDragEnd(application, source, target) {
	var applicationDataEID = getDataEID(application);
	var sourceDataID = getDataID(source.parentNode);
	var targetDataID = getDataID(target.parentNode);

	var items = kanbanObj.getBoardElements(targetDataID);
	var applicationIDs = [];
	for(var i = 0 ; i < items.length; i++) {
		item = items[i];
		applicationIDs.push(getAppIDFromDataEID(getDataEID(item)));
	}
	applicationID = getAppIDFromDataEID(applicationDataEID);
	// For now not needed. In future need to do front end validation before sending request to backend.
	sourceStatusID = getStatusIDFromDataID(sourceDataID); 
	targetStatusID = getStatusIDFromDataID(targetDataID);

	updateApplication(applicationID, sourceStatusID, targetStatusID, applicationIDs);
}

function hideElement(id) {
	var x = document.getElementById(id);
	x.style.display = "none";
}

function showElement(id){
	var x = document.getElementById(id);
	x.style.display = "block";	
}


function addApplicationModalPreProcessing() {

	hideElement("applicationDetailsModalTitle");
	hideElement("modalSaveBtn");
	//hideElement("modalDeleteBtn");
	
	showElement("addApplicationModalTitle");
	showElement("modalAddBtn");
	
	document.getElementById("modal-companyName").removeAttribute("readonly")
	document.getElementById("modal-jobTitle").removeAttribute("readonly")
	document.getElementById("modal-appliedDate").removeAttribute("readonly")
	document.getElementById("modal-jobDescription").removeAttribute("readonly")
	document.getElementById("modal-aps").removeAttribute("readonly")
	document.getElementById("modal-appStatuses").removeAttribute("readonly")
}

function applicationDetailsModalPreProcessing(){ 
	hideElement("addApplicationModalTitle");
	hideElement("modalAddBtn");

	showElement("applicationDetailsModalTitle")
	showElement("modalSaveBtn")
	//showElement("modalDeleteBtn")

	document.getElementById("modal-companyName").setAttribute("readonly", "");
	document.getElementById("modal-jobTitle").setAttribute("readonly", "");
	document.getElementById("modal-appliedDate").setAttribute("readonly", "");
	document.getElementById("modal-jobDescription").setAttribute("readonly", "");
	document.getElementById("modal-aps").setAttribute("readonly", "");
	document.getElementById("modal-appStatuses").setAttribute("readonly", "");
	setCurrentDateAsAppliedDate();
}

function setFirstStatusAsSelectBoxValue(){
	var statusesSelectElement = document.getElementById('modal-appStatuses');
	for (var statusID in statusesMap){
		var status = statusesMap[statusID];
		var statusID = status["id"]
		var statusValue = status["status"]
		statusesSelectElement.value = statusValue;
		return
	}
}
function populateModalStatusesSelectBox() {
	var statusesSelectElement = document.getElementById('modal-appStatuses');
	selectBoxValue = ""
	for (var statusID in statusesMap){
		var status = statusesMap[statusID];
		var statusID = status["id"]
		var statusValue = status["status"]
		var opt = document.createElement('option');
		opt.text = statusValue;
		opt.value = statusValue;
		opt.setAttribute("id", statusID);
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
	document.getElementById("modal-appliedDate").value = today;
}
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
		    addItemButton    : true,                                         // add a button to board for easy item creation
		    buttonContent    : '+',                                          // text or html content of the board button
		    itemHandleOptions: {
		        enabled             : false,                                 // if board item handle is enabled or not
		        handleClass         : "item_handle",                         // css class for your custom item handle
		        customCssHandler    : "drag_handler",                        // when customHandler is undefined, jKanban will use this property to set main handler class
		        customCssIconHandler: "drag_handler_icon",                   // when customHandler is undefined, jKanban will use this property to set main icon handler class. If you want, you can use font icon libraries here
		        customHandler       : "<span class='item_handle'>+</span> %s"// your entirely customized handler. Use %s to position item title
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
			handleApplicationDragEnd(el, source, target)
			//console.log('Source: statusID ' + getIDFromSourceElement(source));
			// console.log('Target: statusID ' + getIDFromSourceElement(target));
        	// console.log('Target: ',target);
			// console.log('Sibling: ',sibling);
			// console.log("ElementID is " + el.getAttribute("data-eid"))
			// console.log("SourceID is " + source.getAttribute("-eid"))
			// console.log("TargetID is " + source.getAttribute("data-eid"))        	
        	// if (source != target){
        	// 	/* Update Database */
        		
        	// }
        	// else{
        	// 	/* Do Nothing */	
        	// }    		
        	
		}
		
		function dragBoard(el) {
			//console.log('Dragging: ',el);
		}

		function dragendBoard(el, source){
			console.log("Drag End: ",el," Source: ",source);
		}

		function buttonClick(el, boardId) {
			$("#modal-companyName").val("");	
			$("#modal-jobTitle").val("");
			$("#modal-appliedDate").val("");
			$("#modal-jobDescription").val("");
			$("#modal-aps").val("");
			$("#modal-aps").val("");
			$("#modal-appStatus").val("");
			addApplicationModalPreProcessing();
			$("#modalButton").click();
			addApplicationModalPreProcessing();
		}
	},

	buildAppCard : function(application) {
		var companyName = application.companyName;
		var id = application.id;
		var jobDescription = application.jobDescription;
		var jobTitle = application.jobDescription;
		var statusID = application.statusID;
		var aps = application.aps;
		var appliedDate = application.appliedDate;

		$appCard = $appCardTempalte.clone();
		$appCard.find(".company-name").text(companyName);
		$appCard.find(".job-title").text(jobTitle);
		$appCard.find(".applied-date").text(appliedDate);

		var appItem = {}
		appItem['id'] = applicationIDPrefix + id;
		var appValues = {}

		appValues.companyName = companyName;
		appValues.id = id;
		appValues.jobDescription = jobDescription;
		appValues.jobTitle = jobTitle;
		appValues.statusID = statusID;
		appValues.aps = aps;
		appValues.appliedDate = appliedDate;

		$appCard.attr("onclick", function (event) {			
			return "populateModal("+id+")";
		})		
		
		appItem['id'] = applicationIDPrefix + id;		
		appItem['title'] = $appCard.wrap("<div />").parent().html()

		kanbanObj.addElement(statusIDPrefix+statusID, appItem);
		allAppData[id] = appValues;		
	}
	
};

function populateModal(appID){	
	var appData = allAppData[appID];
	$("#modal-companyName").val(appData.companyName);	
	$("#modal-jobTitle").val(appData.jobTitle);
	$("#modal-appliedDate").val(appData.appliedDate);
	$("#modal-jobDescription").val(appData.jobDescription);
	$("#modal-aps").val(appData.aps);
	var statusesSelectElement = document.getElementById('modal-appStatuses');
	statusesSelectElement.value = statusesMap[appData.statusID]["status"];
	applicationDetailsModalPreProcessing();
	// $("#modalButton").click();
	showModal("modalButton");
}

$('#appModal').submit(function () {
	return false;
});

$("input.form-control").on("click", function(event) {				
	$(this).attr("readonly", false);	
})

$("input.form-control").on("blur", function (event) {	
	$(this).attr("readonly", true);
})

function showModal(name) {
	console.log(name);
	$("#" + name ).click();
}
