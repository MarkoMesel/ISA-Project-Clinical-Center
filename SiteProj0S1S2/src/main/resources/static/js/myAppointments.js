if (localStorage.getItem('token') == null) {
	window.location.href = "../login";
}

var dateFormatter = function(cell, formatterParams) {
	var value = cell.getValue();

	if (value) {
		value = moment(value).format("HH:mm:ss");
	}

	return value;
}

$("#appointment_table").tabulator({
	height:"300px",
	layout: 'fitColumns',
	movableCols:true,
	selectable: 1,
	columns:[
		{title: "Date", field:"date", formatter:dateFormatter, sortable:true, width:250, headerFilter:"input"},
		{title: "Doctor", field:"doctor", sortable:true, width:200, headerFilter:"input"},
		{title: "Clinic", field:"clinic", sortable:true, width:200, headerFilter:"input"},
		{title: "Price", field:"price", sortable:true, width:100, formatter:"money", sorter:"number", headerFilter:"input"},
		{title: "Discount", field:"discount", sortable:false, width:140, headerFilter:"input"},
		{title: "Id", field:"id", visible:false},
		],
	});

$.ajax({
	type : 'GET',
	url : "/getMyAppointments",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		$("#appointment_table").tabulator("setData", successData);
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../login";
		}
	}
});	

$(window).resize(function(){
	  $("appointment_table").tabulator("redraw");
	});

function podaci() {
	var sd = $("#appointment_table").tabulator("getSelectedData");
	$
			.ajax({
				type : 'DELETE',
				url : "/cancel/"+sd[0].id,
				headers : {
					'token' : localStorage.getItem('token')
				},
				success : function(successData) {
					//GET APPOINTMENTS AGAIN
					$.ajax({
						type : 'GET',
						url : "/getMyAppointments",
						dataType : "json",
						headers:{
							'token':localStorage.getItem('token')
						},
						success : function(successData) {
							$("#appointment_table").tabulator("setData", successData);
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							if(textStatus=="401"){			
								window.location.href = "../login";
							}
						}
					});
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					if (textStatus == "401") {
						window.location.href = "../login";
					}
				}
			});
}