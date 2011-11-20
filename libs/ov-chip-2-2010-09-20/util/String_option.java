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
// Created 29.5.09 by Hendrik
// 
// command line option with String argument
// 
// $Id: String_option.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


/**
 * Command line switch with a String argument. When the option is
 * found the string is recorded in a reference.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class String_option extends Option {

    /**
     * 
     * The reference where the option argument will be stored.
     */
    private Reference<String> ref;

    /**
     * 
     * Construct a string option. When found on the
     * command line, the reference {@code ref} will be set to the
     * next command line element.
     * 
     * @param option the literal option string as it will appear on
     * the command line
     * @param ref the string reference 
     * @param argument name of the argument for the explanation
     * @param explanation explanation for the usage information
     */
    public String_option(String option, Reference<String> ref, 
                         String argument, String explanation) 
    {
        super(option, argument, explanation);
        this.ref = ref;
    }


    /**
     * 
     * Retrieve a String argument from the command line. Displays an
     * error message and terminates the application if there is no
     * next command line element. 
     * 
     * @param cl the command line instance
     * @return next unprocessed command line element as String
     */
    public String get_string_argument(Commandline cl) {
        String next = cl.retrieve_option();
        if(next != null) {
            return next;
        }

        // Still here? Then we have an error!
        System.out.flush();
        System.err.format("option %s requires a string argument",
                          option);
        System.exit(1);
        return "";
    }


    /**
     * 
     * Called when the option is recognized. Retrieves the next
     * command line element and stores it in the String reference
     * {@link #ref}. 
     * 
     * @param cl command line instance
     */
    public void matched(Commandline cl) {
        ref.ref = get_string_argument(cl);
    }
}