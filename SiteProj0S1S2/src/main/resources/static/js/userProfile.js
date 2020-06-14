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
		$("#firstName").html("First name: <b>" + successData.firstName + "</b>");
		$("#lastName").html("Last name: <b>" + successData.lastName + "</b>");
		$("#country").html("Country: <b>" + successData.country + "</b>");
		$("#city").html("City: <b>" + successData.city + "</b>");
		$("#street").html("Street: <b>" + successData.street + "</b>");
		$("#phone").html("Phone: <b>" + successData.phone + "</b>");
		$("#jmbg").html("JMBG: <b>" + successData.jmbg + "</b>");
		$("#email").html("Email: <b>" + successData.email + "</b>");
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
	url : "/getReservedMcFromDoctorAndPatient/"+uId,
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