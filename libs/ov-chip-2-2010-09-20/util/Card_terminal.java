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
// Created 10.8.08 by Hendrik
// 
// utility functions to connect to a an applet on a card/emulator
// 
// $Id: Card_terminal.java,v 1.24 2009-06-17 20:14:05 tews Exp $

package ds.ov2.util;

import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.Security;
import java.security.Provider;
import javax.smartcardio.TerminalFactory;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.CardException;


/** 
 * Various utility methods for makeing connections to cards (or emulators) 
 * and applet selection. This is a static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.24 $
 * @commitdate $Date: 2009-06-17 20:14:05 $ by $Author: tews $
 * @environment host
 * @todo add sun_provider_present
 * @CPP no cpp preprocessing needed
 */
public class Card_terminal {

  /**
   * Object creation is disabled, because all fields are static.
   * 
   */
  protected Card_terminal() {}


  /**
   * Enumeration type for card terminals and emulators.
   */
  public static enum Terminal_type {
    /**
     * A real card terminal, connected via PC/SC drivers.
     */
    PCSC_TERMINAL,
    /**
     * One of the emulators from SUN, either cref or jcwde.
     */
    SUN_EMULATOR,
    /**
     * The emulator from the jcop tools.
     */
    JCOP_EMULATOR
  }    


  /**
   * Return a list of all PC/SC terminals connected to this machine.
   *
   * @return the list of terminals
   * @throws CardException for communication errors
   * @throws NoSuchAlgorithmException if the provider for the PC/SC 
   *           terminal factory is absent (which should rarely occur, 
   *           because this provider is in the JDK)
   */
  public static List<CardTerminal> get_all_pcsc_terminals() 
    throws NoSuchAlgorithmException, CardException
  {
    //TerminalFactory factory = TerminalFactory.getDefault();
    TerminalFactory factory = TerminalFactory.getInstance("PC/SC", null);
    // System.out.println("Factory type " + factory.getType());
    // System.out.println("pcsc terminals list");
    List<CardTerminal> res = factory.terminals().list();
    // System.out.println("pcsc terminals list finished");
    return res;
  }


  /**
   * Returns a terminal list for connecting to one of the SUN
   * emulators (cref or jcwde). 
   * Loads the {@link Provider Provider} for the SunEmulator
   * {@link TerminalFactory TerminalFactory} that gives access to
   * one of the SUN emulators via the {@link javax.smartcardio} package.
   * This works only if emulatorio.jar 
   * and apduio.jar are accessible at runtime. 
   *
   * @param o argument for {@link TerminalFactory#getInstance
   * TerminalFactory.getInstance}
   * @return terminal list for connecting to a SUN emulator
   * @throws CardException if one of the necessary libraries is missing
   *     or if communication error with the emulator occurs
   * @throws NoSuchAlgorithmException is never thrown when the right
   *     emulatorio.jar is provided.
   */
  public static List<CardTerminal> get_all_sun_emulators(Object o) 
    throws NoSuchAlgorithmException, CardException
  {
    Class<?> sun_provider;

    try {
      sun_provider =
        Class.forName("ds.javacard.emulator.smartcardio.DS_provider");
      Security.addProvider((Provider)(sun_provider.newInstance()));
    }
    catch(ClassNotFoundException e) {
      throw new CardException("emulatorio.jar missing", e);
    }
    catch(ProviderException e) {
      throw new CardException("apduio.jar missing", e);
    }
    catch(InstantiationException e) {
      assert false;
      throw new CardException
        ("sun emulator communication library incompatibility", e);
    }
    catch(IllegalAccessException e) {
      assert false;
      throw new CardException
        ("sun emulator communication library incompatibility", e);
    }
    

    TerminalFactory factory = 
      TerminalFactory.getInstance("SunEmulator", o);

    // System.out.println("Factory type " + factory.getType());
    return factory.terminals().list();
  }


  /**
   * Loads the {@link Provider Provider} for the JcopEmulator
   * {@link TerminalFactory TerminalFactory} that gives access to
   * a jcop emulator via the {@link javax.smartcardio} package.
   * This will only succeed if both jcopio.jar and offcard.jar
   * are in the class path.
   *
   * @throws CardException if adding the provider fails because 
   *           one of the required jar files is missing.
   */
  public static void add_jcop_provider() 
    throws CardException
  {
    Class<?> jcop_provider;

    try {
      jcop_provider = 
        Class.forName("ds.javacard.emulator.jcop.DS_provider");
      Security.addProvider((Provider)(jcop_provider.newInstance()));
    }
    catch(ClassNotFoundException e) {
      throw new CardException("jcopio.jar missing", e);
    }
    catch(ProviderException e) {
      throw new CardException("offcard.jar missing", e);
    }
    catch(InstantiationException e) {
      assert false;
      throw new CardException("jcop communication library incompatibility", e);
    }
    catch(IllegalAccessException e) {
      assert false;
      throw new CardException("jcop communication library incompatibility", e);
    }
    return;
  }


  /**
   * Tests the presence of the libraries for connecting to a jcop 
   * emulator.
   * Tries to load the {@link Provider Provider} for the JcopEmulator
   * {@link TerminalFactory TerminalFactory} that gives access to
   * a jcop emulator via the {@link javax.smartcardio} package.
   * This will only succeed if both jcopio.jar and offcard.jar
   * are in the class path. Returns true precisely if the provider is 
   * present and access to a jcop emulator is possible.
   *
   * @return true if a jcop emulator can be accessed, false if some
   *           of the necessary libraries are missing
   */
  public static boolean jcop_provider_present() {
    try {
      add_jcop_provider();
      return true;
    }
    catch(Exception e) {
      return false;
    }
  }


  /**
   * Returns a terminal list for connecting to the jcop emulator.
   * For that the appropriate provider is loaded via {@link 
   * #add_jcop_provider}.
   * 
   * @param o argument for {@link TerminalFactory#getInstance
   * TerminalFactory.getInstance}
   * @return a terminal list for accessing a jcop emulator
   * @throws CardException if one of the necessary libraries is missing 
   *    (see throws clause at {@link #add_jcop_provider})
   *    or a communication problem is detected
   * @throws NoSuchAlgorithmException never thrown if the right libraries 
   *    are used
   */
  public static List<CardTerminal> get_all_jcop_emulators(Object o) 
    throws NoSuchAlgorithmException, CardException
  {
    add_jcop_provider();
    TerminalFactory factory = 
      TerminalFactory.getInstance("JcopEmulator", o);

    // System.out.println("Factory type " + factory.getType());
    // System.out.println("jcop terminals list");
    List<CardTerminal> res = factory.terminals().list();
    // System.out.println("jcop terminals list finished");
    return res;
  }


  /**
   * Print a list of all connected PC/SC card terminals.
   *
   * @param out the channel to print to
   */
  public static void print_readers(PrintWriter out) {
    try {
      // Do not require the getInstance parameter here. The PCSC
      // provider does not interpret it anyway.
      List<CardTerminal> terminals = get_all_pcsc_terminals();
      out.format("Found %d terminals:\n", terminals.size());
      for(ListIterator<CardTerminal> i = terminals.listIterator(); 
          i.hasNext(); ) 
        {
          out.format("  %2d: %s\n", i.nextIndex(), i.next().toString());
        }
    }
    catch(NoSuchAlgorithmException e) {
      System.err.println("Cannot connect to PC/SC subsystem");
      System.exit(1);
    }
    catch(CardException e) {
      System.err.format("Communication error %s\n", e);
      System.exit(1);
    }
  }


  /**
   * 
   * Exception indicating that applet selection failed.
   */
  public static class Applet_selection_exception extends CardException {

    /** Field to disable the serialVersionUID warning. */
    public static final long serialVersionUID = 1L;

    /** The response status of the failing selection command. */
    public final short status;

    /**
     * 
     * Constructs a new Applet_selection_exception with the specified
     * message and status.
     * 
     * @param message the detailed message
     * @param status the failing response status
     */
    public Applet_selection_exception(String message, short status) {
      super(message);
      this.status = status;
    }
  }


  /**
   * Applet selection. Select the specified applet on the card cannel.
   * Print the select APDU and other diagnostics to {@code out} if 
   * {@code out} is non null.
   *
   * @param channel the card channel to issue the select command
   * @param applet_id the applet identifier
   * @param out if non-null, channel for diagnostics
   * @param apduscript if true print apduscript lines to {@code out}
   * @throws AppletSelectionException if applet selection fails
   * @throws CardException for communication errors
   */
  public static void open_applet_ex(CardChannel channel, byte[] applet_id,
                                    PrintWriter out, boolean apduscript) 
    throws Applet_selection_exception, CardException
  {
    CommandAPDU apdu = new CommandAPDU(0x00,           // CLA
                                       0xA4,           // INS
                                       0x04,           // P1
                                       0x00,           // P2
                                       applet_id);     // data
    if(out != null)
      Host_protocol.print_apdu_full(out, apdu, "Select applet", 
                                    apduscript, "Select applet");
  
    Response_apdu res = new Response_apdu(channel.transmit(apdu));

    if(res.error()) {
      if(out != null) {
        out.println("Applet selection failed.");
        res.print(out, true);
      }
      throw new Applet_selection_exception("Applet selection failed", 
                                           res.get_status());
    }
  }


  /**
   * Wrapper for {@link #open_applet_ex open_applet_ex}. 
   * Calls {@link #open_applet_ex open_applet_ex} and prints any 
   * errors to {@code out} or {@link System#err}. If an error 
   * occurs the program is terminated.
   *
   * @param channel the card channel to issue the select command
   * @param applet_id the applet identifier
   * @param out if non-null, channel for diagnostics
   * @param apduscript if true print apduscript lines to {@code out}
   */
  public static void open_applet(CardChannel channel, byte[] applet_id,
                                 PrintWriter out, boolean apduscript) {
    try {
      open_applet_ex(channel, applet_id, out, apduscript);
    }
    catch(CardException e) {
      PrintWriter oout = out == null ? new PrintWriter(System.err, true) : out;
      oout.format("Card communication error during applet selection %s\n", e);
      System.exit(1);
    }
  }


  /**
   * Connect to a card (or emulator) and return the basic channel.
   * In the list of all available terminals of type {@code terminal_type}
   * the one with index {@code terminal_number} is selected, a 
   * connection to the card there is made and the basic channel returned.
   *
   * @param terminal_number terminal index
   * @param terminal_type determines whether to connect to a real terminal 
   *          or to some emulator
   * @param o argument for {@link TerminalFactory#getInstance
   * TerminalFactory.getInstance}
   * @param out if non-null print diagnostics and progress messages there
   * @return the basic channel to the selected card or emulator
   * @throws CardException if a library for connecting to one of the 
   *     emulators is missing or another communication problem occurs 
   *     (see also {@link #add_jcop_provider} and {@link 
   *     #get_all_sun_emulators} which are used internally)
   * @throws NoSuchAlgorithmException is never thrown if the proper 
   *     libraries are used.
   */
  public static CardChannel open_card_channel_ex
    (int terminal_number, Terminal_type terminal_type, Object o,
     PrintWriter out) 
    throws CardException, NoSuchAlgorithmException
  {
    List<CardTerminal> terminals;
    switch(terminal_type) {
    case PCSC_TERMINAL:
      if(out != null)
        out.print("Connecting to card terminals ...");
      terminals = get_all_pcsc_terminals();
      break;

    case SUN_EMULATOR:
      if(out != null)
        out.print("Connecting to a SUN emulator ...");
      terminals = get_all_sun_emulators(o);
      break;

    case JCOP_EMULATOR:
      if(out != null)
        out.print("Connecting to the jcop emulator ...");
      terminals = get_all_jcop_emulators(o);
      break;

    default:
      // There are only three constants in Terminal_type, nevertheless
      // java demands that we assign here something to terminals.
      terminals = null;
      assert false;
      System.exit(1);
    }

    if(terminals.size() == 0) {
      if(out != null)
        out.println("");
      throw new CardException("Found no card terminals");
    }
    if(terminal_number > terminals.size()) {
      if(out != null)
        out.println("");
      throw new CardException(String.format("Invalid terminal number %d", 
                                            terminal_number));
    }

    Card card = terminals.get(terminal_number).connect("*");
    if(out != null)
      out.println(" connected");

    // gives exclusive exess to the card from this thread
    // card.beginExclusive();

    return card.getBasicChannel();
  }


  /**
   * Wrapper for {@link #open_card_channel_ex open_card_channel_ex}.
   * Calls {@link #open_card_channel_ex open_card_channel_ex} and
   * prints any error to {@code out} or {@link System#err}. The 
   * program is terminated if an error occurs.
   *
   * @param terminal_number terminal index
   * @param terminal_type determines whether to connect to a real terminal 
   *          or to some emulator
   * @param o argument for {@link TerminalFactory#getInstance
   * TerminalFactory.getInstance}
   * @param out if non-null print diagnostics and progress messages there,
   *         if null, errors are printed to {@link System#err}
   * @return the basic channel to the selected card or emulator
   */
  public static CardChannel open_card_channel(int terminal_number, 
                                              Terminal_type terminal_type, 
                                              Object o,
                                              PrintWriter out) 
  {
    try {
      return open_card_channel_ex(terminal_number, terminal_type, o, out);
    }
    catch(NoSuchAlgorithmException e) {
      PrintWriter oout = out == null ? new PrintWriter(System.err, true) : out;
      oout.format("Driver error during connect %s\n", e);
      System.exit(1);
      return null;
    }
    catch(CardException e) {
      PrintWriter oout = out == null ? new PrintWriter(System.err, true) : out;
      oout.format("Card communication error during connect %s\n", e);
      System.exit(1);
      return null;
    }
  }


  /**
   * Close a card channel and disconnect the connection to the card.
   *
   * @param out channel to write error diagnostics, if the operation fails.
   *          If null {@link System#err} is used.
   * @param card_channel the channel to close
   */
  public static void close_connection(PrintWriter out, 
                                      CardChannel card_channel) 
  {
    try {
      Card card = card_channel.getCard();
      // basic channel cannot be closed
      // card_channel.close();
      // card.endExclusive();
      card.disconnect(false);
    }
    catch(CardException e) {
      PrintWriter oout = out == null ? new PrintWriter(System.err, true) : out;
      oout.format("Card communication error during disconnect %s\n", e);
      System.exit(1);
    }
  }
}


/// Local Variables:
/// c-basic-offset: 2
/// End:
