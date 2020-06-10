$("#doctorTable").tabulator({
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

$("#foundDoctorTable").tabulator({
	height : "300px",
	layout : 'fitColumns',
	movableCols : false,
	selectable:false,
	sortable: false,
	columns : [ {
		title : "ID",
		field : "id",
		visible : false
	},{
		title : "First Name",
		field : "firstName",
		width : 140,
	},{
		title : "Last Name",
		field : "lastName",
		width : 140,
	},{
		title : "Working Hours",
		field : "workingHours",
		width : 170,
	},{
		title : "Rating",
		field : "rating",
		width : 90,
	}
	
	],

});

$("#findDoctorBtn").click(function() {
	var pFirstName = $("#pFirstName").val();
	var pLastName = $("#pLastName").val();
	var pRating = $("#pRating").val();
	
	$.ajax({
		type : 'GET',
		url : "/findDoctorFirstNameLastNameRating",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'firstName': pFirstName,
			'lastName': pLastName,
			'rating': pRating
		},
		success : function(successData) {
			var foundId = successData.id;
			var firstName = successData.firstName;
			var lastName = successData.lastName;
			var workingHours = "from " + successData.shiftStart + " to " + successData.shiftEnd;
			var rating = successData.rating;
			
			//alert(foundId + " " + firstName + " " + lastName + " " + workingHours + " " + rating);
			
			var doctorSearchResult = [ {
				"id" : foundId,
				"firstName": firstName,
				"lastName": lastName,
				"workingHours": workingHours,
				"rating": rating
			} ];
			$("#foundDoctorTable").tabulator("setData", doctorSearchResult);
			$("#doctorTable").tabulator("selectRow", foundId);
			//selectedRowIndex = foundId;
			//$("#doctorTable").selectRow(successData.id);		
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
			$("#foundDoctorTable").tabulator("clearData");
		}
	});
});

//$("#doctorTable").tabulator("setData", setdata1); // OVDE IDE LINK PODATAKA O KLINICI
$(window).resize(function() {
	$("#doctorTable").tabulator("redraw");
	$("#foundDoctorTable").tabulator("redraw");
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

$("#deleteDoctor").click(function() {
	var selectedData = $("#doctorTable").tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	var doctorId = parsedData.id;
	var doctorFirstName = parsedData.firstName;
	var doctorLastName = parsedData.lastName;

	if(window.confirm("Are you sure you want to remove doctor " + parsedData.firstName + " " + parsedData.lastName + "?")) {
		$.ajax({
			type : 'PUT',
			url : "/logicalDeleteDoctor",
			contentType : 'application/json',	
			headers:{
				'token':localStorage.getItem('token'),
				'doctorId': doctorId
			},
			success : function(successData) {
				window.location.href = "../doctorManager";	
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(textStatus=="401"){			
					window.location.href = "../doctorManager";
				}
			}
		});
	}
});
