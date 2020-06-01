if(localStorage.getItem('token')==null){
	window.location.href = "../login";
}

$("#logout").click(function() {
	localStorage.removeItem('token');
	window.location.href = "../login";
});

var dateFormatter = function(cell, formatterParams) {
	var value = cell.getValue();

	if (value) {
		value = moment(value).format("HH:mm:ss");
	}

	return value;
}

$("#appointment_clinic_list2").tabulator({
	height : "300px",
	layout : 'fitColumns',
	movableCols : true,
	selectable : 1,
	columns : [ {
		title : "scheduleId",
		field : "scheduleId",
		visible : false
	}, {
		title : "Doctor Name",
		field : "doctorName",
		sortable : true,
		width : 250,
		headerFilter : "input"
	}, {
		title : "Clinic Name",
		field : "clinic",
		sortable : true,
		width : 200,
		headerFilter : "input",
		sorter : "number"
	}, {
		title : "Price",
		field : "price",
		sortable : true,
		width : 100,
		formatter : "money",
		sorter : "number",
		headerFilter : "input"
	}, {
        title : "Date",
        field : "date",
        formatter:dateFormatter,
        sortable : true,
        width : 300,
        sortable : true,
        headerFilter : "input"
    },],
});

var locationObject = window.location.pathname.split("/");
var clinicId = locationObject.pop();
var date = locationObject.pop();

$("#appointment_clinic_list2").tabulator("setData",
		'/availableAppointmentsByDateAndClinic/' + date + '/' + clinicId);

$(window).resize(function() {
	$("#appointment_clinic_list2").tabulator("redraw");
});
// $("#appointment_clinic_list1").tabulator("selectRow",1);

// var sd = $("#appointment_clinic_list1").tabulator("getSelectedData");
// console.log(sd);

function podaci() {
	var sd = $("#appointment_clinic_list2").tabulator("getSelectedData");

	const data = formToJSON(sd[0].scheduleId);
	$
			.ajax({
				type : 'POST',
				url : "/requestAppointment",
				contentType : 'application/json',
				data : data,
				headers : {
					'token' : localStorage.getItem('token')
				},
				success : function(successData) {
					window.location.replace("../" + date + "/" + clinicId
							+ "?success");

					$("#appointment_clinic_list2").tabulator(
							"setData",
							'/availableAppointmentsByDateAndClinic/' + date
									+ '/' + clinicId);
					$("#appointment_clinic_list2").tabulator("redraw");
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					if (textStatus == "401") {
						window.location.href = "../login";
					} else {
						window.location.replace("../" + date + "/" + clinicId
								+ "?error")
					}
				}
			});
}

function formToJSON(scheduleId) {
	return JSON.stringify({
		"date" : date,
		"scheduleId" : scheduleId
	});
}
