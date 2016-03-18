package ch.vorburger.minecraft.logo

import org.eclipse.xtend.lib.annotations.Delegate

abstract class DelegatingTurtle implements Turtle {

    @Delegate protected final Turtle delegate
    
    protected new(Turtle delegate) { this.delegate = delegate }

}