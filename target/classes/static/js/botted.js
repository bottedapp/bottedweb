jQuery(document).ready(function($) {
    $('.results').toggle();
	$('.submit').click(function() {
        $( ".usage" ).css("visibility","hidden");
        $( ".results" ).css("visibility","hidden");
        $( ".isbot" ).css("visibility","hidden");
        $( ".data" ).css("visibility","hidden");
        $( ".loading" ).html('<br><img src="/images/loading.gif" width="80">');
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

   var hamburger = $("#hamburger_menu");
   var menu = $("#menubody");
   $(hamburger).click(function (e) {
     menu.toggleClass("open");
     hamburger.toggleClass("open");
   });

});
