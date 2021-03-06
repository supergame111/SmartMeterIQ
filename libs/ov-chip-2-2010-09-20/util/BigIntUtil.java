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
// Created 19.11.08 by Hendrik
// 
// utility methods for BigInteger
// 
// $Id: BigIntUtil.java,v 1.10 2009-05-12 14:48:38 tews Exp $


package ds.ov2.util;


import java.util.Random;
import java.math.BigInteger;
import java.io.PrintWriter;


/** 
 * {@link BigInteger} Utilities. Extending the {@link BigInteger}
 * class for these few methods is not an option because one would need
 * to program downcasts for all methods that return a fresh {@link
 * BigInteger}, which are basically all methods of {@link BigInteger}.
 * <P>
 *
 * Static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.10 $
 * @commitdate $Date: 2009-05-12 14:48:38 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class BigIntUtil {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected BigIntUtil() {}


    /**
     * 
     * Coprime check. Tests whether the two arguments are coprime. Two
     * natural numbers are said to be coprime if the biggest factor
     * they share is 1, ie. if there greatest common divisor is 1.
     * 
     * @param a first BigInteger
     * @param b second BigInteger
     * @return true if {@code a} and {@code b} are coprime.
     */
    public static boolean coprime(BigInteger a, BigInteger b) {
        return a.gcd(b).compareTo(BigInteger.ONE) == 0;
    }


    /**
     * 
     * Modular random number generator. Generate an equally
     * distributed random number less than {@code mod} with a bit
     * length at most 4 less than the bit length of {@code mod}.
     * 
     * @param rand Random number generator
     * @param mod modulus
     * @return a random number less than {@code mod}
     */
    public static BigInteger mod_rand(Random rand, BigInteger mod) {
        int bit_size = mod.bitLength();
        BigInteger res;
        do {
            res = new BigInteger(bit_size, rand);
        } while(res.bitLength() < bit_size -4 ||
                res.compareTo(mod) >= 0);
        return res;
    }       


    /**
     * 
     * Modular random number generator for the multiplicative
     * subgroup. Generates an equally disctributed random number less
     * than {@code mod} that has a modular inverse modulo {@code mod}.
     * The bit length of the random number is at most 4 less than
     * {@code mod}.
     * 
     * @param rand Random number generator
     * @param mod modulus
     * @return random number less than {@code mod} with a modular
     * inverse 
     */
    public static BigInteger mod_rand_with_inverse(Random rand, 
                                                   BigInteger mod) {
        BigInteger res;
        do {
            res = mod_rand(rand, mod);
        } while(!coprime(res, mod));
        return res;
    }


    /**
     * 
     * Compute the multi-exponent {@code base^exponent (modulo
     * modulus)}. 
     * <P>
     *
     * Asserts that the {@code base} and {@code exponent} array have
     * the same length.
     * 
     * @param base the bases
     * @param exponent the exponents
     * @param modulus
     * @return the exponent {@code base[0]^exponent[0] *
     * base[1]^exponent[1] * ... (modulo modulus)}
     */
    public static BigInteger multi_exponent(BigInteger[] base, 
                                            BigInteger[] exponent,
                                            BigInteger modulus) {
        assert base.length == exponent.length;
        BigInteger res = BigInteger.ONE;
        for(int i = 0; i < base.length; i++)
            res = res.multiply(base[i].modPow(exponent[i], modulus)).
                mod(modulus);
        return res;
    }


    /**
     * BigInteger size in bytes. For {@link java.math.BigInteger#ZERO
     * BigInteger.ZERO} it returns 1.
     * Note that the value computed here might be different 
     * from 
     * {@link java.math.BigInteger#toByteArray BigInteger.toByteArray}.length
     * because the latter might add a leading zero byte. 
     *
     * @param bi
     * @return the size in bytes of {@code bi}
     */
    public static int byte_size(BigInteger bi) {
        int l = (bi.bitLength() + 7) / 8;
        return l == 0 ? 1 : l;
    }


    /**
     * 
     * Converts a {@link BigInteger} into a hex string. In contrast to
     * {@link BigInteger#toString(int) BigInteger.toString(16)}
     * 4-digit-groups are separated by points, like 23EA.4F11...
     * 
     * @param bi BigInteger to convert
     * @return hex string
     */
    public static String to_byte_hex_string(BigInteger bi) {
        return Misc_host.to_byte_hex_string(bi.toByteArray());
    }


    /**
     * 
     * Copy a BigInteger into a byte array. This is a bit more tricky
     * than just copying the result of {@link BigInteger#toByteArray}.
     * First, the argument array {@code a} might need some zero
     * padding and second, {@link BigInteger#toByteArray} might have a
     * leading zero that does not fit into {@code a}.
     * <P>
     *
     * Asserts that the BigInteger {@code bi}, with leading zeros
     * removed, fits into {@code a}.
     * 
     * @param bi the source BigInteger 
     * @param a the destination array
     */
    public static void copy_into_byte_array(BigInteger bi, byte[] a) 
    {
        byte[] bia = bi.toByteArray();
        assert bia.length <= a.length || 
            (bia.length == a.length + 1 && bia[0] == 0);
        for(int i = 0; i < a.length - bia.length; i++)
            a[i] = 0;
        System.arraycopy(bia, 
                         bia.length == a.length + 1 ? 1 : 0,
                         a,
                         bia.length == a.length + 1 ? 0 : a.length - bia.length,
                         bia.length == a.length + 1 ? a.length : bia.length);
        return;
    }


    /**
     * 
     * Convert a byte array into a {@link BigInteger}. This is just as
     * {@link BigInteger#BigInteger(byte[])} but does not produce
     * negative BigIntegers if the first bit in {@code a} is set.
     * 
     * @param a source byte array
     * @return positive BigInteger with the value of {@code a}
     */
    public static BigInteger from_byte_array(byte[] a) {
        byte[] a0 = new byte[a.length + 1];
        a0[0] = 0;
        System.arraycopy(a, 0, a0, 1, a.length);
        return new BigInteger(a0);
    }


    /**
     * 
     * Prints a {@link BigInteger} array as follows.
     * <PRE>
     *  4 bases
     *  base[0]: 5F11.9615
     *         = 1594988053
     *  base[1]: 1999.3BC3
     *         = 429472707
     *  base[2]: 00.8327.3245
     *         = 2200384069
     *  base[3]: 5D9E.AA9A
     *         = 1570679450
     * </PRE>
     * The first line is hex, the second decimal. 
     * <P>
     *
     * Argument {@code intro_format} must be a format string that
     * accepts the length of the array as integer argument. It is used
     * for the heading. In the above example it is "%d bases\n". Note
     * the newline at the end!
     * <P>
     *
     * The {@code line_start} is a string with the name of the array,
     * in the above example it is "base".
     * 
     * @param out output channel
     * @param intro_format format string with at most integer directive
     * for the heading
     * @param line_start name of the array
     * @param bi the array to print
     */
    // Intro_format is the heading. line_start would typically be the
    // array name, it's printed at the start of each BigInteger line.
    // intro_format must be a format string for one integer argument.
    public static void print_array(PrintWriter out,
                                   String intro_format, 
                                   String line_start, 
                                   BigInteger bi[]) 
    {
        out.format(intro_format, bi.length);
        String line_start_spaces = "                                   ";
        if(line_start.length() <= line_start_spaces.length())
            line_start_spaces = 
                line_start_spaces.substring(0, line_start.length());
        
        for(int i = 0; i < bi.length; i++)
            out.format("%s[%d]: %s\n" +
                       "%s   = %s\n", 
                       line_start, i, 
                       to_byte_hex_string(bi[i]),
                       line_start_spaces,
                       bi[i]);
        return;
    }
}
