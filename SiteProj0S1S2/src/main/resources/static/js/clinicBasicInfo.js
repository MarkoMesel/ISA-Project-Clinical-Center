var clinicName = '';
var clinicDescription = '';
var clinicLocation = '';
$.ajax({
	type : 'GET',
	url : "/getClinicInfo",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		clinicName = successData.name;
		clinicDescription = successData.description;
		clinicLocation = successData.address;
		$("#name").val(clinicName);
		$("#description").val(clinicDescription);
		$("#address").val(clinicLocation);
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){	
			window.location.href = "../whatAreYou";
		}
	}
});

$(document).on('submit', '*[name="editClinicForm"]',function(e){
	e.preventDefault();
	const form = document.forms['editClinicForm'];
	const data = formToJSON(form.elements);
	
	$.ajax({
		type : 'PUT',
		url : "/editClinicInfo",
		contentType : 'application/json',
		data : data,	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			window.location.href = "../clinicManager";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../clinicBasicInfo";
			}
			else{
				window.location.href = "../clinicBasicInfo";				
			}
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({				
		'name': form['name'].value,
		'description': form['description'].value,
		'address': form['address'].value,
		
	});
}