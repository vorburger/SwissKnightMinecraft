package ch.vorburger.xtend

import org.eclipse.xtext.xbase.lib.Procedures.Procedure0

class IntegerExtensions2 {
    
    /**
     * @see http://stackoverflow.com/a/18065434/421602
     */
    def static times(int iterations, Procedure0 action) {
        if (iterations < 0)
            throw new IllegalArgumentException('''Can't iterate negative («iterations») times.''')
        for (var i = 0; i < iterations; i++) // for (i : 0 ..< iterations)
            action.apply
    }
    
}