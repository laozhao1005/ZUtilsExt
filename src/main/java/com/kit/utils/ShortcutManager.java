package com.kit.utils;

import android.content.ComponentName;
import android.content.res.XmlResourceParser;

import com.kit.shortcut.Extra;
import com.kit.shortcut.ZShortcutInfo;
import com.kit.utils.log.Zog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Zhao on 2017/5/31.
 */

public class ShortcutManager {


    public static ArrayList<ZShortcutInfo> getShortcuts(ComponentName componentName, XmlResourceParser xmlParser) {
        if (xmlParser == null) {
            return null;
        }

        ArrayList<ZShortcutInfo> shortcutInfos = new ArrayList<>();

        ZShortcutInfo shortcutInfo = null;

        try {
            int event = xmlParser.getEventType();
            //先获取当前解析器光标在哪
            while (event != XmlPullParser.END_DOCUMENT) {

                //如果还没到文档的结束标志，那么就继续往下处理
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        //Zog.d("xml解析开始");
                        break;
                    case XmlPullParser.START_TAG:
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        String startTag = xmlParser.getName();

                        if (StringUtils.isEmptyOrNullStr(startTag)) {
                            continue;
                        }
                        //Zog.d("标签：" + xmlParser.getName() + "开始");
                        if ("shortcut".equals(startTag)) {
                            ZShortcutInfo info = null;
                            try {
                                info = shortcutInfo != null ? shortcutInfo.clone() : null;
                            } catch (Exception e) {
                            }
                            if (info != null && !shortcutInfos.contains(info)) {
                                shortcutInfos.add(info);
                            }

                            shortcutInfo = new ZShortcutInfo();
                            shortcutInfo.setComponentName(componentName);
                        }

                        if (shortcutInfo != null) {
                            parseTag(startTag, shortcutInfo, xmlParser);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        Zog.d("Text:" + xmlParser.getText());
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = xmlParser.getName();
                        if (StringUtils.isEmptyOrNullStr(endTag)) {
                            continue;
                        }

                        switch (endTag) {
                            case "shortcut":
                                ZShortcutInfo info = null;
                                try {
                                    info = shortcutInfo != null ? shortcutInfo.clone() : null;
                                } catch (Exception e) {
                                }
                                if (info != null && !shortcutInfos.contains(info)) {
                                    shortcutInfos.add(info);
                                }
                                shortcutInfo = null;
                                break;

                            case "shortcuts":
                                return shortcutInfos;

                        }

                        break;
                    default:
                        break;
                }
                event = xmlParser.next();   //将当前解析器光标往下一步移
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static void parseTag(String tag, ZShortcutInfo shortcutInfo, XmlResourceParser xmlParser) {
        if (StringUtils.isEmptyOrNullStr(tag)) {
            return;
        }

        switch (tag) {
            case "shortcuts":
                try {
                    int attCount = xmlParser.getAttributeCount();
                    //Zog.d("shortcuts 有" + attCount + "个属性");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "shortcut":
                try {

                    int attCount = xmlParser.getAttributeCount();
                    //Zog.d("shortcut 有" + attCount + "个属性");

                    for (int i = 0; i < attCount; i++) {

                        String attrName = xmlParser.getAttributeName(i);

                        if (StringUtils.isEmptyOrNullStr(attrName)) {
                            continue;
                        }

                        switch (attrName) {
                            case "icon":
                                String icon = xmlParser.getAttributeValue(i);
                                if (icon != null && icon.startsWith("@")) {
                                    shortcutInfo.setIcon(Integer.parseInt(icon.substring(1)));
                                }
                                break;

                            case "enabled":
                                String enabled = xmlParser.getAttributeValue(i);
                                shortcutInfo.setEnabled(Boolean.parseBoolean(enabled));
                                break;

                            case "shortcutId":
                                String shortcutId = xmlParser.getAttributeValue(i);
                                shortcutInfo.setShortcutId(shortcutId);
                                break;

                            case "shortcutShortLabel":
                                String shortcutShortLabel = xmlParser.getAttributeValue(i);
                                if (shortcutShortLabel != null && shortcutShortLabel.startsWith("@")) {
                                    shortcutInfo.setShortcutShortLabel(Integer.parseInt(shortcutShortLabel.substring(1)));
                                }
                                break;

                            case "shortcutLongLabel":
                                String shortcutLongLabel = xmlParser.getAttributeValue(i);
                                if (shortcutLongLabel != null && shortcutLongLabel.startsWith("@")) {
                                    shortcutInfo.setShortcutLongLabel(Integer.parseInt(shortcutLongLabel.substring(1)));
                                }
                                break;


                            case "shortcutDisabledMessage":
                                String shortcutDisabledMessage = xmlParser.getAttributeValue(i);
                                if (shortcutDisabledMessage != null && shortcutDisabledMessage.startsWith("@")) {
                                    shortcutInfo.setShortcutDisabledMessage(Integer.parseInt(shortcutDisabledMessage.substring(1)));
                                }
                                break;

                            default:
                                break;

                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "extra":
                Extra extra = new Extra();

                try {

                    int attCount = xmlParser.getAttributeCount();
                    for (int i = 0; i < attCount; i++) {

                        String attrName = xmlParser.getAttributeName(i);

                        if (StringUtils.isEmptyOrNullStr(attrName)) {
                            continue;
                        }

                        switch (attrName) {
                            case "name":
                                String name = xmlParser.getAttributeValue(i);
                                extra.setName(name);
                                break;

                            case "value":
                                String value = xmlParser.getAttributeValue(i);
                                extra.setValue(value);
                                break;

                            default:
                                break;
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                shortcutInfo.putExtra(extra);
                break;

            case "intent":
                try {
                    int attCount = xmlParser.getAttributeCount();
                    //Zog.d("intent 有" + attCount + "个属性");

                    for (int i = 0; i < attCount; i++) {

                        String attrName = xmlParser.getAttributeName(i);
                        //Zog.d("intent 属性:" + attrName);
                        if (StringUtils.isEmptyOrNullStr(attrName)) {
                            continue;
                        }

                        switch (attrName) {
                            case "action":
                                String action = xmlParser.getAttributeValue(i);
                                shortcutInfo.setAction(action);
                                break;

                            case "targetClass":
                                String targetClass = xmlParser.getAttributeValue(i);
                                shortcutInfo.setTargetClass(targetClass);
                                break;

                            case "targetPackage":
                                String targetPackage = xmlParser.getAttributeValue(i);
                                shortcutInfo.setTargetPackage(targetPackage);
                                break;

                            case "data":
                                int attCountData = xmlParser.getAttributeCount();
                                //Zog.d("data 属性:" + xmlParser.getAttributeName(i));
                                //Zog.d("data 值:" + xmlParser.getAttributeValue(i));
                                String data = xmlParser.getAttributeValue(i);
                                shortcutInfo.setData(data);
                                break;

                            case "intent-filter":
                                //Zog.e("intent-filter");
                                int attCountIntentFilter = xmlParser.getAttributeCount();
                                //Zog.d("intent-filter 有" + attCountIntentFilter + "个属性");
                                break;

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "categories":
                try {
                    int attCountCategories = xmlParser.getAttributeCount();
                    //Zog.d("categories 有" + attCountCategories + "个属性");

                    if (attCountCategories > 0) {

                        Set<String> cats = new LinkedHashSet<>();
                        for (int j = 0; j < attCountCategories; j++) {
                            //Zog.d("categories 属性：" + xmlParser.getAttributeName(0)
//                                    + ": " + xmlParser.getAttributeValue(0));
                            String cat = xmlParser.getAttributeValue(j);
                            if (StringUtils.isEmptyOrNullStr(cat)) {
                                continue;
                            }

                            cats.add(cat);
                        }
                        shortcutInfo.setCategories(cats);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }


    }
}
