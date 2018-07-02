package com.groupdocs.ui.signature.signer;

import com.groupdocs.signature.options.SignOptions;
import com.groupdocs.ui.signature.entity.web.SignatureDataEntity;

import javax.ws.rs.NotSupportedException;
import java.awt.*;
import java.text.ParseException;

public abstract class Signer {
    protected SignatureDataEntity signatureData;

    public Signer(SignatureDataEntity signatureData){
        this.signatureData = signatureData;
    }

    /**
     *
     * @param rgbColor
     * @return
     */
    protected Color getColor(String rgbColor){
        String[] colors = rgbColor.split(",");
        int redColor = Integer.parseInt(colors[0].replaceAll("\\D+", ""));
        int greenColor = Integer.parseInt(colors[1].replaceAll("\\D+", ""));
        int blueColor = Integer.parseInt(colors[2].replaceAll("\\D+", ""));
        return new Color(redColor, greenColor, blueColor);
    }

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract SignOptions signPdf() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract SignOptions signImage() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract SignOptions signWord() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract SignOptions signCells() throws NotSupportedException, ParseException;

    /**
     *
     * @return
     * @throws NotSupportedException
     * @throws ParseException
     */
    public abstract SignOptions signSlides() throws NotSupportedException, ParseException;


}
