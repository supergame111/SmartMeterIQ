// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Misc_protocols.id
// by some sort of idl compiler.

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.test;
#endif


#if defined(JAVACARD_APPLET) || defined(JAVADOC)
  import javacard.framework.APDU;
  import javacard.framework.JCSystem;
#endif
#if !defined(JAVACARD_APPLET) || defined(JAVADOC)
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Protocol_step;
  import ds.ov2.util.Protocol;
  import ds.ov2.util.APDU_boolean;
  import ds.ov2.util.APDU_short;
  import ds.ov2.util.APDU_byte_array;
#endif

#if defined(APPLET_TESTFRAME) || defined(JAVADOC)
   import ds.ov2.util.Void_method;
   import ds.ov2.util.Empty_void_method;
#endif

/**
 * Protocol description for Misc_protocols. Defines suitable Protocol's and Protocol_steps for all protocols described in Misc_protocols.id for use in the OV-chip protocol layer.
 * 
 * @author idl compiler
 * @version automatically generated from Misc_protocols.id
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class Misc_protocols_description {

    //#########################################################################
    // Variable declarations
    // 

    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short short_bignat_size;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short long_bignat_size;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short double_bignat_size;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short cipher_size;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short mem_persistent;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short mem_transient_reset;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short mem_transient_deselect;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ Test_protocols test_protocols;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short max_short_bignat_size;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short max_long_bignat_size;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short max_double_bignat_size;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_short max_vector_length;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_byte_array cap_creation_time;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_boolean assertions_on;
    /**
     * Variable declaration from Misc_protocols.id.
     */
    /* package local */ APDU_boolean use_squared_rsa_mult_4;


    //#########################################################################
    //#########################################################################
    // 
    // Protocol Ping
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step ping in protocol Ping.
     */
    Protocol_step ping_step;



    /**
     * Initialize {@link #ping_step}.
     * Initialize the step instance for step ping in protocol Ping.
     */
    private void init_ping_step() {
        if(ping_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        ping_step = 
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
     * Update step instance in {@link #ping_step}.
     * Update argument and result references in the step ping
     * of protocol Ping.
     */
    public void update_ping_step() {
        ASSERT(ping_step != null);

        return;
    }



    //#########################################################################
    // Ping protocol definition
    // 

    /**
     * Protocol instance for protocol Ping.
     */
    public Protocol ping_protocol;

    /**
     * Initialize {@link #ping_protocol}.
     * Initialize the protocol instance for protocol Ping.
     */
    private void init_ping_protocol() {
        if(ping_protocol != null)
            return;

        init_ping_step();

        Protocol_step[] steps = new Protocol_step[]{
            ping_step
        };
        ping_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #ping_protocol}.
     * Update argument and result references in all 
     * steps of protocol Ping.
     */
    public void update_ping_protocol() {
        update_ping_step();
        ping_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol set_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step set_size in protocol set_size.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class set_size_call implements Void_method {
          /** Empty constructor. */
          /* package */ set_size_call() {}

          /**
           * Run the card action for step set_size in protocol set_size.
           */
          public void method() { 
              Resize.resize_bignats(short_bignat_size.value, 
                               long_bignat_size.value, 
                               double_bignat_size.value, 
                               cipher_size.value); 
         test_protocols.set_result_sizes();;
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step set_size in protocol set_size.
     */
    Protocol_step set_size_step;



    /**
     * Initialize {@link #set_size_step}.
     * Initialize the step instance for step set_size in protocol set_size.
     */
    private void init_set_size_step() {
        if(set_size_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            short_bignat_size,
            long_bignat_size,
            double_bignat_size,
            cipher_size
        };

        APDU_Serializable[] res = null;

        set_size_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new set_size_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #set_size_step}.
     * Update argument and result references in the step set_size
     * of protocol set_size.
     */
    public void update_set_size_step() {
        ASSERT(set_size_step != null);

        set_size_step.arguments[0] = short_bignat_size;
        set_size_step.arguments[1] = long_bignat_size;
        set_size_step.arguments[2] = double_bignat_size;
        set_size_step.arguments[3] = cipher_size;
        return;
    }



    //#########################################################################
    // set_size protocol definition
    // 

    /**
     * Protocol instance for protocol set_size.
     */
    public Protocol set_size_protocol;

    /**
     * Initialize {@link #set_size_protocol}.
     * Initialize the protocol instance for protocol set_size.
     */
    private void init_set_size_protocol() {
        if(set_size_protocol != null)
            return;

        init_set_size_step();

        Protocol_step[] steps = new Protocol_step[]{
            set_size_step
        };
        set_size_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #set_size_protocol}.
     * Update argument and result references in all 
     * steps of protocol set_size.
     */
    public void update_set_size_protocol() {
        update_set_size_step();
        set_size_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol mem_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step mem_size in protocol mem_size.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class mem_size_call implements Void_method {
          /** Empty constructor. */
          /* package */ mem_size_call() {}

          /**
           * Run the card action for step mem_size in protocol mem_size.
           */
          public void method() { 
              
      mem_persistent.value = 
          JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_PERSISTENT); 
      mem_transient_reset.value = 
          JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_TRANSIENT_RESET); 
      mem_transient_deselect.value = 
          JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_TRANSIENT_DESELECT);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step mem_size in protocol mem_size.
     */
    Protocol_step mem_size_step;



    /**
     * Initialize {@link #mem_size_step}.
     * Initialize the step instance for step mem_size in protocol mem_size.
     */
    private void init_mem_size_step() {
        if(mem_size_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            mem_persistent,
            mem_transient_reset,
            mem_transient_deselect
        };

        mem_size_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new mem_size_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mem_size_step}.
     * Update argument and result references in the step mem_size
     * of protocol mem_size.
     */
    public void update_mem_size_step() {
        ASSERT(mem_size_step != null);

        mem_size_step.results[0] = mem_persistent;
        mem_size_step.results[1] = mem_transient_reset;
        mem_size_step.results[2] = mem_transient_deselect;
        return;
    }



    //#########################################################################
    // mem_size protocol definition
    // 

    /**
     * Protocol instance for protocol mem_size.
     */
    public Protocol mem_size_protocol;

    /**
     * Initialize {@link #mem_size_protocol}.
     * Initialize the protocol instance for protocol mem_size.
     */
    private void init_mem_size_protocol() {
        if(mem_size_protocol != null)
            return;

        init_mem_size_step();

        Protocol_step[] steps = new Protocol_step[]{
            mem_size_step
        };
        mem_size_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #mem_size_protocol}.
     * Update argument and result references in all 
     * steps of protocol mem_size.
     */
    public void update_mem_size_protocol() {
        update_mem_size_step();
        mem_size_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol status
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step status in protocol status.
     */
    Protocol_step status_step;



    /**
     * Initialize {@link #status_step}.
     * Initialize the step instance for step status in protocol status.
     */
    private void init_status_step() {
        if(status_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            max_short_bignat_size,
            max_long_bignat_size,
            max_double_bignat_size,
            max_vector_length,
            cap_creation_time,
            assertions_on,
            use_squared_rsa_mult_4
        };

        status_step = 
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
     * Update step instance in {@link #status_step}.
     * Update argument and result references in the step status
     * of protocol status.
     */
    public void update_status_step() {
        ASSERT(status_step != null);

        status_step.results[0] = max_short_bignat_size;
        status_step.results[1] = max_long_bignat_size;
        status_step.results[2] = max_double_bignat_size;
        status_step.results[3] = max_vector_length;
        status_step.results[4] = cap_creation_time;
        status_step.results[5] = assertions_on;
        status_step.results[6] = use_squared_rsa_mult_4;
        return;
    }



    //#########################################################################
    // status protocol definition
    // 

    /**
     * Protocol instance for protocol status.
     */
    public Protocol status_protocol;

    /**
     * Initialize {@link #status_protocol}.
     * Initialize the protocol instance for protocol status.
     */
    private void init_status_protocol() {
        if(status_protocol != null)
            return;

        init_status_step();

        Protocol_step[] steps = new Protocol_step[]{
            status_step
        };
        status_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #status_protocol}.
     * Update argument and result references in all 
     * steps of protocol status.
     */
    public void update_status_protocol() {
        update_status_step();
        status_protocol.set_result_sizes();
    }


    /**
     * Update all protocols in this object.
     * Update all argument and result references in all
     * steps of all protocol instances described in Misc_protocols.id.
     */
    public void update_all() {
        update_ping_protocol();
        update_set_size_protocol();
        update_mem_size_protocol();
        update_status_protocol();
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
     * for all protocols described in Misc_protocols.id,
     * except for those that are declared as delayed there.
     */
    public Misc_protocols_description(Test_protocols test_protocols, APDU_short max_short_bignat_size, APDU_short max_long_bignat_size, APDU_short max_double_bignat_size, APDU_short max_vector_length, APDU_byte_array cap_creation_time) {
        // initialize variables
        short_bignat_size = new APDU_short();
        long_bignat_size = new APDU_short();
        double_bignat_size = new APDU_short();
        cipher_size = new APDU_short();
        mem_persistent = new APDU_short();;
        mem_transient_reset = new APDU_short();;
        mem_transient_deselect = new APDU_short();;
        assertions_on = new APDU_boolean();
        use_squared_rsa_mult_4 = new APDU_boolean();

        // constructor statements
        this.test_protocols = test_protocols;
        this.max_short_bignat_size = max_short_bignat_size;
        this.max_long_bignat_size = max_long_bignat_size;
        this.max_double_bignat_size = max_double_bignat_size;
        this.max_vector_length = max_vector_length;
        this.cap_creation_time = cap_creation_time;
        
  #ifdef NO_CARD_ASSERT 
    assertions_on.value = false; 
  #else 
    assertions_on.value = true; 
  #endif 
;
        
  #ifdef USE_SQUARED_RSA_MULT_4 
    use_squared_rsa_mult_4.value = true; 
  #else 
    use_squared_rsa_mult_4.value = false; 
  #endif 
;

        // initialize protocols
        init_ping_protocol();
        init_set_size_protocol();
        init_mem_size_protocol();
        init_status_protocol();
        return;
    }
}

