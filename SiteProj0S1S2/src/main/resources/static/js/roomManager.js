$("#roomTable").tabulator({
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

$(window).resize(function() {
	$("#roomTable").tabulator("redraw");
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

$("#editRoom").click(function() {
	var parsedData = getParsedDataFromTable("#roomTable");
	var roomId = parsedData.id;
	
	$.ajax({
		type : 'GET',
		url : "/isRoomReserved",
		dataType : "text",
		headers:{
			'token':localStorage.getItem('token'),
			'roomId': roomId
		},
		success : function(successData) {
			//$("#roomTable").tabulator("setData", successData);
			var isInUse = successData;
			if(isInUse == 'true') {
				alert("This room is reserved and therefore cannot be edited.");	
			} else if(isInUse == 'false')  {
				window.location.href = "../editRoom/"+roomId;
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
		}
	});

	
	//window.location.href = "../editRoom/"+roomId;	
});

$("#deleteRoom").click(function() {
	var parsedData = getParsedDataFromTable("#roomTable");
	var roomId = parsedData.id;
	var roomName = parsedData.name;
	var roomNumber = parsedData.number;

	$.ajax({
		type : 'GET',
		url : "/isRoomInUse",
		dataType : "text",
		headers:{
			'token':localStorage.getItem('token'),
			'roomId': roomId
		},
		success : function(successData) {
			//$("#roomTable").tabulator("setData", successData);
			var isInUse = successData;
			if(isInUse == 'true') {
				alert("This room is reserved or  and therefore cannot be deleted.");	
			} else if(isInUse == 'false')  {
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
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
		}
	});
	
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

