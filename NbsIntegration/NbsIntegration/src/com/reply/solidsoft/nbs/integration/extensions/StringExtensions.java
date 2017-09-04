/**
 * -----------------------------------------------------------------------------
 * File=StringExtensions.java
 * Company=Solidsoft Reply
 * Copyright © 2007 - 2017 Tangible Software Solutions Inc.
 * This class can be used by anyone provided that the copyright notice remains intact.
 *
 * String extensions, based on DotNet functionality.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions;

/**
 * String extensions, based on DotNet functionality.
 */
public final class StringExtensions {

    /**
     * Retrieves a substring from this instance. The substring starts at a
     * specified character position and has a specified length.
     *
     * @param string The string to be searched.
     * @param startIndex The zero-based starting character position of a
     * substring in this instance.
     * @param length The number of characters in the substring.
     * @return A string that is equivalent to the substring of length length
     * that begins at startIndex in this instance, or Empty if startIndex is
     * equal to the length of this instance and length is zero.
     */
    public static String substring(String string, int startIndex, int length) {
        if (length < 0) {
            throw new IndexOutOfBoundsException("Parameter length cannot be negative.");
        }

        return string.substring(startIndex, startIndex + length);
    }

    /**
     * Indicates whether the specified string is null or an Empty string.
     *
     * @param string The string to test.
     * @return True if the value parameter is null or an empty string ("");
     * otherwise, false.
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * Indicates whether a specified string is null, empty, or consists only of
     * white-space characters.
     *
     * @param string The string to test.
     * @return True if the value parameter is null or String.Empty, or if value
     * consists exclusively of white-space characters.
     */
    public static boolean isNullOrWhiteSpace(String string) {
        if (string == null) {
            return true;
        }

        for (int index = 0; index < string.length(); index++) {
            if (!Character.isWhitespace(string.charAt(index))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Concatenates all the elements of a string array, using the specified
     * separator between each element.
     *
     * @param separator The string to use as a separator. separator is included
     * in the returned string only if value has more than one element.
     * @param value An array that contains the elements to concatenate.
     * @return A string that consists of the elements in value delimited by the
     * separator string. If value is an empty array, the method returns an empty
     * string.
     */
    public static String join(String separator, String[] value) {
        if (value == null) {
            return null;
        } else {
            return join(separator, value, 0, value.length);
        }
    }

    /**
     * Concatenates the specified elements of a string array, using the
     * specified separator between each element.
     *
     * @param separator The string to use as a separator. separator is included
     * in the returned string only if value has more than one element.
     * @param value An array that contains the elements to concatenate.
     * @param startIndex The first element in value to use.
     * @param count The number of elements of value to use.
     * @return A string that consists of the strings in value delimited by the
     * separator string.
     * <p>
     * -or-
     * <p>
     * An empty string if count is zero, value has no elements, or separator and
     * all the elements of value are an empty string.
     */
    public static String join(String separator, String[] value, int startIndex, int count) {
        String result = "";

        if (value == null) {
            return null;
        }

        for (int index = startIndex; index < value.length && index - startIndex < count; index++) {
            if (separator != null && index > startIndex) {
                result += separator;
            }

            if (value[index] != null) {
                result += value[index];
            }
        }

        return result;
    }

    /**
     * Returns a new string in which all the characters in the current instance,
     * beginning at a specified position and continuing through the last
     * position, have been deleted.
     *
     * @param string The string to be searched.
     * @param startIndex The zero-based position to begin deleting characters.
     * @return A new string that is equivalent to this string except for the
     * removed characters.
     */
    public static String remove(String string, int startIndex) {
        return string.substring(0, startIndex);
    }

    /**
     * Returns a new string in which a specified number of characters in the
     * current instance beginning at a specified position have been deleted.
     *
     * @param string The string to be searched.
     * @param startIndex The zero-based position to begin deleting characters.
     * @param count The number of characters to delete.
     * @return A new string that is equivalent to this instance except for the
     * removed characters.
     */
    public static String remove(String string, int startIndex, int count) {
        return string.substring(0, startIndex) + string.substring(startIndex + count);
    }

    /**
     * Removes all trailing occurrences of a set of characters specified in an
     * array from the current String object.
     *
     * @param string The string to be searched.
     * @param charsToTrim An array of Unicode characters to remove, or null.
     * @return The string that remains after all occurrences of the characters
     * in the trimChars parameter are removed from the end of the current
     * string. If trimChars is null or an empty array, Unicode white-space
     * characters are removed instead. If no characters can be trimmed from the
     * current instance, the method returns the current instance unchanged.
     */
    public static String trimEnd(String string, Character... charsToTrim) {
        if (string == null || charsToTrim == null) {
            return string;
        }

        int lengthToKeep = string.length();
        for (int index = string.length() - 1; index >= 0; index--) {
            boolean removeChar = false;
            if (charsToTrim.length == 0) {
                if (Character.isWhitespace(string.charAt(index))) {
                    lengthToKeep = index;
                    removeChar = true;
                }
            } else {
                for (Character charsToTrim1 : charsToTrim) {
                    if (string.charAt(index) == charsToTrim1) {
                        lengthToKeep = index;
                        removeChar = true;
                        break;
                    }
                }
            }
            if (!removeChar) {
                break;
            }
        }
        return string.substring(0, lengthToKeep);
    }

    /**
     * Removes all leading occurrences of a set of characters specified in an
     * array from the current String object.
     *
     * @param string The string to be searched.
     * @param charsToTrim An array of Unicode characters to remove, or null.
     * @return The string that remains after all occurrences of characters in
     * the trimChars parameter are removed from the start of the current string.
     * If trimChars is null or an empty array, white-space characters are
     * removed instead.
     */
    public static String trimStart(String string, Character... charsToTrim) {
        if (string == null || charsToTrim == null) {
            return string;
        }

        int startingIndex = 0;
        for (int index = 0; index < string.length(); index++) {
            boolean removeChar = false;
            if (charsToTrim.length == 0) {
                if (Character.isWhitespace(string.charAt(index))) {
                    startingIndex = index + 1;
                    removeChar = true;
                }
            } else {
                for (Character charsToTrim1 : charsToTrim) {
                    if (string.charAt(index) == charsToTrim1) {
                        startingIndex = index + 1;
                        removeChar = true;
                        break;
                    }
                }
            }
            if (!removeChar) {
                break;
            }
        }
        return string.substring(startingIndex);
    }

    /**
     * Removes all leading and trailing occurrences of a set of characters
     * specified in an array from the current String object.
     *
     * @param string The string to be searched.
     * @param charsToTrim An array of Unicode characters to remove, or null.
     * @return The string that remains after all occurrences of the characters
     * in the trimChars parameter are removed from the start and end of the
     * current string. If trimChars is null or an empty array, white-space
     * characters are removed instead. If no characters can be trimmed from the
     * current instance, the method returns the current instance unchanged.
     */
    public static String trim(String string, Character... charsToTrim) {
        return trimEnd(trimStart(string, charsToTrim), charsToTrim);
    }

    /**
     * This method is used for string equality comparisons when the option 'Use
     * helper 'stringsEqual' method to handle null strings' is selected (The
     * Java String 'equals' method can't be called on a null instance).
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return True, if the strings are equal; otherwise false.
     */
    public static boolean stringsEqual(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        } else {
            return s1 != null && s1.equals(s2);
        }
    }

    /**
     * Returns a new string that left-aligns the characters in this string by
     * padding them with spaces on the right, for a specified total length.
     *
     * @param string The string to be searched.
     * @param totalWidth The number of characters in the resulting string, equal
     * to the number of original characters plus any additional padding
     * characters.
     * @return A new string that is equivalent to this instance, but
     * left-aligned and padded on the right with as many spaces as needed to
     * create a length of totalWidth. However, if totalWidth is less than the
     * length of this instance, the method returns a reference to the existing
     * instance. If totalWidth is equal to the length of this instance, the
     * method returns a new string that is identical to this instance.
     */
    public static String padRight(String string, int totalWidth) {
        return padRight(string, totalWidth, ' ');
    }

    /**
     * Returns a new string that left-aligns the characters in this string by
     * padding them on the right with a specified Unicode character, for a
     * specified total length.
     *
     * @param string The string to be searched.
     * @param totalWidth The number of characters in the resulting string, equal
     * to the number of original characters plus any additional padding
     * characters.
     * @param paddingChar A Unicode padding character.
     * @return A new string that is equivalent to this instance, but
     * left-aligned and padded on the right with as many paddingChar characters
     * as needed to create a length of totalWidth. However, if totalWidth is
     * less than the length of this instance, the method returns a reference to
     * the existing instance. If totalWidth is equal to the length of this
     * instance, the method returns a new string that is identical to this
     * instance.
     */
    public static String padRight(String string, int totalWidth, char paddingChar) {
        StringBuilder sb = new StringBuilder(string);

        while (sb.length() < totalWidth) {
            sb.append(paddingChar);
        }

        return sb.toString();
    }

    /**
     * Returns a new string that right-aligns the characters in this instance by
     * padding them with spaces on the left, for a specified total length.
     *
     * @param string The string to be searched.
     * @param totalWidth The number of characters in the resulting string, equal
     * to the number of original characters plus any additional padding
     * characters.
     * @return A new string that is equivalent to this instance, but
     * right-aligned and padded on the left with as many spaces as needed to
     * create a length of totalWidth. However, if totalWidth is less than the
     * length of this instance, the method returns a reference to the existing
     * instance. If totalWidth is equal to the length of this instance, the
     * method returns a new string that is identical to this instance
     */
    public static String padLeft(String string, int totalWidth) {
        return padLeft(string, totalWidth, ' ');
    }

    /**
     * Returns a new string that right-aligns the characters in this instance by
     * padding them on the left with a specified Unicode character, for a
     * specified total length.
     *
     * @param string The string to be searched.
     * @param totalWidth The number of characters in the resulting string, equal
     * to the number of original characters plus any additional padding
     * characters.
     * @param paddingChar A Unicode padding character.
     * @return A new string that is equivalent to this instance, but
     * right-aligned and padded on the left with as many paddingChar characters
     * as needed to create a length of totalWidth. However, if totalWidth is
     * less than the length of this instance, the method returns a reference to
     * the existing instance. If totalWidth is equal to the length of this
     * instance, the method returns a new string that is identical to this
     * instance.
     */
    public static String padLeft(String string, int totalWidth, char paddingChar) {
        StringBuilder sb = new StringBuilder("");

        while (sb.length() + string.length() < totalWidth) {
            sb.append(paddingChar);
        }

        sb.append(string);
        return sb.toString();
    }

    /**
     * Reports the zero-based index position of the last occurrence of the
     * specified Unicode character in a substring within this instance. The
     * search starts at a specified character position and proceeds backward
     * toward the beginning of the string for a specified number of character
     * positions.
     *
     * @param string The string to be searched.
     * @param value The Unicode character to seek.
     * @param startIndex The starting position of the search. The search
     * proceeds from startIndex toward the beginning of this instance.
     * @param count The number of character positions to examine.
     * @return The zero-based index position of value if that character is
     * found, or -1 if it is not found or if the current instance equals an
     * empty string.Empty.
     */
    public static int lastIndexOf(String string, char value, int startIndex, int count) {
        int leftMost = startIndex + 1 - count;
        int rightMost = startIndex + 1;
        String substring = string.substring(leftMost, rightMost);
        int lastIndexInSubstring = substring.lastIndexOf(value);
        if (lastIndexInSubstring < 0) {
            return -1;
        } else {
            return lastIndexInSubstring + leftMost;
        }
    }

    /**
     * Reports the zero-based index position of the last occurrence of a
     * specified string within this instance. The search starts at a specified
     * character position and proceeds backward toward the beginning of the
     * string for a specified number of character positions.
     *
     * @param string The string to be searched.
     * @param value The string to seek.
     * @param startIndex The search starting position. The search proceeds from
     * startIndex toward the beginning of this instance.
     * @param count The number of character positions to examine.
     * @return The zero-based starting index position of value if that string is
     * found, or -1 if it is not found or if the current instance equals
     * String.Empty. If value is Empty, the return value is the smaller of
     * startIndex and the last index position in this instance.
     */
    public static int lastIndexOf(String string, String value, int startIndex, int count) {
        int leftMost = startIndex + 1 - count;
        int rightMost = startIndex + 1;
        String substring = string.substring(leftMost, rightMost);
        int lastIndexInSubstring = substring.lastIndexOf(value);
        if (lastIndexInSubstring < 0) {
            return -1;
        } else {
            return lastIndexInSubstring + leftMost;
        }
    }

    /**
     * Reports the zero-based index of the first occurrence in this instance of
     * any character in a specified array of Unicode characters.
     *
     * @param string The string to be searched.
     * @param anyOf A Unicode character array containing one or more characters
     * to seek.
     * @return The zero-based index position of the first occurrence in this
     * instance where any character in anyOf was found; -1 if no character in
     * anyOf was found.
     */
    public static int indexOfAny(String string, char[] anyOf) {
        int lowestIndex = -1;
        for (char c : anyOf) {
            int index = string.indexOf(c);
            if (index > -1) {
                if (lowestIndex == -1 || index < lowestIndex) {
                    lowestIndex = index;

                    if (index == 0) {
                        break;
                    }
                }
            }
        }

        return lowestIndex;
    }

    /**
     * Reports the zero-based index of the first occurrence in this instance of
     * any character in a specified array of Unicode characters. The search
     * starts at a specified character position.
     *
     * @param string The string to be searched.
     * @param anyOf A Unicode character array containing one or more characters
     * to seek.
     * @param startIndex The search starting position.
     * @return The zero-based index position of the first occurrence in this
     * instance where any character in anyOf was found; -1 if no character in
     * anyOf was found.
     */
    public static int indexOfAny(String string, char[] anyOf, int startIndex) {
        int indexInSubstring = indexOfAny(string.substring(startIndex), anyOf);
        if (indexInSubstring == -1) {
            return -1;
        } else {
            return indexInSubstring + startIndex;
        }
    }

    /**
     * Reports the zero-based index of the first occurrence in this instance of
     * any character in a specified array of Unicode characters. The search
     * starts at a specified character position and examines a specified number
     * of character positions.
     *
     * @param string The string to be searched.
     * @param anyOf A Unicode character array containing one or more characters
     * to seek.
     * @param startIndex The search starting position.
     * @param count The number of character positions to examine.
     * @return The zero-based index position of the first occurrence in this
     * instance where any character in anyOf was found; -1 if no character in
     * anyOf was found.
     */
    public static int indexOfAny(String string, char[] anyOf, int startIndex, int count) {
        int endIndex = startIndex + count;
        int indexInSubstring = indexOfAny(string.substring(startIndex, endIndex), anyOf);
        if (indexInSubstring == -1) {
            return -1;
        } else {
            return indexInSubstring + startIndex;
        }
    }

    /**
     * Reports the zero-based index position of the last occurrence in this
     * instance of one or more characters specified in a Unicode array.
     *
     * @param string The string to be searched.
     * @param anyOf A Unicode character array containing one or more characters
     * to seek.
     * @return The index position of the last occurrence in this instance where
     * any character in anyOf was found; -1 if no character in anyOf was found.
     */
    public static int lastIndexOfAny(String string, char[] anyOf) {
        int highestIndex = -1;
        for (char c : anyOf) {
            int index = string.lastIndexOf(c);
            if (index > highestIndex) {
                highestIndex = index;

                if (index == string.length() - 1) {
                    break;
                }
            }
        }

        return highestIndex;
    }

    /**
     * Reports the zero-based index position of the last occurrence in this
     * instance of one or more characters specified in a Unicode array. The
     * search starts at a specified character position and proceeds backward
     * toward the beginning of the string.
     *
     * @param string The string to be searched.
     * @param anyOf A Unicode character array containing one or more characters
     * to seek.
     * @param startIndex The search starting position. The search proceeds from
     * startIndex toward the beginning of this instance.
     * @return The index position of the last occurrence in this instance where
     * any character in anyOf was found; -1 if no character in anyOf was found
     * or if the current instance equals an empty string.
     */
    public static int lastIndexOfAny(String string, char[] anyOf, int startIndex) {
        String substring = string.substring(0, startIndex + 1);
        int lastIndexInSubstring = lastIndexOfAny(substring, anyOf);
        if (lastIndexInSubstring < 0) {
            return -1;
        } else {
            return lastIndexInSubstring;
        }
    }

    /**
     * Reports the zero-based index position of the last occurrence in this
     * instance of one or more characters specified in a Unicode array. The
     * search starts at a specified character position and proceeds backward
     * toward the beginning of the string for a specified number of character
     * positions.
     *
     * @param string The string to be searched.
     * @param anyOf A Unicode character array containing one or more characters
     * to seek.
     * @param startIndex The search starting position. The search proceeds from
     * startIndex toward the beginning of this instance.
     * @param count The number of character positions to examine.
     * @return The index position of the last occurrence in this instance where
     * any character in anyOf was found; -1 if no character in anyOf was found
     * or if the current instance equals an empty string.
     */
    public static int lastIndexOfAny(String string, char[] anyOf, int startIndex, int count) {
        int leftMost = startIndex + 1 - count;
        int rightMost = startIndex + 1;
        String substring = string.substring(leftMost, rightMost);
        int lastIndexInSubstring = lastIndexOfAny(substring, anyOf);
        if (lastIndexInSubstring < 0) {
            return -1;
        } else {
            return lastIndexInSubstring + leftMost;
        }
    }

}
