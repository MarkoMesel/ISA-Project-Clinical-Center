var url = document.URL;
var uId = url.substring(url.lastIndexOf('/') + 1);

$.ajax({
	type : 'GET',
	url : "/getUserInfo/"+uId,
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#name").html("Medical record of " + successData.firstName + " " + successData.lastName + ":");
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

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
		title : "Doctor",
		field : "doctor",
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
	},{
		title : "Finished",
		field : "finished"
	}
	
	],

});

//$("#doctorTable").tabulator("setData", setdata1); // OVDE IDE LINK PODATAKA O KLINICI
$(window).resize(function() {
$("#mcTable").tabulator("redraw");
});

$.ajax({
	type : 'GET',
	url : "/getMcFromPatient/"+uId,
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
	
	window.location.href = "../beginMedicalCheckup/"+mcId;	
});

$("#medicalRecord").click(function() {
	window.location.href = "../medicalRecord/"+uId;	
});

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}