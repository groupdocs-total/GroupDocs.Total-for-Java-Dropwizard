# GroupDocs.Total for Java Dropwizard Example
###### version 0.4.42

[![Build Status](https://travis-ci.org/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard.svg?branch=master)](https://travis-ci.org/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard)
[![Maintainability](https://api.codeclimate.com/v1/badges/83ca13ac2b000eafba60/maintainability)](https://codeclimate.com/github/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard/maintainability)

## System Requirements
- Java 8 (JDK 1.8)
- Maven 3


## Description
GroupDocs.Total UI suite is a native, simple, fully configurable and optimized application which allows you to manipulate documents within your desktop solutions and web apps without requiring any other commercial application through GroupDocs APIs.

**Note** Without a license application will run in trial mode, purchase [GroupDocs.Total for Java license](https://purchase.groupdocs.com/order-online-step-1-of-8.aspx) or request [GroupDocs.Total for Java temporary license](https://purchase.groupdocs.com/temporary-license).


## Demo Video
Coming soon


## Features
#### GroupDocs.Viewer
- Clean, modern and intuitive design
- Easily switchable colour theme (create your own colour theme in 5 minutes)
- Responsive design
- Mobile support (open application on any mobile device)
- Support over 50 documents and image formats
- HTML and image modes
- Fully customizable navigation panel
- Open password protected documents
- Text searching & highlighting
- Download documents
- Upload documents
- Print document
- Rotate pages
- Zoom in/out documents without quality loss in HTML mode
- Thumbnails
- Smooth page navigation
- Smooth document scrolling
- Preload pages for faster document rendering
- Multi-language support for displaying errors
- Display two or more pages side by side (when zooming out)
- Cross-browser support (Safari, Chrome, Opera, Firefox)
- Cross-platform support (Windows, Linux, MacOS)
#### GroupDocs.Signature
- Clean, modern and intuitive design
- Easily switchable colour theme (create your own colour theme in 5 minutes)
- Responsive design
- Mobile support (open application on any mobile device)
- Support over 50 documents and image formats
- Image mode
- Fully customizable navigation panel
- Sign password protected documents
- Download original documents
- Download signed documents
- Upload documents
- Upload signatures
- Sign document with such signature types: digital certificate, image, stamp, qrCode, barCode.
- Draw signature image
- Draw stamp signature
- Generate bar code signature
- Generate qr code signature
- Print document
- Smooth page navigation
- Smooth document scrolling
- Preload pages for faster document rendering
- Multi-language support for displaying errors
- Cross-browser support (Safari, Chrome, Opera, Firefox)
- Cross-platform support (Windows, Linux, MacOS)
#### GroupDocs.Annotation
- Clean, modern and intuitive design
- Easily switchable colour theme (create your own colour theme in 5 minutes)
- Responsive design
- Mobile support (open application on any mobile device)
- Support over 50 documents and image formats
- Image mode
- Fully customizable navigation panel
- Annotate password protected documents
- Download original documents
- Download annotated documents
- Upload documents
- Annotate document with such annotation types: 
   * Text
   * Area
   * Point
   * TextStrikeout
   * Polyline
   * TextField
   * Watermark
   * TextReplacement
   * Arrow
   * TextRedaction
   * ResourcesRedaction
   * TextUnderline
   * Distance
- Draw annotation over the document page
- Add comment or reply
- Print document
- Smooth page navigation
- Smooth document scrolling
- Preload pages for faster document rendering
- Multi-language support for displaying errors
- Cross-browser support (Safari, Chrome, Opera, Firefox)
- Cross-platform support (Windows, Linux, MacOS)
#### GroupDocs.Comparison
- Clean, modern and intuitive design
- Easily switchable colour theme (create your own colour theme in 5 minutes)
- Responsive design
- Mobile support (open application on any mobile device)
- HTML and image modes
- Fully customizable navigation panel
- Compare documents
- Multi-compare several documents
- Compare password protected documents
- Upload documents
- Display clearly visible differences
- Download comparison results
- Print comparison results
- Smooth document scrolling
- Preload pages for faster document rendering
- Multi-language support for displaying errors
- Cross-browser support (Safari, Chrome, Opera, Firefox)
- Cross-platform support (Windows, Linux, MacOS)

## How to run

You can run this sample by one of following methods


#### Build from source

Download [source code](https://github.com/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard/archive/master.zip) from github or clone this repository.

```bash
git clone https://github.com/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard
cd GroupDocs.Total-for-Java-Dropwizard
mvn clean compile exec:java
## Open http://localhost:8080/ in your favorite browser.
```

#### Binary release (with all dependencies)

Download [latest release](https://github.com/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard/releases/latest) from [releases page](https://github.com/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard/releases). 

**Note**: This method is **recommended** for running this sample behind firewall.

```bash
curl -J -L -o release.tar.gz https://github.com/groupdocs-total/GroupDocs.Total-for-Java-Dropwizard/releases/download/0.4.42/release.tar.gz
tar -xvzf release.tar.gz
cd release
java -jar total-0.4.42.jar configuration.yaml
## Open http://localhost:8080/ in your favorite browser.
```

#### Docker image
Use [docker](https://www.docker.com/) image.

```bash
mkdir DocumentSamples
mkdir Licenses
docker run -p 8080:8080 --env application.hostAddress=localhost -v `pwd`/DocumentSamples:/home/groupdocs/app/DocumentSamples -v `pwd`/Licenses:/home/groupdocs/app/Licenses groupdocs/total
## Open http://localhost:8080/ in your favorite browser.
```

#### Configuration
For all methods above you can adjust settings in `configuration.yml`. By default in this sample will lookup for license file in `./Licenses` folder, so you can simply put your license file in that folder or specify relative/absolute path by setting `licensePath` value in `configuration.yml`. 

## Resources
- **Website:** [www.groupdocs.com](http://www.groupdocs.com)
- **Product Home:** [GroupDocs.Total for Java](https://products.groupdocs.com/total/java)
- **Product API References:** [GroupDocs.Total for Java API](https://apireference.groupdocs.com)
- **Download:** [Download GroupDocs.Total for Java](http://downloads.groupdocs.com/total/java)
- **Documentation:** [GroupDocs.Total for Java Documentation](https://docs.groupdocs.com/dashboard.action)
- **Free Support Forum:** [GroupDocs.Total for Java Free Support Forum](https://forum.groupdocs.com/c/total)
- **Paid Support Helpdesk:** [GroupDocs.Total for Java Paid Support Helpdesk](https://helpdesk.groupdocs.com)
- **Blog:** [GroupDocs.Total for Java Blog](https://blog.groupdocs.com/category/groupdocs-total-product-family)
