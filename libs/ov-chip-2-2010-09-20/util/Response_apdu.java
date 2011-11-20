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
// Created 27.8.08 by Hendrik
//
// decompose the response apdu
//
// $Id: Response_apdu.java,v 1.18 2009-05-19 14:59:53 tews Exp $


package ds.ov2.util;

import java.io.PrintWriter;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.CardException;
import javacard.framework.ISO7816;


/** 
 * Utility functions around Response APDU's. Takes the status words
 * defined for the OV-chip project (in {@link Response_status}) into
 * account. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.18 $
 * @commitdate $Date: 2009-05-19 14:59:53 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Response_apdu {

    /**
     * 
     * The raw response APDU.
     */
    private ResponseAPDU response_apdu;


    /**
     * 
     * The pure response status. For status words that carry an
     * argument as for instance {@link ISO7816#SW_BYTES_REMAINING_00}
     * the argument is stripped off.
     */
    private short response_code;


    /**
     * 
     * The response status argument. -1 for status words that do not
     * carry an argument.
     */
    private short response_argument = -1;


    /**
     * 
     * A readable representation of the respose status. Lazily
     * computed in {@link #get_name}.
     */
    private String name = null;


    /**
     * 
     * Check whether the response status is OK. This is the case if it
     * equals {@link ISO7816#SW_NO_ERROR} or 0x9000. Negation of
     * {@link #error}.
     * 
     * @return true if the response status is {@link
     * ISO7816#SW_NO_ERROR}; 
     */
    public boolean OK() { 
        return response_code == ISO7816.SW_NO_ERROR; 
    }


    /**
     * 
     * Check whether the respose status indicates an error. This is
     * the case if the status is different from {@link
     * ISO7816#SW_NO_ERROR} and 0x9000. Negation of {@link #OK}.
     * 
     * @return true if the response status is different from {@link
     * ISO7816#SW_NO_ERROR}
     */
    public boolean error() { 
        return response_code != ISO7816.SW_NO_ERROR; 
    }


    /**
     * 
     * Return the pure response status. For status words that carry an
     * argument, as for instance {@link ISO7816#SW_BYTES_REMAINING_00},
     * the argument is stripped off.
     * 
     * @return the response status without potential arguments, 
     */
    public short get_code() { return response_code; }


    /**
     * 
     * Return the response status as obtained from the card.
     *
     * @return original response status
     */
    public short get_status() { 
        return response_argument == -1 ? response_code 
            : (short)(response_code | response_argument);
    }


    /**
     * 
     * Argument of the respose status.
     * 
     * @return the argument of the response status or -1 if the status
     * did not contain an argument.
     */
    public short get_argument() { return response_argument; }


    /**
     * 
     * Length of the data in the response APDU.
     * 
     * @return length in bytes
     */
    public int get_length() { return response_apdu.getNr(); }


    /**
     * 
     * The data of the response APDU.
     * 
     * @return the data of this response APDU
     */
    public byte[] get_data() { return response_apdu.getData(); }


    /**
     * 
     * Constructs a new response APDU from a raw response APDU {@code
     * r}. 
     * 
     * @param r the raw response APDU
     */
    public Response_apdu(ResponseAPDU r) {
        response_apdu = r;

        // Tests suggest that we don't need the 0xffff, but the 
        // documentation is not clear about it, so leave it in.
        response_code = (short)(r.getSW());

        switch(response_code & 0xff00) {
        case Response_status.OV_ASSERTION_00 & 0xff00:
        case Response_status.OV_TEST_FAILED_00 & 0xff00:
        case ISO7816.SW_BYTES_REMAINING_00 & 0xff00:
        case ISO7816.SW_CORRECT_LENGTH_00 & 0xff00:
            response_argument = (short)(response_code & 0x00ff);
            response_code = (short)(response_code & 0xff00);
        }

        return;
    }


    /**
     * 
     * Return a readable name of the response status. Knows about all
     * response status code in Java Card 2.2.1 and the OV-chip
     * specific ones from {@link Response_status}.
     * 
     * @return name of the response status
     */
    public String get_name() {
        if(name != null)
            return name;

        switch(response_code & 0xffff) {

        //###################################################################
        // First the OV specific response codes
        // 

// OV_UNEXPECTED_PROTOCOL_ID
// OV_UNEXPECTED_PROTOCOL_STEP
// OV_UNEXPECTED_BATCH
// OV_WRONG_RESPONSE_LENGTH
// OV_RSA_NOPAD_CIPHER_FAILURE
// OV_RSA_KEY_FAILURE
// OV_RSA_MOD_FAILURE
// OV_RSA_EXP_FAILURE
// OV_BAD_HOST_DATA
// OV_TEST_FAILED_00
// OV_ASSERTION_00

        case Response_status.OV_UNEXPECTED_PROTOCOL_ID & 0xffff:
            name = "OV_UNEXPECTED_PROTOCOL_ID";
            break;
        case Response_status.OV_UNEXPECTED_PROTOCOL_STEP & 0xffff:
            name = "OV_UNEXPECTED_PROTOCOL_STEP";
            break;
        case Response_status.OV_UNEXPECTED_BATCH & 0xffff:
            name = "OV_UNEXPECTED_BATCH";
            break;
        case Response_status.OV_WRONG_RESPONSE_LENGTH & 0xffff:
            name = "OV_WRONG_RESPONSE_LENGTH";
            break;
        case Response_status.OV_RSA_NOPAD_CIPHER_FAILURE & 0xffff:
            name = "OV_RSA_NOPAD_CIPHER_FAILURE";
            break;
        case Response_status.OV_RSA_KEY_FAILURE & 0xffff:
            name = "OV_RSA_KEY_FAILURE";
            break;
        case Response_status.OV_RSA_MOD_FAILURE & 0xffff:
            name = "OV_RSA_MOD_FAILURE";
            break;
        case Response_status.OV_RSA_EXP_FAILURE & 0xffff:
            name = "OV_RSA_EXP_FAILURE";
            break;
        case Response_status.OV_BAD_HOST_DATA & 0xffff:
            name = "OV_BAD_HOST_DATA";
            break;
        case Response_status.OV_TEST_FAILED_00 & 0xffff:
            name = "OV_TEST_FAILED_00";
            break;
        case Response_status.OV_ASSERTION_00 & 0xffff:
            name = "OV_ASSERTION_00";
            break;


        //###################################################################
        // Standard response codes
        // 

        case ISO7816.SW_APPLET_SELECT_FAILED & 0xffff:
            name = "SW_APPLET_SELECT_FAILED";
            break;
        case ISO7816.SW_BYTES_REMAINING_00 & 0xffff:
            name = "SW_BYTES_REMAINING_00";
            break;
        case ISO7816.SW_CLA_NOT_SUPPORTED & 0xffff:
            name = "SW_CLA_NOT_SUPPORTED";
            break;
        // only in 2.2.2
        // case ISO7816.SW_COMMAND_CHAINING_NOT_SUPPORTED & 0xffff:
        //     name = "SW_COMMAND_CHAINING_NOT_SUPPORTED";
        //     break;
        case ISO7816.SW_COMMAND_NOT_ALLOWED & 0xffff:
            name = "SW_COMMAND_NOT_ALLOWED";
            break;
        case ISO7816.SW_CONDITIONS_NOT_SATISFIED & 0xffff:
            name = "SW_CONDITIONS_NOT_SATISFIED";
            break;
        case ISO7816.SW_CORRECT_LENGTH_00 & 0xffff:
            name = "SW_CORRECT_LENGTH_00";
            break;
        case ISO7816.SW_DATA_INVALID & 0xffff:
            name = "SW_DATA_INVALID";
            break;
        case ISO7816.SW_FILE_FULL & 0xffff:
            name = "SW_FILE_FULL";
            break;
        case ISO7816.SW_FILE_INVALID & 0xffff:
            name = "SW_FILE_INVALID";
            break;
        case ISO7816.SW_FILE_NOT_FOUND & 0xffff:
            name = "SW_FILE_NOT_FOUND";
            break;
        case ISO7816.SW_FUNC_NOT_SUPPORTED & 0xffff:
            name = "SW_FUNC_NOT_SUPPORTED";
            break;
        case ISO7816.SW_INCORRECT_P1P2 & 0xffff:
            name = "SW_INCORRECT_P1P2";
            break;
        case ISO7816.SW_INS_NOT_SUPPORTED & 0xffff:
            name = "SW_INS_NOT_SUPPORTED";
            break;
        // only in 2.2.2
        // case ISO7816.SW_LAST_COMMAND_EXPECTED & 0xffff:
        //     name = "SW_LAST_COMMAND_EXPECTED";
        //     break;
        case ISO7816.SW_LOGICAL_CHANNEL_NOT_SUPPORTED & 0xffff:
            name = "SW_LOGICAL_CHANNEL_NOT_SUPPORTED";
            break;
        case ISO7816.SW_NO_ERROR & 0xffff:
            // name = "SW_NO_ERROR";
            name = "OK";
            break;
        case ISO7816.SW_RECORD_NOT_FOUND & 0xffff:
            name = "SW_RECORD_NOT_FOUND";
            break;
        case ISO7816.SW_SECURE_MESSAGING_NOT_SUPPORTED & 0xffff:
            name = "SW_SECURE_MESSAGING_NOT_SUPPORTED";
            break;
        case ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED & 0xffff:
            name = "SW_SECURITY_STATUS_NOT_SATISFIED";
            break;
        case ISO7816.SW_UNKNOWN & 0xffff:
            name = "SW_UNKNOWN";
            break;
        case ISO7816.SW_WARNING_STATE_UNCHANGED & 0xffff:
            name = "SW_WARNING_STATE_UNCHANGED";
            break;
        case ISO7816.SW_WRONG_DATA & 0xffff:
            name = "SW_WRONG_DATA";
            break;
        case ISO7816.SW_WRONG_LENGTH & 0xffff:
            name = "SW_WRONG_LENGTH";
            break;
        case ISO7816.SW_WRONG_P1P2 & 0xffff:
            name = "SW_WRONG_P1P2";
            break;

        default:
            name = "Unknown response status";
            break;
        }

        return name;
    }


    /**
     * Print this response APDU in human readable form to {@code out}. 
     * Print the data in the body if {@code with_message} is true, 
     * otherwise the data is supressed.
     *
     * @param out channel to print to, pass null to disable printing
     * @param with_message print payload data if true
     */
    public void print(PrintWriter out, boolean with_message) {
        if(out == null)
            return;
        if(response_argument == -1 ) {
            out.format("Status %s (0x%04X), received %d bytes",
                       get_name(), 
                       response_code & 0xffff, 
                       response_apdu.getNr());
        }
        else {
            out.format("Status %s %d (0x%04X), received %d bytes",
                       get_name(), 
                       response_argument,
                       (response_code | response_argument) & 0xffff,
                       response_apdu.getNr());
        }
        if(with_message) {
            if(response_apdu.getNr() == 0)
                out.println(" no data");
            else {
                byte[] data = response_apdu.getData();
                out.format(" data: %02X", data[0]);
                for(int i = 1; i < response_apdu.getNr(); i++) {
                    out.format("%02X", data[i]);
                }
                out.println("");
            }
        }
    }


    //########################################################################
    // APDU Exceptions
    // 


    /**
     * 
     * Exception of response APDU's that indicate an error. Every such
     * exception is linked to a response APDU (because this is a
     * non-static inner class) and the status and its argument can be
     * accessed through the exception.
     */
    public class Card_response_error extends CardException {

        /**
         * 
         * Field to disable the serialVersionUID warning.
         */
        public static final long serialVersionUID = 1L;


        /**
         * 
         * Return the pure response status. For status words that
         * carry an argument, as for instance {@link
         * ISO7816#SW_BYTES_REMAINING_00}, the argument is stripped
         * off.
         * 
         * @return the response status without potential arguments,
         */
        public short response_code() { 
            return response_code;
        }


        /**
         * 
         * Argument of the respose status.
         * 
         * @return the argument of the response status or -1 if the status
         * did not contain an argument.
         * 
         */
        public short response_argument() {
            return response_argument;
        }


        /**
         * 
         * Create a new response status exception out of the
         * containing response status object.
         * 
         */
        public Card_response_error() {
            // Call to super must be the first statement, can't use 
            // a conventional if.
            super(
                  response_argument != -1 ?
                      String.format("APDU response error %s (0x%04X) value %d",
                                    get_name(), 
                                    (response_code | response_argument) 
                                        & 0xffff, 
                                    response_argument)
                  :
                      String.format("APDU response error %s (0x%04X)",
                                    get_name(), 
                                    response_code & 0xffff));
        }
    }


    /**
     * 
     * Check whether the response status is OK and the contained data
     * has the expected length. Return normally if the check passes
     * and through an {@link Card_response_error} if the check fails.
     * This method combines precisely all the tests that are needed
     * after the OV-chip protocol layer received a response APDU.
     * <P>
     *
     * If the status is ok, but the length of the data is wrong, then
     * the status of this response APDU is set to {@link
     * Response_status#OV_WRONG_RESPONSE_LENGTH} and an {@link
     * Card_response_error} is thrown.
     * 
     * @param check_expected_length the expected length of the data in
     * the response APDU; the special value -1 indicates that 255 and
     * 256 data bytes are ok.
     * @throws Card_response_error in case the status is not ok or the
     * length of the response data does not match
     */
    public void throw_if_not_ok(int check_expected_length) 
        throws Card_response_error 
    {
        if(! OK() )
            throw new Card_response_error();
        if((response_apdu.getNr() == check_expected_length) ||
           (check_expected_length == -1 && 
            (response_apdu.getNr() == 255 || response_apdu.getNr() == 256)))
            return;

        // Wrong length. Set the error code to the special 
        // OV_WRONG_RESPONSE_LENGTH, which indicates that the response 
        // length does not meet the expectations of the host.
        response_code = Response_status.OV_WRONG_RESPONSE_LENGTH;
        name = null;
        throw new Card_response_error();
    }
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
