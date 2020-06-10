var url = document.URL;
var roomId = url.substring(url.lastIndexOf('/') + 1);

var roomName = "";
var roomNumber = "";

$.ajax({
	type : 'GET',
	url : "/getRoomInfo/"+roomId,
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		roomName = successData.name;
		roomNumber = successData.number;
		$("#name").val(roomName);
		$("#number").val(roomNumber);
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){	
			window.location.href = "../whatAreYou";
		}
		window.location.href = "../roomManager";
	}
});

$(document).on('submit', '*[name="editRoomForm"]',function(e){
	e.preventDefault();
	const form = document.forms['editRoomForm'];
	const data = formToJSON(form.elements);
	
	$.ajax({
		type : 'PUT',
		url : "/editRoomInfo",
		contentType : 'application/json',
		data : data,	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			window.location.href = "../roomManager";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../editRoom/"+roomId;
			}
			else{
				window.location.href = "../editRoom/"+roomId;				
			}
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({
		'id' : roomId,
		'name': form['name'].value,
		'number': form['number'].value,		
	});
}


/*
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
		url : "/editClinicLocation",
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
				window.location.href = "../clinicLocation";
			}
			else{
				window.location.href = "../clinicLocation";				
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
*/