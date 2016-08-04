package com.tsr.pachttp.net;

import android.content.res.XmlResourceParser;
import com.tsr.pachttp.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.HashMap;

class URLConfigManager {
    private static HashMap<String, URLEntity> sUrlMap;

    public static URLEntity findURLByKey(String key) {
        if (sUrlMap == null || sUrlMap.isEmpty()) {
            fetchURLFromXml();
        }

        return sUrlMap.get(key);
    }

    private static void fetchURLFromXml() {
        sUrlMap = new HashMap<>();
        // 获取xml解释器
        XmlResourceParser parser = PacHttpClient.config.context.getResources().getXml(R.xml.request_urls);
        int eventCode;
        try {
            eventCode = parser.getEventType();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Node")) {
                            URLEntity url = new URLEntity();
                            String key = parser.getAttributeValue(null, "key");
                            url.setKey(key);
                            url.setExpires(Long.parseLong(parser
                                    .getAttributeValue(null, "expires")));
                            url.setNetType(parser.getAttributeValue(null,
                                    "netType").equals("GET") ? HttpRequest.RequestType.GET : HttpRequest.RequestType.POST);
                            url.setUrl(parser.getAttributeValue(null, "url"));
                            sUrlMap.put(key, url);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                try {
                    eventCode = parser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            parser.close();
        }
    }
}
