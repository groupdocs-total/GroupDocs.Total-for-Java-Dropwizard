<#-- @ftlvariable name="" type="Annotation" -->
<!DOCTYPE html>
<html>
    <head>
        <title>Annotation for Java Dropwizard</title>
        <link type="text/css" rel="stylesheet" href="/assets/common/css/all.min.css">
        <link type="text/css" rel="stylesheet" href="/assets/common/css/v4-shims.min.css">
        <link type="text/css" rel="stylesheet" href="/assets/common/css/swiper.min.css">
        <link type="text/css" rel="stylesheet" href="/assets/common/css/jquery-ui.min.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/common/css/circle-progress.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer.mobile.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer-dark.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/annotation/css/annotation.css"/>
        <script type="text/javascript" src="/assets/common/js/jquery.min.js"></script>
        <script type="text/javascript" src="/assets/common/js/swiper.min.js"></script>
        <script type="text/javascript" src="/assets/common/js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/assets/common/js/jquery.ui.touch-punch.min.js"></script>
        <script type="text/javascript" src="/assets/common/js/jquery.initialize.min.js"></script>
        <script type="text/javascript" src="/assets/common/js/jquery.timeago.js"></script>
        <script type="text/javascript" src="/assets/viewer/js/viewer.js"></script>
        <script type="text/javascript" src="/assets/annotation/js/svg.min.js"></script>
        <script type="text/javascript" src="/assets/annotation/js/svg.draw.js"></script>
        <script type="text/javascript" src="/assets/annotation/js/drawSvgAnnotation.js"></script>
        <script type="text/javascript" src="/assets/annotation/js/drawTextAnnotation.js"></script>
        <script type="text/javascript" src="/assets/annotation/js/drawFieldAnnotation.js"></script>
        <script type="text/javascript" src="/assets/annotation/js/annotation.js"></script>
    </head>
    <body>
        <div id="element"></div>
        <script type="text/javascript">
            $('#element').viewer({
                applicationPath: 'http://${globalConfiguration.server.hostAddress}:${globalConfiguration.server.httpPort?c}/annotation',
                defaultDocument: '${globalConfiguration.annotation.defaultDocument}',
                htmlMode: false,
                preloadPageCount: ${globalConfiguration.annotation.preloadPageCount?c},
                zoom : false,
                pageSelector: ${globalConfiguration.common.pageSelector?c},
                search: false,
                thumbnails: false,
                rotate: false,
                download: ${globalConfiguration.common.download?c},
                upload: ${globalConfiguration.common.upload?c},
                print: ${globalConfiguration.common.print?c},
                browse: ${globalConfiguration.common.browse?c},
                rewrite: ${globalConfiguration.common.rewrite?c}
            });
            $('#element').annotation({
                textAnnotation: ${globalConfiguration.annotation.textAnnotation?c},
                areaAnnotation: ${globalConfiguration.annotation.areaAnnotation?c},
                pointAnnotation: ${globalConfiguration.annotation.pointAnnotation?c},
                textStrikeoutAnnotation: ${globalConfiguration.annotation.textStrikeoutAnnotation?c},
                polylineAnnotation: ${globalConfiguration.annotation.polylineAnnotation?c},
                textFieldAnnotation: ${globalConfiguration.annotation.textFieldAnnotation?c},
                watermarkAnnotation: ${globalConfiguration.annotation.watermarkAnnotation?c},
                textReplacementAnnotation: ${globalConfiguration.annotation.textReplacementAnnotation?c},
                arrowAnnotation: ${globalConfiguration.annotation.arrowAnnotation?c},
                textRedactionAnnotation: ${globalConfiguration.annotation.textRedactionAnnotation?c},
                resourcesRedactionAnnotation: ${globalConfiguration.annotation.resourcesRedactionAnnotation?c},
                textUnderlineAnnotation: ${globalConfiguration.annotation.textUnderlineAnnotation?c},
                distanceAnnotation: ${globalConfiguration.annotation.distanceAnnotation?c},
                downloadOriginal:  ${globalConfiguration.annotation.downloadOriginal?c},
                downloadAnnotated:  ${globalConfiguration.annotation.downloadAnnotated?c}
            });
        </script>
    </body>
</html>