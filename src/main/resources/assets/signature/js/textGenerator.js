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
        backgroundColor : 'gd-text-background',
		fontColor : 'gd-text-font-color',
		bold : 'gd-text-bold',
		italic : 'gd-text-italic',
        underline : 'gd-text-underline',
		font : 'gd-text-font',
		fontSize : 'gd-text-font-size',
        width : 200,
        height : 50
	}

	$.fn.textGenerator = function(documentType) {
        if ($("#gd-text-container").length == 0) {
            $(this).append($.fn.textGenerator.baseHtml());
            var propertiesContainer = $.fn.textGenerator.propertiesHtml(documentType);
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
				var borderWidth = parseInt($(this).find('#' + paramValues.borderWidth).val());
				var backgroundColor = $('#' + paramValues.backgroundColor).children().css('background-color');
				var fontColor = $('#' + paramValues.fontColor).children().css('background-color');
            	var bold = $(this).find('#' + paramValues.bold).is(':checked') ? true : false;
            	var italic = $(this).find('#' + paramValues.italic).is(':checked') ? true : false;
            	var underline = $(this).find('#' + paramValues.underline).is(':checked') ? true : false;
            	var font = $(this).find('#' + paramValues.font).val();
           		var fontSize = parseInt($(this).find('#' + paramValues.fontSize).val());
				var properties = {
					text: text,
					borderColor: borderColor,
					borderStyle: borderStyle,
					borderWidth: borderWidth,
                    backgroundColor: backgroundColor,
                    fontColor: fontColor,
					bold: bold,
					italic: italic,
					underline: underline,
					font: font,
					fontSize: fontSize,
                    width: paramValues.width,
                    height: paramValues.height
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
										'<td>'+
                                            '<label>Text</label>'+
                                            '<input type="text" id="' + paramValues.text + '" class="gd-text-property" value="this is my text "/>'+
                                        '</td>'+
									'</tr>'+
                                '</tbody>'+
                            '</table>'+
                            '<table id="gd-text-font-properties-table">'+
                                '<tbody>'+
									'<tr>'+
										'<td>'+
                                            '<label>Font color</label>' +
                                            '<div class="gd-text-color-picker gd-text-property" id="' + paramValues.fontColor + '"></div>'+
                                        '</td>'+
                                        '<td>' +
                                            '<label>Size</label>' +
                                            '<input type="number" class="gd-text-property" id="' + paramValues.fontSize + '" value="10"/>' +
                                        '</td>'+
									'</tr>'+
									'<tr>'+
                                        '<td>'+
                                            '<label>Bold</label>'+
                                            '<input type="checkbox" id="' + paramValues.bold + '" class="gd-text-property" value="1"/>'+
                                            '<label>Italic</label>'+
                                            '<input type="checkbox" id="' + paramValues.italic + '" class="gd-text-property" value="1"/>'+
                                            '<label>Underline</label>'+
                                            '<input type="checkbox" id="' + paramValues.underline + '" class="gd-text-property" value="1"/>' +
                                        '</td>'+
                						'<td>'+
                                            '<label>Font</label>'+
                                            '<input type="text" id="' + paramValues.font + '" class="gd-text-property" value="Arial"/>'+
                                        '</td>'+
									'</tr>'+
								'</tbody>'+
							'</table>'+
							'<table id="gd-text-border-table">'+
								'<tbody>'+
									'<tr>'+
										'<td>'+
                                            '<label>Border color</label>'+
                                            '<div class="gd-text-color-picker gd-text-property" id="' + paramValues.borderColor + '"></div>'+
                                        '</td>'+
                						'<td>'+
                                            '<label>Background color</label>'+
                                            '<div class="gd-text-color-picker gd-text-property" id="' + paramValues.backgroundColor + '"></div>'+
                                        '</td>'+
                                    '</tr>'+
									'<tr>'+
                						'<td>'+
                                            '<label id="gd-text-border-width-label">Border width</label>'+
                                            '<input type="number" class="gd-text-property" id="' + paramValues.borderWidth + '" value="0"/>'+
                                        '</td>'+
                                        '<td id="gd-text-border-style-line">'+
                                            '<label>Border style</label>'+
                                            '<div class="gd-border-style-wrapper">'+
                                                '<select class="gd-border-style-select gd-text-property" id="' + paramValues.borderStyle + '">'+
                                                    '<option value="0">Solid</option>'+
                                                    '<option value="1">ShortDash</option>'+
                                                    '<option value="2">ShortDot</option>'+
                                                    '<option value="3">ShortDashDot</option>'+
                                                    '<option value="4">ShortDashDotDot</option>'+
                                                    '<option value="5">Dot</option>'+
                                                    '<option value="7">SquareDot</option>'+
                                                    '<option value="6">Dash</option>'+
                                                    '<option value="7">LongDash</option>'+
                                                    '<option value="8">DashDot</option>'+
                                                    '<option value="9">LongDashDot</option>'+
                                                    '<option value="10">LongDashDotDot</option>'+
                                                '</select>'+
                                            '</div>'+
                                        '</td>'+
									'</tr>'+
								'</tbody>'+
							'</table>'+
						'</div>';
			return html;
		}
	});

})(jQuery);