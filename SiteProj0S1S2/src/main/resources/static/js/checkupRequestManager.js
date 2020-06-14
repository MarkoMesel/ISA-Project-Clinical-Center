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
		title : "Doctor",
		field : "doctor"
	},{
		title : "Checkup Type",
		field : "checkupType",
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
	url : "/getCheckupsWithNoRoom",
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

$("#findRoomBtn").click(function() {
	var parsedData = getParsedDataFromTable("#mcTable");
	var mcId = parsedData.id;
	
	window.location.href = "../findRoomForCheckup/"+mcId;	
});

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}