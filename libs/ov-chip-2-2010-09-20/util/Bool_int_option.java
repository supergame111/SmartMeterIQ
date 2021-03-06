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
// command line option with int argument setting a boolean as well
// 
// $Id: Bool_int_option.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


/**
 * {@link Int_option} that additionally sets a boolean reference to
 * true. 
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Bool_int_option extends Int_option {

    /**
     * 
     * The boolean reference.
     */
    private Reference<Boolean> bool_ref;


    /**
     * 
     * Construct an bool-integer option. When found on the command
     * line, the boolean reference will be set to true and the int
     * reference will be set to the value of the next command line
     * element. 
     * 
     * @param option the literal option string as it will appear on
     * the command line
     * @param bool_ref the boolean reference
     * @param int_ref the int reference 
     * @param argument name of the argument for the explanation
     * @param explanation explanation for the usage information
     */
    public Bool_int_option(String option, 
                           Reference<Boolean> bool_ref, 
                           Reference<Integer> int_ref, 
                           String argument, String explanation) 
    {
        super(option, int_ref, argument, explanation);
        this.bool_ref = bool_ref;
    }


    /**
     * 
     * Called when the option is recognized. Converts the next command
     * line element to an integer and puts it into the int reference.
     * Additionally set the boolean reference to true.
     * Prints an error and exits the program on any problem.
     * 
     * @param cl command line instance
     */
    public void matched(Commandline cl) {
        super.matched(cl);
        bool_ref.ref = true;
    }
}