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
// test frame state
// 
// $Id: Test_state.java,v 1.22 2010-02-16 10:26:08 tews Exp $

#include <config>

package ds.ov2.front;

import ds.ov2.util.Reference;
import ds.ov2.util.BigInteger_inputs;


/** 
 * State class for the front office test frame. Contains the global
 * variables for the test frame. Most of them can be changed with
 * command line options.
 * <P>
 *
 * Static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.22 $
 * @commitdate $Date: 2010-02-16 10:26:08 $ by $Author: tews $
 * @environment host
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PLAIN_APPLET_TESTFRAME">PLAIN_APPLET_TESTFRAME<a>
 *   <a href="../../../overview-summary.html#MONT_APPLET_TESTFRAME">MONT_APPLET_TESTFRAME<a>
 *   <a href="../../../overview-summary.html#SQUARE_APPLET_TESTFRAME">SQUARE_APPLET_TESTFRAME<a>
 *   <a href="../../../overview-summary.html#SQUARE4_APPLET_TESTFRAME">SQUARE4_APPLET_TESTFRAME<a>
 */
public class Test_state {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected Test_state() {}


    /**
     * 
     * Verbosity of the generated output. Higher values give more
     * output. Set with the command line options {@code -v}, {@code
     * -d}, {@code -dd} and {@code -ddd}.
     */
    public static Reference<Integer> verbosity = new Reference<Integer>(0);


    /**
     * 
     * Default initialization for {@link #applet_type}. The default
     * value is controled by the Makefile with various cpp defines.
     * <P>
     *
     */
    public static Applet_type init_applet_type =
        // First check for the different host testframes
        #ifdef PLAIN_APPLET_TESTFRAME
            Applet_type.PLAIN_RSA_APPLET;
        #elif defined(MONT_APPLET_TESTFRAME)
            Applet_type.MONT_RSA_APPLET;
        #elif defined(SQUARE_APPLET_TESTFRAME)
            Applet_type.SQUARED_RSA_APPLET;
        #elif defined(SQUARE4_APPLET_TESTFRAME)
            Applet_type.SQUARED4_RSA_APPLET;
        #else
            // It's not an host testframe, so it can only be the card
            // test frame. This can drive all different applets and
            // the applet type can be changed with options. 
            Applet_type.MONT_RSA_APPLET;
        #endif


    static {
        // Set State.base_length for the default applet type.
        State.base_length.ref = init_applet_type.minimal_base_size();
        State.update_exponent_length();
    }
        

    /**
     * 
     * The applet type of the applet the testframe works with.
     */
    public static Reference<Applet_type> applet_type = 
        new Reference<Applet_type>(init_applet_type);


    /**
     * 
     * Test with successively increasing key sizes. Enabled with
     * option {@code -test-size}
     */
    public static Reference<Boolean> test_increase_size = 
        new Reference<Boolean>(false);


    /**
     * 
     * Test a fixed size. Enabled with option {@code -test-const}. 
     */
    public static Reference<Boolean> test_const_size = 
        new Reference<Boolean>(false);


    /**
     * 
     * Test PTLS system parameter save/restore. Only for the host test
     * frame. Enabled with {@code -test-ptls-save}.
     */
    public static Reference<Boolean> test_ptls_save = 
        new Reference<Boolean>(false);

    /**
     * 
     * Number of key generation rounds for each tested key size. Set
     * with {@code -ptls-rounds}.
     */
    public static Reference<Integer> ptls_param_rounds = 
        new Reference<Integer>(3);


    /**
     * 
     * Number of applet initialization rounds per generated key. Set
     * with {@code -card-init-rounds}.
     */
    public static Reference<Integer> card_init_rounds = 
        new Reference<Integer>(3);


    /**
     * 
     * Number of resign rounds per applet. Set with {@code
     * -resign-rounds}. 
     */
    public static Reference<Integer> resign_rounds = 
        new Reference<Integer>(10);


    /**
     * 
     * Number of proof rounds per resign round. Set with {@code
     * -proof-rounds}. 
     */
    public static Reference<Integer> proof_rounds = 
        new Reference<Integer>(20);


    /**
     * 
     * Milliseconds to sleep before each protocol invocation. 
     */
    public static int sleep_time = 0;


    /**
     * 
     * Ignore signature errors and failed runs of the resing and proof
     * protocol. Enabled with option {@code -ignore}.
     */
    public static Reference<Boolean> ignore_all_kinds_of_problems = 
        new Reference<Boolean>(false);


    /**
     * Vector of input arguments from the command line via options
     * {@code -i} and {@code -hex}.
     */
    public static BigInteger_inputs fix_inputs = new BigInteger_inputs();
}
