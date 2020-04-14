# GenerativeMarkovChain

Intial upload:

Known issues: NLayerMarkovNode needs to forward method calls such as good(), bad(), add(), remove(). 
              Spamming bad() or good() a bunch may set some probabilities to 0 and 1.
