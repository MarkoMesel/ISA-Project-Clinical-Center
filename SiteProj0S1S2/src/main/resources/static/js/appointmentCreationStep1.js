if(localStorage.getItem('token')==null){
	window.location.href = "../login";
}

$("#logout").click(function() {
	localStorage.removeItem('token');
	window.location.href = "../login";
});



$("#appointment_clinic_list1").tabulator({
	height : "300px",
	layout : 'fitColumns',
	movableCols : true,
	selectable: 1,
	columns : [ {
		title : "Id",
		field : "id",
		visible : false
	}, {
		title : "Name",
		field : "name",
		sortable : true,
		width : 250,
		headerFilter : "input"
	}, {
		title : "Rating",
		field : "rating",
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
	}, ],
});

$("#appointment_clinic_list1").tabulator("setData", '../clinicsAtDate/'+window.location.pathname.split("/").pop()); // OVDE IDE LINK
																// PODATAKA O
																// KLINICI


$(window).resize(function() {
	$("#appointment_clinic_list1").tabulator("redraw");
});
//$("#appointment_clinic_list1").tabulator("selectRow",1);

//var sd = $("#appointment_clinic_list1").tabulator("getSelectedData");
//console.log(sd);

function podaci(){
	var sd = $("#appointment_clinic_list1").tabulator("getSelectedData");
	if( sd==null || sd[0]==null){
		return;
	}
	//logika slanja
	window.location.href = "../apc2/"+window.location.pathname.split("/").pop()+"/"+sd[0].id;
}

