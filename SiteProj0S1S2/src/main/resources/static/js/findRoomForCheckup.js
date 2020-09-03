var chosenDate = "";
var url = document.URL;
var mcId = url.substring(url.lastIndexOf('/') + 1);

$("#roomTable").tabulator({
	height : "false",
	layout : 'fitColumns',
	movableCols : false,
	selectable:false,
	sortable: false,
	columns : [ {
		title : "ID",
		field : "id",
		visible : false
	},{
		title : "Name",
		field : "name",
		width : 120,
	},{
		title : "Number",
		field : "number",
		width : 105,
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],
	
});

$("#foundRoomTable").tabulator({
	height : "false",
	layout : 'fitColumns',
	movableCols : false,
	selectable:1,
	sortable: false,
	columns : [ {
		title : "ID",
		field : "id",
		visible : false
	},{
		title : "Name",
		field : "name",
		headerFilter:true
	},{
		title : "Number",
		field : "number",
		headerFilter:true
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	], rowSelected:function(e, row){
        //row.toggleSelect();
        
    	var parsedData = getParsedDataFromTable("#foundRoomTable");
    	var pId = parsedData.id;
    	
    	var rName = parsedData.name;
    	var rNumber = parsedData.number;
    	
    	$("#busyDatesFor").html("Busy dates for <b>"+ rName + "-" + rNumber + "</b><br/><br/>");
    	
    	$.ajax({
    		type : 'GET',
    		url : "/getBusyDates/"+pId,
    		dataType : "json",
    		headers:{
    			'token':localStorage.getItem('token')
    		},
    		success : function(successData) {
    			$("#busyDatesTable").tabulator("setData", successData);
    			
    			
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    			if(textStatus=="401"){			
    				window.location.href = "../whatAreYou";
    			}
    		}
    	});
    }
	
});

$("#busyDatesTable").tabulator({
	height : "false",
	layout : 'fitColumns',
	movableCols : false,
	selectable:false,
	sortable: false,
	columns : [ {
		title : "Date",
		field : "date",
		headerFilter:true
	}
	
	],
	
});


$(window).resize(function() {
	$("#roomTable").tabulator("redraw");
	$("#foundRoomTable").tabulator("redraw");
	$("#busyDatesTable").tabulator("redraw");
});

$.ajax({
	type : 'GET',
	url : "/getRoomsFromClinicAdmin",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#roomTable").tabulator("setData", successData);
		
		
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

$("#chooseRoomDateBtn").click(function() {
	var parsedData = getParsedDataFromTable("#foundRoomTable");
	var roomId = parsedData.id;
	
	$.ajax({
		type : 'PUT',
		url : "/saveRoomAndDate",
		contentType : 'application/json',	
		headers:{
			'token':localStorage.getItem('token'),
			'mcId':mcId,
			'roomId':roomId,
			'chosenDate':chosenDate
		},
		success : function(successData) {
			window.location.href = "../clinicAdminHome";
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../doctorHome";
			}
			else{
				window.location.href = "../beginMedicalCheckup/"+mcId;			
			}
		}
	});	
});



$("#findRoomNameBtn").click(function() {
	var searchByThis = $("#rNameOrNum").val();
	var rDate = $("#rDate").val();
	
	chosenDate = $("#rDate").val();
	
	$("#roomsFreeOn").html("Rooms \'" + searchByThis + "-?\'<br/>free on <b>"+ chosenDate + "</b>");
	
	$.ajax({
		type : 'GET',
		url : "/findRoomByName",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'searchByThis': searchByThis,
			'rDate': rDate,
		},
		success : function(successData) {
			$("#foundRoomTable").tabulator("setData", successData);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
			//$("#foundDoctorTable").tabulator("clearData");
		}
	});
});

$("#findRoomNumBtn").click(function() {
	var searchByThis = $("#rNameOrNum").val();
	var rDate = $("#rDate").val();
	
	chosenDate = $("#rDate").val();
	
	$("#roomsFreeOn").html("Rooms \'?-" + searchByThis + "\'<br/> free on <b>"+ chosenDate + "</b>");
	
	$.ajax({
		type : 'GET',
		url : "/findRoomByNumber",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'searchByThis': searchByThis,
			'rDate': rDate,
		},
		success : function(successData) {
			$("#foundRoomTable").tabulator("setData", successData);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
			//$("#foundDoctorTable").tabulator("clearData");
		}
	});
});

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}

