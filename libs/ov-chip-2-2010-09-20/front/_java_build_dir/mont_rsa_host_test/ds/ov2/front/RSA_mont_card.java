//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_mont_card.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_mont_card.java"
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
// Created 5.11.08 by Hendrik
// 
// RSA version of the protocols, only Montgomery multiplication,
// montgomerized communication
// 
// $Id: RSA_mont_card.java,v 1.8 2009-06-18 11:57:39 tews Exp $

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
// Created 5.11.08 by Hendrik
// 
// preprocessor config directives
// 
// $Id: config,v 1.16 2010-02-18 12:40:38 tews Exp $
//# 200 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 27 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_mont_card.java" 2




  package ds.ov2.front;



import javacard.framework.ISOException;





   import java.util.Random;
   import ds.ov2.util.Misc;
   import ds.ov2.util.Misc_host;
   import ds.ov2.util.Response_status;
   import ds.ov2.util.APDU_boolean;
   import ds.ov2.util.Message_digest_wrapper;
   import ds.ov2.bignat.Bignat;
   import ds.ov2.bignat.Vector;
   import ds.ov2.bignat.Fake_rsa_exponent;
//# 61 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_mont_card.java"
/** 
 * 
 * Protocol methods for the Montgomerizing RSA applet. The code here
 * is very similar to {@link RSA_plain_card} and {@link
 * RSA_squared_card}. There are however enough differences to make a
 * gerneralized unified class a mess. All three applets, the
 * Montgomerizing, the plain RSA and the squaring RSA applet are very
 * similar. For simplicity even all protocol steps are identical
 * (although not all of the data that is transfered during applet
 * personalization is used in all applets). Most differences between
 * the two applets are just treated by conditionals. Only for this
 * class the differences are too big to cover them all nicely in
 * conditionals. 
 * <P>
 *
 * The Montgomerizing RSA applet computes everything on the JCVM,
 * without using the crypto coprocessor. Everything is done with
 * Montgomery muliplication. To spare some (de)montgomerizations on
 * the card many numbers in the communication with the host are
 * montgomerized. The applet needs no Montgomery correction factors,
 * but it needs 2 Montgomery digits.
 * <P>
 *
 * This class contains the methods that are called from the actions in
 * the protocol steps. Some of the necessary temporary values are
 * defined and allocated here. Most of the necessary data is however
 * in {@link RSA_data}. The temporary values defined here do not take
 * part in the communication with the host. Therefore this class does
 * not need to be present in the host driver. <P>
 *
 * The applet is always in one of four states, see the explanations in
 * {@link RSA_data}. 
 * <P>
 *
 * All computations use solely Montgomery multiplication. Therefore
 * everything (except for the exponents) has to be montgomerized. Data
 * received from and sent to the host is Montgomerized too.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.8 $
 * @commitdate $Date: 2009-06-18 11:57:39 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#RANDOM">RANDOM<a>,
 *   <a href="../../../overview-summary.html#RSA_EXPONENT">RSA_EXPONENT<a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>,
 *   <a href="../../../overview-summary.html#MESSAGE_DIGEST">MESSAGE_DIGEST<a>,
 *   <a href="../../../overview-summary.html#HOST_TESTFRAME">HOST_TESTFRAME<a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>,
 *   <a href="../../../overview-summary.html#CARD_SIGNATURE_ALWAYS_ACCEPT">CARD_SIGNATURE_ALWAYS_ACCEPT<a>
 */
public class RSA_mont_card {

    /**
     * 
     * Registered protocols instance. Needed here to update the
     * protocol steps after resigning. (Resinging changes to new
     * attributes, blindings, etc, by atomically switching references,
     * see {@link #switch_to_new_attributes switch_to_new_attributes}.)
     */
    Front_protocols front_protocols;


    /**
     * 
     * Static data and data send and received from the host.
     */
    /* package */ RSA_data data;


    /**
     * 
     * Temporary. Used together with {@link #temp_3} and {@link
     * RSA_data#result} in various computations.
     * <P>
     *
     * Long bignat.
     */
    private Bignat temp_2;


    /**
     * 
     * Temporary. Used together with {@link #temp_2} and {@link
     * RSA_data#result} in various computations.
     * <P>
     *
     * Long bignat.
     */
    private Bignat temp_3;


    /**
     * 
     * Alphas and quotients. Keeps the alphas in the proof step until
     * the remainders and quotients are computed. At that point the
     * alphas are not needed any more. Therefore, this vector is used
     * immediately afterwards for the quotients. (The remainders go
     * into {@link RSA_data#remainders}.) 
     * <P>
     *
     * The last element with index {@link RSA_data#attribute_length}
     * is always an alias of either {@link RSA_data#v} or {@link
     * RSA_data#gamma_beta_3}.
     * <P>
     *
     * In the resign protocol the vector is not used except for its
     * first entry (at index 0), which aliased under {@link
     * #sig_quotient} and which stores the quotient there.
     * <P>
     *
     * Length {@link RSA_data#attribute_length} + 1. Filled with short
     * bignats. 
     */
    private Vector alphas;


    /**
     * 
     * Intermediate value of double small size. This is the only
     * double small bignat, with twice the size of the exponents. It
     * is used in many places for intermediate results, especially
     * where the exponents are manipulated and then a quotient and a
     * remainder is computed. Also used for the attribute updates in
     * the resign protocol. 
     */
    private Bignat double_small;


    /**
     * 
     * Random beta_1 from the resign protocol and beta from the proof
     * protocoll. 
     * <P>
     *
     * Long bignat.
     */
    Bignat beta_1;


    /**
     * 
     * Random beta_2 from the resign protocol. Not used in the proof
     * protocol. 
     * <P>
     *
     * Long bignat.
     */
    Bignat beta_2;


    /**
     * 
     * Temporary blinded attribute expression. In the resign protocol
     * the attribute expression consisting of the new attributes and
     * the old blinding is temporarily needed. 
     * <P>
     *
     * Long bignat.
     */
    Bignat temp_blinded_a;


    /**
     * 
     * Qutient d in the signature protocol. (The remainder is in
     * {@link RSA_data#sig_remainder}). Alias of {@link
     * #alphas}[0], which are otherwise not used during resigning.
     * <P>
     *
     * Short bignat.
     */
    Bignat sig_quotient; // Alias to alphas[0].


    /**
     * 
     * Random number generator of type <a
     * href="../../../overview-summary.html#RANDOM">RANDOM<a>.
     */
    private Random rand;


    /**
     * 
     * Message digest for computing 160 bit SHA-1. Has type <a
     * href="../../../overview-summary.html#MESSAGE_DIGEST">MESSAGE_DIGEST<a>.
     */
    Message_digest_wrapper hash;


    /**
     * 
     * Temporary base vector of length 2. Used in the resign and proof
     * protocols to compute a multi-exponent of length 2. On the two
     * locations the bases are completely differend and are, in part,
     * temporary numbers. Therefore this base vector is only filled
     * with aliases of other bignats. 
     * <P>
     *
     * Contains precisely two aliases of long bignats.
     */
    private Vector temp_base_vec;


    /**
     * 
     * Temporary exponent vector of length 2. Used in the resign and
     * proof protocols to compute a multi-exponent of length 2. On the
     * two locations the exponents are completely differend and are,
     * in part, temporary numbers. Therefore this base vector is only
     * filled with aliases of other bignats. <P>
     *
     * Contains precisely two aliases of short bignats.
     */
    private Vector temp_exp_vec;



        /**
         * 
         * Debug printer. Used for debug messages in the <a
         * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>.
         * Needs to be set from the outside.
         */
        public static java.io.PrintWriter out = null;



    /**
     * 
     * Non-allocating constructor. Sets the {@link #data} and {@link
     * #front_protocols} fields. Sets the <a
     * href="RSA_data.html#applet-state">State</a> to {@link
     * RSA_data#UNALLOCTED UNALLOCTED} when finished.
     * Allocation of all the bignats and vectors has to be done
     * explicitely with {@link #allocate}.
     * 
     * @param data the data instance
     * @param front_protocols the protocols instance
     */
    public RSA_mont_card(RSA_data data, Front_protocols front_protocols) {
        this.data = data;
        this.front_protocols = front_protocols;
        data.state = data.UNALLOCTED;
        return;
    }


    /**
     * 
     * Allocate all data of the applet. Main method of the only step
     * in the allocate protocol. The object structure of the applet up
     * to {@link RSA_data} and this class is allocated during applet
     * installation. Afterwards this method must be called in order to
     * allocate all further data structures (mainly bignats and
     * vectors). 
     * <P>
     *
     * The decision whether to allocate the data in RAM (transient
     * memory) or EEProm is hardwired here and in {@link
     * RSA_data#allocate}. It works for cards with about 2K of RAM up
     * to 1952 bit keys. If there is too little RAM or the key
     * size is too big strange things will happen.
     * <P>
     *
     * Asserts that the <a href="RSA_data.html#applet-state">state</a>
     * is {@link RSA_data#UNALLOCTED UNALLOCTED}. Sets the state to
     * {@link RSA_data#UNINITIALIZED UNINITIALIZED} when finished (see
     * <A HREF="RSA_data.html#applet-state">applet state
     * description</A>).
     * 
     * @param short_bignat_size size in bytes of the short (exponent) bignats
     * @param long_bignat_size size in bytes of the long (base) bignats
     * @param attribute_length number of attributes (without counting
     * the blinding)
     * @param mont_correction_len length of the {@link
     * RSA_data#montgomery_corrections data.montgomery_corrections} array
     */
    public void allocate(short short_bignat_size, short long_bignat_size,
                         short attribute_length, short mont_correction_len)
    {
        assert(data.state == data.UNALLOCTED);

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        data.allocate(short_bignat_size, long_bignat_size, attribute_length,
                      mont_correction_len, data.MONT_RSA_APPLET);

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        temp_2 = new Bignat(long_bignat_size, true);
        temp_3 = new Bignat(long_bignat_size, true);

        alphas = new Vector((short)(attribute_length + 1), false);
        for(short i = 0; i < attribute_length; i++)
            alphas.set(i, new Bignat(short_bignat_size, true));

        double_small = new Bignat((short)(2 * short_bignat_size), true);

        beta_1 = new Bignat(long_bignat_size, true);
        // Allocate beta_2 in EEPROM to get up to 1952 bit keys
        // beta_2 = new Bignat(long_bignat_size, true);
        beta_2 = new Bignat(long_bignat_size, false);

        // Allocate temp_blinded_a in EEPROM to get up to 1952 bit keys
        // temp_blinded_a = new Bignat(long_bignat_size, true);
        temp_blinded_a = new Bignat(long_bignat_size, false);

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        assert(attribute_length > 0);
        sig_quotient = alphas.get((short)0);

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        hash = Misc.get_message_digest();

        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT) >> 8);
        // ASSERT_TAG(false, javacard.framework.JCSystem.getAvailableMemory
        //            (javacard.framework.JCSystem.CLEAR_ON_DESELECT));

        rand = Misc.get_new_rand();

        // These vectors remain uninitialized. They are filled with
        // aliases just before they are needed.
        temp_base_vec = new Vector((short)2, true);
        temp_exp_vec = new Vector((short)2, false);

        data.state = data.UNINITIALIZED;
    }


    /**
     * 
     * Initialize the plain RSA applet data structures. This is the
     * only action of the only step of the initialize protocol. It
     * must be run after {@link #allocate allocation} and before the
     * first resigning. 
     * <P>
     *
     * Before this method is entered the OV-chip protocol layer
     * transfers the following data as arguments of the initialize
     * step into the right place:
     * <UL>
     * <LI>modulus {@link #data}.{@link RSA_data#n n}</LI>
     * <LI>{@link #data}.{@link RSA_data#v v} (indirectly via {@link
     * RSA_data#current_attributes})</LI>
     * <LI>{@link #data}.{@link RSA_data#ptls_key ptls_key}</LI>
     * <LI>{@link #data}.{@link RSA_data#bases bases}</LI>
     * <LI>{@link #data}.{@link RSA_data#base_factors base_factors}
     * </LI>
     * <LI>{@link #data}.{@link RSA_data#current_attributes
     * current_attributes}</LI>
     * <LI>{@link #data}.{@link RSA_data#current_blinding
     * current_blinding} (indirectly via {@link RSA_data#bases})</LI>
     * <LI>{@link #data}.{@link RSA_data#montgomerized_one}
     * </LI>
     * <LI>{@link #data}.{@link RSA_data#montgomery_corrections
     * montgomery_corrections}</LI>
     * </UL>
     * <P>
     *
     * Asserts that the applet is in state {@link
     * RSA_data#UNINITIALIZED UNINITIALIZED}. After completion the
     * state is set to {@link RSA_data#INITIALIZED INITIALIZED} (see
     * <A HREF="RSA_data.html#applet-state">applet state
     * description</A>). <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. The detailed operations of
     * this method are:
     * <DL>
     * <DT><STRONG>check state</STRONG>
     * <DD><a href="../../../overview-summary.html#ASSERT">ASSERT</a>
     * that {@link RSA_data#state} contains {@link
     * RSA_data#UNINITIALIZED UNINITIALIZED} 
     * <DT><STRONG>initialize digit masks {@link
     * RSA_data#mod_first_digit_mask}, {@link
     * RSA_data#v_first_digit_mask}</STRONG>
     * <DD>with calling {@link Bignat#get_first_digit_mask
     * Bignat.get_first_digit_mask}, an important subtlety here is
     * that because of the <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * digits</a> the index of the first digit of the modulus in
     * {@link ds.ov2.bignat.Modulus#m data.n.m} is 2.
     * <DT><STRONG>Compute current blinded attribute expression {@link
     * RSA_data#current_blinded_a data.current_blinded_a}</STRONG>
     * <DD>compute {@link RSA_data#bases data.bases}^{@link
     * RSA_data#current_attributes data.current_attributes} with
     * {@link Vector#mont_rsa_exponent_mod
     * data.bases.mont_rsa_exponent_mod}. Because in this method the result
     * and first temporary is used heavily in the multiplication, it
     * is wise to use {@link RSA_data#result data.result} as first
     * temporary and {@link Bignat#copy copy} the result afterwards
     * into {@link RSA_data#current_blinded_a data.current_blinded_a}.
     * Note that the {@link RSA_data#current_blinding} and the {@link
     * RSA_data#v} are alias in the bases and current_attributes
     * vector at index {@link RSA_data#attribute_length} + 1. Because
     * of the blinding one has to use a montgomery correction factor
     * for {@code data.attribute_length + 1} factors, see {@link
     * RSA_data#get_montgomery_correction
     * data.get_montgomery_correction}. 
     * <DT><STRONG>advance the {@link RSA_data#state}</STRONG>
     * <DD>by setting it to {@link RSA_data#INITIALIZED INITIALIZED}
     * </DL>
     * <P>
     *
     * There are no results sent back to the host by the OV-chip
     * protocol layer after this method has finished.
     */
    public void initialize() {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here.
        // END PATENT CUT
    }


    /**
     * 
     * Atomically switch to various new values. During the resign
     * protocol the applet gets new attributes, chooses a new
     * blinding, and computes the new blinded attribute expression and
     * a new signature on it. To improve fault tolerance all these new
     * data is stored in new_* variables, that are distinct form the
     * current_* ones. At the end of the {@link #finish_signature
     * finish_signature} step of the resign protocol the new_*
     * versions are atomically copied into the current_* ones. This is
     * done in this method by swapping references. 
     * <P>
     *
     * Swapping the references introduces a little problem in the
     * protocol layer: The layer also has aliases for instance of 
     * {@link RSA_data#current_blinded_a} and of {@link
     * RSA_data#current_signature}. After swapping references, these
     * aliases suddenly alias invalid data. Therefore one has to
     * uptate the step data structure with {@link
     * Front_protocols#update_steps front_protocols.update_steps}. 
     * <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. The detailed operations of
     * this method are:
     * <DL>
     * <DT><STRONG>Create temporaries</STRONG>
     * <DD>Create 4 temporary references and initialize them to {@link
     * RSA_data#current_attributes data.current_attributes}, {@link
     * RSA_data#current_blinding data.current_blinding}, {@link
     * RSA_data#current_blinded_a data.current_blinded_a} and {@link
     * RSA_data#current_signature data.current_signature},
     * respectively.
     * <DT><STRONG>Check side conditions</STRONG>
     * <DD><a href="../../../overview-summary.html#ASSERT">ASSERT</a>
     * that the argument {@code success} is still false and that
     * {@link RSA_data#new_attributes data.new_attributes} aliases
     * {@link RSA_data#v data.v} at index {@link
     * RSA_data#attribute_length data.attribute_length}.
     * <DT><STRONG>Start a transaction</STRONG>
     * <DD>with {@link ds.ov2.util.Misc#begin_transaction} in order to be
     * compatible with the <a
     * href="../../../overview-summary.html#HOST_TESTFRAME">HOST_TESTFRAME<a>
     * <DT><STRONG>finish attribute swapping</STRONG>
     * <DD>by writing the new_* references into the current_*
     * variables and then by assigning the temporary references to the
     * new_* variables.
     * <DT><STRONG>update protocol step aliases</STRONG>
     * <DD>with {@link Front_protocols#update_steps}
     * <DT><STRONG>set state, mark success and commit</STRONG>
     * <DD>set the state to {@link RSA_data#BLINDED BLINDED} if it was {@link
     * RSA_data#INITIALIZED INITIALIZED}, set {@code success} to true
     * and commit the 
     * transaction with 
     * {@link ds.ov2.util.Misc#commit_transaction}
     * </DL>
     * 
     * @param success alias of {@link
     * RSA_card_protocol_description#signature_accepted}, initially
     * false, set to true when the atomic swapping succeeds
     */
    public void switch_to_new_attributes(APDU_boolean success) {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here.
        // END PATENT CUT
    }


    //########################################################################
    //########################################################################
    // 
    // signature protocol methods
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Second step of the resign protocol. (In the first step no
     * method is executed on the card, only {@link
     * RSA_data#applet_id}, {@link RSA_data#current_blinded_a} and
     * {@link RSA_data#current_signature} are sent to the host in the
     * first step.) <P>
     *
     * Prior to calling this method the protocol layer receives
     * <UL>
     * <LI>the host commitment {@link RSA_data#host_alpha}</LI>
     * <LI>the attribute updates in {@link RSA_data#remainders}; these
     * updates are in modulo format: to subract {@code x} from an
     * attribute, the host sends {@link RSA_data#v}{@code - x}.
     * </UL>
     * <P>
     *
     * After this method the protocol layer sends the remainder c in
     * {@link RSA_data#sig_remainder}.
     * <P>
     *
     * Asserts that the applet is in state {@link RSA_data#INITIALIZED
     * INITIALIZED} or {@link RSA_data#BLINDED BLINDED} (see <A
     * HREF="RSA_data.html#applet-state">applet state
     * description</A>). <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. The detailed operations of
     * this method are:
     * <DL>
     * <DT><STRONG>check state</STRONG>
     * <DD><a href="../../../overview-summary.html#ASSERT">ASSERT</a>
     * that the state is either {@link RSA_data#INITIALIZED
     * INITIALIZED} or {@link 
     * RSA_data#BLINDED BLINDED}.
     * <DT><STRONG>compute new attributes and store them in {@link
     * RSA_data#new_attributes}</STRONG>
     * <DD>The addition of the current attribute value with the update
     * might overflow, because of the modulo representation of the
     * updates. Therefore, each attribute must be copied into {@link
     * #double_small}, where the update is added and the
     * remainder modulo {@link RSA_data#v data.v} is taken. Finally
     * the remainder in {@link #double_small} is copied into the
     * {@link RSA_data#new_attributes data.new_attributes}. The alias
     * to {@link RSA_data#v data.v} at index {@link
     * RSA_data#attribute_length data.attribute_length} in {@link
     * RSA_data#new_attributes data.new_attributes} must not be
     * touched.
     * <DT>XXXXXXXXXXXXXXXXXXX
     * <DL>
     * 
     */
    public void make_sig_hash() {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here.
        // END PATENT CUT
    }


    /**
     * 
     * Third step of the resign protocol. Prior to calling this method
     * the protocol layer receives the host response r in {@link
     * #data}.{@link RSA_data#host_response host_response}. After this
     * method the protocol layer sends the boolean result of this
     * method in {@link
     * RSA_card_protocol_description#signature_accepted}. 
     * <P>
     *
     * Sets the state of the applet to {@link RSA_data#BLINDED
     * BLINDED} if it was {@link RSA_data#INITIALIZED INITIALIZED}
     * (see <A HREF="RSA_data.html#applet-state">applet state
     * description</A>). <P>
     *
     * If the host response passes the acceptance check this method
     * computes the final signature and starts a transaction to switch
     * to the new attributes and the new signature. If all this goes
     * through the result value in {@code accept} is set to true.
     * Otherwise it remains false when this method finishes.
     * <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. 
     * 
     * @param accept reference to the accept result of this method,
     * equal to {@link
     * RSA_card_protocol_description#signature_accepted}, 
     * will be true when returning from this method precisely if the
     * applet accepts the new signature and the transaction for
     * changing to the new attributes and the new signature succeeded.
     */
    public void finish_signature(APDU_boolean accept) {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here.
        // END PATENT CUT
    }


    //########################################################################
    //########################################################################
    // 
    // proof protocol methods
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * First step of the gate or proof protocol. Prior to calling this
     * method no arguments are received. After this method the
     * protocol layer sends the following data to the host:
     * <UL>
     * <LI>the applet identifier {@link #data}.{@link
     * RSA_data#applet_id applet_id} to let the host distinguish
     * between the plain and the Montgomerizing applet,</LI>
     *
     * <LI>the current blinded attribute expression {@link
     * #data}.{@link RSA_data#current_blinded_a current_blinded_a},
     * </LI>
     *
     * <LI>the current signature {@link #data}.{@link
     * RSA_data#current_signature current_signature}, and</LI>
     *
     * <LI>the applet commitment w in {@link #data}.{@link
     * RSA_data#result result}</LI>
     * </UL>
     * <P>
     *
     * Asserts that the applet is in the state {@link RSA_data#BLINDED
     * BLINDED} (see <A HREF="RSA_data.html#applet-state">applet state
     * description</A>). <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. 
     */
    public void proof_commit() {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here.
        // END PATENT CUT
    }


    /**
     * 
     * Second step of the gate or proof protocol. Prior to calling
     * this method the protocol layer receives the host challenge
     * gamma in {@link #data}.{@link RSA_data#gamma_beta_3
     * gamma_beta_3}. After this method the protocol layer sends the
     * following data to the host:
     * <UL>
     * <LI>the remainders r_i in {@link #data}.{@link
     * RSA_data#remainders remainders}</LI>
     *
     * <LI>the applet response s in {@link #data}.{@link
     * RSA_data#result result}</LI>
     * </UL>
     * <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. 
     */
    public void respond_to_challenge() {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here.
        // END PATENT CUT
    }
}
