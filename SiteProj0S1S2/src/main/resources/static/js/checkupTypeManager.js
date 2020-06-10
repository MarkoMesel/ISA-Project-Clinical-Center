$("#ctTable").tabulator({
	height : "300px",
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
		width : 120,
	},{
		title : "Price",
		field : "price",
		width : 105,
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],
	
});

$(window).resize(function() {
	$("#ctTable").tabulator("redraw");
});

$.ajax({
	type : 'GET',
	url : "/getCtFromClinicAdmin",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#ctTable").tabulator("setData", successData);
		
		
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

/*

$("#editRoom").click(function() {
	var parsedData = getParsedDataFromTable("#roomTable");
	var roomId = parsedData.id;
	
	window.location.href = "../editRoom/"+roomId;	
});

$("#deleteRoom").click(function() {
	var parsedData = getParsedDataFromTable("#roomTable");
	var roomId = parsedData.id;
	var roomName = parsedData.name;
	var roomNumber = parsedData.number;

	if(window.confirm("Are you sure you want to remove room " + roomName + " " + roomNumber + "?")) {
		$.ajax({
			type : 'PUT',
			url : "/logicalDeleteRoom",
			contentType : 'application/json',	
			headers:{
				'token':localStorage.getItem('token'),
				'roomId': roomId
			},
			success : function(successData) {
				window.location.href = "../roomManager";	
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(textStatus=="401"){			
					window.location.href = "../roomManager";
				}
			}
		});
	}
});

$("#findRoomBtn").click(function() {
	var rName = $("#rName").val();
	var rNumber = $("#rNumber").val();
	
	$.ajax({
		type : 'GET',
		url : "/findRoomNameNumber",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'name': rName,
			'number': rNumber,
		},
		success : function(successData) {
			var foundId = successData.id;
			$("#roomTable").tabulator("selectRow", foundId);	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
			$("#foundDoctorTable").tabulator("clearData");
		}
	});
});

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData
}
*/
