jQuery(document).ready(function($) {
    $('.results').toggle();
	$('.submit').click(function() {
        $( ".results" ).css("visibility","hidden");
        $( ".showdata" ).css("visibility","hidden");
        $( ".loading" ).html('<img src="/images/loading.gif">');
        $( ".result" ).html('');
    });

    $('.showdata').click(function() {
         $( ".results" ).css("visibility","visible");
        if ($(this).val() == "show data")
            $( ".showdata" ).val("hide data");
        else
            $( ".showdata" ).val("show data");

        $('.results').toggle();
    });


});
