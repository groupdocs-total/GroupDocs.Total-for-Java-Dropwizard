/**
 * groupdocs.signature Plugin
 * Copyright (c) 2018 Aspose Pty Ltd
 * Licensed under MIT.
 * @author Aspose Pty Ltd
 * @version 1.0.0
 */

/*
******************************************************************
******************************************************************
GLOBAL VARIABLES
******************************************************************
******************************************************************
*/
var signatureImageIndex = 0;
var signaturesList = [];
var signature = {
    signaturePassword:  "",
    signatureComment: "",
    signatureType: "",
    left: 0,
    top: 0,
    imageWidth: 0,
    imageHeight: 0,
    signatureGuid: "",
    reason: "",
    contact: "",
    address: "",
    date: "",
    pageNumber: 0,
    angle: 0,
    documentType: ""
}

$(document).ready(function(){

    /*
    ******************************************************************
    NAV BAR CONTROLS
    ******************************************************************
    */

    //////////////////////////////////////////////////
    // Disable default download event
    //////////////////////////////////////////////////
    $('#gd-btn-download').off('click');

    //////////////////////////////////////////////////
    // Fix for tooltips of the dropdowns
    //////////////////////////////////////////////////
    $('#gd-download-val-container').on('click', function(e){
        if($(this).hasClass('open')){
            $('#gd-btn-download-value').parent().find('.gd-tooltip').css('display', 'none');
        }else{
            $('#gd-btn-download-value').parent().find('.gd-tooltip').css('display', 'initial');
        }
    });

    $('#gd-signature-val-container').on('click', function(e){
        if($(this).hasClass('open')){
            $('#gd-btn-signature-value').parent().find('.gd-tooltip').css('display', 'none');
        }else{
            $('#gd-btn-signature-value').parent().find('.gd-tooltip').css('display', 'initial');
        }
    });

    //////////////////////////////////////////////////
    // Digital sign event
    //////////////////////////////////////////////////
    $('#qd-digital-sign').on('click', function(e){
        if(typeof documentGuid == "undefined" || documentGuid == ""){
            printMessage("Please open document first");
        } else {
            signature.signatureType = "digital";
            toggleModalDialog(true, 'Digital Signature', getHtmlDigitalSign());
            loadSignaturesTree('', openSigningFirstStepModal);
        }
    });

    //////////////////////////////////////////////////
    // Image sign event
    //////////////////////////////////////////////////
    $('#qd-image-sign').on('click', function(e){
        if(typeof documentGuid == "undefined" || documentGuid == ""){
            printMessage("Please open document first");
        } else {
            signature.signatureType = "image";
            toggleModalDialog(true, 'Image Signature', getHtmlImageSign());
            loadSignaturesTree('', openSigningFirstStepModal);
        }
    });

    //////////////////////////////////////////////////
    // Stamp sign event
    //////////////////////////////////////////////////
    $('#gd-stamp-sign').on('click', function(e){
        // if(typeof documentGuid == "undefined" || documentGuid == ""){
        //     printMessage("Please open document first");
        // } else {
        signature.signatureType = "stamp";
        toggleModalDialog(true, 'Stamp Signature', getHtmlImageSign());
        loadSignaturesTree('', openSigningFirstStepModal);
        // }
    });

    //////////////////////////////////////////////////
    // Open signatures browse button (digital sign dialog) click
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '#gd-open-signature', function(e){
        $('#modalDialog .gd-modal-title').text("Signing Document");
        $( "#gd-signature-draw-step" ).remove();
        if( $("#gd-signature-select-step").length == 0){
            var browseStep = getHtmlSignaturesSelectModal();
            $(browseStep).insertBefore("#gd-signature-page-select-step");
            loadSignaturesTree('');
        }
        // open browse signatures step
        switchSlide(1, 0, "right");
        // set currently active step pagination
        if(!$($(".gd-pagination")[0]).hasClass("gd-pagination-active")){
            $($(".gd-pagination")[0]).addClass("gd-pagination-active")
        }
        $("#gd-signing-footer").show();
    });

    //////////////////////////////////////////////////
    // Open signatures draw button (digital sign dialog) click
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '#gd-draw-signature', function(e) {
        $('#modalDialog .gd-modal-title').text("Signing Document");
        $("#gd-signature-select-step").remove();
        if( $("#gd-signature-draw-step").length == 0){
            var drawStep = getHtmlDrawImageModal();
            $(drawStep).insertBefore("#gd-signature-page-select-step");
        }
        // open draw signatures step
        switchSlide(1, 0, "right");
        // set currently active step pagination
        if (!$($(".gd-pagination")[0]).hasClass("gd-pagination-active")) {
            $($(".gd-pagination")[0]).addClass("gd-pagination-active")
        }
        $("#gd-signing-footer").show();
        if ($("#bcPaint-container").length == 0) {
            $("#gd-draw-image").bcPaint();
        }
    });

    //////////////////////////////////////////////////
    // signature or directory click event from signature tree
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '.gd-signature-name', function(e){
        var isDir = $(this).parent().find('.fa-folder').hasClass('fa-folder');
        if(isDir){
            // if directory -> browse
            if(currentDirectory.length > 0) {
                currentDirectory = currentDirectory + '/' + $(this).text();
            } else {
                currentDirectory = $(this).text();
            }
            $('#gd-signatures').html(loadSignaturesTree(currentDirectory, ""));
        } else {
            $(".gd-signature-select").removeClass("gd-signing-disabled");
        }
    });

    //////////////////////////////////////////////////
    // Go to parent directory event from signature tree
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '.gd-go-up-signature', function(e){
        if(currentDirectory.length > 0 && currentDirectory.indexOf('/') == -1){
            currentDirectory = "";
        }else{
            currentDirectory = currentDirectory.replace(/\/[^\/]+\/?$/, "");
        }
        $('#gd-signatures').html(loadSignaturesTree(currentDirectory, ""));
    });

    //////////////////////////////////////////////////
    // Upload signature event
    //////////////////////////////////////////////////
    $(".gd-modal-body").on('change', '#gd-signature-upload-input', function(e) {
        var uploadFiles = $(this).get(0).files;
        // upload file one by one
        for (var i = 0; i < uploadFiles.length; i++) {
            // upload local file
            uploadSignature(uploadFiles[i], i, "");
        }
        $(".gd-browse-signatures").show();
        if (signature.signatureType == "image") {
            $(".gd-upload-signatures").css("left", "calc(100% - 87%)");
            $(".gd-browse-signatures").css("left", "calc(100% - 77%)");
        } else {
            $(".gd-upload-signatures").css("left", "calc(100% - 76%)");
        }
    });

    //////////////////////////////////////////////////
    // Signature select event
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '.gd-signature', function(e){
        // get selected signature guid
        signature.signatureGuid = $(this).find("label").data("guid");
        if(signature.signatureType == "digital") {
            // set styles for selected signature
            $.each($(".gd-signature"), function (index, elem) {
                $(elem).css("background-color", "#e5e5e5");
            });
            $(this).css("background-color", "#3e4e5a");
        } else {
            $(".gd-signature-select").removeClass("gd-signing-disabled");
        }
    });

    //////////////////////////////////////////////////
    // Back button click event (digital signing modal)
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '#gd-back', function(e){
        var currentSlide;
        $(".gd-slide").each(function(index, elem){
            if($(elem).css("display") == "block"){
                currentSlide = $(elem).data("index");
            }
        });
        if(currentSlide == 1){
            $("#gd-signing-footer").hide();
        }
        switchSlide(currentSlide - 1, currentSlide, "left");
        $("#gd-next").html("NEXT");
        $($(".gd-pagination")[currentSlide - 1]).toggleClass("gd-pagination-active");
        $($(".gd-pagination")[currentSlide - 2]).toggleClass("gd-pagination-active");
    });

    //////////////////////////////////////////////////
    // Next button click event (digital signing modal)
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '#gd-next', switchToNextSlide);

    //////////////////////////////////////////////////
    // Select page number event (image signing modal)
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '.gd-page-number', function(pageNumber){
        $(".gd-signature-information-review i").html($(pageNumber.target).html());
    });

    //////////////////////////////////////////////////
    // Select all pages event (image signing modal)
    //////////////////////////////////////////////////
    $('.gd-modal-body').on('click', '#gd-all-pages label', function(e){
        if( $(".gd-signature-information-review i").html() == "") {
            $(".gd-signature-information-review i").html("All");
            $(this).parent().css("background-color", "#3e4e5a");
        } else {
            $(".gd-signature-information-review i").html("");
            $(this).parent().css("background-color", "#e5e5e5");
        }
    });

    //////////////////////////////////////////////////
    // Apply image current state button click event
    //////////////////////////////////////////////////
    $("#gd-panzoom").on("click", "#gd-apply", function(){
        // lock feature for image signature modifications
        $(this).parent().draggable("disable").rotatable("disable").resizable("disable");
        // enable save button on the dashboard
        $("#gd-nav-save").toggleClass("gd-save-disabled");
        $("#gd-nav-save").on('click', function(){
            sign();
        });
    });

    //////////////////////////////////////////////////
    // Cancel image current state button click event
    //////////////////////////////////////////////////
    $("#gd-panzoom").on("click", "#gd-cancel", function(){
        // unlock feature for image signature modifications
        $(this).parent().draggable("enable").rotatable("enable").resizable("enable");
        // disable save button on the dashboard
        $("#gd-nav-save").toggleClass("gd-save-disabled");
        $("#gd-nav-save").off('click');
    });

    $('.gd-modal-body').on('click', '#bcPaint-export', function(){
        var drawnImage = $.fn.bcPaint.export();
        saveDrawnImage(drawnImage);
    });
});

/*
******************************************************************
FUNCTIONS
******************************************************************
*/

/**
 * Load file tree
 * @param {string} dir - files location directory
 * @param {object} callback - function that will be executed after ajax call
 */
function loadSignaturesTree(dir, callback) {
    var data = {path: dir, signatureType: signature.signatureType};
    currentDirectory = dir;
    // show loading spinner
    $('#gd-modal-spinner').show();
    // get data
    $.ajax({
        type: 'POST',
        url: getApplicationPath('loadFileTree'),
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(returnedData) {
            if(returnedData.message != undefined){
                // open error popup
                toggleModalDialog(false, "");
                printMessage(returnedData.message);
                return;
            }
            // hide loading spinner
            $('#gd-modal-spinner').hide();
            // append files to tree list
            var signatures = "";
            $.each(returnedData, function(index, elem){
                // document name
                var name = elem.name;
                // document guid
                var guid = elem.guid;
                // append signature
                var icon = elem.isDirectory? "fa-folder" : "fa-file-o";
                if(elem.isDirectory){
                    signatures = signatures + '<div class="gd-signature">'+
                        '<i class="fa ' + icon + '"></i>'+
                        '<div class="gd-signature-name" data-guid="' + guid + '">' + name + '</div>'+
                        '</div>';
                } else {
                    if(signature.signatureType == "image" || signature.signatureType == "stamp"){
                        signatures = signatures + '<div class="gd-signature gd-signature-thumbnail">' +
                            '<input id="gd-radio' + index + '" class="gd-signature-radio" name="gd-radio" type="radio">' +
                            '<label for="gd-radio' + index + '" class="gd-signature-name" data-guid="' + guid + '"></label>' +
                            '<image class="gd-signature-thumbnail-image" src="data:image/png;base64,' + elem.image + '" alt></image>' +
                            '</div>';
                    } else {
                        signatures = signatures + '<div class="gd-signature">' +
                            '<input id="gd-radio' + index + '" class="gd-signature-radio" name="gd-radio" type="radio">' +
                            '<label for="gd-radio' + index + '" class="gd-signature-name" data-guid="' + guid + '">' + name + '</label>' +
                            '</div>';
                    }
                }

            });
            // append go up element if current directory is not a root directory
            var goUpHtml = "";
            if(currentDirectory != ""){
                goUpHtml = '<div class="gd-signature gd-go-up-signature">'+
                    '<i class="fa fa-level-up"></i>'+
                    '<i>...</i>'+
                    '</div>';
            }
            $("#gd-signatures").html(goUpHtml + signatures);
        },
        error: function(xhr, status, error) {
            var err = eval("(" + xhr.responseText + ")");
            console.log(err.Message);
        }
    }).done(function(data){
        if(typeof callback == "function") {
            callback(data);
        }
    });
}

/**
 * Upload signature
 * @param {file} file - File for uploading
 * @param {int} index - Number of the file to upload
 * @param {string} url - URL of the file, set it if URL used instead of file
 */
function uploadSignature(file, index, url) {
    // prepare form data for uploading
    var formData = new FormData();
    // add local file for uploading
    formData.append("file", file);
    if (typeof url == "undefined") {
        url = "";
    }
    // add URL if set
    formData.append("url", url);
    formData.append("signatureType", signature.signatureType)
    formData.append("rewrite", rewrite);
    $.ajax({
        // callback function which updates upload progress bar
        xhr: function()
        {
            var xhr = new window.XMLHttpRequest();
            // upload progress
            xhr.upload.addEventListener("progress", function(event){
                if (event.lengthComputable) {
                    $(".gd-modal-close-action").off('click');
                    $("#gd-open-document").prop("disabled", true);
                    // increase progress
                    $("#gd-pregress-bar-" + index).addClass("p"+ Math.round(event.loaded / event.total * 100));
                    if(event.loaded == event.total){
                        $("#gd-pregress-bar-" + index).fadeOut();
                        $("#gd-upload-complete-" + index).fadeIn();
                        $('.gd-modal-close-action').on('click', closeModal);
                        $("#gd-open-document").prop("disabled", false);
                    }
                }
            }, false);
            return xhr;
        },
        type: 'POST',
        url: getApplicationPath('uploadDocument'),
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function(returnedData) {
            if(returnedData.message != undefined){
                toggleModalDialog(false, "");
                // open error popup
                printMessage(returnedData.message);
                return;
            }
            loadSignaturesTree('');
        },
        error: function(xhr, status, error) {
            var err = eval("(" + xhr.responseText + ")");
            console.log(err.Message);
        }
    });
}

/**
 * Sign current document
 */
function sign() {
    $('#gd-modal-spinner').show();
    var url = "";
    var signatureType = "";
    // check signature type: digital, image etc.
    if(signaturesList.length > 0){
        signatureType = signaturesList[0].signatureType;
    } else {
        signatureType = signature.signatureType;
        signature.documentType = getDocumentFormat(documentGuid).format;
    }
    // get signing action URL, depends from signature type
    switch (signatureType){
        case "digital": url = getApplicationPath('signDigital')
            signaturesList.push(signature);
            break;
        case "image": url = getApplicationPath('signImage')
            break;
    }
    // current document guid is taken from the viewer.js globals
    var data = {guid: documentGuid,
        password: password,
        signaturesData: signaturesList};
    // sign the document
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(returnedData) {
            $('#gd-modal-spinner').hide();
            if(returnedData.message != undefined){
                // if password for signature certificate is incorrect return to previouse step and show error
                if(returnedData.message.toLowerCase().indexOf("password") != -1){
                    switchSlide(2, 3, "left");
                    $("#gd-next").html("NEXT");
                    $("#gd-password-required").html(returnedData.message);
                    $("#gd-password-required").show();
                } else {
                    // open error popup
                    printMessage(returnedData.message);
                }
                return;
            }
            // prepare signing results HTML
            var result = '<div id="gd-modal-signed">Document signed successfully</div>';
            // show signing results
            switch(signaturesList[0].signatureType) {
                case "digital":
                    $("#gd-finish-step").html(result);
                    $("#gd-signing-footer").hide();
                    break;
                case "image":
                    toggleModalDialog(true, "Signing results", result);
                    $("#gd-modal-signed").toggleClass("gd-image-signed");
                    break;
            }
        },
        error: function(xhr, status, error) {
            var err = eval("(" + xhr.responseText + ")");
            console.log(err.Message);
        }
    });
}

/**
 * Sign current document
 */
function saveDrawnImage(image) {
    $('#gd-modal-spinner').show();
    // current document guid is taken from the viewer.js globals
    var data = {image: image};
    // sign the document
    $.ajax({
        type: 'POST',
        url: getApplicationPath("saveImage"),
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(returnedData) {
            $('#gd-modal-spinner').hide();
            if(returnedData.message != undefined){
                // open error popup
                printMessage(returnedData.message);
                return;
            }
            signature.signatureGuid = returnedData.guid;
            loadSignaturesTree('');
            $(".gd-signature-select").removeClass("gd-signing-disabled");
        },
        error: function(xhr, status, error) {
            var err = eval("(" + xhr.responseText + ")");
            console.log(err.Message);
        }
    });
}

/**
 * Open modal on signature upload step
 */
function openSigningFirstStepModal(){
    var browseSignatures = ($("#gd-signatures").children().length > 0) ? true: false;
    switchSlide(0, "", "right");
    // show or hide the browse button, depends on signature availability in the storage
    if(!browseSignatures){
        $(".gd-browse-signatures").hide();
        switch (signature.signatureType) {
            case "digital": $(".gd-upload-signatures").css("left", "calc(100% - 60%)");
                break;
            case "image": $(".gd-upload-signatures").css("left", "calc(100% - 72%)");
                break;
        }
    } else {
        if(signature.signatureType == "image") {
            $(".gd-upload-signatures").css("left", "calc(100% - 87%)");
            $(".gd-browse-signatures").css("left", "calc(100% - 77%)");
        }
    }
}

/**
 * Generate HTML content of the Digital sign modal
 */
function getHtmlDigitalSign() {
    // prepare signing steps HTML
    var uploadStep = getHtmlSignatureUploadModal();
    var signaturesSelectStep = getHtmlSignaturesSelectModal();
    var signatureInformationStep = getHtmlSignatureInformationModal();
    var footer = getHtmlSigningModalFooter(3);
    // generate signing modal HTML
    return '<section id="gd-sign-section" data-type="digital">' +
        '<div id="gd-modal-spinner"><i class="fa fa-circle-o-notch fa-spin"></i> &nbsp;Loading... Please wait.</div>'+
        '<div id="gd-upload-step" class="gd-slide" data-index="0">'+
        uploadStep+
        '</div>'+
        signaturesSelectStep+
        '<div id="gd-additional-info-step" class="gd-slide" data-index="2">'+
        signatureInformationStep+
        '</div>'+
        '<div id="gd-review-step" class="gd-slide" data-index="3" data-last="true">'+
        // entered date review will be here
        '</div>'+
        '<div id="gd-finish-step" class="gd-slide" data-index="4">'+
        // Signing results will be here
        '</div>'+
        footer+
        '</section>';
}

/**
 * Generate HTML content of the Image sign modal
 */
function getHtmlImageSign() {
    // prepare signing steps HTML
    var uploadStep = getHtmlSignatureUploadModal();
    var signaturesSelectStep = getHtmlSignaturesSelectModal();
    var drawStep = getHtmlDrawImageModal();
    var signaturePageSelectStep = getHtmlPagesSelectModal();
    var footer = getHtmlSigningModalFooter(2);
    // generate signing modal HTML
    return '<section id="gd-sign-section"  data-type="image">' +
        '<div id="gd-modal-spinner"><i class="fa fa-circle-o-notch fa-spin"></i> &nbsp;Loading... Please wait.</div>'+
        '<div id="gd-upload-step" class="gd-slide" data-index="0">'+
        uploadStep+
        '</div>'+
        signaturesSelectStep+
        drawStep+
        '<div id="gd-signature-page-select-step" class="gd-slide" data-index="2" data-last="true">'+
        signaturePageSelectStep+
        '</div>'+
        footer+
        '</section>';
}

/**
 * Get HTML content for signature upload/browser modal
 **/
function getHtmlSignatureUploadModal(){
    var uploadButton =  '<label class="gd-upload-signatures">'+
        '<i class="fa fa-upload"></i>UPLOAD signature(S)<input id="gd-signature-upload-input" type="file" multiple style="display: none;">'+
        '</label>';
    var browseButton =  '<label class="gd-browse-signatures">'+
        '<i class="fa fa-folder-open"></i>BROWSE signature(S)<button id="gd-open-signature" style="display: none;"></button>'+
        '</label>';
    var drawImage = "";
    if (signature.signatureType == "image"){
        drawImage = '<label class="gd-draw-signatures">'+
            '<i class="fa fa-pencil-square-o" aria-hidden="true"></i>DRAW signature(S)<button id="gd-draw-signature" style="display: none;"></button>'+
            '</label>';
    }
    return uploadButton + browseButton + drawImage;
}

/**
 * Get HTML content for signature browser modal
 **/
function getHtmlSignaturesSelectModal(){
    return  '<div id="gd-signature-select-step" class="gd-slide" data-index="1">'+
        '<div class="gd-signing-label">'+
        '<label>1. Signatures <i>Select signature to sign your document</i></label>'+
        '</div>'+
        '<div id="gd-signatures">'+
        //signatures will be here
        '</div>'+
        '</div>';
}

/**
 * Get HTML content for page select modal
 **/
function getHtmlPagesSelectModal(){
    var stepHeader = '<div class="gd-signing-label">'+
        '<label>2. Pages <i>Select one page or all pages to add signature</i></label>'+
        '</div>'+
        '<div class="gd-signature-information-review">'+
        '<label>Selected Page : <i></i></label>'+
        '</div>';
    var pageSelector = "";
    var pageNumbers = "";
    for(var i = 0; i < $("#gd-panzoom > div").length; i++){
        var pageNumber = i + 1;
        pageNumbers = pageNumbers + '<a href="#" class="gd-page-number">' + pageNumber + '</a>';
    }
    pageSelector = '<div class="gd-pages-dropdown">'+
        '<button class="gd-drop-button">Select page</button>'+
        '<div class="gd-pages-dropdown-content">'+
        pageNumbers+
        '</div>'+
        '</div>'+
        '<div id="gd-all-pages">'+
        '<div class="gd-signature">'+
        '<input id="gd-radio-all" class="gd-signature-radio" name="gd-radio" type="checkbox">'+
        '<label for="gd-radio-all" class="gd-all-pages-label">Add for all pages</label>'+
        '</div>'+
        '</div>';

    return stepHeader + pageSelector;
}

/**
 * Get HTML content for draw image step
 **/
function getHtmlDrawImageModal() {
    return  '<div id="gd-signature-draw-step" class="gd-slide" data-index="1">'+
        '<div class="gd-signing-label">'+
        '<label>1. Draw image <i>Draw your signature</i></label>'+
        '</div>'+
        '<div id="gd-draw-image">' +
        // draw area will be here
        '</div>'+
        '</div>';
}
/**
 * Get HTML content for signature information modal
 **/
function getHtmlSignatureInformationModal(){
    var documentFormat = getDocumentFormat(documentGuid, false);
    var inputs = "";
    // generate signature information imputs for depending from current document type
    if(documentFormat.format.indexOf("Portable Document") >= 0) {
        inputs = '<input id="gd-reason" type="text" placeholder="Reason">'+
            '<input id="gd-contact" type="text" placeholder="Contact">'+
            '<input id="gd-location" type="text" placeholder="Location">'+
            '<input id="gd-signature-password" type="password" placeholder="Password">'+
            '<i class="fa fa-calendar" aria-hidden="true"></i>'+
            '<input type="text" id="gd-datepicker" placeholder="Date">';
    } else if(documentFormat.format.indexOf("Word") >= 0 || documentFormat.format.indexOf("Excel")) {
        inputs = '<input id="gd-signature-comment" type="text" placeholder="Comment">'+
            '<input id="gd-signature-password" type="password" placeholder="Password">'+
            '<i class="fa fa-calendar" aria-hidden="true"></i>'+
            '<input type="text" id="gd-datepicker" placeholder="Date">';
    }
    return '<div class="gd-signing-label">'+
        '<label>2. Signature Information <i>Add additional information to sign your document</i></label>'+
        '</div>'+
        '<div class="gd-signature-information">'+
        '<div id="gd-password-required"></div>'+
        inputs+
        '</div>';
}

/**
 * Get HTML content for signature information review modal
 **/
function getHtmlReviewModal(){
    var documentFormat = getDocumentFormat(documentGuid, false);
    var info = "";
    var signatureName = signature.signatureGuid.split(/[\\\/]/).pop();
    if(documentFormat.format.indexOf("Portable Document") >= 0) {
        info = '<label>Signature: <i>' + signatureName + '</i></label>'+
            '<label>Reason: <i>' + signature.reason + '</i></label>'+
            '<label>Contact: <i>' + signature.contact + '</i></label>'+
            '<label>Location: <i>' + signature.address + '</i></label>'+
            '<label>Password: <i>' + signature.signaturePassword + '</i></label>'+
            '<label>Date: <i>' + signature.date + '</i></label>';
    } else if(documentFormat.format.indexOf("Word") >= 0 || documentFormat.format.indexOf("Excel")) {
        info = '<label>Signature: <i>' + signatureName + '</i></label>'+
            '<label>Comment: <i>' + signature.signatureComment + '</i></label>'+
            '<label>Password: <i>' + signature.signaturePassword + '</i></label>'+
            '<label>Date: <i>' + signature.date + '</i></label>';
    }
    return '<div class="gd-signing-label">'+
        '<label>3. Finish <i>Review and confirm</i></label>'+
        '</div>'+
        '<div class="gd-signature-information-review">'+
        info+
        '</div>';
}

/**
 * Get HTML content for digital signing modal footer (signing steps navigation elements)
 **/
function getHtmlSigningModalFooter(numberOfSteps){
    var steps = "";
    for(var i = 2; i <= numberOfSteps; i++){
        steps = steps + '<li>'+
            '<a href="#" class="gd-pagination">' + i + '</a>'+
            '</li>';
    }
    return  '<div id="gd-signing-footer">'+
        '<label id="gd-back">BACK</label>'+
        '<ol class="gd-modal-pagination">' +
        '<li>'+
        '<a href="#" class="gd-pagination gd-pagination-active">1</a>'+
        '</li>'+
        steps+
        '</ol>'+
        '<label id="gd-next" class="gd-signature-select gd-signing-disabled">NEXT</label>'+
        '</div>';
}

/**
 * Next button click event
 */
function switchToNextSlide(){
    // check if the last step
    if($("#gd-next").html() == "CONFIRM") {
        // get current signature type
        switch ($("#gd-sign-section").data("type")) {
            case "digital":
                // switch to entered data review step
                switchSlide(4, 3, "right");
                // sign document if step is last
                sign();
                break;
            case "image":
                // get signature positioning info
                signature.pageNumber = $(".gd-signature-information-review i").html();
                if(signature.pageNumber == ""){
                    $(".gd-signature-information-review i").html("Please select page first");
                } else {
                    // load selected signature image from storage
                    loadSignatureImage();
                }
                break
        }
    } else {
        var currentSlide = null;
        // get current signing step index
        $(".gd-slide").each(function (index, elem) {
            if ($(elem).css("display") == "block") {
                currentSlide = $(elem).data("index");
            }
            // if next step is last step, update next button to confirm button
            if ($($(".gd-slide")[index + 1]).data("last")) {
                $("#gd-next").html("CONFIRM");
                $("#gd-next").css("left", "calc(100% - 123px)");
            }
            if(currentSlide != null){
                return false;
            }
        });
        // if next step is review signing data - save entered data values
        if ($(".gd-signature-information").is(":visible")) {
            setAdditionalInformation();
        }
        // switch to next signing step if the current step is not last
        switchSlide(currentSlide + 1, currentSlide, "right");
        // update step pagination
        $($(".gd-pagination")[currentSlide - 1]).toggleClass("gd-pagination-active");
        $($(".gd-pagination")[currentSlide]).toggleClass("gd-pagination-active");
        // enable datepicker input if current step is signing data information
        if ($("#gd-datepicker").is(":visible")) {
            $("#gd-datepicker").datepicker({dateFormat: 'dd-mm-yy'});
        }
    }
}

/**
 * Switch signing steps slides in the signing modal
 * @param {int} stepNumber - step number to switch in
 * @param {int} currentSlide - current step
 * @param {string} direction - slide animation direction
 */
function switchSlide(stepNumber, currentSlide, direction) {
    var slides = $(".gd-slide");
    if(direction == "right" && stepNumber > 0){
        $(slides[currentSlide]).hide("slide", { direction: "left" }, 500);
    } else if (direction == "left" && stepNumber >= 0){
        $(slides[currentSlide]).hide("slide", { direction: "right" }, 500);
    }
    $(slides[stepNumber]).show("slide", { direction: direction }, 500);
}

/**
 * Set signature additional information data
 */
function setAdditionalInformation(){
    // save entered signature data on add additional information step
    signature.reason = $("#gd-reason").val();
    signature.contact = $("#gd-contact").val();
    signature.address = $("#gd-location").val();
    signature.date = $("#gd-datepicker").val();
    signature.signaturePassword = $("#gd-signature-password").val();
    signature.signatureComment = $("#gd-signature-comment").val();
    $("#gd-review-step").html(getHtmlReviewModal);
}

/**
 * Get selected signature image stream
 */
function loadSignatureImage() {
    // current document guid is taken from the viewer.js globals
    var data = { guid: signature.signatureGuid, page: 1, password: "" };
    $('#gd-modal-spinner').show();
    // load signature image from the storage
    $.ajax({
        type: 'POST',
        url: getApplicationPath('loadSignatureImage'),
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(returnedData) {
            if(returnedData.message != undefined){
                toggleModalDialog(false, "");
                // open error popup
                printMessage(returnedData.message);
                return;
            }
            // when ajax is done insert loaded image into the document page
            toggleModalDialog(false, "");
            var signatureGuid = "";
            // check if image should be added to all pages
            if(signature.pageNumber == "All") {
                // add image for each document page
                for(var i = 0; i < $("#gd-panzoom > div").length; i++) {
                    signature.pageNumber = i + 1;
                    // fix required to restore dropped during image inserting signature object,
                    // otherwise if not to drop it during inserting it will use the same object for all signatures
                    if(typeof(signature.signatureGuid) != "undefined"){
                        signatureGuid = signature.signatureGuid;
                    }
                    signature.signatureGuid = signatureGuid;
                    // insert image over the document page
                    insertImage(returnedData.pageImage, i + 1);
                }
            } else {
                // insert image over the selected document page
                insertImage(returnedData.pageImage, signature.pageNumber);
            }
        },
        error: function(xhr, status, error) {
            var err = eval("(" + xhr.responseText + ")");
            console.log(err.Message);
        }
    });
}

/**
 * Get selected signature image stream
 * @param {string} image - Base64 encoded image
 * @param {int} pageNumber - Number of the page into which the image should be inserted
 */
function insertImage(image, pageNumber) {
    // set document format
    signature.documentType = getDocumentFormat(documentGuid).format;
    // add current signature object into the list of signatures
    signaturesList.push(signature);
    // prepare index which will be used for specific image HTMl elements naming
    var currentImage = signatureImageIndex;
    // get HTML markup of the resize handles
    var resizeHandles = getHtmlResizeHandles();
    // prepare signature image HTML
    var signatureHtml = '<div id="gd-draggable-helper-' + currentImage + '"  class="gd-draggable-helper">' +
        '<a id="gd-apply" class="gd-image-apply" href="#">' +
        '<i class="fa fa-check-circle" aria-hidden="true"></i>' +
        '</a>' +
        '<a id="gd-cancel" class="gd-image-apply gd-image-cancel" href="#">' +
        '<i class="fa fa-ban" aria-hidden="true"></i>' +
        '</a>' +
        '<image id="gd-image-signature-' + currentImage + '" class="gd-signature-image" src="data:image/png;base64,' + image + '" alt></image>' +
        resizeHandles +
        '</div>';
    // add signature to the selected page
    $(signatureHtml).insertBefore($("#gd-page-" + pageNumber).find(".gd-wrapper"));
    // calculate initial centre of the rotation
    var rotationTop = $("#gd-image-signature-" + currentImage).height() / 2;
    var rotationLeft = $("#gd-image-signature-" + currentImage).width() / 2;
    // prepare rotation parameters object
    var rotationParams = {
        // Callback fired on rotation end.
        stop: function (event, ui) {
            signaturesList[currentImage].angle = Math.round(ui.angle.current * 180 / Math.PI);
        },
        // Set the rotation center
        rotationCenterOffset: {
            top: rotationTop,
            left: rotationLeft
        }
    };
    // enable rotation, dragging and resizing features for current image
    $("#gd-draggable-helper-" + currentImage).rotatable(rotationParams).draggable({
        // set restriction for image dragging area to current document page
        containment: "#gd-page-" + pageNumber,
        // action fired when dragging stoped
        stop: function () {
            var signatureImage = $(this);
            var signaturePos = signatureImage.position();
            // get image positioning coordinates after dragging
            signaturesList[currentImage].left = Math.round(signaturePos.left);
            signaturesList[currentImage].top = Math.round(signaturePos.top);
        }
    }).resizable({
        // set restriction for image resizing to current document page
        containment: "#gd-page-" + pageNumber,
        // set image resize handles
        handles: {
            'n': '.ui-resizable-n',
            'e': '.ui-resizable-e',
            's': '.ui-resizable-s',
            'w': '.ui-resizable-w',
            'ne': '.ui-resizable-ne',
            'se': '.ui-resizable-se',
            'sw': '.ui-resizable-sw',
            'nw': '.ui-resizable-nw'
        },
        grid: [10, 10],
        create: function (event, ui) {
            var width = $(event.target).width();
            var height = $(event.target).height();
            setGridPosition(width, height);
        },
        stop: function (event, image) {
            // set signature updated size and position
            signaturesList[currentImage].imageWidth = Math.round(image.size.width);
            signaturesList[currentImage].imageHeight = Math.round(image.size.height);
            signaturesList[currentImage].left = Math.round(image.position.left);
            signaturesList[currentImage].top = Math.round(image.position.top);
            setGridPosition(signaturesList[currentImage].imageWidth, signaturesList[currentImage].imageHeight);
            // update rotation centre
            $("#gd-draggable-helper-" + currentImage).rotatable({
                rotationCenterOffset: {
                    top: signaturesList[currentImage].imageHeight / 2,
                    left: signaturesList[currentImage].imageWidth / 2
                }
            });
        }
    });
    // encrease signature index
    signatureImageIndex = signatureImageIndex + 1;
    // drop the signature object
    signature = {};
}

function getHtmlResizeHandles(){
    return '<div class="ui-resizable-handle ui-resizable-n"></div>'+
        '<div class="ui-resizable-handle ui-resizable-e"></div>'+
        '<div class="ui-resizable-handle ui-resizable-s"></div>'+
        '<div class="ui-resizable-handle ui-resizable-w"></div>'+
        '<div class="ui-resizable-handle ui-resizable-ne"></div>'+
        '<div class="ui-resizable-handle ui-resizable-se"></div>'+
        '<div class="ui-resizable-handle ui-resizable-sw"></div>'+
        '<div class="ui-resizable-handle ui-resizable-nw"></div>';
}

function setGridPosition(width, height){
    $('.ui-resizable-n').css('left', (width/2-4)+'px');
    $('.ui-resizable-e').css('top', (height/2-4)+'px');
    $('.ui-resizable-s').css('left', (width/2-4)+'px');
    $('.ui-resizable-w').css('top', (height/2-4)+'px');
}
/*
******************************************************************
******************************************************************
GROUPDOCS.SIGNATURE PLUGIN
******************************************************************
******************************************************************
*/
(function( $ ) {
    /*
    ******************************************************************
    STATIC VALUES
    ******************************************************************
    */
    var gd_navbar = '#gd-navbar';

    /*
    ******************************************************************
    METHODS
    ******************************************************************
    */
    var methods = {
        init : function( options ) {
            // set defaults
            var defaults = {
                textSignature: true,
                imageSignature: true,
                digitalSignature: true,
                qrCodeSignature: true,
                barCodeSignature: true,
                stampSignature: true,
                downloadOriginal: true,
                downloadSigned: true
            };

            options = $.extend(defaults, options);

            getHtmlDownloadPanel();
            $(gd_navbar).append(getHtmlSignaturePanel);
            $(gd_navbar).append(getHtmlSavePanel);

            if(options.textSignature){
                $("#gd-btn-signature-value").append(getHtmlTextSignatureElement);
            }

            if(options.imageSignature){
                $("#gd-btn-signature-value").append(getHtmlImageSignatureElement);
            }

            if(options.digitalSignature){
                $("#gd-btn-signature-value").append(getHtmlDigitalSignatureElement);
            }

            if(options.qrCodeSignature){
                $("#gd-btn-signature-value").append(getHtmlQrcodeSignatureElement);
            }

            if(options.barCodeSignature){
                $("#gd-btn-signature-value").append(getHtmlBarcodeSignatureElement);
            }

            if(options.stampSignature){
                $("#gd-btn-signature-value").append(getHtmlStampSignatureElement);
            }

            if(options.downloadOriginal){
                $("#gd-btn-download-value").append(getHtmlDownloadOriginalElement());
            }

            if(options.downloadSigned){
                $("#gd-btn-download-value").append(getHtmlDownloadSignedElement());
            }
        }
    };


    /*
    ******************************************************************
    INIT PLUGIN
    ******************************************************************
    */
    $.fn.signature = function( method ) {
        if ( methods[method] ) {
            return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method' +  method + ' does not exist on jQuery.signature' );
        }
    };


    /*
    ******************************************************************
    HTML MARKUP
    ******************************************************************
    */
    function getHtmlSignaturePanel(){
        return '<li class="gd-nav-toggle" id="gd-signature-val-container">'+
            '<span id="gd-signature-value">' +
            '<i class="fa fa-pencil-square-o"></i>'+
            '<span class="gd-tooltip">Sign</span>'+
            '</span>'+
            '<span class="gd-nav-caret"></span>'+
            '<ul class="gd-nav-dropdown-menu gd-nav-dropdown" id="gd-btn-signature-value">'+
            // signature types will be here
            '</ul>'+
            '</li>';
    }

    function getHtmlDownloadPanel(){
        var downloadBtn = $("#gd-btn-download");
        var defaultHtml = downloadBtn.html();
        var downloadDropDown = '<li class="gd-nav-toggle" id="gd-download-val-container">'+
            '<span id="gd-download-value">' +
            '<i class="fa fa-download"></i>' +
            '<span class="gd-tooltip">Download</span>' +
            '</span>'+
            '<span class="gd-nav-caret"></span>'+
            '<ul class="gd-nav-dropdown-menu gd-nav-dropdown" id="gd-btn-download-value">'+
            // download types will be here
            '</ul>'+
            '</li>';
        downloadBtn.html(downloadDropDown);
    }

    function getHtmlDownloadOriginalElement(){
        return '<li id="gd-original-download">Download Original</li>';
    }

    function getHtmlDownloadSignedElement(){
        return '<li id="gd-signed-download">Download Signed</li>';
    }

    function getHtmlSavePanel(){
        return '<li id="gd-nav-save" class="gd-save-disabled"><i class="fa fa-floppy-o"></i><span class="gd-tooltip">Save</span></li>';
    }

    function getHtmlTextSignatureElement(){
        return '<li id="qd-text-sign">Text</li>';
    }

    function getHtmlImageSignatureElement(){
        return '<li id="qd-image-sign">Image</li>';
    }

    function getHtmlDigitalSignatureElement(){
        return '<li id="qd-digital-sign">Digital</li>';
    }

    function getHtmlQrcodeSignatureElement(){
        return '<li id="qd-qr-sign">QR-Code</li>';
    }

    function getHtmlBarcodeSignatureElement(){
        return '<li id="qd-barcode-sign">Barcode</li>';
    }

    function getHtmlStampSignatureElement(){
        return '<li id="gd-stamp-sign">Stamp</li>';
    }

})(jQuery);