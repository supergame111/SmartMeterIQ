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
// Created 14.10.08 by Hendrik
// 
// record bignat's and vectors to resize
// 
// $Id: Resize.java,v 1.17 2009-05-23 21:03:30 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.bignat;
#endif


#if !defined(VARIABLE_SIZE_BIGNATS)
   #error "Support for resizing Bignat's and Vector's is only for testing."
#endif


/** 
 * Centralized resizing of {@link Bignat Bignats}, {@link Bignat_array
 * Bignat arrays} and {@link RSA_exponent RSA exponents}. The whole
 * resising functionality is meant for test frames only, see <a
 * href="package-summary.html#resizing">the general explanations for
 * resizing</a>. Although on the card all arrays are allocated just
 * once, the three named data types have an optional interface for
 * virtual size and length changes. The interface is optional, because
 * it is only available when <a
 * href="../../../overview-summary.html#VARIABLE_SIZE_BIGNATS">VARIABLE_SIZE_BIGNATS<a>
 * is defined. The
 * length and size changes are only virtual, because the underlying
 * arrays do not change and resizing does therefore not cost any
 * memory. An exception is the resizing functionality of {@link
 * RSA_exponent}. There, with every {@link RSA_exponent#init_key
 * init_key} operation, a new RSA key is created and probably some
 * memory lost.
 * <P>
 *
 * This class offers the possibility for registering objects of the
 * three named types for performing centralized resizing operations.
 * One problem thereby is that because of the lack of an garbage
 * collector the java card environment does not really provide the
 * possibility of arrays that grow in size according to demand.
 * Therefore, and for simplicity, the internal arrays that hold the
 * registered objects are allocated just once, with a hardwired size.
 * If one of the arrays turns out to be too small an assertion will
 * fail. 
 * <P>
 *
 * Objects must be registered according to their type of course.
 * {@link Bignat Bignats} can be registered as long, as short, or as
 * double-sized Bignats. Thereby, long, short and double-sized have no
 * particular meaning. It is only that the resize operation {@link
 * #resize_bignats resize_bignats} takes three size arguments for
 * Bignats, one for the long ones, one for the short ones, and one for
 * the double-sized ones. The {@link RSA_exponent RSA exponents} are
 * neither long nor small for these purposes. They get their own
 * {@code cipher_len} argument. <P>
 *
 * (The distinction in long and short comes from the OV-Chip 2.0
 * context. There bases are 1000-2000 bits long and exponents 150-200
 * bits. For multiplication of exponent (attribute) values also one
 * double-sized Bignat twice as long as the exponents are needed. The
 * {@link RSA_exponent} even requires that the ``long'' bases are
 * longer than the ``short'' exponents.) <P>
 *
 * Their are two kinds of arrays distinguished here. The first kind
 * are the arrays of bases and exponents, that all have the same size,
 * around 2-15 elements. The second kind are the arrays of precomputed
 * base factors (see {@link Vector#exponent_mod Vector.exponent_mod}) that grow
 * exponentially with the length of the bases. Therefore arrays can be
 * registered as either vectors (the first kind) or factor arrays (the
 * second kind). The length changing operation, {@link #resize_vectors
 * resize_vectors} takes two length arguments and treats these two
 * kinds of arrays separately.
 * <P>
 *
 * Types that wrap one or several Bignats typically offer some
 * register method, that registers all contained objects here, see for
 * instance {@link Bignat_array#register_long_bignats}, {@link
 * Bignat_array#register_short_bignats} and {@link
 * Modulus#register_long_bignats}.
 * <P>
 *
 * This is a static class. All fields and methods are static, object
 * creation is disabled. The class must be initialized with {@link
 * #init} before using it.
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#ASSERT_TAG">ASSERT_TAG(condition, tag)</a>,
 *   <a href="../../../overview-summary.html#HOST_TESTFRAME">HOST_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#TESTFRAME">TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#VARIABLE_SIZE_BIGNATS">VARIABLE_SIZE_BIGNATS</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.17 $
 * @commitdate $Date: 2009-05-23 21:03:30 $ by $Author: tews $
 * @environment host, card
 */
PUBLIC class Resize {

    /**
     * 
     * Maximal number of short bignats, currently {@value}.
     */
    private static final short max_short_bignats = 7;

    /**
     * 
     * Maximal number of long bignats, currently {@value}.
     */
    private static final short max_long_bignats = 50;

    /**
     * 
     * Maximal number of double-sized bignats, currently {@value}.
     */
    private static final short max_double_bignats = 1;

    /**
     * 
     * Maximal number of vectors, currently {@value}.
     */
    private static final short max_vectors = 2;

    /**
     * 
     * Maximal number of factor arrays, currently {@value}.
     */
    private static final short max_factors = 1;

    /**
     * 
     * Maximal number of RSA exponents, currently {@value}.
     */
    private static final short max_long_rsa = 2;


    /**
     * 
     * Array of registered short bignats.
     */
    private static Bignat[] short_bignats;

    /**
     * 
     * Array of registered long bignats.
     */
    private static Bignat[] long_bignats;

    /**
     * 
     * Array of registered double-sized bignats.
     */
    private static Bignat[] double_bignats;

    /**
     * 
     * Array of registered vectors.
     */
    private static Vector[] vectors;

    /**
     * 
     * Array of registered factor arrays.
     */
    private static Vector[] factors;

    #if defined(JAVACARD_APPLET) || defined(JAVADOC)

       /**
        * 
        * Array of registered RSA exponents. This field is only
        * available if JAVACARD_APPLET is defined, because {@link
        * RSA_exponent} does only exist there.
        */
       private static RSA_exponent[] long_rsa;
    #endif


    /**
     * 
     * Current index in the {@link #short_bignats} array.
     */
    private static short short_bignat_index;

    /**
     * 
     * Current index in the {@link #long_bignats} array.
     */
    private static short long_bignat_index;

    /**
     * 
     * Current index in the {@link #double_bignats} array.
     */
    private static short double_bignat_index;

    /**
     * 
     * Current index in the {@link #vectors} array.
     */
    private static short vector_index;

    /**
     * 
     * Current index in the {@link #factors} array.
     */
    private static short factor_index;

    /**
     * 
     * Current index in the {@link #long_rsa} array.
     */
    private static short long_rsa_index;


    /**
     * 
     * Static class, object creation disabled.
     */
    protected Resize() {}


    /**
     * 
     * Initializes all the arrays to their hardwired maximal size.
     * This method must be called once before the first registration.
     * 
     */
    public static void init() {
        short_bignats = new Bignat[max_short_bignats];
        long_bignats = new Bignat[max_long_bignats];
        double_bignats = new Bignat[max_double_bignats];
        vectors = new Vector[max_vectors];
        factors = new Vector[max_factors];

        #if defined(JAVACARD_APPLET) || defined(JAVADOC)
           long_rsa = new RSA_exponent[max_long_rsa];
        #endif

        short_bignat_index = 0;
        long_bignat_index = 0;
        double_bignat_index = 0;
        vector_index = 0;
        factor_index = 0;
        long_rsa_index = 0;
    }


    /**
     * 
     * Register the short bignat {@code b}.
     * <P>
     *
     * Asserts that the arrays have been allocated and that there is
     * still space in the array for short bignats.
     *
     * 
     * @param b bignat to register
     */
    public static void register_short_bignat(Bignat b) {
        ASSERT(short_bignats != null && short_bignat_index < max_short_bignats);
        short_bignats[short_bignat_index++] = b;
        return;
    }


    /**
     * 
     * Register the long bignat {@code b}.
     * <P>
     *
     * Asserts that the arrays have been allocated and that there is
     * still space in the array for long bignats.
     *
     * 
     * @param b bignat to register
     */
    public static void register_long_bignat(Bignat b) {
        //System.out.format("RLB %d\n", long_bignat_index);
        ASSERT(long_bignats != null && long_bignat_index < max_long_bignats);
        long_bignats[long_bignat_index++] = b;
        return;
    }


    /**
     * 
     * Register the double-sized bignat {@code b}.
     * <P>
     *
     * Asserts that the arrays have been allocated and that there is
     * still space in the array for double-sized bignats.
     *
     * 
     * @param b bignat to register
     */
    public static void register_double_bignat(Bignat b) {
        //System.out.format("RLB %d\n", long_bignat_index);
        ASSERT(double_bignats != null && 
               double_bignat_index < max_double_bignats);
        double_bignats[double_bignat_index++] = b;
        return;
    }


    /**
     * 
     * Register the vector {@code v}.
     * <P>
     *
     * Asserts that the arrays have been allocated and that there is
     * still space in the array for vectors.
     *
     * 
     * @param v vector to register
     */
    public static void register_vector(Vector v) {
        ASSERT(vectors != null && vector_index < max_vectors);
        vectors[vector_index++] = v;
        return;
    }


    /**
     * 
     * Register the factor array {@code v}.
     * <P>
     *
     * Asserts that the arrays have been allocated and that there is
     * still space in the array for factor arrays.
     *
     * 
     * @param v vector of factors to register
     */
    public static void register_factor(Vector v) {
        ASSERT(factors != null && factor_index < max_factors);
        factors[factor_index++] = v;
        return;
    }


    #if defined(JAVACARD_APPLET) || defined(JAVADOC)

       /**
        * 
        * Register the RSA exponent {@code e}. 
        * <P>
        *
        * Asserts that the arrays have been allocated and that there is
        * still space in the array for RSA exponents.
        * <P>
        *
        * This method is only available if JAVACARD_APPLET is defined,
        * because {@link RSA_exponent} is only available there.
        * 
        * @param e RSA exponent to register
        */
       public static void register_long_rsa(RSA_exponent e) {
           ASSERT(long_rsa != null && long_rsa_index < max_long_rsa);
           long_rsa[long_rsa_index++] = e;
           return;
       }
    #endif


    /**
     * 
     * Resize bignats and RSA exponents. Resizes all registered long
     * and short bignats and, possibly, the registered RSA exponents.
     * On the registered bignats the method {@link Bignat#resize
     * Bignat.resize} is called. On the RSA exponents {@link
     * RSA_exponent#init_key RSA_exponent.init_key} is called.
     * <P>
     *
     * If {@code cipher_len} is 0 the RSA exponent size change is
     * skipped. 
     *
     * @param short_len new size for the short bignats
     * @param long_len new size for the long bignats
     * @param cipher_len new size for the RSA exponents
     */
    public static void resize_bignats(short short_len, short long_len,
                                      short double_len, short cipher_len) 
    {
        for(short i = 0; i < short_bignat_index; i++)
            short_bignats[i].resize(short_len);
        for(short i = 0; i < long_bignat_index; i++)
            long_bignats[i].resize(long_len);
        for(short i = 0; i < double_bignat_index; i++)
            double_bignats[i].resize(double_len);

        #if defined(JAVACARD_APPLET) || defined(JAVADOC)
            if(cipher_len != 0)
                for(short i = 0; i < long_rsa_index; i++)
                    long_rsa[i].init_key(cipher_len);
        #endif

        return;
    }


    /**
     * 
     * Resize arrays. Changes the length of all registered vectors and
     * factor arrays by calling the {@link Vector#set_length
     * Vector.set_length} method on them. Because the factor array
     * will always store all possible products of some number of
     * factors, its size will always be equal to {@code 2^n - 1},
     * where n is the number of factors (the {@code -1} is there
     * because the array does not contain the empty product, see
     * {@link Vector#exponent_mod Vector.exponent_mod}). The {@code
     * factors_len} argument gets the {@code n} rather than the real
     * factor array size.
     * 
     * @param vector_len new length for the (normal) vectors
     * @param factors_len base 2 log of the new length for the factor
     * arrays, i.e., if the factor array stores the products of 4
     * factors, its length would be 15 and {@code factors_len} would
     * be 4.
     */
    public static void resize_vectors(short vector_len, short factors_len) {
        for(short i = 0; i < vector_index; i++)
            vectors[i].set_length(vector_len);
        // factors_len gives the number of precomputed factors. 
        // The length of the factors array is then 2^factors_len - 1.
        for(short i = 0; i < factor_index; i++)
            factors[i].set_length((short)((1 << factors_len) -1));
        return;
    }
}
