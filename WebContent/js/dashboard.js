var kanbanObj;
var boardsMap;
var statusIDPrefix = "status-id-";
var applicationIDPrefix = "application-id-";
var $appCardTempalte;
function init() {
	$appCardTempalte = $('.app-card-template').clone();
	$('.templates').remove();
}

function showUserDashboard() {
	init()
	showUserBoards();
	// Add cards
}

function showUserBoards() {
	var url = "statuses";                
    sendAjaxRequest(url, function(resp){                                
        if (resp['responseMessage'] != null && resp['responseMessage'].includes('Error'))
        {               
            alert("Failed to fetch user info try again later");
        }
        else
        {
			var statusesMap = resp['statusesMap'];
			var boards = [];
			for (var statusID in statusesMap){
				var status = statusesMap[statusID];
				item = {};
				item['id'] = statusIDPrefix+statusID;
				item['title'] = status.status;
				boards.push(item);
			}           
			kanban.init(boards);
			addUserApplications();
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
			for(i in applications){
				application = applications[i];
				console.log(application.companyName);
				kanban.buildAppCard(application);
			}
        }
    });
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
		    buttonClick      : function(el, boardId) {}                      // callback when the board's button is clicked				
		});
		
		function click(el){
        	console.log('Item Clicked');
        }
        
        function dropEl(el, target, source, sibling){
        	console.log('Element: ',el);
        	console.log('Source: ',source);
        	console.log('Target: ',target);
        	console.log('Sibling: ',sibling);
        	
        	if (source != target){
        		/* Update Database */
        		
        	}
        	else{
        		/* Do Nothing */	
        	}            		
        	
		}
		
		function dragBoard(el) {
			console.log('Dragging: ',el);
		}

		function dragendBoard(el, source){
			console.log("Drag End: ",el," Source: ",source);
		}
	},

	buildAppCard : function(application) {
		companyName = application.companyName;
		id = application.id;
		jobDescription = application.jobDescription;
		jobTitle = application.jobDescription;
		statusID = application.statusID;
		aps = application.aps;
		appliedDate = application.appliedDate;

		$appCard = $appCardTempalte.clone();
		$appCard.find(".company-name").text(companyName);
		$appCard.find(".job-title").text(jobTitle);
		$appCard.find(".applied-date").text(appliedDate);

		item['id'] = applicationIDPrefix + id;
		console.log($appCard.html());
		item['title'] = $appCard.wrap("<div />").parent().html()
		kanbanObj.addElement(statusIDPrefix+statusID, item);
	}
	
};