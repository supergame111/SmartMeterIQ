//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/bignat/../util/Commandline.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/bignat/../util/Commandline.java"
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
// Created 1.2.09 by Hendrik
// 
// small utilities for parsing the command line
// 
// $Id: Commandline.java,v 1.2 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


/**
 * Command line instance that adds an index and the notion of the next
 * unprocessed command line element to the string array received in
 * the main method. 
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.2 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host, card
 * @CPP no cpp preprocessing needed
 */

public class Commandline {

    /**
     * 
     * The command line as received in the main method.
     */
    private final String[] args;


    /**
     * 
     * The index of the next element in the command line.
     * Automatically incremented in {@link #match match} and {@link
     * #retrieve_option}.
     */
    private int next_option = 0;


    /**
     * 
     * Constructor. Initialize the fields of this instance.
     * 
     * @param args the command line as received in the main method
     */
    public Commandline(String[] args) {
        this.args = args;
    }


    /**
     * 
     * Return true if not all command-line elements have been
     * processed yet.
     * 
     * @return true if {@link #next_option} still points to some
     * unprocessed command-line element
     */
    public boolean continue_parsing() {
        return next_option < args.length;
    }


    /**
     * 
     * Return the next unprocessed command-line element without
     * incrementing the option index {@link #next_option}.
     * 
     * @return next option to be parsed
     */
    public String next_option() {
        return args[next_option];
    }


    /**
     * 
     * Return the next command unprocessed line element and
     * increment the option index {@link #next_option}.
     * 
     * @return next command line element, or null if no further
     * element is available
     */
    public String retrieve_option() {
        if(next_option < args.length)
            return args[next_option++];
        else
            return null;
    }


    /**
     * 
     * Check if the next unprocessed command-line element is equal to
     * {@code option}. If it is, the method returns true and the
     * command-line element is considered to be processed and the
     * index of the next unprocessed command line element {@link
     * #next_option} is incremented. The actual state changes to
     * record that the option {@code option} has been recognized is
     * done in {@link Option#matched Option.matched}.
     * 
     * @param option option to check agains the next command line element
     * @return true if {@code option} equals the next command line element
     */
    public boolean match(String option) {
        if(args[next_option].equals(option)) {
            next_option += 1;
            return true;
        }
        else {
            return false;
        }
    }
}
