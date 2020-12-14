package com.restraunt.shapshuk.util;

import com.restraunt.shapshuk.core.constants.CommonAppConstants;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import static java.util.Objects.isNull;

public final class PictureEncodingUtil {

    private PictureEncodingUtil() {

    }

    public static String getPictureEncoded(Part picture) throws IOException {

        String fileName = getFileName(picture);
        if (isNull(fileName) || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        String filePath = CommonAppConstants.TMP_DIR + File.separator + fileName;
        picture.write(filePath);

        File file = new File(filePath);

        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private static String getFileName(Part part) {

        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
