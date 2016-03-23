package ch.vorburger.minecraft.hot

import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent
import org.spongepowered.api.event.game.state.GameStartingServerEvent
import org.spongepowered.api.event.game.state.GameStoppingServerEvent
import org.spongepowered.api.event.game.state.GameStoppedServerEvent

class AbstractHotPlugin {

    private var boolean isHotReloading = true // TODO Implement real discovery, instead of hard-coding

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
