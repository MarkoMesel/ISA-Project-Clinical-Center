var clinicLocation = '';
$.ajax({
	type : 'GET',
	url : "/getClinicInfo",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		clinicLocation = successData.address;
		$("#address").html('' + clinicLocation);
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){	
			window.location.href = "../whatAreYou";
		}
	}
});

$("#confirmLocation").click(function() {
	let data = {"name": null,
			"address": document.getElementById('address').innerHTML};
	
	$.ajax({
		type : 'PUT',
		url : "/editClinicLocation",
		contentType : 'application/json',
		data : JSON.stringify(data),	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			window.location.href = "../clinicManager";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../clinicLocation";
			}
			else{
				window.location.href = "../clinicLocation";				
			}
		}
	});	
});