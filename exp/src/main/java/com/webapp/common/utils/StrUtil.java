package com.webapp.common.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StrUtil {

    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String SPACE = " ";
    public static final String UNDERSCORE = "_";
    public static final String EMPTY = "";
    public static final String LF = "\n";
    public static final String CR = "\r";
    public static final String SEMICOLON = ";";
    public static final String COLON = ":";
    public static final String SLASH = "/";
    public static final String PERCENT = "%";
    public static final String HASH = "#";
    public static final String EQUAL_SIGN = "=";
    public static final String LEFT_BRACKET = "[";
    public static final String RIGHT_BRACKET = "]";
    public static final String N_A = "N/A";
    public static final String ELLIPSIS = "...";
    public static final String INFINITY = "∞";
    private static final String FORBIDDEN_XML_10_CHARS_PATTERN = "[^"
                                                                 + "\u0009\r\n"
                                                                 + "\u0020-\uD7FF"
                                                                 + "\uE000-\uFFFD"
                                                                 + "\ud800\udc00-\udbff\udfff"
                                                                 + "]";

    public static String replaceNaAsEmpty(String str) {
        return N_A.equalsIgnoreCase(str) ? StrUtil.EMPTY : str;
    }

    public static String removeWhitespaceChars(String str) {
        return str.replaceAll("\\s+", StrUtil.EMPTY);
    }

    public static String replaceLFtoSpace(String str) {
        return str.replaceAll(LF, StrUtil.SPACE);
    }

    public static String removeForbiddenXmlChars(String str) {
        return str.replaceAll(FORBIDDEN_XML_10_CHARS_PATTERN, StrUtil.EMPTY);
    }

    public static String getDigits(String str) {
        return str.replaceAll("[^0-9]", StrUtil.EMPTY);
    }

    public static int getNumberFromStr(String str) {
        return Integer.parseInt(getDigits(str));
    }

    public static double getDoubleFromStr(String str) {
        return Double.parseDouble(getDigitsAndDots(str));
    }

    public static String getDigitsAndDots(String str) {
        return str.replaceAll("[^0-9.]", StrUtil.EMPTY);
    }

    public static String getChars(String str) {
        return str.replaceAll("[^a-z,A-Z]", StrUtil.EMPTY);
    }

    public static String getFirstMatch(String regex, String text) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        return matcher.find() ? matcher.group(0) : null;
    }

    public static List<String> getAllMatches(String regex, String text) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group().trim());
        }
        return list;
    }

    public static String trimZeros(String text) {
        return text.replaceAll("\\.00", EMPTY);
    }

    public static Double convertToDouble(String value) {
        return Optional.ofNullable(StrUtil.getFirstMatch("\\d+(\\.\\d+)", value)).map(Double::valueOf).orElse(0.0);
    }

    public static String deleteLastSymbolIfPResent(String result, String symbol) {
        if (result.endsWith(symbol)) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}