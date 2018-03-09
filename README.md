Spelling corrector in Java that supports the following functions.

* One letter edits:
    * a) deletion (remove one letter),
    * b) transposition (swap two adjacent letters)
    * c) replacement (change one letter to another)
    * d) insertion (add a letter).
* Two edits: a combination of #1’s a-d

Test cases:

1. 'speling' ->   ‘spelling’
2. 'korrectud' -> 'corrected'          
3. 'bycycle' -> 'bicycle'              
4. 'inconvient' -> 'inconvenient'      
5. 'arrainged' -> 'arranged'            
6. 'peotry'->'poetry'                  
7. 'peotryy' ->'poetry'                
8. 'word'-> 'word'      no correction
9. 'quintessential' -> 'quintessential' ß if the word is not found in the dictionary, the program would return the original

The build will generate 2 jars. To run please one of the below. To avoid potential hassle of putting all relevant jars
 on the class path please use jar with dependencies.

__Samples:__

java -jar target/spellchecker-1.0.0-SNAPSHOT.jar speling

java -jar target/spellchecker-1.0.0-SNAPSHOT-jar-with-dependencies.jar spelling

java -jar target/spellchecker-1.0.0-SNAPSHOT.jar "I am speling word bycycle  korrectly and may fixx it"