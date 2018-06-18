<#-- @ftlvariable name="" type="Signature" -->
<!DOCTYPE html>
<html>
    <head>
        <title>Signature for Java Dropwizard</title>
        <link type="text/css" rel="stylesheet" href="assets/common/css/font-awesome.min.css"/>
        <link type="text/css" rel="stylesheet" href="assets/common/css/swiper.min.css">
        <link type="text/css" rel="stylesheet" href="assets/signature/css/jquery-ui.min.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/viewer.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/viewer.mobile.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/viewer-dark.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/bcPaint.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/bcPaint.mobile.css"/>
		<link type="text/css" rel="stylesheet" href="assets/signature/css/viewer-circle-progress.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/signature.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/stampGenerator.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/qrCodeGenerator.css"/>
        <link type="text/css" rel="stylesheet" href="assets/signature/css/bcPicker.css"/>
        <script type="text/javascript" src="assets/common/js/jquery.min.js"></script>
        <script type="text/javascript" src="assets/common/js/swiper.min.js"></script>
        <script type="text/javascript" src="assets/signature/js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="assets/signature/js/viewer.js"></script>
        <script type="text/javascript" src="assets/signature/js/signature.js"></script>
        <script type="text/javascript" src="assets/signature/js/rotatable.js"></script>
        <script type="text/javascript" src="assets/signature/js/bcPaint.js"></script>
        <script type="text/javascript" src="assets/signature/js/bcPicker.js"></script>
        <script type="text/javascript" src="assets/signature/js/stampGenerator.js"></script>
        <script type="text/javascript" src="assets/signature/js/qrCodeGenerator.js"></script>
    </head>
    <body>
        <div id="element"></div>
        <script type="text/javascript">
            $('#element').viewer({
                applicationPath: 'http://${globalConfiguration.server.hostAddress}:${globalConfiguration.server.httpPort?c}/signature',
                defaultDocument: '${globalConfiguration.viewer.defaultDocument}',
                htmlMode: false,
                preloadPageCount: ${globalConfiguration.viewer.preloadPageCount?c},
                zoom : false,
                pageSelector: ${globalConfiguration.viewer.pageSelector?c},
                search: false,
                thumbnails: false,
                rotate: false,
                download: ${globalConfiguration.viewer.download?c},
                upload: ${globalConfiguration.viewer.upload?c},
                print: ${globalConfiguration.viewer.print?c},
                browse: ${globalConfiguration.viewer.browse?c},
                rewrite: ${globalConfiguration.viewer.rewrite?c}
            });
            $('#element').signature({
                textSignature: ${globalConfiguration.signature.textSignature?c},
                imageSignature:  ${globalConfiguration.signature.imageSignature?c},
                digitalSignature:  ${globalConfiguration.signature.digitalSignature?c},
                qrCodeSignature:  ${globalConfiguration.signature.qrCodeSignature?c},
                barCodeSignature:  ${globalConfiguration.signature.barCodeSignature?c},
                stampSignature:  ${globalConfiguration.signature.stampSignature?c},
                downloadOriginal:  ${globalConfiguration.signature.downloadOriginal?c},
                downloadSigned:  ${globalConfiguration.signature.downloadSigned?c}
            });
        </script>
    </body>
</html>