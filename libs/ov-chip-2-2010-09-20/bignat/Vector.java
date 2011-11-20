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
// Created 12.9.08 by Hendrik
// 
// a fixed array of Bignat's, grouped for exponentiation
// 
// $Id: Vector.java,v 1.30 2010-02-16 10:26:06 tews Exp $

#include <config>

#include "bignatconfig"

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.bignat;
#endif


#ifndef JAVACARD_APPLET
  import ds.ov2.util.Misc;
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Serializable_array;
#endif


/** 
 * {@link Bignat} array with multi-exponent functionality. This class
 * defines two methods for computing multi-exponents. The first one is
 * a navtive Java implementation, using simultaneous squaring. The
 * second one uses an {@link RSA_exponent} and multiplies the results
 * with montgomery multiplication. The compatibility between the two
 * methods is limited, because different precomputations are required.
 * The biggest difference is that for the simultaneous squaring method
 * the bases must be montgomerized.
 * <P>
 *
 * Inherits the functionality for providing the illusion of a
 * different array length from {@link Bignat_array}.
 * <P>
 *
 * This is a card data type. For the protocol layer, an object of this
 * class is compatible with 
 * <UL><LI>a vector of the same length and containing
 * Bignat's of the same size.</LI>
 * <LI>a {@link Host_vector} of the same length and whose configured
 * bignat size equals the size of the Bignat's in this vector.
 * </LI>
 * </Ul>
 *
 * see {@link #is_compatible_with is_compatible_with}.
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 *
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#SQUARED_RSA_MULT">SQUARED_RSA_MULT<a>
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#ASSERT_TAG">ASSERT_TAG(condition, tag)</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#HOST_TESTFRAME">HOST_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#BIGNAT_USE_BYTE ">BIGNAT_USE_BYTE </a>,
 *   <a href="../../../overview-summary.html#DOUBLE_DIGIT_TYPE">DOUBLE_DIGIT_TYPE</a>,
 *   <a href="../../../overview-summary.html#OPT_SPECIAL_SQUARE">OPT_SPECIAL_SQUARE</a>,
 *   <a href="../../../overview-summary.html#JAVADOC">JAVADOC</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.30 $
 * @commitdate $Date: 2010-02-16 10:26:06 $ by $Author: tews $
 * @environment host, card
 */
PUBLIC class Vector 
    extends Bignat_array
    implements APDU_Serializable 
{

    // By extending Bignat_array, Vector supports changing its length
    // within the boundaries of the underlying array.

    /**
     * 
     * Temporary digit array of type DIGIT_TYPE used inside the {@link
     * #exponent_mod exponent_mod} method. Only allocated if the
     * {@code init_buf} constructor argument is true. If it is
     * allocated its length will be the same as the length of the
     * underlying Bignat array of this vector. If DIGIT_TYPE is byte,
     * the array will be allocated as transient array.
     */
    private DIGIT_TYPE[] buf;


    /**
     * 
     * References of temporaries used inside {@link #exponent_mod
     * exponent_mod} and {@link #mont_rsa_exponent_mod mont_rsa_exponent_mod}.
     * Only allocated if the {@code init_buf} constructor argument is
     * true. If allocated the length of the array is 3. The method
     * {@link #exponent_mod exponent_mod} uses the first two elements,
     * {@link #mont_rsa_exponent_mod mont_rsa_exponent_mod} uses all three. <P>
     *
     * Only the array and not the temporary bignat object themselves
     * belong to this object. The temporary bignats are passed as
     * arguments to the {@link #exponent_mod exponent_mod} and {@link
     * #mont_rsa_exponent_mod mont_rsa_exponent_mod} methods. <P>
     *
     * This array is not allocated in (transient) RAM, because I don't
     * know how to downcast an object array to a Bignat array. 
     */
    private Bignat[] temp;


    #if defined(HOST_TESTFRAME) || defined(JAVADOC)

        /**
         * 
         * Debug and diagnostics output channel. Only available for
         * the host test frame if HOST_TESTFRAME is defined. Must be
         * set from the outside before use. Leave or set a null
         * reference to disable diagnostics.
         */
        public static java.io.PrintWriter out;
    #endif


    /**
     * 
     * Non-allocating constructor. Allocates only the underlying
     * bignat array, but not the bignat themselves. If one of the
     * methods {@link #exponent_mod exponent_mod} or {@link
     * #mont_rsa_exponent_mod mont_rsa_exponent_mod} will ever be used on this
     * object then {@code init_buf} must be set to true. The
     * constructor will then allocate the temporary data structures
     * that these methods require (see fields {@link #buf} and {@link
     * #temp}).
     * 
     * @param length length of the underlying bignat array
     * @param init_buf whether to initialize the temporary arrays
     * needed for {@link #exponent_mod exponent_mod} and {@link
     * #mont_rsa_exponent_mod mont_rsa_exponent_mod}
     */
    public Vector(short length, boolean init_buf) {
        super(length);
        if(init_buf) {
            #ifdef BIGNAT_USE_BYTE
                buf = Misc.allocate_transient_byte_array(length);
            #else               // BIGNAT_USE_INT
                buf = new int[length];
            #endif

            // If we allocate temp in RAM we would have to declare it
            // as Object array and then pay the downcast costs whenever
            // we access an element. The array is only written once per
            // exponent_mod call, therefore, we might even be faster 
            // allocating it in EEPROM.
            temp = new Bignat[3];
        }
        else {
            buf = null;
            temp = null;
        }
        return;
    }


    /**
     * 
     * Allocating constructor. Allocates the underlying bignat array
     * of length {@code length} complete with all bignats, each of
     * size {@code bignat_size}. The argument {@code in_ram} is passed
     * through to the {@link Bignat#Bignat(short, boolean) Bignat
     * constructor}, if true all the bignats are allocated in
     * transient RAM.
     * <P>
     * 
     * If one of the methods {@link #exponent_mod exponent_mod} or
     * {@link #mont_rsa_exponent_mod mont_rsa_exponent_mod} will ever be used on
     * this object then {@code init_buf} must be set to true. The
     * constructor will then allocate the temporary data structures
     * that these methods require (see fields {@link #buf} and {@link
     * #temp}).
     * 
     * @param bignat_size size of the Bignats in the array in bytes
     * @param length length of the underlying bignat array
     * @param init_buf whether to initialize the temporary arrays
     * needed for {@link #exponent_mod exponent_mod} and {@link
     * #mont_rsa_exponent_mod mont_rsa_exponent_mod}
     * @param in_ram    allocate the bignats in transient RAM if true
     */
    public Vector(short bignat_size, short length, 
                  boolean init_buf, boolean in_ram) {
        this(length, init_buf);
        allocate(bignat_size, in_ram);
    }


    /**
     * 
     * Montgomerized modular multi-exponent. Computes {@code
     * this.get(0)^exponent.get(0) * this.get(1)^exponent.get(1)...}
     * modulo {@code modulus} up to the length of this vector. The
     * result will be stored in {@code result}. The bases in this
     * object must be <a
     * href="package-summary.html#montgomery_factor">montgomerized</a>, the
     * exponents must not.
     * <P>
     *
     * Asserts that the fields {@link #buf} and {@link #temp} are
     * non-null, that is, the constructor for this object must have
     * been called with {@code init_buf == true}.
     * <P>
     *
     * The use of Montgomery multiplication implies other
     * restrictions, namely that the first two digits of all bases and
     * factors are zero (Montgomery digits) and that the modulus is
     * odd, <a href="package-summary.html#montgomery_factor">see
     * Montgomery multiplication</a>.
     * <P>
     *
     * The argument {@code base_factors} must contain the precomputed
     * factors of the first {@code base_factor_size} bases (ie.
     * elements of this vector). See {@link
     * Host_vector#make_montgomerized_factors
     * Host_vector.make_montgomerized_factors}, for an easy way to
     * compute these factors.
     * <P>
     * 
     * The vector {@code base_factor_size} must have size {@code
     * base_factor_size^2 -1}, where the {@code -1} comes in because
     * the empty product of all bases is not needed. The argument
     * {@code base_factor_size} can be zero, in this case {@code
     * base_factors} can also be a null reference. The factors in
     * {@code base_factors} must be <a
     * href="package-summary.html#montgomery_factor">montgomerized</a>. The
     * base factors must be stored in the following order
     *
     * <PRE>
     *   base_factors.get(0) == this.get(0)
     *   base_factors.get(1) == this.get(1)
     *   base_factors.get(2) == this.get(1) * this.get(0)
     *   base_factors.get(3) == this.get(2)
     *   base_factors.get(4) == this.get(2) * this.get(0)                
     *   base_factors.get(5) == this.get(2) * this.get(1)                
     *   base_factors.get(6) == this.get(2) * this.get(1) * this.get(0)
     *   ...
     * </PRE>
     *
     * (If you want to take these lines literally, then ``*'' must
     * denote <a
     * href="package-summary.html#montgomery_factor">montgomery
     * multiplication</a> and ``=='' equality of Bignat's.)
     * <P>
     *
     * The general rule is as follows. Take a base factors index
     * {@code i} and look at the binary representation of {@code i+1}.
     * The base factor {@code i} is the product of those bases whose
     * index has a 1-bit in the binary representation of {@code i+1}.
     * For instance, for index 5, look at the binary representation of
     * {@code 6 == 110}, ie., it must be the product of base 1 with
     * base 2.
     * <P>
     *
     * {@code base_factor_size} can be shorter than the length of this
     * vector, but it must not be longer. Best performance is obtained
     * if {@code base_factor_size} equals the length of this vector.
     * Then 2 multiplications per exponent bit are needed. Otherwise
     * {@code length - base_factor_size} additional multiplications
     * each with probability 0.5 are needed for each exponent bit.
     * <P>
     * 
     * Asserts that {@code base_factor_size} is at most 15.
     * 
     * The argument {@code mont_one} is used to initialize the
     * internal multiplication akkumulator, it must therefore be a <a
     * href="package-summary.html#montgomery_factor">montgomerized</a> 1. Note
     * that this is equal to the montgomery factor mont_fac, see
     * {@link Host_modulus#mont_fac Host_modulus.mont_fac}.
     * <P>
     *
     * The computation requires an additional temporary that is used
     * together with {@code result} for intermediate results.
     * Therefore {@code result} and {@code temp_arg} must be different
     * from all other Bignat passed in as arguments.
     * <P>
     *
     * The bases in this vector, the factors in {@code
     * base_factor_size}, {@code result} and {@code temp_arg} must all
     * have the same size, otherwise an exception in {@link
     * Bignat#montgomery_mult Bignat.montgomery_mult} might be thrown.
     * The size of the exponents is arbitrary, but all exponents must
     * have the same size.
     * <P>
     *
     * For an example on how to convert normal number into the format
     * required by this method, see the source code of {@link
     * Convenience#vector_exponent_mod Convenience.vector_exponent_mod}. 
     * <P>
     *
     * If OPT_SPECIAL_SQUARE is defined, squaring will be done with
     * {@link Bignat#montgomery_square Bignat.montgomery_square},
     * otherwise with {@link Bignat#montgomery_mult
     * Bignat.montgomery_mult}. 
     * 
     * @param exponent exponents
     * @param modulus modulus
     * @param base_factor_size number of bases for which precomputed
     * factors are available in {@code base_factors}
     * @param base_factors Base factor array of size 2^{@code
     * base_factor_size} -1, use {@link
     * Host_vector#make_montgomerized_factors
     * Host_vector.make_montgomerized_factors} to compute it
     * @param mont_one a montgomerized one, equals the <a
     * href="package-summary.html#montgomery_factor">Montgomery factor mont_fac
     * </a>, see {@link Host_modulus#mont_fac Host_modulus.mont_fac}.
     * @param result result
     * @param temp_arg a temporary of the same size as {@code result}
     * and the bases
     */
    public void exponent_mod(Vector exponent, Modulus modulus, 
                             short base_factor_size, Vector base_factors,
                             Bignat mont_one,
                             Bignat result, Bignat temp_arg) {
        ASSERT(buf != null && temp != null);
        // For base_factor_size == 16 we will get negative array
        // indices, therefore permit only 15.
        ASSERT(base_factor_size <= 15);

        // Here we only use two temporaries in temp[0] and temp[1].
        // The two temps are alternatively used as factor and multiplication 
        // result. That is, first is temp[0] a factor and the product is 
        // computed in temp[1] and then temp[1] becomes a factor and the
        // next product is computed in temp[0].
        temp[0] = result;
        temp[1] = temp_arg;

        // t will be either 0 or 1. 
        // temp[t] holds a value and temp[1-t] is a free temp.
        short t = 0;

        // Initialize temp[t] with one. That is, with a montgomerized one!
        temp[t].copy(mont_one);

        short i, j;
        DOUBLE_DIGIT_TYPE bit;
        short exponent_index;

        for(i = 0; i < exponent.get_bignat_length(); i++) {
            exponent.copy_digit(i, buf);
            bit = Bignat.highest_digit_bit;
            while(bit != 0) {
                // square akku
                #ifdef OPT_SPECIAL_SQUARE
                   temp[(short)(1-t)].montgomery_square(temp[t], modulus);
                #else
                   temp[(short)(1-t)].montgomery_mult(temp[t], temp[t], 
                                                      modulus);
                #endif

                #ifdef BIGNAT_TESTFRAME
                   if(Bignat.verbosity >= 15)
                       System.out.format("EM LOOP %d %02X akku %s\n" +
                                         "EM squared %s\n",
                                         i, bit, 
                                         temp[t].to_hex_string(),
                                         temp[1-t].to_hex_string());
                #endif

                t = (short)(1-t);

                exponent_index = 0;
                for(j = (short)(base_factor_size -1); j >= 0; j--) {
                    exponent_index <<= 1;
                    if((buf[j] & bit) != 0)
                        exponent_index += 1;
                }
                // System.out.format("EM base_fac_size %d exponent_index %d\n",
                //                base_factor_size, exponent_index);
                if(exponent_index != 0) {
                    temp[(short)(1-t)].montgomery_mult(temp[t],
                                  base_factors.get((short)(exponent_index -1)),
                                  modulus);
                    // System.out.format("EM fac mult res %s\n",
                    //                temp[1-t].to_hex_string());
                    t = (short)(1-t);
                }

                for(j = base_factor_size; j < length; j++) {
                    // System.out.format("EM mult base %d\n", j);
                    if((buf[j] & bit) != 0) {
                        temp[(short)(1-t)].montgomery_mult(temp[t],
                                                           this.get(j),
                                                           modulus);
                        // System.out.format("EM mult base res %s * %s = %s\n",
                        //                temp[t].to_hex_string(),
                        //                this.a.get(j).to_hex_string(),
                        //                temp[1-t].to_hex_string());
                        t = (short)(1-t);
                    }
                }

                bit >>= 1;
            }
        }
        
        // Copy result into result, which is temp[0] if it is not there yet.
        if(t == 1)
            result.copy(temp[1]);
    }


    /**
     * 
     * Modular multi-exponent with Montgomery multiplication. 
     * Relies internally on {@link
     * RSA_exponent} to compute single exponents. They are then
     * multiplied with <a
     * href="package-summary.html#montgomery_factor">Montgomery
     * multiplication</a>. Computes {@code this.get(0)^exponent.get(0) *
     * this.get(1)^exponent.get(1)...} modulo {@code modulus} up to
     * the length of this vector. The result will be stored in {@code
     * result}. The bases in this vector and the exponents must be in
     * normal form, i.e., not montgomerized. 
     * <P>
     *
     * The argument {@code rsa_vector_correction} is multiplied to the
     * product of all single exponents as a Montgomery correction
     * factor. For two bases two multiplications are performed in
     * total, so the {@code rsa_vector_correction} argument must be
     * mont_fac^2 to obtain a normal result, where mont_fac is the <a
     * href="package-summary.html#montgomery_factor">Montgomery
     * factor</a>. For three bases it must be mont_fac^3, and so on.
     * If the multi-exponent computed here is only an intermediate
     * result, one can of course also use an higher exponent of the
     * Montgomery factor to also adjust for multiplications done
     * outside of this method.
     * <P>
     *
     * Internally three temporaries are needed. The argument {@code
     * result} is used as one of them, the two other temporaries must
     * be passed explicitely. The three arguments {@code result},
     * {@code temp_1} and {@code temp_2} must be pairwise different
     * references and they must also differ from the bases and the
     * modulus.
     * <P>
     *
     * The bases in this object must match the configured size of the
     * {@code rsa_exponent} that is used to compute the single
     * exponents. (This means that the size of the bases must be 2
     * bytes larger than the key size of the {@code rsa_exponent}.)
     * The modulus must have been installed (with {@link
     * RSA_exponent_interface#set_modulus set_modulus}) in the {@code
     * rsa_exponent} prior to calling this method. 
     * <P>
     *
     * This method can only be used if the object has been constructed
     * with {@code init_buf == true}.
     * <P>
     *
     * This method has been written for use on the card with an {@link
     * RSA_exponent} object for doing exponentiations on the crypto
     * coprocessor. However, for testing purposes, it can also be used
     * together with {@link Fake_rsa_exponent}.
     * 
     * @param exponent vector with the exponents, must have the same
     * length as this vector
     * @param modulus modulus
     * @param rsa_exponent RSA exponent object used for single exponentiations
     * @param rsa_vector_correction Montgomery correction factor
     * @param result result and 1st temporary
     * @param temp_1 2nd temporary
     * @param temp_2 3rd temporary
     */
    public void mont_rsa_exponent_mod(Vector exponent, Modulus modulus, 
                                      RSA_exponent_interface rsa_exponent,
                                      Bignat rsa_vector_correction,
                                      Bignat result, 
                                      Bignat temp_1, Bignat temp_2) 
    {
        ASSERT(this.length == exponent.length);

        // Here we use three temporaries in the the temp array. They are
        // used cyclicly as factors and result. The short t below
        // will always contain the index of the result. Therefore 
        // temp[(t+1) % 3] is the first factor and temp[(t+2) % 3] the 
        // second factor.
        temp[0] = result;
        temp[1] = temp_1;
        temp[2] = temp_2;

        // t will be either 0,1 or 2.
        short t = 0;
        // t_pre and t_succ are, respectively, the predecessor and successor
        // of t in the ring modulo 3. That is, we have always
        //   t_pre == (t + 2) % 3     and
        //   t_succ == (t + 1) % 3
        short t_pre = 2;
        short t_succ = 1;

        // Initialize temp[t] with the Montgomery correction for 
        // base.length multiplications.
        temp[t].copy(rsa_vector_correction);

        for(short i = 0; i < this.length; i++) {
            #ifdef HOST_TESTFRAME
                if(out != null)
                    out.format("rsa exp round %d\n" +
                               "   akku = %s\n" +
                               "   base = %s\n" +
                               "   exp  = %s\n",
                               i,
                               temp[t].to_hex_string(),
                               this.get(i).to_hex_string(),
                               exponent.get(i).to_hex_string());
            #endif

            t = (short)((short)(t + 1) % 3);
            t_pre = (short)((short)(t + 2) % 3);
            t_succ = (short)((short)(t + 1) % 3);

            // Invariant: Now, after increasing t,
            // the last multiplication result is in temp[t_pre],
            // now used as a factor.
            // temp[t] is at this point unused and will get the result
            // of this round.
            // temp[t_succ] is also unused and will get the second factor.
            rsa_exponent.power(this.get(i), exponent.get(i), 
                               temp[t_succ], (short)2);
            temp[t].montgomery_mult(temp[t_pre], temp[t_succ], modulus);
        }

        // The result is now in temp[t], copy it into result, if t != 0.
        if(t != 0)
            result.copy(temp[t]);
    }


    /**
     * 
     * Modular multi-exponent with squared multiplication. Relies
     * internally on {@link RSA_exponent} to compute single exponents.
     * They are then multiplied with squared multiplication ({@link
     * Bignat#squared_rsa_mult_2 Bignat.squared_rsa_mult_2} or {@link
     * Bignat#squared_rsa_mult_4 Bignat.squared_rsa_mult_4} depending
     * on <a
     * href="../../../overview-summary.html#SQUARED_RSA_MULT">SQUARED_RSA_MULT<a>).
     * Computes 
     * {@code this.get(0)^exponent.get(0) *
     * this.get(1)^exponent.get(1)...} modulo {@code modulus} up to
     * the length of this vector. The result will be stored in {@code
     * result}. The bases in this vector and the exponents must be in
     * normal form, i.e., not montgomerized. <P>
     *
     * Internally four temporaries are needed. The argument {@code
     * result} is used as one of them, the three other temporaries
     * must be passed explicitely. The four arguments {@code result},
     * {@code temp_1}, {@code temp_2} and {@code temp_3} must be
     * pairwise different references and they must also differ from
     * the bases and the modulus.
     * <P>
     *
     * Both RSA exponents {@code rsa_exponent} and {@code
     * square_exponent} must be configured for the same size. {@code
     * square_exponent} must have the modulus {@code modulus} and an
     * exponent of 2 installed before entering this method. {@code
     * rsa_exponent} must have the modulus {@code modulus} installed
     * before calling this method.
     * <P>
     *
     * The bases in this object, the temporaries and the result must
     * have the same length, which must be identical to the configured
     * size of both {@code rsa_exponent} and {@code square_exponent}.
     * <P>
     *
     * This method can only be used if the object has been constructed
     * with {@code init_buf == true}.
     * <P>
     *
     * This method has been written for use on the card with an {@link
     * RSA_exponent} object for doing exponentiations on the crypto
     * coprocessor. However, for testing purposes, it can also be used
     * together with {@link Fake_rsa_exponent}.
     * 
     * @param exponent vector with the exponents, must have the same
     * length as this vector
     * @param modulus modulus
     * @param result result and 1st temporary
     * @param rsa_exponent RSA exponent object used for single
     * exponentiations with arbitrary exponents
     * @param square_exponent RSA exponents object used for squaring
     * inside {@link Bignat#squared_rsa_mult_2 Bignat.squared_rsa_mult_2}
     * @param temp_1 2nd temporary
     * @param temp_2 3rd temporary
     * @param temp_3 4th temporary
     */
    public void squared_rsa_exponent_mod(Vector exponent, Modulus modulus, 
                                         Bignat result, 
                                         RSA_exponent_interface rsa_exponent,
                                         RSA_exponent_interface square_exponent,
                                         Bignat temp_1, Bignat temp_2,
                                         Bignat temp_3) 
    {
        ASSERT(this.length == exponent.length);
        ASSERT(this.length >= 1);
        ASSERT(temp != null);

        // We use two temporaries in the temp array as akkumulator.
        // One of them holds the intermediate result of the previous
        // round which is then used as a factor to compute the
        // (intermediate) result of the current round. Third temporary
        // temp_2 is always used for the i-th exponent. Fourth
        // temporary temp_3 is only used in squared multiplication.
        temp[0] = temp_1;
        temp[1] = result;

        // t will be either 0 or 1. temp[t] will hold the current
        // intermediate result, while temp[1-t] will be free and get
        // the next result.
        short t = 0;

        // Compute the first exponent into temp[t] == temp_1.
        rsa_exponent.power(this.get((short)0), exponent.get((short)0), 
                           temp_1, (short)0);

        for(short i = 1; i < this.length; i++) {
            #ifdef HOST_TESTFRAME
                if(out != null)
                    out.format("squared rsa exp round %d\n" +
                               "   akku = %s\n" +
                               "   base = %s\n" +
                               "   exp  = %s\n",
                               i,
                               temp[t].to_hex_string(),
                               this.get(i).to_hex_string(),
                               exponent.get(i).to_hex_string());
            #endif

            // Invariant: The last multiplication result is in
            // temp[t], now used as a factor. temp[1-t] is at this
            // point unused and will get the result of this round.
            rsa_exponent.power(this.get(i), exponent.get(i), 
                               temp_2, (short)0);

            temp[(short)(1 - t)].SQUARED_RSA_MULT(temp[t], temp_2, modulus,
                                                  square_exponent, temp_3);
            t = (short)(1-t);
        }

        // The result is now in temp[t], copy it into result, if t != 1.
        if(t != 1)
            result.copy(temp[t]);
    }



    //########################################################################
    // Serializable_array support
    // 


    /**
     *
     * Compatibility check for the OV-chip protocol layer. Vector is
     * compatible with
     * <UL><LI>vectors of the same length, containing
     * Bignat's of the same size.</LI>
     * 
     * <LI>{@link Host_vector Host_vectors} of the same length and
     * whose configured bignat size equals the size of the Bignat's in
     * this vector. </LI>
     * 
     * </Ul>
     *
     *
     * See <a href="../util/APDU_Serializable.html#apdu_compatibility">
     * the compatibility check 
     * explanations</a> and also
     * {@link ds.ov2.util.APDU_Serializable#is_compatible_with 
     * APDU_Serializable.is_compatible_with}.
     *
     * @param o actual argument or result
     * @return true if this (the declared argument or result) is considered 
     *         binary compatible with {@code o}.
     */
    public boolean is_compatible_with(Object o) {
        if(o instanceof Vector) {
            Vector vo = (Vector)o;
            return this.length == vo.length && 
                this.get_bignat_size() == vo.get_bignat_size();
        }
        #ifndef JAVACARD_APPLET
            else if(o instanceof Host_vector) {
                Host_vector ho = (Host_vector)o;
                // System.out.format("YY bs %d %d l %d %d\n", 
                //                get_bignat_size(), ho.bignat_size,
                //                this.length, ho.get_length());
                return this.length == ho.get_length() &&
                    this.get_bignat_size() == ho.bignat_size;
            }
        #endif
        return false;
    }
}
