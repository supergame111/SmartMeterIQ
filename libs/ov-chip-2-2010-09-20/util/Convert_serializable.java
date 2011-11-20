// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2008, 2009
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation; either version 2 of
// the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// General Public License in file COPYING in this or one of the
// parent directories for more details.
// 
// Created 18.9.08 by Hendrik
// 
// convert one APDU_Serializable into another one using to/from byte
// 
// $Id: Convert_serializable.java,v 1.13 2009-04-09 10:42:16 tews Exp $

package ds.ov2.util;

import java.io.PrintWriter;


/** 
 * Compatibility checking and
 * data conversion by (ab-)using the {@link APDU_Serializable} interface.
 * Converts single objects and arrays that are compatible according to
 * {@link APDU_Serializable#is_compatible_with is_compatible_with}.
 * Contains also the utility methods to convert to and from byte
 * arrays.
 * <P>
 *
 * This is a static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.13 $
 * @commitdate $Date: 2009-04-09 10:42:16 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Convert_serializable {

    /**
     * Static class, object creation disabled.
     */
    private Convert_serializable() {}


    /**
     * Converts {@code src} into {@code dest} on the assumption
     * that they are compatible and that especially the sizes 
     * are equal.
     *
     * @param src source
     * @param dest destination
     */
    private static void convert(APDU_Serializable src, APDU_Serializable dest) {
        assert src.size() == dest.size();
        short len = src.size();
        byte buf[] = new byte[len];
        short res = src.to_byte_array(len, (short)0, buf, (short)0);
        assert res == len +1;
        res = dest.from_byte_array(len, (short)0, buf, (short)0);
        assert res == len +1;
        return;
    }


    /**
     * Converts host type {@code src} to card type {@code dest}.
     * Note that in place of {@code to(x, y)} one cannot use
     * {@link #from from}{@code (y, x)} in general, because compatibility 
     * need not be symmetric, 
     * <a href="APDU_Serializable.html#apdu_compatibility">see compatibility 
     * checks.</a>
     *
     * @param src source, host type
     * @param dest destination, card type
     * @throws IllegalArgumentException if 
     *     {@code dest.}{@link APDU_Serializable#is_compatible_with
     *     is_compatible_with}{@code (src)} fails
     */
    public static void to(APDU_Serializable src, APDU_Serializable dest)
        throws IllegalArgumentException
    {
        if(!dest.is_compatible_with(src))
            throw new IllegalArgumentException(
                "Convert_serializable.to: src is not compatible with dest");
        convert(src, dest);
        return;
    }


    /**
     * Converts card type {@code src} to host type {@code dest}.
     * Note that in place of {@code from(x, y)} one cannot use
     * {@link #to to}{@code (y, x)} in general, because compatibility 
     * need not be symmetric, 
     * <a href="APDU_Serializable.html#apdu_compatibility">see compatibility 
     * checks.</a>
     *
     * @param dest destination, host type
     * @param src source, card type
     * @throws IllegalArgumentException if
     *     {@code src.}{@link APDU_Serializable#is_compatible_with
     *     is_compatible_with}{@code (dest)} fails
     */
    public static void from(APDU_Serializable dest, APDU_Serializable src) 
        throws IllegalArgumentException
    {
        if(!src.is_compatible_with(dest))
            throw new IllegalArgumentException(
                "Convert_serializable.from: dest is not compatible with src");
        convert(src, dest);
        return;
    }


    /**
     * Compatibility check for arrays. Checks that
     * {@code card[i].}{@link APDU_Serializable#is_compatible_with
     * is_compatible_with}{@code (host[i])} for all array elements.
     * Checks also that the arrays have the same size, of course.
     * Null references for the arrays or empty arrays are 
     * permissable, as long as the other argument has no elements. 
     *
     * @param host host type array
     * @param card card type array
     * @throws IllegalArgumentException if the compatibility check fails
     * @throws NullPointerException if one of the arrays contains a null
     *              reference
     */
    public static void check_compatibility(APDU_Serializable[] host,
                                           APDU_Serializable[] card) 
        throws IllegalArgumentException, NullPointerException
    {

        // Check array length.
        if(!( ((host == null || host.length == 0) && 
               (card == null || card.length == 0))
              ||
              (host != null && card != null && host.length == card.length)))
            throw new IllegalArgumentException(
                    "Convert_serializable.check_compatibility: " +
                    "different array sizes");
        
        if(host != null) {
            for(int i = 0; i < host.length; i++) {
                // System.out.format("XXX assert compat %d\n", i);
                if(!card[i].is_compatible_with(host[i]))
                    throw new IllegalArgumentException(
                        String.format(
                            "Convert_serializable.check_compatibility: " +
                            "index %d not compatible", i));
            }
        }

        return;
    }


    /**
     * Output serialized data of array element. Outputs the byte 
     * representation of {@code a[i]} to {@code out}. The format string
     * {@code line_start} must contain at most one integer conversion.
     * It is printed with the index before the data.
     *
     * @param out the channel to output to
     * @param line_start format string to be printed with {@code i} before 
     *          the data
     * @param a 
     * @param i 
     */
    private static void output(PrintWriter out,
                               String line_start,
                               APDU_Serializable[] a,
                               int i)
    {
        out.format(line_start, i);
        short size = a[i].size();
        byte[] data = new byte[size];
        a[i].to_byte_array(size, (short)0, data, (short)0);
        out.println(Misc_host.to_byte_hex_string(data));
    }


    /**
     * Converts a host type array into a card type array, 
     * possibly verbosely. Prints all data 
     * to {@code out} if {@code out} is not {@code null}. If {@code out} 
     * is null then {@code line_start} is not used. Otherwise 
     * it must be a format string
     * with at most one integer conversion, which is printed
     * with the current index to {@code out} before the data.
     * <P>
     * Note that in place of {@code array_to} one cannot use
     * {@link #array_from array_from} in general, because compatibility 
     * need not be symmetric, 
     * <a href="APDU_Serializable.html#apdu_compatibility">see compatibility 
     * checks.</a>
     *
     * @param out if not null, channel to print all serialized data to
     * @param line_start not used if {@code out} is {@code null}, otherwise
     *          a format string to be printed with the array index before
     *          the serialized data
     * @param host source array of host objects
     * @param card destination array of card objects
     * @throws IllegalArgumentException if the compatibility check fails
     * @throws NullPointerException if one of the arrays contains a null
     *              reference
     */
    public static void array_to(PrintWriter out, 
                                String line_start,
                                APDU_Serializable[] host, 
                                APDU_Serializable[] card) 
        throws IllegalArgumentException, NullPointerException
    {
        
        check_compatibility(host, card);
        if(host != null) {
            for(int i = 0; i < host.length; i++) {
                if(out != null)
                    output(out, line_start, host, i);
                to(host[i], card[i]);
            }
            return;
        }
    }


    /**
     * Converts a card type array into a host type array, 
     * possibly verbosely. Prints all data 
     * to {@code out} if {@code out} is not {@code null}. If {@code out} 
     * is null then {@code line_start} is not used. Otherwise 
     * it must be a format string
     * with at most one integer conversion, which is printed
     * with the current index to {@code out} before the data.
     * <P>
     * Note that in place of {@code array_from} one cannot use
     * {@link #array_to array_to} in general, because compatibility 
     * need not be symmetric, 
     * <a href="APDU_Serializable.html#apdu_compatibility">see compatibility 
     * checks.</a>
     *
     * @param out if not null, channel to print all serialized data to
     * @param line_start not used if {@code out} is {@code null}, otherwise
     *          a format string to be printed with the array index before
     *          the serialized data
     * @param host destination array of host objects
     * @param card source array of card objects
     * @throws IllegalArgumentException if the compatibility check fails
     * @throws NullPointerException if one of the arrays contains a null
     *              reference
     */
    public static void array_from(PrintWriter out,
                                  String line_start,
                                  APDU_Serializable[] host,
                                  APDU_Serializable[] card) 
        throws IllegalArgumentException, NullPointerException
    {
        check_compatibility(host, card);
        if(host != null) {
            for(int i = 0; i < host.length; i++) {
                if(out != null)
                    output(out, line_start, card, i);
                from(host[i], card[i]);
            }
            return;
        }
    }


    /**
     * Serialize an array of {@link APDU_Serializable APDU_Serializable's}
     * into a byte array.
     * <P>
     * Asserts that the data of {@code s} fits into {@code buf} at 
     * {@code index}.
     *
     * @param s the array to serialize, can be null, if non-null all
     *          references in the array must be non-null too
     * @param buf the byte array to serialize into
     * @param index starting index in {@code buf}
     * @return the first unused index off {@code buf}; this could equal
     *          {@code buf.length} (if the complete buffer is used) or
     *          the original {@code index} argument (if {@code s} is null)
     * @throws NullPointerException if one of the references in the 
     *      array {@code s} is null
     */
    public static int array_to_bytes(APDU_Serializable[] s,
                                     byte[] buf,
                                     int index) 
    {
        assert Misc.length_of_serializable_array(s) + index <= buf.length;
        assert 
            Misc.length_of_serializable_array(s) + index <= Short.MAX_VALUE;
        if(s != null) {
            for(int i = 0; i < s.length; i++) {
                short size = s[i].size();
                int res = s[i].to_byte_array(size, (short)0, 
                                             buf, (short)index);
                assert res == size +1;
                index += size;
            }
        }
        return index;
    }


    /**
     * Serialize an array of {@link APDU_Serializable APDU_Serializable's}
     * into a byte array.
     *
     * @param s the array to serialize, can be null, if non-null all
     *          references in the array must be non-null too
     * @return a fresh byte array with the serialized data
     * @throws NullPointerException if one of the references in the 
     *      array {@code s} is null
     */
    public static byte[] array_to_bytes(APDU_Serializable[] s) {
        int size = Misc.length_of_serializable_array(s);
        byte[] buf = new byte[size];
        int res = array_to_bytes(s, buf, 0);
        assert res == size;
        return buf;
    }


    /**
     * Deserializes an array of {@link APDU_Serializable 
     * APDU_Serializable's}. The data is taken starting from index 
     * {@code index}.
     *
     * @param buf data to deserialize
     * @param index starting index of data
     * @param s {@link APDU_Serializable APDU_Serializables} to deserialize,
     *           can be null, if non-null all
     *          references in the array must be non-null too
     * @return the first unused index in {@code buf}; this could equal
     *          {@code buf.length} (if the complete buffer is used) or
     *          the original {@code index} argument (if {@code s} is null)
     * @throws NullPointerException if one of the references in {@code s} 
     *          is null
     */
    public static int array_from_bytes(byte[] buf, int index,
                                       APDU_Serializable[] s) 
    {
        assert Misc.length_of_serializable_array(s) + index <= buf.length;
        assert 
            Misc.length_of_serializable_array(s) + index <= Short.MAX_VALUE;
        if(s != null) {
            for(int i = 0; i < s.length; i++) {
                short size = s[i].size();
                int res = s[i].from_byte_array(size, (short)0, 
                                               buf, (short)index);
                assert res == size + 1;
                index += size;
            }
        }
        return index;
    }
}
