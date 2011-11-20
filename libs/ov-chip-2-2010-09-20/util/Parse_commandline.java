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
// parse command lines and print usage
// 
// $Id: Parse_commandline.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


/**
 * Command line parsing. Invoke the options found on the command line
 * and print a usage information and exit the program if one of the
 * options {@code -h, -help} or {@code --help} was found or if one of
 * the options was used incorrectly.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Parse_commandline {

    /**
     * 
     * The options array.
     */
    private final Option[] options;


    /**
     * 
     * Short application name for the usage.
     */
    private final String application_name;

    /**
     * 
     * Construct an instance for parsing the command line.
     * 
     * @param options the options to parse
     * @param application_name for the usage message
     */
    public Parse_commandline(Option[] options, 
                             String application_name) 
    {
        this.options = options;
        this.application_name = application_name;
    }


    /**
     * 
     * Return the option and argument string. This is a small helper
     * method for {@link #format_option_description}.
     * 
     * @param o option
     * @return the option string concatenated with the argument, as
     * needed for {@link #format_option_description}
     */
    private String left_side(Option o) {
        if(o.argument == null)
            return o.option;
        else
            return o.option + " " + o.argument;
    }


    /**
     * 
     * Format option descriptions for the usage message to System.out.
     * 
     */
    public void format_option_description() {
        int longest_left = 2;   // provide space for -h
        for(int i = 0; i < options.length; i++) {
            String left = left_side(options[i]);
            if(left.length() > longest_left)
                longest_left = left.length();
        }
        for(int i = 0; i < options.length; i++) {
            System.out.print("  ");
            String left = left_side(options[i]);
            System.out.print(left);
            for(int j = left.length(); j < longest_left; j++)
                System.out.print(' ');
            System.out.print("  ");
            System.out.println(options[i].explanation);
        }
        // print -h
        System.out.print("  -h");
        for(int j = 2; j < longest_left; j++)
            System.out.print(' ');
        System.out.println("  print usage information");
    }


    /**
     * 
     * Print the usage message to System.out. Prints one line of
     * explanation for each option, similar to
     * <PRE>
     *    -h      print help
     *    -i n    int argument n
     * </PRE>
     * The explanations on the right hand side are nicely lined up. 
     */
    private void usage() {
        System.out.println(application_name + ": Recognized options are");
        format_option_description();
        return;
    }


    /**
     * 
     * Parse a complete command line. Constructs a {@link Commandline}
     * instance from the arguments {@code args}, matches against the
     * options and calls their {@link Option#matched Option.matched}
     * method as appropriate.
     * <P>
     *
     * If one of {@code -h, -help, --help} or an unknow option is
     * found or if one of the options is used inappropriately, a usage
     * information is printed and the program is exited.
     * 
     * @param args the command line arguments as received in main
     */
    public void parse(String[] args) {
        Commandline cl = new Commandline(args);

        while(cl.continue_parsing()) {
            if(cl.match("-h") ||
               cl.match("-help") ||
               cl.match("--help")) {
                usage();
                System.exit(0);
            }

            boolean found_one_option = false;
            for(int i = 0; i < options.length; i++) {
                if(cl.match(options[i].option)) {
                    options[i].matched(cl);
                    found_one_option = true;
                    break;
                }
            }

            if(!found_one_option) {
                System.err.format("%s: unrecognized option %s\n",
                                  application_name,
                                  cl.next_option());
                usage();
                System.exit(1);
            }
        }
    }
}