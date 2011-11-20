//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../bignat/Modulus.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../bignat/Modulus.java"
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
// Created 17.9.08 by Hendrik
// 
// additional information needed for moduli
// 
// $Id: Modulus.java,v 1.21 2010-02-12 10:59:31 tews Exp $

//# 1 "./config" 1
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
// Created 26.7.08 by Hendrik
// 
// preprocessor config directives
// 
// $Id: config,v 1.17 2010-02-16 10:26:10 tews Exp $
//# 91 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../bignat/Modulus.java" 2

//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../bignat/bignatconfig" 1
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
// Created 23.10.08 by Hendrik
// 
// bignat configuration
// 
// $Id: bignatconfig,v 1.3 2010-02-12 20:19:36 tews Exp $
//# 34 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../bignat/bignatconfig"
  // Let bignat operate on bytes, using short for multiplication results.
//# 45 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../bignat/bignatconfig"
/// Local Variables:
/// mode: c
/// End:
//# 28 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../bignat/Modulus.java" 2




  package ds.ov2.bignat;




  import ds.ov2.util.Misc;
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Serializable_array;


    import ds.ov2.util.APDU_short;






/** 
 * Division modulus for Java Card. Contains the modulus itself and the
 * negated inverse of its last digit. The latter is needed inside <a
 * href="package-summary.html#montgomery_factor">Montgomery
 * multiplication</a>. 
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 * <P>
 *
 * This is a card data type. It is compatible with
 * <UL>
 * <LI>Moduli of the same size</LI>
 * <LI>{@link Host_modulus Host_moduli}, configured to the same
 * size</LI>
 * </UL>
 *
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#VARIABLE_SIZE_BIGNATS">VARIABLE_SIZE_BIGNATS</a>,
 *   <a href="../../../overview-summary.html#DOUBLE_DIGIT_TYPE">DOUBLE_DIGIT_TYPE</a>,
 *   <a href="../../../overview-summary.html#APDU_DOUBLE_DIGIT_TYPE">APDU_DOUBLE_DIGIT_TYPE</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#BIGNAT_USE_BYTE ">BIGNAT_USE_BYTE </a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.21 $
 * @commitdate $Date: 2010-02-12 10:59:31 $ by $Author: tews $
 * @environment host, card
 */
public class Modulus
    extends Serializable_array
    implements APDU_Serializable
{

    /**
     * 
     * The modulus itself.
     */
    public Bignat m;


    /**
     * 
     * Modulus times 2. Allocated and initialized explicitely in
     * {@link #allocate_multiples allocate_multiples} only when
     * needed for {@link Bignat#squared_rsa_mult_4 squared_rsa_mult_4}.
     */
    public Bignat mod_x_2;


    /**
     * 
     * Modulus times 3. Allocated and initialized explicitely in
     * {@link #allocate_multiples allocate_multiples} only when
     * needed for {@link Bignat#squared_rsa_mult_4 squared_rsa_mult_4}.
     */
    public Bignat mod_x_3;


    /**
     * 
     * Negated modular inverse of the last digit. Has type
     * DOUBLE_DIGIT_TYPE. This is needed inside <a
     * href="package-summary.html#montgomery_factor">Montgomery
     * multiplication</a>.
     * <P>
     *
     * Conveniently computed by the {@link
     * Host_modulus#Host_modulus(int, BigInteger) initializing
     * Host_modulus constructor}, see {@link
     * Host_modulus#last_digit_inverse}.
     * <P>
     *
     * Must fulfill the following property: {@link #m} *
     * last_digit_inverse + 1 == 0 (modulo {@link
     * Bignat#bignat_base}), where bignat_base is 256 for the
     * byte/short configuration and 2^32 for the int/long
     * configuration. 
     * <P>
     *
     * To compute one takes the inverse of the last digit of {@link
     * #m} with respect to {@link Bignat#bignat_base} and multiplies
     * it with -1, i.e., {@link Bignat#bignat_base} -1, again modulo
     * {@link Bignat#bignat_base}. Because {@link Bignat#bignat_base}
     * is a power of 2, the modular inverse exists only for odd
     * moduli. 
     * 
     */
    public short last_digit_inverse;


    /**
     * 
     * APDU wrapper for {@link #last_digit_inverse}. Used for
     *(de-)serialization of {@link #last_digit_inverse}.
     */
    private APDU_short last_digit_inverse_box;


    /**
     * 
     * Length of the underlying serializable array. It is currently
     * {@value}. 
     */
    /* package local */ static final short serializable_contents_length = 2;


    /**
     * 
     * Serializable array for the {@link Serializable_array} base
     * class. Will contain {@link #serializable_contents_length}
     * length elements, currently the modulus {@link #m} and {@link
     * #last_digit_inverse_box} in this order. Must be compatible with
     * {@link Host_modulus#serializable_array}.
     */
    private APDU_Serializable[] serializable_contents;


    /**
     * 
     * Allocating constructor. Allocates a {@link Bignat} of size
     * {@code bignat_size} for the modulus and allocates and fills the
     * remaining fields, in particular {@link #serializable_contents}.
     * The argument {@code in_ram} is passed through to {@link
     * Bignat#Bignat(short, boolean) the Bignat constructor}, that is,
     * if {@code in_ram} is true then the modulus is allocated in
     * transient RAM.
     * 
     * @param bignat_size size of the modulus
     * @param in_ram allocate modulus in transient ram if true
     */
    public Modulus(short bignat_size, boolean in_ram) {
        m = new Bignat(bignat_size, in_ram);
        last_digit_inverse = 0;
        last_digit_inverse_box = new APDU_short();
        serializable_contents =
            new APDU_Serializable[serializable_contents_length];
        serializable_contents[0] = m;
        serializable_contents[1] = last_digit_inverse_box;
    }


    // Convenience method to register all contained Bignats.


       /**
        * 
        * Register the modulus and the multiples (if allocated) in
        * {@link Resize} for resizing. Only available if
        * VARIABLE_SIZE_BIGNATS is defined. The internal {@link
        * Bignat}, containing the modulus, is registered as a long
        * Bignat.
        * 
        */
       public void register_long_bignats() {
           Resize.register_long_bignat(m);
           if(mod_x_2 != null) {
               assert(mod_x_3 != null);
               Resize.register_long_bignat(mod_x_2);
               Resize.register_long_bignat(mod_x_3);
           }
       }



    /**
     * 
     * Allocate the space for the multiples {@link #mod_x_2} and
     * {@link #mod_x_3} of the modulus {@link #m}.
     * They are needed only inside {@link Bignat#squared_rsa_mult_4
     * squared_rsa_mult_4}, so only call this method if {@link
     * Bignat#squared_rsa_mult_4 squared_rsa_mult_4} is used with this
     * modulus. 
     * <P>
     *
     * If <a
     * href="../../../overview-summary.html#VARIABLE_SIZE_BIGNATS">VARIABLE_SIZE_BIGNATS</a>
     * is defined this method must be called before {@link
     * #register_long_bignats} (and thus before the first resize)
     * otherwise the multiples will not be resized.
     * <P>
     *
     * Once allocated the multiples are automatically initialized when
     * the modulus is initialized inside {@link #from_byte_array
     * from_byte_array}. This requires of course that the first two
     * bits of the modulus are empty, because otherwise an overflow
     * will occur inside {@link Bignat#add add}.
     *
     * Asserts that that {@link #mod_x_2} and {@link #mod_x_3} are
     * {@code null}, ie. that this method has not been called before.
     * 
     */
    public void allocate_multiples() {
        assert(mod_x_2 == null && mod_x_3 == null);
        mod_x_2 = new Bignat(m.length(), false);
        mod_x_3 = new Bignat(m.length(), false);
    }


    /**
     * 
     * (Re-)Initialize the multiples {@link #mod_x_2} and {@link
     * #mod_x_3} if they have been allocated before with {@link
     * #allocate_multiples}. Automatically called from {@link
     * #from_byte_array from_byte_array}.
     * <P>
     *
     * Requires that the first two bits of the just received modulus
     * are zero, because otherwise {@link Bignat#add add} will
     * overflow.
     * 
     */
    private void initialize_multiples() {
        if(mod_x_2 != null) {
            assert(mod_x_3 != null);
            mod_x_2.copy(m);
            mod_x_2.add(m);
            mod_x_3.copy(mod_x_2);
            mod_x_3.add(m);
        }
    }


    //########################################################################
    // Serializable_array support
    // 

    /**
     * Return {@link #serializable_contents} in support for abstract
     * {@link Serializable_array}.
     *
     * @return array of objects to (de-)serialize
     */
    protected APDU_Serializable[] get_array() {
        return serializable_contents;
    }


    /**
     * Return {@link #serializable_contents_length} 
     * as effective size in support for abstract
     * {@link Serializable_array}.
     *
     * @return {@link #serializable_contents_length}
     */
    public short get_length() {
        return serializable_contents_length;
    }


    /**
     * Compatibility check for the OV-chip protocol layer.
     * See <a href="../util/APDU_Serializable.html#apdu_compatibility">
     * the compatibility check 
     * explanations</a> and also
     * {@link ds.ov2.util.APDU_Serializable#is_compatible_with 
     * APDU_Serializable.is_compatible_with}.
     * <P>
     *
     * An object of this class is compatible with instances of Modulus
     * or {@link Host_modulus} if the internal modulus has the same
     * size. 
     *
     * @param o actual argument or result
     * @return true if this (the declared argument or result) is considered 
     *         binary compatible with {@code o}.
     */
    public boolean is_compatible_with(Object o) {
        if(o instanceof Modulus) {
            return this.m.size() == ((Modulus) o).m.size();
        }

            else if(o instanceof Host_modulus) {
                return this.m.size() == ((Host_modulus)o).bignat_size;
            }

        return false;
    }


    /**
     * 
     * Serialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#to_byte_array 
     * APDU_Serializable.to_byte_array}. Overridden here to update
     * {@link #last_digit_inverse_box} before serialization.
     *
     * @param len available space in {@code byte_array}
     * @param this_index number of bytes that
     * have already been written in preceeding calls
     * @param byte_array data array to serialize the state into
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually written, except for the case 
     * where serialization finished by writing precisely 
     * {@code len} bytes, in this case {@code len + 1} is 
     * returned.
     */
    public short to_byte_array(short len, short this_index,
                               byte[] byte_array, short byte_index) {
        last_digit_inverse_box.value = last_digit_inverse;
        return super.to_byte_array(len, this_index, byte_array, byte_index);
    }


    /**
     * Deserialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#from_byte_array 
     * APDU_Serializable.from_byte_array}. Overridden here to update
     * {@link #last_digit_inverse} after deserialization.
     *
     * @param len available data in {@code byte_array}
     * @param this_index number of bytes that
     * have already been read in preceeding calls
     * @param byte_array data array to deserialize from
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually read, except for the case 
     * where deserialization finished by reading precisely 
     * {@code len} bytes, in this case {@code len + 1} is 
     * returned.
     */
    public short from_byte_array(short len, short this_index,
                                 byte[] byte_array, short byte_index) {
        short res = super.from_byte_array(len, this_index,
                                          byte_array, byte_index);
        last_digit_inverse = last_digit_inverse_box.value;
        initialize_multiples();
        return res;
    }

}
