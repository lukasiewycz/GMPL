package io.github.gmpl.main

import static io.github.gmpl.main.GDefaultFunctions.var

/**
 * Created by martin.lukasiewycz on 3/8/2015.
 */

def alpha = 'abcdefghijklmnopqrstuvwxyz'

def rotors = [
        I:      [wires: 'ekmflgdqvzntowyhxuspaibrcj', notch: 'q'],
        II:     [wires: 'ajdksiruxblhwtmcqgznpyfvoe', notch: 'e'],
        III:    [wires: 'bdfhjlcprtxvznyeiwgakmusqo', notch: 'v']
]
def rotortypes = rotors.keySet()
def rotorslots = (1..3)
def rotorsignals = (0..3)

def reflectors = [
        B:      [wires: 'yruhqsldpxngokmiebfzcwvjat'],
        C:      [wires: 'fvpjiaoyedrzxwgctkuqsbnmhl']
]

def String next(String a){
    int base = ((int) ('a'))
    return ''+(char)(((((int)a.charAt(0))-base+1)%26)+base)
}

def String prev(String a){
    int base = ((int) ('a'))
    return ''+(char)(((((int)a.charAt(0))-base+(26-1))%26)+base)
}

def String add(String a, String b){
    int aint = ((int)a)-((int)'a')
    int bint = ((int)b)-((int)'a')
    return (char)(((aint+bint)%26)+(int)'a')
}

def String add(String a, String... b){
    String result = a;
    for(e in b){
        result = add(result,e)
    }
    return result
}

def String subtract(String a, String b){
    int aint = ((int)a)-((int)'a')
    int bint = ((int)b)-((int)'a')
    return (char)(((aint-bint+26)%26)+(int)'a')
}

def int getOffset(String wires, String i){
    int input = ((int)i)-((int)'a')
    int output = ((int)wires.charAt(input))-((int)'a')
    return (output - input + 26) % 26
}

def String getOutput(String wires, String i){
    int input = ((int)i)-((int)'a')
    return ((char)wires.charAt(input)) as String
}

def p = 's'
def input = 'w'

//println add(input,p,'a')

println 'input on grid '+input
println 'input on rotor '+add(input,p)
println 'ouput on rotor '+getOutput(rotors['I'].wires, add(input,p))
println 'ouput on grid '+subtract(getOutput(rotors['I'].wires, add(input,p)),p)

println 'input on rotor '+add(input,p)
println 'output on rotor (with shift) '+add(getOutput(rotors['I'].wires, subtract(add(input,p),'d')),'d')


