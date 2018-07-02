package com.groupdocs.ui.signature.signer;

import com.groupdocs.signature.options.SignOptions;
import com.groupdocs.signature.options.digitalsignature.CellsSignDigitalOptions;
import com.groupdocs.signature.options.digitalsignature.PdfSignDigitalOptions;
import com.groupdocs.signature.options.digitalsignature.WordsSignDigitalOptions;
import com.groupdocs.ui.signature.domain.wrapper.SignatureDataWrapper;

import javax.ws.rs.NotSupportedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * StampSigner
 * Signs documents with the stamp signature
 * @author Aspose Pty Ltd
 */
public class DigitalSigner extends Signer{
    private String password;

    public DigitalSigner(SignatureDataWrapper signatureData, String password){
        super(signatureData);
        this.password = password;
    }

    @Override
    public PdfSignDigitalOptions signPdf() throws ParseException {
        // initiate date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yy");
        // setup digital signature options
        PdfSignDigitalOptions pdfSignOptions = new PdfSignDigitalOptions(signatureData.getSignatureGuid());
        pdfSignOptions.setReason(signatureData.getReason());
        pdfSignOptions.setContact(signatureData.getContact());
        pdfSignOptions.setLocation(signatureData.getAddress());
        pdfSignOptions.setPassword(password);
        pdfSignOptions.setSignAllPages(true);
        if(signatureData.getDate() != null && !signatureData.getDate().isEmpty()) {
            pdfSignOptions.getSignature().setSignTime(formatter.parse(signatureData.getDate()));
        }
        return pdfSignOptions;
    }

    @Override
    public SignOptions signImage() throws NotSupportedException {
        throw new NotSupportedException("This file type is not supported");
    }

    @Override
    public WordsSignDigitalOptions signWord() throws ParseException {
        // initiate date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yy");
        // setup digital signature options
        WordsSignDigitalOptions wordsSignOptions = new WordsSignDigitalOptions(signatureData.getSignatureGuid());
        wordsSignOptions.getSignature().setComments(signatureData.getSignatureComment());
        if(signatureData.getDate() != null && !signatureData.getDate().isEmpty()) {
            wordsSignOptions.getSignature().setSignTime(formatter.parse(signatureData.getDate()));
        }
        wordsSignOptions.setPassword(password);
        wordsSignOptions.setSignAllPages(true);
        return wordsSignOptions;
    }

    @Override
    public CellsSignDigitalOptions signCells() throws ParseException {
        // initiate date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yy");
        CellsSignDigitalOptions cellsSignOptions = new CellsSignDigitalOptions(signatureData.getSignatureGuid());
        cellsSignOptions.getSignature().setComments(signatureData.getSignatureComment());
        if(signatureData.getDate() != null && !signatureData.getDate().isEmpty()) {
            cellsSignOptions.getSignature().setSignTime(formatter.parse(signatureData.getDate()));
        }
        cellsSignOptions.setPassword(password);
        cellsSignOptions.setSignAllPages(true);
        return cellsSignOptions;
    }

    @Override
    public SignOptions signSlides() throws NotSupportedException, ParseException {
        throw new NotSupportedException("This file type is not supported");
    }
}
