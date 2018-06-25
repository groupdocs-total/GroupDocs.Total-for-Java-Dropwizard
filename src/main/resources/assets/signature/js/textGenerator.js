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
	var paramValues = {	
		text : 'gd-text-text',
		borderColor : 'gd-text-border-color',
		borderStyle : 'gd-text-border-style',
		borderWidth : 'gd-text-border-width',
		borderEffect : 'gd-text-border-effect',
        borderEffectIntensity : 'gd-text-border-effect-intensity',
        cornerRadius : 'gd-text-corner-radius',
        contentsDescription : 'gd-text-content-description',
        subject : 'gd-text-subject',
		title : 'gd-text-title',
        backgroundColor : 'gd-text-background',
		fontColor : 'gd-text-font-color',
		bold : 'gd-text-bold',
		italic : 'gd-text-italic',
        underline : 'gd-text-underline',
		font : 'gd-text-font',
		fontSize : 'gd-text-font-size',

	}

	$.fn.textGenerator = function() {
        if ($("#gd-text-container").length == 0) {
            $(this).append($.fn.textGenerator.baseHtml());
            var propertiesContainer = $.fn.textGenerator.propertiesHtml();
            $("#gd-text-params-header").append(propertiesContainer);
            $('#' + paramValues.borderColor).bcPicker();
            $('#' + paramValues.backgroundColor).bcPicker();
            $('#' + paramValues.fontColor).bcPicker();
        }
	}

	$.extend(true, $.fn.textGenerator, {

        getProperties : function(){
				var text = $(this).find('#' + paramValues.text).val();
				var borderColor = $('#' + paramValues.borderColor).children().css('background-color');
            	var borderStyle = parseInt($(this).find('#' + paramValues.borderStyle).val());
				var borderWidth = $(this).find('#' + paramValues.borderWidth).val();
				var borderEffect = parseInt($(this).find('#' + paramValues.borderEffect).val());
				var borderEffectIntensity = parseInt($(this).find('#' + paramValues.borderEffectIntensity).val());
				var cornerRadius = $(this).find('#' + paramValues.cornerRadius).val();
				var contentsDescription = $(this).find('#' + paramValues.contentsDescription).val();
				var subject =  $(this).find('#' + paramValues.subject).val();
				var title = $(this).find('#' + paramValues.title).val();
				var backgroundColor = $('#' + paramValues.backgroundColor).children().css('background-color');
				var fontColor = $('#' + paramValues.fontColor).children().css('background-color');
            	var bold = parseInt($(this).find('#' + paramValues.bold).val());
            	var italic = parseInt($(this).find('#' + paramValues.italic).val());
            	var underline = parseInt($(this).find('#' + paramValues.underline).val());
            	var font = $(this).find('#' + paramValues.font).val();
           		var fontSize = $(this).find('#' + paramValues.fontSize).val();
				var properties = {
					text: text,
					borderColor: borderColor,
					borderStyle: borderStyle,
					borderWidth: borderWidth,
                    borderEffect: borderEffect,
                    borderEffectIntensity: borderEffectIntensity,
                    cornerRadius: cornerRadius,
                    contentsDescription: contentsDescription,
					subject: subject,
					title: title,
                    backgroundColor: backgroundColor,
                    fontColor: fontColor,
					bold: bold,
					italic: italic,
					underline: underline,
					font: font,
					fontSize: fontSize
				};
				return properties;
		},

		baseHtml : function(){
			var html = '<div id="gd-text-container">' +
				'<div id="gd-text-params-container">' +
					'<div id="gd-text-params-header">' +
						// QR-Code properties will be here
					'</div>' +
				'</div>' +
				'<div id="gd-text-preview-container"></div>' +
			'</div>';
			return html;
		},

		propertiesHtml : function(){
			var html = '<div class="gd-text-params" id="gd-text-params">'+
							'<h3>Signature Properties</h3>' +
							'<table id="gd-text-table">'+
								'<tbody>'+
									'<tr>'+
										'<td><i>Title</i><input type="text" id="' + paramValues.title + '" class="gd-text-property" value="this is my title "/></td>'+
									'</tr>'+
									'<tr>'+
										'<td><i>Subject</i><input type="text" id="' + paramValues.subject + '" class="gd-text-property" value="this is my subject "/></td>'+
									'</tr>'+
									'<tr>'+
										'<td><i>Text</i><input type="text" id="' + paramValues.text + '" class="gd-text-property" value="this is my text "/></td>'+
									'</tr>'+
									'<tr>'+
										'<td><i>Description</i><input type="text" id="' + paramValues.contentsDescription + '" class="gd-text-property" value="this is my description "/></td>'+
									'</tr>'+
									'<tr>'+
										'<td>'+
                                            '<i>Font color</i><div class="gd-text-color-picker gd-text-property" id="' + paramValues.fontColor + '"></div>'+
                                        '</td>'+
                                        '<td>'+
                                            '<i>Bold</i><input type="checkbox" id="' + paramValues.bold + '" class="gd-text-property" />'+
                                            '<i>Italic</i><input type="checkbox" id="' + paramValues.italic + '" class="gd-text-property" />'+
                                            '<i>Underline</i><input type="checkbox" id="' + paramValues.underline + '" class="gd-text-property" />' +
                                        '</td>'+
									'</tr>'+
									'<tr>'+
                						'<td><i>Font</i><input type="text" id="' + paramValues.font + '" class="gd-text-property" value="Arial"/></td>'+
										'<td><i>Size</i><input type="number" class="gd-text-property" id="' + paramValues.fontSize + '" value="0"/></td>'+
									'</tr>'+
								'</tbody>'+
							'</table>'+
							'<table id="gd-text-border-table">'+
								'<tbody>'+
									'<tr>'+
										'<td><i>Border color</i><div class="gd-text-color-picker gd-text-property" id="' + paramValues.borderColor + '"></div></td>'+
                						'<td><i>Background color</i><div class="gd-text-color-picker gd-text-property" id="' + paramValues.backgroundColor + '"></div></td>'+
										'<td>'+
                							'<i>Border style</i>'+
											'<div class="gd-border-style-wrapper">'+
												'<select class="gd-border-style-select gd-text-property" id="' + paramValues.borderStyle + '">'+
													'<option value="0">Dash</option>'+
													'<option value="1">DashDot</option>'+
													'<option value="2">DashDotDot</option>'+
                									'<option value="3">DashLongDash</option>'+
													'<option value="4">DashLongDashDot</option>'+
													'<option value="5">RoundDot</option>'+
													'<option value="6">Solid</option>'+
													'<option value="7">SquareDot</option>'+
												'</select>'+
											'</div>'+
										'</td>'+
										'<td>'+
                							'<td><i>Border width</i><input type="checkbox" class="gd-text-property" id="' + paramValues.borderEffect + '" value="Cloudy"/></td>'+
										'</td>'+
									'</tr>'+
									'<tr>'+
                						'<td><i>Border width</i><input type="number" class="gd-text-property" id="' + paramValues.borderWidth + '" value="0"/></td>'+
                						'<td><i>Border effect intensity</i><input type="number" class="gd-text-property" id="' + paramValues.borderEffectIntensity + '" value="0"/></td>'+
                						'<td><i>Border corner radius</i><input type="number" class="gd-text-property" id="' + paramValues.cornerRadius + '" value="0"/></td>'+
									'</tr>'+
								'</tbody>'+
							'</table>'+
						'</div>';
			return html;
		}
	});

})(jQuery);