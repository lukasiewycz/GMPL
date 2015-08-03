package io.github.gmpl.main

/**
 * Created by martin.lukasiewycz on 3/8/2015.
 */

def alpha = 'abcdefghijklmnopqrstuvwxyz'

def rotors = [
        I:      [wires: 'ekmflgdqvzntowyhxuspaibrcj', notch: 'q'],
        II:     [wires: 'ajdksiruxblhwtmcqgznpyfvoe', notch: 'e'],
        III:    [wires: 'bdfhjlcprtxvznyeiwgakmusqo', notch: 'v']
]

def reflectors = [
        B:      [wires: 'yruhqsldpxngokmiebfzcwvjat', rotatable: false],
        C:      [wires: 'fvpjiaoyedrzxwgctkuqsbnmhl', rotatable: false]
]

def rotortypes = rotors.keySet()
def rotorslots = (1..3)
def rotorsignals = (0..3)
def reflectorAdjustable = false

def strInput  = 'wetterbericht'
def strOutput = 'vwcmznncymnkk'
