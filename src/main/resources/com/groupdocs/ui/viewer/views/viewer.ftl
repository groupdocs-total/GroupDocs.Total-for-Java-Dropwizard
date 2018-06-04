<#-- @ftlvariable name="" type="Viewer" -->
<!DOCTYPE html>
<html>
    <head>
        <title>GroupDocs.Viewer for Java Dropwizard</title>
        <link type="text/css" rel="stylesheet" href="assets/common/css/font-awesome.min.css"/>
        <link type="text/css" rel="stylesheet" href="assets/common/css/swiper.min.css">
        <link type="text/css" rel="stylesheet" href="assets/viewer/css/viewer.css"/>
        <link type="text/css" rel="stylesheet" href="assets/viewer/css/viewer.mobile.css"/>
        <link type="text/css" rel="stylesheet" href="assets/viewer/css/viewer-dark.css"/>
		<link type="text/css" rel="stylesheet" href="assets/viewer/css/viewer-circle-progress.css"/>
        <script type="text/javascript" src="assets/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="assets/common/js/swiper.min.js"></script>
        <script type="text/javascript" src="assets/viewer/js/viewer.js"></script>
    </head>
    <body>
        <div id="element"></div>
        <script type="text/javascript">
            $('#element').viewer({
                applicationPath: 'http://${totalConfiguration.server.hostAddress}:${totalConfiguration.server.httpPort?c}/viewer',
				defaultDocument: '${totalConfiguration.viewer.defaultDocument}',
				htmlMode: ${totalConfiguration.viewer.htmlMode?c},
                preloadPageCount: ${totalConfiguration.viewer.preloadPageCount?c},
				zoom : ${totalConfiguration.viewer.zoom?c},
				pageSelector: ${totalConfiguration.viewer.pageSelector?c},
				search: ${totalConfiguration.viewer.search?c},
				thumbnails: ${totalConfiguration.viewer.thumbnails?c},
				rotate: ${totalConfiguration.viewer.rotate?c},
				download: ${totalConfiguration.viewer.download?c},
                upload: ${totalConfiguration.viewer.upload?c},
                print: ${totalConfiguration.viewer.print?c},
                browse: ${totalConfiguration.viewer.browse?c},
                rewrite: ${totalConfiguration.viewer.rewrite?c}
            });
        </script>
    </body>
</html>