package io.github.gmpl.main

import static io.github.gmpl.main.GDefaultFunctions.*

// https://people.physik.hu-berlin.de/~palloks/js/enigma/enigma-u_v20_en.html

/*
NS.Extend(NS, {
        alpha: "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        mRotors: {
                I: {wires: "EKMFLGDQVZNTOWYHXUSPAIBRCJ", notch: 'Q'},
                II: {wires: "AJDKSIRUXBLHWTMCQGZNPYFVOE", notch: 'E'},
                III: {wires: "BDFHJLCPRTXVZNYEIWGAKMUSQO", notch: 'V'},
                IV: {wires: "ESOVPZJAYQUIRHXLNFTGKDCMWB", notch: 'J'},
                V: {wires: "VZBRGITYUPSDNHLXAWMJQOFECK", notch: 'Z'}
                },

        mReflectors: {
        B: {wires: "YRUHQSLDPXNGOKMIEBFZCWVJAT"},
        C: {wires: "FVPJIAOYEDRZXWGCTKUQSBNMHL"}
        },
 */

def alpha = 'abcdefghijklmnopqrstuvwxyz'

def rotors = [
        I:      [wires: 'ekmflgdqvzntowyhxuspaibrcj', notch: 'q'],
        II:     [wires: 'ajdksiruxblhwtmcqgznpyfvoe', notch: 'e'],
        III:    [wires: 'bdfhjlcprtxvznyeiwgakmusqo', notch: 'v']
]

def reflectors = [
        B:      [wires: 'yruhqsldpxngokmiebfzcwvjat'],
        C:      [wires: 'fvpjiaoyedrzxwgctkuqsbnmhl']
]

def str = 'wetterbericht'
def T = str.length()

def r = var name: 'r', type: boolean, domain: [rotors.keySet(),('a'..'z'),(0..T)] // rotor position as time
def rp = var name: 'rp', type: boolean, domain: [rotors.keySet(),('a'..'z')] // ring position
