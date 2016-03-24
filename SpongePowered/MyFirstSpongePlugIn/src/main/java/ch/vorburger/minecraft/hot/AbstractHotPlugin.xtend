package ch.vorburger.minecraft.hot

import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent
import org.spongepowered.api.event.game.state.GameStartingServerEvent
import org.spongepowered.api.event.game.state.GameStoppingServerEvent
import org.spongepowered.api.event.game.state.GameStoppedServerEvent

class AbstractHotPlugin {
    // TODO Is this really needed? It doesn't look like, because Sponge does not actually seem to call AbstractDrawingPlugin onServerStopping, so no /undo on server shutdown, so.. no problem - this is pointless.  

    private var boolean isHotReloading = true // TODO Implement this, instead of hard-coding.. probably just ugly, by having HoteaPlugin set this field via reflection if it's available on each plugin instance (TODO make available in HotPlugins)

    def final boolean isHotReloading() {
        isHotReloading;
    }

    @Listener
    def void onAboutToStartServer(GameAboutToStartServerEvent event) {
        
    }

    @Listener
    def void onServerStarting(GameStartingServerEvent event) {
    }
    
    @Listener
    def void onServerStopping(GameStoppingServerEvent event) {
    }
    
    @Listener
    def void onStoppedServer(GameStoppedServerEvent event) {
        
    }
    
}
