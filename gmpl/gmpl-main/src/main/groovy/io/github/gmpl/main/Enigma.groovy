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

def String next(String a){
        int base = ((int) ('a'))
        return ''+(char)(((((int)a.charAt(0))-base+1)%26)+base)
}

def String prev(String a){
        int base = ((int) ('a'))
        return ''+(char)(((((int)a.charAt(0))-base+(26-1))%26)+base)
}

def int getOffset(String wires, String i){
        int input = ((int)i)-((int)'a')
        int output = ((int)wires.charAt(input))-((int)'a')
        return (output - input + 26) % 26
}

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

def str = 'wetterbericht'
def T = str.length()

def xr = var name: 'xr', type: boolean, domain: [rotors.keySet(),'a'..'z',0..T] // rotor position as time
def xp = var name: 'xp', type: boolean, domain: [rotors.keySet(),'a'..'z'] // ring position
def xs = var name: 'xs', type: boolean, domain: [rotors.keySet(), rotorslots] // rotor to slot assignment
def xm = var name: 'xm', type: boolean, domain: [rotorslots,0..T] // move rotor yes/no
def is = var name: 'is', type: boolean, domain: [rotorsignals, 'a'..'z', 1..T]
def io = var name: 'io', type: boolean, domain: ['a'..'z', 1..T]

solver SAT4J

// rotor in slot assigment
for (s in rotorslots){
        constraint compare(sum(xs[_,s]),'==',1)
}

for (r in rotors.keySet()){
        constraint compare(sum(xs[r,_]),'<=', 1)
}

// rotor movement 1
for(t in 0..T) {
        clause xm[1,t]
        //clause xm[3,t]
}
// rotor movement 2 (consider anomaly)
for(t in 0..T) for (r in rotors.keySet()) for (p in 'a'..'z') {
        //clause ~xs[r,1] | ~xr[r,p,t] | (rotors[r].notch.contains(p)? xm[2,t]  : ~xm[2,t] ) without anomaly
        if(rotors[r].notch.contains(p)){
                clause ~xs[r,1] | ~xr[r,p,t] | xm[2,t]
        } else if(rotors[r].notch.contains(prev(p))){ // this is due to the anomaly of double stepping
                clause ~xs[r,1] | ~xr[r,p,t] | ~xm[3,t] | xm[2,t]
        } else {
                clause ~xs[r,1] | ~xr[r,p,t] | ~xm[2,t]
        }
}
// rotor movement 3
for(t in 0..T) for (r0 in rotors.keySet()) for (r1 in rotors.keySet()) for (p0 in 'a'..'z')  for (p1 in 'a'..'z') {
        if(rotors[r0].notch.contains(prev(p0)) && rotors[r1].notch.contains(p1)){
                clause ~xs[r0,1] | ~xr[r0,p0,t] | ~xs[r1,2] | ~xr[r1,p1,t] | xm[3,t]
        } else {
                clause ~xs[r0,1] | ~xr[r0,p0,t] | ~xs[r1,2] | ~xr[r1,p1,t] | ~xm[3,t]
        }
}

// rotor in exactly one position
for(r in rotors.keySet()) for(t in 0..T){
        constraint compare(sum(xr[r,_,t]),'==',1)
}

// move rotor to next position
for(r in rotors.keySet()) for(t in 0..T-1) for(p in 'a'..'z') for(s in rotorslots) {
        clause ~xm[s,t] | ~xs[r,s] | ~xr[r,p,t] | xr[r,next(p),t+1]
        clause  xm[s,t] | ~xs[r,s] | ~xr[r,p,t] | xr[r,p      ,t+1]
        //println compare(xr[r,s,t] + xm[r,t] - xr[r,next(s),t],'<=',1)
        //constraint compare(xr[r,s,t] + xm[r,t] - xr[r,next(s),t+1],'<=',1)
}

for(s in rotorsignals) for(t in 1..T) {
        constraint compare(sum(is[s,_,t]),'==',2)
}

for(l in 'a'..'z') for(t in 1..T) {
        constraint compare(io[l,t]-is[0,l,t],'==',0)
}

// set input
for(int i=0; i<str.length();i++){
        def s = str.substring(i,i+1)
        clause io[s,i+1]
}


rotorInit = [
        I:      'r',
        II:     'e',
        III:    'a'
]

for(r in rotors.keySet()) {
        clause xr[r,rotorInit[r],0]
}
clause xs['I',1]
clause xs['II',2]
clause xs['III',3]

println solve()

for (s in rotorslots) for(r in rotors.keySet()){
        if(xs[r,s] as boolean) print  '\t' + r
}
println ''

for (t in (0..T)){
        print "${t}\t\t"
        for(r in rotors.keySet()) for(s in ('a'..'z')) { if (xr[r,s,t] as boolean) print s + '\t\t' }
        print '\t'
        for(s in rotorslots) { print ''+(xm[s,t] as int)+'\t' }

        //for(r in rotors.keySet()) { print ''+(xm[r,t] as int)+'\t' }
        println ''

        if(t > 0) {
                for (s in 'a'..'z') {
                        print "${s}\t"
                        for (p in rotorsignals) {
                                print '' + (is[p, s, t] as int) + '\t\t'
                        }
                        println ''

                }
        }
}

println getOffset(rotors['I'].wires, 's')
println getOffset(rotors['I'].wires, 't')
println getOffset(rotors['I'].wires, 'u')
println getOffset(rotors['II'].wires, 'f')
