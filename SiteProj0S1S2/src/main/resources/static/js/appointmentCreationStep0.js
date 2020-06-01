if(localStorage.getItem('token')==null){
	window.location.href = "../login";
}

$("#logout").click(function() {
	localStorage.removeItem('token');
	window.location.href = "../login";
});

$("#appointment_clinic_list").tabulator({
	height:"300px",
	layout: 'fitColumns',
	movableCols:true,
	columns:[
		{title: "Id", field:"id", visible:false},
		{title: "Name", field:"name", sortable:true, width:250, headerFilter:"input"},
		{title: "Rating", field:"rating", sortable:true, width:200, headerFilter:"input", sorter:"number"},
		{title: "Price", field:"price", sortable:true, width:100, formatter:"money", sorter:"number", headerFilter:"input"},		
		],
	});

$("#appointment_clinic_list").tabulator("setData", "../getClinics");
$(window).resize(function(){
	  $("#appointment_clinic_list").tabulator("redraw");
	});

$(document).on('submit', '*[name="appointmentForm"]',function(e){
	e.preventDefault();
	const form = document.forms['appointmentForm'];
	var date = form.elements['datepicker'];
	window.location.href = "../apc1/"+date.value;
});
