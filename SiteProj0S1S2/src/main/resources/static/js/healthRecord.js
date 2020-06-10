if (localStorage.getItem('token') == null) {
	window.location.href = "../login";
}

var setdata1 = [ {
	"id" : 1,
	"name": "Ime",
	"surname": "Prezime",
	"blood": "A+",
	"age": 26,
	"height": 180,
	"weight": 80
} ];

$("#healthRecord").tabulator({
	height : "300px",
	layout : 'fitColumns',
	movableCols : false,
	selectable: true,
	sortable: false,
	columns : [ {
		title : "ID",
		field : "id",
		visible : false
	}, {
		title : "Name",
		field : "name",
		width : 250,
	}, {
		title : "Surname",
		field : "surname",
		width : 250,
	}, {
		title : "Blood Type",
		field : "blood",
		width : 250,
	},{
		title : "Age",
		field : "age",
		width : 250,
	},{
		title : "Height",
		field : "height",
		width : 250,
	},{
		title : "Weight",
		field : "weight",
		width : 250,
	},
	
	],
});

$("#healthRecord").tabulator("setData", setdata1); // OVDE IDE LINK PODATAKA O KLINICI



$(window).resize(function() {
	$("#healthRecord").tabulator("redraw");
});



