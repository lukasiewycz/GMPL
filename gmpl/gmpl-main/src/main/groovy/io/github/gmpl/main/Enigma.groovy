package io.github.gmpl.main

import static io.github.gmpl.main.GDefaultFunctions.*

// https://people.physik.hu-berlin.de/~palloks/js/enigma/enigma-u_v20_en.html

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

def String getOutput(String wires, String i){
        int input = ((int)i)-((int)'a')
        return ((char)wires.charAt(input)) as String
}

def String add(String a, String b){
        int aint = ((int)a)-((int)'a')
        int bint = ((int)b)-((int)'a')
        return (char)(((aint+bint)%26)+(int)'a')
}

def String add(String a, int b){
        int aint = ((int)a)-((int)'a')
        return (char)(((aint+(b-1))%26)+(int)'a')
}

def String subtract(String a, String b){
        int aint = ((int)a)-((int)'a')
        int bint = ((int)b)-((int)'a')
        return (char)(((aint-bint+26)%26)+(int)'a')
}

def String subtract(String a, int b){
        int aint = ((int)a)-((int)'a')
        return (char)(((aint-(b-1)+26)%26)+(int)'a')
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

def strInput  = 'wetterbericht'
def strOutput = 'vwcmznncymnkk'
def T = strInput.length()

def xr = var name: 'xr', type: boolean, domain: [rotors.keySet(),'a'..'z',0..T] // rotor position as time
def xq = var name: 'xq', type: boolean, domain: [rotors.keySet(),'a'..'z',1..T] // rotor position with ring adjustment
def xp = var name: 'xp', type: boolean, domain: [rotors.keySet(),1..26] // ring position
def xs = var name: 'xs', type: boolean, domain: [rotors.keySet(), rotorslots] // rotor to slot assignment
def xm = var name: 'xm', type: boolean, domain: [rotorslots,0..T] // move rotor yes/no
def is = var name: 'is', type: boolean, domain: [rotorsignals, 'a'..'z', 1..T]
def io = var name: 'io', type: boolean, domain: ['a'..'z', 1..T]
def yr = var name: 'yr', type: boolean, domain: [reflectors.keySet()]

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
for(r in rotors.keySet()) for(t in 0..T) {
        constraint compare(sum(xr[r, _, t]), '==', 1)
}
for(r in rotors.keySet()) for(t in 1..T) {
        constraint compare(sum(xq[r,_,t]),'==',1)
}
for(r in rotors.keySet()) for(sh in 1..26) {
        constraint compare(sum(xp[r,_]),'==',1)
}

// move rotor to next position
for(r in rotors.keySet()) for(t in 0..T-1) for(p in 'a'..'z') for(s in rotorslots) {
        clause ~xm[s,t] | ~xs[r,s] | ~xr[r,p,t] | xr[r,next(p),t+1]
        clause  xm[s,t] | ~xs[r,s] | ~xr[r,p,t] | xr[r,p      ,t+1]
        //println compare(xr[r,s,t] + xm[r,t] - xr[r,next(s),t],'<=',1)
        //constraint compare(xr[r,s,t] + xm[r,t] - xr[r,next(s),t+1],'<=',1)
}

// choose exactly one refelector
constraint compare(sum(yr[_]),'==',1)



for(s in rotorsignals) for(t in 1..T) {
        constraint compare(sum(is[s,_,t]),'==',2)
}

for(l in 'a'..'z') for(t in 1..T) {
        constraint compare(io[l,t]-is[0,l,t],'==',0)
}

// define routing with getOffset(..) and
// https://people.physik.hu-berlin.de/~palloks/js/enigma/enigma-u_v20_en.html
for(r in rotors.keySet()) for(p in 'a'..'z') for(sh in 1..26) for(t in 1..T) {
        clause ~xr[r,p,t] | ~xp[r,sh] | xq[r,subtract(p,sh),t]
        clause xr[r,p,t] | ~xp[r,sh] | ~xq[r,subtract(p,sh),t]
}


for(s in rotorslots) for(r in rotors.keySet()) for(p in 'a'..'z') for(t in 1..T) for(input in 'a'..'z') {
        def inputOnGrid = input
        def inputOnRing = add(input,p)
        def outputOnRing = getOutput(rotors[r].wires, inputOnRing)
        def outputOnGrid = subtract(outputOnRing,p)

        clause ~xs[r,s] | ~xq[r,p,t] | ~is[s-1,inputOnGrid,t] | is[s,outputOnGrid,t]
        clause ~xs[r,s] | ~xq[r,p,t] | is[s-1,inputOnGrid,t] | ~is[s,outputOnGrid,t]
}

for(f in reflectors.keySet()) for(p in 'a'..'z') for(t in 1..T) {
        def q = getOutput(reflectors[f].wires,p)
        clause ~yr[f] | ~is[rotorsignals.last(),p,t] | is[rotorsignals.last(),q,t]
}





// set input
for(int i=0; i<strInput.length();i++){
        def s = strInput.substring(i,i+1)
        clause io[s,i+1]
        def o = strOutput.substring(i,i+1)
        clause io[o,i+1]
}



//clause xr['I','r',0]
//clause xr['II','e',0]
//clause xr['III','a',0]
clause xs['I',1]
clause xs['II',2]
clause xs['III',3]
//clause xp['I',5]
//clause xp['II',5]
//clause xp['III',5]

clause yr['B']

println 'Build all constraints'

def counter = 0

while(solve()){
        counter++;
        println 'solved '+counter
        def list = xs[_,_] + yr[_]
        for(r in rotors.keySet()) if(disjunction(xs[r,_] as GLiteral[]) as boolean) {
                // smart approach that detects symmetries
                int slot = 0
                for(s in rotorslots) if(xs[r,s] as boolean) {
                        slot = s
                }
                for(p in 'a'..'z') if(xq[r,p,1] as boolean) list << xq[r,p,1]
                for(t in 1..T-1) list << xm[slot,t]

                // silly approach:
                //for(p in 'a'..'z') if(xr[r,p,0] as boolean) list << xr[r,p,0]
                //for(sh in 1..26) if(xp[r,sh] as boolean)  list << xp[r,sh]
        }
        println list as GLiteral[]

        clause disjunction(~(list as GLiteral[]))
}


for (s in rotorslots) for(r in rotors.keySet()){
        if(xs[r,s] as boolean) {
                print  '\t\t' + r
                for(sh in 1..26){
                        if(xp[r,sh] as boolean) print "($sh)"
                }

        }
}
for (f in reflectors.keySet()){
        if(yr[f] as boolean) print  '\t\t' + f
}
println ''

for (t in (0..T)){
        print "${t}\t\t"
        for(r in rotors.keySet()) for(s in ('a'..'z')) {
                if (xr[r,s,t] as boolean){
                        print s
                        if(t>0) {
                                for (p in ('a'..'z')) if (xq[r, p, t] as boolean) print "($p)"
                        }
                        print '\t\t'
                }
        }
        print '\t'
        for(s in rotorslots) { print ''+(xm[s,t] as int)+'\t' }

        //for(r in rotors.keySet()) { print ''+(xm[r,t] as int)+'\t' }
        println ''

        /*if(t > 0) {
                for (s in 'a'..'z') {
                        print "${s}\t"
                        for (p in rotorsignals) {
                                print '' + (is[p, s, t] as int) + '\t\t'
                        }
                        println ''

                }
        }*/
}

println 'result string: '
for(t in 1..T) for(l in 'a'..'z') {
        if(io[l,t] as boolean && strInput[t-1] != l) print l
}
println ''
