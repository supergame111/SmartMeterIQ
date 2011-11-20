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
// Created 4.11.08 by Hendrik
// 
// all the static data of the applet
// 
// $Id: RSA_data.java,v 1.25 2010-02-16 10:26:08 tews Exp $

#include <config>

#include "bignatconfig"


#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.front;
#endif

#ifndef JAVACARD_APPLET
   import ds.ov2.util.APDU_byte;
   import ds.ov2.bignat.Bignat;
   import ds.ov2.bignat.Modulus;
   import ds.ov2.bignat.Vector;
#endif


/** 
 * Data structures for the OV-chip RSA applet. Together with precisely
 * one of {@link RSA_plain_card} or {@link RSA_mont_card} this class
 * implements the protocols for the RSA applet. With {@link
 * RSA_plain_card} it yields the plain RSA applet, with {@link
 * RSA_mont_card} it yields the Montgomerizing RSA applet. This class
 * contains the static and some transient data. Some other data and
 * the methods are in {@link RSA_plain_card} and {@link
 * RSA_mont_card}. The distribution of the data between this class and
 * {@link RSA_plain_card}/{@link RSA_mont_card} is very simple: All
 * variables that take part in the communication with the host are in
 * this class, the remainder is in {@link RSA_plain_card}/{@link
 * RSA_mont_card}. Therefore the host driver only has to include this
 * class but not {@link RSA_plain_card} or {@link RSA_mont_card}. <P>
 *
 * For the RSA applet there are three sizes of bignats: short ones,
 * double short ones and long ones. The short bignats appear in the
 * exponents, they should be 100-200 bits long. The short bignats are
 * never combined with Montgomery multiplication, they therefore never
 * contain Montgomery digits. The long bignats are the bases, the
 * blinding and the modulus. They should be 1000-2000 bits long. The
 * long bignats are combined with Montgomery multiplication. They must
 * therefore always have two leading zero digits (i.e., two leading
 * zero bytes or shorts), the <a
 * href="../bignat/package-summary.html#montgomery_factor">Montgomery
 * digits</a>. Some computations with the exponent numbers have
 * intermediate results of the double size. For these there is one
 * variable of double short length (200-400 bits).
 * <P>
 *
 * <a name="applet-state"></a>
 * The applet is always in one of four states, see {@link #state}:
 * <DL>
 * <DT>{@link #UNALLOCTED}
 * <DD>After applet installation until the allocate protocol ({@link
 * RSA_squared_card#allocate}) is
 * run. In this state only the allocate protocol is enabled. All other
 * entries are null in {@link Front_protocols} in this state.
 * <DT>{@link #UNINITIALIZED}
 * <DD>After the allocate protocol ({@link RSA_squared_card#allocate})
 * finished and before the
 * initialize protocol ({@link RSA_squared_card#initialize})
 * is run. In this state only the 
 * initialize protocol is enabled. From this state on all protocols
 * are represented with non-null references in {@link
 * Front_protocols}. In this state all other protocols beside 
 * initialize are disabled with <a
 * href="../../../overview-summary.html#ASSERT">ASSERT</a>. 
 * <DT>{@link #INITIALIZED}
 * <DD>After the initialize protocol until the first resign
 * protocol. In this state the applet is fully initialized but has a blinding
 * that the host knows. Therefore the applet waits for the first
 * resign protocol run which must be part of the personalization
 * procedure. In this state only the resign protocol is enabled. All
 * the other protocols are disabled with <a
 * href="../../../overview-summary.html#ASSERT">ASSERT</a>. 
 * <DT>{@link #BLINDED}
 * <DD>After the first resign protocol until the
 * applet is deinstalled. In this state only the proof and resign
 * protocols are enabled. The other ones are disabled with <a
 * href="../../../overview-summary.html#ASSERT">ASSERT</a>. 
 * </DL>
 * The debug protocols contain a reset protocol that in any state can
 * reset the applet to the {@link #UNALLOCTED} state. For Java Cards
 * the use of reset will lead to uncollected garbage on the card (the
 * reset protocol has been implemented for the phone).
 *
 * @author Hendrik Tews
 * @version $Revision: 1.25 $
 * @commitdate $Date: 2010-02-16 10:26:08 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#HOST_TESTFRAME">HOST_TESTFRAME<a>,
 *   <a href="../../../overview-summary.html#DIGIT_TYPE">DIGIT_TYPE<a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
 */
PUBLIC class RSA_data {

    /**
     * 
     * Invalid applet identification constant, with value {@value}.
     */
    public final static byte INVALID_APPLET_ID = 0x00;


    /**
     * 
     * Plain RSA Applet identification constant, with value {@value}.
     */
    public final static byte PLAIN_RSA_APPLET = 0x02;


    /**
     * 
     * Montgomerized RSA Applet identification constant, with value {@value}.
     */
    public final static byte MONT_RSA_APPLET = 0x03;


    /**
     * 
     * Squaring RSA applet identification constant, with value {@value}.
     */
    public final static byte SQUARED_RSA_APPLET = 0x04;


    /**
     * 
     * Square 4 RSA applet identification constant, with value {@value}.
     */
    public final static byte SQUARED4_RSA_APPLET = 0x05;


    /**
     * 
     * Applet identification. Initialized in {@link #allocate}.
     */
    /* package */ APDU_byte applet_id;


    /**
     * 
     * <a href="#applet-state">State</a> constant for the state
     * UNALLOCTED, value {@value}. See the <A
     * HREF="#applet-state">applet state description</A>.
     */
    public static final short UNALLOCTED = (short)0;


    /**
     * 
     * <a href="#applet-state">State</a> constant for the state
     * UNINITIALIZED, value {@value}. See the <A
     * HREF="#applet-state">applet state description</A>. 
     */
    public static final short UNINITIALIZED = (short)1;


    /**
     * 
     * <a href="#applet-state">State</a> constant for the state
     * INITIALIZED, value {@value}. See the <A
     * HREF="#applet-state">applet state description</A>. 
     */
    public static final short INITIALIZED = (short)2;


    /**
     * 
     * <a href="#applet-state">State</a> constant for the state
     * BLINDED, value {@value}. See the <A HREF="#applet-state">applet
     * state description</A>. 
     */
    public static final short BLINDED = (short)3;
    

    /**
     * 
     * <a href="#applet-state">State</a> field. Holds one of the state
     * constants. See the <A HREF="#applet-state">applet state
     * description</A>.
     */
    public short state;


    /**
     * 
     * The RSA parameter n. Product of two primes and modulus for all 
     * the computations. Initialized once during the personalization of
     * the applet.
     * <P>
     * This modulus contains a long Bignat.
     * 
     */
    public Modulus n;


    /**
     * 
     * Digit mask of the most significant byte of the modulus {@link
     * #n}. For generating random numbers less than {@link #n}.{@link
     * Modulus#m m} we need a mask for the most significant byte in
     * n.m. The mask will mask everything above the most significant
     * bit in n.m. Initialized once during personalization of the
     * applet (in {@link RSA_plain_card#initialize}/{@link
     * RSA_mont_card#initialize}). <P>
     *
     * Has type <a
     * href="../../../overview-summary.html#DIGIT_TYPE">DIGIT_TYPE<a>.
     * 
     */
    public DIGIT_TYPE mod_first_digit_mask;


    /**
     * 
     * The RSA parameter v. Prime and coprime to phi({@link #n}),
     * where phi is Eulers phi function. It will always be aliased in
     * {@link #current_attributes}{@link #attribute_length
     * [attribute_length]} and {@link #new_attributes}{@link
     * #attribute_length [attribute_length]}. <P> This is a short
     * Bignat.
     * 
     */
    public Bignat v;


    /**
     * 
     * Digit mask of the most significant byte of {@link #v}. Needed
     * for generating random numbers less then {@link #v}.
     * Initialized once during personalization of the applet (in
     * {@link RSA_plain_card#initialize}/{@link RSA_mont_card#initialize}). 
     * <P>
     *
     * Has type <a
     * href="../../../overview-summary.html#DIGIT_TYPE">DIGIT_TYPE<a>.
     * 
     */
    public DIGIT_TYPE v_first_digit_mask;


    /**
     * 
     * The public key of service provider. PTLS stands for Public Transport
     * Logistics Services. The key equals {@code x}^{@link #v}, where {@code
     * x} is the unknown private key.
     * <P>
     * This is a long Bignat.
     * 
     */
    public Bignat ptls_key;


    /**
     * 
     * The number of attributes this card possesses. Many arrays have
     * size attribute_length + 1 to accomodate for the blinding. But
     * there are also exceptions: {@link #remainders} has only
     * size attribute_length.
     * 
     */
    public short attribute_length;


    /**
     * 
     * Bases. The bases g_1 .. g_n are stored in bases[0] to
     * bases[n-1], where n is the number of attributes (see {@link
     * #attribute_length}). bases[{@link #attribute_length}] is always
     * an alias to either the current blinding {@link
     * #current_blinding} or the current beta. There is no guarantee
     * on what is contained in bases[{@link #attribute_length}], so
     * always set it before use with either {@link
     * #set_current_blinding} or {@link #set_blinding set_blinding}.
     * <P>
     * bases has length {@link #attribute_length} + 1. It is filled with
     * long Bignats.
     * 
     */
    public Vector bases;


    /**
     * 
     * Set current blinding. Alias {@link #bases}{@link
     * #attribute_length [attribute_length]} to {@link
     * #current_blinding}.
     */
    public void set_current_blinding() {
        bases.set(attribute_length, current_blinding);
        return;
    }


    /**
     * 
     * Set beta. Alias {@link #bases}{@link
     * #attribute_length [attribute_length]} to {@code beta}.
     */
    public void set_blinding(Bignat beta) {
        bases.set(attribute_length, beta);
        return;
    }


    /**
     * 
     * Base factors needed for {@link Vector#exponent_mod
     * Vector.exponent_mod}. The array contains the products of all
     * possible combinations of the bases in montgomerized form with
     * one exception: the empty product is not contained. See also
     * {@link ds.ov2.bignat.Host_vector#make_montgomerized_factors
     * Host_vector.make_montgomerized_factors}.
     * <P>
     *
     * These factors are only needed in the Montgomerizing applet,
     * therefore, the length of this array is 1 on the plain applet
     * (zero-length vectors do not exists, see the {@link
     * ds.ov2.bignat.Bignat_array#Bignat_array(short) Bignat_array constructor}).
     * On the Montgomerizing applet the length is 2^{@link
     * #attribute_length} -1. The array contains
     * long bignats.
     */
    public Vector base_factors;


    /**
     * 
     * Current attributes. Index {@link #attribute_length} is always
     * an alias of {@link #v}.
     * <P>
     * At the end of the resign protocol the {@code
     * current_attributes} and the {@link #new_attributes} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     * <P>
     * Length {@link #attribute_length} + 1. Filled with short
     * bignats.
     */
    public Vector current_attributes;


    /**
     * 
     * New attributes. The new attributes are obtained at the
     * beginning of the resign protocol. Because the resign step can
     * fail we have to build up the new attributes in a different
     * place. 
     * <P>
     * Index {@link #attribute_length} is always
     * an alias of {@link #v}.
     * <P>
     * At the end of the resign protocol the {@code
     * new_attributes} and the {@link #current_attributes} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     * <P>
     * Length {@link #attribute_length} + 1. Filled with short
     * bignats.
     */
    public Vector new_attributes;


    /**
     * 
     * Current blinding. Sometimes aliased in {@link #bases}{@link
     * #attribute_length [attribute_length]}.
     * <P>
     * At the end of the resign protocol the {@code
     * current_blinding} and the {@link #new_blinding} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     * <P>
     * Long Bignat.
     */
    public Bignat current_blinding;

    /**
     * 
     * New blinding. Chosen in the beginning of the resign protocol
     * when it is still unclear whether resigning will succeed.
     * Sometimes aliased in {@link #bases}{@link 
     * #attribute_length [attribute_length]}.
     * <P>
     * At the end of the resign protocol the {@code
     * new_blinding} and the {@link #current_blinding} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     * <P>
     * Long Bignat.
     */
    public Bignat new_blinding;


    /**
     * 
     * Current blinded attribute expression. Equals {@link
     * #bases}^{@link #current_attributes}, where the {@link
     * #current_blinding} has been set in {@link #bases}. 
     * <P>
     * At the end of the resign protocol {@code
     * current_blinded_a} and the {@link #new_blinded_a} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     * <P>
     * Long Bignat.
     */
    public Bignat current_blinded_a;


    /**
     * 
     * New blinded attribute expression. Computed during the resign
     * protocol when it is not yet clear whether the resigning will
     * succeed. Equals {@link
     * #bases}^{@link #new_attributes}, where the {@link
     * #new_blinding} has been set in {@link #bases}. 
     * <P>
     * At the end of the resign protocol {@code
     * new_blinded_a} and the {@link #current_blinded_a} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     * <P>
     * Long Bignat.
     */
    public Bignat new_blinded_a;


    /**
     * 
     * Current signature on the current blinded attribute expression.
     * <P>
     * At the end of the resign protocol the {@code
     * current_signature} and the {@link #new_signature} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     */
    public Signature current_signature;


    /**
     * 
     * New signature on the new blinded attribute expression. Computed
     * during the resign protocol.
     * <P>
     *
     * At the end of the resign protocol the {@code
     * current_signature} and the {@link #new_signature} are
     * atomically swapped. This is done by exchanging the two
     * references in a transaction, see {@link
     * RSA_plain_card#switch_to_new_attributes
     * RSA_plain_card.switch_to_new_attributes} and {@link
     * RSA_mont_card#switch_to_new_attributes
     * RSA_mont_card.switch_to_new_attributes}.
     */
    public Signature new_signature;


    /**
     * 
     * Montgomerized 1. Needed in the Montgomerizing applet for {@link
     * Vector#exponent_mod Vector.exponent_mod}. 
     * <P>
     *
     * Long Bignat.
     */
    public Bignat montgomerized_one;


    /**
     * 
     * Montgomery correction factors. The correction factor for 2
     * factors is stored at index 0, for 3 factors at index 1, and so
     * on. The plain RSA applet needs all correction factors for up to
     * 6 factors. See {@link #get_montgomery_correction
     * get_montgomery_correction} for additional explanations.
     * <P>
     * Length is determined by the {@code mont_correction_len}
     * argument of {@link #allocate allocate}. For the plain RSA applet the
     * length must be at least 5. Filled with long bignats.
     */
    /* package */ Vector montgomery_corrections;


    /**
     * 
     * Return the Montgomery correction factor for {@code factors}
     * factors. In the plain RSA applet non of the numbers is
     * montgomerized. For most computations, however, <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * multiplication</a> is used. Therefore all computations contain
     * an additional Montgomery correction factor. To muliply {@code
     * A} and {@code B} do {@code C x A x B}, where {@code x} denotes
     * Montgomery multiplication and {@code C ==
     * get_montgomery_correction(2)}. 
     * <P>
     *
     * Note that for counting the number of factors the Montgomery
     * correction factor is not counted. Therefore the smallest number
     * of factors is 2.
     * 
     * @param factors number of factors (not counting the Montgomery
     * correction factor)
     * @return the correction factor for {@code factors} factors
     * @throws ArrayIndexOutOfBoundsException if {@code factors} is
     * larger than the maximal correction factor contained in {@link
     * #montgomery_corrections}. 
     */
    public Bignat get_montgomery_correction(short factors) {
        return montgomery_corrections.get((short)(factors -2));
    }


    //########################################################################
    // 
    // Transient data
    // 

    /**
     * 
     * Host commitment alpha for the signature protocol. Not used
     * during the proof protocol. 
     * <P>
     *
     * Long bignat.
     * 
     */
    Bignat host_alpha;


    /**
     * 
     * Host response r for the signature
     * protocol. Not used during the proof protocol.
     * <P>
     * Long bignat.
     * 
     */
    Bignat host_response;


    /**
     * 
     * Remainders vector. Stores the remainders during the proof
     * protocol. In the {@link RSA_plain_card#make_sig_hash}/{@link
     * RSA_mont_card#make_sig_hash} step of the resign protocol it
     * first carries the attribute updates. Later in that step the
     * remainder c of the resign protocol is stored in index 0 via the
     * {@link #sig_remainder} alias. <P>
     *
     * There is no blinding in the remainders. The length is therefore
     * just {@link #attribute_length}. Filled with short Bignats.
     * 
     */
    /* package */ Vector remainders;


    /**
     * 
     * Remainder c in the signature protocol. This is an alias to
     * {@link #remainders}{@code [0]}, because the remainders array is
     * not used when c is needed. 
     * <P>
     *
     * Short bignat.
     */
    Bignat sig_remainder;       // Alias to remainders[0].


    /**
     * 
     * Temporary result. Used to sent results back to the host in the
     * two steps of the proof protocol. Further used as temporary in
     * many computations together with {@link
     * RSA_plain_card#temp_2}/{@link RSA_mont_card#temp_2} and {@link
     * RSA_plain_card#temp_3}/{@link RSA_mont_card#temp_2}. <P>
     *
     * Long bignat.
     */
    /* package */ Bignat result;


    /**
     * 
     * Beta_3 and gamma. Holds beta_3 during the resing protocol and
     * gamma during the proof protocol.
     * <P>
     *
     * Short bignat.
     */
    Bignat gamma_beta_3;


    #if defined(HOST_TESTFRAME) || defined(JAVADOC)
        /**
         * 
         * Debug printer. Used for debug messages in the <a
         * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>.
         * Needs to be set from the outside.
         */
        public static java.io.PrintWriter out = null;
    #endif


    /**
     * 
     * Nonallocating constructor. Allocation must be done explicitely
     * with {@link #allocate allocate}.
     */
    public RSA_data() {
        return;
    }


    /**
     * 
     * Allocate all data and initialize all aliases. The decision
     * whether to allocate the data in RAM (transient memory) or
     * EEProm is hardwired here. It works for cards with about 2K of
     * RAM up to 1952 bit keys. If there is too little RAM or the
     * key size is too big strange things will happen.
     * <P>
     *
     * This allocate here is always called twice! In the normal case, where
     * the host application is talking to a java card one object of this 
     * class exists both on the card and in the host application. Both must
     * execute allocate, of course.
     * <P>
     *
     * In the host testframe, where applet code and host code are linked in 
     * one application there is only one RSA_data object. Then allocate
     * here is called twice on the same object. Once from the action 
     * of the allocate step and once directly from the host driver in 
     * {@link RSA_host_card#host_side_init host_side_init}. There are special
     * provisions to make sure this second call does not reallocate
     * everything. 
     * 
     * @param short_bignat_size size in bytes of the short (exponent) bignats
     * @param long_bignat_size size in bytes of the long (base) bignats
     * @param attribute_length number of attributes (without counting
     * the blinding)
     * @param mont_correction_len length of the {@link
     * #montgomery_corrections} array
     * @param applet_id applet identification
     */
    public void allocate(short short_bignat_size, short long_bignat_size,
                         short attribute_length, short mont_correction_len,
                         byte applet_id) 
    {
        // Host test frame fix to avoid reallocation.
        if(state != UNALLOCTED)
            return;
        this.applet_id = new APDU_byte(applet_id);

        n = new Modulus(long_bignat_size, false);
        if(applet_id == SQUARED4_RSA_APPLET) {
            n.allocate_multiples();
        }

        v = new Bignat(short_bignat_size, false);

        ptls_key = new Bignat(long_bignat_size, false);

        this.attribute_length = attribute_length;

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);

        bases = new Vector((short)(attribute_length + 1), true);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);

        // bases.set(attribute_length, current_blinding) see below
        for(short i = 0; i < attribute_length; i++)
            bases.set(i, new Bignat(long_bignat_size, false));

        switch(applet_id){
        case MONT_RSA_APPLET:
            base_factors = new Vector(long_bignat_size,
                                      (short)((1 << attribute_length) -1),
                                      false, false);
            break;
        case PLAIN_RSA_APPLET:
        case SQUARED_RSA_APPLET:
        case SQUARED4_RSA_APPLET:
            base_factors = new Vector(long_bignat_size, (short)1,
                                      false, false);
            break;
        default:
            ASSERT(false);
        }

        current_attributes = new Vector((short)(attribute_length + 1), false);
        current_attributes.set(attribute_length, v);
        for(short i = 0; i < attribute_length; i++)
            current_attributes.set(i, new Bignat(short_bignat_size, false));

        new_attributes = new Vector((short)(attribute_length + 1), false);
        new_attributes.set(attribute_length, v);
        for(short i = 0; i < attribute_length; i++)
            new_attributes.set(i, new Bignat(short_bignat_size, false));

        current_blinding = new Bignat(long_bignat_size, false);
        bases.set(attribute_length, current_blinding);
        new_blinding = new Bignat(long_bignat_size, false);

        current_blinded_a = new Bignat(long_bignat_size, false);
        new_blinded_a = new Bignat(long_bignat_size, false);

        current_signature = 
            new Signature(short_bignat_size, long_bignat_size, applet_id);
        new_signature = 
            new Signature(short_bignat_size, long_bignat_size, applet_id);

        montgomerized_one = new Bignat(long_bignat_size, false);

        montgomery_corrections = 
            new Vector(long_bignat_size, mont_correction_len, false, false);

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        // On the squaring and the square 4 applet we need one more
        // temporary. Therefore, on these applets host_alpha needs to
        // be allocated in EEPROM.
        host_alpha = new Bignat(long_bignat_size, 
                                (applet_id != SQUARED_RSA_APPLET) &&
                                (applet_id != SQUARED4_RSA_APPLET));
        host_response = new Bignat(long_bignat_size, true);

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        remainders = new Vector(short_bignat_size, attribute_length, 
                                false, true);

        sig_remainder = remainders.get((short)0);

        result = new Bignat(long_bignat_size, true);

        gamma_beta_3 = new Bignat(short_bignat_size, true);
    }
}
