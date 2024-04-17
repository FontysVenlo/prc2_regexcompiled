/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package io.github.fontysvenlo.regexcompiled;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@Fork( 1 )
@Warmup( iterations = 6, time = 200, timeUnit = TimeUnit.MILLISECONDS )
@Measurement( iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS )
public class Regexcompiled {

    public static void main(String[] args) {
        System.out.println( "Hello World!" );
    }

    // the idea is the regex is defined in a string ".+@(\\w+\\.)+(?<topdomain>\\w+)"
    // you make a pattern out of it as in line 26
    // you can feed the actual text to that pattern
    //String regExString = ".+@.+";
    //String regExString = ".+@[0-9a-zA-Z]+\\.[0-9a-zA-Z]+";   // Make sure . at right side
    //String regExString = ".+@\\w+\\.\\w+";     // shorter -> word characters
    //String regExString = ".+@(\\w+\\.)+\\w+";    // enable more sub domains
//    static final String regExString = ".+@(\\w+\\.)+(\\w+)";    // group for top level domain
//    static final String regExString = "(?<username>.+)" // local part aka user name
//                                      + "@" // mandator separator
//                                      + "(?<fqdn>(\\w+\\.)+(?<topdomain>\\w+))";    // named group for top level domain
    static final String regExString = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                      + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    static final Pattern regExPattern = Pattern.compile( regExString );
    static final Matcher matcherFromPreCompiledPattern = regExPattern
            .matcher( "" );
    static final List<String> testAddresses = List.of(
            "p.vandenhombergh@fontys,nl",
            "r.vandenham@fontys.nl",
            "i.kouzak@fontys.nl",
            "m.bonajo@fontys.nl"
    );

    /**
     * Check if given email address is valid. Should contain @ sign, at least
     * one character on the left-hand-side of it, at least one dot at the
     * right-hand-side (but the dot shouldn't be at begin or end of the
     * right-hand-side).
     *
     * @param emailAddress
     * @return
     */
    public boolean isValidUsingString(String emailAddress) {
        return emailAddress.matches( regExString );

    }

    /**
     * Check if given email address is valid. Should contain @ sign, at least
     * one character on the left-hand-side of it, at least one dot at the
     * right-hand-side (but the dot shouldn't be at begin or end of the
     * right-hand-side).
     *
     * @param emailAddress
     * @return
     */
    public boolean isValidCompiledPattern(String emailAddress) {
        return regExPattern.matcher( emailAddress )
                .matches();

    }

    @Benchmark
    @OutputTimeUnit( TimeUnit.NANOSECONDS )
    @BenchmarkMode( Mode.AverageTime )
    public void b1oneUsingString(Blackhole blackhole) {
        for ( String testAddress : testAddresses ) {

            blackhole.consume( isValidUsingString( testAddress ) );
        }
    }

    @Benchmark
    @OutputTimeUnit( TimeUnit.NANOSECONDS )
    @BenchmarkMode( Mode.AverageTime )
    public void b2CompiledPattern(Blackhole blackhole) {
        for ( String testAddress : testAddresses ) {
            blackhole.consume( isValidCompiledPattern( testAddress ) );
        }
    }

    @Benchmark
    @OutputTimeUnit( TimeUnit.NANOSECONDS )
    @BenchmarkMode( Mode.AverageTime )
    public void b3ReuseMatcher(Blackhole blackhole) {
        for ( String testAddress : testAddresses ) {
            blackhole.consume( matcherFromPreCompiledPattern
                    .reset( testAddress )
                    .matches() );
        }
    }
}
