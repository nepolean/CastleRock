jQuery(function($) {'use strict',
	$("#sidebar-menu-toggle").click(function(e) {
		e.preventDefault();
		$("#dashboard-container").toggleClass("sidebar-active");
	});
});