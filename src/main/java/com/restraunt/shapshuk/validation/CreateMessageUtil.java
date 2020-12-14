package com.restraunt.shapshuk.validation;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CreateMessageUtil {

    private static final Logger LOGGER = Logger.getLogger(CreateMessageUtil.class.getName());

    private CreateMessageUtil() {

    }

    public static String createUrlMessage(List<BrokenField> brokenFields) {

        StringBuilder message = new StringBuilder();

        try {
            for (int i = 0; i < brokenFields.size(); i++) {

                String utf8 = StandardCharsets.UTF_8.name();
                if (i == brokenFields.size() - 1) {

                    message
                            .append("error")
                            .append("=")
                            .append(URLEncoder.encode(Arrays.toString(brokenFields.get(i).getArgs()), utf8));
                    break;
                }
                message
                        .append("error")
                        .append("=")
                        .append(URLEncoder.encode(Arrays.toString(brokenFields.get(i).getArgs()), utf8))
                        .append("&");
            }
        } catch (UnsupportedEncodingException e) {

            LOGGER.error(e.getMessage());
            return e.getMessage();
        }

        return new String(message);
    }

    public static List<String> createPageMessageList(List<BrokenField> brokenFields) {

        List<String> messages = new ArrayList<>();

        for (int i = 0; i < brokenFields.size(); i++) {

            if (i == brokenFields.size() - 1) {
                messages.add(Arrays.toString(brokenFields.get(i).getArgs()));
                break;
            }
            messages.add(Arrays.toString(brokenFields.get(i).getArgs()));
        }

        return messages;
    }
}
