/**
 * groupdocs.signature Plugin
 * Copyright (c) 2018 Aspose Pty Ltd
 * Licensed under MIT.
 * @author Aspose Pty Ltd
 * @version 1.0.0
 */

(function( $ ) {

	/**
	* Create private variables.
	**/	
	var xCoord = 125;
	var yCoord = 125;	
	var paramValues = {	
		text : '#gd-qr-text',		
		borderColor : '#gd-qr-border-color',
		borderStyle : '#gd-qr-border-style',
		borderWidth : '#gd-qr-border-width'		
	}

	$.fn.qrCodeGenerator = function () {
		$(this).append($.fn.qrCodeGenerator.baseHtml());
		var propertiesContainer = $.fn.qrCodeGenerator.propertiesHtml();
		$(propertiesContainer).insertBefore("#gd-qr-add");
	}

	$.extend(true, $.fn.qrCodeGenerator, {

        addQr : function(elem){
            elem.parent().parent().append($.fn.qrCodeGenerator.propertiesHtml());
            $(paramValues.strokeColor + canvasCount).bcPicker();
        },

		setProperties : function(){
				var text = $(this).find(paramValues.text).val();
				var borderColor = $(paramValues.borderColor).children().css('background-color');
            	var borderStyle = $(this).find(paramValues.borderStyle).val();
				var borderWidth = $(this).find(paramValues.borderWidth).val();
		},

		baseHtml : function(){
			var html = '<div id="gd-qr-container">' +
				'<div id="gd-qr-params-container">' +
					'<div id="gd-qr-params-header">' +
						'<button id="gd-qr-add">Preview</button>' +
					'</div>' +
				'</div>' +
				'<div id="gd-qr-preview-container"></div>' +
			'</div>';
			return html;
		},

		propertiesHtml : function(){
			var html = '<div class="gd-qr-params" id="gd-qr-params">'+
				'<div>' +
					'<h3>Qr Code Properties</h3>' +
					'<table>' +
						'<thead>' +
							'<tr>' +
								'<td>text</td>' +
							'</tr>' +
						'</thead>' +
						'<tbody>' +
							'<tr>' +
								'<td><input type="text" id="gd-qr-text" value="this is my text "/></td>' +
							'</tr>' +
						'</tbody>' +
					'</table>' +
					'<table>' +
						'<thead>' +
							'<tr>' +
								'<td>Border color</td>' +
								'<td>Border style</td>' +
								'<td>Border Width</td>' +
							'</tr>' +
						'</thead>' +
						'<tbody>' +
							'<tr>' +
								'<td><div class="gd-qr-color-picker" id="gd-qr-fg-color"></div></td>' +
								'<td>' +
									'<div class="gd-pages-dropdown">'+
										'<button class="gd-drop-button">Select style</button>'+
										'<div class="gd-pages-dropdown-content">'+
                							'<a href="#" class="gd-page-number">Dot</a>'+
                							'<a href="#" class="gd-page-number">Solid</a>'+
                							'<a href="#" class="gd-page-number">Dash</a>'+
											'<a href="#" class="gd-page-number">DashDot</a>'+
											'<a href="#" class="gd-page-number">DashDotDot</a>'+
											'<a href="#" class="gd-page-number">DashLongDash</a>'+
											'<a href="#" class="gd-page-number">DashLongDashDot</a>'+
											'<a href="#" class="gd-page-number">RoundDot</a>'+
											'<a href="#" class="gd-page-number">SquareDot</a>'+
										'</div>'+
									'</div>'
								'</td>' +
								'<td><input type="number" id="gd-qr-border-width" value="1"/></td>' +
							'</tr>' +
						'</tbody>' +
					'</table>' +
				'</div>' +
			'</div>';
			return html;
		}
	});

})(jQuery);