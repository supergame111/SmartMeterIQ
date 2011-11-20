//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/State.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/State.java"
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
// Created 2.11.08 by Hendrik
// 
// frontoffice state
// 
// $Id: State.java,v 1.14 2010-03-12 15:40:20 tews Exp $

package ds.ov2.front;

import java.util.Random;

import ds.ov2.util.Security_parameter;
import ds.ov2.util.Security_parameter.Calibration;
import ds.ov2.util.Reference;


/** 
 * Static global state class for the host driver code for the OV-chip
 * RSA applets. The parameters here are always needed for the OV-chip
 * front office. More parameters that are only needed in the test
 * frames are in {@link Test_state}.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.14 $
 * @commitdate $Date: 2010-03-12 15:40:20 $ by $Author: tews $
 * @environment host
 */
public class State {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected State() {}


    /**
     * 
     * Year for choosing the security parameter. See {@link
     * Security_parameter#exponent_length_for_modulus_length
     * Security_parameter.exponent_length_for_modulus_length}.
     */
    public static int security_year = 2009;


    /**
     * 
     * Calibration data to use for the {@link Security_parameter}
     * methods. Set to {@link Security_parameter#rsa_768_break_2009}
     * by option {@code -768}. 
     */
    public static Reference<Calibration> security_calibration =
        new Reference<Calibration>(null);

    /**
     * 
     * Whether {@link #base_length} has been set by the user.
     */
    public static Reference<Boolean> base_length_set =
        new Reference<Boolean>(false);


    /**
     * 
     * Length of the bases and the RSA key in bits. This is the pure
     * number size, without counting any <a
     * href="../bignat/package-summary.html#montgomery_factor">Mongomery
     * digits.</a>
     */
    public static Reference<Integer> base_length =
        new Reference<Integer>(512);


    /**
     * 
     * Length of the attributes and other exponents in bits.
     */
    public static Reference<Integer> exponent_length =
        new Reference<Integer>();


    /**
     * 
     * True if {@link #exponent_length} has been set by the user.
     */
    public static Reference<Boolean> exponent_length_set =
        new Reference<Boolean>(false);


    /**
     * 
     * Number of attributes.
     */
    public static Reference<Integer> attribute_number =
        new Reference<Integer>(4);


    /**
     * 
     * Computes an exponent length that matches the {@code
     * base_length} argument.
     * 
     * @param base_length length of the base in bits
     * @return exponent length in bits
     */
    public static int make_exponent_length(int base_length) {
        if(exponent_length_set.ref)
            return exponent_length.ref;
        else
            return Security_parameter.
                exponent_length_for_modulus_length_full_byte(security_year,
                                                             base_length,
                                                      security_calibration.ref);
    }


    /**
     * 
     * Update {@link #exponent_length} to match a (supposedly) changed
     * {@link #base_length}. 
     */
    public static void update_exponent_length() {
        exponent_length.ref = make_exponent_length(base_length.ref);
    }


    // initialize the exponent length
    static {
        update_exponent_length();
    }
}
