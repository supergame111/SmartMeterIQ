//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/gui/../front/Applet_type.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/gui/../front/Applet_type.java"
// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2009
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
// Created 22.4.09 by Hendrik
// 
// Enumeration for the two different applets together with utility methods
// 
// $Id: Applet_type.java,v 1.5 2010-02-16 10:26:07 tews Exp $

package ds.ov2.front;


import java.nio.charset.Charset;


/**
 * 
 * Enumeration of the different RSA applets this class can deal
 * with. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.5 $
 * @commitdate $Date: 2010-02-16 10:26:07 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public enum Applet_type {

    /**
     * 
     * ID for the plain RSA applet. The plain RSA applet computes
     * exponents with the RSA cipher but uses Java Montgomery
     * multiplication for products. It communicates with the
     * host by exchanging the plain, unmontgomerized numbers.
     * Corresponds to {@link RSA_data#PLAIN_RSA_APPLET}.
     */
    PLAIN_RSA_APPLET,

    /**
     * 
     * ID for the Montgomerizing RSA applet. The Montgomerizing
     * RSA applet computes everything on the JCVM, without the RSA
     * cipher. It communicates with the host by exchanging
     * montgomerized numbers. Corresponds to {@link
     * RSA_data#MONT_RSA_APPLET}.
     */
    MONT_RSA_APPLET,

    /**
     * 
     * ID for the squaring RSA applet. The squaring RSA applet
     * computes exponents and products with the RSA cipher. Products
     * are computed with the equation {@code x*y = ((x+y)^2 - x^2 -
     * y^2)/2}. The applet communicates with the host by exchanging
     * the plain, unmontgomerized numbers. Corresponds to {@link
     * RSA_data#SQUARED_RSA_APPLET}.
     */
    SQUARED_RSA_APPLET,


    /**
     * 
     * ID for the square 4 RSA applet. The square 4 RSA applet
     * computes exponents and products with the RSA cipher. Products
     * are computed with the equation {@code x*y = ((x+y)^2 -
     * (x-y)^2)/4}. The applet communicates with the host by
     * exchanging the plain, unmontgomerized numbers. Corresponds to
     * {@link RSA_data#SQUARED4_RSA_APPLET}.
     */
    SQUARED4_RSA_APPLET;


    /**
     * 
     * Decide whether the communication for this applet type is
     * montgomerized or not.
     * 
     * @return true if numbers are montgomerized for this applet type
     */
    public boolean montgomerize() {
        switch(this) {
        case MONT_RSA_APPLET:
            return true;
        case PLAIN_RSA_APPLET:
        case SQUARED_RSA_APPLET:
        case SQUARED4_RSA_APPLET:
            return false;
        default:
            assert false;
            return false;
        }
    }


    /**
     * 
     * Return the number of <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * digits</a> that this applet uses.
     * 
     * @return number of Montgomery digits
     */
    public int montgomery_digits() {
        switch(this) {
        case MONT_RSA_APPLET:
            return 2;
        case PLAIN_RSA_APPLET:
            return 2;
        case SQUARED_RSA_APPLET:
        case SQUARED4_RSA_APPLET:
            return 0;
        default:
            assert false;
            return -1;
        }
    }


    /**
     * 
     * Return a minimal default base size, depending on the applet
     * type. Take thereby RSA cipher limitations into account.
     * 
     * @return a minimal default base (RSA key) size
     */
    public int minimal_base_size() {
        switch(this) {
        case MONT_RSA_APPLET:
            return 32;
        case PLAIN_RSA_APPLET:
            return 512;
        case SQUARED_RSA_APPLET:
            return 511;
        case SQUARED4_RSA_APPLET:
            return 510;
        default:
            assert false;
            return 512;
        }
    }


    /**
     * 
     * Convert from this enumeration to the byte constants used in
     * the applet code, see {@link RSA_data#PLAIN_RSA_APPLET} and
     * {@link RSA_data#MONT_RSA_APPLET}.
     * 
     * @return Returns the byte constant that corresponds to this 
     */
    public byte to_byte() {
        switch(this){
        case PLAIN_RSA_APPLET: return RSA_data.PLAIN_RSA_APPLET;
        case MONT_RSA_APPLET: return RSA_data.MONT_RSA_APPLET;
        case SQUARED_RSA_APPLET: return RSA_data.SQUARED_RSA_APPLET;
        case SQUARED4_RSA_APPLET: return RSA_data.SQUARED4_RSA_APPLET;
        default: return RSA_data.INVALID_APPLET_ID;
        }
    }


    /**
     * 
     * Convert the applet-type byte constants (see {@link
     * RSA_data#PLAIN_RSA_APPLET} and {@link
     * RSA_data#MONT_RSA_APPLET}) into this enumeration type.
     * 
     * @param byte_type applet-type byte
     * @return corresponding enumeration constant
     * @throws IllegalArgumentException if {@code byte_type} is
     * not one of the valid constants
     */
    static Applet_type from_byte(byte byte_type) {
        switch(byte_type) {
        case RSA_data.PLAIN_RSA_APPLET: return PLAIN_RSA_APPLET;
        case RSA_data.MONT_RSA_APPLET: return MONT_RSA_APPLET;
        case RSA_data.SQUARED_RSA_APPLET: return SQUARED_RSA_APPLET;
        case RSA_data.SQUARED4_RSA_APPLET: return SQUARED4_RSA_APPLET;
        default:
            throw new IllegalArgumentException(
                     String.format("invalid type constant 0x%02X",
                                   byte_type));
        }
    }


    /**
     * 
     * Package name for this applet type.
     * 
     * @return package name as string.
     */
    public String package_name() {
        switch(this) {
        case PLAIN_RSA_APPLET: return "ds_ov2_prsa";
        case MONT_RSA_APPLET: return "ds_ov2_mrsa";
        case SQUARED_RSA_APPLET: return "ds_ov2_srsa";
        case SQUARED4_RSA_APPLET: return "ds_ov2_s4rsa";
        default:
            assert false;
            return "XXX";
        }
    }


    /**
     * 
     * Package name as byte array, like an AID.
     * 
     * @return package name as byte array
     */
    public byte[] package_aid() {
        return package_name().getBytes(Charset.forName("US-ASCII"));
    }


    /**
     * 
     * Applet name for this applet type.
     * 
     * @return applet name as string.
     */
    public String applet_name() {
        return package_name() + ".app";
    }


    /**
     * 
     * Applet name as byte array, like an AID.
     * 
     * @return applet name as byte array
     */
    public byte[] applet_aid() {
        return applet_name().getBytes(Charset.forName("US-ASCII"));
    }


    /**
     * 
     * Cap file for this applet type.
     * 
     * @return path to the cap file
     */
    public String applet_file() {
        String subdir = "XXX";
        switch(this) {
        case PLAIN_RSA_APPLET:
            subdir = "plain_rsa_card";
            break;
        case MONT_RSA_APPLET:
            subdir = "mont_rsa_card";
            break;
        case SQUARED_RSA_APPLET:
            subdir = "square_rsa_card";
            break;
        case SQUARED4_RSA_APPLET:
            subdir = "square4_rsa_card";
            break;
        default:
            assert false;
        }

        return "_java_build_dir/" + subdir + "/front/"
            + package_name() + "/javacard/" + package_name() + ".cap";
    }
}
