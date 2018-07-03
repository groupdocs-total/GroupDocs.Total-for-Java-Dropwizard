package com.groupdocs.ui.signature.signer;

import com.groupdocs.signature.domain.stamps.StampBackgroundCropType;
import com.groupdocs.signature.domain.stamps.StampLine;
import com.groupdocs.signature.domain.stamps.StampTextRepeatType;
import com.groupdocs.signature.options.stampsignature.CellsStampSignOptions;
import com.groupdocs.signature.options.stampsignature.WordsStampSignOptions;
import com.groupdocs.signature.options.stampsignature.ImagesStampSignOptions;
import com.groupdocs.signature.options.stampsignature.SlidesStampSignOptions;
import com.groupdocs.signature.options.stampsignature.PdfStampSignOptions;
import com.groupdocs.ui.signature.entity.web.SignatureDataEntity;
import com.groupdocs.ui.signature.entity.xml.StampXmlEntity;

/**
 * StampSigner
 * Signs documents with the stamp signature
 * @author Aspose Pty Ltd
 */
public class StampSigner extends Signer{
    private StampXmlEntity[] stampData;

    /**
     * Constructor
     * @param stampData
     * @param signatureData
     */
    public StampSigner(StampXmlEntity[] stampData, SignatureDataEntity signatureData){
        super(signatureData);
        this.stampData = stampData;
    }

    /**
     * Add stamp signature data to pdf sign options
     * @return PdfStampSignOptions
     */
    @Override
    public PdfStampSignOptions signPdf(){
        // setup options
        PdfStampSignOptions pdfSignOptions = new PdfStampSignOptions();
        pdfSignOptions.setHeight(signatureData.getImageHeight());
        pdfSignOptions.setWidth(signatureData.getImageWidth());
        pdfSignOptions.setTop(signatureData.getTop());
        pdfSignOptions.setLeft(signatureData.getLeft());
        pdfSignOptions.setDocumentPageNumber(signatureData.getPageNumber());
        pdfSignOptions.setRotationAngle(signatureData.getAngle());
        pdfSignOptions.setBackgroundColor(getColor(stampData[stampData.length - 1].getBackgroundColor()));
        pdfSignOptions.setBackgroundColorCropType(StampBackgroundCropType.OuterArea);
        // draw stamp lines
        for (int n = 0; n < stampData.length; n++) {
            String text = "";
            // prepare line text
            for (int m = 0; m < stampData[n].getTextRepeat(); m++) {
                text = text + stampData[n].getText();
            }
            // set reduction size - required to recalculate each stamp line height and font size after stamp resizing in the UI
            int reductionSize = 0;
            // check if reduction size is between 1 and 2. for example: 1.25
            if((double)stampData[n].getHeight() / signatureData.getImageHeight() > 1 && (double)stampData[n].getHeight() / signatureData.getImageHeight() < 2) {
                reductionSize = 2;
            } else if(stampData[n].getHeight() / signatureData.getImageHeight() == 0) {
                reductionSize = 1;
            } else {
                reductionSize = stampData[n].getHeight() / signatureData.getImageHeight();
            }
            // draw most inner line - horizontal text
            if ((n + 1) == stampData.length) {
                StampLine squareLine = new StampLine();
                squareLine.setText(text);
                squareLine.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                squareLine.setTextColor(getColor(stampData[n].getTextColor()));
                pdfSignOptions.getInnerLines().add(squareLine);
                // check if stamp contains from only one line
                if(stampData.length == 1){
                    // if stamp contains only one line draw it as outer and inner line
                    StampLine line = new StampLine();
                    line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                    line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                    line.getOuterBorder().setWeight(0.5);
                    line.getInnerBorder().setColor(getColor(stampData[n].getBackgroundColor()));
                    line.getInnerBorder().setWeight(0.5);
                    line.setHeight(1);
                    pdfSignOptions.getOuterLines().add(line);
                }
            } else {
                // draw outer stamp lines - rounded
                int height = (stampData[n].getRadius() - stampData[n + 1].getRadius()) / reductionSize;
                StampLine line = new StampLine();
                line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                line.getOuterBorder().setWeight(0.5);
                line.getInnerBorder().setColor(getColor(stampData[n + 1].getStrokeColor()));
                line.getInnerBorder().setWeight(0.5);
                line.setText(text);
                line.setHeight(height);
                line.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                line.setTextColor(getColor(stampData[n].getTextColor()));
                line.setTextBottomIntent((height / 2));
                line.setTextRepeatType(StampTextRepeatType.RepeatWithTruncation);
                pdfSignOptions.getOuterLines().add(line);
            }
        }
        return pdfSignOptions;
    }

    /**
     * Add stamp signature data to image sign options
     * @return ImageStampSignOptions
     */
    @Override
    public ImagesStampSignOptions signImage(){
        // setup options
        ImagesStampSignOptions imageSignOptions = new ImagesStampSignOptions();
        imageSignOptions.setHeight(signatureData.getImageHeight());
        imageSignOptions.setWidth(signatureData.getImageWidth());
        imageSignOptions.setTop(signatureData.getTop());
        imageSignOptions.setLeft(signatureData.getLeft());
        imageSignOptions.setDocumentPageNumber(signatureData.getPageNumber());
        imageSignOptions.setRotationAngle(signatureData.getAngle());
        imageSignOptions.setBackgroundColor(getColor(stampData[stampData.length - 1].getBackgroundColor()));
        imageSignOptions.setBackgroundColorCropType(StampBackgroundCropType.OuterArea);
        for (int n = 0; n < stampData.length; n++) {
            String text = "";
            for (int m = 0; m < stampData[n].getTextRepeat(); m++) {
                text = text + stampData[n].getText();
            }
            // set reduction size - required to recalculate each stamp line height and font size after stamp resizing in the UI
            int reductionSize = 0;
            // check if reduction size is between 1 and 2. for example: 1.25
            if((double)stampData[n].getHeight() / signatureData.getImageHeight() > 1 && (double)stampData[n].getHeight() / signatureData.getImageHeight() < 2) {
                reductionSize = 2;
            } else if(stampData[n].getHeight() / signatureData.getImageHeight() == 0) {
                reductionSize = 1;
            } else {
                reductionSize = stampData[n].getHeight() / signatureData.getImageHeight();
            }
            if ((n + 1) == stampData.length) {
                // draw inner horizontal line
                StampLine squareLine = new StampLine();
                squareLine.setText(text);
                squareLine.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                squareLine.setTextColor(getColor(stampData[n].getTextColor()));
                imageSignOptions.getInnerLines().add(squareLine);
                if(stampData.length == 1){
                    StampLine line = new StampLine();
                    line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                    line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                    line.getOuterBorder().setWeight(0.5);
                    line.getInnerBorder().setColor(getColor(stampData[n].getBackgroundColor()));
                    line.getInnerBorder().setWeight(0.5);
                    line.setHeight(1);
                    imageSignOptions.getOuterLines().add(line);
                }
            } else {
                // draw outer rounded lines
                int height = (stampData[n].getRadius() - stampData[n + 1].getRadius()) / reductionSize;
                StampLine line = new StampLine();
                line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                line.getOuterBorder().setWeight(0.5);
                line.getInnerBorder().setColor(getColor(stampData[n + 1].getStrokeColor()));
                line.getInnerBorder().setWeight(0.5);
                line.setText(text);
                line.setHeight(height);
                line.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                line.setTextColor(getColor(stampData[n].getTextColor()));
                line.setTextBottomIntent((height / 2));
                line.setTextRepeatType(StampTextRepeatType.RepeatWithTruncation);
                imageSignOptions.getOuterLines().add(line);
            }
        }
        return imageSignOptions;
    }

    /**
     * Add stamp signature data to words sign options
     * @return WordsStampSignOptions
     */
    @Override
    public WordsStampSignOptions signWord(){
        // setup options
        WordsStampSignOptions wordsSignOptions = new WordsStampSignOptions();
        wordsSignOptions.setHeight(signatureData.getImageHeight());
        wordsSignOptions.setWidth(signatureData.getImageWidth());
        wordsSignOptions.setTop(signatureData.getTop());
        wordsSignOptions.setLeft(signatureData.getLeft());
        wordsSignOptions.setDocumentPageNumber(signatureData.getPageNumber());
        wordsSignOptions.setRotationAngle(signatureData.getAngle());
        wordsSignOptions.setBackgroundColor(getColor(stampData[stampData.length - 1].getBackgroundColor()));
        wordsSignOptions.setBackgroundColorCropType(StampBackgroundCropType.OuterArea);
        for (int n = 0; n < stampData.length; n++) {
            String text = "";
            for (int m = 0; m < stampData[n].getTextRepeat(); m++) {
                text = text + stampData[n].getText();
            }
            // set reduction size - required to recalculate each stamp line height and font size after stamp resizing in the UI
            int reductionSize = 0;
            // check if reduction size is between 1 and 2. for example: 1.25
            if((double)stampData[n].getHeight() / signatureData.getImageHeight() > 1 && (double)stampData[n].getHeight() / signatureData.getImageHeight() < 2) {
                reductionSize = 2;
            } else if(stampData[n].getHeight() / signatureData.getImageHeight() == 0) {
                reductionSize = 1;
            } else {
                reductionSize = stampData[n].getHeight() / signatureData.getImageHeight();
            }
            if ((n + 1) == stampData.length) {
                // draw inner horizontal line
                StampLine squareLine = new StampLine();
                squareLine.setText(text);
                squareLine.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                squareLine.setTextColor(getColor(stampData[n].getTextColor()));
                wordsSignOptions.getInnerLines().add(squareLine);
                if(stampData.length == 1){
                    StampLine line = new StampLine();
                    line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                    line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                    line.getOuterBorder().setWeight(0.5);
                    line.getInnerBorder().setColor(getColor(stampData[n].getBackgroundColor()));
                    line.getInnerBorder().setWeight(0.5);
                    line.setHeight(1);
                    wordsSignOptions.getOuterLines().add(line);
                }
            } else {
                // draw outer rounded lines
                int height = (stampData[n].getRadius() - stampData[n + 1].getRadius()) / reductionSize;
                StampLine line = new StampLine();
                line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                line.getOuterBorder().setWeight(0.5);
                line.getInnerBorder().setColor(getColor(stampData[n + 1].getStrokeColor()));
                line.getInnerBorder().setWeight(0.5);
                line.setText(text);
                line.setHeight(height);
                line.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                line.setTextColor(getColor(stampData[n].getTextColor()));
                line.setTextBottomIntent((height / 2));
                line.setTextRepeatType(StampTextRepeatType.RepeatWithTruncation);
                wordsSignOptions.getOuterLines().add(line);
            }
        }
        return wordsSignOptions;
    }

    /**
     * Add stamp signature data to cells sign options
     * @return CellsStampSignOptions
     */
    @Override
    public CellsStampSignOptions signCells(){
        // setup options
        CellsStampSignOptions cellsSignOptions = new CellsStampSignOptions();
        cellsSignOptions.setHeight(signatureData.getImageHeight());
        cellsSignOptions.setWidth(signatureData.getImageWidth());
        cellsSignOptions.setTop(signatureData.getTop());
        cellsSignOptions.setLeft(signatureData.getLeft());
        cellsSignOptions.setDocumentPageNumber(signatureData.getPageNumber());
        cellsSignOptions.setRotationAngle(signatureData.getAngle());
        cellsSignOptions.setBackgroundColor(getColor(stampData[stampData.length - 1].getBackgroundColor()));
        cellsSignOptions.setBackgroundColorCropType(StampBackgroundCropType.OuterArea);
        for (int n = 0; n < stampData.length; n++) {
            String text = "";
            for (int m = 0; m < stampData[n].getTextRepeat(); m++) {
                text = text + stampData[n].getText();
            }
            // set reduction size - required to recalculate each stamp line height and font size after stamp resizing in the UI
            int reductionSize = 0;
            // check if reduction size is between 1 and 2. for example: 1.25
            if((double)stampData[n].getHeight() / signatureData.getImageHeight() > 1 && (double)stampData[n].getHeight() / signatureData.getImageHeight() < 2) {
                reductionSize = 2;
            } else if(stampData[n].getHeight() / signatureData.getImageHeight() == 0) {
                reductionSize = 1;
            } else {
                reductionSize = stampData[n].getHeight() / signatureData.getImageHeight();
            }
            if ((n + 1) == stampData.length) {
                // draw inner horizontal line
                StampLine squareLine = new StampLine();
                squareLine.setText(text);
                squareLine.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                squareLine.setTextColor(getColor(stampData[n].getTextColor()));
                cellsSignOptions.getInnerLines().add(squareLine);
                if(stampData.length == 1){
                    StampLine line = new StampLine();
                    line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                    line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                    line.getOuterBorder().setWeight(0.5);
                    line.getInnerBorder().setColor(getColor(stampData[n].getBackgroundColor()));
                    line.getInnerBorder().setWeight(0.5);
                    line.setHeight(1);
                    cellsSignOptions.getOuterLines().add(line);
                }
            } else {
                // draw outer rounded lines
                int height = (stampData[n].getRadius() - stampData[n + 1].getRadius()) / reductionSize;
                StampLine line = new StampLine();
                line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                line.getOuterBorder().setWeight(0.5);
                line.getInnerBorder().setColor(getColor(stampData[n + 1].getStrokeColor()));
                line.getInnerBorder().setWeight(0.5);
                line.setText(text);
                line.setHeight(height);
                line.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                line.setTextColor(getColor(stampData[n].getTextColor()));
                line.setTextBottomIntent((height / 2));
                line.setTextRepeatType(StampTextRepeatType.RepeatWithTruncation);
                cellsSignOptions.getOuterLines().add(line);
            }
        }
        return cellsSignOptions;
    }

    /**
     * Add stamp signature data to slides sign options
     * @return SlidesStampSignOptions
     */
    @Override
    public SlidesStampSignOptions signSlides(){
        // setup options
        SlidesStampSignOptions slidesSignOptions = new SlidesStampSignOptions();
        slidesSignOptions.setHeight(signatureData.getImageHeight());
        slidesSignOptions.setWidth(signatureData.getImageWidth());
        slidesSignOptions.setTop(signatureData.getTop());
        slidesSignOptions.setLeft(signatureData.getLeft());
        slidesSignOptions.setDocumentPageNumber(signatureData.getPageNumber());
        slidesSignOptions.setRotationAngle(signatureData.getAngle());
        slidesSignOptions.setBackgroundColor(getColor(stampData[stampData.length - 1].getBackgroundColor()));
        slidesSignOptions.setBackgroundColorCropType(StampBackgroundCropType.OuterArea);
        for (int n = 0; n < stampData.length; n++) {
            String text = "";
            for (int m = 0; m < stampData[n].getTextRepeat(); m++) {
                text = text + stampData[n].getText();
            }
            // set reduction size - required to recalculate each stamp line height and font size after stamp resizing in the UI
            int reductionSize = 0;
            // check if reduction size is between 1 and 2. for example: 1.25
            if((double)stampData[n].getHeight() / signatureData.getImageHeight() > 1 && (double)stampData[n].getHeight() / signatureData.getImageHeight() < 2) {
                reductionSize = 2;
            } else if(stampData[n].getHeight() / signatureData.getImageHeight() == 0) {
                reductionSize = 1;
            } else {
                reductionSize = stampData[n].getHeight() / signatureData.getImageHeight();
            }
            if ((n + 1) == stampData.length) {
                // draw inner horizontal line
                StampLine squareLine = new StampLine();
                squareLine.setText(text);
                squareLine.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                squareLine.setTextColor(getColor(stampData[n].getTextColor()));
                slidesSignOptions.getInnerLines().add(squareLine);
                if(stampData.length == 1){
                    StampLine line = new StampLine();
                    line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                    line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                    line.getOuterBorder().setWeight(0.5);
                    line.getInnerBorder().setColor(getColor(stampData[n].getBackgroundColor()));
                    line.getInnerBorder().setWeight(0.5);
                    line.setHeight(1);
                    slidesSignOptions.getOuterLines().add(line);
                }
            } else {
                // draw outer rounded lines
                int height = (stampData[n].getRadius() - stampData[n + 1].getRadius()) / reductionSize;
                StampLine line = new StampLine();
                line.setBackgroundColor(getColor(stampData[n].getBackgroundColor()));
                line.getOuterBorder().setColor(getColor(stampData[n].getStrokeColor()));
                line.getOuterBorder().setWeight(0.5);
                line.getInnerBorder().setColor(getColor(stampData[n + 1].getStrokeColor()));
                line.getInnerBorder().setWeight(0.5);
                line.setText(text);
                line.setHeight(height);
                line.getFont().setFontSize(stampData[n].getFontSize() / reductionSize);
                line.setTextColor(getColor(stampData[n].getTextColor()));
                line.setTextBottomIntent((height / 2));
                line.setTextRepeatType(StampTextRepeatType.RepeatWithTruncation);
                slidesSignOptions.getOuterLines().add(line);
            }
        }
        return slidesSignOptions;
    }

}
