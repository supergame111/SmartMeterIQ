// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from RSA_card_protocol.id
// by some sort of idl compiler.

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.front;
#endif


#if defined(JAVACARD_APPLET) || defined(JAVADOC)
  import javacard.framework.APDU;
#endif
#if !defined(JAVACARD_APPLET) || defined(JAVADOC)
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Protocol_step;
  import ds.ov2.util.Protocol;
  import java.math.BigInteger;
  import ds.ov2.util.APDU_byte;
  import ds.ov2.util.APDU_short;
  import ds.ov2.util.APDU_boolean;
  import ds.ov2.bignat.APDU_BigInteger;
  import ds.ov2.bignat.Host_modulus;
  import ds.ov2.bignat.Host_vector;
#endif

#if defined(APPLET_TESTFRAME) || defined(JAVADOC)
   import ds.ov2.util.Void_method;
   import ds.ov2.util.Empty_void_method;
#endif

/**
 * Protocol description for RSA_card_protocol. Defines suitable Protocol's and Protocol_steps for all protocols described in RSA_card_protocol.id for use in the OV-chip protocol layer.
 * 
 * @author idl compiler
 * @version automatically generated from RSA_card_protocol.id
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class RSA_card_protocol_description {

    //#########################################################################
    // Variable declarations
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)
        /**
         * Card variable declararion from RSA_card_protocol.id.
         * <P>
         * Only available if either JAVACARD_APPLET
         * or APPLET_TESTFRAME is defined.
         */
        /* package local */ RSA_CARD card;

    #endif

    /**
     * Variable declaration from RSA_card_protocol.id.
     */
    /* package local */ Front_protocols front_protocols;
    /**
     * Variable declaration from RSA_card_protocol.id.
     */
    /* package local */ RSA_data data;
    /**
     * Variable declaration from RSA_card_protocol.id.
     */
    /* package local */ APDU_short short_bignat_size;
    /**
     * Variable declaration from RSA_card_protocol.id.
     */
    /* package local */ APDU_short long_bignat_size;
    /**
     * Variable declaration from RSA_card_protocol.id.
     */
    /* package local */ APDU_short attribute_length;
    /**
     * Variable declaration from RSA_card_protocol.id.
     */
    /* package local */ APDU_short mont_correction_len;
    /**
     * Variable declaration from RSA_card_protocol.id.
     */
    /* package local */ APDU_boolean signature_accepted;


    //#########################################################################
    //#########################################################################
    // 
    // Protocol allocate
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step allocate in protocol allocate.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class allocate_call implements Void_method {
          /** Empty constructor. */
          /* package */ allocate_call() {}

          /**
           * Run the card action for step allocate in protocol allocate.
           */
          public void method() { 
              card.allocate(short_bignat_size.value, 
                             long_bignat_size.value, 
                             attribute_length.value, 
                             mont_correction_len.value); 
           delayed_init(); 
           #ifdef TESTFRAME 
              front_protocols.rsa_debug.delayed_init(); 
              update_all(); 
              front_protocols.rsa_debug.update_all(); 
           #endif 
           front_protocols.init_protocols();;
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step allocate in protocol allocate.
     */
    Protocol_step allocate_step;



    /**
     * Initialize {@link #allocate_step}.
     * Initialize the step instance for step allocate in protocol allocate.
     */
    private void init_allocate_step() {
        if(allocate_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            short_bignat_size,
            long_bignat_size,
            attribute_length,
            mont_correction_len
        };

        APDU_Serializable[] res = null;

        allocate_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new allocate_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #allocate_step}.
     * Update argument and result references in the step allocate
     * of protocol allocate.
     */
    public void update_allocate_step() {
        ASSERT(allocate_step != null);

        allocate_step.arguments[0] = short_bignat_size;
        allocate_step.arguments[1] = long_bignat_size;
        allocate_step.arguments[2] = attribute_length;
        allocate_step.arguments[3] = mont_correction_len;
        return;
    }



    //#########################################################################
    // allocate protocol definition
    // 

    /**
     * Protocol instance for protocol allocate.
     */
    public Protocol allocate_protocol;

    /**
     * Initialize {@link #allocate_protocol}.
     * Initialize the protocol instance for protocol allocate.
     */
    private void init_allocate_protocol() {
        if(allocate_protocol != null)
            return;

        init_allocate_step();

        Protocol_step[] steps = new Protocol_step[]{
            allocate_step
        };
        allocate_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #allocate_protocol}.
     * Update argument and result references in all 
     * steps of protocol allocate.
     */
    public void update_allocate_protocol() {
        update_allocate_step();
        allocate_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol initialize
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step init_data in protocol initialize.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class init_data_call implements Void_method {
          /** Empty constructor. */
          /* package */ init_data_call() {}

          /**
           * Run the card action for step init_data in protocol initialize.
           */
          public void method() { 
              card.initialize();
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step init_data in protocol initialize.
     */
    Protocol_step init_data_step;



    /**
     * Initialize {@link #init_data_step}.
     * Initialize the step instance for step init_data in protocol initialize.
     */
    private void init_init_data_step() {
        if(init_data_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            data.n,
            data.ptls_key,
            data.bases,
            data.base_factors,
            data.current_attributes,
            data.montgomerized_one,
            data.montgomery_corrections
        };

        APDU_Serializable[] res = null;

        init_data_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new init_data_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #init_data_step}.
     * Update argument and result references in the step init_data
     * of protocol initialize.
     */
    public void update_init_data_step() {
        ASSERT(init_data_step != null);

        init_data_step.arguments[0] = data.n;
        init_data_step.arguments[1] = data.ptls_key;
        init_data_step.arguments[2] = data.bases;
        init_data_step.arguments[3] = data.base_factors;
        init_data_step.arguments[4] = data.current_attributes;
        init_data_step.arguments[5] = data.montgomerized_one;
        init_data_step.arguments[6] = data.montgomery_corrections;
        return;
    }



    //#########################################################################
    // initialize protocol definition
    // 

    /**
     * Protocol instance for protocol initialize.
     */
    public Protocol initialize_protocol;

    /**
     * Initialize {@link #initialize_protocol}.
     * Initialize the protocol instance for protocol initialize.
     */
    private void init_initialize_protocol() {
        if(initialize_protocol != null)
            return;

        init_init_data_step();

        Protocol_step[] steps = new Protocol_step[]{
            init_data_step
        };
        initialize_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #initialize_protocol}.
     * Update argument and result references in all 
     * steps of protocol initialize.
     */
    public void update_initialize_protocol() {
        update_init_data_step();
        initialize_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol resign
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step make_sig_hash in protocol resign.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class make_sig_hash_call implements Void_method {
          /** Empty constructor. */
          /* package */ make_sig_hash_call() {}

          /**
           * Run the card action for step make_sig_hash in protocol resign.
           */
          public void method() { 
              card.make_sig_hash();
              return;
          }
      }

      /**
       * Card action for step finish_signature in protocol resign.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class finish_signature_call implements Void_method {
          /** Empty constructor. */
          /* package */ finish_signature_call() {}

          /**
           * Run the card action for step finish_signature in protocol resign.
           */
          public void method() { 
              card.finish_signature(signature_accepted);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step get_signature in protocol resign.
     */
    Protocol_step get_signature_step;

    /**
     * Step instance for step make_sig_hash in protocol resign.
     */
    Protocol_step make_sig_hash_step;

    /**
     * Step instance for step finish_signature in protocol resign.
     */
    Protocol_step finish_signature_step;



    /**
     * Initialize {@link #get_signature_step}.
     * Initialize the step instance for step get_signature in protocol resign.
     */
    private void init_get_signature_step() {
        if(get_signature_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            data.applet_id,
            data.current_blinded_a,
            data.current_signature
        };

        get_signature_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #get_signature_step}.
     * Update argument and result references in the step get_signature
     * of protocol resign.
     */
    public void update_get_signature_step() {
        ASSERT(get_signature_step != null);

        get_signature_step.results[0] = data.applet_id;
        get_signature_step.results[1] = data.current_blinded_a;
        get_signature_step.results[2] = data.current_signature;
        return;
    }


    /**
     * Initialize {@link #make_sig_hash_step}.
     * Initialize the step instance for step make_sig_hash in protocol resign.
     */
    private void init_make_sig_hash_step() {
        if(make_sig_hash_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            data.host_alpha,
            data.remainders
        };

        APDU_Serializable[] res = new APDU_Serializable[]{
            data.sig_remainder
        };

        make_sig_hash_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new make_sig_hash_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #make_sig_hash_step}.
     * Update argument and result references in the step make_sig_hash
     * of protocol resign.
     */
    public void update_make_sig_hash_step() {
        ASSERT(make_sig_hash_step != null);

        make_sig_hash_step.arguments[0] = data.host_alpha;
        make_sig_hash_step.arguments[1] = data.remainders;
        make_sig_hash_step.results[0] = data.sig_remainder;
        return;
    }


    /**
     * Initialize {@link #finish_signature_step}.
     * Initialize the step instance for step finish_signature in protocol resign.
     */
    private void init_finish_signature_step() {
        if(finish_signature_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            data.host_response
        };

        APDU_Serializable[] res = new APDU_Serializable[]{
            signature_accepted
        };

        finish_signature_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new finish_signature_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #finish_signature_step}.
     * Update argument and result references in the step finish_signature
     * of protocol resign.
     */
    public void update_finish_signature_step() {
        ASSERT(finish_signature_step != null);

        finish_signature_step.arguments[0] = data.host_response;
        finish_signature_step.results[0] = signature_accepted;
        return;
    }



    //#########################################################################
    // resign protocol definition
    // 

    /**
     * Protocol instance for protocol resign.
     */
    public Protocol resign_protocol;

    /**
     * Initialize {@link #resign_protocol}.
     * Initialize the protocol instance for protocol resign.
     */
    private void init_resign_protocol() {
        if(resign_protocol != null)
            return;

        init_get_signature_step();
        init_make_sig_hash_step();
        init_finish_signature_step();

        Protocol_step[] steps = new Protocol_step[]{
            get_signature_step,
            make_sig_hash_step,
            finish_signature_step
        };
        resign_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #resign_protocol}.
     * Update argument and result references in all 
     * steps of protocol resign.
     */
    public void update_resign_protocol() {
        update_get_signature_step();
        update_make_sig_hash_step();
        update_finish_signature_step();
        resign_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol gate
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step commit in protocol gate.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class commit_call implements Void_method {
          /** Empty constructor. */
          /* package */ commit_call() {}

          /**
           * Run the card action for step commit in protocol gate.
           */
          public void method() { 
              card.proof_commit();
              return;
          }
      }

      /**
       * Card action for step respond in protocol gate.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class respond_call implements Void_method {
          /** Empty constructor. */
          /* package */ respond_call() {}

          /**
           * Run the card action for step respond in protocol gate.
           */
          public void method() { 
              card.respond_to_challenge();
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step commit in protocol gate.
     */
    Protocol_step commit_step;

    /**
     * Step instance for step respond in protocol gate.
     */
    Protocol_step respond_step;



    /**
     * Initialize {@link #commit_step}.
     * Initialize the step instance for step commit in protocol gate.
     */
    private void init_commit_step() {
        if(commit_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            data.applet_id,
            data.current_blinded_a,
            data.current_signature,
            data.result
        };

        commit_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new commit_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #commit_step}.
     * Update argument and result references in the step commit
     * of protocol gate.
     */
    public void update_commit_step() {
        ASSERT(commit_step != null);

        commit_step.results[0] = data.applet_id;
        commit_step.results[1] = data.current_blinded_a;
        commit_step.results[2] = data.current_signature;
        commit_step.results[3] = data.result;
        return;
    }


    /**
     * Initialize {@link #respond_step}.
     * Initialize the step instance for step respond in protocol gate.
     */
    private void init_respond_step() {
        if(respond_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            data.gamma_beta_3
        };

        APDU_Serializable[] res = new APDU_Serializable[]{
            data.remainders,
            data.result
        };

        respond_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new respond_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #respond_step}.
     * Update argument and result references in the step respond
     * of protocol gate.
     */
    public void update_respond_step() {
        ASSERT(respond_step != null);

        respond_step.arguments[0] = data.gamma_beta_3;
        respond_step.results[0] = data.remainders;
        respond_step.results[1] = data.result;
        return;
    }



    //#########################################################################
    // gate protocol definition
    // 

    /**
     * Protocol instance for protocol gate.
     */
    public Protocol gate_protocol;

    /**
     * Initialize {@link #gate_protocol}.
     * Initialize the protocol instance for protocol gate.
     */
    private void init_gate_protocol() {
        if(gate_protocol != null)
            return;

        init_commit_step();
        init_respond_step();

        Protocol_step[] steps = new Protocol_step[]{
            commit_step,
            respond_step
        };
        gate_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #gate_protocol}.
     * Update argument and result references in all 
     * steps of protocol gate.
     */
    public void update_gate_protocol() {
        update_commit_step();
        update_respond_step();
        gate_protocol.set_result_sizes();
    }


    /**
     * Update all protocols in this object.
     * Update all argument and result references in all
     * steps of all protocol instances described in RSA_card_protocol.id.
     */
    public void update_all() {
        update_allocate_protocol();
        update_initialize_protocol();
        update_resign_protocol();
        update_gate_protocol();
    }


    /**
     * Initialization of delayed protocols.
     * Initialization of those protocols and their steps
     * that are declared as delayed in RSA_card_protocol.id.
     */
    public void delayed_init() {
        init_initialize_protocol();
        init_resign_protocol();
        init_gate_protocol();
    }


    //#########################################################################
    //#########################################################################
    // 
    // constructor: initialize protocols
    // 
    //#########################################################################
    //#########################################################################

    /**
     * Construct protocol descriptions.
     * Construct and initialize the protocol descriptions
     * for all protocols described in RSA_card_protocol.id,
     * except for those that are declared as delayed there.
     */
    public RSA_card_protocol_description(Front_protocols front_protocols) {
        // initialize variables
        data = new RSA_data();
        short_bignat_size = new APDU_short();
        long_bignat_size = new APDU_short();
        attribute_length = new APDU_short();
        mont_correction_len = new APDU_short();
        signature_accepted = new APDU_boolean();
        #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
            card = new RSA_CARD(data, front_protocols);
        #endif

        // constructor statements
        this.front_protocols = front_protocols;

        // initialize protocols
        init_allocate_protocol();
        return;
    }
}

