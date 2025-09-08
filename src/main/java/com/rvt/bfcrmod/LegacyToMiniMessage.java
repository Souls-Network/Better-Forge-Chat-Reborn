package com.rvt.bfcrmod;

public final class LegacyToMiniMessage {

    private LegacyToMiniMessage() {}

    public static String convert(String input) {
        if (input == null || input.isEmpty()) return input;

        StringBuilder out = new StringBuilder(input.length() + 16);
        final char[] arr = input.toCharArray();
        final int n = arr.length;

        for (int i = 0; i < n; i++) {
            char c = arr[i];

            if (c == '&' || c == '§') {
                // Escaped marker "&&" or "§§" -> literal marker
                if (i + 1 < n && arr[i + 1] == c) {
                    out.append(c);
                    i++; // skip second marker
                    continue;
                }

                // Hex color pattern: &x&R&R&G&G&B&B  or  §x§R§R§G§G§B§B
                if (i + 13 < n && isHexPattern(arr, i, c)) {
                    String hex = gatherHex(arr, i, c);
                    out.append("<#").append(hex).append(">");
                    i += 13; // consumed &x&R&R&G&G&B&B (14 chars total)
                    continue;
                }

                // Single code
                if (i + 1 < n) {
                    char code = Character.toLowerCase(arr[i + 1]);
                    String tag = mapCodeToTag(code);
                    if (tag != null) {
                        out.append(tag);
                        i++; // consume code
                        continue;
                    }
                    // Not a valid code -> keep literal marker
                    out.append(c);
                    // do not consume next; let it be appended in next loop
                    continue;
                }

                // Trailing marker with no code -> keep literal
                out.append(c);
            } else {
                out.append(c);
            }
        }

        return out.toString();
    }

    private static boolean isHexPattern(char[] arr, int i, char marker) {
        // Expect: marker, 'x', (marker, hex)*6
        // Indices: i   i+1   i+2..i+13
        if (i + 13 >= arr.length) return false;
        if (Character.toLowerCase(arr[i + 1]) != 'x') return false;
        // Verify six pairs: (marker, hex)
        for (int k = 0; k < 6; k++) {
            int idxMarker = i + 2 + (k * 2);
            int idxHex    = i + 3 + (k * 2);
            if (arr[idxMarker] != marker) return false;
            if (!isHexDigit(arr[idxHex])) return false;
        }
        return true;
    }

    private static String gatherHex(char[] arr, int i, char marker) {
        StringBuilder hex = new StringBuilder(6);
        for (int k = 0; k < 6; k++) {
            int idxHex = i + 3 + (k * 2);
            hex.append(Character.toUpperCase(arr[idxHex]));
        }
        return hex.toString();
    }

    private static boolean isHexDigit(char c) {
        c = Character.toUpperCase(c);
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
    }

    private static String mapCodeToTag(char code) {
        // Colors
        switch (code) {
            case '0': return "<black>";
            case '1': return "<dark_blue>";
            case '2': return "<dark_green>";
            case '3': return "<dark_aqua>";
            case '4': return "<dark_red>";
            case '5': return "<dark_purple>";
            case '6': return "<gold>";
            case '7': return "<gray>";
            case '8': return "<dark_gray>";
            case '9': return "<blue>";
            case 'a': return "<green>";
            case 'b': return "<aqua>";
            case 'c': return "<red>";
            case 'd': return "<light_purple>";
            case 'e': return "<yellow>";
            case 'f': return "<white>";
            // Styles
            case 'l': return "<bold>";
            case 'n': return "<underlined>";
            case 'o': return "<italic>";
            case 'm': return "<strikethrough>";
            case 'k': return "<obfuscated>";
            // Reset
            case 'r': return "<reset>";
            default:  return null;
        }
    }
}

