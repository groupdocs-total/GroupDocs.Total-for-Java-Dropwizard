<#-- @ftlvariable name="" type="Comparison" -->
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>GroupDocs.Comparison for Java Spring</title>
    <link type="text/css" rel="stylesheet" href="/assets/common/css/all.min.css">
    <link type="text/css" rel="stylesheet" href="/assets/common/css/v4-shims.min.css">
    <link type="text/css" rel="stylesheet" href="/assets/comparison/css/all.css"/>
    <link type="text/css" rel="stylesheet" href="/assets/common/css/swiper.min.css">
    <link type="text/css" rel="stylesheet" href="/assets/common/css/circle-progress.css"/>
    <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer.css"/>
    <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer.mobile.css"/>
    <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer-dark.css"/>
    <link type="text/css" rel="stylesheet" href="/assets/comparison/css/comparison.css"/>
    <link type="text/css" rel="stylesheet" href="/assets/comparison/css/comparison.mobile.css"/>
    <script type="text/javascript" src="/assets/common/js/jquery.min.js"></script>
    <script type="text/javascript" src="/assets/common/js/swiper.min.js"></script>
    <script type="text/javascript" src="/assets/comparison/js/comparison.js"></script>
</head>
<body>
<div id="element"></div>
<script th:inline="javascript">
    $('#element').comparison({
        applicationPath: 'http://${globalConfiguration.application.hostAddress}:${globalConfiguration.server.httpPort?c}/comparison',
        download: ${globalConfiguration.common.download?c},
        upload: ${globalConfiguration.common.upload?c},
        print: ${globalConfiguration.common.print?c},
        rewrite: ${globalConfiguration.common.rewrite?c},
        preloadResultPageCount: ${globalConfiguration.comparison.preloadResultPageCount?c},
        multiComparing: ${globalConfiguration.comparison.multiComparing?c},
    });
</script>
</body>
</html>