var chosenDate = "";
var url = document.URL;
var mcId = url.substring(url.lastIndexOf('/') + 1);

$("#doctorTable").tabulator({
	height : "150px",
	layout : 'fitColumns',
	movableCols : false,
	selectable:1,
	sortable: false,
	columns : [ {
		title : "ID",
		field : "id",
		visible : false
	},{
		title : "First Name",
		field : "firstName",
		width : 250,
	},{
		title : "Last Name",
		field : "lastName",
		width : 250,
	},{
		title : "JMBG",
		field : "jmbg",
		width : 250,
	},{
		title : "Country",
		field : "country",
		width : 250,
	},{
		title : "City",
		field : "city",
		width : 250,
	},{
		title : "Street",
		field : "street",
		width : 250,
	},{
		title : "Email",
		field : "email",
		width : 250,
	},{
		title : "Phone",
		field : "phone",
		width : 250,
	},{
		title : "Rating",
		field : "rating",
		width : 250,
	},{
		title : "Shift Start Time",
		field : "shiftStart",
		width : 250,
	},{
		title : "Shift End Time",
		field : "shiftEnd",
		width : 250,
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],

});

$("#roomTable").tabulator({
	height: 200,
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
		field : "name"
	},{
		title : "Number",
		field : "number"
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	], rowSelected:function(e, row){
        //row.toggleSelect();
        
    	var parsedData = getParsedDataFromTable("#roomTable");
    	var pId = parsedData.id;
    	
    	$.ajax({
    		type : 'GET',
    		url : "/getfirstFreeDateForRoom/"+pId,
    		dataType : "json",
    		headers:{
    			'token':localStorage.getItem('token')
    		},
    		success : function(successData) {
    			var fDate = successData.date;
    			$("#firstFreeDate").html(fDate);
    			
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    			if(textStatus=="401"){			
    				window.location.href = "../whatAreYou";
    			}
    		}
    	});
    }
	
});

$("#foundRoomTable").tabulator({
	height: 150,
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
	height: 150,
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
	$("#doctorTable").tabulator("redraw");
	$("#roomTable").tabulator("redraw");
	$("#foundRoomTable").tabulator("redraw");
	$("#busyDatesTable").tabulator("redraw");
});

$.ajax({
	type : 'GET',
	url : "/getDoctorsFromClinicAdmin",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#doctorTable").tabulator("setData", successData);
		
		
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
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

$("#pickThisDateBtn").click(function() {
	var doctorData = $("#doctorTable").tabulator("getSelectedData");
	mcId = mcId.replace('#','');
	var parsedData = getParsedDataFromTable("#roomTable");
	var roomId = parsedData.id;
	var date = document.getElementById("firstFreeDate").innerText;
	if(doctorData.length == 0 && date != '... click on room ...') {	
		$.ajax({
			type : 'PUT',
			url : "/saveRoomAndDate",
			contentType : 'application/json',	
			headers:{
				'token':localStorage.getItem('token'),
				'mcId':mcId,
				'roomId':roomId,
				'chosenDate':date
			},
			success : function(successData) {
				window.location.href = "../clinicAdminHome";
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//location.reload();
			}
		});	
	} else {
		var parsedDoctorData = getParsedDataFromTable("#doctorTable");
		var doctorId = parsedData.id;
		$.ajax({
			type : 'PUT',
			url : "/saveRoomAndDateAndDoctor",
			contentType : 'application/json',	
			headers:{
				'token':localStorage.getItem('token'),
				'mcId':mcId,
				'roomId':roomId,
				'chosenDate':date,
				'doctorId':doctorId
			},
			success : function(successData) {
				window.location.href = "../clinicAdminHome";
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//location.reload();
			}
		});	
	}
});

$("#chooseRoomDateBtn").click(function() {
	var doctorData = $("#doctorTable").tabulator("getSelectedData");
	mcId = mcId.replace('#','');
	var parsedData = getParsedDataFromTable("#foundRoomTable");
	var roomId = parsedData.id;
	var date = document.getElementById("rDate").value;
	if(doctorData.length == 0) {
		$.ajax({
			type : 'PUT',
			url : "/saveRoomAndDate",
			contentType : 'application/json',	
			headers:{
				'token':localStorage.getItem('token'),
				'mcId':mcId,
				'roomId':roomId,
				'chosenDate':date
			},
			success : function(successData) {
				window.location.href = "../clinicAdminHome";
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//location.reload();
			}
		});
	} else {
		var parsedDoctorData = getParsedDataFromTable("#doctorTable");
		var doctorId = parsedData.id;
		$.ajax({
			type : 'PUT',
			url : "/saveRoomAndDateAndDoctor",
			contentType : 'application/json',	
			headers:{
				'token':localStorage.getItem('token'),
				'mcId':mcId,
				'roomId':roomId,
				'chosenDate':date,
				'doctorId':doctorId
			},
			success : function(successData) {
				window.location.href = "../clinicAdminHome";
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//location.reload();
			}
		});	
	}
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

