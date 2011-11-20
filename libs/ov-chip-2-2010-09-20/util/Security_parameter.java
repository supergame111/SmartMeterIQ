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
// Created 3.11.08 by Hendrik
// 
// compute suitable choices of security parameters
// 
// $Id: Security_parameter.java,v 1.9 2010-03-12 15:40:22 tews Exp $


package ds.ov2.util;



/** 
 * Security estimates for RSA keys and exponent length choice. Based
 * on the estimations of Lenstra in his article "Key length" in the
 * Handbook of Information Security, this class can estimate the
 * security level of an RSA key size in a given year and select a
 * suitable exponent length that achieves the same security level for
 * the descrete logarithm problem.
 * <P>
 *
 * Static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.9 $
 * @commitdate $Date: 2010-03-12 15:40:22 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Security_parameter {


    /**
     * 
     * Static class, object creation disabled.
     */
    protected Security_parameter() {}


    /**
     * 
     * Record containing the necessary data for calibrating the
     * machinery for estimating the security parameters. The needed
     * data are the (estimated) costs of factoring an RSA modulus in a
     * given year and the security level that RSA modulus provides in
     * that given year. 
     */
    public static class Calibration {

        /**
         * 
         * The length of the RSA modulus in bits.
         */
        public int rsa_modulus_length;

        /**
         * 
         * The year to which the fields {@link #cost_to_break} and
         * {@link #security_level} apply.
         */
        public int year;

        /**
         * 
         * The (estimated) cost to factor an RSA modulus of length
         * {@link #rsa_modulus_length} in the year {@link #year} in
         * dollar days. 
         */
        public double cost_to_break;

        /**
         * 
         * The security level that an RSA modulus of length {@link
         * #rsa_modulus_length} provides in the year {@link #year}.
         */
        public int security_level;

        /**
         * 
         * Initialize the record with the provided data.
         * 
         * @param rsa_modulus_length length, see {@link #rsa_modulus_length}
         * @param year year, see {@link #year}
         * @param cost_to_break factoring costs, see {@link #cost_to_break}
         * @param security_level security level, see {@link #security_level}
         */
        public Calibration(int rsa_modulus_length, int year, 
                           double cost_to_break, int security_level)
        {
            this.rsa_modulus_length = rsa_modulus_length;
            this.year = year;
            this.cost_to_break = cost_to_break;
            this.security_level = security_level;
        }
    }


    /**
     * 
     * Calibration data from Lenstra's "Key length" article. In 2004
     * it was estimated that breaking an 1024 bit RSA modulus would
     * cost 400 million dollar days. 
     * <P>
     *
     * From this data the security level is estimated as follows. In
     * 1982 40 Million dollar days correspond to security level 56
     * (DES). With Moores law the same effort corresponds to security
     * level 70.6 in 2004. Therefore an 1024 bit key had security
     * level 74 in 2004.</LI>
     */
    public static Calibration rsa_2004_estimation =
        new Calibration(1024, 2004, 400 * 1E6, 74);
        
        
    /**
     * 
     * Calibration from the RSA 768 factorization in 2009.
     * In 2009 the RSA 768 challenge was broken, see "Factorization of
     * a 768-bit RSA modulus" by Thorsten Kleinjung et. al. The
     * authors estimated that 2000 years of computing on a single AMD
     * Opteron 2.2 GHz core were needed for the factorization. In
     * early 2010 an rack with an 2-core Opteron was offered at 700
     * dollars. This would make 255 Million dollar days. Bying en-mass
     * probably gives a reduction, so I take 100 Million dollar days.
     * <P>
     *
     * The key was broken in 2009 with quite some effort, so I assume
     * that 768 bit keys were secure until 2008, ie, the key had
     * security level 73 in 2008. Because of the double Moores-law
     * effect the factorization would have cost about 200 Million
     * dollar days in 2008.
     */
    public static Calibration rsa_768_break_2009 =
        new Calibration(768, 2008, 200 * 1E6, 73);


    /**
     * 
     * Constant 1/3 with value {@value}
     */
    private static final double one_third = 1.0 / 3;


    /**
     * 
     * An RSA related constant in the estimations of Lenstra. See page
     * 24 of his "Key Length" paper. Has value {@value}.
     */
    private static final double rsa_alpha = 1.976;


    /**
     * 
     * The L function to describe the subexponential complexity of
     * number factoring. See page 21 of Lenstras "Key Length" paper.
     * In Lenstras version the first argument of the L function is the
     * key itself. Here we take the key length as first argument
     * (which is the logarithm to the base 2 of the key).
     * 
     * @param log_2_n key length
     * @param r distribution between polynomial and exponential parts,
     * r = 0 gives polynomial complexity, r = 1 exponential
     * complexity. 
     * @param alpha some factor
     * @return the value of the L function, which is proporional to
     * the effort needed to factor an RSA key of {@code log_2_n} bits.
     */
    static double L(int log_2_n, double r, double alpha) {
        double log_n = log_2_n * Math.log(2);
        return 
            Math.exp(alpha * Math.pow(log_n, r) *
                     Math.pow(Math.log(log_n), 1 - r));
    }


    /**
     * 
     * Cost in dollar days that is needed to break an RSA key of size
     * {@code modulus_length} in the year {@code year}. The estimation
     * is based on a calibraion record and the following:
     * <UL>
     * <LI>by Moores low every 18 month the hardware is twice as fast
     * and therefore 
     * the cost drops to 50% every 18 month</LI>
     * <LI>advances in number factoring half the needed effort every
     * 18 month</LI>
     * <LI>Moores low and the advances in number factoring are
     * independent, so that effectively the cost drops to 50% every 9
     * month. 
     * </UL>
     * 
     * @param year year for which the cost should be estimated
     * @param modulus_length the bit length of the RSA modulus
     * @param cal Calibration data, if null {@link
     * #rsa_2004_estimation} is used
     * @return estimated costs in dollar days
     */
    public static double rsa_cost(int year, int modulus_length, 
                                  Calibration cal) 
    {
        if(cal == null) 
            cal = rsa_2004_estimation;
        return
            L(modulus_length, one_third, rsa_alpha) * 
            cal.cost_to_break /
            L(cal.rsa_modulus_length, one_third, rsa_alpha) / 
            Math.pow(2, 4.0 / 3 * (year - cal.year));
    }


    /**
     * 
     * Security level an RSA key length provides in a given year. The
     * estimation is based on a calibraion record and the following:
     * <UL>
     * <LI>advances in number factoring half the effort needed every
     * 18 month</LI>
     * </UL>
     *
     * The security level of a given RSA key length decreases with
     * time because of the estimated advances in number crunching.
     * 
     * @param year year for which the security level should be estimated
     * @param modulus_length the bit length of the RSA modulus
     * @param cal Calibration data, if null {@link
     * #rsa_2004_estimation} is used
     * @return estimated security level
     */
    public static double rsa_level(int year, int modulus_length,
                                   Calibration cal) 
    {
        if(cal == null) 
            cal = rsa_2004_estimation;
        return
            Math.log(L(modulus_length, one_third, rsa_alpha) /
                     L(cal.rsa_modulus_length, one_third, rsa_alpha)) /
            Math.log(2) +
            (cal.security_level - 2.0 / 3 * (year - cal.year));
    }


    /**
     * 
     * Compute an exponent length for an RSA key length suitable up to
     * year {@code year}. Based on the
     * estimated security level (compare {@link #rsa_level rsa_level})
     * an exponent length is chosen that provides the same security.
     * For the exponent length we assume that an exponent of length 2n
     * privides n bits of security against solving the discrete
     * logarithm problem. 
     * <P>
     *
     * For a fixed RSA key length the returned exponent length
     * decreases with time because of the assumed advances
     * in number crunching. 
     * <P>
     *
     * The estimations of Lenstra on which the computations are bases
     * are certainly only valid for a relatively narrow range of the
     * arguments. For key length below 512 bits the results are
     * probably only good for amusement. Nevertheless this is the main
     * method to choose exponent length' for key sizes from 32-2048
     * bits. Simply because there is no sensible way to choose
     * an exponent length. 
     * <P>
     *
     * For very small RSA key sizes the estimated security level
     * becomes negative (not sure what that means). As minimal
     * exponent length 2 is returned in such cases.
     * 
     * @param year year for which the cost should be estimated
     * @param modulus_length the bit length of the RSA modulus
     * @param cal Calibration data, if null {@link
     * #rsa_2004_estimation} is used
     * @return exponent length providing the same security
     */
    public static int exponent_length_for_modulus_length(int year, 
                                                         int modulus_length,
                                                         Calibration cal)
    {
        if(cal == null) 
            cal = rsa_2004_estimation;
        // The level of the discrete log problem in a group G is 
        // ln2(sqrt(#G)) == ln2(#G) / 2, where #G is the order of G.
        double rsa_level = rsa_level(year, modulus_length, cal);
        int exp_len = (int)(2 * rsa_level + 0.5);
        // depending on the year, exp_len becomes negative for 
        // short modulus_length. 
        return exp_len >= 2 ? exp_len : 2;
    }


    /**
     * 
     * Same as {@link #exponent_length_for_modulus_length
     * exponent_length_for_modulus_length} but round the exponent
     * length up to the next multiple of 8. 
     * 
     * @param year year for which the cost should be estimated
     * @param modulus_length the bit length of the RSA modulus
     * @param cal Calibration data, if null {@link
     * #rsa_2004_estimation} is used
     * @return exponent length providing the same security rounded up
     * to the next byte boundary
     */
    public static int exponent_length_for_modulus_length_full_byte
        (int year, int modulus_length, Calibration cal) 
    {
        if(cal == null) 
            cal = rsa_2004_estimation;
        int res = exponent_length_for_modulus_length(year, modulus_length, cal);

        if(res % 8 == 0)
            return res;
        else
            return res + 8 - res % 8;
    }
}
