$("#patientTable").tabulator({
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
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],

});

$("#searchResultTable").tabulator({
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
		headerFilter:true
	},{
		title : "Last Name",
		field : "lastName",
		headerFilter:true
	},{
		title : "JMBG",
		field : "jmbg",
		headerFilter:true
	},{
		title : "Country",
		field : "country",
		visible : false
	},{
		title : "City",
		field : "city",
		visible : false
	},{
		title : "Street",
		field : "street",
		visible : false
	},{
		title : "Email",
		field : "email",
		visible : false
	},{
		title : "Phone",
		field : "phone",
		visible : false
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	], rowSelected:function(e, row){
        //row.toggleSelect();
        
    	var parsedData = getParsedDataFromTable("#searchResultTable");
    	var pId = parsedData.id;
		window.location.href = "../userProfile/"+pId;
    }

});

//$("#doctorTable").tabulator("setData", setdata1); // OVDE IDE LINK PODATAKA O KLINICI
$(window).resize(function() {
	$("#patientTable").tabulator("redraw");
});

$.ajax({
	type : 'GET',
	url : "/getPatientsFromDoctor",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#patientTable").tabulator("setData", successData);
		
		
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

$("#findByFirstName").click(function() {
	var findByThis = $("#findByThis").val();
	
	$.ajax({
		type : 'GET',
		url : "/findPatientsByFirstName",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'findByThis': findByThis
		},
		success : function(successData) {
			$("#searchResultTable").tabulator("setData", successData);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
			$("#foundDoctorTable").tabulator("clearData");
		}
	});
});

$("#findByLastName").click(function() {
	var findByThis = $("#findByThis").val();
	
	$.ajax({
		type : 'GET',
		url : "/findPatientsByLastName",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'findByThis': findByThis
		},
		success : function(successData) {
			$("#searchResultTable").tabulator("setData", successData);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
			$("#foundDoctorTable").tabulator("clearData");
		}
	});
});

$("#findByJmbg").click(function() {
	var findByThis = $("#findByThis").val();
	
	$.ajax({
		type : 'GET',
		url : "/findPatientsByJmbg",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'findByThis': findByThis
		},
		success : function(successData) {
			$("#searchResultTable").tabulator("setData", successData);
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
	return parsedData;
}