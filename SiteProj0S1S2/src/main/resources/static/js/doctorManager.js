$("#doctorTable").tabulator({
	height : "300px",
	layout : 'fitColumns',
	movableCols : false,
	selectable: false,
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
	},
	
	],
});
//$("#doctorTable").tabulator("setData", setdata1); // OVDE IDE LINK PODATAKA O KLINICI
$(window).resize(function() {
	$("#doctorTable").tabulator("redraw");
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