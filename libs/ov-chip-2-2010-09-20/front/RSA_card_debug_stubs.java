// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from RSA_card_debug.id
// by some sort of idl compiler.

package ds.ov2.front;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Host_protocol;
import java.math.BigInteger;;
import ds.ov2.util.APDU_byte;;
import ds.ov2.util.APDU_short;;
import ds.ov2.util.APDU_boolean;;
import ds.ov2.bignat.Host_modulus;;
import ds.ov2.bignat.Host_vector;;
import ds.ov2.bignat.APDU_BigInteger;;


/**
 * Stub code for running methods on the card.
 * Defines one stub method for each protocol step in RSA_card_debug.id.
 * The stub methods are the top entry point into the
 * OV-chip protocol layer for host driver code.
 * Each stub method performs the following actions:
 * <OL>
 * <LI>argument conversion (for instance from
 *     {@link java.math.BigInteger BigInteger} to
 *     {@link ds.ov2.bignat.Bignat Bignat})</LI>
 * <LI>transfers the arguments to the card
 *     (possibly using several APDU's)</LI>
 * <LI>invokes the right method on the card</LI>
 * <LI>transfers the results back (again with possibly
 *     several APDU's)</LI>
 * <LI>result conversion</LI>
 * <LI>and finally packages several results into one
 *     tuple object</LI>
 * </OL>
 * 
 * @author idl compiler
 * @version automatically generated from RSA_card_debug.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class RSA_card_debug_stubs {

    /**
     * A protocol description instance from RSA_card_debug.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private RSA_card_debug_description protocol_description;


    /**
     * The output channel for debugging information of the 
     * OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out;


    /**
     * Controls apdutool line printing. Initialized in the constructor,
     * if true, the OV-chip protocol layer prints apdutool lines as 
     * part of its debugging output.
     */
    private boolean with_apdu_script;

    //#########################################################################
    //#########################################################################
    // 
    // Protocol status
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step get
    // 

    /**
     * Host protocol instance for step get of protocol status.
     * Initialized via {@link #init_hp_get init_hp_get} 
     * (which is called from {@link #delayed_init}).
     */
    private Host_protocol hp_get;

    /**
     * Initialization method for {@link #hp_get}.
     *
     * @param d description instance for RSA_card_debug.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_get(RSA_card_debug_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_get = 
            new Host_protocol(d.status_protocol,
                              d.get_step,
                              out,
                              script,
                              "get status"
                              );
    }


    /**
     * Result record for step get of
     * protocol status.
     */
    public static class Get_result {
        /**
         * Return value data.applet_id converted from APDU_byte.
         */
        public final byte data_applet_id;
        /**
         * Return value data.n converted from Modulus.
         */
        public final Host_modulus data_n;
        /**
         * Return value data.v converted from Bignat.
         */
        public final BigInteger data_v;
        /**
         * Return value data.ptls_key converted from Bignat.
         */
        public final BigInteger data_ptls_key;
        /**
         * Return value data.bases converted from Vector.
         */
        public final Host_vector data_bases;
        /**
         * Return value data.current_attributes converted from Vector.
         */
        public final Host_vector data_current_attributes;
        /**
         * Return value data.new_attributes converted from Vector.
         */
        public final Host_vector data_new_attributes;
        /**
         * Return value data.current_blinding converted from Bignat.
         */
        public final BigInteger data_current_blinding;
        /**
         * Return value data.new_blinding converted from Bignat.
         */
        public final BigInteger data_new_blinding;
        /**
         * Return value data.current_blinded_a converted from Bignat.
         */
        public final BigInteger data_current_blinded_a;
        /**
         * Return value data.new_blinded_a converted from Bignat.
         */
        public final BigInteger data_new_blinded_a;
        /**
         * Return value data.current_signature converted from Signature.
         */
        public final Host_signature data_current_signature;
        /**
         * Return value data.new_signature converted from Signature.
         */
        public final Host_signature data_new_signature;
        /**
         * Return value data.montgomery_corrections converted from Vector.
         */
        public final Host_vector data_montgomery_corrections;
        /**
         * Return record constructor.
         */
        public Get_result(
                    byte a0,
                    Host_modulus a1,
                    BigInteger a2,
                    BigInteger a3,
                    Host_vector a4,
                    Host_vector a5,
                    Host_vector a6,
                    BigInteger a7,
                    BigInteger a8,
                    BigInteger a9,
                    BigInteger a10,
                    Host_signature a11,
                    Host_signature a12,
                    Host_vector a13) {
            data_applet_id = a0;
            data_n = a1;
            data_v = a2;
            data_ptls_key = a3;
            data_bases = a4;
            data_current_attributes = a5;
            data_new_attributes = a6;
            data_current_blinding = a7;
            data_new_blinding = a8;
            data_current_blinded_a = a9;
            data_new_blinded_a = a10;
            data_current_signature = a11;
            data_new_signature = a12;
            data_montgomery_corrections = a13;
        }
    }


    /**
     * Call step get of protocol status
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Get_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Get_result get_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_byte _data_applet_id_host_res = new APDU_byte();
        Host_modulus _data_n_host_res = new Host_modulus(protocol_description.data.n.m.size());
        APDU_BigInteger _data_v_host_res = new APDU_BigInteger(protocol_description.data.v.size());
        APDU_BigInteger _data_ptls_key_host_res = new APDU_BigInteger(protocol_description.data.ptls_key.size());
        Host_vector _data_bases_host_res = new Host_vector(protocol_description.data.bases.get_bignat_size(), protocol_description.data.bases.get_length());
        Host_vector _data_current_attributes_host_res = new Host_vector(protocol_description.data.current_attributes.get_bignat_size(), protocol_description.data.current_attributes.get_length());
        Host_vector _data_new_attributes_host_res = new Host_vector(protocol_description.data.new_attributes.get_bignat_size(), protocol_description.data.new_attributes.get_length());
        APDU_BigInteger _data_current_blinding_host_res = new APDU_BigInteger(protocol_description.data.current_blinding.size());
        APDU_BigInteger _data_new_blinding_host_res = new APDU_BigInteger(protocol_description.data.new_blinding.size());
        APDU_BigInteger _data_current_blinded_a_host_res = new APDU_BigInteger(protocol_description.data.current_blinded_a.size());
        APDU_BigInteger _data_new_blinded_a_host_res = new APDU_BigInteger(protocol_description.data.new_blinded_a.size());
        Host_signature _data_current_signature_host_res = new Host_signature(protocol_description.data.current_signature.sig_short_size, protocol_description.data.current_signature.sig_long_size, protocol_description.data.current_signature.applet_id);
        Host_signature _data_new_signature_host_res = new Host_signature(protocol_description.data.new_signature.sig_short_size, protocol_description.data.new_signature.sig_long_size, protocol_description.data.new_signature.applet_id);
        Host_vector _data_montgomery_corrections_host_res = new Host_vector(protocol_description.data.montgomery_corrections.get_bignat_size(), protocol_description.data.montgomery_corrections.get_length());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _data_applet_id_host_res,
            _data_n_host_res,
            _data_v_host_res,
            _data_ptls_key_host_res,
            _data_bases_host_res,
            _data_current_attributes_host_res,
            _data_new_attributes_host_res,
            _data_current_blinding_host_res,
            _data_new_blinding_host_res,
            _data_current_blinded_a_host_res,
            _data_new_blinded_a_host_res,
            _data_current_signature_host_res,
            _data_new_signature_host_res,
            _data_montgomery_corrections_host_res
        };

        hp_get.run_step(_cc, call_args, call_res);
        return new Get_result(_data_applet_id_host_res.value, _data_n_host_res, _data_v_host_res.value, _data_ptls_key_host_res.value, _data_bases_host_res, _data_current_attributes_host_res, _data_new_attributes_host_res, _data_current_blinding_host_res.value, _data_new_blinding_host_res.value, _data_current_blinded_a_host_res.value, _data_new_blinded_a_host_res.value, _data_current_signature_host_res, _data_new_signature_host_res, _data_montgomery_corrections_host_res);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol mem_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step mem_size
    // 

    /**
     * Host protocol instance for step mem_size of protocol mem_size.
     * Initialized via {@link #init_hp_mem_size init_hp_mem_size} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mem_size;

    /**
     * Initialization method for {@link #hp_mem_size}.
     *
     * @param d description instance for RSA_card_debug.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mem_size(RSA_card_debug_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mem_size = 
            new Host_protocol(d.mem_size_protocol,
                              d.mem_size_step,
                              out,
                              script,
                              "report ram size"
                              );
    }


    /**
     * Result record for step mem_size of
     * protocol mem_size.
     */
    public static class Mem_size_result {
        /**
         * Return value assertions_on converted from APDU_boolean.
         */
        public final boolean assertions_on;
        /**
         * Return value mem_persistent converted from APDU_short.
         */
        public final int mem_persistent;
        /**
         * Return value mem_transient_reset converted from APDU_short.
         */
        public final int mem_transient_reset;
        /**
         * Return value mem_transient_deselect converted from APDU_short.
         */
        public final int mem_transient_deselect;
        /**
         * Return record constructor.
         */
        public Mem_size_result(
                    boolean a0,
                    int a1,
                    int a2,
                    int a3) {
            assertions_on = a0;
            mem_persistent = a1;
            mem_transient_reset = a2;
            mem_transient_deselect = a3;
        }
    }


    /**
     * Call step mem_size of protocol mem_size
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Mem_size_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Mem_size_result mem_size_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_boolean _assertions_on_host_res = new APDU_boolean();
        APDU_short _mem_persistent_host_res = new APDU_short();
        APDU_short _mem_transient_reset_host_res = new APDU_short();
        APDU_short _mem_transient_deselect_host_res = new APDU_short();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _assertions_on_host_res,
            _mem_persistent_host_res,
            _mem_transient_reset_host_res,
            _mem_transient_deselect_host_res
        };

        hp_mem_size.run_step(_cc, call_args, call_res);
        return new Mem_size_result(_assertions_on_host_res.value, _mem_persistent_host_res.value, _mem_transient_reset_host_res.value, _mem_transient_deselect_host_res.value);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol reset_applet_state
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step reset
    // 

    /**
     * Host protocol instance for step reset of protocol reset_applet_state.
     * Initialized via {@link #init_hp_reset init_hp_reset} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_reset;

    /**
     * Initialization method for {@link #hp_reset}.
     *
     * @param d description instance for RSA_card_debug.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_reset(RSA_card_debug_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_reset = 
            new Host_protocol(d.reset_applet_state_protocol,
                              d.reset_step,
                              out,
                              script,
                              "reset applet state"
                              );
    }


    /**
     * Call step reset of protocol reset_applet_state
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @throws CardException in case of communication errors
     */
    public void reset_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        hp_reset.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    /**
     * Delayed initialization.
     * Initialize protocols annotated with
     * <EM>delayed protocol init</EM> in RSA_card_debug.id.
     */
    public void delayed_init() {
        init_hp_get(protocol_description, out, with_apdu_script);
    }


    //#########################################################################
    // Constructor
    // 

    /**
     * Stub constructor. Initializes all non-delayed host protocol
     * instances from RSA_card_debug.id. Delayed protocols must be initialized
     * separately with {@link #delayed_init} at the appropriate moment.
     *
     * @param d protocol description instance for RSA_card_debug.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script if true, print apdutool lines for all APDUs as part 
     *          of the debugging information.
     */
    public RSA_card_debug_stubs(RSA_card_debug_description d,
                               PrintWriter o, 
                               boolean script) {
        protocol_description = d;
        out = o;
        with_apdu_script = script;
        // initialize the Host_protocols
        init_hp_mem_size(protocol_description, out, with_apdu_script);
        init_hp_reset(protocol_description, out, with_apdu_script);
    }
}

