package com.contently.htmldiffservice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.XslFilter;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@RestController
public class HtmlDiffController {

    @RequestMapping(value = "/diff", produces = "text/html; charset=UTF-8")
    public void diff(@RequestParam(value="oldHtml") String oldHtml, @RequestParam(value="newHtml") String newHtml, OutputStream outputStream) {

        try {
            InputStream oldStream = new ByteArrayInputStream(oldHtml.getBytes());
            InputStream newStream = new ByteArrayInputStream(newHtml.getBytes());

            SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
            TransformerHandler result = tf.newTransformerHandler();
            result.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            result.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
            result.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            result.setResult(new StreamResult(outputStream));

            String[] css = new String[]{};
            XslFilter filter = new XslFilter();

            Locale locale = Locale.getDefault();
            String prefix = "diff";

            HtmlCleaner cleaner = new HtmlCleaner();
            DomTreeBuilder oldHandler = new DomTreeBuilder();

            InputSource oldSource = new InputSource(oldStream);
            oldSource.setEncoding("UTF-8");
            InputSource newSource = new InputSource(newStream);
            newSource.setEncoding("UTF-8");
            cleaner.cleanAndParse(oldSource, oldHandler);

            TextNodeComparator leftComparator = new TextNodeComparator(oldHandler, locale);

            DomTreeBuilder newHandler = new DomTreeBuilder();
            cleaner.cleanAndParse(newSource, newHandler);
            TextNodeComparator rightComparator = new TextNodeComparator(newHandler, locale);

            result.startDocument();
            HtmlSaxDiffOutput output = new HtmlSaxDiffOutput(result, prefix);

            HTMLDiffer differ = new HTMLDiffer(output);
            differ.diff(leftComparator, rightComparator);

            result.endDocument();
        } catch (Throwable e) {
            e.printStackTrace();
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            if (e instanceof SAXException) {
                ((SAXException) e).getException().printStackTrace();
            }
        }
    }
}
