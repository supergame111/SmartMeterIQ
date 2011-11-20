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
// Abstract option
// 
// $Id: Option.java,v 1.1 2009-06-02 09:56:03 tews Exp $


package ds.ov2.util;

/**
 * Abstract command line option. Internally an option is almost a
 * record with one method. The record stores the option string and the
 * documentation. All this data is accessed from {@link
 * Parse_commandline}, which contains all the machinery for parsing
 * command lines. The only method in an option is {@link #matched
 * matched}, which is called by {@link Parse_commandline} when the
 * option has been recognized on the command line. 
 * <P>
 *
 * Clients need to override {@link #matched matched} with some code
 * that records the option and its possible arguments somewhere. 
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 *
 */
public abstract class Option {

    /**
     * 
     * The literally option string as it appears on the command line.
     */
    public final String option;

    /**
     * 
     * A string describing arguments, if any. See {@link #explanation}
     * for more information.
     */
    public final String argument;

    /**
     * 
     * The explanation of the option, to be printed in the usage
     * information. The usage information consists of one line for
     * each option, for instance
     * <PRE>
     *    -h      print help
     *    -i n    int argument n
     * </PRE>
     * To ease formatting of the usage information, option arguments
     * (such as the {@code n} in the example) are kept seperately in
     * the field {@link #argument}. The explanation in the right hand
     * column is kept in this field.
     * <P>
     *
     * See also {@link Parse_commandline#usage}.
     */
    public final String explanation;


    /**
     * 
     * Construct a new abstract option. Initialize the fields.
     * 
     * @param option the literal option string as it will appear on
     * the command line
     * @param argument a string describing any arguments of the
     * option, for the usuage information; might be null or the empty
     * string for no arguments
     * @param explanation explanation for the usuage information
     */
    public Option(String option, String argument, String explanation) 
        {
            this.option = option;
            this.argument = argument;
            this.explanation = explanation;
        }


    /**
     * 
     * Called by the option parsing machinery in {@link
     * Parse_commandline} when this option has been recognized on the
     * command line. When called the command line object points to the
     * next unprocessed element of the command line. If this option
     * has arguments, it must retrieve them from {@code cl} and
     * advance the index therein such that it points to the next
     * unprocessed options when this method exits.
     * <P>
     *
     * If the option is illegal in the current state or if its
     * argument are missing or are of the wrong format, the method
     * should print an appropriate error message and exit the program. 
     * 
     * @param cl command line instance; its index points to the next
     * unprocessed command line element
     * @throws Option_error 
     */
    public abstract void matched(Commandline cl);
}
