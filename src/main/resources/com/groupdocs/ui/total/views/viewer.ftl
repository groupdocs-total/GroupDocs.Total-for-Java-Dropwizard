<#-- @ftlvariable name="" type="com.groupdocs.ui.total.views.Viewer" -->
<!DOCTYPE html>
<html>
    <head>
        <title>GroupDocs.Viewer for Java Dropwizard</title>
        <link type="text/css" rel="stylesheet" href="assets/css/font-awesome.min.css"/>
        <link type="text/css" rel="stylesheet" href="assets/css/swiper.min.css">
        <link type="text/css" rel="stylesheet" href="assets/css/viewer/viewer.css"/>
        <link type="text/css" rel="stylesheet" href="assets/css/viewer/viewer.mobile.css"/>
        <link type="text/css" rel="stylesheet" href="assets/css/viewer/viewer-dark.css"/>
		<link type="text/css" rel="stylesheet" href="assets/css/viewer/viewer-circle-progress.css"/>
        <script type="text/javascript" src="assets/js/jquery.min.js"></script>
		<script type="text/javascript" src="assets/js/swiper.min.js"></script>
        <script type="text/javascript" src="assets/js/viewer/viewer.js"></script>
    </head>
    <body>
        <div id="element"></div>
        <script type="text/javascript">
            $('#element').viewer({
                applicationPath: 'http://${totalConfig.server.hostAddress}:${totalConfig.server.httpPort?c}/viewer',
				defaultDocument: '${totalConfig.viewer.defaultDocument}',
				htmlMode: ${totalConfig.viewer.htmlMode?c},
                preloadPageCount: ${totalConfig.viewer.preloadPageCount?c},
				zoom : ${totalConfig.viewer.zoom?c},
				pageSelector: ${totalConfig.viewer.pageSelector?c},
				search: ${totalConfig.viewer.search?c},
				thumbnails: ${totalConfig.viewer.thumbnails?c},
				rotate: ${totalConfig.viewer.rotate?c},
				download: ${totalConfig.viewer.download?c},
                upload: ${totalConfig.viewer.upload?c},
                print: ${totalConfig.viewer.print?c},                
                browse: ${totalConfig.viewer.browse?c},               
                rewrite: ${totalConfig.viewer.rewrite?c}
            });
        </script>
    </body>
</html>