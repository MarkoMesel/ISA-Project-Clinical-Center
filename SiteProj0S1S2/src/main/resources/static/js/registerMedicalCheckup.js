$(window).resize(function() {
	$("#ctTable").tabulator("redraw");
	$("#roomTable").tabulator("redraw");
	$("#doctorTable").tabulator("redraw");
});

$("#ctTable").tabulator({
	height : false,
	layout:"fitColumns",
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
	},{
		title : "Price",
		field : "price",
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],
	
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

$("#roomTable").tabulator({
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
		title : "Name",
		field : "name",
	},{
		title : "Number",
		field : "number",
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],
	
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

$("#doctorTable").tabulator({
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
		title : "First Name",
		field : "firstName",
	},{
		title : "Last Name",
		field : "lastName",
	},{
		title : "JMBG",
		field : "jmbg",
		visible : false,
	},{
		title : "Country",
		field : "country",
		visible : false,
	},{
		title : "City",
		field : "city",
		visible : false,
	},{
		title : "Street",
		field : "street",
		visible : false,
	},{
		title : "Email",
		field : "email",
		visible : false,
	},{
		title : "Phone",
		field : "phone",
		visible : false,
	},{
		title : "Rating",
		field : "rating",
	},{
		title : "Shift Start Time",
		field : "shiftStart",
	},{
		title : "Shift End Time",
		field : "shiftEnd",
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],
	

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

$(document).on('submit', '*[name="registerMedicalCheckupForm"]',function(e){
	e.preventDefault();
	const form = document.forms['registerMedicalCheckupForm'];
	const data = formToJSON(form.elements);
	
	$.ajax({
		type : 'POST',
		url : "/registerMedicalCheckup",
		contentType : 'application/json',
		data : data,	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			window.location.href = "../clinicManager";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../clinicBasicInfo";
			}
			else{
				window.location.href = "../registerMedicalCheckup";				
			}
		}
	});	
});

$("#calculatePrice").click(function(e) {
    e.preventDefault();
	var ctParsedData = getParsedDataFromTable("#ctTable");		
	var ctPrice = ctParsedData.price;
	var doctorParsedData = getParsedDataFromTable("#doctorTable");	
	var drPrice = (doctorParsedData.rating)/4;
	var totalPrice = ctPrice*drPrice;
	$("#price").val(totalPrice);
});

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}

function getSelectedIdFromtable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData.id;
}

function formToJSON(form) {
	return JSON.stringify({
		'id' : 21,
		'date': form['date'].value,
		'time': form['time'].value,
		'duration' : form['duration'].value,
		'price' : form['price'].value,
		'ctId' : getSelectedIdFromtable("#ctTable"),
		'clinicId' : form['clinicId'].value,
		'roomId' : getSelectedIdFromtable("#roomTable"),
		'doctorId' : getSelectedIdFromtable("#doctorTable"),
	});
}