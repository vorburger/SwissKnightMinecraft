package ch.vorburger.xtend

import org.eclipse.xtext.xbase.lib.Procedures.Procedure0

class IntegerExtensions2 {
    
    /**
     * Do 'action' for 'n' times.
     */
     // http://stackoverflow.com/a/18065434/421602
    def static times(int n, Procedure0 action) {
        if (n < 0)
            throw new IllegalArgumentException('''Can't iterate negative («n») times.''')
        for (var i = 0; i < n; i++) // for (i : 0 ..< iterations)
            action.apply
    }
    
    /**
     * Do 'action' for 'n' times, and do 'inBetween' every time after 'action' except for the last time (so n-1 times).
     */
    def static times(int n, Procedure0 action, Procedure0 inBetween) {
        if (n < 0)
            throw new IllegalArgumentException('''Can't iterate negative («n») times.''')
        for (var i = 0; i < n; i++) { // for (i : 0 ..< iterations)
            action.apply
            if (i < n-1)
                inBetween.apply
        }
    }

    
}