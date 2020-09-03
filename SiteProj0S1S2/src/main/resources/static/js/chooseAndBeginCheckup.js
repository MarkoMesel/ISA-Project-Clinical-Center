$("#mcTable").tabulator({
	height : false,
	layout : 'fitColumns',
	movableCols : false,
	selectable:1,
	sortable: false,
	columns : [ {
		title : "ID",
		field : "id",
		visible : false
	},{
		title : "Patient",
		field : "patient",
	},{
		title : "Checkup Type",
		field : "checkupType",
	},{
		title : "Room",
		field : "room",
	},{
		title : "Date",
		field : "date",
	},{
		title : "Time",
		field : "time",
	},{
		title : "Duration (minutes)",
		field : "duration",
	},{
		title : "Price",
		field : "price",
	},{
		title : "Notes",
		field : "notes",
	}
	
	],

});

//$("#doctorTable").tabulator("setData", setdata1); // OVDE IDE LINK PODATAKA O KLINICI
$(window).resize(function() {
	$("#mcTable").tabulator("redraw");
});

$.ajax({
	type : 'GET',
	url : "/checkIfMcInProgress",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		var id = successData;
		if(id != 'ne') {
			window.location.href = "../beginMedicalCheckup/"+id;	
		}
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

$.ajax({
	type : 'GET',
	url : "/getReservedMcFromDoctor",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#mcTable").tabulator("setData", successData);
		
		
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

$("#beginCheckupBtn").click(function() {
	var parsedData = getParsedDataFromTable("#mcTable");
	var mcId = parsedData.id;
	
	$.ajax({
		type : 'PUT',
		url : "/mcInProgressYes/"+mcId,
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			window.location.href = "../beginMedicalCheckup/"+mcId;	
	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
		}
	});
	
});

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}