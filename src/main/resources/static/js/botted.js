jQuery(document).ready(function($) {
	
	var $username;
	$('.submit').click(function() {
        $username = $( ".uname" ).val();
	$( ".result" ).html('<img src="/images/loading.gif">');
        query();
    	});

	function query()
    	{
		$.ajax({
				type: 'post',
				url: '/query.php',
				data: {'username': $username},
				cache:false,
				success: function(data)
				{
				    $( ".result" ).html('<h2 style="font-family:system-ui;color:#ffffff;">' + data + '</h2>');
				}
		});  
	}
});
