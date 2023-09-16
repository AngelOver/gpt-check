//package com.example.chatcheck.controller.api.dto;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.safety.Whitelist;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Map;
//import java.util.TreeMap;
//
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.nodes.Entities.EscapeMode;
//import org.jsoup.nodes.Node;
//import org.jsoup.nodes.TextNode;
//import org.jsoup.parser.Tag;
//import org.jsoup.safety.Cleaner;
//import org.jsoup.safety.Whitelist;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//    /**
//     * Convert Html to MarkDown
//     */
//    public class HTML2Md {
//        private static int indentation = -1;
//        private static boolean orderedList = false;
//
//        public static String convert(String theHTML, String baseURL) {
//            Document doc = Jsoup.parse(theHTML, baseURL);
//
//            return parseDocument(doc);
//        }
//
//        public static String convert(URL url, int timeoutMillis) throws IOException {
//            Document doc = Jsoup.parse(url, timeoutMillis);
//
//            return parseDocument(doc);
//        }
//
//        public static String convertHtml(String html, String charset) throws IOException {
//            Document doc = Jsoup.parse(html, charset);
//            return parseDocument(doc);
//        }
//
//        public static String convertFile(File file, String charset) throws IOException {
//            Document doc = Jsoup.parse(file, charset);
//
//            return parseDocument(doc);
//        }
//
//
//
//
//        private static String parseDocument(Document dirtyDoc) {
//            indentation = -1;
//
//            String title = dirtyDoc.title();
//
//            Whitelist whitelist = Whitelist.relaxed();
//            Cleaner cleaner = new Cleaner(whitelist);
//
//            Document doc = cleaner.clean(dirtyDoc);
//            doc.outputSettings().escapeMode(EscapeMode.xhtml);
//
//            if (!title.trim().equals("")) {
//                return "# " + title + "\n\n" + getTextContent(doc);
//            } else {
//                return getTextContent(doc);
//            }
//        }
//
//
//
//
//
//
//        private static void p(Element element, ArrayList<MDLine> lines) {
//            MDLine line = getLastLine(lines);
//            if (!line.getContent().trim().equals(""))
//                lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, getTextContent(element)));
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            if (!line.getContent().trim().equals(""))
//                lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//        }
//
//        private static void br(ArrayList<MDLine> lines) {
//            MDLine line = getLastLine(lines);
//            if (!line.getContent().trim().equals(""))
//                lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//        }
//
//        private static void h(Element element, ArrayList<MDLine> lines) {
//            MDLine line = getLastLine(lines);
//            if (!line.getContent().trim().equals(""))
//                lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//
//            int level = Integer.valueOf(element.tagName().substring(1));
//            switch (level) {
//                case 1:
//                    lines.add(new MDLine(MDLine.MDLineType.Head1, 0, getTextContent(element)));
//                    break;
//                case 2:
//                    lines.add(new MDLine(MDLine.MDLineType.Head2, 0, getTextContent(element)));
//                    break;
//                default:
//                    lines.add(new MDLine(MDLine.MDLineType.Head3, 0, getTextContent(element)));
//                    break;
//            }
//
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//        }
//
//        private static void strong(Element element, ArrayList<MDLine> lines) {
//            MDLine line = getLastLine(lines);
//            line.append("**");
//            line.append(getTextContent(element));
//            line.append("**");
//        }
//
//        private static void em(Element element, ArrayList<MDLine> lines) {
//            MDLine line = getLastLine(lines);
//            line.append("*");
//            line.append(getTextContent(element));
//            line.append("*");
//        }
//
//        private static void hr(ArrayList<MDLine> lines) {
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            lines.add(new MDLine(MDLine.MDLineType.HR, 0, ""));
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//        }
//
//        private static void a(Element element, ArrayList<MDLine> lines) {
//            MDLine line = getLastLine(lines);
//            line.append("[");
//            line.append(getTextContent(element));
//            line.append("]");
//            line.append("(");
//            String url = element.attr("href");
//            line.append(url);
//            String title = element.attr("title");
//            if (!title.equals("")) {
//                line.append(" \"");
//                line.append(title);
//                line.append("\"");
//            }
//            line.append(")");
//        }
//
//        private static void img(Element element, ArrayList<MDLine> lines) {
//            MDLine line = getLastLine(lines);
//
//            line.append("![");
//            String alt = element.attr("alt");
//            line.append(alt);
//            line.append("]");
//            line.append("(");
//            String url = element.attr("src");
//            line.append(url);
//            String title = element.attr("title");
//            if (!title.equals("")) {
//                line.append(" \"");
//                line.append(title);
//                line.append("\"");
//            }
//            line.append(")");
//        }
//
//        private static void code(Element element, ArrayList<MDLine> lines) {
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            MDLine line = new MDLine(MDLine.MDLineType.None, 0, "    ");
//            line.append(getTextContent(element).replace("\n", "    "));
//            lines.add(line);
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//        }
//
//        private static void ul(Element element, ArrayList<MDLine> lines) {
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            indentation++;
//            orderedList = false;
//            MDLine line = new MDLine(MDLine.MDLineType.None, 0, "");
//            line.append(getTextContent(element));
//            lines.add(line);
//            indentation--;
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//        }
//
//        private static void ol(Element element, ArrayList<MDLine> lines) {
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//            indentation++;
//            orderedList = true;
//            MDLine line = new MDLine(MDLine.MDLineType.None, 0, "");
//            line.append(getTextContent(element));
//            lines.add(line);
//            indentation--;
//            lines.add(new MDLine(MDLine.MDLineType.None, 0, ""));
//        }
//
//        private static void li(Element element, ArrayList<MDLine> lines) {
//            MDLine line;
//            if (orderedList) {
//                line = new MDLine(MDLine.MDLineType.Ordered, indentation,
//                        getTextContent(element));
//            } else {
//                line = new MDLine(MDLine.MDLineType.Unordered, indentation,
//                        getTextContent(element));
//            }
//            lines.add(line);
//        }
//    }
