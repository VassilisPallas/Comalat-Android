package org.sakaiproject.customviews.rich_textview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;

import org.sakaiproject.sakai.R;
import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vspallas on 20/02/16.
 */
public class HtmlHandler implements Html.TagHandler {

    private int mListItemCount = 0;
    private Vector<String> mListParents = new Vector<>();
    private Context context;

    public HtmlHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        if (tag.equals("ul") || tag.equals("ol")) {
            if (opening) {
                output.append("\n");
                mListParents.add(tag);
            } else mListParents.remove(tag);

            mListItemCount = 0;
        } else if (tag.equals("li") && !opening) {
            handleListTag(output);
        } else if (tag.equalsIgnoreCase("s") || tag.equalsIgnoreCase("strike")) {
            if (opening) {
                output.setSpan(new StrikethroughSpan(), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
            } else {
                Object obj = getLast(output, StrikethroughSpan.class);
                int where = output.getSpanStart(obj);

                output.setSpan(new StrikethroughSpan(), where, output.length(), 0);
            }
        } else if (tag.equals("span")) {

            if (opening)
                processAttributes(xmlReader);

            if (attributes != null && attributes.size() > 0) {

                for (int i = 0; i < attributes.size(); i++) {
                    Attribute attribute = attributes.get(i);
                    for (int j = 0; j < attribute.attributes.size(); j++) {
                        String attr = attribute.attributes.get(j);

                        Pattern rgbPatter = Pattern.compile("rgb\\(([0-9]+),([0-9]+),([0-9]+)\\)");
                        Matcher rgbMatcher = rgbPatter.matcher(attr);

                        Pattern hexPattern = Pattern.compile("#[0-9a-zA-Z]+");
                        Matcher hexMatcher = hexPattern.matcher(attr);

                        Pattern fontAwesomePattern = Pattern.compile("(fa-.+? |fa-.+)");
                        Matcher fontAwesomeMatcher = fontAwesomePattern.matcher(attr);

                        String hexColor = "#000000";

                        if (rgbMatcher.find()) {
                            String r = rgbMatcher.group(1).trim();
                            String g = rgbMatcher.group(2).trim();
                            String b = rgbMatcher.group(3).trim();
                            hexColor = String.format("#%02x%02x%02x", Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
                        } else if (hexMatcher.find()) {
                            hexColor = hexMatcher.group().trim();
                        }

                        if (opening) {
                            if (attr.contains("background-color"))
                                output.setSpan(new BackgroundColorSpan(Color.parseColor(hexColor)), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                            else if (attr.contains("color") && !attr.contains("background"))
                                output.setSpan(new ForegroundColorSpan(Color.parseColor(hexColor)), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                            else if (attr.contains("fa")) {
                                if (fontAwesomeMatcher.find()) {
                                    output.setSpan(new FontSpan("sans-serif", Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf")), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                                }
                            }
                        } else {
                            Object obj = null;
                            if (attr.contains("background-color"))
                                obj = getLast(output, BackgroundColorSpan.class);
                            else if (attr.contains("color") && !attr.contains("background"))
                                obj = getLast(output, ForegroundColorSpan.class);
                            else if (attr.contains("fa-")) {
                                obj = getLast(output, FontSpan.class);
                            }

                            int where = output.getSpanStart(obj);

                            if (attr.contains("background-color"))
                                output.setSpan(new BackgroundColorSpan(Color.parseColor(hexColor)), where, output.length(), 0);
                            else if (attr.contains("color") && !attr.contains("background"))
                                output.setSpan(new ForegroundColorSpan(Color.parseColor(hexColor)), where, output.length(), 0);
                            else if (attr.contains("fa")) {
                                if (fontAwesomeMatcher.find()) {
                                    String packageName = context.getPackageName();
                                    int resId = context.getResources().getIdentifier(fontAwesomeMatcher.group().replaceAll("-", "_").trim(), "string", packageName);
                                    if (resId != 0) {
                                        output.insert(output.length(), context.getResources().getString(resId));
                                        output.setSpan(new FontSpan("sans-serif", Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf")), where, output.length(), Spannable.SPAN_MARK_MARK);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!opening)
                    attributes.remove(0);
            }
        }
    }

    List<Attribute> attributes = new LinkedList<>();

    private void processAttributes(final XMLReader xmlReader) {
        Attribute attribute = null;
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);

            if (len > 0) {
                attribute = new Attribute();
                for (int i = 0; i < len; i++) {
                    attribute.attributes.add(data[i * 5 + 4]);
                }
            }
        } catch (Exception e) {
            Log.d("TAG", "Exception: " + e);
        }
        if (attribute != null)
            attributes.add(attribute);
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        } else {
            for (int i = objs.length; i > 0; i--) {
                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i - 1];
                }
            }
            return null;
        }
    }

    private void handleListTag(Editable output) {
        if (mListParents.lastElement().equals("ul")) {
            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.insert(start, "• ");
        } else if (mListParents.lastElement().equals("ol")) {
            mListItemCount++;
            output.append("\n");
            String[] split = output.toString().split("\n");
            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.insert(start, mListItemCount + ". ");
            output.setSpan(new LeadingMarginSpan.Standard(15 * mListParents.size()), start, output.length(), 0);
        }
    }

    private class Attribute {
        List<String> attributes = new LinkedList<>();
    }
}
