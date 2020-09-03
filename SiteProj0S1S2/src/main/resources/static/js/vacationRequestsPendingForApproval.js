$("#vrTable").tabulator({
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
		title : "Type",
		field : "type",
	},{
		title : "Start Date",
		field : "startDate",
	},{
		title : "End Date",
		field : "endDate",
	}
	
	],

});

//$("#doctorTable").tabulator("setData", setdata1); // OVDE IDE LINK PODATAKA O KLINICI
$(window).resize(function() {
	$("#vrTable").tabulator("redraw");
});

$.ajax({
	type : 'GET',
	url : "/getPendingVacationRequests",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#vrTable").tabulator("setData", successData);
		
		
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

$("#approveBtn").click(function() {
	var parsedData = getParsedDataFromTable("#vrTable");
	var vrId = parsedData.id;

	$.ajax({
		type : 'PUT',
		url : "/approveVacationRequest",
		contentType : 'application/json',	
		headers:{
			'token':localStorage.getItem('token'),
			'vrId': vrId
		},
		success : function(successData) {
			window.location.href = "../vacationRequestsPendingForApproval";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../doctorHome";
			}
		}
	});
});

$("#rejectBtn").click(function() {
	if (!$.trim($("#reasonTextArea").val())) {
		return;
	} else {
		var parsedData = getParsedDataFromTable("#vrTable");
		var vrId = parsedData.id;
		var notes = document.getElementById("reasonTextArea").value;
	
		$.ajax({
			type : 'PUT',
			url : "/rejectVacationRequest",
			contentType : 'application/json',	
			headers:{
				'token':localStorage.getItem('token'),
				'vrId': vrId,
				'notes': notes
			},
			success : function(successData) {
				window.location.href = "../vacationRequestsPendingForApproval";	
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(textStatus=="401"){			
					window.location.href = "../doctorHome";
				}
			}
		});
	}
});

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}