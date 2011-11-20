// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Data_protocol.id
// by some sort of idl compiler.

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Host_protocol;
import ds.ov2.util.Resizable_buffer;
import ds.ov2.util.APDU_boolean;
import ds.ov2.util.APDU_short_array;


/**
 * Stub code for running methods on the card.
 * Defines one stub method for each protocol step in Data_protocol.id.
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
 * @version automatically generated from Data_protocol.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Data_protocol_stubs {

    /**
     * A protocol description instance from Data_protocol.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Data_protocol_description protocol_description;


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
    // Protocol check_data
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step check_data
    // 

    /**
     * Host protocol instance for step check_data of protocol check_data.
     * Initialized via {@link #init_hp_check_data init_hp_check_data} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_check_data;

    /**
     * Initialization method for {@link #hp_check_data}.
     *
     * @param d description instance for Data_protocol.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_check_data(Data_protocol_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_check_data = 
            new Host_protocol(d.check_data_protocol,
                              d.check_data_step,
                              out,
                              script,
                              "data check"
                              );
    }


    /**
     * Result record for step check_data of
     * protocol check_data.
     */
    public static class Check_data_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value buf_5.
         */
        public final Resizable_buffer buf_5;
        /**
         * Return value buf_6.
         */
        public final Resizable_buffer buf_6;
        /**
         * Return value buf_7.
         */
        public final Resizable_buffer buf_7;
        /**
         * Return value buf_8.
         */
        public final Resizable_buffer buf_8;
        /**
         * Return value buf_9.
         */
        public final Resizable_buffer buf_9;
        /**
         * Return record constructor.
         */
        public Check_data_result(
                    long ad,
                    Resizable_buffer a0,
                    Resizable_buffer a1,
                    Resizable_buffer a2,
                    Resizable_buffer a3,
                    Resizable_buffer a4) {
            duration = ad;
            buf_5 = a0;
            buf_6 = a1;
            buf_7 = a2;
            buf_8 = a3;
            buf_9 = a4;
        }
    }


    /**
     * Call step check_data of protocol check_data
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _buf_0_host_arg argument buf_0
     * @param _buf_1_host_arg argument buf_1
     * @param _buf_2_host_arg argument buf_2
     * @param _buf_3_host_arg argument buf_3
     * @param _buf_4_host_arg argument buf_4
     * @return Check_data_result record containing all results, including the duration of the call.
     * @throws CardException in case of communication errors
     */
    public Check_data_result check_data_call(CardChannel _cc,
                                         Resizable_buffer _buf_0_host_arg,
                                         Resizable_buffer _buf_1_host_arg,
                                         Resizable_buffer _buf_2_host_arg,
                                         Resizable_buffer _buf_3_host_arg,
                                         Resizable_buffer _buf_4_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_0_host_arg,
            _buf_1_host_arg,
            _buf_2_host_arg,
            _buf_3_host_arg,
            _buf_4_host_arg
        };

        Resizable_buffer _buf_5_host_res = new Resizable_buffer(protocol_description.buf_5.size());
        Resizable_buffer _buf_6_host_res = new Resizable_buffer(protocol_description.buf_6.size());
        Resizable_buffer _buf_7_host_res = new Resizable_buffer(protocol_description.buf_7.size());
        Resizable_buffer _buf_8_host_res = new Resizable_buffer(protocol_description.buf_8.size());
        Resizable_buffer _buf_9_host_res = new Resizable_buffer(protocol_description.buf_9.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_5_host_res,
            _buf_6_host_res,
            _buf_7_host_res,
            _buf_8_host_res,
            _buf_9_host_res
        };

        long start = System.nanoTime();
        hp_check_data.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return new Check_data_result(duration, _buf_5_host_res, _buf_6_host_res, _buf_7_host_res, _buf_8_host_res, _buf_9_host_res);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol set_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step set_size
    // 

    /**
     * Host protocol instance for step set_size of protocol set_size.
     * Initialized via {@link #init_hp_set_size init_hp_set_size} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_set_size;

    /**
     * Initialization method for {@link #hp_set_size}.
     *
     * @param d description instance for Data_protocol.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_set_size(Data_protocol_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_set_size = 
            new Host_protocol(d.set_size_protocol,
                              d.set_size_step,
                              out,
                              script,
                              "data set size"
                              );
    }


    /**
     * Call step set_size of protocol set_size
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _buf_sizes_host_arg argument buf_sizes to be converted to APDU_short_array
     * @param _performance_test_host_arg argument performance_test to be converted to APDU_boolean
     * @return result buf_sizes converted from APDU_short_array
     * @throws CardException in case of communication errors
     */
    public int[] set_size_call(CardChannel _cc,
                               int[] _buf_sizes_host_arg,
                               boolean _performance_test_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short_array(protocol_description.buf_sizes.get_length(), _buf_sizes_host_arg),
            new APDU_boolean(_performance_test_host_arg)
        };

        APDU_short_array _buf_sizes_host_res = new APDU_short_array(protocol_description.buf_sizes.get_length());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_sizes_host_res
        };

        hp_set_size.run_step(_cc, call_args, call_res);
        return _buf_sizes_host_res.get_int_array();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_performance_receive
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step data_performance_receive
    // 

    /**
     * Host protocol instance for step data_performance_receive of protocol data_performance_receive.
     * Initialized via {@link #init_hp_data_performance_receive init_hp_data_performance_receive} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_data_performance_receive;

    /**
     * Initialization method for {@link #hp_data_performance_receive}.
     *
     * @param d description instance for Data_protocol.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_data_performance_receive(Data_protocol_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_data_performance_receive = 
            new Host_protocol(d.data_performance_receive_protocol,
                              d.data_performance_receive_step,
                              out,
                              script,
                              "data measure send"
                              );
    }


    /**
     * Call step data_performance_receive of protocol data_performance_receive
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _buf_0_host_arg argument buf_0
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long data_performance_receive_call(CardChannel _cc,
                                         Resizable_buffer _buf_0_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_0_host_arg
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_data_performance_receive.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_performance_send
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step data_performance_send
    // 

    /**
     * Host protocol instance for step data_performance_send of protocol data_performance_send.
     * Initialized via {@link #init_hp_data_performance_send init_hp_data_performance_send} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_data_performance_send;

    /**
     * Initialization method for {@link #hp_data_performance_send}.
     *
     * @param d description instance for Data_protocol.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_data_performance_send(Data_protocol_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_data_performance_send = 
            new Host_protocol(d.data_performance_send_protocol,
                              d.data_performance_send_step,
                              out,
                              script,
                              "data measure receive"
                              );
    }


    /**
     * Result record for step data_performance_send of
     * protocol data_performance_send.
     */
    public static class Data_performance_send_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value buf_5.
         */
        public final Resizable_buffer buf_5;
        /**
         * Return record constructor.
         */
        public Data_performance_send_result(
                    long ad,
                    Resizable_buffer a0) {
            duration = ad;
            buf_5 = a0;
        }
    }


    /**
     * Call step data_performance_send of protocol data_performance_send
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Data_performance_send_result record containing all results, including the duration of the call.
     * @throws CardException in case of communication errors
     */
    public Data_performance_send_result data_performance_send_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        Resizable_buffer _buf_5_host_res = new Resizable_buffer(protocol_description.buf_5.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_5_host_res
        };

        long start = System.nanoTime();
        hp_data_performance_send.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return new Data_performance_send_result(duration, _buf_5_host_res);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_perf_proof
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step data_perf_proof_commit
    // 

    /**
     * Host protocol instance for step data_perf_proof_commit of protocol data_perf_proof.
     * Initialized via {@link #init_hp_data_perf_proof_commit init_hp_data_perf_proof_commit} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_data_perf_proof_commit;

    /**
     * Initialization method for {@link #hp_data_perf_proof_commit}.
     *
     * @param d description instance for Data_protocol.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_data_perf_proof_commit(Data_protocol_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_data_perf_proof_commit = 
            new Host_protocol(d.data_perf_proof_protocol,
                              d.data_perf_proof_commit_step,
                              out,
                              script,
                              "card commit"
                              );
    }


    /**
     * Result record for step data_perf_proof_commit of
     * protocol data_perf_proof.
     */
    public static class Data_perf_proof_commit_result {
        /**
         * Return value buf_1.
         */
        public final Resizable_buffer buf_1;
        /**
         * Return value buf_2.
         */
        public final Resizable_buffer buf_2;
        /**
         * Return value buf_3.
         */
        public final Resizable_buffer buf_3;
        /**
         * Return record constructor.
         */
        public Data_perf_proof_commit_result(
                    Resizable_buffer a0,
                    Resizable_buffer a1,
                    Resizable_buffer a2) {
            buf_1 = a0;
            buf_2 = a1;
            buf_3 = a2;
        }
    }


    /**
     * Call step data_perf_proof_commit of protocol data_perf_proof
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _buf_0_host_arg argument buf_0
     * @return Data_perf_proof_commit_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Data_perf_proof_commit_result data_perf_proof_commit_call(CardChannel _cc,
                                         Resizable_buffer _buf_0_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_0_host_arg
        };

        Resizable_buffer _buf_1_host_res = new Resizable_buffer(protocol_description.buf_1.size());
        Resizable_buffer _buf_2_host_res = new Resizable_buffer(protocol_description.buf_2.size());
        Resizable_buffer _buf_3_host_res = new Resizable_buffer(protocol_description.buf_3.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_1_host_res,
            _buf_2_host_res,
            _buf_3_host_res
        };

        hp_data_perf_proof_commit.run_step(_cc, call_args, call_res);
        return new Data_perf_proof_commit_result(_buf_1_host_res, _buf_2_host_res, _buf_3_host_res);
    }


    //#########################################################################
    // Step data_perf_answer_to_challenge
    // 

    /**
     * Host protocol instance for step data_perf_answer_to_challenge of protocol data_perf_proof.
     * Initialized via {@link #init_hp_data_perf_answer_to_challenge init_hp_data_perf_answer_to_challenge} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_data_perf_answer_to_challenge;

    /**
     * Initialization method for {@link #hp_data_perf_answer_to_challenge}.
     *
     * @param d description instance for Data_protocol.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_data_perf_answer_to_challenge(Data_protocol_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_data_perf_answer_to_challenge = 
            new Host_protocol(d.data_perf_proof_protocol,
                              d.data_perf_answer_to_challenge_step,
                              out,
                              script,
                              "card response to challenge"
                              );
    }


    /**
     * Result record for step data_perf_answer_to_challenge of
     * protocol data_perf_proof.
     */
    public static class Data_perf_answer_to_challenge_result {
        /**
         * Return value buf_5.
         */
        public final Resizable_buffer buf_5;
        /**
         * Return value buf_6.
         */
        public final Resizable_buffer buf_6;
        /**
         * Return value buf_7.
         */
        public final Resizable_buffer buf_7;
        /**
         * Return value buf_8.
         */
        public final Resizable_buffer buf_8;
        /**
         * Return value buf_9.
         */
        public final Resizable_buffer buf_9;
        /**
         * Return record constructor.
         */
        public Data_perf_answer_to_challenge_result(
                    Resizable_buffer a0,
                    Resizable_buffer a1,
                    Resizable_buffer a2,
                    Resizable_buffer a3,
                    Resizable_buffer a4) {
            buf_5 = a0;
            buf_6 = a1;
            buf_7 = a2;
            buf_8 = a3;
            buf_9 = a4;
        }
    }


    /**
     * Call step data_perf_answer_to_challenge of protocol data_perf_proof
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _buf_4_host_arg argument buf_4
     * @return Data_perf_answer_to_challenge_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Data_perf_answer_to_challenge_result data_perf_answer_to_challenge_call(CardChannel _cc,
                                         Resizable_buffer _buf_4_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_4_host_arg
        };

        Resizable_buffer _buf_5_host_res = new Resizable_buffer(protocol_description.buf_5.size());
        Resizable_buffer _buf_6_host_res = new Resizable_buffer(protocol_description.buf_6.size());
        Resizable_buffer _buf_7_host_res = new Resizable_buffer(protocol_description.buf_7.size());
        Resizable_buffer _buf_8_host_res = new Resizable_buffer(protocol_description.buf_8.size());
        Resizable_buffer _buf_9_host_res = new Resizable_buffer(protocol_description.buf_9.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_5_host_res,
            _buf_6_host_res,
            _buf_7_host_res,
            _buf_8_host_res,
            _buf_9_host_res
        };

        hp_data_perf_answer_to_challenge.run_step(_cc, call_args, call_res);
        return new Data_perf_answer_to_challenge_result(_buf_5_host_res, _buf_6_host_res, _buf_7_host_res, _buf_8_host_res, _buf_9_host_res);
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    //#########################################################################
    // Constructor
    // 

    /**
     * Stub constructor. Initializes all host protocol
     * instances from Data_protocol.id. 
     *
     * @param d protocol description instance for Data_protocol.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script if true, print apdutool lines for all APDUs as part 
     *          of the debugging information.
     */
    public Data_protocol_stubs(Data_protocol_description d,
                               PrintWriter o, 
                               boolean script) {
        protocol_description = d;
        out = o;
        with_apdu_script = script;
        // initialize the Host_protocols
        init_hp_check_data(protocol_description, out, with_apdu_script);
        init_hp_set_size(protocol_description, out, with_apdu_script);
        init_hp_data_performance_receive(protocol_description, out, with_apdu_script);
        init_hp_data_performance_send(protocol_description, out, with_apdu_script);
        init_hp_data_perf_proof_commit(protocol_description, out, with_apdu_script);
        init_hp_data_perf_answer_to_challenge(protocol_description, out, with_apdu_script);
    }
}

