if (localStorage.getItem('token') == null) {
	window.location.href = "../login";
}

var dateFormatter = function(cell, formatterParams) {
	var value = cell.getValue();

	if (value) {
		value = moment(value).format("YYYY:MM:DD ddd HH:mm");
	}

	return value;
}

$("#clinic_table").tabulator({
	height:"300px",
	layout: 'fitColumns',
	movableCols:true,
	selectable: 1,
	columns:[
		{title: "Date", field:"date", sortable:true, formatter:dateFormatter, width:250, headerFilter:"input"},
		{title: "Doctor", field:"doctor", sortable:true, width:200, headerFilter:"input"},
		{title: "Clinic", field:"clinic", sortable:true, width:200, headerFilter:"input"},
		{title: "Price", field:"price", sortable:true, width:100, formatter:"money", sorter:"number", headerFilter:"input"},
		{title: "Discount", field:"discount", sortable:false, width:140, headerFilter:"input"},
		{title: "Id", field:"id", visible:false},
		],
	});

$("#clinic_table").tabulator("setData", "../unreservedPredefinedAppointments/"+window.location.pathname.split("/").pop()); // OVDE IDE LINK PODATAKA O KLINICI
$(window).resize(function(){
	  $("#sample_table").tabulator("redraw");
	});


function podaci(){
	var sd = $("#clinic_table").tabulator("getSelectedData");
	console.log(sd);
	
	$.ajax({
		type : 'PUT',
		url : "/reservePredefinedAppointment/"+sd[0].id,
		headers : {
			'token' : localStorage.getItem('token')
		},
		success : function(successData) {
				$("#clinic_table").tabulator("setData", "../unreservedPredefinedAppointments/"+window.location.pathname.split("/").pop());
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if (textStatus == "401") {
				window.location.href = "../login";
			}
		}
	});
}