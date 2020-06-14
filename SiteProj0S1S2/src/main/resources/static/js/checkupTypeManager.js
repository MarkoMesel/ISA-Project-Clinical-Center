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

$("#editCt").click(function() {
	var parsedData = getParsedDataFromTable("#ctTable");
	var ctId = parsedData.id;
	
	$.ajax({
		type : 'GET',
		url : "/isCheckupTypeReserved",
		dataType : "text",
		headers:{
			'token':localStorage.getItem('token'),
			'ctId': ctId
		},
		success : function(successData) {
			//$("#roomTable").tabulator("setData", successData);
			var isInUse = successData;
			if(isInUse == 'true') {
				alert("A checkup of this type is reserved and therefore this type cannot be edited.");	
			} else if(isInUse == 'false')  {
				window.location.href = "../editCheckupType/"+ctId;	
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
		}
	});
	
});

$("#findCtBtn").click(function() {
	var name = $("#ctName").val();
	var price = $("#ctPrice").val();
	
	$.ajax({
		type : 'GET',
		url : "/findCtNamePrice",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'name': name,
			'price': price
		},
		success : function(successData) {
			var foundId = successData.id;
			$("#ctTable").tabulator("selectRow", foundId);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
			//$("#foundDoctorTable").tabulator("clearData");
		}
	});
});

$("#deleteCt").click(function() {
	var parsedData = getParsedDataFromTable("#ctTable");
	var ctId = parsedData.id;
	var name = parsedData.name;

	$.ajax({
		type : 'GET',
		url : "/isCheckupTypeInUse",
		dataType : "text",
		headers:{
			'token':localStorage.getItem('token'),
			'ctId': ctId
		},
		success : function(successData) {
			//$("#roomTable").tabulator("setData", successData);
			var isInUse = successData;
			if(isInUse == 'true') {
				alert("A checkup of this type is reserved and therefore this type cannot be deleted.");	
			} else if(isInUse == 'false')  {
				if(window.confirm("Are you sure you want to remove checkup type " + name + "?")) {
					$.ajax({
						type : 'PUT',
						url : "/logicalDeleteCt",
						contentType : 'application/json',	
						headers:{
							'token':localStorage.getItem('token'),
							'ctId': ctId
						},
						success : function(successData) {
							window.location.href = "../checkupTypeManager";	
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							if(textStatus=="401"){			
								window.location.href = "../checkupTypeManager";
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

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}

