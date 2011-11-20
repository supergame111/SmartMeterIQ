//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/gui/../bignat/RSA_exponent_interface.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/gui/../bignat/RSA_exponent_interface.java"
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
// Created 1.2.09 by Hendrik
// 
// common interface for RSA_exponent and Fake_rsa_exponent
// 
// $Id: RSA_exponent_interface.java,v 1.5 2009-05-16 21:43:33 tews Exp $

//# 1 "./config" 1
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
// Created 18.12.08 by Hendrik
// 
// preprocessor config directives
// 
// $Id: config,v 1.6 2010-02-18 14:02:08 tews Exp $
//# 51 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/gui/../bignat/RSA_exponent_interface.java" 2




  package ds.ov2.bignat;



/**
 * 
 * Common interface of {@link RSA_exponent} and {@link
 * Fake_rsa_exponent}. {@link RSA_exponent} does only run on the card
 * and {@link Fake_rsa_exponent} is a compatible drop-in replacement
 * of {@link RSA_exponent} that only runs on the host. This interface
 * makes it possible to make some code independent of the
 * implementation used.
 * <P>
 *
 * Besides the methods specified here, implementing classes must
 * implement two constructors, an allocating and a non-allocating one.
 * Because the allocation functionality is factored out in {@link
 * #allocate allocate} the allocating constructor calls the
 * non-allocating constructor and {@link #allocate allocate}. 
 * <P>
 *
 * Classes implementing this interface might internally remember the
 * key size that is set in {@link #allocate} and the allocating
 * constructor. Certain arguments of the other methods must then match
 * this key size in some way.
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 * <P>
 *
 *
 * I would be grateful if somebody could explain me why I cannot
 * specify constructors here. And don't tell me that one cannot call
 * constructors of interfaces. I don't want to do that. I want to
 * specify the constructors here in order to put constraints on
 * implementing classes. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.5 $
 * @commitdate $Date: 2009-05-16 21:43:33 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>
 *
 */
public interface RSA_exponent_interface {

    /**
     *
     * (Re-)Initialize those internal data structures that depend on
     * the key size. The key size is the effective size of the
     * numbers, without Montgomery digits, if any. Therefore, if <a
     * href="package-summary.html#montgomery_factor">Montgomery
     * multiplication</a> is used the key size must be two less than the
     * {@link Bignat#size() size} of the base and modulus.
     * <P>
     *
     * It is not recommended to call this method from the outside,
     * because uncollected garbage might be left.
     * 
     * @param key_byte_size the key size in bytes.
     */
    public void init_key(short key_byte_size);


    /**
     *
     * Allocate internal data structures. Call {@link #init_key
     * init_key} internally. The key is initialized to size {@code
     * key_byte_size} with {@link #init_key init_key}. This key size
     * determins the size of the Bignat arguments for all the other
     * methods. The key size is the effective size of the numbers,
     * without Montgomery digits, if any. Therefore, if <a
     * href="package-summary.html#montgomery_factor">Montgomery
     * multiplication</a> is used the key size must be two less than
     * the {@link Bignat#size() size} of the base and modulus. <P>
     *
     * This method should only used once and only if the
     * non-allocating constructor has been used to create this object.
     * (The allocating constructor calls this method itself.) 
     * <P>
     * 
     * @param key_byte_size key size in bytes
     */
    public void allocate(short key_byte_size);


    /**
     *
     * Set modulus. Must be called before {@link #power power} or
     * before {@link #set_exponent set_exponent}. Once set a modulus
     * is used for all subsequently computed exponents until it is
     * changed via this method. The {@code offset} argument specifies
     * the number of ditits that {@code mod} is longer than the
     * configured key size. If {@code offset} is different from {@code
     * 0} all digits left of {@code offset} are assumed to be zero.
     * For a modulus that is used with Montgomery multiplication use
     * an {@code offset} of 2. <P>
     *
     * If {@link #fixed_power fixed_power} is used, {@link #set_exponent
     * set_exponent} must be called (again) after setting the modulus.
     * 
     * @param mod modulus
     * @param offset starting index of the most significant digit of
     * the modulus
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_MOD_FAILURE} if the modulus
     * has the wrong size
     */
    public void set_modulus(Bignat mod, short offset);


    /**
     *
     * Initialize the exponent for subsequent use of this object with
     * {@link #fixed_power fixed_power}. This is advantageous if more than one
     * power is computed with the same exponent, because exponent
     * initializtion amounts for about 60% (for short keys sizes) to
     * 30% (for long key sizes) of the total computation time. <P>
     *
     * The modulus must have been set with {@link #set_modulus
     * set_modulus} before calling this method. <P>
     *
     * The temporary is used here to permit exponents which are
     * shorter than the configured key size of the underlying cipher.
     * The {@code offset} argument specifies the number of digits
     * that {@code temp} is larger then the configured key size. For a
     * temporary that is used with <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * multiplication </a> use an offset of {@code 2}.
     * <P>
     *
     * @param exp exponent
     * @param temp temporary
     * @param offset number of digits {@code temp} is longer than the
     * configured key size of the underlying cipher object
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_EXP_FAILURE} if setting the
     * exponent fails (which I believe happens for invalid key sizes
     * that are not reported as such when initiliazing the key)
     */
    public void set_exponent(Bignat exp, Bignat temp, short offset);


    /**
     *
     * Modular power with preconfigured modulus and exponent. Sets
     * {@code result} to {@code base}^{@code exp} mod {@code modulus},
     * where the {@code modulus} and {@code exp} must have been
     * configured before with {@link #set_modulus set_modulus} and
     * {@link #set_exponent set_exponent}, respectively. Note that
     * {@link #set_modulus set_modulus} must always be called before
     * {@link #set_exponent set_exponent}. <P>
     *
     * Using this method makes sense when more than one power is
     * computed with the same modulus and exponent. Measurements show
     * that initializing the modulus and the exponent amounts for
     * about 60% (for short keys sizes) to 30% (for long key sizes) of
     * the total computation time. This method is merely a wrapper
     * around {@link javacardx.crypto.Cipher#doFinal doFinal}. <P>
     *
     * The argument {@code offset} specifies the number of digits that
     * the {@code base} and the {@code result} are longer than the
     * configured key size. For bases and results that are used with
     * <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * multiplication </a> use an {@code offset} of {@code 2}.
     * <P>
     *
     * {@code base} and {@code result} must not be the same reference,
     * otherwise the cipher may produce incorrect results.
     * <P>
     * 
     * @param base 
     * @param result reference for storing the result
     * @param offset number of digits {@code base} and {@code result}
     * is longer than the configured key size 
     */
    public void fixed_power(Bignat base, Bignat result, short offset);


    /**
     *
     * Modular power. Sets {@code result} to {@code base}^{@code
     * exp} mod {@code modulus}, where the {@code modulus} must have
     * been configured before with {@link #set_modulus set_modulus}.
     * <P>
     *
     * The argument {@code offset} specifies the index of the most
     * significant digits of {@code base} and {@code result}. If
     * {@code base} and {@code result} 
     * are used with <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * multiplication</a> the {@code offset} should be {@code 2}.
     * <P>
     *
     * The exponent must fit into key size many bytes but can
     * otherwise be arbitrarily long. <P>
     *
     * {@code base} and {@code result} must not be the same reference,
     * because {@code result} the exponent is first copied into {@code
     * result} to permit exponents shorter than key size. Moreover the
     * cipher on the card does not permit them to be the same
     * reference.
     * 
     * @param base
     * @param exp exponent
     * @param result reference for storing the result
     */
    public void power(Bignat base, Bignat exp, Bignat result, short offset);

}
