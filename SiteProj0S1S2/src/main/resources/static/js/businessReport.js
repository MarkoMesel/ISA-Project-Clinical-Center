var clinicRating = 0.0;
var chart = null;
var totalIncome = '';

window.addEventListener("load", loadDaily());

$("#doctorTable").tabulator({
	height : "150px",
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
		width : 197,
	},{
		title : "Last Name",
		field : "lastName",
		width : 197,
	},{
		title : "Rating",
		field : "rating",
		width : 197,
	},{
		title : "CLINIC ID",
		field : "clinicId",
		visible : false
	}
	
	],

});

$.ajax({
	type : 'GET',
	url : "/getClinicAverageRating",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		clinicRating = parseFloat(successData).toFixed(2);
		$("#clinicRating").html("Average Clinic rating: <b>"+ clinicRating + "</b>");
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});

$.ajax({
	type : 'GET',
	url : "/getDoctorAverageRatings",
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

function loadDaily() {
	document.getElementById('myChart').remove();
	let canvas = document.createElement('canvas');
	canvas.setAttribute('id','myChart');
	canvas.setAttribute('width','1050');
	canvas.setAttribute('height','460');
	document.querySelector('#chart-container').appendChild(canvas);
	$.ajax({
		type : 'GET',
		url : "/getMcDaily",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			$("#checkupGraphHeader").html("Daily medical checkup graph");
			
			var jsonfile = successData;
	
			var labels = successData.map(function(e) {
			   return e.date;
			});
			var data = successData.map(function(e) {
			   return e.count;
			});;
			
			var ctx = document.getElementById('myChart').getContext('2d');
			var config = {
			   type: 'bar',
			   data: {
			      labels: labels,
			      datasets: [{
			         label: 'daily checkups',
			         data: data,
			         backgroundColor: '#C0C0C0'
			      }]
			   },
			options: {
				responsive: false,
				legend: {
					labels: {
						fontColor: '#F5F5DC'
					}
				},
		        scales: {
		        	xAxes: [{
		        		barPercentage: 1.0,
		        		ticks: {
		        			fontColor: '#F5F5DC',
			        		autoSkip: false,
		        		}
		        	}],
		            yAxes: [{
		                ticks: {
		                	fontColor: '#F5F5DC',
		                    beginAtZero: true,
		                    stepSize: 1
		                }
		            }]
		        }
		    }
			};
			
			chart = new Chart(ctx, config);
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
		}
	});
}

function loadWeekly() {
	document.getElementById('myChart').remove();
	let canvas = document.createElement('canvas');
	canvas.setAttribute('id','myChart');
	canvas.setAttribute('width','1050');
	canvas.setAttribute('height','460');
	document.querySelector('#chart-container').appendChild(canvas);
	
	$.ajax({
		type : 'GET',
		url : "/getMcWeekly",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			$("#checkupGraphHeader").html("Weekly medical checkup graph");
			
			var jsonfile = successData;
	
			var labels = successData.map(function(e) {
			   return e.date;
			});
			var data = successData.map(function(e) {
			   return e.count;
			});;
			
			var ctx = document.getElementById('myChart').getContext('2d');
			var config = {
			   type: 'bar',
			   data: {
			      labels: labels,
			      datasets: [{
			         label: 'weekly checkups',
			         data: data,
			         backgroundColor: '#C0C0C0'
			      }]
			   },
			options: {
				responsive: false,
				legend: {
					labels: {
						fontColor: '#F5F5DC'
					}
				},
		        scales: {
		        	xAxes: [{
		        		barPercentage: 1.0,
		        		ticks: {
		        			fontColor: '#F5F5DC',
			        		autoSkip: false,
		        		}
		        	}],
		            yAxes: [{
		                ticks: {
		                	fontColor: '#F5F5DC',
		                    beginAtZero: true,
		                    stepSize: 1
		                }
		            }]
		        }
		    }
			};
			
			chart = new Chart(ctx, config);
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
		}
	});
}

function loadMonthly() {
	document.getElementById('myChart').remove();
	let canvas = document.createElement('canvas');
	canvas.setAttribute('id','myChart');
	canvas.setAttribute('width','1050');
	canvas.setAttribute('height','460');
	document.querySelector('#chart-container').appendChild(canvas);
	
	$.ajax({
		type : 'GET',
		url : "/getMcMonthly",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			$("#checkupGraphHeader").html("Monthly medical checkup graph");
			
			var jsonfile = successData;
	
			var labels = successData.map(function(e) {
			   return e.date;
			});
			var data = successData.map(function(e) {
			   return e.count;
			});;
			
			var ctx = document.getElementById('myChart').getContext('2d');
			var config = {
			   type: 'bar',
			   data: {
			      labels: labels,
			      datasets: [{
			         label: 'monthly checkups',
			         data: data,
			         backgroundColor: '#C0C0C0'
			      }]
			   },
			options: {
				responsive: false,
				legend: {
					labels: {
						fontColor: '#F5F5DC'
					}
				},
		        scales: {
		        	xAxes: [{
		        		barPercentage: 1.0,
		        		ticks: {
		        			fontColor: '#F5F5DC',
			        		autoSkip: false,
		        		}
		        	}],
		            yAxes: [{
		                ticks: {
		                	fontColor: '#F5F5DC',
		                    beginAtZero: true,
		                    stepSize: 1
		                }
		            }]
		        }
		    }
			};
			
			chart = new Chart(ctx, config);
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			}
		}
	});
}

$("#calculateIncome").click(function(e) {
    e.preventDefault();
    var dateFrom = document.getElementById('dateFrom').value;
    var dateTo = document.getElementById('dateTo').value;
    $.ajax({
		type : 'GET',
		url : "/calculateClinicIncome",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token'),
			'dateFrom':dateFrom,
			'dateTo':dateTo
		},
		success : function(successData) {
			totalIncome = successData;
			$("#incomeResult").html("Profit is "+ totalIncome);
		}
	});
});