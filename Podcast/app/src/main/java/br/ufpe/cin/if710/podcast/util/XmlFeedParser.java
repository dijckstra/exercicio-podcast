package br.ufpe.cin.if710.podcast.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;

public class XmlFeedParser {

    public static List<Podcast> parse(String xmlFeed) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(xmlFeed));
        xpp.nextTag();
        return readRss(xpp);
    }

    public static List<Podcast> readRss(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Podcast> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                items.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static List<Podcast> readChannel(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        List<Podcast> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static Podcast readItem(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String pubDate = null;
        String description = null;
        String downloadLink = null;

        parser.require(XmlPullParser.START_TAG, null, "item");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            switch (parser.getName()) {
                case "title":
                    title = readData(parser, "title");
                    break;
                case "link":
                    link = readData(parser, "link");
                    break;
                case "pubDate":
                    pubDate = readData(parser, "pubDate");
                    break;
                case "description":
                    description = readData(parser, "description");
                    break;
                case "enclosure":
                    downloadLink = readEnclosure(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new Podcast(title, description, pubDate, link, downloadLink);
    }

    // Processa tags do tipo <enclosure> para obter dados do episodio
    public static String readEnclosure(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "enclosure");
        String url = readAttribute(parser, "url");
        parser.nextTag();
        return url;
    }

    // Processes tags in a parametrized way
    public static String readData(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return data;
    }

    public static String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Processes tag attributes in a parametrized way
    private static String readAttribute(XmlPullParser parser, String attr) {
        String value = parser.getAttributeValue(null, attr);

        return value != null ? value : "";
    }

    public static void skip(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}