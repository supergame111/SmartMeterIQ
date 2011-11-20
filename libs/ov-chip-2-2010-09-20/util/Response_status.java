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
// Created 26.8.08 by Hendrik
//
// ov-chip specific respose status words
//
// $Id: Response_status.java,v 1.10 2009-04-09 10:42:17 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


/** 
 * Response status words for the OV-ship project and the OV-chip
 * protocol layer. I read somewhere that the status words 0x9xxx that
 * are different from 0x9000 can be defined and used in application. I
 * am not completely sure this is true, but until now it works for all
 * the cards that I am using.
 * <P>
 *
 * Static class containing constants only.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.10 $
 * @commitdate $Date: 2009-04-09 10:42:17 $ by $Author: tews $
 * @environment host
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>
 */
PUBLIC class Response_status {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected Response_status() {}


    /**
     * 
     * Wrong protocol.
     * An response status of OV_UNEXPECTED_PROTOCOL_ID (value
     * 0x9101) indicates that the state of the applet does not permit
     * the protocol identification (INS byte) that was
     * contained in the command APDU.
     */
    public static final short OV_UNEXPECTED_PROTOCOL_ID = (short)(0x9101);


    /**
     * 
     * Wrong step. An response status of OV_UNEXPECTED_PROTOCOL_STEP
     * (value 0x9102) indicates that the state of the applet does not
     * permit an command the step number (P1 byte) that was contained
     * in the command APDU.
     */
    public static final short OV_UNEXPECTED_PROTOCOL_STEP = (short)(0x9102);


    /**
     * 
     * Wrong batch. An response status of OV_UNEXPECTED_BATCH (value
     * 0x9103) indicates that the state of the applet does not permit
     * the batch (P2 byte) that was contained in the command APDU.
     */
    public static final short OV_UNEXPECTED_BATCH = (short)(0x9103);


    /**
     * 
     * Unexpected response length. An response status of
     * OV_WRONG_RESPONSE_LENGTH (value 0x9104) indicates that the
     * applet has sent a different number of bytes than was expected
     * by the host. This status is never send by the card. It is
     * generated in {@link Response_apdu#throw_if_not_ok
     * Response_apdu.throw_if_not_ok} for APDU without error but
     * unexpected response length.
     */
    public static final short OV_WRONG_RESPONSE_LENGTH = (short)(0x9104);


    /**
     * 
     * RSA NOPAD cipher initialization failed. An response status of
     * OV_RSA_NOPAD_CIPHER_FAILURE (value 0x9105) indicates that the
     * factory method for creating the RSA NOPAD cipher failed. This
     * typically indicates that the card does not implement this
     * chipher. 
     */
    public static final short OV_RSA_NOPAD_CIPHER_FAILURE = (short)(0x9105);


    /**
     * 
     * RSA public key creation failed. An response status of
     * OV_RSA_KEY_FAILURE (value 0x9106) indicates that the creation
     * of the RSA public key failed. This typically indicates that the
     * requested key length is not supported by the card.
     */
    public static final short OV_RSA_KEY_FAILURE = (short)(0x9106);


    /**
     * 
     * RSA modulus initialization failed. An response status of
     * OV_RSA_MOD_FAILURE (value 0x9107) indicates that setting the
     * RSA modulus in the key failed. This typically happens if the
     * length of the modulus does not match the key length.
     */
    public static final short OV_RSA_MOD_FAILURE = (short)0x9107;


    /**
     * 
     * RSA exponent initialization failed. An response status of
     * OV_RSA_EXP_FAILURE (value 0x9108) indicates that setting the
     * exponent in the RSA key failed. This should only happen if the
     * exponent length is wrong. However on the NXP cards that I have
     * this exception is always thrown for certain key length. I
     * interpred this as a late insight of the card that it does not
     * support the requested key length.
     */
    public static final short OV_RSA_EXP_FAILURE = (short)0x9108;


    /**
     * 
     * Bad host challenge. An response status of OV_BAD_HOST_DATA
     * (value 0x9109) indicates that the data the host sent does not
     * fullfill the conditions that are prescribed in the protocol.
     */
    public static final short OV_BAD_HOST_DATA = (short)(0x9109);


    /**
     * 
     * Test failed in the test applet. An response status of
     * OV_TEST_FAILED_00 (value 0x9Exx) indicates that the test applet
     * has detected an error. The less significant byte of this status
     * is a tag that indicates which test failed. See <a
     * href="../../../overview-summary.html#test_failed_tags">the list
     * of currently used OV_TEST_FAILED_00 tags<a>.
     */
    public static final short OV_TEST_FAILED_00 = (short)(0x9E00);


    /**
     * 
     * Assertion failure. An response status of OV_ASSERTION_00 (value
     * 0x9Fxx) indicates that an assertion failed during applet
     * execution. The less significant byte of this status equals the
     * tag argument of <a
     * href="../../../overview-summary.html#ASSERT_TAG">ASSERT_TAG<a>
     * or {@link Misc#myassert Misc.myassert}. For a failed <a
     * href="../../../overview-summary.html#ASSERT">ASSERT<a> the
     * argument is 0. See also the <a
     * href="../../../overview-summary.html#assert_tags">list of used
     * assert tags<a>.

     */
    public static final short OV_ASSERTION_00 = (short)(0x9F00);

    // public static final short ;

}


// java card 2.2.1 response codes
//
//
//    SW_APPLET_SELECT_FAILED                 0x6999
//    SW_BYTES_REMAINING_00                   0x6100
//    SW_CLA_NOT_SUPPORTED                    0x6E00
//    SW_COMMAND_NOT_ALLOWED                  0x6986
//    SW_CONDITIONS_NOT_SATISFIED             0x6985
//    SW_CORRECT_LENGTH_00                    0x6C00
//    SW_DATA_INVALID                         0x6984
//    SW_FILE_FULL                            0x6A84
//    SW_FILE_INVALID                         0x6983
//    SW_FILE_NOT_FOUND                       0x6A82
//    SW_FUNC_NOT_SUPPORTED                   0x6A81
//    SW_INCORRECT_P1P2                       0x6A86
//    SW_INS_NOT_SUPPORTED                    0x6D00
//    SW_LOGICAL_CHANNEL_NOT_SUPPORTED        0x6881
//    SW_NO_ERROR                             0x9000
//    SW_RECORD_NOT_FOUND                     0x6A83
//    SW_SECURE_MESSAGING_NOT_SUPPORTED       0x6882
//    SW_SECURITY_STATUS_NOT_SATISFIED        0x6982
//    SW_UNKNOWN                              0x6F00
//    SW_WARNING_STATE_UNCHANGED              0x6200
//    SW_WRONG_DATA                           0x6A80
//    SW_WRONG_LENGTH                         0x6700
//    SW_WRONG_P1P2                           0x6B00
//
//
// Sorted for values
//    
//    0x6100      SW_BYTES_REMAINING_00
//    0x6200      SW_WARNING_STATE_UNCHANGED
//    0x6700      SW_WRONG_LENGTH
//    0x6881      SW_LOGICAL_CHANNEL_NOT_SUPPORTED
//    0x6882      SW_SECURE_MESSAGING_NOT_SUPPORTED
//    0x6982      SW_SECURITY_STATUS_NOT_SATISFIED
//    0x6983      SW_FILE_INVALID
//    0x6984      SW_DATA_INVALID
//    0x6985      SW_CONDITIONS_NOT_SATISFIED
//    0x6986      SW_COMMAND_NOT_ALLOWED
//    0x6999      SW_APPLET_SELECT_FAILED
//    0x6A80      SW_WRONG_DATA
//    0x6A81      SW_FUNC_NOT_SUPPORTED
//    0x6A82      SW_FILE_NOT_FOUND
//    0x6A83      SW_RECORD_NOT_FOUND
//    0x6A84      SW_FILE_FULL
//    0x6A86      SW_INCORRECT_P1P2
//    0x6B00      SW_WRONG_P1P2
//    0x6C00      SW_CORRECT_LENGTH_00
//    0x6D00      SW_INS_NOT_SUPPORTED
//    0x6E00      SW_CLA_NOT_SUPPORTED
//    0x6F00      SW_UNKNOWN
//    0x9000      SW_NO_ERROR
//        
//    
// added in 2.2.2:
//
//     SW_COMMAND_CHAINING_NOT_SUPPORTED
//     SW_LAST_COMMAND_EXPECTED
//
//
// java card 2.2.2 response codes
//
//     SW_APPLET_SELECT_FAILED
//     SW_BYTES_REMAINING_00
//     SW_CLA_NOT_SUPPORTED
//     SW_COMMAND_CHAINING_NOT_SUPPORTED
//     SW_COMMAND_NOT_ALLOWED
//     SW_CONDITIONS_NOT_SATISFIED
//     SW_CORRECT_LENGTH_00
//     SW_DATA_INVALID
//     SW_FILE_FULL
//     SW_FILE_INVALID
//     SW_FILE_NOT_FOUND
//     SW_FUNC_NOT_SUPPORTED
//     SW_INCORRECT_P1P2
//     SW_INS_NOT_SUPPORTED
//     SW_LAST_COMMAND_EXPECTED
//     SW_LOGICAL_CHANNEL_NOT_SUPPORTED
//     SW_NO_ERROR
//     SW_RECORD_NOT_FOUND
//     SW_SECURE_MESSAGING_NOT_SUPPORTED
//     SW_SECURITY_STATUS_NOT_SATISFIED
//     SW_UNKNOWN
//     SW_WARNING_STATE_UNCHANGED
//     SW_WRONG_DATA
//     SW_WRONG_LENGTH
//     SW_WRONG_P1P2
